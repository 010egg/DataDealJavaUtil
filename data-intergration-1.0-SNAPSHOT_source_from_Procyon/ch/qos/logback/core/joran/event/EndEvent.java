// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.joran.event;

import org.xml.sax.Locator;

public class EndEvent extends SaxEvent
{
    EndEvent(final String namespaceURI, final String localName, final String qName, final Locator locator) {
        super(namespaceURI, localName, qName, locator);
    }
    
    @Override
    public String toString() {
        return "  EndEvent(" + this.getQName() + ")  [" + this.locator.getLineNumber() + "," + this.locator.getColumnNumber() + "]";
    }
}
