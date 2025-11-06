package com.webcrafters.helpify.controladores;

import com.webcrafters.helpify.DTO.UniversitarioConUsuarioDTO;
import com.webcrafters.helpify.DTO.UniversitarioDTO;
import com.webcrafters.helpify.interfaces.IUniversitarioService;
import com.webcrafters.helpify.interfaces.IUsuarioService;
import com.webcrafters.helpify.seguridad.entidades.Usuario;
import com.webcrafters.helpify.seguridad.repositorios.UsuarioRepositorio;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:4200",
        allowCredentials = "true",
        exposedHeaders = "Authorization")
@RequestMapping("/api")
public class UniversitarioController {
    @Autowired
    private IUniversitarioService universitarioService;

    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @PreAuthorize("hasAnyRole('ADMIN', 'VOLUNTARIO')")
    @PostMapping("/universitario")
    public ResponseEntity<UniversitarioDTO> insertarUniversitario(@Valid @RequestBody UniversitarioDTO universitarioDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Usuario usuario = usuarioRepositorio.findByNombre(username)
                .orElseThrow(() -> new RuntimeException("Usuario autenticado no encontrado: " + username));

        universitarioDTO.setIdusuario(usuario.getIdusuario()); // asociar al usuario autenticado
        return ResponseEntity.ok(universitarioService.insertarUniversitario(universitarioDTO));
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'VOLUNTARIO')")
    @GetMapping("/universitarios-con-usuario")
    public List<UniversitarioConUsuarioDTO> listarUniversitariosConUsuario() {
        return universitarioService.listarUniversitariosConUsuario();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/universitario/{iduniversitario}")
    public ResponseEntity<UniversitarioDTO> buscarUniversitarioPorId(@PathVariable Long iduniversitario){
        return ResponseEntity.ok(universitarioService.buscarPorIdUniversitario(iduniversitario));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'VOLUNTARIO')")
    @PutMapping("/universitario")
    public ResponseEntity<UniversitarioDTO> actualizarUniversitario(@RequestBody UniversitarioDTO universitarioDTO){
        return ResponseEntity.ok(universitarioService.actualizarUniversitario(universitarioDTO));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'VOLUNTARIO')")
    @DeleteMapping("/universitario/{id}")
    public void eliminarUniversitario(@PathVariable Long id)
    {
        universitarioService.eliminarUniversitario(id);
    }

}
