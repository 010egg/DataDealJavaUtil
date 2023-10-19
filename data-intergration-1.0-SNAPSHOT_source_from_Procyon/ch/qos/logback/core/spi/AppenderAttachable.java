// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.spi;

import java.util.Iterator;
import ch.qos.logback.core.Appender;

public interface AppenderAttachable<E>
{
    void addAppender(final Appender<E> p0);
    
    Iterator<Appender<E>> iteratorForAppenders();
    
    Appender<E> getAppender(final String p0);
    
    boolean isAttached(final Appender<E> p0);
    
    void detachAndStopAllAppenders();
    
    boolean detachAppender(final Appender<E> p0);
    
    boolean detachAppender(final String p0);
}
