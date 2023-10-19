// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

import scala.Function2;
import scala.collection.Seq;

public final class JsonAST$
{
    public static final JsonAST$ MODULE$;
    
    static {
        new JsonAST$();
    }
    
    public JsonAST.JValue concat(final Seq<JsonAST.JValue> xs) {
        return (JsonAST.JValue)xs.foldLeft((Object)JsonAST.JNothing$.MODULE$, (Function2)new JsonAST$$anonfun$concat.JsonAST$$anonfun$concat$1());
    }
    
    private JsonAST$() {
        MODULE$ = this;
    }
}
