package wad.sentinel.security.configuration;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException)
            throws IOException, ServletException {
        logger.error("{}: {}", authException.getClass().getSimpleName(), authException.getMessage());

        // Comprobar si el error original en la request es un problema de deserialización
        Throwable cause = (Throwable) request.getAttribute("javax.servlet.error.exception");

        if (cause instanceof HttpMessageNotReadableException) {
            logger.error("Error de deserialización: {}", cause.getMessage());

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            String jsonResponse = String.format(
                    "{\"error\": \"%s\", \"message\": \"%s\"}",
                    "BadRequest",
                    "Error de deserialización: JSON no válido o formato incorrecto");

            response.getWriter().write(jsonResponse);
            return; // Salir del método para evitar que sea tratado como un error de autenticación
        }

        // Procesar errores de autenticación normales
        if (authException.getClass().getSimpleName().equals("InsufficientAuthenticationException")) {
            if (response.getStatus() == HttpServletResponse.SC_OK) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            String jsonResponse = String.format(
                    "{\"error\": \"%s\", \"message\": \"%s\"}",
                    authException.getClass().getSimpleName(),
                    authException.getMessage());

            response.getWriter().write(jsonResponse);
        }
    }
}