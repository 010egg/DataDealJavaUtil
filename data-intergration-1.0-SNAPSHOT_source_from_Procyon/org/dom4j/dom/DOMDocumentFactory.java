// 
// Decompiled by Procyon v0.5.36
// 

package org.dom4j.dom;

import org.w3c.dom.DOMException;
import java.util.Map;
import org.dom4j.ProcessingInstruction;
import org.dom4j.Namespace;
import org.dom4j.Entity;
import org.dom4j.Text;
import org.dom4j.Comment;
import org.dom4j.CDATA;
import org.dom4j.Attribute;
import org.dom4j.Element;
import org.dom4j.QName;
import org.dom4j.DocumentType;
import org.dom4j.Document;
import org.dom4j.util.SingletonStrategy;
import org.w3c.dom.DOMImplementation;
import org.dom4j.DocumentFactory;

public class DOMDocumentFactory extends DocumentFactory implements DOMImplementation
{
    private static SingletonStrategy singleton;
    
    public static DocumentFactory getInstance() {
        final DOMDocumentFactory fact = (DOMDocumentFactory)DOMDocumentFactory.singleton.instance();
        return fact;
    }
    
    public Document createDocument() {
        final DOMDocument answer = new DOMDocument();
        answer.setDocumentFactory(this);
        return answer;
    }
    
    public DocumentType createDocType(final String name, final String publicId, final String systemId) {
        return new DOMDocumentType(name, publicId, systemId);
    }
    
    public Element createElement(final QName qname) {
        return new DOMElement(qname);
    }
    
    public Element createElement(final QName qname, final int attributeCount) {
        return new DOMElement(qname, attributeCount);
    }
    
    public Attribute createAttribute(final Element owner, final QName qname, final String value) {
        return new DOMAttribute(qname, value);
    }
    
    public CDATA createCDATA(final String text) {
        return new DOMCDATA(text);
    }
    
    public Comment createComment(final String text) {
        return new DOMComment(text);
    }
    
    public Text createText(final String text) {
        return new DOMText(text);
    }
    
    public Entity createEntity(final String name) {
        return new DOMEntityReference(name);
    }
    
    public Entity createEntity(final String name, final String text) {
        return new DOMEntityReference(name, text);
    }
    
    public Namespace createNamespace(final String prefix, final String uri) {
        return new DOMNamespace(prefix, uri);
    }
    
    public ProcessingInstruction createProcessingInstruction(final String target, final String data) {
        return new DOMProcessingInstruction(target, data);
    }
    
    public ProcessingInstruction createProcessingInstruction(final String target, final Map data) {
        return new DOMProcessingInstruction(target, data);
    }
    
    public boolean hasFeature(final String feat, final String version) {
        return ("XML".equalsIgnoreCase(feat) || "Core".equalsIgnoreCase(feat)) && (version == null || version.length() == 0 || "1.0".equals(version) || "2.0".equals(version));
    }
    
    public org.w3c.dom.DocumentType createDocumentType(final String qualifiedName, final String publicId, final String systemId) throws DOMException {
        return new DOMDocumentType(qualifiedName, publicId, systemId);
    }
    
    public org.w3c.dom.Document createDocument(final String namespaceURI, final String qualifiedName, final org.w3c.dom.DocumentType docType) throws DOMException {
        DOMDocument document;
        if (docType != null) {
            final DOMDocumentType documentType = this.asDocumentType(docType);
            document = new DOMDocument(documentType);
        }
        else {
            document = new DOMDocument();
        }
        document.addElement(this.createQName(qualifiedName, namespaceURI));
        return document;
    }
    
    protected DOMDocumentType asDocumentType(final org.w3c.dom.DocumentType docType) {
        if (docType instanceof DOMDocumentType) {
            return (DOMDocumentType)docType;
        }
        return new DOMDocumentType(docType.getName(), docType.getPublicId(), docType.getSystemId());
    }
    
    static {
        DOMDocumentFactory.singleton = null;
        try {
            final String defaultSingletonClass = "org.dom4j.util.SimpleSingleton";
            Class clazz = null;
            try {
                String singletonClass = defaultSingletonClass;
                singletonClass = System.getProperty("org.dom4j.dom.DOMDocumentFactory.singleton.strategy", singletonClass);
                clazz = Class.forName(singletonClass);
            }
            catch (Exception exc1) {
                try {
                    final String singletonClass2 = defaultSingletonClass;
                    clazz = Class.forName(singletonClass2);
                }
                catch (Exception ex) {}
            }
            (DOMDocumentFactory.singleton = clazz.newInstance()).setSingletonClassName(DOMDocumentFactory.class.getName());
        }
        catch (Exception ex2) {}
    }
}
