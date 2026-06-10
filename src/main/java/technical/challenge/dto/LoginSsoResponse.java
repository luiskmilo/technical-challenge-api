package technical.challenge.dto;

/**
 * DTO para la respuesta de inicio de sesion, incluye el token JWT generado.
 */
public class LoginSsoResponse {

    private String token;
    private long expiresIn;

    public LoginSsoResponse() {
    }

    public LoginSsoResponse(String token, long expiresIn) {
        this.token = token;
        this.expiresIn = expiresIn;
    }

    public String getToken() {
        return token;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

}
