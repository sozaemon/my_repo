# CODEBUDDY.md

This file provides guidance to CodeBuddy Code when working with code in this repository.

## Build and test commands

Java 21 and the Maven wrapper are required. The parent POM is `spring-boot-starter-parent:4.1.2-SNAPSHOT`, resolved from `https://repo.spring.io/snapshot`.

```bash
./mvnw spring-boot:run            # run locally (expects PostgreSQL at 127.0.0.1:5432)
./mvnw test                       # run all tests
./mvnw clean test                 # clean build + tests
./mvnw clean package              # build target/*.jar
./mvnw test -Dtest=RoleAccessServiceTest
./mvnw test -Dtest=RoleAccessServiceTest#getOneRoleAccessReturnMappedRoleAccessFound
docker compose up --build         # postgres + liquibase migration + app
```

There is no linter or formatter plugin configured in the build.

Tests that start a Spring context run against in-memory H2: `src/test/resources/application.properties` overrides the datasource, sets `ddl-auto=create-drop`, and disables Liquibase. Test styles in use:

- `@DataJpaTest` repository tests against H2.
- `@ExtendWith(MockitoExtension.class)` service tests with mocked repositories/mappers.
- Standalone `MockMvc` controller tests (`MockMvcBuilders.standaloneSetup(...).setControllerAdvice(new AdviceController())`).

## Architecture

### Layering

Resources generally flow through three model shapes, with MapStruct mappers between the hops:

```
controllers/httpmodel/*   (request/response JSON)
        ↕  MapStruct mapper (mapper/*.java, componentModel = "spring")
domain/dto/*              (service-layer DTOs; also carry filter/join metadata)
        ↕  MapStruct mapper
model/*                   (JPA entities, extend BasicModel)
        ↕
repository/*              (JpaRepository + JpaSpecificationExecutor)
```

Controllers depend on `*ServiceDomain` interfaces located in `com.arif.hrs.domain.service` (despite the package name, these are service interfaces) plus MapStruct mappers; `AuthController` additionally uses `AuthenticationManager` and `CookieComponent`. Implementations live in `com.arif.hrs.service`. Most services implement `CommonServiceDomain<T, K>`, which defines `getOne`, `create`, `update`, `delete`, `paginate`. All service implementations except `UploadHeaderServiceImpl` are annotated `@Transactional(rollbackOn = Exception.class)` from `jakarta.transaction`.

Controller responses use the `ResponseModel<T>` envelope (`responseTime`, `data`, `message`, `error`). Most controllers build it with `ControllerUtil.createSuccessResponse`; `AccessController`, `AuthController`, `ShiftController`, and parts of `UserController` construct `ResponseModel` directly. `AdviceController` maps `NoResourceFoundException` → 404, `MethodArgumentNotValidException` → 400, and all other exceptions → 500.

### Pagination and filtering

All `POST /list` endpoints accept `PaginationRequest` (`page`, `pageSize`, `filter[]`, `sortBy`, `sortDirection`) and convert it to `PaginationDto`. Note the page conversion in `PaginationDto.getPage()`: callers send 1-based pages, `0` is also accepted as the first page, and every positive value is decremented before reaching Spring Data.

Services build queries with:

```java
CommonImpl.buildFilterSpecificationDto(paginationRequest.getFilters(), XxxDto.class);
CommonImpl.buildPaginationPage(paginationRequest, "id", XxxDto.class);
```

Filter/join behavior is driven by annotations on the DTO, not by the entity:

- `FilterValidator` reflects over the DTO's `@SpecificationMapping(joinTable, targetColumn, joinType)` fields.
- `SpecificationBuilder` turns filters into predicates for `EQ`, `NEQ`, `LIKE`, `GT`, `LT`, `IN`, `NOTIN`, `NOTNULL`, `BETWEEN` (`SpecificationEnum`), combining with `AND`/`OR` from `joinOperator` and adding fetch joins from the mapping.
- To make a joined field filterable or sortable, add it to the DTO with `@SpecificationMapping` (see `RoleAccessDto`). The hand-written specifications in `repository/specification/specifications/` are currently used only for fixed queries (`AccessSpecification`, `RoleAccessSpecification`).

The older `buildPaginationSpecification` / two-arg `buildPaginationPage` overloads are deprecated; use the DTO-aware versions.

### Security and dynamic authorization

Stateless JWT security, configured in `configuration/SecurityConfiguration`:

- `AuthenticationFilter` extracts the token from the `hrs-jwt-authentication-key` cookie first, then from the `Authorization` header (strips optional `Bearer`). On `/api/auth/refresh-token` it prefers the `hrs-jwt-authentication-key-refresh` cookie instead.
- The role in the JWT claim `claims_role` is installed as the single `GrantedAuthority` on the security context. `AuthUser` separately derives its authorities from `RoleModel.name` when built from a `UserModel`.
- `DynamicAuthorizationManager` authorizes every non-whitelisted request by calling `AccessServiceDomain.getAccessByRolesAndPathAndMethod(roles, httpMethod)`, which joins `sys_access` → `sys_role_access` → `sys_role` and matches the role's `name` (not `code`). The returned paths are matched against the request URI with `AntPathMatcher`.
- Consequence: a new protected endpoint requires `sys_access` and `sys_role_access` seed rows (via a Liquibase changeset), or it will return 403 even for an authenticated admin.
- Public-path handling is spread across three lists that are similar but not identical: `SecurityConfiguration.securityFilterChain` (`permitAll`), `AuthenticationFilter.shouldNotFilter`, and the skip list at the top of `DynamicAuthorizationManager.authorize`. The dynamic manager additionally bypasses `/api/auth/refresh-token` and the user self-service routes `/api/user`, `/api/user/user-roles`, `/api/user/update-password`. Adding a public route means updating the `permitAll` matchers and `shouldNotFilter`, plus the dynamic manager's skip list if it must bypass access checks.
- Login (`POST /api/auth/login`) requires `userName`, `password`, and `role`; the requested role must match one of the user's role names. Both access and refresh tokens are also written as httpOnly/secure cookies (`CookieComponent`, settings under `app.cookie.*`).
- CORS origins come from `app.allowed.cors`. OpenAPI/Swagger is configured at `/api-docs` and `/swagger-ui/index.html`, but only those exact strings are whitelisted — assets under `/swagger-ui/**` and `/api-docs/**` are not matched by the `permitAll` rules.

