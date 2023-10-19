// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common.processor.core;

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import shadeio.univocity.parsers.common.Context;

public abstract class AbstractListProcessor<T extends Context> implements Processor<T>
{
    private List<String[]> rows;
    private String[] headers;
    private final int expectedRowCount;
    
    public AbstractListProcessor() {
        this(0);
    }
    
    public AbstractListProcessor(final int expectedRowCount) {
        this.expectedRowCount = ((expectedRowCount <= 0) ? 10000 : expectedRowCount);
    }
    
    @Override
    public void processStarted(final T context) {
        this.rows = new ArrayList<String[]>(this.expectedRowCount);
    }
    
    @Override
    public void rowProcessed(final String[] row, final T context) {
        this.rows.add(row);
    }
    
    @Override
    public void processEnded(final T context) {
        this.headers = context.headers();
    }
    
    public List<String[]> getRows() {
        return (this.rows == null) ? Collections.emptyList() : this.rows;
    }
    
    public String[] getHeaders() {
        return this.headers;
    }
}
