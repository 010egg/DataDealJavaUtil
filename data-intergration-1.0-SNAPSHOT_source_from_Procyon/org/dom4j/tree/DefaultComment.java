// 
// Decompiled by Procyon v0.5.36
// 

package org.dom4j.tree;

import org.dom4j.Element;

public class DefaultComment extends FlyweightComment
{
    private Element parent;
    
    public DefaultComment(final String text) {
        super(text);
    }
    
    public DefaultComment(final Element parent, final String text) {
        super(text);
        this.parent = parent;
    }
    
    public void setText(final String text) {
        this.text = text;
    }
    
    public Element getParent() {
        return this.parent;
    }
    
    public void setParent(final Element parent) {
        this.parent = parent;
    }
    
    public boolean supportsParent() {
        return true;
    }
    
    public boolean isReadOnly() {
        return false;
    }
}
