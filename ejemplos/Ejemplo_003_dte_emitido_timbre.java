/**
 * LibreDTE
 * Copyright (C) SASCO SpA (https://sasco.cl)
 *
 * Este programa es software libre: usted puede redistribuirlo y/o modificarlo
 * bajo los términos de la GNU Lesser General Public License (LGPL) publicada
 * por la Fundación para el Software Libre, ya sea la versión 3 de la Licencia,
 * o (a su elección) cualquier versión posterior de la misma.
 *
 * Este programa se distribuye con la esperanza de que sea útil, pero SIN
 * GARANTÍA ALGUNA; ni siquiera la garantía implícita MERCANTIL o de APTITUD
 * PARA UN PROPÓSITO DETERMINADO. Consulte los detalles de la GNU Lesser General
 * Public License (LGPL) para obtener una información más detallada.
 *
 * Debería haber recibido una copia de la GNU Lesser General Public License
 * (LGPL) junto a este programa. En caso contrario, consulte
 * <http://www.gnu.org/licenses/lgpl.html>.
 */

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import cl.libredte.LibreDTE;
import cl.libredte.Rest;
import java.nio.file.*;
import java.io.*;

/**
 * Ejemplo que muestra los pasos para:
 *  - Obtener el timbre en PNG de un DTE emitido
 * @author Esteban De La Fuente Rubio, DeLaF (esteban[at]sasco.cl)
 * @version 2016-07-30
 */
public class Ejemplo_003_dte_emitido_timbre {

    private static String url = "https://libredte.cl";
    private static String hash = "";
    private static int rut = 76192083;
    private static int dte = 33;
    private static int folio = 42;

    public static void main(String[] args) {

        // crear cliente
        LibreDTE LibreDTE = new LibreDTE(hash, url);

        // crear DTE temporal
        Rest ted = LibreDTE.get("/dte/dte_emitidos/ted/"+dte+"/"+folio+"/"+rut);
        if (ted.getStatus()!=200) {
            System.out.println("Error al obtener el TED del DTE: "+ted.getResult());
            System.exit(1);
        }

        // guardar PNG en el disco
        try {
            Files.write(Paths.get("ejemplos/Ejemplo_003_dte_emitido_timbre.png"), ted.getBytes());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

}
