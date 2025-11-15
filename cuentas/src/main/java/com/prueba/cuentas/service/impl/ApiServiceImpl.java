package com.prueba.cuentas.service.impl;

import com.prueba.cuentas.dto.response.ClienteResponseDto;
import com.prueba.cuentas.exception.CuentasException;
import com.prueba.cuentas.service.ApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;


@Slf4j
@Service
@RequiredArgsConstructor
public class ApiServiceImpl implements ApiService {

    private final RestTemplate restTemplate;

    public ClienteResponseDto obtenerCliente(final String identificacion) {
        HttpEntity<Void> request;
        ResponseEntity<ClienteResponseDto> response;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        request = new HttpEntity<>(headers);

        String url = "http://localhost:8081/api/clientes/".concat(identificacion);
        response = restTemplate.exchange(url, HttpMethod.GET, request, ClienteResponseDto.class);
        if (response.getStatusCode().isError()) {
            throw new CuentasException("Error al obtener el cliente");
        }
        Objects.requireNonNull(response.getBody(), "El cliente no existe");

        return response.getBody();
    }
}
