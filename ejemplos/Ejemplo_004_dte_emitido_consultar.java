/**
 * LibreDTE
 * Copyright (C) SASCO SpA (https://sasco.cl)
 *
 * Este programa es software libre: usted puede redistribuirlo y/o
 * modificarlo bajo los términos de la Licencia Pública General Affero de GNU
 * publicada por la Fundación para el Software Libre, ya sea la versión
 * 3 de la Licencia, o (a su elección) cualquier versión posterior de la
 * misma.
 *
 * Este programa se distribuye con la esperanza de que sea útil, pero
 * SIN GARANTÍA ALGUNA; ni siquiera la garantía implícita
 * MERCANTIL o de APTITUD PARA UN PROPÓSITO DETERMINADO.
 * Consulte los detalles de la Licencia Pública General Affero de GNU para
 * obtener una información más detallada.
 *
 * Debería haber recibido una copia de la Licencia Pública General Affero de GNU
 * junto a este programa.
 * En caso contrario, consulte <http://www.gnu.org/licenses/agpl.html>.
 */

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import cl.libredte.LibreDTE;
import cl.libredte.Rest;
import java.nio.file.*;
import java.io.*;

/**
 * Ejemplo que muestra los pasos para:
 * - Consultar por un DTE emitido, además verifica la fecha y el monto total (similar a lo que hace el SII)
 * @author Esteban De La Fuente Rubio, DeLaF (esteban[at]sasco.cl)
 * @version 2017-08-05
 */
public class Ejemplo_004_dte_emitido_consultar {

    private static String url = "https://libredte.cl";
    private static String hash = "";
    private static int rut = 76192083;
    private static int dte = 33;
    private static int folio = 394;
    private static String fecha = "2017-08-04";
    private static int total = 6731;
    private static int getXML = 0;

    public static void main(String[] args) {

        // crear cliente
        LibreDTE LibreDTE = new LibreDTE(hash, url);

        // preparar datos
        JSONObject datos = new JSONObject();
        datos.put("emisor", rut);
        datos.put("dte", dte);
        datos.put("folio", folio);
        datos.put("fecha", fecha);
        datos.put("total", total);

        // consultar datos del dte emitido
        Rest consultar = LibreDTE.post("/dte/dte_emitidos/consultar?getXML="+getXML, datos);
        if (consultar.getStatus()!=200) {
            System.out.println("Error al realizar la consulta del DTE emitido: "+consultar.getResult());
            System.exit(1);
        }

        // mostrar resultado de la consulta
        System.out.println(consultar.getResult());

    }

}
