// 
// Decompiled by Procyon v0.5.36
// 

package org.xml.sax;

public interface XMLFilter extends XMLReader
{
    void setParent(final XMLReader p0);
    
    XMLReader getParent();
}
