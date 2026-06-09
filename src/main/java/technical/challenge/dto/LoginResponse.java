package technical.challenge.dto;

/**
 * DTO para la respuesta de inicio de sesion, incluye el token JWT generado.
 */
public class LoginResponse {

    private String token;
    private String type;
    private String email;
    private String name;
    private String role;
    private long expiresIn;

    public LoginResponse() {
    }

    public LoginResponse(String token, String type, String email, String name, String role, long expiresIn) {
        this.token = token;
        this.type = type;
        this.email = email;
        this.name = name;
        this.role = role;
        this.expiresIn = expiresIn;
    }

    public String getToken() {
        return token;
    }

    public String getType() {
        return type;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

}
