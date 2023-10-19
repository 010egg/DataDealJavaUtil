// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.scalap.scalasig;

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

@ScalaSignature(bytes = "\u0006\u0001\t\u001dx!B\u0001\u0003\u0011\u0003Y\u0011\u0001F*dC2\f7+[4F]R\u0014\u0018\u0010U1sg\u0016\u00148O\u0003\u0002\u0004\t\u0005A1oY1mCNLwM\u0003\u0002\u0006\r\u000511oY1mCBT!a\u0002\u0005\u0002\r)\u001cxN\u001c\u001bt\u0015\u0005I\u0011aA8sO\u000e\u0001\u0001C\u0001\u0007\u000e\u001b\u0005\u0011a!\u0002\b\u0003\u0011\u0003y!\u0001F*dC2\f7+[4F]R\u0014\u0018\u0010U1sg\u0016\u00148o\u0005\u0003\u000e!YQ\u0002CA\t\u0015\u001b\u0005\u0011\"\"A\n\u0002\u000bM\u001c\u0017\r\\1\n\u0005U\u0011\"AB!osJ+g\r\u0005\u0002\u001815\tA!\u0003\u0002\u001a\t\tq!+\u001e7fg^KG\u000f[*uCR,\u0007CA\f\u001c\u0013\taBAA\bNK6|\u0017n]1cY\u0016\u0014V\u000f\\3t\u0011\u0015qR\u0002\"\u0001 \u0003\u0019a\u0014N\\5u}Q\t1\"\u0002\u0003\"\u001b\u0001\u0011#!A*\u0011\u0005\r2\u0003C\u0001\u0007%\u0013\t)#A\u0001\u0005TG\u0006d\u0017mU5h\u0013\t9CEA\u0003F]R\u0014\u00180\u0002\u0003*\u001b\u0001Q#aC#oiJL\b+\u0019:tKJ,\"aK\u001a\u0011\t1j\u0013\u0007P\u0007\u0002\u001b%\u0011af\f\u0002\u0005%VdW-\u0003\u00021\t\tQ1\u000b^1uKJ+H.Z:\u0011\u0005I\u001aD\u0002\u0001\u0003\u0006i!\u0012\r!\u000e\u0002\u0002\u0003F\u0011a'\u000f\t\u0003#]J!\u0001\u000f\n\u0003\u000f9{G\u000f[5oOB\u0011\u0011CO\u0005\u0003wI\u00111!\u00118z!\ti\u0004I\u0004\u0002\u0012}%\u0011qHE\u0001\u0007!J,G-\u001a4\n\u0005\u0005\u0013%AB*ue&twM\u0003\u0002@%!)A)\u0004C\u0002\u000b\u0006\u0019\"-\u001f;f\u0007>$W-\u00128uef\u0004\u0016M]:feV\u0011a)\u0013\u000b\u0003\u000f*\u00032\u0001\f\u0015I!\t\u0011\u0014\nB\u00035\u0007\n\u0007Q\u0007C\u0003L\u0007\u0002\u0007A*\u0001\u0003sk2,\u0007cA'Q\u0011:\u0011ABT\u0005\u0003\u001f\n\t\u0001dU2bY\u0006\u001c\u0016nZ!uiJL'-\u001e;f!\u0006\u00148/\u001a:t\u0013\t\t&K\u0001\u0004QCJ\u001cXM]\u0005\u0003'\n\u0011aBQ=uK\u000e{G-\u001a*fC\u0012,'\u000fC\u0003V\u001b\u0011\u0005a+A\u0004u_\u0016sGO]=\u0016\u0005]\u0003GC\u0001-_!\u00199\u0012L\u0017.\\m%\u0011a\u0006\u0002\t\u0003Y\u0001\u0002\"!\u0005/\n\u0005u\u0013\"aA%oi\")q\f\u0016a\u00017\u0006)\u0011N\u001c3fq\u0012)A\u0007\u0016b\u0001k!)!-\u0004C\u0001G\u0006Q\u0001/\u0019:tK\u0016sGO]=\u0016\u0005\u0011DGCA3k)\t1\u0017\u000e\u0005\u0004\u00183jSv\r\u0010\t\u0003e!$Q\u0001N1C\u0002UBQaX1A\u0002mCQa[1A\u00021\fa\u0001]1sg\u0016\u0014\bc\u0001\u0017)O\")a.\u0004C\u0002_\u0006IQM\u001c;ssRK\b/\u001a\u000b\u00031BDQ!]7A\u0002m\u000bAaY8eK\"9q,\u0004b\u0001\n\u0003\u0019X#\u0001-\t\rUl\u0001\u0015!\u0003Y\u0003\u0019Ig\u000eZ3yA!9q/\u0004b\u0001\n\u0003\u0019\u0018aA6fs\"1\u00110\u0004Q\u0001\na\u000bAa[3zA!A10\u0004EC\u0002\u0013\u0005A0A\u0003f]R\u0014\u00180F\u0001~!\ra\u0003&\u000f\u0005\t\u007f6A\t\u0011)Q\u0005{\u00061QM\u001c;ss\u0002B\u0011\"a\u0001\u000e\u0005\u0004%\t!!\u0002\u0002\u0007I,g-\u0006\u0002\u0002\bA\u0019A\u0006K.\t\u0011\u0005-Q\u0002)A\u0005\u0003\u000f\tAA]3gA!I\u0011qB\u0007C\u0002\u0013\u0005\u0011\u0011C\u0001\ti\u0016\u0014XNT1nKV\u0011\u00111\u0003\t\u0007/eS&\f\u0010\u001f\t\u0011\u0005]Q\u0002)A\u0005\u0003'\t\u0011\u0002^3s[:\u000bW.\u001a\u0011\t\u0013\u0005mQB1A\u0005\u0002\u0005E\u0011\u0001\u0003;za\u0016t\u0015-\\3\t\u0011\u0005}Q\u0002)A\u0005\u0003'\t\u0011\u0002^=qK:\u000bW.\u001a\u0011\t\u0013\u0005\rRB1A\u0005\u0002\u0005\u0015\u0012\u0001\u00028b[\u0016,\"!a\n\u0013\r\u0005%\u00121CA\u0019\r\u0019\tY\u0003\u0001\u0001\u0002(\taAH]3gS:,W.\u001a8u}%\u0019\u0011q\u0006\u0003\u0003\u000bI+H.Z:\u0011\u0007]\t\u0019$C\u0002\u00026\u0011\u0011AAT1nK\"A\u0011\u0011H\u0007!\u0002\u0013\t9#A\u0003oC6,\u0007\u0005C\u0004\u0002>5!\t!a\u0010\u0002\u000bI,g\rV8\u0016\t\u0005\u0005\u0013q\t\u000b\u0005\u0003\u0007\nI\u0005\u0005\u0003-Q\u0005\u0015\u0003c\u0001\u001a\u0002H\u00111A'a\u000fC\u0002UBqaSA\u001e\u0001\u0004\t\u0019\u0005\u0003\u0006\u0002N5A)\u0019!C\u0001\u0003\u001f\nqA\\1nKJ+g-\u0006\u0002\u0002RA\u0019A\u0006\u000b\u001f\t\u0015\u0005US\u0002#A!B\u0013\t\t&\u0001\u0005oC6,'+\u001a4!\u0011)\tI&\u0004EC\u0002\u0013\u0005\u00111L\u0001\ngfl'm\u001c7SK\u001a,\"!!\u0018\u0011\t1B\u0013q\f\t\u0004\u0019\u0005\u0005\u0014bAA2\u0005\t11+_7c_2D!\"a\u001a\u000e\u0011\u0003\u0005\u000b\u0015BA/\u0003)\u0019\u00180\u001c2pYJ+g\r\t\u0005\u000b\u0003Wj\u0001R1A\u0005\u0002\u00055\u0014a\u0002;za\u0016\u0014VMZ\u000b\u0003\u0003_\u0002B\u0001\f\u0015\u0002rA\u0019A\"a\u001d\n\u0007\u0005U$A\u0001\u0003UsB,\u0007BCA=\u001b!\u0005\t\u0015)\u0003\u0002p\u0005AA/\u001f9f%\u00164\u0007\u0005C\u0005\u0002~5A)\u0019!C\u0001y\u0006Y1m\u001c8ti\u0006tGOU3g\u0011%\t\t)\u0004E\u0001B\u0003&Q0\u0001\u0007d_:\u001cH/\u00198u%\u00164\u0007\u0005C\u0005\u0002\u00066\u0011\r\u0011\"\u0001\u0002\b\u0006Q1/_7c_2LeNZ8\u0016\u0005\u0005%\u0005cB\fZ5j\u000bY\t\u0010\t\u0004\u0019\u00055\u0015bAAH\u0005\tQ1+_7c_2LeNZ8\t\u0011\u0005MU\u0002)A\u0005\u0003\u0013\u000b1b]=nE>d\u0017J\u001c4pA!9\u0011qS\u0007\u0005\u0002\u0005e\u0015!C:z[\"+\u0017\rZ3s)\ri\u00181\u0014\u0005\u0007o\u0006U\u0005\u0019A.\t\u000f\u0005}U\u0002\"\u0001\u0002\"\u0006Y1/_7c_2,e\u000e\u001e:z)\u0011\tI)a)\t\r]\fi\n1\u0001\\\u0011%\t9+\u0004b\u0001\n\u0003\tI+\u0001\u0005o_NKXNY8m+\t\tY\u000bE\u0004\u00183jS\u0016Q\u0016\u001c\u000f\u00071\ty+C\u0002\u00022\n\t\u0001BT8Ts6\u0014w\u000e\u001c\u0005\t\u0003kk\u0001\u0015!\u0003\u0002,\u0006Ian\\*z[\n|G\u000e\t\u0005\n\u0003sk!\u0019!C\u0001\u0003w\u000b!\u0002^=qKNKXNY8m+\t\tiL\u0005\u0004\u0002@\u0006\u0005\u0017\u0011\u0007\u0004\u0007\u0003W\u0001\u0001!!0\u0011\u000f]I&LWAbyA\u0019A\"!2\n\u0007\u0005\u001d'A\u0001\u0006UsB,7+_7c_2D\u0001\"a3\u000eA\u0003%\u0011QX\u0001\fif\u0004XmU=nE>d\u0007\u0005C\u0005\u0002P6\u0011\r\u0011\"\u0001\u0002R\u0006Y\u0011\r\\5bgNKXNY8m+\t\t\u0019N\u0005\u0004\u0002V\u0006]\u0017\u0011\u0007\u0004\u0007\u0003W\u0001\u0001!a5\u0011\u000f]I&LWAmyA\u0019A\"a7\n\u0007\u0005u'AA\u0006BY&\f7oU=nE>d\u0007\u0002CAq\u001b\u0001\u0006I!a5\u0002\u0019\u0005d\u0017.Y:Ts6\u0014w\u000e\u001c\u0011\t\u0013\u0005\u0015XB1A\u0005\u0002\u0005\u001d\u0018aC2mCN\u001c8+_7c_2,\"!!;\u0013\r\u0005-\u0018Q^A\u0019\r\u0019\tY\u0003\u0001\u0001\u0002jB9q#\u0017.[\u0003_d\u0004c\u0001\u0007\u0002r&\u0019\u00111\u001f\u0002\u0003\u0017\rc\u0017m]:Ts6\u0014w\u000e\u001c\u0005\t\u0003ol\u0001\u0015!\u0003\u0002j\u0006a1\r\\1tgNKXNY8mA!I\u00111`\u0007C\u0002\u0013\u0005\u0011Q`\u0001\r_\nTWm\u0019;Ts6\u0014w\u000e\\\u000b\u0003\u0003\u007f\u0014bA!\u0001\u0003\u0004\u0005EbABA\u0016\u0001\u0001\ty\u0010E\u0004\u00183jS&Q\u0001\u001f\u0011\u00071\u00119!C\u0002\u0003\n\t\u0011Ab\u00142kK\u000e$8+_7c_2D\u0001B!\u0004\u000eA\u0003%\u0011q`\u0001\u000e_\nTWm\u0019;Ts6\u0014w\u000e\u001c\u0011\t\u0013\tEQB1A\u0005\u0002\tM\u0011\u0001D7fi\"|GmU=nE>dWC\u0001B\u000b%\u0019\u00119B!\u0007\u00022\u00191\u00111\u0006\u0001\u0001\u0005+\u0001raF-[5\nmA\bE\u0002\r\u0005;I1Aa\b\u0003\u00051iU\r\u001e5pINKXNY8m\u0011!\u0011\u0019#\u0004Q\u0001\n\tU\u0011!D7fi\"|GmU=nE>d\u0007\u0005C\u0005\u0003(5\u0011\r\u0011\"\u0001\u0003*\u00051Q\r\u001f;SK\u001a,\"Aa\u000b\u0013\r\t5\"qFA\u0019\r\u0019\tY\u0003\u0001\u0001\u0003,A9q#\u0017.[\u0005ca\u0004c\u0001\u0007\u00034%\u0019!Q\u0007\u0002\u0003\u001d\u0015CH/\u001a:oC2\u001c\u00160\u001c2pY\"A!\u0011H\u0007!\u0002\u0013\u0011Y#A\u0004fqR\u0014VM\u001a\u0011\t\u0013\tuRB1A\u0005\u0002\t%\u0012AD3yi6{Gm\u00117bgN\u0014VM\u001a\u0005\t\u0005\u0003j\u0001\u0015!\u0003\u0003,\u0005yQ\r\u001f;N_\u0012\u001cE.Y:t%\u00164\u0007\u0005\u0003\u0006\u0003F5A)\u0019!C\u0001\u00037\naa]=nE>d\u0007B\u0003B%\u001b!\u0005\t\u0015)\u0003\u0002^\u000591/_7c_2\u0004\u0003\"\u0003B'\u001b\t\u0007I\u0011\u0001B(\u0003-\u0019G.Y:t'fl'+\u001a4\u0016\u0005\tE\u0003\u0003\u0002\u0017)\u0003_D\u0001B!\u0016\u000eA\u0003%!\u0011K\u0001\rG2\f7o]*z[J+g\r\t\u0005\n\u00053j!\u0019!C\u0001\u0003\u000b\tQ\"\u0019;ue&\u0014GK]3f%\u00164\u0007\u0002\u0003B/\u001b\u0001\u0006I!a\u0002\u0002\u001d\u0005$HO]5c)J,WMU3gA!I!\u0011M\u0007C\u0002\u0013\u0005!1M\u0001\nif\u0004X\rT3wK2,\"A!\u001a\u0011\u0011]I&q\rB47Z\u00022!\u0014B5\u0013\t\t#\u000b\u0003\u0005\u0003n5\u0001\u000b\u0011\u0002B3\u0003)!\u0018\u0010]3MKZ,G\u000e\t\u0005\n\u0005cj!\u0019!C\u0001\u0005G\n\u0011\u0002^=qK&sG-\u001a=\t\u0011\tUT\u0002)A\u0005\u0005K\n!\u0002^=qK&sG-\u001a=!\u0011)\u0011I(\u0004EC\u0002\u0013\u0005\u0011QN\u0001\nif\u0004X-\u00128uefD!B! \u000e\u0011\u0003\u0005\u000b\u0015BA8\u0003)!\u0018\u0010]3F]R\u0014\u0018\u0010\t\u0005\n\u0005\u0003k\u0001R1A\u0005\u0002q\fq\u0001\\5uKJ\fG\u000eC\u0005\u0003\u00066A\t\u0011)Q\u0005{\u0006AA.\u001b;fe\u0006d\u0007\u0005\u0003\u0006\u0003\n6A)\u0019!C\u0001\u0005\u0017\u000bQ\"\u0019;ue&\u0014W\u000f^3J]\u001a|WC\u0001BG!\u001d9\u0012L\u0017.\u0003\u0010r\u00022\u0001\u0004BI\u0013\r\u0011\u0019J\u0001\u0002\u000e\u0003R$(/\u001b2vi\u0016LeNZ8\t\u0015\t]U\u0002#A!B\u0013\u0011i)\u0001\bbiR\u0014\u0018NY;uK&sgm\u001c\u0011\t\u0015\tmU\u0002#b\u0001\n\u0003\u0011i*\u0001\u0005dQ&dGM]3o+\t\u0011y\nE\u0004\u00183jS&\u0011\u0015\u001f\u0011\u00071\u0011\u0019+C\u0002\u0003&\n\u0011\u0001b\u00115jY\u0012\u0014XM\u001c\u0005\u000b\u0005Sk\u0001\u0012!Q!\n\t}\u0015!C2iS2$'/\u001a8!\u0011)\u0011i+\u0004EC\u0002\u0013\u0005!qV\u0001\nC:tw\u000e^%oM>,\"A!-\u0011\u000f]I&L\u0017BZyA\u0019AB!.\n\u0007\t]&AA\u0005B]:|G/\u00138g_\"Q!1X\u0007\t\u0002\u0003\u0006KA!-\u0002\u0015\u0005tgn\u001c;J]\u001a|\u0007\u0005\u0003\u0006\u0003@6A)\u0019!C\u0001\u0005\u0003\fQ\u0002^8q\u0019\u00164X\r\\\"mCN\u001cXCAAw\u0011)\u0011)-\u0004E\u0001B\u0003&\u0011Q^\u0001\u000fi>\u0004H*\u001a<fY\u000ec\u0017m]:!\u0011)\u0011I-\u0004EC\u0002\u0013\u0005!1Z\u0001\u000fi>\u0004H*\u001a<fY>\u0013'.Z2u+\t\u0011\u0019\u0001\u0003\u0006\u0003P6A\t\u0011)Q\u0005\u0005\u0007\tq\u0002^8q\u0019\u00164X\r\\(cU\u0016\u001cG\u000f\t\u0005\b\u0005'lA\u0011\u0001Bk\u0003)I7\u000fV8q\u0019\u00164X\r\u001c\u000b\u0005\u0005/\u0014i\u000eE\u0002\u0012\u00053L1Aa7\u0013\u0005\u001d\u0011un\u001c7fC:D\u0001B!\u0012\u0003R\u0002\u0007\u0011q\f\u0005\b\u0005ClA\u0011\u0001Br\u0003=I7\u000fV8q\u0019\u00164X\r\\\"mCN\u001cH\u0003\u0002Bl\u0005KD\u0001B!\u0012\u0003`\u0002\u0007\u0011q\f")
public final class ScalaSigEntryParsers
{
    public static <In, Out, A, Any> Function1<In, A> expect(final Rule<In, Out, A, Any> rule) {
        return ScalaSigEntryParsers$.MODULE$.expect(rule);
    }
    
