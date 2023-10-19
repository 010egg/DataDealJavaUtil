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

@ScalaSignature(bytes = "\u0006\u0001Q2A!\u0001\u0002\u0007\u000f\tA\"\nR3dS6\fGNS!se\u0006L(j]8o/JLG/\u001a:\u000b\u0005\r!\u0011A\u00026t_:$4OC\u0001\u0006\u0003\ry'oZ\u0002\u0001'\t\u0001\u0001\u0002\u0005\u0002\n\u00155\t!!\u0003\u0002\f\u0005\t)\"\nR3dS6\fG.Q:u\u0015N|gn\u0016:ji\u0016\u0014\b\u0002C\u0007\u0001\u0005\u0003\u0005\u000b\u0011\u0002\b\u0002\rA\f'/\u001a8u!\rIq\"E\u0005\u0003!\t\u0011!BS:p]^\u0013\u0018\u000e^3s!\t\u0011RC\u0004\u0002\n'%\u0011ACA\u0001\ba\u0006\u001c7.Y4f\u0013\t1rC\u0001\u0004K-\u0006dW/\u001a\u0006\u0003)\tAQ!\u0007\u0001\u0005\u0002i\ta\u0001P5oSRtDCA\u000e\u001d!\tI\u0001\u0001C\u0003\u000e1\u0001\u0007a\u0002\u0003\u0004\u001f\u0001\u0001\u0006IaH\u0001\u0006]>$Wm\u001d\t\u0004A\u001d\nR\"A\u0011\u000b\u0005\t\u001a\u0013aB7vi\u0006\u0014G.\u001a\u0006\u0003I\u0015\n!bY8mY\u0016\u001cG/[8o\u0015\u00051\u0013!B:dC2\f\u0017B\u0001\u0015\"\u0005)a\u0015n\u001d;Ck\u001a4WM\u001d\u0005\u0006U\u0001!\taK\u0001\bC\u0012$gj\u001c3f)\tqA\u0006C\u0003.S\u0001\u0007\u0011#\u0001\u0003o_\u0012,\u0007\"B\u0018\u0001\t\u0003\u0002\u0014\u0001C3oI\u0006\u0013(/Y=\u0015\u00039AQA\r\u0001\u0005\u0002M\naA]3tk2$X#A\t")
public final class JDecimalJArrayJsonWriter extends JDecimalAstJsonWriter
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
        if (parent instanceof JDecimalAstJsonWriter) {
            jsonWriter = ((JDecimalAstJsonWriter)parent).addNode(this.result());
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
    
    public JDecimalJArrayJsonWriter(final JsonWriter<JsonAST.JValue> parent) {
        this.parent = parent;
        this.nodes = (ListBuffer<JsonAST.JValue>)ListBuffer$.MODULE$.apply((Seq)Nil$.MODULE$);
    }
}
