package com.empresa.nomina.presentacion;

import com.empresa.nomina.servicio.ConceptoNomina;
import com.empresa.nomina.servicio.ReciboNomina;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Convierte un {@link ReciboNomina} en texto legible.
 *
 * <p>Separa la presentación del cálculo (principio de <b>responsabilidad única</b>):
 * si mañana el recibo se muestra en PDF o en una página web, solo cambia esta capa.</p>
 */
public class FormateadorRecibo {

    private static final int ANCHO = 58;
    private static final String LINEA = "-".repeat(ANCHO);

    private final NumberFormat formatoPesos;

    public FormateadorRecibo() {
        this.formatoPesos = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-CO"));
        this.formatoPesos.setMaximumFractionDigits(0);
    }

    public String formatear(ReciboNomina recibo) {
        StringBuilder texto = new StringBuilder();
        texto.append(LINEA).append('\n');
        texto.append(recibo.empleado().getNombre())
                .append(" · ").append(recibo.empleado().getTipo())
                .append(" · Corte ").append(recibo.fechaCorte()).append('\n');
        texto.append(LINEA).append('\n');
        agregarFila(texto, "Salario base", recibo.salarioBase());
        agregarFila(texto, "Bonos", recibo.bonos());
        agregarFila(texto, "SALARIO BRUTO", recibo.salarioBruto());
        agregarConceptos(texto, "Deducciones", recibo.deducciones(), "- ");
        agregarConceptos(texto, "Beneficios", recibo.beneficios(), "+ ");
        texto.append(LINEA).append('\n');
        agregarFila(texto, "SALARIO NETO", recibo.salarioNeto());
        return texto.toString();
    }

    private void agregarConceptos(StringBuilder texto, String titulo,
                                  List<ConceptoNomina> conceptos, String signo) {
        if (conceptos.isEmpty()) {
            return;
        }
        texto.append(titulo).append(":\n");
        for (ConceptoNomina concepto : conceptos) {
            agregarFila(texto, "  " + signo + concepto.nombre(), concepto.valor());
        }
    }

    private void agregarFila(StringBuilder texto, String etiqueta, BigDecimal valor) {
        String monto = formatoPesos.format(valor);
        int espacios = Math.max(1, ANCHO - etiqueta.length() - monto.length());
        texto.append(etiqueta).append(" ".repeat(espacios)).append(monto).append('\n');
    }
}
