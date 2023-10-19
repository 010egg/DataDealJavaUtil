// 
// Decompiled by Procyon v0.5.36
// 

package com.google.errorprone.annotations;

import javax.lang.model.element.Modifier;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Documented;
import java.lang.annotation.Annotation;

@Documented
@Retention(RetentionPolicy.CLASS)
@Target({ ElementType.ANNOTATION_TYPE })
public @interface RequiredModifiers {
    Modifier[] value();
}
