package com.prueba.cuentas.service;

import com.prueba.cuentas.dto.response.ReporteResponseDto;

import java.util.List;

public interface ReporteService {

    /**
     * Este metodo obtiene un reporte por rango de fechas con los movimientos bancarios.
     *
     * @param rangoFechas rango de fechas
     * @return lista de reporte
     */
    List<ReporteResponseDto> buscarReporte(String rangoFechas);

}
