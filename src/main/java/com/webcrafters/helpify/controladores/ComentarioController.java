package com.webcrafters.helpify.controladores;

import com.webcrafters.helpify.DTO.ComentarioDTO;
import com.webcrafters.helpify.DTO.ComentarioSinProyectoyUsuarioDTO;
import com.webcrafters.helpify.interfaces.IComentarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comentario")
public class ComentarioController {
    @Autowired
    private IComentarioService comentarioService;

    // Crear comentario
    @PostMapping("/{usuarioId}/proyecto/{proyectoId}")
    public ComentarioDTO insertarComentario(@RequestBody ComentarioDTO comentarioDTO,
                                            @PathVariable Long usuarioId,
                                            @PathVariable Long proyectoId) {
        return comentarioService.insertarComentario(comentarioDTO, usuarioId, proyectoId);
    }

    // Actualizar comentario
    @PutMapping("/comentario/{idUsuario}/proyecto/{idProyecto}")
    public ResponseEntity<ComentarioDTO> actualizarComentario(
            @RequestBody ComentarioDTO comentarioDTO,
            @PathVariable Long idUsuario,
            @PathVariable Long idProyecto) {
        return ResponseEntity.ok(comentarioService.actualizarComentario(comentarioDTO, idUsuario, idProyecto));
    }


    // Eliminar comentario
    @DeleteMapping("/{comentarioId}/usuario/{usuarioId}/proyecto/{proyectoId}")
    public void eliminarComentario(@PathVariable Long comentarioId,
                                   @PathVariable Long usuarioId,
                                   @PathVariable Long proyectoId) {
        comentarioService.eliminarComentario(comentarioId, usuarioId, proyectoId);
    }

    // Listar comentarios por proyecto y usuario
    @GetMapping("/proyecto/{proyectoId}/usuario/{usuarioId}")
    public List<ComentarioSinProyectoyUsuarioDTO> listarPorProyectoYUsuario(@PathVariable Long proyectoId,
                                                                            @PathVariable Long usuarioId) {
        return comentarioService.listarComentarioPorProyectoyUsuario(proyectoId, usuarioId);
    }


    // Buscar comentarios por calificación
    @GetMapping("/calificacion/{estrella}")
    public List<ComentarioSinProyectoyUsuarioDTO> buscarPorCalificacion(@PathVariable double estrella) {
        return comentarioService.buscarPorCalificacion(estrella);
    }
}
