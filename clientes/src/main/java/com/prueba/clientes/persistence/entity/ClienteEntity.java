package com.prueba.clientes.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "cliente")
@PrimaryKeyJoinColumn(name = "persona_id")
public class ClienteEntity extends PersonaEntity {
    @Column(nullable = false, unique = true, length = 50)
    private String clienteId;
    @Column(nullable = false, length = 100)
    private String contrasena;
    @Column(nullable = false)
    private Boolean estado = true;
    @PrePersist
    public void prePersist() {
        if (this.clienteId == null || this.clienteId.isEmpty()) {
            this.clienteId = "CLI-" + System.currentTimeMillis();
        }
        if (this.estado == null) {
            this.estado = true;
        }
    }
}
