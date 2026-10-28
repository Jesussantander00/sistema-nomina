package com.empresa.nomina.reglas;

import com.empresa.nomina.modelo.Empleado;
import com.empresa.nomina.util.Dinero;

import java.math.BigDecimal;

/** Seguro social y pensión: 4 % del salario bruto. Aplica a todos los empleados. */
public class DeduccionSeguridadSocial implements Deduccion {

    static final BigDecimal PORCENTAJE = new BigDecimal("0.04");

    @Override
    public String getNombre() {
        return "Seguro social y pensión (4 %)";
    }

    @Override
    public BigDecimal calcular(Empleado empleado, BigDecimal salarioBruto) {
        return Dinero.porcentajeDe(salarioBruto, PORCENTAJE);
    }
}
