// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import org.json4s.reflect.package;

public final class package$
{
    public static final package$ MODULE$;
    private final JsonAST.JNothing$ JNothing;
    private final JsonAST.JNull$ JNull;
    private final JsonAST.JString$ JString;
    private final JsonAST.JDouble$ JDouble;
    private final JsonAST.JDecimal$ JDecimal;
    private final JsonAST.JLong$ JLong;
    private final JsonAST.JInt$ JInt;
    private final JsonAST.JBool$ JBool;
    private final JsonAST.JField$ JField;
    private final JsonAST.JObject$ JObject;
    private final JsonAST.JArray$ JArray;
    private final JsonAST.JSet$ JSet;
    private final package.TypeInfo$ TypeInfo;
    
    static {
        new package$();
    }
    
    public JsonAST.JNothing$ JNothing() {
        return this.JNothing;
    }
    
    public JsonAST.JNull$ JNull() {
        return this.JNull;
    }
    
    public JsonAST.JString$ JString() {
        return this.JString;
    }
    
    public JsonAST.JDouble$ JDouble() {
        return this.JDouble;
    }
    
    public JsonAST.JDecimal$ JDecimal() {
        return this.JDecimal;
    }
    
    public JsonAST.JLong$ JLong() {
        return this.JLong;
    }
    
    public JsonAST.JInt$ JInt() {
        return this.JInt;
    }
    
    public JsonAST.JBool$ JBool() {
        return this.JBool;
    }
    
    public JsonAST.JField$ JField() {
        return this.JField;
    }
    
    public JsonAST.JObject$ JObject() {
        return this.JObject;
    }
    
    public JsonAST.JArray$ JArray() {
        return this.JArray;
    }
    
    public JsonAST.JSet$ JSet() {
        return this.JSet;
    }
    
    public package.TypeInfo$ TypeInfo() {
        return this.TypeInfo;
    }
    
    public JsonInput string2JsonInput(final String s) {
        return new StringInput(s);
    }
    
    public JsonInput reader2JsonInput(final Reader rdr) {
        return new ReaderInput(rdr);
    }
    
    public JsonInput stream2JsonInput(final InputStream stream) {
        return new StreamInput(stream);
    }
    
    public JsonInput file2JsonInput(final File file) {
        return new FileInput(file);
    }
    
    public ExtractableJsonAstNode jvalue2extractable(final JsonAST.JValue jv) {
        return new ExtractableJsonAstNode(jv);
    }
    
    public MonadicJValue jvalue2monadic(final JsonAST.JValue jv) {
        return new MonadicJValue(jv);
    }
    
    public <T> ToJsonWritable<T> jsonwritable(final T a, final Writer<T> evidence$1) {
        return new ToJsonWritable<T>(a, evidence$1);
    }
    
    private package$() {
        MODULE$ = this;
        this.JNothing = JsonAST.JNothing$.MODULE$;
        this.JNull = JsonAST.JNull$.MODULE$;
        this.JString = JsonAST.JString$.MODULE$;
        this.JDouble = JsonAST.JDouble$.MODULE$;
        this.JDecimal = JsonAST.JDecimal$.MODULE$;
        this.JLong = JsonAST.JLong$.MODULE$;
        this.JInt = JsonAST.JInt$.MODULE$;
        this.JBool = JsonAST.JBool$.MODULE$;
        this.JField = JsonAST.JField$.MODULE$;
        this.JObject = JsonAST.JObject$.MODULE$;
        this.JArray = JsonAST.JArray$.MODULE$;
        this.JSet = JsonAST.JSet$.MODULE$;
        this.TypeInfo = package.TypeInfo$.MODULE$;
    }
}
