// 
// Decompiled by Procyon v0.5.36
// 

package io.pivotal.greenplum.spark;

import scala.runtime.Null$;
import shadeio.univocity.parsers.csv.CsvParserSettings;
import scala.reflect.ScalaSignature;

@ScalaSignature(bytes = "\u0006\u0001\u0005\rq!B\u0001\u0003\u0011\u0003Y\u0011AE$sK\u0016t\u0007\u000f\\;n\u0007N3fi\u001c:nCRT!a\u0001\u0003\u0002\u000bM\u0004\u0018M]6\u000b\u0005\u00151\u0011!C4sK\u0016t\u0007\u000f\\;n\u0015\t9\u0001\"A\u0004qSZ|G/\u00197\u000b\u0003%\t!![8\u0004\u0001A\u0011A\"D\u0007\u0002\u0005\u0019)aB\u0001E\u0001\u001f\t\u0011rI]3f]BdW/\\\"T-\u001a{'/\\1u'\ti\u0001\u0003\u0005\u0002\u0012)5\t!CC\u0001\u0014\u0003\u0015\u00198-\u00197b\u0013\t)\"C\u0001\u0004B]f\u0014VM\u001a\u0005\u0006/5!\t\u0001G\u0001\u0007y%t\u0017\u000e\u001e \u0015\u0003-AqAG\u0007C\u0002\u0013\u00051$\u0001\u0004F'\u000e\u000b\u0005+R\u000b\u00029A\u0011\u0011#H\u0005\u0003=I\u0011Aa\u00115be\"1\u0001%\u0004Q\u0001\nq\tq!R*D\u0003B+\u0005\u0005C\u0004#\u001b\t\u0007I\u0011A\u000e\u0002\u001d\rC\u0015IU0E\u000b2KU*\u0013+F%\"1A%\u0004Q\u0001\nq\tqb\u0011%B%~#U\tT%N\u0013R+%\u000b\t\u0005\bM5\u0011\r\u0011\"\u0001(\u0003\u001dqUi\u0016'J\u001d\u0016+\u0012\u0001\u000b\t\u0003S9j\u0011A\u000b\u0006\u0003W1\nA\u0001\\1oO*\tQ&\u0001\u0003kCZ\f\u0017BA\u0018+\u0005\u0019\u0019FO]5oO\"1\u0011'\u0004Q\u0001\n!\n\u0001BT#X\u0019&sU\t\t\u0005\bg5\u0011\r\u0011\"\u0001\u001c\u0003\u0015\tVk\u0014+F\u0011\u0019)T\u0002)A\u00059\u00051\u0011+V(U\u000b\u0002BqaN\u0007C\u0002\u0013\u0005q%\u0001\u0007R+>#ViX*U%&su\t\u0003\u0004:\u001b\u0001\u0006I\u0001K\u0001\u000e#V{E+R0T)JKej\u0012\u0011\t\u000fmj!\u0019!C\u0001O\u0005ia+\u0011'V\u000b~{ei\u0018(V\u00192Ca!P\u0007!\u0002\u0013A\u0013A\u0004,B\u0019V+ul\u0014$`\u001dVcE\n\t\u0005\b\u007f5\u0011\r\u0011\"\u0001(\u0003-)U\n\u0015+Z?Z\u000bE*V#\t\r\u0005k\u0001\u0015!\u0003)\u00031)U\n\u0015+Z?Z\u000bE*V#!\u0011\u001d\u0019UB1A\u0005\u0002\u0011\u000b!BT+M\u0019~3\u0016\tT+F+\u0005)\u0005CA\tG\u0013\t9%C\u0001\u0003Ok2d\u0007BB%\u000eA\u0003%Q)A\u0006O+2cuLV!M+\u0016\u0003\u0003bB&\u000e\u0005\u0004%\taJ\u0001\u0011\t\u00163\u0015)\u0016'U?\u0016s5i\u0014#J\u001d\u001eCa!T\u0007!\u0002\u0013A\u0013!\u0005#F\r\u0006+F\nV0F\u001d\u000e{E)\u0013(HA!9q*\u0004b\u0001\n\u0003\u0001\u0016!D*U%&\u001bEkX)V\u001fR+5+F\u0001R!\t\t\"+\u0003\u0002T%\t9!i\\8mK\u0006t\u0007BB+\u000eA\u0003%\u0011+\u0001\bT)JK5\tV0R+>#Vi\u0015\u0011\t\u000f]k!\u0019!C\u0001!\u0006\t\u0012j\u0012(P%\u0016{v\u000bS%U\u000bN\u0003\u0016iQ#\t\rek\u0001\u0015!\u0003R\u0003IIuIT(S\u000b~;\u0006*\u0013+F'B\u000b5)\u0012\u0011\t\u000fmk!\u0019!C\u0001O\u0005iQiU\"B!\u0016#u,U+P)\u0016Ca!X\u0007!\u0002\u0013A\u0013AD#T\u0007\u0006\u0003V\tR0R+>#V\t\t\u0005\b?6\u0011\r\u0011\"\u0001a\u0003=Ig\u000e];u\u0005V4g-\u001a:TSj,W#A1\u0011\u0005E\u0011\u0017BA2\u0013\u0005\rIe\u000e\u001e\u0005\u0007K6\u0001\u000b\u0011B1\u0002!%t\u0007/\u001e;Ck\u001a4WM]*ju\u0016\u0004\u0003bB4\u000e\u0005\u0004%\t\u0001Y\u0001\u0012[\u0006D8\t[1sgB+'oQ8mk6t\u0007BB5\u000eA\u0003%\u0011-\u0001\nnCb\u001c\u0005.\u0019:t!\u0016\u00148i\u001c7v[:\u0004\u0003bB6\u000e\u0005\u0004%\t\u0001Y\u0001\u0016[\u0006DXI\u001d:pe\u000e{g\u000e^3oi2+gn\u001a;i\u0011\u0019iW\u0002)A\u0005C\u00061R.\u0019=FeJ|'oQ8oi\u0016tG\u000fT3oORD\u0007\u0005\u0003\u0005p\u001b!\u0015\r\u0011\"\u0001q\u0003\u001d!UIR!V\u0019R+\u0012!\u001d\t\u0003enl\u0011a\u001d\u0006\u0003iV\f1aY:w\u0015\t1x/A\u0004qCJ\u001cXM]:\u000b\u0005aL\u0018!C;oSZ|7-\u001b;z\u0015\u0005Q\u0018aA2p[&\u0011Ap\u001d\u0002\u0012\u0007N4\b+\u0019:tKJ\u001cV\r\u001e;j]\u001e\u001c\b\u0002\u0003@\u000e\u0011\u0003\u0005\u000b\u0015B9\u0002\u0011\u0011+e)Q+M)\u0002Ba!!\u0001\u000e\t\u0013\u0001\u0018AC4fi\u0012+g-Y;mi\u0002")
public final class GreenplumCSVFormat
{
    public static CsvParserSettings DEFAULT() {
        return GreenplumCSVFormat$.MODULE$.DEFAULT();
    }
    