    public static <In, Out, A, X> Rule<In, Out, A, X> oneOf(final Seq<Rule<In, Out, A, X>> rules) {
        return ScalaSigEntryParsers$.MODULE$.oneOf(rules);
    }
    
    public static <X> Rule<Object, Nothing$, Nothing$, X> error(final X err) {
        return ScalaSigEntryParsers$.MODULE$.error(err);
    }
    
    public static <In> Rule<In, Nothing$, Nothing$, In> error() {
        return ScalaSigEntryParsers$.MODULE$.error();
    }
    
    public static Rule<Object, Nothing$, Nothing$, Nothing$> failure() {
        return ScalaSigEntryParsers$.MODULE$.failure();
    }
    
    public static <Out, A> Rule<Object, Out, A, Nothing$> success(final Out out, final A a) {
        return ScalaSigEntryParsers$.MODULE$.success(out, a);
    }
    
    public static <s> Object state() {
        return ScalaSigEntryParsers$.MODULE$.state();
    }
    
    public static <In> Object from() {
        return ScalaSigEntryParsers$.MODULE$.from();
    }
    
    public static <In, A, X> SeqRule<In, A, X> seqRule(final Rule<In, In, A, X> rule) {
        return ScalaSigEntryParsers$.MODULE$.seqRule(rule);
    }
    
