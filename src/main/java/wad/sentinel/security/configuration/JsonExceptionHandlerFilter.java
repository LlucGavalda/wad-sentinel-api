package wad.sentinel.security.configuration;

import java.io.IOException;

import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JsonExceptionHandlerFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (HttpMessageNotReadableException ex) {
        	logger.error(ex.getMessage(), ex);
            handleException(response, "Error de deserialización: JSON no válido o formato incorrecto.", HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception ex) {
        	logger.error(ex.getMessage(), ex);
            handleException(response, ex.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void handleException(HttpServletResponse response, String message, int status) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String jsonResponse = String.format("{\"error\": \"BadRequest\", \"message\": \"%s\"}", message);
        response.getWriter().write(jsonResponse);
    }
}