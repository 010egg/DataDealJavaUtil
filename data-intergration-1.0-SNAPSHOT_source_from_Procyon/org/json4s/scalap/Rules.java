// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.scalap;

import scala.Function1$class;
import scala.Function7;
import scala.Function6;
import scala.Function5;
import scala.Function4;
import scala.Function3;
import scala.Function2;
import scala.collection.immutable.List;
import scala.PartialFunction;
import scala.Function0;
import scala.collection.Seq;
import scala.runtime.Nothing$;
import scala.Function1;
import scala.reflect.ScalaSignature;

@ScalaSignature(bytes = "\u0006\u0001\t-aaB\u0001\u0003!\u0003\r\t!\u0003\u0002\u0006%VdWm\u001d\u0006\u0003\u0007\u0011\taa]2bY\u0006\u0004(BA\u0003\u0007\u0003\u0019Q7o\u001c85g*\tq!A\u0002pe\u001e\u001c\u0001a\u0005\u0002\u0001\u0015A\u00111BD\u0007\u0002\u0019)\tQ\"A\u0003tG\u0006d\u0017-\u0003\u0002\u0010\u0019\t1\u0011I\\=SK\u001aDQ!\u0005\u0001\u0005\u0002I\ta\u0001J5oSR$C#A\n\u0011\u0005-!\u0012BA\u000b\r\u0005\u0011)f.\u001b;\t\u000b]\u0001A1\u0001\r\u0002\tI,H.Z\u000b\u00063\u0001RS\u0006\r\u000b\u00035I\u0002ba\u0007\u000f\u001fS1zS\"\u0001\u0002\n\u0005u\u0011!\u0001\u0002*vY\u0016\u0004\"a\b\u0011\r\u0001\u0011)\u0011E\u0006b\u0001E\t\u0011\u0011J\\\t\u0003G\u0019\u0002\"a\u0003\u0013\n\u0005\u0015b!a\u0002(pi\"Lgn\u001a\t\u0003\u0017\u001dJ!\u0001\u000b\u0007\u0003\u0007\u0005s\u0017\u0010\u0005\u0002 U\u0011)1F\u0006b\u0001E\t\u0019q*\u001e;\u0011\u0005}iC!\u0002\u0018\u0017\u0005\u0004\u0011#!A!\u0011\u0005}\u0001D!B\u0019\u0017\u0005\u0004\u0011#!\u0001-\t\u000bM2\u0002\u0019\u0001\u001b\u0002\u0003\u0019\u0004BaC\u001b\u001fo%\u0011a\u0007\u0004\u0002\n\rVt7\r^5p]F\u0002Ra\u0007\u001d*Y=J!!\u000f\u0002\u0003\rI+7/\u001e7u\u0011\u0015Y\u0004\u0001b\u0001=\u0003\u0019IgNU;mKV)QH\u0011#G\u0011R\u0011a(\u0013\t\u00077}\n5)R$\n\u0005\u0001\u0013!AB%o%VdW\r\u0005\u0002 \u0005\u0012)\u0011E\u000fb\u0001EA\u0011q\u0004\u0012\u0003\u0006Wi\u0012\rA\t\t\u0003?\u0019#QA\f\u001eC\u0002\t\u0002\"a\b%\u0005\u000bER$\u0019\u0001\u0012\t\u000b]Q\u0004\u0019\u0001&\u0011\rma\u0012iQ#H\u0011\u0015a\u0005\u0001b\u0001N\u0003\u001d\u0019X-\u001d*vY\u0016,BAT*V/R\u0011q\n\u0017\t\u00067A\u0013FKV\u0005\u0003#\n\u0011qaU3r%VdW\r\u0005\u0002 '\u0012)\u0011e\u0013b\u0001EA\u0011q$\u0016\u0003\u0006]-\u0013\rA\t\t\u0003?]#Q!M&C\u0002\tBQaF&A\u0002e\u0003ba\u0007\u000fS%R3faB.\u0001!\u0003\r\n\u0001\u0018\u0002\t\rJ|WNU;mKV\u0011Q\fZ\n\u00035*AQa\u0018.\u0007\u0002\u0001\fQ!\u00199qYf,B!\u00194iUR\u0011!m\u001b\t\u00077q\u0019WmZ5\u0011\u0005}!G!B\u0011[\u0005\u0004\u0011\u0003CA\u0010g\t\u0015YcL1\u0001#!\ty\u0002\u000eB\u0003/=\n\u0007!\u0005\u0005\u0002 U\u0012)\u0011G\u0018b\u0001E!)1G\u0018a\u0001YB!1\"N2n!\u0015Y\u0002(Z4j\u0011\u0015y\u0007\u0001\"\u0001q\u0003\u00111'o\\7\u0016\u0005EDX#\u0001:\u0013\u0007MTQO\u0002\u0003u]\u0002\u0011(\u0001\u0004\u001fsK\u001aLg.Z7f]Rt\u0004c\u0001<[o6\t\u0001\u0001\u0005\u0002 q\u0012)\u0011E\u001cb\u0001E!)!\u0010\u0001C\u0001w\u0006)1\u000f^1uKV\u0019A0a\u0003\u0016\u0003u\u00142A \u0006\u0000\r\u0011!\u0018\u0010A?\u0011\u0007m\t\t!C\u0002\u0002\u0004\t\u0011!b\u0015;bi\u0016\u0014V\u000f\\3t\u000b\u0019\t9A \u0001\u0002\n\t\t1\u000bE\u0002 \u0003\u0017!a!!\u0004z\u0005\u0004\u0011#!A:\t\u000f\u0005E\u0001\u0001\"\u0001\u0002\u0014\u000591/^2dKN\u001cXCBA\u000b\u00037\ty\u0002\u0006\u0004\u0002\u0018\u0005\u0005\u0012Q\u0005\t\t7q1\u0013\u0011DA\u000fGA\u0019q$a\u0007\u0005\r-\nyA1\u0001#!\ry\u0012q\u0004\u0003\u0007]\u0005=!\u0019\u0001\u0012\t\u0011\u0005\r\u0012q\u0002a\u0001\u00033\t1a\\;u\u0011!\t9#a\u0004A\u0002\u0005u\u0011!A1\t\u000f\u0005-\u0002\u0001\"\u0001\u0002.\u00059a-Y5mkJ,WCAA\u0018!\u0019YBDJ\u0012$G!9\u00111\u0007\u0001\u0005\u0002\u0005U\u0012!B3se>\u0014X\u0003BA\u001c\u0003{)\"!!\u000f\u0011\u0011ma\u00121H\u0012$\u0003w\u00012aHA\u001f\t\u0019\t\u0013\u0011\u0007b\u0001E!9\u00111\u0007\u0001\u0005\u0002\u0005\u0005S\u0003BA\"\u0003\u0013\"B!!\u0012\u0002LA91\u0004\b\u0014$G\u0005\u001d\u0003cA\u0010\u0002J\u00111\u0011'a\u0010C\u0002\tB\u0001\"!\u0014\u0002@\u0001\u0007\u0011qI\u0001\u0004KJ\u0014\bbBA)\u0001\u0011\u0005\u00111K\u0001\u0006_:,wJZ\u000b\u000b\u0003+\nY&a\u0018\u0002d\u0005\u001dD\u0003BA,\u0003S\u0002\"b\u0007\u000f\u0002Z\u0005u\u0013\u0011MA3!\ry\u00121\f\u0003\u0007C\u0005=#\u0019\u0001\u0012\u0011\u0007}\ty\u0006\u0002\u0004,\u0003\u001f\u0012\rA\t\t\u0004?\u0005\rDA\u0002\u0018\u0002P\t\u0007!\u0005E\u0002 \u0003O\"a!MA(\u0005\u0004\u0011\u0003\u0002CA6\u0003\u001f\u0002\r!!\u001c\u0002\u000bI,H.Z:\u0011\u000b-\ty'a\u0016\n\u0007\u0005EDB\u0001\u0006=e\u0016\u0004X-\u0019;fIzBq!!\u001e\u0001\t\u0003\t9(\u0001\u0007sk2,w+\u001b;i\u001d\u0006lW-\u0006\u0006\u0002z\u0005\r\u0015qQAF\u0003\u001f#b!a\u001f\u0002\u0018\u0006%&CBA?\u0003\u007f\n\tJB\u0003u\u0001\u0001\tY\b\u0005\u0006\u001c9\u0005\u0005\u0015QQAE\u0003\u001b\u00032aHAB\t\u0019\t\u00131\u000fb\u0001EA\u0019q$a\"\u0005\r-\n\u0019H1\u0001#!\ry\u00121\u0012\u0003\u0007]\u0005M$\u0019\u0001\u0012\u0011\u0007}\ty\t\u0002\u00042\u0003g\u0012\rA\t\t\u00047\u0005M\u0015bAAK\u0005\t!a*Y7f\u0011!\tI*a\u001dA\u0002\u0005m\u0015!B0oC6,\u0007\u0003BAO\u0003Gs1aCAP\u0013\r\t\t\u000bD\u0001\u0007!J,G-\u001a4\n\t\u0005\u0015\u0016q\u0015\u0002\u0007'R\u0014\u0018N\\4\u000b\u0007\u0005\u0005F\u0002C\u00044\u0003g\u0002\r!a+\u0011\r-)\u0014\u0011QAW!!Y\u0002(!\"\u0002\n\u00065eABAY\u0001\u0001\t\u0019LA\u0006EK\u001a\fW\u000f\u001c;Sk2,WCCA[\u0003w\u000by,a1\u0002HN)\u0011q\u0016\u0006\u00028BQ1\u0004HA]\u0003{\u000b\t-!2\u0011\u0007}\tY\f\u0002\u0004\"\u0003_\u0013\rA\t\t\u0004?\u0005}FAB\u0016\u00020\n\u0007!\u0005E\u0002 \u0003\u0007$aALAX\u0005\u0004\u0011\u0003cA\u0010\u0002H\u00121\u0011'a,C\u0002\tB!bMAX\u0005\u0003\u0005\u000b\u0011BAf!\u0019YQ'!/\u0002NBA1\u0004OA_\u0003\u0003\f)\r\u0003\u0005\u0002R\u0006=F\u0011AAj\u0003\u0019a\u0014N\\5u}Q!\u0011Q[Al!-1\u0018qVA]\u0003{\u000b\t-!2\t\u000fM\ny\r1\u0001\u0002L\"Q\u00111\\AX\u0005\u0004%\t!!8\u0002\u000f\u0019\f7\r^8ssV\u0011\u0011q\u001c\t\u00037\u0001A\u0011\"a9\u00020\u0002\u0006I!a8\u0002\u0011\u0019\f7\r^8ss\u0002BqaXAX\t\u0003\t9\u000f\u0006\u0003\u0002N\u0006%\b\u0002CAv\u0003K\u0004\r!!/\u0002\u0005%t\u0007bBAx\u0001\u0011\u0005\u0011\u0011_\u0001\u0007Kb\u0004Xm\u0019;\u0016\u0015\u0005M\u0018\u0011 B\u0003\u0003{\u0014I\u0001\u0006\u0003\u0002v\u0006}\bCB\u00066\u0003o\fY\u0010E\u0002 \u0003s$a!IAw\u0005\u0004\u0011\u0003cA\u0010\u0002~\u00121a&!<C\u0002\tBqaFAw\u0001\u0004\u0011\t\u0001\u0005\u0006\u001c9\u0005](1AA~\u0005\u000f\u00012a\bB\u0003\t\u0019Y\u0013Q\u001eb\u0001EA\u0019qD!\u0003\u0005\r!\niO1\u0001#\u0001")
public interface Rules
{
     <In, Out, A, X> Rule<In, Out, A, X> rule(final Function1<In, Result<Out, A, X>> p0);
    
