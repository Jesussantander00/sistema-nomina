package com.empresa.nomina.excepciones;

/**
 * Se lanza cuando un dato de entrada o un resultado de la nómina
 * incumple una regla de validación del negocio
 * (por ejemplo, horas negativas o salario neto negativo).
 */
public class ValidacionNominaException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ValidacionNominaException(String mensaje) {
        super(mensaje);
    }
}