    public static <In, Out, A, X> InRule<In, Out, A, X> inRule(final Rule<In, Out, A, X> rule) {
        return ScalaSigEntryParsers$.MODULE$.inRule(rule);
    }
    
    public static <In, Out, A, X> Rule<In, Out, A, X> rule(final Function1<In, Result<Out, A, X>> f) {
        return ScalaSigEntryParsers$.MODULE$.rule(f);
    }
    
    public static <T, X> Rule<Object, Object, T, X> repeatUntil(final Rule<Object, Object, Function1<T, T>, X> rule, final Function1<T, Object> finished, final T initial) {
        return ScalaSigEntryParsers$.MODULE$.repeatUntil(rule, finished, initial);
    }
    
    public static <A, X> Rule<Object, Object, List<A>, X> anyOf(final Seq<Rule<Object, Object, A, X>> rules) {
        return ScalaSigEntryParsers$.MODULE$.anyOf(rules);
    }
    
    public static <A, X> Function1<Object, Result<Object, List<A>, X>> allOf(final Seq<Rule<Object, Object, A, X>> rules) {
        return ScalaSigEntryParsers$.MODULE$.allOf(rules);
    }
    
    public static Rule<Object, Object, Object, Nothing$> cond(final Function1<Object, Object> f) {
        return ScalaSigEntryParsers$.MODULE$.cond(f);
    }
    
