// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic;

import java.io.ObjectStreamException;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.spi.FilterReply;
import org.slf4j.Marker;
import java.util.concurrent.CopyOnWriteArrayList;
import ch.qos.logback.classic.util.LoggerNameUtil;
import java.util.Collections;
import java.util.Iterator;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.spi.AppenderAttachableImpl;
import java.util.List;
import java.io.Serializable;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.spi.AppenderAttachable;
import org.slf4j.spi.LocationAwareLogger;

public final class Logger implements org.slf4j.Logger, LocationAwareLogger, AppenderAttachable<ILoggingEvent>, Serializable
{
    private static final long serialVersionUID = 5454405123156820674L;
    public static final String FQCN;
    private String name;
    private transient Level level;
    private transient int effectiveLevelInt;
    private transient Logger parent;
    private transient List<Logger> childrenList;
    private transient AppenderAttachableImpl<ILoggingEvent> aai;
    private transient boolean additive;
    final transient LoggerContext loggerContext;
    
    static {
        FQCN = Logger.class.getName();
    }
    
    Logger(final String name, final Logger parent, final LoggerContext loggerContext) {
        this.additive = true;
        this.name = name;
        this.parent = parent;
        this.loggerContext = loggerContext;
    }
    
    public Level getEffectiveLevel() {
        return Level.toLevel(this.effectiveLevelInt);
    }
    
    int getEffectiveLevelInt() {
        return this.effectiveLevelInt;
    }
    
