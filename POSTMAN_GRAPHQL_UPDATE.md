# 🚀 Guía de GraphQL - Colección Postman

## Endpoints GraphQL Disponibles

La colección incluye 14 requests GraphQL que cubren todas las operaciones disponibles:

### Queries (Consultas de Lectura)
1. **Health Check** - Verificar estado del API GraphQL
2. **Listar productos (campos completos)** - Incluyendo fechas
3. **Producto por ID con variables** - Usa `{{producto_id}}`
4. **Productos (campos básicos)** - Solo id, nombre, precio
5. **Listar bodegas (campos completos)** - Incluyendo fechas
6. **Bodega por ID con variables** - Usa `{{bodega_id}}`
7. **Consulta múltiple** - Productos y bodegas en una sola query

### Mutations (Operaciones de Escritura)
8. **Crear producto** - Mutation con input completo
9. **Actualizar producto** - Mutation con ProductoUpdateInput
10. **Eliminar producto** - Mutation con ID
11. **Crear bodega** - Mutation con input completo
12. **Actualizar bodega** - Mutation con BodegaUpdateInput
13. **Eliminar bodega** - Mutation con ID
14. **Info endpoint** - GET request para información del endpoint

---

## 📊 Resumen de la Colección

### Total de Requests por Categoría

| Categoría | Requests | Descripción |
|-----------|----------|-------------|
| **Productos** | 5 | CRUD completo REST |
| **Bodegas** | 5 | CRUD completo REST |
| **GraphQL Queries** | 7 | Consultas de lectura |
| **GraphQL Mutations** | 6 | Operaciones de escritura |
| **GraphQL Info** | 1 | Información del endpoint |
| **TOTAL** | **24** | **Cobertura completa** |

---

## 🧪 Ejemplos de Uso

### 1. Health Check
```bash
curl -u user:myStrongPassword123 -X POST http://localhost:8080/api/graphql \
  -H "Content-Type: application/json" \
  -d '{"query":"{ health }","variables":{}}'
```

**Resultado:**
```json
{
  "data": {
    "health": "GraphQL API funcionando correctamente"
  }
}
```

### 2. Listar Productos
```bash
curl -u user:myStrongPassword123 -X POST http://localhost:8080/api/graphql \
  -H "Content-Type: application/json" \
  -d '{"query":"{ productos { id nombre precio cantidad } }"}'
```

**Resultado:**
```json
{
  "data": {
    "productos": [
      {
        "id": "2",
        "nombre": "Frijoles",
        "precio": 30.0,
        "cantidad": 200
      }
    ]
  }
}
```

### 3. Consulta Múltiple
```bash
curl -u user:myStrongPassword123 -X POST http://localhost:8080/api/graphql \
  -H "Content-Type: application/json" \
  -d '{"query":"{ productos { id nombre precio } bodegas { id nombre ubicacion } }"}'
```

**Resultado:**
```json
{
  "data": {
    "productos": [...],
    "bodegas": [...]
  }
}
```

---

## 📋 Esquema GraphQL Completo

### Tipos

```graphql
type Producto {
    id: ID!
    nombre: String!
    descripcion: String
    precio: Float!
    cantidad: Int!              # ← Campo correcto
    fechaCreacion: DateTime
    fechaActualizacion: DateTime
}

type Bodega {
    id: ID!
    nombre: String!
    ubicacion: String!
    capacidad: Int!
    fechaCreacion: DateTime
    fechaActualizacion: DateTime
}
```

### Queries Disponibles

```graphql
type Query {
    # Productos
    productos: [Producto!]!
    producto(id: ID!): Producto
    
    # Bodegas
    bodegas: [Bodega!]!
    bodega(id: ID!): Bodega
    
    # Health check
    health: String!
}
```

### Mutations Disponibles

```graphql
type Mutation {
    # Productos
    crearProducto(input: ProductoInput!): ProductoResponse!
    actualizarProducto(input: ProductoUpdateInput!): ProductoResponse!
    eliminarProducto(id: ID!): DeleteResponse!
    
    # Bodegas
    crearBodega(input: BodegaInput!): BodegaResponse!
    actualizarBodega(input: BodegaUpdateInput!): BodegaResponse!
    eliminarBodega(id: ID!): DeleteResponse!
}
```

---

## 🎯 Casos de Uso Cubiertos

