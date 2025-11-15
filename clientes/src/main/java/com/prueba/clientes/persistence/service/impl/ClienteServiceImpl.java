package com.prueba.clientes.persistence.service.impl;

import com.prueba.clientes.dto.request.ClienteDeleteRequestDto;
import com.prueba.clientes.dto.request.ClienteRequestDto;
import com.prueba.clientes.dto.request.ConsultaClienteRequestDto;
import com.prueba.clientes.dto.response.ClienteResponseDto;
import com.prueba.clientes.dto.response.MessageResponseDto;
import com.prueba.clientes.exception.ClientesException;
import com.prueba.clientes.persistence.entity.ClienteEntity;
import com.prueba.clientes.persistence.mapper.ClienteMapper;
import com.prueba.clientes.persistence.repository.ClienteRepository;
import com.prueba.clientes.persistence.service.ClienteService;
import com.prueba.clientes.producer.ClienteProducer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;
    private final ClienteProducer clienteProducer;

    /**
     * Este metodo crea un cliente.-
     *
     * @param clienteRequestDto Dto con la informacion del cliente
     * @return mensaje de exito si se crea corrctamten
     */
    @Override
    public MessageResponseDto crearCliente(final ClienteRequestDto clienteRequestDto) {
        log.info("Ingresa a crear un cliente {}", clienteRequestDto.nombre());
        if (clienteRepository.existsByIdentificacion(clienteRequestDto.identificacion())) {
            throw new ClientesException("Ya existe un cliente con la identificación: ".concat(clienteRequestDto.identificacion()));
        }
        ClienteEntity cliente = clienteMapper.toEntity(clienteRequestDto);
        clienteRepository.save(cliente);
        return new MessageResponseDto("Transaccion realizada con exito");
    }

    /**
     * Este mnetodo obtiene un cliente por ID.
     *
     * @param identificacion del cliente
     * @return cliente como DTO
     */
    @Override
    public ClienteResponseDto obtenerClientePorIdentificacion(final String identificacion) {
        return clienteMapper.toResponseDto(clienteRepository.findByIdentificacionAndEstado(identificacion, true)
                .orElseThrow(() -> new ClientesException("Cliente no encontrado")));
    }

    /**
     * Este metodo elimina de forma logica al cliente por numero de identificacion.
     *
     * @param clienteRequestDto DTO con informacion del cliente a eliminar
     * @return mensaje de exito en caso de ser eliminado exitosamente
     */
    @Override
    public MessageResponseDto actualizarCliente(final ClienteRequestDto clienteRequestDto) {
        log.info("Ingresa actualizacion de cliente {}", clienteRequestDto.nombre());
        ClienteEntity cliente = clienteRepository.findById(Long.parseLong(String.valueOf(clienteRequestDto.id())))
                .orElseThrow(() -> new ClientesException("Cliente no encontrado"));
        clienteMapper.updateEntityFromDto(clienteRequestDto, cliente);
        ClienteEntity updatedCliente = clienteRepository.save(cliente);
        clienteRepository.save(updatedCliente);
        clienteProducer.enviarClienteActualizado(updatedCliente.getClienteId(), updatedCliente.getNombre());
        return new MessageResponseDto("Transaccion realizada con exito");
    }

    /**
     * Este metodo elimina de forma logica al cliente por numero de identificacion.
     *
     * @param clienteDeleteRequestDto DTO con informacion del cliente a eliminar
     * @return mensaje de exito en caso de ser eliminado exitosamente
     */
    @Override
    public MessageResponseDto eliminarCliente(final ClienteDeleteRequestDto clienteDeleteRequestDto) {
        log.info("Ingresa a eliminar un cliente");
        ClienteEntity cliente = clienteRepository.findByIdentificacionAndEstado(clienteDeleteRequestDto.identificacion(), true)
                .orElseThrow(() -> new ClientesException("Cliente no encontrado"));
        cliente.setEstado(false);
        clienteRepository.save(cliente);
        return null;
    }

    /**
     * Este metodo obtiene una lista de clientes paginados por estado.
     *
     * @param consultaClienteRequestDto estado y paginacion de la consulta
     * @return una lista de tipo page con los clientes encontrados
     */
    @Override
    public Page<ClienteResponseDto> obtenerClientesPorEstado(final ConsultaClienteRequestDto consultaClienteRequestDto) {
        Pageable pageable = PageRequest.of(
                consultaClienteRequestDto.pageNumber(),
                consultaClienteRequestDto.pageSize()
        );

        Page<ClienteEntity> pageResult = clienteRepository.consultarClientes(consultaClienteRequestDto.estado(), pageable);
        return pageResult.map(clienteMapper::toResponseDto);
    }
}
