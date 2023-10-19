// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.joran.event.stax;

import javax.xml.stream.Location;

public class BodyEvent extends StaxEvent
{
    private String text;
    
    BodyEvent(final String text, final Location location) {
        super(null, location);
        this.text = text;
    }
    
    public String getText() {
        return this.text;
    }
    
    void append(final String txt) {
        this.text += txt;
    }
    
    @Override
    public String toString() {
        return "BodyEvent(" + this.getText() + ")" + this.location.getLineNumber() + "," + this.location.getColumnNumber();
    }
}
