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

package cl.libredte;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.nio.charset.StandardCharsets;

/**
 * Clase para consumir servicios web basados en REST
 * @author Esteban De La Fuente Rubio, DeLaF (esteban[at]sasco.cl)
 * @version 2016-07-30
 */
public class Rest {

    private String url; ///< Dirección web base del servicio web (example.com)
    private String auth = null; ///< HTTP Basic Auth (usuario y contraseña en base64)
    private int status; ///< Código de estado entregado por el servicio web
    private String result; ///< Datos del resultado del consumo del servicio web
    private byte[] bytes; ///< Bytes de los datos del resultado del consumo del servicio web

    /**
     * Constructor de la clase para el servicio web
     * @param url Dirección URL del servicio web
     * @author Esteban De La Fuente Rubio, DeLaF (esteban[at]sasco.cl)
     * @version 2016-02-20
     */
    public Rest(String url) {
        this.url = url;
    }

    /**
     * Asigna un token para autenticación (enviará una X como contraseña)
     * @param token Token de autorización para el servicio web
     * @author Esteban De La Fuente Rubio, DeLaF (esteban[at]sasco.cl)
     * @version 2016-06-05
     */
    public void setAuth(String token) {
        byte[] message = (token+":X").getBytes(StandardCharsets.UTF_8);
        this.auth = Base64.getEncoder().encodeToString(message);
    }

    /**
     * Entrega el estado de la respuesta del servicio web
     * @return Código de respuesta HTTP (ok es 200)
     * @author Esteban De La Fuente Rubio, DeLaF (esteban[at]sasco.cl)
     * @version 2016-02-20
     */
    public int getStatus() {
        return this.status;
    }

    /**
     * Entrega el string con el resultado de la consulta del servicio web
     * @return String con el resultado de la consulta al servicio web
     * @author Esteban De La Fuente Rubio, DeLaF (esteban[at]sasco.cl)
     * @version 2016-02-20
     */
    public String getResult() {
        return this.result;
    }

    /**
     * Entrega los bytes con el resultado de la consulta del servicio web
     * @return Bytes con el resultado de la consulta al servicio web
     * @author Esteban De La Fuente Rubio, DeLaF (esteban[at]sasco.cl)
     * @version 2016-06-05
     */
    public byte[] getBytes() {
        return this.bytes;
    }

    /**
     * Entrega el objeto JSON con el resultado de la consulta del servicio web
     * @return Objeto JSON con el resultado de la consulta al servicio web
     * @author Esteban De La Fuente Rubio, DeLaF (esteban[at]sasco.cl)
     * @version 2016-06-05
     */
    public JSONObject getJSON() {
        if (this.result!=null) {
            JSONParser parser = new JSONParser();
            try {
                return (JSONObject)parser.parse(this.result);

            } catch (ParseException e) {
                return null;
            }
        }
        return null;
    }

    /**
     * Método para consumir vía GET un recurso
     * @param resource Recurso que se desea consumir en el servicio web
     * @author Esteban De La Fuente Rubio, DeLaF (esteban[at]sasco.cl)
     * @version 2016-07-30
     */
    public void get(String resource) {
        this.consume(resource, null, "GET");
    }

    /**
     * Método para consumir vía POST un recurso
     * @param resource Recurso que se desea consumir en el servicio web
     * @param data String JSON con los datos que se enviarán al servicio web
     * @author Esteban De La Fuente Rubio, DeLaF (esteban[at]sasco.cl)
     * @version 2016-07-30
     */
    public void post(String resource, String data) {
        this.consume(resource, data, "POST");
    }

    /**
     * Consumir recurso en el servicio web
     * @param resource Recurso que se desea consumir en el servicio web
     * @param data String JSON con los datos que se enviarán al servicio web
     * @author Esteban De La Fuente Rubio, DeLaF (esteban[at]sasco.cl)
     * @version 2016-02-20
     */
    private void consume(String resource, String data, String metodo) {
        this.result = "";
        URL url = null;
        HttpURLConnection conn = null;
        try {
            // crear conexión
            url = new URL(this.url+resource);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(metodo);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            if (this.auth!=null)
                conn.setRequestProperty("Authorization", "Basic " + this.auth);
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            // enviar datos
            if (data != null) {
                conn.setRequestProperty("Content-Length", "" + Integer.toString(data.getBytes().length));
                OutputStream os = conn.getOutputStream();
                os.write(data.getBytes());
                os.flush();
                os.close();
            }
            // obtener respuesta
            this.status = conn.getResponseCode();
            InputStream is = conn.getInputStream();
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int nRead;
            byte[] dataIs = new byte[4098];
            while ((nRead = is.read(dataIs, 0, dataIs.length)) != -1) {
                buffer.write(dataIs, 0, nRead);
            }
            buffer.flush();
            this.bytes = buffer.toByteArray();
            this.result = new String(this.bytes, "UTF-8");
            // cerrar conexión
            conn.disconnect();
        } catch (FileNotFoundException e) {
            // si el código de estado fue diferente a 200 se caerá en esta excepción y se añade el
            // resultado de salida del servicio web
            InputStream error = conn.getErrorStream();
            try {
                int code = error.read();
                while (code != -1) {
                    this.result += (char)code;
                    code = error.read();
                }
                error.close();
            } catch (IOException ignored) {
            }
        }
        catch (Exception e) {
            InputStream error = conn.getErrorStream();
            try {
                int code = error.read();
                while (code != -1) {
                    this.result += (char)code;
                    code = error.read();
                }
                error.close();
            } catch (IOException ignored) {
            }
        }
    }

}
