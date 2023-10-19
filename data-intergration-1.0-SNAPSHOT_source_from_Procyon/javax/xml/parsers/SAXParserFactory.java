// 
// Decompiled by Procyon v0.5.36
// 

package javax.xml.parsers;

import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXException;

public abstract class SAXParserFactory
{
    private boolean validating;
    private boolean namespaceAware;
    
    protected SAXParserFactory() {
        this.validating = false;
        this.namespaceAware = false;
    }
    
    public static SAXParserFactory newInstance() throws FactoryConfigurationError {
        try {
            return (SAXParserFactory)FactoryFinder.find("javax.xml.parsers.SAXParserFactory", null);
        }
        catch (FactoryFinder.ConfigurationError configurationError) {
            throw new FactoryConfigurationError(configurationError.getException(), configurationError.getMessage());
        }
    }
    
    public abstract SAXParser newSAXParser() throws ParserConfigurationException, SAXException;
    
    public void setNamespaceAware(final boolean namespaceAware) {
        this.namespaceAware = namespaceAware;
    }
    
    public void setValidating(final boolean validating) {
        this.validating = validating;
    }
    
    public boolean isNamespaceAware() {
        return this.namespaceAware;
    }
    
    public boolean isValidating() {
        return this.validating;
    }
    
    public abstract void setFeature(final String p0, final boolean p1) throws ParserConfigurationException, SAXNotRecognizedException, SAXNotSupportedException;
    
    public abstract boolean getFeature(final String p0) throws ParserConfigurationException, SAXNotRecognizedException, SAXNotSupportedException;
}
