package com.agranelos.bff.controller;

import com.agranelos.bff.dto.BodegaDto;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/bodegas")
public class BodegaController {

    private final WebClient webClient;
    private final String azureFunctionsBaseUrl;

    public BodegaController(
        @Value("${azure.functions.base-url}") String azureFunctionsBaseUrl
    ) {
        this.azureFunctionsBaseUrl = azureFunctionsBaseUrl;
        this.webClient = WebClient.builder()
            .baseUrl(azureFunctionsBaseUrl)
            .build();
    }

    private String buildFunctionUrl(String path) {
        return azureFunctionsBaseUrl + path;
    }

    @GetMapping
    public Mono<ResponseEntity<Object>> listarBodegas() {
        String url = buildFunctionUrl("/bodegas");
        return webClient
            .get()
            .uri(url)
            .retrieve()
            .bodyToMono(Object.class)
            .map(ResponseEntity::ok)
            .onErrorResume(e ->
                Mono.just(
                    ResponseEntity.status(
                        HttpStatus.INTERNAL_SERVER_ERROR
                    ).body(
                        Map.of(
                            "error",
                            "No se pudo obtener la lista de bodegas",
                            "detalle",
                            e.getMessage()
                        )
                    )
                )
            );
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Object>> obtenerBodega(
        @PathVariable String id
    ) {
        String url = buildFunctionUrl("/bodegas/" + id);
        return webClient
            .get()
            .uri(url)
            .retrieve()
            .bodyToMono(Object.class)
            .map(ResponseEntity::ok)
            .onErrorResume(e ->
                Mono.just(
                    ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        Map.of(
                            "error",
                            "Bodega no encontrada",
                            "detalle",
                            e.getMessage()
                        )
                    )
                )
            );
    }

