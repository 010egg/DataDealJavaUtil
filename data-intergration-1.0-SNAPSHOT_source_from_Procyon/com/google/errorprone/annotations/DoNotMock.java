// 
// Decompiled by Procyon v0.5.36
// 

package com.google.errorprone.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Annotation;

@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE })
public @interface DoNotMock {
    String value() default "Create a real instance instead";
}
