// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

import java.io.Writer;

public final class JsonWriter$
{
    public static final JsonWriter$ MODULE$;
    
    static {
        new JsonWriter$();
    }
    
    public JsonWriter<JsonAST.JValue> ast() {
        return new JDoubleAstRootJsonWriter();
    }
    
    public JsonWriter<JsonAST.JValue> bigDecimalAst() {
        return new JDecimalAstRootJsonWriter();
    }
    
    public <T extends Writer> JsonWriter<T> streaming(final T writer, final Formats formats) {
        final Writer x$1 = writer;
        final boolean x$2 = false;
        final Formats x$3 = formats;
        final int x$4 = RootStreamingJsonWriter$.MODULE$.$lessinit$greater$default$3();
        return new RootStreamingJsonWriter<T>((T)x$1, x$2, x$4, x$3);
    }
    
    public <T extends Writer> Formats streaming$default$2(final T writer) {
        return DefaultFormats$.MODULE$;
    }
    
    public <T extends Writer> JsonWriter<T> streamingPretty(final T writer, final Formats formats) {
        final Writer x$5 = writer;
        final boolean x$6 = true;
        final Formats x$7 = formats;
        final int x$8 = RootStreamingJsonWriter$.MODULE$.$lessinit$greater$default$3();
        return new RootStreamingJsonWriter<T>((T)x$5, x$6, x$8, x$7);
    }
    
    public <T extends Writer> Formats streamingPretty$default$2(final T writer) {
        return DefaultFormats$.MODULE$;
    }
    
    private JsonWriter$() {
        MODULE$ = this;
    }
}
