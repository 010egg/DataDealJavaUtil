// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common.processor.core;

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import shadeio.univocity.parsers.common.Context;

public abstract class AbstractObjectListProcessor<T extends Context> extends AbstractObjectProcessor<T>
{
    private List<Object[]> rows;
    private String[] headers;
    private final int expectedRowCount;
    
    public AbstractObjectListProcessor() {
        this(0);
    }
    
    public AbstractObjectListProcessor(final int expectedRowCount) {
        this.expectedRowCount = ((expectedRowCount <= 0) ? 10000 : expectedRowCount);
    }
    
    @Override
    public void processStarted(final T context) {
        super.processStarted(context);
        this.rows = new ArrayList<Object[]>(this.expectedRowCount);
    }
    
    @Override
    public void rowProcessed(final Object[] row, final T context) {
        this.rows.add(row);
    }
    
    @Override
    public void processEnded(final T context) {
        super.processEnded(context);
        this.headers = context.headers();
    }
    
    public List<Object[]> getRows() {
        return (this.rows == null) ? Collections.emptyList() : this.rows;
    }
    
    public String[] getHeaders() {
        return this.headers;
    }
}
