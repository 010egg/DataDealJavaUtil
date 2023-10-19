// 
// Decompiled by Procyon v0.5.36
// 

package io.pivotal.greenplum.spark.util;

import java.util.ArrayList;
import scala.Function0;
import scala.util.Try$;
import scala.runtime.BoxedUnit;
import scala.util.Try;
import java.io.InputStream;
import java.util.List;
import scala.reflect.ScalaSignature;

@ScalaSignature(bytes = "\u0006\u0001\u00153A!\u0001\u0002\u0001\u001b\tyA*\u0019:hK\nKH/\u001a\"vM\u001a,'O\u0003\u0002\u0004\t\u0005!Q\u000f^5m\u0015\t)a!A\u0003ta\u0006\u00148N\u0003\u0002\b\u0011\u0005IqM]3f]BdW/\u001c\u0006\u0003\u0013)\tq\u0001]5w_R\fGNC\u0001\f\u0003\tIwn\u0001\u0001\u0014\u0005\u0001q\u0001CA\b\u0013\u001b\u0005\u0001\"\"A\t\u0002\u000bM\u001c\u0017\r\\1\n\u0005M\u0001\"AB!osJ+g\rC\u0003\u0016\u0001\u0011\u0005a#\u0001\u0004=S:LGO\u0010\u000b\u0002/A\u0011\u0001\u0004A\u0007\u0002\u0005!9!\u0004\u0001b\u0001\n\u0013Y\u0012A\u00032vM\u001a,'\u000fT5tiV\tA\u0004E\u0002\u001eC\rj\u0011A\b\u0006\u0003\u0007}Q\u0011\u0001I\u0001\u0005U\u00064\u0018-\u0003\u0002#=\t!A*[:u!\ryAEJ\u0005\u0003KA\u0011Q!\u0011:sCf\u0004\"aD\u0014\n\u0005!\u0002\"\u0001\u0002\"zi\u0016DaA\u000b\u0001!\u0002\u0013a\u0012a\u00032vM\u001a,'\u000fT5ti\u0002BQ\u0001\f\u0001\u0005\u00025\nabZ3u\u0013:\u0004X\u000f^*ue\u0016\fW\u000eF\u0001/!\ty\u0013'D\u00011\u0015\tYq$\u0003\u00023a\tY\u0011J\u001c9viN#(/Z1n\u0011\u0015!\u0004\u0001\"\u00016\u0003\u00159(/\u001b;f)\r1d\b\u0011\t\u0004oeZT\"\u0001\u001d\u000b\u0005\r\u0001\u0012B\u0001\u001e9\u0005\r!&/\u001f\t\u0003\u001fqJ!!\u0010\t\u0003\tUs\u0017\u000e\u001e\u0005\u0006\u007fM\u0002\rAL\u0001\fS:\u0004X\u000f^*ue\u0016\fW\u000eC\u0003Bg\u0001\u0007!)\u0001\u0003tSj,\u0007CA\bD\u0013\t!\u0005CA\u0002J]R\u0004")
public class LargeByteBuffer
{
    private final List<byte[]> io$pivotal$greenplum$spark$util$LargeByteBuffer$$bufferList;
    
    public List<byte[]> io$pivotal$greenplum$spark$util$LargeByteBuffer$$bufferList() {
        return this.io$pivotal$greenplum$spark$util$LargeByteBuffer$$bufferList;
    }
    
    public InputStream getInputStream() {
        return (InputStream)new LargeByteBuffer$$anon.LargeByteBuffer$$anon$1(this);
    }
    
    public Try<BoxedUnit> write(final InputStream inputStream, final int size) {
        return (Try<BoxedUnit>)Try$.MODULE$.apply((Function0)new LargeByteBuffer$$anonfun$write.LargeByteBuffer$$anonfun$write$1(this, inputStream, size));
    }
    
    public LargeByteBuffer() {
        this.io$pivotal$greenplum$spark$util$LargeByteBuffer$$bufferList = new ArrayList<byte[]>();
    }
}
