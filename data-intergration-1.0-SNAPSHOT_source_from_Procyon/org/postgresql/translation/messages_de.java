// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.translation;

import java.util.Enumeration;
import java.util.MissingResourceException;
import java.util.Hashtable;
import java.util.ResourceBundle;

public class messages_de extends ResourceBundle
{
    private static final Hashtable table;
    
    public Object handleGetObject(final String key) throws MissingResourceException {
        return messages_de.table.get(key);
    }
    
    public Enumeration getKeys() {
        return messages_de.table.keys();
    }
    
    public ResourceBundle getParent() {
        return super.parent;
    }
    
    static {
        final Hashtable<String, String> table2 = new Hashtable<String, String>();
        table2.put("", "Project-Id-Version: head-de\nReport-Msgid-Bugs-To: \nPOT-Creation-Date: 2012-09-10 19:32-0400\nPO-Revision-Date: 2008-09-12 14:22+0200\nLast-Translator: Andre Bialojahn <ab.spamnews@freenet.de>\nLanguage-Team: Deutsch\nLanguage: \nMIME-Version: 1.0\nContent-Type: text/plain; charset=UTF-8\nContent-Transfer-Encoding: 8bit\nX-Generator: KBabel 1.0.2\nX-Poedit-Language: German\nX-Poedit-Country: GERMANY\n");
        table2.put("Error loading default settings from driverconfig.properties", "Fehler beim Laden der Voreinstellungen aus driverconfig.properties");
        table2.put("Your security policy has prevented the connection from being attempted.  You probably need to grant the connect java.net.SocketPermission to the database server host and port that you wish to connect to.", "Ihre Sicherheitsrichtlinie hat den Versuch des Verbindungsaufbaus verhindert. Sie m\u00fcssen wahrscheinlich der Verbindung zum Datenbankrechner java.net.SocketPermission gew\u00e4hren, um den Rechner auf dem gew\u00e4hlten Port zu erreichen.");
        table2.put("Something unusual has occured to cause the driver to fail. Please report this exception.", "Etwas Ungew\u00f6hnliches ist passiert, das den Treiber fehlschlagen lie\u00df. Bitte teilen Sie diesen Fehler mit.");
        table2.put("Connection attempt timed out.", "Keine Verbindung innerhalb des Zeitintervalls m\u00f6glich.");
        table2.put("Interrupted while attempting to connect.", "Beim Verbindungsversuch trat eine Unterbrechung auf.");
        table2.put("Method {0} is not yet implemented.", "Die Methode {0} ist noch nicht implementiert.");
        table2.put("A connection could not be made using the requested protocol {0}.", "Es konnte keine Verbindung unter Verwendung des Protokolls {0} hergestellt werden.");
        table2.put("Premature end of input stream, expected {0} bytes, but only read {1}.", "Vorzeitiges Ende des Eingabedatenstroms. Es wurden {0} Bytes erwartet, jedoch nur {1} gelesen.");
        table2.put("Expected an EOF from server, got: {0}", "Vom Server wurde ein EOF erwartet, jedoch {0} gelesen.");
        table2.put("An unexpected result was returned by a query.", "Eine Abfrage lieferte ein unerwartetes Resultat.");
        table2.put("Illegal UTF-8 sequence: byte {0} of {1} byte sequence is not 10xxxxxx: {2}", "Ung\u00fcltige UTF-8-Sequenz: Byte {0} der {1} Bytesequenz ist nicht 10xxxxxx: {2}");
        table2.put("Illegal UTF-8 sequence: {0} bytes used to encode a {1} byte value: {2}", "Ung\u00fcltige UTF-8-Sequenz: {0} Bytes wurden verwendet um einen {1} Bytewert zu kodieren: {2}");
        table2.put("Illegal UTF-8 sequence: initial byte is {0}: {1}", "Ung\u00fcltige UTF-8-Sequenz: das erste Byte ist {0}: {1}");
        table2.put("Illegal UTF-8 sequence: final value is out of range: {0}", "Ung\u00fcltige UTF-8-Sequenz: Der letzte Wert ist au\u00dferhalb des zul\u00e4ssigen Bereichs: {0}");
        table2.put("Illegal UTF-8 sequence: final value is a surrogate value: {0}", "Ung\u00fcltige UTF-8-Sequenz: der letzte Wert ist ein Ersatzwert: {0}");
        table2.put("Zero bytes may not occur in string parameters.", "Stringparameter d\u00fcrfen keine Nullbytes enthalten.");
        table2.put("Zero bytes may not occur in identifiers.", "Nullbytes d\u00fcrfen in Bezeichnern nicht vorkommen.");
        table2.put("Cannot convert an instance of {0} to type {1}", "Die Typwandlung f\u00fcr eine Instanz von {0} nach {1} ist nicht m\u00f6glich.");
        table2.put("Connection refused. Check that the hostname and port are correct and that the postmaster is accepting TCP/IP connections.", "Verbindung verweigert. \u00dcberpr\u00fcfen Sie die Korrektheit von Hostnamen und der Portnummer und dass der Datenbankserver TCP/IP-Verbindungen annimmt.");
        table2.put("The connection attempt failed.", "Der Verbindungsversuch schlug fehl.");
        table2.put("The server does not support SSL.", "Der Server unterst\u00fctzt SSL nicht.");
        table2.put("An error occured while setting up the SSL connection.", "Beim Aufbau der SSL-Verbindung trat ein Fehler auf.");
        table2.put("Connection rejected: {0}.", "Verbindung abgewiesen: {0}.");
        table2.put("The server requested password-based authentication, but no password was provided.", "Der Server verlangt passwortbasierte Authentifizierung, jedoch wurde kein Passwort angegeben.");
        table2.put("The authentication type {0} is not supported. Check that you have configured the pg_hba.conf file to include the client''s IP address or subnet, and that it is using an authentication scheme supported by the driver.", "Der Authentifizierungstyp {0} wird nicht unterst\u00fctzt. Stellen Sie sicher, dass die Datei ''pg_hba.conf'' die IP-Adresse oder das Subnetz des Clients enth\u00e4lt und dass der Client ein Authentifizierungsschema nutzt, das vom Treiber unterst\u00fctzt wird.");
        table2.put("Protocol error.  Session setup failed.", "Protokollfehler.  Die Sitzung konnte nicht gestartet werden.");
        table2.put("Backend start-up failed: {0}.", "Das Backend konnte nicht gestartet werden: {0}.");
        table2.put("The column index is out of range: {0}, number of columns: {1}.", "Der Spaltenindex {0} ist au\u00dferhalb des g\u00fcltigen Bereichs. Anzahl Spalten: {1}.");
        table2.put("No value specified for parameter {0}.", "F\u00fcr den Parameter {0} wurde kein Wert angegeben.");
        table2.put("Expected command status BEGIN, got {0}.", "Statt des erwarteten Befehlsstatus BEGIN, wurde {0} empfangen.");
        table2.put("Unexpected command status: {0}.", "Unerwarteter Befehlsstatus: {0}.");
        table2.put("An I/O error occured while sending to the backend.", "Eingabe/Ausgabe-Fehler {0} beim Senden an das Backend.");
        table2.put("Unknown Response Type {0}.", "Die Antwort weist einen unbekannten Typ auf: {0}.");
        table2.put("Ran out of memory retrieving query results.", "Nicht gen\u00fcgend Speicher beim Abholen der Abfrageergebnisse.");
        table2.put("Unable to interpret the update count in command completion tag: {0}.", "Der Updatecount aus der Kommandovervollst\u00e4ndigungsmarkierung(?) {0} konnte nicht interpretiert werden.");
        table2.put("Unable to bind parameter values for statement.", "Der Anweisung konnten keine Parameterwerte zugewiesen werden.");
        table2.put("Bind message length {0} too long.  This can be caused by very large or incorrect length specifications on InputStream parameters.", "Die Nachrichtenl\u00e4nge {0} ist zu gro\u00df. Das kann von sehr gro\u00dfen oder inkorrekten L\u00e4ngenangaben eines InputStream-Parameters herr\u00fchren.");
        table2.put("The server''s DateStyle parameter was changed to {0}. The JDBC driver requires DateStyle to begin with ISO for correct operation.", "Der Parameter ''Date Style'' wurde auf dem Server auf {0} ver\u00e4ndert. Der JDBC-Treiber setzt f\u00fcr korrekte Funktion voraus, dass ''Date Style'' mit ''ISO'' beginnt.");
        table2.put("The server''s standard_conforming_strings parameter was reported as {0}. The JDBC driver expected on or off.", "Der standard_conforming_strings Parameter des Servers steht auf {0}. Der JDBC-Treiber erwartete on oder off.");
        table2.put("The driver currently does not support COPY operations.", "Der Treiber unterst\u00fctzt derzeit keine COPY-Operationen.");
        table2.put("This PooledConnection has already been closed.", "Diese PooledConnection ist bereits geschlossen worden.");
        table2.put("Connection has been closed automatically because a new connection was opened for the same PooledConnection or the PooledConnection has been closed.", "Die Verbindung wurde automatisch geschlossen, da entweder eine neue Verbindung f\u00fcr die gleiche PooledConnection ge\u00f6ffnet wurde, oder die PooledConnection geschlossen worden ist..");
        table2.put("Connection has been closed.", "Die Verbindung wurde geschlossen.");
        table2.put("Statement has been closed.", "Die Anweisung wurde geschlossen.");
        table2.put("DataSource has been closed.", "Die Datenquelle wurde geschlossen.");
        table2.put("Fastpath call {0} - No result was returned and we expected an integer.", "Der Fastpath-Aufruf {0} gab kein Ergebnis zur\u00fcck, jedoch wurde ein Integer erwartet.");
        table2.put("The fastpath function {0} is unknown.", "Die Fastpath-Funktion {0} ist unbekannt.");
        table2.put("Conversion to type {0} failed: {1}.", "Die Umwandlung in den Typ {0} schlug fehl: {1}.");
        table2.put("Cannot tell if path is open or closed: {0}.", "Es konnte nicht ermittelt werden, ob der Pfad offen oder geschlossen ist: {0}.");
        table2.put("The array index is out of range: {0}", "Der Arrayindex ist au\u00dferhalb des g\u00fcltigen Bereichs: {0}.");
        table2.put("The array index is out of range: {0}, number of elements: {1}.", "Der Arrayindex {0} ist au\u00dferhalb des g\u00fcltigen Bereichs. Vorhandene Elemente: {1}.");
        table2.put("Invalid character data was found.  This is most likely caused by stored data containing characters that are invalid for the character set the database was created in.  The most common example of this is storing 8bit data in a SQL_ASCII database.", "Ung\u00fcltige Zeichendaten.  Das ist h\u00f6chstwahrscheinlich von in der Datenbank gespeicherten Zeichen hervorgerufen, die in einer anderen Kodierung vorliegen, als die, in der die Datenbank erstellt wurde.  Das h\u00e4ufigste Beispiel daf\u00fcr ist es, 8Bit-Daten in SQL_ASCII-Datenbanken abzulegen.");
        table2.put("Truncation of large objects is only implemented in 8.3 and later servers.", "Das Abschneiden gro\u00dfer Objekte ist nur in Versionen nach 8.3 implementiert.");
        table2.put("PostgreSQL LOBs can only index to: {0}", "LOBs in PostgreSQL k\u00f6nnen nur auf {0} verweisen.");
        table2.put("LOB positioning offsets start at 1.", "Positionsoffsets f\u00fcr LOBs beginnen bei 1.");
        table2.put("free() was called on this LOB previously", "free() wurde bereits f\u00fcr dieses LOB aufgerufen.");
        table2.put("Unsupported value for stringtype parameter: {0}", "Nichtunterst\u00fctzter Wert f\u00fcr den Stringparameter: {0}");
        table2.put("No results were returned by the query.", "Die Abfrage lieferte kein Ergebnis.");
        table2.put("A result was returned when none was expected.", "Die Anweisung lieferte ein Ergebnis obwohl keines erwartet wurde.");
        table2.put("Failed to create object for: {0}.", "Erstellung des Objektes schlug fehl f\u00fcr: {0}.");
        table2.put("Unable to load the class {0} responsible for the datatype {1}", "Die f\u00fcr den Datentyp {1} verantwortliche Klasse {0} konnte nicht geladen werden.");
        table2.put("Cannot change transaction read-only property in the middle of a transaction.", "Die Nur-Lesen-Eigenschaft einer Transaktion kann nicht w\u00e4hrend der Transaktion ver\u00e4ndert werden.");
        table2.put("Cannot change transaction isolation level in the middle of a transaction.", "Die Transaktions-Trennungsstufe kann nicht w\u00e4hrend einer Transaktion ver\u00e4ndert werden.");
        table2.put("Transaction isolation level {0} not supported.", "Die Transaktions-Trennungsstufe {0} ist nicht unterst\u00fctzt.");
        table2.put("Finalizing a Connection that was never closed:", "Eine Connection wurde finalisiert, die nie geschlossen wurde:");
        table2.put("Unable to translate data into the desired encoding.", "Die Daten konnten nicht in die gew\u00fcnschte Kodierung gewandelt werden.");
        table2.put("Unable to determine a value for MaxIndexKeys due to missing system catalog data.", "Es konnte kein Wert f\u00fcr MaxIndexKeys gefunden werden, da die Systemkatalogdaten fehlen.");
        table2.put("Unable to find name datatype in the system catalogs.", "In den Systemkatalogen konnte der Namensdatentyp nicht gefunden werden.");
        table2.put("Operation requires a scrollable ResultSet, but this ResultSet is FORWARD_ONLY.", "Die Operation erfordert ein scrollbares ResultSet, dieses jedoch ist FORWARD_ONLY.");
        table2.put("Unexpected error while decoding character data from a large object.", "Ein unerwarteter Fehler trat beim Dekodieren von Zeichen aus einem LargeObject (LOB) auf.");
        table2.put("Can''t use relative move methods while on the insert row.", "Relative Bewegungen k\u00f6nnen in der Einf\u00fcgezeile nicht durchgef\u00fchrt werden.");
        table2.put("Invalid fetch direction constant: {0}.", "Unzul\u00e4ssige Richtungskonstante bei fetch: {0}.");
        table2.put("Cannot call cancelRowUpdates() when on the insert row.", "''cancelRowUpdates()'' kann in der Einf\u00fcgezeile nicht aufgerufen werden.");
        table2.put("Cannot call deleteRow() when on the insert row.", "''deleteRow()'' kann in der Einf\u00fcgezeile nicht aufgerufen werden.");
        table2.put("Currently positioned before the start of the ResultSet.  You cannot call deleteRow() here.", "Die augenblickliche Position ist vor dem Beginn des ResultSets.  Dort kann ''deleteRow()'' nicht aufgerufen werden.");
        table2.put("Currently positioned after the end of the ResultSet.  You cannot call deleteRow() here.", "Die augenblickliche Position ist hinter dem Ende des ResultSets.  Dort kann ''deleteRow()'' nicht aufgerufen werden.");
        table2.put("There are no rows in this ResultSet.", "Es gibt keine Zeilen in diesem ResultSet.");
        table2.put("Not on the insert row.", "Nicht in der Einf\u00fcgezeile.");
        table2.put("You must specify at least one column value to insert a row.", "Sie m\u00fcssen mindestens einen Spaltenwert angeben, um eine Zeile einzuf\u00fcgen.");
        table2.put("The JVM claims not to support the encoding: {0}", "Die JVM behauptet, die Zeichenkodierung {0} nicht zu unterst\u00fctzen.");
        table2.put("Provided InputStream failed.", "Der bereitgestellte InputStream scheiterte.");
        table2.put("Provided Reader failed.", "Der bereitgestellte Reader scheiterte.");
        table2.put("Can''t refresh the insert row.", "Die Einf\u00fcgezeile kann nicht aufgefrischt werden.");
        table2.put("Cannot call updateRow() when on the insert row.", "''updateRow()'' kann in der Einf\u00fcgezeile nicht aufgerufen werden.");
        table2.put("Cannot update the ResultSet because it is either before the start or after the end of the results.", "Das ResultSet kann nicht aktualisiert werden, da es entweder vor oder nach dem Ende der Ergebnisse ist.");
        table2.put("ResultSets with concurrency CONCUR_READ_ONLY cannot be updated.", "ResultSets, deren Zugriffsart CONCUR_READ_ONLY ist, k\u00f6nnen nicht aktualisiert werden.");
        table2.put("No primary key found for table {0}.", "F\u00fcr die Tabelle {0} konnte kein Prim\u00e4rschl\u00fcssel gefunden werden.");
        table2.put("Fetch size must be a value greater to or equal to 0.", "Die Fetch-Gr\u00f6\u00dfe muss ein Wert gr\u00f6\u00dfer oder gleich Null sein.");
        table2.put("Bad value for type {0} : {1}", "Unzul\u00e4ssiger Wert f\u00fcr den Typ {0} : {1}.");
        table2.put("The column name {0} was not found in this ResultSet.", "Der Spaltenname {0} wurde in diesem ResultSet nicht gefunden.");
        table2.put("ResultSet is not updateable.  The query that generated this result set must select only one table, and must select all primary keys from that table. See the JDBC 2.1 API Specification, section 5.6 for more details.", "Das ResultSet kann nicht aktualisiert werden.  Die Abfrage, die es erzeugte, darf nur eine Tabelle und muss darin alle Prim\u00e4rschl\u00fcssel ausw\u00e4hlen. Siehe JDBC 2.1 API-Spezifikation, Abschnitt 5.6 f\u00fcr mehr Details.");
        table2.put("This ResultSet is closed.", "Dieses ResultSet ist geschlossen.");
        table2.put("ResultSet not positioned properly, perhaps you need to call next.", "Das ResultSet ist nicht richtig positioniert. Eventuell muss ''next'' aufgerufen werden.");
        table2.put("Multiple ResultSets were returned by the query.", "Die Abfrage ergab mehrere ResultSets.");
        table2.put("A CallableStatement was executed with nothing returned.", "Ein CallableStatement wurde ausgef\u00fchrt ohne etwas zur\u00fcckzugeben.");
        table2.put("A CallableStatement was executed with an invalid number of parameters", "Ein CallableStatement wurde mit einer falschen Anzahl Parameter ausgef\u00fchrt.");
        table2.put("A CallableStatement function was executed and the out parameter {0} was of type {1} however type {2} was registered.", "Eine CallableStatement-Funktion wurde ausgef\u00fchrt und der R\u00fcckgabewert {0} war vom Typ {1}. Jedoch wurde der Typ {2} daf\u00fcr registriert.");
        table2.put("Maximum number of rows must be a value grater than or equal to 0.", "Die maximale Zeilenzahl muss ein Wert gr\u00f6\u00dfer oder gleich Null sein.");
        table2.put("Query timeout must be a value greater than or equals to 0.", "Das Abfragetimeout muss ein Wert gr\u00f6\u00dfer oder gleich Null sein.");
        table2.put("The maximum field size must be a value greater than or equal to 0.", "Die maximale Feldgr\u00f6\u00dfe muss ein Wert gr\u00f6\u00dfer oder gleich Null sein.");
        table2.put("Unknown Types value.", "Unbekannter Typ.");
        table2.put("Invalid stream length {0}.", "Ung\u00fcltige L\u00e4nge des Datenstroms: {0}.");
        table2.put("The JVM claims not to support the {0} encoding.", "Die JVM behauptet, die Zeichenkodierung {0} nicht zu unterst\u00fctzen.");
        table2.put("Unknown type {0}.", "Unbekannter Typ {0}.");
        table2.put("Cannot cast an instance of {0} to type {1}", "Die Typwandlung f\u00fcr eine Instanz von {0} nach {1} ist nicht m\u00f6glich.");
        table2.put("Unsupported Types value: {0}", "Unbekannter Typ: {0}.");
        table2.put("Can''t infer the SQL type to use for an instance of {0}. Use setObject() with an explicit Types value to specify the type to use.", "Der in SQL f\u00fcr eine Instanz von {0} zu verwendende Datentyp kann nicht abgeleitet werden. Benutzen Sie ''setObject()'' mit einem expliziten Typ, um ihn festzulegen.");
        table2.put("This statement does not declare an OUT parameter.  Use '{' ?= call ... '}' to declare one.", "Diese Anweisung deklariert keinen OUT-Parameter. Benutzen Sie '{' ?= call ... '}' um das zu tun.");
        table2.put("wasNull cannot be call before fetching a result.", "wasNull kann nicht aufgerufen werden, bevor ein Ergebnis abgefragt wurde.");
        table2.put("Malformed function or procedure escape syntax at offset {0}.", "Unzul\u00e4ssige Syntax f\u00fcr ein Funktions- oder Prozedur-Escape an Offset {0}.");
        table2.put("Parameter of type {0} was registered, but call to get{1} (sqltype={2}) was made.", "Ein Parameter des Typs {0} wurde registriert, jedoch erfolgte ein Aufruf get{1} (sqltype={2}).");
        table2.put("A CallableStatement was declared, but no call to registerOutParameter(1, <some type>) was made.", "Ein CallableStatement wurde deklariert, aber kein Aufruf von ''registerOutParameter(1, <some type>)'' erfolgte.");
        table2.put("Results cannot be retrieved from a CallableStatement before it is executed.", "Ergebnisse k\u00f6nnen nicht von einem CallableStatement abgerufen werden, bevor es ausgef\u00fchrt wurde.");
        table2.put("This statement has been closed.", "Die Anweisung wurde geschlossen.");
        table2.put("Too many update results were returned.", "Zu viele Updateergebnisse wurden zur\u00fcckgegeben.");
        table2.put("Batch entry {0} {1} was aborted.  Call getNextException to see the cause.", "Batch-Eintrag {0} {1} wurde abgebrochen.  Rufen Sie ''getNextException'' auf, um die Ursache zu erfahren.");
        table2.put("Unexpected error writing large object to database.", "Beim Schreiben eines LargeObjects (LOB) in die Datenbank trat ein unerwarteter Fehler auf.");
        table2.put("{0} function takes one and only one argument.", "Die {0}-Funktion erwartet nur genau ein Argument.");
        table2.put("{0} function takes two and only two arguments.", "Die {0}-Funktion erwartet genau zwei Argumente.");
        table2.put("{0} function takes four and only four argument.", "Die {0}-Funktion erwartet genau vier Argumente.");
        table2.put("{0} function takes two or three arguments.", "Die {0}-Funktion erwartet zwei oder drei Argumente.");
        table2.put("{0} function doesn''t take any argument.", "Die {0}-Funktion akzeptiert kein Argument.");
        table2.put("{0} function takes three and only three arguments.", "Die {0}-Funktion erwartet genau drei Argumente.");
        table2.put("Interval {0} not yet implemented", "Intervall {0} ist noch nicht implementiert.");
        table2.put("Infinite value found for timestamp/date. This cannot be represented as time.", "F\u00fcr den Zeitstempel oder das Datum wurde der Wert ''unendlich'' gefunden. Dies kann nicht als Zeit repr\u00e4sentiert werden.");
        table2.put("The class {0} does not implement org.postgresql.util.PGobject.", "Die Klasse {0} implementiert nicht ''org.postgresql.util.PGobject''.");
        table2.put("Unknown ResultSet holdability setting: {0}.", "Unbekannte Einstellung f\u00fcr die Haltbarkeit des ResultSets: {0}.");
        table2.put("Server versions prior to 8.0 do not support savepoints.", "Der Server unterst\u00fctzt keine Rettungspunkte vor Version 8.0.");
        table2.put("Cannot establish a savepoint in auto-commit mode.", "Ein Rettungspunkt kann im Modus ''auto-commit'' nicht erstellt werden.");
        table2.put("Returning autogenerated keys is not supported.", "Die R\u00fcckgabe automatisch generierter Schl\u00fcssel wird nicht unterst\u00fctzt,");
        table2.put("The parameter index is out of range: {0}, number of parameters: {1}.", "Der Parameterindex {0} ist au\u00dferhalb des g\u00fcltigen Bereichs. Es gibt {1} Parameter.");
        table2.put("Cannot reference a savepoint after it has been released.", "Ein Rettungspunkt kann nicht angesprochen werden, nach dem er entfernt wurde.");
        table2.put("Cannot retrieve the id of a named savepoint.", "Die ID eines benamten Rettungspunktes kann nicht ermittelt werden.");
        table2.put("Cannot retrieve the name of an unnamed savepoint.", "Der Name eines namenlosen Rettungpunktes kann nicht ermittelt werden.");
        table2.put("ClientInfo property not supported.", "Die ClientInfo-Eigenschaft ist nicht unterst\u00fctzt.");
        table2.put("Failed to initialize LargeObject API", "Die LargeObject-API konnte nicht initialisiert werden.");
        table2.put("Large Objects may not be used in auto-commit mode.", "LargeObjects (LOB) d\u00fcrfen im Modus ''auto-commit'' nicht verwendet werden.");
        table2.put("The SSLSocketFactory class provided {0} could not be instantiated.", "Die von {0} bereitgestellte SSLSocketFactory-Klasse konnte nicht instanziiert werden.");
        table2.put("Conversion of interval failed", "Die Umwandlung eines Intervalls schlug fehl.");
        table2.put("Conversion of money failed.", "Die Umwandlung eines W\u00e4hrungsbetrags schlug fehl.");
        table2.put("Detail: {0}", "Detail: {0}");
        table2.put("Hint: {0}", "Hinweis: {0}");
        table2.put("Position: {0}", "Position: {0}");
        table2.put("Where: {0}", "Wobei: {0}");
        table2.put("Internal Query: {0}", "Interne Abfrage: {0}");
        table2.put("Internal Position: {0}", "Interne Position: {0}");
        table2.put("Location: File: {0}, Routine: {1}, Line: {2}", "Ort: Datei: {0}, Routine: {1}, Zeile: {2}.");
        table2.put("Server SQLState: {0}", "Server SQLState: {0}");
        table2.put("Invalid flags", "Ung\u00fcltige Flags");
        table2.put("xid must not be null", "Die xid darf nicht null sein.");
        table2.put("Connection is busy with another transaction", "Die Verbindung ist derzeit mit einer anderen Transaktion besch\u00e4ftigt.");
        table2.put("suspend/resume not implemented", "Anhalten/Fortsetzen ist nicht implementiert.");
        table2.put("Transaction interleaving not implemented", "Transaktionsinterleaving ist nicht implementiert.");
        table2.put("Error disabling autocommit", "Fehler beim Abschalten von Autocommit.");
        table2.put("tried to call end without corresponding start call", "Es wurde versucht, ohne dazugeh\u00f6rigen ''start''-Aufruf ''end'' aufzurufen.");
        table2.put("Not implemented: Prepare must be issued using the same connection that started the transaction", "Nicht implementiert: ''Prepare'' muss \u00fcber die selbe Verbindung abgesetzt werden, die die Transaktion startete.");
        table2.put("Prepare called before end", "''Prepare'' wurde vor ''end'' aufgerufen.");
        table2.put("Server versions prior to 8.1 do not support two-phase commit.", "Der Server unterst\u00fctzt keine zweiphasige Best\u00e4tigung vor Version 8.1.");
        table2.put("Error preparing transaction", "Beim Vorbereiten der Transaktion trat ein Fehler auf.");
        table2.put("Invalid flag", "Ung\u00fcltiges Flag.");
        table2.put("Error during recover", "Beim Wiederherstellen trat ein Fehler auf.");
        table2.put("Error rolling back prepared transaction", "Fehler beim Rollback einer vorbereiteten Transaktion.");
        table2.put("Not implemented: one-phase commit must be issued using the same connection that was used to start it", "Nicht implementiert: Die einphasige Best\u00e4tigung muss \u00fcber die selbe Verbindung abgewickelt werden, die verwendet wurde, um sie zu beginnen.");
        table2.put("commit called before end", "''Commit'' wurde vor ''end'' aufgerufen.");
        table2.put("Error during one-phase commit", "Bei der einphasigen Best\u00e4tigung trat ein Fehler auf.");
        table2.put("Not implemented: 2nd phase commit must be issued using an idle connection", "Nicht implementiert: Die zweite Best\u00e4tigungsphase muss \u00fcber eine im Leerlauf befindliche Verbindung abgewickelt werden.");
        table2.put("Heuristic commit/rollback not supported", "Heuristisches Commit/Rollback wird nicht unterst\u00fctzt.");
        table = table2;
    }
}
