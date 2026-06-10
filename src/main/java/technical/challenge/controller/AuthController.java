package technical.challenge.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import technical.challenge.dto.ErrorResponse;
import technical.challenge.dto.LoginRequest;
import technical.challenge.dto.LoginResponse;
import technical.challenge.dto.LoginSsoResponse;
import technical.challenge.dto.SsoLoginRequest;
import technical.challenge.service.AuthService;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Controlador para los endpoints de autenticacion.
 * Expone POST /api/auth/login para credenciales y GET /api/auth/sso para SSO.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Endpoint de inicio de sesion por credenciales (email + password).
     *
     * Ejemplo body:
     *   { "email": "admin@test.com", "password": "admin123" }
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        LoginResponse response = authService.authenticate(request);

        if (response == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse(401, "Credenciales invalidas"));
        }

        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint de inicio de sesion por SSO.
     *
     */
    @GetMapping("/sso")
    public ResponseEntity<?> initSso() {
        String clientId = "123";
        String redirectUri = "http://localhost:8080/api/auth/sso/callback";
        String ssoProviderUrl = "https://sso-econocom.com/auth";

        String url = ssoProviderUrl
                + "?client_id=" + clientId
                + "&redirect_uri=" + URLEncoder.encode(redirectUri, StandardCharsets.UTF_8)
                + "&response_type=code";

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(url))
                .build();
    }

    /**
     * Paso 2 — Callback del proveedor SSO con el code de autorización
     * Ejemplo: GET /api/auth/sso/callback?code=123
     */
    @GetMapping("/sso/callback")
    public ResponseEntity<?> ssoCallback(@RequestParam String code) {
        if (code == null || code.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(400, "Codigo de autorización ausente"));
        }

        LoginSsoResponse response = authService.authenticateWithSso(code);

        if (response == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse(401, "Codigo SSO invalido o expirado"));
        }

        return ResponseEntity.ok(response);
    }

}
