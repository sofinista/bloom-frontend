package com.floristeria.bloomfront.dtos;

import lombok.Data;

@Data
public class ArregloDto {
    private Integer idArreglo;
    private String tipoArreglo;
    private String tamano;
    private String colores;
    private String descripcion;
    private Double valorUnitario;
}