     <In, Out, A, X> InRule<In, Out, A, X> inRule(final Rule<In, Out, A, X> p0);
    
     <In, A, X> SeqRule<In, A, X> seqRule(final Rule<In, In, A, X> p0);
    
     <In> Object from();
    
     <s> Object state();
    
     <Out, A> Rule<Object, Out, A, Nothing$> success(final Out p0, final A p1);
    
    Rule<Object, Nothing$, Nothing$, Nothing$> failure();
    
     <In> Rule<In, Nothing$, Nothing$, In> error();
    
     <X> Rule<Object, Nothing$, Nothing$, X> error(final X p0);
    
     <In, Out, A, X> Rule<In, Out, A, X> oneOf(final Seq<Rule<In, Out, A, X>> p0);
    
     <In, Out, A, X> Rule<In, Out, A, X> ruleWithName(final String p0, final Function1<In, Result<Out, A, X>> p1);
    
     <In, Out, A, Any> Function1<In, A> expect(final Rule<In, Out, A, Any> p0);
    
    public class DefaultRule<In, Out, A, X> implements Rule<In, Out, A, X>
    {
        private final Function1<In, Result<Out, A, X>> f;
        private final Rules factory;
        
        @Override
        public Rule<In, Out, A, X> as(final String name) {
            return (Rule<In, Out, A, X>)Rule$class.as(this, name);
        }
        
