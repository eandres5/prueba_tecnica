package com.prueba.clientes.producer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class ClienteProducer {

    private final JmsTemplate jmsTemplate;

    /**
     * Este metodo envia una notifiacion a la cola cuando se actualiza el cliente.
     *
     * @param clienteId cliente id
     * @param nombre del cliente
     */
    public void enviarClienteActualizado(final String clienteId, final String nombre) {
        log.info("Enviando cliente ".concat(nombre));
        Map<String, Object> mensaje = Map.of(
                "clienteId", clienteId,
                "nombre", nombre
        );

        jmsTemplate.convertAndSend("cliente.actualizado.queue", mensaje);
    }

}
