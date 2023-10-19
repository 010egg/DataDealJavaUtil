// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.scalap.scalasig;

import org.json4s.scalap.$tilde;
import org.json4s.scalap.Memoisable;
import org.json4s.scalap.RulesWithState;
import scala.Function0;
import scala.collection.immutable.Nil$;
import scala.None$;
import scala.collection.immutable.List;
import org.json4s.scalap.Result;
import org.json4s.scalap.InRule;
import org.json4s.scalap.SeqRule;
import scala.runtime.Nothing$;
import scala.collection.Seq;
import scala.Function1;
import org.json4s.scalap.Rule;
import scala.reflect.ScalaSignature;

@ScalaSignature(bytes = "\u0006\u0001\u0005=v!B\u0001\u0003\u0011\u0003Y\u0011aD*dC2\f7+[4QCJ\u001cXM]:\u000b\u0005\r!\u0011\u0001C:dC2\f7/[4\u000b\u0005\u00151\u0011AB:dC2\f\u0007O\u0003\u0002\b\u0011\u00051!n]8oiMT\u0011!C\u0001\u0004_J<7\u0001\u0001\t\u0003\u00195i\u0011A\u0001\u0004\u0006\u001d\tA\ta\u0004\u0002\u0010'\u000e\fG.Y*jOB\u000b'o]3sgN!Q\u0002\u0005\f\u001b!\t\tB#D\u0001\u0013\u0015\u0005\u0019\u0012!B:dC2\f\u0017BA\u000b\u0013\u0005\u0019\te.\u001f*fMB\u0011q\u0003G\u0007\u0002\t%\u0011\u0011\u0004\u0002\u0002\u000f%VdWm],ji\"\u001cF/\u0019;f!\t92$\u0003\u0002\u001d\t\tyQ*Z7pSN\f'\r\\3Sk2,7\u000fC\u0003\u001f\u001b\u0011\u0005q$\u0001\u0004=S:LGO\u0010\u000b\u0002\u0017\u0015!\u0011%\u0004\u0001#\u0005\u0005\u0019\u0006C\u0001\u0007$\u0013\t!#A\u0001\u0005TG\u0006d\u0017mU5h\u000b\u00111S\u0002A\u0014\u0003\rA\u000b'o]3s+\tA\u0003\u0007\u0005\u0003*U9JT\"A\u0007\n\u0005-b#\u0001\u0002*vY\u0016L!!\f\u0003\u0003\u0015M#\u0018\r^3Sk2,7\u000f\u0005\u00020a1\u0001A!B\u0019&\u0005\u0004\u0011$!A!\u0012\u0005M2\u0004CA\t5\u0013\t)$CA\u0004O_RD\u0017N\\4\u0011\u0005E9\u0014B\u0001\u001d\u0013\u0005\r\te.\u001f\t\u0003uur!!E\u001e\n\u0005q\u0012\u0012A\u0002)sK\u0012,g-\u0003\u0002?\u007f\t11\u000b\u001e:j]\u001eT!\u0001\u0010\n\t\u000f\u0005k!\u0019!C\u0001\u0005\u000611/_7UC\n,\u0012a\u0011\t\u0007/\u0011+UIR\u001a\n\u0005-\"\u0001CA\u0015!!\r9uJ\u0015\b\u0003\u00116s!!\u0013'\u000e\u0003)S!a\u0013\u0006\u0002\rq\u0012xn\u001c;?\u0013\u0005\u0019\u0012B\u0001(\u0013\u0003\u001d\u0001\u0018mY6bO\u0016L!\u0001U)\u0003\u0007M+\u0017O\u0003\u0002O%A!qcU+Y\u0013\t!FA\u0001\u0004%i&dG-\u001a\t\u0003#YK!a\u0016\n\u0003\u0007%sG\u000f\u0005\u0002\r3&\u0011!L\u0001\u0002\t\u0005f$XmQ8eK\"1A,\u0004Q\u0001\n\r\u000bqa]=n)\u0006\u0014\u0007\u0005C\u0004_\u001b\t\u0007I\u0011A0\u0002\tML'0Z\u000b\u0002AB1q\u0003R#F+NBaAY\u0007!\u0002\u0013\u0001\u0017!B:ju\u0016\u0004\u0003\"\u00023\u000e\t\u0003)\u0017!B3oiJLHC\u00014k!\u00199B)R4VgA\u0011Q\t[\u0005\u0003S\u000e\u0012Q!\u00128uefDQa[2A\u0002U\u000bQ!\u001b8eKbDQ!\\\u0007\u0005\u00029\f!\u0002]1sg\u0016,e\u000e\u001e:z+\ty7\u000f\u0006\u0002qkR\u0011\u0011\u000f\u001e\t\u0004S\u0015\u0012\bCA\u0018t\t\u0015\tDN1\u00013\u0011\u0015YG\u000e1\u0001V\u0011\u00151H\u000e1\u0001x\u0003\u0019\u0001\u0018M]:feB\u0019\u0001p\u001f:\u000f\u00051I\u0018B\u0001>\u0003\u0003Q\u00196-\u00197b'&<WI\u001c;ssB\u000b'o]3sg&\u0011A0 \u0002\f\u000b:$(/\u001f)beN,'O\u0003\u0002{\u0005!1q0\u0004C\u0001\u0003\u0003\t!\"\u00197m\u000b:$(/[3t+\u0011\t\u0019!!\u0007\u0015\t\u0005\u0015\u00111\u0004\t\b/\u0011+U)a\u0002:!\u0019\tI!a\u0005\u0002\u00185\u0011\u00111\u0002\u0006\u0005\u0003\u001b\ty!A\u0005j[6,H/\u00192mK*\u0019\u0011\u0011\u0003\n\u0002\u0015\r|G\u000e\\3di&|g.\u0003\u0003\u0002\u0016\u0005-!\u0001\u0002'jgR\u00042aLA\r\t\u0015\tdP1\u00013\u0011\u001d\tiB a\u0001\u0003?\t\u0011A\u001a\t\u0005qn\f9\u0002\u0003\u0006\u0002$5A)\u0019!C\u0001\u0003K\tq!\u001a8ue&,7/\u0006\u0002\u0002(I1\u0011\u0011FA\u0019\u0003k1a!a\u000b\u0001\u0001\u0005\u001d\"\u0001\u0004\u001fsK\u001aLg.Z7f]Rt\u0014bAA\u0018\t\t)!+\u001e7fgB9q\u0003R#F\u0003gI\u0004#BA\u0005\u0003'1\u0004cA\f\u00028%\u0019\u0011\u0011\b\u0003\u0003\t9\u000bW.\u001a\u0005\u000b\u0003{i\u0001\u0012!Q!\n\u0005\u001d\u0012\u0001C3oiJLWm\u001d\u0011\t\u0015\u0005\u0005S\u0002#b\u0001\n\u0003\t\u0019%A\u0004ts6\u0014w\u000e\\:\u0016\u0005\u0005\u0015#CBA$\u0003\u0013\n)D\u0002\u0004\u0002,\u0001\u0001\u0011Q\t\t\b/\u0011+U)a\u0013:!\u0019\tI!a\u0005\u0002NA\u0019A\"a\u0014\n\u0007\u0005E#A\u0001\u0004Ts6\u0014w\u000e\u001c\u0005\u000b\u0003+j\u0001\u0012!Q!\n\u0005\u0015\u0013\u0001C:z[\n|Gn\u001d\u0011\t\u0015\u0005eS\u0002#b\u0001\n\u0003\tY&A\u0004nKRDw\u000eZ:\u0016\u0005\u0005u#CBA0\u0003C\n)D\u0002\u0004\u0002,\u0001\u0001\u0011Q\f\t\b/\u0011+U)a\u0019:!\u0019\tI!a\u0005\u0002fA\u0019A\"a\u001a\n\u0007\u0005%$A\u0001\u0007NKRDw\u000eZ*z[\n|G\u000e\u0003\u0006\u0002n5A\t\u0011)Q\u0005\u0003;\n\u0001\"\\3uQ>$7\u000f\t\u0005\u000b\u0003cj\u0001R1A\u0005\u0002\u0005M\u0014AC1uiJL'-\u001e;fgV\u0011\u0011Q\u000f\n\u0007\u0003o\nI(!\u000e\u0007\r\u0005-\u0002\u0001AA;!\u001d9B)R#\u0002|e\u0002b!!\u0003\u0002\u0014\u0005u\u0004c\u0001\u0007\u0002\u0000%\u0019\u0011\u0011\u0011\u0002\u0003\u001b\u0005#HO]5ckR,\u0017J\u001c4p\u0011)\t))\u0004E\u0001B\u0003&\u0011QO\u0001\fCR$(/\u001b2vi\u0016\u001c\b\u0005\u0003\u0006\u0002\n6A)\u0019!C\u0001\u0003\u0017\u000bq\u0002^8q\u0019\u00164X\r\\\"mCN\u001cXm]\u000b\u0003\u0003\u001b\u0003ra\u0006#F\u000b\u0006=\u0015\b\u0005\u0004\u0002\n\u0005M\u0011\u0011\u0013\t\u0004\u0019\u0005M\u0015bAAK\u0005\tY1\t\\1tgNKXNY8m\u0011)\tI*\u0004E\u0001B\u0003&\u0011QR\u0001\u0011i>\u0004H*\u001a<fY\u000ec\u0017m]:fg\u0002B!\"!(\u000e\u0011\u000b\u0007I\u0011AAP\u0003=!x\u000e\u001d'fm\u0016dwJ\u00196fGR\u001cXCAAQ!\u001d9B)R#\u0002$f\u0002b!!\u0003\u0002\u0014\u0005\u0015\u0006c\u0001\u0007\u0002(&\u0019\u0011\u0011\u0016\u0002\u0003\u0019=\u0013'.Z2u'fl'm\u001c7\t\u0015\u00055V\u0002#A!B\u0013\t\t+\u0001\tu_BdUM^3m\u001f\nTWm\u0019;tA\u0001")
public final class ScalaSigParsers
{
    public static <In, Out, A, Any> Function1<In, A> expect(final Rule<In, Out, A, Any> rule) {
        return ScalaSigParsers$.MODULE$.expect(rule);
    }
    
