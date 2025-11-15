package com.prueba.cuentas.service.impl;

import com.prueba.cuentas.dto.request.CuentaRequestDto;
import com.prueba.cuentas.dto.request.ReporteRequestDto;
import com.prueba.cuentas.dto.response.ClienteResponseDto;
import com.prueba.cuentas.dto.response.CuentaResponseDto;
import com.prueba.cuentas.dto.response.MessageResponseDto;
import com.prueba.cuentas.exception.CuentasException;
import com.prueba.cuentas.persistence.entity.CuentaEntity;
import com.prueba.cuentas.persistence.mapper.CuentaMapper;
import com.prueba.cuentas.persistence.repository.CuentaRepository;
import com.prueba.cuentas.service.ApiService;
import com.prueba.cuentas.service.CuentaService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class CuentaServiceImpl implements CuentaService {

    private final CuentaRepository cuentaRepository;
    private final CuentaMapper cuentaMapper;
    private final ApiService apiService;

    /**
     * Este metodo crea un cuenta con un usuario.
     *
     * @param cuentaRequestDto dto con informacion de la cuenta a crear
     * @return mensaje de exito al crear cuenta
     */
    @Override
    @Transactional
    public MessageResponseDto crearCuenta(final CuentaRequestDto cuentaRequestDto) {
        log.info("Creando nueva cuenta: {}", cuentaRequestDto.getNumeroCuenta());

        if (cuentaRepository.existsByNumeroCuentaAndTipoCuenta(cuentaRequestDto.getNumeroCuenta(), cuentaRequestDto.getTipoCuenta())) {
            throw new CuentasException("Ya existe una cuenta de: ".concat(cuentaRequestDto.getTipoCuenta())
                    .concat(" con numero: ").concat(cuentaRequestDto.getNumeroCuenta()));
        }
        CuentaEntity cuenta = cuentaMapper.toEntity(cuentaRequestDto);
        cuentaRepository.save(mapCliente(cuenta, cuentaRequestDto));
        return new MessageResponseDto("Transaccion realizada con exito");
    }

    /**
     * Este metodo obtiene una cuenta por numeroCuenta.
     *
     * @param numeroCuenta numero de cuenta
     * @return CuentaResponseDto con la informacion de la cuenta
     */
    @Override
    @Transactional
    public CuentaResponseDto obtenerCuentaPorNumero(final String numeroCuenta) {
        CuentaEntity cuenta = cuentaRepository.findByNumeroCuenta(numeroCuenta)
                .orElseThrow(() -> new CuentasException("numeroCuenta".concat(numeroCuenta)));
        return cuentaMapper.toResponse(cuenta);
    }


    @Transactional
    public List<CuentaResponseDto> obtenerTodasLasCuentas() {
        log.info("Obteniendo lista de todas las cuentas");

        List<CuentaEntity> cuentas = cuentaRepository.findAll();
        return cuentaMapper.toResponseList(cuentas);
    }

    @Override
    @Transactional
    public List<CuentaResponseDto> obtenerCuentasPorCliente(final String clienteId) {
        List<CuentaEntity> cuentas = cuentaRepository.findByClienteId(clienteId);
        return cuentaMapper.toResponseList(cuentas);
    }

    /**
     * Este metodo elimina una cuenta de forma logica.
     *
     * @param numeroCuenta numero de cuenta
     * @return mensaje de exito
     */
    @Override
    @Transactional
    public MessageResponseDto eliminarCuenta(final String numeroCuenta) {
        log.info("Eliminando cuenta #: {}", numeroCuenta);
        CuentaEntity cuenta = cuentaRepository.findByNumeroCuentaAndEstado(numeroCuenta, true)
                .orElseThrow(() -> new CuentasException("Cuenta no encontrada"));
        cuenta.setEstado(false);
        cuentaRepository.save(cuenta);
        return new MessageResponseDto("Transaccion realizada con exito.");
    }

    /**
     * Este metodo obntiene una cuenta.
     *
     * @param numeroCuenta numero de cuenta
     * @return entity de tipo cuenta
     */
    @Override
    @Transactional
    public CuentaEntity obtenerCuentaEntity(final String numeroCuenta) {
        return cuentaRepository.findByNumeroCuenta(numeroCuenta)
                .orElseThrow(() -> new CuentasException("No se encontro el numero de cuenta: ".concat(numeroCuenta)));
    }

    /**
     * Este metodo actualiza el nombre del cliente en las cuentas que esten registras.
     *
     * @param clienteId     id del cliente
     * @param nombreCliente nombre del cliente
     */
    @Override
    @Transactional
    public void actualizarNombreCliente(final String clienteId, final String nombreCliente) {
        log.info("Actualizando nombre de cliente {} en cuentas", clienteId);
        List<CuentaEntity> cuentas = cuentaRepository.findByClienteId(clienteId);
        cuentas.forEach(cuenta -> {
            cuenta.setClienteNombre(nombreCliente);
            cuentaRepository.save(cuenta);
        });
    }

    /**
     * Este metodo obtiene un reporte entre un rango de fechas.
     *
     * @param reporteRequestDto rango de fechas
     * @return lista con las cuentas encontadas
     */
    @Override
    public List<CuentaEntity> obtenerReporte(final ReporteRequestDto reporteRequestDto) {
        return cuentaRepository.findCuentaReporte(reporteRequestDto.fechaInicio(), reporteRequestDto.fechaFin());
    }

    private CuentaEntity mapCliente(final CuentaEntity cuentaEntity, final CuentaRequestDto cuentaRequestDto) {
        ClienteResponseDto clienteResponseDto = apiService.obtenerCliente(cuentaRequestDto.getIdentificacion());
        cuentaEntity.setClienteId(clienteResponseDto.clienteId());
        cuentaEntity.setClienteNombre(clienteResponseDto.nombre());
        return cuentaEntity;
    }


}
