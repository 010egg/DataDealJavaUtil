// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.joran.util.beans;

import java.util.Map;
import java.lang.reflect.Method;
import java.util.HashMap;

public class BeanDescriptionFactory
{
    public static final BeanDescriptionFactory INSTANCE;
    private BeanUtil beanUtil;
    
    public BeanDescriptionFactory() {
        this.beanUtil = BeanUtil.INSTANCE;
    }
    
    public BeanDescription create(final Class<?> clazz) {
        final Map<String, Method> propertyNameToGetter = new HashMap<String, Method>();
        final Map<String, Method> propertyNameToSetter = new HashMap<String, Method>();
        final Map<String, Method> propertyNameToAdder = new HashMap<String, Method>();
        final Method[] arr$;
        final Method[] methods = arr$ = clazz.getMethods();
        for (final Method method : arr$) {
            if (this.beanUtil.isGetter(method)) {
                final String propertyName = this.beanUtil.getPropertyName(method);
                final Method oldGetter = propertyNameToGetter.put(propertyName, method);
                if (oldGetter != null) {
                    if (oldGetter.getName().startsWith("is")) {
                        propertyNameToGetter.put(propertyName, oldGetter);
                    }
                    final String message = String.format("Warning: Class '%s' contains multiple getters for the same property '%s'.", clazz.getCanonicalName(), propertyName);
                    System.err.println(message);
                }
            }
            else if (this.beanUtil.isSetter(method)) {
                final String propertyName = this.beanUtil.getPropertyName(method);
                final Method oldSetter = propertyNameToSetter.put(propertyName, method);
                if (oldSetter != null) {
                    final String message = String.format("Warning: Class '%s' contains multiple setters for the same property '%s'.", clazz.getCanonicalName(), propertyName);
                    System.err.println(message);
                }
            }
            else if (this.beanUtil.isAdder(method)) {
                final String propertyName = this.beanUtil.getPropertyName(method);
                final Method oldAdder = propertyNameToAdder.put(propertyName, method);
                if (oldAdder != null) {
                    final String message = String.format("Warning: Class '%s' contains multiple adders for the same property '%s'.", clazz.getCanonicalName(), propertyName);
                    System.err.println(message);
                }
            }
        }
        return new BeanDescription(clazz, propertyNameToGetter, propertyNameToSetter, propertyNameToAdder);
    }
    
    static {
        INSTANCE = new BeanDescriptionFactory();
    }
}
