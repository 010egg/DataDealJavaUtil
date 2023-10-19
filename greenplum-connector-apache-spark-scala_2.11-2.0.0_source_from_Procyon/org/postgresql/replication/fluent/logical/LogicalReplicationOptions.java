// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.replication.fluent.logical;

import java.util.Properties;
import org.postgresql.replication.fluent.CommonOptions;

public interface LogicalReplicationOptions extends CommonOptions
{
    String getSlotName();
    
    Properties getSlotOptions();
}
