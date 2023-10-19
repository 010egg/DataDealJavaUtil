// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common.processor.core;

import java.util.ArrayList;
import java.util.Collections;
import shadeio.univocity.parsers.annotations.helpers.MethodFilter;
import java.util.List;
import shadeio.univocity.parsers.common.Context;

public abstract class AbstractBeanListProcessor<T, C extends Context> extends AbstractBeanProcessor<T, C>
{
    private List<T> beans;
    private String[] headers;
    private final int expectedBeanCount;
    
    public AbstractBeanListProcessor(final Class<T> beanType) {
        this(beanType, 0);
    }
    
    public AbstractBeanListProcessor(final Class<T> beanType, final int expectedBeanCount) {
        super(beanType, MethodFilter.ONLY_SETTERS);
        this.expectedBeanCount = ((expectedBeanCount <= 0) ? 10000 : expectedBeanCount);
    }
    
    @Override
    public void beanProcessed(final T bean, final C context) {
        this.beans.add(bean);
    }
    
    public List<T> getBeans() {
        return (this.beans == null) ? Collections.emptyList() : this.beans;
    }
    
    @Override
    public void processStarted(final C context) {
        super.processStarted(context);
        this.beans = new ArrayList<T>(this.expectedBeanCount);
    }
    
    @Override
    public void processEnded(final C context) {
        this.headers = context.headers();
        super.processEnded(context);
    }
    
    public String[] getHeaders() {
        return this.headers;
    }
}
