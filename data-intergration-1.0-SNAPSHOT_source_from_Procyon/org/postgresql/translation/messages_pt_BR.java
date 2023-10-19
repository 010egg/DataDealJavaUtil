// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.translation;

import java.util.Enumeration;
import java.util.MissingResourceException;
import java.util.Hashtable;
import java.util.ResourceBundle;

public class messages_pt_BR extends ResourceBundle
{
    private static final Hashtable table;
    
    public Object handleGetObject(final String key) throws MissingResourceException {
        return messages_pt_BR.table.get(key);
    }
    
    public Enumeration getKeys() {
        return messages_pt_BR.table.keys();
    }
    
    public ResourceBundle getParent() {
        return super.parent;
    }
    
    static {
        final Hashtable<String, String> table2 = new Hashtable<String, String>();
        table2.put("", "Project-Id-Version: PostgreSQL 8.4\nReport-Msgid-Bugs-To: \nPOT-Creation-Date: 2012-10-17 14:17-0400\nPO-Revision-Date: 2004-10-31 20:48-0300\nLast-Translator: Euler Taveira de Oliveira <euler@timbira.com>\nLanguage-Team: Brazilian Portuguese <pgbr-dev@listas.postgresql.org.br>\nLanguage: pt_BR\nMIME-Version: 1.0\nContent-Type: text/plain; charset=UTF-8\nContent-Transfer-Encoding: 8bit\n");
        table2.put("Error loading default settings from driverconfig.properties", "Erro ao carregar configura\u00e7\u00f5es padr\u00e3o do driverconfig.properties");
        table2.put("Your security policy has prevented the connection from being attempted.  You probably need to grant the connect java.net.SocketPermission to the database server host and port that you wish to connect to.", "Sua pol\u00edtica de seguran\u00e7a impediu que a conex\u00e3o pudesse ser estabelecida. Voc\u00ea provavelmente precisa conceder permiss\u00e3o em java.net.SocketPermission para a m\u00e1quina e a porta do servidor de banco de dados que voc\u00ea deseja se conectar.");
        table2.put("Something unusual has occured to cause the driver to fail. Please report this exception.", "Alguma coisa n\u00e3o usual ocorreu para causar a falha do driver. Por favor reporte esta exce\u00e7\u00e3o.");
        table2.put("Connection attempt timed out.", "Tentativa de conex\u00e3o falhou.");
        table2.put("Interrupted while attempting to connect.", "Interrompido ao tentar se conectar.");
        table2.put("Method {0} is not yet implemented.", "M\u00e9todo {0} ainda n\u00e3o foi implementado.");
        table2.put("A connection could not be made using the requested protocol {0}.", "A conex\u00e3o n\u00e3o pode ser feita usando protocolo informado {0}.");
        table2.put("Premature end of input stream, expected {0} bytes, but only read {1}.", "Fim de entrada prematuro, eram esperados {0} bytes, mas somente {1} foram lidos.");
        table2.put("Expected an EOF from server, got: {0}", "Esperado um EOF do servidor, recebido: {0}");
        table2.put("An unexpected result was returned by a query.", "Um resultado inesperado foi retornado pela consulta.");
        table2.put("Illegal UTF-8 sequence: byte {0} of {1} byte sequence is not 10xxxxxx: {2}", "Sequ\u00eancia UTF-8 ilegal: byte {0} da sequ\u00eancia de bytes {1} n\u00e3o \u00e9 10xxxxxx: {2}");
        table2.put("Illegal UTF-8 sequence: {0} bytes used to encode a {1} byte value: {2}", "Sequ\u00eancia UTF-8 ilegal: {0} bytes utilizados para codificar um valor de {1} bytes: {2}");
        table2.put("Illegal UTF-8 sequence: initial byte is {0}: {1}", "Sequ\u00eancia UTF-8 ilegal: byte inicial \u00e9 {0}: {1}");
        table2.put("Illegal UTF-8 sequence: final value is out of range: {0}", "Sequ\u00eancia UTF-8 ilegal: valor final est\u00e1 fora do intervalo: {0}");
        table2.put("Illegal UTF-8 sequence: final value is a surrogate value: {0}", "Sequ\u00eancia UTF-8 ilegal: valor final \u00e9 um valor suplementar: {0}");
        table2.put("Zero bytes may not occur in string parameters.", "Zero bytes n\u00e3o podem ocorrer em par\u00e2metros de cadeia de caracteres.");
        table2.put("Zero bytes may not occur in identifiers.", "Zero bytes n\u00e3o podem ocorrer em identificadores.");
        table2.put("Cannot convert an instance of {0} to type {1}", "N\u00e3o pode converter uma inst\u00e2ncia de {0} para tipo {1}");
        table2.put("Connection refused. Check that the hostname and port are correct and that the postmaster is accepting TCP/IP connections.", "Conex\u00e3o negada. Verifique se o nome da m\u00e1quina e a porta est\u00e3o corretos e se o postmaster est\u00e1 aceitando conex\u00f5es TCP/IP.");
        table2.put("The connection attempt failed.", "A tentativa de conex\u00e3o falhou.");
        table2.put("The server does not support SSL.", "O servidor n\u00e3o suporta SSL.");
        table2.put("An error occured while setting up the SSL connection.", "Um erro ocorreu ao estabelecer uma conex\u00e3o SSL.");
        table2.put("Connection rejected: {0}.", "Conex\u00e3o negada: {0}.");
        table2.put("The server requested password-based authentication, but no password was provided.", "O servidor pediu autentica\u00e7\u00e3o baseada em senha, mas nenhuma senha foi fornecida.");
        table2.put("The authentication type {0} is not supported. Check that you have configured the pg_hba.conf file to include the client''s IP address or subnet, and that it is using an authentication scheme supported by the driver.", "O tipo de autentica\u00e7\u00e3o {0} n\u00e3o \u00e9 suportado. Verifique se voc\u00ea configurou o arquivo pg_hba.conf incluindo a subrede ou endere\u00e7o IP do cliente, e se est\u00e1 utilizando o esquema de autentica\u00e7\u00e3o suportado pelo driver.");
        table2.put("Protocol error.  Session setup failed.", "Erro de Protocolo. Configura\u00e7\u00e3o da sess\u00e3o falhou.");
        table2.put("Backend start-up failed: {0}.", "Inicializa\u00e7\u00e3o do processo servidor falhou: {0}.");
        table2.put("The column index is out of range: {0}, number of columns: {1}.", "O \u00edndice da coluna est\u00e1 fora do intervalo: {0}, n\u00famero de colunas: {1}.");
        table2.put("No value specified for parameter {0}.", "Nenhum valor especificado para par\u00e2metro {0}.");
        table2.put("Expected command status BEGIN, got {0}.", "Status do comando BEGIN esperado, recebeu {0}.");
        table2.put("Unexpected command status: {0}.", "Status do comando inesperado: {0}.");
        table2.put("An I/O error occured while sending to the backend.", "Um erro de E/S ocorreu ao enviar para o processo servidor.");
        table2.put("Unknown Response Type {0}.", "Tipo de Resposta Desconhecido {0}.");
        table2.put("Ran out of memory retrieving query results.", "Mem\u00f3ria insuficiente ao recuperar resultados da consulta.");
        table2.put("Unable to interpret the update count in command completion tag: {0}.", "N\u00e3o foi poss\u00edvel interpretar o contador de atualiza\u00e7\u00e3o na marca\u00e7\u00e3o de comando completo: {0}.");
        table2.put("Unable to bind parameter values for statement.", "N\u00e3o foi poss\u00edvel ligar valores de par\u00e2metro ao comando.");
        table2.put("Bind message length {0} too long.  This can be caused by very large or incorrect length specifications on InputStream parameters.", "Tamanho de mensagem de liga\u00e7\u00e3o {0} \u00e9 muito longo. Isso pode ser causado por especifica\u00e7\u00f5es de tamanho incorretas ou muito grandes nos par\u00e2metros do InputStream.");
        table2.put("The server''s DateStyle parameter was changed to {0}. The JDBC driver requires DateStyle to begin with ISO for correct operation.", "O par\u00e2metro do servidor DateStyle foi alterado para {0}. O driver JDBC requer que o DateStyle come\u00e7e com ISO para opera\u00e7\u00e3o normal.");
        table2.put("The server''s standard_conforming_strings parameter was reported as {0}. The JDBC driver expected on or off.", "O par\u00e2metro do servidor standard_conforming_strings foi definido como {0}. O driver JDBC espera que seja on ou off.");
        table2.put("The driver currently does not support COPY operations.", "O driver atualmente n\u00e3o suporta opera\u00e7\u00f5es COPY.");
        table2.put("This PooledConnection has already been closed.", "Este PooledConnection j\u00e1 foi fechado.");
        table2.put("Connection has been closed automatically because a new connection was opened for the same PooledConnection or the PooledConnection has been closed.", "Conex\u00e3o foi fechada automaticamente porque uma nova conex\u00e3o foi aberta pelo mesmo PooledConnection ou o PooledConnection foi fechado.");
        table2.put("Connection has been closed.", "Conex\u00e3o foi fechada.");
        table2.put("Statement has been closed.", "Comando foi fechado.");
        table2.put("DataSource has been closed.", "DataSource foi fechado.");
        table2.put("Fastpath call {0} - No result was returned and we expected an integer.", "Chamada ao Fastpath {0} - Nenhum resultado foi retornado e n\u00f3s esper\u00e1vamos um inteiro.");
        table2.put("The fastpath function {0} is unknown.", "A fun\u00e7\u00e3o do fastpath {0} \u00e9 desconhecida.");
        table2.put("Conversion to type {0} failed: {1}.", "Convers\u00e3o para tipo {0} falhou: {1}.");
        table2.put("Cannot tell if path is open or closed: {0}.", "N\u00e3o pode dizer se caminho est\u00e1 aberto ou fechado: {0}.");
        table2.put("GSS Authentication failed", "Autentica\u00e7\u00e3o GSS falhou");
        table2.put("The array index is out of range: {0}", "O \u00edndice da matriz est\u00e1 fora do intervalo: {0}");
        table2.put("The array index is out of range: {0}, number of elements: {1}.", "O \u00edndice da matriz est\u00e1 fora do intervalo: {0}, n\u00famero de elementos: {1}.");
        table2.put("Invalid character data was found.  This is most likely caused by stored data containing characters that are invalid for the character set the database was created in.  The most common example of this is storing 8bit data in a SQL_ASCII database.", "Caracter inv\u00e1lido foi encontrado. Isso \u00e9 mais comumente causado por dado armazenado que cont\u00e9m caracteres que s\u00e3o inv\u00e1lidos para a codifica\u00e7\u00e3o que foi criado o banco de dados. O exemplo mais comum disso \u00e9 armazenar dados de 8 bits em um banco de dados SQL_ASCII.");
        table2.put("Truncation of large objects is only implemented in 8.3 and later servers.", "Truncar objetos grandes s\u00f3 \u00e9 implementado por servidores 8.3 ou superiores.");
        table2.put("PostgreSQL LOBs can only index to: {0}", "LOBs do PostgreSQL s\u00f3 podem indexar at\u00e9: {0}");
        table2.put("LOB positioning offsets start at 1.", "Deslocamentos da posi\u00e7\u00e3o de LOB come\u00e7am em 1.");
        table2.put("free() was called on this LOB previously", "free() j\u00e1 foi chamado neste LOB");
        table2.put("Unsupported value for stringtype parameter: {0}", "Valor do par\u00e2metro stringtype n\u00e3o \u00e9 suportado: {0}");
        table2.put("No results were returned by the query.", "Nenhum resultado foi retornado pela consulta.");
        table2.put("A result was returned when none was expected.", "Um resultado foi retornado quando nenhum era esperado.");
        table2.put("Custom type maps are not supported.", "Mapeamento de tipos personalizados n\u00e3o s\u00e3o suportados.");
        table2.put("Failed to create object for: {0}.", "Falhou ao criar objeto para: {0}.");
        table2.put("Unable to load the class {0} responsible for the datatype {1}", "N\u00e3o foi poss\u00edvel carregar a classe {0} respons\u00e1vel pelo tipo de dado {1}");
        table2.put("Cannot change transaction read-only property in the middle of a transaction.", "N\u00e3o pode mudar propriedade somente-leitura da transa\u00e7\u00e3o no meio de uma transa\u00e7\u00e3o.");
        table2.put("Cannot change transaction isolation level in the middle of a transaction.", "N\u00e3o pode mudar n\u00edvel de isolamento da transa\u00e7\u00e3o no meio de uma transa\u00e7\u00e3o.");
        table2.put("Transaction isolation level {0} not supported.", "N\u00edvel de isolamento da transa\u00e7\u00e3o {0} n\u00e3o \u00e9 suportado.");
        table2.put("Finalizing a Connection that was never closed:", "Fechando uma Conex\u00e3o que n\u00e3o foi fechada:");
        table2.put("Unable to translate data into the desired encoding.", "N\u00e3o foi poss\u00edvel traduzir dado para codifica\u00e7\u00e3o desejada.");
        table2.put("Unable to determine a value for MaxIndexKeys due to missing system catalog data.", "N\u00e3o foi poss\u00edvel determinar um valor para MaxIndexKeys por causa de falta de dados no cat\u00e1logo do sistema.");
        table2.put("Unable to find name datatype in the system catalogs.", "N\u00e3o foi poss\u00edvel encontrar tipo de dado name nos cat\u00e1logos do sistema.");
        table2.put("Operation requires a scrollable ResultSet, but this ResultSet is FORWARD_ONLY.", "Opera\u00e7\u00e3o requer um ResultSet rol\u00e1vel, mas este ResultSet \u00e9 FORWARD_ONLY (somente para frente).");
        table2.put("Unexpected error while decoding character data from a large object.", "Erro inesperado ao decodificar caracter de um objeto grande.");
        table2.put("Can''t use relative move methods while on the insert row.", "N\u00e3o pode utilizar m\u00e9todos de movimenta\u00e7\u00e3o relativos enquanto estiver inserindo registro.");
        table2.put("Invalid fetch direction constant: {0}.", "Constante de dire\u00e7\u00e3o da busca \u00e9 inv\u00e1lida: {0}.");
        table2.put("Cannot call cancelRowUpdates() when on the insert row.", "N\u00e3o pode chamar cancelRowUpdates() quando estiver inserindo registro.");
        table2.put("Cannot call deleteRow() when on the insert row.", "N\u00e3o pode chamar deleteRow() quando estiver inserindo registro.");
        table2.put("Currently positioned before the start of the ResultSet.  You cannot call deleteRow() here.", "Posicionado antes do in\u00edcio do ResultSet.  Voc\u00ea n\u00e3o pode chamar deleteRow() aqui.");
        table2.put("Currently positioned after the end of the ResultSet.  You cannot call deleteRow() here.", "Posicionado depois do fim do ResultSet.  Voc\u00ea n\u00e3o pode chamar deleteRow() aqui.");
        table2.put("There are no rows in this ResultSet.", "N\u00e3o h\u00e1 nenhum registro neste ResultSet.");
        table2.put("Not on the insert row.", "N\u00e3o est\u00e1 inserindo um registro.");
        table2.put("You must specify at least one column value to insert a row.", "Voc\u00ea deve especificar pelo menos uma coluna para inserir um registro.");
        table2.put("The JVM claims not to support the encoding: {0}", "A JVM reclamou que n\u00e3o suporta a codifica\u00e7\u00e3o: {0}");
        table2.put("Provided InputStream failed.", "InputStream fornecido falhou.");
        table2.put("Provided Reader failed.", "Reader fornecido falhou.");
        table2.put("Can''t refresh the insert row.", "N\u00e3o pode renovar um registro inserido.");
        table2.put("Cannot call updateRow() when on the insert row.", "N\u00e3o pode chamar updateRow() quando estiver inserindo registro.");
        table2.put("Cannot update the ResultSet because it is either before the start or after the end of the results.", "N\u00e3o pode atualizar o ResultSet porque ele est\u00e1 antes do in\u00edcio ou depois do fim dos resultados.");
        table2.put("ResultSets with concurrency CONCUR_READ_ONLY cannot be updated.", "ResultSets com CONCUR_READ_ONLY concorrentes n\u00e3o podem ser atualizados.");
        table2.put("No primary key found for table {0}.", "Nenhuma chave prim\u00e1ria foi encontrada para tabela {0}.");
        table2.put("Fetch size must be a value greater to or equal to 0.", "Tamanho da busca deve ser um valor maior ou igual a 0.");
        table2.put("Bad value for type {0} : {1}", "Valor inv\u00e1lido para tipo {0} : {1}");
        table2.put("The column name {0} was not found in this ResultSet.", "A nome da coluna {0} n\u00e3o foi encontrado neste ResultSet.");
        table2.put("ResultSet is not updateable.  The query that generated this result set must select only one table, and must select all primary keys from that table. See the JDBC 2.1 API Specification, section 5.6 for more details.", "ResultSet n\u00e3o \u00e9 atualiz\u00e1vel. A consulta que gerou esse conjunto de resultados deve selecionar somente uma tabela, e deve selecionar todas as chaves prim\u00e1rias daquela tabela. Veja a especifica\u00e7\u00e3o na API do JDBC 2.1, se\u00e7\u00e3o 5.6 para obter mais detalhes.");
        table2.put("This ResultSet is closed.", "Este ResultSet est\u00e1 fechado.");
        table2.put("ResultSet not positioned properly, perhaps you need to call next.", "ResultSet n\u00e3o est\u00e1 posicionado corretamente, talvez voc\u00ea precise chamar next.");
        table2.put("Can''t use query methods that take a query string on a PreparedStatement.", "N\u00e3o pode utilizar m\u00e9todos de consulta que pegam uma consulta de um comando preparado.");
        table2.put("Multiple ResultSets were returned by the query.", "ResultSets m\u00faltiplos foram retornados pela consulta.");
        table2.put("A CallableStatement was executed with nothing returned.", "Uma fun\u00e7\u00e3o foi executada e nada foi retornado.");
        table2.put("A CallableStatement was executed with an invalid number of parameters", "Uma fun\u00e7\u00e3o foi executada com um n\u00famero inv\u00e1lido de par\u00e2metros");
        table2.put("A CallableStatement function was executed and the out parameter {0} was of type {1} however type {2} was registered.", "Uma fun\u00e7\u00e3o foi executada e o par\u00e2metro de retorno {0} era do tipo {1} contudo tipo {2} foi registrado.");
        table2.put("Maximum number of rows must be a value grater than or equal to 0.", "N\u00famero m\u00e1ximo de registros deve ser um valor maior ou igual a 0.");
        table2.put("Query timeout must be a value greater than or equals to 0.", "Tempo de espera da consulta deve ser um valor maior ou igual a 0.");
        table2.put("The maximum field size must be a value greater than or equal to 0.", "O tamanho m\u00e1ximo de um campo deve ser um valor maior ou igual a 0.");
        table2.put("Unknown Types value.", "Valor de Types desconhecido.");
        table2.put("Invalid stream length {0}.", "Tamanho de dado {0} \u00e9 inv\u00e1lido.");
        table2.put("The JVM claims not to support the {0} encoding.", "A JVM reclamou que n\u00e3o suporta a codifica\u00e7\u00e3o {0}.");
        table2.put("Unknown type {0}.", "Tipo desconhecido {0}.");
        table2.put("Cannot cast an instance of {0} to type {1}", "N\u00e3o pode converter uma inst\u00e2ncia de {0} para tipo {1}");
        table2.put("Unsupported Types value: {0}", "Valor de Types n\u00e3o \u00e9 suportado: {0}");
        table2.put("Can''t infer the SQL type to use for an instance of {0}. Use setObject() with an explicit Types value to specify the type to use.", "N\u00e3o pode inferir um tipo SQL a ser usado para uma inst\u00e2ncia de {0}. Use setObject() com um valor de Types expl\u00edcito para especificar o tipo a ser usado.");
        table2.put("This statement does not declare an OUT parameter.  Use '{' ?= call ... '}' to declare one.", "Este comando n\u00e3o declara um par\u00e2metro de sa\u00edda. Utilize '{' ?= chamada ... '}' para declarar um)");
        table2.put("wasNull cannot be call before fetching a result.", "wasNull n\u00e3o pode ser chamado antes de obter um resultado.");
        table2.put("Malformed function or procedure escape syntax at offset {0}.", "Sintaxe de escape mal formada da fun\u00e7\u00e3o ou do procedimento no deslocamento {0}.");
        table2.put("Parameter of type {0} was registered, but call to get{1} (sqltype={2}) was made.", "Par\u00e2metro do tipo {0} foi registrado, mas uma chamada a get{1} (tiposql={2}) foi feita.");
        table2.put("A CallableStatement was declared, but no call to registerOutParameter(1, <some type>) was made.", "Uma fun\u00e7\u00e3o foi declarada mas nenhuma chamada a registerOutParameter (1, <algum_tipo>) foi feita.");
        table2.put("No function outputs were registered.", "Nenhum sa\u00edda de fun\u00e7\u00e3o foi registrada.");
        table2.put("Results cannot be retrieved from a CallableStatement before it is executed.", "Resultados n\u00e3o podem ser recuperados de uma fun\u00e7\u00e3o antes dela ser executada.");
        table2.put("This statement has been closed.", "Este comando foi fechado.");
        table2.put("Too many update results were returned.", "Muitos resultados de atualiza\u00e7\u00e3o foram retornados.");
        table2.put("Batch entry {0} {1} was aborted.  Call getNextException to see the cause.", "Entrada em lote {0} {1} foi abortada. Chame getNextException para ver a causa.");
        table2.put("Unexpected error writing large object to database.", "Erro inesperado ao escrever objeto grande no banco de dados.");
        table2.put("{0} function takes one and only one argument.", "fun\u00e7\u00e3o {0} recebe somente um argumento.");
        table2.put("{0} function takes two and only two arguments.", "fun\u00e7\u00e3o {0} recebe somente dois argumentos.");
        table2.put("{0} function takes four and only four argument.", "fun\u00e7\u00e3o {0} recebe somente quatro argumentos.");
        table2.put("{0} function takes two or three arguments.", "fun\u00e7\u00e3o {0} recebe dois ou tr\u00eas argumentos.");
        table2.put("{0} function doesn''t take any argument.", "fun\u00e7\u00e3o {0} n\u00e3o recebe nenhum argumento.");
        table2.put("{0} function takes three and only three arguments.", "fun\u00e7\u00e3o {0} recebe tr\u00eas e somente tr\u00eas argumentos.");
        table2.put("Interval {0} not yet implemented", "Intervalo {0} ainda n\u00e3o foi implementado");
        table2.put("Infinite value found for timestamp/date. This cannot be represented as time.", "Valor infinito encontrado em timestamp/date. Isto n\u00e3o pode ser representado como tempo.");
        table2.put("The class {0} does not implement org.postgresql.util.PGobject.", "A classe {0} n\u00e3o implementa org.postgresql.util.PGobject.");
        table2.put("Unknown ResultSet holdability setting: {0}.", "Defini\u00e7\u00e3o de durabilidade do ResultSet desconhecida: {0}.");
        table2.put("Server versions prior to 8.0 do not support savepoints.", "Vers\u00f5es do servidor anteriores a 8.0 n\u00e3o suportam savepoints.");
        table2.put("Cannot establish a savepoint in auto-commit mode.", "N\u00e3o pode estabelecer um savepoint no modo de efetiva\u00e7\u00e3o autom\u00e1tica (auto-commit).");
        table2.put("Returning autogenerated keys is not supported.", "Retorno de chaves geradas automaticamente n\u00e3o \u00e9 suportado.");
        table2.put("The parameter index is out of range: {0}, number of parameters: {1}.", "O \u00edndice de par\u00e2metro est\u00e1 fora do intervalo: {0}, n\u00famero de par\u00e2metros: {1}.");
        table2.put("Returning autogenerated keys is only supported for 8.2 and later servers.", "Retorno de chaves geradas automaticamente s\u00f3 \u00e9 suportado por servidores 8.2 ou mais recentes.");
        table2.put("Returning autogenerated keys by column index is not supported.", "Retorno de chaves geradas automaticamente por \u00edndice de coluna n\u00e3o \u00e9 suportado.");
        table2.put("Cannot reference a savepoint after it has been released.", "N\u00e3o pode referenciar um savepoint ap\u00f3s ele ser descartado.");
        table2.put("Cannot retrieve the id of a named savepoint.", "N\u00e3o pode recuperar o id de um savepoint com nome.");
        table2.put("Cannot retrieve the name of an unnamed savepoint.", "N\u00e3o pode recuperar o nome de um savepoint sem nome.");
        table2.put("Invalid UUID data.", "dado UUID \u00e9 inv\u00e1lido.");
        table2.put("Unable to find server array type for provided name {0}.", "N\u00e3o foi poss\u00edvel encontrar tipo matriz para nome fornecido {0}.");
        table2.put("ClientInfo property not supported.", "propriedade ClientInfo n\u00e3o \u00e9 suportada.");
        table2.put("Unable to decode xml data.", "N\u00e3o foi poss\u00edvel decodificar dado xml.");
        table2.put("Unknown XML Source class: {0}", "Classe XML Source desconhecida: {0}");
        table2.put("Unable to create SAXResult for SQLXML.", "N\u00e3o foi poss\u00edvel criar SAXResult para SQLXML.");
        table2.put("Unable to create StAXResult for SQLXML", "N\u00e3o foi poss\u00edvel criar StAXResult para SQLXML");
        table2.put("Unknown XML Result class: {0}", "Classe XML Result desconhecida: {0}");
        table2.put("This SQLXML object has already been freed.", "Este objeto SQLXML j\u00e1 foi liberado.");
        table2.put("This SQLXML object has not been initialized, so you cannot retrieve data from it.", "Este objeto SQLXML n\u00e3o foi inicializado, ent\u00e3o voc\u00ea n\u00e3o pode recuperar dados dele.");
        table2.put("Failed to convert binary xml data to encoding: {0}.", "Falhou ao converter dados xml bin\u00e1rios para codifica\u00e7\u00e3o: {0}.");
        table2.put("Unable to convert DOMResult SQLXML data to a string.", "N\u00e3o foi poss\u00edvel converter dado SQLXML do DOMResult para uma cadeia de caracteres.");
        table2.put("This SQLXML object has already been initialized, so you cannot manipulate it further.", "Este objeto SQLXML j\u00e1 foi inicializado, ent\u00e3o voc\u00ea n\u00e3o pode manipul\u00e1-lo depois.");
        table2.put("Failed to initialize LargeObject API", "Falhou ao inicializar API de Objetos Grandes");
        table2.put("Large Objects may not be used in auto-commit mode.", "Objetos Grandes n\u00e3o podem ser usados no modo de efetiva\u00e7\u00e3o autom\u00e1tica (auto-commit).");
        table2.put("The SSLSocketFactory class provided {0} could not be instantiated.", "A classe SSLSocketFactory forneceu {0} que n\u00e3o p\u00f4de ser instanciado.");
        table2.put("Conversion of interval failed", "Convers\u00e3o de interval falhou");
        table2.put("Conversion of money failed.", "Convers\u00e3o de money falhou.");
        table2.put("Detail: {0}", "Detalhe: {0}");
        table2.put("Hint: {0}", "Dica: {0}");
        table2.put("Position: {0}", "Posi\u00e7\u00e3o: {0}");
        table2.put("Where: {0}", "Onde: {0}");
        table2.put("Internal Query: {0}", "Consulta Interna: {0}");
        table2.put("Internal Position: {0}", "Posi\u00e7\u00e3o Interna: {0}");
        table2.put("Location: File: {0}, Routine: {1}, Line: {2}", "Local: Arquivo: {0}, Rotina: {1}, Linha: {2}");
        table2.put("Server SQLState: {0}", "SQLState: {0}");
        table2.put("Invalid flags", "Marcadores inv\u00e1lidos");
        table2.put("xid must not be null", "xid n\u00e3o deve ser nulo");
        table2.put("Connection is busy with another transaction", "Conex\u00e3o est\u00e1 ocupada com outra transa\u00e7\u00e3o");
        table2.put("suspend/resume not implemented", "suspender/recome\u00e7ar n\u00e3o est\u00e1 implementado");
        table2.put("Transaction interleaving not implemented", "Intercala\u00e7\u00e3o de transa\u00e7\u00e3o n\u00e3o est\u00e1 implementado");
        table2.put("Error disabling autocommit", "Erro ao desabilitar autocommit");
        table2.put("tried to call end without corresponding start call", "tentou executar end sem a chamada ao start correspondente");
        table2.put("Not implemented: Prepare must be issued using the same connection that started the transaction", "N\u00e3o est\u00e1 implementado: Prepare deve ser executado utilizando a mesma conex\u00e3o que iniciou a transa\u00e7\u00e3o");
        table2.put("Prepare called before end", "Prepare executado antes do end");
        table2.put("Server versions prior to 8.1 do not support two-phase commit.", "Vers\u00f5es do servidor anteriores a 8.1 n\u00e3o suportam efetiva\u00e7\u00e3o em duas fases.");
        table2.put("Error preparing transaction", "Erro ao preparar transa\u00e7\u00e3o");
        table2.put("Invalid flag", "Marcador inv\u00e1lido");
        table2.put("Error during recover", "Erro durante recupera\u00e7\u00e3o");
        table2.put("Error rolling back prepared transaction", "Erro ao cancelar transa\u00e7\u00e3o preparada");
        table2.put("Not implemented: one-phase commit must be issued using the same connection that was used to start it", "N\u00e3o est\u00e1 implementado: efetivada da primeira fase deve ser executada utilizando a mesma conex\u00e3o que foi utilizada para inici\u00e1-la");
        table2.put("commit called before end", "commit executado antes do end");
        table2.put("Error during one-phase commit", "Erro durante efetiva\u00e7\u00e3o de uma fase");
        table2.put("Not implemented: 2nd phase commit must be issued using an idle connection", "N\u00e3o est\u00e1 implementado: efetiva\u00e7\u00e3o da segunda fase deve ser executada utilizado uma conex\u00e3o ociosa");
        table2.put("Heuristic commit/rollback not supported", "Efetiva\u00e7\u00e3o/Cancelamento heur\u00edstico n\u00e3o \u00e9 suportado");
        table = table2;
    }
}