### Persistence

- PostgreSQL with `spring.jpa.hibernate.ddl-auto=none`; the schema is owned by Liquibase.
- Changelogs live in `src/main/resources/changelog/`. The master file is `db.changelog.master.xml`; individual changesets live in `changes/`, are typically named `yyyyMMddHHmm_description.xml` (the initial schema is `00000_initial.changelog.xml`), and must be added to the master's `<include>` list.
- Liquibase is disabled in the checked-in `application.properties` (`spring.liquibase.enabled=false`). Also, `spring.liquibase.change-log` points at `classpath:changelog/db.changelog-master.xml`, which does not match the actual file name `db.changelog.master.xml`; fix this if enabling Spring-run migrations. `docker-compose.yml` runs Liquibase as a separate one-shot container instead.
- The naming strategy is `PhysicalNamingStrategyStandardImpl`, so there is no camelCase → snake_case conversion. Column and table names are exactly what `@Column`/`@Table` declare (for example `updatedBy`, `modulename`).
- `BasicModel` is a `@MappedSuperclass` supplying `created`, `updated`, `createdBy`, `updatedBy`, and `uuid`; `@PrePersist`/`@PreUpdate` populate them from `SecurityContextHolder`, so `createdBy`/`updatedBy` are empty strings when there is no authenticated principal.
- Table prefixes: `sys_` for users/roles/access/permissions, `r_` for reference data (shift types, tolerances), `t_` for transactional data (shift), `u_` for upload staging.

### Excel import/export

- Export: implement/instantiate `ExcelBuilder<T>` with an excel model class annotated with `@ExcelProperty`, then call `ExcelGenerator.createExcel(...)`. `ExcelBuilderXlsxService` / `ExcelBuilderXlsService` are the concrete workbook implementations.
- Import: subclass `ExcelReader<T>` and implement `mapToResult(Map<String, Object>)`; `parseWorkbook(file, headers, startDataRow, sheetIndex)` returns the mapped rows.
- Role-access upload is a staged flow: `POST /api/role-access/upload` parses rows into `u_role_access` with `UploadStatusEnum.VALID`/`NOTVALID`, `PATCH /api/role-access/upload/update` fixes invalid rows, and `POST /api/role-access/upload/process/{headerId}` creates `sys_access` + `sys_role_access` rows and marks the `u_upload_header` record `COMPLETE`. `RoleAccessUploadRepository` contains a native query that flags rows duplicating existing `sys_access` path+method pairs.

## API areas

| Base path | Controller | Notes |
| --- | --- | --- |
| `/api/auth` | `AuthController` | `login`, `logout`, `refresh-token`, `user-roles` (pre-auth) |
| `/api/user` | `UserController` | CRUD, `update-password`, `assign-role`, `user-roles` |
| `/api/role` | `RoleController` | CRUD |
| `/api/access` | `AccessController` | CRUD + `/excel` export |
| `/api/role-access` | `RoleAccessController` | CRUD, `assign-roles`, upload/template/process |
| `/api/permission` | `PermissionController` | CRUD |
| `/api/shift` | `ShiftController`, `ShiftTypeController`, `ShiftToleranceController` | shift, `/shift-type`, `/shift-tolerance` |
| `/api/upload` | `UploadController` | paginated upload headers |

## Conventions and gotchas

- MapStruct mappers are interfaces annotated `@Mapper(componentModel = "spring")`. Some declare explicit `@Mapping` entries; others (`PermissionMapper`, `ShiftToleranceMapper`, `UploadHeaderMapper`) rely on name-based mapping. Generated implementations land in `target/generated-sources/annotations`. The compiler plugin orders Lombok, `lombok-mapstruct-binding`, then `mapstruct-processor` — keep that order, and rebuild after adding or changing a mapper.
- Validation groups live in `ValidationGroups` (`Create`, `Update`, `Login`, `LoginWithRole`); controllers select them with `@Validated(ValidationGroups.X.class)`.
- Multipart uploads are capped at 5 MB (`spring.servlet.multipart.*`).
- Local defaults: database `db_local`, user `user_local`, password `password123`, PostgreSQL at `127.0.0.1:5432`. Docker Compose uses `mydb`/`myuser`/`mypassword`, publishes PostgreSQL on host port 5433, and injects datasource settings via `SPRING_DATASOURCE_*` environment variables.
- `spring.docker.compose.enabled=false` prevents Spring Boot from trying to manage Compose itself.
