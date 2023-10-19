// 
// Decompiled by Procyon v0.5.36
// 

package io.pivotal.greenplum.spark;

import scala.Function0;
import scala.runtime.TraitSetter;
import org.slf4j.Logger;
import scala.reflect.ScalaSignature;

@ScalaSignature(bytes = "\u0006\u0001\r3q!\u0001\u0002\u0011\u0002\u0007\u00051BA\u0004M_\u001e<\u0017N\\4\u000b\u0005\r!\u0011!B:qCJ\\'BA\u0003\u0007\u0003%9'/Z3oa2,XN\u0003\u0002\b\u0011\u00059\u0001/\u001b<pi\u0006d'\"A\u0005\u0002\u0005%|7\u0001A\n\u0003\u00011\u0001\"!\u0004\t\u000e\u00039Q\u0011aD\u0001\u0006g\u000e\fG.Y\u0005\u0003#9\u0011a!\u00118z%\u00164\u0007\"B\n\u0001\t\u0003!\u0012A\u0002\u0013j]&$H\u0005F\u0001\u0016!\tia#\u0003\u0002\u0018\u001d\t!QK\\5u\u0011%I\u0002\u00011AA\u0002\u0013%!$\u0001\u0003m_\u001e|V#A\u000e\u0011\u0005q\tS\"A\u000f\u000b\u0005yy\u0012!B:mMRR'\"\u0001\u0011\u0002\u0007=\u0014x-\u0003\u0002#;\t1Aj\\4hKJD\u0011\u0002\n\u0001A\u0002\u0003\u0007I\u0011B\u0013\u0002\u00111|wmX0%KF$\"!\u0006\u0014\t\u000f\u001d\u001a\u0013\u0011!a\u00017\u0005\u0019\u0001\u0010J\u0019\t\r%\u0002\u0001\u0015)\u0003\u001c\u0003\u0015awnZ0!Q\tA3\u0006\u0005\u0002\u000eY%\u0011QF\u0004\u0002\niJ\fgn]5f]RDQa\f\u0001\u0005\u0012i\t1\u0001\\8h\u0011\u0015\t\u0004\u0001\"\u00053\u0003)awnZ,be:Lgn\u001a\u000b\u0003+MBa\u0001\u000e\u0019\u0005\u0002\u0004)\u0014aA7tOB\u0019QB\u000e\u001d\n\u0005]r!\u0001\u0003\u001fcs:\fW.\u001a \u0011\u0005ebdBA\u0007;\u0013\tYd\"\u0001\u0004Qe\u0016$WMZ\u0005\u0003{y\u0012aa\u0015;sS:<'BA\u001e\u000f\u0011\u0015\u0001\u0005\u0001\"\u0005B\u0003!awn\u001a#fEV<GCA\u000bC\u0011\u0019!t\b\"a\u0001k\u0001")
public interface Logging
{
    Logger io$pivotal$greenplum$spark$Logging$$log_();
    
    @TraitSetter
    void io$pivotal$greenplum$spark$Logging$$log__$eq(final Logger p0);
    
    Logger log();
    
    void logWarning(final Function0<String> p0);
    
    void logDebug(final Function0<String> p0);
}
