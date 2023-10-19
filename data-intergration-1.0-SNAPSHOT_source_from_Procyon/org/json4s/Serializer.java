// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

import org.json4s.reflect.package;
import scala.Tuple2;
import scala.PartialFunction;
import scala.reflect.ScalaSignature;

@ScalaSignature(bytes = "\u0006\u0001a2q!\u0001\u0002\u0011\u0002G\u0005qA\u0001\u0006TKJL\u0017\r\\5{KJT!a\u0001\u0003\u0002\r)\u001cxN\u001c\u001bt\u0015\u0005)\u0011aA8sO\u000e\u0001QC\u0001\u0005&'\t\u0001\u0011\u0002\u0005\u0002\u000b\u001b5\t1BC\u0001\r\u0003\u0015\u00198-\u00197b\u0013\tq1B\u0001\u0004B]f\u0014VM\u001a\u0005\u0006!\u00011\t!E\u0001\fI\u0016\u001cXM]5bY&TX\r\u0006\u0002\u0013]A!!bE\u000b$\u0013\t!2BA\bQCJ$\u0018.\u00197Gk:\u001cG/[8o!\u0011Qa\u0003\u0007\u0011\n\u0005]Y!A\u0002+va2,'\u0007\u0005\u0002\u001a;9\u0011!dG\u0007\u0002\u0005%\u0011ADA\u0001\ba\u0006\u001c7.Y4f\u0013\tqrD\u0001\u0005UsB,\u0017J\u001c4p\u0015\ta\"\u0001\u0005\u0002\u001aC%\u0011!e\b\u0002\u0007\u0015Z\u000bG.^3\u0011\u0005\u0011*C\u0002\u0001\u0003\u0006M\u0001\u0011\ra\n\u0002\u0002\u0003F\u0011\u0001f\u000b\t\u0003\u0015%J!AK\u0006\u0003\u000f9{G\u000f[5oOB\u0011!\u0002L\u0005\u0003[-\u00111!\u00118z\u0011\u0015ys\u0002q\u00011\u0003\u00191wN]7biB\u0011!$M\u0005\u0003e\t\u0011qAR8s[\u0006$8\u000fC\u00035\u0001\u0019\u0005Q'A\u0005tKJL\u0017\r\\5{KR\u0011ag\u000e\t\u0005\u0015MY\u0003\u0005C\u00030g\u0001\u000f\u0001\u0007")
public interface Serializer<A>
{
    PartialFunction<Tuple2<package.TypeInfo, JsonAST.JValue>, A> deserialize(final Formats p0);
    
    PartialFunction<Object, JsonAST.JValue> serialize(final Formats p0);
}
