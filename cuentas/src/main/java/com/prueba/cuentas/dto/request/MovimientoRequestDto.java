package com.prueba.cuentas.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record MovimientoRequestDto (
        @NotBlank(message = "El número de cuenta es obligatorio")
        @Size(max = 20, message = "El número de cuenta no puede exceder 20 caracteres")
        String numeroCuenta,
        @Pattern(regexp = "^(Retiro|Deposito)$",
                message = "Tipo de cuenta debe ser: Retiro o Deposito")
        String tipoMovimiento,
        @NotNull(message = "El valor es obligatorio")
        BigDecimal valor){
}
