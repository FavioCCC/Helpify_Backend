package com.webcrafters.helpify.controladores;

import com.webcrafters.helpify.DTO.UsuarioConComentariosDTO;
import com.webcrafters.helpify.DTO.UsuarioConDonacionesDTO;
import com.webcrafters.helpify.DTO.UsuarioDTO;
import com.webcrafters.helpify.interfaces.IUsuarioService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:4200",
        allowCredentials = "true",
        exposedHeaders = "Authorization")
@RequestMapping("/api")
public class UsuarioController {
    @Autowired
    private IUsuarioService usuarioService;

    @PostMapping("/usuario")
    public ResponseEntity<UsuarioDTO> insertarUsuario(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        log.info("Registrando usuario {}", usuarioDTO.getNombre());
        return ResponseEntity.ok(usuarioService.insertar(usuarioDTO));
    }


    @GetMapping("/usuarios")
    public List<UsuarioDTO> listarUsuarios(){
        log.info("Lista de usuarios");
        return usuarioService.listarTodos();
    }

    @GetMapping("/usuarios/comentarios")
    public List<UsuarioConComentariosDTO> listarUsuariosConComentarios(){
        log.info("Lista de usuarios con sus comentarios");
        return usuarioService.listarUsuariosConComentarios();
    }

    @GetMapping("/usuarios/donaciones")
    public List<UsuarioConDonacionesDTO> listarUsuariosConDonaciones(){
        log.info("Lista de usuarios con sus donaciones");
        return usuarioService.listarUsuariosConDonaciones();
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<UsuarioDTO> buscarUsuarioPorId(@PathVariable Long idUsuario){
        return ResponseEntity.ok(usuarioService.buscarPorId(idUsuario));
    }

    @PutMapping("/usuario")
    public ResponseEntity<UsuarioDTO> actualizarUsuario(@RequestBody UsuarioDTO usuarioDTO){
        return ResponseEntity.ok(usuarioService.actualizar(usuarioDTO));
    }

    @DeleteMapping("/usuario/{id}")
    public void eliminarUsuario(@PathVariable Long id)
    {
        usuarioService.eliminar(id);
    }

}



