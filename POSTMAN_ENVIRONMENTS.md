# 🌐 Configuración de Entornos - Postman Collection

## Resumen

La colección de Postman de Agranelos BFF ahora incluye **soporte completo para múltiples entornos**, permitiendo cambiar fácilmente entre desarrollo local, contenedores Docker/Podman, y despliegues en la nube (AWS/Azure).

---

## ✅ Respuesta a tu Pregunta

### ¿La colección está actualizada?
**Sí** ✅ - La colección incluye:
- ✅ Todos los 16 endpoints implementados (5 Productos + 5 Bodegas + 6 GraphQL)
- ✅ Autenticación HTTP Basic configurada
- ✅ Variables parametrizadas (`{{base_url}}`, `{{producto_id}}`, `{{bodega_id}}`)
- ✅ Ejemplos de datos para cada endpoint

### ¿Soporta hosts configurables?
**Sí** ✅ - Ahora incluye 4 archivos de entorno:
1. **Local** - `http://localhost:8080` (desarrollo local)
2. **Docker/Podman** - `http://localhost:8080` (contenedores)
3. **AWS** - Configurable para despliegue en AWS
4. **Azure** - Configurable para despliegue en Azure

---

## 📦 Archivos Creados

### Ubicación: `/postman/`

```
postman/
├── README.md                        ← Guía completa de uso
├── Local.postman_environment.json   ← Entorno local
├── Docker.postman_environment.json  ← Entorno Docker/Podman
├── AWS.postman_environment.json     ← Entorno AWS
└── Azure.postman_environment.json   ← Entorno Azure
```

### Colección Principal
- `Agranelos-BFF.postman_collection.json` (directorio raíz)
  - Actualizada con descripción mejorada
  - Información sobre entornos disponibles
  - Instrucciones de uso incluidas

---

## 🚀 Cómo Usar

### Paso 1: Importar en Postman

#### Importar Colección
```
1. Abrir Postman
2. Click en "Import"
3. Seleccionar "Agranelos-BFF.postman_collection.json"
4. Click en "Import"
```

#### Importar Entornos
```
1. Click en "Import"
2. Seleccionar todos los archivos de la carpeta "postman/"
3. Click en "Import"
```

### Paso 2: Seleccionar Entorno

En el dropdown superior derecho de Postman, seleccionar el entorno deseado:

#### Para desarrollo local (Spring Boot):
- Seleccionar: **Local Environment**
- URL automática: `http://localhost:8080`

#### Para contenedores locales (Docker/Podman):
- Seleccionar: **Docker/Podman Environment**
- URL automática: `http://localhost:8080`

#### Para AWS:
- Seleccionar: **AWS Environment**
- Editar `base_url` con tu dominio real

#### Para Azure:
- Seleccionar: **Azure Environment**
- Editar `base_url` con tu App Service URL

---

## 🔧 Configuración de Entornos Cloud

### AWS Configuration

1. Click en el ícono del ojo 👁️ (junto al selector de entorno)
2. Click en **Edit** en "AWS Environment"
3. Actualizar `base_url`:

**Opciones comunes:**
```json
// Load Balancer
"base_url": "https://api.agranelos.com"

// API Gateway + Lambda
"base_url": "https://abc123.execute-api.us-east-1.amazonaws.com/prod"

// Elastic Beanstalk
"base_url": "http://agranelos-bff.us-east-1.elasticbeanstalk.com"

// ECS/Fargate con ALB
"base_url": "https://agranelos-alb-123456.us-east-1.elb.amazonaws.com"
```

### Azure Configuration

1. Click en el ícono del ojo 👁️
2. Click en **Edit** en "Azure Environment"
3. Actualizar `base_url`:

**Opciones comunes:**
```json
// Azure App Service
"base_url": "https://agranelos-bff.azurewebsites.net"

// Azure Container Instances
"base_url": "http://agranelos-bff.eastus.azurecontainer.io"

// Azure Kubernetes Service
"base_url": "https://agranelos.eastus.cloudapp.azure.com"

// Azure Front Door
"base_url": "https://agranelos-bff.azurefd.net"
```

---

## 📊 Variables por Entorno

Todas las configuraciones incluyen las mismas variables:

| Variable | Descripción | Valor Default |
|----------|-------------|---------------|
| `base_url` | URL base del BFF | Varía según entorno |
| `producto_id` | ID para pruebas de producto | `1` |
| `bodega_id` | ID para pruebas de bodega | `1` |
| `username` | Usuario HTTP Basic | `user` |
| `password` | Contraseña HTTP Basic | `myStrongPassword123` |
| `environment` | Identificador del entorno | `local`, `aws`, `azure`, `docker` |

