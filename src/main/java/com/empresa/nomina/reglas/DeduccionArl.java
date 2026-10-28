package com.empresa.nomina.reglas;

import com.empresa.nomina.modelo.Empleado;
import com.empresa.nomina.util.Dinero;

import java.math.BigDecimal;

/**
 * ARL (riesgos laborales): porcentaje del salario bruto según
 * la clase de riesgo asignada a cada empleado.
 */
public class DeduccionArl implements Deduccion {

    @Override
    public String getNombre() {
        return "ARL";
    }

    @Override
    public BigDecimal calcular(Empleado empleado, BigDecimal salarioBruto) {
        return Dinero.porcentajeDe(salarioBruto, empleado.getNivelRiesgoArl().getTarifa());
    }
}
