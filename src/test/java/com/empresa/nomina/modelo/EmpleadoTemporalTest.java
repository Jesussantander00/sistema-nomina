package com.empresa.nomina.modelo;

import com.empresa.nomina.excepciones.ValidacionNominaException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static com.empresa.nomina.util.Montos.assertMonto;
import static com.empresa.nomina.util.Montos.monto;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EmpleadoTemporalTest {

    private static final LocalDate INICIO = LocalDate.of(2026, 1, 15);
    private static final LocalDate FIN = LocalDate.of(2026, 12, 31);

    private final EmpleadoTemporal empleado = new EmpleadoTemporal("4", "Pedro", INICIO,
            NivelRiesgoArl.I, monto("2000000"), FIN);

    @Test
    @DisplayName("Recibe su salario fijo y ningún bono")
    void salarioFijoSinBonos() {
        assertMonto(2_000_000, empleado.calcularSalarioBase());
        assertMonto(0, empleado.calcularBonos(FIN));
    }

    @Test
    @DisplayName("El contrato está vigente desde el ingreso hasta la fecha fin, inclusive")
    void vigenciaDelContrato() {
        assertTrue(empleado.estaVigenteEn(INICIO));
        assertTrue(empleado.estaVigenteEn(FIN));
        assertFalse(empleado.estaVigenteEn(FIN.plusDays(1)));
        assertFalse(empleado.estaVigenteEn(INICIO.minusDays(1)));
    }

    @Test
    @DisplayName("La fecha fin del contrato debe ser posterior al ingreso")
    void rechazaFechaFinAnterior() {
        assertThrows(ValidacionNominaException.class, () -> new EmpleadoTemporal("4", "Pedro",
                INICIO, NivelRiesgoArl.I, monto("2000000"), INICIO.minusDays(1)));
    }

    @Test
    @DisplayName("No es un empleado permanente")
    void noEsPermanente() {
        assertFalse(empleado instanceof EmpleadoPermanente);
    }
}
