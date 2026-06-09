package technical.challenge.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import technical.challenge.dto.ErrorResponse;
import technical.challenge.dto.LoginRequest;
import technical.challenge.dto.LoginResponse;
import technical.challenge.service.AuthService;

/**
 * Controlador para el endpoint de autenticacion.
 * Expone POST /api/auth/login para login con credenciales o SSO.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Endpoint de inicio de sesion unificado.
     * Soporta login por email+password o por token SSO.
     *
     * Ejemplo body (credenciales):
     *   { "email": "admin@test.com", "password": "admin123" }
     *
     * Ejemplo body (SSO):
     *   { "ssoToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9" }
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

}
