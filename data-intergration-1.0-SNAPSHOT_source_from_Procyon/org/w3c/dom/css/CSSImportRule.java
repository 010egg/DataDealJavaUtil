// 
// Decompiled by Procyon v0.5.36
// 

package org.w3c.dom.css;

import org.w3c.dom.stylesheets.MediaList;

public interface CSSImportRule extends CSSRule
{
    String getHref();
    
    MediaList getMedia();
    
    CSSStyleSheet getStyleSheet();
}
