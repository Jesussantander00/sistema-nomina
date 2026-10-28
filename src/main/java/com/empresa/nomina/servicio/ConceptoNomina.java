package com.empresa.nomina.servicio;

import java.math.BigDecimal;

/** Línea de un recibo de nómina: nombre del concepto y su valor en pesos. */
public record ConceptoNomina(String nombre, BigDecimal valor) {
}
