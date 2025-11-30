package com.webcrafters.helpify.controladores;

import com.webcrafters.helpify.DTO.ChatRequestDTO;
import com.webcrafters.helpify.DTO.ChatResponseDTO;
import com.webcrafters.helpify.interfaces.IChatbotService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@CrossOrigin(
        origins = "${ip.frontend}",
        allowCredentials = "true",
        exposedHeaders = {"Authorization", "Mensaje"}
)
@RequestMapping("/api")
public class ChatbotController {

    @Autowired
    private IChatbotService chatbotService;


    @PreAuthorize("isAuthenticated()")
    @PostMapping("/chatbot")
    public ResponseEntity<ChatResponseDTO> conversar(@RequestBody ChatRequestDTO request) {
        log.info("Mensaje recibido en chatbot: {}", request.getMessage());

        String respuesta = chatbotService.generarRespuesta(request.getMessage());
        ChatResponseDTO dto = new ChatResponseDTO(respuesta);

        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }
}
