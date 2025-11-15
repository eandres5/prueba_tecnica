package com.prueba.cuentas.persistence.entity;

import jakarta.persistence.PrePersist;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cuenta")
public class CuentaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El número de cuenta es obligatorio")
    @Column(nullable = false, unique = true, length = 20)
    private String numeroCuenta;

    @NotBlank(message = "El tipo de cuenta es obligatorio")
    @Column(nullable = false, length = 20)
    private String tipoCuenta;

    @NotNull(message = "El saldo inicial es obligatorio")
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal saldoInicial;

    @NotNull(message = "El saldo actual es obligatorio")
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal saldoActual;

    @Column(nullable = false)
    private Boolean estado = true;

    /**
     * ID del cliente (referencia al microservicio Cliente)
     * No es FK física porque son microservicios separados
     */
    @NotBlank(message = "El cliente ID es obligatorio")
    @Column(nullable = false, length = 50)
    private String clienteId;
    @Column(length = 100)
    private String clienteNombre;
    @OneToMany(mappedBy = "cuenta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MovimientoEntity> movimientos = new ArrayList<>();

    /**
     * Hook que se ejecuta antes de insertar
     */
    @PrePersist
    public void prePersist() {
        if (this.estado == null) {
            this.estado = true;
        }
        if (this.saldoActual == null && this.saldoInicial != null) {
            this.saldoActual = this.saldoInicial;
        }
    }

    /**
     * Actualizar saldo después de un movimiento
     *
     * @param valor Valor del movimiento (+ depósito, - retiro)
     * @return nuevo saldo
     */
    public BigDecimal actualizarSaldo(BigDecimal valor) {
        this.saldoActual = this.saldoActual.add(valor);
        return this.saldoActual;
    }

    /**
     * Verificar si hay saldo suficiente para un retiro
     *
     * @param monto Monto a retirar (valor absoluto)
     * @return true si hay saldo suficiente
     */
    public boolean tieneSaldoSuficiente(BigDecimal monto) {
        return this.saldoActual.compareTo(monto.abs()) >= 0;
    }

}
