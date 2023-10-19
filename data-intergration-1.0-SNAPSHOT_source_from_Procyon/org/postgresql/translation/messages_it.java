// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.translation;

import java.util.Enumeration;
import java.util.MissingResourceException;
import java.util.Hashtable;
import java.util.ResourceBundle;

public class messages_it extends ResourceBundle
{
    private static final Hashtable table;
    
    public Object handleGetObject(final String key) throws MissingResourceException {
        return messages_it.table.get(key);
    }
    
    public Enumeration getKeys() {
        return messages_it.table.keys();
    }
    
    public ResourceBundle getParent() {
        return super.parent;
    }
    
    static {
        final Hashtable<String, String> table2 = new Hashtable<String, String>();
        table2.put("", "Project-Id-Version: PostgreSQL JDBC Driver 8.2\nReport-Msgid-Bugs-To: \nPOT-Creation-Date: 2012-09-10 19:32-0400\nPO-Revision-Date: 2006-06-23 17:25+0200\nLast-Translator: Giuseppe Sacco <eppesuig@debian.org>\nLanguage-Team: Italian <tp@lists.linux.it>\nLanguage: it\nMIME-Version: 1.0\nContent-Type: text/plain; charset=UTF-8\nContent-Transfer-Encoding: 8bit\n");
        table2.put("Error loading default settings from driverconfig.properties", "Si \u00e8 verificato un errore caricando le impostazioni predefinite da «driverconfig.properties».");
        table2.put("Something unusual has occured to cause the driver to fail. Please report this exception.", "Qualcosa di insolito si \u00e8 verificato causando il fallimento del driver. Per favore riferire all''autore del driver questa eccezione.");
        table2.put("Connection attempt timed out.", "Il tentativo di connessione \u00e8 scaduto.");
        table2.put("Interrupted while attempting to connect.", "Si \u00e8 verificata una interruzione durante il tentativo di connessione.");
        table2.put("Method {0} is not yet implemented.", "Il metodo «{0}» non \u00e8 stato ancora implementato.");
        table2.put("A connection could not be made using the requested protocol {0}.", "Non \u00e8 stato possibile attivare la connessione utilizzando il protocollo richiesto {0}.");
        table2.put("Premature end of input stream, expected {0} bytes, but only read {1}.", "Il flusso di input \u00e8 stato interrotto, sono arrivati {1} byte al posto dei {0} attesi.");
        table2.put("Expected an EOF from server, got: {0}", "Ricevuto dal server «{0}» mentre era atteso un EOF");
        table2.put("An unexpected result was returned by a query.", "Un risultato inaspettato \u00e8 stato ricevuto dalla query.");
        table2.put("Illegal UTF-8 sequence: byte {0} of {1} byte sequence is not 10xxxxxx: {2}", "Sequenza UTF-8 illegale: il byte {0} di una sequenza di {1} byte non \u00e8 10xxxxxx: {2}");
        table2.put("Illegal UTF-8 sequence: {0} bytes used to encode a {1} byte value: {2}", "Sequenza UTF-8 illegale: {0} byte utilizzati per codificare un valore di {1} byte: {2}");
        table2.put("Illegal UTF-8 sequence: initial byte is {0}: {1}", "Sequenza UTF-8 illegale: il byte iniziale \u00e8 {0}: {1}");
        table2.put("Illegal UTF-8 sequence: final value is out of range: {0}", "Sequenza UTF-8 illegale: il valore finale \u00e8 fuori dall''intervallo permesso: {0}");
        table2.put("Illegal UTF-8 sequence: final value is a surrogate value: {0}", "Sequenza UTF-8 illegale: il valore \u00e8 finale \u00e8 un surrogato: {0}");
        table2.put("Zero bytes may not occur in string parameters.", "Byte con valore zero non possono essere contenuti nei parametri stringa.");
        table2.put("Cannot convert an instance of {0} to type {1}", "Non \u00e8 possibile convertire una istanza di «{0}» nel tipo «{1}»");
        table2.put("Connection refused. Check that the hostname and port are correct and that the postmaster is accepting TCP/IP connections.", "Connessione rifiutata. Controllare che il nome dell''host e la porta siano corretti, e che il server (postmaster) sia in esecuzione con l''opzione -i, che abilita le connessioni attraverso la rete TCP/IP.");
        table2.put("The connection attempt failed.", "Il tentativo di connessione \u00e8 fallito.");
        table2.put("The server does not support SSL.", "Il server non supporta SSL.");
        table2.put("An error occured while setting up the SSL connection.", "Si \u00e8 verificato un errore impostando la connessione SSL.");
        table2.put("Connection rejected: {0}.", "Connessione rifiutata: {0}.");
        table2.put("The server requested password-based authentication, but no password was provided.", "Il server ha richiesto l''autenticazione con password, ma tale password non \u00e8 stata fornita.");
        table2.put("The authentication type {0} is not supported. Check that you have configured the pg_hba.conf file to include the client''s IP address or subnet, and that it is using an authentication scheme supported by the driver.", "L''autenticazione di tipo {0} non \u00e8 supportata. Verificare che nel file di configurazione pg_hba.conf sia presente l''indirizzo IP o la sottorete del client, e che lo schema di autenticazione utilizzato sia supportato dal driver.");
        table2.put("Protocol error.  Session setup failed.", "Errore di protocollo. Impostazione della sessione fallita.");
        table2.put("Backend start-up failed: {0}.", "Attivazione del backend fallita: {0}.");
        table2.put("The column index is out of range: {0}, number of columns: {1}.", "Indice di colonna, {0}, \u00e8 maggiore del numero di colonne {1}.");
        table2.put("No value specified for parameter {0}.", "Nessun valore specificato come parametro {0}.");
        table2.put("Expected command status BEGIN, got {0}.", "Lo stato del comando avrebbe dovuto essere BEGIN, mentre invece \u00e8 {0}.");
        table2.put("Unexpected command status: {0}.", "Stato del comando non previsto: {0}.");
        table2.put("An I/O error occured while sending to the backend.", "Si \u00e8 verificato un errore di I/O nella spedizione di dati al server.");
        table2.put("Unknown Response Type {0}.", "Risposta di tipo sconosciuto {0}.");
        table2.put("Ran out of memory retrieving query results.", "Fine memoria scaricando i risultati della query.");
        table2.put("Unable to interpret the update count in command completion tag: {0}.", "Impossibile interpretare il numero degli aggiornamenti nel «tag» di completamento del comando: {0}.");
        table2.put("Unable to bind parameter values for statement.", "Impossibile fare il «bind» dei valori passati come parametri per lo statement.");
        table2.put("Bind message length {0} too long.  This can be caused by very large or incorrect length specifications on InputStream parameters.", "Il messaggio di «bind» \u00e8 troppo lungo ({0}). Questo pu\u00f2 essere causato da una dimensione eccessiva o non corretta dei parametri dell''«InputStream».");
        table2.put("The server''s DateStyle parameter was changed to {0}. The JDBC driver requires DateStyle to begin with ISO for correct operation.", "Il parametro del server «DateStyle» \u00e8 stato cambiato in {0}. Il driver JDBC richiede che «DateStyle» cominci con «ISO» per un corretto funzionamento.");
        table2.put("The driver currently does not support COPY operations.", "Il driver non supporta al momento l''operazione «COPY».");
        table2.put("This PooledConnection has already been closed.", "Questo «PooledConnection» \u00e8 stato chiuso.");
        table2.put("Connection has been closed automatically because a new connection was opened for the same PooledConnection or the PooledConnection has been closed.", "La «Connection» \u00e8 stata chiusa automaticamente perch\u00e9 una nuova l''ha sostituita nello stesso «PooledConnection», oppure il «PooledConnection» \u00e8 stato chiuso.");
        table2.put("Connection has been closed.", "Questo «Connection» \u00e8 stato chiuso.");
        table2.put("Statement has been closed.", "Questo «Statement» \u00e8 stato chiuso.");
        table2.put("DataSource has been closed.", "Questo «DataSource» \u00e8 stato chiuso.");
        table2.put("Fastpath call {0} - No result was returned and we expected an integer.", "Chiamata Fastpath «{0}»: Nessun risultato restituito mentre ci si aspettava un intero.");
        table2.put("The fastpath function {0} is unknown.", "La funzione fastpath «{0}» \u00e8 sconosciuta.");
        table2.put("Conversion to type {0} failed: {1}.", "Conversione al tipo {0} fallita: {1}.");
        table2.put("Cannot tell if path is open or closed: {0}.", "Impossibile stabilire se il percorso \u00e8 aperto o chiuso: {0}.");
        table2.put("The array index is out of range: {0}", "Indice di colonna fuori dall''intervallo ammissibile: {0}");
        table2.put("The array index is out of range: {0}, number of elements: {1}.", "L''indice dell''array \u00e8 fuori intervallo: {0}, numero di elementi: {1}.");
        table2.put("Invalid character data was found.  This is most likely caused by stored data containing characters that are invalid for the character set the database was created in.  The most common example of this is storing 8bit data in a SQL_ASCII database.", "Sono stati trovati caratteri non validi tra i dati. Molto probabilmente sono stati memorizzati dei caratteri che non sono validi per la codifica dei caratteri impostata alla creazione del database. Il caso pi\u00f9 diffuso \u00e8 quello nel quale si memorizzano caratteri a 8bit in un database con codifica SQL_ASCII.");
        table2.put("PostgreSQL LOBs can only index to: {0}", "Il massimo valore per l''indice dei LOB di PostgreSQL \u00e8 {0}. ");
        table2.put("LOB positioning offsets start at 1.", "L''offset per la posizione dei LOB comincia da 1.");
        table2.put("Unsupported value for stringtype parameter: {0}", "Il valore per il parametro di tipo string «{0}» non \u00e8 supportato.");
        table2.put("No results were returned by the query.", "Nessun risultato \u00e8 stato restituito dalla query.");
        table2.put("A result was returned when none was expected.", "\u00c8 stato restituito un valore nonostante non ne fosse atteso nessuno.");
        table2.put("Failed to create object for: {0}.", "Fallita la creazione dell''oggetto per: {0}.");
        table2.put("Unable to load the class {0} responsible for the datatype {1}", "Non \u00e8 possibile caricare la class «{0}» per gestire il tipo «{1}».");
        table2.put("Cannot change transaction read-only property in the middle of a transaction.", "Non \u00e8 possibile modificare la propriet\u00e0 «read-only» delle transazioni nel mezzo di una transazione.");
        table2.put("Cannot change transaction isolation level in the middle of a transaction.", "Non \u00e8 possibile cambiare il livello di isolamento delle transazioni nel mezzo di una transazione.");
        table2.put("Transaction isolation level {0} not supported.", "Il livello di isolamento delle transazioni «{0}» non \u00e8 supportato.");
        table2.put("Finalizing a Connection that was never closed:", "Finalizzazione di una «Connection» che non \u00e8 stata chiusa.");
        table2.put("Unable to translate data into the desired encoding.", "Impossibile tradurre i dati nella codifica richiesta.");
        table2.put("Unable to determine a value for MaxIndexKeys due to missing system catalog data.", "Non \u00e8 possibile trovare il valore di «MaxIndexKeys» nel catalogo si sistema.");
        table2.put("Unable to find name datatype in the system catalogs.", "Non \u00e8 possibile trovare il datatype «name» nel catalogo di sistema.");
        table2.put("Operation requires a scrollable ResultSet, but this ResultSet is FORWARD_ONLY.", "L''operazione richiete un «ResultSet» scorribile mentre questo \u00e8 «FORWARD_ONLY».");
        table2.put("Unexpected error while decoding character data from a large object.", "Errore non previsto durante la decodifica di caratteri a partire da un «large object».");
        table2.put("Can''t use relative move methods while on the insert row.", "Non \u00e8 possibile utilizzare gli spostamenti relativi durante l''inserimento di una riga.");
        table2.put("Invalid fetch direction constant: {0}.", "Costante per la direzione dell''estrazione non valida: {0}.");
        table2.put("Cannot call cancelRowUpdates() when on the insert row.", "Non \u00e8 possibile invocare «cancelRowUpdates()» durante l''inserimento di una riga.");
        table2.put("Cannot call deleteRow() when on the insert row.", "Non \u00e8 possibile invocare «deleteRow()» durante l''inserimento di una riga.");
        table2.put("Currently positioned before the start of the ResultSet.  You cannot call deleteRow() here.", "La posizione attuale \u00e8 precedente all''inizio del ResultSet. Non \u00e8 possibile invocare «deleteRow()» qui.");
        table2.put("Currently positioned after the end of the ResultSet.  You cannot call deleteRow() here.", "La posizione attuale \u00e8 successiva alla fine del ResultSet. Non \u00e8 possibile invocare «deleteRow()» qui.");
        table2.put("There are no rows in this ResultSet.", "Non ci sono righe in questo «ResultSet».");
        table2.put("Not on the insert row.", "Non si \u00e8 in una nuova riga.");
        table2.put("You must specify at least one column value to insert a row.", "Per inserire un record si deve specificare almeno il valore di una colonna.");
        table2.put("The JVM claims not to support the encoding: {0}", "La JVM sostiene di non supportare la codifica: {0}.");
        table2.put("Provided InputStream failed.", "L''«InputStream» fornito \u00e8 fallito.");
        table2.put("Provided Reader failed.", "Il «Reader» fornito \u00e8 fallito.");
        table2.put("Can''t refresh the insert row.", "Non \u00e8 possibile aggiornare la riga in inserimento.");
        table2.put("Cannot call updateRow() when on the insert row.", "Non \u00e8 possibile invocare «updateRow()» durante l''inserimento di una riga.");
        table2.put("Cannot update the ResultSet because it is either before the start or after the end of the results.", "Non \u00e8 possibile aggiornare il «ResultSet» perch\u00e9 la posizione attuale \u00e8 precedente all''inizio o successiva alla file dei risultati.");
        table2.put("ResultSets with concurrency CONCUR_READ_ONLY cannot be updated.", "I «ResultSet» in modalit\u00e0 CONCUR_READ_ONLY non possono essere aggiornati.");
        table2.put("No primary key found for table {0}.", "Non \u00e8 stata trovata la chiave primaria della tabella «{0}».");
        table2.put("Fetch size must be a value greater to or equal to 0.", "La dimensione dell''area di «fetch» deve essere maggiore o eguale a 0.");
        table2.put("Bad value for type {0} : {1}", "Il valore «{1}» non \u00e8 adeguato al tipo «{0}».");
        table2.put("The column name {0} was not found in this ResultSet.", "Colonna denominata «{0}» non \u00e8 presente in questo «ResultSet».");
        table2.put("ResultSet is not updateable.  The query that generated this result set must select only one table, and must select all primary keys from that table. See the JDBC 2.1 API Specification, section 5.6 for more details.", "Il «ResultSet» non \u00e8 aggiornabile. La query che lo genera deve selezionare una sola tabella e deve selezionarne tutti i campi che ne compongono la chiave primaria. Si vedano le specifiche dell''API JDBC 2.1, sezione 5.6, per ulteriori dettagli.");
        table2.put("This ResultSet is closed.", "Questo «ResultSet» \u00e8 chiuso.");
        table2.put("ResultSet not positioned properly, perhaps you need to call next.", "Il «ResultSet» non \u00e8 correttamente posizionato; forse \u00e8 necessario invocare «next()».");
        table2.put("Can''t use query methods that take a query string on a PreparedStatement.", "Non si possono utilizzare i metodi \"query\" che hanno come argomento una stringa nel caso di «PreparedStatement».");
        table2.put("Multiple ResultSets were returned by the query.", "La query ha restituito «ResultSet» multipli.");
        table2.put("A CallableStatement was executed with nothing returned.", "Un «CallableStatement» \u00e8 stato eseguito senza produrre alcun risultato. ");
        table2.put("A CallableStatement function was executed and the out parameter {0} was of type {1} however type {2} was registered.", "\u00c8 stato eseguito un «CallableStatement» ma il parametro in uscita «{0}» era di tipo «{1}» al posto di «{2}», che era stato dichiarato.");
        table2.put("Maximum number of rows must be a value grater than or equal to 0.", "Il numero massimo di righe deve essere maggiore o eguale a 0.");
        table2.put("Query timeout must be a value greater than or equals to 0.", "Il timeout relativo alle query deve essere maggiore o eguale a 0.");
        table2.put("The maximum field size must be a value greater than or equal to 0.", "La dimensione massima del campo deve essere maggiore o eguale a 0.");
        table2.put("Unknown Types value.", "Valore di tipo sconosciuto.");
        table2.put("Invalid stream length {0}.", "La dimensione specificata, {0}, per lo «stream» non \u00e8 valida.");
        table2.put("The JVM claims not to support the {0} encoding.", "La JVM sostiene di non supportare la codifica {0}.");
        table2.put("Unknown type {0}.", "Tipo sconosciuto {0}.");
        table2.put("Cannot cast an instance of {0} to type {1}", "Non \u00e8 possibile fare il cast di una istanza di «{0}» al tipo «{1}».");
        table2.put("Unsupported Types value: {0}", "Valore di tipo «{0}» non supportato.");
        table2.put("Can''t infer the SQL type to use for an instance of {0}. Use setObject() with an explicit Types value to specify the type to use.", "Non \u00e8 possibile identificare il tipo SQL da usare per l''istanza di tipo «{0}». Usare «setObject()» specificando esplicitamente il tipo da usare per questo valore.");
        table2.put("This statement does not declare an OUT parameter.  Use '{' ?= call ... '}' to declare one.", "Questo statement non dichiara il parametro in uscita. Usare «{ ?= call ... }» per farlo.");
        table2.put("Malformed function or procedure escape syntax at offset {0}.", "Sequenza di escape definita erroneamente nella funzione o procedura all''offset {0}.");
        table2.put("Parameter of type {0} was registered, but call to get{1} (sqltype={2}) was made.", "\u00c8 stato definito il parametro di tipo «{0}», ma poi \u00e8 stato invocato il metodo «get{1}()» (sqltype={2}).");
        table2.put("A CallableStatement was declared, but no call to registerOutParameter(1, <some type>) was made.", "\u00c8 stato definito un «CallableStatement» ma non \u00e8 stato invocato il metodo «registerOutParameter(1, <tipo>)».");
        table2.put("This statement has been closed.", "Questo statement \u00e8 stato chiuso.");
        table2.put("Too many update results were returned.", "Sono stati restituiti troppi aggiornamenti.");
        table2.put("Batch entry {0} {1} was aborted.  Call getNextException to see the cause.", "L''operazione «batch» {0} {1} \u00e8 stata interrotta. Chiamare «getNextException» per scoprirne il motivo.");
        table2.put("Unexpected error writing large object to database.", "Errore inatteso inviando un «large object» al database.");
        table2.put("{0} function takes one and only one argument.", "Il metodo «{0}» accetta un ed un solo argomento.");
        table2.put("{0} function takes two and only two arguments.", "Il metodo «{0}» accetta due e solo due argomenti.");
        table2.put("{0} function takes four and only four argument.", "Il metodo «{0}» accetta quattro e solo quattro argomenti.");
        table2.put("{0} function takes two or three arguments.", "Il metodo «{0}» accetta due o tre argomenti.");
        table2.put("{0} function doesn''t take any argument.", "Il metodo «{0}» non accetta argomenti.");
        table2.put("{0} function takes three and only three arguments.", "Il metodo «{0}» accetta tre e solo tre argomenti.");
        table2.put("Interval {0} not yet implemented", "L''intervallo «{0}» non \u00e8 stato ancora implementato.");
        table2.put("Infinite value found for timestamp/date. This cannot be represented as time.", "Il valore specificato per il tipo «timestamp» o «date», infinito, non pu\u00f2 essere rappresentato come «time».");
        table2.put("The class {0} does not implement org.postgresql.util.PGobject.", "La class «{0}» non implementa «org.postgresql.util.PGobject».");
        table2.put("Unknown ResultSet holdability setting: {0}.", "Il parametro «holdability» per il «ResultSet» \u00e8 sconosciuto: {0}.");
        table2.put("Server versions prior to 8.0 do not support savepoints.", "Le versioni del server precedenti alla 8.0 non permettono i punti di ripristino.");
        table2.put("Cannot establish a savepoint in auto-commit mode.", "Non \u00e8 possibile impostare i punti di ripristino in modalit\u00e0 «auto-commit».");
        table2.put("Returning autogenerated keys is not supported.", "La restituzione di chiavi autogenerate non \u00e8 supportata.");
        table2.put("The parameter index is out of range: {0}, number of parameters: {1}.", "Il parametro indice \u00e8 fuori intervallo: {0}, numero di elementi: {1}.");
        table2.put("Cannot reference a savepoint after it has been released.", "Non \u00e8 possibile utilizzare un punto di ripristino successivamente al suo rilascio.");
        table2.put("Cannot retrieve the id of a named savepoint.", "Non \u00e8 possibile trovare l''id del punto di ripristino indicato.");
        table2.put("Cannot retrieve the name of an unnamed savepoint.", "Non \u00e8 possibile trovare il nome di un punto di ripristino anonimo.");
        table2.put("Failed to initialize LargeObject API", "Inizializzazione di LargeObject API fallita.");
        table2.put("Large Objects may not be used in auto-commit mode.", "Non \u00e8 possibile impostare i «Large Object» in modalit\u00e0 «auto-commit».");
        table2.put("The SSLSocketFactory class provided {0} could not be instantiated.", "La classe «SSLSocketFactory» specificata, «{0}», non pu\u00f2 essere istanziata.");
        table2.put("Conversion of interval failed", "Fallita la conversione di un «interval».");
        table2.put("Conversion of money failed.", "Fallita la conversione di un «money».");
        table2.put("Detail: {0}", "Dettaglio: {0}");
        table2.put("Hint: {0}", "Suggerimento: {0}");
        table2.put("Position: {0}", "Posizione: {0}");
        table2.put("Where: {0}", "Dove: {0}");
        table2.put("Internal Query: {0}", "Query interna: {0}");
        table2.put("Internal Position: {0}", "Posizione interna: {0}");
        table2.put("Location: File: {0}, Routine: {1}, Line: {2}", "Individuazione: file: \"{0}\", routine: {1}, linea: {2}");
        table2.put("Server SQLState: {0}", "SQLState del server: {0}");
        table2.put("Invalid flags", "Flag non validi");
        table2.put("xid must not be null", "xid non pu\u00f2 essere NULL");
        table2.put("Connection is busy with another transaction", "La connessione \u00e8 utilizzata da un''altra transazione");
        table2.put("suspend/resume not implemented", "«suspend»/«resume» non implementato");
        table2.put("Transaction interleaving not implemented", "L''\"interleaving\" delle transazioni «{0}» non \u00e8 supportato.");
        table2.put("tried to call end without corresponding start call", "\u00c8 stata chiamata «end» senza la corrispondente chiamata a «start»");
        table2.put("Not implemented: Prepare must be issued using the same connection that started the transaction", "Non implementato: «Prepare» deve essere eseguito nella stessa connessione che ha iniziato la transazione.");
        table2.put("Prepare called before end", "«Prepare» invocato prima della fine");
        table2.put("Server versions prior to 8.1 do not support two-phase commit.", "Le versioni del server precedenti alla 8.1 non permettono i commit \"two-phase\".");
        table2.put("Error preparing transaction", "Errore nel preparare una transazione");
        table2.put("Invalid flag", "Flag non valido");
        table2.put("Error during recover", "Errore durante il ripristino");
        table2.put("Error rolling back prepared transaction", "Errore durante il «rollback» di una transazione preparata");
        table2.put("Not implemented: one-phase commit must be issued using the same connection that was used to start it", "Non implementato: il commit \"one-phase\" deve essere invocato sulla stessa connessione che ha iniziato la transazione.");
        table2.put("commit called before end", "«Commit» \u00e8 stato chiamato prima della fine");
        table2.put("Error during one-phase commit", "Errore durante il commit \"one-phase\"");
        table2.put("Not implemented: 2nd phase commit must be issued using an idle connection", "Non implementato: la seconda fase del «commit» deve essere effettuata con una connessione non in uso");
        table2.put("Heuristic commit/rollback not supported", "«Commit» e «rollback» euristici non sono supportati");
        table = table2;
    }
}
