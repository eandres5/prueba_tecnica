package com.prueba.cuentas.service;

import com.prueba.cuentas.dto.request.CuentaRequestDto;
import com.prueba.cuentas.dto.request.ReporteRequestDto;
import com.prueba.cuentas.dto.response.CuentaResponseDto;
import com.prueba.cuentas.dto.response.MessageResponseDto;
import com.prueba.cuentas.persistence.entity.CuentaEntity;

import java.util.List;

public interface CuentaService {

    /**
     * Este metodo crea un cuenta con un usuario.
     *
     * @param cuentaRequestDto dto con informacion de la cuenta a crear
     * @return mensaje de exito al crear cuenta
     */
    MessageResponseDto crearCuenta(CuentaRequestDto cuentaRequestDto);

    /**
     * Este metodo obtiene una cuenta por numeroCuenta.
     *
     * @param numeroCuenta numero de cuenta
     * @return CuentaResponseDto con la informacion de la cuenta
     */
    CuentaResponseDto obtenerCuentaPorNumero(String numeroCuenta);

    List<CuentaResponseDto> obtenerCuentasPorCliente(String clienteId);

    /**
     * Este metodo obntiene una cuenta.
     *
     * @param numeroCuenta numero de cuenta
     * @return entity de tipo cuenta
     */
    CuentaEntity obtenerCuentaEntity(String numeroCuenta);

    /**
     * Este metodo actualiza el nombre del cliente en las cuentas que esten registras.
     *
     * @param clienteId     id del cliente
     * @param nombreCliente nombre del cliente
     */
    void actualizarNombreCliente(String clienteId, String nombreCliente);

    /**
     * Este metodo obtiene un reporte entre un rango de fechas.
     *
     * @param reporteRequestDto rango de fechas
     * @return lista con las cuentas encontadas
     */
    List<CuentaEntity> obtenerReporte(ReporteRequestDto reporteRequestDto);

    /**
     * Este metodo elimina una cuenta de forma logica.
     *
     * @param numeroCuenta numero de cuenta
     * @return mensaje de exito
     */
    MessageResponseDto eliminarCuenta(String numeroCuenta);
}
