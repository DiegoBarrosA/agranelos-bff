# 📮 Colección Postman - Agranelos BFF

Esta carpeta contiene la colección de Postman y los archivos de entorno para probar el BFF de Agranelos.

---

## 📁 Archivos

### Colección Principal
- **`Agranelos-BFF.postman_collection.json`** (en el directorio raíz)
  - Colección completa con todos los endpoints
  - 16 requests organizados en 3 carpetas
  - Autenticación HTTP Basic configurada

### Entornos (Environments)
1. **`Local.postman_environment.json`**
   - Para desarrollo local (Spring Boot corriendo directamente)
   - URL: `http://localhost:8080`

2. **`Docker.postman_environment.json`**
   - Para contenedores Docker/Podman locales
   - URL: `http://localhost:8080`

3. **`AWS.postman_environment.json`**
   - Para despliegue en AWS (ECS, Elastic Beanstalk, etc.)
   - URL: `https://your-bff-domain.amazonaws.com` (personalizar)

4. **`Azure.postman_environment.json`**
   - Para despliegue en Azure App Service
   - URL: `https://your-bff-app.azurewebsites.net` (personalizar)

---

## 🚀 Guía de Uso

### 1. Importar en Postman

#### Importar la Colección
1. Abrir Postman
2. Click en **Import** (esquina superior izquierda)
3. Arrastrar o seleccionar `Agranelos-BFF.postman_collection.json`
4. Click en **Import**

#### Importar los Entornos
1. Click en **Import**
2. Seleccionar todos los archivos `.postman_environment.json` de la carpeta `postman/`
3. Click en **Import**

### 2. Seleccionar Entorno

1. En el dropdown superior derecho de Postman (dice "No Environment")
2. Seleccionar el entorno que desees:
   - **Local** → Para desarrollo local
   - **Docker/Podman** → Para contenedores locales
   - **AWS** → Para producción en AWS
   - **Azure** → Para producción en Azure

### 3. Configurar Variables (si es necesario)

#### Para entornos en la nube (AWS/Azure):
1. Click en el ícono del ojo 👁️ (junto al selector de entorno)
2. Click en **Edit** junto al entorno seleccionado
3. Actualizar `base_url` con tu URL real
4. Guardar cambios

#### Ejemplo para AWS:
```json
"base_url": "https://api.agranelos.com"
```

#### Ejemplo para Azure:
```json
"base_url": "https://agranelos-bff.azurewebsites.net"
```

### 4. Ejecutar Requests

1. Expandir las carpetas en la colección:
   - 📁 Productos (5 requests)
   - 📁 Bodegas (5 requests)
   - 📁 GraphQL (5 requests)

2. Seleccionar cualquier request
3. Click en **Send**

---

## 📊 Estructura de la Colección

```
Agranelos BFF - API Completa
├── 📁 Productos
│   ├── GET    Listar todos los productos
│   ├── GET    Obtener producto por ID
│   ├── POST   Crear nuevo producto
│   ├── PUT    Actualizar producto existente
│   └── DELETE Eliminar producto
│
├── 📁 Bodegas
│   ├── GET    Listar todas las bodegas
│   ├── GET    Obtener bodega por ID
│   ├── POST   Crear nueva bodega
│   ├── PUT    Actualizar bodega existente
│   └── DELETE Eliminar bodega
│
└── 📁 GraphQL
    ├── GET  Info del endpoint GraphQL
    ├── POST Consulta GraphQL - Listar productos
    ├── POST Consulta GraphQL - Listar bodegas
    ├── POST Consulta GraphQL - Producto por ID
    └── POST Consulta GraphQL - Bodega por ID
```

---

## 🔐 Autenticación

Todos los endpoints requieren **HTTP Basic Authentication**.

### Credenciales por defecto:
- **Username:** `user`
- **Password:** `myStrongPassword123`

La autenticación está configurada a nivel de colección, por lo que aplica automáticamente a todos los requests.

### Cambiar credenciales:
1. En Postman, hacer click derecho sobre la colección
2. Seleccionar **Edit**
3. Ir a la pestaña **Authorization**
4. Actualizar username y password
5. Guardar

---

## 🧪 Ejemplos de Requests

### Crear una Bodega
```http
POST {{base_url}}/api/bodegas
Content-Type: application/json
Authorization: Basic dXNlcjpteVN0cm9uZ1Bhc3N3b3JkMTIz

{
  "nombre": "Bodega Central",
  "ubicacion": "Av. Principal 123, Ciudad",
  "capacidad": 5000
}
```

