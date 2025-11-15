package com.prueba.cuentas.dto.request;

import jakarta.validation.constraints.Pattern;

public record CuentaUpdateRequestDto(
        @Pattern(regexp = "^(Ahorro|Corriente|Ahorros)$",
                message = "Tipo de cuenta debe ser: Ahorro, Ahorros o Corriente")
        String tipoCuenta,
        Boolean estado) {
}
