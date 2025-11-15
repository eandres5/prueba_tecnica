package com.prueba.cuentas.service;

import com.prueba.cuentas.dto.request.MovimientoRequestDto;
import com.prueba.cuentas.dto.response.MessageResponseDto;
import com.prueba.cuentas.dto.response.MovimientoResponseDto;
import jakarta.transaction.Transactional;

public interface MovimientoService {

    /**
     * Este metodo registro un movimiento y vincula al cliente con su respectiva cuenta.
     *
     * @param movimientoRequestDto dto con informacion del movimiento
     * @return mensaje de exito
     */
    MessageResponseDto registrarMovimiento(MovimientoRequestDto movimientoRequestDto);

    /**
     * Este metodo consulta el movimiento por id.
     *
     * @param id id del movimiento
     * @return movimiento en DTO
     */
    MovimientoResponseDto obtenerMovimientoPorId(Long id);

}
