// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.spi;

import java.util.Arrays;
import java.io.Serializable;

public class ThrowableProxyVO implements IThrowableProxy, Serializable
{
    private static final long serialVersionUID = -773438177285807139L;
    private String className;
    private String message;
    private int commonFramesCount;
    private StackTraceElementProxy[] stackTraceElementProxyArray;
    private IThrowableProxy cause;
    private IThrowableProxy[] suppressed;
    
    @Override
    public String getMessage() {
        return this.message;
    }
    
    @Override
    public String getClassName() {
        return this.className;
    }
    
    @Override
    public int getCommonFrames() {
        return this.commonFramesCount;
    }
    
    @Override
    public IThrowableProxy getCause() {
        return this.cause;
    }
    
    @Override
    public StackTraceElementProxy[] getStackTraceElementProxyArray() {
        return this.stackTraceElementProxyArray;
    }
    
    @Override
    public IThrowableProxy[] getSuppressed() {
        return this.suppressed;
    }
    
    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + ((this.className == null) ? 0 : this.className.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final ThrowableProxyVO other = (ThrowableProxyVO)obj;
        if (this.className == null) {
            if (other.className != null) {
                return false;
            }
        }
        else if (!this.className.equals(other.className)) {
            return false;
        }
        if (!Arrays.equals(this.stackTraceElementProxyArray, other.stackTraceElementProxyArray)) {
            return false;
        }
        if (!Arrays.equals(this.suppressed, other.suppressed)) {
            return false;
        }
        if (this.cause == null) {
            if (other.cause != null) {
                return false;
            }
        }
        else if (!this.cause.equals(other.cause)) {
            return false;
        }
        return true;
    }
    
    public static ThrowableProxyVO build(final IThrowableProxy throwableProxy) {
        if (throwableProxy == null) {
            return null;
        }
        final ThrowableProxyVO tpvo = new ThrowableProxyVO();
        tpvo.className = throwableProxy.getClassName();
        tpvo.message = throwableProxy.getMessage();
        tpvo.commonFramesCount = throwableProxy.getCommonFrames();
        tpvo.stackTraceElementProxyArray = throwableProxy.getStackTraceElementProxyArray();
        final IThrowableProxy cause = throwableProxy.getCause();
        if (cause != null) {
            tpvo.cause = build(cause);
        }
        final IThrowableProxy[] suppressed = throwableProxy.getSuppressed();
        if (suppressed != null) {
            tpvo.suppressed = new IThrowableProxy[suppressed.length];
            for (int i = 0; i < suppressed.length; ++i) {
                tpvo.suppressed[i] = build(suppressed[i]);
            }
        }
        return tpvo;
    }
}
