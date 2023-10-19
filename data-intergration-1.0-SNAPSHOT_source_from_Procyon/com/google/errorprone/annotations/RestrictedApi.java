// 
// Decompiled by Procyon v0.5.36
// 

package com.google.errorprone.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.Annotation;

@Target({ ElementType.CONSTRUCTOR, ElementType.METHOD })
public @interface RestrictedApi {
    String checkerName() default "RestrictedApi";
    
    String explanation();
    
    String link();
    
    String allowedOnPath() default "";
    
    Class<? extends Annotation>[] whitelistAnnotations() default {};
    
    Class<? extends Annotation>[] whitelistWithWarningAnnotations() default {};
}
