// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.translation;

import java.util.Enumeration;
import java.util.MissingResourceException;
import java.util.Hashtable;
import java.util.ResourceBundle;

public class messages_es extends ResourceBundle
{
    private static final Hashtable table;
    
    public Object handleGetObject(final String key) throws MissingResourceException {
        return messages_es.table.get(key);
    }
    
    public Enumeration getKeys() {
        return messages_es.table.keys();
    }
    
    public ResourceBundle getParent() {
        return super.parent;
    }
    
    static {
        final Hashtable<String, String> table2 = new Hashtable<String, String>();
        table2.put("", "Project-Id-Version: JDBC PostgreSQL Driver\nReport-Msgid-Bugs-To: \nPOT-Creation-Date: 2012-09-10 19:32-0400\nPO-Revision-Date: 2004-10-22 16:51-0300\nLast-Translator: Diego Gil <diego@adminsa.com>\nLanguage-Team: \nLanguage: \nMIME-Version: 1.0\nContent-Type: text/plain; charset=UTF-8\nContent-Transfer-Encoding: 8bit\nX-Poedit-Language: Spanish\n");
        table2.put("Something unusual has occured to cause the driver to fail. Please report this exception.", "Algo inusual ha ocurrido que provoc\u00f3 un fallo en el controlador. Por favor reporte esta excepci\u00f3n.");
        table2.put("Premature end of input stream, expected {0} bytes, but only read {1}.", "Final prematuro del flujo de entrada, se esperaban {0} bytes, pero solo se leyeron {1}.");
        table2.put("An unexpected result was returned by a query.", "Una consulta retorn\u00f3 un resultado inesperado.");
        table2.put("Connection refused. Check that the hostname and port are correct and that the postmaster is accepting TCP/IP connections.", "Conexi\u00f3n rechazada. Verifique que el nombre del Host y el puerto sean correctos y que postmaster este aceptando conexiones TCP/IP.");
        table2.put("The connection attempt failed.", "El intento de conexi\u00f3n fall\u00f3.");
        table2.put("The server does not support SSL.", "Este servidor no soporta SSL.");
        table2.put("An error occured while setting up the SSL connection.", "Ha ocorrido un error mientras se establec\u00eda la conexi\u00f3n SSL.");
        table2.put("Connection rejected: {0}.", "Conexi\u00f3n rechazada: {0}.");
        table2.put("The server requested password-based authentication, but no password was provided.", "El servidor requiere autenticaci\u00f3n basada en contrase\u00f1a, pero no se ha provisto ninguna contrase\u00f1a.");
        table2.put("Protocol error.  Session setup failed.", "Error de protocolo. Fall\u00f3 el inicio de la sesi\u00f3n.");
        table2.put("Backend start-up failed: {0}.", "Fall\u00f3 el arranque del Backend: {0}. ");
        table2.put("The column index is out of range: {0}, number of columns: {1}.", "El \u00edndice de la columna est\u00e1 fuera de rango: {0}, n\u00famero de columnas: {1}.");
        table2.put("No value specified for parameter {0}.", "No se ha especificado un valor para el par\u00e1metro {0}.");
        table2.put("An I/O error occured while sending to the backend.", "Un error de E/S ha ocurrido mientras se enviaba al backend.");
        table2.put("Unknown Response Type {0}.", "Tipo de respuesta desconocida {0}.");
        table2.put("The array index is out of range: {0}, number of elements: {1}.", "El \u00edndice del arreglo esta fuera de rango: {0}, n\u00famero de elementos: {1}.");
        table2.put("No results were returned by the query.", "La consulta no retorn\u00f3 ning\u00fan resultado.");
        table2.put("A result was returned when none was expected.", "Se retorn\u00f3 un resultado cuando no se esperaba ninguno.");
        table2.put("Failed to create object for: {0}.", "Fallo al crear objeto: {0}.");
        table2.put("The class {0} does not implement org.postgresql.util.PGobject.", "La clase {0} no implementa org.postgresql.util.PGobject.");
        table2.put("Server SQLState: {0}", "SQLState del servidor: {0}.");
        table = table2;
    }
}
