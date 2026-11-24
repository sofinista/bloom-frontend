package com.floristeria.bloomfront.dtos;

import lombok.Data;

@Data
public class SaldoDto {
    private Double total;
    private Double pagado;
    private Double saldo;
}