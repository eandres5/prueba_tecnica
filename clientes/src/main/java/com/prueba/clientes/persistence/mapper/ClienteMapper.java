package com.prueba.clientes.persistence.mapper;

import com.prueba.clientes.dto.request.ClienteRequestDto;
import com.prueba.clientes.dto.request.ClienteUpdateRequestDto;
import com.prueba.clientes.dto.response.ClienteResponseDto;
import com.prueba.clientes.persistence.entity.ClienteEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.Mapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ClienteMapper {

    /**
     * Este metodo transforma un DTO en una ENTIDAD de tipo cliente omitiendo id y clienteId.
     *
     * @param clienteRequestDto cliente DTO a transformarse
     * @return Cliente Entity con informacion del DTO recibido
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "clienteId", ignore = true)
    ClienteEntity toEntity(ClienteRequestDto clienteRequestDto);

    /**
     * Esta funcion transforma una entidad en DTO de cliente.
     *
     * @param clienteEntity entidad cliente
     * @return DTO cliente response entity con la informacion de la entidad
     */
    ClienteResponseDto toResponseDto(final ClienteEntity clienteEntity);

    /**
     * Actualiza una entidad Cliente existente con datos del Update DTO
     * Solo actualiza los campos no nulos
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(ClienteRequestDto updateDto, @MappingTarget ClienteEntity cliente);

}
