package technical.challenge.security;

import technical.challenge.model.User;
import technical.challenge.model.UserRole;

import java.util.HashMap;
import java.util.Map;

/**
 * Proveedor SSO simulado para pruebas sin infraestructura real.
 * Valida tokens SSO ficticios y retorna datos de usuario.
 */
public class SsoProvider {

    // Mapa de tokens SSO simulados a datos de usuario
    private static final Map<String, SsoUser> SSO_TOKENS = new HashMap<>();

    static {
        SSO_TOKENS.put("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9", new SsoUser("admin@test.com", "Admin SSO", UserRole.ADMIN));
        SSO_TOKENS.put("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9", new SsoUser("user@test.com", "User SSO", UserRole.USER));
    }

    /**
     * Valida un token SSO simulado y retorna el usuario asociado.
     * @param ssoToken token SSO a validar
     * @return User si el token es valido, null en caso contrario
     */
    public User validateSsoToken(String ssoToken) {
        SsoUser ssoUser = SSO_TOKENS.get(ssoToken);
        if (ssoUser == null) return null;
        return new User(ssoUser.email, null, ssoUser.name, ssoUser.role);
    }

    /**
     * Clase interna para datos de usuario SSO.
     */
    private static class SsoUser {
        final String email;
        final String name;
        final UserRole role;

        SsoUser(String email, String name, UserRole role) {
            this.email = email;
            this.name = name;
            this.role = role;
        }
    }

}
