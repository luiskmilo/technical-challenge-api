package technical.challenge.dto;

/**
 * DTO para la solicitud de inicio de sesion.
 * Soporta autenticacion por credenciales (email+password) o token SSO.
 */
public class LoginRequest {

    private String email;
    private String password;
    private String ssoToken;

    public LoginRequest() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSsoToken() {
        return ssoToken;
    }

    public void setSsoToken(String ssoToken) {
        this.ssoToken = ssoToken;
    }

    public boolean isSsoRequest() {
        return ssoToken != null && !ssoToken.isEmpty();
    }

}
