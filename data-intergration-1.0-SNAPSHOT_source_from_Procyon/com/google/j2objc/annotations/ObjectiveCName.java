// 
// Decompiled by Procyon v0.5.36
// 

package com.google.j2objc.annotations;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.Annotation;

@Target({ ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.PACKAGE })
@Retention(RetentionPolicy.CLASS)
public @interface ObjectiveCName {
    String value();
}
