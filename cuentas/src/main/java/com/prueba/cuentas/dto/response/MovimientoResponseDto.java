package com.prueba.cuentas.dto.response;

import java.math.BigDecimal;

public record MovimientoResponseDto(String tipo, BigDecimal valor, BigDecimal saldo, String clienteNombre) {

}
