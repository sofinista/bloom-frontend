package com.floristeria.bloomfront.webservicesclients;

import com.floristeria.bloomfront.dtos.PedidoDto;
import com.floristeria.bloomfront.dtos.TarjetaDto;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "pedido-api-client", url = "${bloom.api.url}/pedidos")
public interface PedidoApiClient {

    @PostMapping("/nuevo")
    PedidoDto insertar(@RequestBody PedidoDto pedido);

    @GetMapping("/listar")
    List<PedidoDto> listar(@RequestParam(value = "estado", required = false) String estado,
                           @RequestParam(value = "idCliente", required = false) Integer idCliente);

    @GetMapping("/consultar/{id}")
    PedidoDto consultar(@PathVariable("id") Integer id);

    @PatchMapping("/estado")
    PedidoDto cambiarEstado(@RequestParam("id") Integer id, @RequestParam("estado") String estado);

    @PutMapping("/tarjeta/{id}")
    PedidoDto guardarTarjeta(@PathVariable("id") Integer id, @RequestBody TarjetaDto tarjeta);
}