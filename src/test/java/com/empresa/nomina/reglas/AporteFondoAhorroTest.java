package com.empresa.nomina.reglas;

import com.empresa.nomina.modelo.EmpleadoAsalariado;
import com.empresa.nomina.modelo.EmpleadoPorHoras;
import com.empresa.nomina.modelo.NivelRiesgoArl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static com.empresa.nomina.util.Montos.assertMonto;
import static com.empresa.nomina.util.Montos.monto;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AporteFondoAhorroTest {

    private static final LocalDate CORTE = LocalDate.of(2026, 10, 31);
    private final AporteFondoAhorro fondo = new AporteFondoAhorro();

    private EmpleadoPorHoras porHoras(LocalDate ingreso, boolean acepta) {
        return new EmpleadoPorHoras("2", "Carlos", ingreso, NivelRiesgoArl.I, monto("20000"), acepta);
    }

    @Test
    @DisplayName("Aplica a empleado por horas con más de 1 año que aceptó el fondo")
    void aplicaCuandoCumpleTodo() {
        assertTrue(fondo.aplicaA(porHoras(LocalDate.of(2024, 2, 1), true), CORTE));
    }

    @Test
    @DisplayName("No aplica si el empleado no aceptó el fondo")
    void noAplicaSiNoAcepta() {
        assertFalse(fondo.aplicaA(porHoras(LocalDate.of(2024, 2, 1), false), CORTE));
    }

    @Test
    @DisplayName("No aplica con 1 año o menos de antigüedad")
    void noAplicaSinAntiguedad() {
        assertFalse(fondo.aplicaA(porHoras(CORTE.minusYears(1), true), CORTE));
    }

    @Test
    @DisplayName("No aplica a empleados que no son por horas")
    void noAplicaAOtrosTipos() {
        EmpleadoAsalariado asalariado = new EmpleadoAsalariado("1", "Laura", LocalDate.of(2015, 1, 1),
                NivelRiesgoArl.I, monto("3000000"));

        assertFalse(fondo.aplicaA(asalariado, CORTE));
    }

    @Test
    @DisplayName("Deposita el 2 % del salario")
    void calculaDosPorCiento() {
        assertMonto(19_000, fondo.calcular(porHoras(LocalDate.of(2024, 2, 1), true), monto("950000")));
    }
}
