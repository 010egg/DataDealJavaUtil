// 
// Decompiled by Procyon v0.5.36
// 

package javax.servlet.annotation;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.Annotation;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface MultipartConfig {
    String location() default "";
    
    long maxFileSize() default -1L;
    
    long maxRequestSize() default -1L;
    
    int fileSizeThreshold() default 0;
}
