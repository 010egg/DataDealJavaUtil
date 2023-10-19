// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.spi;

public class EventArgUtil
{
    public static final Throwable extractThrowable(final Object[] argArray) {
        if (argArray == null || argArray.length == 0) {
            return null;
        }
        final Object lastEntry = argArray[argArray.length - 1];
        if (lastEntry instanceof Throwable) {
            return (Throwable)lastEntry;
        }
        return null;
    }
    
    public static Object[] trimmedCopy(final Object[] argArray) {
        if (argArray == null || argArray.length == 0) {
            throw new IllegalStateException("non-sensical empty or null argument array");
        }
        final int trimemdLen = argArray.length - 1;
        final Object[] trimmed = new Object[trimemdLen];
        System.arraycopy(argArray, 0, trimmed, 0, trimemdLen);
        return trimmed;
    }
    
    public static Object[] arrangeArguments(final Object[] argArray) {
        return argArray;
    }
    
    public static boolean successfulExtraction(final Throwable throwable) {
        return throwable != null;
    }
}
