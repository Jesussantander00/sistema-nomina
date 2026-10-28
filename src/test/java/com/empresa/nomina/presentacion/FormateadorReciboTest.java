package com.empresa.nomina.presentacion;

import com.empresa.nomina.modelo.EmpleadoTemporal;
import com.empresa.nomina.modelo.NivelRiesgoArl;
import com.empresa.nomina.servicio.ReciboNomina;
import com.empresa.nomina.servicio.ReglasNominaPorDefecto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static com.empresa.nomina.util.Montos.monto;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FormateadorReciboTest {

    @Test
    @DisplayName("El recibo muestra el nombre, el tipo y el salario neto")
    void muestraDatosPrincipales() {
        EmpleadoTemporal empleado = new EmpleadoTemporal("1004", "Pedro Díaz", LocalDate.of(2026, 1, 15),
                NivelRiesgoArl.I, monto("2000000"), LocalDate.of(2026, 12, 31));
        ReciboNomina recibo = ReglasNominaPorDefecto.crearCalculadora()
                .liquidar(empleado, LocalDate.of(2026, 10, 31));

        String texto = new FormateadorRecibo().formatear(recibo);

        assertTrue(texto.contains("Pedro Díaz"));
        assertTrue(texto.contains("Temporal"));
        assertTrue(texto.contains("SALARIO NETO"));
        assertTrue(texto.contains("1.909.560"));
    }
}
