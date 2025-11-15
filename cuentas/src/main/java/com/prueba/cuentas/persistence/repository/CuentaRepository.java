package com.prueba.cuentas.persistence.repository;

import com.prueba.cuentas.persistence.entity.CuentaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CuentaRepository extends JpaRepository<CuentaEntity, Long> {

    /**
     * Este metodo obtiene la cuenta por el clienteId que viaja desde la notifiacion JMS.
     *
     * @param clienteId cliente id desde JMS
     * @return lista de cuentas por cliente ID
     */
    List<CuentaEntity> findByClienteId(String clienteId);

    /**
     * Este metodo consulta na cuenta por numero.
     *
     * @param numeroCuenta numero de cuenta
     * @return cuenta entity
     */
    Optional<CuentaEntity> findByNumeroCuenta(String numeroCuenta);

    /**
     * Este metodo consulta na cuenta por numero y estado.
     *
     * @param numeroCuenta numero de cuenta
     * @param estado       estado de la cuenta
     * @return cuenta entity
     */
    Optional<CuentaEntity> findByNumeroCuentaAndEstado(String numeroCuenta, Boolean estado);

    /**
     * Valida si existe un cuenta por numero y tipo de cuenta.
     *
     * @param numeroCuenta numero de cuenta
     * @param tipoCuenta   tipo de cuenta
     * @return valor true o false
     */
    boolean existsByNumeroCuentaAndTipoCuenta(String numeroCuenta, String tipoCuenta);

    /**
     * Este metodo obtiene un reporte de cuentas activas por fecha.
     *
     * @param fechaInicio fecha de incio
     * @param fechaFin    fecha de fin
     * @return lista de entity de tipo cuenta
     */
    @Query("""
            SELECT DISTINCT c FROM CuentaEntity c
            LEFT JOIN FETCH c.movimientos m
            WHERE c.estado = true
            AND m.fecha >= :fechaInicio
            AND m.fecha <= :fechaFin
            """)
    List<CuentaEntity> findCuentaReporte(@Param("fechaInicio") LocalDateTime fechaInicio, @Param("fechaFin") LocalDateTime fechaFin);
}
