// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.jackson;

import org.apache.logging.log4j.ThreadContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.apache.logging.log4j.core.impl.ThrowableProxy;
import org.apache.logging.log4j.core.impl.ExtendedStackTraceElement;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import com.fasterxml.jackson.databind.Module;

class Initializers
{
    static class SetupContextInitializer
    {
        void setupModule(final Module.SetupContext context, final boolean includeStacktrace) {
            context.setMixInAnnotations(StackTraceElement.class, StackTraceElementMixIn.class);
            context.setMixInAnnotations(Marker.class, MarkerMixIn.class);
            context.setMixInAnnotations(Level.class, LevelMixIn.class);
            context.setMixInAnnotations(LogEvent.class, LogEventWithContextListMixIn.class);
            context.setMixInAnnotations(ExtendedStackTraceElement.class, ExtendedStackTraceElementMixIn.class);
            context.setMixInAnnotations(ThrowableProxy.class, (Class<?>)(includeStacktrace ? ThrowableProxyMixIn.class : ThrowableProxyWithoutStacktraceMixIn.class));
        }
    }
    
    static class SetupContextJsonInitializer
    {
        void setupModule(final Module.SetupContext context, final boolean includeStacktrace) {
            context.setMixInAnnotations(StackTraceElement.class, StackTraceElementMixIn.class);
            context.setMixInAnnotations(Marker.class, MarkerMixIn.class);
            context.setMixInAnnotations(Level.class, LevelMixIn.class);
            context.setMixInAnnotations(LogEvent.class, LogEventJsonMixIn.class);
            context.setMixInAnnotations(ExtendedStackTraceElement.class, ExtendedStackTraceElementMixIn.class);
            context.setMixInAnnotations(ThrowableProxy.class, (Class<?>)(includeStacktrace ? ThrowableProxyMixIn.class : ThrowableProxyWithoutStacktraceMixIn.class));
        }
    }
    
    static class SimpleModuleInitializer
    {
        void initialize(final SimpleModule simpleModule) {
            simpleModule.addDeserializer(StackTraceElement.class, new Log4jStackTraceElementDeserializer());
            simpleModule.addDeserializer(ThreadContext.ContextStack.class, new MutableThreadContextStackDeserializer());
        }
    }
}
