package com.empresa.nomina.servicio;

import com.empresa.nomina.reglas.AporteFondoAhorro;
import com.empresa.nomina.reglas.BonoAlimentacion;
import com.empresa.nomina.reglas.DeduccionArl;
import com.empresa.nomina.reglas.DeduccionSeguridadSocial;

import java.util.List;

/**
 * Arma la calculadora con las reglas de negocio vigentes de la empresa.
 * Es el único lugar que conoce las reglas concretas (punto de composición).
 */
public final class ReglasNominaPorDefecto {

    private ReglasNominaPorDefecto() {
        // Clase de fábrica: no se instancia.
    }

    public static CalculadoraNomina crearCalculadora() {
        return new CalculadoraNomina(
                List.of(new DeduccionSeguridadSocial(), new DeduccionArl(), new AporteFondoAhorro()),
                List.of(new BonoAlimentacion()));
    }
}
