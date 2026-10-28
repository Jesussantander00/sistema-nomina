package com.empresa.nomina.servicio;

import com.empresa.nomina.excepciones.ContratoNoVigenteException;
import com.empresa.nomina.excepciones.ValidacionNominaException;
import com.empresa.nomina.modelo.Empleado;
import com.empresa.nomina.modelo.EmpleadoAsalariado;
import com.empresa.nomina.modelo.EmpleadoPorComision;
import com.empresa.nomina.modelo.EmpleadoPorHoras;
import com.empresa.nomina.modelo.EmpleadoTemporal;
import com.empresa.nomina.modelo.NivelRiesgoArl;
import com.empresa.nomina.reglas.Deduccion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static com.empresa.nomina.util.Montos.assertMonto;
import static com.empresa.nomina.util.Montos.monto;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Pruebas de integración: cada tipo de empleado liquidado con las reglas reales de la empresa. */
class CalculadoraNominaTest {

    private static final LocalDate CORTE = LocalDate.of(2026, 10, 31);
    private static final String SEGURIDAD_SOCIAL = "Seguro social y pensión (4 %)";
    private static final String ARL = "ARL";
    private static final String FONDO_AHORRO = "Aporte fondo de ahorro (2 %)";
    private static final String BONO_ALIMENTACION = "Bono de alimentación";

    private CalculadoraNomina calculadora;

    @BeforeEach
    void crearCalculadora() {
        calculadora = ReglasNominaPorDefecto.crearCalculadora();
    }

    @Test
    @DisplayName("Asalariado con más de 5 años: bono 10 %, deducciones y bono de alimentación")
    void liquidaAsalariado() {
        EmpleadoAsalariado empleado = new EmpleadoAsalariado("1001", "Laura Gómez",
                LocalDate.of(2018, 3, 1), NivelRiesgoArl.I, monto("3000000"));

        ReciboNomina recibo = calculadora.liquidar(empleado, CORTE);

        assertAll(
                () -> assertMonto(3_300_000, recibo.salarioBruto()),
                () -> assertMonto(132_000, recibo.valorDe(SEGURIDAD_SOCIAL)),
                () -> assertMonto(17_226, recibo.valorDe(ARL)),
                () -> assertMonto(1_000_000, recibo.valorDe(BONO_ALIMENTACION)),
                () -> assertMonto(4_150_774, recibo.salarioNeto()));
    }

    @Test
    @DisplayName("Por horas con extras y fondo de ahorro: sin bonos ni bono de alimentación")
    void liquidaPorHoras() {
        EmpleadoPorHoras empleado = new EmpleadoPorHoras("1002", "Carlos Ruiz",
                LocalDate.of(2024, 2, 1), NivelRiesgoArl.II, monto("20000"), true);
        empleado.registrarHoras(monto("45"));

        ReciboNomina recibo = calculadora.liquidar(empleado, CORTE);

        assertAll(
                () -> assertMonto(950_000, recibo.salarioBruto()),
                () -> assertMonto(0, recibo.bonos()),
                () -> assertMonto(38_000, recibo.valorDe(SEGURIDAD_SOCIAL)),
                () -> assertMonto(9_918, recibo.valorDe(ARL)),
                () -> assertMonto(19_000, recibo.valorDe(FONDO_AHORRO)),
                () -> assertTrue(recibo.beneficios().isEmpty()),
                () -> assertMonto(883_082, recibo.salarioNeto()));
    }

    @Test
    @DisplayName("Por comisión con ventas mayores a $20 millones: bono 3 % y bono de alimentación")
    void liquidaPorComision() {
        EmpleadoPorComision empleado = new EmpleadoPorComision("1003", "Ana Torres",
                LocalDate.of(2022, 6, 15), NivelRiesgoArl.I, monto("1500000"), monto("0.05"));
        empleado.registrarVentas(monto("25000000"));

        ReciboNomina recibo = calculadora.liquidar(empleado, CORTE);

        assertAll(
                () -> assertMonto(2_750_000, recibo.salarioBase()),
                () -> assertMonto(750_000, recibo.bonos()),
                () -> assertMonto(3_500_000, recibo.salarioBruto()),
                () -> assertMonto(1_000_000, recibo.valorDe(BONO_ALIMENTACION)),
                () -> assertMonto(4_341_730, recibo.salarioNeto()));
    }

    @Test
    @DisplayName("Temporal: solo salario fijo menos deducciones obligatorias")
    void liquidaTemporal() {
        EmpleadoTemporal empleado = new EmpleadoTemporal("1004", "Pedro Díaz",
                LocalDate.of(2026, 1, 15), NivelRiesgoArl.I, monto("2000000"), LocalDate.of(2026, 12, 31));

        ReciboNomina recibo = calculadora.liquidar(empleado, CORTE);

        assertAll(
                () -> assertEquals(2, recibo.deducciones().size()),
                () -> assertTrue(recibo.beneficios().isEmpty()),
                () -> assertMonto(1_909_560, recibo.salarioNeto()));
    }

    @Test
    @DisplayName("No liquida a un temporal con el contrato vencido")
    void rechazaContratoVencido() {
        EmpleadoTemporal vencido = new EmpleadoTemporal("1005", "Sofía", LocalDate.of(2025, 1, 1),
                NivelRiesgoArl.I, monto("1800000"), LocalDate.of(2025, 12, 31));

        assertThrows(ContratoNoVigenteException.class, () -> calculadora.liquidar(vencido, CORTE));
    }

    @Test
    @DisplayName("Ningún empleado puede quedar con salario neto negativo")
    void rechazaNetoNegativo() {
        CalculadoraNomina conDescuentoExcesivo = new CalculadoraNomina(
                List.of(deduccionFija("Embargo", "5000000")), List.of());
        EmpleadoAsalariado empleado = new EmpleadoAsalariado("1006", "Mario", LocalDate.of(2025, 1, 1),
                NivelRiesgoArl.I, monto("1000000"));

        assertThrows(ValidacionNominaException.class, () -> conDescuentoExcesivo.liquidar(empleado, CORTE));
    }

    @Test
    @DisplayName("Una deducción nueva se agrega sin modificar la calculadora (abierto/cerrado)")
    void permiteAgregarDeduccionesNuevas() {
        CalculadoraNomina conCooperativa = new CalculadoraNomina(
                List.of(deduccionFija("Cooperativa", "50000")), List.of());
        EmpleadoAsalariado empleado = new EmpleadoAsalariado("1007", "Lucía", LocalDate.of(2025, 1, 1),
                NivelRiesgoArl.I, monto("1000000"));

        ReciboNomina recibo = conCooperativa.liquidar(empleado, CORTE);

        assertMonto(950_000, recibo.salarioNeto());
    }

    @Test
    @DisplayName("Liquida a todos los empleados de una lista")
    void liquidaVariosEmpleados() {
        List<Empleado> empleados = List.of(
                new EmpleadoAsalariado("1", "A", LocalDate.of(2025, 1, 1), NivelRiesgoArl.I, monto("1000000")),
                new EmpleadoAsalariado("2", "B", LocalDate.of(2025, 1, 1), NivelRiesgoArl.I, monto("2000000")));

        assertEquals(2, calculadora.liquidarTodos(empleados, CORTE).size());
    }

    private static Deduccion deduccionFija(String nombre, String valor) {
        return new Deduccion() {
            @Override
            public String getNombre() {
                return nombre;
            }

            @Override
            public BigDecimal calcular(Empleado empleado, BigDecimal salarioBruto) {
                return new BigDecimal(valor);
            }
        };
    }
}
