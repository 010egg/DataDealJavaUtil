// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common.processor.core;

import java.util.List;
import java.util.ArrayList;
import shadeio.univocity.parsers.common.fields.FieldSet;
import shadeio.univocity.parsers.conversions.Conversion;
import shadeio.univocity.parsers.annotations.helpers.MethodFilter;
import shadeio.univocity.parsers.common.ArgumentUtils;
import java.util.HashMap;
import java.util.Map;
import shadeio.univocity.parsers.common.ConversionProcessor;
import shadeio.univocity.parsers.common.Context;

public abstract class AbstractMultiBeanProcessor<C extends Context> implements Processor<C>, ConversionProcessor
{
    private final AbstractBeanProcessor<?, C>[] beanProcessors;
    private final Map<Class, AbstractBeanProcessor> processorMap;
    
    public AbstractMultiBeanProcessor(final Class... beanTypes) {
        this.processorMap = new HashMap<Class, AbstractBeanProcessor>();
        ArgumentUtils.noNulls("Bean types", (Class[])beanTypes);
        this.beanProcessors = (AbstractBeanProcessor<?, C>[])new AbstractBeanProcessor[beanTypes.length];
        for (int i = 0; i < beanTypes.length; ++i) {
            final Class type = beanTypes[i];
            this.beanProcessors[i] = new AbstractBeanProcessor<Object, C>(type, MethodFilter.ONLY_SETTERS) {
                @Override
                public void beanProcessed(final Object bean, final C context) {
                    AbstractMultiBeanProcessor.this.beanProcessed(type, bean, context);
                }
            };
            this.processorMap.put(type, this.beanProcessors[i]);
        }
    }
    
    public final Class[] getBeanClasses() {
        final Class[] classes = new Class[this.beanProcessors.length];
        for (int i = 0; i < this.beanProcessors.length; ++i) {
            classes[i] = this.beanProcessors[i].beanClass;
        }
        return classes;
    }
    
    public <T> AbstractBeanProcessor<T, C> getProcessorOfType(final Class<T> type) {
        final AbstractBeanProcessor<T, C> processor = this.processorMap.get(type);
        if (processor == null) {
            throw new IllegalArgumentException("No processor of type '" + type.getName() + "' is available. Supported types are: " + this.processorMap.keySet());
        }
        return processor;
    }
    
    public abstract void beanProcessed(final Class<?> p0, final Object p1, final C p2);
    
    @Override
    public void processStarted(final C context) {
        for (int i = 0; i < this.beanProcessors.length; ++i) {
            this.beanProcessors[i].processStarted(context);
        }
    }
    
    @Override
    public final void rowProcessed(final String[] row, final C context) {
        for (int i = 0; i < this.beanProcessors.length; ++i) {
            this.beanProcessors[i].rowProcessed(row, context);
        }
    }
    
    @Override
    public void processEnded(final C context) {
        for (int i = 0; i < this.beanProcessors.length; ++i) {
            this.beanProcessors[i].processEnded(context);
        }
    }
    
    @Override
    public FieldSet<Integer> convertIndexes(final Conversion... conversions) {
        final List<FieldSet<Integer>> sets = new ArrayList<FieldSet<Integer>>(this.beanProcessors.length);
        for (int i = 0; i < this.beanProcessors.length; ++i) {
            sets.add(this.beanProcessors[i].convertIndexes(conversions));
        }
        return new FieldSet<Integer>(sets);
    }
    
    @Override
    public void convertAll(final Conversion... conversions) {
        for (int i = 0; i < this.beanProcessors.length; ++i) {
            this.beanProcessors[i].convertAll(conversions);
        }
    }
    
    @Override
    public FieldSet<String> convertFields(final Conversion... conversions) {
        final List<FieldSet<String>> sets = new ArrayList<FieldSet<String>>(this.beanProcessors.length);
        for (int i = 0; i < this.beanProcessors.length; ++i) {
            sets.add(this.beanProcessors[i].convertFields(conversions));
        }
        return new FieldSet<String>(sets);
    }
    
    @Override
    public void convertType(final Class<?> type, final Conversion... conversions) {
        for (int i = 0; i < this.beanProcessors.length; ++i) {
            this.beanProcessors[i].convertType(type, conversions);
        }
    }
}
