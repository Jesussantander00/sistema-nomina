package com.empresa.nomina.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Operaciones monetarias del sistema.
 * Se usa {@link BigDecimal} (nunca double) para evitar errores de redondeo,
 * y todos los valores se redondean a pesos enteros, como se paga en Colombia.
 */
public final class Dinero {

    /** Cero pesos, con la escala usada por el sistema. */
    public static final BigDecimal CERO = redondear(BigDecimal.ZERO);

    private Dinero() {
        // Clase utilitaria: no se instancia.
    }

    /** Redondea un valor a pesos enteros (mitad hacia arriba). */
    public static BigDecimal redondear(BigDecimal valor) {
        return valor.setScale(0, RoundingMode.HALF_UP);
    }

    /** Calcula {@code base × porcentaje} y lo redondea a pesos. */
    public static BigDecimal porcentajeDe(BigDecimal base, BigDecimal porcentaje) {
        return redondear(base.multiply(porcentaje));
    }

    /** Crea un monto en pesos a partir de un número entero. */
    public static BigDecimal pesos(long valor) {
        return redondear(BigDecimal.valueOf(valor));
    }
}
