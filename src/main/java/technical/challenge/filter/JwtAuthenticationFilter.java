package technical.challenge.filter;

import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;
import technical.challenge.security.JwtTokenProvider;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filtro que intercepta todas las peticiones excepto /api/auth/login
 * y valida el token JWT presente en el header Authorization.
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtTokenProvider jwtProvider;

    public JwtAuthenticationFilter() {
        this.jwtProvider = new JwtTokenProvider();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        // Permitir los endpoints de autenticacion sin token JWT
        if (path.equals("/api/auth/login") || path.equals("/api/auth/sso")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extraer token del header Authorization
        String authHeader = request.getHeader(AUTHORIZATION_HEADER);
        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            sendUnauthorized(response, "Token JWT requerido");
            return;
        }

        String token = authHeader.substring(BEARER_PREFIX.length());
        String email = jwtProvider.validateToken(token);

        if (email == null) {
            sendUnauthorized(response, "Token JWT invalido o expirado");
            return;
        }

        // Adjuntar email del usuario autenticado al request
        request.setAttribute("authenticatedUser", email);
        filterChain.doFilter(request, response);
    }

    /**
     * Envia respuesta de error 401 en formato JSON.
     */
    private void sendUnauthorized(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(
                "{\"status\":401,\"message\":\"" + message + "\",\"timestamp\":" + System.currentTimeMillis() + "}");
    }

}
