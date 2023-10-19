// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.scalap;

import scala.Function0;
import scala.runtime.Nothing$;
import scala.Tuple2;
import scala.Function2;
import scala.Function1;
import scala.Option;
import scala.reflect.ScalaSignature;

@ScalaSignature(bytes = "\u0006\u0001y4Q!\u0001\u0002\u0002\"%\u0011aAU3tk2$(BA\u0002\u0005\u0003\u0019\u00198-\u00197ba*\u0011QAB\u0001\u0007UN|g\u000eN:\u000b\u0003\u001d\t1a\u001c:h\u0007\u0001)BA\u0003\r#KM\u0011\u0001a\u0003\t\u0003\u0019=i\u0011!\u0004\u0006\u0002\u001d\u0005)1oY1mC&\u0011\u0001#\u0004\u0002\u0007\u0003:L(+\u001a4\t\u000bI\u0001A\u0011A\n\u0002\rqJg.\u001b;?)\u0005!\u0002#B\u000b\u0001-\u0005\"S\"\u0001\u0002\u0011\u0005]AB\u0002\u0001\u0003\u00073\u0001!)\u0019\u0001\u000e\u0003\u0007=+H/\u0005\u0002\u001c=A\u0011A\u0002H\u0005\u0003;5\u0011qAT8uQ&tw\r\u0005\u0002\r?%\u0011\u0001%\u0004\u0002\u0004\u0003:L\bCA\f#\t\u0019\u0019\u0003\u0001\"b\u00015\t\t\u0011\t\u0005\u0002\u0018K\u00111a\u0005\u0001CC\u0002i\u0011\u0011\u0001\u0017\u0005\u0006Q\u00011\t!K\u0001\u0004_V$X#\u0001\f\t\u000b-\u0002a\u0011\u0001\u0017\u0002\u000bY\fG.^3\u0016\u0003\u0005BQA\f\u0001\u0007\u0002=\nQ!\u001a:s_J,\u0012\u0001\n\u0005\u0006c\u00011\u0019AM\u0001\ti>|\u0005\u000f^5p]V\t1\u0007E\u0002\ri\u0005J!!N\u0007\u0003\r=\u0003H/[8o\u0011\u00159\u0004A\"\u00019\u0003\ri\u0017\r]\u000b\u0003sq\"\"A\u000f \u0011\u000bU\u0001ac\u000f\u0013\u0011\u0005]aD!B\u001f7\u0005\u0004Q\"!\u0001\"\t\u000b}2\u0004\u0019\u0001!\u0002\u0003\u0019\u0004B\u0001D!\"w%\u0011!)\u0004\u0002\n\rVt7\r^5p]FBQ\u0001\u0012\u0001\u0007\u0002\u0015\u000ba!\\1q\u001fV$XC\u0001$J)\t95\nE\u0003\u0016\u0001!\u000bC\u0005\u0005\u0002\u0018\u0013\u0012)!j\u0011b\u00015\t!q*\u001e;3\u0011\u0015y4\t1\u0001M!\u0011a\u0011I\u0006%\t\u000b]\u0002a\u0011\u0001(\u0016\u0007=\u0013F\u000b\u0006\u0002Q+B)Q\u0003A)TIA\u0011qC\u0015\u0003\u0006\u00156\u0013\rA\u0007\t\u0003/Q#Q!P'C\u0002iAQaP'A\u0002Y\u0003R\u0001D,\u0017CeK!\u0001W\u0007\u0003\u0013\u0019+hn\u0019;j_:\u0014\u0004\u0003\u0002\u0007[#NK!aW\u0007\u0003\rQ+\b\u000f\\33\u0011\u0015i\u0006A\"\u0001_\u0003\u001d1G.\u0019;NCB,2a\u00182e)\t\u0001W\rE\u0003\u0016\u0001\u0005\u001cG\u0005\u0005\u0002\u0018E\u0012)!\n\u0018b\u00015A\u0011q\u0003\u001a\u0003\u0006{q\u0013\rA\u0007\u0005\u0006\u007fq\u0003\rA\u001a\t\u0006\u0019]3\u0012e\u001a\t\u0006+\u0001\t7m\u0007\u0005\u0006S\u00021\tA[\u0001\u0007_J,En]3\u0016\u0007-t\u0017\u000f\u0006\u0002mgB)Q\u0003A7qIA\u0011qC\u001c\u0003\u0006\u0015\"\u0014\ra\\\t\u0003-y\u0001\"aF9\u0005\u000buB'\u0019\u0001:\u0012\u0005\u0005r\u0002B\u0002;i\t\u0003\u0007Q/A\u0003pi\",'\u000fE\u0002\rmbL!a^\u0007\u0003\u0011q\u0012\u0017P\\1nKz\u0002R!\u0006\u0001nanI3\u0001\u0001>}\u0013\tY(AA\u0005O_N+8mY3tg&\u0011QP\u0001\u0002\b'V\u001c7-Z:t\u0001")
public abstract class Result<Out, A, X>
{
    public abstract Out out();
    
    public abstract A value();
    
    public abstract X error();
    
    public abstract Option<A> toOption();
    
    public abstract <B> Result<Out, B, X> map(final Function1<A, B> p0);
    
    public abstract <Out2> Result<Out2, A, X> mapOut(final Function1<Out, Out2> p0);
    
    public abstract <Out2, B> Result<Out2, B, X> map(final Function2<Out, A, Tuple2<Out2, B>> p0);
    
    public abstract <Out2, B> Result<Out2, B, X> flatMap(final Function2<Out, A, Result<Out2, B, Nothing$>> p0);
    
    public abstract <Out2, B> Result<Out2, B, X> orElse(final Function0<Result<Out2, B, Nothing$>> p0);
}
