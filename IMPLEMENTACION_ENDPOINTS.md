# Implementación de Endpoints Faltantes - BFF Agranelos

## Resumen de Cambios

Se realizó una revisión completa de los endpoints disponibles en `agranelos-functions-crud-create` (Azure Functions) y se identificaron y corrigieron las siguientes carencias en el BFF:

## Endpoints Implementados

### ✅ Productos (Ya existían)
- **GET** `/api/productos` - Listar todos los productos
- **GET** `/api/productos/{id}` - Obtener producto por ID
- **POST** `/api/productos` - Crear nuevo producto
- **PUT** `/api/productos/{id}` - Actualizar producto existente
- **DELETE** `/api/productos/{id}` - Eliminar producto

### ✨ Bodegas (NUEVOS)
- **GET** `/api/bodegas` - Listar todas las bodegas
- **GET** `/api/bodegas/{id}` - Obtener bodega por ID
- **POST** `/api/bodegas` - Crear nueva bodega
- **PUT** `/api/bodegas/{id}` - Actualizar bodega existente
- **DELETE** `/api/bodegas/{id}` - Eliminar bodega

### ✨ GraphQL (NUEVO)
- **GET** `/api/graphql` - Información del endpoint GraphQL
- **POST** `/api/graphql` - Ejecutar consultas GraphQL

## Archivos Creados

### 1. DTOs (Data Transfer Objects)

#### `BodegaDto.java`
```
/agranelos-bff/src/main/java/com/agranelos/bff/dto/BodegaDto.java
```
- DTO para operaciones de Bodega
- Validaciones con Jakarta Validation:
  - `nombre`: Obligatorio (NotBlank)
  - `ubicacion`: Obligatoria (NotBlank)
  - `capacidad`: Obligatoria y positiva (NotNull, Positive)

#### `GraphQLRequestDto.java`
```
/agranelos-bff/src/main/java/com/agranelos/bff/dto/GraphQLRequestDto.java
```
- DTO para peticiones GraphQL
- Campos:
  - `query`: String obligatorio con la consulta GraphQL
  - `variables`: Map opcional de variables
  - `operationName`: String opcional del nombre de la operación

### 2. Controllers

#### `BodegaController.java`
```
/agranelos-bff/src/main/java/com/agranelos/bff/controller/BodegaController.java
```
- Controlador REST para gestión de bodegas
- Utiliza WebClient reactivo para comunicarse con Azure Functions
- Manejo robusto de errores con respuestas apropiadas
- Todos los métodos CRUD implementados

#### `GraphQLController.java`
```
/agranelos-bff/src/main/java/com/agranelos/bff/controller/GraphQLController.java
```
- Controlador REST para ejecutar consultas GraphQL
- Endpoint GET para información/documentación
- Endpoint POST para ejecutar consultas
- Proxy transparente hacia el endpoint GraphQL de Azure Functions

### 3. Postman Collection Actualizada

#### `Agranelos-BFF.postman_collection.json`
Se actualizó la colección de Postman con:

**Estructura organizada en carpetas:**
1. **Productos** (5 endpoints)
2. **Bodegas** (5 endpoints - NUEVOS)
3. **GraphQL** (5 ejemplos - NUEVOS)

**Variables actualizadas:**
- `base_url`: http://localhost:8080
- `producto_id`: 1
- `bodega_id`: 1
- `username`: user
- `password`: myStrongPassword123

**Ejemplos de consultas GraphQL incluidos:**
- Listar productos
- Listar bodegas
- Producto por ID con variables
- Bodega por ID con variables

## Características Técnicas

### Validación
Todos los DTOs utilizan Jakarta Validation para asegurar:
- Datos obligatorios presentes
- Valores numéricos positivos donde corresponde
- Strings no vacíos

### Manejo de Errores
- Respuestas HTTP apropiadas (400, 404, 500)
- Mensajes de error descriptivos en formato JSON
- Manejo de errores 4xx y 5xx del backend

