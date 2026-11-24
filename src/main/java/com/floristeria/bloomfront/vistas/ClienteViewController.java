package com.floristeria.bloomfront.vistas;

import com.floristeria.bloomfront.dtos.ClienteDto;
import com.floristeria.bloomfront.utilidades.MensajeError;
import com.floristeria.bloomfront.webservicesclients.ClienteApiClient;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteViewController {

    private final ClienteApiClient clienteClient;

    @GetMapping
    public String listar(Model model) {
        try {
            model.addAttribute("clientes", clienteClient.listar());
        } catch (Exception e) {
            model.addAttribute("clientes", List.of());
            model.addAttribute("error", MensajeError.de(e));
        }
        return "clientes/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("cliente", new ClienteDto());
        return "clientes/formulario";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable("id") Integer id, Model model, RedirectAttributes redirect) {
        try {
            model.addAttribute("cliente", clienteClient.consultar(id));
            return "clientes/formulario";
        } catch (Exception e) {
            redirect.addFlashAttribute("error", MensajeError.de(e));
            return "redirect:/clientes";
        }
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute("cliente") ClienteDto cliente, Model model, RedirectAttributes redirect) {
        try {
            if (cliente.getIdCliente() == null) {
                clienteClient.insertar(cliente);
                redirect.addFlashAttribute("exito", "Cliente creado correctamente");
            } else {
                clienteClient.actualizar(cliente);
                redirect.addFlashAttribute("exito", "Cliente actualizado correctamente");
            }
            return "redirect:/clientes";
        } catch (Exception e) {
            model.addAttribute("error", MensajeError.de(e));
            return "clientes/formulario";
        }
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable("id") Integer id, RedirectAttributes redirect) {
        try {
            clienteClient.eliminar(id);
            redirect.addFlashAttribute("exito", "Cliente eliminado correctamente");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", MensajeError.de(e));
        }
        return "redirect:/clientes";
    }
}