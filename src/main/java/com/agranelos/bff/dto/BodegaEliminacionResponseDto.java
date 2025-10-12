package com.agranelos.bff.dto;

import java.util.List;

public class BodegaEliminacionResponseDto {

    private String mensaje;
    private String bodegaId;
    private int productosAfectados;
    private List<ProductoAfectadoDto> detalleProductos;
    private String advertencia;
    private boolean eliminacionForzada;

    // Constructor vacío
    public BodegaEliminacionResponseDto() {}

    // Constructor completo
    public BodegaEliminacionResponseDto(
        String mensaje,
        String bodegaId,
        int productosAfectados,
        List<ProductoAfectadoDto> detalleProductos,
        String advertencia,
        boolean eliminacionForzada
    ) {
        this.mensaje = mensaje;
        this.bodegaId = bodegaId;
        this.productosAfectados = productosAfectados;
        this.detalleProductos = detalleProductos;
        this.advertencia = advertencia;
        this.eliminacionForzada = eliminacionForzada;
    }

    // Getters y Setters
    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getBodegaId() {
        return bodegaId;
    }

    public void setBodegaId(String bodegaId) {
        this.bodegaId = bodegaId;
    }

    public int getProductosAfectados() {
        return productosAfectados;
    }

    public void setProductosAfectados(int productosAfectados) {
        this.productosAfectados = productosAfectados;
    }

    public List<ProductoAfectadoDto> getDetalleProductos() {
        return detalleProductos;
    }

    public void setDetalleProductos(List<ProductoAfectadoDto> detalleProductos) {
        this.detalleProductos = detalleProductos;
    }

    public String getAdvertencia() {
        return advertencia;
    }

    public void setAdvertencia(String advertencia) {
        this.advertencia = advertencia;
    }

    public boolean isEliminacionForzada() {
        return eliminacionForzada;
    }

    public void setEliminacionForzada(boolean eliminacionForzada) {
        this.eliminacionForzada = eliminacionForzada;
    }

    @Override
    public String toString() {
        return "BodegaEliminacionResponseDto{" +
               "mensaje='" + mensaje + '\'' +
               ", bodegaId='" + bodegaId + '\'' +
               ", productosAfectados=" + productosAfectados +
               ", detalleProductos=" + detalleProductos +
               ", advertencia='" + advertencia + '\'' +
               ", eliminacionForzada=" + eliminacionForzada +
               '}';
    }

    /**
     * DTO para representar un producto afectado por la eliminación de bodega
     */
    public static class ProductoAfectadoDto {
        private Long id;
        private String nombre;
        private int cantidadPerdida;

        // Constructor vacío
        public ProductoAfectadoDto() {}

        // Constructor completo
        public ProductoAfectadoDto(Long id, String nombre, int cantidadPerdida) {
            this.id = id;
            this.nombre = nombre;
            this.cantidadPerdida = cantidadPerdida;
        }

        // Getters y Setters
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public int getCantidadPerdida() {
            return cantidadPerdida;
        }

        public void setCantidadPerdida(int cantidadPerdida) {
            this.cantidadPerdida = cantidadPerdida;
        }

        @Override
        public String toString() {
            return "ProductoAfectadoDto{" +
                   "id=" + id +
                   ", nombre='" + nombre + '\'' +
                   ", cantidadPerdida=" + cantidadPerdida +
                   '}';
        }
    }
}