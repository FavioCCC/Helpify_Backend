package com.webcrafters.helpify.controladores;

import com.webcrafters.helpify.seguridad.DTO.RolDTO;
import com.webcrafters.helpify.interfaces.IRolService;
import com.webcrafters.helpify.interfaces.IUsuarioService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
public class RolController {
    @Autowired
    private IRolService rolService;

    @Autowired
    private IUsuarioService usuarioService;

    @PostMapping(value = "/rol")
    public ResponseEntity<RolDTO> insertarRol(@Valid @RequestBody RolDTO rolDTO) {
        // Si la validación falla, este código no se ejecutará.
        // Spring lanzará una excepción MethodArgumentNotValidException
        log.info("Registrando rol {}", rolDTO.getNombre());
        return ResponseEntity.ok(rolService.insertarRol(rolDTO));
    }

    @GetMapping("/roles")
    public List<RolDTO> listarRoles(){
        log.info("Lista de roles");
        return rolService.listarTodosLosRoles();
    }

    @GetMapping("/rol/{idRol}")
    public ResponseEntity<RolDTO> buscarRolPorId(@PathVariable Long idRol){
        return ResponseEntity.ok(rolService.buscarRolPorId(idRol));
    }

    @PutMapping("/rol")
    public ResponseEntity<RolDTO> actualizarRol(@RequestBody RolDTO rolDTO){
        return ResponseEntity.ok(rolService.actualizarRol(rolDTO));
    }

    @DeleteMapping("/rol/{id}")
    public void eliminarRol(@PathVariable Long id)
    {
        rolService.eliminarRol(id);
    }

    //@PostMapping("/usuarioDeRol")
   // public ResponseEntity<UsuarioDTO> insertarUsuario(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        // Si la validación falla, este código no se ejecutará.
        // Spring lanzará una excepción MethodArgumentNotValidException
     //   log.info("Registrando usuario {}", usuarioDTO.getNombre());
     //   return ResponseEntity.ok(usuarioService.insertar(usuarioDTO));
   // }

    //@GetMapping("/usuariosDeRoles")
    //public List<UsuarioDTO> listarUsuarios(){
    //    log.info("Lista de usuario");
    //    return usuarioService.listarTodos();
   // }
}
