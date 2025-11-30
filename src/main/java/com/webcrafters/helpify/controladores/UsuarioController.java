package com.webcrafters.helpify.controladores;

import com.webcrafters.helpify.DTO.RegistroDonanteRespuestaDTO;
import com.webcrafters.helpify.seguridad.DTO.UsuarioConComentariosDTO;
import com.webcrafters.helpify.seguridad.DTO.UsuarioConDonacionesDTO;
import com.webcrafters.helpify.seguridad.DTO.UsuarioDTO;
import com.webcrafters.helpify.interfaces.IUsuarioService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin(origins = "${ip.frontend}",
        allowCredentials = "true",
        exposedHeaders = "Authorization")
@RequestMapping("/api")
public class UsuarioController {
    @Autowired
    private IUsuarioService usuarioService;

    @PostMapping("/usuario")
    public ResponseEntity<RegistroDonanteRespuestaDTO> insertarUsuario(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        log.info("Registrando usuario {}", usuarioDTO.getNombre());
        RegistroDonanteRespuestaDTO resp = usuarioService.insertar(usuarioDTO);
        if (resp.isSuccess()) {
            return ResponseEntity.status(201).body(resp);
        } else {
            return ResponseEntity.badRequest().body(resp);
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'VOLUNTARIO', 'DONANTE')")
    @GetMapping("/usuarios")
    public List<UsuarioDTO> listarUsuarios(){
        log.info("Lista de usuarios");
        return usuarioService.listarTodos();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'VOLUNTARIO', 'DONANTE')")
    @GetMapping("/usuarios/comentarios")
    public List<UsuarioConComentariosDTO> listarUsuariosConComentarios(){
        log.info("Lista de usuarios con sus comentarios");
        return usuarioService.listarUsuariosConComentarios();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DONANTE')")
    @GetMapping("/usuarios/donaciones")
    public List<UsuarioConDonacionesDTO> listarUsuariosConDonaciones(){
        log.info("Lista de usuarios con sus donaciones");
        return usuarioService.listarUsuariosConDonaciones();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<UsuarioDTO> buscarUsuarioPorId(@PathVariable Long idUsuario){
        return ResponseEntity.ok(usuarioService.buscarPorId(idUsuario));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'VOLUNTARIO', 'DONANTE')")
    @PutMapping("/usuario")
    public ResponseEntity<UsuarioDTO> actualizarUsuario(@RequestBody UsuarioDTO usuarioDTO){
        return ResponseEntity.ok(usuarioService.actualizar(usuarioDTO));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'VOLUNTARIO', 'DONANTE')")
    @DeleteMapping("/usuario/{id}")
    public void eliminarUsuario(@PathVariable Long id)
    {
        usuarioService.eliminar(id);
    }

    // UsuarioController.java
    @PreAuthorize("hasAnyRole('ADMIN','VOLUNTARIO','DONANTE')")
    @GetMapping("/usuario/me")
    public ResponseEntity<UsuarioDTO> miPerfil(java.security.Principal principal) {
        // username que vino autenticado (jwt)
        final String username = principal.getName();

        // usa tu método ya existente
        Long id = usuarioService.obtenerIdPorUsername(username);
        UsuarioDTO dto = usuarioService.buscarPorId(id);

        return ResponseEntity.ok(dto);
    }

}



