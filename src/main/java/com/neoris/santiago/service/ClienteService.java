package com.neoris.santiago.service;

import com.neoris.santiago.dto.ClienteDto;
import com.neoris.santiago.exception.ApiException;

public interface ClienteService {

    void crearCliente(ClienteDto cliente) throws ApiException;
    ClienteDto obtenerClientePorId(Integer idCliente) throws ApiException;

    boolean editarCliente(ClienteDto cliente) throws ApiException;

    boolean eliminarCliente(Integer id) throws ApiException;
}
