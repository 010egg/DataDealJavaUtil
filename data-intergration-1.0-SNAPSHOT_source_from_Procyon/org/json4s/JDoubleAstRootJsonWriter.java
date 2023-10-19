// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

import scala.collection.immutable.List$;
import scala.collection.immutable.List;
import scala.reflect.ScalaSignature;

@ScalaSignature(bytes = "\u0006\u000192A!\u0001\u0002\u0007\u000f\tA\"\nR8vE2,\u0017i\u001d;S_>$(j]8o/JLG/\u001a:\u000b\u0005\r!\u0011A\u00026t_:$4OC\u0001\u0006\u0003\ry'oZ\u0002\u0001'\t\u0001\u0001\u0002\u0005\u0002\n\u00155\t!!\u0003\u0002\f\u0005\t!\"\nR8vE2,\u0017i\u001d;Kg>twK]5uKJDQ!\u0004\u0001\u0005\u00029\ta\u0001P5oSRtD#A\b\u0011\u0005%\u0001\u0001BB\t\u0001A\u0003&!#A\u0003o_\u0012,7\u000fE\u0002\u00145qi\u0011\u0001\u0006\u0006\u0003+Y\t\u0011\"[7nkR\f'\r\\3\u000b\u0005]A\u0012AC2pY2,7\r^5p]*\t\u0011$A\u0003tG\u0006d\u0017-\u0003\u0002\u001c)\t!A*[:u!\ti\u0002E\u0004\u0002\n=%\u0011qDA\u0001\ba\u0006\u001c7.Y4f\u0013\t\t#E\u0001\u0004K-\u0006dW/\u001a\u0006\u0003?\tAQ\u0001\n\u0001\u0005\u0002\u0015\nq!\u00193e\u001d>$W\r\u0006\u0002'SA\u0019\u0011b\n\u000f\n\u0005!\u0012!A\u0003&t_:<&/\u001b;fe\")!f\ta\u00019\u0005!an\u001c3f\u0011\u0015a\u0003\u0001\"\u0001.\u0003\u0019\u0011Xm];miV\tA\u0004")
public final class JDoubleAstRootJsonWriter extends JDoubleAstJsonWriter
{
    private List<JsonAST.JValue> nodes;
    
    @Override
    public JsonWriter<JsonAST.JValue> addNode(final JsonAST.JValue node) {
        this.nodes = (List<JsonAST.JValue>)this.nodes.$colon$colon((Object)node);
        return this;
    }
    
    @Override
    public JsonAST.JValue result() {
        return this.nodes.nonEmpty() ? ((JsonAST.JValue)this.nodes.head()) : package$.MODULE$.JNothing();
    }
    
    public JDoubleAstRootJsonWriter() {
        this.nodes = (List<JsonAST.JValue>)List$.MODULE$.empty();
    }
}
