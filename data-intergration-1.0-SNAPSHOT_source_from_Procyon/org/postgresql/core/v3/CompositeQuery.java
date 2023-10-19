// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.core.v3;

import org.postgresql.core.ParameterList;

class CompositeQuery implements V3Query
{
    private final SimpleQuery[] subqueries;
    private final int[] offsets;
    
    CompositeQuery(final SimpleQuery[] subqueries, final int[] offsets) {
        this.subqueries = subqueries;
        this.offsets = offsets;
    }
    
    @Override
    public ParameterList createParameterList() {
        final SimpleParameterList[] subparams = new SimpleParameterList[this.subqueries.length];
        for (int i = 0; i < this.subqueries.length; ++i) {
            subparams[i] = (SimpleParameterList)this.subqueries[i].createParameterList();
        }
        return new CompositeParameterList(subparams, this.offsets);
    }
    
    @Override
    public String toString(final ParameterList parameters) {
        final StringBuilder sbuf = new StringBuilder(this.subqueries[0].toString());
        for (int i = 1; i < this.subqueries.length; ++i) {
            sbuf.append(';');
            sbuf.append(this.subqueries[i]);
        }
        return sbuf.toString();
    }
    
    @Override
    public String toString() {
        return this.toString(null);
    }
    
    @Override
    public void close() {
        for (int i = 0; i < this.subqueries.length; ++i) {
            this.subqueries[i].close();
        }
    }
    
    @Override
    public SimpleQuery[] getSubqueries() {
        return this.subqueries;
    }
    
    @Override
    public boolean isStatementDescribed() {
        for (int i = 0; i < this.subqueries.length; ++i) {
            if (!this.subqueries[i].isStatementDescribed()) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean isEmpty() {
        for (int i = 0; i < this.subqueries.length; ++i) {
            if (!this.subqueries[i].isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
