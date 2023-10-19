// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common.record;

import shadeio.univocity.parsers.common.Context;

public class RecordFactory extends AbstractRecordFactory<Record, RecordMetaDataImpl>
{
    public RecordFactory(final Context context) {
        super(context);
    }
    
    @Override
    public Record newRecord(final String[] data) {
        return new RecordImpl<Object>(data, (RecordMetaDataImpl)this.metaData);
    }
    
    @Override
    public RecordMetaDataImpl createMetaData(final Context context) {
        return new RecordMetaDataImpl((C)context);
    }
}
