// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.joran.util.beans;

import java.util.Collections;
import java.lang.reflect.Method;
import java.util.Map;

public class BeanDescription
{
    private final Class<?> clazz;
    private final Map<String, Method> propertyNameToGetter;
    private final Map<String, Method> propertyNameToSetter;
    private final Map<String, Method> propertyNameToAdder;
    
    protected BeanDescription(final Class<?> clazz, final Map<String, Method> propertyNameToGetter, final Map<String, Method> propertyNameToSetter, final Map<String, Method> propertyNameToAdder) {
        this.clazz = clazz;
        this.propertyNameToGetter = Collections.unmodifiableMap((Map<? extends String, ? extends Method>)propertyNameToGetter);
        this.propertyNameToSetter = Collections.unmodifiableMap((Map<? extends String, ? extends Method>)propertyNameToSetter);
        this.propertyNameToAdder = Collections.unmodifiableMap((Map<? extends String, ? extends Method>)propertyNameToAdder);
    }
    
    public Class<?> getClazz() {
        return this.clazz;
    }
    
    public Map<String, Method> getPropertyNameToGetter() {
        return this.propertyNameToGetter;
    }
    
    public Map<String, Method> getPropertyNameToSetter() {
        return this.propertyNameToSetter;
    }
    
    public Method getGetter(final String propertyName) {
        return this.propertyNameToGetter.get(propertyName);
    }
    
    public Method getSetter(final String propertyName) {
        return this.propertyNameToSetter.get(propertyName);
    }
    
    public Map<String, Method> getPropertyNameToAdder() {
        return this.propertyNameToAdder;
    }
    
    public Method getAdder(final String propertyName) {
        return this.propertyNameToAdder.get(propertyName);
    }
}
