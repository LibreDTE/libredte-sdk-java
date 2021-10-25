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
 *  - Emitir DTE temporal
 *  - Generar DTE real a partir del temporal
 *  - Obtener PDF a partir del DTE real
 * @author Esteban De La Fuente Rubio, DeLaF (esteban[at]sasco.cl)
 * @version 2016-11-20
 */
public class Ejemplo_001_generar_dte {

    private static String url = "https://libredte.cl";
    private static String hash = "";

    private static JSONObject crearDTE() {
        JSONObject dte = new JSONObject();
        // crear encabezado
        JSONObject Encabezado = new JSONObject();
        JSONObject IdDoc = new JSONObject();
        JSONObject Emisor = new JSONObject();
        JSONObject Receptor = new JSONObject();
        dte.put("Encabezado", Encabezado);
        Encabezado.put("IdDoc", IdDoc);
        Encabezado.put("Emisor", Emisor);
        Encabezado.put("Receptor", Receptor);
        IdDoc.put("TipoDTE", 33);
        Emisor.put("RUTEmisor", "76192083-9");
        Receptor.put("RUTRecep", "66666666-6");
        Receptor.put("RznSocRecep", "Persona sin RUT");
        Receptor.put("GiroRecep", "Particular");
        Receptor.put("DirRecep", "Santiago");
        Receptor.put("CmnaRecep", "Santiago");
        // crear detalle
        JSONArray Detalle = new JSONArray();
        dte.put("Detalle", Detalle);
        JSONObject Item = new JSONObject();
        Detalle.add(Item);
        Item.put("NmbItem", "Producto 1");
        Item.put("QtyItem", 2);
        Item.put("PrcItem", 1000);
        // entregar DTE
        return dte;
    }

    public static void main(String[] args) {

        // crear cliente
        LibreDTE LibreDTE = new LibreDTE(hash, url);

        // crear DTE temporal
        Rest emitir = LibreDTE.post("/dte/documentos/emitir", crearDTE());
        if (emitir.getStatus()!=200) {
            System.out.println("Error al emitir DTE temporal: "+emitir.getResult());
            System.exit(1);
        }

        // crear DTE real
        Rest generar = LibreDTE.post("/dte/documentos/generar", emitir.getJSON());
        if (generar.getStatus()!=200) {
            System.out.println("Error al generar DTE real: "+generar.getResult());
            System.exit(1);
        }

        // obtener el PDF del DTE
        Rest generar_pdf = LibreDTE.get("/dte/dte_emitidos/pdf/"+generar.getJSON().get("dte")+"/"+generar.getJSON().get("folio")+"/"+generar.getJSON().get("emisor"));
        if (generar_pdf.getStatus()!=200) {
            System.out.println("Error al generar PDF del DTE: "+generar_pdf.getResult());
            System.exit(1);
        }

        // guardar PDF en el disco
        try {
            Files.write(Paths.get("ejemplos/Ejemplo_001_generar_dte.pdf"), generar_pdf.getBytes());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

}
