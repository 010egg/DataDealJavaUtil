// 
// Decompiled by Procyon v0.5.36
// 

package io.pivotal.greenplum.spark;

import scala.util.Try;
import scala.PartialFunction;
import scala.reflect.ScalaSignature;

@ScalaSignature(bytes = "\u0006\u0001\u0019;Q!\u0001\u0002\t\u0002-\tQ\"\u0012:s_JD\u0015M\u001c3mS:<'BA\u0002\u0005\u0003\u0015\u0019\b/\u0019:l\u0015\t)a!A\u0005he\u0016,g\u000e\u001d7v[*\u0011q\u0001C\u0001\ba&4x\u000e^1m\u0015\u0005I\u0011AA5p\u0007\u0001\u0001\"\u0001D\u0007\u000e\u0003\t1QA\u0004\u0002\t\u0002=\u0011Q\"\u0012:s_JD\u0015M\u001c3mS:<7CA\u0007\u0011!\t\tB#D\u0001\u0013\u0015\u0005\u0019\u0012!B:dC2\f\u0017BA\u000b\u0013\u0005\u0019\te.\u001f*fM\")q#\u0004C\u00011\u00051A(\u001b8jiz\"\u0012a\u0003\u0005\u000655!\taG\u0001\u0011oJ\f\u0007/\u0012:s_JlUm]:bO\u0016,\"\u0001\b\u001b\u0015\u0005ui\u0004\u0003B\t\u001fA1J!a\b\n\u0003\u001fA\u000b'\u000f^5bY\u001a+hn\u0019;j_:\u0004\"!I\u0015\u000f\u0005\t:cBA\u0012'\u001b\u0005!#BA\u0013\u000b\u0003\u0019a$o\\8u}%\t1#\u0003\u0002)%\u00059\u0001/Y2lC\u001e,\u0017B\u0001\u0016,\u0005%!\u0006N]8xC\ndWM\u0003\u0002)%A\u0019Q\u0006\r\u001a\u000e\u00039R!a\f\n\u0002\tU$\u0018\u000e\\\u0005\u0003c9\u00121\u0001\u0016:z!\t\u0019D\u0007\u0004\u0001\u0005\u000bUJ\"\u0019\u0001\u001c\u0003\u0003U\u000b\"a\u000e\u001e\u0011\u0005EA\u0014BA\u001d\u0013\u0005\u001dqu\u000e\u001e5j]\u001e\u0004\"!E\u001e\n\u0005q\u0012\"aA!os\")a(\u0007a\u0001\u007f\u00059Q.Z:tC\u001e,\u0007C\u0001!D\u001d\t\t\u0012)\u0003\u0002C%\u00051\u0001K]3eK\u001aL!\u0001R#\u0003\rM#(/\u001b8h\u0015\t\u0011%\u0003")
public final class ErrorHandling
{
    public static <U> PartialFunction<Throwable, Try<U>> wrapErrorMessage(final String message) {
        return ErrorHandling$.MODULE$.wrapErrorMessage(message);
    }
}
