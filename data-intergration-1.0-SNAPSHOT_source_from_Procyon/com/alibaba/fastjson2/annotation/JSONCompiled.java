// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.annotation;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;

@Retention(RetentionPolicy.RUNTIME)
public @interface JSONCompiled {
    boolean referenceDetect() default true;
    
    boolean smartMatch() default true;
}
