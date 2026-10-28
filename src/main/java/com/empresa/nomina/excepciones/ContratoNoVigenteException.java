package com.empresa.nomina.excepciones;

/**
 * Se lanza cuando se intenta liquidar la nómina de un empleado
 * cuyo contrato no está vigente en la fecha de corte.
 */
public class ContratoNoVigenteException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ContratoNoVigenteException(String mensaje) {
        super(mensaje);
    }
}
