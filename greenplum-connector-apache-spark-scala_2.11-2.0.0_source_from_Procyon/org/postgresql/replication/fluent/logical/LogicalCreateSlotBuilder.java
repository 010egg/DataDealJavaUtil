// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.replication.fluent.logical;

import org.postgresql.replication.fluent.ChainedCommonCreateSlotBuilder;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import org.postgresql.replication.LogSequenceNumber;
import org.postgresql.replication.ReplicationType;
import org.postgresql.replication.ReplicationSlotInfo;
import org.postgresql.core.BaseConnection;
import org.postgresql.replication.fluent.AbstractCreateSlotBuilder;

public class LogicalCreateSlotBuilder extends AbstractCreateSlotBuilder<ChainedLogicalCreateSlotBuilder> implements ChainedLogicalCreateSlotBuilder
{
    private String outputPlugin;
    
    public LogicalCreateSlotBuilder(final BaseConnection connection) {
        super(connection);
    }
    
    @Override
    protected ChainedLogicalCreateSlotBuilder self() {
        return this;
    }
    
    @Override
    public ChainedLogicalCreateSlotBuilder withOutputPlugin(final String outputPlugin) {
        this.outputPlugin = outputPlugin;
        return this.self();
    }
    
    @Override
    public ReplicationSlotInfo make() throws SQLException {
        if (this.outputPlugin == null || this.outputPlugin.isEmpty()) {
            throw new IllegalArgumentException("OutputPlugin required parameter for logical replication slot");
        }
        if (this.slotName == null || this.slotName.isEmpty()) {
            throw new IllegalArgumentException("Replication slotName can't be null");
        }
        final Statement statement = this.connection.createStatement();
        ResultSet result = null;
        ReplicationSlotInfo slotInfo = null;
        try {
            statement.execute(String.format("CREATE_REPLICATION_SLOT %s %s LOGICAL %s", this.slotName, this.temporaryOption ? "TEMPORARY" : "", this.outputPlugin));
            result = statement.getResultSet();
            if (result != null && result.next()) {
                slotInfo = new ReplicationSlotInfo(result.getString("slot_name"), ReplicationType.LOGICAL, LogSequenceNumber.valueOf(result.getString("consistent_point")), result.getString("snapshot_name"), result.getString("output_plugin"));
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
