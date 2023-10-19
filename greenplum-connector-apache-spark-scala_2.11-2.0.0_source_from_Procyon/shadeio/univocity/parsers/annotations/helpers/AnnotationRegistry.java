// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.annotations.helpers;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Map;

public class AnnotationRegistry
{
    private static final Map<AnnotatedElement, FieldAnnotations> modifiedAnnotations;
    
    static final synchronized void setValue(final AnnotatedElement annotatedElement, final Annotation annotation, final String attribute, final Object newValue) {
        FieldAnnotations attributes = AnnotationRegistry.modifiedAnnotations.get(annotatedElement);
        if (attributes == null) {
            attributes = new FieldAnnotations();
            AnnotationRegistry.modifiedAnnotations.put(annotatedElement, attributes);
        }
        attributes.setValue(annotation, attribute, newValue);
    }
    
    public static final synchronized <T> T getValue(final AnnotatedElement annotatedElement, final Annotation annotation, final String attribute, final T valueIfNull) {
        if (annotatedElement == null) {
            return valueIfNull;
        }
        final Object value = getValue(annotatedElement, annotation, attribute);
        if (value == null) {
            return valueIfNull;
        }
        return (T)value;
    }
    
    static final synchronized Object getValue(final AnnotatedElement annotatedElement, final Annotation annotation, final String attribute) {
        final FieldAnnotations attributes = AnnotationRegistry.modifiedAnnotations.get(annotatedElement);
        if (attributes == null) {
            return null;
        }
        return attributes.getValue(annotation, attribute);
    }
    
    public static final void reset() {
        AnnotationRegistry.modifiedAnnotations.clear();
    }
    
    static {
        modifiedAnnotations = new HashMap<AnnotatedElement, FieldAnnotations>();
    }
    
    private static class FieldAnnotations
    {
        private Map<Annotation, AnnotationAttributes> annotations;
        
        private FieldAnnotations() {
            this.annotations = new HashMap<Annotation, AnnotationAttributes>();
        }
        
        private void setValue(final Annotation annotation, final String attribute, final Object newValue) {
            AnnotationAttributes attributes = this.annotations.get(annotation);
            if (attributes == null) {
                attributes = new AnnotationAttributes();
                this.annotations.put(annotation, attributes);
            }
            attributes.setAttribute(attribute, newValue);
        }
        
        private Object getValue(final Annotation annotation, final String attribute) {
            final AnnotationAttributes attributes = this.annotations.get(annotation);
            if (attributes == null) {
                return null;
            }
            return attributes.getAttribute(attribute);
        }
    }
    
    private static class AnnotationAttributes
    {
        private Map<String, Object> attributes;
        
        private AnnotationAttributes() {
            this.attributes = new HashMap<String, Object>();
        }
        
        private void setAttribute(final String attribute, final Object newValue) {
            if (!this.attributes.containsKey(attribute)) {
                this.attributes.put(attribute, newValue);
            }
            else {
                final Object existingValue = this.attributes.get(attribute);
                if (existingValue == null || newValue == null) {
                    return;
                }
                final Class originalClass = existingValue.getClass();
                final Class newClass = newValue.getClass();
                if (originalClass != newClass && newClass.isArray() && newClass.getComponentType() == existingValue.getClass()) {
                    final Object array = Array.newInstance(originalClass, 1);
                    Array.set(array, 0, existingValue);
                    this.attributes.put(attribute, array);
                }
            }
        }
        
        private Object getAttribute(final String attribute) {
            return this.attributes.get(attribute);
        }
    }
}
