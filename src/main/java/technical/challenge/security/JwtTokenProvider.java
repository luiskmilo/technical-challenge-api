package technical.challenge.security;

import technical.challenge.model.UserRole;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Base64;
import java.util.StringJoiner;

/**
 * Proveedor de tokens JWT manual (sin librerias externas).
 * Implementa JWT con firma HMAC-SHA256 usando solo Java standard.
 */
public class JwtTokenProvider {

    private static final String SECRET = "clave-secreta-super-segura-2024";
    private static final long EXPIRATION_MS = 3_600_000; // 1 hora
    private static final String ALGORITHM = "HmacSHA256";

    /**
     * Genera un token JWT firmado con HS256.
     * Formato: base64(header).base64(payload).base64(signature)
     */
    public String generateToken(String email, UserRole role) {
        try {
            // Header: algoritmo HS256 y tipo JWT
            String header = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";

            // Payload: claims estandar (sub, iat, exp) y datos personalizados
            Instant now = Instant.now();
            Instant expiration = now.plusMillis(EXPIRATION_MS);
            String payload = "{"
                    + "\"sub\":\"" + email + "\","
                    + "\"role\":\"" + role.name() + "\","
                    + "\"iat\":" + now.getEpochSecond() + ","
                    + "\"exp\":" + expiration.getEpochSecond()
                    + "}";

            // Codificar header y payload en Base64 URL-safe
            String encodedHeader = base64UrlEncode(header.getBytes(StandardCharsets.UTF_8));
            String encodedPayload = base64UrlEncode(payload.getBytes(StandardCharsets.UTF_8));

            // Firmar con HMAC-SHA256
            String signingInput = encodedHeader + "." + encodedPayload;
            String signature = sign(signingInput);

            return encodedHeader + "." + encodedPayload + "." + signature;

        } catch (Exception e) {
            throw new RuntimeException("Error generando token JWT", e);
        }
    }

    /**
     * Valida un token JWT y retorna el email del sujeto si es valido.
     */
    public String validateToken(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) return null;

            String encodedHeader = parts[0];
            String encodedPayload = parts[1];
            String signature = parts[2];

            // Verificar firma
            String signingInput = encodedHeader + "." + encodedPayload;
            String expectedSignature = sign(signingInput);
            if (!expectedSignature.equals(signature)) return null;

            // Decodificar payload y verificar expiracion
            String payload = new String(base64UrlDecode(encodedPayload), StandardCharsets.UTF_8);
            long exp = extractClaim(payload, "exp");
            if (Instant.now().getEpochSecond() > exp) return null;

            return extractClaimValue(payload, "sub");

        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Obtiene la duracion del token en milisegundos.
     */
    public long getExpirationMs() {
        return EXPIRATION_MS;
    }

    private String sign(String data) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac mac = Mac.getInstance(ALGORITHM);
        SecretKeySpec keySpec = new SecretKeySpec(SECRET.getBytes(StandardCharsets.UTF_8), ALGORITHM);
        mac.init(keySpec);
        return base64UrlEncode(mac.doFinal(data.getBytes(StandardCharsets.UTF_8)));
    }

    private String base64UrlEncode(byte[] data) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(data);
    }

    private byte[] base64UrlDecode(String data) {
        return Base64.getUrlDecoder().decode(data);
    }

    /**
     * Extrae el valor de una claim del JSON del payload usando busqueda simple.
     */
    private long extractClaim(String json, String claim) {
        String searchKey = "\"" + claim + "\":";
        int start = json.indexOf(searchKey);
        if (start == -1) return 0;
        start += searchKey.length();
        int end = json.indexOf(",", start);
        if (end == -1) end = json.indexOf("}", start);
        return Long.parseLong(json.substring(start, end).trim());
    }

    /**
     * Extrae el valor string de una claim del JSON del payload.
     */
    private String extractClaimValue(String json, String claim) {
        String searchKey = "\"" + claim + "\":\"";
        int start = json.indexOf(searchKey);
        if (start == -1) return null;
        start += searchKey.length();
        int end = json.indexOf("\"", start);
        return json.substring(start, end);
    }

}
