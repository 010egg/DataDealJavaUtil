// 
// Decompiled by Procyon v0.5.36
// 

package org.eclipse.jetty.server;

import org.eclipse.jetty.util.Jetty;
import org.eclipse.jetty.util.annotation.ManagedAttribute;
import java.util.Iterator;
import java.util.Collection;
import org.eclipse.jetty.http.HttpScheme;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.List;
import org.eclipse.jetty.util.annotation.ManagedObject;

@ManagedObject("HTTP Configuration")
public class HttpConfiguration
{
    public static final String SERVER_VERSION;
    private List<Customizer> _customizers;
    private int _outputBufferSize;
    private int _outputAggregationSize;
    private int _requestHeaderSize;
    private int _responseHeaderSize;
    private int _headerCacheSize;
    private int _securePort;
    private String _secureScheme;
    private boolean _sendServerVersion;
    private boolean _sendXPoweredBy;
    private boolean _sendDateHeader;
    private boolean _delayDispatchUntilContent;
    
    public HttpConfiguration() {
        this._customizers = new CopyOnWriteArrayList<Customizer>();
        this._outputBufferSize = 32768;
        this._outputAggregationSize = this._outputBufferSize / 4;
        this._requestHeaderSize = 8192;
        this._responseHeaderSize = 8192;
        this._headerCacheSize = 512;
        this._secureScheme = HttpScheme.HTTPS.asString();
        this._sendServerVersion = true;
        this._sendXPoweredBy = false;
        this._sendDateHeader = true;
        this._delayDispatchUntilContent = false;
    }
    
    public HttpConfiguration(final HttpConfiguration config) {
        this._customizers = new CopyOnWriteArrayList<Customizer>();
        this._outputBufferSize = 32768;
        this._outputAggregationSize = this._outputBufferSize / 4;
        this._requestHeaderSize = 8192;
        this._responseHeaderSize = 8192;
        this._headerCacheSize = 512;
        this._secureScheme = HttpScheme.HTTPS.asString();
        this._sendServerVersion = true;
        this._sendXPoweredBy = false;
        this._sendDateHeader = true;
        this._delayDispatchUntilContent = false;
        this._customizers.addAll(config._customizers);
        this._outputBufferSize = config._outputBufferSize;
        this._outputAggregationSize = config._outputAggregationSize;
        this._requestHeaderSize = config._requestHeaderSize;
        this._responseHeaderSize = config._responseHeaderSize;
        this._securePort = config._securePort;
        this._secureScheme = config._secureScheme;
        this._sendDateHeader = config._sendDateHeader;
        this._sendServerVersion = config._sendServerVersion;
        this._headerCacheSize = config._headerCacheSize;
    }
    
    public void addCustomizer(final Customizer customizer) {
        this._customizers.add(customizer);
    }
    
    public List<Customizer> getCustomizers() {
        return this._customizers;
    }
    
    public <T> T getCustomizer(final Class<T> type) {
        for (final Customizer c : this._customizers) {
            if (type.isAssignableFrom(c.getClass())) {
                return (T)c;
            }
        }
        return null;
    }
    
    @ManagedAttribute("The size in bytes of the output buffer used to aggregate HTTP output")
    public int getOutputBufferSize() {
        return this._outputBufferSize;
    }
    
    @ManagedAttribute("The maximum size in bytes for HTTP output to be aggregated")
    public int getOutputAggregationSize() {
        return this._outputAggregationSize;
    }
    
    @ManagedAttribute("The maximum allowed size in bytes for a HTTP request header")
    public int getRequestHeaderSize() {
        return this._requestHeaderSize;
    }
    
    @ManagedAttribute("The maximum allowed size in bytes for a HTTP response header")
    public int getResponseHeaderSize() {
        return this._responseHeaderSize;
    }
    
    @ManagedAttribute("The maximum allowed size in bytes for a HTTP header field cache")
    public int getHeaderCacheSize() {
        return this._headerCacheSize;
    }
    
    @ManagedAttribute("The port to which Integral or Confidential security constraints are redirected")
    public int getSecurePort() {
        return this._securePort;
    }
    
    @ManagedAttribute("The scheme with which Integral or Confidential security constraints are redirected")
    public String getSecureScheme() {
        return this._secureScheme;
    }
    
    public void setSendServerVersion(final boolean sendServerVersion) {
        this._sendServerVersion = sendServerVersion;
    }
    
    @ManagedAttribute("if true, send the Server header in responses")
    public boolean getSendServerVersion() {
        return this._sendServerVersion;
    }
    
    public void setSendXPoweredBy(final boolean sendXPoweredBy) {
        this._sendXPoweredBy = sendXPoweredBy;
    }
    
    @ManagedAttribute("if true, send the X-Powered-By header in responses")
    public boolean getSendXPoweredBy() {
        return this._sendXPoweredBy;
    }
    
    public void setSendDateHeader(final boolean sendDateHeader) {
        this._sendDateHeader = sendDateHeader;
    }
    
    @ManagedAttribute("if true, include the date in HTTP headers")
    public boolean getSendDateHeader() {
        return this._sendDateHeader;
    }
    
    public void setDelayDispatchUntilContent(final boolean delay) {
        this._delayDispatchUntilContent = delay;
    }
    
    @ManagedAttribute("if true, delay the application dispatch until content is available")
    public boolean isDelayDispatchUntilContent() {
        return this._delayDispatchUntilContent;
    }
    
    public void setCustomizers(final List<Customizer> customizers) {
        this._customizers.clear();
        this._customizers.addAll(customizers);
    }
    
    public void setOutputBufferSize(final int outputBufferSize) {
        this.setOutputAggregationSize((this._outputBufferSize = outputBufferSize) / 4);
    }
    
    public void setOutputAggregationSize(final int outputAggregationSize) {
        this._outputAggregationSize = outputAggregationSize;
    }
    
    public void setRequestHeaderSize(final int requestHeaderSize) {
        this._requestHeaderSize = requestHeaderSize;
    }
    
    public void setResponseHeaderSize(final int responseHeaderSize) {
        this._responseHeaderSize = responseHeaderSize;
    }
    
    public void setHeaderCacheSize(final int headerCacheSize) {
        this._headerCacheSize = headerCacheSize;
    }
    
    public void setSecurePort(final int securePort) {
        this._securePort = securePort;
    }
    
    public void setSecureScheme(final String secureScheme) {
        this._secureScheme = secureScheme;
    }
    
    @Override
    public String toString() {
        return String.format("%s@%x{%d/%d,%d/%d,%s://:%d,%s}", this.getClass().getSimpleName(), this.hashCode(), this._outputBufferSize, this._outputAggregationSize, this._requestHeaderSize, this._responseHeaderSize, this._secureScheme, this._securePort, this._customizers);
    }
    
    static {
        SERVER_VERSION = "Jetty(" + Jetty.VERSION + ")";
    }
    
    public interface ConnectionFactory
    {
        HttpConfiguration getHttpConfiguration();
    }
    
    public interface Customizer
    {
        void customize(final Connector p0, final HttpConfiguration p1, final Request p2);
    }
}