### Lectura (Queries)
- ✅ Obtener todos los recursos
- ✅ Obtener recurso por ID
- ✅ Selección de campos específicos
- ✅ Consultas múltiples simultáneas
- ✅ Verificación de salud del API

### Escritura (Mutations)
- ✅ Crear recursos (productos y bodegas)
- ✅ Actualizar recursos parcialmente
- ✅ Eliminar recursos
- ✅ Respuestas estructuradas con success/error

### Funcionalidades Avanzadas
- ✅ Variables tipadas (`$id: ID!`)
- ✅ Operation names para debugging
- ✅ Inputs complejos (ProductoInput, BodegaInput)
- ✅ Respuestas tipo union (success + data + error)

---

## 🔧 Uso de Variables en Postman

Los requests GraphQL utilizan variables de Postman:

```json
{
  "query": "query GetProducto($id: ID!) { 
    producto(id: $id) { 
      id nombre precio cantidad 
    } 
  }",
  "variables": {
    "id": "{{producto_id}}"
  }
}
```

**Beneficio:** Cambiar `{{producto_id}}` en el entorno y se actualiza en todos los requests.

---

## 📦 Estructura Mejorada

```
GraphQL (14 requests)
├── 📄 Info del endpoint GraphQL (GET)
├── 📄 Health Check
├── 📁 Queries (6 requests)
│   ├── Listar todos los productos
│   ├── Producto por ID (con variables)
│   ├── Productos (solo campos básicos)
│   ├── Listar todas las bodegas
│   ├── Bodega por ID (con variables)
│   └── Consulta múltiple (productos y bodegas)
└── 📁 Mutations (6 requests)
    ├── Crear producto
    ├── Actualizar producto
    ├── Eliminar producto
    ├── Crear bodega
    ├── Actualizar bodega
    └── Eliminar bodega
```

---

## ✨ Características de la Colección

### 1. Cobertura Completa
- Todas las queries del esquema GraphQL
- Todas las mutations disponibles
- Health check incluido

### 2. Variables Parametrizadas
- Usa `{{producto_id}}` y `{{bodega_id}}` del entorno
- Fácil cambio de IDs sin editar queries

### 3. Ejemplos Realistas
- Datos de ejemplo completos
- Nombres descriptivos
- Comentarios inline donde necesario

### 4. Organización Clara
- Separación entre Queries y Mutations
- Nombres descriptivos de requests
- Agrupación lógica

---

## 🧰 Testing Automatizado

### Ejecutar toda la carpeta GraphQL
1. En Postman, click derecho en "GraphQL"
2. "Run folder"
3. Seleccionar todos los 14 requests
4. "Run GraphQL"

### Scripts de Test Sugeridos

Para agregar en la pestaña "Tests" de cada request:

```javascript
// Verificar respuesta exitosa
pm.test("Status code is 200", () => {
    pm.response.to.have.status(200);
});

// Verificar que hay data
pm.test("Response has data", () => {
    const jsonData = pm.response.json();
    pm.expect(jsonData.data).to.exist;
});

// Para mutations: verificar success
pm.test("Mutation was successful", () => {
    const jsonData = pm.response.json();
    if (jsonData.data) {
        const mutation = Object.values(jsonData.data)[0];
        pm.expect(mutation.success).to.be.true;
    }
});
```

---

## 🎓 Recursos de Aprendizaje

### Documentación GraphQL
- [GraphQL Official Docs](https://graphql.org/)
- [GraphQL Best Practices](https://graphql.org/learn/best-practices/)

### En este proyecto
- Esquema: `agranelos-functions-crud-create/src/main/resources/schema.graphqls`
- Implementación: `agranelos-functions-crud-create/src/main/java/com/agranelos/inventario/graphql/`

---

## 🚀 Próximos Pasos

### Recomendaciones
1. **Importar colección actualizada** en Postman
2. **Probar cada request** para familiarizarte
3. **Ejecutar folder completo** para regression testing
4. **Agregar tests automatizados** según ejemplos arriba
5. **Documentar casos de uso** específicos de tu proyecto

### Extensiones Futuras
- Agregar subscriptions GraphQL (si se implementan)
- Agregar fragments para reutilización
- Crear collections para diferentes roles de usuario
- Integrar con Newman para CI/CD

---

**Total de endpoints GraphQL:** 14 (7 queries + 6 mutations + 1 info)
