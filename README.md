# Technical Challenge Api

API REST de autenticación con JWT y SSO simulados. Proyecto Spring Boot con usuarios en memoria.

---

## Tecnologías usadas

| Tecnología | Versión |
|---|---|
| Java | 1.8 |
| Spring Boot | 2.7.18 |
| Maven | 3.x+ |
| JWT (HMAC-SHA256) | Implementación manual |

## Librerías usadas

| Librería | Versión | Propósito |
|---|---|---|
| `spring-boot-starter-web` | 2.7.18 | Controladores REST y embedded Tomcat |
| `lombok` | 1.18.30 | Reducción de boilerplate (getters, setters, etc.) |

## Estructura de carpetas

```
src/
├── main/
│   ├── java/technical/challenge/
│   │   ├── ChallengeApplication.java          # Punto de entrada
│   │   ├── config/
│   │   │   └── WebConfig.java                 # CORS y registro del filtro JWT
│   │   ├── controller/
│   │   │   └── AuthController.java            # Endpoints de autenticación
│   │   ├── dto/
│   │   │   ├── ErrorResponse.java             # DTO de error
│   │   │   ├── LoginRequest.java              # Request: email + password
│   │   │   ├── LoginResponse.java             # Response: token + datos
│   │   │   └── SsoLoginRequest.java           # Request: ssoToken
│   │   ├── filter/
│   │   │   └── JwtAuthenticationFilter.java   # Filtro de validación JWT
│   │   ├── model/
│   │   │   ├── User.java                      # Usuario con datos en memoria
│   │   │   └── UserRole.java                  # Enum: ADMIN / USER
│   │   ├── security/
│   │   │   ├── JwtTokenProvider.java          # Generación/validación JWT
│   │   │   └── SsoProvider.java               # Simulación de SSO
│   │   └── service/
│   │       └── AuthService.java               # Lógica de autenticación
│   └── resources/
│       └── application.yaml                   # Configuración (puerto 8080)

```

## Endpoints

### `POST /api/auth/login`

Inicio de sesión por credenciales.

**Body (JSON):**
```json
{
  "email": "admin@test.com",
  "password": "admin123"
}
```

**Respuesta exitosa (200):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "email": "admin@test.com",
  "name": "Admin",
  "role": "ADMIN",
  "expiresIn": 3600000
}
```

**Respuesta error (401):**
```json
{
  "status": 401,
  "message": "Credenciales invalidas",
  "timestamp": 1700000000000
}
```

### `POST /api/auth/sso`

Inicio de sesión mediante token SSO simulado.


**Respuesta exitosa (302):**.
 Respuesta de redirección exitosa

> **Nota:** Todos los demás endpoints requieren el header `Authorization: Bearer <token>`.

## Usuarios de prueba (en memoria)

| Email | Password | Nombre | Rol |
|---|---|---|---|
| admin@test.com | admin123 | Admin | ADMIN |
| user@test.com | user123 | User | USER |

## Comandos

```bash
# Compilar el proyecto
mvn clean compile

# Ejecutar la aplicación (puerto 8080)
mvn spring-boot:run
```
