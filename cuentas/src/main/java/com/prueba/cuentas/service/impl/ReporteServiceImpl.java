package com.prueba.cuentas.service.impl;

import com.prueba.cuentas.dto.request.ReporteRequestDto;
import com.prueba.cuentas.dto.response.ReporteResponseDto;
import com.prueba.cuentas.persistence.entity.CuentaEntity;
import com.prueba.cuentas.persistence.entity.MovimientoEntity;
import com.prueba.cuentas.service.CuentaService;
import com.prueba.cuentas.service.ReporteService;
import com.prueba.cuentas.util.FechaTransform;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReporteServiceImpl implements ReporteService {

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private final CuentaService cuentaService;

    /**
     * Este metodo obtiene un reporte por rango de fechas con los movimientos bancarios.
     *
     * @param rangoFechas rango de fechas
     * @return lista de reporte
     */
    @Override
    public List<ReporteResponseDto> buscarReporte(final String rangoFechas) {
        String[] fechas = rangoFechas.split(",");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        ReporteRequestDto reporteRequestDto = new ReporteRequestDto(FechaTransform.traformarFecha(fechas[0].concat(" 00:00:00"), DATE_FORMAT),
                FechaTransform.traformarFecha(fechas[1].concat(" 23:59:59"), DATE_FORMAT));
        List<CuentaEntity> lista = cuentaService.obtenerReporte(reporteRequestDto);
        List<ReporteResponseDto> reporteResponseDtos = new ArrayList<>();

        for (CuentaEntity cuenta : lista) {
            for (MovimientoEntity mov : cuenta.getMovimientos()) {
                ReporteResponseDto dto = new ReporteResponseDto(
                        mov.getFecha() != null ? mov.getFecha().format(formatter) : "",
                        cuenta.getClienteNombre(),
                        cuenta.getNumeroCuenta(),
                        cuenta.getTipoCuenta(),
                        cuenta.getSaldoInicial(),
                        cuenta.getEstado(),
                        mov.getValor(),
                        mov.getSaldo()
                );
                reporteResponseDtos.add(dto);
            }

            if (cuenta.getMovimientos().isEmpty()) {
                ReporteResponseDto dto = new ReporteResponseDto(
                        "",
                        cuenta.getClienteNombre(),
                        cuenta.getNumeroCuenta(),
                        cuenta.getTipoCuenta(),
                        cuenta.getSaldoInicial(),
                        cuenta.getEstado(),
                        BigDecimal.ZERO,
                        cuenta.getSaldoActual()
                );
                reporteResponseDtos.add(dto);
            }
        }

        return reporteResponseDtos;
    }
}
