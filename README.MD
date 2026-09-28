# HRS

HRS is a Spring Boot application for managing users, roles, access permissions, and shift configuration. The application exposes a REST API and persists data in PostgreSQL.

## Technology Stack

- Java 21
- Spring Boot 4.1.2-SNAPSHOT
- Spring Web MVC
- Spring Data JPA and Hibernate
- Spring Security with JWT and OAuth2 client support
- PostgreSQL
- Liquibase database migrations
- MapStruct and Lombok
- Apache POI for spreadsheet processing
- Maven Wrapper

## Project Structure

```text
src/
├── main/
│   ├── java/com/arif/hrs/
│   │   ├── auth/            JWT authentication, filters, handlers, and models
│   │   ├── configuration/   Spring Security configuration
│   │   ├── controllers/     REST controllers and HTTP models
│   │   ├── domain/          Domain services and DTOs
│   │   ├── mapper/          MapStruct DTO/entity and HTTP mappings
│   │   ├── model/           Persistence models and enums
│   │   ├── repository/      Spring Data repositories and specifications
│   │   ├── service/         Application services
│   │   └── util/            Shared utilities, including cookies and Excel support
│   └── resources/
│       ├── application.properties
│       ├── changelog/       Liquibase master changelog and changesets
│       ├── static/
│       └── templates/
└── test/java/               Application tests
```

## Prerequisites

- JDK 21
- PostgreSQL
- A PostgreSQL database named `db_local`
- A PostgreSQL user with permission to access the database

The default local configuration expects PostgreSQL at `127.0.0.1:5432`. Update `src/main/resources/application.properties` or provide environment-specific configuration before running the application.

## Getting Started

1. Create the local PostgreSQL database and user required by your environment.
2. Review the datasource, JWT, CORS, and cookie settings in `src/main/resources/application.properties`.
3. Run the test suite:

   ```bash
   ./mvnw clean test
   ```

4. Start the application:

   ```bash
   ./mvnw spring-boot:run
   ```

The application name is `hrs`. The default server port is Spring Boot's standard port, `8080`, unless overridden by configuration.

## Database Migrations

Liquibase changelogs are stored under `src/main/resources/changelog/`. The master changelog includes the initial schema, shift-type changes, and user/role/access seed data.

Liquibase is currently disabled in the checked-in application configuration. Enable it for environments where the application should run migrations automatically, or apply migrations through the configured Maven Liquibase plugin after reviewing its database connection settings.

## API Areas

The REST controllers are organized around these resources:

- Authentication: login and token-related operations
- Users: user management
- Roles: role management
- Access: access permission management
- Role access: role-to-access assignments
- Shift types and tolerances: shift configuration

For example, role-access operations are rooted at `/api/role-access` and include paginated listing, lookup by access ID, and creation.

## Configuration Notes

Important settings include:

- `spring.datasource.*`: PostgreSQL connection
- `security.jwt.*`: JWT secret and token lifetimes
- `app.allowed.cors`: allowed frontend origins
- `app.cookie.*`: authentication cookie behavior
- `spring.liquibase.*`: migration configuration

Do not commit real database passwords or JWT signing keys. The current local properties file contains development credentials and a development JWT secret; replace them with environment-specific values before sharing or deploying the application.

## Testing

Run all tests with:

```bash
./mvnw test
```

The test sources are located under `src/test/java/`.

## Packaging

Create a production JAR with:

```bash
./mvnw clean package
```

The generated artifact is written to `target/`.
