package com.neoris.santiago.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ReporteDto implements Serializable {

    private LocalDate fecha;

    private String cliente;

    private String numeroCuenta;

    private String tipoCuenta;

    private int saldoInicial;

    private Boolean estado;

    private String tipoMovimiento;

    private int saldoDisponible;

}