    public static Rule<Object, Object, None$, Nothing$> none() {
        return ScalaSigEntryParsers$.MODULE$.none();
    }
    
    public static Rule<Object, Object, Nil$, Nothing$> nil() {
        return ScalaSigEntryParsers$.MODULE$.nil();
    }
    
    public static Rule<Object, Object, Object, Nothing$> update(final Function1<Object, Object> f) {
        return ScalaSigEntryParsers$.MODULE$.update(f);
    }
    
    public static Rule<Object, Object, Object, Nothing$> set(final Function0<Object> s) {
        return ScalaSigEntryParsers$.MODULE$.set(s);
    }
    
    public static Rule<Object, Object, Object, Nothing$> get() {
        return ScalaSigEntryParsers$.MODULE$.get();
    }
    
    public static <A> Rule<Object, Object, A, Nothing$> read(final Function1<Object, A> f) {
        return ScalaSigEntryParsers$.MODULE$.read(f);
    }
    
    public static <A> Rule<Object, Object, A, Nothing$> unit(final Function0<A> a) {
        return ScalaSigEntryParsers$.MODULE$.unit(a);
    }
    
    public static <A, X> Rule<Object, Object, A, X> apply(final Function1<Object, Result<Object, A, X>> f) {
        return ScalaSigEntryParsers$.MODULE$.apply(f);
    }
    