    public static <In, Out, A, X> Rule<In, Out, A, X> oneOf(final Seq<Rule<In, Out, A, X>> rules) {
        return ScalaSigParsers$.MODULE$.oneOf(rules);
    }
    
    public static <X> Rule<Object, Nothing$, Nothing$, X> error(final X err) {
        return ScalaSigParsers$.MODULE$.error(err);
    }
    
    public static <In> Rule<In, Nothing$, Nothing$, In> error() {
        return ScalaSigParsers$.MODULE$.error();
    }
    
    public static Rule<Object, Nothing$, Nothing$, Nothing$> failure() {
        return ScalaSigParsers$.MODULE$.failure();
    }
    
    public static <Out, A> Rule<Object, Out, A, Nothing$> success(final Out out, final A a) {
        return ScalaSigParsers$.MODULE$.success(out, a);
    }
    
    public static <s> Object state() {
        return ScalaSigParsers$.MODULE$.state();
    }
    
    public static <In> Object from() {
        return ScalaSigParsers$.MODULE$.from();
    }
    
    public static <In, A, X> SeqRule<In, A, X> seqRule(final Rule<In, In, A, X> rule) {
        return ScalaSigParsers$.MODULE$.seqRule(rule);
    }
    
    public static <In, Out, A, X> InRule<In, Out, A, X> inRule(final Rule<In, Out, A, X> rule) {
        return ScalaSigParsers$.MODULE$.inRule(rule);
    }
    
