// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.scalap;

import scala.runtime.Nothing$;
import scala.MatchError;
import scala.Function2;
import scala.collection.Seq;
import scala.Function0;
import scala.collection.immutable.List;
import scala.Function1;
import scala.Option;
import scala.reflect.ScalaSignature;

@ScalaSignature(bytes = "\u0006\u0001\u0005%e\u0001B\u0001\u0003\u0001%\u0011qaU3r%VdWM\u0003\u0002\u0004\t\u000511oY1mCBT!!\u0002\u0004\u0002\r)\u001cxN\u001c\u001bt\u0015\u00059\u0011aA8sO\u000e\u0001Q\u0003\u0002\u0006\u001aG\u0019\u001a\"\u0001A\u0006\u0011\u00051yQ\"A\u0007\u000b\u00039\tQa]2bY\u0006L!\u0001E\u0007\u0003\r\u0005s\u0017PU3g\u0011!\u0011\u0002A!A!\u0002\u0013\u0019\u0012\u0001\u0002:vY\u0016\u0004b\u0001F\u000b\u0018/\t*S\"\u0001\u0002\n\u0005Y\u0011!\u0001\u0002*vY\u0016\u0004\"\u0001G\r\r\u0001\u0011)!\u0004\u0001b\u00017\t\t1+\u0005\u0002\u001d?A\u0011A\"H\u0005\u0003=5\u0011qAT8uQ&tw\r\u0005\u0002\rA%\u0011\u0011%\u0004\u0002\u0004\u0003:L\bC\u0001\r$\t\u0019!\u0003\u0001\"b\u00017\t\t\u0011\t\u0005\u0002\u0019M\u00111q\u0005\u0001CC\u0002m\u0011\u0011\u0001\u0017\u0005\u0006S\u0001!\tAK\u0001\u0007y%t\u0017\u000e\u001e \u0015\u0005-b\u0003#\u0002\u000b\u0001/\t*\u0003\"\u0002\n)\u0001\u0004\u0019\u0002\"\u0002\u0018\u0001\t\u0003y\u0013A\u0002\u0013r[\u0006\u00148.F\u00011!\u0019!RcF\f2KA\u0019AB\r\u0012\n\u0005Mj!AB(qi&|g\u000eC\u00036\u0001\u0011\u0005a'\u0001\u0007%[&tWo\u001d\u0013r[\u0006\u00148.F\u00018!\u0019!RcF\f9KA\u0011A\"O\u0005\u0003u5\u0011qAQ8pY\u0016\fg\u000eC\u0003=\u0001\u0011\u0005Q(\u0001\u0004%i&lWm]\u000b\u0002}A1A#F\f\u0018\u007f\u0015\u00022\u0001\u0011%#\u001d\t\teI\u0004\u0002C\u000b6\t1I\u0003\u0002E\u0011\u00051AH]8pizJ\u0011AD\u0005\u0003\u000f6\tq\u0001]1dW\u0006<W-\u0003\u0002J\u0015\n!A*[:u\u0015\t9U\u0002C\u0003M\u0001\u0011\u0005Q*A\u0003%a2,8/F\u0001O!\u0019!RcF\fPKA\u0019\u0001+\u0016\u0012\u000e\u0003ES!AU*\u0002\u0013%lW.\u001e;bE2,'B\u0001+\u000e\u0003)\u0019w\u000e\u001c7fGRLwN\\\u0005\u0003\u0013FCQa\u0016\u0001\u0005\u0002a\u000bA\u0003\n;jY\u0012,Ge\u001a:fCR,'\u000fJ9nCJ\\WcA-]AR\u0011!l\u0019\t\u0007)U9rcW0\u0011\u0005aaF!B/W\u0005\u0004q&!\u0001\"\u0012\u0005\tz\u0002C\u0001\ra\t\u0015\tgK1\u0001c\u0005\tA&'\u0005\u0002&?!1AM\u0016CA\u0002\u0015\f\u0011A\u001a\t\u0004\u0019\u0019D\u0017BA4\u000e\u0005!a$-\u001f8b[\u0016t\u0004C\u0002\u000b\u0016/]Iw\f\u0005\u0003\rUn[\u0016BA6\u000e\u0005%1UO\\2uS>t\u0017\u0007C\u0003n\u0001\u0011\u0005a.\u0001\u000b%i&dG-\u001a\u0013he\u0016\fG/\u001a:%i&lWm]\u000b\u0004_J$HC\u00019v!\u0019!RcF\frgB\u0011\u0001D\u001d\u0003\u0006;2\u0014\rA\u0018\t\u00031Q$Q!\u00197C\u0002\tDa\u0001\u001a7\u0005\u0002\u00041\bc\u0001\u0007goB1A#F\f\u0018qN\u0004B\u0001\u00046rc\")!\u0010\u0001C\u0001w\u0006\u0011B\u0005^5mI\u0016$C/[7fg\u0012\"\u0018\u000e\u001c3f+\u0011ax0a\u0001\u0015\u0007u\f)\u0001E\u0004\u0015+]9b0!\u0001\u0011\u0005ayH!B/z\u0005\u0004q\u0006c\u0001\r\u0002\u0004\u0011)\u0011-\u001fb\u0001E\"A\u0011qA=\u0005\u0002\u0004\tI!\u0001\u0003k_&t\u0007\u0003\u0002\u0007g\u0003\u0017\u0001\u0002\u0002F\u000b\u0018/\u00055\u0011\u0011\u0001\t\u0007\u0019\u0005=aP @\n\u0007\u0005EQBA\u0005Gk:\u001cG/[8oe!9\u0011Q\u0003\u0001\u0005\u0002\u0005]\u0011!\u0003\u0013qYV\u001cH\u0005Z5w+\u0011\tI\"a\b\u0015\t\u0005m\u0011\u0011\u0005\t\b)U9rcTA\u000f!\rA\u0012q\u0004\u0003\u0007C\u0006M!\u0019\u00012\t\u0013\u0005\r\u00121\u0003CA\u0002\u0005\u0015\u0012aA:faB!ABZA\u0014!\u001d!RcF\f \u0003;Aq!a\u000b\u0001\t\u0003\ti#\u0001\u0006%i&lWm\u001d\u0013eSZ,B!a\f\u00026Q!\u0011\u0011GA\u001c!\u001d!RcF\fP\u0003g\u00012\u0001GA\u001b\t\u0019\t\u0017\u0011\u0006b\u0001E\"I\u00111EA\u0015\t\u0003\u0007\u0011\u0011\b\t\u0005\u0019\u0019\fY\u0004E\u0004\u0015+]9r$a\r\t\u000f\u0005}\u0002\u0001\"\u0001\u0002B\u0005\u0011B\u0005^5nKN$C/\u001b7eK\u0012j\u0017N\\;t+\u0019\t\u0019%!\u0013\u0002PQ!\u0011QIA)!!!RcFA$\u007f\u00055\u0003c\u0001\r\u0002J\u00119\u00111JA\u001f\u0005\u0004Y\"aA(viB\u0019\u0001$a\u0014\u0005\r\u0005\fiD1\u0001c\u0011%\t\u0019&!\u0010\u0005\u0002\u0004\t)&A\u0002f]\u0012\u0004B\u0001\u00044\u0002XAAA#F\f\u0002H}\ti\u0005C\u0004\u0002\\\u0001!\t!!\u0018\u0002#\u0011\u0002H.^:%i&dG-\u001a\u0013nS:,8/\u0006\u0004\u0002`\u0005\u0015\u0014\u0011\u000e\u000b\u0005\u0003C\nY\u0007\u0005\u0005\u0015+]\t\u0019gTA4!\rA\u0012Q\r\u0003\b\u0003\u0017\nIF1\u0001\u001c!\rA\u0012\u0011\u000e\u0003\u0007C\u0006e#\u0019\u00012\t\u0013\u0005M\u0013\u0011\fCA\u0002\u00055\u0004\u0003\u0002\u0007g\u0003_\u0002\u0002\u0002F\u000b\u0018\u0003Gz\u0012q\r\u0005\b\u0003g\u0002A\u0011AA;\u0003\u0015!\u0018.\\3t)\u0011\t9(a \u0011\u000fQ)rcFA=KA!\u0001)a\u001f#\u0013\r\tiH\u0013\u0002\u0004'\u0016\f\b\u0002CAA\u0003c\u0002\r!a!\u0002\u00079,X\u000eE\u0002\r\u0003\u000bK1!a\"\u000e\u0005\rIe\u000e\u001e")
public class SeqRule<S, A, X>
{
    public final Rule<S, S, A, X> org$json4s$scalap$SeqRule$$rule;
    
