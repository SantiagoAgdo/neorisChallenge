package com.neoris.santiago.service.impl;

import com.neoris.santiago.dto.ClienteDto;
import com.neoris.santiago.entity.ClienteEntity;
import com.neoris.santiago.exception.ApiException;
import com.neoris.santiago.repository.ClienteRepository;
import com.neoris.santiago.service.ClienteService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Optional;

@Service
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ModelMapper modelMapper;

    private static final String USER_NOT_FOUND = "NO SE ENCONTRO USUARIO ";


    @Override
    public void crearCliente(ClienteDto cliente) throws ApiException {
        if(this.consultarClientePorIdentificacion(cliente.getPersona().getIdentificacion()).isPresent()){
            throw new ApiException("Identificacion ya esta en uso", HttpStatus.OK);
        }else{
            ClienteEntity clienteEntity = modelMapper.map(cliente, ClienteEntity.class);
            clienteRepository.save(clienteEntity);
        }
    }

    @Override
    public ClienteDto obtenerClientePorId(Integer idCliente) throws ApiException {
        Optional<ClienteEntity> clienteEntity = clienteRepository.findById(idCliente);
        if(clienteEntity.isPresent()){
            return modelMapper.map(clienteEntity.get(), ClienteDto.class);
        }else{
            throw new ApiException(USER_NOT_FOUND, HttpStatus.OK);
        }
    }

    @Override
    public boolean editarCliente(ClienteDto cliente) throws ApiException {
        if(!ObjectUtils.isEmpty(cliente.getId())){
            Optional<ClienteEntity> clienteEntity = clienteRepository.findById(cliente.getId());
            if(clienteEntity.isPresent()){
                clienteRepository.save(modelMapper.map(cliente, ClienteEntity.class));
                return true;
            }else{
                throw new ApiException(USER_NOT_FOUND + cliente.getId(), HttpStatus.OK);
            }
        }else{
            throw new ApiException(USER_NOT_FOUND + cliente.getId(), HttpStatus.OK);
        }
    }

    @Override
    public boolean eliminarCliente(Integer id) throws ApiException {
        Optional<ClienteEntity> clienteEntity = clienteRepository.findById(id);
        if(clienteEntity.isPresent()){
            clienteRepository.deleteById(id);
            return true;
        }else {
            throw new ApiException(USER_NOT_FOUND,HttpStatus.OK);
        }
    }

    private Optional<ClienteEntity> consultarClientePorIdentificacion(String identificacion){
        return clienteRepository.findByIdentificacion(identificacion);
    }
}
