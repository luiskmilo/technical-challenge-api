package technical.challenge.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Representa un usuario del sistema con datos básicos para autenticación.
 */
public class User {

    private String email;
    private String password;
    private String name;
    private UserRole role;

    // Usuarios simulados en memoria (sin base de datos real)
    private static final Map<String, User> USERS = new HashMap<>();

    static {
        USERS.put("admin@test.com", new User("admin@test.com", "admin123", "Admin", UserRole.ADMIN));
        USERS.put("user@test.com", new User("user@test.com", "user123", "User", UserRole.USER));
    }

    public User() {
    }

    public User(String email, String password, String name, UserRole role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public UserRole getRole() {
        return role;
    }

    /**
     * Busca un usuario por su correo electrónico en el almacén en memoria.
     */
    public static User findByEmail(String email) {
        return USERS.get(email);
    }

}
