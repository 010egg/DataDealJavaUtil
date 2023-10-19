// 
// Decompiled by Procyon v0.5.36
// 

package org.w3c.dom.html;

public interface HTMLTableSectionElement extends HTMLElement
{
    String getAlign();
    
    void setAlign(final String p0);
    
    String getCh();
    
    void setCh(final String p0);
    
    String getChOff();
    
    void setChOff(final String p0);
    
    String getVAlign();
    
    void setVAlign(final String p0);
    
    HTMLCollection getRows();
    
    HTMLElement insertRow(final int p0);
    
    void deleteRow(final int p0);
}
