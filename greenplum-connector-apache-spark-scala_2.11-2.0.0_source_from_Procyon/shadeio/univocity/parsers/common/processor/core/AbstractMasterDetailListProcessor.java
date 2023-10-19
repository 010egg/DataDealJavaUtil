// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common.processor.core;

import java.util.ArrayList;
import shadeio.univocity.parsers.common.processor.RowPlacement;
import shadeio.univocity.parsers.common.processor.MasterDetailRecord;
import java.util.List;
import shadeio.univocity.parsers.common.Context;

public abstract class AbstractMasterDetailListProcessor<T extends Context> extends AbstractMasterDetailProcessor<T>
{
    private final List<MasterDetailRecord> records;
    private String[] headers;
    
    public AbstractMasterDetailListProcessor(final RowPlacement rowPlacement, final AbstractObjectListProcessor detailProcessor) {
        super(rowPlacement, detailProcessor);
        this.records = new ArrayList<MasterDetailRecord>();
    }
    
    public AbstractMasterDetailListProcessor(final AbstractObjectListProcessor detailProcessor) {
        super(detailProcessor);
        this.records = new ArrayList<MasterDetailRecord>();
    }
    
    @Override
    protected void masterDetailRecordProcessed(final MasterDetailRecord record, final T context) {
        this.records.add(record);
    }
    
    @Override
    public void processEnded(final T context) {
        this.headers = context.headers();
        super.processEnded(context);
    }
    
    public List<MasterDetailRecord> getRecords() {
        return this.records;
    }
    
    public String[] getHeaders() {
        return this.headers;
    }
}
