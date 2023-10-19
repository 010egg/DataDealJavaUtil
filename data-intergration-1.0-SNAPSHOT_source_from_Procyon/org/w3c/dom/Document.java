// 
// Decompiled by Procyon v0.5.36
// 

package org.w3c.dom;

public interface Document extends Node
{
    DocumentType getDoctype();
    
    DOMImplementation getImplementation();
    
    Element getDocumentElement();
    
    Element createElement(final String p0) throws DOMException;
    
    DocumentFragment createDocumentFragment();
    
    Text createTextNode(final String p0);
    
    Comment createComment(final String p0);
    
    CDATASection createCDATASection(final String p0) throws DOMException;
    
    ProcessingInstruction createProcessingInstruction(final String p0, final String p1) throws DOMException;
    
    Attr createAttribute(final String p0) throws DOMException;
    
    EntityReference createEntityReference(final String p0) throws DOMException;
    
    NodeList getElementsByTagName(final String p0);
    
    Node importNode(final Node p0, final boolean p1) throws DOMException;
    
    Element createElementNS(final String p0, final String p1) throws DOMException;
    
    Attr createAttributeNS(final String p0, final String p1) throws DOMException;
    
    NodeList getElementsByTagNameNS(final String p0, final String p1);
    
    Element getElementById(final String p0);
}
