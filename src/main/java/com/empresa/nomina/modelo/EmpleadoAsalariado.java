package com.empresa.nomina.modelo;

import com.empresa.nomina.util.Dinero;
import com.empresa.nomina.util.Validaciones;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Empleado con salario fijo mensual.
 * Recibe un bono del 10 % del salario si lleva más de 5 años en la empresa.
 */
public class EmpleadoAsalariado extends Empleado implements EmpleadoPermanente {

    static final int ANIOS_PARA_BONO = 5;
    static final BigDecimal PORCENTAJE_BONO_ANTIGUEDAD = new BigDecimal("0.10");

    private final BigDecimal salarioMensual;

    public EmpleadoAsalariado(String documento, String nombre, LocalDate fechaIngreso,
                              NivelRiesgoArl nivelRiesgoArl, BigDecimal salarioMensual) {
        super(documento, nombre, fechaIngreso, nivelRiesgoArl);
        this.salarioMensual = Validaciones.noNegativo(salarioMensual, "salario mensual");
    }

    @Override
    public BigDecimal calcularSalarioBase() {
        return Dinero.redondear(salarioMensual);
    }

    @Override
    public BigDecimal calcularBonos(LocalDate fechaCorte) {
        if (tieneAntiguedadMayorA(ANIOS_PARA_BONO, fechaCorte)) {
            return Dinero.porcentajeDe(salarioMensual, PORCENTAJE_BONO_ANTIGUEDAD);
        }
        return Dinero.CERO;
    }

    @Override
    public String getTipo() {
        return "Asalariado";
    }

    public BigDecimal getSalarioMensual() {
        return salarioMensual;
    }
}
