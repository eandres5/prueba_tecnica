package com.prueba.cuentas.persistence.mapper;

import com.prueba.cuentas.dto.request.CuentaRequestDto;
import com.prueba.cuentas.dto.response.CuentaResponseDto;
import com.prueba.cuentas.dto.response.ReporteResponseDto;
import com.prueba.cuentas.persistence.entity.CuentaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface CuentaMapper {
    /**
     * Convierte Request DTO a Entidad Cuenta
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "movimientos", ignore = true)
    @Mapping(target = "saldoActual", source = "saldoInicial")
    CuentaEntity toEntity(CuentaRequestDto cuentaRequestDto);

    /**
     * Convierte Entidad Cuenta a Response DTO
     */
    CuentaResponseDto toResponse(CuentaEntity cuentaEntity);

    /**
     * Convierte lista de Cuentas a lista de Response DTOs
     */
    List<CuentaResponseDto> toResponseList(List<CuentaEntity> cuentas);

}
