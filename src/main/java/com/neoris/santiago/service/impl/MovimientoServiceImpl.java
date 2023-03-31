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
    public void crearMovimiento(MovimientoEntity movimientoDto) throws ApiException {
        Optional<CuentaEntity> cuentaEntity = this.obtenerCuentaPorId(movimientoDto.getId());
        if(cuentaEntity.isPresent()){
            if(movimientoDto.getTipoMovimiento().toUpperCase().equals("CREDITO")){
                movimientoDto.setSaldo(movimientoDto.getSaldo() + movimientoDto.getValor());
            }else{
                if(movimientoDto.getSaldo() == 0){
                    throw new ApiException("Saldo no Disponible",HttpStatus.OK);
                }
                movimientoDto.setSaldo(movimientoDto.getSaldo() - movimientoDto.getValor());
            }
            MovimientoEntity movimientoEntity = modelMapper.map(movimientoDto, MovimientoEntity.class);
            movimientoEntity.setCuenta(cuentaEntity.get());
            movimientosRepository.save(movimientoEntity);
        }else{
            throw new ApiException( USER_NOT_FOUND, HttpStatus.OK);
        }
    }

    @Override
    public MovimientoDto obtenerMovimientoPorId(Integer idMovimiento) throws ApiException {
        Optional<MovimientoEntity> movimientoEntity = movimientosRepository.findById(idMovimiento);
        if(movimientoEntity.isPresent()){
            return modelMapper.map(movimientoEntity.get(), MovimientoDto.class);
        }else{
            throw new ApiException("Movimiento con ID no encontrado",HttpStatus.OK);
        }
    }

    @Override
    public boolean editarMovimiento(MovimientoEntity movimiento) throws ApiException {
        Optional<MovimientoEntity> movimientoEntity = movimientosRepository.findById(movimiento.getId());

        if(movimientoEntity == null){
            return false;
        }

        Optional<MovimientoEntity> movimientoDto = movimientoEntity;
        movimientoDto.get().setId(movimiento.getId());
        movimientoDto.get().setTipoMovimiento(movimiento.getTipoMovimiento());
        movimientoDto.get().setSaldo(movimiento.getSaldo());
        movimientoDto.get().setValor(movimiento.getValor());
        movimientoDto.get().setFecha(movimiento.getFecha());

        crearMovimiento(movimientoDto.get());
        return true;
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
