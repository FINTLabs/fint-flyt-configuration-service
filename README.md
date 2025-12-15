# FINT Flyt Configuration Service

Spring Boot service that stores and validates configuration documents for FINT Flyt integrations. It exposes an internal API for creating, updating, versioning, and deleting configurations, persists state in PostgreSQL, validates payloads against integration metadata via Kafka, and serves Kafka request/reply endpoints so other Flyt services can fetch configurations and mappings on demand.

## Highlights

- **RESTful configuration registry** — Spring WebFlux controller under `/internal/api/konfigurasjoner` for paginated listings, detail fetch, create, patch, and delete operations.
- **Versioned persistence** — JPA repository backed by PostgreSQL automatically increments a configuration version when a document is marked as completed.
- **Kafka request/reply bridges** — Consumers expose configuration and mapping lookups, while producers fetch integrations, integration metadata, and instance metadata used during validation.
- **Context-aware validation** — Custom Jakarta Bean Validation constraints ensure integration↔metadata consistency, key uniqueness, type compatibility, and value parsability before persisting.
- **Audited updates** — `AuditorScope` binds the calling principal from JWTs so `lastModifiedBy`/`lastModifiedAt` fields are populated through Spring Data auditing.

## Architecture Overview

| Component | Responsibility |
|-----------|----------------|
| `ConfigurationController` | Handles internal HTTP requests, binds auditing context, orchestrates validation, and enforces completion rules. |
| `ConfigurationService` | Wraps repository access, DTO ↔ entity mapping, and mapping persistence for CRUD flows. |
| `ConfigurationRepository` & `ObjectMappingRepository` | Spring Data JPA repositories storing configurations and nested object mappings with custom versioning logic. |
| `ConfigurationMappingService` & mapping helpers | Convert between DTO graphs and entity graphs for mappings, collections, and per-key values. |
| `ConfigurationValidatorFactory` & constraints | Build validators with integration + metadata payload to validate references, keys, types, and completion-specific rules. |
| `IntegrationRequestProducerService`, `MetadataRequestProducerService`, `InstanceMetadataRequestProducerService` | Perform Kafka request/reply lookups so validation has the latest integration and metadata context. |
| `ConfigurationRequestConsumerConfiguration` | Registers Kafka consumers that answer configuration and mapping fetch requests by configuration ID for other Flyt services. |
| `TokenParsingUtils`, `TokenAuditorAware` | Extract auditing data from OAuth2 tokens and plug into Spring Data’s `@EnableJpaAuditing`. |

## HTTP API

Base path: `/internal/api/konfigurasjoner`

| Method | Path | Description | Request body | Response |
|--------|------|-------------|--------------|----------|
| `GET` | `/?side&antall&sorteringFelt&sorteringRetning&integrasjonId&ferdigstilt&ekskluderMapping` | Paginated listing filtered by integration and completion status. `ekskluderMapping=true` removes heavy mapping payloads. | – | `200 OK` with `Page<ConfigurationDto>`. |
| `GET` | `/{configurationId}?ekskluderMapping` | Fetch a single configuration, optionally omitting the mapping section. | – | `200 OK` with `ConfigurationDto`, `404` when missing. |
| `POST` | `/` | Create a configuration draft. Validates that integration and metadata IDs exist and mapping content passes structural checks. | `ConfigurationDto` JSON (see below). | `200 OK` with persisted `ConfigurationDto`; validation failures return `422`. |
| `PATCH` | `/{configurationId}` | Update metadata reference, comment, mapping, or mark as completed. Completed configurations become immutable. | `ConfigurationPatchDto` JSON. | `200 OK` with updated `ConfigurationDto`, `404` when missing, `403` if already completed. |
| `DELETE` | `/{configurationId}` | Remove a configuration that is still a draft (not completed). | – | `204 No Content`, `403` when completed, `404` when missing. |

Example `ConfigurationDto` payload:

```json
{
  "integrationId": 42,
  "integrationMetadataId": 1337,
  "comment": "Draft mapping for elevmappe",
  "mapping": {
    "valueMappings": [
      {
        "toKey": "case.status",
        "fromValue": "OPPRETTET"
      }
    ]
  }
}
```

Validation errors return `422 Unprocessable Entity` with aggregated constraint messages. When the resource-server permissions consumer is enabled, access to non-authorized orgs yields `403 Forbidden`.

## Kafka Integration

