// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface JSONBuilder {
    String buildMethod() default "build";
    
    String withPrefix() default "with";
}
