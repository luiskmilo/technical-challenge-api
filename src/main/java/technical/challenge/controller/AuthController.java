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
import technical.challenge.dto.SsoLoginRequest;
import technical.challenge.service.AuthService;

/**
 * Controlador para los endpoints de autenticacion.
 * Expone POST /api/auth/login para credenciales y POST /api/auth/sso para SSO.
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
     * Ejemplo body:
     *   { "ssoToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9" }
     */
    @PostMapping("/sso")
    public ResponseEntity<?> ssoLogin(@RequestBody SsoLoginRequest request) {
        LoginResponse response = authService.authenticateWithSso(request.getSsoToken());

        if (response == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse(401, "Token SSO invalido"));
        }

        return ResponseEntity.ok(response);
    }

}
