// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.layout;

import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.classic.pattern.ThrowableProxyConverter;
import ch.qos.logback.core.util.CachingDateFormatter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.LayoutBase;

public class TTLLLayout extends LayoutBase<ILoggingEvent>
{
    CachingDateFormatter cachingDateFormatter;
    ThrowableProxyConverter tpc;
    
    public TTLLLayout() {
        this.cachingDateFormatter = new CachingDateFormatter("HH:mm:ss.SSS");
        this.tpc = new ThrowableProxyConverter();
    }
    
    @Override
    public void start() {
        this.tpc.start();
        super.start();
    }
    
    @Override
    public String doLayout(final ILoggingEvent event) {
        if (!this.isStarted()) {
            return "";
        }
        final StringBuilder sb = new StringBuilder();
        final long timestamp = event.getTimeStamp();
        sb.append(this.cachingDateFormatter.format(timestamp));
        sb.append(" [");
        sb.append(event.getThreadName());
        sb.append("] ");
        sb.append(event.getLevel().toString());
        sb.append(" ");
        sb.append(event.getLoggerName());
        sb.append(" - ");
        sb.append(event.getFormattedMessage());
        sb.append(CoreConstants.LINE_SEPARATOR);
        final IThrowableProxy tp = event.getThrowableProxy();
        if (tp != null) {
            final String stackTrace = this.tpc.convert(event);
            sb.append(stackTrace);
        }
        return sb.toString();
    }
}
