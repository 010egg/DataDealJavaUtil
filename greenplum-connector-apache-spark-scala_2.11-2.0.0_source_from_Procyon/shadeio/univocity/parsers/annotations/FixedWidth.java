// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.annotations;

import shadeio.univocity.parsers.fixed.FieldAlignment;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.Inherited;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;

@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE })
public @interface FixedWidth {
    int value() default -1;
    
    FieldAlignment alignment() default FieldAlignment.LEFT;
    
    char padding() default ' ';
    
    int from() default -1;
    
    int to() default -1;
}
