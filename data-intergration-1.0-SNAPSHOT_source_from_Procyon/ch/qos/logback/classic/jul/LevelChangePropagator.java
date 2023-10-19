// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.jul;

import java.util.Iterator;
import java.util.List;
import java.util.Enumeration;
import java.util.logging.LogManager;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import java.util.HashSet;
import java.util.logging.Logger;
import java.util.Set;
import ch.qos.logback.core.spi.LifeCycle;
import ch.qos.logback.classic.spi.LoggerContextListener;
import ch.qos.logback.core.spi.ContextAwareBase;

public class LevelChangePropagator extends ContextAwareBase implements LoggerContextListener, LifeCycle
{
    private Set<Logger> julLoggerSet;
    boolean isStarted;
    boolean resetJUL;
    
    public LevelChangePropagator() {
        this.julLoggerSet = new HashSet<Logger>();
        this.isStarted = false;
        this.resetJUL = false;
    }
    
    public void setResetJUL(final boolean resetJUL) {
        this.resetJUL = resetJUL;
    }
    
    @Override
    public boolean isResetResistant() {
        return false;
    }
    
    @Override
    public void onStart(final LoggerContext context) {
    }
    
    @Override
    public void onReset(final LoggerContext context) {
    }
    
    @Override
    public void onStop(final LoggerContext context) {
    }
    
    @Override
    public void onLevelChange(final ch.qos.logback.classic.Logger logger, final Level level) {
        this.propagate(logger, level);
    }
    
    private void propagate(final ch.qos.logback.classic.Logger logger, final Level level) {
        this.addInfo("Propagating " + level + " level on " + logger + " onto the JUL framework");
        final Logger julLogger = JULHelper.asJULLogger(logger);
        this.julLoggerSet.add(julLogger);
        final java.util.logging.Level julLevel = JULHelper.asJULLevel(level);
        julLogger.setLevel(julLevel);
    }
    
    public void resetJULLevels() {
        final LogManager lm = LogManager.getLogManager();
        final Enumeration<String> e = lm.getLoggerNames();
        while (e.hasMoreElements()) {
            final String loggerName = e.nextElement();
            final Logger julLogger = lm.getLogger(loggerName);
            if (JULHelper.isRegularNonRootLogger(julLogger) && julLogger.getLevel() != null) {
                this.addInfo("Setting level of jul logger [" + loggerName + "] to null");
                julLogger.setLevel(null);
            }
        }
    }
    
    private void propagateExistingLoggerLevels() {
        final LoggerContext loggerContext = (LoggerContext)this.context;
        final List<ch.qos.logback.classic.Logger> loggerList = loggerContext.getLoggerList();
        for (final ch.qos.logback.classic.Logger l : loggerList) {
            if (l.getLevel() != null) {
                this.propagate(l, l.getLevel());
            }
        }
    }
    
    @Override
    public void start() {
        if (this.resetJUL) {
            this.resetJULLevels();
        }
        this.propagateExistingLoggerLevels();
        this.isStarted = true;
    }
    
    @Override
    public void stop() {
        this.isStarted = false;
    }
    
    @Override
    public boolean isStarted() {
        return this.isStarted;
    }
}