    public Rule<S, S, Option<A>, X> $qmark() {
        return this.org$json4s$scalap$SeqRule$$rule.factory().inRule(this.org$json4s$scalap$SeqRule$$rule).mapRule((scala.Function1<Result<S, A, X>, scala.Function1<S, Result<S, Option<A>, X>>>)new SeqRule$$anonfun$$qmark.SeqRule$$anonfun$$qmark$1(this));
    }
    
    public Rule<S, S, Object, X> $minus$qmark() {
        return this.$qmark().map((scala.Function1<Option<A>, Object>)new SeqRule$$anonfun$$minus$qmark.SeqRule$$anonfun$$minus$qmark$1(this));
    }
    
    public Rule<S, S, List<A>, X> $times() {
        return this.org$json4s$scalap$SeqRule$$rule.factory().from().apply((scala.Function1<S, Result<S, List<A>, X>>)new SeqRule$$anonfun$$times.SeqRule$$anonfun$$times$1(this));
    }
    
    public Rule<S, S, List<A>, X> $plus() {
        return this.org$json4s$scalap$SeqRule$$rule.$tilde$plus$plus((scala.Function0<Rule<S, S, scala.collection.Seq<A>, X>>)new SeqRule$$anonfun$$plus.SeqRule$$anonfun$$plus$1(this));
    }
    
