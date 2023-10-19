// 
// Decompiled by Procyon v0.5.36
// 

package io.pivotal.greenplum.spark.util;

import java.util.HashMap;
import scala.runtime.BoxesRunTime;
import scala.Function0;
import scala.Predef$;
import scala.runtime.BoxedUnit;
import scala.util.Try;
import java.io.InputStream;
import java.util.Map;
import scala.reflect.ScalaSignature;

@ScalaSignature(bytes = "\u0006\u0001Y3A!\u0001\u0002\u0001\u001b\tyAK]1og\u0006\u001cG/[8o\t\u0006$\u0018M\u0003\u0002\u0004\t\u0005!Q\u000f^5m\u0015\t)a!A\u0003ta\u0006\u00148N\u0003\u0002\b\u0011\u0005IqM]3f]BdW/\u001c\u0006\u0003\u0013)\tq\u0001]5w_R\fGNC\u0001\f\u0003\tIwn\u0001\u0001\u0014\u0005\u0001q\u0001CA\b\u0013\u001b\u0005\u0001\"\"A\t\u0002\u000bM\u001c\u0017\r\\1\n\u0005M\u0001\"AB!osJ+g\rC\u0003\u0016\u0001\u0011\u0005a#\u0001\u0004=S:LGO\u0010\u000b\u0002/A\u0011\u0001\u0004A\u0007\u0002\u0005!9!\u0004\u0001b\u0001\n\u0003Y\u0012!D#O\t~{eiX*U%\u0016\u000bU*F\u0001\u001d!\tyQ$\u0003\u0002\u001f!\t\u0019\u0011J\u001c;\t\r\u0001\u0002\u0001\u0015!\u0003\u001d\u00039)e\nR0P\r~\u001bFKU#B\u001b\u0002BqA\t\u0001A\u0002\u0013%1%\u0001\bti\u0006\u0014H/\u001a3SK\u0006$\u0017N\\4\u0016\u0003\u0011\u0002\"aD\u0013\n\u0005\u0019\u0002\"a\u0002\"p_2,\u0017M\u001c\u0005\bQ\u0001\u0001\r\u0011\"\u0003*\u0003I\u0019H/\u0019:uK\u0012\u0014V-\u00193j]\u001e|F%Z9\u0015\u0005)j\u0003CA\b,\u0013\ta\u0003C\u0001\u0003V]&$\bb\u0002\u0018(\u0003\u0003\u0005\r\u0001J\u0001\u0004q\u0012\n\u0004B\u0002\u0019\u0001A\u0003&A%A\bti\u0006\u0014H/\u001a3SK\u0006$\u0017N\\4!\u0011\u001d\u0011\u0004A1A\u0005\nM\n\u0011BY;gM\u0016\u0014X*\u00199\u0016\u0003Q\u0002B!N\u001d\u001dw5\taG\u0003\u0002\u0004o)\t\u0001(\u0001\u0003kCZ\f\u0017B\u0001\u001e7\u0005\ri\u0015\r\u001d\t\u00031qJ!!\u0010\u0002\u0003\u001f1\u000b'oZ3CsR,')\u001e4gKJDaa\u0010\u0001!\u0002\u0013!\u0014A\u00032vM\u001a,'/T1qA!)\u0011\t\u0001C\u0001\u0005\u0006qq-\u001a;J]B,Ho\u0015;sK\u0006lG#A\"\u0011\u0005\u00113U\"A#\u000b\u0005-9\u0014BA$F\u0005-Ie\u000e];u'R\u0014X-Y7\t\u000b%\u0003A\u0011\u0001&\u0002\u000b]\u0014\u0018\u000e^3\u0015\t-\u0003&\u000b\u0016\t\u0004\u0019:SS\"A'\u000b\u0005\r\u0001\u0012BA(N\u0005\r!&/\u001f\u0005\u0006#\"\u0003\r\u0001H\u0001\ng\u0016<W.\u001a8u\u0013\u0012DQa\u0015%A\u0002\r\u000b1\"\u001b8qkR\u001cFO]3b[\")Q\u000b\u0013a\u00019\u0005i1m\u001c8uK:$H*\u001a8hi\"\u0004")
public class TransactionData
{
    private final int END_OF_STREAM;
    private boolean io$pivotal$greenplum$spark$util$TransactionData$$startedReading;
    private final Map<Object, LargeByteBuffer> io$pivotal$greenplum$spark$util$TransactionData$$bufferMap;
    
    public int END_OF_STREAM() {
        return this.END_OF_STREAM;
    }
    
    private boolean io$pivotal$greenplum$spark$util$TransactionData$$startedReading() {
        return this.io$pivotal$greenplum$spark$util$TransactionData$$startedReading;
    }
    
    public void io$pivotal$greenplum$spark$util$TransactionData$$startedReading_$eq(final boolean x$1) {
        this.io$pivotal$greenplum$spark$util$TransactionData$$startedReading = x$1;
    }
    
    public Map<Object, LargeByteBuffer> io$pivotal$greenplum$spark$util$TransactionData$$bufferMap() {
        return this.io$pivotal$greenplum$spark$util$TransactionData$$bufferMap;
    }
    
    public InputStream getInputStream() {
        return (InputStream)new TransactionData$$anon.TransactionData$$anon$1(this);
    }
    
    public Try<BoxedUnit> write(final int segmentId, final InputStream inputStream, final int contentLength) {
        Predef$.MODULE$.require(!this.io$pivotal$greenplum$spark$util$TransactionData$$startedReading(), (Function0)new TransactionData$$anonfun$write.TransactionData$$anonfun$write$1(this));
        LargeByteBuffer partitionBuffer = null;
        Label_0148: {
            if (this.io$pivotal$greenplum$spark$util$TransactionData$$bufferMap().containsKey(BoxesRunTime.boxToInteger(segmentId))) {
                partitionBuffer = this.io$pivotal$greenplum$spark$util$TransactionData$$bufferMap().get(BoxesRunTime.boxToInteger(segmentId));
                final BoxedUnit unit = BoxedUnit.UNIT;
                break Label_0148;
            }
            synchronized (this.io$pivotal$greenplum$spark$util$TransactionData$$bufferMap()) {
                if (this.io$pivotal$greenplum$spark$util$TransactionData$$bufferMap().containsKey(BoxesRunTime.boxToInteger(segmentId))) {
                    partitionBuffer = this.io$pivotal$greenplum$spark$util$TransactionData$$bufferMap().get(BoxesRunTime.boxToInteger(segmentId));
                    final BoxedUnit unit2 = BoxedUnit.UNIT;
                }
                else {
                    partitionBuffer = new LargeByteBuffer();
                    this.io$pivotal$greenplum$spark$util$TransactionData$$bufferMap().put(BoxesRunTime.boxToInteger(segmentId), partitionBuffer);
                }
                // monitorexit(this.io$pivotal$greenplum$spark$util$TransactionData$$bufferMap())
                return partitionBuffer.write(inputStream, contentLength);
            }
        }
    }
    
    public TransactionData() {
        this.END_OF_STREAM = -1;
        this.io$pivotal$greenplum$spark$util$TransactionData$$startedReading = false;
        this.io$pivotal$greenplum$spark$util$TransactionData$$bufferMap = new HashMap<Object, LargeByteBuffer>();
    }
}
