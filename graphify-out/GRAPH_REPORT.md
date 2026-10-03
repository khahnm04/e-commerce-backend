# Graph Report - e-commerce-backend  (2026-10-03)

## Corpus Check
- 131 files · ~14,438 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 7 file(s) not represented in the graph (top: .properties 3, (none) 2, .example 1)

## Summary
- 822 nodes · 3013 edges · 26 communities (13 shown, 13 thin omitted)
- Extraction: 94% EXTRACTED · 6% INFERRED · 0% AMBIGUOUS · INFERRED: 173 edges (avg confidence: 0.8)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `a635294a`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- AdminServiceImpl.java
- lombok.RequiredArgsConstructor
- Brand
- Account
- JwtServiceImpl.java
- lombok.Builder
- Role
- Custom Validation Annotations
- org.springframework.stereotype.Component
- Exception Handling & i18n
- ErrorCode
- WebsiteInfo
- RedisConfig.java
- S3 Presigned Storage
- CORS & Security Config
- CloudflareR2Config.java
- org.springframework.context.annotation.Bean
- OpenAPI / Swagger Config
- Permission Management
- org.springframework.context.annotation.Configuration
- Spring Boot Entry Point
- Unit Tests
- Auth Provider Constants
- Maven Project Root

## God Nodes (most connected - your core abstractions)
1. `Account` - 68 edges
2. `Role` - 53 edges
3. `ApiResponse` - 51 edges
4. `AppException` - 42 edges
5. `AccountStatus` - 37 edges
6. `ErrorCode` - 36 edges
7. `Brand` - 31 edges
8. `AdminServiceImpl` - 30 edges
9. `Permission` - 28 edges
10. `BulkActionRequest` - 26 edges

## Surprising Connections (you probably didn't know these)
- `CloudflareR2Config` --references--> `CloudflareR2Properties`  [EXTRACTED]
  src/main/java/com/khahnm18/ecommercebackend/config/CloudflareR2Config.java → src/main/java/com/khahnm18/ecommercebackend/config/properties/CloudflareR2Properties.java
- `R2StorageServiceImpl` --references--> `CloudflareR2Properties`  [EXTRACTED]
  src/main/java/com/khahnm18/ecommercebackend/service/storage/impl/R2StorageServiceImpl.java → src/main/java/com/khahnm18/ecommercebackend/config/properties/CloudflareR2Properties.java
- `JwtServiceImpl` --references--> `JwtProperties`  [EXTRACTED]
  src/main/java/com/khahnm18/ecommercebackend/service/auth/impl/JwtServiceImpl.java → src/main/java/com/khahnm18/ecommercebackend/config/properties/JwtProperties.java
- `Account` --references--> `AccountStatus`  [EXTRACTED]
  src/main/java/com/khahnm18/ecommercebackend/entity/auth/Account.java → src/main/java/com/khahnm18/ecommercebackend/constant/auth/AccountStatus.java
- `AuthController` --references--> `AuthService`  [EXTRACTED]
  src/main/java/com/khahnm18/ecommercebackend/controller/AuthController.java → src/main/java/com/khahnm18/ecommercebackend/service/auth/AuthService.java

## Import Cycles
- None detected.

## Communities (26 total, 13 thin omitted)

### Community 0 - "AdminServiceImpl.java"
Cohesion: 0.08
Nodes (9): AuditorAwareConfig, AccountRepository, AccountRoleRepository, RolePermissionRepository, RoleRepository, AdminProfileRepository, UserProfileRepository, PageableUtil (+1 more)

### Community 1 - "lombok.RequiredArgsConstructor"
Cohesion: 0.09
Nodes (15): AdminController, AuthController, BrandController, PermissionController, R2StorageController, RoleController, UserController, WebsiteInfoController (+7 more)

### Community 2 - "Brand"
Cohesion: 0.10
Nodes (12): BrandStatus, ACTIVE, INACTIVE, BrandCreationRequest, BrandUpdateRequest, BrandResponse, Brand, BrandMapper (+4 more)

