// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.translation;

import java.util.Enumeration;
import java.util.MissingResourceException;
import java.util.Hashtable;
import java.util.ResourceBundle;

public class messages_sr extends ResourceBundle
{
    private static final Hashtable table;
    
    public Object handleGetObject(final String key) throws MissingResourceException {
        return messages_sr.table.get(key);
    }
    
    public Enumeration getKeys() {
        return messages_sr.table.keys();
    }
    
    public ResourceBundle getParent() {
        return super.parent;
    }
    
    static {
        final Hashtable<String, String> table2 = new Hashtable<String, String>();
        table2.put("", "Project-Id-Version: PostgreSQL 8.1\nReport-Msgid-Bugs-To: \nPOT-Creation-Date: 2012-09-10 19:32-0400\nPO-Revision-Date: 2009-05-26 11:13+0100\nLast-Translator: Bojan \u0160kaljac <skaljac (at) gmail.com>\nLanguage-Team: Srpski <skaljac@gmail.com>\nLanguage: \nMIME-Version: 1.0\nContent-Type: text/plain; charset=UTF-8\nContent-Transfer-Encoding: 8bit\nX-Poedit-Language: Serbian\nX-Poedit-Country: YUGOSLAVIA\n");
        table2.put("Error loading default settings from driverconfig.properties", "Gre\u0161ka u \u010ditanju standardnih pode\u0161avanja iz driverconfig.properties");
        table2.put("Your security policy has prevented the connection from being attempted.  You probably need to grant the connect java.net.SocketPermission to the database server host and port that you wish to connect to.", "Sigurnosna pode\u0161avanja su spre\u010dila konekciju. Verovatno je potrebno da dozvolite konekciju klasi java.net.SocketPermission na bazu na serveru.");
        table2.put("Something unusual has occured to cause the driver to fail. Please report this exception.", "Ne\u0161to neobi\u010dno se dogodilo i drajver je zakazao. Molim prijavite ovaj izuzetak.");
        table2.put("Connection attempt timed out.", "Isteklo je vreme za poku\u0161aj konektovanja.");
        table2.put("Interrupted while attempting to connect.", "Prekinut poku\u0161aj konektovanja.");
        table2.put("Method {0} is not yet implemented.", "Metod {0} nije jo\u0161 impelemtiran.");
        table2.put("A connection could not be made using the requested protocol {0}.", "Konekciju nije mogu\u0107e kreirati uz pomo\u0107 protokola {0}.");
        table2.put("Premature end of input stream, expected {0} bytes, but only read {1}.", "Prevremen zavr\u0161etak ulaznog toka podataka,o\u010dekivano {0} bajtova, a pro\u010ditano samo {1}.");
        table2.put("Expected an EOF from server, got: {0}", "O\u010dekivan EOF od servera, a dobijeno: {0}");
        table2.put("An unexpected result was returned by a query.", "Nepredvi\u0111en rezultat je vra\u0107en od strane upita.");
        table2.put("Illegal UTF-8 sequence: byte {0} of {1} byte sequence is not 10xxxxxx: {2}", "Ilegalna UTF-8 sekvenca: bajt {0} od {1} bajtova sekvence nije 10xxxxxx: {2}");
        table2.put("Illegal UTF-8 sequence: {0} bytes used to encode a {1} byte value: {2}", "Ilegalna UTF-8 sekvenca: {0} bytes used to encode a {1} byte value: {2}");
        table2.put("Illegal UTF-8 sequence: initial byte is {0}: {1}", "Ilegalna UTF-8 sekvenca: inicijalni bajt je {0}: {1}");
        table2.put("Illegal UTF-8 sequence: final value is out of range: {0}", "Ilegalna UTF-8 sekvenca: finalna vrednost je van opsega: {0}");
        table2.put("Illegal UTF-8 sequence: final value is a surrogate value: {0}", "Ilegalna UTF-8 sekvenca: finalna vrednost je zamena vrednosti: {0}");
        table2.put("Zero bytes may not occur in string parameters.", "Nula bajtovji se ne smeju pojavljivati u string parametrima.");
        table2.put("Zero bytes may not occur in identifiers.", "Nula bajtovji se ne smeju pojavljivati u identifikatorima.");
        table2.put("Cannot convert an instance of {0} to type {1}", "Nije mogu\u0107e konvertovati instancu {0} u tip {1}");
        table2.put("Connection refused. Check that the hostname and port are correct and that the postmaster is accepting TCP/IP connections.", "Konekcija odbijena. Proverite dali je ime dom\u0107ina (host) koretno i da postmaster podr\u017eava TCP/IP konekcije.");
        table2.put("The connection attempt failed.", "Poku\u0161aj konektovanja propao.");
        table2.put("The server does not support SSL.", "Server ne podr\u017eava SSL.");
        table2.put("An error occured while setting up the SSL connection.", "Gre\u0161ka se dogodila prilikom pode\u0161avanja SSL konekcije.");
        table2.put("Connection rejected: {0}.", "Konekcija odba\u010dena: {0}.");
        table2.put("The server requested password-based authentication, but no password was provided.", "Server zahteva autentifikaciju baziranu na \u0161ifri, ali \u0161ifra nije prosle\u0111ena.");
        table2.put("The authentication type {0} is not supported. Check that you have configured the pg_hba.conf file to include the client''s IP address or subnet, and that it is using an authentication scheme supported by the driver.", "Tip autentifikacije {0} nije podr\u017ean. Proverite dali imate pode\u0161en pg_hba.conf fajl koji uklju\u010duje klijentovu IP adresu ili podmre\u017eu, i da ta mre\u017ea koristi \u0161emu autentifikacije koja je podr\u017eana od strane ovog drajvera.");
        table2.put("Protocol error.  Session setup failed.", "Gre\u0161ka protokola.  Zakazivanje sesije propalo.");
        table2.put("Backend start-up failed: {0}.", "Pozadinsko startovanje propalo: {0}.");
        table2.put("The column index is out of range: {0}, number of columns: {1}.", "Indeks kolone van osega: {0}, broj kolona: {1}.");
        table2.put("No value specified for parameter {0}.", "Nije zadata vrednost za parametar {0}.");
        table2.put("Expected command status BEGIN, got {0}.", "O\u010dekivan status komande je BEGIN, a dobijeno je {0}.");
        table2.put("Unexpected command status: {0}.", "Neo\u010dekivan komandni status: {0}.");
        table2.put("An I/O error occured while sending to the backend.", "Ulazno/izlazna gre\u0161ka se dogodila prilikom slanja podataka pozadinskom procesu.");
        table2.put("Unknown Response Type {0}.", "Nepoznat tip odziva {0}.");
        table2.put("Ran out of memory retrieving query results.", "Nestalo je memorije prilikom preuzimanja rezultata upita.");
        table2.put("Unable to interpret the update count in command completion tag: {0}.", "Neuspe\u0161no prekidanje prebrojavanja a\u017eurivanja u tagu zakompletiranje komandi: {0}.");
        table2.put("Unable to bind parameter values for statement.", "Nije mogu\u0107e na\u0107i vrednost vezivnog parametra za izjavu (statement).");
        table2.put("Bind message length {0} too long.  This can be caused by very large or incorrect length specifications on InputStream parameters.", "Du\u017eina vezivne poruke {0} prevelika.  Ovo je mo\u017eda rezultat veoma velike ili pogre\u0161ne du\u017eine specifikacije za InputStream parametre.");
        table2.put("The server''s DateStyle parameter was changed to {0}. The JDBC driver requires DateStyle to begin with ISO for correct operation.", "Serverov DataStyle parametar promenjen u {0}. JDBC zahteva da DateStyle po\u010dinje sa ISO za uspe\u0161no zavr\u0161avanje operacije.");
        table2.put("The server''s standard_conforming_strings parameter was reported as {0}. The JDBC driver expected on or off.", "Serverov standard_conforming_strings parametar javlja {0}. JDBC drajver ocekuje on ili off.");
        table2.put("The driver currently does not support COPY operations.", "Drajver trenutno ne podr\u017eava COPY operacije.");
        table2.put("This PooledConnection has already been closed.", "PooledConnection je ve\u0107 zatvoren.");
        table2.put("Connection has been closed automatically because a new connection was opened for the same PooledConnection or the PooledConnection has been closed.", "Konekcija je zatvorena automatski zato \u0161to je nova konekcija otvorena za isti PooledConnection ili je PooledConnection zatvoren.");
        table2.put("Connection has been closed.", "Konekcija je ve\u0107 zatvorena.");
        table2.put("Statement has been closed.", "Statemen je ve\u0107 zatvoren.");
        table2.put("DataSource has been closed.", "DataSource je zatvoren.");
        table2.put("Fastpath call {0} - No result was returned and we expected an integer.", "Fastpath poziv {0} - Nikakav rezultat nije vra\u0107en a o\u010dekivan je integer.");
        table2.put("The fastpath function {0} is unknown.", "Fastpath funkcija {0} je nepoznata.");
        table2.put("Conversion to type {0} failed: {1}.", "Konverzija u tip {0} propala: {1}.");
        table2.put("Cannot tell if path is open or closed: {0}.", "Nije mogu\u0107e utvrditi dali je putanja otvorena ili zatvorena: {0}.");
        table2.put("The array index is out of range: {0}", "Indeks niza je van opsega: {0}");
        table2.put("The array index is out of range: {0}, number of elements: {1}.", "Indeks niza je van opsega: {0}, broj elemenata: {1}.");
        table2.put("Invalid character data was found.  This is most likely caused by stored data containing characters that are invalid for the character set the database was created in.  The most common example of this is storing 8bit data in a SQL_ASCII database.", "Prona\u0111eni su neva\u017ee\u0107i karakter podaci. Uzrok je najverovatnije to \u0161to pohranjeni podaci sadr\u017ee karaktere koji su neva\u017ee\u0107i u setu karaktera sa kojima je baza kreirana.  Npr. \u010cuvanje 8bit podataka u SQL_ASCII bazi podataka.");
        table2.put("Truncation of large objects is only implemented in 8.3 and later servers.", "Skra\u0107ivanje velikih objekata je implementirano samo u 8.3 i novijim serverima.");
        table2.put("PostgreSQL LOBs can only index to: {0}", "PostgreSQL LOB mogu jedino da ozna\u010davaju: {0}");
        table2.put("LOB positioning offsets start at 1.", "LOB pozicija ofset po\u010dinje kod 1.");
        table2.put("free() was called on this LOB previously", "free() je pozvan na ovom LOB-u prethodno");
        table2.put("Unsupported value for stringtype parameter: {0}", "Vrednost za parametar tipa string nije podr\u017eana: {0}");
        table2.put("No results were returned by the query.", "Nikakav rezultat nije vra\u0107en od strane upita.");
        table2.put("A result was returned when none was expected.", "Rezultat vra\u0107en ali nikakav rezultat nije o\u010dekivan.");
        table2.put("Custom type maps are not supported.", "Mape sa korisni\u010dki definisanim tipovima nisu podr\u017eane.");
        table2.put("Failed to create object for: {0}.", "Propao poku\u0161aj kreiranja objekta za: {0}.");
        table2.put("Unable to load the class {0} responsible for the datatype {1}", "Nije mogu\u0107e u\u010ditati kalsu {0} odgovornu za tip podataka {1}");
        table2.put("Cannot change transaction read-only property in the middle of a transaction.", "Nije mogu\u0107e izmeniti read-only osobinu transakcije u sred izvr\u0161avanja transakcije.");
        table2.put("Cannot change transaction isolation level in the middle of a transaction.", "Nije mogu\u0107e izmeniti nivo izolacije transakcije u sred izvr\u0161avanja transakcije.");
        table2.put("Transaction isolation level {0} not supported.", "Nivo izolacije transakcije {0} nije podr\u017ean.");
        table2.put("Finalizing a Connection that was never closed:", "Dovr\u0161avanje konekcije koja nikada nije zatvorena:");
        table2.put("Unable to translate data into the desired encoding.", "Nije mogu\u0107e prevesti podatke u odabrani encoding format.");
        table2.put("Unable to determine a value for MaxIndexKeys due to missing system catalog data.", "Nije mogu\u0107e odrediti vrednost za MaxIndexKezs zbog nedostatka podataka u sistemskom katalogu.");
        table2.put("Unable to find name datatype in the system catalogs.", "Nije mogu\u0107e prona\u0107i ime tipa podatka u sistemskom katalogu.");
        table2.put("Operation requires a scrollable ResultSet, but this ResultSet is FORWARD_ONLY.", "Operacija zahteva skrolabilan ResultSet,ali ovaj ResultSet je FORWARD_ONLY.");
        table2.put("Unexpected error while decoding character data from a large object.", "Neo\u010dekivana gre\u0161ka prilikom dekodiranja karaktera iz velikog objekta.");
        table2.put("Can''t use relative move methods while on the insert row.", "Ne mo\u017ee se koristiti metod relativnog pomeranja prilikom ubacivanja redova.");
        table2.put("Invalid fetch direction constant: {0}.", "Pogre\u0161na konstanta za direkciju dono\u0161enja: {0}.");
        table2.put("Cannot call cancelRowUpdates() when on the insert row.", "Nije mogu\u0107e pozvati cancelRowUpdates() prilikom ubacivanja redova.");
        table2.put("Cannot call deleteRow() when on the insert row.", "Nije mogu\u0107e pozvati deleteRow() prilikom ubacivanja redova.");
        table2.put("Currently positioned before the start of the ResultSet.  You cannot call deleteRow() here.", "Trenutna pozicija pre po\u010detka ResultSet-a.  Ne mo\u017eete pozvati deleteRow() na toj poziciji.");
        table2.put("Currently positioned after the end of the ResultSet.  You cannot call deleteRow() here.", "Trenutna pozicija posle kraja ResultSet-a.  Ne mo\u017eete pozvati deleteRow() na toj poziciji.");
        table2.put("There are no rows in this ResultSet.", "U ResultSet-u nema redova.");
        table2.put("Not on the insert row.", "Nije mod ubacivanja redova.");
        table2.put("You must specify at least one column value to insert a row.", "Morate specificirati barem jednu vrednost za kolonu da bi ste ubacili red.");
        table2.put("The JVM claims not to support the encoding: {0}", "JVM tvrdi da ne podr\u017eava encoding: {0}");
        table2.put("Provided InputStream failed.", "Pribaljeni InputStream zakazao.");
        table2.put("Provided Reader failed.", "Pribavljeni \u010dita\u010d (Reader) zakazao.");
        table2.put("Can''t refresh the insert row.", "Nije mogu\u0107e osve\u017eiti uba\u010deni red.");
        table2.put("Cannot call updateRow() when on the insert row.", "Nije mogu\u0107e pozvati updateRow() prilikom ubacivanja redova.");
        table2.put("Cannot update the ResultSet because it is either before the start or after the end of the results.", "Nije mogu\u0107e a\u017eurirati ResultSet zato \u0161to je ili po\u010detak ili kraj rezultata.");
        table2.put("ResultSets with concurrency CONCUR_READ_ONLY cannot be updated.", "ResultSets sa osobinom CONCUR_READ_ONLY ne moe\u017ee biti a\u017euriran.");
        table2.put("No primary key found for table {0}.", "Nije prona\u0111en klju\u010d za tabelu {0}.");
        table2.put("Fetch size must be a value greater to or equal to 0.", "Doneta veli\u010dina mora biti vrednost ve\u0107a ili jednaka 0.");
        table2.put("Bad value for type {0} : {1}", "Pogre\u0161na vrednost za tip {0} : {1}");
        table2.put("The column name {0} was not found in this ResultSet.", "Ime kolone {0} nije pronadjeno u ResultSet.");
        table2.put("ResultSet is not updateable.  The query that generated this result set must select only one table, and must select all primary keys from that table. See the JDBC 2.1 API Specification, section 5.6 for more details.", "ResultSet nije mogu\u0107e a\u017eurirati. Upit koji je generisao ovaj razultat mora selektoati jedino tabelu,i mora selektovati sve primrne klju\u010deve iz te tabele. Pogledajte API specifikaciju za JDBC 2.1, sekciju 5.6 za vi\u0161e detalja.");
        table2.put("This ResultSet is closed.", "ResultSet je zatvoren.");
        table2.put("ResultSet not positioned properly, perhaps you need to call next.", "ResultSet nije pravilno pozicioniran, mo\u017eda je potrebno da pozovete next.");
        table2.put("Can''t use query methods that take a query string on a PreparedStatement.", "Ne mo\u017eete da koristite metode za upit koji uzimaju string iz upita u PreparedStatement-u.");
        table2.put("Multiple ResultSets were returned by the query.", "Vi\u0161estruki ResultSet-vi su vra\u0107eni od strane upita.");
        table2.put("A CallableStatement was executed with nothing returned.", "CallableStatement je izvr\u0161en ali ni\u0161ta nije vre\u0107eno kao rezultat.");
        table2.put("A CallableStatement was executed with an invalid number of parameters", "CallableStatement je izvr\u0161en sa neva\u017ee\u0107im brojem parametara");
        table2.put("A CallableStatement function was executed and the out parameter {0} was of type {1} however type {2} was registered.", "CallableStatement funkcija je izvr\u0161ena dok je izlazni parametar {0} tipa {1} a tip {2} je registrovan kao izlazni parametar.");
        table2.put("Maximum number of rows must be a value grater than or equal to 0.", "Maksimalni broj redova mora biti vrednosti ve\u0107e ili jednake 0.");
        table2.put("Query timeout must be a value greater than or equals to 0.", "Tajm-aut mora biti vrednost ve\u0107a ili jednaka 0.");
        table2.put("The maximum field size must be a value greater than or equal to 0.", "Maksimalna vrednost veli\u010dine polja mora biti vrednost ve\u0107a ili jednaka 0.");
        table2.put("Unknown Types value.", "Nepoznata vrednost za Types.");
        table2.put("Invalid stream length {0}.", "Neva\u017ee\u0107a du\u017eina toka {0}.");
        table2.put("The JVM claims not to support the {0} encoding.", "JVM tvrdi da ne podr\u017eava {0} encoding.");
        table2.put("Unknown type {0}.", "Nepoznat tip {0}.");
        table2.put("Cannot cast an instance of {0} to type {1}", "Nije mogu\u0107e kastovati instancu {0} u tip {1}");
        table2.put("Unsupported Types value: {0}", "Za tip nije podr\u017eana vrednost: {0}");
        table2.put("Can''t infer the SQL type to use for an instance of {0}. Use setObject() with an explicit Types value to specify the type to use.", "Nije mogu\u0107e zaklju\u010diti SQL tip koji bi se koristio sa instancom {0}. Koristite setObject() sa zadatim eksplicitnim tipom vrednosti.");
        table2.put("This statement does not declare an OUT parameter.  Use '{' ?= call ... '}' to declare one.", "Izraz ne deklari\u0161e izlazni parametar. Koristite '{' ?= poziv ... '}' za deklarisanje.");
        table2.put("wasNull cannot be call before fetching a result.", "wasNull nemo\u017ee biti pozvan pre zahvatanja rezultata.");
        table2.put("Malformed function or procedure escape syntax at offset {0}.", "Pogre\u0161na sintaksa u funkciji ili proceduri na poziciji {0}.");
        table2.put("Parameter of type {0} was registered, but call to get{1} (sqltype={2}) was made.", "Parametar tipa {0} je registrovan,ali poziv za get{1} (sql tip={2}) je izvr\u0161en.");
        table2.put("A CallableStatement was declared, but no call to registerOutParameter(1, <some type>) was made.", "CallableStatement jedeklarisan ali nije bilo poziva registerOutParameter (1, <neki_tip>).");
        table2.put("No function outputs were registered.", "Nije registrovan nikakv izlaz iz funkcije.");
        table2.put("Results cannot be retrieved from a CallableStatement before it is executed.", "Razultat nemo\u017ee da se primi iz CallableStatement pre nego \u0161to se on izvr\u0161i.");
        table2.put("This statement has been closed.", "Statement je zatvoren.");
        table2.put("Too many update results were returned.", "Previ\u0161e rezultata za a\u017euriranje je vra\u0107eno.");
        table2.put("Batch entry {0} {1} was aborted.  Call getNextException to see the cause.", "Sme\u0161a prijava {0} {1} je odba\u010dena. Pozovite getNextException da proverite rezlog.");
        table2.put("Unexpected error writing large object to database.", "Neo\u010dekivana gre\u0161ka prilikom upisa velikog objekta u bazu podataka.");
        table2.put("{0} function takes one and only one argument.", "Funkcija {0} prima jedan i samo jedan parametar.");
        table2.put("{0} function takes two and only two arguments.", "Funkcija {0} prima dva i samo dva parametra.");
        table2.put("{0} function takes four and only four argument.", "Funkcija {0} prima \u010detiri i samo \u010detiri parametra.");
        table2.put("{0} function takes two or three arguments.", "Funkcija {0} prima dva ili tri parametra.");
        table2.put("{0} function doesn''t take any argument.", "Funkcija {0} nema parametara.");
        table2.put("{0} function takes three and only three arguments.", "Funkcija {0} prima tri i samo tri parametra.");
        table2.put("Interval {0} not yet implemented", "Interval {0} jo\u0161 nije implementiran.");
        table2.put("Infinite value found for timestamp/date. This cannot be represented as time.", "Beskona\u010dna vrednost je prona\u0111ena za tipestamp/date. To se nemo\u017ee predstaviti kao vreme.");
        table2.put("The class {0} does not implement org.postgresql.util.PGobject.", "Klasa {0} ne implementira org.postgresql.util.PGobject.");
        table2.put("Unknown ResultSet holdability setting: {0}.", "Nepoznata ResultSet pode\u0161avanja za mogu\u0107nost dr\u017eanja (holdability): {0}.");
        table2.put("Server versions prior to 8.0 do not support savepoints.", "Verzije servera manje od 8.0 ne podr\u017eavaju ta\u010dke snimanja.");
        table2.put("Cannot establish a savepoint in auto-commit mode.", "U auto-commit modu nije mogu\u0107e pode\u0161avanje ta\u010dki snimanja.");
        table2.put("Returning autogenerated keys is not supported.", "Vra\u0107anje autogenerisanih klju\u010deva nije podr\u017eano.");
        table2.put("The parameter index is out of range: {0}, number of parameters: {1}.", "Index parametra je van opsega: {0}, broj parametara je: {1}.");
        table2.put("Returning autogenerated keys is only supported for 8.2 and later servers.", "Vra\u0107anje autogenerisanih klju\u010deva je podr\u017eano samo za verzije servera od 8.2 pa na dalje.");
        table2.put("Returning autogenerated keys by column index is not supported.", "Vra\u0107anje autogenerisanih klju\u010deva po kloloni nije podr\u017eano.");
        table2.put("Cannot reference a savepoint after it has been released.", "Nije mogu\u0107e referenciranje ta\u010dke snimanja nakon njenog osloba\u0111anja.");
        table2.put("Cannot retrieve the id of a named savepoint.", "Nije mogu\u0107e primiti id imena ta\u010dke snimanja.");
        table2.put("Cannot retrieve the name of an unnamed savepoint.", "Nije mogu\u0107e izvaditi ime ta\u010dke snimanja koja nema ime.");
        table2.put("Invalid UUID data.", "Neva\u017ee\u0107a UUID podatak.");
        table2.put("Unable to find server array type for provided name {0}.", "Neuspe\u0161no nala\u017eenje liste servera za zadato ime {0}.");
        table2.put("ClientInfo property not supported.", "ClientInfo property nije podr\u017ean.");
        table2.put("Unable to decode xml data.", "Neuspe\u0161no dekodiranje XML podataka.");
        table2.put("Unknown XML Source class: {0}", "Nepoznata XML ulazna klasa: {0}");
        table2.put("Unable to create SAXResult for SQLXML.", "Nije mogu\u0107e kreirati SAXResult za SQLXML.");
        table2.put("Unable to create StAXResult for SQLXML", "Nije mogu\u0107e kreirati StAXResult za SQLXML");
        table2.put("Unknown XML Result class: {0}", "nepoznata XML klasa rezultata: {0}");
        table2.put("This SQLXML object has already been freed.", "Ovaj SQLXML je ve\u0107 obrisan.");
        table2.put("This SQLXML object has not been initialized, so you cannot retrieve data from it.", "SQLXML objekat nije inicijalizovan tako da nije mogu\u0107e preuzimati podatke iz njega.");
        table2.put("Failed to convert binary xml data to encoding: {0}.", "Neuspe\u0161no konvertovanje binarnih XML podataka u kodnu stranu: {0}.");
        table2.put("Unable to convert DOMResult SQLXML data to a string.", "Nije mogu\u0107e konvertovati DOMResult SQLXML podatke u string.");
        table2.put("This SQLXML object has already been initialized, so you cannot manipulate it further.", "SQLXML objekat je ve\u0107 inicijalizovan, tako da ga nije mogu\u0107e dodatno menjati.");
        table2.put("Failed to initialize LargeObject API", "Propao poku\u0161aj inicijalizacije LargeObject API-ja.");
        table2.put("Large Objects may not be used in auto-commit mode.", "Veliki objekti (Large Object) se nemogu koristiti u auto-commit modu.");
        table2.put("The SSLSocketFactory class provided {0} could not be instantiated.", "SSLSocketFactory klasa koju pru\u017ea {0} se nemo\u017ee instancirati.");
        table2.put("Conversion of interval failed", "Konverzija intervala propala.");
        table2.put("Conversion of money failed.", "Konverzija novca (money) propala.");
        table2.put("Detail: {0}", "Detalji: {0}");
        table2.put("Hint: {0}", "Nagovest: {0}");
        table2.put("Position: {0}", "Pozicija: {0}");
        table2.put("Where: {0}", "Gde: {0}");
        table2.put("Internal Query: {0}", "Interni upit: {0}");
        table2.put("Internal Position: {0}", "Interna pozicija: {0}");
        table2.put("Location: File: {0}, Routine: {1}, Line: {2}", "Lokacija: Fajl: {0}, Rutina: {1}, Linija: {2}");
        table2.put("Server SQLState: {0}", "SQLState servera: {0}");
        table2.put("Invalid flags", "Neva\u017ee\u0107e zastavice");
        table2.put("xid must not be null", "xid ne sme biti null");
        table2.put("Connection is busy with another transaction", "Konekcija je zauzeta sa drugom transakciom.");
        table2.put("suspend/resume not implemented", "obustavljanje/nastavljanje nije implementirano.");
        table2.put("Transaction interleaving not implemented", "Preplitanje transakcija nije implementirano.");
        table2.put("Error disabling autocommit", "Gre\u0161ka u isklju\u010divanju autokomita");
        table2.put("tried to call end without corresponding start call", "Poku\u0161aj pozivanja kraja pre odgovaraju\u0107eg po\u010detka.");
        table2.put("Not implemented: Prepare must be issued using the same connection that started the transaction", "Nije implementirano: Spremanje mora biti pozvano uz kori\u0161\u0107enje iste konekcije koja se koristi za startovanje transakcije.");
        table2.put("Prepare called before end", "Pripremanje poziva pre kraja.");
        table2.put("Server versions prior to 8.1 do not support two-phase commit.", "Verzije servera pre 8.1 verzije ne podr\u017eavaju commit iz dve faze.");
        table2.put("Error preparing transaction", "Gre\u0161ka u pripremanju transakcije.");
        table2.put("Invalid flag", "Neva\u017ee\u0107a zastavica (flag)");
        table2.put("Error during recover", "Gre\u0161ka prilikom oporavljanja.");
        table2.put("Error rolling back prepared transaction", "Gre\u0161ka prilikom povratka na prethodo pripremljenu transakciju.");
        table2.put("Not implemented: one-phase commit must be issued using the same connection that was used to start it", "Nije implementirano: Commit iz jedne faze mora biti izdat uz kori\u0161tenje iste konekcije koja je kori\u0161tena za startovanje.");
        table2.put("commit called before end", "commit pozvan pre kraja.");
        table2.put("Error during one-phase commit", "Kre\u0161ka prilikom commit-a iz jedne faze.");
        table2.put("Not implemented: 2nd phase commit must be issued using an idle connection", "Nije implementirano: Dvofazni commit mora biti izdat uz kori\u0161tenje besposlene konekcije.");
        table2.put("Heuristic commit/rollback not supported", "Heuristi\u010dki commit/rollback nije podr\u017ean.");
        table = table2;
    }
}
