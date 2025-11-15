package com.prueba.clientes.unit;

import com.prueba.clientes.dto.request.ClienteRequestDto;
import com.prueba.clientes.dto.response.MessageResponseDto;
import com.prueba.clientes.exception.ClientesException;
import com.prueba.clientes.persistence.entity.ClienteEntity;
import com.prueba.clientes.persistence.mapper.ClienteMapper;
import com.prueba.clientes.persistence.repository.ClienteRepository;
import com.prueba.clientes.persistence.service.impl.ClienteServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ClienteMapper clienteMapper;

    @InjectMocks
    private ClienteServiceImpl clienteService;

    private ClienteEntity cliente;
    private ClienteRequestDto requestDto;

    @BeforeEach
    void setUp() {

        cliente = new ClienteEntity();
        cliente.setId(1L);
        cliente.setClienteId("CLI-12345");
        cliente.setNombre("Jose Lema");
        cliente.setGenero("Masculino");
        cliente.setEdad(30);
        cliente.setIdentificacion("1234567890");
        cliente.setDireccion("Otavalo sn y principal");
        cliente.setTelefono("098254785");
        cliente.setContrasena("1234");
        cliente.setEstado(true);

        requestDto = new ClienteRequestDto(1L, "Jose Lema", "Masculino", 30, "1234567890",
                "otavalo", "0974581256", "12345", true);
    }

    @Test
    public void testCrearCliente_Exitoso() {
        // Arrange
        when(clienteMapper.toEntity(any(ClienteRequestDto.class))).thenReturn(cliente);
        when(clienteRepository.save(any(ClienteEntity.class))).thenReturn(cliente);

        MessageResponseDto result = clienteService.crearCliente(requestDto);

        assertNotNull(result);

        verify(clienteRepository, times(1)).existsByIdentificacion("1234567890");
        verify(clienteRepository, times(1)).save(any(ClienteEntity.class));
        verify(clienteMapper, times(1)).toEntity(requestDto);
    }

    @Test
    void testCrearCliente_IdentificacionDuplicada() {
        when(clienteRepository.existsByIdentificacion(anyString())).thenReturn(true);

        ClientesException exception = assertThrows(
                ClientesException.class,
                () -> clienteService.crearCliente(requestDto)
        );

        assertTrue(exception.getMessage().contains("Ya existe un cliente"));
        verify(clienteRepository, times(1)).existsByIdentificacion("1234567890");
        verify(clienteRepository, never()).save(any(ClienteEntity.class));
    }
}
