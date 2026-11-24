package com.floristeria.bloomfront.webservicesclients;

import com.floristeria.bloomfront.dtos.FlorDto;
import java.util.List;
import java.util.Map;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "flor-api-client", url = "${bloom.api.url}/flores")
public interface FlorApiClient {

    @GetMapping("/listar")
    List<FlorDto> listar();

    @GetMapping("/consultar/{id}")
    FlorDto consultar(@PathVariable("id") Integer id);

    @PostMapping("/nueva")
    FlorDto insertar(@RequestBody FlorDto flor);

    @PutMapping("/actualizar")
    FlorDto actualizar(@RequestBody FlorDto flor);

    @DeleteMapping("/eliminar/{id}")
    Map<String, String> eliminar(@PathVariable("id") Integer id);

    @PatchMapping("/stock")
    FlorDto ajustarStock(@RequestParam("id") Integer id, @RequestParam("cantidad") int cantidad);
}