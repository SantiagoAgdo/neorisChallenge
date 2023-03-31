package com.neoris.santiago.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ClienteDto implements Serializable {

    private Integer id;

    @Valid
    @NotNull()
    private PersonaDto persona;

    @NotNull()
    @NotEmpty()
    private String contrasena;

    @NotNull()
    private Boolean estado;
}

