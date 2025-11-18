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
import java.math.BigDecimal;

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
        if (proyectoDTO.getFechafin().isBefore(proyectoDTO.getFechainicio())) {
            throw new IllegalArgumentException();
        }

        Proyecto proyectoEntidad = modelMapper.map(proyectoDTO, Proyecto.class);

        if (proyectoEntidad.getCupoMaximo() == null || proyectoEntidad.getCupoMaximo() < 0) {
            throw new IllegalArgumentException("cupoMaximo inválido");
        }

        // Inicializar cupoRestante igual al cupoMaximo al crear
        proyectoEntidad.setCupoRestante(proyectoEntidad.getCupoMaximo());

        Proyecto guardado = proyectoRepositorio.save(proyectoEntidad);
        return modelMapper.map(guardado, ProyectoSoloConDatosDTO.class);
    }

    @Override
    public ProyectoDTO actualizarProyecto(ProyectoDTO proyectoDTO) {
        return proyectoRepositorio.findById(proyectoDTO.getIdproyecto())
                .map(existing -> {
                    // Campos editables
                    existing.setNombreproyecto(proyectoDTO.getNombreproyecto());
                    existing.setDescripcion(proyectoDTO.getDescripcion());
                    existing.setMontoobjetivo(proyectoDTO.getMontoobjetivo());
                    existing.setMontorecaudado(proyectoDTO.getMontorecaudado());
                    existing.setFechainicio(proyectoDTO.getFechainicio());
                    existing.setFechafin(proyectoDTO.getFechafin());
                    existing.setNombreorganizacion(proyectoDTO.getNombreorganizacion());
                    existing.setEscuelabeneficiada(proyectoDTO.getEscuelabeneficiada());
                    existing.setImagen(proyectoDTO.getImagen());

                    // Manejo seguro de cupoMaximo / cupoRestante:
                    int newMax = proyectoDTO.getCupoMaximo();
                    Integer oldMaxObj = existing.getCupoMaximo();
                    int oldMax = (oldMaxObj == null) ? newMax : oldMaxObj;

                    // Si cambió el cupo máximo, ajustar cupoRestante según los ya utilizados
                    if (newMax != oldMax) {
                        int currentRest = (existing.getCupoRestante() != null) ? existing.getCupoRestante() : oldMax;
                        int used = Math.max(0, oldMax - currentRest); // cuántos ya se registraron
                        int newRest = Math.max(0, newMax - used);
                        existing.setCupoMaximo(newMax);
                        existing.setCupoRestante(newRest);
                    } else {
                        // si no cambia el max, mantener cupoRestante tal cual (no resetear)
                        if (existing.getCupoRestante() == null) {
                            existing.setCupoRestante(newMax);
                        }
                    }

                    Proyecto guardado = proyectoRepositorio.save(existing);

                    // Construir DTO manualmente (evitar ModelMapper en colecciones)
                    ProyectoDTO dto = new ProyectoDTO();
                    dto.setIdproyecto(guardado.getIdproyecto());
                    dto.setNombreproyecto(guardado.getNombreproyecto());
                    dto.setDescripcion(guardado.getDescripcion());
                    dto.setMontoobjetivo(guardado.getMontoobjetivo());
                    dto.setMontorecaudado(guardado.getMontorecaudado());
                    dto.setFechainicio(guardado.getFechainicio());
                    dto.setFechafin(guardado.getFechafin());
                    dto.setNombreorganizacion(guardado.getNombreorganizacion());
                    dto.setEscuelabeneficiada(guardado.getEscuelabeneficiada());
                    dto.setCupoMaximo(guardado.getCupoMaximo() != null ? guardado.getCupoMaximo() : 0);
                    dto.setImagen(guardado.getImagen());

                    dto.setDonaciones(new java.util.ArrayList<>());
                    dto.setNotificaciones(new java.util.ArrayList<>());

                    return dto;
                })
                .orElseThrow(() -> new RuntimeException("Proyecto con ID " + proyectoDTO.getIdproyecto() + " no encontrado"));
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
            dto.setCupoRestante(proyecto.getCupoRestante());
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
    public List<ProyectoSoloConDatosDTO> buscarPorMontoObjetivo(BigDecimal min, BigDecimal max) {
        List<Proyecto> proyectos;

        if (min != null && max != null) {
            if (min.compareTo(max) > 0) {
                BigDecimal temp = min;
                min = max;
                max = temp;
            }
            proyectos = proyectoRepositorio.findByMontoobjetivoBetween(min, max);
        } else if (min != null) {
            proyectos = proyectoRepositorio.findByMontoobjetivoGreaterThanEqual(min);
        } else if (max != null) {
            proyectos = proyectoRepositorio.findByMontoobjetivoLessThanEqual(max);
        } else {
            proyectos = proyectoRepositorio.findAll();
        }


        return proyectos.stream()
                .map(p -> modelMapper.map(p, ProyectoSoloConDatosDTO.class))
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

