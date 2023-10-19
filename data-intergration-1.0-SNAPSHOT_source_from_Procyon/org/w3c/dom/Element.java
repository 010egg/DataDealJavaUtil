// 
// Decompiled by Procyon v0.5.36
// 

package org.w3c.dom;

public interface Element extends Node
{
    String getTagName();
    
    String getAttribute(final String p0);
    
    void setAttribute(final String p0, final String p1) throws DOMException;
    
    void removeAttribute(final String p0) throws DOMException;
    
    Attr getAttributeNode(final String p0);
    
    Attr setAttributeNode(final Attr p0) throws DOMException;
    
    Attr removeAttributeNode(final Attr p0) throws DOMException;
    
    NodeList getElementsByTagName(final String p0);
    
    String getAttributeNS(final String p0, final String p1);
    
    void setAttributeNS(final String p0, final String p1, final String p2) throws DOMException;
    
    void removeAttributeNS(final String p0, final String p1) throws DOMException;
    
    Attr getAttributeNodeNS(final String p0, final String p1);
    
    Attr setAttributeNodeNS(final Attr p0) throws DOMException;
    
    NodeList getElementsByTagNameNS(final String p0, final String p1);
    
    boolean hasAttribute(final String p0);
    
    boolean hasAttributeNS(final String p0, final String p1);
}