        @Override
        public <Out2, B, X2> Rule<In, Out2, B, X2> flatMap(final Function1<A, Function1<Out, Result<Out2, B, X2>>> fa2ruleb) {
            return (Rule<In, Out2, B, X2>)Rule$class.flatMap(this, fa2ruleb);
        }
        
        @Override
        public <B> Rule<In, Out, B, X> map(final Function1<A, B> fa2b) {
            return (Rule<In, Out, B, X>)Rule$class.map(this, fa2b);
        }
        
        @Override
        public Rule<In, Out, A, X> filter(final Function1<A, Object> f) {
            return (Rule<In, Out, A, X>)Rule$class.filter(this, f);
        }
        
        @Override
        public <Out2, B, Y> Rule<In, Out2, B, Y> mapResult(final Function1<Result<Out, A, X>, Result<Out2, B, Y>> f) {
            return (Rule<In, Out2, B, Y>)Rule$class.mapResult(this, f);
        }
        
        @Override
        public <In2 extends In, Out2, A2, X2> Rule<In2, Out2, A2, X2> orElse(final Function0<Rule<In2, Out2, A2, X2>> other) {
            return (Rule<In2, Out2, A2, X2>)Rule$class.orElse(this, other);
        }
        
        @Override
        public <In2 extends In> Rule<In, Out, A, Object> orError() {
            return (Rule<In, Out, A, Object>)Rule$class.orError(this);
        }
        
