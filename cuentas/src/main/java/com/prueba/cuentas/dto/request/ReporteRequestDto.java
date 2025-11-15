package com.prueba.cuentas.dto.request;

import java.time.LocalDateTime;

public record ReporteRequestDto(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
}
