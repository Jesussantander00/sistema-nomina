package com.empresa.nomina.modelo;

import com.empresa.nomina.excepciones.ValidacionNominaException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static com.empresa.nomina.util.Montos.assertMonto;
import static com.empresa.nomina.util.Montos.monto;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EmpleadoPorComisionTest {

    private static final LocalDate CORTE = LocalDate.of(2026, 10, 31);

    private EmpleadoPorComision empleado;

    @BeforeEach
    void crearEmpleado() {
        empleado = new EmpleadoPorComision("3", "Ana", LocalDate.of(2022, 6, 15),
                NivelRiesgoArl.I, monto("1500000"), monto("0.05"));
    }

    @Test
    @DisplayName("Salario = base + 5 % de las ventas")
    void salarioBaseMasComision() {
        empleado.registrarVentas(monto("10000000"));

        assertMonto(500_000, empleado.calcularComision());
        assertMonto(2_000_000, empleado.calcularSalarioBase());
    }

    @Test
    @DisplayName("Con ventas de exactamente $20.000.000 no hay bono")
    void sinBonoEnElUmbral() {
        empleado.registrarVentas(monto("20000000"));

        assertMonto(0, empleado.calcularBonos(CORTE));
    }

    @Test
    @DisplayName("Con ventas mayores a $20.000.000 recibe bono del 3 % de las ventas")
    void bonoPorVentasAltas() {
        empleado.registrarVentas(monto("25000000"));

        assertMonto(750_000, empleado.calcularBonos(CORTE));
        // 1.500.000 + 1.250.000 de comisión + 750.000 de bono
        assertMonto(3_500_000, empleado.calcularSalarioBruto(CORTE));
    }

    @Test
    @DisplayName("Sin ventas solo recibe el salario base")
    void sinVentas() {
        assertMonto(1_500_000, empleado.calcularSalarioBruto(CORTE));
    }

    @Test
    @DisplayName("Las ventas no pueden ser menores a $0")
    void rechazaVentasNegativas() {
        assertThrows(ValidacionNominaException.class, () -> empleado.registrarVentas(monto("-1")));
    }

    @Test
    @DisplayName("El porcentaje de comisión debe estar entre 0 y 1")
    void rechazaPorcentajeInvalido() {
        assertThrows(ValidacionNominaException.class, () -> new EmpleadoPorComision("3", "Ana",
                LocalDate.of(2022, 6, 15), NivelRiesgoArl.I, monto("1500000"), monto("5")));
    }

    @Test
    @DisplayName("Es un empleado permanente")
    void esPermanente() {
        assertTrue(empleado instanceof EmpleadoPermanente);
    }
}
