package com.empresa.nomina.modelo;

import com.empresa.nomina.util.Dinero;
import com.empresa.nomina.util.Validaciones;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Empleado que cobra por horas trabajadas.
 * Las horas por encima de 40 se pagan a 1,5 veces la tarifa. No recibe bonos.
 */
public class EmpleadoPorHoras extends Empleado implements ParticipanteFondoAhorro {

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

    /** Registra las horas trabajadas en el periodo a liquidar. */
    public void registrarHoras(BigDecimal horas) {
        this.horasTrabajadas = Validaciones.noNegativo(horas, "horas trabajadas");
    }

    @Override
    public BigDecimal calcularSalarioBase() {
        BigDecimal pago;
        if (horasTrabajadas.compareTo(new BigDecimal("40")) <= 0) {
            pago = horasTrabajadas.multiply(tarifaPorHora);
        } else {
            BigDecimal extras = horasTrabajadas.subtract(new BigDecimal("40"));
            pago = new BigDecimal("40").multiply(tarifaPorHora)
                    .add(extras.multiply(tarifaPorHora).multiply(new BigDecimal("1.5")));
        }
        return Dinero.redondear(pago);
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
