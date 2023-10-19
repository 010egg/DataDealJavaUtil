// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common;

public abstract class ContextSnapshot<T extends Context> extends ContextWrapper<T>
{
    private final int currentColumn;
    private final long currentRecord;
    
    public ContextSnapshot(final T context) {
        super(context);
        this.currentColumn = context.currentColumn();
        this.currentRecord = context.currentRecord();
    }
    
    @Override
    public int currentColumn() {
        return this.currentColumn;
    }
    
    @Override
    public long currentRecord() {
        return this.currentRecord;
    }
}
