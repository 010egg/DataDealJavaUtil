// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

import scala.math.BigDecimal;
import scala.Function1;
import scala.reflect.ScalaSignature;

@ScalaSignature(bytes = "\u0006\u0001E3q!\u0001\u0002\u0011\u0002\u0007\u0005qAA\u0007E_V\u0014G.Z,sSR,'o\u001d\u0006\u0003\u0007\u0011\taA[:p]R\u001a(\"A\u0003\u0002\u0007=\u0014xm\u0001\u0001\u0014\u0007\u0001Aa\u0002\u0005\u0002\n\u00195\t!BC\u0001\f\u0003\u0015\u00198-\u00197b\u0013\ti!B\u0001\u0004B]f\u0014VM\u001a\t\u0003\u001fAi\u0011AA\u0005\u0003#\t\u0011a\u0002R3gCVdGo\u0016:ji\u0016\u00148\u000fC\u0003\u0014\u0001\u0011\u0005A#\u0001\u0004%S:LG\u000f\n\u000b\u0002+A\u0011\u0011BF\u0005\u0003/)\u0011A!\u00168ji\u001e)\u0011\u0004\u0001E\u00025\u0005Ya\t\\8bi^\u0013\u0018\u000e^3s!\tYB$D\u0001\u0001\r\u0015i\u0002\u0001#\u0001\u001f\u0005-1En\\1u/JLG/\u001a:\u0014\u0005qy\u0002cA\u000e!E%\u0011\u0011\u0005\u0005\u0002\u0002/B\u0011\u0011bI\u0005\u0003I)\u0011QA\u00127pCRDQA\n\u000f\u0005\u0002\u001d\na\u0001P5oSRtD#\u0001\u000e\b\u000b%\u0002\u00012\u0001\u0016\u0002\u0019\u0011{WO\u00197f/JLG/\u001a:\u0011\u0005mYc!\u0002\u0017\u0001\u0011\u0003i#\u0001\u0004#pk\ndWm\u0016:ji\u0016\u00148CA\u0016/!\rY\u0002e\f\t\u0003\u0013AJ!!\r\u0006\u0003\r\u0011{WO\u00197f\u0011\u001513\u0006\"\u00014)\u0005Qs!B\u001b\u0001\u0011\u00071\u0014\u0001\u0005\"jO\u0012+7-[7bY^\u0013\u0018\u000e^3s!\tYrGB\u00039\u0001!\u0005\u0011H\u0001\tCS\u001e$UmY5nC2<&/\u001b;feN\u0011qG\u000f\t\u00047\u0001Z\u0004C\u0001\u001fE\u001d\ti$I\u0004\u0002?\u00036\tqH\u0003\u0002A\r\u00051AH]8pizJ\u0011aC\u0005\u0003\u0007*\tq\u0001]1dW\u0006<W-\u0003\u0002F\r\nQ!)[4EK\u000eLW.\u00197\u000b\u0005\rS\u0001\"\u0002\u00148\t\u0003AE#\u0001\u001c\b\u000b)\u0013\u0001\u0012A&\u0002\u001b\u0011{WO\u00197f/JLG/\u001a:t!\tyAJB\u0003\u0002\u0005!\u0005QjE\u0002M\u00119\u0003\"a\u0004\u0001\t\u000b\u0019bE\u0011\u0001)\u0015\u0003-\u0003")
public interface DoubleWriters extends DefaultWriters
{
    FloatWriter$ FloatWriter();
    
    DoubleWriter$ DoubleWriter();
    
    BigDecimalWriter$ BigDecimalWriter();
    
    public class FloatWriter$ extends W<Object>
    {
        public FloatWriter$(final DoubleWriters $outer) {
            $outer.super((Function1)new DoubleWriters$FloatWriter$$anonfun$$lessinit$greater.DoubleWriters$FloatWriter$$anonfun$$lessinit$greater$9($outer));
        }
    }
    
    public class DoubleWriter$ extends W<Object>
    {
        public DoubleWriter$(final DoubleWriters $outer) {
            $outer.super((Function1)new DoubleWriters$DoubleWriter$$anonfun$$lessinit$greater.DoubleWriters$DoubleWriter$$anonfun$$lessinit$greater$10($outer));
        }
    }
    
    public class BigDecimalWriter$ extends W<BigDecimal>
    {
        public BigDecimalWriter$(final DoubleWriters $outer) {
            $outer.super((Function1)new DoubleWriters$BigDecimalWriter$$anonfun$$lessinit$greater.DoubleWriters$BigDecimalWriter$$anonfun$$lessinit$greater$11($outer));
        }
    }
}
