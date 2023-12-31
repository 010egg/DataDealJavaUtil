// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.filter;

import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.LifeCycle2;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.core.util.ObjectArrayIterator;
import java.util.Iterator;
import java.util.List;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.AbstractLifeCycle;

@Plugin(name = "filters", category = "Core", printObject = true)
public final class CompositeFilter extends AbstractLifeCycle implements Iterable<Filter>, Filter
{
    private static final Filter[] EMPTY_FILTERS;
    private final Filter[] filters;
    
    private CompositeFilter() {
        this.filters = CompositeFilter.EMPTY_FILTERS;
    }
    
    private CompositeFilter(final Filter[] filters) {
        this.filters = ((filters == null) ? CompositeFilter.EMPTY_FILTERS : filters);
    }
    
    public CompositeFilter addFilter(final Filter filter) {
        if (filter == null) {
            return this;
        }
        if (filter instanceof CompositeFilter) {
            final int size = this.filters.length + ((CompositeFilter)filter).size();
            final Filter[] copy = Arrays.copyOf(this.filters, size);
            final int index = this.filters.length;
            for (final Filter currentFilter : ((CompositeFilter)filter).filters) {
                copy[index] = currentFilter;
            }
            return new CompositeFilter(copy);
        }
        final Filter[] copy2 = Arrays.copyOf(this.filters, this.filters.length + 1);
        copy2[this.filters.length] = filter;
        return new CompositeFilter(copy2);
    }
    
    public CompositeFilter removeFilter(final Filter filter) {
        if (filter == null) {
            return this;
        }
        final List<Filter> filterList = new ArrayList<Filter>(Arrays.asList(this.filters));
        if (filter instanceof CompositeFilter) {
            for (final Filter currentFilter : ((CompositeFilter)filter).filters) {
                filterList.remove(currentFilter);
            }
        }
        else {
            filterList.remove(filter);
        }
        return new CompositeFilter(filterList.toArray(new Filter[this.filters.length - 1]));
    }
    
    @Override
    public Iterator<Filter> iterator() {
        return new ObjectArrayIterator<Filter>(this.filters);
    }
    
    @Deprecated
    public List<Filter> getFilters() {
        return Arrays.asList(this.filters);
    }
    
    public Filter[] getFiltersArray() {
        return this.filters;
    }
    
    public boolean isEmpty() {
        return this.filters.length == 0;
    }
    
    public int size() {
        return this.filters.length;
    }
    
    @Override
    public void start() {
        this.setStarting();
        for (final Filter filter : this.filters) {
            filter.start();
        }
        this.setStarted();
    }
    
    @Override
    public boolean stop(final long timeout, final TimeUnit timeUnit) {
        this.setStopping();
        for (final Filter filter : this.filters) {
            if (filter instanceof LifeCycle2) {
                ((LifeCycle2)filter).stop(timeout, timeUnit);
            }
            else {
                filter.stop();
            }
        }
        this.setStopped();
        return true;
    }
    
    @Override
    public Result getOnMismatch() {
        return Result.NEUTRAL;
    }
    
    @Override
    public Result getOnMatch() {
        return Result.NEUTRAL;
    }
    
