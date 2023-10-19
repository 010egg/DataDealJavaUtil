// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.replication.fluent.logical;

import java.sql.SQLException;
import org.postgresql.replication.PGReplicationStream;

public interface StartLogicalReplicationCallback
{
    PGReplicationStream start(final LogicalReplicationOptions p0) throws SQLException;
}