    public static void org$json4s$scalap$RulesWithState$_setter_$factory_$eq(final RulesWithState x$1) {
        ScalaSigEntryParsers$.MODULE$.org$json4s$scalap$RulesWithState$_setter_$factory_$eq(x$1);
    }
    
    public static RulesWithState factory() {
        return ScalaSigEntryParsers$.MODULE$.factory();
    }
    
    public static <In, Out, A, X> Rule<In, Out, A, X> ruleWithName(final String name, final Function1<In, Result<Out, A, X>> f) {
        return ScalaSigEntryParsers$.MODULE$.ruleWithName(name, f);
    }
    
    public static <In extends Memoisable, Out, A, X> Rule<In, Out, A, X> memo(final Object key, final Function0<Function1<In, Result<Out, A, X>>> toRule) {
        return ScalaSigEntryParsers$.MODULE$.memo(key, toRule);
    }
    
    public static boolean isTopLevelClass(final Symbol symbol) {
        return ScalaSigEntryParsers$.MODULE$.isTopLevelClass(symbol);
    }
    
    public static boolean isTopLevel(final Symbol symbol) {
        return ScalaSigEntryParsers$.MODULE$.isTopLevel(symbol);
    }
    
    public static Rule<ScalaSig.Entry, ScalaSig.Entry, ObjectSymbol, String> topLevelObject() {
        return ScalaSigEntryParsers$.MODULE$.topLevelObject();
    }
    
