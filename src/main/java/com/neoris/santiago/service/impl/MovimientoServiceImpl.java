package com.neoris.santiago.service.impl;

import com.neoris.santiago.dto.MovimientoDto;
import com.neoris.santiago.dto.ReporteDto;
import com.neoris.santiago.entity.ClienteEntity;
import com.neoris.santiago.entity.CuentaEntity;
import com.neoris.santiago.entity.MovimientoEntity;
import com.neoris.santiago.exception.ApiException;
import com.neoris.santiago.repository.ClienteRepository;
import com.neoris.santiago.repository.CuentaRepository;
import com.neoris.santiago.repository.MovimientosRepository;
import com.neoris.santiago.service.MovimientoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MovimientoServiceImpl implements MovimientoService {

    @Autowired
    private CuentaRepository cuentaRepository;

    @Autowired
    private MovimientosRepository movimientosRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ModelMapper modelMapper;

    private static final String USER_NOT_FOUND = "NO SE ENCONTRO USUARIO";


    @Override
    public void crearMovimiento(MovimientoDto movimientoDto) throws ApiException {
        Optional<CuentaEntity> cuentaEntity = this.obtenerCuentaPorId(movimientoDto.getId());
        if(cuentaEntity.isPresent()){
            if(movimientoDto.getTipoMovimiento().toUpperCase().equals("CREDITO")){
                movimientoDto.setSaldo(movimientoDto.getSaldo().add(movimientoDto.getValor()));
            }else{
                if(movimientoDto.getSaldo().equals(BigDecimal.ZERO)){
                    throw new ApiException("Saldo no Disponible",HttpStatus.OK);
                }
                movimientoDto.setSaldo(movimientoDto.getSaldo().subtract(movimientoDto.getValor()));
            }
            MovimientoEntity movimientoEntity = modelMapper.map(movimientoDto, MovimientoEntity.class);
            movimientoEntity.setCuenta(cuentaEntity.get());
            movimientosRepository.save(movimientoEntity);
        }else{
            throw new ApiException( USER_NOT_FOUND, HttpStatus.OK);
        }
    }

    @Override
    public Optional<MovimientoEntity> obtenerMovimientoPorId(Integer idMovimiento) throws ApiException {
        Optional<MovimientoEntity> movimientoEntity = movimientosRepository.findById(idMovimiento);
        if(movimientoEntity.isPresent()){
            return movimientoEntity;
        }else{
            throw new ApiException("Movimiento con ID no encontrado - id: " + idMovimiento,HttpStatus.OK);
        }
    }

    @Override
    public boolean editarMovimiento(MovimientoDto movimiento) throws ApiException {
        if(!ObjectUtils.isEmpty(movimiento.getId())){
            Optional<MovimientoEntity> movimientoEntity = movimientosRepository.findById(movimiento.getId());
            if(movimientoEntity.isPresent()){
                Optional<CuentaEntity> cuentaEntity = this.obtenerCuentaPorId(movimiento.getIdCuenta());
                if(cuentaEntity.isPresent()){
                    MovimientoEntity movimientoEntityPersistir = modelMapper.map(movimiento, MovimientoEntity.class);
                    movimientoEntityPersistir.setCuenta(cuentaEntity.get());
                    movimientosRepository.save(movimientoEntityPersistir);
                    return true;
                }else{
                    throw new ApiException( USER_NOT_FOUND + movimiento.getId() , HttpStatus.NO_CONTENT);
                }
            }else{
                throw new ApiException( USER_NOT_FOUND + movimiento.getId() , HttpStatus.NO_CONTENT);
            }
        }else{
            throw new ApiException( USER_NOT_FOUND + movimiento.getId() , HttpStatus.NO_CONTENT);
        }
    }


    @Override
    public boolean eliminarMovimiento(Integer id) throws ApiException {
        Optional<MovimientoEntity> movimiento = movimientosRepository.findById(id);
        if(movimiento.isPresent()){
            movimientosRepository.deleteById(id);
            return true;
        }else {
            throw new ApiException("No se encontro cuenta con ID", HttpStatus.OK);
        }
    }

    @Override
    public List<ReporteDto> obtenerReportePorFechas(LocalDate fechaInicial, LocalDate fechaFinal, Integer idCliente) {
        Optional<ClienteEntity> clienteEntity = clienteRepository.findById(idCliente);
        if(clienteEntity.isPresent()){
            List<MovimientoEntity> movimientoEntityList = movimientosRepository.getMovimientoEntityByFechas(fechaInicial, fechaFinal);
            if(movimientoEntityList.isEmpty()){
                throw new ApiException("Movimientos no encontrados en fecha indicada", HttpStatus.OK);
            }else{
                return this.generarListaReporte(movimientoEntityList, idCliente);
            }
        }else{
            throw new ApiException(USER_NOT_FOUND, HttpStatus.OK);
        }
    }

    private List<ReporteDto> generarListaReporte(List<MovimientoEntity> movimientoEntityList, Integer idCliente ){
        return movimientoEntityList.stream()
                .filter(movimiento -> movimiento.getCuenta().getCliente().getId().equals(idCliente))
                .map(movimiento -> ReporteDto.builder()
                        .fecha(movimiento.getFecha())
                        .cliente(movimiento.getCuenta().getCliente().getPersona().getNombre())
                        .numeroCuenta(movimiento.getCuenta().getNumeroCuenta())
                        .tipoCuenta(movimiento.getCuenta().getTipoCuenta())
                        .saldoInicial(movimiento.getCuenta().getSaldoInicial())
                        .estado(movimiento.getCuenta().getEstado())
                        .tipoMovimiento(movimiento.getTipoMovimiento())
                        .saldoDisponible(movimiento.getSaldo())
                        .build()
                ).collect(Collectors.toList());
    }

    private Optional<CuentaEntity> obtenerCuentaPorId(Integer idCuenta){
        return cuentaRepository.findById(idCuenta);
    }

}
