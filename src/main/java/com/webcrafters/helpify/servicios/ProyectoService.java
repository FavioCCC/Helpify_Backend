package com.webcrafters.helpify.servicios;

import com.webcrafters.helpify.DTO.*;
import com.webcrafters.helpify.entidades.Proyecto;
import com.webcrafters.helpify.entidades.Universitario;
import com.webcrafters.helpify.excepciones.GlobalExceptionHandle;
import com.webcrafters.helpify.interfaces.IProyectoService;
import com.webcrafters.helpify.repositorios.DonacionRepositorio;
import com.webcrafters.helpify.repositorios.InscripcionRepositorio;
import com.webcrafters.helpify.repositorios.ProyectoRepositorio;
import com.webcrafters.helpify.seguridad.DTO.UsuarioSoloConDatosDTO;
import com.webcrafters.helpify.seguridad.entidades.Usuario;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class ProyectoService implements IProyectoService {

    @Autowired
    private ProyectoRepositorio proyectoRepositorio;

    @Autowired
    private DonacionRepositorio donacionRepositorio;

    @Autowired
    private InscripcionRepositorio inscripcionRepositorio;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ProyectoSoloConDatosDTO insertarProyecto(ProyectoDTO proyectoDTO) {
        // Verificar autenticación: si no hay auth válida -> 401 (BadCredentialsException)
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            throw new BadCredentialsException("Usuario no autenticado");
        }

        // Verificar rol: si está autenticado pero no tiene ROLE_ADMIN -> 403 (AccessDeniedException)
        boolean isAdmin = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> "ROLE_ADMIN".equals(role) || "ADMIN".equals(role));
        if (!isAdmin) {
            throw new AccessDeniedException("Usuario no tiene permisos suficientes");
        }

        // Validaciones básicas de campos obligatorios -> 400 (IllegalArgumentException)
        if (proyectoDTO == null
                || proyectoDTO.getNombreproyecto() == null || proyectoDTO.getNombreproyecto().trim().isEmpty()
                || proyectoDTO.getFechainicio() == null
                || proyectoDTO.getFechafin() == null) {
            throw new IllegalArgumentException();
        }

        // Validación opcional de consistencia de fechas
        if (proyectoDTO.getFechafin().isBefore(proyectoDTO.getFechainicio())) {
            throw new IllegalArgumentException();
        }

        // Si existe campo montoobjetivo y es <= 0, considerar inválido
        try {
            double monto = proyectoDTO.getMontoobjetivo();
            if (monto <= 0) {
                throw new IllegalArgumentException();
            }
        } catch (NullPointerException ignored) {
            // Si el getter devuelve objeto nulo, ya fue cubierto por validación anterior opcional
        }

        Proyecto proyectoEntidad = modelMapper.map(proyectoDTO, Proyecto.class);
        Proyecto guardado = proyectoRepositorio.save(proyectoEntidad);
        return modelMapper.map(guardado, ProyectoSoloConDatosDTO.class);
    }

    @Override
    public ProyectoDTO actualizarProyecto(ProyectoDTO proyectoDTO) {
        return proyectoRepositorio.findById(proyectoDTO.getIdproyecto())
                .map(existing -> {
                    Proyecto proyectoEntidad = modelMapper.map(proyectoDTO, Proyecto.class);
                    Proyecto guardado = proyectoRepositorio.save(proyectoEntidad);
                    return modelMapper.map(guardado, ProyectoDTO.class);
                })
                .orElseThrow(() -> new RuntimeException("Proyecto con ID " + proyectoDTO.getIdproyecto() +
                        " no encontrado"));
    }

    @Override
    public void eliminarProyecto(Long id) {
        if (!proyectoRepositorio.existsById(id)) {
            throw new RuntimeException("Proyecto no encontrado con ID: " + id);
        }

        // usar el parámetro 'id' y el método definido en DonacionRepositorio
        if (donacionRepositorio.existsByProyecto_Idproyecto(id)) {
            throw new GlobalExceptionHandle.ConflictException("No se puede eliminar un proyecto con donaciones asociadas.");
        }

        proyectoRepositorio.deleteById(id);
    }

    @Override
    public List<ProyectoSoloConDatosDTO> listarTodosLosProyectos() {
        return proyectoRepositorio.findAll().stream().map(proyecto -> modelMapper.map(proyecto, ProyectoSoloConDatosDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProyectoConDonacionesDTO> listarProyectosConDonaciones() {
        return proyectoRepositorio.listarProyectosConDonaciones().stream()
                .map(proyecto -> {
                    ProyectoConDonacionesDTO dto = modelMapper.map(proyecto, ProyectoConDonacionesDTO.class);

                    dto.setDonaciones(
                            proyecto.getDonaciones().stream()
                                    .map(d -> {
                                        DonacionSoloDatosDTO donacionDTO = modelMapper.map(d, DonacionSoloDatosDTO.class);

                                        if (d.getPago() != null) {
                                            PagoSoloConDatosDTO pagoDTO = modelMapper.map(d.getPago(), PagoSoloConDatosDTO.class);
                                            donacionDTO.setPago(pagoDTO);
                                        }

                                        return donacionDTO;
                                    })
                                    .collect(Collectors.toList())
                    );

                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<ProyectoConInscripcionesDTO> listarProyectosConInscripciones() {
        return proyectoRepositorio.findAll().stream().map(proyecto -> {
            ProyectoConInscripcionesDTO dto = new ProyectoConInscripcionesDTO();

            // --- Datos del proyecto ---
            dto.setIdproyecto(proyecto.getIdproyecto());
            dto.setNombreproyecto(proyecto.getNombreproyecto());
            dto.setDescripcion(proyecto.getDescripcion());
            dto.setMontoobjetivo(proyecto.getMontoobjetivo());
            dto.setMontorecaudado(proyecto.getMontorecaudado());
            dto.setFechainicio(proyecto.getFechainicio());
            dto.setFechafin(proyecto.getFechafin());
            dto.setNombreorganizacion(proyecto.getNombreorganizacion());
            dto.setEscuelabeneficiada(proyecto.getEscuelabeneficiada());
            dto.setCupoMaximo(proyecto.getCupoMaximo());
            dto.setImagen(proyecto.getImagen());

            // --- Inscripciones asociadas ---
            List<InscripcionSoloConUniversitarioDTO> inscripciones = inscripcionRepositorio.findAll().stream()
                    .filter(i -> i.getProyecto().getIdproyecto().equals(proyecto.getIdproyecto()))
                    .map(i -> {
                        Universitario u = i.getUniversitario();
                        Usuario usuario = u.getUsuario();

                        // Crear DTO de usuario
                        UsuarioSoloConDatosDTO usuarioDTO = new UsuarioSoloConDatosDTO();
                        usuarioDTO.setIdusuario(usuario.getIdusuario());
                        usuarioDTO.setNumerodocumento(usuario.getNumerodocumento());
                        usuarioDTO.setNombredocumento(usuario.getNombredocumento());
                        usuarioDTO.setNombre(usuario.getNombre());
                        usuarioDTO.setApellidopaterno(usuario.getApellidopaterno());
                        usuarioDTO.setApellidomaterno(usuario.getApellidomaterno());
                        usuarioDTO.setCelular(usuario.getCelular());
                        usuarioDTO.setCorreo(usuario.getCorreo());
                        usuarioDTO.setPassword(usuario.getPassword());
                        usuarioDTO.setFecharegistro(usuario.getFecharegistro());

                        // Crear DTO de universitario con usuario anidado
                        UniversitarioConUsuarioDTO uniDTO = new UniversitarioConUsuarioDTO();
                        uniDTO.setIduniversitario(u.getIduniversitario());
                        uniDTO.setCodigoestudiante(u.getCodigoestudiante());
                        uniDTO.setUsuario(usuarioDTO);

                        // Crear DTO de inscripción con ID, fecha y universitario
                        InscripcionSoloConUniversitarioDTO inscDTO = new InscripcionSoloConUniversitarioDTO();
                        inscDTO.setId(i.getId());
                        inscDTO.setFecharegistro(i.getFecharegistro());
                        inscDTO.setUniversitario(uniDTO);

                        return inscDTO;
                    })
                    .collect(Collectors.toList());

            dto.setInscripciones(inscripciones);

            return dto;
        }).collect(Collectors.toList());
    }

    @Async
    @Override
    public CompletableFuture<List<ProyectoSoloConDatosDTO>> listarTodosLosProyectosAsync() {
        List<ProyectoSoloConDatosDTO> proyectos = proyectoRepositorio.findAll()
                .stream()
                .map(proyecto -> modelMapper.map(proyecto, ProyectoSoloConDatosDTO.class))
                .collect(Collectors.toList());

        return CompletableFuture.completedFuture(proyectos);
    }

    @Override
    public List<ProyectoSoloConDatosDTO> buscarPorNombreProyecto(String nombreproyecto) {
        return proyectoRepositorio.findAllByNombreproyectoContainingIgnoreCase(nombreproyecto)
                .stream()
                .map(proyecto -> modelMapper.map(proyecto, ProyectoSoloConDatosDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProyectoSoloConDatosDTO> buscarPorMontoObjetivo(double montoobjetivo) {
        return proyectoRepositorio.findAllByMontoobjetivo(montoobjetivo)
                .stream()
                .map(proyecto -> modelMapper.map(proyecto, ProyectoSoloConDatosDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProyectoSoloConDatosDTO> buscarPorFechaInicioEntreFechaFin(LocalDate fechainicio, LocalDate fechafin) {
        return proyectoRepositorio.findByFechainicioBetween(fechainicio, fechafin)
                .stream()
                .map(proyecto -> modelMapper.map(proyecto, ProyectoSoloConDatosDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProyectoSoloConDatosDTO> buscarPorAnioYMes(int anio, int mes) {
        return proyectoRepositorio.findAll().stream()
                .filter(proyecto -> proyecto.getFechainicio().getYear() == anio
                        && proyecto.getFechainicio().getMonthValue() == mes) // <- antes usabas getFechafin()
                .map(proyecto -> modelMapper.map(proyecto, ProyectoSoloConDatosDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<UniversitariosPorProyectoDTO> obtenerUniversitariosPorProyecto() {
        return proyectoRepositorio.obtenerUniversitariosPorProyecto();
    }

    @Override
    public long obtenerTotalUniversitarios() {
        return proyectoRepositorio.obtenerTotalUniversitarios();
    }

    @Override
    public List<PorcentajeUniversitariosDTO> obtenerPorcentajeUniversitariosPorProyecto(long total) {
        return proyectoRepositorio.obtenerPorcentajeUniversitariosPorProyecto(total);
    }

    @Override
    public ProyectoSoloConDatosDTO obtenerProyectoPorId(Long id) {
        Proyecto entidad = proyectoRepositorio.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Proyecto no encontrado"));

        // usa ModelMapper como ya lo tienes
        return modelMapper.map(entidad, ProyectoSoloConDatosDTO.class);
    }

}

