// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.joran.event.stax;

import javax.xml.stream.Location;

public class StaxEvent
{
    final String name;
    final Location location;
    
    StaxEvent(final String name, final Location location) {
        this.name = name;
        this.location = location;
    }
    
    public String getName() {
        return this.name;
    }
    
    public Location getLocation() {
        return this.location;
    }
}
