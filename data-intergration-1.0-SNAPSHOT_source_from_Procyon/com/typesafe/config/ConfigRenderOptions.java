// 
// Decompiled by Procyon v0.5.36
// 

package com.typesafe.config;

public final class ConfigRenderOptions
{
    private final boolean originComments;
    private final boolean comments;
    private final boolean formatted;
    private final boolean json;
    
    private ConfigRenderOptions(final boolean originComments, final boolean comments, final boolean formatted, final boolean json) {
        this.originComments = originComments;
        this.comments = comments;
        this.formatted = formatted;
        this.json = json;
    }
    
    public static ConfigRenderOptions defaults() {
        return new ConfigRenderOptions(true, true, true, true);
    }
    
    public static ConfigRenderOptions concise() {
        return new ConfigRenderOptions(false, false, false, true);
    }
    
    public ConfigRenderOptions setComments(final boolean value) {
        if (value == this.comments) {
            return this;
        }
        return new ConfigRenderOptions(this.originComments, value, this.formatted, this.json);
    }
    
    public boolean getComments() {
        return this.comments;
    }
    
    public ConfigRenderOptions setOriginComments(final boolean value) {
        if (value == this.originComments) {
            return this;
        }
        return new ConfigRenderOptions(value, this.comments, this.formatted, this.json);
    }
    
    public boolean getOriginComments() {
        return this.originComments;
    }
    
    public ConfigRenderOptions setFormatted(final boolean value) {
        if (value == this.formatted) {
            return this;
        }
        return new ConfigRenderOptions(this.originComments, this.comments, value, this.json);
    }
    
    public boolean getFormatted() {
        return this.formatted;
    }
    
    public ConfigRenderOptions setJson(final boolean value) {
        if (value == this.json) {
            return this;
        }
        return new ConfigRenderOptions(this.originComments, this.comments, this.formatted, value);
    }
    
    public boolean getJson() {
        return this.json;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ConfigRenderOptions(");
        if (this.originComments) {
            sb.append("originComments,");
        }
        if (this.comments) {
            sb.append("comments,");
        }
        if (this.formatted) {
            sb.append("formatted,");
        }
        if (this.json) {
            sb.append("json,");
        }
        if (sb.charAt(sb.length() - 1) == ',') {
            sb.setLength(sb.length() - 1);
        }
        sb.append(")");
        return sb.toString();
    }
}
