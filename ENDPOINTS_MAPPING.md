# Agranelos BFF - Endpoints Mapping

## Azure Functions → BFF Endpoints

### Productos ✅ (Ya implementados)
```
Azure Function                    →  BFF Endpoint
─────────────────────────────────────────────────────────
GET  /productos                   →  GET  /api/productos
GET  /productos/{id}              →  GET  /api/productos/{id}
POST /productos                   →  POST /api/productos
PUT  /productos/{id}              →  PUT  /api/productos/{id}
DELETE /productos/{id}            →  DELETE /api/productos/{id}
```

### Bodegas ✨ (RECIÉN IMPLEMENTADOS)
```
Azure Function                    →  BFF Endpoint
─────────────────────────────────────────────────────────
GET  /bodegas                     →  GET  /api/bodegas
GET  /bodegas/{id}                →  GET  /api/bodegas/{id}
POST /bodegas                     →  POST /api/bodegas
PUT  /bodegas/{id}                →  PUT  /api/bodegas/{id}
DELETE /bodegas/{id}              →  DELETE /api/bodegas/{id}
```

### GraphQL ✨ (RECIÉN IMPLEMENTADO)
```
Azure Function                    →  BFF Endpoint
─────────────────────────────────────────────────────────
POST /graphql                     →  GET  /api/graphql (info)
                                  →  POST /api/graphql (query)
```

### Otros Endpoints de Azure Functions
```
Endpoint                          Estado
─────────────────────────────────────────────────────────
POST /init (InitializeDatabase)   No expuesto en BFF (operación interna)
```

## Archivos Creados

```
agranelos-bff/
├── src/main/java/com/agranelos/bff/
│   ├── controller/
│   │   ├── BodegaController.java           ✨ NUEVO
│   │   ├── GraphQLController.java          ✨ NUEVO
│   │   └── ProductoController.java         ✅ Existente
│   └── dto/
│       ├── BodegaDto.java                  ✨ NUEVO
│       ├── GraphQLRequestDto.java          ✨ NUEVO
│       └── ProductoDto.java                ✅ Existente
├── Agranelos-BFF.postman_collection.json   🔄 ACTUALIZADO
└── IMPLEMENTACION_ENDPOINTS.md             ✨ NUEVO (documentación)
```

## Totales

- **Controllers creados:** 2
- **DTOs creados:** 2
- **Endpoints nuevos:** 11
- **Endpoints totales en BFF:** 16

## Estado de Implementación: ✅ COMPLETO

Todos los endpoints de las Azure Functions están ahora disponibles a través del BFF.
