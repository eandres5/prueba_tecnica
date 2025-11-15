package com.prueba.cuentas.controller;

import com.prueba.cuentas.dto.request.CuentaRequestDto;
import com.prueba.cuentas.dto.request.ReporteRequestDto;
import com.prueba.cuentas.dto.response.MessageResponseDto;
import com.prueba.cuentas.dto.response.ReporteResponseDto;
import com.prueba.cuentas.service.ReporteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
@Tag(name = "reporte", description = "API para gestion de reportes")
public class ReporteController {

    private final ReporteService reporteService;

    /**
     * Este metodo obtiene un reporte por rango de fechas con los movimientos bancarios.
     *
     * @param rangoFechas rango de fechas
     * @return lista de reporte
     */
    @Operation(summary = "Obtiene reporte", description = "Obtiene reporte")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cuenta reporte"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
    })
    @GetMapping
    public ResponseEntity<List<ReporteResponseDto>> reporte(@RequestParam("fecha") String rangoFechas) {
        return new ResponseEntity<>(reporteService.buscarReporte(rangoFechas), HttpStatus.OK);
    }

}
