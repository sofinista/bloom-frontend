package com.floristeria.bloomfront.utilidades;

import feign.FeignException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;

public final class MensajeError {

    private static final String SIN_CONEXION =
            "No se pudo conectar con la API de Bloom. Revisa que el backend esté corriendo en el puerto 8080.";
    private static final Pattern CAMPO_ERROR = Pattern.compile("\"error\"\\s*:\\s*\"(.*?)\"");

    private MensajeError() {
    }

    public static String de(Exception e) {
        String cuerpo = null;
        if (e instanceof FeignException feign) {
            if (feign.status() == -1) {
                return SIN_CONEXION;
            }
            cuerpo = feign.contentUTF8();
        } else if (e instanceof HttpStatusCodeException http) {
            cuerpo = http.getResponseBodyAsString();
        } else if (e instanceof ResourceAccessException) {
            return SIN_CONEXION;
        }
        if (cuerpo != null) {
            Matcher m = CAMPO_ERROR.matcher(cuerpo);
            if (m.find()) {
                return m.group(1);
            }
        }
        return "Ocurrió un error inesperado: " + e.getMessage();
    }
}