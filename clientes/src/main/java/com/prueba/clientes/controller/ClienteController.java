package com.prueba.clientes.controller;

import com.prueba.clientes.dto.request.ClienteDeleteRequestDto;
import com.prueba.clientes.dto.request.ClienteRequestDto;
import com.prueba.clientes.dto.request.ConsultaClienteRequestDto;
import com.prueba.clientes.dto.response.ClienteResponseDto;
import com.prueba.clientes.dto.response.MessageResponseDto;
import com.prueba.clientes.persistence.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    @Operation(summary = "Crear un nuevo cliente", description = "Crea un cliente con todos sus datos personales")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cliente creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "409", description = "Cliente ya existe")
    })
    @PostMapping
    public ResponseEntity<MessageResponseDto> crearCliente(@Validated @RequestBody final ClienteRequestDto clienteRequestDto) {
        return new ResponseEntity<>(clienteService.crearCliente(clienteRequestDto), HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar Cliente", description = "Actualiza cliente con sus datos personales")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cliente actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PutMapping
    public ResponseEntity<MessageResponseDto> actualizarCliente(@Validated @RequestBody final ClienteRequestDto clienteRequestDto) {
        return new ResponseEntity<>(clienteService.actualizarCliente(clienteRequestDto), HttpStatus.OK);
    }

    @Operation(summary = "Obtener Cliente por identificacion", description = "Obtener Cliente por identificacion")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente consultado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @GetMapping("/{identificacion}")
    public ResponseEntity<ClienteResponseDto> obterCliente(@Parameter(description = "ID del cliente") @PathVariable String identificacion) {
        return new ResponseEntity<>(clienteService.obtenerClientePorIdentificacion(identificacion), HttpStatus.OK);
    }

    @Operation(summary = "Obtener Clientes", description = "Obtener Clientes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente Exitoso"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping("/clientes")
    public ResponseEntity<Page<ClienteResponseDto>> obterClientes(@Validated @RequestBody final ConsultaClienteRequestDto consultaClienteRequestDto) {
        return new ResponseEntity<>(clienteService.obtenerClientesPorEstado(consultaClienteRequestDto), HttpStatus.OK);
    }

    @Operation(summary = "Eliminar cliente", description = "Eliminar cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cliente Eliminado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @DeleteMapping()
    public ResponseEntity<MessageResponseDto> eliminarCliente(@Validated @RequestBody final ClienteDeleteRequestDto clienteDeleteRequestDto) {
        return new ResponseEntity<>(clienteService.eliminarCliente(clienteDeleteRequestDto), HttpStatus.OK);
    }

}
