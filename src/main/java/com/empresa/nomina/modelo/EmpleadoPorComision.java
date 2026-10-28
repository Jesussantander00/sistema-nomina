package com.empresa.nomina.modelo;

import com.empresa.nomina.util.Dinero;
import com.empresa.nomina.util.Validaciones;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Empleado con salario base más un porcentaje de comisión sobre sus ventas.
 * Si las ventas superan $20.000.000 recibe un bono adicional del 3 % de las ventas.
 */
public class EmpleadoPorComision extends Empleado implements EmpleadoPermanente {

    static final BigDecimal UMBRAL_VENTAS_BONO = new BigDecimal("20000000");
    static final BigDecimal PORCENTAJE_BONO_VENTAS = new BigDecimal("0.03");

    private final BigDecimal salarioBase;
    private final BigDecimal porcentajeComision;
    private BigDecimal ventas = BigDecimal.ZERO;

    /**
     * @param porcentajeComision fracción entre 0 y 1 (0.05 = 5 % de comisión)
     */
    public EmpleadoPorComision(String documento, String nombre, LocalDate fechaIngreso,
                               NivelRiesgoArl nivelRiesgoArl, BigDecimal salarioBase,
                               BigDecimal porcentajeComision) {
        super(documento, nombre, fechaIngreso, nivelRiesgoArl);
        this.salarioBase = Validaciones.noNegativo(salarioBase, "salario base");
        this.porcentajeComision = Validaciones.porcentaje(porcentajeComision, "porcentaje de comisión");
    }

    /**
     * Registra el total vendido en el periodo a liquidar.
     *
     * @throws com.empresa.nomina.excepciones.ValidacionNominaException si las ventas son menores a $0
     */
    public void registrarVentas(BigDecimal ventas) {
        this.ventas = Validaciones.noNegativo(ventas, "ventas");
    }

    @Override
    public BigDecimal calcularSalarioBase() {
        return Dinero.redondear(salarioBase).add(calcularComision());
    }

    /** Comisión = ventas × porcentaje de comisión. */
    public BigDecimal calcularComision() {
        return Dinero.porcentajeDe(ventas, porcentajeComision);
    }

    @Override
    public BigDecimal calcularBonos(LocalDate fechaCorte) {
        if (ventas.compareTo(UMBRAL_VENTAS_BONO) > 0) {
            return Dinero.porcentajeDe(ventas, PORCENTAJE_BONO_VENTAS);
        }
        return Dinero.CERO;
    }

    @Override
    public String getTipo() {
        return "Por comisión";
    }

    public BigDecimal getVentas() {
        return ventas;
    }

    public BigDecimal getPorcentajeComision() {
        return porcentajeComision;
    }
}
