// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.translation;

import java.util.Enumeration;
import java.util.MissingResourceException;
import java.util.Hashtable;
import java.util.ResourceBundle;

public class messages_pl extends ResourceBundle
{
    private static final Hashtable table;
    
    public Object handleGetObject(final String key) throws MissingResourceException {
        return messages_pl.table.get(key);
    }
    
    public Enumeration getKeys() {
        return messages_pl.table.keys();
    }
    
    public ResourceBundle getParent() {
        return super.parent;
    }
    
    static {
        final Hashtable<String, String> table2 = new Hashtable<String, String>();
        table2.put("", "Project-Id-Version: head-pl\nReport-Msgid-Bugs-To: \nPOT-Creation-Date: 2012-09-10 19:32-0400\nPO-Revision-Date: 2005-05-22 03:01+0200\nLast-Translator: Jaros\u0142aw Jan Pyszny <jarek@pyszny.net>\nLanguage-Team:  <pl@li.org>\nLanguage: \nMIME-Version: 1.0\nContent-Type: text/plain; charset=UTF-8\nContent-Transfer-Encoding: 8bit\nX-Generator: KBabel 1.10\nPlural-Forms:  nplurals=3; plural=(n==1 ? 0 : n%10>=2 && n%10<=4 && (n%100<10 || n%100>=20) ? 1 : 2);\n");
        table2.put("Error loading default settings from driverconfig.properties", "B\u0142\u0105d podczas wczytywania ustawie\u0144 domy\u015blnych z driverconfig.properties");
        table2.put("Something unusual has occured to cause the driver to fail. Please report this exception.", "Co\u015b niezwyk\u0142ego spowodowa\u0142o pad sterownika. Prosz\u0119, zg\u0142o\u015b ten wyj\u0105tek.");
        table2.put("Method {0} is not yet implemented.", "Metoda {0}nie jest jeszcze obs\u0142ugiwana.");
        table2.put("A connection could not be made using the requested protocol {0}.", "Nie mo\u017cna by\u0142o nawi\u0105za\u0107 po\u0142\u0105czenia stosuj\u0105c \u017c\u0105dany protoko\u0142u {0}.");
        table2.put("Premature end of input stream, expected {0} bytes, but only read {1}.", "Przedwczesny koniec strumienia wej\u015bciowego, oczekiwano {0} bajt\u00f3w, odczytano tylko {1}.");
        table2.put("An unexpected result was returned by a query.", "Zapytanie zwr\u00f3ci\u0142o nieoczekiwany wynik.");
        table2.put("Zero bytes may not occur in string parameters.", "Zerowe bajty nie mog\u0105 pojawia\u0107 si\u0119 w parametrach typu \u0142a\u0144cuch znakowy.");
        table2.put("Connection refused. Check that the hostname and port are correct and that the postmaster is accepting TCP/IP connections.", "Po\u0142\u0105czenie odrzucone. Sprawd\u017a, czy prawid\u0142owo ustawi\u0142e\u015b nazw\u0119 hosta oraz port i upewnij si\u0119, czy postmaster przyjmuje po\u0142\u0105czenia TCP/IP.");
        table2.put("The connection attempt failed.", "Pr\u00f3ba nawi\u0105zania po\u0142\u0105czenia nie powiod\u0142a si\u0119.");
        table2.put("The server does not support SSL.", "Serwer nie obs\u0142uguje SSL.");
        table2.put("An error occured while setting up the SSL connection.", "Wyst\u0105pi\u0142 b\u0142\u0105d podczas ustanawiania po\u0142\u0105czenia SSL.");
        table2.put("Connection rejected: {0}.", "Po\u0142\u0105czenie odrzucone: {0}.");
        table2.put("The server requested password-based authentication, but no password was provided.", "Serwer za\u017c\u0105da\u0142 uwierzytelnienia opartego na ha\u015ble, ale \u017cadne has\u0142o nie zosta\u0142o dostarczone.");
        table2.put("The authentication type {0} is not supported. Check that you have configured the pg_hba.conf file to include the client''s IP address or subnet, and that it is using an authentication scheme supported by the driver.", "Uwierzytelnienie typu {0} nie jest obs\u0142ugiwane. Upewnij si\u0119, \u017ce skonfigurowa\u0142e\u015b plik pg_hba.conf tak, \u017ce zawiera on adres IP lub podsie\u0107 klienta oraz \u017ce u\u017cyta metoda uwierzytelnienia jest wspierana przez ten sterownik.");
        table2.put("Protocol error.  Session setup failed.", "B\u0142\u0105d protoko\u0142u. Nie uda\u0142o si\u0119 utworzy\u0107 sesji.");
        table2.put("Backend start-up failed: {0}.", "Start serwera si\u0119 nie powi\u00f3d\u0142: {0}.");
        table2.put("The column index is out of range: {0}, number of columns: {1}.", "Indeks kolumny jest poza zakresem: {0}, liczba kolumn: {1}.");
        table2.put("No value specified for parameter {0}.", "Nie podano warto\u015bci dla parametru {0}.");
        table2.put("Expected command status BEGIN, got {0}.", "Spodziewano si\u0119 statusu komendy BEGIN, otrzymano {0}.");
        table2.put("Unexpected command status: {0}.", "Nieoczekiwany status komendy: {0}.");
        table2.put("An I/O error occured while sending to the backend.", "Wyst\u0105pi\u0142 b\u0142\u0105d We/Wy podczas wysy\u0142ania do serwera.");
        table2.put("Unknown Response Type {0}.", "Nieznany typ odpowiedzi {0}.");
        table2.put("The driver currently does not support COPY operations.", "Sterownik nie obs\u0142uguje aktualnie operacji COPY.");
        table2.put("This PooledConnection has already been closed.", "To PooledConnection zosta\u0142o ju\u017c zamkni\u0119te.");
        table2.put("Connection has been closed automatically because a new connection was opened for the same PooledConnection or the PooledConnection has been closed.", "Po\u0142\u0105czenie zosta\u0142o zamkni\u0119te automatycznie, poniewa\u017c nowe po\u0142\u0105czenie zosta\u0142o otwarte dla tego samego PooledConnection lub PooledConnection zosta\u0142o zamkni\u0119te.");
        table2.put("Connection has been closed.", "Po\u0142\u0105czenie zosta\u0142o zamkni\u0119te.");
        table2.put("DataSource has been closed.", "DataSource zosta\u0142o zamkni\u0119te.");
        table2.put("Fastpath call {0} - No result was returned and we expected an integer.", "Wywo\u0142anie fastpath {0} - Nie otrzymano \u017cadnego wyniku, a oczekiwano liczby ca\u0142kowitej.");
        table2.put("The fastpath function {0} is unknown.", "Funkcja fastpath {0} jest nieznana.");
        table2.put("Conversion to type {0} failed: {1}.", "Konwersja do typu {0} nie powiod\u0142a si\u0119: {1}.");
        table2.put("Cannot tell if path is open or closed: {0}.", "Nie mo\u017cna stwierdzi\u0107, czy \u015bcie\u017cka jest otwarta czy zamkni\u0119ta: {0}.");
        table2.put("The array index is out of range: {0}", "Indeks tablicy jest poza zakresem: {0}");
        table2.put("The array index is out of range: {0}, number of elements: {1}.", "Indeks tablicy jest poza zakresem: {0}, liczba element\u00f3w: {1}.");
        table2.put("Invalid character data was found.  This is most likely caused by stored data containing characters that are invalid for the character set the database was created in.  The most common example of this is storing 8bit data in a SQL_ASCII database.", "Znaleziono nieprawid\u0142owy znak. Najprawdopodobniej jest to spowodowane przechowywaniem w bazie znak\u00f3w, kt\u00f3re nie pasuj\u0105 do zestawu znak\u00f3w wybranego podczas tworzenia bazy danych. Najcz\u0119stszy przyk\u0142ad to przechowywanie 8-bitowych znak\u00f3w w bazie o kodowaniu SQL_ASCII.");
        table2.put("No results were returned by the query.", "Zapytanie nie zwr\u00f3ci\u0142o \u017cadnych wynik\u00f3w.");
        table2.put("A result was returned when none was expected.", "Zwr\u00f3cono wynik zapytania, cho\u0107 nie by\u0142 on oczekiwany.");
        table2.put("Failed to create object for: {0}.", "Nie powiod\u0142o si\u0119 utworzenie obiektu dla: {0}.");
        table2.put("Unable to load the class {0} responsible for the datatype {1}", "Nie jest mo\u017cliwe za\u0142adowanie klasy {0} odpowiedzialnej za typ danych {1}");
        table2.put("Transaction isolation level {0} not supported.", "Poziom izolacji transakcji {0} nie jest obs\u0142ugiwany.");
        table2.put("Cannot call cancelRowUpdates() when on the insert row.", "Nie mo\u017cna wywo\u0142a\u0107 cancelRowUpdates() na wstawianym rekordzie.");
        table2.put("Cannot call deleteRow() when on the insert row.", "Nie mo\u017cna wywo\u0142a\u0107 deleteRow() na wstawianym rekordzie.");
        table2.put("Currently positioned before the start of the ResultSet.  You cannot call deleteRow() here.", "Aktualna pozycja przed pocz\u0105tkiem ResultSet. Nie mo\u017cna wywo\u0142a\u0107 deleteRow().");
        table2.put("Currently positioned after the end of the ResultSet.  You cannot call deleteRow() here.", "Aktualna pozycja za ko\u0144cem ResultSet. Nie mo\u017cna wywo\u0142a\u0107 deleteRow().");
        table2.put("There are no rows in this ResultSet.", "Nie ma \u017cadnych wierszy w tym ResultSet.");
        table2.put("Not on the insert row.", "Nie na wstawianym rekordzie.");
        table2.put("Cannot call updateRow() when on the insert row.", "Nie mo\u017cna wywo\u0142a\u0107 updateRow() na wstawianym rekordzie.");
        table2.put("Fetch size must be a value greater to or equal to 0.", "Rozmiar pobierania musi by\u0107 warto\u015bci\u0105 dodatni\u0105 lub 0.");
        table2.put("Bad value for type {0} : {1}", "Z\u0142a warto\u015b\u0107 dla typu {0}: {1}");
        table2.put("ResultSet is not updateable.  The query that generated this result set must select only one table, and must select all primary keys from that table. See the JDBC 2.1 API Specification, section 5.6 for more details.", "ResultSet nie jest modyfikowalny (not updateable). Zapytanie, kt\u00f3re zwr\u00f3ci\u0142o ten wynik musi dotyczy\u0107 tylko jednej tabeli oraz musi pobiera\u0107 wszystkie klucze g\u0142\u00f3wne tej tabeli. Zobacz Specyfikacj\u0119 JDBC 2.1 API, rozdzia\u0142 5.6, by uzyska\u0107 wi\u0119cej szczeg\u00f3\u0142\u00f3w.");
        table2.put("This ResultSet is closed.", "Ten ResultSet jest zamkni\u0119ty.");
        table2.put("ResultSet not positioned properly, perhaps you need to call next.", "Z\u0142a pozycja w ResultSet, mo\u017ce musisz wywo\u0142a\u0107 next.");
        table2.put("Maximum number of rows must be a value grater than or equal to 0.", "Maksymalna liczba rekord\u00f3w musi by\u0107 warto\u015bci\u0105 dodatni\u0105 lub 0.");
        table2.put("Query timeout must be a value greater than or equals to 0.", "Timeout zapytania musi by\u0107 warto\u015bci\u0105 dodatni\u0105 lub 0.");
        table2.put("The maximum field size must be a value greater than or equal to 0.", "Maksymalny rozmiar pola musi by\u0107 warto\u015bci\u0105 dodatni\u0105 lub 0.");
        table2.put("Unknown Types value.", "Nieznana warto\u015b\u0107 Types.");
        table2.put("Unknown type {0}.", "Nieznany typ {0}.");
        table2.put("Unsupported Types value: {0}", "Nieznana warto\u015b\u0107 Types: {0}");
        table2.put("A CallableStatement was declared, but no call to registerOutParameter(1, <some type>) was made.", "Funkcja CallableStatement zosta\u0142a zadeklarowana, ale nie wywo\u0142ano registerOutParameter (1, <jaki\u015b typ>).");
        table2.put("Too many update results were returned.", "Zapytanie nie zwr\u00f3ci\u0142o \u017cadnych wynik\u00f3w.");
        table2.put("Batch entry {0} {1} was aborted.  Call getNextException to see the cause.", "Zadanie wsadowe {0} {1} zosta\u0142o przerwane. Wywo\u0142aj getNextException by pozna\u0107 przyczyn\u0119.");
        table2.put("The class {0} does not implement org.postgresql.util.PGobject.", "Klasa {0} nie implementuje org.postgresql.util.PGobject.");
        table2.put("The parameter index is out of range: {0}, number of parameters: {1}.", "Indeks parametru jest poza zakresem: {0}, liczba parametr\u00f3w: {1}.");
        table2.put("Failed to initialize LargeObject API", "Nie uda\u0142o si\u0119 zainicjowa\u0107 LargeObject API");
        table2.put("Conversion of interval failed", "Konwersja typu interval nie powiod\u0142a si\u0119");
        table2.put("Conversion of money failed.", "Konwersja typu money nie powiod\u0142a si\u0119.");
        table2.put("Detail: {0}", "Szczeg\u00f3\u0142y: {0}");
        table2.put("Hint: {0}", "Wskaz\u00f3wka: {0}");
        table2.put("Position: {0}", "Pozycja: {0}");
        table2.put("Where: {0}", "Gdzie: {0}");
        table2.put("Internal Query: {0}", "Wewn\u0119trzne Zapytanie: {0}");
        table2.put("Internal Position: {0}", "Wewn\u0119trzna Pozycja: {0}");
        table2.put("Location: File: {0}, Routine: {1}, Line: {2}", "Lokalizacja: Plik: {0}, Procedura: {1}, Linia: {2}");
        table2.put("Server SQLState: {0}", "Serwer SQLState: {0}");
        table = table2;
    }
}
