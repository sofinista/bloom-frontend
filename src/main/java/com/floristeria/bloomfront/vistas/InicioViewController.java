package com.floristeria.bloomfront.vistas;

import com.floristeria.bloomfront.utilidades.MensajeError;
import com.floristeria.bloomfront.webservicesclients.ClienteApiClient;
import com.floristeria.bloomfront.webservicesclients.FlorApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class InicioViewController {

    private final ClienteApiClient clienteClient;
    private final FlorApiClient florClient;

    @GetMapping("/")
    public String inicio(Model model) {
        try {
            model.addAttribute("totalClientes", clienteClient.listar().size());
            model.addAttribute("totalFlores", florClient.listar().size());
        } catch (Exception e) {
            model.addAttribute("error", MensajeError.de(e));
        }
        return "index";
    }
}