package com.webcrafters.helpify.controladores;

import com.webcrafters.helpify.DTO.UniversitarioConUsuarioDTO;
import com.webcrafters.helpify.DTO.UniversitarioDTO;
import com.webcrafters.helpify.interfaces.IUniversitarioService;
import com.webcrafters.helpify.interfaces.IUsuarioService;
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
@RequestMapping("/api")
public class UniversitarioController {
    @Autowired
    private IUniversitarioService universitarioService;

    @Autowired
    private IUsuarioService usuarioService;

    @PreAuthorize("hasRole('VOLUNTARIO')")
    @PostMapping("/universitario")
    public ResponseEntity<UniversitarioDTO> insertarUniversitario(@Valid @RequestBody UniversitarioDTO universitarioDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Long idUsuario = usuarioService.obtenerIdPorUsername(username);

        universitarioDTO.setIdusuario(idUsuario);// Asocia el universitario al usuario autenticado
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

    @PreAuthorize("hasRole('VOLUNTARIO')")
    @PutMapping("/universitario")
    public ResponseEntity<UniversitarioDTO> actualizarUniversitario(@RequestBody UniversitarioDTO universitarioDTO){
        return ResponseEntity.ok(universitarioService.actualizarUniversitario(universitarioDTO));
    }

    @PreAuthorize("hasRole('VOLUNTARIO')")
    @DeleteMapping("/universitario/{id}")
    public void eliminarUniversitario(@PathVariable Long id)
    {
        universitarioService.eliminarUniversitario(id);
    }

}