    public static <In, Out, A, X> Rule<In, Out, A, X> rule(final Function1<In, Result<Out, A, X>> f) {
        return ScalaSigParsers$.MODULE$.rule(f);
    }
    
    public static <T, X> Rule<Object, Object, T, X> repeatUntil(final Rule<Object, Object, Function1<T, T>, X> rule, final Function1<T, Object> finished, final T initial) {
        return ScalaSigParsers$.MODULE$.repeatUntil(rule, finished, initial);
    }
    
    public static <A, X> Rule<Object, Object, List<A>, X> anyOf(final Seq<Rule<Object, Object, A, X>> rules) {
        return ScalaSigParsers$.MODULE$.anyOf(rules);
    }
    
    public static <A, X> Function1<Object, Result<Object, List<A>, X>> allOf(final Seq<Rule<Object, Object, A, X>> rules) {
        return ScalaSigParsers$.MODULE$.allOf(rules);
    }
    
    public static Rule<Object, Object, Object, Nothing$> cond(final Function1<Object, Object> f) {
        return ScalaSigParsers$.MODULE$.cond(f);
    }
    
    public static Rule<Object, Object, None$, Nothing$> none() {
        return ScalaSigParsers$.MODULE$.none();
    }
    
    public static Rule<Object, Object, Nil$, Nothing$> nil() {
        return ScalaSigParsers$.MODULE$.nil();
    }
    
