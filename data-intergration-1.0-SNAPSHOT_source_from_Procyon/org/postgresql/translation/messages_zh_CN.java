// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.translation;

import java.util.Enumeration;
import java.util.MissingResourceException;
import java.util.Hashtable;
import java.util.ResourceBundle;

public class messages_zh_CN extends ResourceBundle
{
    private static final Hashtable table;
    
    public Object handleGetObject(final String key) throws MissingResourceException {
        return messages_zh_CN.table.get(key);
    }
    
    public Enumeration getKeys() {
        return messages_zh_CN.table.keys();
    }
    
    public ResourceBundle getParent() {
        return super.parent;
    }
    
    static {
        final Hashtable<String, String> table2 = new Hashtable<String, String>();
        table2.put("", "Project-Id-Version: PostgreSQL JDBC Driver 8.3\nReport-Msgid-Bugs-To: \nPOT-Creation-Date: 2012-09-10 19:32-0400\nPO-Revision-Date: 2008-01-31 14:34+0800\nLast-Translator: \u90ed\u671d\u76ca(ChaoYi, Kuo) <Kuo.ChaoYi@gmail.com>\nLanguage-Team: The PostgreSQL Development Team <Kuo.ChaoYi@gmail.com>\nLanguage: \nMIME-Version: 1.0\nContent-Type: text/plain; charset=UTF-8\nContent-Transfer-Encoding: 8bit\nX-Poedit-Language: Chinese\nX-Poedit-Country: CHINA\nX-Poedit-SourceCharset: utf-8\n");
        table2.put("Something unusual has occured to cause the driver to fail. Please report this exception.", "\u4e0d\u660e\u7684\u539f\u56e0\u5bfc\u81f4\u9a71\u52a8\u7a0b\u5e8f\u9020\u6210\u5931\u8d25\uff0c\u8bf7\u56de\u62a5\u8fd9\u4e2a\u4f8b\u5916\u3002");
        table2.put("Connection attempt timed out.", "Connection \u5c1d\u8bd5\u903e\u65f6\u3002");
        table2.put("Method {0} is not yet implemented.", "\u8fd9\u4e2a {0} \u65b9\u6cd5\u5c1a\u672a\u88ab\u5b9e\u4f5c\u3002");
        table2.put("A connection could not be made using the requested protocol {0}.", "\u65e0\u6cd5\u4ee5\u8981\u6c42\u7684\u901a\u8baf\u534f\u5b9a {0} \u5efa\u7acb\u8fde\u7ebf\u3002");
        table2.put("An unexpected result was returned by a query.", "\u4f20\u56de\u975e\u9884\u671f\u7684\u67e5\u8be2\u7ed3\u679c\u3002");
        table2.put("Zero bytes may not occur in string parameters.", "\u5b57\u7b26\u53c2\u6570\u4e0d\u80fd\u6709 0 \u4e2a\u4f4d\u5143\u7ec4\u3002");
        table2.put("Zero bytes may not occur in identifiers.", "\u5728\u6807\u8bc6\u8bc6\u522b\u7b26\u4e2d\u4e0d\u5b58\u5728\u96f6\u4f4d\u5143\u7ec4\u3002");
        table2.put("Cannot convert an instance of {0} to type {1}", "\u65e0\u6cd5\u8f6c\u6362 {0} \u5230\u7c7b\u578b {1} \u7684\u5b9e\u4f8b");
        table2.put("Connection refused. Check that the hostname and port are correct and that the postmaster is accepting TCP/IP connections.", "\u8fde\u7ebf\u88ab\u62d2\uff0c\u8bf7\u68c0\u67e5\u4e3b\u673a\u540d\u79f0\u548c\u57e0\u53f7\uff0c\u5e76\u786e\u5b9a postmaster \u53ef\u4ee5\u63a5\u53d7 TCP/IP \u8fde\u7ebf\u3002");
        table2.put("The connection attempt failed.", "\u5c1d\u8bd5\u8fde\u7ebf\u5df2\u5931\u8d25\u3002");
        table2.put("The server does not support SSL.", "\u670d\u52a1\u5668\u4e0d\u652f\u63f4 SSL \u8fde\u7ebf\u3002");
        table2.put("An error occured while setting up the SSL connection.", "\u8fdb\u884c SSL \u8fde\u7ebf\u65f6\u53d1\u751f\u9519\u8bef\u3002");
        table2.put("Connection rejected: {0}.", "\u8fde\u7ebf\u5df2\u88ab\u62d2\u7edd\uff1a{0}\u3002");
        table2.put("The server requested password-based authentication, but no password was provided.", "\u670d\u52a1\u5668\u8981\u6c42\u4f7f\u7528\u5bc6\u7801\u9a8c\u8bc1\uff0c\u4f46\u662f\u5bc6\u7801\u5e76\u672a\u63d0\u4f9b\u3002");
        table2.put("The authentication type {0} is not supported. Check that you have configured the pg_hba.conf file to include the client''s IP address or subnet, and that it is using an authentication scheme supported by the driver.", "\u4e0d\u652f\u63f4 {0} \u9a8c\u8bc1\u7c7b\u578b\u3002\u8bf7\u6838\u5bf9\u60a8\u5df2\u7ecf\u7ec4\u6001 pg_hba.conf \u6587\u4ef6\u5305\u542b\u5ba2\u6237\u7aef\u7684IP\u4f4d\u5740\u6216\u7f51\u8def\u533a\u6bb5\uff0c\u4ee5\u53ca\u9a71\u52a8\u7a0b\u5e8f\u6240\u652f\u63f4\u7684\u9a8c\u8bc1\u67b6\u6784\u6a21\u5f0f\u5df2\u88ab\u652f\u63f4\u3002");
        table2.put("Protocol error.  Session setup failed.", "\u901a\u8baf\u534f\u5b9a\u9519\u8bef\uff0cSession \u521d\u59cb\u5316\u5931\u8d25\u3002");
        table2.put("Backend start-up failed: {0}.", "\u540e\u7aef\u542f\u52a8\u5931\u8d25\uff1a{0}\u3002");
        table2.put("The column index is out of range: {0}, number of columns: {1}.", "\u680f\u4f4d\u7d22\u5f15\u8d85\u8fc7\u8bb8\u53ef\u8303\u56f4\uff1a{0}\uff0c\u680f\u4f4d\u6570\uff1a{1}\u3002");
        table2.put("No value specified for parameter {0}.", "\u672a\u8bbe\u5b9a\u53c2\u6570\u503c {0} \u7684\u5185\u5bb9\u3002");
        table2.put("An I/O error occured while sending to the backend.", "\u4f20\u9001\u6570\u636e\u81f3\u540e\u7aef\u65f6\u53d1\u751f I/O \u9519\u8bef\u3002");
        table2.put("Unknown Response Type {0}.", "\u4e0d\u660e\u7684\u56de\u5e94\u7c7b\u578b {0}\u3002");
        table2.put("Unable to interpret the update count in command completion tag: {0}.", "\u65e0\u6cd5\u89e3\u8bfb\u547d\u4ee4\u5b8c\u6210\u6807\u7b7e\u4e2d\u7684\u66f4\u65b0\u8ba1\u6570\uff1a{0}\u3002");
        table2.put("The server''s DateStyle parameter was changed to {0}. The JDBC driver requires DateStyle to begin with ISO for correct operation.", "\u8fd9\u670d\u52a1\u5668\u7684 DateStyle \u53c2\u6570\u88ab\u66f4\u6539\u6210 {0}\uff0cJDBC \u9a71\u52a8\u7a0b\u5e8f\u8bf7\u6c42\u9700\u8981 DateStyle \u4ee5 ISO \u5f00\u5934\u4ee5\u6b63\u786e\u5de5\u4f5c\u3002");
        table2.put("The server''s standard_conforming_strings parameter was reported as {0}. The JDBC driver expected on or off.", "\u8fd9\u670d\u52a1\u5668\u7684 standard_conforming_strings \u53c2\u6570\u5df2\u56de\u62a5\u4e3a {0}\uff0cJDBC \u9a71\u52a8\u7a0b\u5e8f\u5df2\u9884\u671f\u5f00\u542f\u6216\u662f\u5173\u95ed\u3002");
        table2.put("The driver currently does not support COPY operations.", "\u9a71\u52a8\u7a0b\u5e8f\u76ee\u524d\u4e0d\u652f\u63f4 COPY \u64cd\u4f5c\u3002");
        table2.put("This PooledConnection has already been closed.", "\u8fd9\u4e2a PooledConnection \u5df2\u7ecf\u88ab\u5173\u95ed\u3002");
        table2.put("Connection has been closed automatically because a new connection was opened for the same PooledConnection or the PooledConnection has been closed.", "Connection \u5df2\u81ea\u52a8\u7ed3\u675f\uff0c\u56e0\u4e3a\u4e00\u4e2a\u65b0\u7684  PooledConnection \u8fde\u7ebf\u88ab\u5f00\u542f\u6216\u8005\u6216 PooledConnection \u5df2\u88ab\u5173\u95ed\u3002");
        table2.put("Connection has been closed.", "Connection \u5df2\u7ecf\u88ab\u5173\u95ed\u3002");
        table2.put("Statement has been closed.", "Sstatement \u5df2\u7ecf\u88ab\u5173\u95ed\u3002");
        table2.put("DataSource has been closed.", "DataSource \u5df2\u7ecf\u88ab\u5173\u95ed\u3002");
        table2.put("Fastpath call {0} - No result was returned and we expected an integer.", "Fastpath \u547c\u53eb {0} - \u6ca1\u6709\u4f20\u56de\u503c\uff0c\u4e14\u5e94\u8be5\u4f20\u56de\u4e00\u4e2a\u6574\u6570\u3002");
        table2.put("The fastpath function {0} is unknown.", "\u4e0d\u660e\u7684 fastpath \u51fd\u5f0f {0}\u3002");
        table2.put("Conversion to type {0} failed: {1}.", "\u8f6c\u6362\u7c7b\u578b {0} \u5931\u8d25\uff1a{1}\u3002");
        table2.put("Cannot tell if path is open or closed: {0}.", "\u65e0\u6cd5\u5f97\u77e5 path \u662f\u5f00\u542f\u6216\u5173\u95ed\uff1a{0}\u3002");
        table2.put("The array index is out of range: {0}", "\u9635\u5217\u7d22\u5f15\u8d85\u8fc7\u8bb8\u53ef\u8303\u56f4\uff1a{0}");
        table2.put("The array index is out of range: {0}, number of elements: {1}.", "\u9635\u5217\u7d22\u5f15\u8d85\u8fc7\u8bb8\u53ef\u8303\u56f4\uff1a{0}\uff0c\u5143\u7d20\u6570\u91cf\uff1a{1}\u3002");
        table2.put("Invalid character data was found.  This is most likely caused by stored data containing characters that are invalid for the character set the database was created in.  The most common example of this is storing 8bit data in a SQL_ASCII database.", "\u53d1\u73b0\u4e0d\u5408\u6cd5\u7684\u5b57\u5143\uff0c\u53ef\u80fd\u7684\u539f\u56e0\u662f\u6b32\u50a8\u5b58\u7684\u6570\u636e\u4e2d\u5305\u542b\u6570\u636e\u5e93\u7684\u5b57\u5143\u96c6\u4e0d\u652f\u63f4\u7684\u5b57\u7801\uff0c\u5176\u4e2d\u6700\u5e38\u89c1\u4f8b\u5b50\u7684\u5c31\u662f\u5c06 8 \u4f4d\u5143\u6570\u636e\u5b58\u5165\u4f7f\u7528 SQL_ASCII \u7f16\u7801\u7684\u6570\u636e\u5e93\u4e2d\u3002");
        table2.put("Truncation of large objects is only implemented in 8.3 and later servers.", "\u5927\u578b\u5bf9\u8c61\u7684\u622a\u65ad(Truncation)\u4ec5\u88ab\u5b9e\u4f5c\u6267\u884c\u5728 8.3 \u548c\u540e\u6765\u7684\u670d\u52a1\u5668\u3002");
        table2.put("PostgreSQL LOBs can only index to: {0}", "PostgreSQL LOBs \u4ec5\u80fd\u7d22\u5f15\u5230\uff1a{0}");
        table2.put("Unsupported value for stringtype parameter: {0}", "\u5b57\u7b26\u7c7b\u578b\u53c2\u6570\u503c\u672a\u88ab\u652f\u6301\uff1a{0}");
        table2.put("No results were returned by the query.", "\u67e5\u8be2\u6ca1\u6709\u4f20\u56de\u4efb\u4f55\u7ed3\u679c\u3002");
        table2.put("A result was returned when none was expected.", "\u4f20\u56de\u9884\u671f\u4e4b\u5916\u7684\u7ed3\u679c\u3002");
        table2.put("Failed to create object for: {0}.", "\u4e3a {0} \u5efa\u7acb\u5bf9\u8c61\u5931\u8d25\u3002");
        table2.put("Cannot change transaction read-only property in the middle of a transaction.", "\u4e0d\u80fd\u5728\u4e8b\u7269\u4ea4\u6613\u8fc7\u7a0b\u4e2d\u6539\u53d8\u4e8b\u7269\u4ea4\u6613\u552f\u8bfb\u5c5e\u6027\u3002");
        table2.put("Cannot change transaction isolation level in the middle of a transaction.", "\u4e0d\u80fd\u5728\u4e8b\u52a1\u4ea4\u6613\u8fc7\u7a0b\u4e2d\u6539\u53d8\u4e8b\u7269\u4ea4\u6613\u9694\u7edd\u7b49\u7ea7\u3002");
        table2.put("Transaction isolation level {0} not supported.", "\u4e0d\u652f\u63f4\u4ea4\u6613\u9694\u7edd\u7b49\u7ea7 {0} \u3002");
        table2.put("Unable to translate data into the desired encoding.", "\u65e0\u6cd5\u5c06\u6570\u636e\u8f6c\u6210\u76ee\u6807\u7f16\u7801\u3002");
        table2.put("Unable to find name datatype in the system catalogs.", "\u5728\u7cfb\u7edf catalog \u4e2d\u627e\u4e0d\u5230\u540d\u79f0\u6570\u636e\u7c7b\u578b(datatype)\u3002");
        table2.put("Operation requires a scrollable ResultSet, but this ResultSet is FORWARD_ONLY.", "\u64cd\u4f5c\u8981\u6c42\u53ef\u5377\u52a8\u7684 ResultSet\uff0c\u4f46\u6b64 ResultSet \u662f FORWARD_ONLY\u3002");
        table2.put("Unexpected error while decoding character data from a large object.", "\u4ece\u5927\u578b\u5bf9\u8c61(large object)\u89e3\u7801\u5b57\u5143\u6570\u636e\u65f6\u53d1\u751f\u9519\u8bef\u3002");
        table2.put("Can''t use relative move methods while on the insert row.", "\u4e0d\u80fd\u5728\u65b0\u589e\u7684\u6570\u636e\u5217\u4e0a\u4f7f\u7528\u76f8\u5bf9\u4f4d\u7f6e move \u65b9\u6cd5\u3002");
        table2.put("Invalid fetch direction constant: {0}.", "\u65e0\u6548\u7684 fetch \u65b9\u5411\u5e38\u6570\uff1a{0}\u3002");
        table2.put("Cannot call cancelRowUpdates() when on the insert row.", "\u4e0d\u80fd\u5728\u65b0\u589e\u7684\u6570\u636e\u5217\u4e0a\u547c\u53eb cancelRowUpdates()\u3002");
        table2.put("Cannot call deleteRow() when on the insert row.", "\u4e0d\u80fd\u5728\u65b0\u589e\u7684\u6570\u636e\u4e0a\u547c\u53eb deleteRow()\u3002");
        table2.put("Currently positioned before the start of the ResultSet.  You cannot call deleteRow() here.", "\u4e0d\u80fd\u5728 ResultSet \u7684\u7b2c\u4e00\u7b14\u6570\u636e\u4e4b\u524d\u547c\u53eb deleteRow()\u3002");
        table2.put("Currently positioned after the end of the ResultSet.  You cannot call deleteRow() here.", "\u4e0d\u80fd\u5728 ResultSet \u7684\u6700\u540e\u4e00\u7b14\u6570\u636e\u4e4b\u540e\u547c\u53eb deleteRow()\u3002");
        table2.put("There are no rows in this ResultSet.", "ResultSet \u4e2d\u627e\u4e0d\u5230\u6570\u636e\u5217\u3002");
        table2.put("Not on the insert row.", "\u4e0d\u5728\u65b0\u589e\u7684\u6570\u636e\u5217\u4e0a\u3002");
        table2.put("The JVM claims not to support the encoding: {0}", "JVM \u58f0\u660e\u5e76\u4e0d\u652f\u63f4\u7f16\u7801\uff1a{0} \u3002");
        table2.put("Provided InputStream failed.", "\u63d0\u4f9b\u7684 InputStream \u5df2\u5931\u8d25\u3002");
        table2.put("Provided Reader failed.", "\u63d0\u4f9b\u7684 Reader \u5df2\u5931\u8d25\u3002");
        table2.put("Can''t refresh the insert row.", "\u65e0\u6cd5\u91cd\u8bfb\u65b0\u589e\u7684\u6570\u636e\u5217\u3002");
        table2.put("Cannot call updateRow() when on the insert row.", "\u4e0d\u80fd\u5728\u65b0\u589e\u7684\u6570\u636e\u5217\u4e0a\u547c\u53eb deleteRow()\u3002");
        table2.put("Cannot update the ResultSet because it is either before the start or after the end of the results.", "\u65e0\u6cd5\u66f4\u65b0 ResultSet\uff0c\u53ef\u80fd\u5728\u7b2c\u4e00\u7b14\u6570\u636e\u4e4b\u524d\u6216\u6700\u672a\u7b14\u6570\u636e\u4e4b\u540e\u3002");
        table2.put("ResultSets with concurrency CONCUR_READ_ONLY cannot be updated.", "ResultSets \u4e0e\u5e76\u53d1\u540c\u4f5c(Concurrency) CONCUR_READ_ONLY \u4e0d\u80fd\u88ab\u66f4\u65b0\u3002");
        table2.put("No primary key found for table {0}.", "{0} \u6570\u636e\u8868\u4e2d\u672a\u627e\u5230\u4e3b\u952e(Primary key)\u3002");
        table2.put("Fetch size must be a value greater to or equal to 0.", "\u6570\u636e\u8bfb\u53d6\u7b14\u6570(fetch size)\u5fc5\u987b\u5927\u4e8e\u6216\u7b49\u4e8e 0\u3002");
        table2.put("Bad value for type {0} : {1}", "\u4e0d\u826f\u7684\u7c7b\u578b\u503c {0} : {1}");
        table2.put("The column name {0} was not found in this ResultSet.", "ResultSet \u4e2d\u627e\u4e0d\u5230\u680f\u4f4d\u540d\u79f0 {0}\u3002");
        table2.put("ResultSet is not updateable.  The query that generated this result set must select only one table, and must select all primary keys from that table. See the JDBC 2.1 API Specification, section 5.6 for more details.", "\u4e0d\u53ef\u66f4\u65b0\u7684 ResultSet\u3002\u7528\u6765\u4ea7\u751f\u8fd9\u4e2a ResultSet \u7684 SQL \u547d\u4ee4\u53ea\u80fd\u64cd\u4f5c\u4e00\u4e2a\u6570\u636e\u8868\uff0c\u5e76\u4e14\u5fc5\u9700\u9009\u62e9\u6240\u6709\u4e3b\u952e\u680f\u4f4d\uff0c\u8be6\u7ec6\u8bf7\u53c2\u9605 JDBC 2.1 API \u89c4\u683c\u4e66 5.6 \u8282\u3002");
        table2.put("This ResultSet is closed.", "\u8fd9\u4e2a ResultSet \u5df2\u7ecf\u88ab\u5173\u95ed\u3002");
        table2.put("ResultSet not positioned properly, perhaps you need to call next.", "\u67e5\u8be2\u7ed3\u679c\u6307\u6807\u4f4d\u7f6e\u4e0d\u6b63\u786e\uff0c\u60a8\u4e5f\u8bb8\u9700\u8981\u547c\u53eb ResultSet \u7684 next() \u65b9\u6cd5\u3002");
        table2.put("Can''t use query methods that take a query string on a PreparedStatement.", "\u5728 PreparedStatement \u4e0a\u4e0d\u80fd\u4f7f\u7528\u83b7\u53d6\u67e5\u8be2\u5b57\u7b26\u7684\u67e5\u8be2\u65b9\u6cd5\u3002");
        table2.put("Multiple ResultSets were returned by the query.", "\u67e5\u8be2\u4f20\u56de\u591a\u4e2a ResultSet\u3002");
        table2.put("A CallableStatement was executed with nothing returned.", "\u4e00\u4e2a CallableStatement \u6267\u884c\u51fd\u5f0f\u540e\u6ca1\u6709\u4f20\u56de\u503c\u3002");
        table2.put("A CallableStatement function was executed and the out parameter {0} was of type {1} however type {2} was registered.", "\u4e00\u4e2a CallableStatement \u6267\u884c\u51fd\u5f0f\u540e\u8f93\u51fa\u7684\u53c2\u6570\u7c7b\u578b\u4e3a {1} \u503c\u4e3a {0}\uff0c\u4f46\u662f\u5df2\u6ce8\u518c\u7684\u7c7b\u578b\u662f {2}\u3002");
        table2.put("Maximum number of rows must be a value grater than or equal to 0.", "\u6700\u5927\u6570\u636e\u8bfb\u53d6\u7b14\u6570\u5fc5\u987b\u5927\u4e8e\u6216\u7b49\u4e8e 0\u3002");
        table2.put("Query timeout must be a value greater than or equals to 0.", "\u67e5\u8be2\u903e\u65f6\u7b49\u5019\u65f6\u95f4\u5fc5\u987b\u5927\u4e8e\u6216\u7b49\u4e8e 0\u3002");
        table2.put("The maximum field size must be a value greater than or equal to 0.", "\u6700\u5927\u680f\u4f4d\u5bb9\u91cf\u5fc5\u987b\u5927\u4e8e\u6216\u7b49\u4e8e 0\u3002");
        table2.put("Unknown Types value.", "\u4e0d\u660e\u7684\u7c7b\u578b\u503c\u3002");
        table2.put("Invalid stream length {0}.", "\u65e0\u6548\u7684\u4e32\u6d41\u957f\u5ea6 {0}.");
        table2.put("The JVM claims not to support the {0} encoding.", "JVM \u58f0\u660e\u5e76\u4e0d\u652f\u63f4 {0} \u7f16\u7801\u3002");
        table2.put("Unknown type {0}.", "\u4e0d\u660e\u7684\u7c7b\u578b {0}");
        table2.put("Cannot cast an instance of {0} to type {1}", "\u4e0d\u80fd\u8f6c\u6362\u4e00\u4e2a {0} \u5b9e\u4f8b\u5230\u7c7b\u578b {1}");
        table2.put("Unsupported Types value: {0}", "\u672a\u88ab\u652f\u6301\u7684\u7c7b\u578b\u503c\uff1a{0}");
        table2.put("This statement does not declare an OUT parameter.  Use '{' ?= call ... '}' to declare one.", "\u8fd9\u4e2a statement \u672a\u5ba3\u544a OUT \u53c2\u6570\uff0c\u4f7f\u7528 '{' ?= call ... '}' \u5ba3\u544a\u4e00\u4e2a\u3002");
        table2.put("Malformed function or procedure escape syntax at offset {0}.", "\u4e0d\u6b63\u786e\u7684\u51fd\u5f0f\u6216\u7a0b\u5e8f escape \u8bed\u6cd5\u4e8e {0}\u3002");
        table2.put("Parameter of type {0} was registered, but call to get{1} (sqltype={2}) was made.", "\u5df2\u6ce8\u518c\u53c2\u6570\u7c7b\u578b {0}\uff0c\u4f46\u662f\u53c8\u547c\u53eb\u4e86get{1}(sqltype={2})\u3002");
        table2.put("A CallableStatement was declared, but no call to registerOutParameter(1, <some type>) was made.", "\u5df2\u7ecf\u5ba3\u544a CallableStatement \u51fd\u5f0f\uff0c\u4f46\u662f\u5c1a\u672a\u547c\u53eb registerOutParameter (1, <some_type>) \u3002");
        table2.put("This statement has been closed.", "\u8fd9\u4e2a statement \u5df2\u7ecf\u88ab\u5173\u95ed\u3002");
        table2.put("Batch entry {0} {1} was aborted.  Call getNextException to see the cause.", "\u6279\u6b21\u5904\u7406 {0} {1} \u88ab\u4e2d\u6b62\uff0c\u547c\u53eb getNextException \u4ee5\u53d6\u5f97\u539f\u56e0\u3002");
        table2.put("Unexpected error writing large object to database.", "\u5c06\u5927\u578b\u5bf9\u8c61(large object)\u5199\u5165\u6570\u636e\u5e93\u65f6\u53d1\u751f\u4e0d\u660e\u9519\u8bef\u3002");
        table2.put("{0} function takes one and only one argument.", "{0} \u51fd\u5f0f\u53d6\u5f97\u4e00\u4e2a\u4e14\u4ec5\u6709\u4e00\u4e2a\u5f15\u6570\u3002");
        table2.put("{0} function takes two and only two arguments.", "{0} \u51fd\u5f0f\u53d6\u5f97\u4e8c\u4e2a\u4e14\u4ec5\u6709\u4e8c\u4e2a\u5f15\u6570\u3002");
        table2.put("{0} function takes four and only four argument.", "{0} \u51fd\u5f0f\u53d6\u5f97\u56db\u4e2a\u4e14\u4ec5\u6709\u56db\u4e2a\u5f15\u6570\u3002");
        table2.put("{0} function takes two or three arguments.", "{0} \u51fd\u5f0f\u53d6\u5f97\u4e8c\u4e2a\u6216\u4e09\u4e2a\u5f15\u6570\u3002");
        table2.put("{0} function doesn''t take any argument.", "{0} \u51fd\u5f0f\u65e0\u6cd5\u53d6\u5f97\u4efb\u4f55\u7684\u5f15\u6570\u3002");
        table2.put("{0} function takes three and only three arguments.", "{0} \u51fd\u5f0f\u53d6\u5f97\u4e09\u4e2a\u4e14\u4ec5\u6709\u4e09\u4e2a\u5f15\u6570\u3002");
        table2.put("Interval {0} not yet implemented", "\u9694\u7edd {0} \u5c1a\u672a\u88ab\u5b9e\u4f5c\u3002");
        table2.put("The class {0} does not implement org.postgresql.util.PGobject.", "\u7c7b\u522b {0} \u672a\u5b9e\u505a org.postgresql.util.PGobject\u3002");
        table2.put("Unknown ResultSet holdability setting: {0}.", "\u672a\u77e5\u7684 ResultSet \u53ef\u9002\u7528\u7684\u8bbe\u7f6e\uff1a{0}\u3002");
        table2.put("Server versions prior to 8.0 do not support savepoints.", "8.0 \u7248\u4e4b\u524d\u7684\u670d\u52a1\u5668\u4e0d\u652f\u63f4\u50a8\u5b58\u70b9(SavePints)\u3002");
        table2.put("Cannot establish a savepoint in auto-commit mode.", "\u5728\u81ea\u52a8\u786e\u8ba4\u4e8b\u7269\u4ea4\u6613\u6a21\u5f0f\u65e0\u6cd5\u5efa\u7acb\u50a8\u5b58\u70b9(Savepoint)\u3002");
        table2.put("The parameter index is out of range: {0}, number of parameters: {1}.", "\u53c2\u6570\u7d22\u5f15\u8d85\u51fa\u8bb8\u53ef\u8303\u56f4\uff1a{0}\uff0c\u53c2\u6570\u603b\u6570\uff1a{1}\u3002");
        table2.put("Cannot reference a savepoint after it has been released.", "\u65e0\u6cd5\u53c2\u7167\u5df2\u7ecf\u88ab\u91ca\u653e\u7684\u50a8\u5b58\u70b9\u3002");
        table2.put("Cannot retrieve the id of a named savepoint.", "\u65e0\u6cd5\u53d6\u5f97\u5df2\u547d\u540d\u50a8\u5b58\u70b9\u7684 id\u3002");
        table2.put("Cannot retrieve the name of an unnamed savepoint.", "\u65e0\u6cd5\u53d6\u5f97\u672a\u547d\u540d\u50a8\u5b58\u70b9(Savepoint)\u7684\u540d\u79f0\u3002");
        table2.put("Failed to initialize LargeObject API", "\u521d\u59cb\u5316 LargeObject API \u5931\u8d25");
        table2.put("Large Objects may not be used in auto-commit mode.", "\u5927\u578b\u5bf9\u8c61\u65e0\u6cd5\u88ab\u4f7f\u7528\u5728\u81ea\u52a8\u786e\u8ba4\u4e8b\u7269\u4ea4\u6613\u6a21\u5f0f\u3002");
        table2.put("Conversion of interval failed", "\u9694\u7edd(Interval)\u8f6c\u6362\u5931\u8d25\u3002");
        table2.put("Conversion of money failed.", "money \u8f6c\u6362\u5931\u8d25\u3002");
        table2.put("Detail: {0}", "\u8be6\u7ec6\uff1a{0}");
        table2.put("Hint: {0}", "\u5efa\u8bae\uff1a{0}");
        table2.put("Position: {0}", "\u4f4d\u7f6e\uff1a{0}");
        table2.put("Where: {0}", "\u5728\u4f4d\u7f6e\uff1a{0}");
        table2.put("Internal Query: {0}", "\u5185\u90e8\u67e5\u8be2\uff1a{0}");
        table2.put("Internal Position: {0}", "\u5185\u90e8\u4f4d\u7f6e\uff1a{0}");
        table2.put("Location: File: {0}, Routine: {1}, Line: {2}", "\u4f4d\u7f6e\uff1a\u6587\u4ef6\uff1a{0}\uff0c\u5e38\u5f0f\uff1a{1}\uff0c\u884c\uff1a{2}");
        table2.put("Server SQLState: {0}", "\u670d\u52a1\u5668 SQLState\uff1a{0}");
        table2.put("Invalid flags", "\u65e0\u6548\u7684\u65d7\u6807");
        table2.put("suspend/resume not implemented", "\u6682\u505c(suspend)/\u518d\u7ee7\u7eed(resume)\u5c1a\u672a\u88ab\u5b9e\u4f5c\u3002");
        table2.put("Transaction interleaving not implemented", "\u4e8b\u7269\u4ea4\u6613\u9694\u7edd(Transaction interleaving)\u672a\u88ab\u5b9e\u4f5c\u3002");
        table2.put("Server versions prior to 8.1 do not support two-phase commit.", "8.1 \u7248\u4e4b\u524d\u7684\u670d\u52a1\u5668\u4e0d\u652f\u63f4\u4e8c\u6bb5\u5f0f\u63d0\u4ea4(Two-Phase Commit)\u3002");
        table2.put("Invalid flag", "\u65e0\u6548\u7684\u65d7\u6807");
        table = table2;
    }
}
