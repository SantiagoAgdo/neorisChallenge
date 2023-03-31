package com.neoris.santiago.controller;

import com.neoris.santiago.entity.CuentaEntity;
import com.neoris.santiago.service.CuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/cuentas")
public class CuentaController {

    @Autowired
    private CuentaService cuentaService;

    @PostMapping("/crear")
    private ResponseEntity saveCuenta(@Valid @RequestBody CuentaEntity cuentaDto, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST.getReasonPhrase(), HttpStatus.BAD_REQUEST);
        }
        cuentaService.crearCuenta(cuentaDto);
        return new ResponseEntity<>("Cuenta creado con exito" , HttpStatus.OK);
    }

    @GetMapping("/{id}")
    private ResponseEntity getCuenta(@PathVariable("id") Integer idCuenta){
        try {
            return new ResponseEntity<>(cuentaService.obtenerCuentaPorId(idCuenta), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>("No se encontraron cuentas" , HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping
    private ResponseEntity putCuenta(@Valid @RequestBody CuentaEntity cuenta, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>("error en la peticion", HttpStatus.BAD_REQUEST);
        }
        cuentaService.editarCuenta(cuenta);
        return new ResponseEntity("Editado correctamente", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    private ResponseEntity deleteCuenta(@PathVariable Integer id){
        if (cuentaService.eliminarCuenta(id)){
            return new ResponseEntity("Cuenta eliminada con exito", HttpStatus.OK);
        }else{
            return new ResponseEntity("Cuenta no pudo ser eliminada", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