---

## 🔐 Seguridad

### Credenciales en Entornos Cloud

Para entornos de producción, considera:

1. **No commitear passwords reales:**
   ```json
   "password": {
     "value": "",
     "type": "secret"
   }
   ```

2. **Usar Postman Cloud con encriptación:**
   - Variables tipo "secret" se encriptan
   - No se exportan en plain text

3. **Alternativamente, usar variables locales:**
   - En Postman, usar "Current Value" en vez de "Initial Value"
   - Current Value no se exporta ni comparte

---

## 🧪 Ejemplos de Uso

### Probar Localmente (Local Environment)
```bash
# 1. Iniciar Spring Boot
mvn spring-boot:run

# 2. En Postman:
#    - Seleccionar "Local Environment"
#    - Ejecutar cualquier request
```

### Probar con Docker (Docker Environment)
```bash
# 1. Iniciar contenedor
podman run -d --name agranelos-bff -p 8080:8080 \
  -e AZURE_FUNCTIONS_BASE_URL="https://..." \
  agranelos-bff:latest

# 2. En Postman:
#    - Seleccionar "Docker/Podman Environment"
#    - Ejecutar cualquier request
```

### Probar en AWS (AWS Environment)
```bash
# 1. Configurar base_url con tu URL de AWS

# 2. En Postman:
#    - Seleccionar "AWS Environment"
#    - Actualizar base_url si es necesario
#    - Ejecutar cualquier request
```

---

## 📈 Casos de Uso

### Desarrollo Local → Staging → Producción

1. **Desarrollo:**
   - Usar "Local Environment"
   - Desarrollar y probar localmente
   
2. **Testing con Container:**
   - Usar "Docker/Podman Environment"
   - Probar imagen antes de desplegar

3. **Staging en AWS:**
   - Copiar "AWS Environment" → "AWS Staging"
   - Configurar URL de staging
   - Ejecutar tests de integración

4. **Producción:**
   - Usar "AWS Environment"
   - URL de producción configurada
   - Tests de smoke y monitoring

---

## 🔄 Cambiar entre Entornos Rápidamente

En Postman, puedes:

1. **Usar atajos de teclado:**
   - `Ctrl/Cmd + E` para cambiar entorno

2. **Duplicar entornos:**
   - Click derecho → Duplicate
   - Útil para staging vs production

3. **Compartir entornos:**
   - Exportar → Compartir con equipo
   - Importar en otros workspaces

---

## 🎯 Mejoras Implementadas

### Antes ❌
- Solo variable `base_url` en la colección
- Un solo valor hardcoded: `http://localhost:8080`
- No soportaba fácilmente otros entornos
- Había que editar manualmente la colección

### Ahora ✅
- 4 archivos de entorno separados
- Fácil cambio entre local, Docker, AWS, Azure
- Variables bien organizadas
- Credenciales configurables por entorno
- README con instrucciones detalladas

---

## 📝 Mantenimiento

### Agregar Nuevo Entorno

1. Duplicar un archivo existente:
   ```bash
   cp postman/Local.postman_environment.json postman/Production.postman_environment.json
   ```

2. Editar valores:
   ```json
   {
     "name": "Production",
     "values": [
       {
         "key": "base_url",
         "value": "https://prod.agranelos.com"
       }
     ]
   }
   ```

3. Importar en Postman

### Actualizar Colección

1. Exportar desde Postman (Collection v2.1)
2. Reemplazar `Agranelos-BFF.postman_collection.json`
3. Commit y push

---

## 🆘 Troubleshooting

### No veo los entornos en Postman
- ✅ Verificar que importaste los archivos `.postman_environment.json`
- ✅ Revisar en "Environments" tab (ícono de ojo)

### Los requests van al lugar equivocado
- ✅ Verificar que el entorno correcto está seleccionado
- ✅ Verificar valor de `{{base_url}}` en el entorno

### Error de autenticación
- ✅ Verificar `username` y `password` en el entorno
- ✅ Verificar que la autenticación está habilitada a nivel de colección

---

## 📚 Recursos

- [Postman Environments Documentation](https://learning.postman.com/docs/sending-requests/managing-environments/)
- [Variables in Postman](https://learning.postman.com/docs/sending-requests/variables/)
- [Collection Runner](https://learning.postman.com/docs/running-collections/intro-to-collection-runs/)

---

**Documentación creada:** 4 de Octubre, 2025  
**Versión:** 1.0.0  
**Soporte:** Local, Docker/Podman, AWS, Azure
