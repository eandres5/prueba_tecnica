package com.prueba.cuentas.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class CuentaRequestDto {
    @NotBlank(message = "El numero de identifiacion es obligatorio")
    private String identificacion;
    @NotBlank(message = "El número de cuenta es obligatorio")
    @Size(max = 20, message = "El número de cuenta no puede exceder 20 caracteres")
    private String numeroCuenta;
    @NotBlank(message = "El tipo de cuenta es obligatorio")
    @Pattern(regexp = "^(Ahorros|Corriente)$",
            message = "Tipo de cuenta debe ser: Ahorro, Ahorros o Corriente")
    private String tipoCuenta;
    @NotNull(message = "El saldo inicial es obligatorio")
    @DecimalMin(value = "0.0", message = "El saldo inicial debe ser mayor o igual a 0")
    private BigDecimal saldoInicial;
    @NotNull(message = "El estado es obligatorio")
    private Boolean estado;

}
