// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

public abstract class JsonMethods$class
{
    public static boolean parse$default$2(final JsonMethods $this) {
        return false;
    }
    
    public static boolean parse$default$3(final JsonMethods $this) {
        return true;
    }
    
    public static boolean parseOpt$default$2(final JsonMethods $this) {
        return false;
    }
    
    public static boolean parseOpt$default$3(final JsonMethods $this) {
        return true;
    }
    
    public static Formats render$default$2(final JsonMethods $this, final JsonAST.JValue value) {
        return DefaultFormats$.MODULE$;
    }
    
    public static void $init$(final JsonMethods $this) {
    }
}
