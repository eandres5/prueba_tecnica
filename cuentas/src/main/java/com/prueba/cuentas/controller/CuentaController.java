package com.prueba.cuentas.controller;

import com.prueba.cuentas.dto.request.CuentaRequestDto;
import com.prueba.cuentas.dto.response.CuentaResponseDto;
import com.prueba.cuentas.dto.response.MessageResponseDto;
import com.prueba.cuentas.service.CuentaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/cuentas")
@RequiredArgsConstructor
@Tag(name = "Cuentas", description = "API para gestión de cuentas bancarias")
public class CuentaController {

    private final CuentaService cuentaService;

    @Operation(summary = "Crear una nueva cuenta", description = "Crea una cuenta bancaria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cuenta creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "409", description = "Cuenta ya existe")
    })
    @PostMapping
    public ResponseEntity<MessageResponseDto> crearCuenta(@Valid @RequestBody CuentaRequestDto cuentaRequestDto) {
        return new ResponseEntity<>(cuentaService.crearCuenta(cuentaRequestDto), HttpStatus.CREATED);
    }

    @Operation(summary = "Obtener una cuenta", description = "Obtener cuenta bancaria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cuenta consultada"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
    })
    @GetMapping("/{numeroCuenta}")
    public ResponseEntity<CuentaResponseDto> obtenerCuenta(@Parameter(description = "numero de cuenta") @PathVariable String numeroCuenta) {
        return new ResponseEntity<>(cuentaService.obtenerCuentaPorNumero(numeroCuenta), HttpStatus.OK);
    }

    @Operation(summary = "Eliminar una nueva cuenta", description = "Elimina una cuenta bancaria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cuenta eliminada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
    })
    @DeleteMapping("/{numeroCuenta}")
    public ResponseEntity<MessageResponseDto> eliminarCuenta(@Parameter(description = "numero de cuenta") @PathVariable String numeroCuenta) {
        return new ResponseEntity<>(cuentaService.eliminarCuenta(numeroCuenta), HttpStatus.OK);
    }

}
