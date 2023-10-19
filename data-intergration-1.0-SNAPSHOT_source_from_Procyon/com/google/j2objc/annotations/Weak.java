// 
// Decompiled by Procyon v0.5.36
// 

package com.google.j2objc.annotations;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.Annotation;

@Target({ ElementType.FIELD, ElementType.LOCAL_VARIABLE, ElementType.PARAMETER })
@Retention(RetentionPolicy.CLASS)
public @interface Weak {
}
