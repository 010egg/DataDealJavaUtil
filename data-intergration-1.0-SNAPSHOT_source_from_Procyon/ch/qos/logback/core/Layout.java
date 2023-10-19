// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core;

import ch.qos.logback.core.spi.LifeCycle;
import ch.qos.logback.core.spi.ContextAware;

public interface Layout<E> extends ContextAware, LifeCycle
{
    String doLayout(final E p0);
    
    String getFileHeader();
    
    String getPresentationHeader();
    
    String getPresentationFooter();
    
    String getFileFooter();
    
    String getContentType();
}
