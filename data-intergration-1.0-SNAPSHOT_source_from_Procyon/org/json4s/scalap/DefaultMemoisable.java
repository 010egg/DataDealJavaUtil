// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.scalap;

import scala.Function0;
import scala.collection.mutable.HashMap;
import scala.reflect.ScalaSignature;

@ScalaSignature(bytes = "\u0006\u0001-<Q!\u0001\u0002\t\u0002%\t\u0011\u0003R3gCVdG/T3n_&\u001c\u0018M\u00197f\u0015\t\u0019A!\u0001\u0004tG\u0006d\u0017\r\u001d\u0006\u0003\u000b\u0019\taA[:p]R\u001a(\"A\u0004\u0002\u0007=\u0014xm\u0001\u0001\u0011\u0005)YQ\"\u0001\u0002\u0007\u000b1\u0011\u0001\u0012A\u0007\u0003#\u0011+g-Y;mi6+Wn\\5tC\ndWm\u0005\u0002\f\u001dA\u0011qBE\u0007\u0002!)\t\u0011#A\u0003tG\u0006d\u0017-\u0003\u0002\u0014!\t1\u0011I\\=SK\u001aDQ!F\u0006\u0005\u0002Y\ta\u0001P5oSRtD#A\u0005\t\u000faY\u0001\u0019!C\u00013\u0005)A-\u001a2vOV\t!\u0004\u0005\u0002\u00107%\u0011A\u0004\u0005\u0002\b\u0005>|G.Z1o\u0011\u001dq2\u00021A\u0005\u0002}\t\u0011\u0002Z3ck\u001e|F%Z9\u0015\u0005\u0001\u001a\u0003CA\b\"\u0013\t\u0011\u0003C\u0001\u0003V]&$\bb\u0002\u0013\u001e\u0003\u0003\u0005\rAG\u0001\u0004q\u0012\n\u0004B\u0002\u0014\fA\u0003&!$\u0001\u0004eK\n,x\r\t\u0004\b\u0019\t\u0001\n1!\u0001)'\r9c\"\u000b\t\u0003\u0015)J!a\u000b\u0002\u0003\u00155+Wn\\5tC\ndW\rC\u0003.O\u0011\u0005a&\u0001\u0004%S:LG\u000f\n\u000b\u0002A!9\u0001g\nb\u0001\n#\t\u0014aA7baV\t!\u0007\u0005\u00034q9QT\"\u0001\u001b\u000b\u0005U2\u0014aB7vi\u0006\u0014G.\u001a\u0006\u0003oA\t!bY8mY\u0016\u001cG/[8o\u0013\tIDGA\u0004ICNDW*\u00199\u0011\u0005=Y\u0014B\u0001\u001f\u0011\u0005\r\te.\u001f\u0005\u0007}\u001d\u0002\u000b\u0011\u0002\u001a\u0002\t5\f\u0007\u000f\t\u0005\u0006\u0001\u001e\"\t!Q\u0001\u0005[\u0016lw.\u0006\u0002C\u000bR\u00191iS'\u0011\u0005\u0011+E\u0002\u0001\u0003\u0006\r~\u0012\ra\u0012\u0002\u0002\u0003F\u0011\u0001J\u000f\t\u0003\u001f%K!A\u0013\t\u0003\u000f9{G\u000f[5oO\")Aj\u0010a\u0001\u001d\u0005\u00191.Z=\t\r9{D\u00111\u0001P\u0003\u0005\t\u0007cA\bQ\u0007&\u0011\u0011\u000b\u0005\u0002\ty\tLh.Y7f}!)1k\nC\t)\u000691m\\7qkR,WCA+[)\rQdk\u0016\u0005\u0006\u0019J\u0003\rA\u0004\u0005\u0007\u001dJ#\t\u0019\u0001-\u0011\u0007=\u0001\u0016\f\u0005\u0002E5\u0012)aI\u0015b\u0001\u000f\")Al\nC\t;\u0006IqN\\*vG\u000e,7o]\u000b\u0004=\u001aLGc\u0001\u0011`A\")Aj\u0017a\u0001\u001d!)\u0011m\u0017a\u0001E\u00061!/Z:vYR\u0004BAC2fQ&\u0011AM\u0001\u0002\b'V\u001c7-Z:t!\t!e\rB\u0003h7\n\u0007qIA\u0001T!\t!\u0015\u000eB\u0003k7\n\u0007qIA\u0001U\u0001")
public interface DefaultMemoisable extends Memoisable
{
    void org$json4s$scalap$DefaultMemoisable$_setter_$map_$eq(final HashMap p0);
    
    HashMap<Object, Object> map();
    
     <A> A memo(final Object p0, final Function0<A> p1);
    
     <A> Object compute(final Object p0, final Function0<A> p1);
    
     <S, T> void onSuccess(final Object p0, final Success<S, T> p1);
}
