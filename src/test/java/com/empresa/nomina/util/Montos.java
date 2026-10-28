package com.empresa.nomina.util;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

/** Utilidades compartidas por las pruebas para comparar montos en pesos. */
public final class Montos {

    private Montos() {
    }

    /** Compara un monto esperado (en pesos enteros) con el valor calculado, ignorando la escala. */
    public static void assertMonto(long esperado, BigDecimal real) {
        assertEquals(BigDecimal.valueOf(esperado), Dinero.redondear(real),
                () -> "Se esperaba $" + esperado + " pero se obtuvo $" + real);
    }

    public static BigDecimal monto(String valor) {
        return new BigDecimal(valor);
    }
}
