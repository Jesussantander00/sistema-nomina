package com.empresa.nomina.modelo;

/**
 * Empleado que puede acceder al fondo de ahorro de la empresa.
 * Solo participa si aceptó el acceso al fondo.
 */
public interface ParticipanteFondoAhorro {

    /** {@code true} si el empleado aceptó participar en el fondo de ahorro. */
    boolean aceptaFondoAhorro();
}