    public <B, X2> Rule<S, S, B, X2> $tilde$greater$qmark(final Function0<Rule<S, S, Function1<B, B>, X2>> f) {
        return this.org$json4s$scalap$SeqRule$$rule.flatMap((scala.Function1<A, scala.Function1<S, Result<S, B, X2>>>)new SeqRule$$anonfun$$tilde$greater$qmark.SeqRule$$anonfun$$tilde$greater$qmark$1(this, (Function0)f));
    }
    
    public <B, X2> Rule<S, S, B, X2> $tilde$greater$times(final Function0<Rule<S, S, Function1<B, B>, X2>> f) {
        return this.org$json4s$scalap$SeqRule$$rule.flatMap((scala.Function1<A, scala.Function1<S, Result<S, B, X2>>>)new SeqRule$$anonfun$$tilde$greater$times.SeqRule$$anonfun$$tilde$greater$times$1(this, (Function0)f));
    }
    
    public <B, X2> Rule<S, S, B, X2> $tilde$times$tilde(final Function0<Rule<S, S, Function2<B, B, B>, X2>> join) {
        return (Rule<S, S, B, X2>)this.$tilde$greater$times((scala.Function0<Rule<S, S, scala.Function1<Object, Object>, Object>>)new SeqRule$$anonfun$$tilde$times$tilde.SeqRule$$anonfun$$tilde$times$tilde$1(this, (Function0)join));
    }
    
