// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.translation;

import java.util.Enumeration;
import java.util.MissingResourceException;
import java.util.Hashtable;
import java.util.ResourceBundle;

public class messages_tr extends ResourceBundle
{
    private static final Hashtable table;
    
    public Object handleGetObject(final String key) throws MissingResourceException {
        return messages_tr.table.get(key);
    }
    
    public Enumeration getKeys() {
        return messages_tr.table.keys();
    }
    
    public ResourceBundle getParent() {
        return super.parent;
    }
    
    static {
        final Hashtable<String, String> table2 = new Hashtable<String, String>();
        table2.put("", "Project-Id-Version: jdbc-tr\nReport-Msgid-Bugs-To: \nPOT-Creation-Date: 2012-09-10 19:32-0400\nPO-Revision-Date: 2009-05-31 21:47+0200\nLast-Translator: Devrim G\u00dcND\u00dcZ <devrim@gunduz.org>\nLanguage-Team: Turkish <pgsql-tr-genel@PostgreSQL.org>\nLanguage: tr\nMIME-Version: 1.0\nContent-Type: text/plain; charset=UTF-8\nContent-Transfer-Encoding: 8bit\nX-Generator: KBabel 1.3.1\nX-Poedit-Language: Turkish\nX-Poedit-Country: TURKEY\n");
        table2.put("Error loading default settings from driverconfig.properties", "driverconfig.properties dosyas\u0131ndan varsay\u0131lan ayarlar\u0131 y\u00fckleme hatas\u0131");
        table2.put("Your security policy has prevented the connection from being attempted.  You probably need to grant the connect java.net.SocketPermission to the database server host and port that you wish to connect to.", "G\u00fcvenlik politikan\u0131z ba\u011flant\u0131n\u0131n kurulmas\u0131n\u0131 engelledi. java.net.SocketPermission'a veritaban\u0131na ve de ba\u011flanaca\u011f\u0131 porta ba\u011flant\u0131 izni vermelisiniz.");
        table2.put("Something unusual has occured to cause the driver to fail. Please report this exception.", "S\u0131rad\u0131\u015f\u0131 bir durum s\u00fcr\u00fcc\u00fcn\u00fcn hata vermesine sebep oldu. L\u00fctfen bu durumu geli\u015ftiricilere bildirin.");
        table2.put("Connection attempt timed out.", "Ba\u011flant\u0131 denemesi zaman a\u015f\u0131m\u0131na u\u011frad\u0131.");
        table2.put("Interrupted while attempting to connect.", "Ba\u011flan\u0131rken kesildi.");
        table2.put("Method {0} is not yet implemented.", "{0} y\u00f6ntemi hen\u00fcz kodlanmad\u0131.");
        table2.put("A connection could not be made using the requested protocol {0}.", "\u0130stenilen protokol ile ba\u011flant\u0131 kurulamad\u0131 {0}");
        table2.put("Premature end of input stream, expected {0} bytes, but only read {1}.", "Giri\u015f ak\u0131m\u0131nda beklenmeyen dosya sonu, {0} bayt beklenirken sadece {1} bayt al\u0131nd\u0131.");
        table2.put("Expected an EOF from server, got: {0}", "Sunucudan EOF beklendi; ama {0} al\u0131nd\u0131.");
        table2.put("An unexpected result was returned by a query.", "Sorgu beklenmeyen bir sonu\u00e7 d\u00f6nd\u00fcrd\u00fc.");
        table2.put("Illegal UTF-8 sequence: byte {0} of {1} byte sequence is not 10xxxxxx: {2}", "Ge\u00e7ersiz UTF-8 \u00e7oklu bayt karakteri: {0}/{1} bayt\u0131 10xxxxxx de\u011fildir: {2}");
        table2.put("Illegal UTF-8 sequence: {0} bytes used to encode a {1} byte value: {2}", "Ge\u00e7ersiz UTF-8 \u00e7oklu bayt karakteri: {0} bayt, {1} bayt de\u011feri kodlamak i\u00e7in kullan\u0131lm\u0131\u015f: {2}");
        table2.put("Illegal UTF-8 sequence: initial byte is {0}: {1}", "Ge\u00e7ersiz UTF-8 \u00e7oklu bayt karakteri: ilk bayt {0}: {1}");
        table2.put("Illegal UTF-8 sequence: final value is out of range: {0}", "Ge\u00e7ersiz UTF-8 \u00e7oklu bayt karakteri: son de\u011fer s\u0131ra d\u0131\u015f\u0131d\u0131r: {0}");
        table2.put("Illegal UTF-8 sequence: final value is a surrogate value: {0}", "Ge\u00e7ersiz UTF-8 \u00e7oklu bayt karakteri: son de\u011fer yapay bir de\u011ferdir: {0}");
        table2.put("Zero bytes may not occur in string parameters.", "String parametrelerinde s\u0131f\u0131r bayt olamaz.");
        table2.put("Zero bytes may not occur in identifiers.", "Belirte\u00e7lerde s\u0131f\u0131r bayt olamaz.");
        table2.put("Cannot convert an instance of {0} to type {1}", "{0} instance, {1} tipine d\u00f6n\u00fc\u015ft\u00fcr\u00fclemiyor");
        table2.put("Connection refused. Check that the hostname and port are correct and that the postmaster is accepting TCP/IP connections.", "Ba\u011flant\u0131 reddedildi. Sunucu ad\u0131 ve portun do\u011fru olup olmad\u0131\u011f\u0131n\u0131 ve postmaster''in TCP/IP ba\u011flant\u0131lar\u0131n\u0131 kabul edip etmedi\u011fini kontrol ediniz.");
        table2.put("The connection attempt failed.", "Ba\u011flant\u0131 denemesi ba\u015far\u0131s\u0131z oldu.");
        table2.put("The server does not support SSL.", "Sunucu SSL desteklemiyor.");
        table2.put("An error occured while setting up the SSL connection.", "SSL ba\u011flant\u0131s\u0131 ayarlan\u0131rken bir hata olu\u015ftu.");
        table2.put("Connection rejected: {0}.", "Ba\u011flant\u0131 reddedildi {0}");
        table2.put("The server requested password-based authentication, but no password was provided.", "Sunucu \u015fifre tabanl\u0131 yetkilendirme istedi; ancak bir \u015fifre sa\u011flanmad\u0131.");
        table2.put("The authentication type {0} is not supported. Check that you have configured the pg_hba.conf file to include the client''s IP address or subnet, and that it is using an authentication scheme supported by the driver.", "{0} yetkinlendirme tipi desteklenmemektedir. pg_hba.conf dosyan\u0131z\u0131 istemcinin IP adresini ya da subnetini i\u00e7erecek \u015fekilde ayarlay\u0131p ayarlamad\u0131\u011f\u0131n\u0131z\u0131 ve s\u00fcr\u00fcc\u00fc taraf\u0131ndan desteklenen yetkilendirme y\u00f6ntemlerinden birisini kullan\u0131p kullanmad\u0131\u011f\u0131n\u0131 kontrol ediniz.");
        table2.put("Protocol error.  Session setup failed.", "Protokol hatas\u0131.  Oturum kurulumu ba\u015far\u0131s\u0131z oldu.");
        table2.put("Backend start-up failed: {0}.", "Backend ba\u015flamas\u0131 ba\u015far\u0131s\u0131z oldu: {0}");
        table2.put("The column index is out of range: {0}, number of columns: {1}.", "S\u00fctun g\u00e7stergesi kapsam d\u0131\u015f\u0131d\u0131r: {0}, s\u00fctun say\u0131s\u0131: {1}.");
        table2.put("No value specified for parameter {0}.", "{0} parametresi i\u00e7in hi\u00e7 bir de\u011fer belirtilmedi.");
        table2.put("Expected command status BEGIN, got {0}.", "BEGIN komut durumunu beklenirken {0} al\u0131nd\u0131.");
        table2.put("Unexpected command status: {0}.", "Beklenmeyen komut durumu: {0}.");
        table2.put("An I/O error occured while sending to the backend.", "Backend''e g\u00f6nderirken bir I/O hatas\u0131 olu\u015ftu.");
        table2.put("Unknown Response Type {0}.", "Bilinmeyen yan\u0131t tipi {0}");
        table2.put("Ran out of memory retrieving query results.", "Sorgu sonu\u00e7lar\u0131 al\u0131n\u0131rken bellek yetersiz.");
        table2.put("Unable to interpret the update count in command completion tag: {0}.", "Komut tamamlama etiketinde update say\u0131s\u0131 yorumlanam\u0131yor: {0}.");
        table2.put("Unable to bind parameter values for statement.", "Komut i\u00e7in parametre de\u011ferlei ba\u011flanamad\u0131.");
        table2.put("Bind message length {0} too long.  This can be caused by very large or incorrect length specifications on InputStream parameters.", "Bind mesaj uzunlu\u011fu ({0}) fazla uzun. Bu durum InputStream yaln\u0131\u015f uzunluk belirtimlerden kaynaklanabilir.");
        table2.put("The server''s DateStyle parameter was changed to {0}. The JDBC driver requires DateStyle to begin with ISO for correct operation.", "Sunucunun DateStyle parametresi {0} olarak de\u011fi\u015ftirildi. JDBC s\u00fcr\u00fcc\u00fcs\u00fc do\u011fru i\u015flemesi i\u00e7in DateStyle tan\u0131m\u0131n\u0131n ISO i\u015fle ba\u015flamas\u0131n\u0131 gerekir.");
        table2.put("The server''s standard_conforming_strings parameter was reported as {0}. The JDBC driver expected on or off.", "\u0130stemcinin client_standard_conforming_strings parametresi {0} olarak raporland\u0131. JDBC s\u00fcr\u00fcc\u00fcs\u00fc on ya da off olarak bekliyordu.");
        table2.put("The driver currently does not support COPY operations.", "Bu sunucu \u015fu a\u015famada COPY i\u015flemleri desteklememktedir.");
        table2.put("This PooledConnection has already been closed.", "Ge\u00e7erli PooledConnection zaten \u00f6nceden kapat\u0131ld\u0131.");
        table2.put("Connection has been closed automatically because a new connection was opened for the same PooledConnection or the PooledConnection has been closed.", "PooledConnection kapat\u0131ld\u0131\u011f\u0131 i\u00e7in veya ayn\u0131 PooledConnection i\u00e7in yeni bir ba\u011flant\u0131 a\u00e7\u0131ld\u0131\u011f\u0131 i\u00e7in ge\u00e7erli ba\u011flant\u0131 otomatik kapat\u0131ld\u0131.");
        table2.put("Connection has been closed.", "Ba\u011flant\u0131 kapat\u0131ld\u0131.");
        table2.put("Statement has been closed.", "Komut kapat\u0131ld\u0131.");
        table2.put("DataSource has been closed.", "DataSource kapat\u0131ld\u0131.");
        table2.put("Fastpath call {0} - No result was returned and we expected an integer.", "Fastpath call {0} - Integer beklenirken hi\u00e7bir sonu\u00e7 getirilmedi.");
        table2.put("The fastpath function {0} is unknown.", "{0} fastpath fonksiyonu bilinmemektedir.");
        table2.put("Conversion to type {0} failed: {1}.", "{0} veri tipine d\u00f6n\u00fc\u015ft\u00fcrme hatas\u0131: {1}.");
        table2.put("Cannot tell if path is open or closed: {0}.", "Path\u0131n a\u00e7\u0131k m\u0131 kapal\u0131 oldu\u011funu tespit edilemiyor: {0}.");
        table2.put("The array index is out of range: {0}", "Dizi g\u00f6stergesi kapsam d\u0131\u015f\u0131d\u0131r: {0}");
        table2.put("The array index is out of range: {0}, number of elements: {1}.", "Dizin g\u00f6stergisi kapsam d\u0131\u015f\u0131d\u0131r: {0}, \u00f6\u011fe say\u0131s\u0131: {1}.");
        table2.put("Invalid character data was found.  This is most likely caused by stored data containing characters that are invalid for the character set the database was created in.  The most common example of this is storing 8bit data in a SQL_ASCII database.", "Ge\u00e7ersiz karakterler bulunmu\u015ftur. Bunun sebebi, verilerde veritaban\u0131n destekledi\u011fi dil kodlamadaki karakterlerin d\u0131\u015f\u0131nda bir karaktere rastlamas\u0131d\u0131r. Bunun en yayg\u0131n \u00f6rne\u011fi 8 bitlik veriyi SQL_ASCII veritaban\u0131nda saklamas\u0131d\u0131r.");
        table2.put("Truncation of large objects is only implemented in 8.3 and later servers.", "Large objectlerin temizlenmesi 8.3 ve sonraki s\u00fcr\u00fcmlerde kodlanm\u0131\u015ft\u0131r.");
        table2.put("PostgreSQL LOBs can only index to: {0}", "PostgreSQL LOB g\u00f6stergeleri sadece {0} referans edebilir");
        table2.put("LOB positioning offsets start at 1.", "LOB ba\u011flang\u0131\u00e7 adresi 1Den ba\u015fl\u0131yor");
        table2.put("free() was called on this LOB previously", "Bu LOB'da free() daha \u00f6nce \u00e7a\u011f\u0131r\u0131ld\u0131");
        table2.put("Unsupported value for stringtype parameter: {0}", "strinftype parametresi i\u00e7in destekleneyen de\u011fer: {0}");
        table2.put("No results were returned by the query.", "Sorgudan hi\u00e7 bir sonu\u00e7 d\u00f6nmedi.");
        table2.put("A result was returned when none was expected.", "Hi\u00e7bir sonu\u00e7 kebklenimezken sonu\u00e7 getirildi.");
        table2.put("Custom type maps are not supported.", "\u00d6zel tip e\u015fle\u015ftirmeleri desteklenmiyor.");
        table2.put("Failed to create object for: {0}.", "{0} i\u00e7in nesne olu\u015fturma hatas\u0131.");
        table2.put("Unable to load the class {0} responsible for the datatype {1}", "{1} veri tipinden sorumlu {0} s\u0131n\u0131f\u0131 y\u00fcklenemedi");
        table2.put("Cannot change transaction read-only property in the middle of a transaction.", "Transaction ortas\u0131nda ge\u00e7erli transactionun read-only \u00f6zell\u011fi de\u011fi\u015ftirilemez.");
        table2.put("Cannot change transaction isolation level in the middle of a transaction.", "Transaction ortas\u0131nda ge\u00e7erli transactionun transaction isolation level \u00f6zell\u011fi de\u011fi\u015ftirilemez.");
        table2.put("Transaction isolation level {0} not supported.", "Transaction isolation level {0} desteklenmiyor.");
        table2.put("Finalizing a Connection that was never closed:", "Kapat\u0131lmam\u0131\u015f ba\u011flant\u0131 sonland\u0131r\u0131l\u0131yor.");
        table2.put("Unable to translate data into the desired encoding.", "Veri, istenilen dil kodlamas\u0131na \u00e7evrilemiyor.");
        table2.put("Unable to determine a value for MaxIndexKeys due to missing system catalog data.", "Sistem katalo\u011fu olmad\u0131\u011f\u0131ndan MaxIndexKeys de\u011ferini tespit edilememektedir.");
        table2.put("Unable to find name datatype in the system catalogs.", "Sistem kataloglar\u0131nda name veri tipi bulunam\u0131yor.");
        table2.put("Operation requires a scrollable ResultSet, but this ResultSet is FORWARD_ONLY.", "\u0130\u015flem, kayd\u0131r\u0131labilen ResultSet gerektirir, ancak bu ResultSet FORWARD_ONLYdir.");
        table2.put("Unexpected error while decoding character data from a large object.", "Large-object nesnesinden karakter veriyi \u00e7\u00f6zerken beklenmeyen hata.");
        table2.put("Can''t use relative move methods while on the insert row.", "Insert kayd\u0131 \u00fczerinde relative move method kullan\u0131lamaz.");
        table2.put("Invalid fetch direction constant: {0}.", "Getirme y\u00f6n\u00fc de\u011fi\u015fmezi ge\u00e7ersiz: {0}.");
        table2.put("Cannot call cancelRowUpdates() when on the insert row.", "Insert edilmi\u015f kayd\u0131n \u00fczerindeyken cancelRowUpdates() \u00e7a\u011f\u0131r\u0131lamaz.");
        table2.put("Cannot call deleteRow() when on the insert row.", "Insert  kayd\u0131 \u00fczerinde deleteRow() \u00e7a\u011f\u0131r\u0131lamaz.");
        table2.put("Currently positioned before the start of the ResultSet.  You cannot call deleteRow() here.", "\u015eu an ResultSet ba\u015flangc\u0131\u0131ndan \u00f6nce konumland\u0131. deleteRow() burada \u00e7a\u011f\u0131rabilirsiniz.");
        table2.put("Currently positioned after the end of the ResultSet.  You cannot call deleteRow() here.", "\u015eu an ResultSet sonucundan sonra konumland\u0131. deleteRow() burada \u00e7a\u011f\u0131rabilirsiniz.");
        table2.put("There are no rows in this ResultSet.", "Bu ResultSet i\u00e7inde kay\u0131t bulunamad\u0131.");
        table2.put("Not on the insert row.", "Insert kayd\u0131 de\u011fil.");
        table2.put("You must specify at least one column value to insert a row.", "Bir sat\u0131r eklemek i\u00e7in en az bir s\u00fctun de\u011ferini belirtmelisiniz.");
        table2.put("The JVM claims not to support the encoding: {0}", "JVM, {0} dil kodlamas\u0131n\u0131 desteklememektedir.");
        table2.put("Provided InputStream failed.", "Sa\u011flanm\u0131\u015f InputStream ba\u015far\u0131s\u0131z.");
        table2.put("Provided Reader failed.", "Sa\u011flanm\u0131\u015f InputStream ba\u015far\u0131s\u0131z.");
        table2.put("Can''t refresh the insert row.", "Inser sat\u0131r\u0131 yenilenemiyor.");
        table2.put("Cannot call updateRow() when on the insert row.", "Insert  kayd\u0131 \u00fczerinde updateRow() \u00e7a\u011f\u0131r\u0131lamaz.");
        table2.put("Cannot update the ResultSet because it is either before the start or after the end of the results.", "ResultSet, sonu\u00e7lar\u0131n ilk kayd\u0131ndan \u00f6nce veya son kayd\u0131ndan sonra oldu\u011fu i\u00e7in g\u00fcncelleme yap\u0131lamamaktad\u0131r.");
        table2.put("ResultSets with concurrency CONCUR_READ_ONLY cannot be updated.", "E\u015f zamanlama CONCUR_READ_ONLY olan ResultSet''ler de\u011fi\u015ftirilemez");
        table2.put("No primary key found for table {0}.", "{0} tablosunda primary key yok.");
        table2.put("Fetch size must be a value greater to or equal to 0.", "Fetch boyutu s\u0131f\u0131r veya daha b\u00fcy\u00fck bir de\u011fer olmal\u0131d\u0131r.");
        table2.put("Bad value for type {0} : {1}", "{0} veri tipi i\u00e7in ge\u00e7ersiz de\u011fer : {1}");
        table2.put("The column name {0} was not found in this ResultSet.", "Bu ResultSet i\u00e7inde {0} s\u00fctun ad\u0131 bulunamad\u0131.");
        table2.put("ResultSet is not updateable.  The query that generated this result set must select only one table, and must select all primary keys from that table. See the JDBC 2.1 API Specification, section 5.6 for more details.", "ResultSet de\u011fi\u015ftirilemez. Bu sonucu \u00fcreten sorgu tek bir tablodan sorgulamal\u0131 ve tablonun t\u00fcm primary key alanlar\u0131 belirtmelidir. Daha fazla bilgi i\u00e7in bk. JDBC 2.1 API Specification, section 5.6.");
        table2.put("This ResultSet is closed.", "ResultSet kapal\u0131d\u0131r.");
        table2.put("ResultSet not positioned properly, perhaps you need to call next.", "ResultSet do\u011fru konumlanmam\u0131\u015ft\u0131r, next i\u015flemi \u00e7a\u011f\u0131rman\u0131z gerekir.");
        table2.put("Can''t use query methods that take a query string on a PreparedStatement.", "PreparedStatement ile sorgu sat\u0131r\u0131 alan sorgu y\u00f6ntemleri kullan\u0131lamaz.");
        table2.put("Multiple ResultSets were returned by the query.", "Sorgu taraf\u0131ndan birden fazla ResultSet getirildi.");
        table2.put("A CallableStatement was executed with nothing returned.", "CallableStatement \u00e7al\u0131\u015ft\u0131rma sonucunda veri getirilmedi.");
        table2.put("A CallableStatement was executed with an invalid number of parameters", "CallableStatement ge\u00e7ersiz say\u0131da parametre ile \u00e7al\u0131\u015ft\u0131r\u0131ld\u0131.");
        table2.put("A CallableStatement function was executed and the out parameter {0} was of type {1} however type {2} was registered.", "CallableStatement \u00e7al\u0131\u015ft\u0131r\u0131ld\u0131, ancak {2} tipi kaydedilmesine ra\u011fmen d\u00f6nd\u00fcrme parametresi {0} ve tipi {1} idi.");
        table2.put("Maximum number of rows must be a value grater than or equal to 0.", "En b\u00fcy\u00fck getirilecek sat\u0131r say\u0131s\u0131 s\u0131f\u0131rdan b\u00fcy\u00fck olmal\u0131d\u0131r.");
        table2.put("Query timeout must be a value greater than or equals to 0.", "Sorgu zaman a\u015f\u0131m\u0131 de\u011fer s\u0131f\u0131r veya s\u0131f\u0131rdan b\u00fcy\u00fck bir say\u0131 olmal\u0131d\u0131r.");
        table2.put("The maximum field size must be a value greater than or equal to 0.", "En b\u00fcy\u00fck alan boyutu s\u0131f\u0131r ya da s\u0131f\u0131rdan b\u00fcy\u00fck bir de\u011fer olmal\u0131.");
        table2.put("Unknown Types value.", "Ge\u00e7ersiz Types de\u011feri.");
        table2.put("Invalid stream length {0}.", "Ge\u00e7ersiz ak\u0131m uzunlu\u011fu {0}.");
        table2.put("The JVM claims not to support the {0} encoding.", "JVM, {0} dil kodlamas\u0131n\u0131 desteklememektedir.");
        table2.put("Unknown type {0}.", "Bilinmeyen tip {0}.");
        table2.put("Cannot cast an instance of {0} to type {1}", "{0} tipi {1} tipine d\u00f6n\u00fc\u015ft\u00fcr\u00fclemiyor");
        table2.put("Unsupported Types value: {0}", "Ge\u00e7ersiz Types de\u011feri: {0}");
        table2.put("Can''t infer the SQL type to use for an instance of {0}. Use setObject() with an explicit Types value to specify the type to use.", "{0}''nin \u00f6rne\u011fi ile kullan\u0131lacak SQL tip bulunamad\u0131. Kullan\u0131lacak tip belirtmek i\u00e7in kesin Types de\u011ferleri ile setObject() kullan\u0131n.");
        table2.put("This statement does not declare an OUT parameter.  Use '{' ?= call ... '}' to declare one.", "Bu komut OUT parametresi bildirmemektedir.  Bildirmek i\u00e7in '{' ?= call ... '}' kullan\u0131n.");
        table2.put("wasNull cannot be call before fetching a result.", "wasNull sonu\u00e7 \u00e7ekmeden \u00f6nce \u00e7a\u011f\u0131r\u0131lamaz.");
        table2.put("Malformed function or procedure escape syntax at offset {0}.", "{0} adresinde fonksiyon veya yordamda ka\u00e7\u0131\u015f s\u00f6z dizimi ge\u00e7ersiz.");
        table2.put("Parameter of type {0} was registered, but call to get{1} (sqltype={2}) was made.", "{0} tipinde parametre tan\u0131t\u0131ld\u0131, ancak {1} (sqltype={2}) tipinde geri getirmek i\u00e7in \u00e7a\u011fr\u0131 yap\u0131ld\u0131.");
        table2.put("A CallableStatement was declared, but no call to registerOutParameter(1, <some type>) was made.", "CallableStatement bildirildi ancak registerOutParameter(1, < bir tip>) tan\u0131t\u0131m\u0131 yap\u0131lmad\u0131.");
        table2.put("No function outputs were registered.", "Hi\u00e7bir fonksiyon \u00e7\u0131kt\u0131s\u0131 kaydedilmedi.");
        table2.put("Results cannot be retrieved from a CallableStatement before it is executed.", "CallableStatement \u00e7al\u0131\u015ft\u0131r\u0131lmadan sonu\u00e7lar ondan al\u0131namaz.");
        table2.put("This statement has been closed.", "Bu komut kapat\u0131ld\u0131.");
        table2.put("Too many update results were returned.", "\u00c7ok fazla g\u00fcncelleme sonucu d\u00f6nd\u00fcr\u00fcld\u00fc.");
        table2.put("Batch entry {0} {1} was aborted.  Call getNextException to see the cause.", "Tpil\u0131 i\u015f giri\u015fi {0} {1} durduruldu.  Nedenini g\u00f6rmek i\u00e7in getNextException fonksiyonu \u00e7a\u011f\u0131r\u0131n.");
        table2.put("Unexpected error writing large object to database.", "Large object veritaban\u0131na yaz\u0131l\u0131rken beklenmeyan hata.");
        table2.put("{0} function takes one and only one argument.", "{0} fonksiyonunu yaln\u0131z tek bir parametre alabilir.");
        table2.put("{0} function takes two and only two arguments.", "{0} fonksiyonunu sadece iki parametre alabilir.");
        table2.put("{0} function takes four and only four argument.", "{0} fonksiyonunu yaln\u0131z d\u00f6rt parametre alabilir.");
        table2.put("{0} function takes two or three arguments.", "{0} fonksiyonu yaln\u0131z iki veya \u00fc\u00e7 arg\u00fcman alabilir.");
        table2.put("{0} function doesn''t take any argument.", "{0} fonksiyonu parametre almaz.");
        table2.put("{0} function takes three and only three arguments.", "{0} fonksiyonunu sadece \u00fc\u00e7 parametre alabilir.");
        table2.put("Interval {0} not yet implemented", "{0} aral\u0131\u011f\u0131 hen\u00fcz kodlanmad\u0131.");
        table2.put("Infinite value found for timestamp/date. This cannot be represented as time.", "Timestamp veri tipinde sonsuz de\u011fer bulunmu\u015ftur. Buna uygun bir g\u00f6sterim yoktur.");
        table2.put("The class {0} does not implement org.postgresql.util.PGobject.", "{0} s\u0131n\u0131f\u0131 org.postgresql.util.PGobject implemente etmiyor.");
        table2.put("Unknown ResultSet holdability setting: {0}.", "ResultSet tutabilme ayar\u0131 ge\u00e7ersiz: {0}.");
        table2.put("Server versions prior to 8.0 do not support savepoints.", "Sunucunun 8.0''dan  \u00f6nceki s\u00fcr\u00fcmler savepoint desteklememektedir.");
        table2.put("Cannot establish a savepoint in auto-commit mode.", "Auto-commit bi\u00e7imde savepoint olu\u015fturulam\u0131yor.");
        table2.put("Returning autogenerated keys is not supported.", "Otomatik \u00fcretilen de\u011ferlerin getirilmesi desteklenememktedir.");
        table2.put("The parameter index is out of range: {0}, number of parameters: {1}.", "Dizin g\u00f6stergisi kapsam d\u0131\u015f\u0131d\u0131r: {0}, \u00f6\u011fe say\u0131s\u0131: {1}.");
        table2.put("Returning autogenerated keys is only supported for 8.2 and later servers.", "Otomatik \u00fcretilen anahtarlar\u0131n d\u00f6nd\u00fcr\u00fclmesi sadece 8.2 ve \u00fczerindeki s\u00fcr\u00fcmlerdeki sunucularda desteklenmektedir.");
        table2.put("Returning autogenerated keys by column index is not supported.", "Kolonlar\u0131n indexlenmesi ile otomatik olarak olu\u015fturulan anahtarlar\u0131n d\u00f6nd\u00fcr\u00fclmesi desteklenmiyor.");
        table2.put("Cannot reference a savepoint after it has been released.", "B\u0131rak\u0131ld\u0131ktan sonra savepoint referans edilemez.");
        table2.put("Cannot retrieve the id of a named savepoint.", "Adland\u0131r\u0131lm\u0131\u015f savepointin id de\u011ferine eri\u015filemiyor.");
        table2.put("Cannot retrieve the name of an unnamed savepoint.", "Ad\u0131 verilmemi\u015f savepointin id de\u011ferine eri\u015filemiyor.");
        table2.put("Invalid UUID data.", "Ge\u00e7ersiz UUID verisi.");
        table2.put("Unable to find server array type for provided name {0}.", "Belirtilen {0} ad\u0131 i\u00e7in sunucu array tipi bulunamad\u0131.");
        table2.put("ClientInfo property not supported.", "Clientinfo property'si desteklenememktedir.");
        table2.put("Unable to decode xml data.", "XML verisinin kodu \u00e7\u00f6z\u00fclemedi.");
        table2.put("Unknown XML Source class: {0}", "Bilinmeyen XML Kaynak S\u0131n\u0131f\u0131: {0}");
        table2.put("Unable to create SAXResult for SQLXML.", "SQLXML i\u00e7in SAXResult yarat\u0131lamad\u0131.");
        table2.put("Unable to create StAXResult for SQLXML", "SQLXML i\u00e7in StAXResult yarat\u0131lamad\u0131");
        table2.put("Unknown XML Result class: {0}", "Bilinmeyen XML Sonu\u00e7 s\u0131n\u0131f\u0131: {0}.");
        table2.put("This SQLXML object has already been freed.", "Bu SQLXML nesnesi zaten bo\u015falt\u0131lm\u0131\u015f.");
        table2.put("This SQLXML object has not been initialized, so you cannot retrieve data from it.", "Bu SQLXML nesnesi ilklendirilmemi\u015f; o y\u00fczden ondan veri alamazs\u0131n\u0131z.");
        table2.put("Failed to convert binary xml data to encoding: {0}.", "xml verisinin \u015fu dil kodlamas\u0131na \u00e7evirilmesi ba\u015far\u0131s\u0131z oldu: {0}");
        table2.put("Unable to convert DOMResult SQLXML data to a string.", "DOMResult SQLXML verisini diziye d\u00f6n\u00fc\u015ft\u00fcr\u00fclemedi.");
        table2.put("This SQLXML object has already been initialized, so you cannot manipulate it further.", "Bu SQLXML nesnesi daha \u00f6nceden ilklendirilmi\u015ftir; o y\u00fczden daha fazla m\u00fcdahale edilemez.");
        table2.put("Failed to initialize LargeObject API", "LArgeObject API ilklendirme hatas\u0131");
        table2.put("Large Objects may not be used in auto-commit mode.", "Auto-commit bi\u00e7imde large object kullan\u0131lamaz.");
        table2.put("The SSLSocketFactory class provided {0} could not be instantiated.", "SSLSocketFactory {0} ile \u00f6rneklenmedi.");
        table2.put("Conversion of interval failed", "Interval d\u00f6n\u00fc\u015ft\u00fcrmesi ba\u015far\u0131s\u0131z.");
        table2.put("Conversion of money failed.", "Money d\u00f6n\u00fc\u015ft\u00fcrmesi ba\u015far\u0131s\u0131z.");
        table2.put("Detail: {0}", "Ayr\u0131nt\u0131: {0}");
        table2.put("Hint: {0}", "\u0130pucu: {0}");
        table2.put("Position: {0}", "Position: {0}");
        table2.put("Where: {0}", "Where: {0}");
        table2.put("Internal Query: {0}", "Internal Query: {0}");
        table2.put("Internal Position: {0}", "Internal Position: {0}");
        table2.put("Location: File: {0}, Routine: {1}, Line: {2}", "Yer: Dosya: {0}, Yordam: {1}, Sat\u0131r: {2}");
        table2.put("Server SQLState: {0}", "Sunucu SQLState: {0}");
        table2.put("Invalid flags", "Ge\u00e7ersiz se\u00e7enekler");
        table2.put("xid must not be null", "xid null olamaz");
        table2.put("Connection is busy with another transaction", "Ba\u011flant\u0131, ba\u015fka bir transaction taraf\u0131ndan me\u015fgul ediliyor");
        table2.put("suspend/resume not implemented", "suspend/resume desteklenmiyor");
        table2.put("Transaction interleaving not implemented", "Transaction interleaving desteklenmiyor.");
        table2.put("Error disabling autocommit", "autocommit'i devre d\u0131\u015f\u0131 b\u0131rakma s\u0131ras\u0131nda hata");
        table2.put("tried to call end without corresponding start call", "start \u00e7a\u011f\u0131r\u0131m\u0131 olmadan end \u00e7a\u011f\u0131r\u0131lm\u0131\u015ft\u0131r");
        table2.put("Not implemented: Prepare must be issued using the same connection that started the transaction", "Desteklenmiyor: Prepare, transaction ba\u015flatran ba\u011flant\u0131 taraf\u0131ndan \u00e7a\u011f\u0131rmal\u0131d\u0131r");
        table2.put("Prepare called before end", "Sondan \u00f6nce prepare \u00e7a\u011f\u0131r\u0131lm\u0131\u015f");
        table2.put("Server versions prior to 8.1 do not support two-phase commit.", "Sunucunun 8.1''den  \u00f6nceki s\u00fcr\u00fcmler two-phase commit desteklememektedir.");
        table2.put("Error preparing transaction", "Transaction haz\u0131rlama hatas\u0131");
        table2.put("Invalid flag", "Ge\u00e7ersiz se\u00e7enek");
        table2.put("Error during recover", "Kurtarma s\u0131ras\u0131nda hata");
        table2.put("Error rolling back prepared transaction", "Haz\u0131rlanm\u0131\u015f transaction rollback hatas\u0131");
        table2.put("Not implemented: one-phase commit must be issued using the same connection that was used to start it", "Desteklenmiyor: one-phase commit, i\u015flevinde ba\u015flatan ve bitiren ba\u011flant\u0131 ayn\u0131 olmal\u0131d\u0131r");
        table2.put("commit called before end", "commit, sondan \u00f6nce \u00e7a\u011f\u0131r\u0131ld\u0131");
        table2.put("Error during one-phase commit", "One-phase commit s\u0131ras\u0131nda hata");
        table2.put("Not implemented: 2nd phase commit must be issued using an idle connection", "Desteklenmiyor: 2nd phase commit, at\u0131l bir ba\u011flant\u0131dan ba\u015flat\u0131lmal\u0131d\u0131r");
        table2.put("Heuristic commit/rollback not supported", "Heuristic commit/rollback desteklenmiyor");
        table = table2;
    }
}
