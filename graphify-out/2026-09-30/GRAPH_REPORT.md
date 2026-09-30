# Graph Report - e-commerce-backend  (2026-09-30)

## Corpus Check
- Corpus is ~12,850 words - fits in a single context window. You may not need a graph.

## Summary
- 743 nodes · 2630 edges · 26 communities (15 shown, 11 thin omitted)
- Extraction: 94% EXTRACTED · 6% INFERRED · 0% AMBIGUOUS · INFERRED: 148 edges (avg confidence: 0.8)
- Token cost: 0 input · 0 output

## Community Hubs (Navigation)
- Service & Business Logic
- Controller & HTTP Layer
- DTO & Validation
- JPA Specification & Filtering
- JWT Authentication & Security
- JPA Entity & Auditing
- Role Management
- Custom Validation Annotations
- Spring Security & Auth Filters
- Exception Handling & i18n
- Error Codes & App Exceptions
- Website Info Module
- Redis Cache Configuration
- S3 Presigned Storage
- CORS & Security Config
- AWS S3 Client Config
- i18n & Locale Config
- OpenAPI / Swagger Config
- JWT Decoder & Token Config
- Permission Management
- App Initialization & PasswordEncoder
- Spring Boot Entry Point
- Unit Tests
- Auth Provider Constants
- Maven Project Root

## God Nodes (most connected - your core abstractions)
1. `Account` - 66 edges
2. `Role` - 51 edges
3. `ApiResponse` - 43 edges
4. `AccountStatus` - 37 edges
5. `AppException` - 37 edges
6. `ErrorCode` - 33 edges
7. `AdminServiceImpl` - 30 edges
8. `Permission` - 28 edges
9. `GenderType` - 25 edges
10. `RoleResponse` - 25 edges

## Surprising Connections (you probably didn't know these)
- `CloudflareR2Config` --references--> `CloudflareR2Properties`  [EXTRACTED]
  src/main/java/com/khahnm18/ecommercebackend/config/CloudflareR2Config.java → src/main/java/com/khahnm18/ecommercebackend/config/properties/CloudflareR2Properties.java
- `R2StorageServiceImpl` --references--> `CloudflareR2Properties`  [EXTRACTED]
  src/main/java/com/khahnm18/ecommercebackend/service/storage/impl/R2StorageServiceImpl.java → src/main/java/com/khahnm18/ecommercebackend/config/properties/CloudflareR2Properties.java
- `JwtServiceImpl` --references--> `JwtProperties`  [EXTRACTED]
  src/main/java/com/khahnm18/ecommercebackend/service/auth/impl/JwtServiceImpl.java → src/main/java/com/khahnm18/ecommercebackend/config/properties/JwtProperties.java
- `AdminCreationRequest` --references--> `AccountStatus`  [EXTRACTED]
  src/main/java/com/khahnm18/ecommercebackend/dto/request/user/AdminCreationRequest.java → src/main/java/com/khahnm18/ecommercebackend/constant/auth/AccountStatus.java
- `AdminUpdateRequest` --references--> `AccountStatus`  [EXTRACTED]
  src/main/java/com/khahnm18/ecommercebackend/dto/request/user/AdminUpdateRequest.java → src/main/java/com/khahnm18/ecommercebackend/constant/auth/AccountStatus.java

## Import Cycles
- None detected.

## Communities (26 total, 11 thin omitted)

### Community 0 - "Service & Business Logic"
Cohesion: 0.07
Nodes (15): AccountRepository, AccountRoleRepository, PermissionRepository, RolePermissionRepository, RoleRepository, AdminProfileRepository, UserProfileRepository, WebsiteInfoRepository (+7 more)

### Community 1 - "Controller & HTTP Layer"
Cohesion: 0.13
Nodes (11): AdminController, AuthController, PermissionController, R2StorageController, RoleController, UserController, WebsiteInfoController, BulkActionRequest (+3 more)

### Community 2 - "DTO & Validation"
Cohesion: 0.07
Nodes (14): GenderType, FEMALE, MALE, OTHER, AdminCreationRequest, AdminUpdateRequest, ChangePasswordRequest, MyProfileAdminRequest (+6 more)

### Community 3 - "JPA Specification & Filtering"
Cohesion: 0.08
Nodes (15): AccountStatus, ACTIVE, BANNED, INACTIVE, LOCKED, UserCreationRequest, UserUpdateRequest, UserResponse (+7 more)

