// 
// Decompiled by Procyon v0.5.36
// 

package org.xml.sax;

import java.io.IOException;

public interface EntityResolver
{
    InputSource resolveEntity(final String p0, final String p1) throws SAXException, IOException;
}
