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
@Target({ ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.ANNOTATION_TYPE })
public @interface JSONCompiler {
    CompilerOption value() default CompilerOption.DEFAULT;
    
    public enum CompilerOption
    {
        DEFAULT, 
        LAMBDA;
        
        private static /* synthetic */ CompilerOption[] $values() {
            return new CompilerOption[] { CompilerOption.DEFAULT, CompilerOption.LAMBDA };
        }
        
        static {
            $VALUES = $values();
        }
    }
}
