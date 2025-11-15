package com.prueba.clientes.dto.request;

public record ConsultaClienteRequestDto(
        Boolean estado,
        Integer pageNumber,
        Integer pageSize) {
}
