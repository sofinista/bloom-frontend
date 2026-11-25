package com.floristeria.bloomfront.dtos;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class PedidoDto {
    private Integer idPedido;
    private Integer idCliente;
    private LocalDateTime fechaRegistro;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime fechaHoraEntrega;

    private String direccionEntrega;
    private String ocasion;
    private String estado;
    private Double valorTotal;
    private LocalDateTime fechaEntregaReal;
    private List<ArregloDto> arreglos = new ArrayList<>();
    private TarjetaDto tarjeta;
}