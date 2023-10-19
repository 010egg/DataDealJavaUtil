// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.html;

import ch.qos.logback.core.html.CssBuilder;

public class UrlCssBuilder implements CssBuilder
{
    String url;
    
    public UrlCssBuilder() {
        this.url = "http://logback.qos.ch/css/classic.css";
    }
    
    public String getUrl() {
        return this.url;
    }
    
    public void setUrl(final String url) {
        this.url = url;
    }
    
    @Override
    public void addCss(final StringBuilder sbuf) {
        sbuf.append("<link REL=StyleSheet HREF=\"");
        sbuf.append(this.url);
        sbuf.append("\" TITLE=\"Basic\" />");
    }
}
