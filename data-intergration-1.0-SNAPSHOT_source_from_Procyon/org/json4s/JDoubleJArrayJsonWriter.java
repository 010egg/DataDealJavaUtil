// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

import scala.collection.Seq;
import scala.collection.immutable.Nil$;
import scala.collection.mutable.ListBuffer$;
import scala.collection.immutable.List;
import scala.collection.mutable.ListBuffer;
import scala.reflect.ScalaSignature;

@ScalaSignature(bytes = "\u0006\u0001Q2A!\u0001\u0002\u0007\u000f\t9\"\nR8vE2,'*\u0011:sCfT5o\u001c8Xe&$XM\u001d\u0006\u0003\u0007\u0011\taA[:p]R\u001a(\"A\u0003\u0002\u0007=\u0014xm\u0001\u0001\u0014\u0005\u0001A\u0001CA\u0005\u000b\u001b\u0005\u0011\u0011BA\u0006\u0003\u0005QQEi\\;cY\u0016\f5\u000f\u001e&t_:<&/\u001b;fe\"AQ\u0002\u0001B\u0001B\u0003%a\"\u0001\u0004qCJ,g\u000e\u001e\t\u0004\u0013=\t\u0012B\u0001\t\u0003\u0005)Q5o\u001c8Xe&$XM\u001d\t\u0003%Uq!!C\n\n\u0005Q\u0011\u0011a\u00029bG.\fw-Z\u0005\u0003-]\u0011aA\u0013,bYV,'B\u0001\u000b\u0003\u0011\u0015I\u0002\u0001\"\u0001\u001b\u0003\u0019a\u0014N\\5u}Q\u00111\u0004\b\t\u0003\u0013\u0001AQ!\u0004\rA\u00029AaA\b\u0001!\u0002\u0013y\u0012!\u00028pI\u0016\u001c\bc\u0001\u0011(#5\t\u0011E\u0003\u0002#G\u00059Q.\u001e;bE2,'B\u0001\u0013&\u0003)\u0019w\u000e\u001c7fGRLwN\u001c\u0006\u0002M\u0005)1oY1mC&\u0011\u0001&\t\u0002\u000b\u0019&\u001cHOQ;gM\u0016\u0014\b\"\u0002\u0016\u0001\t\u0003Y\u0013aB1eI:{G-\u001a\u000b\u0003\u001d1BQ!L\u0015A\u0002E\tAA\\8eK\")q\u0006\u0001C!a\u0005AQM\u001c3BeJ\f\u0017\u0010F\u0001\u000f\u0011\u0015\u0011\u0004\u0001\"\u00014\u0003\u0019\u0011Xm];miV\t\u0011\u0003")
public final class JDoubleJArrayJsonWriter extends JDoubleAstJsonWriter
{
    private final JsonWriter<JsonAST.JValue> parent;
    private final ListBuffer<JsonAST.JValue> nodes;
    
    @Override
    public JsonWriter<JsonAST.JValue> addNode(final JsonAST.JValue node) {
        this.nodes.$plus$eq((Object)node);
        return this;
    }
    
    @Override
    public JsonWriter<JsonAST.JValue> endArray() {
        final JsonWriter<JsonAST.JValue> parent = this.parent;
        JsonWriter<JsonAST.JValue> jsonWriter;
        if (parent instanceof JDoubleAstJsonWriter) {
            jsonWriter = ((JDoubleAstJsonWriter)parent).addNode(this.result());
        }
        else {
            jsonWriter = this.parent;
        }
        return jsonWriter;
    }
    
    @Override
    public JsonAST.JValue result() {
        return package$.MODULE$.JArray().apply((List<JsonAST.JValue>)this.nodes.toList());
    }
    
    public JDoubleJArrayJsonWriter(final JsonWriter<JsonAST.JValue> parent) {
        this.parent = parent;
        this.nodes = (ListBuffer<JsonAST.JValue>)ListBuffer$.MODULE$.apply((Seq)Nil$.MODULE$);
    }
}
