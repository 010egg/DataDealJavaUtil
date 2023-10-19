// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.scalap.scalasig;

import scala.runtime.Nothing$;
import org.json4s.scalap.Rule;
import scala.reflect.ScalaSignature;
import org.json4s.scalap.RulesWithState;

@ScalaSignature(bytes = "\u0006\u0001}3q!\u0001\u0002\u0011\u0002\u0007\u00051B\u0001\bCsR,7i\u001c3f%\u0016\fG-\u001a:\u000b\u0005\r!\u0011\u0001C:dC2\f7/[4\u000b\u0005\u00151\u0011AB:dC2\f\u0007O\u0003\u0002\b\u0011\u00051!n]8oiMT\u0011!C\u0001\u0004_J<7\u0001A\n\u0004\u00011\u0011\u0002CA\u0007\u0011\u001b\u0005q!\"A\b\u0002\u000bM\u001c\u0017\r\\1\n\u0005Eq!AB!osJ+g\r\u0005\u0002\u0014)5\tA!\u0003\u0002\u0016\t\tq!+\u001e7fg^KG\u000f[*uCR,\u0007\"B\f\u0001\t\u0003A\u0012A\u0002\u0013j]&$H\u0005F\u0001\u001a!\ti!$\u0003\u0002\u001c\u001d\t!QK\\5u\u000b\u0011i\u0002\u0001\u0001\u0010\u0003\u0003M\u0003\"a\b\u0011\u000e\u0003\tI!!\t\u0002\u0003\u0011\tKH/Z\"pI\u0016,Aa\t\u0001\u0001I\t1\u0001+\u0019:tKJ,\"!J\u0017\u0011\t\u0019:3FN\u0007\u0002\u0001%\u0011\u0001&\u000b\u0002\u0005%VdW-\u0003\u0002+\t\tQ1\u000b^1uKJ+H.Z:\u0011\u00051jC\u0002\u0001\u0003\u0006]\t\u0012\ra\f\u0002\u0002\u0003F\u0011\u0001g\r\t\u0003\u001bEJ!A\r\b\u0003\u000f9{G\u000f[5oOB\u0011Q\u0002N\u0005\u0003k9\u00111!\u00118z!\t9$H\u0004\u0002\u000eq%\u0011\u0011HD\u0001\u0007!J,G-\u001a4\n\u0005mb$AB*ue&twM\u0003\u0002:\u001d!9a\b\u0001b\u0001\n\u0003y\u0014\u0001\u00022zi\u0016,\u0012\u0001\u0011\t\u0007'\u0005\u0013%i\u0011\u0019\n\u0005!\"\u0001C\u0001\u0014\u001d!\tiA)\u0003\u0002F\u001d\t!!)\u001f;f\u0011\u00199\u0005\u0001)A\u0005\u0001\u0006)!-\u001f;fA!9\u0011\n\u0001b\u0001\n\u0003Q\u0015AA;2+\u0005Y\u0005CB\nB\u0005\nc\u0005\u0007\u0005\u0002\u000e\u001b&\u0011aJ\u0004\u0002\u0004\u0013:$\bB\u0002)\u0001A\u0003%1*A\u0002vc\u0001BqA\u0015\u0001C\u0002\u0013\u0005!*\u0001\u0002ve!1A\u000b\u0001Q\u0001\n-\u000b1!\u001e\u001a!\u0011\u001d1\u0006A1A\u0005\u0002)\u000b!!\u001e\u001b\t\ra\u0003\u0001\u0015!\u0003L\u0003\r)H\u0007\t\u0005\u00065\u0002!\taW\u0001\u0006Ef$Xm\u001d\u000b\u00039v\u0003baE!C\u0005z\u0001\u0004\"\u00020Z\u0001\u0004a\u0015!\u00018")
public interface ByteCodeReader extends RulesWithState
{
    void org$json4s$scalap$scalasig$ByteCodeReader$_setter_$byte_$eq(final Rule p0);
    
    void org$json4s$scalap$scalasig$ByteCodeReader$_setter_$u1_$eq(final Rule p0);
    
    void org$json4s$scalap$scalasig$ByteCodeReader$_setter_$u2_$eq(final Rule p0);
    
    void org$json4s$scalap$scalasig$ByteCodeReader$_setter_$u4_$eq(final Rule p0);
    
    Rule<ByteCode, ByteCode, Object, Nothing$> byte();
    
    Rule<ByteCode, ByteCode, Object, Nothing$> u1();
    
    Rule<ByteCode, ByteCode, Object, Nothing$> u2();
    
    Rule<ByteCode, ByteCode, Object, Nothing$> u4();
    
    Rule<ByteCode, ByteCode, ByteCode, Nothing$> bytes(final int p0);
}
