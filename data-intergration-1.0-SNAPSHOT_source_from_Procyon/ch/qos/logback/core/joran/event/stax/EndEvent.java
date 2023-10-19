// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.joran.event.stax;

import javax.xml.stream.Location;

public class EndEvent extends StaxEvent
{
    public EndEvent(final String name, final Location location) {
        super(name, location);
    }
    
    @Override
    public String toString() {
        return "EndEvent(" + this.getName() + ")  [" + this.location.getLineNumber() + "," + this.location.getColumnNumber() + "]";
    }
}
