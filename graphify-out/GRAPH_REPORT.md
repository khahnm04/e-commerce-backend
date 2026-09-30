# Graph Report - e-commerce-backend  (2026-09-30)

## Corpus Check
- 118 files · ~12,850 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 7 file(s) not represented in the graph (top: .properties 3, (none) 2, .example 1)

## Summary
- 747 nodes · 2632 edges · 26 communities (13 shown, 13 thin omitted)
- Extraction: 94% EXTRACTED · 6% INFERRED · 0% AMBIGUOUS · INFERRED: 148 edges (avg confidence: 0.8)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `af63602a`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

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

## Communities (26 total, 13 thin omitted)

### Community 0 - "Service & Business Logic"
Cohesion: 0.12
Nodes (8): AccountRepository, AccountRoleRepository, PermissionRepository, RolePermissionRepository, AdminProfileRepository, UserProfileRepository, PermissionCacheService, RolePermissionServiceImpl

### Community 1 - "Controller & HTTP Layer"
Cohesion: 0.08
Nodes (23): AccountStatus, ACTIVE, BANNED, INACTIVE, LOCKED, AdminController, AuthController, PermissionController (+15 more)

### Community 2 - "DTO & Validation"
Cohesion: 0.15
Nodes (7): GenderType, FEMALE, MALE, OTHER, AdminCreationRequest, AdminUpdateRequest, MyProfileAdminRequest

### Community 3 - "JPA Specification & Filtering"
Cohesion: 0.05
Nodes (16): MyProfileAdminResponse, Account, Role, AppException, AccountMapper, AdminMapper, UserMapper, RoleRepository (+8 more)

### Community 4 - "JWT Authentication & Security"
Cohesion: 0.06
Nodes (15): LoginRequest, LogoutRequest, RefreshTokenRequest, LoginResponse, PermissionResponse, RefreshTokenResponse, TokenResponse, PermissionMapper (+7 more)

### Community 5 - "JPA Entity & Auditing"
Cohesion: 0.16
Nodes (10): CloudflareR2Properties, JwtProperties, ChangePasswordRequest, AccountRole, Permission, RolePermission, BaseEntity, AdminProfile (+2 more)

### Community 6 - "Role Management"
Cohesion: 0.12
Nodes (7): RoleRequest, RoleResponse, Meta, RoleMapper, RoleServiceImpl, RolePermissionService, RoleService

### Community 7 - "Custom Validation Annotations"
Cohesion: 0.10
Nodes (6): EmailValidator, ValidEmail, PasswordValidator, ValidPassword, PhoneValidator, ValidPhone

### Community 8 - "Spring Security & Auth Filters"
Cohesion: 0.06
Nodes (6): AuditorAwareConfig, InstantConverter, GlobalExceptionHandler, JwtAccessDeniedHandler, JwtAuthenticationConverterConfig, JwtAuthenticationEntryPoint

### Community 10 - "Error Codes & App Exceptions"
Cohesion: 0.10
Nodes (17): ErrorCode, ACCOUNT_EXISTED, ACCOUNT_NOT_FOUND, FORBIDDEN, INVALID_INSTANT_FORMAT, LOGIN_FAILED, NEW_PASSWORD_SAME_AS_OLD, OLD_PASSWORD_INCORRECT (+9 more)

### Community 11 - "Website Info Module"
Cohesion: 0.14
Nodes (7): WebsiteInfoRequest, WebsiteInfoResponse, WebsiteInfoMapper, WebsiteInfoRepository, WebsiteInfoServiceImpl, WebsiteInfoService, PageableUtil

### Community 13 - "S3 Presigned Storage"
Cohesion: 0.19
Nodes (3): PresignedResponse, R2StorageServiceImpl, StorageService

### Community 24 - "Auth Provider Constants"
Cohesion: 0.50
Nodes (3): AuthProvider, GITHUB, GOOGLE

## Knowledge Gaps
- **28 isolated node(s):** `com.khahnm04:e-commerce`, `ACTIVE`, `INACTIVE`, `BANNED`, `LOCKED` (+23 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 116 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **13 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `Account` connect `JPA Specification & Filtering` to `Service & Business Logic`, `Controller & HTTP Layer`, `DTO & Validation`, `JWT Authentication & Security`, `JPA Entity & Auditing`, `App Initialization & PasswordEncoder`?**
  _High betweenness centrality (0.067) - this node is a cross-community bridge._
- **Why does `ErrorCode` connect `Error Codes & App Exceptions` to `JPA Specification & Filtering`, `JWT Authentication & Security`, `JPA Entity & Auditing`, `Role Management`, `Spring Security & Auth Filters`, `Website Info Module`?**
  _High betweenness centrality (0.050) - this node is a cross-community bridge._
- **Why does `Role` connect `JPA Specification & Filtering` to `Service & Business Logic`, `DTO & Validation`, `JWT Authentication & Security`, `JPA Entity & Auditing`, `Role Management`, `App Initialization & PasswordEncoder`?**
  _High betweenness centrality (0.042) - this node is a cross-community bridge._
- **What connects `com.khahnm04:e-commerce`, `ACTIVE`, `INACTIVE` to the rest of the system?**
  _28 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `Service & Business Logic` be split into smaller, more focused modules?**
  _Cohesion score 0.11794871794871795 - nodes in this community are weakly interconnected._
- **Should `Controller & HTTP Layer` be split into smaller, more focused modules?**
  _Cohesion score 0.07606338615512928 - nodes in this community are weakly interconnected._
- **Should `DTO & Validation` be split into smaller, more focused modules?**
  _Cohesion score 0.14516129032258066 - nodes in this community are weakly interconnected._