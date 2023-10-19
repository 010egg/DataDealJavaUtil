// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.util;

import ch.qos.logback.core.net.ssl.SSLNestedComponentRegistryRules;
import ch.qos.logback.classic.boolex.JaninoEventEvaluator;
import ch.qos.logback.core.filter.EvaluatorFilter;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.joran.spi.DefaultNestedComponentRegistry;

public class DefaultNestedComponentRules
{
    public static void addDefaultNestedComponentRegistryRules(final DefaultNestedComponentRegistry registry) {
        registry.add(AppenderBase.class, "layout", PatternLayout.class);
        registry.add(UnsynchronizedAppenderBase.class, "layout", PatternLayout.class);
        registry.add(AppenderBase.class, "encoder", PatternLayoutEncoder.class);
        registry.add(UnsynchronizedAppenderBase.class, "encoder", PatternLayoutEncoder.class);
        registry.add(EvaluatorFilter.class, "evaluator", JaninoEventEvaluator.class);
        SSLNestedComponentRegistryRules.addDefaultNestedComponentRegistryRules(registry);
    }
}
