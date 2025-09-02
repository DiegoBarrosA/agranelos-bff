package com.agranelos.bff.controller;

import com.agranelos.bff.dto.ProductoDto;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final WebClient webClient;
    private final String azureFunctionsBaseUrl;

    public ProductoController(
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
    public Mono<ResponseEntity<Object>> listarProductos() {
        String url = buildFunctionUrl("/productos");
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
                            "No se pudo obtener la lista de productos",
                            "detalle",
                            e.getMessage()
                        )
                    )
                )
            );
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Object>> obtenerProducto(
        @PathVariable String id
    ) {
        String url = buildFunctionUrl("/productos/" + id);
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
                            "Producto no encontrado",
                            "detalle",
                            e.getMessage()
                        )
                    )
                )
            );
    }

    @PostMapping
    public Mono<ResponseEntity<Object>> crearProducto(
        @Valid @RequestBody ProductoDto productoDto
    ) {
        String url = buildFunctionUrl("/productos");
        return webClient
            .post()
            .uri(url)
            .bodyValue(productoDto)
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
                            "No se pudo crear el producto",
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
    public Mono<ResponseEntity<Object>> actualizarProducto(
        @PathVariable String id,
        @Valid @RequestBody ProductoDto productoDto
    ) {
        String url = buildFunctionUrl("/productos/" + id);
        return webClient
            .put()
            .uri(url)
            .bodyValue(productoDto)
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
                            "No se pudo actualizar el producto",
                            "detalle",
                            e.getMessage(),
                            "url",
                            url,
                            "productId",
                            id
                        )
                    )
                )
            );
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Object>> eliminarProducto(
        @PathVariable String id
    ) {
        String url = buildFunctionUrl("/productos/" + id);
        return webClient
            .delete()
            .uri(url)
            .retrieve()
            .bodyToMono(Object.class)
            .map(body -> ResponseEntity.status(HttpStatus.NO_CONTENT).build())
            .onErrorResume(e ->
                Mono.just(
                    ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        Map.of(
                            "error",
                            "No se pudo eliminar el producto",
                            "detalle",
                            e.getMessage()
                        )
                    )
                )
            );
    }
}
