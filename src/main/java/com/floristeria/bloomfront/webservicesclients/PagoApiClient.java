package com.floristeria.bloomfront.webservicesclients;

import com.floristeria.bloomfront.dtos.PagoDto;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "pago-api-client", url = "${bloom.api.url}/pagos")
public interface PagoApiClient {

    @PostMapping("/nuevo")
    PagoDto registrar(@RequestBody PagoDto pago);

    @GetMapping("/pedido/{idPedido}")
    List<PagoDto> listarPorPedido(@PathVariable("idPedido") Integer idPedido);
}