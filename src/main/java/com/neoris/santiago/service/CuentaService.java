package com.neoris.santiago.service;

import com.neoris.santiago.dto.CuentaDto;
import com.neoris.santiago.entity.CuentaEntity;

public interface CuentaService {

    void crearCuenta(CuentaEntity cuentaDto);

    CuentaDto obtenerCuentaPorId(Integer idCuenta);

    boolean editarCuenta(CuentaEntity cuenta);

    boolean eliminarCuenta(Integer id);
}