    @Override
    public Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object... params) {
        Result result = Result.NEUTRAL;
        for (int i = 0; i < this.filters.length; ++i) {
            result = this.filters[i].filter(logger, level, marker, msg, params);
            if (result == Result.ACCEPT || result == Result.DENY) {
                return result;
            }
        }
        return result;
    }
    
    @Override
    public Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0) {
        Result result = Result.NEUTRAL;
        for (int i = 0; i < this.filters.length; ++i) {
            result = this.filters[i].filter(logger, level, marker, msg, p0);
            if (result == Result.ACCEPT || result == Result.DENY) {
                return result;
            }
        }
        return result;
    }
    
    @Override
    public Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1) {
        Result result = Result.NEUTRAL;
        for (int i = 0; i < this.filters.length; ++i) {
            result = this.filters[i].filter(logger, level, marker, msg, p0, p1);
            if (result == Result.ACCEPT || result == Result.DENY) {
                return result;
            }
        }
        return result;
    }
    
    @Override
    public Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1, final Object p2) {
        Result result = Result.NEUTRAL;
        for (int i = 0; i < this.filters.length; ++i) {
            result = this.filters[i].filter(logger, level, marker, msg, p0, p1, p2);
            if (result == Result.ACCEPT || result == Result.DENY) {
                return result;
            }
        }
        return result;
    }
    
    @Override
    public Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1, final Object p2, final Object p3) {
        Result result = Result.NEUTRAL;
        for (int i = 0; i < this.filters.length; ++i) {
            result = this.filters[i].filter(logger, level, marker, msg, p0, p1, p2, p3);
            if (result == Result.ACCEPT || result == Result.DENY) {
                return result;
            }
        }
        return result;
    }
    
    @Override
    public Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4) {
        Result result = Result.NEUTRAL;
        for (int i = 0; i < this.filters.length; ++i) {
            result = this.filters[i].filter(logger, level, marker, msg, p0, p1, p2, p3, p4);
            if (result == Result.ACCEPT || result == Result.DENY) {
                return result;
            }
        }
        return result;
    }
    
    @Override
    public Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5) {
        Result result = Result.NEUTRAL;
        for (int i = 0; i < this.filters.length; ++i) {
            result = this.filters[i].filter(logger, level, marker, msg, p0, p1, p2, p3, p4, p5);
            if (result == Result.ACCEPT || result == Result.DENY) {
                return result;
            }
        }
        return result;
    }
    
    @Override
    public Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6) {
        Result result = Result.NEUTRAL;
        for (int i = 0; i < this.filters.length; ++i) {
            result = this.filters[i].filter(logger, level, marker, msg, p0, p1, p2, p3, p4, p5, p6);
            if (result == Result.ACCEPT || result == Result.DENY) {
                return result;
            }
        }
        return result;
    }
    
    @Override
    public Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7) {
        Result result = Result.NEUTRAL;
        for (int i = 0; i < this.filters.length; ++i) {
            result = this.filters[i].filter(logger, level, marker, msg, p0, p1, p2, p3, p4, p5, p6, p7);
            if (result == Result.ACCEPT || result == Result.DENY) {
                return result;
            }
        }
        return result;
    }
    
    @Override
    public Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8) {
        Result result = Result.NEUTRAL;
        for (int i = 0; i < this.filters.length; ++i) {
            result = this.filters[i].filter(logger, level, marker, msg, p0, p1, p2, p3, p4, p5, p6, p7, p8);
            if (result == Result.ACCEPT || result == Result.DENY) {
                return result;
            }
        }
        return result;
    }
    
    @Override
    public Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8, final Object p9) {
        Result result = Result.NEUTRAL;
        for (int i = 0; i < this.filters.length; ++i) {
            result = this.filters[i].filter(logger, level, marker, msg, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
            if (result == Result.ACCEPT || result == Result.DENY) {
                return result;
            }
        }
        return result;
    }
    
    @Override
    public Result filter(final Logger logger, final Level level, final Marker marker, final Object msg, final Throwable t) {
        Result result = Result.NEUTRAL;
        for (int i = 0; i < this.filters.length; ++i) {
            result = this.filters[i].filter(logger, level, marker, msg, t);
            if (result == Result.ACCEPT || result == Result.DENY) {
                return result;
            }
        }
        return result;
    }
    
    @Override
    public Result filter(final Logger logger, final Level level, final Marker marker, final Message msg, final Throwable t) {
        Result result = Result.NEUTRAL;
        for (int i = 0; i < this.filters.length; ++i) {
            result = this.filters[i].filter(logger, level, marker, msg, t);
            if (result == Result.ACCEPT || result == Result.DENY) {
                return result;
            }
        }
        return result;
    }
    
    @Override
    public Result filter(final LogEvent event) {
        Result result = Result.NEUTRAL;
        for (int i = 0; i < this.filters.length; ++i) {
            result = this.filters[i].filter(event);
            if (result == Result.ACCEPT || result == Result.DENY) {
                return result;
            }
        }
        return result;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.filters.length; ++i) {
            if (sb.length() == 0) {
                sb.append('{');
            }
            else {
                sb.append(", ");
            }
            sb.append(this.filters[i].toString());
        }
        if (sb.length() > 0) {
            sb.append('}');
        }
        return sb.toString();
    }
    
    @PluginFactory
    public static CompositeFilter createFilters(@PluginElement("Filters") final Filter[] filters) {
        return new CompositeFilter(filters);
    }
    
    static {
        EMPTY_FILTERS = new Filter[0];
    }
}
