// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.helpers;

import ch.qos.logback.core.AppenderBase;

public final class NOPAppender<E> extends AppenderBase<E>
{
    @Override
    protected void append(final E eventObject) {
    }
}
