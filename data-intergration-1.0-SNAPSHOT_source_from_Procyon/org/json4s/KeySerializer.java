// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

import org.json4s.reflect.package;
import scala.Tuple2;
import scala.PartialFunction;
import scala.reflect.ScalaSignature;

@ScalaSignature(bytes = "\u0006\u0001q2q!\u0001\u0002\u0011\u0002G\u0005qAA\u0007LKf\u001cVM]5bY&TXM\u001d\u0006\u0003\u0007\u0011\taA[:p]R\u001a(\"A\u0003\u0002\u0007=\u0014xm\u0001\u0001\u0016\u0005!I3C\u0001\u0001\n!\tQQ\"D\u0001\f\u0015\u0005a\u0011!B:dC2\f\u0017B\u0001\b\f\u0005\u0019\te.\u001f*fM\")\u0001\u0003\u0001D\u0001#\u0005YA-Z:fe&\fG.\u001b>f)\t\u0011\"\u0007\u0005\u0003\u000b'U9\u0013B\u0001\u000b\f\u0005=\u0001\u0016M\u001d;jC24UO\\2uS>t\u0007\u0003\u0002\u0006\u00171\u0001J!aF\u0006\u0003\rQ+\b\u000f\\33!\tIRD\u0004\u0002\u001b75\t!!\u0003\u0002\u001d\u0005\u00059\u0001/Y2lC\u001e,\u0017B\u0001\u0010 \u0005!!\u0016\u0010]3J]\u001a|'B\u0001\u000f\u0003!\t\tCE\u0004\u0002\u000bE%\u00111eC\u0001\u0007!J,G-\u001a4\n\u0005\u00152#AB*ue&twM\u0003\u0002$\u0017A\u0011\u0001&\u000b\u0007\u0001\t\u0015Q\u0003A1\u0001,\u0005\u0005\t\u0015C\u0001\u00170!\tQQ&\u0003\u0002/\u0017\t9aj\u001c;iS:<\u0007C\u0001\u00061\u0013\t\t4BA\u0002B]fDQaM\bA\u0004Q\naAZ8s[\u0006$\bC\u0001\u000e6\u0013\t1$AA\u0004G_Jl\u0017\r^:\t\u000ba\u0002a\u0011A\u001d\u0002\u0013M,'/[1mSj,GC\u0001\u001e<!\u0011Q1c\f\u0011\t\u000bM:\u00049\u0001\u001b")
public interface KeySerializer<A>
{
    PartialFunction<Tuple2<package.TypeInfo, String>, A> deserialize(final Formats p0);
    
    PartialFunction<Object, String> serialize(final Formats p0);
}
