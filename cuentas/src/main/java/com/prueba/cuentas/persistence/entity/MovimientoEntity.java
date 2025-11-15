package com.prueba.cuentas.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "movimientos")
public class MovimientoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime fecha;
    @NotBlank(message = "El tipo es obligatorio")
    @Column(nullable = false, length = 20)
    private String tipo;
    @NotNull(message = "El saldo actual es obligatorio")
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal valor;
    @NotNull(message = "El saldo actual es obligatorio")
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal saldo;
    @ManyToOne
    @JoinColumn(name = "numero_cuenta")
    private CuentaEntity cuenta;
}
