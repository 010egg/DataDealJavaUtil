// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.xmlcommons;

public class Version
{
    public static String getVersion() {
        return getProduct() + " " + getVersionNum();
    }
    
    public static String getProduct() {
        return "XmlCommons";
    }
    
    public static String getVersionNum() {
        return "1.0";
    }
    
    public static void main(final String[] array) {
        System.out.println(getVersion());
    }
}
