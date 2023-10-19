// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.translation;

import java.util.Enumeration;
import java.util.MissingResourceException;
import java.util.Hashtable;
import java.util.ResourceBundle;

public class messages_nl extends ResourceBundle
{
    private static final Hashtable table;
    
    public Object handleGetObject(final String key) throws MissingResourceException {
        return messages_nl.table.get(key);
    }
    
    public Enumeration getKeys() {
        return messages_nl.table.keys();
    }
    
    public ResourceBundle getParent() {
        return super.parent;
    }
    
    static {
        final Hashtable<String, String> table2 = new Hashtable<String, String>();
        table2.put("", "Project-Id-Version: PostgreSQL JDBC Driver 8.0\nReport-Msgid-Bugs-To: \nPOT-Creation-Date: 2012-09-10 19:32-0400\nPO-Revision-Date: 2004-10-11 23:55-0700\nLast-Translator: Arnout Kuiper <ajkuiper@wxs.nl>\nLanguage-Team: Dutch <ajkuiper@wxs.nl>\nLanguage: nl\nMIME-Version: 1.0\nContent-Type: text/plain; charset=UTF-8\nContent-Transfer-Encoding: 8bit\n");
        table2.put("Something unusual has occured to cause the driver to fail. Please report this exception.", "Iets ongewoons is opgetreden, wat deze driver doet falen. Rapporteer deze fout AUB: {0}");
        table2.put("An unexpected result was returned by a query.", "Een onverwacht resultaat werd teruggegeven door een query");
        table2.put("Connection refused. Check that the hostname and port are correct and that the postmaster is accepting TCP/IP connections.", "Verbinding geweigerd. Controleer dat de hostnaam en poort correct zijn, en dat de postmaster is opgestart met de -i vlag, welke TCP/IP networking aanzet.");
        table2.put("Fastpath call {0} - No result was returned and we expected an integer.", "Fastpath aanroep {0} - Geen resultaat werd teruggegeven, terwijl we een integer verwacht hadden.");
        table2.put("The fastpath function {0} is unknown.", "De fastpath functie {0} is onbekend.");
        table2.put("No results were returned by the query.", "Geen resultaten werden teruggegeven door de query.");
        table2.put("Unknown Types value.", "Onbekende Types waarde.");
        table = table2;
    }
}
