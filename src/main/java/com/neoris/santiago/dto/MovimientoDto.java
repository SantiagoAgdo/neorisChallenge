package com.neoris.santiago.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MovimientoDto implements Serializable {

    private Integer id;

    @NotNull()
    @JsonFormat(pattern = "yyyy-mm-dd")
    private LocalDate fecha;

    private String tipoMovimiento;

    @NotNull()
    private BigDecimal valor;

    @NotNull()
    private BigDecimal saldo;

    @NotNull()
    private Integer idCuenta;
}

