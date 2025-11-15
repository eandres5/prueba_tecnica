package com.prueba.clientes.dto.request;

public record ClienteUpdateRequestDto (Long id,
                                       String nombre,
                                       String genero,
                                       Integer edad,
                                       String identificacion,
                                       String direccion,
                                       String telefono,
                                       String contrasena,
                                       Boolean estado){
}