### Arquitectura Reactiva
- Uso de `WebClient` de Spring WebFlux
- Retorno de `Mono<ResponseEntity<Object>>` para procesamiento asíncrono
- Manejo reactivo de errores con `onErrorResume`

### Seguridad
- Autenticación HTTP Basic heredada de la configuración existente
- Todos los endpoints protegidos por el SecurityConfig existente

## Estructura de Datos

### Bodega
```json
{
  "id": 1,
  "nombre": "Bodega Central",
  "ubicacion": "Av. Principal 123, Ciudad",
  "capacidad": 5000
}
```

### GraphQL Request
```json
{
  "query": "{ productos { id nombre precio } }",
  "variables": {},
  "operationName": null
}
```

## Uso con Postman

1. Importar la colección actualizada: `Agranelos-BFF.postman_collection.json`
2. Las variables están preconfiguradas para localhost:8080
3. Los ejemplos incluyen datos de prueba listos para usar
4. La autenticación HTTP Basic se aplicará automáticamente

## Ejemplos de Consultas GraphQL

### Listar todos los productos
```graphql
{
  productos {
    id
    nombre
    descripcion
    precio
    cantidadEnStock
  }
}
```

### Listar todas las bodegas
```graphql
{
  bodegas {
    id
    nombre
    ubicacion
    capacidad
  }
}
```

### Obtener producto por ID
```graphql
query GetProducto($id: Int!) {
  producto(id: $id) {
    id
    nombre
    descripcion
    precio
    cantidadEnStock
  }
}
```
Variables:
```json
{
  "id": 1
}
```

### Obtener bodega por ID
```graphql
query GetBodega($id: Int!) {
  bodega(id: $id) {
    id
    nombre
    ubicacion
    capacidad
  }
}
```
Variables:
```json
{
  "id": 1
}
```

## Testing

Para probar los nuevos endpoints:

1. **Asegúrate de que Azure Functions esté ejecutándose:**
   - Los endpoints del BFF hacen proxy a Azure Functions
   - Verifica la variable `azure.functions.base-url` en `application.yml`

2. **Usa Postman:**
   - Importa la colección actualizada
   - Ejecuta los requests de ejemplo
   - Modifica los IDs y datos según necesites

3. **Testing manual con cURL:**

```bash
# Listar bodegas
curl -u user:myStrongPassword123 http://localhost:8080/api/bodegas

# Crear bodega
curl -u user:myStrongPassword123 -X POST http://localhost:8080/api/bodegas \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Bodega Norte","ubicacion":"Calle 123","capacidad":3000}'

# GraphQL - Listar productos
curl -u user:myStrongPassword123 -X POST http://localhost:8080/api/graphql \
  -H "Content-Type: application/json" \
  -d '{"query":"{ productos { id nombre precio } }"}'
```

## Compatibilidad

- ✅ Compatible con Spring Boot 3.x
- ✅ Jakarta EE (jakarta.validation)
- ✅ Spring WebFlux (WebClient reactivo)
- ✅ Postman Collection v2.1.0
- ✅ Azure Functions existentes sin modificaciones

## Próximos Pasos Sugeridos

1. Agregar tests unitarios para los nuevos controllers
2. Implementar cache para consultas frecuentes
3. Agregar métricas y observabilidad
4. Documentar con OpenAPI/Swagger
5. Implementar rate limiting
6. Agregar validaciones de negocio adicionales

## Notas Técnicas

- Los endpoints de Azure Functions no requieren modificaciones
- El BFF actúa como gateway/proxy hacia Azure Functions
- La autenticación se maneja en el BFF, no se propaga a Azure Functions
- Los DTOs son independientes de los modelos de Azure Functions
- El código sigue los patrones existentes en `ProductoController.java`

---

**Autor:** GitHub Copilot  
**Fecha:** 4 de Octubre, 2025  
**Proyecto:** Agranelos BFF - Sistema de Inventario
