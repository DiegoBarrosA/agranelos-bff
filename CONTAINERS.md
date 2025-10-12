# Container Configuration

This directory contains Docker/Podman configuration for the Agranelos BFF microservice.

## Quick Start

### Prerequisites
- Docker or Podman installed
- Java 17+ and Maven 3.6+ for building

### Build and Run

```bash
# Build and start the service
docker-compose up -d

# Or with Podman
podman-compose up -d

# Build only (no start)
docker-compose build
```

## Service Configuration

### agranelos-bff
- **Port**: 8080
- **Framework**: Spring Boot 3.x
- **Runtime**: Eclipse Temurin 17 JRE Alpine
- **Health Check**: `/actuator/health` endpoint
- **Security**: Runs as non-root user (appuser:1000)

## Environment Variables

Create a `.env` file in this directory:

```bash
# Azure Functions Backend
AZURE_FUNCTIONS_BASE_URL=http://localhost:7071/api

# Spring Security (Basic Auth)
SPRING_SECURITY_USER_NAME=user
SPRING_SECURITY_USER_PASSWORD=myStrongPassword123

# Spring Profile
SPRING_PROFILES_ACTIVE=prod

# JVM Options (optional)
JAVA_OPTS=-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0
```

## Development Workflow

### Local Development
```bash
# Start with local Azure Functions
export AZURE_FUNCTIONS_BASE_URL=http://host.docker.internal:7071/api
docker-compose up -d
```

### Production Testing
```bash
# Use deployed Azure Functions
export AZURE_FUNCTIONS_BASE_URL=https://your-functions-app.azurewebsites.net/api
docker-compose up -d
```

### Code Changes
```bash
# Rebuild after changes
docker-compose down
docker-compose build --no-cache
docker-compose up -d
```

## Integration with Azure Functions

The BFF connects to Azure Functions backend. Ensure the functions are running:

### Option 1: Local Functions
```bash
# In agranelos-functions-crud-create directory
mvn clean package
func host start
```

### Option 2: Containerized Functions
```bash
# In agranelos-functions-crud-create directory
docker-compose up -d azure-functions
```

### Option 3: Full-Stack Container
```bash
# In agranelos-functions-crud-create directory
docker-compose --profile full-stack up -d
```

## API Testing

### Health Check
```bash
curl http://localhost:8080/actuator/health
```

### API Endpoints
```bash
# Products
curl -u user:myStrongPassword123 http://localhost:8080/api/v1/productos

# Warehouses
curl -u user:myStrongPassword123 http://localhost:8080/api/v1/bodegas

# Warehouse deletion with product consultation
curl -u user:myStrongPassword123 http://localhost:8080/api/v1/bodegas/1/productos
```

## Security Features

### Container Security
- **Non-root execution**: Application runs as `appuser` (UID 1000)
- **Minimal base image**: Alpine Linux with JRE only
- **Read-only filesystem**: Application files are immutable
- **Resource limits**: JVM configured for container memory limits

### Application Security
- **Basic Authentication**: Spring Security with configurable credentials
- **CORS Configuration**: Controlled cross-origin access
- **Input Validation**: Jakarta Validation for request bodies
- **Error Handling**: Sanitized error responses

## Performance Optimization

### Docker Build
- **Multi-stage build**: Separate build and runtime environments
- **Layer caching**: Dependencies cached separately from source code
- **Minimal artifacts**: Only JAR file in final image

### JVM Configuration
- **Container-aware**: Automatic memory detection
- **Memory percentage**: 75% of container memory allocated to heap
- **Startup optimization**: Fast application startup

## Troubleshooting

### Common Issues

1. **Connection refused**: Check if Azure Functions are running
2. **Authentication failed**: Verify Spring Security credentials
3. **Out of memory**: Adjust JAVA_OPTS memory settings

### Debugging

```bash
# View container logs
docker-compose logs agranelos-bff

# Follow logs in real-time
docker-compose logs -f agranelos-bff

# Execute commands in container
docker-compose exec agranelos-bff sh

# Check container health
docker-compose ps
```

### Network Connectivity

```bash
# Test from container to Azure Functions
docker-compose exec agranelos-bff curl http://host.docker.internal:7071/api/GetProductos

# Check container network
docker network ls
docker network inspect agranelos-bff_agranelos-network
```

## Production Deployment

For production environments:

1. **Azure Container Instances**: Single container deployment
2. **Azure App Service**: Managed container hosting
3. **Kubernetes**: Scalable orchestration with ConfigMaps/Secrets
4. **Environment Variables**: Use Azure Key Vault for secrets
5. **Monitoring**: Configure Application Insights
6. **Scaling**: Implement horizontal pod autoscaling