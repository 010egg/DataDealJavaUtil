// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.net;

import java.util.concurrent.LinkedBlockingDeque;

public class QueueFactory
{
    public <E> LinkedBlockingDeque<E> newLinkedBlockingDeque(final int capacity) {
        final int actualCapacity = (capacity < 1) ? 1 : capacity;
        return new LinkedBlockingDeque<E>(actualCapacity);
    }
}
