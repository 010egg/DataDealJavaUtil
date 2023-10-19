// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common;

import java.util.Iterator;

public interface ResultIterator<T, C extends Context> extends Iterator<T>
{
    C getContext();
}
