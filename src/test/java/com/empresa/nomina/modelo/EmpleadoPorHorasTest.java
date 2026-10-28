package com.empresa.nomina.modelo;

import com.empresa.nomina.excepciones.ValidacionNominaException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static com.empresa.nomina.util.Montos.assertMonto;
import static com.empresa.nomina.util.Montos.monto;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EmpleadoPorHorasTest {

    private static final LocalDate CORTE = LocalDate.of(2026, 10, 31);

    private EmpleadoPorHoras empleado;

    @BeforeEach
    void crearEmpleado() {
        empleado = new EmpleadoPorHoras("2", "Carlos", LocalDate.of(2024, 2, 1),
                NivelRiesgoArl.I, monto("20000"), true);
    }

    @Test
    @DisplayName("Hasta 40 horas se pagan a la tarifa normal")
    void horasNormales() {
        empleado.registrarHoras(monto("40"));

        assertMonto(800_000, empleado.calcularSalarioBase());
    }

    @Test
    @DisplayName("Las horas por encima de 40 se pagan a 1,5 veces la tarifa")
    void horasExtras() {
        empleado.registrarHoras(monto("45"));

        // 40 × 20.000 + 5 × 30.000
        assertMonto(950_000, empleado.calcularSalarioBase());
    }

    @Test
    @DisplayName("Acepta fracciones de hora extra")
    void fraccionDeHoraExtra() {
        empleado.registrarHoras(monto("40.5"));

        assertMonto(815_000, empleado.calcularSalarioBase());
    }

    @Test
    @DisplayName("Sin horas registradas el pago es cero")
    void sinHoras() {
        assertMonto(0, empleado.calcularSalarioBase());
    }

    @Test
    @DisplayName("No recibe bonos aunque trabaje muchas horas")
    void noRecibeBonos() {
        empleado.registrarHoras(monto("60"));

        assertMonto(0, empleado.calcularBonos(CORTE));
    }

    @Test
    @DisplayName("Las horas trabajadas no pueden ser negativas")
    void rechazaHorasNegativas() {
        assertThrows(ValidacionNominaException.class, () -> empleado.registrarHoras(monto("-1")));
    }

    @Test
    @DisplayName("La tarifa por hora no puede ser negativa")
    void rechazaTarifaNegativa() {
        assertThrows(ValidacionNominaException.class, () -> new EmpleadoPorHoras("2", "Carlos",
                LocalDate.of(2024, 2, 1), NivelRiesgoArl.I, monto("-100"), false));
    }

    @Test
    @DisplayName("No es un empleado permanente")
    void noEsPermanente() {
        assertFalse(empleado instanceof EmpleadoPermanente);
    }
}
