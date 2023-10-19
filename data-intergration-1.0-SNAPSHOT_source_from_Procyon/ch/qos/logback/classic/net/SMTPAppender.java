// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.net;

import ch.qos.logback.core.pattern.PatternLayoutBase;
import ch.qos.logback.core.pattern.PostCompileProcessor;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.core.Layout;
import org.slf4j.Marker;
import ch.qos.logback.classic.ClassicConstants;
import ch.qos.logback.core.helpers.CyclicBuffer;
import ch.qos.logback.core.boolex.EventEvaluator;
import ch.qos.logback.classic.boolex.OnErrorEvaluator;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.net.SMTPAppenderBase;

public class SMTPAppender extends SMTPAppenderBase<ILoggingEvent>
{
    static final String DEFAULT_SUBJECT_PATTERN = "%logger{20} - %m";
    private int bufferSize;
    private boolean includeCallerData;
    
    public SMTPAppender() {
        this.bufferSize = 512;
        this.includeCallerData = false;
    }
    
    @Override
    public void start() {
        if (this.eventEvaluator == null) {
            final OnErrorEvaluator onError = new OnErrorEvaluator();
            onError.setContext(this.getContext());
            onError.setName("onError");
            onError.start();
            this.eventEvaluator = (EventEvaluator<E>)onError;
        }
        super.start();
    }
    
    public SMTPAppender(final EventEvaluator<ILoggingEvent> eventEvaluator) {
        this.bufferSize = 512;
        this.includeCallerData = false;
        this.eventEvaluator = (EventEvaluator<E>)eventEvaluator;
    }
    
    @Override
    protected void subAppend(final CyclicBuffer<ILoggingEvent> cb, final ILoggingEvent event) {
        if (this.includeCallerData) {
            event.getCallerData();
        }
        event.prepareForDeferredProcessing();
        cb.add(event);
    }
    
    @Override
    protected void fillBuffer(final CyclicBuffer<ILoggingEvent> cb, final StringBuffer sbuf) {
        for (int len = cb.length(), i = 0; i < len; ++i) {
            final ILoggingEvent event = cb.get();
            sbuf.append(this.layout.doLayout((E)event));
        }
    }
    
    @Override
    protected boolean eventMarksEndOfLife(final ILoggingEvent eventObject) {
        final Marker marker = eventObject.getMarker();
        return marker != null && marker.contains(ClassicConstants.FINALIZE_SESSION_MARKER);
    }
    
    @Override
    protected Layout<ILoggingEvent> makeSubjectLayout(String subjectStr) {
        if (subjectStr == null) {
            subjectStr = "%logger{20} - %m";
        }
        final PatternLayout pl = new PatternLayout();
        pl.setContext(this.getContext());
        pl.setPattern(subjectStr);
        pl.setPostCompileProcessor(null);
        pl.start();
        return pl;
    }
    
    @Override
    protected PatternLayout makeNewToPatternLayout(final String toPattern) {
        final PatternLayout pl = new PatternLayout();
        pl.setPattern(String.valueOf(toPattern) + "%nopex");
        return pl;
    }
    
    public boolean isIncludeCallerData() {
        return this.includeCallerData;
    }
    
    public void setIncludeCallerData(final boolean includeCallerData) {
        this.includeCallerData = includeCallerData;
    }
}
