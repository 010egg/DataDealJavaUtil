// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.copy;

import java.sql.SQLException;

public interface CopyOut extends CopyOperation
{
    byte[] readFromCopy() throws SQLException;
    
    byte[] readFromCopy(final boolean p0) throws SQLException;
}
