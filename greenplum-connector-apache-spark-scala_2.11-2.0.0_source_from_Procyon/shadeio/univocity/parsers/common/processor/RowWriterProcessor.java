// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common.processor;

public interface RowWriterProcessor<T>
{
    Object[] write(final T p0, final String[] p1, final int[] p2);
}
