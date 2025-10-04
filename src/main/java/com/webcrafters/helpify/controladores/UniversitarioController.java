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
        // Si la validación falla, este código no se ejecutará.
        // Spring lanzará una excepción MethodArgumentNotValidException
        log.info("Registrando universitario {}", universitarioDTO.getCodigoestudiante());
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