- `ConfigurationRequestConsumerConfiguration` exposes request/reply consumers that return either the full configuration or just the mapping for a configuration ID.
- `IntegrationRequestProducerService` fetches the owning integration by ID to validate configuration references.
- `MetadataRequestProducerService` and `InstanceMetadataRequestProducerService` fetch structural metadata and instance metadata content used by validators to verify keys, types, and required fields.
- All topics follow Flyt domain defaults, use per-tenant prefixes, retain requests for five minutes, and set reply timeouts to five seconds for outbound templates.

## Scheduled Tasks

The service does not define scheduled jobs; validation and versioning happen inline with POST/PATCH requests.

## Configuration

Spring profiles include common Flyt layers: `flyt-kafka`, `flyt-logging`, `flyt-resource-server`, and `flyt-postgres`.

Key properties:

| Property                                                                | Description |
|-------------------------------------------------------------------------|-------------|
| `fint.application-id`                                                   | Used for Kafka client IDs, request/reply reply topics, and default topic prefixes. |
| `novari.kafka.topic.org-id`                                             | Scoped per kustomize overlay to control Kafka ACLs and topic names. |
| `fint.database.url`, `fint.database.username`, `fint.database.password` | PostgreSQL connection parameters injected from secrets. |
| `spring.security.oauth2.resourceserver.jwt.issuer-uri`                  | Identity provider for validating OAuth2 JWTs. |
| `management.endpoints.web.exposure.include`                             | Actuator endpoints exposed (health, info, prometheus). |
| `novari.flyt.resource-server.security.api.internal.*`                   | Toggles the internal API and per-org authorization matrix. |

Secrets referenced by the base manifest must supply database credentials and OAuth client configuration.

## Running Locally

Prerequisites:

- Java 21+
- Docker (used by `start-postgres` helper)
- Local Kafka broker (e.g., `docker compose` or existing dev cluster)

Helpful commands:

```shell
./gradlew clean build        # compile sources and run tests
./gradlew test               # unit + validation tests
./gradlew bootRun            # start with Flyt profiles
./start-postgres             # launch PostgreSQL on localhost:5434 (Ctrl+C/docker stop to tear down)
```

Use `SPRING_PROFILES_ACTIVE=local-staging` to pick up overrides in `src/main/resources/application-local-staging.yaml`. The profile expects PostgreSQL on `jdbc:postgresql://localhost:5434/fint-flyt-configuration-service`, username `postgres`, password `password`, and Kafka on `localhost:9092`.

Swagger UI is available at `http://localhost:8082/swagger-ui/index.html` when the application runs with the local profile.

## Deployment

Kustomize layout:

- `kustomize/base/` — shared Application manifest, Flyt wiring, secrets, and Actuator configuration.
- `kustomize/overlays/<org>/<env>/` — tenant-specific patches (namespace, labels, Kafka topics, ingress paths).

Templates live under `kustomize/templates/`:

- `overlay.yaml.tpl` — canonical template rendered per overlay.

Regenerate overlays whenever template logic changes:

```shell
./scripts/render-overlay.sh
```

The script walks all overlay directories, injects org/env-specific values (namespace, Kafka topic prefixes, role maps, ingress paths), and rewrites `kustomization.yaml` files in place.

## Security

- OAuth2 resource server that validates JWTs against `https://idp.felleskomponent.no`.
- Internal API gated by `novari.flyt.resource-server.security.api.internal` with optional per-org role mappings.
- `TokenAuditorAware` and `AuditorScope` tie JWT claims to Spring Data auditing so updates are traceable.

## Observability & Operations

- Liveness/readiness probe: `/actuator/health`.
- Prometheus metrics: `/actuator/prometheus`.
- Spring Boot + Reactor structured logging; leverage Flyt log conventions for correlation IDs.

## Development Tips

- Validators call Kafka services to verify metadata; stub `IntegrationRequestProducerService`, `MetadataRequestProducerService`, and `InstanceMetadataRequestProducerService` in tests when asserting validation rules.
- Flyway migrations live in `src/main/resources/db/migration`; add new scripts for schema changes instead of altering existing ones.
- `ConfigurationMappingService` centralizes DTO/entity conversion—extend it instead of duplicating mapping logic in controllers or services.

## Contributing

1. Create a topic branch for your change.
2. Run `./gradlew test` before opening a pull request.
3. If you touch kustomize content, run `./scripts/render-overlay.sh` and commit the regenerated overlays.
4. Add or update unit/integration tests that cover new functionality or bug fixes.

———

FINT Flyt Configuration Service is maintained by the FINT Flyt team. Reach out on the internal Slack channel or open an issue in this repository for questions or enhancement requests.
