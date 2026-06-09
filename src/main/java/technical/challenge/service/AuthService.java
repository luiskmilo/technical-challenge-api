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
     * Autentica un usuario por credenciales (email + password).
     * @param request con email y password
     * @return LoginResponse con token JWT, o null si falla la autenticacion
     */
    public LoginResponse authenticate(LoginRequest request) {
        User user = User.findByEmail(request.getEmail());
        if (user == null || !user.getPassword().equals(request.getPassword())) {
            return null;
        }
        return buildResponse(user);
    }

    /**
     * Autentica un usuario mediante token SSO.
     * @param ssoToken token SSO a validar
     * @return LoginResponse con token JWT, o null si falla la autenticacion
     */
    public LoginResponse authenticateWithSso(String ssoToken) {
        User user = ssoProvider.validateSsoToken(ssoToken);
        if (user == null) return null;
        return buildResponse(user);
    }

    private LoginResponse buildResponse(User user) {
        String token = jwtProvider.generateToken(user.getEmail(), user.getRole());
        long expiresIn = jwtProvider.getExpirationMs();
        return new LoginResponse(token, "Bearer", user.getEmail(), user.getName(),
                user.getRole().name(), expiresIn);
    }

}
