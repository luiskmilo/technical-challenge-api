package technical.challenge.security;

import org.springframework.stereotype.Component;
import technical.challenge.model.User;
import java.util.List;

@Component
public class SsoProvider {

    // Códigos simulados válidos
    private static final List<String> VALID_CODES = List.of(
            "123",
            "code-econocom-ok"
    );

    public User validateSsoToken(String code) {
        if (!VALID_CODES.contains(code)) return null;
        // Simula usuario autenticado via SSO
        return User.findByEmail("admin@test.com");
    }
}