### Community 4 - "JWT Authentication & Security"
Cohesion: 0.06
Nodes (13): LoginRequest, LogoutRequest, RefreshTokenRequest, LoginResponse, RefreshTokenResponse, TokenResponse, Meta, AppException (+5 more)

### Community 5 - "JPA Entity & Auditing"
Cohesion: 0.14
Nodes (10): CloudflareR2Properties, JwtProperties, AccountRole, Permission, Role, RolePermission, BaseEntity, AdminProfile (+2 more)

### Community 6 - "Role Management"
Cohesion: 0.10
Nodes (6): RoleRequest, RoleResponse, RoleMapper, RoleServiceImpl, RoleService, RoleSpecification

### Community 7 - "Custom Validation Annotations"
Cohesion: 0.10
Nodes (6): EmailValidator, ValidEmail, PasswordValidator, ValidPassword, PhoneValidator, ValidPhone

### Community 8 - "Spring Security & Auth Filters"
Cohesion: 0.08
Nodes (3): AuditorAwareConfig, InstantConverter, JwtAuthenticationConverterConfig

### Community 9 - "Exception Handling & i18n"
Cohesion: 0.14
Nodes (3): GlobalExceptionHandler, JwtAccessDeniedHandler, JwtAuthenticationEntryPoint

### Community 10 - "Error Codes & App Exceptions"
Cohesion: 0.10
Nodes (17): ErrorCode, ACCOUNT_EXISTED, ACCOUNT_NOT_FOUND, FORBIDDEN, INVALID_INSTANT_FORMAT, LOGIN_FAILED, NEW_PASSWORD_SAME_AS_OLD, OLD_PASSWORD_INCORRECT (+9 more)

### Community 11 - "Website Info Module"
Cohesion: 0.21
Nodes (5): WebsiteInfoRequest, WebsiteInfoResponse, WebsiteInfoMapper, WebsiteInfoServiceImpl, WebsiteInfoService

### Community 13 - "S3 Presigned Storage"
Cohesion: 0.19
Nodes (3): PresignedResponse, R2StorageServiceImpl, StorageService

### Community 19 - "Permission Management"
Cohesion: 0.31
Nodes (4): PermissionResponse, PermissionMapper, PermissionServiceImpl, PermissionService

### Community 24 - "Auth Provider Constants"
Cohesion: 0.50
Nodes (3): AuthProvider, GITHUB, GOOGLE

## Knowledge Gaps
- **26 isolated node(s):** `com.khahnm04:e-commerce`, `ACTIVE`, `INACTIVE`, `BANNED`, `LOCKED` (+21 more)
  These have ≤1 connection - possible missing edges. (Counts symbols only; 112 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **11 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `Account` connect `JPA Specification & Filtering` to `Service & Business Logic`, `DTO & Validation`, `JWT Authentication & Security`, `JPA Entity & Auditing`, `App Initialization & PasswordEncoder`?**
  _High betweenness centrality (0.067) - this node is a cross-community bridge._
- **Why does `ErrorCode` connect `Error Codes & App Exceptions` to `Service & Business Logic`, `JWT Authentication & Security`, `JPA Entity & Auditing`, `Spring Security & Auth Filters`, `Exception Handling & i18n`?**
  _High betweenness centrality (0.051) - this node is a cross-community bridge._
- **Why does `Role` connect `JPA Entity & Auditing` to `Service & Business Logic`, `DTO & Validation`, `JPA Specification & Filtering`, `JWT Authentication & Security`, `Role Management`, `App Initialization & PasswordEncoder`?**
  _High betweenness centrality (0.043) - this node is a cross-community bridge._
- **What connects `com.khahnm04:e-commerce`, `ACTIVE`, `INACTIVE` to the rest of the system?**
  _26 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `Service & Business Logic` be split into smaller, more focused modules?**
  _Cohesion score 0.0673616680032077 - nodes in this community are weakly interconnected._
- **Should `Controller & HTTP Layer` be split into smaller, more focused modules?**
  _Cohesion score 0.1281613123718387 - nodes in this community are weakly interconnected._
- **Should `DTO & Validation` be split into smaller, more focused modules?**
  _Cohesion score 0.06915113871635611 - nodes in this community are weakly interconnected._