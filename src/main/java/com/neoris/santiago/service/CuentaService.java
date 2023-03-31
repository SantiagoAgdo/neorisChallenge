package com.neoris.santiago.service;

import com.neoris.santiago.dto.CuentaDto;

public interface CuentaService {

    void crearCuenta(CuentaDto cuentaDto);

    CuentaDto obtenerCuentaPorId(Integer idCuenta);

    boolean editarCuenta(CuentaDto cuenta);

    boolean eliminarCuenta(Integer id);
}
