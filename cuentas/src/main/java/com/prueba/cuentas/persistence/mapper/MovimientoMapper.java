package com.prueba.cuentas.persistence.mapper;

import com.prueba.cuentas.dto.response.MovimientoResponseDto;
import com.prueba.cuentas.persistence.entity.MovimientoEntity;
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
public interface MovimientoMapper {

    /**
     * Convierte Request DTO a Entidad Movimiento
     */
    @Mapping(target = "id", ignore = true)
    MovimientoEntity toEntity(MovimientoResponseDto request);

    /**
     * Convierte Entidad Cuenta a Response DTO
     */
    MovimientoResponseDto toResponse(MovimientoEntity movimientoEntity);

    /**
     * Convierte lista de Cuentas a lista de Response DTOs
     */
    List<MovimientoResponseDto> toResponseList(List<MovimientoEntity> movimientos);

}
