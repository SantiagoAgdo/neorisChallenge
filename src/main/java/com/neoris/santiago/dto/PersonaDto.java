package com.neoris.santiago.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PersonaDto {

    private Integer id;

    @NotNull()
    @NotEmpty()
    private String nombre;

    private String genero;

    @NotNull()
    private Integer edad;

    @NotNull()
    @NotEmpty()
    private String identificacion;

    @NotNull()
    @NotEmpty()
    private String direccion;

    @NotNull()
    @NotEmpty()
    private String telefono;
}