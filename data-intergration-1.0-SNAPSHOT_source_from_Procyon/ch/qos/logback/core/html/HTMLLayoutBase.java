// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.html;

import java.util.Date;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.Context;
import java.util.HashMap;
import ch.qos.logback.core.pattern.parser.Node;
import ch.qos.logback.core.spi.ScanException;
import ch.qos.logback.core.pattern.ConverterUtil;
import java.util.Map;
import ch.qos.logback.core.pattern.parser.Parser;
import ch.qos.logback.core.pattern.Converter;
import ch.qos.logback.core.LayoutBase;

public abstract class HTMLLayoutBase<E> extends LayoutBase<E>
{
    protected String pattern;
    protected Converter<E> head;
    protected String title;
    protected CssBuilder cssBuilder;
    protected long counter;
    
    public HTMLLayoutBase() {
        this.title = "Logback Log Messages";
        this.counter = 0L;
    }
    
    public void setPattern(final String conversionPattern) {
        this.pattern = conversionPattern;
    }
    
    public String getPattern() {
        return this.pattern;
    }
    
    public CssBuilder getCssBuilder() {
        return this.cssBuilder;
    }
    
    public void setCssBuilder(final CssBuilder cssBuilder) {
        this.cssBuilder = cssBuilder;
    }
    
    @Override
    public void start() {
        int errorCount = 0;
        try {
            final Parser<E> p = new Parser<E>(this.pattern);
            p.setContext(this.getContext());
            final Node t = p.parse();
            ConverterUtil.startConverters(this.head = p.compile(t, this.getEffectiveConverterMap()));
        }
        catch (ScanException ex) {
            this.addError("Incorrect pattern found", ex);
            ++errorCount;
        }
        if (errorCount == 0) {
            super.started = true;
        }
    }
    
    protected abstract Map<String, String> getDefaultConverterMap();
    
    public Map<String, String> getEffectiveConverterMap() {
        final Map<String, String> effectiveMap = new HashMap<String, String>();
        final Map<String, String> defaultMap = this.getDefaultConverterMap();
        if (defaultMap != null) {
            effectiveMap.putAll(defaultMap);
        }
        final Context context = this.getContext();
        if (context != null) {
            final Map<String, String> contextMap = (Map<String, String>)context.getObject("PATTERN_RULE_REGISTRY");
            if (contextMap != null) {
                effectiveMap.putAll(contextMap);
            }
        }
        return effectiveMap;
    }
    
    public void setTitle(final String title) {
        this.title = title;
    }
    
    public String getTitle() {
        return this.title;
    }
    
    @Override
    public String getContentType() {
        return "text/html";
    }
    
    @Override
    public String getFileHeader() {
        final StringBuilder sbuf = new StringBuilder();
        sbuf.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"");
        sbuf.append(" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
        sbuf.append(CoreConstants.LINE_SEPARATOR);
        sbuf.append("<html>");
        sbuf.append(CoreConstants.LINE_SEPARATOR);
        sbuf.append("  <head>");
        sbuf.append(CoreConstants.LINE_SEPARATOR);
        sbuf.append("    <title>");
        sbuf.append(this.title);
        sbuf.append("</title>");
        sbuf.append(CoreConstants.LINE_SEPARATOR);
        this.cssBuilder.addCss(sbuf);
        sbuf.append(CoreConstants.LINE_SEPARATOR);
        sbuf.append("  </head>");
        sbuf.append(CoreConstants.LINE_SEPARATOR);
        sbuf.append("<body>");
        sbuf.append(CoreConstants.LINE_SEPARATOR);
        return sbuf.toString();
    }
    
    @Override
    public String getPresentationHeader() {
        final StringBuilder sbuf = new StringBuilder();
        sbuf.append("<hr/>");
        sbuf.append(CoreConstants.LINE_SEPARATOR);
        sbuf.append("<p>Log session start time ");
        sbuf.append(new Date());
        sbuf.append("</p><p></p>");
        sbuf.append(CoreConstants.LINE_SEPARATOR);
        sbuf.append(CoreConstants.LINE_SEPARATOR);
        sbuf.append("<table cellspacing=\"0\">");
        sbuf.append(CoreConstants.LINE_SEPARATOR);
        this.buildHeaderRowForTable(sbuf);
        return sbuf.toString();
    }
    
    private void buildHeaderRowForTable(final StringBuilder sbuf) {
        Converter c = this.head;
        sbuf.append("<tr class=\"header\">");
        sbuf.append(CoreConstants.LINE_SEPARATOR);
        while (c != null) {
            final String name = this.computeConverterName(c);
            if (name == null) {
                c = c.getNext();
            }
            else {
                sbuf.append("<td class=\"");
                sbuf.append(this.computeConverterName(c));
                sbuf.append("\">");
                sbuf.append(this.computeConverterName(c));
                sbuf.append("</td>");
                sbuf.append(CoreConstants.LINE_SEPARATOR);
                c = c.getNext();
            }
        }
        sbuf.append("</tr>");
        sbuf.append(CoreConstants.LINE_SEPARATOR);
    }
    
    @Override
    public String getPresentationFooter() {
        final StringBuilder sbuf = new StringBuilder();
        sbuf.append("</table>");
        return sbuf.toString();
    }
    
    @Override
    public String getFileFooter() {
        final StringBuilder sbuf = new StringBuilder();
        sbuf.append(CoreConstants.LINE_SEPARATOR);
        sbuf.append("</body></html>");
        return sbuf.toString();
    }
    
    protected void startNewTableIfLimitReached(final StringBuilder sbuf) {
        if (this.counter >= 10000L) {
            this.counter = 0L;
            sbuf.append("</table>");
            sbuf.append(CoreConstants.LINE_SEPARATOR);
            sbuf.append("<p></p>");
            sbuf.append("<table cellspacing=\"0\">");
            sbuf.append(CoreConstants.LINE_SEPARATOR);
            this.buildHeaderRowForTable(sbuf);
        }
    }
    
    protected String computeConverterName(final Converter c) {
        final String className = c.getClass().getSimpleName();
        final int index = className.indexOf("Converter");
        if (index == -1) {
            return className;
        }
        return className.substring(0, index);
    }
}
