package com.prueba.clientes.persistence.repository;

import com.prueba.clientes.persistence.entity.ClienteEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<ClienteEntity, Long> {

    /**
     * Este metodo obtiene un entity de tipo cliente por numero de identificacion.
     *
     * @param identificacion numero de identificaion del cliente
     * @return cliente entity
     */
    Optional<ClienteEntity> findByIdentificacionAndEstado(String identificacion, Boolean estado);


    /**
     * Este metodo verifica si existe un registro por numero de identificacion.
     *
     * @param identificacion numero de identificacion
     * @return boolean con valor true o false
     */
    boolean existsByIdentificacion(String identificacion);

    /**
     * Este metodo consulta una lista de cliente paginados por estado.
     *
     * @param estado   estado del cliente
     * @param pageable paginacion
     * @return lista de clientes
     */
    @Query("SELECT c FROM ClienteEntity c WHERE (:estado IS NULL OR c.estado = :estado)")
    Page<ClienteEntity> consultarClientes(
            @Param("estado") Boolean estado,
            Pageable pageable
    );
}
