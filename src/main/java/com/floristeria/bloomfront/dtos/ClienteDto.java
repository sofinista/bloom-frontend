package com.floristeria.bloomfront.dtos;

import lombok.Data;

@Data
public class ClienteDto {
    private Integer idCliente;
    private String nombre;
    private String telefono;
    private String direccion;
    private String correoElectronico;
    private String tipoDocumento;
    private String numeroDocumento;
    private Boolean activo;
}