// 
// Decompiled by Procyon v0.5.36
// 

package javax.xml.parsers;

public abstract class DocumentBuilderFactory
{
    private boolean validating;
    private boolean namespaceAware;
    private boolean whitespace;
    private boolean expandEntityRef;
    private boolean ignoreComments;
    private boolean coalescing;
    
    protected DocumentBuilderFactory() {
        this.validating = false;
        this.namespaceAware = false;
        this.whitespace = false;
        this.expandEntityRef = true;
        this.ignoreComments = false;
        this.coalescing = false;
    }
    
    public static DocumentBuilderFactory newInstance() throws FactoryConfigurationError {
        try {
            return (DocumentBuilderFactory)FactoryFinder.find("javax.xml.parsers.DocumentBuilderFactory", null);
        }
        catch (FactoryFinder.ConfigurationError configurationError) {
            throw new FactoryConfigurationError(configurationError.getException(), configurationError.getMessage());
        }
    }
    
    public abstract DocumentBuilder newDocumentBuilder() throws ParserConfigurationException;
    
    public void setNamespaceAware(final boolean namespaceAware) {
        this.namespaceAware = namespaceAware;
    }
    
    public void setValidating(final boolean validating) {
        this.validating = validating;
    }
    
    public void setIgnoringElementContentWhitespace(final boolean whitespace) {
        this.whitespace = whitespace;
    }
    
    public void setExpandEntityReferences(final boolean expandEntityRef) {
        this.expandEntityRef = expandEntityRef;
    }
    
    public void setIgnoringComments(final boolean ignoreComments) {
        this.ignoreComments = ignoreComments;
    }
    
    public void setCoalescing(final boolean coalescing) {
        this.coalescing = coalescing;
    }
    
    public boolean isNamespaceAware() {
        return this.namespaceAware;
    }
    
    public boolean isValidating() {
        return this.validating;
    }
    
    public boolean isIgnoringElementContentWhitespace() {
        return this.whitespace;
    }
    
    public boolean isExpandEntityReferences() {
        return this.expandEntityRef;
    }
    
    public boolean isIgnoringComments() {
        return this.ignoreComments;
    }
    
    public boolean isCoalescing() {
        return this.coalescing;
    }
    
    public abstract void setAttribute(final String p0, final Object p1) throws IllegalArgumentException;
    
    public abstract Object getAttribute(final String p0) throws IllegalArgumentException;
}
