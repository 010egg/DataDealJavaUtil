// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.translation;

import java.util.Enumeration;
import java.util.MissingResourceException;
import java.util.Hashtable;
import java.util.ResourceBundle;

public class messages_fr extends ResourceBundle
{
    private static final Hashtable table;
    
    public Object handleGetObject(final String key) throws MissingResourceException {
        return messages_fr.table.get(key);
    }
    
    public Enumeration getKeys() {
        return messages_fr.table.keys();
    }
    
    public ResourceBundle getParent() {
        return super.parent;
    }
    
    static {
        final Hashtable<String, String> table2 = new Hashtable<String, String>();
        table2.put("", "Project-Id-Version: head-fr\nReport-Msgid-Bugs-To: \nPOT-Creation-Date: 2012-09-10 19:32-0400\nPO-Revision-Date: 2007-07-27 12:27+0200\nLast-Translator: \nLanguage-Team:  <en@li.org>\nLanguage: \nMIME-Version: 1.0\nContent-Type: text/plain; charset=UTF-8\nContent-Transfer-Encoding: 8bit\nX-Generator: KBabel 1.11.4\nPlural-Forms:  nplurals=2; plural=(n > 1);\n");
        table2.put("Error loading default settings from driverconfig.properties", "Erreur de chargement des valeurs par d\u00e9faut depuis driverconfig.properties");
        table2.put("Something unusual has occured to cause the driver to fail. Please report this exception.", "Quelque chose d''inhabituel a provoqu\u00e9 l''\u00e9chec du pilote. Veuillez faire un rapport sur cette erreur.");
        table2.put("Connection attempt timed out.", "La tentative de connexion a \u00e9chou\u00e9 dans le d\u00e9lai imparti.");
        table2.put("Interrupted while attempting to connect.", "Interrompu pendant l''\u00e9tablissement de la connexion.");
        table2.put("Method {0} is not yet implemented.", "La fonction {0} n''est pas encore impl\u00e9ment\u00e9e.");
        table2.put("A connection could not be made using the requested protocol {0}.", "Aucune connexion n''a pu \u00eatre \u00e9tablie en utilisant le protocole demand\u00e9 {0}. ");
        table2.put("Premature end of input stream, expected {0} bytes, but only read {1}.", "Fin pr\u00e9matur\u00e9e du flux en entr\u00e9e, {0} octets attendus, mais seulement {1} lus.");
        table2.put("Expected an EOF from server, got: {0}", "Attendait une fin de fichier du serveur, re\u00e7u: {0}");
        table2.put("An unexpected result was returned by a query.", "Un r\u00e9sultat inattendu a \u00e9t\u00e9 retourn\u00e9 par une requ\u00eate.");
        table2.put("Illegal UTF-8 sequence: byte {0} of {1} byte sequence is not 10xxxxxx: {2}", "S\u00e9quence UTF-8 ill\u00e9gale: l''octet {0} de la s\u00e9quence d''octet {1} n''est pas 10xxxxxx: {2}");
        table2.put("Illegal UTF-8 sequence: {0} bytes used to encode a {1} byte value: {2}", "S\u00e9quence UTF-8 ill\u00e9gale: {0} octets utilis\u00e9 pour encoder une valeur \u00e0 {1} octets: {2}");
        table2.put("Illegal UTF-8 sequence: initial byte is {0}: {1}", "S\u00e9quence UTF-8 ill\u00e9gale: le premier octet est {0}: {1}");
        table2.put("Illegal UTF-8 sequence: final value is out of range: {0}", "S\u00e9quence UTF-8 ill\u00e9gale: la valeur finale est en dehors des limites: {0}");
        table2.put("Illegal UTF-8 sequence: final value is a surrogate value: {0}", "S\u00e9quence UTF-8 ill\u00e9gale: la valeur finale est une valeur de remplacement: {0}");
        table2.put("Zero bytes may not occur in string parameters.", "Z\u00e9ro octets ne devrait pas se produire dans les param\u00e8tres de type cha\u00eene de caract\u00e8res.");
        table2.put("Zero bytes may not occur in identifiers.", "Des octects \u00e0 0 ne devraient pas appara\u00eetre dans les identifiants.");
        table2.put("Cannot convert an instance of {0} to type {1}", "Impossible de convertir une instance de type {0} vers le type {1}");
        table2.put("Connection refused. Check that the hostname and port are correct and that the postmaster is accepting TCP/IP connections.", "Connexion refus\u00e9e. V\u00e9rifiez que le nom de machine et le port sont corrects et que postmaster accepte les connexions TCP/IP.");
        table2.put("The connection attempt failed.", "La tentative de connexion a \u00e9chou\u00e9.");
        table2.put("The server does not support SSL.", "Le serveur ne supporte pas SSL.");
        table2.put("An error occured while setting up the SSL connection.", "Une erreur s''est produite pendant l''\u00e9tablissement de la connexion SSL.");
        table2.put("Connection rejected: {0}.", "Connexion rejet\u00e9e : {0}.");
        table2.put("The server requested password-based authentication, but no password was provided.", "Le serveur a demand\u00e9 une authentification par mots de passe, mais aucun mot de passe n''a \u00e9t\u00e9 fourni.");
        table2.put("The authentication type {0} is not supported. Check that you have configured the pg_hba.conf file to include the client''s IP address or subnet, and that it is using an authentication scheme supported by the driver.", "Le type d''authentification {0} n''est pas support\u00e9. V\u00e9rifiez que vous avez configur\u00e9 le fichier pg_hba.conf pour inclure l''adresse IP du client ou le sous-r\u00e9seau et qu''il utilise un sch\u00e9ma d''authentification support\u00e9 par le pilote.");
        table2.put("Protocol error.  Session setup failed.", "Erreur de protocole. Ouverture de la session en \u00e9chec.");
        table2.put("Backend start-up failed: {0}.", "D\u00e9marrage du serveur en \u00e9chec : {0}.");
        table2.put("The column index is out of range: {0}, number of columns: {1}.", "L''indice de la colonne est hors limite : {0}, nombre de colonnes : {1}.");
        table2.put("No value specified for parameter {0}.", "Pas de valeur sp\u00e9cifi\u00e9e pour le param\u00e8tre {0}.");
        table2.put("Expected command status BEGIN, got {0}.", "Attendait le statut de commande BEGIN, obtenu {0}.");
        table2.put("Unexpected command status: {0}.", "Statut de commande inattendu : {0}.");
        table2.put("An I/O error occured while sending to the backend.", "Une erreur d''entr\u00e9e/sortie a eu lieu lors d''envoi vers le serveur.");
        table2.put("Unknown Response Type {0}.", "Type de r\u00e9ponse inconnu {0}.");
        table2.put("Ran out of memory retrieving query results.", "Ai manqu\u00e9 de m\u00e9moire en r\u00e9cup\u00e9rant les r\u00e9sultats de la requ\u00eate.");
        table2.put("Unable to interpret the update count in command completion tag: {0}.", "Incapable d''interpr\u00e9ter le nombre de mise \u00e0 jour dans la balise de compl\u00e9tion de commande : {0}.");
        table2.put("Unable to bind parameter values for statement.", "Incapable de lier les valeurs des param\u00e8tres pour la commande.");
        table2.put("Bind message length {0} too long.  This can be caused by very large or incorrect length specifications on InputStream parameters.", "La longueur du message de liaison {0} est trop grande. Cela peut \u00eatre caus\u00e9 par des sp\u00e9cification de longueur tr\u00e8s grandes ou incorrectes pour les param\u00e8tres de type InputStream.");
        table2.put("The server''s DateStyle parameter was changed to {0}. The JDBC driver requires DateStyle to begin with ISO for correct operation.", "Le param\u00e8tre DateStyle du serveur a \u00e9t\u00e9 chang\u00e9 pour {0}. Le pilote JDBC n\u00e9cessite que DateStyle commence par ISO pour un fonctionnement correct.");
        table2.put("The server''s standard_conforming_strings parameter was reported as {0}. The JDBC driver expected on or off.", "Le param\u00e8tre serveur standard_conforming_strings a pour valeur {0}. Le driver JDBC attend on ou off.");
        table2.put("The driver currently does not support COPY operations.", "Le pilote ne supporte pas actuellement les op\u00e9rations COPY.");
        table2.put("This PooledConnection has already been closed.", "Cette PooledConnection a d\u00e9j\u00e0 \u00e9t\u00e9 ferm\u00e9e.");
        table2.put("Connection has been closed automatically because a new connection was opened for the same PooledConnection or the PooledConnection has been closed.", "La connexion a \u00e9t\u00e9 ferm\u00e9e automatiquement car une nouvelle connexion a \u00e9t\u00e9 ouverte pour la m\u00eame PooledConnection ou la PooledConnection a \u00e9t\u00e9 ferm\u00e9e.");
        table2.put("Connection has been closed.", "La connexion a \u00e9t\u00e9 ferm\u00e9e.");
        table2.put("Statement has been closed.", "Statement a \u00e9t\u00e9 ferm\u00e9.");
        table2.put("DataSource has been closed.", "DataSource a \u00e9t\u00e9 ferm\u00e9e.");
        table2.put("Fastpath call {0} - No result was returned and we expected an integer.", "Appel Fastpath {0} - Aucun r\u00e9sultat n''a \u00e9t\u00e9 retourn\u00e9 et nous attendions un entier.");
        table2.put("The fastpath function {0} is unknown.", "La fonction fastpath {0} est inconnue.");
        table2.put("Conversion to type {0} failed: {1}.", "La conversion vers le type {0} a \u00e9chou\u00e9 : {1}.");
        table2.put("Cannot tell if path is open or closed: {0}.", "Impossible de dire si path est ferm\u00e9 ou ouvert : {0}.");
        table2.put("The array index is out of range: {0}", "L''indice du tableau est hors limites : {0}");
        table2.put("The array index is out of range: {0}, number of elements: {1}.", "L''indice du tableau est hors limites : {0}, nombre d''\u00e9l\u00e9ments : {1}.");
        table2.put("Invalid character data was found.  This is most likely caused by stored data containing characters that are invalid for the character set the database was created in.  The most common example of this is storing 8bit data in a SQL_ASCII database.", "Des donn\u00e9es de caract\u00e8res invalides ont \u00e9t\u00e9 trouv\u00e9es. C''est probablement caus\u00e9 par le stockage de caract\u00e8res invalides pour le jeu de caract\u00e8res de cr\u00e9ation de la base. L''exemple le plus courant est le stockage de donn\u00e9es 8bit dans une base SQL_ASCII.");
        table2.put("Truncation of large objects is only implemented in 8.3 and later servers.", "Le troncage des large objects n''est impl\u00e9ment\u00e9 que dans les serveurs 8.3 et sup\u00e9rieurs.");
        table2.put("PostgreSQL LOBs can only index to: {0}", "Les LOB PostgreSQL peuvent seulement s''indicer \u00e0: {0}");
        table2.put("LOB positioning offsets start at 1.", "Les d\u00e9calages de position des LOB commencent \u00e0 1.");
        table2.put("free() was called on this LOB previously", "free() a \u00e9t\u00e9 appel\u00e9e auparavant sur ce LOB");
        table2.put("Unsupported value for stringtype parameter: {0}", "Valeur non support\u00e9e pour les param\u00e8tre de type cha\u00eene de caract\u00e8res : {0}");
        table2.put("No results were returned by the query.", "Aucun r\u00e9sultat retourn\u00e9 par la requ\u00eate.");
        table2.put("A result was returned when none was expected.", "Un r\u00e9sultat a \u00e9t\u00e9 retourn\u00e9 alors qu''aucun n''\u00e9tait attendu.");
        table2.put("Failed to create object for: {0}.", "\u00c9chec \u00e0 la cr\u00e9ation de l''objet pour : {0}.");
        table2.put("Unable to load the class {0} responsible for the datatype {1}", "Incapable de charger la classe {0} responsable du type de donn\u00e9es {1}");
        table2.put("Cannot change transaction read-only property in the middle of a transaction.", "Impossible de changer la propri\u00e9t\u00e9 read-only d''une transaction au milieu d''une transaction.");
        table2.put("Cannot change transaction isolation level in the middle of a transaction.", "Impossible de changer le niveau d''isolation des transactions au milieu d''une transaction.");
        table2.put("Transaction isolation level {0} not supported.", "Le niveau d''isolation de transaction {0} n''est pas support\u00e9.");
        table2.put("Finalizing a Connection that was never closed:", "Destruction d''une connection qui n''a jamais \u00e9t\u00e9 ferm\u00e9e:");
        table2.put("Unable to translate data into the desired encoding.", "Impossible de traduire les donn\u00e9es dans l''encodage d\u00e9sir\u00e9.");
        table2.put("Unable to determine a value for MaxIndexKeys due to missing system catalog data.", "Incapable de d\u00e9terminer la valeur de MaxIndexKeys en raison de donn\u00e9es manquante dans lecatalogue syst\u00e8me.");
        table2.put("Unable to find name datatype in the system catalogs.", "Incapable de trouver le type de donn\u00e9e name dans les catalogues syst\u00e8mes.");
        table2.put("Operation requires a scrollable ResultSet, but this ResultSet is FORWARD_ONLY.", "L''op\u00e9ration n\u00e9cessite un scrollable ResultSet, mais ce ResultSet est FORWARD_ONLY.");
        table2.put("Unexpected error while decoding character data from a large object.", "Erreur inattendue pendant le d\u00e9codage des donn\u00e9es caract\u00e8res pour un large object.");
        table2.put("Can''t use relative move methods while on the insert row.", "Impossible d''utiliser les fonctions de d\u00e9placement relatif pendant l''insertion d''une ligne.");
        table2.put("Invalid fetch direction constant: {0}.", "Constante de direction pour la r\u00e9cup\u00e9ration invalide : {0}.");
        table2.put("Cannot call cancelRowUpdates() when on the insert row.", "Impossible d''appeler cancelRowUpdates() pendant l''insertion d''une ligne.");
        table2.put("Cannot call deleteRow() when on the insert row.", "Impossible d''appeler deleteRow() pendant l''insertion d''une ligne.");
        table2.put("Currently positioned before the start of the ResultSet.  You cannot call deleteRow() here.", "Actuellement positionn\u00e9 avant le d\u00e9but du ResultSet. Vous ne pouvez pas appeler deleteRow() ici.");
        table2.put("Currently positioned after the end of the ResultSet.  You cannot call deleteRow() here.", "Actuellement positionn\u00e9 apr\u00e8s la fin du ResultSet. Vous ne pouvez pas appeler deleteRow() ici.");
        table2.put("There are no rows in this ResultSet.", "Il n''y pas pas de lignes dans ce ResultSet.");
        table2.put("Not on the insert row.", "Pas sur la ligne en insertion.");
        table2.put("You must specify at least one column value to insert a row.", "Vous devez sp\u00e9cifier au moins une valeur de colonne pour ins\u00e9rer une ligne.");
        table2.put("The JVM claims not to support the encoding: {0}", "La JVM pr\u00e9tend ne pas supporter l''encodage: {0}");
        table2.put("Provided InputStream failed.", "L''InputStream fourni a \u00e9chou\u00e9.");
        table2.put("Provided Reader failed.", "Le Reader fourni a \u00e9chou\u00e9.");
        table2.put("Can''t refresh the insert row.", "Impossible de rafra\u00eechir la ligne ins\u00e9r\u00e9e.");
        table2.put("Cannot call updateRow() when on the insert row.", "Impossible d''appeler updateRow() tant que l''on est sur la ligne ins\u00e9r\u00e9e.");
        table2.put("Cannot update the ResultSet because it is either before the start or after the end of the results.", "Impossible de mettre \u00e0 jour le ResultSet car c''est soit avant le d\u00e9but ou apr\u00e8s la fin des r\u00e9sultats.");
        table2.put("ResultSets with concurrency CONCUR_READ_ONLY cannot be updated.", "Les ResultSets avec la concurrence CONCUR_READ_ONLY ne peuvent \u00eatre mis \u00e0 jour.");
        table2.put("No primary key found for table {0}.", "Pas de cl\u00e9 primaire trouv\u00e9e pour la table {0}.");
        table2.put("Fetch size must be a value greater to or equal to 0.", "Fetch size doit \u00eatre une valeur sup\u00e9rieur ou \u00e9gal \u00e0 0.");
        table2.put("Bad value for type {0} : {1}", "Mauvaise valeur pour le type {0} : {1}");
        table2.put("The column name {0} was not found in this ResultSet.", "Le nom de colonne {0} n''a pas \u00e9t\u00e9 trouv\u00e9 dans ce ResultSet.");
        table2.put("ResultSet is not updateable.  The query that generated this result set must select only one table, and must select all primary keys from that table. See the JDBC 2.1 API Specification, section 5.6 for more details.", "Le ResultSet n''est pas modifiable. La requ\u00eate qui a g\u00e9n\u00e9r\u00e9 ce r\u00e9sultat doit s\u00e9lectionner seulement une table, et doit s\u00e9lectionner toutes les cl\u00e9s primaires de cette table. Voir la sp\u00e9cification de l''API JDBC 2.1, section 5.6 pour plus de d\u00e9tails.");
        table2.put("This ResultSet is closed.", "Ce ResultSet est ferm\u00e9.");
        table2.put("ResultSet not positioned properly, perhaps you need to call next.", "Le ResultSet n''est pas positionn\u00e9 correctement, vous devez peut-\u00eatre appeler next().");
        table2.put("Can''t use query methods that take a query string on a PreparedStatement.", "Impossible d''utiliser les fonctions de requ\u00eate qui utilisent une cha\u00eene de caract\u00e8res sur un PreparedStatement.");
        table2.put("Multiple ResultSets were returned by the query.", "Plusieurs ResultSets ont \u00e9t\u00e9 retourn\u00e9s par la requ\u00eate.");
        table2.put("A CallableStatement was executed with nothing returned.", "Un CallableStatement a \u00e9t\u00e9 ex\u00e9cut\u00e9 mais n''a rien retourn\u00e9.");
        table2.put("A CallableStatement function was executed and the out parameter {0} was of type {1} however type {2} was registered.", "Une fonction CallableStatement a \u00e9t\u00e9 ex\u00e9cut\u00e9e et le param\u00e8tre en sortie {0} \u00e9tait du type {1} alors que le type {2} \u00e9tait pr\u00e9vu.");
        table2.put("Maximum number of rows must be a value grater than or equal to 0.", "Le nombre maximum de lignes doit \u00eatre une valeur sup\u00e9rieure ou \u00e9gale \u00e0 0.");
        table2.put("Query timeout must be a value greater than or equals to 0.", "Query timeout doit \u00eatre une valeur sup\u00e9rieure ou \u00e9gale \u00e0 0.");
        table2.put("The maximum field size must be a value greater than or equal to 0.", "La taille maximum des champs doit \u00eatre une valeur sup\u00e9rieure ou \u00e9gale \u00e0 0.");
        table2.put("Unknown Types value.", "Valeur de Types inconnue.");
        table2.put("Invalid stream length {0}.", "Longueur de flux invalide {0}.");
        table2.put("The JVM claims not to support the {0} encoding.", "La JVM pr\u00e9tend ne pas supporter l''encodage {0}.");
        table2.put("Unknown type {0}.", "Type inconnu : {0}.");
        table2.put("Cannot cast an instance of {0} to type {1}", "Impossible de convertir une instance de {0} vers le type {1}");
        table2.put("Unsupported Types value: {0}", "Valeur de type non support\u00e9e : {0}");
        table2.put("Can''t infer the SQL type to use for an instance of {0}. Use setObject() with an explicit Types value to specify the type to use.", "Impossible de d\u00e9duire le type SQL \u00e0 utiliser pour une instance de {0}. Utilisez setObject() avec une valeur de type explicite pour sp\u00e9cifier le type \u00e0 utiliser.");
        table2.put("This statement does not declare an OUT parameter.  Use '{' ?= call ... '}' to declare one.", "Cette requ\u00eate ne d\u00e9clare pas de param\u00e8tre OUT. Utilisez '{' ?= call ... '}' pour en d\u00e9clarer un.");
        table2.put("wasNull cannot be call before fetching a result.", "wasNull ne peut pas \u00eatre appel\u00e9 avant la r\u00e9cup\u00e9ration d''un r\u00e9sultat.");
        table2.put("Malformed function or procedure escape syntax at offset {0}.", "Syntaxe de fonction ou d''\u00e9chappement de proc\u00e9dure malform\u00e9e \u00e0 l''indice {0}.");
        table2.put("Parameter of type {0} was registered, but call to get{1} (sqltype={2}) was made.", "Un param\u00e8tre de type {0} a \u00e9t\u00e9 enregistr\u00e9, mais un appel \u00e0 get{1} (sqltype={2}) a \u00e9t\u00e9 fait.");
        table2.put("A CallableStatement was declared, but no call to registerOutParameter(1, <some type>) was made.", "Un CallableStatement a \u00e9t\u00e9 d\u00e9clar\u00e9, mais aucun appel \u00e0 registerOutParameter(1, <un type>) n''a \u00e9t\u00e9 fait.");
        table2.put("No function outputs were registered.", "Aucune fonction outputs n''a \u00e9t\u00e9 enregistr\u00e9e.");
        table2.put("Results cannot be retrieved from a CallableStatement before it is executed.", "Les r\u00e9sultats ne peuvent \u00eatre r\u00e9cup\u00e9r\u00e9s \u00e0 partir d''un CallableStatement avant qu''il ne soit ex\u00e9cut\u00e9.");
        table2.put("This statement has been closed.", "Ce statement a \u00e9t\u00e9 ferm\u00e9.");
        table2.put("Too many update results were returned.", "Trop de r\u00e9sultats de mise \u00e0 jour ont \u00e9t\u00e9 retourn\u00e9s.");
        table2.put("Batch entry {0} {1} was aborted.  Call getNextException to see the cause.", "L''\u00e9l\u00e9ment du batch {0} {1} a \u00e9t\u00e9 annul\u00e9. Appeler getNextException pour en conna\u00eetre la cause.");
        table2.put("Unexpected error writing large object to database.", "Erreur inattendue pendant l''\u00e9criture de large object dans la base.");
        table2.put("{0} function takes one and only one argument.", "La fonction {0} n''accepte qu''un et un seul argument.");
        table2.put("{0} function takes two and only two arguments.", "La fonction {0} n''accepte que deux et seulement deux arguments.");
        table2.put("{0} function takes four and only four argument.", "La fonction {0} n''accepte que quatre et seulement quatre arguments.");
        table2.put("{0} function takes two or three arguments.", "La fonction {0} n''accepte que deux ou trois arguments.");
        table2.put("{0} function doesn''t take any argument.", "La fonction {0} n''accepte aucun argument.");
        table2.put("{0} function takes three and only three arguments.", "La fonction {0} n''accepte que trois et seulement trois arguments.");
        table2.put("Interval {0} not yet implemented", "L''interval {0} n''est pas encore impl\u00e9ment\u00e9");
        table2.put("Infinite value found for timestamp/date. This cannot be represented as time.", "Valeur infinie trouv\u00e9e pour une date/timestamp. Cette valeur ne peut \u00eatre repr\u00e9sent\u00e9 comme une valeur temporelle.");
        table2.put("The class {0} does not implement org.postgresql.util.PGobject.", "La classe {0} n''impl\u00e9mente pas org.postgresql.util.PGobject.");
        table2.put("Unknown ResultSet holdability setting: {0}.", "Param\u00e8tre holdability du ResultSet inconnu : {0}.");
        table2.put("Server versions prior to 8.0 do not support savepoints.", "Les serveurs de version ant\u00e9rieure \u00e0 8.0 ne supportent pas les savepoints.");
        table2.put("Cannot establish a savepoint in auto-commit mode.", "Impossible d''\u00e9tablir un savepoint en mode auto-commit.");
        table2.put("Returning autogenerated keys is not supported.", "Le renvoi des cl\u00e9s automatiquement g\u00e9n\u00e9r\u00e9es n''est pas support\u00e9.");
        table2.put("The parameter index is out of range: {0}, number of parameters: {1}.", "L''indice du param\u00e8tre est hors limites : {0}, nombre de param\u00e8tres : {1}.");
        table2.put("Cannot reference a savepoint after it has been released.", "Impossible de r\u00e9f\u00e9rencer un savepoint apr\u00e8s qu''il ait \u00e9t\u00e9 lib\u00e9r\u00e9.");
        table2.put("Cannot retrieve the id of a named savepoint.", "Impossible de retrouver l''identifiant d''un savepoint nomm\u00e9.");
        table2.put("Cannot retrieve the name of an unnamed savepoint.", "Impossible de retrouver le nom d''un savepoint sans nom.");
        table2.put("Failed to initialize LargeObject API", "\u00c9chec \u00e0 l''initialisation de l''API LargeObject");
        table2.put("Large Objects may not be used in auto-commit mode.", "Les Large Objects ne devraient pas \u00eatre utilis\u00e9s en mode auto-commit.");
        table2.put("The SSLSocketFactory class provided {0} could not be instantiated.", "La classe SSLSocketFactory fournie {0} n''a pas pu \u00eatre instanci\u00e9e.");
        table2.put("Conversion of interval failed", "La conversion de l''intervalle a \u00e9chou\u00e9");
        table2.put("Conversion of money failed.", "La conversion de money a \u00e9chou\u00e9.");
        table2.put("Detail: {0}", "D\u00e9tail : {0}");
        table2.put("Hint: {0}", "Indice : {0}");
        table2.put("Position: {0}", "Position : {0}");
        table2.put("Where: {0}", "O\u00f9 : {0}");
        table2.put("Internal Query: {0}", "Requ\u00eate interne: {0}");
        table2.put("Internal Position: {0}", "Position interne : {0}");
        table2.put("Location: File: {0}, Routine: {1}, Line: {2}", "Localisation : Fichier : {0}, Routine : {1}, Ligne : {2}");
        table2.put("Server SQLState: {0}", "SQLState serveur : {0}");
        table2.put("Invalid flags", "Drapeaux invalides");
        table2.put("xid must not be null", "xid ne doit pas \u00eatre nul");
        table2.put("Connection is busy with another transaction", "La connection est occup\u00e9e avec une autre transaction");
        table2.put("suspend/resume not implemented", "suspend/resume pas impl\u00e9ment\u00e9");
        table2.put("Transaction interleaving not implemented", "L''entrelacement des transactions n''est pas impl\u00e9ment\u00e9");
        table2.put("Error disabling autocommit", "Erreur en d\u00e9sactivant autocommit");
        table2.put("tried to call end without corresponding start call", "tentative d''appel de fin sans l''appel start correspondant");
        table2.put("Not implemented: Prepare must be issued using the same connection that started the transaction", "Pas impl\u00e9ment\u00e9: Prepare doit \u00eatre envoy\u00e9 sur la m\u00eame connection qui a d\u00e9marr\u00e9 la transaction");
        table2.put("Prepare called before end", "Pr\u00e9paration appel\u00e9e avant la fin");
        table2.put("Server versions prior to 8.1 do not support two-phase commit.", "Les serveurs de versions ant\u00e9rieures \u00e0 8.1 ne supportent pas le commit \u00e0 deux phases.");
        table2.put("Error preparing transaction", "Erreur en pr\u00e9parant la transaction");
        table2.put("Invalid flag", "Drapeau invalide");
        table2.put("Error during recover", "Erreur durant la restauration");
        table2.put("Error rolling back prepared transaction", "Erreur en annulant une transaction pr\u00e9par\u00e9e");
        table2.put("Not implemented: one-phase commit must be issued using the same connection that was used to start it", "Pas impl\u00e9ment\u00e9: le commit \u00e0 une phase doit avoir lieu en utilisant la m\u00eame connection que celle o\u00f9 il a commenc\u00e9");
        table2.put("commit called before end", "Commit appel\u00e9 avant la fin");
        table2.put("Error during one-phase commit", "Erreur pendant le commit \u00e0 une phase");
        table2.put("Not implemented: 2nd phase commit must be issued using an idle connection", "Pas impl\u00e9ment\u00e9: le commit \u00e0 deux phase doit \u00eatre envoy\u00e9 sur une connection inutilis\u00e9e");
        table2.put("Heuristic commit/rollback not supported", "Heuristic commit/rollback non support\u00e9");
        table = table2;
    }
}
