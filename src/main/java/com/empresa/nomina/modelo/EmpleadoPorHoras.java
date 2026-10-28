package com.empresa.nomina.modelo;

import com.empresa.nomina.util.Dinero;
import com.empresa.nomina.util.Validaciones;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Empleado que cobra por horas trabajadas.
 *
 * <ul>
 *   <li>Las primeras 40 horas se pagan a la tarifa normal.</li>
 *   <li>Las horas por encima de 40 son extras y se pagan a 1,5 veces la tarifa.</li>
 *   <li>No recibe bonos (hereda {@code calcularBonos} = 0).</li>
 * </ul>
 */
public class EmpleadoPorHoras extends Empleado implements ParticipanteFondoAhorro {

    static final BigDecimal HORAS_JORNADA_NORMAL = new BigDecimal("40");
    static final BigDecimal FACTOR_HORA_EXTRA = new BigDecimal("1.5");

    private final BigDecimal tarifaPorHora;
    private final boolean aceptaFondoAhorro;
    private BigDecimal horasTrabajadas = BigDecimal.ZERO;

    public EmpleadoPorHoras(String documento, String nombre, LocalDate fechaIngreso,
                            NivelRiesgoArl nivelRiesgoArl, BigDecimal tarifaPorHora,
                            boolean aceptaFondoAhorro) {
        super(documento, nombre, fechaIngreso, nivelRiesgoArl);
        this.tarifaPorHora = Validaciones.noNegativo(tarifaPorHora, "tarifa por hora");
        this.aceptaFondoAhorro = aceptaFondoAhorro;
    }

    /**
     * Registra las horas trabajadas en el periodo a liquidar.
     *
     * @throws com.empresa.nomina.excepciones.ValidacionNominaException si las horas son negativas
     */
    public void registrarHoras(BigDecimal horas) {
        this.horasTrabajadas = Validaciones.noNegativo(horas, "horas trabajadas");
    }

    @Override
    public BigDecimal calcularSalarioBase() {
        return Dinero.redondear(calcularPagoHorasNormales().add(calcularPagoHorasExtra()));
    }

    private BigDecimal calcularPagoHorasNormales() {
        return horasNormales().multiply(tarifaPorHora);
    }

    private BigDecimal calcularPagoHorasExtra() {
        return horasExtra().multiply(tarifaPorHora).multiply(FACTOR_HORA_EXTRA);
    }

    private BigDecimal horasNormales() {
        return horasTrabajadas.min(HORAS_JORNADA_NORMAL);
    }

    private BigDecimal horasExtra() {
        return horasTrabajadas.subtract(HORAS_JORNADA_NORMAL).max(BigDecimal.ZERO);
    }

    @Override
    public boolean aceptaFondoAhorro() {
        return aceptaFondoAhorro;
    }

    @Override
    public String getTipo() {
        return "Por horas";
    }

    public BigDecimal getTarifaPorHora() {
        return tarifaPorHora;
    }

    public BigDecimal getHorasTrabajadas() {
        return horasTrabajadas;
    }
}
