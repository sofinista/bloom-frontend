package com.floristeria.bloomfront.vistas;

import com.floristeria.bloomfront.dtos.ArregloDto;
import com.floristeria.bloomfront.dtos.ClienteDto;
import com.floristeria.bloomfront.dtos.PedidoDto;
import com.floristeria.bloomfront.dtos.TarjetaDto;
import com.floristeria.bloomfront.utilidades.MensajeError;
import com.floristeria.bloomfront.webservicesclients.ClienteApiClient;
import com.floristeria.bloomfront.webservicesclients.PagoApiClient;
import com.floristeria.bloomfront.webservicesclients.PedidoApiClient;
import com.floristeria.bloomfront.webservicesclients.SaldoClientRestTemplate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
@RequestMapping("/pedidos")
@RequiredArgsConstructor
public class PedidoViewController {

    private static final int FILAS_DE_ARREGLOS = 3;
    private static final List<String> ESTADOS =
            List.of("REGISTRADO", "EN_ELABORACION", "LISTO", "ENTREGADO", "CANCELADO");
    private static final Map<String, List<String>> SIGUIENTES = Map.of(
            "REGISTRADO", List.of("EN_ELABORACION", "CANCELADO"),
            "EN_ELABORACION", List.of("LISTO", "CANCELADO"),
            "LISTO", List.of("ENTREGADO", "CANCELADO"),
            "ENTREGADO", List.of(),
            "CANCELADO", List.of());

    private final PedidoApiClient pedidoClient;
    private final ClienteApiClient clienteClient;
    private final PagoApiClient pagoClient;
    private final SaldoClientRestTemplate saldoClient;

    @GetMapping
    public String listar(@RequestParam(value = "estado", required = false) String estado,
                         @RequestParam(value = "idCliente", required = false) Integer idCliente,
                         Model model) {
        String filtroEstado = (estado == null || estado.isBlank()) ? null : estado;
        try {
            model.addAttribute("pedidos", pedidoClient.listar(filtroEstado, idCliente));
        } catch (Exception e) {
            model.addAttribute("pedidos", List.of());
            model.addAttribute("error", MensajeError.de(e));
        }
        cargarClientes(model);
        model.addAttribute("estados", ESTADOS);
        model.addAttribute("estadoElegido", filtroEstado);
        model.addAttribute("clienteElegido", idCliente);
        return "pedidos/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        PedidoDto pedido = new PedidoDto();
        completarFilas(pedido);
        model.addAttribute("pedido", pedido);
        model.addAttribute("tarjeta", new TarjetaDto());
        cargarClientes(model);
        return "pedidos/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute("pedido") PedidoDto pedido,
                          @RequestParam(value = "mensajeTarjeta", required = false) String mensaje,
                          @RequestParam(value = "remitenteTarjeta", required = false) String remitente,
                          Model model, RedirectAttributes redirect) {
        pedido.setArreglos(pedido.getArreglos().stream()
                .filter(a -> a.getTipoArreglo() != null && !a.getTipoArreglo().isBlank())
                .toList());
        pedido.setTarjeta(armarTarjeta(mensaje, remitente));
        try {
            PedidoDto creado = pedidoClient.insertar(pedido);
            redirect.addFlashAttribute("exito", "Pedido #" + creado.getIdPedido() + " registrado correctamente");
            return "redirect:/pedidos/" + creado.getIdPedido();
        } catch (Exception e) {
            model.addAttribute("error", MensajeError.de(e));
            pedido.setArreglos(new ArrayList<>(pedido.getArreglos()));
            completarFilas(pedido);
            TarjetaDto tarjeta = new TarjetaDto();
            tarjeta.setMensaje(mensaje);
            tarjeta.setRemitente(remitente);
            model.addAttribute("tarjeta", tarjeta);
            cargarClientes(model);
            return "pedidos/formulario";
        }
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable("id") Integer id, Model model, RedirectAttributes redirect) {
        PedidoDto pedido;
        try {
            pedido = pedidoClient.consultar(id);
        } catch (Exception e) {
            redirect.addFlashAttribute("error", MensajeError.de(e));
            return "redirect:/pedidos";
        }
        model.addAttribute("pedido", pedido);
        model.addAttribute("siguientes", SIGUIENTES.getOrDefault(pedido.getEstado(), List.of()));
        model.addAttribute("nombreCliente", nombreDelCliente(pedido.getIdCliente()));
        try {
            model.addAttribute("pagos", pagoClient.listarPorPedido(id));
            model.addAttribute("saldo", saldoClient.consultarSaldo(id));
        } catch (Exception e) {
            model.addAttribute("pagos", List.of());
            model.addAttribute("error", MensajeError.de(e));
        }
        return "pedidos/detalle";
    }

    @PostMapping("/{id}/estado")
    public String cambiarEstado(@PathVariable("id") Integer id, @RequestParam("estado") String estado,
                                RedirectAttributes redirect) {
        try {
            pedidoClient.cambiarEstado(id, estado);
            redirect.addFlashAttribute("exito", "El pedido pasó a " + estado);
        } catch (Exception e) {
            redirect.addFlashAttribute("error", MensajeError.de(e));
        }
        return "redirect:/pedidos/" + id;
    }

    @PostMapping("/{id}/tarjeta")
    public String guardarTarjeta(@PathVariable("id") Integer id,
                                 @RequestParam("mensajeTarjeta") String mensaje,
                                 @RequestParam(value = "remitenteTarjeta", required = false) String remitente,
                                 RedirectAttributes redirect) {
        try {
            TarjetaDto tarjeta = new TarjetaDto();
            tarjeta.setMensaje(mensaje);
            tarjeta.setRemitente(remitente);
            pedidoClient.guardarTarjeta(id, tarjeta);
            redirect.addFlashAttribute("exito", "Tarjeta guardada");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", MensajeError.de(e));
        }
        return "redirect:/pedidos/" + id;
    }

    private void cargarClientes(Model model) {
        List<ClienteDto> clientes;
        try {
            clientes = clienteClient.listar();
        } catch (Exception e) {
            clientes = List.of();
        }
        Map<Integer, String> nombres = new HashMap<>();
        for (ClienteDto cliente : clientes) {
            nombres.put(cliente.getIdCliente(), cliente.getNombre());
        }
        model.addAttribute("clientes", clientes);
        model.addAttribute("nombres", nombres);
    }

    private String nombreDelCliente(Integer idCliente) {
        try {
            return clienteClient.consultar(idCliente).getNombre();
        } catch (Exception e) {
            return "Cliente #" + idCliente;
        }
    }

    private void completarFilas(PedidoDto pedido) {
        while (pedido.getArreglos().size() < FILAS_DE_ARREGLOS) {
            pedido.getArreglos().add(new ArregloDto());
        }
    }

    private TarjetaDto armarTarjeta(String mensaje, String remitente) {
        if (mensaje == null || mensaje.isBlank()) {
            return null;
        }
        TarjetaDto tarjeta = new TarjetaDto();
        tarjeta.setMensaje(mensaje.trim());
        tarjeta.setRemitente(remitente);
        return tarjeta;
    }
}