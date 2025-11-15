package com.prueba.cuentas.util;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class FechaTransform {

    /**
     * Este metodo transforma una fecha string a un datetime.
     *
     * @param fecha fecha en string
     * @return fecha transformada en datetime
     */
    public static LocalDateTime traformarFecha(final String fecha, String formato) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formato);
        return LocalDateTime.parse(fecha, formatter);
    }
}
