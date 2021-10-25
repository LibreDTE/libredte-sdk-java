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

public class LibreDTE {

    private Rest Rest;

    public LibreDTE(String hash, String url) {
        this.Rest = new Rest(url);
        this.Rest.setAuth(hash);
    }

    public LibreDTE(String hash) {
        this(hash, "https://libredte.cl");
    }

    public Rest get(String api) {
        this.Rest.get("/api"+api);
        return this.Rest;
    }

    public Rest post(String api, JSONObject data) {
        this.Rest.post("/api"+api, data.toString());
        return this.Rest;
    }

}
