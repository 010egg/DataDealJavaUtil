// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.schema;

public final class ValidateResult
{
    private final boolean success;
    final String format;
    final Object[] args;
    final ValidateResult cause;
    String message;
    
    public ValidateResult(final ValidateResult cause, final String format, final Object... args) {
        this.success = false;
        this.format = format;
        this.args = args;
        this.cause = cause;
        if (args.length == 0) {
            this.message = format;
        }
    }
    
    public ValidateResult(final boolean success, final String format, final Object... args) {
        this.success = success;
        this.format = format;
        this.args = args;
        this.cause = null;
        if (args.length == 0) {
            this.message = format;
        }
    }
    
    public boolean isSuccess() {
        return this.success;
    }
    
    public String getMessage() {
        if (this.message == null && this.format != null && this.args.length > 0) {
            String s = String.format(this.format, this.args);
            if (this.cause != null) {
                s = s + "; " + this.cause.getMessage();
            }
            return this.message = s;
        }
        return this.message;
    }
}
