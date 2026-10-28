package com.empresa.nomina.modelo;

/**
 * Marca a los empleados con contrato permanente (Asalariado y por Comisión).
 *
 * <p>Las reglas de beneficios consultan esta interfaz en lugar de preguntar
 * por clases concretas, así un nuevo tipo permanente solo necesita implementarla
 * (principio de <b>segregación de interfaces</b> y <b>abierto/cerrado</b>).</p>
 */
public interface EmpleadoPermanente {
}
