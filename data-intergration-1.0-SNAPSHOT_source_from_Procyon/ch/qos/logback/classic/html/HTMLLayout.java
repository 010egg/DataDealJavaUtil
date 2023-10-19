// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.html;

import ch.qos.logback.classic.pattern.MDCConverter;
import ch.qos.logback.core.helpers.Transform;
import ch.qos.logback.core.pattern.Converter;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.classic.PatternLayout;
import java.util.Map;
import ch.qos.logback.core.html.IThrowableRenderer;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.html.HTMLLayoutBase;

public class HTMLLayout extends HTMLLayoutBase<ILoggingEvent>
{
    static final String DEFAULT_CONVERSION_PATTERN = "%date%thread%level%logger%mdc%msg";
    IThrowableRenderer<ILoggingEvent> throwableRenderer;
    
    public HTMLLayout() {
        this.pattern = "%date%thread%level%logger%mdc%msg";
        this.throwableRenderer = new DefaultThrowableRenderer();
        this.cssBuilder = new DefaultCssBuilder();
    }
    
    @Override
    public void start() {
        int errorCount = 0;
        if (this.throwableRenderer == null) {
            this.addError("ThrowableRender cannot be null.");
            ++errorCount;
        }
        if (errorCount == 0) {
            super.start();
        }
    }
    
    @Override
    protected Map<String, String> getDefaultConverterMap() {
        return PatternLayout.defaultConverterMap;
    }
    
    @Override
    public String doLayout(final ILoggingEvent event) {
        final StringBuilder buf = new StringBuilder();
        this.startNewTableIfLimitReached(buf);
        boolean odd = true;
        if ((this.counter++ & 0x1L) == 0x0L) {
            odd = false;
        }
        final String level = event.getLevel().toString().toLowerCase();
        buf.append(CoreConstants.LINE_SEPARATOR);
        buf.append("<tr class=\"");
        buf.append(level);
        if (odd) {
            buf.append(" odd\">");
        }
        else {
            buf.append(" even\">");
        }
        buf.append(CoreConstants.LINE_SEPARATOR);
        for (Converter<ILoggingEvent> c = (Converter<ILoggingEvent>)this.head; c != null; c = c.getNext()) {
            this.appendEventToBuffer(buf, c, event);
        }
        buf.append("</tr>");
        buf.append(CoreConstants.LINE_SEPARATOR);
        if (event.getThrowableProxy() != null) {
            this.throwableRenderer.render(buf, event);
        }
        return buf.toString();
    }
    
    private void appendEventToBuffer(final StringBuilder buf, final Converter<ILoggingEvent> c, final ILoggingEvent event) {
        buf.append("<td class=\"");
        buf.append(this.computeConverterName(c));
        buf.append("\">");
        buf.append(Transform.escapeTags(c.convert(event)));
        buf.append("</td>");
        buf.append(CoreConstants.LINE_SEPARATOR);
    }
    
    public IThrowableRenderer getThrowableRenderer() {
        return this.throwableRenderer;
    }
    
    public void setThrowableRenderer(final IThrowableRenderer<ILoggingEvent> throwableRenderer) {
        this.throwableRenderer = throwableRenderer;
    }
    
    @Override
    protected String computeConverterName(final Converter c) {
        if (!(c instanceof MDCConverter)) {
            return super.computeConverterName(c);
        }
        final MDCConverter mc = (MDCConverter)c;
        final String key = mc.getFirstOption();
        if (key != null) {
            return key;
        }
        return "MDC";
    }
}
