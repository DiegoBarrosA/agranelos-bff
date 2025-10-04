package com.agranelos.bff.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.Map;

public class GraphQLRequestDto {

    @NotBlank(message = "La consulta GraphQL es obligatoria")
    private String query;

    private Map<String, Object> variables;

    private String operationName;

    // Constructor vacío
    public GraphQLRequestDto() {}

    // Constructor completo
    public GraphQLRequestDto(
        String query,
        Map<String, Object> variables,
        String operationName
    ) {
        this.query = query;
        this.variables = variables;
        this.operationName = operationName;
    }

    // Getters y Setters

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, Object> variables) {
        this.variables = variables;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }
}
