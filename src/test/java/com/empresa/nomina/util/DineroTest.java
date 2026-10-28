package com.empresa.nomina.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DineroTest {

    @Test
    @DisplayName("Redondea a pesos enteros con la regla de mitad hacia arriba")
    void redondeaMitadHaciaArriba() {
        assertEquals(new BigDecimal("17227"), Dinero.redondear(new BigDecimal("17226.5")));
        assertEquals(new BigDecimal("17226"), Dinero.redondear(new BigDecimal("17226.49")));
    }

    @Test
    @DisplayName("Calcula un porcentaje de una base y lo redondea")
    void porcentajeDe() {
        assertEquals(new BigDecimal("17226"),
                Dinero.porcentajeDe(new BigDecimal("3300000"), new BigDecimal("0.00522")));
    }
}
