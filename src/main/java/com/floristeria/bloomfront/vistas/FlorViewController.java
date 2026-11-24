package com.floristeria.bloomfront.vistas;

import com.floristeria.bloomfront.dtos.FlorDto;
import com.floristeria.bloomfront.utilidades.MensajeError;
import com.floristeria.bloomfront.webservicesclients.FlorApiClient;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/flores")
@RequiredArgsConstructor
public class FlorViewController {

    private final FlorApiClient florClient;

    @GetMapping
    public String listar(Model model) {
        try {
            model.addAttribute("flores", florClient.listar());
        } catch (Exception e) {
            model.addAttribute("flores", List.of());
            model.addAttribute("error", MensajeError.de(e));
        }
        return "flores/lista";
    }

    @GetMapping("/nueva")
    public String nueva(Model model) {
        model.addAttribute("flor", new FlorDto());
        return "flores/formulario";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable("id") Integer id, Model model, RedirectAttributes redirect) {
        try {
            model.addAttribute("flor", florClient.consultar(id));
            return "flores/formulario";
        } catch (Exception e) {
            redirect.addFlashAttribute("error", MensajeError.de(e));
            return "redirect:/flores";
        }
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute("flor") FlorDto flor, Model model, RedirectAttributes redirect) {
        try {
            if (flor.getIdFlor() == null) {
                florClient.insertar(flor);
                redirect.addFlashAttribute("exito", "Flor creada correctamente");
            } else {
                florClient.actualizar(flor);
                redirect.addFlashAttribute("exito", "Flor actualizada correctamente");
            }
            return "redirect:/flores";
        } catch (Exception e) {
            model.addAttribute("error", MensajeError.de(e));
            return "flores/formulario";
        }
    }

    @PostMapping("/stock/{id}")
    public String ajustarStock(@PathVariable("id") Integer id, @RequestParam("cantidad") int cantidad,
                               RedirectAttributes redirect) {
        try {
            FlorDto flor = florClient.ajustarStock(id, cantidad);
            redirect.addFlashAttribute("exito", "Stock de " + flor.getNombre() + ": " + flor.getStock());
        } catch (Exception e) {
            redirect.addFlashAttribute("error", MensajeError.de(e));
        }
        return "redirect:/flores";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable("id") Integer id, RedirectAttributes redirect) {
        try {
            florClient.eliminar(id);
            redirect.addFlashAttribute("exito", "Flor eliminada correctamente");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", MensajeError.de(e));
        }
        return "redirect:/flores";
    }
}