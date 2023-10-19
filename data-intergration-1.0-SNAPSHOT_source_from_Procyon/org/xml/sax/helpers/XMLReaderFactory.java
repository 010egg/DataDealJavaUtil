// 
// Decompiled by Procyon v0.5.36
// 

package org.xml.sax.helpers;

import java.io.InputStream;
import org.xml.sax.SAXException;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.xml.sax.XMLReader;

public final class XMLReaderFactory
{
    private static final String property = "org.xml.sax.driver";
    
    private XMLReaderFactory() {
    }
    
    public static XMLReader createXMLReader() throws SAXException {
        String s = null;
        final ClassLoader classLoader = NewInstance.getClassLoader();
        try {
            s = System.getProperty("org.xml.sax.driver");
        }
        catch (Exception ex) {}
        if (s == null) {
            try {
                final String s2 = "META-INF/services/org.xml.sax.driver";
                InputStream in;
                if (classLoader == null) {
                    in = ClassLoader.getSystemResourceAsStream(s2);
                }
                else {
                    in = classLoader.getResourceAsStream(s2);
                }
                if (in != null) {
                    s = new BufferedReader(new InputStreamReader(in, "UTF8")).readLine();
                    in.close();
                }
            }
            catch (Exception ex2) {}
        }
        if (s == null) {}
        if (s != null) {
            return loadClass(classLoader, s);
        }
        try {
            return new ParserAdapter(ParserFactory.makeParser());
        }
        catch (Exception ex3) {
            throw new SAXException("Can't create default XMLReader; is system property org.xml.sax.driver set?");
        }
    }
    
    public static XMLReader createXMLReader(final String s) throws SAXException {
        return loadClass(NewInstance.getClassLoader(), s);
    }
    
    private static XMLReader loadClass(final ClassLoader classLoader, final String s) throws SAXException {
        try {
            return (XMLReader)NewInstance.newInstance(classLoader, s);
        }
        catch (ClassNotFoundException ex) {
            throw new SAXException("SAX2 driver class " + s + " not found", ex);
        }
        catch (IllegalAccessException ex2) {
            throw new SAXException("SAX2 driver class " + s + " found but cannot be loaded", ex2);
        }
        catch (InstantiationException ex3) {
            throw new SAXException("SAX2 driver class " + s + " loaded but cannot be instantiated (no empty public constructor?)", ex3);
        }
        catch (ClassCastException ex4) {
            throw new SAXException("SAX2 driver class " + s + " does not implement XMLReader", ex4);
        }
    }
}
