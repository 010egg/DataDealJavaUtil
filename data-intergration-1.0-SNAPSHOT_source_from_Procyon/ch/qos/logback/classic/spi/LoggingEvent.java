// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.spi;

import java.io.IOException;
import java.io.ObjectOutputStream;
import org.slf4j.spi.MDCAdapter;
import java.util.Collections;
import ch.qos.logback.classic.util.LogbackMDCAdapter;
import org.slf4j.MDC;
import org.slf4j.helpers.MessageFormatter;
import ch.qos.logback.classic.Logger;
import java.util.Map;
import org.slf4j.Marker;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;

public class LoggingEvent implements ILoggingEvent
{
    transient String fqnOfLoggerClass;
    private String threadName;
    private String loggerName;
    private LoggerContext loggerContext;
    private LoggerContextVO loggerContextVO;
    private transient Level level;
    private String message;
    transient String formattedMessage;
    private transient Object[] argumentArray;
    private ThrowableProxy throwableProxy;
    private StackTraceElement[] callerDataArray;
    private Marker marker;
    private Map<String, String> mdcPropertyMap;
    private long timeStamp;
    
    public LoggingEvent() {
    }
    
    public LoggingEvent(final String fqcn, final Logger logger, final Level level, final String message, Throwable throwable, final Object[] argArray) {
        this.fqnOfLoggerClass = fqcn;
        this.loggerName = logger.getName();
        this.loggerContext = logger.getLoggerContext();
        this.loggerContextVO = this.loggerContext.getLoggerContextRemoteView();
        this.level = level;
        this.message = message;
        this.argumentArray = argArray;
        if (throwable == null) {
            throwable = this.extractThrowableAnRearrangeArguments(argArray);
        }
        if (throwable != null) {
            this.throwableProxy = new ThrowableProxy(throwable);
            final LoggerContext lc = logger.getLoggerContext();
            if (lc.isPackagingDataEnabled()) {
                this.throwableProxy.calculatePackagingData();
            }
        }
        this.timeStamp = System.currentTimeMillis();
    }
    
    private Throwable extractThrowableAnRearrangeArguments(final Object[] argArray) {
        final Throwable extractedThrowable = EventArgUtil.extractThrowable(argArray);
        if (EventArgUtil.successfulExtraction(extractedThrowable)) {
            this.argumentArray = EventArgUtil.trimmedCopy(argArray);
        }
        return extractedThrowable;
    }
    
    public void setArgumentArray(final Object[] argArray) {
        if (this.argumentArray != null) {
            throw new IllegalStateException("argArray has been already set");
        }
        this.argumentArray = argArray;
    }
    
    @Override
    public Object[] getArgumentArray() {
        return this.argumentArray;
    }
    
    @Override
    public Level getLevel() {
        return this.level;
    }
    
    @Override
    public String getLoggerName() {
        return this.loggerName;
    }
    
    public void setLoggerName(final String loggerName) {
        this.loggerName = loggerName;
    }
    
    @Override
    public String getThreadName() {
        if (this.threadName == null) {
            this.threadName = Thread.currentThread().getName();
        }
        return this.threadName;
    }
    
    public void setThreadName(final String threadName) throws IllegalStateException {
        if (this.threadName != null) {
            throw new IllegalStateException("threadName has been already set");
        }
        this.threadName = threadName;
    }
    
    @Override
    public IThrowableProxy getThrowableProxy() {
        return this.throwableProxy;
    }
    
    public void setThrowableProxy(final ThrowableProxy tp) {
        if (this.throwableProxy != null) {
            throw new IllegalStateException("ThrowableProxy has been already set.");
        }
        this.throwableProxy = tp;
    }
    
    @Override
    public void prepareForDeferredProcessing() {
        this.getFormattedMessage();
        this.getThreadName();
        this.getMDCPropertyMap();
    }
    
    @Override
    public LoggerContextVO getLoggerContextVO() {
        return this.loggerContextVO;
    }
    
    public void setLoggerContextRemoteView(final LoggerContextVO loggerContextVO) {
        this.loggerContextVO = loggerContextVO;
    }
    
    @Override
    public String getMessage() {
        return this.message;
    }
    
    public void setMessage(final String message) {
        if (this.message != null) {
            throw new IllegalStateException("The message for this event has been set already.");
        }
        this.message = message;
    }
    
    @Override
    public long getTimeStamp() {
        return this.timeStamp;
    }
    
    public void setTimeStamp(final long timeStamp) {
        this.timeStamp = timeStamp;
    }
    
    public void setLevel(final Level level) {
        if (this.level != null) {
            throw new IllegalStateException("The level has been already set for this event.");
        }
        this.level = level;
    }
    
    @Override
    public StackTraceElement[] getCallerData() {
        if (this.callerDataArray == null) {
            this.callerDataArray = CallerData.extract(new Throwable(), this.fqnOfLoggerClass, this.loggerContext.getMaxCallerDataDepth(), this.loggerContext.getFrameworkPackages());
        }
        return this.callerDataArray;
    }
    
    @Override
    public boolean hasCallerData() {
        return this.callerDataArray != null;
    }
    
    public void setCallerData(final StackTraceElement[] callerDataArray) {
        this.callerDataArray = callerDataArray;
    }
    
    @Override
    public Marker getMarker() {
        return this.marker;
    }
    
    public void setMarker(final Marker marker) {
        if (this.marker != null) {
            throw new IllegalStateException("The marker has been already set for this event.");
        }
        this.marker = marker;
    }
    
    public long getContextBirthTime() {
        return this.loggerContextVO.getBirthTime();
    }
    
    @Override
    public String getFormattedMessage() {
        if (this.formattedMessage != null) {
            return this.formattedMessage;
        }
        if (this.argumentArray != null) {
            this.formattedMessage = MessageFormatter.arrayFormat(this.message, this.argumentArray).getMessage();
        }
        else {
            this.formattedMessage = this.message;
        }
        return this.formattedMessage;
    }
    
    @Override
    public Map<String, String> getMDCPropertyMap() {
        if (this.mdcPropertyMap == null) {
            final MDCAdapter mdc = MDC.getMDCAdapter();
            if (mdc instanceof LogbackMDCAdapter) {
                this.mdcPropertyMap = ((LogbackMDCAdapter)mdc).getPropertyMap();
            }
            else {
                this.mdcPropertyMap = mdc.getCopyOfContextMap();
            }
        }
        if (this.mdcPropertyMap == null) {
            this.mdcPropertyMap = Collections.emptyMap();
        }
        return this.mdcPropertyMap;
    }
    
    public void setMDCPropertyMap(final Map<String, String> map) {
        if (this.mdcPropertyMap != null) {
            throw new IllegalStateException("The MDCPropertyMap has been already set for this event.");
        }
        this.mdcPropertyMap = map;
    }
    
    @Override
    @Deprecated
    public Map<String, String> getMdc() {
        return this.getMDCPropertyMap();
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append('[');
        sb.append(this.level).append("] ");
        sb.append(this.getFormattedMessage());
        return sb.toString();
    }
    
    private void writeObject(final ObjectOutputStream out) throws IOException {
        throw new UnsupportedOperationException(this.getClass() + " does not support serialization. " + "Use LoggerEventVO instance instead. See also LoggerEventVO.build method.");
    }
}
