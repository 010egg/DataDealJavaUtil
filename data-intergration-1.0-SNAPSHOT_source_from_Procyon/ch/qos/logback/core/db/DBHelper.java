// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.db;

import java.sql.Statement;
import java.sql.SQLException;
import java.sql.Connection;

public class DBHelper
{
    public static void closeConnection(final Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            }
            catch (SQLException ex) {}
        }
    }
    
    public static void closeStatement(final Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            }
            catch (SQLException ex) {}
        }
    }
}
