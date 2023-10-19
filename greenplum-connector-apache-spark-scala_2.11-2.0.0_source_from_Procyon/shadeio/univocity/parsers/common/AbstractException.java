// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common;

import java.util.Arrays;

abstract class AbstractException extends RuntimeException
{
    private static final long serialVersionUID = -2993096896413328423L;
    protected int errorContentLength;
    
    protected AbstractException(final String message, final Throwable cause) {
        super(message, cause);
        this.errorContentLength = -1;
    }
    
    @Override
    public final String getMessage() {
        String msg = super.getMessage();
        msg = ((msg == null) ? (this.getErrorDescription() + ": ") : msg);
        msg = this.updateMessage(msg);
        final String details = this.getDetails();
        if (details == null || details.isEmpty()) {
            return msg;
        }
        return msg + "\nInternal state when error was thrown: " + details;
    }
    
    protected String updateMessage(final String msg) {
        return msg;
    }
    
    protected abstract String getDetails();
    
    protected abstract String getErrorDescription();
    
    protected static String printIfNotEmpty(final String previous, final String description, final Object o) {
        if (o == null || o.toString().isEmpty()) {
            return previous;
        }
        if (o instanceof Number && ((Number)o).intValue() < 0) {
            return previous;
        }
        String value;
        if (o.getClass().isArray()) {
            value = Arrays.toString((Object[])o);
        }
        else {
            value = String.valueOf(o);
        }
        String out = description + '=' + value;
        if (!previous.isEmpty()) {
            out = previous + ", " + out;
        }
        return out;
    }
    
    public static String restrictContent(final int errorContentLength, final CharSequence content) {
        return ArgumentUtils.restrictContent(errorContentLength, content);
    }
    
    public static Object[] restrictContent(final int errorContentLength, final Object[] content) {
        if (content == null || errorContentLength == 0) {
            return null;
        }
        return content;
    }
    
    public void setErrorContentLength(final int errorContentLength) {
        this.errorContentLength = errorContentLength;
        final Throwable cause = this.getCause();
        if (cause != null && cause instanceof AbstractException) {
            final AbstractException e = (AbstractException)cause;
            if (e.errorContentLength != errorContentLength) {
                e.setErrorContentLength(errorContentLength);
            }
        }
    }
    
    protected String restrictContent(final CharSequence content) {
        return restrictContent(this.errorContentLength, content);
    }
    
    protected String restrictContent(final Object content) {
        return ArgumentUtils.restrictContent(this.errorContentLength, content);
    }
    
    protected Object[] restrictContent(final Object[] content) {
        return restrictContent(this.errorContentLength, content);
    }
}
