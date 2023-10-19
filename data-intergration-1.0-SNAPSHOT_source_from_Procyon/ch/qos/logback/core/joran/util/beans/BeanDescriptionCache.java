// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.joran.util.beans;

import java.util.HashMap;
import java.util.Map;

public class BeanDescriptionCache
{
    private Map<Class<?>, BeanDescription> classToBeanDescription;
    
    public BeanDescriptionCache() {
        this.classToBeanDescription = new HashMap<Class<?>, BeanDescription>();
    }
    
    public BeanDescription getBeanDescription(final Class<?> clazz) {
        if (!this.classToBeanDescription.containsKey(clazz)) {
            final BeanDescription beanDescription = BeanDescriptionFactory.INSTANCE.create(clazz);
            this.classToBeanDescription.put(clazz, beanDescription);
        }
        return this.classToBeanDescription.get(clazz);
    }
}
