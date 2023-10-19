// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.annotations;

import shadeio.univocity.parsers.conversions.Validator;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.Inherited;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;

@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE })
public @interface Validate {
    boolean nullable() default false;
    
    boolean allowBlanks() default false;
    
    String matches() default "";
    
    String[] oneOf() default {};
    
    String[] noneOf() default {};
    
    Class<? extends Validator>[] validators() default {};
}
