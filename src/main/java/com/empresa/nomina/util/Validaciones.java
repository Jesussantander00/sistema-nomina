package com.empresa.nomina.util;

import com.empresa.nomina.excepciones.ValidacionNominaException;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Reúne las validaciones de entrada que se repiten en el sistema.
 * Centralizarlas evita duplicar código y da mensajes de error uniformes.
 */
public final class Validaciones {

    private Validaciones() {
        // Clase utilitaria: no se instancia.
    }

    /** Verifica que el valor exista; {@code campo} se usa en el mensaje de error. */
    public static <T> T requerido(T valor, String campo) {
        if (valor == null) {
            throw new ValidacionNominaException("El campo '" + campo + "' es obligatorio.");
        }
        return valor;
    }

    /** Verifica que el texto no sea nulo ni esté vacío. */
    public static String textoRequerido(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new ValidacionNominaException("El campo '" + campo + "' no puede estar vacío.");
        }
        return valor.trim();
    }

    /** Verifica que un monto o cantidad no sea negativo. */
    public static BigDecimal noNegativo(BigDecimal valor, String campo) {
        requerido(valor, campo);
        if (valor.signum() < 0) {
            throw new ValidacionNominaException("El campo '" + campo + "' no puede ser negativo: " + valor);
        }
        return valor;
    }

    /** Verifica que un porcentaje esté expresado como fracción entre 0 y 1 (ej. 0.05 = 5 %). */
    public static BigDecimal porcentaje(BigDecimal valor, String campo) {
        noNegativo(valor, campo);
        if (valor.compareTo(BigDecimal.ONE) > 0) {
            throw new ValidacionNominaException(
                    "El campo '" + campo + "' debe estar entre 0 y 1 (ej. 0.05 para 5 %): " + valor);
        }
        return valor;
    }

    /** Verifica que {@code fin} sea posterior a {@code inicio}. */
    public static void fechaPosterior(LocalDate inicio, LocalDate fin, String campo) {
        requerido(inicio, "fecha de inicio");
        requerido(fin, campo);
        if (!fin.isAfter(inicio)) {
            throw new ValidacionNominaException(
                    "El campo '" + campo + "' debe ser posterior a " + inicio + ".");
        }
    }
}
