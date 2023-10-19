// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.filter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface AutoLoad {
    boolean value() default true;
}
