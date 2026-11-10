# IAM Service

The Identity and Access Management (IAM) service is the platform's central authority for identity, authentication, and authorization. It is built on Spring Authorization Server (OAuth 2.1 / OpenID Connect 1.0) and designed using Hexagonal Architecture (Ports and Adapters). It provides browser-based sign-in and profile self-service, federated social authentication, an OAuth 2.1 authorization server with Dynamic Client Registration (DCR), and a secured REST API for managing users, roles, and fine-grained permissions.

---

## Core Capabilities

- **Local Authentication and Registration**: Self-registration, secure password hashing with BCrypt, account lockout protection, and credential expiration tracking.
- **Federated Social Sign-In**: Single Sign-On (SSO) with Google, GitHub, and Facebook through OAuth 2.0 / OIDC login, along with a custom Telegram authentication flow.
- **OAuth 2.1 and OpenID Connect 1.0**: PKCE-enforced authorization code flow, token issuance (JWT access tokens with custom claims, OIDC ID tokens, refresh tokens), user consent management, and Dynamic Client Registration (RFC 7591).
- **Self-Service User Portal**: Profile updates, password modification, linked social account management, and active connected application consent review and revocation.
- **Hierarchical Access Control**: Role-Based Access Control (RBAC) combined with explicit Direct Permission assignments. Effective permissions are computed by combining role-based permissions with direct grants and subtracting explicit denials:
  $$\text{Effective Permissions} = (\text{Role Permissions} \cup \text{Direct GRANT}) \setminus \text{Direct DENY}$$
- **Secured REST Administration**: Comprehensive management of users, roles, and permissions protected by method-level authorities (`@PreAuthorize`) and standardized with RFC 9457 Problem Details error formatting.
- **Bilingual JTE Web Interface**: Server-side rendered views in English (`en`) and Khmer (`km`) featuring a minimalist, organic sage-green aesthetic.

---

## Modular Architecture

The service is organized into a Gradle multi-module project following clean Hexagonal principles. Domain models and application use cases remain strictly decoupled from persistence, security, and web framework dependencies.

| Module | Responsibility |
| --- | --- |
| `iam-domain/iam-domain-core` | Core domain models (`User`, `Role`, `Permission`, `Identity`, and audited assignment models) and domain business rules; zero framework dependencies |
| `iam-domain/iam-application-service` | 38 single-responsibility use cases, command and result DTOs, and inbound/outbound port interfaces |
| `iam-persistence` | Spring Data JPA entities (explicit assignment entities without `@ManyToMany`), repositories, persistence adapters, and Liquibase database migrations |
| `iam-infrastructure` | Spring Authorization Server adapters (`JdbcRegisteredClientRepository`, `JdbcOAuth2AuthorizationService`, `JdbcOAuth2AuthorizationConsentService`), JWT token claims customizer, and federated social login adapters |
| `iam-rest-api` | REST controllers, payload validation, DTO mapping, and RFC 9457 Problem Details exception handlers |
| `iam-web` | JTE templates, web controllers (login, registration, consent, self-service dashboard), and internationalization bundles |
| `iam-main` | Spring Boot bootstrap, runtime configuration, data seeders, and application entry point |

---

## Route Overview

The service operates locally on port `9000` (`http://localhost:9000`).

### Web Interface Routes

| Route | Purpose | Access |
| --- | --- | --- |
| `/login`, `/register` | User authentication and self-registration | Public |
| `/` | Self-service user profile overview and assigned permissions | Authenticated user |
| `/profile/edit` | Update personal details (names, gender, birthdate, address) | Authenticated user |
| `/profile/password` | Change user account password | Authenticated user |
| `/profile/accounts` | View and unlink connected federated identities | Authenticated user |
| `/profile/apps` | Review and revoke authorized client application consents | Authenticated user |
| `/oauth2/consent` | OAuth 2.1 scope consent approval screen | Authenticated user |

### REST API Routes

All endpoints under `/api` require a valid Bearer JWT access token (with the exception of `/api/auth/register`).