        @Override
        public <In2 extends In, Out2, A2, X2> Rule<In2, Out2, A2, X2> $bar(final Function0<Rule<In2, Out2, A2, X2>> other) {
            return (Rule<In2, Out2, A2, X2>)Rule$class.$bar(this, other);
        }
        
        @Override
        public <B> Rule<In, Out, B, X> $up$up(final Function1<A, B> fa2b) {
            return (Rule<In, Out, B, X>)Rule$class.$up$up(this, fa2b);
        }
        
        @Override
        public <B> Rule<In, Out, B, X> $up$up$qmark(final PartialFunction<A, B> pf) {
            return (Rule<In, Out, B, X>)Rule$class.$up$up$qmark(this, pf);
        }
        
        @Override
        public Rule<In, Out, A, X> $qmark$qmark(final PartialFunction<A, Object> pf) {
            return (Rule<In, Out, A, X>)Rule$class.$qmark$qmark(this, pf);
        }
        
        @Override
        public <B> Rule<In, Out, B, X> $minus$up(final B b) {
            return (Rule<In, Out, B, X>)Rule$class.$minus$up(this, b);
        }
        
        @Override
        public <Y> Rule<In, Out, A, Y> $bang$up(final Function1<X, Y> fx2y) {
            return (Rule<In, Out, A, Y>)Rule$class.$bang$up(this, fx2y);
        }
        
