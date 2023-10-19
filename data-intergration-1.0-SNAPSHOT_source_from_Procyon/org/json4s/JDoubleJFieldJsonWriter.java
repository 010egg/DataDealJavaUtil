// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

import scala.Tuple2;
import scala.Predef$;
import scala.Predef;
import scala.reflect.ScalaSignature;

@ScalaSignature(bytes = "\u0006\u0001U2A!\u0001\u0002\u0007\u000f\t9\"\nR8vE2,'JR5fY\u0012T5o\u001c8Xe&$XM\u001d\u0006\u0003\u0007\u0011\taA[:p]R\u001a(\"A\u0003\u0002\u0007=\u0014xm\u0001\u0001\u0014\u0005\u0001A\u0001CA\u0005\u000b\u001b\u0005\u0011\u0011BA\u0006\u0003\u0005QQEi\\;cY\u0016\f5\u000f\u001e&t_:<&/\u001b;fe\"AQ\u0002\u0001B\u0001B\u0003%a\"\u0001\u0003oC6,\u0007CA\b\u0016\u001d\t\u00012#D\u0001\u0012\u0015\u0005\u0011\u0012!B:dC2\f\u0017B\u0001\u000b\u0012\u0003\u0019\u0001&/\u001a3fM&\u0011ac\u0006\u0002\u0007'R\u0014\u0018N\\4\u000b\u0005Q\t\u0002\u0002C\r\u0001\u0005\u0003\u0005\u000b\u0011\u0002\u000e\u0002\rA\f'/\u001a8u!\tI1$\u0003\u0002\u001d\u0005\tA\"\nR8vE2,'j\u00142kK\u000e$(j]8o/JLG/\u001a:\t\u000by\u0001A\u0011A\u0010\u0002\rqJg.\u001b;?)\r\u0001\u0013E\t\t\u0003\u0013\u0001AQ!D\u000fA\u00029AQ!G\u000fA\u0002iAQ\u0001\n\u0001\u0005\u0002\u0015\naA]3tk2$X#\u0001\u0014\u0011\u0005\u001dRcBA\u0005)\u0013\tI#!A\u0004qC\u000e\\\u0017mZ3\n\u0005-b#A\u0002&WC2,XM\u0003\u0002*\u0005!)a\u0006\u0001C\u0001_\u00059\u0011\r\u001a3O_\u0012,GC\u0001\u00194!\rI\u0011GJ\u0005\u0003e\t\u0011!BS:p]^\u0013\u0018\u000e^3s\u0011\u0015!T\u00061\u0001'\u0003\u0011qw\u000eZ3")
public final class JDoubleJFieldJsonWriter extends JDoubleAstJsonWriter
{
    private final String name;
    private final JDoubleJObjectJsonWriter parent;
    
    @Override
    public JsonAST.JValue result() {
        return package$.MODULE$.JNothing();
    }
    
    @Override
    public JsonWriter<JsonAST.JValue> addNode(final JsonAST.JValue node) {
        return this.parent.addNode((Tuple2<String, JsonAST.JValue>)Predef.ArrowAssoc$.MODULE$.$minus$greater$extension(Predef$.MODULE$.ArrowAssoc((Object)this.name), (Object)node));
    }
    
    public JDoubleJFieldJsonWriter(final String name, final JDoubleJObjectJsonWriter parent) {
        this.name = name;
        this.parent = parent;
    }
}
