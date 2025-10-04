package com.agranelos.bff.controller;

import com.agranelos.bff.dto.GraphQLRequestDto;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/graphql")
public class GraphQLController {

    private final WebClient webClient;
    private final String azureFunctionsBaseUrl;

    public GraphQLController(
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

    @PostMapping
    public Mono<ResponseEntity<Object>> ejecutarConsultaGraphQL(
        @Valid @RequestBody GraphQLRequestDto graphQLRequestDto
    ) {
        String url = buildFunctionUrl("/graphql");
        return webClient
            .post()
            .uri(url)
            .bodyValue(graphQLRequestDto)
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
                            "errors",
                            Map.of(
                                "message",
                                "No se pudo ejecutar la consulta GraphQL",
                                "detalle",
                                e.getMessage()
                            )
                        )
                    )
                )
            );
    }

    @GetMapping
    public Mono<ResponseEntity<Object>> obtenerEsquemaGraphQL() {
        return Mono.just(
            ResponseEntity.ok(
                Map.of(
                    "message",
                    "Endpoint GraphQL disponible. Use POST para ejecutar consultas.",
                    "endpoint",
                    "/api/graphql",
                    "ejemplo",
                    Map.of(
                        "query",
                        "{ productos { id nombre precio } }",
                        "variables",
                        Map.of(),
                        "operationName",
                        ""
                    )
                )
            )
        );
    }
}
