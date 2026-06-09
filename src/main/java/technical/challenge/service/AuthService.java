package technical.challenge.service;

import org.springframework.stereotype.Service;
import technical.challenge.dto.LoginRequest;
import technical.challenge.dto.LoginResponse;
import technical.challenge.model.User;
import technical.challenge.security.JwtTokenProvider;
import technical.challenge.security.SsoProvider;

/**
 * Servicio de autenticacion que maneja el login por credenciales y por SSO.
 * Genera tokens JWT para usuarios autenticados correctamente.
 */
@Service
public class AuthService {

    private final JwtTokenProvider jwtProvider;
    private final SsoProvider ssoProvider;

    public AuthService() {
        this.jwtProvider = new JwtTokenProvider();
        this.ssoProvider = new SsoProvider();
    }

    /**
     * Autentica un usuario por credenciales (email+password) o por SSO.
     * @param request con email/password o ssoToken
     * @return LoginResponse con token JWT, o null si falla la autenticacion
     */
    public LoginResponse authenticate(LoginRequest request) {
        User user = null;

        if (request.isSsoRequest()) {
            // Autenticacion via SSO simulado
            user = ssoProvider.validateSsoToken(request.getSsoToken());
        } else {
            // Autenticacion por credenciales (email + password)
            user = User.findByEmail(request.getEmail());
            if (user != null && !user.getPassword().equals(request.getPassword())) {
                user = null;
            }
        }

        if (user == null) return null;

        // Generar token JWT
        String token = jwtProvider.generateToken(user.getEmail(), user.getRole());
        long expiresIn = jwtProvider.getExpirationMs();

        return new LoginResponse(token, "Bearer", user.getEmail(), user.getName(),
                user.getRole().name(), expiresIn);
    }

}
