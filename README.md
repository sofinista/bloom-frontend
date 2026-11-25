# Floristería Bloom — Frontend (Entrega final)

Aplicación web que consume la API REST de la Floristería Bloom para gestionar **clientes**, **flores**,
**pedidos** (con sus arreglos y su tarjeta de dedicatoria) y **pagos**.
Proyecto de Programación, ITM.

**Integrantes:** Ana Sofía Bolívar Celada · Mariana Buriticá Ospina

## Los dos repositorios

| Proyecto | Repositorio | Puerto |
|---|---|---|
| Backend (API REST) | https://github.com/sofinista/bloom-floristeria | 8080 (`/bloom`) |
| Frontend (este) | https://github.com/sofinista/bloom-frontend | 9090 |

Son dos proyectos separados: el frontend no toca la base de datos, todo lo pide a la API del backend.

## Tecnologías

- Java 21 · Spring Boot · Gradle
- Spring MVC con **Thymeleaf** (las páginas HTML) y CSS propio
- **OpenFeign** (Spring Cloud) para consumir la API: clientes, flores, pedidos y pagos
- **RestTemplate** para consultar el saldo de un pedido
- Apache HttpClient 5 (`feign-hc5`) para que Feign pueda enviar peticiones PATCH
- Lombok

## Arquitectura

```
Navegador ──> vistas (@Controller) ──> webservicesclients (Feign / RestTemplate) ──> API backend ──> SQL Server
                 │
                 └──> templates (Thymeleaf)
```

- `vistas/`: controladores de vista; reciben la acción del usuario, llaman al cliente web y escogen la página.
- `webservicesclients/`: interfaces `@FeignClient` (una por recurso) y `SaldoClientRestTemplate`.
- `dtos/`: objetos con los mismos campos que el JSON de la API.
- `utilidades/MensajeError`: saca el mensaje `{"error": "..."}` que manda la API y lo muestra en la página.
- `templates/fragments/plantilla.html`: cabeza, menú, avisos y pie que reutilizan todas las páginas.

## Cómo ejecutarlo

1. Arrancar el backend (`bloom-floristeria`) con SQL Server corriendo: `./gradlew bootRun`
2. En este proyecto: `./gradlew bootRun`
3. Abrir http://localhost:9090

La URL de la API está en `src/main/resources/application.yaml` (`bloom.api.url`).

## Páginas y la API que usan

| Página | Ruta | API del backend |
|---|---|---|
| Inicio | `/` | `GET /clientes/listar`, `GET /flores/listar` |
| Clientes | `/clientes` | `GET /clientes/listar`, `GET /clientes/consultar/{id}`, `POST /clientes/nuevo`, `PUT /clientes/actualizar`, `DELETE /clientes/eliminar/{id}` |
| Flores | `/flores` | `GET /flores/listar`, `GET /flores/consultar/{id}`, `POST /flores/nueva`, `PUT /flores/actualizar`, `DELETE /flores/eliminar/{id}`, `PATCH /flores/stock` |
| Pedidos | `/pedidos` | `GET /pedidos/listar?estado=&idCliente=`, `POST /pedidos/nuevo` |
| Detalle del pedido | `/pedidos/{id}` | `GET /pedidos/consultar/{id}`, `PATCH /pedidos/estado`, `PUT /pedidos/tarjeta/{id}`, `GET /pagos/pedido/{id}`, `GET /pagos/saldo/{id}`, `POST /pagos/nuevo` |

Las reglas de negocio (estados del pedido, saldo, stock, documento repetido...) las valida el backend;
si algo no se cumple, el frontend muestra el mensaje de error que devuelve la API.

## Estrategia de ramas

`main` (versión estable) · `develop` (integración) · `feature/...` (una por funcionalidad, se une por Pull Request).