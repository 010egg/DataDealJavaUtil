// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common;

public interface IterableResult<T, C extends Context> extends Iterable<T>
{
    C getContext();
    
    ResultIterator<T, C> iterator();
}
