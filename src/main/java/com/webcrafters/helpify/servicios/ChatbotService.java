package com.webcrafters.helpify.servicios;

import com.webcrafters.helpify.interfaces.IChatbotService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Service
public class ChatbotService implements IChatbotService {

    @Override
    public String generarRespuesta(String mensajeUsuario) {

        // -----------------------
        // VERIFICAR AUTENTICACIÓN
        // -----------------------
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
            return "Para usar el asistente virtual de Helpify debes iniciar sesión primero.";
        }

        // (Opcional) puedes obtener el usuario logueado:
        // String emailUsuario = auth.getName();

        if (mensajeUsuario == null || mensajeUsuario.isBlank()) {
            return "No entendí tu mensaje. ¿Puedes escribirlo de nuevo, por favor?";
        }

        String msg = mensajeUsuario.toLowerCase().trim();

        // -----------------------
        // SALUDOS
        // -----------------------
        if (msg.matches(".*\\b(hola|ola|buenas|buenos dias|buenas tardes|buenas noches)\\b.*")) {
            return "¡Hola! Soy el asistente virtual de Helpify 😊. ¿En qué puedo ayudarte hoy?";
        }

        // -----------------------
        // REGISTRO / LOGIN
        // (el usuario ya está logueado, pero igual puedo responder)
        // -----------------------
        if (msg.contains("registr") || msg.contains("crear cuenta") || msg.contains("modificar") || msg.contains("cuenta")) {
            return "Ya tienes una cuenta activa 😄. Si quieres modificar tus datos, entra a la sección de perfil.";
        }

        if (msg.contains("iniciar") || msg.contains("login") || msg.contains("entrar")) {
            return "Ya iniciaste sesión correctamente. ¿En qué puedo ayudarte?";
        }

        // -----------------------
        // DONACIONES
        // -----------------------
        if (msg.contains("donar") || msg.contains("donación") || msg.contains("donaciones") || msg.contains("donacion")) {
            return "Para realizar una donación: entra a 'Proyectos' → elige un proyecto → haz clic en 'Donar'.";
        }

        // -----------------------
        // PROYECTOS
        // -----------------------
        if (msg.contains("proyecto") || msg.contains("proyectos")) {
            return "En la sección 'Proyectos' puedes explorar proyectos activos, ver detalles y donar cuando quieras.";
        }

        // -----------------------
        // VOLUNTARIADO
        // -----------------------
        if (msg.contains("voluntari") || msg.contains("voluntariado")) {
            return "Si eres voluntario, revisa los proyectos con vacantes abiertas desde la sección 'Proyectos' y postúlate.";
        }

        // -----------------------
        // WISHLIST
        // -----------------------
        if (msg.contains("wishlist") || msg.contains("lista de deseos") || msg.contains("favorit")) {
            return "Tu lista de deseos guarda los proyectos que marcastes como favoritos. Puedes abrirla desde el menú superior.";
        }

        // -----------------------
        // FORO
        // -----------------------
        if (msg.contains("comentario") || msg.contains("foro") || msg.contains("opinion")) {
            return "En el foro puedes dejar comentarios y calificaciones. Este se encuentra en el menú principal.";
        }

        // -----------------------
        // DEFAULT
        // -----------------------
        return "Soy el asistente virtual de Helpify 😄. Puedo ayudarte con:\n"
                + "- Donaciones\n"
                + "- Proyectos\n"
                + "- Voluntariado\n"
                + "- Wishlist\n"
                + "- Foro y comentarios\n\n"
                + "Dime sobre qué tema necesitas ayuda.";
    }
}