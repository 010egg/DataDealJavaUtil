// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

import scala.math.BigDecimal;
import scala.Function1;
import scala.reflect.ScalaSignature;

@ScalaSignature(bytes = "\u0006\u0001E3q!\u0001\u0002\u0011\u0002\u0007\u0005qAA\tCS\u001e$UmY5nC2<&/\u001b;feNT!a\u0001\u0003\u0002\r)\u001cxN\u001c\u001bt\u0015\u0005)\u0011aA8sO\u000e\u00011c\u0001\u0001\t\u001dA\u0011\u0011\u0002D\u0007\u0002\u0015)\t1\"A\u0003tG\u0006d\u0017-\u0003\u0002\u000e\u0015\t1\u0011I\\=SK\u001a\u0004\"a\u0004\t\u000e\u0003\tI!!\u0005\u0002\u0003\u001d\u0011+g-Y;mi^\u0013\u0018\u000e^3sg\")1\u0003\u0001C\u0001)\u00051A%\u001b8ji\u0012\"\u0012!\u0006\t\u0003\u0013YI!a\u0006\u0006\u0003\tUs\u0017\u000e^\u0004\u00063\u0001A\u0019AG\u0001\f\r2|\u0017\r^,sSR,'\u000f\u0005\u0002\u001c95\t\u0001AB\u0003\u001e\u0001!\u0005aDA\u0006GY>\fGo\u0016:ji\u0016\u00148C\u0001\u000f !\rY\u0002EI\u0005\u0003CA\u0011\u0011a\u0016\t\u0003\u0013\rJ!\u0001\n\u0006\u0003\u000b\u0019cw.\u0019;\t\u000b\u0019bB\u0011A\u0014\u0002\rqJg.\u001b;?)\u0005Qr!B\u0015\u0001\u0011\u0007Q\u0013\u0001\u0004#pk\ndWm\u0016:ji\u0016\u0014\bCA\u000e,\r\u0015a\u0003\u0001#\u0001.\u00051!u.\u001e2mK^\u0013\u0018\u000e^3s'\tYc\u0006E\u0002\u001cA=\u0002\"!\u0003\u0019\n\u0005ER!A\u0002#pk\ndW\rC\u0003'W\u0011\u00051\u0007F\u0001+\u000f\u0015)\u0004\u0001c\u00017\u0003A\u0011\u0015n\u001a#fG&l\u0017\r\\,sSR,'\u000f\u0005\u0002\u001co\u0019)\u0001\b\u0001E\u0001s\t\u0001\")[4EK\u000eLW.\u00197Xe&$XM]\n\u0003oi\u00022a\u0007\u0011<!\taDI\u0004\u0002>\u0005:\u0011a(Q\u0007\u0002\u007f)\u0011\u0001IB\u0001\u0007yI|w\u000e\u001e \n\u0003-I!a\u0011\u0006\u0002\u000fA\f7m[1hK&\u0011QI\u0012\u0002\u000b\u0005&<G)Z2j[\u0006d'BA\"\u000b\u0011\u00151s\u0007\"\u0001I)\u00051t!\u0002&\u0003\u0011\u0003Y\u0015!\u0005\"jO\u0012+7-[7bY^\u0013\u0018\u000e^3sgB\u0011q\u0002\u0014\u0004\u0006\u0003\tA\t!T\n\u0004\u0019\"q\u0005CA\b\u0001\u0011\u00151C\n\"\u0001Q)\u0005Y\u0005")
public interface BigDecimalWriters extends DefaultWriters
{
    FloatWriter$ FloatWriter();
    
    DoubleWriter$ DoubleWriter();
    
    BigDecimalWriter$ BigDecimalWriter();
    
    public class FloatWriter$ extends W<Object>
    {
        public FloatWriter$(final BigDecimalWriters $outer) {
            $outer.super((Function1)new BigDecimalWriters$FloatWriter$$anonfun$$lessinit$greater.BigDecimalWriters$FloatWriter$$anonfun$$lessinit$greater$12($outer));
        }
    }
    
    public class DoubleWriter$ extends W<Object>
    {
        public DoubleWriter$(final BigDecimalWriters $outer) {
            $outer.super((Function1)new BigDecimalWriters$DoubleWriter$$anonfun$$lessinit$greater.BigDecimalWriters$DoubleWriter$$anonfun$$lessinit$greater$13($outer));
        }
    }
    
    public class BigDecimalWriter$ extends W<BigDecimal>
    {
        public BigDecimalWriter$(final BigDecimalWriters $outer) {
            $outer.super((Function1)new BigDecimalWriters$BigDecimalWriter$$anonfun$$lessinit$greater.BigDecimalWriters$BigDecimalWriter$$anonfun$$lessinit$greater$14($outer));
        }
    }
}
