package com.prueba.cuentas.service;

import com.prueba.cuentas.dto.response.ClienteResponseDto;

public interface ApiService {

    ClienteResponseDto obtenerCliente(String identificacion);

}