        @Override
        public <Out2, B, X2> Rule<In, Out2, B, X2> $greater$greater(final Function1<A, Function1<Out, Result<Out2, B, X2>>> fa2ruleb) {
            return (Rule<In, Out2, B, X2>)Rule$class.$greater$greater(this, fa2ruleb);
        }
        
        @Override
        public <Out2, B, X2> Rule<In, Out2, B, X2> $greater$minus$greater(final Function1<A, Result<Out2, B, X2>> fa2resultb) {
            return (Rule<In, Out2, B, X2>)Rule$class.$greater$minus$greater(this, fa2resultb);
        }
        
        @Override
        public <Out2, B, X2> Rule<In, Out2, B, X2> $greater$greater$qmark(final PartialFunction<A, Rule<Out, Out2, B, X2>> pf) {
            return (Rule<In, Out2, B, X2>)Rule$class.$greater$greater$qmark(this, pf);
        }
        
        @Override
        public <B, X2> Rule<In, Out, B, X2> $greater$greater$amp(final Function1<A, Function1<Out, Result<Object, B, X2>>> fa2ruleb) {
            return (Rule<In, Out, B, X2>)Rule$class.$greater$greater$amp(this, fa2ruleb);
        }
        
        @Override
        public <Out2, B, X2> Rule<In, Out2, $tilde<A, B>, X2> $tilde(final Function0<Rule<Out, Out2, B, X2>> next) {
            return (Rule<In, Out2, $tilde<A, B>, X2>)Rule$class.$tilde(this, next);
        }
        
        @Override
        public <Out2, B, X2> Rule<In, Out2, A, X2> $tilde$minus(final Function0<Rule<Out, Out2, B, X2>> next) {
            return (Rule<In, Out2, A, X2>)Rule$class.$tilde$minus(this, next);
        }
        
        @Override
        public <Out2, B, X2> Rule<In, Out2, B, X2> $minus$tilde(final Function0<Rule<Out, Out2, B, X2>> next) {
            return (Rule<In, Out2, B, X2>)Rule$class.$minus$tilde(this, next);
        }
        
        @Override
        public <Out2, B, X2> Rule<In, Out2, List<B>, X2> $tilde$plus$plus(final Function0<Rule<Out, Out2, Seq<B>, X2>> next) {
            return (Rule<In, Out2, List<B>, X2>)Rule$class.$tilde$plus$plus(this, next);
        }
        
        @Override
        public <Out2, B, X2> Rule<In, Out2, B, X2> $tilde$greater(final Function0<Rule<Out, Out2, Function1<A, B>, X2>> next) {
            return (Rule<In, Out2, B, X2>)Rule$class.$tilde$greater(this, next);
        }
        
        @Override
        public <InPrev, B, X2> Rule<InPrev, Out, B, X2> $less$tilde$colon(final Function0<Rule<InPrev, In, Function1<A, B>, X2>> prev) {
            return (Rule<InPrev, Out, B, X2>)Rule$class.$less$tilde$colon(this, prev);
        }
        
        @Override
        public <Out2, B, X2> Rule<In, Out2, $tilde<A, B>, Object> $tilde$bang(final Function0<Rule<Out, Out2, B, X2>> next) {
            return (Rule<In, Out2, $tilde<A, B>, Object>)Rule$class.$tilde$bang(this, next);
        }
        
        @Override
        public <Out2, B, X2> Rule<In, Out2, A, Object> $tilde$minus$bang(final Function0<Rule<Out, Out2, B, X2>> next) {
            return (Rule<In, Out2, A, Object>)Rule$class.$tilde$minus$bang(this, next);
        }
        
        @Override
        public <Out2, B, X2> Rule<In, Out2, B, Object> $minus$tilde$bang(final Function0<Rule<Out, Out2, B, X2>> next) {
            return (Rule<In, Out2, B, Object>)Rule$class.$minus$tilde$bang(this, next);
        }
        
        @Override
        public <In2 extends In> Rule<In2, Out, A, X> $minus(final Function0<Rule<In2, Object, Object, Object>> exclude) {
            return (Rule<In2, Out, A, X>)Rule$class.$minus(this, exclude);
        }
        