    public Level getLevel() {
        return this.level;
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    private boolean isRootLogger() {
        return this.parent == null;
    }
    
    Logger getChildByName(final String childName) {
        if (this.childrenList == null) {
            return null;
        }
        for (int len = this.childrenList.size(), i = 0; i < len; ++i) {
            final Logger childLogger_i = this.childrenList.get(i);
            final String childName_i = childLogger_i.getName();
            if (childName.equals(childName_i)) {
                return childLogger_i;
            }
        }
        return null;
    }
    
    public synchronized void setLevel(Level newLevel) {
        if (this.level == newLevel) {
            return;
        }
        if (newLevel == null && this.isRootLogger()) {
            throw new IllegalArgumentException("The level of the root logger cannot be set to null");
        }
        if ((this.level = newLevel) == null) {
            this.effectiveLevelInt = this.parent.effectiveLevelInt;
            newLevel = this.parent.getEffectiveLevel();
        }
        else {
            this.effectiveLevelInt = newLevel.levelInt;
        }
        if (this.childrenList != null) {
            for (int len = this.childrenList.size(), i = 0; i < len; ++i) {
                final Logger child = this.childrenList.get(i);
                child.handleParentLevelChange(this.effectiveLevelInt);
            }
        }
        this.loggerContext.fireOnLevelChange(this, newLevel);
    }
    
    private synchronized void handleParentLevelChange(final int newParentLevelInt) {
        if (this.level == null) {
            this.effectiveLevelInt = newParentLevelInt;
            if (this.childrenList != null) {
                for (int len = this.childrenList.size(), i = 0; i < len; ++i) {
                    final Logger child = this.childrenList.get(i);
                    child.handleParentLevelChange(newParentLevelInt);
                }
            }
        }
    }
    
    @Override
    public void detachAndStopAllAppenders() {
        if (this.aai != null) {
            this.aai.detachAndStopAllAppenders();
        }
    }
    
    @Override
    public boolean detachAppender(final String name) {
        return this.aai != null && this.aai.detachAppender(name);
    }
    
    @Override
    public synchronized void addAppender(final Appender<ILoggingEvent> newAppender) {
        if (this.aai == null) {
            this.aai = new AppenderAttachableImpl<ILoggingEvent>();
        }
        this.aai.addAppender(newAppender);
    }
    
    @Override
    public boolean isAttached(final Appender<ILoggingEvent> appender) {
        return this.aai != null && this.aai.isAttached(appender);
    }
    
    @Override
    public Iterator<Appender<ILoggingEvent>> iteratorForAppenders() {
        if (this.aai == null) {
            return Collections.EMPTY_LIST.iterator();
        }
        return this.aai.iteratorForAppenders();
    }
    
    @Override
    public Appender<ILoggingEvent> getAppender(final String name) {
        if (this.aai == null) {
            return null;
        }
        return this.aai.getAppender(name);
    }
    
    public void callAppenders(final ILoggingEvent event) {
        int writes = 0;
        for (Logger l = this; l != null; l = l.parent) {
            writes += l.appendLoopOnAppenders(event);
            if (!l.additive) {
                break;
            }
        }
        if (writes == 0) {
            this.loggerContext.noAppenderDefinedWarning(this);
        }
    }
    
    private int appendLoopOnAppenders(final ILoggingEvent event) {
        if (this.aai != null) {
            return this.aai.appendLoopOnAppenders(event);
        }
        return 0;
    }
    
    @Override
    public boolean detachAppender(final Appender<ILoggingEvent> appender) {
        return this.aai != null && this.aai.detachAppender(appender);
    }
    
    Logger createChildByLastNamePart(final String lastPart) {
        final int i_index = LoggerNameUtil.getFirstSeparatorIndexOf(lastPart);
        if (i_index != -1) {
            throw new IllegalArgumentException("Child name [" + lastPart + " passed as parameter, may not include [" + '.' + "]");
        }
        if (this.childrenList == null) {
            this.childrenList = new CopyOnWriteArrayList<Logger>();
        }
        Logger childLogger;
        if (this.isRootLogger()) {
            childLogger = new Logger(lastPart, this, this.loggerContext);
        }
        else {
            childLogger = new Logger(String.valueOf(this.name) + '.' + lastPart, this, this.loggerContext);
        }
        this.childrenList.add(childLogger);
        childLogger.effectiveLevelInt = this.effectiveLevelInt;
        return childLogger;
    }
    
    private void localLevelReset() {
        this.effectiveLevelInt = 10000;
        if (this.isRootLogger()) {
            this.level = Level.DEBUG;
        }
        else {
            this.level = null;
        }
    }
    
    void recursiveReset() {
        this.detachAndStopAllAppenders();
        this.localLevelReset();
        this.additive = true;
        if (this.childrenList == null) {
            return;
        }
        for (final Logger childLogger : this.childrenList) {
            childLogger.recursiveReset();
        }
    }
    
    Logger createChildByName(final String childName) {
        final int i_index = LoggerNameUtil.getSeparatorIndexOf(childName, this.name.length() + 1);
        if (i_index != -1) {
            throw new IllegalArgumentException("For logger [" + this.name + "] child name [" + childName + " passed as parameter, may not include '.' after index" + (this.name.length() + 1));
        }
        if (this.childrenList == null) {
            this.childrenList = new CopyOnWriteArrayList<Logger>();
        }
        final Logger childLogger = new Logger(childName, this, this.loggerContext);
        this.childrenList.add(childLogger);
        childLogger.effectiveLevelInt = this.effectiveLevelInt;
        return childLogger;
    }
    
    private void filterAndLog_0_Or3Plus(final String localFQCN, final Marker marker, final Level level, final String msg, final Object[] params, final Throwable t) {
        final FilterReply decision = this.loggerContext.getTurboFilterChainDecision_0_3OrMore(marker, this, level, msg, params, t);
        if (decision == FilterReply.NEUTRAL) {
            if (this.effectiveLevelInt > level.levelInt) {
                return;
            }
        }
        else if (decision == FilterReply.DENY) {
            return;
        }
        this.buildLoggingEventAndAppend(localFQCN, marker, level, msg, params, t);
    }
    
    private void filterAndLog_1(final String localFQCN, final Marker marker, final Level level, final String msg, final Object param, final Throwable t) {
        final FilterReply decision = this.loggerContext.getTurboFilterChainDecision_1(marker, this, level, msg, param, t);
        if (decision == FilterReply.NEUTRAL) {
            if (this.effectiveLevelInt > level.levelInt) {
                return;
            }
        }
        else if (decision == FilterReply.DENY) {
            return;
        }
        this.buildLoggingEventAndAppend(localFQCN, marker, level, msg, new Object[] { param }, t);
    }
    
    private void filterAndLog_2(final String localFQCN, final Marker marker, final Level level, final String msg, final Object param1, final Object param2, final Throwable t) {
        final FilterReply decision = this.loggerContext.getTurboFilterChainDecision_2(marker, this, level, msg, param1, param2, t);
        if (decision == FilterReply.NEUTRAL) {
            if (this.effectiveLevelInt > level.levelInt) {
                return;
            }
        }
        else if (decision == FilterReply.DENY) {
            return;
        }
        this.buildLoggingEventAndAppend(localFQCN, marker, level, msg, new Object[] { param1, param2 }, t);
    }
    
    private void buildLoggingEventAndAppend(final String localFQCN, final Marker marker, final Level level, final String msg, final Object[] params, final Throwable t) {
        final LoggingEvent le = new LoggingEvent(localFQCN, this, level, msg, t, params);
        le.setMarker(marker);
        this.callAppenders(le);
    }
    
    @Override
    public void trace(final String msg) {
        this.filterAndLog_0_Or3Plus(Logger.FQCN, null, Level.TRACE, msg, null, null);
    }
    
    @Override
    public void trace(final String format, final Object arg) {
        this.filterAndLog_1(Logger.FQCN, null, Level.TRACE, format, arg, null);
    }
    
    @Override
    public void trace(final String format, final Object arg1, final Object arg2) {
        this.filterAndLog_2(Logger.FQCN, null, Level.TRACE, format, arg1, arg2, null);
    }
    
    @Override
    public void trace(final String format, final Object... argArray) {
        this.filterAndLog_0_Or3Plus(Logger.FQCN, null, Level.TRACE, format, argArray, null);
    }
    
    @Override
    public void trace(final String msg, final Throwable t) {
        this.filterAndLog_0_Or3Plus(Logger.FQCN, null, Level.TRACE, msg, null, t);
    }
    
    @Override
    public void trace(final Marker marker, final String msg) {
        this.filterAndLog_0_Or3Plus(Logger.FQCN, marker, Level.TRACE, msg, null, null);
    }
    
    @Override
    public void trace(final Marker marker, final String format, final Object arg) {
        this.filterAndLog_1(Logger.FQCN, marker, Level.TRACE, format, arg, null);
    }
    
    @Override
    public void trace(final Marker marker, final String format, final Object arg1, final Object arg2) {
        this.filterAndLog_2(Logger.FQCN, marker, Level.TRACE, format, arg1, arg2, null);
    }
    
    @Override
    public void trace(final Marker marker, final String format, final Object... argArray) {
        this.filterAndLog_0_Or3Plus(Logger.FQCN, marker, Level.TRACE, format, argArray, null);
    }
    
    @Override
    public void trace(final Marker marker, final String msg, final Throwable t) {
        this.filterAndLog_0_Or3Plus(Logger.FQCN, marker, Level.TRACE, msg, null, t);
    }
    
    @Override
    public boolean isDebugEnabled() {
        return this.isDebugEnabled(null);
    }
    
    @Override
    public boolean isDebugEnabled(final Marker marker) {
        final FilterReply decision = this.callTurboFilters(marker, Level.DEBUG);
        if (decision == FilterReply.NEUTRAL) {
            return this.effectiveLevelInt <= 10000;
        }
        if (decision == FilterReply.DENY) {
            return false;
        }
        if (decision == FilterReply.ACCEPT) {
            return true;
        }
        throw new IllegalStateException("Unknown FilterReply value: " + decision);
    }
    
    @Override
    public void debug(final String msg) {
        this.filterAndLog_0_Or3Plus(Logger.FQCN, null, Level.DEBUG, msg, null, null);
    }
    
    @Override
    public void debug(final String format, final Object arg) {
        this.filterAndLog_1(Logger.FQCN, null, Level.DEBUG, format, arg, null);
    }
    
    @Override
    public void debug(final String format, final Object arg1, final Object arg2) {
        this.filterAndLog_2(Logger.FQCN, null, Level.DEBUG, format, arg1, arg2, null);
    }
    
    @Override
    public void debug(final String format, final Object... argArray) {
        this.filterAndLog_0_Or3Plus(Logger.FQCN, null, Level.DEBUG, format, argArray, null);
    }
    
    @Override
    public void debug(final String msg, final Throwable t) {
        this.filterAndLog_0_Or3Plus(Logger.FQCN, null, Level.DEBUG, msg, null, t);
    }
    
    @Override
    public void debug(final Marker marker, final String msg) {
        this.filterAndLog_0_Or3Plus(Logger.FQCN, marker, Level.DEBUG, msg, null, null);
    }
    
    @Override
    public void debug(final Marker marker, final String format, final Object arg) {
        this.filterAndLog_1(Logger.FQCN, marker, Level.DEBUG, format, arg, null);
    }
    
    @Override
    public void debug(final Marker marker, final String format, final Object arg1, final Object arg2) {
        this.filterAndLog_2(Logger.FQCN, marker, Level.DEBUG, format, arg1, arg2, null);
    }
    
    @Override
    public void debug(final Marker marker, final String format, final Object... argArray) {
        this.filterAndLog_0_Or3Plus(Logger.FQCN, marker, Level.DEBUG, format, argArray, null);
    }
    
    @Override
    public void debug(final Marker marker, final String msg, final Throwable t) {
        this.filterAndLog_0_Or3Plus(Logger.FQCN, marker, Level.DEBUG, msg, null, t);
    }
    
    @Override
    public void error(final String msg) {
        this.filterAndLog_0_Or3Plus(Logger.FQCN, null, Level.ERROR, msg, null, null);
    }
    
    @Override
    public void error(final String format, final Object arg) {
        this.filterAndLog_1(Logger.FQCN, null, Level.ERROR, format, arg, null);
    }
    
    @Override
    public void error(final String format, final Object arg1, final Object arg2) {
        this.filterAndLog_2(Logger.FQCN, null, Level.ERROR, format, arg1, arg2, null);
    }
    
    @Override
    public void error(final String format, final Object... argArray) {
        this.filterAndLog_0_Or3Plus(Logger.FQCN, null, Level.ERROR, format, argArray, null);
    }
    
    @Override
    public void error(final String msg, final Throwable t) {
        this.filterAndLog_0_Or3Plus(Logger.FQCN, null, Level.ERROR, msg, null, t);
    }
    
    @Override
    public void error(final Marker marker, final String msg) {
        this.filterAndLog_0_Or3Plus(Logger.FQCN, marker, Level.ERROR, msg, null, null);
    }
    
    @Override
    public void error(final Marker marker, final String format, final Object arg) {
        this.filterAndLog_1(Logger.FQCN, marker, Level.ERROR, format, arg, null);
    }
    
    @Override
    public void error(final Marker marker, final String format, final Object arg1, final Object arg2) {
        this.filterAndLog_2(Logger.FQCN, marker, Level.ERROR, format, arg1, arg2, null);
    }
    
    @Override
    public void error(final Marker marker, final String format, final Object... argArray) {
        this.filterAndLog_0_Or3Plus(Logger.FQCN, marker, Level.ERROR, format, argArray, null);
    }
    
    @Override
    public void error(final Marker marker, final String msg, final Throwable t) {
        this.filterAndLog_0_Or3Plus(Logger.FQCN, marker, Level.ERROR, msg, null, t);
    }
    
    @Override
    public boolean isInfoEnabled() {
        return this.isInfoEnabled(null);
    }
    
    @Override
    public boolean isInfoEnabled(final Marker marker) {
        final FilterReply decision = this.callTurboFilters(marker, Level.INFO);
        if (decision == FilterReply.NEUTRAL) {
            return this.effectiveLevelInt <= 20000;
        }
        if (decision == FilterReply.DENY) {
            return false;
        }
        if (decision == FilterReply.ACCEPT) {
            return true;
        }
        throw new IllegalStateException("Unknown FilterReply value: " + decision);
    }
    
    @Override
    public void info(final String msg) {
        this.filterAndLog_0_Or3Plus(Logger.FQCN, null, Level.INFO, msg, null, null);
    }
    
    @Override
    public void info(final String format, final Object arg) {
        this.filterAndLog_1(Logger.FQCN, null, Level.INFO, format, arg, null);
    }
    
    @Override
    public void info(final String format, final Object arg1, final Object arg2) {
        this.filterAndLog_2(Logger.FQCN, null, Level.INFO, format, arg1, arg2, null);
    }
    
    @Override
    public void info(final String format, final Object... argArray) {
        this.filterAndLog_0_Or3Plus(Logger.FQCN, null, Level.INFO, format, argArray, null);
    }
    
    @Override
    public void info(final String msg, final Throwable t) {
        this.filterAndLog_0_Or3Plus(Logger.FQCN, null, Level.INFO, msg, null, t);
    }
    
    @Override
    public void info(final Marker marker, final String msg) {
        this.filterAndLog_0_Or3Plus(Logger.FQCN, marker, Level.INFO, msg, null, null);
    }
    
    @Override
    public void info(final Marker marker, final String format, final Object arg) {
        this.filterAndLog_1(Logger.FQCN, marker, Level.INFO, format, arg, null);
    }
    
    @Override
    public void info(final Marker marker, final String format, final Object arg1, final Object arg2) {
        this.filterAndLog_2(Logger.FQCN, marker, Level.INFO, format, arg1, arg2, null);
    }
    
    @Override
    public void info(final Marker marker, final String format, final Object... argArray) {
        this.filterAndLog_0_Or3Plus(Logger.FQCN, marker, Level.INFO, format, argArray, null);
    }
    
    @Override
    public void info(final Marker marker, final String msg, final Throwable t) {
        this.filterAndLog_0_Or3Plus(Logger.FQCN, marker, Level.INFO, msg, null, t);
    }
    
    @Override
    public boolean isTraceEnabled() {
        return this.isTraceEnabled(null);
    }
    
    @Override
    public boolean isTraceEnabled(final Marker marker) {
        final FilterReply decision = this.callTurboFilters(marker, Level.TRACE);
        if (decision == FilterReply.NEUTRAL) {
            return this.effectiveLevelInt <= 5000;
        }
        if (decision == FilterReply.DENY) {
            return false;
        }
        if (decision == FilterReply.ACCEPT) {
            return true;
        }
        throw new IllegalStateException("Unknown FilterReply value: " + decision);
    }
    
    @Override
    public boolean isErrorEnabled() {
        return this.isErrorEnabled(null);
    }
    
    @Override
    public boolean isErrorEnabled(final Marker marker) {
        final FilterReply decision = this.callTurboFilters(marker, Level.ERROR);
        if (decision == FilterReply.NEUTRAL) {
            return this.effectiveLevelInt <= 40000;
        }
        if (decision == FilterReply.DENY) {
            return false;
        }
        if (decision == FilterReply.ACCEPT) {
            return true;
        }
        throw new IllegalStateException("Unknown FilterReply value: " + decision);
    }
    
    @Override
    public boolean isWarnEnabled() {
        return this.isWarnEnabled(null);
    }
    
    @Override
    public boolean isWarnEnabled(final Marker marker) {
        final FilterReply decision = this.callTurboFilters(marker, Level.WARN);
        if (decision == FilterReply.NEUTRAL) {
            return this.effectiveLevelInt <= 30000;
        }
        if (decision == FilterReply.DENY) {
            return false;
        }
        if (decision == FilterReply.ACCEPT) {
            return true;
        }
        throw new IllegalStateException("Unknown FilterReply value: " + decision);
    }
    
    public boolean isEnabledFor(final Marker marker, final Level level) {
        final FilterReply decision = this.callTurboFilters(marker, level);
        if (decision == FilterReply.NEUTRAL) {
            return this.effectiveLevelInt <= level.levelInt;
        }
        if (decision == FilterReply.DENY) {
            return false;
        }
        if (decision == FilterReply.ACCEPT) {
            return true;
        }
        throw new IllegalStateException("Unknown FilterReply value: " + decision);
    }
    
    public boolean isEnabledFor(final Level level) {
        return this.isEnabledFor(null, level);
    }
    
    @Override
    public void warn(final String msg) {
        this.filterAndLog_0_Or3Plus(Logger.FQCN, null, Level.WARN, msg, null, null);
    }
    
    @Override
    public void warn(final String msg, final Throwable t) {
        this.filterAndLog_0_Or3Plus(Logger.FQCN, null, Level.WARN, msg, null, t);
    }
    
    @Override
    public void warn(final String format, final Object arg) {
        this.filterAndLog_1(Logger.FQCN, null, Level.WARN, format, arg, null);
    }
    
    @Override
    public void warn(final String format, final Object arg1, final Object arg2) {
        this.filterAndLog_2(Logger.FQCN, null, Level.WARN, format, arg1, arg2, null);
    }
    
    @Override
    public void warn(final String format, final Object... argArray) {
        this.filterAndLog_0_Or3Plus(Logger.FQCN, null, Level.WARN, format, argArray, null);
    }
    
    @Override
    public void warn(final Marker marker, final String msg) {
        this.filterAndLog_0_Or3Plus(Logger.FQCN, marker, Level.WARN, msg, null, null);
    }
    
    @Override
    public void warn(final Marker marker, final String format, final Object arg) {
        this.filterAndLog_1(Logger.FQCN, marker, Level.WARN, format, arg, null);
    }
    
    @Override
    public void warn(final Marker marker, final String format, final Object... argArray) {
        this.filterAndLog_0_Or3Plus(Logger.FQCN, marker, Level.WARN, format, argArray, null);
    }
    
    @Override
    public void warn(final Marker marker, final String format, final Object arg1, final Object arg2) {
        this.filterAndLog_2(Logger.FQCN, marker, Level.WARN, format, arg1, arg2, null);
    }
    
    @Override
    public void warn(final Marker marker, final String msg, final Throwable t) {
        this.filterAndLog_0_Or3Plus(Logger.FQCN, marker, Level.WARN, msg, null, t);
    }
    
    public boolean isAdditive() {
        return this.additive;
    }
    
    public void setAdditive(final boolean additive) {
        this.additive = additive;
    }
    
    @Override
    public String toString() {
        return "Logger[" + this.name + "]";
    }
    
    private FilterReply callTurboFilters(final Marker marker, final Level level) {
        return this.loggerContext.getTurboFilterChainDecision_0_3OrMore(marker, this, level, null, null, null);
    }
    
    public LoggerContext getLoggerContext() {
        return this.loggerContext;
    }
    
    @Override
    public void log(final Marker marker, final String fqcn, final int levelInt, final String message, final Object[] argArray, final Throwable t) {
        final Level level = Level.fromLocationAwareLoggerInteger(levelInt);
        this.filterAndLog_0_Or3Plus(fqcn, marker, level, message, argArray, t);
    }
    
    public void log(final org.slf4j.event.LoggingEvent slf4jEvent) {
        final Level level = Level.fromLocationAwareLoggerInteger(slf4jEvent.getLevel().toInt());
        this.filterAndLog_0_Or3Plus(Logger.FQCN, slf4jEvent.getMarker(), level, slf4jEvent.getMessage(), slf4jEvent.getArgumentArray(), slf4jEvent.getThrowable());
    }
    
    protected Object readResolve() throws ObjectStreamException {
        return LoggerFactory.getLogger(this.getName());
    }
}
