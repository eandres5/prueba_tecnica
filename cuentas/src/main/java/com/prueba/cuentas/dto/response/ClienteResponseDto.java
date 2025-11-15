package com.prueba.cuentas.dto.response;

public record ClienteResponseDto(String clienteId,
                                 String nombre,
                                 String genero,
                                 Integer edad,
                                 String identificacion,
                                 String direccion,
                                 String telefono,
                                 Boolean estado){
}
