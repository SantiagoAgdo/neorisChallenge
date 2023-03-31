package com.neoris.santiago.service;

import com.neoris.santiago.dto.MovimientoDto;
import com.neoris.santiago.dto.ReporteDto;
import com.neoris.santiago.entity.MovimientoEntity;
import com.neoris.santiago.exception.ApiException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MovimientoService {

    void crearMovimiento(MovimientoEntity movimientoDto) throws ApiException;

    Optional<MovimientoEntity> obtenerMovimientoPorId(Integer idMovimiento) throws ApiException;

    boolean editarMovimiento(MovimientoDto movimientoDto) throws ApiException;

    boolean eliminarMovimiento(Integer id) throws ApiException;

    List<ReporteDto> obtenerReportePorFechas(LocalDate fechaInicial, LocalDate fechaFinal, Integer idCliente);
}

