package com.prueba.cuentas.controller;

import com.prueba.cuentas.dto.request.MovimientoRequestDto;
import com.prueba.cuentas.dto.response.CuentaResponseDto;
import com.prueba.cuentas.dto.response.MessageResponseDto;
import com.prueba.cuentas.dto.response.MovimientoResponseDto;
import com.prueba.cuentas.service.impl.MovimientoServiceImpl;
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
@RequestMapping("/api/movimientos")
@RequiredArgsConstructor
@Tag(name = "Movimientos", description = "API para gestión de movimientos en cuentas de cliente")
public class MovimientoController {
    private final MovimientoServiceImpl movimientoService;

    @Operation(summary = "Crear una nueva cuenta", description = "Crea una cuenta bancaria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cuenta creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "409", description = "Cuenta ya existe")
    })
    @PostMapping("/registrar-movimiento")
    public ResponseEntity<MessageResponseDto> registrarMovimiento(@Valid @RequestBody MovimientoRequestDto movimientoRequestDto) {
        return new ResponseEntity<>(movimientoService.registrarMovimiento(movimientoRequestDto), HttpStatus.CREATED);
    }

    @Operation(summary = "Obtener movimiento", description = "Obtener movimiento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movimiento consultado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
    })
    @GetMapping("/{movimiento}")
    public ResponseEntity<MovimientoResponseDto> obtenerCuenta(@Parameter(description = "movimiento") @PathVariable Long movimiento) {
        return new ResponseEntity<>(movimientoService.obtenerMovimientoPorId(movimiento), HttpStatus.OK);
    }
}
