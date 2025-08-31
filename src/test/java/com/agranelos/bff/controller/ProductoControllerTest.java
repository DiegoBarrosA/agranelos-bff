package com.agranelos.bff.controller;

import com.agranelos.bff.dto.ProductoDto;
import java.math.BigDecimal;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@WebFluxTest(controllers = ProductoController.class)
@Import(ProductoController.class)
class ProductoControllerTest {

    private WebTestClient webTestClient;

    @MockBean
    private WebClient.Builder webClientBuilder;

    @Value("${azure.functions.base-url:http://localhost:7071/api}")
    private String azureFunctionsBaseUrl;

    @Value("${azure.functions.api-key:localdev}")
    private String azureFunctionsApiKey;

    @BeforeEach
    void setUp() {
        ProductoController controller = new ProductoController(
            azureFunctionsBaseUrl
        );
        this.webTestClient = WebTestClient.bindToController(controller).build();
    }

    @Test
    void testListarProductos() {
        webTestClient
            .get()
            .uri("/api/productos")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody()
            .consumeWith(response -> {
                // Aquí podrías agregar validaciones adicionales según la respuesta mock de Azure Functions
            });
    }

    @Test
    void testObtenerProductoPorId() {
        String id = "1";
        webTestClient
            .get()
            .uri("/api/productos/{id}", id)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody()
            .consumeWith(response -> {
                // Validaciones adicionales según la respuesta mock
            });
    }

    @Test
    void testCrearProducto() {
        ProductoDto producto = new ProductoDto(
            null,
            "Producto Test",
            "Descripción de prueba",
            new BigDecimal("10.50"),
            100
        );

        webTestClient
            .post()
            .uri("/api/productos")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(producto)
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody()
            .consumeWith(response -> {
                // Validaciones adicionales según la respuesta mock
            });
    }

    @Test
    void testActualizarProducto() {
        String id = "1";
        ProductoDto producto = new ProductoDto(
            1L,
            "Producto Actualizado",
            "Descripción actualizada",
            new BigDecimal("15.00"),
            200
        );

        webTestClient
            .put()
            .uri("/api/productos/{id}", id)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(producto)
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody()
            .consumeWith(response -> {
                // Validaciones adicionales según la respuesta mock
            });
    }

    @Test
    void testEliminarProducto() {
        String id = "1";
        webTestClient
            .delete()
            .uri("/api/productos/{id}", id)
            .exchange()
            .expectStatus()
            .isNoContent();
    }
}
