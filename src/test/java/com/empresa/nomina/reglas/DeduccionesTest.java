package com.empresa.nomina.reglas;

import com.empresa.nomina.modelo.EmpleadoAsalariado;
import com.empresa.nomina.modelo.NivelRiesgoArl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static com.empresa.nomina.util.Montos.assertMonto;
import static com.empresa.nomina.util.Montos.monto;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DeduccionesTest {

    private static final LocalDate CORTE = LocalDate.of(2026, 10, 31);

    private EmpleadoAsalariado empleadoConRiesgo(NivelRiesgoArl nivel) {
        return new EmpleadoAsalariado("1", "Laura", LocalDate.of(2024, 1, 1), nivel, monto("1000000"));
    }

    @Test
    @DisplayName("Seguro social y pensión descuenta el 4 % del salario bruto")
    void seguridadSocialCuatroPorCiento() {
        DeduccionSeguridadSocial deduccion = new DeduccionSeguridadSocial();
        EmpleadoAsalariado empleado = empleadoConRiesgo(NivelRiesgoArl.I);

        assertTrue(deduccion.aplicaA(empleado, CORTE));
        assertMonto(132_000, deduccion.calcular(empleado, monto("3300000")));
    }

    @Test
    @DisplayName("La ARL usa la tarifa de riesgo I (0,522 %)")
    void arlRiesgoUno() {
        assertMonto(17_226, new DeduccionArl().calcular(empleadoConRiesgo(NivelRiesgoArl.I), monto("3300000")));
    }

    @Test
    @DisplayName("La ARL usa la tarifa de riesgo V (6,96 %)")
    void arlRiesgoCinco() {
        assertMonto(69_600, new DeduccionArl().calcular(empleadoConRiesgo(NivelRiesgoArl.V), monto("1000000")));
    }
}
