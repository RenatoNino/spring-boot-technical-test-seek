# 🚀 Cliente Management API - Spring Boot Technical Test

Una aplicación REST API desarrollada con Spring Boot 3.5.8 para la gestión de clientes con autenticación JWT, validaciones empresariales y métricas estadísticas.

## 📋 Tabla de Contenidos
- [Tecnologías Utilizadas](#-tecnologías-utilizadas)
- [Estructura del Proyecto](#-estructura-del-proyecto)
- [Rutas Implementadas](#-rutas-implementadas)
- [Manejo de Excepciones](#-manejo-de-excepciones)
- [Seguridad y Autenticación](#-seguridad-y-autenticación)
- [Testing](#-testing)
- [Instalación y Ejecución](#-instalación-y-ejecución)

---

## 🛠 Tecnologías Utilizadas

### Backend
- **Java 17** - Lenguaje de programación
- **Spring Boot 3.5.8** - Framework principal
- **Spring Security 6.x** - Autenticación y autorización
- **Spring Data JPA** - Persistencia de datos
- **Flyway 10.x** - Versionamiento de base de datos

### Seguridad & JWT
- **JJWT 0.11.5** - Generación y validación de tokens JWT
- **HMAC-SHA256** - Algoritmo de firma de tokens

### Base de Datos
- **MySQL 8.x** - Base de datos relacional (producción)
- **H2 Database** - Base de datos en memoria (testing)

### Testing
- **JUnit 5** - Framework de testing
- **Mockito** - Mocking de dependencias
- **Spring Test** - Testing de Spring Boot
- **AssertJ** - Assertions fluidas
- **MockMvc** - Testing de controladores

### Utilidades
- **Lombok 1.18.30** - Generación de código boilerplate
- **Validation API** - Validación de datos
- **Micrometer + Prometheus** - Métricas y monitoreo
- **Springdoc OpenAPI 2.8.14** - Documentación Swagger

---

## 📂 Estructura del Proyecto

```
src/
├── main/
│   ├── java/com/renato/pruebatecnica/seek/prueba_tecnica_seek/
│   │   ├── adapters/
│   │   │   └── ClientResponseAdapter.java         # Adaptador para convertir entidades a DTOs
│   │   ├── config/
│   │   │   ├── JpaAuditingConfig.java             # Configuración de auditoría JPA
│   │   │   └── OpenApiConfig.java                 # Configuración de Swagger/OpenAPI
│   │   ├── controllers/
│   │   │   ├── AuthController.java                # Endpoint de autenticación
│   │   │   └── ClientController.java              # Endpoints CRUD de clientes
│   │   ├── dtos/
│   │   │   ├── ClientCreateRequest.java           # DTO para crear cliente
│   │   │   ├── ClientListResponse.java            # DTO para listar clientes
│   │   │   ├── ClientUpdateRequest.java           # DTO para actualizar cliente
│   │   │   ├── LoginRequest.java                  # DTO para login
│   │   │   ├── MetricsResponse.java               # DTO para métricas
│   │   │   └── TokenResponse.java                 # DTO para respuesta de token
│   │   ├── entities/
│   │   │   ├── AuditableEntity.java               # Superclase con campos de auditoría
│   │   │   ├── Client.java                        # Entidad Cliente
│   │   │   ├── Role.java                          # Entidad Rol
│   │   │   └── User.java                          # Entidad Usuario
│   │   ├── exceptions/
│   │   │   ├── BusinessException.java             # Excepción de lógica de negocio
│   │   │   ├── UnauthorizedException.java         # Excepción de autorización
│   │   │   ├── ErrorResponse.java                 # DTO para respuesta de error
│   │   │   └── GlobalExceptionHandler.java        # Manejador global de excepciones
│   │   ├── repositories/
│   │   │   ├── ClientRepository.java              # Repositorio de clientes (JPA)
│   │   │   └── UserRepository.java                # Repositorio de usuarios (JPA)
│   │   ├── security/
│   │   │   ├── JwtAuthenticationFilter.java       # Filtro de autenticación JWT
│   │   │   ├── JwtTokenProvider.java              # Proveedor de tokens JWT
│   │   │   └── SecurityConfig.java                # Configuración de seguridad
│   │   ├── services/
│   │   │   └── ClientService.java                 # Lógica de negocio de clientes
│   │   ├── validations/
│   │   │   └── ClientValidation.java              # Validaciones de cliente
│   │   └── PruebaTecnicaSeekApplication.java      # Clase principal
│   │
│   └── resources/
│       ├── application.properties                 # Configuración principal
│       ├── application-test.properties            # Configuración para testing
│       └── db/migration/
│           ├── V1__create_table_client.sql        # Migración: crear tabla client
│           ├── V2__add_audit_columns.sql          # Migración: agregar columnas de auditoría
│           ├── V3__create_table_role.sql          # Migración: crear tabla role
│           └── V4__create_table_user.sql          # Migración: crear tabla user
│
└── test/
    ├── java/com/renato/pruebatecnica/seek/prueba_tecnica_seek/
    │   ├── AuthControllerIntegrationTest.java     # Tests de AuthController (unit)
    │   ├── ClientControllerIntegrationTest.java   # Tests de ClientController (integración)
    │   └── PruebaTecnicaSeekApplicationTests.java # Test de contexto
    │
    └── resources/
        └── application-test.properties            # Configuración de testing
```

### Arquitectura en Capas

```
┌─────────────────────────────────────────────────────┐
│              PRESENTATION LAYER                     │
│  AuthController  |  ClientController                │
└────────────────────┬────────────────────────────────┘
                     │
┌─────────────────────────────────────────────────────┐
│              SECURITY LAYER                         │
│  JwtTokenProvider  |  JwtAuthenticationFilter       │
│  SecurityConfig                                     │
└────────────────────┬────────────────────────────────┘
                     │
┌─────────────────────────────────────────────────────┐
│              BUSINESS LOGIC LAYER                   │
│  ClientService  |  ClientValidation                 │
│  ClientResponseAdapter                              │
└────────────────────┬────────────────────────────────┘
                     │
┌─────────────────────────────────────────────────────┐
│              DATA LAYER                             │
│  ClientRepository  |  UserRepository                │
│  JPA / Hibernate                                    │
└────────────────────┬────────────────────────────────┘
                     │
                ┌────▼─────┐
                │  MySQL   │
                └──────────┘
```

---

## 🔌 Rutas Implementadas

### Base URL
```
http://localhost:8081
```

### 🔐 Autenticación

#### Login
```http
POST /auth/login
Content-Type: application/json

{
  "email": "user@email.com",
  "password": "password"
}

Response (200 OK):
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwicm9sZXMiOlsiUk9MRV9BRE1JTiJdfQ..."
}
```

**Usuario Básico Incluido:**
- **Email:** `user@email.com`
- **Password:** `password`
- **Rol:** `ROLE_ADMIN`

---

### 👥 Clientes - CRUD Operations

#### Crear Cliente
```http
POST /api/v1/clients
Authorization: Bearer {token}
Content-Type: application/json

{
  "name": "John",
  "surname": "Doe",
  "age": 30,
  "birthDate": "1994-12-17"
}

Response (201 Created):
{
  "id": 1,
  "name": "John",
  "surname": "Doe",
  "age": 30,
  "estimatedDeathDate": "2074-12-17"
}
```

**Validaciones:**
- `name`: Requerido, no puede estar en blanco
- `surname`: Requerido, no puede estar en blanco
- `age`: Requerido, debe ser un número entero
- `birthDate`: Requerido, debe ser una fecha válida
- **Validación de Negocio:** La edad calculada desde `birthDate` debe coincidir con el campo `age`

---

#### Listar Clientes
```http
GET /api/v1/clients
Authorization: Bearer {token}

Response (200 OK):
[
  {
    "id": 1,
    "name": "John",
    "surname": "Doe",
    "age": 30,
    "estimatedDeathDate": "2074-12-17"
  },
  {
    "id": 2,
    "name": "Jane",
    "surname": "Smith",
    "age": 28,
    "estimatedDeathDate": "2072-05-20"
  }
]
```

---

#### Actualizar Cliente
```http
PUT /api/v1/clients/{id}
Authorization: Bearer {token}
Content-Type: application/json

{
  "name": "John Updated",
  "surname": "Doe Updated",
  "age": 31,
  "birthDate": "1993-12-17"
}

Response (200 OK):
{
  "id": 1,
  "name": "John Updated",
  "surname": "Doe Updated",
  "age": 31,
  "estimatedDeathDate": "2073-12-17"
}
```

**Notas:**
- Todos los campos son opcionales
- Si se actualiza `age` y `birthDate`, se valida que coincidan
- Los campos no enviados mantienen su valor anterior

---

#### Eliminar Cliente
```http
DELETE /api/v1/clients/{id}
Authorization: Bearer {token}

Response (204 No Content)
```

**Nota:** La eliminación es lógica (soft delete). El registro se marca como eliminado con `deleted_at`.

---

### 📊 Métricas

#### Obtener Métricas
```http
GET /api/v1/clients/metrics
Authorization: Bearer {token}

Response (200 OK):
{
  "averageAge": 29.5,
  "standardDeviation": 1.5811388
}
```

**Cálculos:**
- **averageAge:** Promedio de edad de todos los clientes no eliminados
- **standardDeviation:** Desviación estándar de las edades

---

### 🔍 Documentación Swagger

```
http://localhost:8081/swagger-ui.html
```

---

## ⚠️ Manejo de Excepciones

### Arquitectura de Excepciones

El proyecto implementa un **manejo global y centralizado de excepciones** mediante el patrón `@RestControllerAdvice`.

#### Excepciones Personalizadas

##### 1. **BusinessException**
```java
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}
```

**Uso:**
```java
throw new BusinessException("Age does not match birth date");
```

**HTTP Response:** `422 Unprocessable Entity`

**Casos de uso:**
- Validaciones de lógica de negocio fallidas
- Recursos no encontrados
- Datos inválidos según reglas de negocio

---

##### 2. **UnauthorizedException**
```java
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
```

**Uso:**
```java
throw new UnauthorizedException("Invalid credentials");
```

**HTTP Response:** `401 Unauthorized`

**Casos de uso:**
- Credenciales inválidas
- Token JWT expirado o inválido
- Falta de permisos/roles requeridos

---

#### Respuesta de Error Estándar

Todas las excepciones devuelven un `ErrorResponse` con estructura uniforme:

```java
public record ErrorResponse(
    int status,              // Código HTTP (401, 422, 400, 500)
    String error,            // Nombre del error (Unauthorized, Unprocessable Entity, etc.)
    String message,          // Mensaje descriptivo del error
    String path,             // Ruta del endpoint que causó el error
    Map<String, String> fieldErrors,  // Errores de validación por campo (null si no aplica)
    Instant timestamp        // Timestamp del error
) {}
```

**Ejemplo de respuesta:**
```json
{
  "status": 422,
  "error": "Unprocessable Entity",
  "message": "Age does not match birth date",
  "path": "/api/v1/clients",
  "fieldErrors": null,
  "timestamp": "2024-01-15T10:30:45.123456Z"
}
```

---

#### Tipos de Excepciones Manejadas

| Excepción | Status HTTP | Caso de Uso |
|-----------|------------|-----------|
| `MethodArgumentNotValidException` | 400 Bad Request | Validación de @RequestBody falla |
| `ConstraintViolationException` | 400 Bad Request | Violación de constraints (ej: @NotBlank) |
| `BusinessException` | 422 Unprocessable Entity | Lógica de negocio rechaza la solicitud |
| `UnauthorizedException` | 401 Unauthorized | Autenticación o autorización fallida |
| `Exception` (genérica) | 500 Internal Server Error | Errores inesperados |

---

#### GlobalExceptionHandler

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    // Maneja validaciones de @Valid
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(...) { ... }
    
    // Maneja validaciones de constraints
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(...) { ... }
    
    // Maneja errores de negocio
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusiness(...) { ... }
    
    // Maneja errores de autorización
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorized(...) { ... }
    
    // Fallback para excepciones no capturadas
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(...) { ... }
}
```

---

### Ejemplo: Flujo de Error

**Request:**
```http
POST /api/v1/clients
{
  "name": "Jane",
  "surname": "Doe",
  "age": 20,
  "birthDate": "2000-01-01"
}
```

**Validación en ClientValidation:**
```java
public void validateCreateClientBody(ClientCreateRequest request) {
    validateAge(request.getBirthDate(), request.getAge());
}

private void validateAge(LocalDate birthDate, Integer age) {
    int years = Period.between(birthDate, LocalDate.now()).getYears();
    if (years != age) {
        throw new BusinessException("Age does not match birth date");
        // ❌ años calculados: 24, edad proporcionada: 20
    }
}
```

**Response:**
```json
{
  "status": 422,
  "error": "Unprocessable Entity",
  "message": "Age does not match birth date",
  "path": "/api/v1/clients",
  "fieldErrors": null,
  "timestamp": "2024-01-15T10:35:22.456789Z"
}
```

---

## 🔒 Seguridad y Autenticación

### Arquitectura de Seguridad

La seguridad se implementa en **tres capas principales**:

#### 1. **JwtTokenProvider** - Generación y Validación de Tokens

```java
@Component
public class JwtTokenProvider {
    
    @Value("${jwt.secret}")
    private String jwtSecret;
    
    @Value("${jwt.expiration}")
    private long validityInSeconds;  // 3600 segundos = 1 hora
    
    // ✅ Genera un nuevo token JWT
    public String generateToken(String username, List<String> roles) {
        Instant now = Instant.now();
        Instant expiry = now.plusSeconds(validityInSeconds);
        Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        
        return Jwts.builder()
            .setSubject(username)           // Quién es el usuario
            .claim("roles", roles)          // Qué roles tiene
            .setIssuedAt(Date.from(now))    // Cuándo se emitió
            .setExpiration(Date.from(expiry)) // Cuándo expira
            .signWith(key, SignatureAlgorithm.HS256) // Firma
            .compact();
    }
    
    // ✅ Extrae el username del token
    public String getUsername(String token) {
        return parseClaims(token).getBody().getSubject();
    }
    
    // ✅ Extrae los roles del token
    public List<String> getRoles(String token) {
        Claims claims = parseClaims(token).getBody();
        Object roles = claims.get("roles");
        if (roles instanceof List<?> list) {
            return list.stream()
                .filter(String.class::isInstance)
                .map(String.class::cast)
                .toList();
        }
        return Collections.emptyList();
    }
    
    // ✅ Valida que el token sea válido y no esté expirado
    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            return false;  // Token inválido o expirado
        }
    }
    
    private Jws<Claims> parseClaims(String token) {
        Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token);
    }
}
```

**Configuración:**
```properties
jwt.secret=spring-boot-technical-test-with-mysql-for-backend-developer
jwt.expiration=3600
```

---

#### 2. **JwtAuthenticationFilter** - Interceptor de Peticiones

```java
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    private final JwtTokenProvider tokenProvider;
    
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        
        // 1️⃣ Extraer token del header Authorization
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        String token = resolveToken(bearerToken);  // "Bearer xyz" → "xyz"
        
        // 2️⃣ Validar token
        if (StringUtils.hasText(token) && tokenProvider.validateToken(token)) {
            // 3️⃣ Obtener información del usuario
            String username = tokenProvider.getUsername(token);
            List<String> roles = tokenProvider.getRoles(token);
            
            // 4️⃣ Convertir roles a autoridades
            List<SimpleGrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
            
            // 5️⃣ Crear objeto de autenticación
            UsernamePasswordAuthenticationToken authentication = 
                new UsernamePasswordAuthenticationToken(
                    username, 
                    null, 
                    authorities
                );
            authentication.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
            );
            
            // 6️⃣ Establecer en SecurityContext
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        
        // 7️⃣ Continuar con el siguiente filtro
        filterChain.doFilter(request, response);
    }
    
    // Extrae el token del header "Bearer xyz"
    private String resolveToken(String bearerToken) {
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
```

**Ciclo de vida del filtro:**
```
Request HTTP
    ↓
JwtAuthenticationFilter.doFilterInternal()
    ↓
¿Token en header?  →  NO  → SecurityContext sin autenticación
    ↓ SÍ
¿Token válido?  →  NO  → SecurityContext sin autenticación
    ↓ SÍ
Extraer username y roles
    ↓
Crear Authentication object
    ↓
Establecer en SecurityContext
    ↓
Siguiente filtro
    ↓
Controller
```

---

#### 3. **SecurityConfig** - Configuración de Autorización

```java
@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    
    private final JwtTokenProvider tokenProvider;
    
    // 🎯 Cadena de filtros #1: Swagger/OpenAPI (Público)
    @Bean
    @Order(1)
    public SecurityFilterChain swaggerChain(HttpSecurity http) throws Exception {
        http.securityMatcher(
                "/swagger-ui.html",
                "/swagger-ui/**",
                "/v3/api-docs/**",
                "/api-docs/**")
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
            .csrf(csrf -> csrf.disable());
        return http.build();
    }
    
    // 🎯 Cadena de filtros #2: Autenticación (Público)
    @Bean
    @Order(2)
    public SecurityFilterChain authChain(HttpSecurity http) throws Exception {
        http.securityMatcher("/auth/**")
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
            .csrf(csrf -> csrf.disable());
        return http.build();
    }
    
    // 🎯 Cadena de filtros #3: API (Protegida)
    @Bean
    @Order(3)
    public SecurityFilterChain apiChain(HttpSecurity http) throws Exception {
        JwtAuthenticationFilter jwtFilter = new JwtAuthenticationFilter(tokenProvider);
        
        http.securityMatcher("/api/**")
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/v1/clients/**").hasRole("ADMIN")
                // Solo usuarios con rol ADMIN pueden acceder a /api/v1/clients/**
                .anyRequest().authenticated()
                // Todas las demás rutas requieren autenticación
            )
            .sessionManagement(sm -> sm
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                // No crear sesiones HTTP (stateless)
            )
            .csrf(csrf -> csrf.disable())
            // CSRF deshabilitado porque es una API REST
            .addFilterBefore(
                jwtFilter, 
                UsernamePasswordAuthenticationFilter.class
                // Ejecutar filtro JWT antes de la autenticación estándar
            );
        
        return http.build();
    }
}
```

---

### Flujo Completo de Autenticación

```
┌─────────────────────────────────────────────────────────────────┐
│ 1. LOGIN - Usuario envía credenciales                           │
├─────────────────────────────────────────────────────────────────┤
│ POST /auth/login                                                │
│ {                                                               │
│   "email": "user@email.com",                                    │
│   "password": "password"                                        │
│ }                                                               │
└─────────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────────┐
│ 2. AUTHCONTROLLER - Verifica credenciales                       │
├─────────────────────────────────────────────────────────────────┤
│ ✓ Buscar usuario en BD por email                                │
│ ✓ Validar contraseña                                            │
│ ✓ Verificar que tenga un rol asignado                           │
└─────────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────────┐
│ 3. JWTOKENPROVIDER - Genera token                               │
├─────────────────────────────────────────────────────────────────┤
│ generateToken("user@email.com", ["ROLE_ADMIN"])                 │
│                                                                 │
│ Resultado:                                                      │
│ {                                                               │
│   "alg": "HS256"  ← Header                                      │
│ }                                                               │
│ {                                                               │
│   "sub": "user@email.com",  ← Subject                           │
│   "roles": ["ROLE_ADMIN"],  ← Claims personalizados             │
│   "iat": 1705316445,        ← Issued At                         │
│   "exp": 1705320045         ← Expiration (1 hora después)       │
│ }                                                               │
│ HmacSHA256(header.payload, secret)  ← Signature                 │
└─────────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────────┐
│ 4. RESPONSE - Enviar token al cliente                           │
├─────────────────────────────────────────────────────────────────┤
│ HTTP 200 OK                                                     │
│ {                                                               │
│   "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOi..."                 │
│ }                                                               │
└─────────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────────┐
│ 5. REQUEST AUTENTICADA - Cliente usa el token                   │
├─────────────────────────────────────────────────────────────────┤
│ GET /api/v1/clients                                             │
│ Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOi...        │
└─────────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────────┐
│ 6. JWTAUTHENTICATIONFILTER - Valida token                       │
├─────────────────────────────────────────────────────────────────┤
│ ✓ Extraer token del header                                      │
│ ✓ Validar firma (HMAC-SHA256)                                   │
│ ✓ Verificar que no esté expirado                                │
│ ✓ Extraer username y roles                                      │
│ ✓ Crear Authentication object                                   │
│ ✓ Establecer en SecurityContext                                 │
└─────────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────────┐
│ 7. SECURITYCONFIG - Valida autorización                         │
├─────────────────────────────────────────────────────────────────┤
│ ✓ ¿Usuario tiene rol ADMIN?                                     │
│   SÍ → Permitir acceso                                          │
│   NO → HTTP 403 Forbidden                                       │
└─────────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────────┐
│ 8. CONTROLLER - Procesar petición                               │
├─────────────────────────────────────────────────────────────────┤
│ clientController.listClients()                                  │
└─────────────────────────────────────────────────────────────────┘
```

---

### Matriz de Seguridad

| Ruta | Método | Autenticación | Roles | Descripción |
|------|--------|---------------|-------|------------|
| `/swagger-ui/**` | GET | ❌ No | - | Documentación pública |
| `/v3/api-docs/**` | GET | ❌ No | - | OpenAPI spec pública |
| `/auth/login` | POST | ❌ No | - | Login público |
| `/api/v1/clients` | POST | ✅ Sí | ADMIN | Crear cliente |
| `/api/v1/clients` | GET | ✅ Sí | ADMIN | Listar clientes |
| `/api/v1/clients/{id}` | PUT | ✅ Sí | ADMIN | Actualizar cliente |
| `/api/v1/clients/{id}` | DELETE | ✅ Sí | ADMIN | Eliminar cliente |
| `/api/v1/clients/metrics` | GET | ✅ Sí | ADMIN | Obtener métricas |

---

## 🧪 Testing

El proyecto implementa **testing de múltiples niveles** utilizando diferentes estrategias según la capa.

### Tipos de Tests Implementados

#### 1. **Unit Tests con Mock** - AuthControllerIntegrationTest

```java
@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class AuthControllerIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private UserRepository userRepository;
    
    @MockBean
    private JwtTokenProvider jwtTokenProvider;
    
    // ✅ Test: Login exitoso
    @Test
    void login_withValidCredentials_returnsToken() throws Exception {
        LoginRequest request = new LoginRequest("user@email.com", "password");
        Role role = Role.builder().id(1L).name("ROLE_ADMIN").build();
        User user = User.builder()
            .id(1L)
            .email(request.email())
            .password(request.password())
            .role(role)
            .build();
        
        // Mock del repositorio
        given(userRepository.findByEmail(eq(request.email())))
            .willReturn(Optional.of(user));
        
        // Mock del token provider
        given(jwtTokenProvider.generateToken(eq(request.email()), any()))
            .willReturn("jwt-token");
        
        // Ejecutar y verificar
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token", notNullValue()));
    }
    
    // ✅ Test: Contraseña inválida
    @Test
    void login_withInvalidPassword_returnsUnauthorized() throws Exception {
        LoginRequest request = new LoginRequest("user@email.com", "wrong");
        // ... setup ...
        
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isUnauthorized());
    }
    
    // ✅ Test: Usuario no existe
    @Test
    void login_withUnknownUser_returnsUnauthorized() throws Exception {
        LoginRequest request = new LoginRequest("unknown@email.com", "password");
        
        given(userRepository.findByEmail(eq(request.email())))
            .willReturn(Optional.empty());
        
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isUnauthorized());
    }
}
```

**Características:**
- `@WebMvcTest`: Carga solo el controlador especificado
- `@MockBean`: Reemplaza dependencias con mocks
- `addFilters = false`: Deshabilita filtros de seguridad para testing
- Usa **BDD Mockito**: `given().willReturn()`

---

#### 2. **Integration Tests** - ClientControllerIntegrationTest

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ClientControllerIntegrationTest {
    
    @LocalServerPort
    private int port;
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    private String authToken;
    
    @BeforeEach
    void setUp() {
        authToken = obtainToken();
    }
    
    // ✅ Test: Crear cliente exitosamente
    @Test
    void createClient_withValidData_returnsCreatedClient() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        
        ClientCreateRequest request = new ClientCreateRequest();
        request.setName("John");
        request.setSurname("Doe");
        request.setAge(30);
        request.setBirthDate(LocalDate.of(1994, 12, 17));
        
        HttpEntity<ClientCreateRequest> entity = new HttpEntity<>(request, headers);
        
        ResponseEntity<ClientListResponse> response = restTemplate.exchange(
            url("/api/v1/clients"),
            HttpMethod.POST,
            entity,
            ClientListResponse.class);
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("John");
    }
    
    // ✅ Test: Crear cliente sin autenticación
    @Test
    void createClient_withoutToken_returnsUnauthorized() {
        ClientCreateRequest request = new ClientCreateRequest();
        // ... setup ...
        
        HttpEntity<ClientCreateRequest> entity = new HttpEntity<>(request);
        
        ResponseEntity<String> response = restTemplate.exchange(
            url("/api/v1/clients"),
            HttpMethod.POST,
            entity,
            String.class);
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }
    
    // ✅ Test: Crear cliente con edad que no coincide
    @Test
    void createClient_withInvalidAge_returnsBadRequest() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        
        ClientCreateRequest request = new ClientCreateRequest();
        request.setName("Jane");
        request.setSurname("Doe");
        request.setAge(25);
        request.setBirthDate(LocalDate.of(1990, 1, 1));  // No coincide
        
        HttpEntity<ClientCreateRequest> entity = new HttpEntity<>(request, headers);
        
        ResponseEntity<String> response = restTemplate.exchange(
            url("/api/v1/clients"),
            HttpMethod.POST,
            entity,
            String.class);
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
    
    // ✅ Test: Listar clientes
    @Test
    void listClients_returnsClientList() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        
        ResponseEntity<List<ClientListResponse>> response = restTemplate.exchange(
            url("/api/v1/clients"),
            HttpMethod.GET,
            entity,
            new ParameterizedTypeReference<List<ClientListResponse>>() {});
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }
    
    // ✅ Test: Actualizar cliente
    @Test
    void updateClient_withValidData_returnsUpdatedClient() {
        Long clientId = createTestClient();
        
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        
        ClientUpdateRequest updateRequest = new ClientUpdateRequest();
        updateRequest.setName("Updated Name");
        updateRequest.setSurname("Updated Surname");
        
        HttpEntity<ClientUpdateRequest> entity = new HttpEntity<>(updateRequest, headers);
        
        ResponseEntity<ClientListResponse> response = restTemplate.exchange(
            url("/api/v1/clients/" + clientId),
            HttpMethod.PUT,
            entity,
            ClientListResponse.class);
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getName()).isEqualTo("Updated Name");
    }
    
    // ✅ Test: Eliminar cliente
    @Test
    void deleteClient_withValidId_returnsNoContent() {
        Long clientId = createTestClient();
        
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        
        ResponseEntity<Void> response = restTemplate.exchange(
            url("/api/v1/clients/" + clientId),
            HttpMethod.DELETE,
            entity,
            Void.class);
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
    
    // ✅ Test: Obtener métricas
    @Test
    void getMetrics_returnsMetricsData() {
        createTestClient();
        createAnotherTestClient();
        
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        
        ResponseEntity<MetricsResponse> response = restTemplate.exchange(
            url("/api/v1/clients/metrics"),
            HttpMethod.GET,
            entity,
            MetricsResponse.class);
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getAverageAge()).isNotNull();
        assertThat(response.getBody().getStandardDeviation()).isNotNull();
    }
    
    // Helper: Obtener token de autenticación
    private String obtainToken() {
        ResponseEntity<TokenResponse> response = restTemplate.postForEntity(
            url("/auth/login"),
            new LoginRequest("user@email.com", "password"),
            TokenResponse.class);
        
        return response.getBody().token();
    }
    
    // Helper: Crear cliente de prueba
    private Long createTestClient() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        
        ClientCreateRequest request = new ClientCreateRequest();
        request.setName("Test");
        request.setSurname("Client");
        request.setAge(25);
        request.setBirthDate(LocalDate.of(1999, 12, 17));
        
        HttpEntity<ClientCreateRequest> entity = new HttpEntity<>(request, headers);
        
        ResponseEntity<ClientListResponse> response = restTemplate.exchange(
            url("/api/v1/clients"),
            HttpMethod.POST,
            entity,
            ClientListResponse.class);
        
        return response.getBody().getId();
    }
    
    private String url(String path) {
        return "http://localhost:" + port + path;
    }
}
```

**Características:**
- `@SpringBootTest`: Carga el contexto completo de Spring
- `RANDOM_PORT`: Usa un puerto aleatorio para no conflictos
- `TestRestTemplate`: Cliente HTTP para hacer peticiones reales
- Tests end-to-end: verifica autenticación, seguridad y lógica completa
- Usa **AssertJ**: `assertThat().isEqualTo()`

---

#### 3. **Context Loading Test**

```java
@SpringBootTest
@ActiveProfiles("test")
class PruebaTecnicaSeekApplicationTests {
    
    // ✅ Test: El contexto de Spring carga correctamente
    @Test
    void contextLoads() {
        // Si el contexto no carga, este test falla
    }
}
```

---

### Comparación de Tipos de Tests

| Aspecto | Unit (Mock) | Integration |
|--------|-----------|-------------|
| **Scope** | Controlador solo | Contexto completo |
| **Dependencias** | Mocks | Reales |
| **BD** | No | H2 en memoria |
| **Velocidad** | ⚡ Muy rápido | 🐢 Más lento |
| **Casos** | Lógica unitaria | Flujos completos |
| **Seguridad** | Deshabilitada | Habilitada |
| **Herramientas** | MockMvc, Mockito | TestRestTemplate, AssertJ |

---

### Configuración de Testing

**application-test.properties:**
```properties
# Base de datos H2 en memoria
spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# JPA - No crear tablas automáticamente
spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=false

# Flyway - Ejecutar migraciones
spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration

# Puerto aleatorio
server.port=0
```

---

### Ejecución de Tests

```bash
# Ejecutar todos los tests
mvn test

# Ejecutar solo tests unitarios
mvn test -Dtest=AuthControllerIntegrationTest

# Ejecutar solo tests de integración
mvn test -Dtest=ClientControllerIntegrationTest

# Ejecutar con reporte de coverage
mvn test jacoco:report
```

---

## 🚀 Instalación y Ejecución

### Requisitos Previos
- Java 17+
- Maven 3.9.11+
- MySQL 8.x (opcional, por defecto usa H2 en testing)

### Pasos de Instalación

1. **Clonar el repositorio**
```bash
git clone https://github.com/RenatoNino/spring-boot-technical-test-seek.git
cd prueba-tecnica-seek
```

2. **Configurar variables de entorno** (opcional)
```bash
export DB_USERNAME=remote
export DB_PASSWORD=remote
export JWT_SECRET=spring-boot-technical-test-with-mysql-for-backend-developer
export JWT_EXPIRATION=3600
```

3. **Compilar el proyecto**
```bash
mvn clean install
```

4. **Ejecutar la aplicación**
```bash
mvn spring-boot:run
```

5. **Acceder a la aplicación**
- API: http://localhost:8081
- Swagger: http://localhost:8081/swagger-ui.html
- H2 Console: http://localhost:8081/h2-console

6. **Ejecutar tests**
```bash
mvn test
```

---

## 📊 Estructura de Base de Datos

### Diagrama ER

```sql
┌──────────────┐         ┌──────────────┐
│    ROLE      │         │     USER     │
├──────────────┤         ├──────────────┤
│ id (PK)      │◄────┐   │ id (PK)      │
│ name         │     │   │ email        │
│ alias        │     └───│ role_id (FK) │
└──────────────┘         │ password     │
                         │ created_at   │
                         │ updated_at   │
                         │ deleted_at   │
                         └──────────────┘

┌──────────────────┐
│     CLIENT       │
├──────────────────┤
│ id (PK)          │
│ name             │
│ surname          │
│ age              │
│ birth_date       │
│ created_at       │
│ updated_at       │
│ deleted_at       │
└──────────────────┘
```

### Migraciones Flyway

| Versión | Script | Descripción |
|---------|--------|------------|
| V1 | `V1__create_table_client.sql` | Crear tabla client |
| V2 | `V2__add_audit_columns.sql` | Agregar columnas de auditoría |
| V3 | `V3__create_table_role.sql` | Crear tabla role |
| V4 | `V4__create_table_user.sql` | Crear tabla user |

---

## 📝 Notas Adicionales

### Soft Delete
La aplicación implementa **soft delete** (eliminación lógica). Cuando se elimina un registro, no se borra de la BD, se marca con un timestamp en `deleted_at`. Las consultas automaticamente filtran registros eliminados.

```java
@Entity
@SQLDelete(sql = "UPDATE client SET deleted_at = NOW() WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
public class Client { ... }
```

### Auditoría Automática
Todas las entidades heredan de `AuditableEntity` que automáticamente registra:
- `created_at`: Fecha de creación
- `updated_at`: Fecha de última modificación

```java
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditableEntity {
    @CreatedDate
    private Instant createdAt;
    
    @LastModifiedDate
    private Instant updatedAt;
}
```

### Métricas con Micrometer
La aplicación expone métricas en `/actuator/metrics` y Prometheus en `/actuator/prometheus`:

```properties
management.endpoints.web.exposure.include=health,metrics,prometheus
```

---

## 👨‍💻 Autor
**Renato Niño** - Analista Desarrollador  
📧 jorgenino07@gmail.com

---

## 📄 Licencia
Este proyecto es de código abierto.