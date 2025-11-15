package com.prueba.clientes.persistence.service;

import com.prueba.clientes.dto.request.ClienteDeleteRequestDto;
import com.prueba.clientes.dto.request.ClienteRequestDto;
import com.prueba.clientes.dto.request.ConsultaClienteRequestDto;
import com.prueba.clientes.dto.response.ClienteResponseDto;
import com.prueba.clientes.dto.response.MessageResponseDto;
import org.springframework.data.domain.Page;


public interface ClienteService {

    /**
     * Este metodo crea un cliente.
     *
     * @param clienteRequestDto Dto con la informacion del cliente
     * @return mensaje de exito si se crea corrctamten
     */
    MessageResponseDto crearCliente(ClienteRequestDto clienteRequestDto);

    /**
     * Este mnetodo obtiene un cliente por ID.
     *
     * @param identificacion del cliente
     * @return cliente como DTO
     */
    ClienteResponseDto obtenerClientePorIdentificacion(String identificacion);

    /**
     * Este metodo elimina de forma logica al cliente por numero de identificacion.
     *
     * @param clienteRequestDto DTO con informacion del cliente a eliminar
     * @return mensaje de exito en caso de ser eliminado exitosamente
     */
    MessageResponseDto actualizarCliente(ClienteRequestDto clienteRequestDto);

    /**
     * Este metodo elimina de forma logica al cliente por numero de identificacion.
     *
     * @param clienteDeleteRequestDto DTO con informacion del cliente a eliminar
     * @return mensaje de exito en caso de ser eliminado exitosamente
     */
    MessageResponseDto eliminarCliente(ClienteDeleteRequestDto clienteDeleteRequestDto);

    /**
     * Este metodo obtiene una lista de clientes paginados por estado.
     *
     * @param consultaClienteRequestDto estado y paginacion de la consulta
     * @return una lista de tipo page con los clientes encontrados
     */
    Page<ClienteResponseDto> obtenerClientesPorEstado(ConsultaClienteRequestDto consultaClienteRequestDto);
}
