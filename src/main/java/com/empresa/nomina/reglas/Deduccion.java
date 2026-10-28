package com.empresa.nomina.reglas;

import com.empresa.nomina.modelo.Empleado;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Valor que se descuenta del salario del empleado.
 *
 * <p>Para agregar una deducción nueva basta con implementar esta interfaz y
 * registrarla en la calculadora; no se modifica ninguna clase existente
 * (principio <b>abierto/cerrado</b>).</p>
 */
public interface Deduccion {

    /** Nombre del concepto tal como aparece en el recibo. */
    String getNombre();

    /** Indica si la deducción aplica al empleado en la fecha de corte. Por defecto aplica a todos. */
    default boolean aplicaA(Empleado empleado, LocalDate fechaCorte) {
        return true;
    }

    /**
     * Calcula el valor a descontar.
     *
     * @param salarioBruto salario base + bonos del periodo
     */
    BigDecimal calcular(Empleado empleado, BigDecimal salarioBruto);
}
