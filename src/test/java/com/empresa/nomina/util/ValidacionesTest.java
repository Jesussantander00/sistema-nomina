package com.empresa.nomina.util;

import com.empresa.nomina.excepciones.ValidacionNominaException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ValidacionesTest {

    @Test
    @DisplayName("Un valor nulo obligatorio lanza excepción con el nombre del campo")
    void requeridoRechazaNulo() {
        ValidacionNominaException error = assertThrows(ValidacionNominaException.class,
                () -> Validaciones.requerido(null, "empleado"));
        assertTrue(error.getMessage().contains("empleado"));
    }

    @Test
    @DisplayName("Un texto en blanco se rechaza y uno válido se devuelve sin espacios")
    void textoRequerido() {
        assertThrows(ValidacionNominaException.class, () -> Validaciones.textoRequerido("   ", "nombre"));
        assertEquals("Ana", Validaciones.textoRequerido("  Ana ", "nombre"));
    }

    @Test
    @DisplayName("Los montos negativos se rechazan; cero es válido")
    void noNegativo() {
        assertThrows(ValidacionNominaException.class,
                () -> Validaciones.noNegativo(new BigDecimal("-0.01"), "monto"));
        assertEquals(BigDecimal.ZERO, Validaciones.noNegativo(BigDecimal.ZERO, "monto"));
    }

    @Test
    @DisplayName("Un porcentaje debe estar entre 0 y 1")
    void porcentajeFueraDeRango() {
        assertThrows(ValidacionNominaException.class,
                () -> Validaciones.porcentaje(new BigDecimal("1.5"), "comisión"));
        assertEquals(BigDecimal.ONE, Validaciones.porcentaje(BigDecimal.ONE, "comisión"));
    }

    @Test
    @DisplayName("La fecha final debe ser posterior a la inicial")
    void fechaPosterior() {
        LocalDate inicio = LocalDate.of(2026, 1, 1);
        assertThrows(ValidacionNominaException.class,
                () -> Validaciones.fechaPosterior(inicio, inicio, "fin"));
        Validaciones.fechaPosterior(inicio, inicio.plusDays(1), "fin");
    }
}
