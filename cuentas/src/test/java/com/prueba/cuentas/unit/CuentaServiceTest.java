package com.prueba.cuentas.unit;

import com.prueba.cuentas.dto.request.CuentaRequestDto;
import com.prueba.cuentas.dto.response.CuentaResponseDto;
import com.prueba.cuentas.exception.CuentasException;
import com.prueba.cuentas.persistence.entity.CuentaEntity;
import com.prueba.cuentas.persistence.mapper.CuentaMapper;
import com.prueba.cuentas.persistence.repository.CuentaRepository;
import com.prueba.cuentas.service.impl.CuentaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CuentaServiceTest {

    @Mock
    private CuentaRepository cuentaRepository;

    @InjectMocks
    private CuentaServiceImpl cuentaService;

    private CuentaEntity cuenta;
    private CuentaRequestDto requestDto;
    private CuentaResponseDto responseDto;

    @BeforeEach
    void setUp() {
        // Preparar datos de prueba
        cuenta = new CuentaEntity();
        cuenta.setId(1L);
        cuenta.setNumeroCuenta("478758");
        cuenta.setTipoCuenta("Ahorro");
        cuenta.setSaldoInicial(new BigDecimal("2000.00"));
        cuenta.setSaldoActual(new BigDecimal("2000.00"));
        cuenta.setEstado(true);
        cuenta.setClienteId("CLI-12345");
        cuenta.setClienteNombre("Jose Lema");

        requestDto = new CuentaRequestDto("1752586392", "478758", "Ahorro", new BigDecimal("2000.00"),
                true);
        responseDto = CuentaResponseDto.builder()
                .id(1L)
                .numeroCuenta("478758")
                .tipoCuenta("Ahorro")
                .saldoInicial(new BigDecimal("2000.00"))
                .saldoActual(new BigDecimal("2000.00"))
                .estado(true)
                .clienteId("CLI-12345")
                .clienteNombre("Jose Lema")
                .build();
    }

    @Test
    void testCrearCuenta_NumeroDuplicado() {
        // Arrange
        when(cuentaRepository.existsByNumeroCuentaAndTipoCuenta(anyString(), anyString())).thenReturn(true);

        // Act & Assert
        CuentasException exception = assertThrows(
                CuentasException.class,
                () -> cuentaService.crearCuenta(requestDto)
        );

        assertTrue(exception.getMessage().contains("Ya existe una cuenta"));
        verify(cuentaRepository, times(1)).existsByNumeroCuentaAndTipoCuenta("478758", "Ahorro");
        verify(cuentaRepository, never()).save(any(CuentaEntity.class));
    }

    @Test
    void testObtenerCuentaPorId_Encontrada() {
        when(cuentaRepository.findByNumeroCuenta("2222222")).thenReturn(Optional.empty());

        CuentasException exception = assertThrows(
                CuentasException.class,
                () -> cuentaService.obtenerCuentaPorNumero("2222222")
        );
        assertTrue(exception.getMessage().contains("2222222"));
        verify(cuentaRepository, times(1)).findByNumeroCuenta("2222222");
    }

}
