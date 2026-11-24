package com.floristeria.bloomfront.vistas;

import com.floristeria.bloomfront.dtos.PagoDto;
import com.floristeria.bloomfront.utilidades.MensajeError;
import com.floristeria.bloomfront.webservicesclients.PagoApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/pagos")
@RequiredArgsConstructor
public class PagoViewController {

    private final PagoApiClient pagoClient;

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute PagoDto pago, RedirectAttributes redirect) {
        try {
            pagoClient.registrar(pago);
            redirect.addFlashAttribute("exito", "Pago registrado correctamente");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", MensajeError.de(e));
        }
        return "redirect:/pedidos/" + pago.getIdPedido();
    }
}