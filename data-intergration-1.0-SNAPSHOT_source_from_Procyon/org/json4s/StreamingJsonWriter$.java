// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

import scala.runtime.RichDouble$;
import scala.runtime.BoxesRunTime;
import scala.Predef$;
import scala.runtime.RichFloat$;

public final class StreamingJsonWriter$
{
    public static final StreamingJsonWriter$ MODULE$;
    private final String posInfinityVal;
    private final String negInfiniteVal;
    
    static {
        new StreamingJsonWriter$();
    }
    
    public String handleInfinity(final float value) {
        return RichFloat$.MODULE$.isPosInfinity$extension(Predef$.MODULE$.floatWrapper(value)) ? this.posInfinityVal : (RichFloat$.MODULE$.isNegInfinity$extension(Predef$.MODULE$.floatWrapper(value)) ? this.negInfiniteVal : BoxesRunTime.boxToFloat(value).toString());
    }
    
    public String handleInfinity(final double value) {
        return RichDouble$.MODULE$.isPosInfinity$extension(Predef$.MODULE$.doubleWrapper(value)) ? this.posInfinityVal : (RichDouble$.MODULE$.isNegInfinity$extension(Predef$.MODULE$.doubleWrapper(value)) ? this.negInfiniteVal : BoxesRunTime.boxToDouble(value).toString());
    }
    
    private StreamingJsonWriter$() {
        MODULE$ = this;
        this.posInfinityVal = "1e+500";
        this.negInfiniteVal = "-1e+500";
    }
}
