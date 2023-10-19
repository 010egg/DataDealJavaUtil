// 
// Decompiled by Procyon v0.5.36
// 

package io.pivotal.greenplum.spark.externaltable;

import io.pivotal.greenplum.spark.conf.ConnectorOptions;
import org.slf4j.Logger;
import scala.Function0;
import scala.reflect.ScalaSignature;

@ScalaSignature(bytes = "\u0006\u0001-<Q!\u0001\u0002\t\u00025\tQc\u00129gI&\u001cHoU3sm&\u001cW-T1oC\u001e,'O\u0003\u0002\u0004\t\u0005iQ\r\u001f;fe:\fG\u000e^1cY\u0016T!!\u0002\u0004\u0002\u000bM\u0004\u0018M]6\u000b\u0005\u001dA\u0011!C4sK\u0016t\u0007\u000f\\;n\u0015\tI!\"A\u0004qSZ|G/\u00197\u000b\u0003-\t!![8\u0004\u0001A\u0011abD\u0007\u0002\u0005\u0019)\u0001C\u0001E\u0001#\t)r\t\u001d4eSN$8+\u001a:wS\u000e,W*\u00198bO\u0016\u00148cA\b\u00131A\u00111CF\u0007\u0002))\tQ#A\u0003tG\u0006d\u0017-\u0003\u0002\u0018)\t1\u0011I\\=SK\u001a\u0004\"!\u0007\u000e\u000e\u0003\u0011I!a\u0007\u0003\u0003\u000f1{wmZ5oO\")Qd\u0004C\u0001=\u00051A(\u001b8jiz\"\u0012!\u0004\u0005\bA=\u0011\r\u0011\"\u0003\"\u0003%\u0011WO\u001a4fe6\u000b\u0007/F\u0001#!\u0011\u0019#\u0006L\u001a\u000e\u0003\u0011R!!\n\u0014\u0002\u0015\r|gnY;se\u0016tGO\u0003\u0002(Q\u0005!Q\u000f^5m\u0015\u0005I\u0013\u0001\u00026bm\u0006L!a\u000b\u0013\u0003#\r{gnY;se\u0016tG\u000fS1tQ6\u000b\u0007\u000f\u0005\u0002.a9\u00111CL\u0005\u0003_Q\ta\u0001\u0015:fI\u00164\u0017BA\u00193\u0005\u0019\u0019FO]5oO*\u0011q\u0006\u0006\t\u0004iYBT\"A\u001b\u000b\u0005\u001d\"\u0012BA\u001c6\u0005\r!&/\u001f\t\u0003smj\u0011A\u000f\u0006\u0003O\u0011I!\u0001\u0010\u001e\u0003\u001fQ\u0013\u0018M\\:bGRLwN\u001c#bi\u0006DaAP\b!\u0002\u0013\u0011\u0013A\u00032vM\u001a,'/T1qA!9\u0001i\u0004b\u0001\n\u0013\t\u0015!D:f]\u0012\u0014UO\u001a4fe6\u000b\u0007/F\u0001C!\u0011\u0019#\u0006L\"\u0011\u00059!\u0015BA#\u0003\u00055\u0001\u0016M\u001d;ji&|g\u000eR1uC\"1qi\u0004Q\u0001\n\t\u000bab]3oI\n+hMZ3s\u001b\u0006\u0004\b\u0005C\u0004J\u001f\t\u0007I\u0011\u0002&\u0002\u0017M,'O^5dKNl\u0015\r]\u000b\u0002\u0017B!1E\u000b'P!\tqQ*\u0003\u0002O\u0005\tQ1+\u001a:wS\u000e,7*Z=\u0011\u00059\u0001\u0016BA)\u0003\u000599\u0005O\u001a3jgR\u001cVM\u001d<jG\u0016DaaU\b!\u0002\u0013Y\u0015\u0001D:feZL7-Z:NCB\u0004\u0003\"B+\u0010\t\u00031\u0016AC4fiN+'O^5dKR\u0011qj\u0016\u0005\u00061R\u0003\r!W\u0001\u0011G>tg.Z2u_J|\u0005\u000f^5p]N\u0004\"AW/\u000e\u0003mS!\u0001\u0018\u0003\u0002\t\r|gNZ\u0005\u0003=n\u0013\u0001cQ8o]\u0016\u001cGo\u001c:PaRLwN\\:\t\u000b\u0001|A\u0011A1\u0002\u001bM$x\u000e]!oIJ+Wn\u001c<f)\t\u0011W\r\u0005\u0002\u0014G&\u0011A\r\u0006\u0002\u0005+:LG\u000fC\u0003g?\u0002\u0007A*A\u0002lKfDQ\u0001[\b\u0005\n%\fQbZ3u'\u0016\u0014h/\u001a:I_N$HC\u0001\u0017k\u0011\u0015Av\r1\u0001Z\u0001")
public final class GpfdistServiceManager
{
    public static void logDebug(final Function0<String> msg) {
        GpfdistServiceManager$.MODULE$.logDebug(msg);
    }
    
    public static void logWarning(final Function0<String> msg) {
        GpfdistServiceManager$.MODULE$.logWarning(msg);
    }
    
    public static Logger log() {
        return GpfdistServiceManager$.MODULE$.log();
    }
    
    public static void stopAndRemove(final ServiceKey key) {
        GpfdistServiceManager$.MODULE$.stopAndRemove(key);
    }
    
    public static GpfdistService getService(final ConnectorOptions connectorOptions) {
        return GpfdistServiceManager$.MODULE$.getService(connectorOptions);
    }
}
