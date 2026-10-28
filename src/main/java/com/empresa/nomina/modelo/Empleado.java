package com.empresa.nomina.modelo;

import com.empresa.nomina.util.Dinero;
import com.empresa.nomina.util.Validaciones;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Representa a cualquier empleado de la empresa.
 *
 * <p>Cada tipo de empleado define cómo se calcula su salario base y sus bonos;
 * esta clase define el orden del cálculo del salario bruto (patrón
 * <em>Template Method</em>) y los datos comunes a todos.</p>
 *
 * <p>Principios aplicados: <b>abierto/cerrado</b> (un tipo nuevo de empleado es
 * una subclase nueva, sin tocar las existentes) y <b>sustitución de Liskov</b>
 * (toda subclase devuelve montos no negativos y puede usarse donde se espera
 * un {@code Empleado}).</p>
 */
public abstract class Empleado {

    private final String documento;
    private final String nombre;
    private final LocalDate fechaIngreso;
    private final NivelRiesgoArl nivelRiesgoArl;

    protected Empleado(String documento, String nombre, LocalDate fechaIngreso, NivelRiesgoArl nivelRiesgoArl) {
        this.documento = Validaciones.textoRequerido(documento, "documento");
        this.nombre = Validaciones.textoRequerido(nombre, "nombre");
        this.fechaIngreso = Validaciones.requerido(fechaIngreso, "fecha de ingreso");
        this.nivelRiesgoArl = Validaciones.requerido(nivelRiesgoArl, "nivel de riesgo ARL");
    }

    /** Pago por el trabajo del periodo (salario, horas o comisiones), sin bonos. */
    public abstract BigDecimal calcularSalarioBase();

    /** Nombre legible del tipo de empleado, para reportes. */
    public abstract String getTipo();

    /**
     * Bonos salariales del periodo. Por defecto el empleado no recibe bonos;
     * los tipos que sí los tienen sobrescriben este método.
     *
     * @param fechaCorte fecha a la que se liquida la nómina (para calcular antigüedad)
     */
    public BigDecimal calcularBonos(LocalDate fechaCorte) {
        return Dinero.CERO;
    }

    /** Salario bruto = salario base + bonos. Es la base de las deducciones. */
    public final BigDecimal calcularSalarioBruto(LocalDate fechaCorte) {
        return calcularSalarioBase().add(calcularBonos(fechaCorte));
    }

    /**
     * Indica si el empleado puede recibir pago en la fecha de corte.
     * Los empleados con contrato indefinido siempre están vigentes desde su ingreso.
     */
    public boolean estaVigenteEn(LocalDate fecha) {
        return !fecha.isBefore(fechaIngreso);
    }

    /**
     * Indica si el empleado lleva <b>más</b> de {@code anios} años en la empresa
     * a la fecha de corte (exactamente {@code anios} años no cuenta).
     */
    public boolean tieneAntiguedadMayorA(int anios, LocalDate fechaCorte) {
        return fechaIngreso.plusYears(anios).isBefore(fechaCorte);
    }

    public String getDocumento() {
        return documento;
    }

    public String getNombre() {
        return nombre;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public NivelRiesgoArl getNivelRiesgoArl() {
        return nivelRiesgoArl;
    }

    @Override
    public String toString() {
        return getTipo() + " " + nombre + " (" + documento + ")";
    }
}
