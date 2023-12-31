// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.joran.event;

import ch.qos.logback.core.status.Status;
import org.xml.sax.SAXParseException;
import org.xml.sax.Attributes;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;
import org.xml.sax.SAXException;
import java.io.IOException;
import ch.qos.logback.core.joran.spi.JoranException;
import org.xml.sax.InputSource;
import java.io.InputStream;
import java.util.ArrayList;
import ch.qos.logback.core.Context;
import ch.qos.logback.core.joran.spi.ElementPath;
import org.xml.sax.Locator;
import java.util.List;
import ch.qos.logback.core.spi.ContextAwareImpl;
import ch.qos.logback.core.spi.ContextAware;
import org.xml.sax.helpers.DefaultHandler;

public class SaxEventRecorder extends DefaultHandler implements ContextAware
{
    final ContextAwareImpl cai;
    public List<SaxEvent> saxEventList;
    Locator locator;
    ElementPath globalElementPath;
    
    public SaxEventRecorder(final Context context) {
        this.saxEventList = new ArrayList<SaxEvent>();
        this.globalElementPath = new ElementPath();
        this.cai = new ContextAwareImpl(context, this);
    }
    
    public final void recordEvents(final InputStream inputStream) throws JoranException {
        this.recordEvents(new InputSource(inputStream));
    }
    
    public List<SaxEvent> recordEvents(final InputSource inputSource) throws JoranException {
        final SAXParser saxParser = this.buildSaxParser();
        try {
            saxParser.parse(inputSource, this);
            return this.saxEventList;
        }
        catch (IOException ie) {
            this.handleError("I/O error occurred while parsing xml file", ie);
        }
        catch (SAXException se) {
            throw new JoranException("Problem parsing XML document. See previously reported errors.", se);
        }
        catch (Exception ex) {
            this.handleError("Unexpected exception while parsing XML document.", ex);
        }
        throw new IllegalStateException("This point can never be reached");
    }
    
    private void handleError(final String errMsg, final Throwable t) throws JoranException {
        this.addError(errMsg, t);
        throw new JoranException(errMsg, t);
    }
    
    private SAXParser buildSaxParser() throws JoranException {
        try {
            final SAXParserFactory spf = SAXParserFactory.newInstance();
            spf.setValidating(false);
            spf.setNamespaceAware(true);
            return spf.newSAXParser();
        }
        catch (Exception pce) {
            final String errMsg = "Parser configuration error occurred";
            this.addError(errMsg, pce);
            throw new JoranException(errMsg, pce);
        }
    }
    
    @Override
    public void startDocument() {
    }
    
    public Locator getLocator() {
        return this.locator;
    }
    
    @Override
    public void setDocumentLocator(final Locator l) {
        this.locator = l;
    }
    
    @Override
    public void startElement(final String namespaceURI, final String localName, final String qName, final Attributes atts) {
        final String tagName = this.getTagName(localName, qName);
        this.globalElementPath.push(tagName);
        final ElementPath current = this.globalElementPath.duplicate();
        this.saxEventList.add(new StartEvent(current, namespaceURI, localName, qName, atts, this.getLocator()));
    }
    
    @Override
    public void characters(final char[] ch, final int start, final int length) {
        final String bodyStr = new String(ch, start, length);
        final SaxEvent lastEvent = this.getLastEvent();
        if (lastEvent instanceof BodyEvent) {
            final BodyEvent be = (BodyEvent)lastEvent;
            be.append(bodyStr);
        }
        else if (!this.isSpaceOnly(bodyStr)) {
            this.saxEventList.add(new BodyEvent(bodyStr, this.getLocator()));
        }
    }
    
    boolean isSpaceOnly(final String bodyStr) {
        final String bodyTrimmed = bodyStr.trim();
        return bodyTrimmed.length() == 0;
    }
    
    SaxEvent getLastEvent() {
        if (this.saxEventList.isEmpty()) {
            return null;
        }
        final int size = this.saxEventList.size();
        return this.saxEventList.get(size - 1);
    }
    
    @Override
    public void endElement(final String namespaceURI, final String localName, final String qName) {
        this.saxEventList.add(new EndEvent(namespaceURI, localName, qName, this.getLocator()));
        this.globalElementPath.pop();
    }
    
    String getTagName(final String localName, final String qName) {
        String tagName = localName;
        if (tagName == null || tagName.length() < 1) {
            tagName = qName;
        }
        return tagName;
    }
    
    @Override
    public void error(final SAXParseException spe) throws SAXException {
        this.addError("XML_PARSING - Parsing error on line " + spe.getLineNumber() + " and column " + spe.getColumnNumber());
        this.addError(spe.toString());
    }
    
    @Override
    public void fatalError(final SAXParseException spe) throws SAXException {
        this.addError("XML_PARSING - Parsing fatal error on line " + spe.getLineNumber() + " and column " + spe.getColumnNumber());
        this.addError(spe.toString());
    }
    
    @Override
    public void warning(final SAXParseException spe) throws SAXException {
        this.addWarn("XML_PARSING - Parsing warning on line " + spe.getLineNumber() + " and column " + spe.getColumnNumber(), spe);
    }
    
    @Override
    public void addError(final String msg) {
        this.cai.addError(msg);
    }
    
    @Override
    public void addError(final String msg, final Throwable ex) {
        this.cai.addError(msg, ex);
    }
    
    @Override
    public void addInfo(final String msg) {
        this.cai.addInfo(msg);
    }
    
    @Override
    public void addInfo(final String msg, final Throwable ex) {
        this.cai.addInfo(msg, ex);
    }
    
    @Override
    public void addStatus(final Status status) {
        this.cai.addStatus(status);
    }
    
    @Override
    public void addWarn(final String msg) {
        this.cai.addWarn(msg);
    }
    
    @Override
    public void addWarn(final String msg, final Throwable ex) {
        this.cai.addWarn(msg, ex);
    }
    
    @Override
    public Context getContext() {
        return this.cai.getContext();
    }
    
    @Override
    public void setContext(final Context context) {
        this.cai.setContext(context);
    }
    
    public List<SaxEvent> getSaxEventList() {
        return this.saxEventList;
    }
}
