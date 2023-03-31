package com.neoris.santiago.repository;

import com.neoris.santiago.entity.MovimientoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MovimientosRepository extends JpaRepository<MovimientoEntity, Integer> {

    @Query("SELECT MOVIMIENTO FROM MovimientoEntity MOVIMIENTO WHERE MOVIMIENTO.fecha BETWEEN :fechaInicial AND :fechaFinal")
    List<MovimientoEntity> getMovimientoEntityByFechas(LocalDate fechaInicial, LocalDate fechaFinal);
}