    public static Rule<ScalaSig.Entry, ScalaSig.Entry, ClassSymbol, String> topLevelClass() {
        return ScalaSigEntryParsers$.MODULE$.topLevelClass();
    }
    
    public static Rule<ScalaSig.Entry, ScalaSig.Entry, AnnotInfo, String> annotInfo() {
        return ScalaSigEntryParsers$.MODULE$.annotInfo();
    }
    
    public static Rule<ScalaSig.Entry, ScalaSig.Entry, Children, String> children() {
        return ScalaSigEntryParsers$.MODULE$.children();
    }
    
    public static Rule<ScalaSig.Entry, ScalaSig.Entry, AttributeInfo, String> attributeInfo() {
        return ScalaSigEntryParsers$.MODULE$.attributeInfo();
    }
    
    public static Rule<ScalaSig.Entry, ScalaSig.Entry, Object, String> literal() {
        return ScalaSigEntryParsers$.MODULE$.literal();
    }
    
    public static Rule<ScalaSig.Entry, ScalaSig.Entry, Type, String> typeEntry() {
        return ScalaSigEntryParsers$.MODULE$.typeEntry();
    }
    
    public static Rule<ByteCode, ByteCode, Object, Nothing$> typeIndex() {
        return ScalaSigEntryParsers$.MODULE$.typeIndex();
    }
    
