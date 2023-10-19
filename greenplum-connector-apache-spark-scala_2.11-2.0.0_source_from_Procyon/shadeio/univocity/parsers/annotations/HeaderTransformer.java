// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.annotations;

import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.lang.reflect.AnnotatedElement;

public abstract class HeaderTransformer
{
    public final String transformName(final AnnotatedElement element, final String name) {
        if (element instanceof Field) {
            return this.transformName((Field)element, name);
        }
        return this.transformName((Method)element, name);
    }
    
    public final int transformIndex(final AnnotatedElement element, final int index) {
        if (element instanceof Field) {
            return this.transformIndex((Field)element, index);
        }
        return this.transformIndex((Method)element, index);
    }
    
    public String transformName(final Field field, final String name) {
        return name;
    }
    
    public int transformIndex(final Field field, final int index) {
        return index;
    }
    
    public String transformName(final Method method, final String name) {
        return name;
    }
    
    public int transformIndex(final Method method, final int index) {
        return index;
    }
}
