// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.spi;

import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import ch.qos.logback.core.helpers.CyclicBuffer;

public class CyclicBufferTracker<E> extends AbstractComponentTracker<CyclicBuffer<E>>
{
    static final int DEFAULT_NUMBER_OF_BUFFERS = 64;
    static final int DEFAULT_BUFFER_SIZE = 256;
    int bufferSize;
    
    public CyclicBufferTracker() {
        this.bufferSize = 256;
        this.setMaxComponents(64);
    }
    
    public int getBufferSize() {
        return this.bufferSize;
    }
    
    public void setBufferSize(final int bufferSize) {
        this.bufferSize = bufferSize;
    }
    
    @Override
    protected void processPriorToRemoval(final CyclicBuffer<E> component) {
        component.clear();
    }
    
    @Override
    protected CyclicBuffer<E> buildComponent(final String key) {
        return new CyclicBuffer<E>(this.bufferSize);
    }
    
    @Override
    protected boolean isComponentStale(final CyclicBuffer<E> eCyclicBuffer) {
        return false;
    }
    
    List<String> liveKeysAsOrderedList() {
        return new ArrayList<String>(this.liveMap.keySet());
    }
    
    List<String> lingererKeysAsOrderedList() {
        return new ArrayList<String>(this.lingerersMap.keySet());
    }
}
