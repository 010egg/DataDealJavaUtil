// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.scalap;

import scala.Option;
import scala.Function0;
import scala.Tuple2;
import scala.Function2;
import scala.Function1;
import scala.None$;
import scala.reflect.ScalaSignature;
import scala.runtime.Nothing$;

@ScalaSignature(bytes = "\u0006\u000154Q!\u0001\u0002\u0002\"%\u0011\u0011BT8Tk\u000e\u001cWm]:\u000b\u0005\r!\u0011AB:dC2\f\u0007O\u0003\u0002\u0006\r\u00051!n]8oiMT\u0011aB\u0001\u0004_J<7\u0001A\u000b\u0003\u0015]\u0019\"\u0001A\u0006\u0011\u000b1iqbD\u000b\u000e\u0003\tI!A\u0004\u0002\u0003\rI+7/\u001e7u!\t\u00012#D\u0001\u0012\u0015\u0005\u0011\u0012!B:dC2\f\u0017B\u0001\u000b\u0012\u0005\u001dqu\u000e\u001e5j]\u001e\u0004\"AF\f\r\u0001\u00111\u0001\u0004\u0001CC\u0002e\u0011\u0011\u0001W\t\u0003\u001fi\u0001\"\u0001E\u000e\n\u0005q\t\"aA!os\")a\u0004\u0001C\u0001?\u00051A(\u001b8jiz\"\u0012\u0001\t\t\u0004\u0019\u0001)\u0002\"\u0002\u0012\u0001\t\u0003\u0019\u0013aA8viV\tq\u0002C\u0003&\u0001\u0011\u00051%A\u0003wC2,X\rC\u0003(\u0001\u0011\u0005\u0001&\u0001\u0005u_>\u0003H/[8o+\u0005IcB\u0001\t+\u0013\tY\u0013#\u0001\u0003O_:,\u0007\"B\u0017\u0001\t\u0003q\u0013aA7baV\u0011qF\u000e\u000b\u0003AABQ!\r\u0017A\u0002I\n\u0011A\u001a\t\u0005!MzQ'\u0003\u00025#\tIa)\u001e8di&|g.\r\t\u0003-Y\"Qa\u000e\u0017C\u0002e\u0011\u0011A\u0011\u0005\u0006s\u0001!\tAO\u0001\u0007[\u0006\u0004x*\u001e;\u0016\u0005mzDC\u0001\u0011=\u0011\u0015\t\u0004\b1\u0001>!\u0011\u00012g\u0004 \u0011\u0005YyD!\u0002!9\u0005\u0004I\"\u0001B(viJBQ!\f\u0001\u0005\u0002\t+2a\u0011'O)\t\u0001C\tC\u00032\u0003\u0002\u0007Q\tE\u0003\u0011\r>y\u0001*\u0003\u0002H#\tIa)\u001e8di&|gN\r\t\u0005!%[U*\u0003\u0002K#\t1A+\u001e9mKJ\u0002\"A\u0006'\u0005\u000b\u0001\u000b%\u0019A\r\u0011\u0005YqE!B\u001cB\u0005\u0004I\u0002\"\u0002)\u0001\t\u0003\t\u0016a\u00024mCRl\u0015\r]\u000b\u0004%^KFC\u0001\u0011T\u0011\u0015\tt\n1\u0001U!\u0015\u0001biD\bV!\u0015aQB\u0016-\u0010!\t1r\u000bB\u0003A\u001f\n\u0007\u0011\u0004\u0005\u0002\u00173\u0012)qg\u0014b\u00013!)1\f\u0001C\u00019\u00061qN]#mg\u0016,2!\u00181c)\tq6\rE\u0003\r\u001b}\u000bw\u0002\u0005\u0002\u0017A\u0012)\u0001I\u0017b\u00013A\u0011aC\u0019\u0003\u0006oi\u0013\r!\u0007\u0005\u0007Ij#\t\u0019A3\u0002\u000b=$\b.\u001a:\u0011\u0007A1g,\u0003\u0002h#\tAAHY=oC6,g(K\u0002\u0001S.L!A\u001b\u0002\u0003\u000b\u0015\u0013(o\u001c:\u000b\u00051\u0014\u0011a\u0002$bS2,(/\u001a")
public abstract class NoSuccess<X> extends Result<Nothing$, Nothing$, X>
{
    @Override
    public Nothing$ out() {
        throw new ScalaSigParserError("No output");
    }
    
    @Override
    public Nothing$ value() {
        throw new ScalaSigParserError("No value");
    }
    
    public None$ toOption() {
        return None$.MODULE$;
    }
    
    @Override
    public <B> NoSuccess<X> map(final Function1<Nothing$, B> f) {
        return this;
    }
    
    @Override
    public <Out2> NoSuccess<X> mapOut(final Function1<Nothing$, Out2> f) {
        return this;
    }
    
    @Override
    public <Out2, B> NoSuccess<X> map(final Function2<Nothing$, Nothing$, Tuple2<Out2, B>> f) {
        return this;
    }
    
    @Override
    public <Out2, B> NoSuccess<X> flatMap(final Function2<Nothing$, Nothing$, Result<Out2, B, Nothing$>> f) {
        return this;
    }
    
    @Override
    public <Out2, B> Result<Out2, B, Nothing$> orElse(final Function0<Result<Out2, B, Nothing$>> other) {
        return (Result<Out2, B, Nothing$>)other.apply();
    }
}
