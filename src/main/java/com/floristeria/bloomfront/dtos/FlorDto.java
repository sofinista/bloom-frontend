package com.floristeria.bloomfront.dtos;

import lombok.Data;

@Data
public class FlorDto {
    private Integer idFlor;
    private String nombre;
    private String color;
    private Integer stock;
    private Double precioUnitario;
    private Boolean activo;
}