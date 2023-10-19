// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.joran.action;

import ch.qos.logback.core.util.AggregationType;
import ch.qos.logback.core.joran.util.PropertySetter;

class IADataForBasicProperty
{
    final PropertySetter parentBean;
    final AggregationType aggregationType;
    final String propertyName;
    boolean inError;
    
    IADataForBasicProperty(final PropertySetter parentBean, final AggregationType aggregationType, final String propertyName) {
        this.parentBean = parentBean;
        this.aggregationType = aggregationType;
        this.propertyName = propertyName;
    }
}