        @Override
        public <B1, B2, B, C> Rule<In, Out, C, X> $up$tilde$up(final Function2<B1, B2, C> f, final Function1<A, $tilde<B1, B2>> A) {
            return (Rule<In, Out, C, X>)Rule$class.$up$tilde$up(this, f, A);
        }
        
        @Override
        public <B1, B2, B3, B, C> Rule<In, Out, C, X> $up$tilde$tilde$up(final Function3<B1, B2, B3, C> f, final Function1<A, $tilde<$tilde<B1, B2>, B3>> A) {
            return (Rule<In, Out, C, X>)Rule$class.$up$tilde$tilde$up(this, f, A);
        }
        
        @Override
        public <B1, B2, B3, B4, B, C> Rule<In, Out, C, X> $up$tilde$tilde$tilde$up(final Function4<B1, B2, B3, B4, C> f, final Function1<A, $tilde<$tilde<$tilde<B1, B2>, B3>, B4>> A) {
            return (Rule<In, Out, C, X>)Rule$class.$up$tilde$tilde$tilde$up(this, f, A);
        }
        
        @Override
        public <B1, B2, B3, B4, B5, B, C> Rule<In, Out, C, X> $up$tilde$tilde$tilde$tilde$up(final Function5<B1, B2, B3, B4, B5, C> f, final Function1<A, $tilde<$tilde<$tilde<$tilde<B1, B2>, B3>, B4>, B5>> A) {
            return (Rule<In, Out, C, X>)Rule$class.$up$tilde$tilde$tilde$tilde$up(this, f, A);
        }
        
        @Override
        public <B1, B2, B3, B4, B5, B6, B, C> Rule<In, Out, C, X> $up$tilde$tilde$tilde$tilde$tilde$up(final Function6<B1, B2, B3, B4, B5, B6, C> f, final Function1<A, $tilde<$tilde<$tilde<$tilde<$tilde<B1, B2>, B3>, B4>, B5>, B6>> A) {
            return (Rule<In, Out, C, X>)Rule$class.$up$tilde$tilde$tilde$tilde$tilde$up(this, f, A);
        }
        
        @Override
        public Rule $up$tilde$tilde$tilde$tilde$tilde$tilde$up(final Function7 f, final Function1 A) {
            return Rule$class.$up$tilde$tilde$tilde$tilde$tilde$tilde$up(this, f, A);
        }
        
        @Override
        public <Out2, B1, B2, B, C, X2> Rule<In, Out2, C, X2> $greater$tilde$greater(final Function2<B1, B2, Function1<Out, Result<Out2, C, X2>>> f, final Function1<A, $tilde<B1, B2>> A) {
            return (Rule<In, Out2, C, X2>)Rule$class.$greater$tilde$greater(this, f, A);
        }
        
        @Override
        public <B1, B2, C> Rule<In, Out, Function1<B1, C>, X> $up$minus$up(final Function2<B1, B2, C> f) {
            return (Rule<In, Out, Function1<B1, C>, X>)Rule$class.$up$minus$up(this, f);
        }
        
        @Override
        public <B1, B2, B3, B, C> Rule<In, Out, Function1<B1, C>, X> $up$tilde$greater$tilde$up(final Function3<B1, B2, B3, C> f, final Function1<A, $tilde<B2, B3>> A) {
            return (Rule<In, Out, Function1<B1, C>, X>)Rule$class.$up$tilde$greater$tilde$up(this, f, A);
        }
        
        public boolean apply$mcZD$sp(final double v1) {
            return Function1$class.apply$mcZD$sp((Function1)this, v1);
        }
        
        public double apply$mcDD$sp(final double v1) {
            return Function1$class.apply$mcDD$sp((Function1)this, v1);
        }
        
        public float apply$mcFD$sp(final double v1) {
            return Function1$class.apply$mcFD$sp((Function1)this, v1);
        }
        
