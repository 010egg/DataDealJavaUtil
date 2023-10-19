// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

import scala.reflect.ScalaSignature;

@ScalaSignature(bytes = "\u0006\u0001\u0005\rq!B\u0001\u0003\u0011\u00039\u0011!B'fe\u001e,'BA\u0002\u0005\u0003\u0019Q7o\u001c85g*\tQ!A\u0002pe\u001e\u001c\u0001\u0001\u0005\u0002\t\u00135\t!AB\u0003\u000b\u0005!\u00051BA\u0003NKJ<Wm\u0005\u0002\n\u0019A\u0011Q\u0002E\u0007\u0002\u001d)\tq\"A\u0003tG\u0006d\u0017-\u0003\u0002\u0012\u001d\t1\u0011I\\=SK\u001aDQaE\u0005\u0005\u0002Q\ta\u0001P5oSRtD#A\u0004\t\u000bYIA\u0011A\f\u0002\u000b5,'oZ3\u0016\tay#\u0007\b\u000b\u00043Q2DC\u0001\u000e*!\tYB\u0004\u0004\u0001\u0005\u000bu)\"\u0019\u0001\u0010\u0003\u0003I\u000b\"a\b\u0012\u0011\u00055\u0001\u0013BA\u0011\u000f\u0005\u001dqu\u000e\u001e5j]\u001e\u0004\"a\t\u0014\u000f\u0005!!\u0013BA\u0013\u0003\u0003\u001dQ5o\u001c8B'RK!a\n\u0015\u0003\r)3\u0016\r\\;f\u0015\t)#\u0001C\u0003++\u0001\u000f1&\u0001\u0005j]N$\u0018M\\2f!\u0015AAFL\u0019\u001b\u0013\ti#A\u0001\u0005NKJ<W\rR3q!\tYr\u0006B\u00031+\t\u0007aDA\u0001B!\tY\"\u0007B\u00034+\t\u0007aDA\u0001C\u0011\u0015)T\u00031\u0001/\u0003\u00111\u0018\r\\\u0019\t\u000b]*\u0002\u0019A\u0019\u0002\tY\fGN\r\u0005\u0007s%!\tA\u0001\u001e\u0002\u00175,'oZ3GS\u0016dGm\u001d\u000b\u0004w)c\u0005c\u0001\u001fE\u000f:\u0011QH\u0011\b\u0003}\u0005k\u0011a\u0010\u0006\u0003\u0001\u001a\ta\u0001\u0010:p_Rt\u0014\"A\b\n\u0005\rs\u0011a\u00029bG.\fw-Z\u0005\u0003\u000b\u001a\u0013A\u0001T5ti*\u00111I\u0004\t\u0003G!K!!\u0013\u0015\u0003\r)3\u0015.\u001a7e\u0011\u0015Y\u0005\b1\u0001<\u0003\r18/\r\u0005\u0006\u001bb\u0002\raO\u0001\u0004mN\u0014\u0004BB(\n\t\u0003\u0011\u0001+A\u0005nKJ<WMV1mgR\u0019\u0011KU*\u0011\u0007q\"%\u0005C\u0003L\u001d\u0002\u0007\u0011\u000bC\u0003N\u001d\u0002\u0007\u0011K\u0002\u0005V\u0013A\u0005\u0019\u0011\u0001\u0002W\u0005%iUM]4fC\ndWmE\u0002U\u0019]\u0003\"\u0001\u0003-\n\u0005e\u0013!!C'fe\u001e,G)\u001a9t\u0011\u0015YF\u000b\"\u0001]\u0003\u0019!\u0013N\\5uIQ\tQ\f\u0005\u0002\u000e=&\u0011qL\u0004\u0002\u0005+:LG\u000fC\u0003b)\u0012\r!-A\u0002ke5,\"aY@\u0015\u0007\u0011\f\t\u0001E\u0002fMzl\u0011\u0001\u0016\u0004\u0005OR\u0003\u0001NA\u0006NKJ<WmU=oi\u0006DXCA5n'\t1G\u0002\u0003\u0005lM\n\u0005\t\u0015!\u0003m\u0003\u0011Q7o\u001c8\u0011\u0005miG!\u0002\u0019g\u0005\u0004q\u0002\"B\ng\t\u0003yGC\u00019r!\r)g\r\u001c\u0005\u0006W:\u0004\r\u0001\u001c\u0005\u0006-\u0019$\ta]\u000b\u0004in<HCA;})\t1\b\u0010\u0005\u0002\u001co\u0012)QD\u001db\u0001=!)!F\u001da\u0002sB)\u0001\u0002\f7{mB\u00111d\u001f\u0003\u0006gI\u0014\rA\b\u0005\u0006{J\u0004\rA_\u0001\u0006_RDWM\u001d\t\u00037}$Q\u0001\r1C\u0002yAQa\u001b1A\u0002y\u0004")
public final class Merge
{
    public static <A extends JsonAST.JValue, B extends JsonAST.JValue, R extends JsonAST.JValue> R merge(final A val1, final B val2, final MergeDep<A, B, R> instance) {
        return Merge$.MODULE$.merge(val1, val2, instance);
    }
    
    public interface Mergeable extends MergeDeps
    {
         <A extends JsonAST.JValue> MergeSyntax<A> j2m(final A p0);
        
        public class MergeSyntax<A extends JsonAST.JValue>
        {
            private final A json;
            
            public <B extends JsonAST.JValue, R extends JsonAST.JValue> R merge(final B other, final MergeDep<A, B, R> instance) {
                return Merge$.MODULE$.merge(this.json, other, instance);
            }
            
            public MergeSyntax(final A json) {
                this.json = json;
                if (Mergeable.this == null) {
                    throw null;
                }
            }
        }
    }
}
