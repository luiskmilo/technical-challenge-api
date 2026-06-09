package technical.challenge.dto;

/**
 * DTO para la solicitud de inicio de sesion por SSO.
 */
public class SsoLoginRequest {

    private String ssoToken;

    public SsoLoginRequest() {
    }

    public String getSsoToken() {
        return ssoToken;
    }

    public void setSsoToken(String ssoToken) {
        this.ssoToken = ssoToken;
    }

}