### Community 3 - "Account"
Cohesion: 0.07
Nodes (9): Account, AppException, AccountMapper, AdminMapper, UserMapper, AdminServiceImpl, UserServiceImpl, AdminSpecification (+1 more)

### Community 4 - "JwtServiceImpl.java"
Cohesion: 0.08
Nodes (11): LoginRequest, LogoutRequest, RefreshTokenRequest, LoginResponse, RefreshTokenResponse, TokenResponse, AuthService, AuthServiceImpl (+3 more)

### Community 5 - "lombok.Builder"
Cohesion: 0.07
Nodes (25): AccountStatus, ACTIVE, BANNED, INACTIVE, LOCKED, GenderType, FEMALE, MALE (+17 more)

### Community 6 - "Role"
Cohesion: 0.06
Nodes (17): RoleRequest, PermissionResponse, RoleResponse, Role, PermissionMapper, RoleMapper, PermissionRepository, AccountRoleService (+9 more)

### Community 7 - "Custom Validation Annotations"
Cohesion: 0.10
Nodes (6): EmailValidator, ValidEmail, PasswordValidator, ValidPassword, PhoneValidator, ValidPhone

### Community 8 - "org.springframework.stereotype.Component"
Cohesion: 0.05
Nodes (8): InstantConverter, CloudflareR2Properties, JwtProperties, GlobalExceptionHandler, JwtAccessDeniedHandler, JwtAuthenticationConverterConfig, JwtAuthenticationEntryPoint, JwtDecoderConfig

### Community 10 - "ErrorCode"
Cohesion: 0.09
Nodes (19): ErrorCode, ACCOUNT_EXISTED, ACCOUNT_NOT_FOUND, BRAND_EXISTED, BRAND_NOT_FOUND, FORBIDDEN, INVALID_INSTANT_FORMAT, LOGIN_FAILED (+11 more)

### Community 11 - "WebsiteInfo"
Cohesion: 0.22
Nodes (7): WebsiteInfoRequest, WebsiteInfoResponse, WebsiteInfo, WebsiteInfoMapper, WebsiteInfoRepository, WebsiteInfoServiceImpl, WebsiteInfoService

### Community 13 - "S3 Presigned Storage"
Cohesion: 0.19
Nodes (3): PresignedResponse, R2StorageServiceImpl, StorageService

### Community 24 - "Auth Provider Constants"
Cohesion: 0.50
Nodes (3): AuthProvider, GITHUB, GOOGLE

## Knowledge Gaps
- **32 isolated node(s):** `com.khahnm04:e-commerce`, `ACTIVE`, `INACTIVE`, `BANNED`, `LOCKED` (+27 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 123 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **13 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `Account` connect `Account` to `AdminServiceImpl.java`, `JwtServiceImpl.java`, `lombok.Builder`, `Role`?**
  _High betweenness centrality (0.061) - this node is a cross-community bridge._
- **Why does `ErrorCode` connect `ErrorCode` to `AdminServiceImpl.java`, `Brand`, `Account`, `JwtServiceImpl.java`, `lombok.Builder`, `Role`, `org.springframework.stereotype.Component`, `WebsiteInfo`?**
  _High betweenness centrality (0.050) - this node is a cross-community bridge._
- **Why does `AppException` connect `Account` to `AdminServiceImpl.java`, `Brand`, `JwtServiceImpl.java`, `lombok.Builder`, `Role`, `org.springframework.stereotype.Component`, `ErrorCode`, `WebsiteInfo`?**
  _High betweenness centrality (0.041) - this node is a cross-community bridge._
- **What connects `com.khahnm04:e-commerce`, `ACTIVE`, `INACTIVE` to the rest of the system?**
  _32 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `AdminServiceImpl.java` be split into smaller, more focused modules?**
  _Cohesion score 0.08143839238498149 - nodes in this community are weakly interconnected._
- **Should `lombok.RequiredArgsConstructor` be split into smaller, more focused modules?**
  _Cohesion score 0.09461260232248239 - nodes in this community are weakly interconnected._
- **Should `Brand` be split into smaller, more focused modules?**
  _Cohesion score 0.0962962962962963 - nodes in this community are weakly interconnected._