package com.empresa.nomina.modelo;

import com.empresa.nomina.excepciones.ValidacionNominaException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static com.empresa.nomina.util.Montos.assertMonto;
import static com.empresa.nomina.util.Montos.monto;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EmpleadoAsalariadoTest {

    private static final LocalDate CORTE = LocalDate.of(2026, 10, 31);

    private EmpleadoAsalariado asalariadoDesde(LocalDate ingreso) {
        return new EmpleadoAsalariado("1", "Laura", ingreso, NivelRiesgoArl.I, monto("3000000"));
    }

    @Test
    @DisplayName("El salario base es el salario fijo mensual")
    void salarioBaseEsElSalarioFijo() {
        assertMonto(3_000_000, asalariadoDesde(LocalDate.of(2024, 1, 1)).calcularSalarioBase());
    }

    @Test
    @DisplayName("Con más de 5 años recibe bono del 10 % del salario")
    void bonoConMasDeCincoAnios() {
        EmpleadoAsalariado empleado = asalariadoDesde(LocalDate.of(2018, 3, 1));

        assertMonto(300_000, empleado.calcularBonos(CORTE));
        assertMonto(3_300_000, empleado.calcularSalarioBruto(CORTE));
    }

    @Test
    @DisplayName("Con exactamente 5 años todavía no recibe bono")
    void sinBonoConExactamenteCincoAnios() {
        EmpleadoAsalariado empleado = asalariadoDesde(CORTE.minusYears(5));

        assertMonto(0, empleado.calcularBonos(CORTE));
    }

    @Test
    @DisplayName("Con 5 años y un día ya recibe bono")
    void bonoConCincoAniosYUnDia() {
        EmpleadoAsalariado empleado = asalariadoDesde(CORTE.minusYears(5).minusDays(1));

        assertMonto(300_000, empleado.calcularBonos(CORTE));
    }

    @Test
    @DisplayName("Es un empleado permanente")
    void esPermanente() {
        assertTrue(asalariadoDesde(LocalDate.of(2024, 1, 1)) instanceof EmpleadoPermanente);
    }

    @Test
    @DisplayName("No acepta salario negativo ni nombre vacío")
    void validaDatosDeEntrada() {
        LocalDate ingreso = LocalDate.of(2024, 1, 1);
        assertThrows(ValidacionNominaException.class, () -> new EmpleadoAsalariado(
                "1", "Laura", ingreso, NivelRiesgoArl.I, new BigDecimal("-1")));
        assertThrows(ValidacionNominaException.class, () -> new EmpleadoAsalariado(
                "1", " ", ingreso, NivelRiesgoArl.I, monto("3000000")));
    }
}