    public static Rule<Object, Object, Object, Nothing$> update(final Function1<Object, Object> f) {
        return ScalaSigParsers$.MODULE$.update(f);
    }
    
    public static Rule<Object, Object, Object, Nothing$> set(final Function0<Object> s) {
        return ScalaSigParsers$.MODULE$.set(s);
    }
    
    public static Rule<Object, Object, Object, Nothing$> get() {
        return ScalaSigParsers$.MODULE$.get();
    }
    
    public static <A> Rule<Object, Object, A, Nothing$> read(final Function1<Object, A> f) {
        return ScalaSigParsers$.MODULE$.read(f);
    }
    
    public static <A> Rule<Object, Object, A, Nothing$> unit(final Function0<A> a) {
        return ScalaSigParsers$.MODULE$.unit(a);
    }
    
    public static <A, X> Rule<Object, Object, A, X> apply(final Function1<Object, Result<Object, A, X>> f) {
        return ScalaSigParsers$.MODULE$.apply(f);
    }
    
    public static void org$json4s$scalap$RulesWithState$_setter_$factory_$eq(final RulesWithState x$1) {
        ScalaSigParsers$.MODULE$.org$json4s$scalap$RulesWithState$_setter_$factory_$eq(x$1);
    }
    
    public static RulesWithState factory() {
        return ScalaSigParsers$.MODULE$.factory();
    }
    
    public static <In, Out, A, X> Rule<In, Out, A, X> ruleWithName(final String name, final Function1<In, Result<Out, A, X>> f) {
        return ScalaSigParsers$.MODULE$.ruleWithName(name, f);
    }
    
    public static <In extends Memoisable, Out, A, X> Rule<In, Out, A, X> memo(final Object key, final Function0<Function1<In, Result<Out, A, X>>> toRule) {
        return ScalaSigParsers$.MODULE$.memo(key, toRule);
    }
    
    public static Rule<ScalaSig, ScalaSig, List<ObjectSymbol>, String> topLevelObjects() {
        return ScalaSigParsers$.MODULE$.topLevelObjects();
    }
    
    public static Rule<ScalaSig, ScalaSig, List<ClassSymbol>, String> topLevelClasses() {
        return ScalaSigParsers$.MODULE$.topLevelClasses();
    }
    
    public static Rule<ScalaSig, ScalaSig, List<AttributeInfo>, String> attributes() {
        return ScalaSigParsers$.MODULE$.attributes();
    }
    
    public static Rule<ScalaSig, ScalaSig, List<MethodSymbol>, String> methods() {
        return ScalaSigParsers$.MODULE$.methods();
    }
    
    public static Rule<ScalaSig, ScalaSig, List<Symbol>, String> symbols() {
        return ScalaSigParsers$.MODULE$.symbols();
    }
    
    public static Rule<ScalaSig, ScalaSig, List<Object>, String> entries() {
        return ScalaSigParsers$.MODULE$.entries();
    }
    
    public static <A> Rule<ScalaSig, ScalaSig, List<A>, String> allEntries(final Rule<ScalaSig.Entry, ScalaSig.Entry, A, String> f) {
        return ScalaSigParsers$.MODULE$.allEntries(f);
    }
    
    public static <A> Rule<ScalaSig, ScalaSig, A, String> parseEntry(final Rule<ScalaSig.Entry, ScalaSig.Entry, A, String> parser, final int index) {
        return ScalaSigParsers$.MODULE$.parseEntry(parser, index);
    }
    
    public static Rule<ScalaSig, ScalaSig.Entry, Object, Nothing$> entry(final int index) {
        return ScalaSigParsers$.MODULE$.entry(index);
    }
    
    public static Rule<ScalaSig, ScalaSig, Object, Nothing$> size() {
        return ScalaSigParsers$.MODULE$.size();
    }
    
    public static Rule<ScalaSig, ScalaSig, Seq<$tilde<Object, ByteCode>>, Nothing$> symTab() {
        return ScalaSigParsers$.MODULE$.symTab();
    }
}
