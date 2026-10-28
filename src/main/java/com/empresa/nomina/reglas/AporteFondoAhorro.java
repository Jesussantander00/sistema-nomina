package com.empresa.nomina.reglas;

import com.empresa.nomina.modelo.Empleado;
import com.empresa.nomina.modelo.ParticipanteFondoAhorro;
import com.empresa.nomina.util.Dinero;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Fondo de ahorro para empleados por horas con más de 1 año en la empresa
 * que aceptaron el acceso al fondo.
 *
 * <p>Cada mes se deposita en el fondo el 2 % del salario. Ese valor sale del
 * pago del mes y queda ahorrado a nombre del empleado, por eso se registra
 * como un descuento del neto.</p>
 */
public class AporteFondoAhorro implements Deduccion {

    static final int ANIOS_MINIMOS = 1;
    static final BigDecimal PORCENTAJE = new BigDecimal("0.02");

    @Override
    public String getNombre() {
        return "Aporte fondo de ahorro (2 %)";
    }

    @Override
    public boolean aplicaA(Empleado empleado, LocalDate fechaCorte) {
        return empleado instanceof ParticipanteFondoAhorro participante
                && participante.aceptaFondoAhorro()
                && empleado.tieneAntiguedadMayorA(ANIOS_MINIMOS, fechaCorte);
    }

    @Override
    public BigDecimal calcular(Empleado empleado, BigDecimal salarioBruto) {
        return Dinero.porcentajeDe(salarioBruto, PORCENTAJE);
    }
}
