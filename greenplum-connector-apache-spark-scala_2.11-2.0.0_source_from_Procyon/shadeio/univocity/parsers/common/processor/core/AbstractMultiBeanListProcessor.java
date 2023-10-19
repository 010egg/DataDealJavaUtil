// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common.processor.core;

import java.util.LinkedHashMap;
import java.util.Arrays;
import shadeio.univocity.parsers.common.ArgumentUtils;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import shadeio.univocity.parsers.common.Context;

public class AbstractMultiBeanListProcessor<C extends Context> extends AbstractMultiBeanRowProcessor<C>
{
    private final Class[] beanTypes;
    private final List[] beans;
    private String[] headers;
    private final int expectedBeanCount;
    
    public AbstractMultiBeanListProcessor(final int expectedBeanCount, final Class... beanTypes) {
        super((Class[])beanTypes);
        this.beanTypes = beanTypes;
        this.beans = new List[beanTypes.length];
        this.expectedBeanCount = ((expectedBeanCount <= 0) ? 10000 : expectedBeanCount);
    }
    
    public AbstractMultiBeanListProcessor(final Class... beanTypes) {
        this(0, (Class[])beanTypes);
    }
    
    @Override
    public final void processStarted(final C context) {
        super.processStarted(context);
        for (int i = 0; i < this.beanTypes.length; ++i) {
            this.beans[i] = new ArrayList(this.expectedBeanCount);
        }
    }
    
    @Override
    protected final void rowProcessed(final Map<Class<?>, Object> row, final C context) {
        for (int i = 0; i < this.beanTypes.length; ++i) {
            final Object bean = row.get(this.beanTypes[i]);
            this.beans[i].add(bean);
        }
    }
    
    @Override
    public final void processEnded(final C context) {
        this.headers = context.headers();
        super.processEnded(context);
    }
    
    public final String[] getHeaders() {
        return this.headers;
    }
    
    public <T> List<T> getBeans(final Class<T> beanType) {
        final int index = ArgumentUtils.indexOf(this.beanTypes, beanType);
        if (index == -1) {
            throw new IllegalArgumentException("Unknown bean type '" + beanType.getSimpleName() + "'. Available types are: " + Arrays.toString(this.beanTypes));
        }
        return (List<T>)this.beans[index];
    }
    
    public Map<Class<?>, List<?>> getBeans() {
        final LinkedHashMap<Class<?>, List<?>> out = new LinkedHashMap<Class<?>, List<?>>();
        for (int i = 0; i < this.beanTypes.length; ++i) {
            out.put(this.beanTypes[i], this.beans[i]);
        }
        return out;
    }
}
