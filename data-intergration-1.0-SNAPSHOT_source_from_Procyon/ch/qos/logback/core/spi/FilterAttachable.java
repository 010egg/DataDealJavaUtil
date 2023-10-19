// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.spi;

import java.util.List;
import ch.qos.logback.core.filter.Filter;

public interface FilterAttachable<E>
{
    void addFilter(final Filter<E> p0);
    
    void clearAllFilters();
    
    List<Filter<E>> getCopyOfAttachedFiltersList();
    
    FilterReply getFilterChainDecision(final E p0);
}
