package com.empresa.nomina.modelo;

import com.empresa.nomina.util.Dinero;
import com.empresa.nomina.util.Validaciones;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Empleado con salario fijo mensual y contrato a término definido.
 * No recibe bonos ni beneficios adicionales, y solo se le paga
 * mientras su contrato esté vigente.
 */
public class EmpleadoTemporal extends Empleado {

    private final BigDecimal salarioMensual;
    private final LocalDate fechaFinContrato;

    public EmpleadoTemporal(String documento, String nombre, LocalDate fechaIngreso,
                            NivelRiesgoArl nivelRiesgoArl, BigDecimal salarioMensual,
                            LocalDate fechaFinContrato) {
        super(documento, nombre, fechaIngreso, nivelRiesgoArl);
        this.salarioMensual = Validaciones.noNegativo(salarioMensual, "salario mensual");
        Validaciones.fechaPosterior(fechaIngreso, fechaFinContrato, "fecha fin de contrato");
        this.fechaFinContrato = fechaFinContrato;
    }

    @Override
    public BigDecimal calcularSalarioBase() {
        return Dinero.redondear(salarioMensual);
    }

    /** Vigente entre la fecha de ingreso y la fecha fin del contrato, ambas incluidas. */
    @Override
    public boolean estaVigenteEn(LocalDate fecha) {
        return super.estaVigenteEn(fecha) && !fecha.isAfter(fechaFinContrato);
    }

    @Override
    public String getTipo() {
        return "Temporal";
    }

    public LocalDate getFechaFinContrato() {
        return fechaFinContrato;
    }
}
