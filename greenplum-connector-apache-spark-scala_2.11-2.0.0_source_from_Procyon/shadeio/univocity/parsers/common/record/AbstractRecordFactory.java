// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common.record;

import shadeio.univocity.parsers.common.Context;

public abstract class AbstractRecordFactory<R extends Record, M extends RecordMetaData>
{
    protected final M metaData;
    
    public AbstractRecordFactory(final Context context) {
        this.metaData = this.createMetaData(context);
    }
    
    public abstract R newRecord(final String[] p0);
    
    public abstract M createMetaData(final Context p0);
    
    public final M getRecordMetaData() {
        return this.metaData;
    }
}
