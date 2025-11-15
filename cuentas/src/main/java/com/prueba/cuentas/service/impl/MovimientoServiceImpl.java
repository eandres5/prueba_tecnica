package com.prueba.cuentas.service.impl;

import com.prueba.cuentas.dto.request.MovimientoRequestDto;
import com.prueba.cuentas.dto.response.MessageResponseDto;
import com.prueba.cuentas.dto.response.MovimientoResponseDto;
import com.prueba.cuentas.exception.CuentasException;
import com.prueba.cuentas.persistence.entity.CuentaEntity;
import com.prueba.cuentas.persistence.entity.MovimientoEntity;
import com.prueba.cuentas.persistence.mapper.MovimientoMapper;
import com.prueba.cuentas.persistence.repository.CuentaRepository;
import com.prueba.cuentas.persistence.repository.MovimientoRepository;
import com.prueba.cuentas.service.CuentaService;
import com.prueba.cuentas.service.MovimientoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MovimientoServiceImpl implements MovimientoService {

    private final MovimientoRepository movimientoRepository;
    private final CuentaRepository cuentaRepository;
    private final MovimientoMapper movimientoMapper;
    private final CuentaService cuentaService;

    /**
     * F1: Obtener todos los movimientos
     */
    @Transactional
    public List<MovimientoResponseDto> obtenerTodosLosMovimientos() {
        log.info("Obteniendo lista de todos los movimientos");

        List<MovimientoEntity> movimientos = movimientoRepository.findAll();
        return movimientoMapper.toResponseList(movimientos);
    }

    /**
     * Este metodo consulta el movimiento por id.
     *
     * @param id id del movimiento
     * @return movimiento en DTO
     */
    @Transactional
    public MovimientoResponseDto obtenerMovimientoPorId(Long id) {
        MovimientoEntity movimiento = movimientoRepository.findById(id)
                .orElseThrow(() -> new CuentasException("Error al buscar movimiento"));
        return movimientoMapper.toResponse(movimiento);
    }

    /**
     * Este metodo registro un movimiento y vincula al cliente con su respectiva cuenta.
     *
     * @param movimientoRequestDto dto con informacion del movimiento
     * @return mensaje de exito
     */
    @Override
    @Transactional
    public MessageResponseDto registrarMovimiento(final MovimientoRequestDto movimientoRequestDto) {
        log.info("Registrando movimiento {} de {} en cuenta {}",
                movimientoRequestDto.tipoMovimiento(),
                movimientoRequestDto.valor(),
                movimientoRequestDto.numeroCuenta());

        CuentaEntity cuenta = cuentaService.obtenerCuentaEntity(movimientoRequestDto.numeroCuenta());

        BigDecimal valorMovimiento = calcularValorMovimiento(
                movimientoRequestDto.tipoMovimiento(),
                movimientoRequestDto.valor()
        );

        if (esRetiro(movimientoRequestDto.tipoMovimiento())) {
            validarSaldoDisponible(cuenta, movimientoRequestDto.valor());
        }

        BigDecimal nuevoSaldo = cuenta.actualizarSaldo(valorMovimiento);

        MovimientoEntity movimiento = new MovimientoEntity();
        movimiento.setFecha(LocalDateTime.now());
        movimiento.setTipo(movimientoRequestDto.tipoMovimiento().toUpperCase());
        movimiento.setValor(valorMovimiento);
        movimiento.setSaldo(nuevoSaldo);
        movimiento.setCuenta(cuenta);

        movimientoRepository.save(movimiento);
        cuentaRepository.save(cuenta);
        log.info("Movimiento registrado exitosamente. Nuevo saldo: {}", nuevoSaldo);
        return new MessageResponseDto("Movimiento registrado exitosamente");
    }

    private void validarSaldoDisponible(CuentaEntity cuenta, BigDecimal monto) {
        if (!cuenta.tieneSaldoSuficiente(monto)) {
            log.error("Saldo no disponible. Saldo actual: {}, Monto solicitado: {}",
                    cuenta.getSaldoActual(), monto);

            // F3: Mensaje específico del PDF
            throw new CuentasException("Saldo no disponible");
        }
    }

    /**
     * Calcular el valor del movimiento segun el tipo
     *
     * @param tipoMovimiento DEPOSITO o RETIRO
     * @param valor          Monto (siempre positivo en el request)
     * @return Valor con signo correcto (+ depósito, - retiro)
     */
    private BigDecimal calcularValorMovimiento(String tipoMovimiento, BigDecimal valor) {
        if (esRetiro(tipoMovimiento)) {
            return valor.negate();
        }
        return valor;
    }

    private boolean esRetiro(String tipoMovimiento) {
        return "RETIRO".equalsIgnoreCase(tipoMovimiento);
    }

}
