package com.webcrafters.helpify.servicios;

import com.webcrafters.helpify.DTO.DonacionDTO;
import com.webcrafters.helpify.DTO.DonacionSinUsuarioyProyectoDTO;
import com.webcrafters.helpify.entidades.Donacion;
import com.webcrafters.helpify.entidades.Proyecto;
import com.webcrafters.helpify.interfaces.IDonacionService;
import com.webcrafters.helpify.repositorios.DonacionRepositorio;
import com.webcrafters.helpify.repositorios.ProyectoRepositorio;
import com.webcrafters.helpify.seguridad.entidades.Usuario;
import com.webcrafters.helpify.seguridad.repositorios.UsuarioRepositorio;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DonacionService implements IDonacionService {

    @Autowired private DonacionRepositorio donacionRepositorio;
    @Autowired private UsuarioRepositorio usuarioRepositorio;
    @Autowired private ProyectoRepositorio proyectoRepositorio;
    @Autowired private ModelMapper modelMapper;

    // ===== Helpers =====
    private Authentication getAuthOrThrow() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            throw new BadCredentialsException("Usuario no autenticado");
        }
        return auth;
    }

    private Usuario getUsuarioActualOrThrow() {
        Authentication auth = getAuthOrThrow();
        String username = auth.getName();
        return usuarioRepositorio.findByCorreo(username)
                .or(() -> usuarioRepositorio.findByNombre(username))
                .orElseThrow(() -> new BadCredentialsException("Usuario autenticado no encontrado: " + username));
    }

    private boolean isAdmin(Authentication auth) {
        return auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(r -> "ROLE_ADMIN".equals(r) || "ADMIN".equals(r));
    }

    // ===== Métodos originales =====

    @Override
    @Transactional
    public DonacionDTO insertarDonacion(DonacionDTO donacionDTO) {
        Donacion donacionEntidad = modelMapper.map(donacionDTO, Donacion.class);

        // Usuario por DTO (como tenías) — tu controller ya manda el id
        if (donacionDTO.getUsuario() != null && donacionDTO.getUsuario().getIdusuario() != null) {
            Usuario usuario = usuarioRepositorio.findById(donacionDTO.getUsuario().getIdusuario())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            donacionEntidad.setUsuario(usuario);
        } else {
            throw new RuntimeException("Usuario de la donación no especificado");
        }

        // Proyecto por DTO
        if (donacionDTO.getProyecto() != null && donacionDTO.getProyecto().getIdproyecto() != null) {
            Proyecto proyecto = proyectoRepositorio.findById(donacionDTO.getProyecto().getIdproyecto())
                    .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));
            donacionEntidad.setProyecto(proyecto);
        } else {
            throw new RuntimeException("Proyecto de la donación no especificado");
        }

        if (donacionEntidad.getFechadonacion() == null) {
            donacionEntidad.setFechadonacion(LocalDate.now());
        }
        if (donacionEntidad.getEstado() == null) {
            donacionEntidad.setEstado("PENDIENTE");
        }

        Donacion guardado = donacionRepositorio.save(donacionEntidad);
        return modelMapper.map(guardado, DonacionDTO.class);
    }

    @Override
    @Transactional
    public DonacionDTO actualizarDonacion(DonacionDTO donacionDTO) {
        return donacionRepositorio.findById(donacionDTO.getId())
                .map(existing -> {
                    // Campos que permites actualizar
                    existing.setEstado(donacionDTO.getEstado());
                    Donacion actualizado = donacionRepositorio.save(existing);
                    return modelMapper.map(actualizado, DonacionDTO.class);
                })
                .orElseThrow(() -> new RuntimeException("Donación con ID " + donacionDTO.getId() + " no encontrada"));
    }

    @Override
    @Transactional
    public void eliminarDonacion(Long id) {
        if (!donacionRepositorio.existsById(id)) {
            throw new RuntimeException("Donación no encontrada con ID: " + id);
        }
        donacionRepositorio.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public DonacionSinUsuarioyProyectoDTO buscarPorId(Long idDonacion) {
        return donacionRepositorio.findById(idDonacion)
                .map(d -> modelMapper.map(d, DonacionSinUsuarioyProyectoDTO.class))
                .orElseThrow(() -> new RuntimeException("Donación con ID " + idDonacion + " no encontrada"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DonacionSinUsuarioyProyectoDTO> listarTodos() {
        return donacionRepositorio.findAll().stream()
                .map(d -> modelMapper.map(d, DonacionSinUsuarioyProyectoDTO.class))
                .collect(Collectors.toList());
    }

    // ===== Nuevos métodos con ownership =====

    @Override
    @Transactional
    public DonacionDTO insertarDonacionComoUsuarioActual(DonacionDTO dto) {
        Usuario actual = getUsuarioActualOrThrow();

        if (dto.getProyecto() == null || dto.getProyecto().getIdproyecto() == null) {
            throw new IllegalArgumentException("Proyecto de la donación no especificado");
        }
        Proyecto proyecto = proyectoRepositorio.findById(dto.getProyecto().getIdproyecto())
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));

        Donacion entidad = new Donacion();
        entidad.setUsuario(actual);
        entidad.setProyecto(proyecto);
        entidad.setFechadonacion(dto.getFechadonacion() != null ? dto.getFechadonacion() : LocalDate.now());
        entidad.setEstado(dto.getEstado() != null ? dto.getEstado() : "PENDIENTE");
        // si manejas monto en donación

        Donacion guardado = donacionRepositorio.save(entidad);
        return modelMapper.map(guardado, DonacionDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DonacionSinUsuarioyProyectoDTO> listarDonacionesDelUsuarioActual() {
        Usuario actual = getUsuarioActualOrThrow();
        return donacionRepositorio.findByUsuario_Idusuario(actual.getIdusuario()).stream()
                .map(d -> modelMapper.map(d, DonacionSinUsuarioyProyectoDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public DonacionSinUsuarioyProyectoDTO buscarPorIdRestringiendoAUsuarioActualSiCorresponde(Long idDonacion) {
        Authentication auth = getAuthOrThrow();
        boolean admin = isAdmin(auth);
        if (admin) {
            // Admin puede ver cualquiera
            return buscarPorId(idDonacion);
        }
        Usuario actual = getUsuarioActualOrThrow();
        Donacion d = donacionRepositorio.findByIdAndUsuario_Idusuario(idDonacion, actual.getIdusuario())
                .orElseThrow(() -> new AccessDeniedException("No tienes acceso a esta donación"));
        return modelMapper.map(d, DonacionSinUsuarioyProyectoDTO.class);
    }

    @Override
    @Transactional
    public DonacionDTO actualizarDonacionComoUsuarioActual(DonacionDTO dto) {
        Usuario actual = getUsuarioActualOrThrow();
        Donacion existing = donacionRepositorio.findByIdAndUsuario_Idusuario(dto.getId(), actual.getIdusuario())
                .orElseThrow(() -> new AccessDeniedException("No puedes actualizar una donación que no es tuya"));

        if (dto.getEstado() != null) existing.setEstado(dto.getEstado());

        // No permitir cambiar usuario/proyecto aquí

        Donacion guardado = donacionRepositorio.save(existing);
        return modelMapper.map(guardado, DonacionDTO.class);
    }

    @Override
    @Transactional
    public void eliminarDonacionComoUsuarioActual(Long idDonacion) {
        Usuario actual = getUsuarioActualOrThrow();
        Donacion d = donacionRepositorio.findByIdAndUsuario_Idusuario(idDonacion, actual.getIdusuario())
                .orElseThrow(() -> new AccessDeniedException("No puedes eliminar una donación que no es tuya"));
        donacionRepositorio.delete(d);
    }
}
