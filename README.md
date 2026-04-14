# 🏢 Franquicias Service API -  **Prueba Técnica Accenture**

API RESTful desarrollada con **Spring Boot 3** para gestionar franquicias, sucursales y productos — **Prueba Técnica Accenture**.

> 🌐 **La solución está 100% desplegada en la nube (GCP).**
> La API corre en **Cloud Run** y persiste en **Cloud SQL (PostgreSQL)**, ambos provisionados con IaC.
> Puedes probarla directamente en la interfaz de Swagger UI sin necesidad de levantar nada localmente:
> 
> **[`https://franquicias-service-prod-209503874304.us-central1.run.app/franquicias-service/swagger-ui.html`](https://franquicias-service-prod-209503874304.us-central1.run.app/franquicias-service/swagger-ui.html)**



>  **Nota:** Para pruebas locales, asegúrate de configurar las variables de entorno las cuales enviare en el correo para conectar con la instancia de PostgreSQL.
> para pruebas directas al endpoint de producción, no es necesario configurar nada,  sin embargo al ser un servicio serveless como Cloudrun, se encuentra inactivo por periodos de inactividad, por lo que la primera consulta puede tardar unos segundos en responder mientras el servicio se "despierta".

considero importante destacar este indice: [🧠 Decisiones técnicas y de diseño](#-decisiones-técnicas-y-de-diseño) , ya que es donde explico las decisiones técnicas y de diseño más relevantes del proyecto que tome, con sus respectivos trade-offs. Recomiendo leerlo para entender el "por qué" detrás de la implementación, no solo el "qué".


---

## 📋 Tabla de Contenidos

1. [🗂️ Repositorios del proyecto](#️-repositorios-del-proyecto)
2. [🛠️ Stack Tecnológico](#️-stack-tecnológico)
3. [☁️ Despliegue en la Nube (GCP)](#️-despliegue-en-la-nube-gcp)
4. [📖 Documentación Swagger UI](#-documentación-swagger-ui)
5. [📡 Endpoints de la API](#-endpoints-de-la-api)
   - [🏢 Franquicias](#-franquicias--getpost-patc-apiv1franquicias)
   - [🏪 Sucursales](#-sucursales--patch-apiv1sucursales)
   - [📦 Productos](#-productos--deletepatch-apiv1productos)
6. [⚠️ Manejo de Errores](#️-manejo-de-errores)
7. [🏛️ Arquitectura](#️-arquitectura)
8. [🧠 Decisiones técnicas y de diseño](#-decisiones-técnicas-y-de-diseño)  *---Considero Importante---*
9. [🗄️ Base de Datos](#️-base-de-datos)
10. [📁 Estructura completa del proyecto](#-estructura-completa-del-proyecto)
11. [🚀 Cómo ejecutar el proyecto localmente](#-cómo-ejecutar-el-proyecto-localmente)
    - [▶️ Opción 1 — Docker](#️-opción-1--docker-recomendado)
    - [▶️ Opción 2 — Variables con `.env`](#️-opción-2--variables-de-entorno-con-archivo-env)
    - [▶️ Opción 3 — Gradle local](#️-opción-3--gradle-local)
    - [🧪 Ejecutar tests](#-ejecutar-tests)

---

## 🗂️ Repositorios del proyecto

La solución está dividida en **dos repositorios independientes**, siguiendo el principio de **Separación de Responsabilidades**: el ciclo de vida de la infraestructura y el del código de aplicación son distintos — la infra cambia por razones de aprovisionamiento, costos o entornos; el microservicio cambia por razones de negocio. Mezclarlos en un solo repositorio generaría pipelines acoplados y responsabilidades difusas.

| Repositorio | Descripción | CI/CD |
|-------------|-------------|-------|
| **Este repo** — Microservicio | Código fuente de la API Spring Boot | ✅ GitHub Actions |
| **[accenture-sandbox-infra](https://github.com/JuanGarces186f/accenture-sandbox-infra)** | Infraestructura como Código (IaC) en GCP | ✅ GitHub Actions |

### ⚙️ Repositorio de Infraestructura — `accenture-sandbox-infra`

El repositorio de infra gestiona todo el aprovisionamiento en GCP mediante IaC. Para el alcance de esta prueba técnica incluye:

- 🗄️ **Base de datos** — Creación y configuración de la instancia **Cloud SQL (PostgreSQL)**
- 📦 **Artifact Registry** — Aprovisionamiento del registro de imágenes Docker donde se publica la imagen del microservicio
- ☁️ **Cloud Run** — Configuración del servicio serverless que ejecuta el contenedor

La IaC fue diseñada con la premisa de que sea **fácil crear y destruir diferentes ambientes** (ej: `dev`, `staging`, `prod`), simulando la realidad de un entorno empresarial donde coexisten múltiples ambientes con configuraciones aisladas.

### 🔄 CI/CD

Ambos repositorios cuentan con pipelines de **GitHub Actions**:

- **Microservicio** → al hacer push/merge a la rama principal, se construye la imagen Docker, se publica en Artifact Registry y se despliega automáticamente en Cloud Run.
- **Infraestructura** → el pipeline aplica los cambios de IaC sobre GCP, permitiendo aprovisionar o actualizar entornos de forma reproducible y auditada.

---

## 🛠️ Stack Tecnológico

| Tecnología                  | Versión  | Rol                                         |
|-----------------------------|----------|---------------------------------------------|
| Java                        | 17       | Lenguaje principal                          |
| Spring Boot                 | 3.4.4    | Framework base                              |
| Spring Data JPA             | (BOM)    | Persistencia / ORM                          |
| Spring Validation           | (BOM)    | Validación de requests                      |
| PostgreSQL                  | —        | Base de datos de producción                 |
| H2 (en memoria)             | (BOM)    | Base de datos embebida para tests           |
| MapStruct                   | 1.6.3    | Mapeo automático entre capas                |
| Lombok                      | (BOM)    | Reducción de boilerplate                    |
| SpringDoc OpenAPI (Swagger) | 2.8.6    | Documentación interactiva de la API         |
| Gradle Wrapper              | —        | Build tool                                  |
| Docker / Docker Compose     | —        | Contenerización y ejecución                 |

---

## ☁️ Despliegue en la Nube (GCP)

La solución está completamente desplegada en **Google Cloud Platform**:

| Componente | Tecnología GCP | URL / Detalles |
|------------|---------------|----------------|
| API REST   | **Cloud Run**  | `https://franquicias-service-prod-209503874304.us-central1.run.app` |
| Base de datos | **Cloud SQL — PostgreSQL** | Instancia administrada en GCP |

### 🔗 URLs en producción

| Recurso | URL |
|---------|-----|
| Swagger UI | [`/franquicias-service/swagger-ui.html`](https://franquicias-service-prod-209503874304.us-central1.run.app/franquicias-service/swagger-ui.html) |
| OpenAPI JSON | [`/franquicias-service/v3/api-docs`](https://franquicias-service-prod-209503874304.us-central1.run.app/franquicias-service/v3/api-docs) |
| Base API | `https://franquicias-service-prod-209503874304.us-central1.run.app/franquicias-service/api/v1` |

> **Cloud Run** ejecuta la imagen Docker construida con el `Dockerfile` multi-stage del proyecto. Las variables de entorno (`DB_HOST`, `DB_NAME`, `DB_USER`, `DB_PASS`) están configuradas como secrets en el servicio de Cloud Run, apuntando a la instancia de **Cloud SQL**.

---

## 📖 Documentación Swagger UI

Una vez levantado el proyecto, la documentación interactiva está disponible en:

| Entorno | URL |
|---------|-----|
| **Producción (GCP)** | [`https://franquicias-service-prod-209503874304.us-central1.run.app/franquicias-service/swagger-ui.html`](https://franquicias-service-prod-209503874304.us-central1.run.app/franquicias-service/swagger-ui.html) |
| **Local** | `http://localhost:8080/franquicias-service/swagger-ui.html` |
| **OpenAPI JSON** | `{base-url}/franquicias-service/v3/api-docs` |

La documentación se genera automáticamente gracias a **SpringDoc OpenAPI 2.8.6** (`springdoc-openapi-starter-webmvc-ui`), lo que garantiza que cualquier cambio en el código (nuevos endpoints, cambios en DTOs, validaciones) se refleje de forma inmediata en la UI sin necesidad de actualizar la documentación manualmente.

---


---

## 📡 Endpoints de la API

**Base URL:** `http://localhost:8080/franquicias-service/api/v1`

Todas las respuestas exitosas siguen el envelope:
```json
{
  "success": true,
  "message": "Descripción del resultado",
  "data": { ... },
  "timestamp": "2026-04-14T10:00:00"
}
```

---
⭐ = Endpoints de puntos extra (PLUS)
### 🏢 Franquicias — `GET|POST|PATCH /api/v1/franquicias`

| Método | Endpoint                                         | Descripción                                      | Status |
|--------|--------------------------------------------------|--------------------------------------------------|--------|
| POST   | `/franquicias`                                   | Crear nueva franquicia                           | 201    |
| GET    | `/franquicias`                                   | Listar todas las franquicias (id + nombre)       | 200    |
| GET    | `/franquicias/{id}`                              | Obtener franquicia completa con sucursales       | 200    |
| PATCH  | `/franquicias/{id}` ⭐                           | Actualizar nombre de franquicia                  | 200    |
| POST   | `/franquicias/{franquiciaId}/sucursales`         | Agregar sucursal a una franquicia                | 201    |
| GET    | `/franquicias/{franquiciaId}/productos/top-stock`| Producto con mayor stock por cada sucursal       | 200    |

---

### 🏪 Sucursales — `PATCH /api/v1/sucursales`

| Método | Endpoint                             | Descripción                         | Status |
|--------|--------------------------------------|-------------------------------------|--------|
| PATCH  | `/sucursales/{sucursalId}` ⭐        | Actualizar nombre de sucursal       | 200    |
| POST   | `/sucursales/{sucursalId}/productos` | Agregar producto a una sucursal     | 201    |

---

### 📦 Productos — `DELETE|PATCH /api/v1/productos`

| Método | Endpoint                       | Descripción                                  | Status |
|--------|--------------------------------|----------------------------------------------|--------|
| DELETE | `/productos/{productoId}`      | Eliminar producto de una sucursal            | 200    |
| PATCH  | `/productos/{productoId}` ⭐   | Actualizar nombre y/o stock (parcialmente)   | 200    |

---

### Ejemplo: Top producto por sucursal

**GET** `/api/v1/franquicias/1/productos/top-stock`
```json
{
  "success": true,
  "message": "Top productos por sucursal obtenidos exitosamente",
  "data": [
    {
      "sucursalId": 1,
      "sucursalNombre": "Sucursal Centro",
      "productoId": 3,
      "productoNombre": "Big Mac",
      "stock": 150
    },
    {
      "sucursalId": 2,
      "sucursalNombre": "Sucursal Norte",
      "productoId": 7,
      "productoNombre": "McFlurry",
      "stock": 200
    }
  ]
}
```

---

## ⚠️ Manejo de Errores

Todos los errores retornan un envelope diferenciado de la respuesta exitosa:

```json
{
  "success": false,
  "message": "El recurso no fue encontrado",
  "error": {
    "status": 404,
    "code": "RESOURCE_NOT_FOUND",
    "message": "Franquicia con id '99' no fue encontrado",
    "details": null,
    "traceId": "a1b2c3d4",
    "timestamp": 1773934758921
  },
  "timestamp": 1773934758921
}
```

| HTTP Status | Código interno          | Descripción                        |
|-------------|-------------------------|------------------------------------|
| 400         | `VALIDATION_ERROR`      | Datos de entrada inválidos         |
| 404         | `RESOURCE_NOT_FOUND`    | Recurso no encontrado              |
| 409         | `DUPLICATE_RESOURCE`    | Conflicto — nombre duplicado       |
| 500         | `INTERNAL_SERVER_ERROR` | Error interno del servidor         |

Cada error incluye un `traceId` (UUID corto) para facilitar la trazabilidad en logs.

---

## 🏛️ Arquitectura

El proyecto implementa **Clean Architecture** (también conocida como Arquitectura Hexagonal / Ports & Adapters), organizada en tres capas principales:

```
src/main/java/.../franquicias_service/
│
├── domain/                          ← Núcleo del negocio (sin dependencias externas)
│   ├── model/                       # Entidades de dominio puras (Franquicia, Sucursal, Producto)
│   ├── port/
│   │   ├── in/                      # Puertos de entrada (contratos UseCase)
│   │   └── out/                     # Puertos de salida (contratos Repository)
│   └── exception/                   # Excepciones de dominio (ResourceNotFound, Duplicate)
│
├── application/                     ← Casos de uso / Lógica de aplicación
│   └── usecase/                     # Implementaciones de los puertos de entrada
│       ├── FranquiciaUseCaseImpl.java
│       ├── SucursalUseCaseImpl.java
│       └── ProductoUseCaseImpl.java
│
└── infrastructure/                  ← Adaptadores (detalles técnicos)
    ├── adapter/
    │   ├── in/web/                  # Adaptador de entrada HTTP
    │   │   ├── controller/          # REST Controllers
    │   │   ├── docs/                # Interfaces de documentación Swagger (separadas)
    │   │   ├── dto/                 # DTOs request/response
    │   │   ├── mapper/              # MapStruct: dominio ↔ DTO web
    │   │   └── exception/           # GlobalExceptionHandler
    │   └── out/persistence/         # Adaptador de salida JPA
    │       ├── entity/              # Entidades JPA (@Entity)
    │       ├── repository/          # Spring Data JPA Repositories
    │       ├── mapper/              # MapStruct: dominio ↔ entidad JPA
    │       └── adapter/             # Implementaciones de los puertos de salida
    └── config/                      # Configuración (SwaggerConfig)
```

### Flujo de una request

```
HTTP Request
    → Controller (infrastructure/in)
        → UseCase (application)
            → RepositoryPort (domain/out) [interface]
                → RepositoryAdapter (infrastructure/out)
                    → JpaRepository → PostgreSQL
```

---

## 🧠 Decisiones técnicas y de diseño

### ✅ Lo que se implementó con intención

#### Clean Architecture / Hexagonal
Se eligió esta arquitectura para desacoplar el dominio del negocio de los detalles técnicos (base de datos, framework web). Las interfaces `*UseCase` y `*RepositoryPort` actúan como contratos que aíslan cada capa. Cualquier cambio en el framework HTTP o en la BD no afecta la lógica de negocio.

#### Mappers separados por capa con MapStruct
Se tienen dos familias de mappers independientes:
- `infrastructure/adapter/in/web/mapper/` → Mapeo entre **dominio** y **DTOs web**
- `infrastructure/adapter/out/persistence/mapper/` → Mapeo entre **dominio** y **entidades JPA**

Esto garantiza que el dominio nunca conozca ni la capa web ni la capa de persistencia. MapStruct genera el código de mapeo en tiempo de compilación, evitando reflexión en runtime.

#### Documentación Swagger separada del Controller (`/docs`)
Cada controller implementa una interfaz ubicada en el paquete `infrastructure/adapter/in/web/docs/` (ej: `FranquiciaControllerDocs.java`). Toda la anotación OpenAPI (`@Operation`, `@ApiResponses`, `@Parameter`, etc.) vive en esa interfaz, y el controller solo contiene lógica de implementación. Esta separación evita que el código de negocio quede "ensuciado" por decenas de anotaciones de documentación, mejorando la legibilidad.

#### Envelope de respuesta diferenciado por tipo
Se usan dos wrappers distintos e intencionalmente separados:
- `ApiResponse<T>` → respuestas exitosas (`success: true`, con `data`)
- `ErrorApiResponse` + `ErrorResponse` → respuestas de error (`success: false`, con `error.code`, `error.traceId`, `error.details`)

Esto facilita el manejo en el frontend/cliente, que puede distinguir el tipo de respuesta antes de parsear el payload.

#### Prevención del problema N+1 con `@EntityGraph`
En lugar de dejar que Hibernate cargue las relaciones lazily de forma implícita (generando una query extra por cada entidad hija), se usan `@EntityGraph` directamente en los repositorios JPA. Esto emite un único `LEFT JOIN FETCH` en la consulta, trayendo todo en una sola query. Es la estrategia más legible para el evaluador y no "ensucia" la clase `@Entity` con anotaciones de configuración de fetch.

#### Consulta de top-stock con proyección JPQL
Para el endpoint del punto 7 (producto con mayor stock por sucursal), se usó una proyección JPQL con la clase `TopProductoPorSucursal` del dominio. La subquery `MAX(stock)` corre en el motor de base de datos, evitando traer todos los productos a memoria para ordenarlos en Java. La lógica permanece en la capa de infraestructura (repositorio), y el dominio solo conoce el resultado tipado.

#### Validaciones con Bean Validation (`@Valid`)
Los DTOs de request usan anotaciones de Jakarta Validation (`@NotBlank`, `@Min`, `@Size`, etc.). Los errores se capturan en el `GlobalExceptionHandler` y se devuelven como lista de campos inválidos con sus mensajes.

#### `ddl-auto: update`
Se dejó habilitado para facilitar la evaluación sin necesidad de scripts de base de datos. En un entorno real de producción esto se reemplazaría por **Flyway** o **Liquibase** para gestión formal y versionada del esquema.

---

### ⚠️ Decisiones conscientes con trade-offs

#### Un UseCase por entidad (en lugar de un UseCase por caso de uso)
Se optó por un UseCase por agregado (`FranquiciaUseCaseImpl`, `SucursalUseCaseImpl`, `ProductoUseCaseImpl`) en lugar de un UseCase por operación (ej: `CrearFranquiciaUseCase`, `ActualizarFranquiciaUseCase`). Esta es una simplificación pragmática para una prueba técnica de este tamaño — en un sistema más grande lo ideal sería un UseCase por caso de uso individual para respetar el Principio de Responsabilidad Única (SRP) de forma más estricta.

#### Endpoints de recursos anidados vs. controladores planos
La creación de sucursales (`POST /franquicias/{id}/sucursales`) y la consulta de top-stock (`GET /franquicias/{id}/productos/top-stock`) se ubicaron en `FranquiciaController` por su naturaleza jerárquica REST. La gestión de productos (agregar, eliminar, actualizar) se distribuye entre `SucursalController` y `ProductoController` según el recurso principal afectado. Esta es una decisión opinada donde existen argumentos para otras organizaciones.

#### Sin paginación
Los endpoints de listado no implementan paginación. En producción con volúmenes reales de datos sería necesario agregar `Pageable` de Spring Data.

---

## 🗄️ Base de Datos

### Producción — PostgreSQL (configuración actual)

```yaml
spring:
  datasource:
    url: jdbc:postgresql://${DB_HOST}/${DB_NAME}
    driver-class-name: org.postgresql.Driver
    username: ${DB_USER}
    password: ${DB_PASS}
  jpa:
    hibernate:
      ddl-auto: update
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
```

Variables de entorno requeridas:

| Variable  | Descripción                |
|-----------|----------------------------|
| `DB_HOST` | Host de PostgreSQL         |
| `DB_NAME` | Nombre de la base de datos |
| `DB_USER` | Usuario                    |
| `DB_PASS` | Contraseña                 |


> 📌`ddl-auto: update`
Se dejó habilitado para facilitar la evaluación sin necesidad de scripts de base de datos. En un entorno real de producción esto se reemplazaría por **Flyway** o **Liquibase** para gestión formal y versionada del esquema.



### Tests — H2 en memoria

El perfil `test` usa H2 para ejecutar los tests de integración de repositorio de forma totalmente aislada, sin necesidad de una base de datos real activa.

---

## 📁 Estructura completa del proyecto

```
src/main/java/.../franquicias_service/
│
├── domain/
│   ├── exception/
│   │   ├── DuplicateResourceException.java
│   │   ├── ErrorCode.java
│   │   └── ResourceNotFoundException.java
│   ├── model/
│   │   ├── Franquicia.java
│   │   ├── Producto.java
│   │   ├── Sucursal.java
│   │   └── TopProductoPorSucursal.java
│   └── port/
│       ├── in/
│       │   ├── FranquiciaUseCase.java
│       │   ├── ProductoUseCase.java
│       │   └── SucursalUseCase.java
│       └── out/
│           ├── FranquiciaRepositoryPort.java
│           ├── ProductoRepositoryPort.java
│           └── SucursalRepositoryPort.java
│
├── application/
│   └── usecase/
│       ├── FranquiciaUseCaseImpl.java
│       ├── ProductoUseCaseImpl.java
│       └── SucursalUseCaseImpl.java
│
└── infrastructure/
    ├── adapter/
    │   ├── in/web/
    │   │   ├── controller/
    │   │   │   ├── FranquiciaController.java
    │   │   │   ├── ProductoController.java
    │   │   │   └── SucursalController.java
    │   │   ├── docs/                        ← Documentación Swagger (separada)
    │   │   │   ├── FranquiciaControllerDocs.java
    │   │   │   ├── ProductoControllerDocs.java
    │   │   │   └── SucursalControllerDocs.java
    │   │   ├── dto/
    │   │   │   ├── request/
    │   │   │   └── response/
    │   │   ├── exception/
    │   │   │   └── GlobalExceptionHandler.java
    │   │   └── mapper/
    │   │       ├── FranquiciaWebMapper.java
    │   │       ├── ProductoWebMapper.java
    │   │       └── SucursalWebMapper.java
    │   └── out/persistence/
    │       ├── adapter/
    │       │   ├── FranquiciaRepositoryAdapter.java
    │       │   ├── ProductoRepositoryAdapter.java
    │       │   └── SucursalRepositoryAdapter.java
    │       ├── entity/
    │       │   ├── FranquiciaEntity.java
    │       │   ├── ProductoEntity.java
    │       │   └── SucursalEntity.java
    │       ├── mapper/
    │       │   ├── FranquiciaEntityMapper.java
    │       │   ├── ProductoEntityMapper.java
    │       │   └── SucursalEntityMapper.java
    │       └── repository/
    │           ├── FranquiciaJpaRepository.java
    │           ├── ProductoJpaRepository.java
    │           └── SucursalJpaRepository.java
    └── config/
        └── SwaggerConfig.java
```

---

## 🚀 Cómo ejecutar el proyecto localmente

### Prerrequisitos

- [Docker](https://www.docker.com/) instalado y corriendo

---

### ▶️ Docker

El proyecto incluye un `Dockerfile` multi-stage optimizado. Para construir y ejecutar la imagen:

```bash
# 1. Construir la imagen
docker build -t franquicias-service .
```

#### Opción A — Variables de entorno directas
```bash
# 2. Ejecutar el contenedor apuntando a tu base de datos PostgreSQL
docker run -p 8080:8080 \
  -e DB_HOST=<host> \
  -e DB_NAME=<nombre_bd> \
  -e DB_USER=<usuario> \
  -e DB_PASS=<contraseña> \
  franquicias-service
```

#### Opción B — Archivo `.env`

Crea un archivo `.env` en la raíz del proyecto:

```env
DB_HOST=34.59.25.187
DB_NAME=accenture-sandbox-db
DB_USER=accenture_sandbox_user
DB_PASS=tu_password
```

Y ejecuta con:

```bash
docker run -p 8080:8080 --env-file .env franquicias-service
```

La aplicación quedará disponible en: `http://localhost:8080/franquicias-service`

> **Nota:** El `Dockerfile` usa un build multi-stage:
> - **Fase `build`**: `eclipse-temurin:17-jdk-alpine` con Gradle Wrapper (sin depender de versiones instaladas localmente).
> - **Fase `runtime`**: `eclipse-temurin:17-jre-alpine` — imagen mínima, solo con el JRE necesario para ejecutar el JAR. Esto reduce considerablemente el tamaño final de la imagen.

---

---

---