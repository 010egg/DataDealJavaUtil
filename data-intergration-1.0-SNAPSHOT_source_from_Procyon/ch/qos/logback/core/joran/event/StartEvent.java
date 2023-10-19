// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.joran.event;

import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.Locator;
import ch.qos.logback.core.joran.spi.ElementPath;
import org.xml.sax.Attributes;

public class StartEvent extends SaxEvent
{
    public final Attributes attributes;
    public final ElementPath elementPath;
    
    StartEvent(final ElementPath elementPath, final String namespaceURI, final String localName, final String qName, final Attributes attributes, final Locator locator) {
        super(namespaceURI, localName, qName, locator);
        this.attributes = new AttributesImpl(attributes);
        this.elementPath = elementPath;
    }
    
    public Attributes getAttributes() {
        return this.attributes;
    }
    
    @Override
    public String toString() {
        final StringBuilder b = new StringBuilder("StartEvent(");
        b.append(this.getQName());
        if (this.attributes != null) {
            for (int i = 0; i < this.attributes.getLength(); ++i) {
                if (i > 0) {
                    b.append(' ');
                }
                b.append(this.attributes.getLocalName(i)).append("=\"").append(this.attributes.getValue(i)).append("\"");
            }
        }
        b.append(")  [");
        b.append(this.locator.getLineNumber());
        b.append(",");
        b.append(this.locator.getColumnNumber());
        b.append("]");
        return b.toString();
    }
}
