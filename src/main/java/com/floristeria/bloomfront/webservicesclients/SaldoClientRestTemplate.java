package com.floristeria.bloomfront.webservicesclients;

import com.floristeria.bloomfront.dtos.SaldoDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SaldoClientRestTemplate {

    private final RestTemplate restTemplate;
    private final String base;

    public SaldoClientRestTemplate(RestTemplate restTemplate, @Value("${bloom.api.url}") String base) {
        this.restTemplate = restTemplate;
        this.base = base;
    }

    public SaldoDto consultarSaldo(Integer idPedido) {
        String url = base + "/pagos/saldo/" + idPedido;
        return restTemplate.getForObject(url, SaldoDto.class);
    }
}