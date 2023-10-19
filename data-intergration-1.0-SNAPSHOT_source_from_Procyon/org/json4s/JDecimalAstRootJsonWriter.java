// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

import scala.collection.immutable.List$;
import scala.collection.immutable.List;
import scala.reflect.ScalaSignature;

@ScalaSignature(bytes = "\u0006\u000192A!\u0001\u0002\u0007\u000f\tI\"\nR3dS6\fG.Q:u%>|GOS:p]^\u0013\u0018\u000e^3s\u0015\t\u0019A!\u0001\u0004kg>tGg\u001d\u0006\u0002\u000b\u0005\u0019qN]4\u0004\u0001M\u0011\u0001\u0001\u0003\t\u0003\u0013)i\u0011AA\u0005\u0003\u0017\t\u0011QC\u0013#fG&l\u0017\r\\!ti*\u001bxN\\,sSR,'\u000fC\u0003\u000e\u0001\u0011\u0005a\"\u0001\u0004=S:LGO\u0010\u000b\u0002\u001fA\u0011\u0011\u0002\u0001\u0005\u0007#\u0001\u0001\u000b\u0015\u0002\n\u0002\u000b9|G-Z:\u0011\u0007MQB$D\u0001\u0015\u0015\t)b#A\u0005j[6,H/\u00192mK*\u0011q\u0003G\u0001\u000bG>dG.Z2uS>t'\"A\r\u0002\u000bM\u001c\u0017\r\\1\n\u0005m!\"\u0001\u0002'jgR\u0004\"!\b\u0011\u000f\u0005%q\u0012BA\u0010\u0003\u0003\u001d\u0001\u0018mY6bO\u0016L!!\t\u0012\u0003\r)3\u0016\r\\;f\u0015\ty\"\u0001C\u0003%\u0001\u0011\u0005Q%A\u0004bI\u0012tu\u000eZ3\u0015\u0005\u0019J\u0003cA\u0005(9%\u0011\u0001F\u0001\u0002\u000b\u0015N|gn\u0016:ji\u0016\u0014\b\"\u0002\u0016$\u0001\u0004a\u0012\u0001\u00028pI\u0016DQ\u0001\f\u0001\u0005\u00025\naA]3tk2$X#\u0001\u000f")
public final class JDecimalAstRootJsonWriter extends JDecimalAstJsonWriter
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
    
    public JDecimalAstRootJsonWriter() {
        this.nodes = (List<JsonAST.JValue>)List$.MODULE$.empty();
    }
}
