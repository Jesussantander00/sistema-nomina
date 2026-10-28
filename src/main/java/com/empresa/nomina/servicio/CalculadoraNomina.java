package com.empresa.nomina.servicio;

import com.empresa.nomina.excepciones.ContratoNoVigenteException;
import com.empresa.nomina.excepciones.ValidacionNominaException;
import com.empresa.nomina.modelo.Empleado;
import com.empresa.nomina.reglas.Beneficio;
import com.empresa.nomina.reglas.Deduccion;
import com.empresa.nomina.util.Validaciones;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Liquida la nómina de los empleados.
 *
 * <p>La calculadora no conoce las deducciones ni los beneficios concretos:
 * los recibe por el constructor como abstracciones ({@link Deduccion},
 * {@link Beneficio}). Así se cumple la <b>inversión de dependencias</b> y se
 * pueden probar o cambiar las reglas sin modificar esta clase.</p>
 */
public class CalculadoraNomina {

    private final List<Deduccion> deducciones;
    private final List<Beneficio> beneficios;

    public CalculadoraNomina(List<Deduccion> deducciones, List<Beneficio> beneficios) {
        this.deducciones = List.copyOf(Validaciones.requerido(deducciones, "deducciones"));
        this.beneficios = List.copyOf(Validaciones.requerido(beneficios, "beneficios"));
    }

    /**
     * Calcula el recibo de nómina de un empleado a una fecha de corte.
     *
     * @throws ContratoNoVigenteException si el contrato no está vigente en esa fecha
     * @throws ValidacionNominaException  si el salario neto resultante es negativo
     */
    public ReciboNomina liquidar(Empleado empleado, LocalDate fechaCorte) {
        Validaciones.requerido(empleado, "empleado");
        Validaciones.requerido(fechaCorte, "fecha de corte");
        verificarVigencia(empleado, fechaCorte);

        BigDecimal salarioBase = empleado.calcularSalarioBase();
        BigDecimal bonos = empleado.calcularBonos(fechaCorte);
        BigDecimal salarioBruto = salarioBase.add(bonos);

        List<ConceptoNomina> conceptosDeduccion = calcularDeducciones(empleado, fechaCorte, salarioBruto);
        List<ConceptoNomina> conceptosBeneficio = calcularBeneficios(empleado, fechaCorte, salarioBruto);

        BigDecimal salarioNeto = salarioBruto
                .subtract(sumar(conceptosDeduccion))
                .add(sumar(conceptosBeneficio));
        verificarNetoNoNegativo(empleado, salarioNeto);

        return new ReciboNomina(empleado, fechaCorte, salarioBase, bonos, salarioBruto,
                conceptosDeduccion, conceptosBeneficio, salarioNeto);
    }

    /** Liquida la nómina de varios empleados con la misma fecha de corte. */
    public List<ReciboNomina> liquidarTodos(List<? extends Empleado> empleados, LocalDate fechaCorte) {
        return empleados.stream()
                .map(empleado -> liquidar(empleado, fechaCorte))
                .toList();
    }

    private List<ConceptoNomina> calcularDeducciones(Empleado empleado, LocalDate fechaCorte,
                                                     BigDecimal salarioBruto) {
        return deducciones.stream()
                .filter(deduccion -> deduccion.aplicaA(empleado, fechaCorte))
                .map(deduccion -> new ConceptoNomina(
                        deduccion.getNombre(), deduccion.calcular(empleado, salarioBruto)))
                .toList();
    }

    private List<ConceptoNomina> calcularBeneficios(Empleado empleado, LocalDate fechaCorte,
                                                    BigDecimal salarioBruto) {
        return beneficios.stream()
                .filter(beneficio -> beneficio.aplicaA(empleado, fechaCorte))
                .map(beneficio -> new ConceptoNomina(
                        beneficio.getNombre(), beneficio.calcular(empleado, salarioBruto)))
                .toList();
    }

    private static void verificarVigencia(Empleado empleado, LocalDate fechaCorte) {
        if (!empleado.estaVigenteEn(fechaCorte)) {
            throw new ContratoNoVigenteException(
                    "El contrato de " + empleado.getNombre() + " no está vigente el " + fechaCorte + ".");
        }
    }

    private static void verificarNetoNoNegativo(Empleado empleado, BigDecimal salarioNeto) {
        if (salarioNeto.signum() < 0) {
            throw new ValidacionNominaException(
                    "El salario neto de " + empleado.getNombre() + " no puede ser negativo: " + salarioNeto);
        }
    }

    private static BigDecimal sumar(List<ConceptoNomina> conceptos) {
        return conceptos.stream()
                .map(ConceptoNomina::valor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
