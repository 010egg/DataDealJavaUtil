// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.replication.fluent.physical;

import org.postgresql.replication.fluent.ChainedCommonCreateSlotBuilder;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import org.postgresql.replication.LogSequenceNumber;
import org.postgresql.replication.ReplicationType;
import org.postgresql.replication.ReplicationSlotInfo;
import org.postgresql.core.BaseConnection;
import org.postgresql.replication.fluent.AbstractCreateSlotBuilder;

public class PhysicalCreateSlotBuilder extends AbstractCreateSlotBuilder<ChainedPhysicalCreateSlotBuilder> implements ChainedPhysicalCreateSlotBuilder
{
    public PhysicalCreateSlotBuilder(final BaseConnection connection) {
        super(connection);
    }
    
    @Override
    protected ChainedPhysicalCreateSlotBuilder self() {
        return this;
    }
    
    @Override
    public ReplicationSlotInfo make() throws SQLException {
        if (this.slotName == null || this.slotName.isEmpty()) {
            throw new IllegalArgumentException("Replication slotName can't be null");
        }
        final Statement statement = this.connection.createStatement();
        ResultSet result = null;
        ReplicationSlotInfo slotInfo = null;
        try {
            statement.execute(String.format("CREATE_REPLICATION_SLOT %s %s PHYSICAL", this.slotName, this.temporaryOption ? "TEMPORARY" : ""));
            result = statement.getResultSet();
            if (result != null && result.next()) {
                slotInfo = new ReplicationSlotInfo(result.getString("slot_name"), ReplicationType.PHYSICAL, LogSequenceNumber.valueOf(result.getString("consistent_point")), result.getString("snapshot_name"), result.getString("output_plugin"));
            }
        }
        finally {
            if (result != null) {
                result.close();
            }
            statement.close();
        }
        return slotInfo;
    }
}
