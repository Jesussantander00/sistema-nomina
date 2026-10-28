package com.empresa.nomina;

import com.empresa.nomina.excepciones.ContratoNoVigenteException;
import com.empresa.nomina.excepciones.ValidacionNominaException;
import com.empresa.nomina.modelo.Empleado;
import com.empresa.nomina.modelo.EmpleadoAsalariado;
import com.empresa.nomina.modelo.EmpleadoPorComision;
import com.empresa.nomina.modelo.EmpleadoPorHoras;
import com.empresa.nomina.modelo.EmpleadoTemporal;
import com.empresa.nomina.modelo.NivelRiesgoArl;
import com.empresa.nomina.presentacion.FormateadorRecibo;
import com.empresa.nomina.servicio.CalculadoraNomina;
import com.empresa.nomina.servicio.ReciboNomina;
import com.empresa.nomina.servicio.ReglasNominaPorDefecto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Programa de demostración: liquida la nómina de un empleado de cada tipo
 * e imprime sus recibos. También muestra cómo responde el sistema a datos inválidos.
 */
public class App {

    private static final LocalDate FECHA_CORTE = LocalDate.of(2026, 10, 31);

    public static void main(String[] args) {
        CalculadoraNomina calculadora = ReglasNominaPorDefecto.crearCalculadora();
        FormateadorRecibo formateador = new FormateadorRecibo();

        List<ReciboNomina> recibos = calculadora.liquidarTodos(crearEmpleadosDeEjemplo(), FECHA_CORTE);
        recibos.forEach(recibo -> System.out.println(formateador.formatear(recibo)));

        mostrarValidaciones(calculadora);
    }

    private static List<Empleado> crearEmpleadosDeEjemplo() {
        EmpleadoAsalariado asalariada = new EmpleadoAsalariado("1001", "Laura Gómez",
                LocalDate.of(2018, 3, 1), NivelRiesgoArl.I, new BigDecimal("3000000"));

        EmpleadoPorHoras porHoras = new EmpleadoPorHoras("1002", "Carlos Ruiz",
                LocalDate.of(2024, 2, 1), NivelRiesgoArl.II, new BigDecimal("20000"), true);
        porHoras.registrarHoras(new BigDecimal("45"));

        EmpleadoPorComision vendedora = new EmpleadoPorComision("1003", "Ana Torres",
                LocalDate.of(2022, 6, 15), NivelRiesgoArl.I, new BigDecimal("1500000"), new BigDecimal("0.05"));
        vendedora.registrarVentas(new BigDecimal("25000000"));

        EmpleadoTemporal temporal = new EmpleadoTemporal("1004", "Pedro Díaz",
                LocalDate.of(2026, 1, 15), NivelRiesgoArl.I, new BigDecimal("2000000"),
                LocalDate.of(2026, 12, 31));

        return List.of(asalariada, porHoras, vendedora, temporal);
    }

    private static void mostrarValidaciones(CalculadoraNomina calculadora) {
        System.out.println("Validaciones:");
        try {
            new EmpleadoPorHoras("2001", "Prueba", LocalDate.of(2025, 1, 1), NivelRiesgoArl.I,
                    new BigDecimal("15000"), false).registrarHoras(new BigDecimal("-5"));
        } catch (ValidacionNominaException e) {
            System.out.println("  Horas negativas rechazadas -> " + e.getMessage());
        }
        try {
            new EmpleadoPorComision("2002", "Prueba", LocalDate.of(2025, 1, 1), NivelRiesgoArl.I,
                    new BigDecimal("1000000"), new BigDecimal("0.05")).registrarVentas(new BigDecimal("-1"));
        } catch (ValidacionNominaException e) {
            System.out.println("  Ventas negativas rechazadas -> " + e.getMessage());
        }
        try {
            EmpleadoTemporal vencido = new EmpleadoTemporal("2003", "Prueba", LocalDate.of(2025, 1, 1),
                    NivelRiesgoArl.I, new BigDecimal("1800000"), LocalDate.of(2025, 12, 31));
            calculadora.liquidar(vencido, FECHA_CORTE);
        } catch (ContratoNoVigenteException e) {
            System.out.println("  Contrato vencido rechazado -> " + e.getMessage());
        }
    }
}
