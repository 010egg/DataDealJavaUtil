// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

import scala.Option;
import scala.reflect.ScalaSignature;

@ScalaSignature(bytes = "\u0006\u000154q!\u0001\u0002\u0011\u0002\u0007\u0005qAA\u0006Kg>tW*\u001a;i_\u0012\u001c(BA\u0002\u0005\u0003\u0019Q7o\u001c85g*\tQ!A\u0002pe\u001e\u001c\u0001!\u0006\u0002\tkM\u0011\u0001!\u0003\t\u0003\u00155i\u0011a\u0003\u0006\u0002\u0019\u0005)1oY1mC&\u0011ab\u0003\u0002\u0007\u0003:L(+\u001a4\t\u000bA\u0001a\u0011A\t\u0002\u000bA\f'o]3\u0015\tIQr\u0004\n\t\u0003']q!\u0001F\u000b\u000e\u0003\tI!A\u0006\u0002\u0002\u000fA\f7m[1hK&\u0011\u0001$\u0007\u0002\u0007\u0015Z\u000bG.^3\u000b\u0005Y\u0011\u0001\"B\u000e\u0010\u0001\u0004a\u0012AA5o!\t!R$\u0003\u0002\u001f\u0005\tI!j]8o\u0013:\u0004X\u000f\u001e\u0005\bA=\u0001\n\u00111\u0001\"\u0003Y)8/\u001a\"jO\u0012+7-[7bY\u001a{'\u000fR8vE2,\u0007C\u0001\u0006#\u0013\t\u00193BA\u0004C_>dW-\u00198\t\u000f\u0015z\u0001\u0013!a\u0001C\u0005\u0001Ro]3CS\u001eLe\u000e\u001e$pe2{gn\u001a\u0005\u0006O\u00011\t\u0001K\u0001\ta\u0006\u00148/Z(qiR!\u0011\u0006L\u0017/!\rQ!FE\u0005\u0003W-\u0011aa\u00149uS>t\u0007\"B\u000e'\u0001\u0004a\u0002b\u0002\u0011'!\u0003\u0005\r!\t\u0005\bK\u0019\u0002\n\u00111\u0001\"\u0011\u0015\u0001\u0004A\"\u00012\u0003\u0019\u0011XM\u001c3feR\u0011!g\u0011\u000b\u0003gy\u0002\"\u0001N\u001b\r\u0001\u0011)a\u0007\u0001b\u0001o\t\tA+\u0005\u00029wA\u0011!\"O\u0005\u0003u-\u0011qAT8uQ&tw\r\u0005\u0002\u000by%\u0011Qh\u0003\u0002\u0004\u0003:L\bbB 0!\u0003\u0005\u001d\u0001Q\u0001\bM>\u0014X.\u0019;t!\t!\u0012)\u0003\u0002C\u0005\t9ai\u001c:nCR\u001c\b\"\u0002#0\u0001\u0004\u0011\u0012!\u0002<bYV,\u0007\"\u0002$\u0001\r\u00039\u0015aB2p[B\f7\r\u001e\u000b\u0003\u0011>\u0003\"!\u0013'\u000f\u0005)Q\u0015BA&\f\u0003\u0019\u0001&/\u001a3fM&\u0011QJ\u0014\u0002\u0007'R\u0014\u0018N\\4\u000b\u0005-[\u0001\"\u0002)F\u0001\u0004\u0019\u0014!\u00013\t\u000bI\u0003a\u0011A*\u0002\rA\u0014X\r\u001e;z)\tAE\u000bC\u0003Q#\u0002\u00071\u0007C\u0004W\u0001E\u0005I\u0011A,\u0002\u001fA\f'o]3%I\u00164\u0017-\u001e7uII*\u0012\u0001\u0017\u0016\u0003Ce[\u0013A\u0017\t\u00037\u0002l\u0011\u0001\u0018\u0006\u0003;z\u000b\u0011\"\u001e8dQ\u0016\u001c7.\u001a3\u000b\u0005}[\u0011AC1o]>$\u0018\r^5p]&\u0011\u0011\r\u0018\u0002\u0012k:\u001c\u0007.Z2lK\u00124\u0016M]5b]\u000e,\u0007bB2\u0001#\u0003%\taV\u0001\u0010a\u0006\u00148/\u001a\u0013eK\u001a\fW\u000f\u001c;%g!9Q\rAI\u0001\n\u00039\u0016A\u00059beN,w\n\u001d;%I\u00164\u0017-\u001e7uIIBqa\u001a\u0001\u0012\u0002\u0013\u0005q+\u0001\nqCJ\u001cXm\u00149uI\u0011,g-Y;mi\u0012\u001a\u0004bB5\u0001#\u0003%\tA[\u0001\u0011e\u0016tG-\u001a:%I\u00164\u0017-\u001e7uII\"\"a\u001b7+\u0005\u0001K\u0006\"\u0002#i\u0001\u0004\u0011\u0002")
public interface JsonMethods<T>
{
    JsonAST.JValue parse(final JsonInput p0, final boolean p1, final boolean p2);
    
    boolean parse$default$2();
    
    boolean parse$default$3();
    
    Option<JsonAST.JValue> parseOpt(final JsonInput p0, final boolean p1, final boolean p2);
    
    boolean parseOpt$default$2();
    
    boolean parseOpt$default$3();
    
    T render(final JsonAST.JValue p0, final Formats p1);
    
    Formats render$default$2(final JsonAST.JValue p0);
    
    String compact(final T p0);
    
    String pretty(final T p0);
}
