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
 * - Descargar el PDF de un DTE emitido
 * @author Esteban De La Fuente Rubio, DeLaF (esteban[at]sasco.cl)
 * @version 2017-08-05
 */
public class Ejemplo_005_dte_emitido_pdf {

    private static String url = "https://libredte.cl";
    private static String hash = "";
    private static int rut = 76192083;
    private static int dte = 33;
    private static int folio = 394;
    private static int papelContinuo = 0; // =75 ó =80 para papel contínuo
    private static int copias_tributarias = 1;
    private static int copias_cedibles = 1;
    private static int cedible = 1; // =1 genera cedible, =0 no genera cedible

    public static void main(String[] args) {

        // crear cliente
        LibreDTE LibreDTE = new LibreDTE(hash, url);

        // descargar PDF
        String opciones = "?papelContinuo="+papelContinuo+"&copias_tributarias="+copias_tributarias+"&copias_cedibles="+copias_cedibles+"&cedible="+cedible;
        Rest pdf = LibreDTE.get("/dte/dte_emitidos/pdf/"+dte+"/"+folio+"/"+rut+opciones);
        if (pdf.getStatus()!=200) {
            System.out.println("Error al descargar el PDF del DTE emitido: "+pdf.getResult());
            System.exit(1);
        }

        // guardar PDF en el disco
        try {
            Files.write(Paths.get("ejemplos/Ejemplo_005_dte_emitido_pdf.pdf"), pdf.getBytes());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

}