    public <X2> Rule<S, S, List<A>, X2> $plus$div(final Function0<Rule<S, S, Object, X2>> sep) {
        return this.org$json4s$scalap$SeqRule$$rule.$tilde$plus$plus((scala.Function0<Rule<S, S, scala.collection.Seq<A>, X2>>)new SeqRule$$anonfun$$plus$div.SeqRule$$anonfun$$plus$div$1(this, (Function0)sep));
    }
    
    public <X2> Rule<S, S, List<A>, X2> $times$div(final Function0<Rule<S, S, Object, X2>> sep) {
        return this.$plus$div((scala.Function0<Rule<S, S, Object, Object>>)sep).$bar((scala.Function0<Rule<S, S, List<A>, X2>>)new SeqRule$$anonfun$$times$div.SeqRule$$anonfun$$times$div$1(this));
    }
    
    public <Out, X2> Rule<S, Out, List<A>, X2> $times$tilde$minus(final Function0<Rule<S, Out, Object, X2>> end) {
        return this.org$json4s$scalap$SeqRule$$rule.factory().seqRule((Rule<S, S, A, X>)this.org$json4s$scalap$SeqRule$$rule.$minus((scala.Function0<Rule<In, Object, Object, Object>>)end)).$times().$tilde$minus(end);
    }
    
    public <Out, X2> Rule<S, Out, List<A>, X2> $plus$tilde$minus(final Function0<Rule<S, Out, Object, X2>> end) {
        return this.org$json4s$scalap$SeqRule$$rule.factory().seqRule((Rule<S, S, A, X>)this.org$json4s$scalap$SeqRule$$rule.$minus((scala.Function0<Rule<In, Object, Object, Object>>)end)).$plus().$tilde$minus(end);
    }
    
    public Rule<S, S, Seq<A>, X> times(final int num) {
        return this.org$json4s$scalap$SeqRule$$rule.factory().from().apply((scala.Function1<S, Result<S, Seq<A>, X>>)new SeqRule$$anonfun$times.SeqRule$$anonfun$times$1(this, num));
    }
    
    public final Result org$json4s$scalap$SeqRule$$rep$1(Object in, List acc) {
        Result obj;
        while (true) {
            obj = (Result)this.org$json4s$scalap$SeqRule$$rule.apply(in);
            if (!(obj instanceof Success)) {
                break;
            }
            final Success<Object, A> success = (Success<Object, A>)obj;
            final Object out = success.out();
            final Object a = success.value();
            final Object o = out;
            acc = acc.$colon$colon(a);
            in = o;
        }
        Object o2;
        if (Failure$.MODULE$.equals(obj)) {
            o2 = new Success(in, acc.reverse());
        }
        else {
            if (!(obj instanceof Error)) {
                throw new MatchError((Object)obj);
            }
            o2 = (Error<?>)obj;
        }
        return (Result)o2;
    }
    
    public final Result org$json4s$scalap$SeqRule$$rep$2(List result, int i, Object in, final int num$1) {
        while (i != num$1) {
            final Result obj = (Result)this.org$json4s$scalap$SeqRule$$rule.apply(in);
            if (!(obj instanceof Success)) {
                Result<Nothing$, Nothing$, X> module$;
                if (Failure$.MODULE$.equals(obj)) {
                    module$ = Failure$.MODULE$;
                }
                else {
                    if (!(obj instanceof Error)) {
                        throw new MatchError((Object)obj);
                    }
                    module$ = (Error<?>)obj;
                }
                return module$;
            }
            final Success<Object, A> success = (Success<Object, A>)obj;
            final Object out = success.out();
            final Object a = success.value();
            final List $colon$colon = result.$colon$colon(a);
            final int n = i + 1;
            in = out;
            i = n;
            result = $colon$colon;
        }
        return (Result<Nothing$, Nothing$, X>)new Success(in, result.reverse());
    }
    
    public SeqRule(final Rule<S, S, A, X> rule) {
        this.org$json4s$scalap$SeqRule$$rule = rule;
    }
}
