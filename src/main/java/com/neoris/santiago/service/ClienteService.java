package com.neoris.santiago.service;

import com.neoris.santiago.dto.ClienteDto;
import com.neoris.santiago.entity.ClienteEntity;
import com.neoris.santiago.exception.ApiException;

public interface ClienteService {

    void crearCliente(ClienteEntity cliente) throws ApiException;
    ClienteDto obtenerClientePorId(Integer idCliente) throws ApiException;

    boolean editarCliente(ClienteEntity cliente) throws ApiException;

    boolean eliminarCliente(Integer id) throws ApiException;
}
