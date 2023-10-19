// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common.routine;

public final class InputDimension
{
    long rows;
    int columns;
    
    InputDimension() {
    }
    
    public final long rowCount() {
        return this.rows;
    }
    
    public final int columnCount() {
        return this.columns;
    }
}
