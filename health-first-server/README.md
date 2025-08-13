# Health First Server

- Java 17, Spring Boot 3
- H2 in-memory database
- Swagger UI at `/swagger-ui.html`

## Run

```bash
mvn spring-boot:run
```

## Test

```bash
mvn test
```

## Registration API

POST `/api/providers/register`

Example request body shown in the task description. The response excludes password fields. 