    public static Rule<ByteCode, ByteCode, Object, Nothing$> typeLevel() {
        return ScalaSigEntryParsers$.MODULE$.typeLevel();
    }
    
    public static Rule<ScalaSig.Entry, ScalaSig.Entry, Object, String> attribTreeRef() {
        return ScalaSigEntryParsers$.MODULE$.attribTreeRef();
    }
    
    public static Rule<ScalaSig.Entry, ScalaSig.Entry, ClassSymbol, String> classSymRef() {
        return ScalaSigEntryParsers$.MODULE$.classSymRef();
    }
    
    public static Rule<ScalaSig.Entry, ScalaSig.Entry, Symbol, String> symbol() {
        return ScalaSigEntryParsers$.MODULE$.symbol();
    }
    
    public static Rule<ScalaSig.Entry, ScalaSig.Entry, ExternalSymbol, String> extModClassRef() {
        return ScalaSigEntryParsers$.MODULE$.extModClassRef();
    }
    
    public static Rule<ScalaSig.Entry, ScalaSig.Entry, ExternalSymbol, String> extRef() {
        return ScalaSigEntryParsers$.MODULE$.extRef();
    }
    
    public static Rule<ScalaSig.Entry, ScalaSig.Entry, MethodSymbol, String> methodSymbol() {
        return ScalaSigEntryParsers$.MODULE$.methodSymbol();
    }
    
    public static Rule<ScalaSig.Entry, ScalaSig.Entry, ObjectSymbol, String> objectSymbol() {
        return ScalaSigEntryParsers$.MODULE$.objectSymbol();
    }
    
    public static Rule<ScalaSig.Entry, ScalaSig.Entry, ClassSymbol, String> classSymbol() {
        return ScalaSigEntryParsers$.MODULE$.classSymbol();
    }
    
    public static Rule<ScalaSig.Entry, ScalaSig.Entry, AliasSymbol, String> aliasSymbol() {
        return ScalaSigEntryParsers$.MODULE$.aliasSymbol();
    }
    
    public static Rule<ScalaSig.Entry, ScalaSig.Entry, TypeSymbol, String> typeSymbol() {
        return ScalaSigEntryParsers$.MODULE$.typeSymbol();
    }
    