### Consulta GraphQL - Productos
```http
POST {{base_url}}/api/graphql
Content-Type: application/json
Authorization: Basic dXNlcjpteVN0cm9uZ1Bhc3N3b3JkMTIz

{
  "query": "{ productos { id nombre precio } }",
  "variables": {},
  "operationName": null
}
```

---

## 🔧 Variables de Entorno

Todas las variables están definidas en cada archivo de entorno:

| Variable | Descripción | Valor por defecto |
|----------|-------------|-------------------|
| `base_url` | URL base del BFF | `http://localhost:8080` |
| `producto_id` | ID de producto para pruebas | `1` |
| `bodega_id` | ID de bodega para pruebas | `1` |
| `username` | Usuario para auth | `user` |
| `password` | Contraseña para auth | `myStrongPassword123` |
| `environment` | Nombre del entorno | `local`, `aws`, `azure`, `docker` |

---

## 🌐 Configuración por Entorno

### Local Development
```json
{
  "base_url": "http://localhost:8080"
}
```
**Uso:** Desarrollo local con Spring Boot

### Docker/Podman
```json
{
  "base_url": "http://localhost:8080"
}
```
**Uso:** Contenedor corriendo localmente en puerto 8080

### AWS Production
```json
{
  "base_url": "https://your-bff-domain.amazonaws.com"
}
```
**Opciones de despliegue:**
- AWS Elastic Beanstalk
- AWS ECS (Fargate o EC2)
- AWS App Runner
- Load Balancer + EC2

### Azure Production
```json
{
  "base_url": "https://your-bff-app.azurewebsites.net"
}
```
**Opciones de despliegue:**
- Azure App Service (recomendado)
- Azure Container Instances
- Azure Kubernetes Service (AKS)

---

## 🧰 Pruebas Automatizadas

### Ejecutar toda la colección
1. Click derecho en la colección
2. Seleccionar **Run collection**
3. Seleccionar todos los requests
4. Click en **Run Agranelos BFF**

### Variables dinámicas
Los requests ya incluyen variables para IDs:
- `{{producto_id}}` → Se puede cambiar en el entorno
- `{{bodega_id}}` → Se puede cambiar en el entorno

### Scripts de ejemplo (Pre-request o Tests)

#### Guardar ID creado (en Tests tab):
```javascript
// Después de POST /api/productos o /api/bodegas
var response = pm.response.json();
if (response.id) {
    pm.environment.set("ultimo_id_creado", response.id);
}
```

#### Test de respuesta exitosa:
```javascript
pm.test("Status code is 200", function () {
    pm.response.to.have.status(200);
});

pm.test("Response has data", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData).to.not.be.empty;
});
```

---

## 🔄 Sincronización de Cambios

Si modificas la colección o los entornos:

1. **Exportar desde Postman:**
   - Click derecho → Export
   - Formato: Collection v2.1 / Environment
   - Guardar en esta carpeta

2. **Commit cambios:**
   ```bash
   git add postman/
   git commit -m "Update Postman collection/environments"
   git push
   ```

---

## 📝 Notas

- La colección está sincronizada con los endpoints implementados
- Todos los endpoints incluyen ejemplos de datos
- Los entornos son fácilmente configurables
- La autenticación se maneja automáticamente
- Compatible con Postman Cloud, Desktop y CLI

---

## 🆘 Troubleshooting

### Error de conexión
- ✅ Verificar que el BFF está corriendo
- ✅ Verificar que el entorno seleccionado es correcto
- ✅ Verificar que `base_url` apunta a la URL correcta

### Error 401 Unauthorized
- ✅ Verificar credenciales en Authorization tab
- ✅ Verificar que `username` y `password` son correctos en el entorno

### Error 404 Not Found
- ✅ Verificar que el endpoint existe en el BFF
- ✅ Verificar que la URL es correcta (incluye `/api/`)
- ✅ Verificar que el ID del recurso existe

---

## 🎯 Recursos Adicionales

- [Documentación del BFF](../README.md)
- [Guía de Implementación](../IMPLEMENTACION_ENDPOINTS.md)
- [Mapeo de Endpoints](../ENDPOINTS_MAPPING.md)
- [Pruebas con Podman](../PRUEBAS_PODMAN.md)

---

**Última actualización:** 4 de Octubre, 2025  
**Versión de la colección:** 1.0.0  
**Total de requests:** 16 (5 Productos + 5 Bodegas + 6 GraphQL)
