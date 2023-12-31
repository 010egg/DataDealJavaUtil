// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.translation;

import java.util.Enumeration;
import java.util.MissingResourceException;
import java.util.Hashtable;
import java.util.ResourceBundle;

public class messages_zh_TW extends ResourceBundle
{
    private static final Hashtable table;
    
    public Object handleGetObject(final String key) throws MissingResourceException {
        return messages_zh_TW.table.get(key);
    }
    
    public Enumeration getKeys() {
        return messages_zh_TW.table.keys();
    }
    
    public ResourceBundle getParent() {
        return super.parent;
    }
    
    static {
        final Hashtable<String, String> table2 = new Hashtable<String, String>();
        table2.put("", "Project-Id-Version: PostgreSQL JDBC Driver 8.3\nReport-Msgid-Bugs-To: \nPOT-Creation-Date: 2012-09-10 19:32-0400\nPO-Revision-Date: 2008-01-21 16:50+0800\nLast-Translator: \u90ed\u671d\u76ca(ChaoYi, Kuo) <Kuo.ChaoYi@gmail.com>\nLanguage-Team: The PostgreSQL Development Team <Kuo.ChaoYi@gmail.com>\nLanguage: \nMIME-Version: 1.0\nContent-Type: text/plain; charset=UTF-8\nContent-Transfer-Encoding: 8bit\nX-Poedit-Language: Chinese\nX-Poedit-Country: TAIWAN\nX-Poedit-SourceCharset: utf-8\n");
        table2.put("Something unusual has occured to cause the driver to fail. Please report this exception.", "\u4e0d\u660e\u7684\u539f\u56e0\u5c0e\u81f4\u9a45\u52d5\u7a0b\u5f0f\u9020\u6210\u5931\u6557\uff0c\u8acb\u56de\u5831\u9019\u500b\u4f8b\u5916\u3002");
        table2.put("Connection attempt timed out.", "Connection \u5617\u8a66\u903e\u6642\u3002");
        table2.put("Method {0} is not yet implemented.", "\u9019\u500b {0} \u65b9\u6cd5\u5c1a\u672a\u88ab\u5be6\u4f5c\u3002");
        table2.put("A connection could not be made using the requested protocol {0}.", "\u7121\u6cd5\u4ee5\u8981\u6c42\u7684\u901a\u8a0a\u5354\u5b9a {0} \u5efa\u7acb\u9023\u7dda\u3002");
        table2.put("An unexpected result was returned by a query.", "\u50b3\u56de\u975e\u9810\u671f\u7684\u67e5\u8a62\u7d50\u679c\u3002");
        table2.put("Zero bytes may not occur in string parameters.", "\u5b57\u4e32\u53c3\u6578\u4e0d\u80fd\u6709 0 \u500b\u4f4d\u5143\u7d44\u3002");
        table2.put("Zero bytes may not occur in identifiers.", "\u5728\u6a19\u8b58\u8b58\u5225\u7b26\u4e2d\u4e0d\u5b58\u5728\u96f6\u4f4d\u5143\u7d44\u3002");
        table2.put("Cannot convert an instance of {0} to type {1}", "\u7121\u6cd5\u8f49\u63db {0} \u5230\u985e\u578b {1} \u7684\u5be6\u4f8b");
        table2.put("Connection refused. Check that the hostname and port are correct and that the postmaster is accepting TCP/IP connections.", "\u9023\u7dda\u88ab\u62d2\uff0c\u8acb\u6aa2\u67e5\u4e3b\u6a5f\u540d\u7a31\u548c\u57e0\u865f\uff0c\u4e26\u78ba\u5b9a postmaster \u53ef\u4ee5\u63a5\u53d7 TCP/IP \u9023\u7dda\u3002");
        table2.put("The connection attempt failed.", "\u5617\u8a66\u9023\u7dda\u5df2\u5931\u6557\u3002");
        table2.put("The server does not support SSL.", "\u4f3a\u670d\u5668\u4e0d\u652f\u63f4 SSL \u9023\u7dda\u3002");
        table2.put("An error occured while setting up the SSL connection.", "\u9032\u884c SSL \u9023\u7dda\u6642\u767c\u751f\u932f\u8aa4\u3002");
        table2.put("Connection rejected: {0}.", "\u9023\u7dda\u5df2\u88ab\u62d2\u7d55\uff1a{0}\u3002");
        table2.put("The server requested password-based authentication, but no password was provided.", "\u4f3a\u670d\u5668\u8981\u6c42\u4f7f\u7528\u5bc6\u78bc\u9a57\u8b49\uff0c\u4f46\u662f\u5bc6\u78bc\u4e26\u672a\u63d0\u4f9b\u3002");
        table2.put("The authentication type {0} is not supported. Check that you have configured the pg_hba.conf file to include the client''s IP address or subnet, and that it is using an authentication scheme supported by the driver.", "\u4e0d\u652f\u63f4 {0} \u9a57\u8b49\u578b\u5225\u3002\u8acb\u6838\u5c0d\u60a8\u5df2\u7d93\u7d44\u614b pg_hba.conf \u6a94\u6848\u5305\u542b\u5ba2\u6236\u7aef\u7684IP\u4f4d\u5740\u6216\u7db2\u8def\u5340\u6bb5\uff0c\u4ee5\u53ca\u9a45\u52d5\u7a0b\u5f0f\u6240\u652f\u63f4\u7684\u9a57\u8b49\u67b6\u69cb\u6a21\u5f0f\u5df2\u88ab\u652f\u63f4\u3002");
        table2.put("Protocol error.  Session setup failed.", "\u901a\u8a0a\u5354\u5b9a\u932f\u8aa4\uff0cSession \u521d\u59cb\u5316\u5931\u6557\u3002");
        table2.put("Backend start-up failed: {0}.", "\u5f8c\u7aef\u555f\u52d5\u5931\u6557\uff1a{0}\u3002");
        table2.put("The column index is out of range: {0}, number of columns: {1}.", "\u6b04\u4f4d\u7d22\u5f15\u8d85\u904e\u8a31\u53ef\u7bc4\u570d\uff1a{0}\uff0c\u6b04\u4f4d\u6578\uff1a{1}\u3002");
        table2.put("No value specified for parameter {0}.", "\u672a\u8a2d\u5b9a\u53c3\u6578\u503c {0} \u7684\u5167\u5bb9\u3002");
        table2.put("An I/O error occured while sending to the backend.", "\u50b3\u9001\u8cc7\u6599\u81f3\u5f8c\u7aef\u6642\u767c\u751f I/O \u932f\u8aa4\u3002");
        table2.put("Unknown Response Type {0}.", "\u4e0d\u660e\u7684\u56de\u61c9\u985e\u578b {0}\u3002");
        table2.put("Unable to interpret the update count in command completion tag: {0}.", "\u7121\u6cd5\u89e3\u8b80\u547d\u4ee4\u5b8c\u6210\u6a19\u7c64\u4e2d\u7684\u66f4\u65b0\u8a08\u6578\uff1a{0}\u3002");
        table2.put("The server''s DateStyle parameter was changed to {0}. The JDBC driver requires DateStyle to begin with ISO for correct operation.", "\u9019\u4f3a\u670d\u5668\u7684 DateStyle \u53c3\u6578\u88ab\u66f4\u6539\u6210 {0}\uff0cJDBC \u9a45\u52d5\u7a0b\u5f0f\u8acb\u6c42\u9700\u8981 DateStyle \u4ee5 ISO \u958b\u982d\u4ee5\u6b63\u78ba\u5de5\u4f5c\u3002");
        table2.put("The server''s standard_conforming_strings parameter was reported as {0}. The JDBC driver expected on or off.", "\u9019\u4f3a\u670d\u5668\u7684 standard_conforming_strings \u53c3\u6578\u5df2\u56de\u5831\u70ba {0}\uff0cJDBC \u9a45\u52d5\u7a0b\u5f0f\u5df2\u9810\u671f\u958b\u555f\u6216\u662f\u95dc\u9589\u3002");
        table2.put("The driver currently does not support COPY operations.", "\u9a45\u52d5\u7a0b\u5f0f\u76ee\u524d\u4e0d\u652f\u63f4 COPY \u64cd\u4f5c\u3002");
        table2.put("This PooledConnection has already been closed.", "\u9019\u500b PooledConnection \u5df2\u7d93\u88ab\u95dc\u9589\u3002");
        table2.put("Connection has been closed automatically because a new connection was opened for the same PooledConnection or the PooledConnection has been closed.", "Connection \u5df2\u81ea\u52d5\u7d50\u675f\uff0c\u56e0\u70ba\u4e00\u500b\u65b0\u7684  PooledConnection \u9023\u7dda\u88ab\u958b\u555f\u6216\u8005\u6216 PooledConnection \u5df2\u88ab\u95dc\u9589\u3002");
        table2.put("Connection has been closed.", "Connection \u5df2\u7d93\u88ab\u95dc\u9589\u3002");
        table2.put("Statement has been closed.", "Sstatement \u5df2\u7d93\u88ab\u95dc\u9589\u3002");
        table2.put("DataSource has been closed.", "DataSource \u5df2\u7d93\u88ab\u95dc\u9589\u3002");
        table2.put("Fastpath call {0} - No result was returned and we expected an integer.", "Fastpath \u547c\u53eb {0} - \u6c92\u6709\u50b3\u56de\u503c\uff0c\u4e14\u61c9\u8a72\u50b3\u56de\u4e00\u500b\u6574\u6578\u3002");
        table2.put("The fastpath function {0} is unknown.", "\u4e0d\u660e\u7684 fastpath \u51fd\u5f0f {0}\u3002");
        table2.put("Conversion to type {0} failed: {1}.", "\u8f49\u63db\u578b\u5225 {0} \u5931\u6557\uff1a{1}\u3002");
        table2.put("Cannot tell if path is open or closed: {0}.", "\u7121\u6cd5\u5f97\u77e5 path \u662f\u958b\u555f\u6216\u95dc\u9589\uff1a{0}\u3002");
        table2.put("The array index is out of range: {0}", "\u9663\u5217\u7d22\u5f15\u8d85\u904e\u8a31\u53ef\u7bc4\u570d\uff1a{0}");
        table2.put("The array index is out of range: {0}, number of elements: {1}.", "\u9663\u5217\u7d22\u5f15\u8d85\u904e\u8a31\u53ef\u7bc4\u570d\uff1a{0}\uff0c\u5143\u7d20\u6578\u91cf\uff1a{1}\u3002");
        table2.put("Invalid character data was found.  This is most likely caused by stored data containing characters that are invalid for the character set the database was created in.  The most common example of this is storing 8bit data in a SQL_ASCII database.", "\u767c\u73fe\u4e0d\u5408\u6cd5\u7684\u5b57\u5143\uff0c\u53ef\u80fd\u7684\u539f\u56e0\u662f\u6b32\u5132\u5b58\u7684\u8cc7\u6599\u4e2d\u5305\u542b\u8cc7\u6599\u5eab\u7684\u5b57\u5143\u96c6\u4e0d\u652f\u63f4\u7684\u5b57\u78bc\uff0c\u5176\u4e2d\u6700\u5e38\u898b\u4f8b\u5b50\u7684\u5c31\u662f\u5c07 8 \u4f4d\u5143\u8cc7\u6599\u5b58\u5165\u4f7f\u7528 SQL_ASCII \u7de8\u78bc\u7684\u8cc7\u6599\u5eab\u4e2d\u3002");
        table2.put("Truncation of large objects is only implemented in 8.3 and later servers.", "\u5927\u578b\u7269\u4ef6\u7684\u622a\u65b7(Truncation)\u50c5\u88ab\u5be6\u4f5c\u57f7\u884c\u5728 8.3 \u548c\u5f8c\u4f86\u7684\u4f3a\u670d\u5668\u3002");
        table2.put("PostgreSQL LOBs can only index to: {0}", "PostgreSQL LOBs \u50c5\u80fd\u7d22\u5f15\u5230\uff1a{0}");
        table2.put("Unsupported value for stringtype parameter: {0}", "\u5b57\u4e32\u578b\u5225\u53c3\u6578\u503c\u672a\u88ab\u652f\u6301\uff1a{0}");
        table2.put("No results were returned by the query.", "\u67e5\u8a62\u6c92\u6709\u50b3\u56de\u4efb\u4f55\u7d50\u679c\u3002");
        table2.put("A result was returned when none was expected.", "\u50b3\u56de\u9810\u671f\u4e4b\u5916\u7684\u7d50\u679c\u3002");
        table2.put("Failed to create object for: {0}.", "\u70ba {0} \u5efa\u7acb\u7269\u4ef6\u5931\u6557\u3002");
        table2.put("Cannot change transaction read-only property in the middle of a transaction.", "\u4e0d\u80fd\u5728\u4e8b\u7269\u4ea4\u6613\u904e\u7a0b\u4e2d\u6539\u8b8a\u4e8b\u7269\u4ea4\u6613\u552f\u8b80\u5c6c\u6027\u3002");
        table2.put("Cannot change transaction isolation level in the middle of a transaction.", "\u4e0d\u80fd\u5728\u4e8b\u52d9\u4ea4\u6613\u904e\u7a0b\u4e2d\u6539\u8b8a\u4e8b\u7269\u4ea4\u6613\u9694\u7d55\u7b49\u7d1a\u3002");
        table2.put("Transaction isolation level {0} not supported.", "\u4e0d\u652f\u63f4\u4ea4\u6613\u9694\u7d55\u7b49\u7d1a {0} \u3002");
        table2.put("Unable to translate data into the desired encoding.", "\u7121\u6cd5\u5c07\u8cc7\u6599\u8f49\u6210\u76ee\u6a19\u7de8\u78bc\u3002");
        table2.put("Unable to find name datatype in the system catalogs.", "\u5728\u7cfb\u7d71 catalog \u4e2d\u627e\u4e0d\u5230\u540d\u7a31\u8cc7\u6599\u985e\u578b(datatype)\u3002");
        table2.put("Operation requires a scrollable ResultSet, but this ResultSet is FORWARD_ONLY.", "\u64cd\u4f5c\u8981\u6c42\u53ef\u6372\u52d5\u7684 ResultSet\uff0c\u4f46\u6b64 ResultSet \u662f FORWARD_ONLY\u3002");
        table2.put("Unexpected error while decoding character data from a large object.", "\u5f9e\u5927\u578b\u7269\u4ef6(large object)\u89e3\u78bc\u5b57\u5143\u8cc7\u6599\u6642\u767c\u751f\u932f\u8aa4\u3002");
        table2.put("Can''t use relative move methods while on the insert row.", "\u4e0d\u80fd\u5728\u65b0\u589e\u7684\u8cc7\u6599\u5217\u4e0a\u4f7f\u7528\u76f8\u5c0d\u4f4d\u7f6e move \u65b9\u6cd5\u3002");
        table2.put("Invalid fetch direction constant: {0}.", "\u7121\u6548\u7684 fetch \u65b9\u5411\u5e38\u6578\uff1a{0}\u3002");
        table2.put("Cannot call cancelRowUpdates() when on the insert row.", "\u4e0d\u80fd\u5728\u65b0\u589e\u7684\u8cc7\u6599\u5217\u4e0a\u547c\u53eb cancelRowUpdates()\u3002");
        table2.put("Cannot call deleteRow() when on the insert row.", "\u4e0d\u80fd\u5728\u65b0\u589e\u7684\u8cc7\u6599\u4e0a\u547c\u53eb deleteRow()\u3002");
        table2.put("Currently positioned before the start of the ResultSet.  You cannot call deleteRow() here.", "\u4e0d\u80fd\u5728 ResultSet \u7684\u7b2c\u4e00\u7b46\u8cc7\u6599\u4e4b\u524d\u547c\u53eb deleteRow()\u3002");
        table2.put("Currently positioned after the end of the ResultSet.  You cannot call deleteRow() here.", "\u4e0d\u80fd\u5728 ResultSet \u7684\u6700\u5f8c\u4e00\u7b46\u8cc7\u6599\u4e4b\u5f8c\u547c\u53eb deleteRow()\u3002");
        table2.put("There are no rows in this ResultSet.", "ResultSet \u4e2d\u627e\u4e0d\u5230\u8cc7\u6599\u5217\u3002");
        table2.put("Not on the insert row.", "\u4e0d\u5728\u65b0\u589e\u7684\u8cc7\u6599\u5217\u4e0a\u3002");
        table2.put("The JVM claims not to support the encoding: {0}", "JVM \u8072\u660e\u4e26\u4e0d\u652f\u63f4\u7de8\u78bc\uff1a{0} \u3002");
        table2.put("Provided InputStream failed.", "\u63d0\u4f9b\u7684 InputStream \u5df2\u5931\u6557\u3002");
        table2.put("Provided Reader failed.", "\u63d0\u4f9b\u7684 Reader \u5df2\u5931\u6557\u3002");
        table2.put("Can''t refresh the insert row.", "\u7121\u6cd5\u91cd\u8b80\u65b0\u589e\u7684\u8cc7\u6599\u5217\u3002");
        table2.put("Cannot call updateRow() when on the insert row.", "\u4e0d\u80fd\u5728\u65b0\u589e\u7684\u8cc7\u6599\u5217\u4e0a\u547c\u53eb deleteRow()\u3002");
        table2.put("Cannot update the ResultSet because it is either before the start or after the end of the results.", "\u7121\u6cd5\u66f4\u65b0 ResultSet\uff0c\u53ef\u80fd\u5728\u7b2c\u4e00\u7b46\u8cc7\u6599\u4e4b\u524d\u6216\u6700\u672a\u7b46\u8cc7\u6599\u4e4b\u5f8c\u3002");
        table2.put("ResultSets with concurrency CONCUR_READ_ONLY cannot be updated.", "ResultSets \u8207\u4e26\u767c\u540c\u4f5c(Concurrency) CONCUR_READ_ONLY \u4e0d\u80fd\u88ab\u66f4\u65b0\u3002");
        table2.put("No primary key found for table {0}.", "{0} \u8cc7\u6599\u8868\u4e2d\u672a\u627e\u5230\u4e3b\u9375(Primary key)\u3002");
        table2.put("Fetch size must be a value greater to or equal to 0.", "\u8cc7\u6599\u8b80\u53d6\u7b46\u6578(fetch size)\u5fc5\u9808\u5927\u65bc\u6216\u7b49\u65bc 0\u3002");
        table2.put("Bad value for type {0} : {1}", "\u4e0d\u826f\u7684\u578b\u5225\u503c {0} : {1}");
        table2.put("The column name {0} was not found in this ResultSet.", "ResultSet \u4e2d\u627e\u4e0d\u5230\u6b04\u4f4d\u540d\u7a31 {0}\u3002");
        table2.put("ResultSet is not updateable.  The query that generated this result set must select only one table, and must select all primary keys from that table. See the JDBC 2.1 API Specification, section 5.6 for more details.", "\u4e0d\u53ef\u66f4\u65b0\u7684 ResultSet\u3002\u7528\u4f86\u7522\u751f\u9019\u500b ResultSet \u7684 SQL \u547d\u4ee4\u53ea\u80fd\u64cd\u4f5c\u4e00\u500b\u8cc7\u6599\u8868\uff0c\u4e26\u4e14\u5fc5\u9700\u9078\u64c7\u6240\u6709\u4e3b\u9375\u6b04\u4f4d\uff0c\u8a73\u7d30\u8acb\u53c3\u95b1 JDBC 2.1 API \u898f\u683c\u66f8 5.6 \u7bc0\u3002");
        table2.put("This ResultSet is closed.", "\u9019\u500b ResultSet \u5df2\u7d93\u88ab\u95dc\u9589\u3002");
        table2.put("ResultSet not positioned properly, perhaps you need to call next.", "\u67e5\u8a62\u7d50\u679c\u6307\u6a19\u4f4d\u7f6e\u4e0d\u6b63\u78ba\uff0c\u60a8\u4e5f\u8a31\u9700\u8981\u547c\u53eb ResultSet \u7684 next() \u65b9\u6cd5\u3002");
        table2.put("Can''t use query methods that take a query string on a PreparedStatement.", "\u5728 PreparedStatement \u4e0a\u4e0d\u80fd\u4f7f\u7528\u7372\u53d6\u67e5\u8a62\u5b57\u4e32\u7684\u67e5\u8a62\u65b9\u6cd5\u3002");
        table2.put("Multiple ResultSets were returned by the query.", "\u67e5\u8a62\u50b3\u56de\u591a\u500b ResultSet\u3002");
        table2.put("A CallableStatement was executed with nothing returned.", "\u4e00\u500b CallableStatement \u57f7\u884c\u51fd\u5f0f\u5f8c\u6c92\u6709\u50b3\u56de\u503c\u3002");
        table2.put("A CallableStatement function was executed and the out parameter {0} was of type {1} however type {2} was registered.", "\u4e00\u500b CallableStatement \u57f7\u884c\u51fd\u5f0f\u5f8c\u8f38\u51fa\u7684\u53c3\u6578\u578b\u5225\u70ba {1} \u503c\u70ba {0}\uff0c\u4f46\u662f\u5df2\u8a3b\u518a\u7684\u578b\u5225\u662f {2}\u3002");
        table2.put("Maximum number of rows must be a value grater than or equal to 0.", "\u6700\u5927\u8cc7\u6599\u8b80\u53d6\u7b46\u6578\u5fc5\u9808\u5927\u65bc\u6216\u7b49\u65bc 0\u3002");
        table2.put("Query timeout must be a value greater than or equals to 0.", "\u67e5\u8a62\u903e\u6642\u7b49\u5019\u6642\u9593\u5fc5\u9808\u5927\u65bc\u6216\u7b49\u65bc 0\u3002");
        table2.put("The maximum field size must be a value greater than or equal to 0.", "\u6700\u5927\u6b04\u4f4d\u5bb9\u91cf\u5fc5\u9808\u5927\u65bc\u6216\u7b49\u65bc 0\u3002");
        table2.put("Unknown Types value.", "\u4e0d\u660e\u7684\u578b\u5225\u503c\u3002");
        table2.put("Invalid stream length {0}.", "\u7121\u6548\u7684\u4e32\u6d41\u9577\u5ea6 {0}.");
        table2.put("The JVM claims not to support the {0} encoding.", "JVM \u8072\u660e\u4e26\u4e0d\u652f\u63f4 {0} \u7de8\u78bc\u3002");
        table2.put("Unknown type {0}.", "\u4e0d\u660e\u7684\u578b\u5225 {0}");
        table2.put("Cannot cast an instance of {0} to type {1}", "\u4e0d\u80fd\u8f49\u63db\u4e00\u500b {0} \u5be6\u4f8b\u5230\u578b\u5225 {1}");
        table2.put("Unsupported Types value: {0}", "\u672a\u88ab\u652f\u6301\u7684\u578b\u5225\u503c\uff1a{0}");
        table2.put("This statement does not declare an OUT parameter.  Use '{' ?= call ... '}' to declare one.", "\u9019\u500b statement \u672a\u5ba3\u544a OUT \u53c3\u6578\uff0c\u4f7f\u7528 '{' ?= call ... '}' \u5ba3\u544a\u4e00\u500b\u3002");
        table2.put("Malformed function or procedure escape syntax at offset {0}.", "\u4e0d\u6b63\u78ba\u7684\u51fd\u5f0f\u6216\u7a0b\u5e8f escape \u8a9e\u6cd5\u65bc {0}\u3002");
        table2.put("Parameter of type {0} was registered, but call to get{1} (sqltype={2}) was made.", "\u5df2\u8a3b\u518a\u53c3\u6578\u578b\u5225 {0}\uff0c\u4f46\u662f\u53c8\u547c\u53eb\u4e86get{1}(sqltype={2})\u3002");
        table2.put("A CallableStatement was declared, but no call to registerOutParameter(1, <some type>) was made.", "\u5df2\u7d93\u5ba3\u544a CallableStatement \u51fd\u5f0f\uff0c\u4f46\u662f\u5c1a\u672a\u547c\u53eb registerOutParameter (1, <some_type>) \u3002");
        table2.put("This statement has been closed.", "\u9019\u500b statement \u5df2\u7d93\u88ab\u95dc\u9589\u3002");
        table2.put("Batch entry {0} {1} was aborted.  Call getNextException to see the cause.", "\u6279\u6b21\u8655\u7406 {0} {1} \u88ab\u4e2d\u6b62\uff0c\u547c\u53eb getNextException \u4ee5\u53d6\u5f97\u539f\u56e0\u3002");
        table2.put("Unexpected error writing large object to database.", "\u5c07\u5927\u578b\u7269\u4ef6(large object)\u5beb\u5165\u8cc7\u6599\u5eab\u6642\u767c\u751f\u4e0d\u660e\u932f\u8aa4\u3002");
        table2.put("{0} function takes one and only one argument.", "{0} \u51fd\u5f0f\u53d6\u5f97\u4e00\u500b\u4e14\u50c5\u6709\u4e00\u500b\u5f15\u6578\u3002");
        table2.put("{0} function takes two and only two arguments.", "{0} \u51fd\u5f0f\u53d6\u5f97\u4e8c\u500b\u4e14\u50c5\u6709\u4e8c\u500b\u5f15\u6578\u3002");
        table2.put("{0} function takes four and only four argument.", "{0} \u51fd\u5f0f\u53d6\u5f97\u56db\u500b\u4e14\u50c5\u6709\u56db\u500b\u5f15\u6578\u3002");
        table2.put("{0} function takes two or three arguments.", "{0} \u51fd\u5f0f\u53d6\u5f97\u4e8c\u500b\u6216\u4e09\u500b\u5f15\u6578\u3002");
        table2.put("{0} function doesn''t take any argument.", "{0} \u51fd\u5f0f\u7121\u6cd5\u53d6\u5f97\u4efb\u4f55\u7684\u5f15\u6578\u3002");
        table2.put("{0} function takes three and only three arguments.", "{0} \u51fd\u5f0f\u53d6\u5f97\u4e09\u500b\u4e14\u50c5\u6709\u4e09\u500b\u5f15\u6578\u3002");
        table2.put("Interval {0} not yet implemented", "\u9694\u7d55 {0} \u5c1a\u672a\u88ab\u5be6\u4f5c\u3002");
        table2.put("The class {0} does not implement org.postgresql.util.PGobject.", "\u985e\u5225 {0} \u672a\u5be6\u505a org.postgresql.util.PGobject\u3002");
        table2.put("Unknown ResultSet holdability setting: {0}.", "\u672a\u77e5\u7684 ResultSet \u53ef\u9069\u7528\u7684\u8a2d\u7f6e\uff1a{0}\u3002");
        table2.put("Server versions prior to 8.0 do not support savepoints.", "8.0 \u7248\u4e4b\u524d\u7684\u4f3a\u670d\u5668\u4e0d\u652f\u63f4\u5132\u5b58\u9ede(SavePints)\u3002");
        table2.put("Cannot establish a savepoint in auto-commit mode.", "\u5728\u81ea\u52d5\u78ba\u8a8d\u4e8b\u7269\u4ea4\u6613\u6a21\u5f0f\u7121\u6cd5\u5efa\u7acb\u5132\u5b58\u9ede(Savepoint)\u3002");
        table2.put("The parameter index is out of range: {0}, number of parameters: {1}.", "\u53c3\u6578\u7d22\u5f15\u8d85\u51fa\u8a31\u53ef\u7bc4\u570d\uff1a{0}\uff0c\u53c3\u6578\u7e3d\u6578\uff1a{1}\u3002");
        table2.put("Cannot reference a savepoint after it has been released.", "\u7121\u6cd5\u53c3\u7167\u5df2\u7d93\u88ab\u91cb\u653e\u7684\u5132\u5b58\u9ede\u3002");
        table2.put("Cannot retrieve the id of a named savepoint.", "\u7121\u6cd5\u53d6\u5f97\u5df2\u547d\u540d\u5132\u5b58\u9ede\u7684 id\u3002");
        table2.put("Cannot retrieve the name of an unnamed savepoint.", "\u7121\u6cd5\u53d6\u5f97\u672a\u547d\u540d\u5132\u5b58\u9ede(Savepoint)\u7684\u540d\u7a31\u3002");
        table2.put("Failed to initialize LargeObject API", "\u521d\u59cb\u5316 LargeObject API \u5931\u6557");
        table2.put("Large Objects may not be used in auto-commit mode.", "\u5927\u578b\u7269\u4ef6\u7121\u6cd5\u88ab\u4f7f\u7528\u5728\u81ea\u52d5\u78ba\u8a8d\u4e8b\u7269\u4ea4\u6613\u6a21\u5f0f\u3002");
        table2.put("Conversion of interval failed", "\u9694\u7d55(Interval)\u8f49\u63db\u5931\u6557\u3002");
        table2.put("Conversion of money failed.", "money \u8f49\u63db\u5931\u6557\u3002");
        table2.put("Detail: {0}", "\u8a73\u7d30\uff1a{0}");
        table2.put("Hint: {0}", "\u5efa\u8b70\uff1a{0}");
        table2.put("Position: {0}", "\u4f4d\u7f6e\uff1a{0}");
        table2.put("Where: {0}", "\u5728\u4f4d\u7f6e\uff1a{0}");
        table2.put("Internal Query: {0}", "\u5167\u90e8\u67e5\u8a62\uff1a{0}");
        table2.put("Internal Position: {0}", "\u5167\u90e8\u4f4d\u7f6e\uff1a{0}");
        table2.put("Location: File: {0}, Routine: {1}, Line: {2}", "\u4f4d\u7f6e\uff1a\u6a94\u6848\uff1a{0}\uff0c\u5e38\u5f0f\uff1a{1}\uff0c\u884c\uff1a{2}");
        table2.put("Server SQLState: {0}", "\u4f3a\u670d\u5668 SQLState\uff1a{0}");
        table2.put("Invalid flags", "\u7121\u6548\u7684\u65d7\u6a19");
        table2.put("suspend/resume not implemented", "\u66ab\u505c(suspend)/\u518d\u7e7c\u7e8c(resume)\u5c1a\u672a\u88ab\u5be6\u4f5c\u3002");
        table2.put("Transaction interleaving not implemented", "\u4e8b\u7269\u4ea4\u6613\u9694\u7d55(Transaction interleaving)\u672a\u88ab\u5be6\u4f5c\u3002");
        table2.put("Server versions prior to 8.1 do not support two-phase commit.", "8.1 \u7248\u4e4b\u524d\u7684\u4f3a\u670d\u5668\u4e0d\u652f\u63f4\u4e8c\u6bb5\u5f0f\u63d0\u4ea4(Two-Phase Commit)\u3002");
        table2.put("Invalid flag", "\u7121\u6548\u7684\u65d7\u6a19");
        table = table2;
    }
}
