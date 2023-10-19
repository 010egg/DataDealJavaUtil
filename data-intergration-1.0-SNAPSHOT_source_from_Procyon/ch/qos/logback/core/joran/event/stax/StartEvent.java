// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.joran.event.stax;

import java.util.ArrayList;
import javax.xml.stream.Location;
import java.util.Iterator;
import ch.qos.logback.core.joran.spi.ElementPath;
import javax.xml.stream.events.Attribute;
import java.util.List;

public class StartEvent extends StaxEvent
{
    List<Attribute> attributes;
    public ElementPath elementPath;
    
    StartEvent(final ElementPath elementPath, final String name, final Iterator<Attribute> attributeIterator, final Location location) {
        super(name, location);
        this.populateAttributes(attributeIterator);
        this.elementPath = elementPath;
    }
    
    private void populateAttributes(final Iterator<Attribute> attributeIterator) {
        while (attributeIterator.hasNext()) {
            if (this.attributes == null) {
                this.attributes = new ArrayList<Attribute>(2);
            }
            this.attributes.add(attributeIterator.next());
        }
    }
    
    public ElementPath getElementPath() {
        return this.elementPath;
    }
    
    public List<Attribute> getAttributeList() {
        return this.attributes;
    }
    
    Attribute getAttributeByName(final String name) {
        if (this.attributes == null) {
            return null;
        }
        for (final Attribute attr : this.attributes) {
            if (name.equals(attr.getName().getLocalPart())) {
                return attr;
            }
        }
        return null;
    }
    
    @Override
    public String toString() {
        return "StartEvent(" + this.getName() + ")  [" + this.location.getLineNumber() + "," + this.location.getColumnNumber() + "]";
    }
}
