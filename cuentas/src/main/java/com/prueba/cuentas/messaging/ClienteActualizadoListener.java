package com.prueba.cuentas.messaging;

import com.prueba.cuentas.service.CuentaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class ClienteActualizadoListener {

    private final CuentaService cuentaService;

    /**
     * Este metodo recibe los mensajes de la cola cliente.actualizado.queue generado desde otro servicio.
     *
     * @param mensaje mensaje que viaja en la cola
     */
    @JmsListener(destination = "cliente.actualizado.queue")
    public void procesar(final Map<String, Object> mensaje) {
        log.info("Mensaje recibido desde cliente-service: {}", mensaje);
        String clienteId = mensaje.get("clienteId").toString();
        String nombre = mensaje.get("nombre").toString();
        cuentaService.actualizarNombreCliente(clienteId, nombre);
    }

}
