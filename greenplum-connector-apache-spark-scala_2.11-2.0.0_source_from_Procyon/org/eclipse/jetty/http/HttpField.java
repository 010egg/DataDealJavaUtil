// 
// Decompiled by Procyon v0.5.36
// 

package org.eclipse.jetty.http;

public class HttpField
{
    private final HttpHeader _header;
    private final String _name;
    private final String _value;
    
    public HttpField(final HttpHeader header, final String name, final String value) {
        this._header = header;
        this._name = name;
        this._value = value;
    }
    
    public HttpField(final HttpHeader header, final String value) {
        this(header, header.asString(), value);
    }
    
    public HttpField(final HttpHeader header, final HttpHeaderValue value) {
        this(header, header.asString(), value.asString());
    }
    
    public HttpField(final String name, final String value) {
        this(HttpHeader.CACHE.get(name), name, value);
    }
    
    public HttpHeader getHeader() {
        return this._header;
    }
    
    public String getName() {
        return this._name;
    }
    
    public String getValue() {
        return this._value;
    }
    
    @Override
    public String toString() {
        final String v = this.getValue();
        return this.getName() + ": " + ((v == null) ? "" : v);
    }
    
    public boolean isSame(final HttpField field) {
        return field != null && (field == this || (this._header != null && this._header == field.getHeader()) || this._name.equalsIgnoreCase(field.getName()));
    }
}
