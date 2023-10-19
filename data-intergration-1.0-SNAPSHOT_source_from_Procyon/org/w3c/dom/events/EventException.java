// 
// Decompiled by Procyon v0.5.36
// 

package org.w3c.dom.events;

public class EventException extends RuntimeException
{
    public short code;
    public static final short UNSPECIFIED_EVENT_TYPE_ERR = 0;
    
    public EventException(final short code, final String message) {
        super(message);
        this.code = code;
    }
}