    @PostMapping
    public Mono<ResponseEntity<Object>> crearBodega(
        @Valid @RequestBody BodegaDto bodegaDto
    ) {
        String url = buildFunctionUrl("/bodegas");
        return webClient
            .post()
            .uri(url)
            .bodyValue(bodegaDto)
            .retrieve()
            .onStatus(
                status -> status.is4xxClientError(),
                response ->
                    response
                        .bodyToMono(String.class)
                        .map(body -> new RuntimeException("Error 4xx: " + body))
            )
            .onStatus(
                status -> status.is5xxServerError(),
                response ->
                    response
                        .bodyToMono(String.class)
                        .map(body -> new RuntimeException("Error 5xx: " + body))
            )
            .bodyToMono(Object.class)
            .map(body -> ResponseEntity.status(HttpStatus.CREATED).body(body))
            .onErrorResume(e ->
                Mono.just(
                    ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        Map.of(
                            "error",
                            "No se pudo crear la bodega",
                            "detalle",
                            e.getMessage(),
                            "url",
                            url
                        )
                    )
                )
            );
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Object>> actualizarBodega(
        @PathVariable String id,
        @Valid @RequestBody BodegaDto bodegaDto
    ) {
        String url = buildFunctionUrl("/bodegas/" + id);
        return webClient
            .put()
            .uri(url)
            .bodyValue(bodegaDto)
            .retrieve()
            .onStatus(
                status -> status.is4xxClientError(),
                response ->
                    response
                        .bodyToMono(String.class)
                        .map(body -> new RuntimeException("Error 4xx: " + body))
            )
            .onStatus(
                status -> status.is5xxServerError(),
                response ->
                    response
                        .bodyToMono(String.class)
                        .map(body -> new RuntimeException("Error 5xx: " + body))
            )
            .bodyToMono(Object.class)
            .map(ResponseEntity::ok)
            .onErrorResume(e ->
                Mono.just(
                    ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        Map.of(
                            "error",
                            "No se pudo actualizar la bodega",
                            "detalle",
                            e.getMessage(),
                            "url",
                            url,
                            "bodegaId",
                            id
                        )
                    )
                )
            );
    }

    @GetMapping("/{id}/productos")
    public Mono<ResponseEntity<Object>> obtenerProductosDeBodega(
        @PathVariable String id
    ) {
        String url = buildFunctionUrl("/bodegas/" + id + "/productos");
        return webClient
            .get()
            .uri(url)
            .retrieve()
            .bodyToMono(Object.class)
            .map(ResponseEntity::ok)
            .onErrorResume(e ->
                Mono.just(
                    ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        Map.of(
                            "error",
                            "No se pudieron obtener los productos de la bodega",
                            "detalle",
                            e.getMessage(),
                            "bodegaId",
                            id
                        )
                    )
                )
            );
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Object>> eliminarBodega(
        @PathVariable String id,
        @RequestParam(defaultValue = "false") boolean force
    ) {
        // Primero verificar si la bodega tiene productos (si no es forzado)
        if (!force) {
            String productosUrl = buildFunctionUrl("/bodegas/" + id + "/productos");
            return webClient
                .get()
                .uri(productosUrl)
                .retrieve()
                .bodyToMono(Object.class)
                .flatMap(productos -> {
                    // Si la respuesta no está vacía, la bodega tiene productos
                    String productosStr = productos.toString();
                    if (productosStr.contains("[") && !productosStr.equals("[]")) {
                        return Mono.just(
                            ResponseEntity.status(HttpStatus.CONFLICT).body(
                                (Object) Map.of(
                                    "error", "La bodega contiene productos",
                                    "mensaje", "Use el parámetro 'force=true' para eliminar la bodega y sus productos, o reasigne los productos primero",
                                    "productos", productos,
                                    "bodegaId", id
                                )
                            )
                        );
                    } else {
                        // La bodega está vacía, proceder con eliminación
                        return eliminarBodegaDirecto(id);
                    }
                })
                .onErrorResume(e -> {
                    // Si no se pueden obtener productos, asumir que la bodega no existe o está vacía
                    return eliminarBodegaDirecto(id);
                });
        } else {
            // Eliminación forzada - obtener información de productos afectados primero
            String productosUrl = buildFunctionUrl("/bodegas/" + id + "/productos");
            return webClient
                .get()
                .uri(productosUrl)
                .retrieve()
                .bodyToMono(Object.class)
                .flatMap(productos -> 
                    eliminarBodegaConDetalles(id, productos)
                )
                .onErrorResume(e -> {
                    // Si no se pueden obtener productos, proceder con eliminación simple
                    return eliminarBodegaDirecto(id);
                });
        }
    }

    private Mono<ResponseEntity<Object>> eliminarBodegaDirecto(String id) {
        String url = buildFunctionUrl("/bodegas/" + id);
        return webClient
            .delete()
            .uri(url)
            .retrieve()
            .bodyToMono(Object.class)
            .map(body -> {
                Map<String, Object> responseMap = new HashMap<>();
                responseMap.put("mensaje", "Bodega eliminada exitosamente");
                responseMap.put("bodegaId", id);
                responseMap.put("productosAfectados", 0);
                return ResponseEntity.ok((Object) responseMap);
            })
            .onErrorResume(e -> {
                Map<String, Object> errorMap = new HashMap<>();
                errorMap.put("error", "No se pudo eliminar la bodega");
                errorMap.put("detalle", e.getMessage());
                return Mono.just(
                    ResponseEntity.status(HttpStatus.NOT_FOUND).body((Object) errorMap)
                );
            });
    }

    private Mono<ResponseEntity<Object>> eliminarBodegaConDetalles(String id, Object productos) {
        String url = buildFunctionUrl("/bodegas/" + id);
        return webClient
            .delete()
            .uri(url)
            .retrieve()
            .bodyToMono(Object.class)
            .map(body -> {
                // Contar productos afectados
                int productosAfectados = 0;
                String productosStr = productos.toString();
                if (productosStr.contains("[") && !productosStr.equals("[]")) {
                    // Estimación simple basada en comas (productos separados)
                    productosAfectados = productosStr.split("\\{").length - 1;
                }

                Map<String, Object> responseMap = new HashMap<>();
                responseMap.put("mensaje", "Bodega eliminada exitosamente");
                responseMap.put("bodegaId", id);
                responseMap.put("productosAfectados", productosAfectados);
                responseMap.put("detalleProductos", productos);
                responseMap.put("advertencia", "Los productos han perdido su asignación a esta bodega");
                return ResponseEntity.ok((Object) responseMap);
            })
            .onErrorResume(e -> {
                Map<String, Object> errorMap = new HashMap<>();
                errorMap.put("error", "Error eliminando la bodega");
                errorMap.put("detalle", e.getMessage());
                errorMap.put("productosQueSeAfectaron", productos);
                return Mono.just(
                    ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body((Object) errorMap)
                );
            });
    }
}
