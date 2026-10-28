package com.empresa.nomina.reglas;

import com.empresa.nomina.modelo.EmpleadoAsalariado;
import com.empresa.nomina.modelo.EmpleadoPorComision;
import com.empresa.nomina.modelo.EmpleadoPorHoras;
import com.empresa.nomina.modelo.EmpleadoTemporal;
import com.empresa.nomina.modelo.NivelRiesgoArl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static com.empresa.nomina.util.Montos.assertMonto;
import static com.empresa.nomina.util.Montos.monto;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BonoAlimentacionTest {

    private static final LocalDate CORTE = LocalDate.of(2026, 10, 31);
    private static final LocalDate INGRESO = LocalDate.of(2025, 1, 1);
    private final BonoAlimentacion bono = new BonoAlimentacion();

    @Test
    @DisplayName("Aplica a asalariados y empleados por comisión (permanentes)")
    void aplicaAPermanentes() {
        assertTrue(bono.aplicaA(new EmpleadoAsalariado("1", "Laura", INGRESO,
                NivelRiesgoArl.I, monto("3000000")), CORTE));
        assertTrue(bono.aplicaA(new EmpleadoPorComision("3", "Ana", INGRESO,
                NivelRiesgoArl.I, monto("1500000"), monto("0.05")), CORTE));
    }

    @Test
    @DisplayName("No aplica a empleados por horas ni temporales")
    void noAplicaANoPermanentes() {
        assertFalse(bono.aplicaA(new EmpleadoPorHoras("2", "Carlos", INGRESO,
                NivelRiesgoArl.I, monto("20000"), true), CORTE));
        assertFalse(bono.aplicaA(new EmpleadoTemporal("4", "Pedro", INGRESO,
                NivelRiesgoArl.I, monto("2000000"), LocalDate.of(2026, 12, 31)), CORTE));
    }

    @Test
    @DisplayName("Vale $1.000.000 sin importar el salario")
    void valorFijo() {
        assertMonto(1_000_000, bono.calcular(null, monto("5000000")));
    }
}