    public static Rule<ScalaSig.Entry, ScalaSig.Entry, NoSymbol$, Nothing$> noSymbol() {
        return ScalaSigEntryParsers$.MODULE$.noSymbol();
    }
    
    public static Rule<ScalaSig.Entry, ScalaSig.Entry, SymbolInfo, String> symbolEntry(final int key) {
        return ScalaSigEntryParsers$.MODULE$.symbolEntry(key);
    }
    
    public static Rule<ScalaSig.Entry, ScalaSig.Entry, Object, String> symHeader(final int key) {
        return ScalaSigEntryParsers$.MODULE$.symHeader(key);
    }
    
    public static Rule<ScalaSig.Entry, ScalaSig.Entry, SymbolInfo, String> symbolInfo() {
        return ScalaSigEntryParsers$.MODULE$.symbolInfo();
    }
    
    public static Rule<ScalaSig.Entry, ScalaSig.Entry, Object, String> constantRef() {
        return ScalaSigEntryParsers$.MODULE$.constantRef();
    }
    
    public static Rule<ScalaSig.Entry, ScalaSig.Entry, Type, String> typeRef() {
        return ScalaSigEntryParsers$.MODULE$.typeRef();
    }
    
    public static Rule<ScalaSig.Entry, ScalaSig.Entry, Symbol, String> symbolRef() {
        return ScalaSigEntryParsers$.MODULE$.symbolRef();
    }
    
    public static Rule<ScalaSig.Entry, ScalaSig.Entry, String, String> nameRef() {
        return ScalaSigEntryParsers$.MODULE$.nameRef();
    }
    
    public static <A> Rule<ScalaSig.Entry, ScalaSig.Entry, A, String> refTo(final Rule<ScalaSig.Entry, ScalaSig.Entry, A, String> rule) {
        return ScalaSigEntryParsers$.MODULE$.refTo(rule);
    }
    
    public static Rule<ScalaSig.Entry, ScalaSig.Entry, String, String> name() {
        return ScalaSigEntryParsers$.MODULE$.name();
    }
    
    public static Rule<ScalaSig.Entry, ScalaSig.Entry, String, String> typeName() {
        return ScalaSigEntryParsers$.MODULE$.typeName();
    }
    
    public static Rule<ScalaSig.Entry, ScalaSig.Entry, String, String> termName() {
        return ScalaSigEntryParsers$.MODULE$.termName();
    }
    
    public static Rule<ScalaSig.Entry, ScalaSig.Entry, Object, String> ref() {
        return ScalaSigEntryParsers$.MODULE$.ref();
    }
    
    public static Rule<ScalaSig.Entry, ScalaSig.Entry, Object, String> entry() {
        return ScalaSigEntryParsers$.MODULE$.entry();
    }
    
    public static Rule<ScalaSig.Entry, ScalaSig.Entry, Object, Nothing$> key() {
        return ScalaSigEntryParsers$.MODULE$.key();
    }
    
    public static Rule<ScalaSig.Entry, ScalaSig.Entry, Object, Nothing$> index() {
        return ScalaSigEntryParsers$.MODULE$.index();
    }
    
    public static Rule<ScalaSig.Entry, ScalaSig.Entry, Object, Nothing$> entryType(final int code) {
        return ScalaSigEntryParsers$.MODULE$.entryType(code);
    }
    
    public static <A> Rule<ScalaSig.Entry, ScalaSig.Entry, A, String> parseEntry(final Rule<ScalaSig.Entry, ScalaSig.Entry, A, String> parser, final int index) {
        return ScalaSigEntryParsers$.MODULE$.parseEntry(parser, index);
    }
    
    public static <A> Rule<ScalaSig.Entry, ScalaSig.Entry, Object, Nothing$> toEntry(final int index) {
        return ScalaSigEntryParsers$.MODULE$.toEntry(index);
    }
    
    public static <A> Rule<ScalaSig.Entry, ScalaSig.Entry, A, String> byteCodeEntryParser(final Rule<ByteCode, ByteCode, A, String> rule) {
        return ScalaSigEntryParsers$.MODULE$.byteCodeEntryParser(rule);
    }
}
