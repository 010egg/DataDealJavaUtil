// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common.processor;

import java.util.Collections;
import java.util.List;

public class MasterDetailRecord implements Cloneable
{
    private Object[] masterRow;
    private List<Object[]> detailRows;
    
    public MasterDetailRecord() {
        this.masterRow = null;
        this.detailRows = Collections.emptyList();
    }
    
    public Object[] getMasterRow() {
        return this.masterRow;
    }
    
    public void setMasterRow(final Object[] masterRow) {
        this.masterRow = masterRow;
    }
    
    public List<Object[]> getDetailRows() {
        return this.detailRows;
    }
    
    public void setDetailRows(final List<Object[]> detailRows) {
        this.detailRows = detailRows;
    }
    
    public void clear() {
        this.detailRows = Collections.emptyList();
        this.masterRow = null;
    }
    
    public MasterDetailRecord clone() {
        try {
            return (MasterDetailRecord)super.clone();
        }
        catch (CloneNotSupportedException e) {
            throw new InternalError(e.getMessage());
        }
    }
}