| Route | Method | Purpose | Authority Required |
| --- | --- | --- | --- |
| `/api/auth/register` | `POST` | Self-registration through the REST API | Public |
| `/api/me` | `GET`, `PUT` | Read or update current user profile | Authenticated |
| `/api/me/password` | `PUT` | Update current user password | Authenticated |
| `/api/me/identities` | `GET`, `DELETE` | List or unlink federated identities | Authenticated |
| `/api/users` | `GET`, `POST` | List users (paginated with search) or create a user | `user:read`, `user:create` |
| `/api/users/{uuid}` | `GET`, `PUT`, `DELETE` | Read, update, or soft-delete a user | `user:read`, `user:update`, `user:delete` |
| `/api/users/{uuid}/roles` | `POST`, `DELETE` | Assign or revoke roles for a user | `user:assign` |
| `/api/users/{uuid}/permissions` | `POST`, `DELETE` | Assign or revoke direct permissions | `user:assign` |
| `/api/users/{uuid}/permissions/effective` | `GET` | Calculate resolved effective permissions | `user:read` |
| `/api/roles` | `GET`, `POST` | List roles or create a new role | `role:read`, `role:create` |
| `/api/roles/{uuid}` | `GET`, `PUT`, `DELETE` | Read, update, or delete a role | `role:read`, `role:update`, `role:delete` |
| `/api/roles/{uuid}/permissions` | `POST`, `DELETE` | Assign or remove permissions from a role | `role:assign` |
| `/api/permissions` | `GET`, `POST` | List permissions or create a permission | `permission:read`, `permission:create` |
| `/api/permissions/{uuid}` | `GET`, `PUT`, `DELETE` | Read, update, or delete a permission | `permission:read`, `permission:update`, `permission:delete` |

### OAuth 2.1 and OpenID Connect Endpoints

| Endpoint | Purpose |
| --- | --- |
| `/.well-known/openid-configuration` | OpenID Connect discovery metadata |
| `/oauth2/authorize` | Authorization endpoint (PKCE S256 required for public clients) |
| `/oauth2/token` | Token issuance endpoint (`authorization_code`, `refresh_token`, `client_credentials`) |
| `/oauth2/jwks` | JSON Web Key Set (JWKS) endpoint for public signature verification |
| `/connect/register` | Dynamic Client Registration (RFC 7591) endpoint |

---

## Configuration and Environment Variables

Configuration is externalized and loaded from the execution environment or a `.env` file.

| Environment Variable | Required | Description |
| --- | --- | --- |
| `URL` | Yes | PostgreSQL JDBC connection URL (e.g. `jdbc:postgresql://localhost:9543/ite_db`) |
| `USERNAME` | Yes | PostgreSQL username |
| `PASSWORD` | Yes | PostgreSQL password |
| `GOOGLE_CLIENT_ID`, `GOOGLE_CLIENT_SECRET` | No | Google OAuth 2.0 credentials for federated sign-in |
| `GITHUB_CLIENT_ID`, `GITHUB_CLIENT_SECRET` | No | GitHub OAuth 2.0 credentials for federated sign-in |
| `FACEBOOK_CLIENT_ID`, `FACEBOOK_CLIENT_SECRET` | No | Facebook OAuth 2.0 credentials for federated sign-in |
| `TELEGRAM_BOT_TOKEN`, `TELEGRAM_BOT_USERNAME` | No | Telegram Bot API credentials for Telegram widget login |

Database schemas and initial system records are managed by Liquibase migrations (`microservices/iam-service/iam-main/src/main/resources/db/changelog/`). On first boot, initial system permissions and standard roles are verified and provisioned automatically.

---

## Running Locally

### 1. Start PostgreSQL
Ensure PostgreSQL is active on port `9543`:
```bash
docker compose up -d
```

### 2. Launch the Application
Run the boot task using Gradle:

```powershell
.\gradlew.bat :microservices:iam-service:iam-main:bootRun
```

The web interface is available at `http://localhost:9000`. Switch between English and Khmer using the interface selector or by appending `?lang=en` or `?lang=km` to the request URL (persisted via the `IAM_LOCALE` cookie).

---

## Testing

### Unit and Domain Tests
Run the automated test suite covering domain business rules and use cases:

```powershell
.\gradlew.bat :microservices:iam-service:iam-main:test
```

### API Testing via Postman
A pre-configured Postman collection is included in the service directory at:
```
microservices/iam-service/iam-service.postman_collection.json
```
The collection includes pre-configured OAuth 2.0 PKCE settings for acquiring tokens against `http://localhost:9000` using the pre-seeded `oidc-client` and testing authenticated REST endpoints.
