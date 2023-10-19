// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.annotations.helpers;

import java.lang.annotation.Annotation;
import shadeio.univocity.parsers.annotations.Parsed;
import shadeio.univocity.parsers.annotations.HeaderTransformer;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.lang.reflect.AnnotatedElement;

public class TransformedHeader
{
    private final AnnotatedElement target;
    private final Field field;
    private final Method method;
    private final HeaderTransformer transformer;
    private int index;
    
    public TransformedHeader(final AnnotatedElement target, final HeaderTransformer transformer) {
        this.index = -2;
        if (target instanceof Field) {
            this.field = (Field)target;
            this.method = null;
        }
        else {
            this.method = (Method)target;
            this.field = null;
        }
        this.target = target;
        this.transformer = transformer;
    }
    
    public String getHeaderName() {
        if (this.target == null) {
            return null;
        }
        String name = null;
        final Parsed annotation = AnnotationHelper.findAnnotation(this.target, Parsed.class);
        if (annotation != null) {
            final String[] field = AnnotationRegistry.getValue(this.target, annotation, "field", annotation.field());
            if (field.length == 0) {
                name = this.getTargetName();
            }
            else {
                name = field[0];
            }
            if (name.length() == 0) {
                name = this.getTargetName();
            }
        }
        if (this.transformer == null) {
            return name;
        }
        if (this.field != null) {
            return this.transformer.transformName(this.field, name);
        }
        return this.transformer.transformName(this.method, name);
    }
    
    public int getHeaderIndex() {
        if (this.index == -2) {
            final Parsed annotation = AnnotationHelper.findAnnotation(this.target, Parsed.class);
            if (annotation != null) {
                this.index = AnnotationRegistry.getValue(this.target, annotation, "index", annotation.index());
                if (this.index != -1 && this.transformer != null) {
                    if (this.field != null) {
                        this.index = this.transformer.transformIndex(this.field, this.index);
                    }
                    else {
                        this.index = this.transformer.transformIndex(this.method, this.index);
                    }
                }
            }
            else {
                this.index = -1;
            }
        }
        return this.index;
    }
    
    public String getTargetName() {
        if (this.target == null) {
            return null;
        }
        if (this.field != null) {
            return this.field.getName();
        }
        return this.method.getName();
    }
    
    public AnnotatedElement getTarget() {
        return this.target;
    }
    
    public boolean isWriteOnly() {
        return this.method != null && this.method.getParameterTypes().length != 0;
    }
    
    public boolean isReadOly() {
        return this.method != null && this.method.getParameterTypes().length == 0 && this.method.getReturnType() != Void.TYPE;
    }
    
    public String describe() {
        return AnnotationHelper.describeElement(this.target);
    }
}
