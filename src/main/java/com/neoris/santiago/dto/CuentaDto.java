package com.neoris.santiago.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CuentaDto implements Serializable {

    private Integer id;

    @NotNull()
    @NotEmpty()
    @Size()
    private String numeroCuenta;

    private String tipoCuenta;

    @NotNull()
    private BigDecimal saldoInicial;

    @NotNull()
    private Boolean estado;

    @NotNull()
    private Integer idCliente;
}
