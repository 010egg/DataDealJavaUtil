// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.joran.event.stax;

import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import ch.qos.logback.core.joran.spi.JoranException;
import javax.xml.stream.XMLInputFactory;
import java.io.InputStream;
import java.util.ArrayList;
import ch.qos.logback.core.Context;
import ch.qos.logback.core.joran.spi.ElementPath;
import java.util.List;
import ch.qos.logback.core.spi.ContextAwareBase;

public class StaxEventRecorder extends ContextAwareBase
{
    List<StaxEvent> eventList;
    ElementPath globalElementPath;
    
    public StaxEventRecorder(final Context context) {
        this.eventList = new ArrayList<StaxEvent>();
        this.globalElementPath = new ElementPath();
        this.setContext(context);
    }
    
    public void recordEvents(final InputStream inputStream) throws JoranException {
        try {
            final XMLEventReader xmlEventReader = XMLInputFactory.newInstance().createXMLEventReader(inputStream);
            this.read(xmlEventReader);
        }
        catch (XMLStreamException e) {
            throw new JoranException("Problem parsing XML document. See previously reported errors.", e);
        }
    }
    
    public List<StaxEvent> getEventList() {
        return this.eventList;
    }
    
    private void read(final XMLEventReader xmlEventReader) throws XMLStreamException {
        while (xmlEventReader.hasNext()) {
            final XMLEvent xmlEvent = xmlEventReader.nextEvent();
            switch (xmlEvent.getEventType()) {
                case 1: {
                    this.addStartElement(xmlEvent);
                    continue;
                }
                case 4: {
                    this.addCharacters(xmlEvent);
                    continue;
                }
                case 2: {
                    this.addEndEvent(xmlEvent);
                    continue;
                }
            }
        }
    }
    
    private void addStartElement(final XMLEvent xmlEvent) {
        final StartElement se = xmlEvent.asStartElement();
        final String tagName = se.getName().getLocalPart();
        this.globalElementPath.push(tagName);
        final ElementPath current = this.globalElementPath.duplicate();
        final StartEvent startEvent = new StartEvent(current, tagName, se.getAttributes(), se.getLocation());
        this.eventList.add(startEvent);
    }
    
    private void addCharacters(final XMLEvent xmlEvent) {
        final Characters characters = xmlEvent.asCharacters();
        final StaxEvent lastEvent = this.getLastEvent();
        if (lastEvent instanceof BodyEvent) {
            final BodyEvent be = (BodyEvent)lastEvent;
            be.append(characters.getData());
        }
        else if (!characters.isWhiteSpace()) {
            final BodyEvent bodyEvent = new BodyEvent(characters.getData(), xmlEvent.getLocation());
            this.eventList.add(bodyEvent);
        }
    }
    
    private void addEndEvent(final XMLEvent xmlEvent) {
        final EndElement ee = xmlEvent.asEndElement();
        final String tagName = ee.getName().getLocalPart();
        final EndEvent endEvent = new EndEvent(tagName, ee.getLocation());
        this.eventList.add(endEvent);
        this.globalElementPath.pop();
    }
    
    StaxEvent getLastEvent() {
        if (this.eventList.isEmpty()) {
            return null;
        }
        final int size = this.eventList.size();
        if (size == 0) {
            return null;
        }
        return this.eventList.get(size - 1);
    }
}
