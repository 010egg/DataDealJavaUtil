// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.util;

import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.Connection;

public class JdbcBlackHole
{
    public static void close(final Connection con) {
        try {
            if (con != null) {
                con.close();
            }
        }
        catch (SQLException ex) {}
    }
    
    public static void close(final Statement s) {
        try {
            if (s != null) {
                s.close();
            }
        }
        catch (SQLException ex) {}
    }
    
    public static void close(final ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        }
        catch (SQLException ex) {}
    }
}
