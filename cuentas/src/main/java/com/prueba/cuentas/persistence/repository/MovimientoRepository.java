package com.prueba.cuentas.persistence.repository;

import com.prueba.cuentas.persistence.entity.MovimientoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovimientoRepository extends JpaRepository<MovimientoEntity, Long> {

}
