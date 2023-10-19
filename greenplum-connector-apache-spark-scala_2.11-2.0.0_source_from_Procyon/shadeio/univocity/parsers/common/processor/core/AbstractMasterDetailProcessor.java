// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common.processor.core;

import java.util.List;
import java.util.Collection;
import java.util.ArrayList;
import shadeio.univocity.parsers.common.ArgumentUtils;
import shadeio.univocity.parsers.common.processor.RowPlacement;
import shadeio.univocity.parsers.common.processor.MasterDetailRecord;
import shadeio.univocity.parsers.common.Context;

public abstract class AbstractMasterDetailProcessor<T extends Context> extends AbstractObjectProcessor<T>
{
    private final AbstractObjectListProcessor detailProcessor;
    private MasterDetailRecord record;
    private final boolean isMasterRowAboveDetail;
    
    public AbstractMasterDetailProcessor(final RowPlacement rowPlacement, final AbstractObjectListProcessor detailProcessor) {
        ArgumentUtils.noNulls("Row processor for reading detail rows", detailProcessor);
        this.detailProcessor = detailProcessor;
        this.isMasterRowAboveDetail = (rowPlacement == RowPlacement.TOP);
    }
    
    public AbstractMasterDetailProcessor(final AbstractObjectListProcessor detailProcessor) {
        this(RowPlacement.TOP, detailProcessor);
    }
    
    @Override
    public void processStarted(final T context) {
        this.detailProcessor.processStarted(context);
    }
    
    @Override
    public final void rowProcessed(final String[] row, final T context) {
        if (this.isMasterRecord(row, context)) {
            super.rowProcessed(row, context);
        }
        else {
            if (this.isMasterRowAboveDetail && this.record == null) {
                return;
            }
            this.detailProcessor.rowProcessed(row, context);
        }
    }
    
    @Override
    public final void rowProcessed(final Object[] row, final T context) {
        if (this.record == null) {
            (this.record = new MasterDetailRecord()).setMasterRow(row);
            if (this.isMasterRowAboveDetail) {
                return;
            }
        }
        this.processRecord(row, context);
    }
    
    private void processRecord(final Object[] row, final T context) {
        final List<Object[]> detailRows = (List<Object[]>)this.detailProcessor.getRows();
        this.record.setDetailRows(new ArrayList<Object[]>(detailRows));
        if (!this.isMasterRowAboveDetail) {
            this.record.setMasterRow(row);
        }
        if (this.record.getMasterRow() != null) {
            this.masterDetailRecordProcessed(this.record.clone(), context);
            this.record.clear();
        }
        detailRows.clear();
        if (this.isMasterRowAboveDetail) {
            this.record.setMasterRow(row);
        }
    }
    
    @Override
    public void processEnded(final T context) {
        super.processEnded(context);
        this.detailProcessor.processEnded(context);
        if (this.isMasterRowAboveDetail) {
            this.processRecord(null, context);
        }
    }
    
    protected abstract boolean isMasterRecord(final String[] p0, final T p1);
    
    protected abstract void masterDetailRecordProcessed(final MasterDetailRecord p0, final T p1);
}
