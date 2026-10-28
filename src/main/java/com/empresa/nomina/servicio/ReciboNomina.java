package com.empresa.nomina.servicio;

import com.empresa.nomina.modelo.Empleado;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

/**
 * Resultado inmutable de liquidar la nómina de un empleado.
 *
 * <p>Salario neto = salario bruto − deducciones + beneficios.</p>
 */
public record ReciboNomina(
        Empleado empleado,
        LocalDate fechaCorte,
        BigDecimal salarioBase,
        BigDecimal bonos,
        BigDecimal salarioBruto,
        List<ConceptoNomina> deducciones,
        List<ConceptoNomina> beneficios,
        BigDecimal salarioNeto) {

    public ReciboNomina {
        deducciones = List.copyOf(deducciones);
        beneficios = List.copyOf(beneficios);
    }

    public BigDecimal totalDeducciones() {
        return sumar(deducciones);
    }

    public BigDecimal totalBeneficios() {
        return sumar(beneficios);
    }

    /** Busca el valor de un concepto por nombre (útil en reportes y pruebas). */
    public BigDecimal valorDe(String nombreConcepto) {
        return Stream.concat(deducciones.stream(), beneficios.stream())
                .filter(concepto -> concepto.nombre().equals(nombreConcepto))
                .map(ConceptoNomina::valor)
                .findFirst()
                .orElse(BigDecimal.ZERO);
    }

    private static BigDecimal sumar(List<ConceptoNomina> conceptos) {
        return conceptos.stream()
                .map(ConceptoNomina::valor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