        public int apply$mcID$sp(final double v1) {
            return Function1$class.apply$mcID$sp((Function1)this, v1);
        }
        
        public long apply$mcJD$sp(final double v1) {
            return Function1$class.apply$mcJD$sp((Function1)this, v1);
        }
        
        public void apply$mcVD$sp(final double v1) {
            Function1$class.apply$mcVD$sp((Function1)this, v1);
        }
        
        public boolean apply$mcZF$sp(final float v1) {
            return Function1$class.apply$mcZF$sp((Function1)this, v1);
        }
        
        public double apply$mcDF$sp(final float v1) {
            return Function1$class.apply$mcDF$sp((Function1)this, v1);
        }
        
        public float apply$mcFF$sp(final float v1) {
            return Function1$class.apply$mcFF$sp((Function1)this, v1);
        }
        
        public int apply$mcIF$sp(final float v1) {
            return Function1$class.apply$mcIF$sp((Function1)this, v1);
        }
        
        public long apply$mcJF$sp(final float v1) {
            return Function1$class.apply$mcJF$sp((Function1)this, v1);
        }
        
        public void apply$mcVF$sp(final float v1) {
            Function1$class.apply$mcVF$sp((Function1)this, v1);
        }
        
        public boolean apply$mcZI$sp(final int v1) {
            return Function1$class.apply$mcZI$sp((Function1)this, v1);
        }
        
        public double apply$mcDI$sp(final int v1) {
            return Function1$class.apply$mcDI$sp((Function1)this, v1);
        }
        
        public float apply$mcFI$sp(final int v1) {
            return Function1$class.apply$mcFI$sp((Function1)this, v1);
        }
        
        public int apply$mcII$sp(final int v1) {
            return Function1$class.apply$mcII$sp((Function1)this, v1);
        }
        
        public long apply$mcJI$sp(final int v1) {
            return Function1$class.apply$mcJI$sp((Function1)this, v1);
        }
        
        public void apply$mcVI$sp(final int v1) {
            Function1$class.apply$mcVI$sp((Function1)this, v1);
        }
        
        public boolean apply$mcZJ$sp(final long v1) {
            return Function1$class.apply$mcZJ$sp((Function1)this, v1);
        }
        
        public double apply$mcDJ$sp(final long v1) {
            return Function1$class.apply$mcDJ$sp((Function1)this, v1);
        }
        
        public float apply$mcFJ$sp(final long v1) {
            return Function1$class.apply$mcFJ$sp((Function1)this, v1);
        }
        
        public int apply$mcIJ$sp(final long v1) {
            return Function1$class.apply$mcIJ$sp((Function1)this, v1);
        }
        
        public long apply$mcJJ$sp(final long v1) {
            return Function1$class.apply$mcJJ$sp((Function1)this, v1);
        }
        
        public void apply$mcVJ$sp(final long v1) {
            Function1$class.apply$mcVJ$sp((Function1)this, v1);
        }
        
        public <A> Function1<A, Result<Out, A, X>> compose(final Function1<A, In> g) {
            return (Function1<A, Result<Out, A, X>>)Function1$class.compose((Function1)this, (Function1)g);
        }
        
        public <A> Function1<In, A> andThen(final Function1<Result<Out, A, X>, A> g) {
            return (Function1<In, A>)Function1$class.andThen((Function1)this, (Function1)g);
        }
        
        @Override
        public String toString() {
            return Function1$class.toString((Function1)this);
        }
        
        @Override
        public Rules factory() {
            return this.factory;
        }
        
        public Result<Out, A, X> apply(final In in) {
            return (Result<Out, A, X>)this.f.apply((Object)in);
        }
        
        public DefaultRule(final Function1<In, Result<Out, A, X>> f) {
            this.f = f;
            if (Rules.this == null) {
                throw null;
            }
            Function1$class.$init$((Function1)this);
            Rule$class.$init$(this);
            this.factory = Rules.this;
        }
    }
    
    public interface FromRule<In>
    {
         <Out, A, X> Rule<In, Out, A, X> apply(final Function1<In, Result<Out, A, X>> p0);
    }
}
