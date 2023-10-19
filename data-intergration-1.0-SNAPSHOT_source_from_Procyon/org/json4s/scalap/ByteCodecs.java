// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.scalap;

import scala.reflect.ScalaSignature;

@ScalaSignature(bytes = "\u0006\u0001A:Q!\u0001\u0002\t\u0002%\t!BQ=uK\u000e{G-Z2t\u0015\t\u0019A!\u0001\u0004tG\u0006d\u0017\r\u001d\u0006\u0003\u000b\u0019\taA[:p]R\u001a(\"A\u0004\u0002\u0007=\u0014xm\u0001\u0001\u0011\u0005)YQ\"\u0001\u0002\u0007\u000b1\u0011\u0001\u0012A\u0007\u0003\u0015\tKH/Z\"pI\u0016\u001c7o\u0005\u0002\f\u001dA\u0011qBE\u0007\u0002!)\t\u0011#A\u0003tG\u0006d\u0017-\u0003\u0002\u0014!\t1\u0011I\\=SK\u001aDQ!F\u0006\u0005\u0002Y\ta\u0001P5oSRtD#A\u0005\t\u000baYA\u0011A\r\u0002\u001dI,w-\u001a8fe\u0006$XMW3s_R\u0011!$\b\t\u0003\u001fmI!\u0001\b\t\u0003\u0007%sG\u000fC\u0003\u001f/\u0001\u0007q$A\u0002te\u000e\u00042a\u0004\u0011#\u0013\t\t\u0003CA\u0003BeJ\f\u0017\u0010\u0005\u0002\u0010G%\u0011A\u0005\u0005\u0002\u0005\u0005f$X\rC\u0003'\u0017\u0011\u0005q%\u0001\u0006eK\u000e|G-Z\u001cu_b\"2A\u0007\u0015*\u0011\u0015qR\u00051\u0001 \u0011\u0015QS\u00051\u0001\u001b\u0003\u0019\u0019(o\u00197f]\")Af\u0003C\u0001[\u00051A-Z2pI\u0016$\"A\u0007\u0018\t\u000b=Z\u0003\u0019A\u0010\u0002\u0005a\u001c\b")
public final class ByteCodecs
{
    public static int decode(final byte[] xs) {
        return ByteCodecs$.MODULE$.decode(xs);
    }
    
    public static int decode7to8(final byte[] src, final int srclen) {
        return ByteCodecs$.MODULE$.decode7to8(src, srclen);
    }
    
    public static int regenerateZero(final byte[] src) {
        return ByteCodecs$.MODULE$.regenerateZero(src);
    }
}
