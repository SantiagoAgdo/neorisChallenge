package com.neoris.santiago.controller;

import com.neoris.santiago.entity.ClienteEntity;
import com.neoris.santiago.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;


    @PostMapping
    private ResponseEntity saveClient(@Valid @RequestBody ClienteEntity cliente, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>("Error en la peticion", HttpStatus.BAD_REQUEST);
        }
        try {
            clienteService.crearCliente(cliente);
            return  new ResponseEntity("Cliente Creado con exito", HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>("Error en la creacion del cliente, vuelva a intentarlo", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    private ResponseEntity getCliente(@PathVariable("id") Integer id) {
        try{
            return new ResponseEntity(clienteService.obtenerClientePorId(id), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>("No se encontraron Usuarios", HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping
    private ResponseEntity putClient(@Valid @RequestBody ClienteEntity cliente, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>("error en la peticion", HttpStatus.BAD_REQUEST);
        }
        clienteService.editarCliente(cliente);
        return new ResponseEntity("Editado correctamente", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    private ResponseEntity deleteCliente(@PathVariable Integer id) {
        if (clienteService.eliminarCliente(id)){
            return new ResponseEntity("Cliente eliminada con exito", HttpStatus.OK);
        }else{
            return new ResponseEntity("Cliente no pudo ser eliminada", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
