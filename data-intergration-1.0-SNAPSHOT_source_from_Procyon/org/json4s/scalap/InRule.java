// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.scalap;

import scala.runtime.Nothing$;
import scala.runtime.BoxedUnit;
import scala.Function1;
import scala.reflect.ScalaSignature;

@ScalaSignature(bytes = "\u0006\u0001M3A!\u0001\u0002\u0001\u0013\t1\u0011J\u001c*vY\u0016T!a\u0001\u0003\u0002\rM\u001c\u0017\r\\1q\u0015\t)a!\u0001\u0004kg>tGg\u001d\u0006\u0002\u000f\u0005\u0019qN]4\u0004\u0001U)!\"G\u0012'SM\u0011\u0001a\u0003\t\u0003\u0019=i\u0011!\u0004\u0006\u0002\u001d\u0005)1oY1mC&\u0011\u0001#\u0004\u0002\u0007\u0003:L(+\u001a4\t\u0011I\u0001!\u0011!Q\u0001\nM\tAA];mKB1A#F\f#K!j\u0011AA\u0005\u0003-\t\u0011AAU;mKB\u0011\u0001$\u0007\u0007\u0001\t\u0015Q\u0002A1\u0001\u001c\u0005\tIe.\u0005\u0002\u001d?A\u0011A\"H\u0005\u0003=5\u0011qAT8uQ&tw\r\u0005\u0002\rA%\u0011\u0011%\u0004\u0002\u0004\u0003:L\bC\u0001\r$\t\u0019!\u0003\u0001\"b\u00017\t\u0019q*\u001e;\u0011\u0005a1CAB\u0014\u0001\t\u000b\u00071DA\u0001B!\tA\u0012\u0006\u0002\u0004+\u0001\u0011\u0015\ra\u0007\u0002\u00021\")A\u0006\u0001C\u0001[\u00051A(\u001b8jiz\"\"AL\u0018\u0011\rQ\u0001qCI\u0013)\u0011\u0015\u00112\u00061\u0001\u0014\u0011\u0015\t\u0004\u0001\"\u00013\u0003\u001di\u0017\r\u001d*vY\u0016,Ba\r\u001c:yQ\u0011AG\u0010\t\u0007)U9R\u0007O\u001e\u0011\u0005a1D!B\u001c1\u0005\u0004Y\"\u0001B(viJ\u0002\"\u0001G\u001d\u0005\u000bi\u0002$\u0019A\u000e\u0003\u0003\t\u0003\"\u0001\u0007\u001f\u0005\u000bu\u0002$\u0019A\u000e\u0003\u0003eCQa\u0010\u0019A\u0002\u0001\u000b\u0011A\u001a\t\u0005\u0019\u0005\u001be)\u0003\u0002C\u001b\tIa)\u001e8di&|g.\r\t\u0006)\u0011\u0013S\u0005K\u0005\u0003\u000b\n\u0011aAU3tk2$\b\u0003\u0002\u0007B/\u001d\u0003R\u0001\u0006#6qmBQ!\u0013\u0001\u0005\u0002)\u000b1\"\u001e8bef|FEY1oOV\t1\n\u0005\u0004\u0015+]9B\n\b\t\u0003\u00195K!AT\u0007\u0003\tUs\u0017\u000e\u001e\u0005\u0006!\u0002!\t!U\u0001\u0005I\u0005l\u0007/F\u0001S!\u0019!RcF\f&Q\u0001")
public class InRule<In, Out, A, X>
{
    public final Rule<In, Out, A, X> org$json4s$scalap$InRule$$rule;
    
    public <Out2, B, Y> Rule<In, Out2, B, Y> mapRule(final Function1<Result<Out, A, X>, Function1<In, Result<Out2, B, Y>>> f) {
        return this.org$json4s$scalap$InRule$$rule.factory().rule((scala.Function1<In, Result<Out2, B, Y>>)new InRule$$anonfun$mapRule.InRule$$anonfun$mapRule$1(this, (Function1)f));
    }
    
    public Rule<In, In, BoxedUnit, Nothing$> unary_$bang() {
        return this.mapRule((scala.Function1<Result<Out, A, X>, scala.Function1<In, Result<In, BoxedUnit, Nothing$>>>)new InRule$$anonfun$unary_$bang.InRule$$anonfun$unary_$bang$1(this));
    }
    
    public Rule<In, In, A, X> $amp() {
        return this.mapRule((scala.Function1<Result<Out, A, X>, scala.Function1<In, Result<In, A, X>>>)new InRule$$anonfun$$amp.InRule$$anonfun$$amp$1(this));
    }
    
    public InRule(final Rule<In, Out, A, X> rule) {
        this.org$json4s$scalap$InRule$$rule = rule;
    }
}
