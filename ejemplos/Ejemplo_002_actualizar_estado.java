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

/**
 * Ejemplo que muestra los pasos para:
 *  - Actualizar el estado de un envío de DTE al SII
 * @author Esteban De La Fuente Rubio, DeLaF (esteban[at]sasco.cl)
 * @version 2016-07-30
 */
public class Ejemplo_002_actualizar_estado {

    private static String url = "https://libredte.cl";
    private static String hash = "";
    private static int rut = 76192083;
    private static int dte = 33;
    private static int folio = 42;
    private static int metodo = 1; // =1 servicio web, =0 correo

    public static void main(String[] args) {

        // crear cliente
        LibreDTE LibreDTE = new LibreDTE(hash, url);

        // crear DTE temporal
        Rest actualizar_estado = LibreDTE.get("/dte/dte_emitidos/actualizar_estado/"+dte+"/"+folio+"/"+rut+"?usarWebservice="+metodo);
        if (actualizar_estado.getStatus()!=200) {
            System.out.println("Error al actualizar el estado del envío del DTE: "+actualizar_estado.getResult());
            System.exit(1);
        }

        // mostrar estado del envío del DTE
        System.out.println(actualizar_estado.getResult());

    }

}
