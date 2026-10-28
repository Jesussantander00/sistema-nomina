package com.empresa.nomina.modelo;

import java.math.BigDecimal;

/**
 * Clases de riesgo laboral para la ARL, con su tarifa sobre el salario
 * (Decreto 1295 de 1994 y Decreto 1772 de 1994, Colombia).
 */
public enum NivelRiesgoArl {

    I("Riesgo mínimo (oficina)", "0.00522"),
    II("Riesgo bajo", "0.01044"),
    III("Riesgo medio", "0.02436"),
    IV("Riesgo alto", "0.04350"),
    V("Riesgo máximo", "0.06960");

    private final String descripcion;
    private final BigDecimal tarifa;

    NivelRiesgoArl(String descripcion, String tarifa) {
        this.descripcion = descripcion;
        this.tarifa = new BigDecimal(tarifa);
    }

    public String getDescripcion() {
        return descripcion;
    }

    /** Tarifa expresada como fracción (0.00522 = 0,522 %). */
    public BigDecimal getTarifa() {
        return tarifa;
    }
}
