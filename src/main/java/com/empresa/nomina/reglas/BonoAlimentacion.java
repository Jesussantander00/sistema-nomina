package com.empresa.nomina.reglas;

import com.empresa.nomina.modelo.Empleado;
import com.empresa.nomina.modelo.EmpleadoPermanente;
import com.empresa.nomina.util.Dinero;

import java.math.BigDecimal;
import java.time.LocalDate;

/** Bono de alimentación de $1.000.000 al mes, cubierto por la empresa, para empleados permanentes. */
public class BonoAlimentacion implements Beneficio {

    static final BigDecimal VALOR_MENSUAL = Dinero.pesos(1_000_000);

    @Override
    public String getNombre() {
        return "Bono de alimentación";
    }

    @Override
    public boolean aplicaA(Empleado empleado, LocalDate fechaCorte) {
        return empleado instanceof EmpleadoPermanente;
    }

    @Override
    public BigDecimal calcular(Empleado empleado, BigDecimal salarioBruto) {
        return VALOR_MENSUAL;
    }
}