    public static int maxErrorContentLength() {
        return GreenplumCSVFormat$.MODULE$.maxErrorContentLength();
    }
    
    public static int maxCharsPerColumn() {
        return GreenplumCSVFormat$.MODULE$.maxCharsPerColumn();
    }
    
    public static int inputBufferSize() {
        return GreenplumCSVFormat$.MODULE$.inputBufferSize();
    }
    
    public static String ESCAPED_QUOTE() {
        return GreenplumCSVFormat$.MODULE$.ESCAPED_QUOTE();
    }
    
    public static boolean IGNORE_WHITESPACE() {
        return GreenplumCSVFormat$.MODULE$.IGNORE_WHITESPACE();
    }
    
    public static boolean STRICT_QUOTES() {
        return GreenplumCSVFormat$.MODULE$.STRICT_QUOTES();
    }
    
    public static String DEFAULT_ENCODING() {
        return GreenplumCSVFormat$.MODULE$.DEFAULT_ENCODING();
    }
    
    public static Null$ NULL_VALUE() {
        return GreenplumCSVFormat$.MODULE$.NULL_VALUE();
    }
    
    public static String EMPTY_VALUE() {
        return GreenplumCSVFormat$.MODULE$.EMPTY_VALUE();
    }
    
    public static String VALUE_OF_NULL() {
        return GreenplumCSVFormat$.MODULE$.VALUE_OF_NULL();
    }
    
    public static String QUOTE_STRING() {
        return GreenplumCSVFormat$.MODULE$.QUOTE_STRING();
    }
    
    public static char QUOTE() {
        return GreenplumCSVFormat$.MODULE$.QUOTE();
    }
    
    public static String NEWLINE() {
        return GreenplumCSVFormat$.MODULE$.NEWLINE();
    }
    
    public static char CHAR_DELIMITER() {
        return GreenplumCSVFormat$.MODULE$.CHAR_DELIMITER();
    }
    
    public static char ESCAPE() {
        return GreenplumCSVFormat$.MODULE$.ESCAPE();
    }
}
