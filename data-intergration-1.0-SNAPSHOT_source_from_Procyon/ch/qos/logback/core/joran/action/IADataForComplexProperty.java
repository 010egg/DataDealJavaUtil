// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.joran.action;

import ch.qos.logback.core.util.AggregationType;
import ch.qos.logback.core.joran.util.PropertySetter;

public class IADataForComplexProperty
{
    final PropertySetter parentBean;
    final AggregationType aggregationType;
    final String complexPropertyName;
    private Object nestedComplexProperty;
    boolean inError;
    
    public IADataForComplexProperty(final PropertySetter parentBean, final AggregationType aggregationType, final String complexPropertyName) {
        this.parentBean = parentBean;
        this.aggregationType = aggregationType;
        this.complexPropertyName = complexPropertyName;
    }
    
    public AggregationType getAggregationType() {
        return this.aggregationType;
    }
    
    public Object getNestedComplexProperty() {
        return this.nestedComplexProperty;
    }
    
    public String getComplexPropertyName() {
        return this.complexPropertyName;
    }
    
    public void setNestedComplexProperty(final Object nestedComplexProperty) {
        this.nestedComplexProperty = nestedComplexProperty;
    }
}
