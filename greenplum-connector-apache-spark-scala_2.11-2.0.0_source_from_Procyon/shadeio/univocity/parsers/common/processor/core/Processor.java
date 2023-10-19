// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common.processor.core;

import shadeio.univocity.parsers.common.Context;

public interface Processor<T extends Context>
{
    void processStarted(final T p0);
    
    void rowProcessed(final String[] p0, final T p1);
    
    void processEnded(final T p0);
}
