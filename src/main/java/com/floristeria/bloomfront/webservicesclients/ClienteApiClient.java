package com.floristeria.bloomfront.webservicesclients;

import com.floristeria.bloomfront.dtos.ClienteDto;
import java.util.List;
import java.util.Map;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "cliente-api-client", url = "${bloom.api.url}/clientes")
public interface ClienteApiClient {

    @GetMapping("/listar")
    List<ClienteDto> listar();

    @GetMapping("/consultar/{id}")
    ClienteDto consultar(@PathVariable("id") Integer id);

    @PostMapping("/nuevo")
    ClienteDto insertar(@RequestBody ClienteDto cliente);

    @PutMapping("/actualizar")
    ClienteDto actualizar(@RequestBody ClienteDto cliente);

    @DeleteMapping("/eliminar/{id}")
    Map<String, String> eliminar(@PathVariable("id") Integer id);
}