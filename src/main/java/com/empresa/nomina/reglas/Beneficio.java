package com.empresa.nomina.reglas;

import com.empresa.nomina.modelo.Empleado;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Pago adicional no salarial que la empresa reconoce al empleado
 * (no hace parte del salario bruto, por eso no se le aplican deducciones).
 */
public interface Beneficio {

    /** Nombre del concepto tal como aparece en el recibo. */
    String getNombre();

    /** Indica si el empleado tiene derecho al beneficio en la fecha de corte. */
    boolean aplicaA(Empleado empleado, LocalDate fechaCorte);

    /**
     * Calcula el valor del beneficio.
     *
     * @param salarioBruto salario base + bonos del periodo
     */
    BigDecimal calcular(Empleado empleado, BigDecimal salarioBruto);
}
