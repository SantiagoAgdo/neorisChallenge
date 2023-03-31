package com.neoris.santiago.repository;

import com.neoris.santiago.entity.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<ClienteEntity, Integer> {

    @Query("SELECT CLIENTE FROM ClienteEntity CLIENTE WHERE CLIENTE.persona.identificacion = :nIdentificacion")
    Optional<ClienteEntity> findByIdentificacion(String nIdentificacion);
}
