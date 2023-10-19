// 
// Decompiled by Procyon v0.5.36
// 

package org.w3c.dom;

public interface ProcessingInstruction extends Node
{
    String getTarget();
    
    String getData();
    
    void setData(final String p0) throws DOMException;
}
