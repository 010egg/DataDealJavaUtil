// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

import scala.Tuple2;
import scala.Predef$;
import scala.Predef;
import scala.reflect.ScalaSignature;

@ScalaSignature(bytes = "\u0006\u0001U2A!\u0001\u0002\u0007\u000f\tA\"\nR3dS6\fGN\u0013$jK2$'j]8o/JLG/\u001a:\u000b\u0005\r!\u0011A\u00026t_:$4OC\u0001\u0006\u0003\ry'oZ\u0002\u0001'\t\u0001\u0001\u0002\u0005\u0002\n\u00155\t!!\u0003\u0002\f\u0005\t)\"\nR3dS6\fG.Q:u\u0015N|gn\u0016:ji\u0016\u0014\b\u0002C\u0007\u0001\u0005\u0003\u0005\u000b\u0011\u0002\b\u0002\t9\fW.\u001a\t\u0003\u001fUq!\u0001E\n\u000e\u0003EQ\u0011AE\u0001\u0006g\u000e\fG.Y\u0005\u0003)E\ta\u0001\u0015:fI\u00164\u0017B\u0001\f\u0018\u0005\u0019\u0019FO]5oO*\u0011A#\u0005\u0005\t3\u0001\u0011\t\u0011)A\u00055\u00051\u0001/\u0019:f]R\u0004\"!C\u000e\n\u0005q\u0011!!\u0007&EK\u000eLW.\u00197K\u001f\nTWm\u0019;Kg>twK]5uKJDQA\b\u0001\u0005\u0002}\ta\u0001P5oSRtDc\u0001\u0011\"EA\u0011\u0011\u0002\u0001\u0005\u0006\u001bu\u0001\rA\u0004\u0005\u00063u\u0001\rA\u0007\u0005\u0006I\u0001!\t!J\u0001\u0007e\u0016\u001cX\u000f\u001c;\u0016\u0003\u0019\u0002\"a\n\u0016\u000f\u0005%A\u0013BA\u0015\u0003\u0003\u001d\u0001\u0018mY6bO\u0016L!a\u000b\u0017\u0003\r)3\u0016\r\\;f\u0015\tI#\u0001C\u0003/\u0001\u0011\u0005q&A\u0004bI\u0012tu\u000eZ3\u0015\u0005A\u001a\u0004cA\u00052M%\u0011!G\u0001\u0002\u000b\u0015N|gn\u0016:ji\u0016\u0014\b\"\u0002\u001b.\u0001\u00041\u0013\u0001\u00028pI\u0016\u0004")
public final class JDecimalJFieldJsonWriter extends JDecimalAstJsonWriter
{
    private final String name;
    private final JDecimalJObjectJsonWriter parent;
    
    @Override
    public JsonAST.JValue result() {
        return package$.MODULE$.JNothing();
    }
    
    @Override
    public JsonWriter<JsonAST.JValue> addNode(final JsonAST.JValue node) {
        return this.parent.addNode((Tuple2<String, JsonAST.JValue>)Predef.ArrowAssoc$.MODULE$.$minus$greater$extension(Predef$.MODULE$.ArrowAssoc((Object)this.name), (Object)node));
    }
    
    public JDecimalJFieldJsonWriter(final String name, final JDecimalJObjectJsonWriter parent) {
        this.name = name;
        this.parent = parent;
    }
}
