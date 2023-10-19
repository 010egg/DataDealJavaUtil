// 
// Decompiled by Procyon v0.5.36
// 

package org.xml.sax.helpers;

import org.xml.sax.Parser;

public class ParserFactory
{
    private ParserFactory() {
    }
    
    public static Parser makeParser() throws ClassNotFoundException, IllegalAccessException, InstantiationException, NullPointerException, ClassCastException {
        final String property = System.getProperty("org.xml.sax.parser");
        if (property == null) {
            throw new NullPointerException("No value for sax.parser property");
        }
        return makeParser(property);
    }
    
    public static Parser makeParser(final String s) throws ClassNotFoundException, IllegalAccessException, InstantiationException, ClassCastException {
        return (Parser)NewInstance.newInstance(NewInstance.getClassLoader(), s);
    }
}
