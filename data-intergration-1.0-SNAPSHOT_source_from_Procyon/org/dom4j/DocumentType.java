// 
// Decompiled by Procyon v0.5.36
// 

package org.dom4j;

import java.util.List;

public interface DocumentType extends Node
{
    String getElementName();
    
    void setElementName(final String p0);
    
    String getPublicID();
    
    void setPublicID(final String p0);
    
    String getSystemID();
    
    void setSystemID(final String p0);
    
    List getInternalDeclarations();
    
    void setInternalDeclarations(final List p0);
    
    List getExternalDeclarations();
    
    void setExternalDeclarations(final List p0);
}
