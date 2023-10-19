// 
// Decompiled by Procyon v0.5.36
// 

package org.slf4j.impl;

import org.slf4j.ILoggerFactory;
import ch.qos.logback.core.util.StatusPrinter;
import ch.qos.logback.core.Context;
import ch.qos.logback.core.status.StatusUtil;
import ch.qos.logback.core.joran.spi.JoranException;
import org.slf4j.helpers.Util;
import ch.qos.logback.classic.util.ContextInitializer;
import ch.qos.logback.classic.util.ContextSelectorStaticBinder;
import ch.qos.logback.classic.LoggerContext;
import org.slf4j.spi.LoggerFactoryBinder;

public class StaticLoggerBinder implements LoggerFactoryBinder
{
    public static String REQUESTED_API_VERSION;
    static final String NULL_CS_URL = "http://logback.qos.ch/codes.html#null_CS";
    private static StaticLoggerBinder SINGLETON;
    private static Object KEY;
    private boolean initialized;
    private LoggerContext defaultLoggerContext;
    private final ContextSelectorStaticBinder contextSelectorBinder;
    
    static {
        StaticLoggerBinder.REQUESTED_API_VERSION = "1.7.16";
        StaticLoggerBinder.SINGLETON = new StaticLoggerBinder();
        StaticLoggerBinder.KEY = new Object();
        StaticLoggerBinder.SINGLETON.init();
    }
    
    private StaticLoggerBinder() {
        this.initialized = false;
        this.defaultLoggerContext = new LoggerContext();
        this.contextSelectorBinder = ContextSelectorStaticBinder.getSingleton();
        this.defaultLoggerContext.setName("default");
    }
    
    public static StaticLoggerBinder getSingleton() {
        return StaticLoggerBinder.SINGLETON;
    }
    
    static void reset() {
        (StaticLoggerBinder.SINGLETON = new StaticLoggerBinder()).init();
    }
    
    void init() {
        try {
            try {
                new ContextInitializer(this.defaultLoggerContext).autoConfig();
            }
            catch (JoranException je) {
                Util.report("Failed to auto configure default logger context", je);
            }
            if (!StatusUtil.contextHasStatusListener(this.defaultLoggerContext)) {
                StatusPrinter.printInCaseOfErrorsOrWarnings(this.defaultLoggerContext);
            }
            this.contextSelectorBinder.init(this.defaultLoggerContext, StaticLoggerBinder.KEY);
            this.initialized = true;
        }
        catch (Throwable t) {
            Util.report("Failed to instantiate [" + LoggerContext.class.getName() + "]", t);
        }
    }
    
    @Override
    public ILoggerFactory getLoggerFactory() {
        if (!this.initialized) {
            return this.defaultLoggerContext;
        }
        if (this.contextSelectorBinder.getContextSelector() == null) {
            throw new IllegalStateException("contextSelector cannot be null. See also http://logback.qos.ch/codes.html#null_CS");
        }
        return this.contextSelectorBinder.getContextSelector().getLoggerContext();
    }
    
    @Override
    public String getLoggerFactoryClassStr() {
        return this.contextSelectorBinder.getClass().getName();
    }
}
