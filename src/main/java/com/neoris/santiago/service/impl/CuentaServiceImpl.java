package com.neoris.santiago.service.impl;

import com.neoris.santiago.dto.CuentaDto;
import com.neoris.santiago.entity.ClienteEntity;
import com.neoris.santiago.entity.CuentaEntity;
import com.neoris.santiago.exception.ApiException;
import com.neoris.santiago.repository.ClienteRepository;
import com.neoris.santiago.repository.CuentaRepository;
import com.neoris.santiago.service.CuentaService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Optional;

@Service
public class CuentaServiceImpl implements CuentaService {

    @Autowired
    private CuentaRepository cuentaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ModelMapper modelMapper;

    private static final String USER_NOT_FOUND = "NO SE ENCONTRO USUARIO ";

    @Override
    public void crearCuenta(CuentaEntity cuentaDto) throws ApiException {
        if(this.obtenerCuenta(cuentaDto.getNumeroCuenta()).isPresent()){
            throw new ApiException("Ya existe una cuenta con este mismo numero de cuenta", HttpStatus.OK);
        }else{
            Optional<ClienteEntity> clienteEntity = this.obtenerCliente(cuentaDto.getId());
            if(clienteEntity.isPresent()){
                CuentaEntity cuentaEntity = modelMapper.map(cuentaDto, CuentaEntity.class);
                cuentaEntity.setCliente(clienteEntity.get());
                cuentaRepository.save(cuentaEntity);
            }else{
                throw new ApiException("NO se encuentro usuario: " + cuentaDto.getId() , HttpStatus.OK);
            }
        }
    }

    private Optional<CuentaEntity> obtenerCuenta(String numeroCuenta){
        return cuentaRepository.findByNumeroCuenta(numeroCuenta);
    }

    private Optional<ClienteEntity> obtenerCliente(Integer idCliente){
        return clienteRepository.findById(idCliente);
    }


    @Override
    public CuentaDto obtenerCuentaPorId(Integer idCuenta) throws ApiException {
        Optional<CuentaEntity> cuentaEntity = cuentaRepository.findById(idCuenta);
        if(cuentaEntity.isPresent()){
            return modelMapper.map(cuentaEntity.get(), CuentaDto.class);
        }else{
            throw new ApiException(USER_NOT_FOUND, HttpStatus.NO_CONTENT);
        }
    }

    @Override
    public boolean editarCuenta(CuentaDto cuenta) throws ApiException  {
        if(!ObjectUtils.isEmpty(cuenta.getId())){
            Optional<CuentaEntity> cuentaEntity = cuentaRepository.findById(cuenta.getId());
            if(cuentaEntity.isPresent()){
                Optional<ClienteEntity> clienteEntity = this.obtenerCliente(cuenta.getIdCliente());
                if(clienteEntity.isPresent()){
                    CuentaEntity cuentaEntityPersistir = modelMapper.map(cuenta, CuentaEntity.class);
                    cuentaEntityPersistir.setCliente(clienteEntity.get());
                    cuentaRepository.save(cuentaEntityPersistir);
                    return true;
                }else{
                    throw new ApiException( USER_NOT_FOUND + cuenta.getIdCliente() , HttpStatus.NO_CONTENT);
                }
            }else{
                throw new ApiException( USER_NOT_FOUND + cuenta.getIdCliente() , HttpStatus.NO_CONTENT);
            }
        }else{
            throw new ApiException( USER_NOT_FOUND + cuenta.getIdCliente() , HttpStatus.NO_CONTENT);
        }
    }


    @Override
    public boolean eliminarCuenta(Integer id) throws ApiException {
        Optional<CuentaEntity> cuentaEntity = cuentaRepository.findById(id);
        if(cuentaEntity.isPresent()){
            cuentaRepository.deleteById(id);
            return true;
        }else {
            throw new ApiException("No se encontro cuenta con ID", HttpStatus.OK);
        }
    }
}
