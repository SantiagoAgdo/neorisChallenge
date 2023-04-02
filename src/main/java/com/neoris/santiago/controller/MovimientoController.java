package com.neoris.santiago.controller;

import com.neoris.santiago.dto.MovimientoDto;
import com.neoris.santiago.service.MovimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequestMapping(value = "/movimientos")
public class MovimientoController {

    @Autowired
    private MovimientoService movimientoService;

    @PostMapping("/crear")
    public ResponseEntity<?> saveMovimiento(@Valid @RequestBody MovimientoDto movimiento, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>("Error en Peticion", HttpStatus.BAD_REQUEST);
        }
        try {
            movimientoService.crearMovimiento(movimiento);
            return  new ResponseEntity<>("Movimiento Creado con exito", HttpStatus.OK);
        }catch (Exception e){
            if (movimiento.getTipoMovimiento().equals("debito") && movimiento.getSaldo().equals(BigDecimal.ZERO)){
                return new ResponseEntity<>("Saldo no disponible", HttpStatus.CONFLICT);
            }
            return new ResponseEntity<>("Error en la creacion de movimiento, vuelva a intentarlo", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMovimiento(@PathVariable("id") Integer idMovimiento){
        try {
            return new ResponseEntity<>(movimientoService.obtenerMovimientoPorId(idMovimiento), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>("No se encontraron movimientos" , HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping
    public ResponseEntity<?> putMovimiento(@Valid @RequestBody MovimientoDto movimientoDto, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>("error en la peticion", HttpStatus.BAD_REQUEST);
        }
        movimientoService.editarMovimiento(movimientoDto);
        return new ResponseEntity<>("Movimiento editado correctamente", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMovimiento(@PathVariable Integer id){
        if (movimientoService.eliminarMovimiento(id)){
            return new ResponseEntity<>("Movimiento eliminado con exito", HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Movimiento no pudo ser eliminado", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/reportes")
    public ResponseEntity<?> gteReporte(@RequestParam(name = "fechainicial")  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechainicial,
                                      @RequestParam(name = "fechafinal")    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFinal,
                                      @RequestParam(name = "id") Integer id) {
        try {
            return new ResponseEntity<>(movimientoService.obtenerReportePorFechas(fechainicial, fechaFinal, id), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>("No se encontraron registros", HttpStatus.FORBIDDEN);
        }
    }

}
