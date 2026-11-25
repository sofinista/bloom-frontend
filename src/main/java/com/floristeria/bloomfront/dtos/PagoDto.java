package com.floristeria.bloomfront.dtos;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class PagoDto {
    private Integer idPago;
    private Integer idPedido;
    private Double monto;
    private String metodo;
    private LocalDateTime fechaPago;
}