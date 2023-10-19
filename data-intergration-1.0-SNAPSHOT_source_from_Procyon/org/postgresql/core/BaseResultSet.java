// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.core;

import java.sql.SQLException;
import java.sql.ResultSet;

public interface BaseResultSet extends ResultSet
{
    String getFixedString(final int p0) throws SQLException;
}
