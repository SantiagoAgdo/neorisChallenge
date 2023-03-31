package com.neoris.santiago.repository;

import com.neoris.santiago.entity.CuentaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CuentaRepository extends JpaRepository<CuentaEntity, Integer> {

    Optional<CuentaEntity> findByNumeroCuenta(String numberAccount);
}