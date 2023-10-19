// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

import java.io.StringWriter;
import java.io.Writer;

public final class RootStreamingJsonWriter$
{
    public static final RootStreamingJsonWriter$ MODULE$;
    
    static {
        new RootStreamingJsonWriter$();
    }
    
    public <T extends Writer> StringWriter $lessinit$greater$default$1() {
        return new StringWriter();
    }
    
    public <T extends Writer> boolean $lessinit$greater$default$2() {
        return false;
    }
    
    public <T extends Writer> int $lessinit$greater$default$3() {
        return 2;
    }
    
    public <T extends Writer> Formats $lessinit$greater$default$4() {
        return DefaultFormats$.MODULE$;
    }
    
    private RootStreamingJsonWriter$() {
        MODULE$ = this;
    }
}
