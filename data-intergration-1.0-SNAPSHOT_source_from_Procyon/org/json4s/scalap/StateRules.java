// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.scalap;

import scala.collection.immutable.List;
import scala.collection.Seq;
import scala.None$;
import scala.collection.immutable.Nil$;
import scala.runtime.Nothing$;
import scala.Function0;
import scala.Function1;
import scala.reflect.ScalaSignature;

@ScalaSignature(bytes = "\u0006\u0001\u0005\u001deaB\u0001\u0003!\u0003\r\t!\u0003\u0002\u000b'R\fG/\u001a*vY\u0016\u001c(BA\u0002\u0005\u0003\u0019\u00198-\u00197ba*\u0011QAB\u0001\u0007UN|g\u000eN:\u000b\u0003\u001d\t1a\u001c:h\u0007\u0001\u0019\"\u0001\u0001\u0006\u0011\u0005-qQ\"\u0001\u0007\u000b\u00035\tQa]2bY\u0006L!a\u0004\u0007\u0003\r\u0005s\u0017PU3g\u0011\u0015\t\u0002\u0001\"\u0001\u0013\u0003\u0019!\u0013N\\5uIQ\t1\u0003\u0005\u0002\f)%\u0011Q\u0003\u0004\u0002\u0005+:LG\u000fB\u0003\u0018\u0001\t\u0005\u0001DA\u0001T#\tIB\u0004\u0005\u0002\f5%\u00111\u0004\u0004\u0002\b\u001d>$\b.\u001b8h!\tYQ$\u0003\u0002\u001f\u0019\t\u0019\u0011I\\=\u0006\t\u0001\u0002\u0001!\t\u0002\u0005%VdW-F\u0002#S1\u0002ba\t\u0013&K\u001dZS\"\u0001\u0002\n\u0005\u0001\u0012\u0001C\u0001\u0014\u0017\u001b\u0005\u0001\u0001C\u0001\u0015*\u0019\u0001!aAK\u0010\u0005\u0006\u0004A\"!A!\u0011\u0005!bCAB\u0017 \t\u000b\u0007\u0001DA\u0001Y\u0011\u001dy\u0003A1A\u0007\u0002A\nqAZ1di>\u0014\u00180F\u00012!\t\u0019#'\u0003\u00024\u0005\t)!+\u001e7fg\")Q\u0007\u0001C\u0001m\u0005)\u0011\r\u001d9msV\u0019qG\u000f\u001f\u0015\u0005aj\u0004CB\u0012%K\u0015J4\b\u0005\u0002)u\u0011)!\u0006\u000eb\u00011A\u0011\u0001\u0006\u0010\u0003\u0006[Q\u0012\r\u0001\u0007\u0005\u0006}Q\u0002\raP\u0001\u0002MB!1\u0002Q\u0013C\u0013\t\tEBA\u0005Gk:\u001cG/[8ocA)1eQ\u0013:w%\u0011AI\u0001\u0002\u0007%\u0016\u001cX\u000f\u001c;\t\u000b\u0019\u0003A\u0011A$\u0002\tUt\u0017\u000e^\u000b\u0003\u0011.#\"!\u0013'\u0011\r\r\"S%\n&\u001a!\tA3\nB\u0003+\u000b\n\u0007\u0001\u0004\u0003\u0004N\u000b\u0012\u0005\rAT\u0001\u0002CB\u00191b\u0014&\n\u0005Ac!\u0001\u0003\u001fcs:\fW.\u001a \t\u000bI\u0003A\u0011A*\u0002\tI,\u0017\rZ\u000b\u0003)^#\"!\u0016-\u0011\r\r\"S%\n,\u001a!\tAs\u000bB\u0003+#\n\u0007\u0001\u0004C\u0003?#\u0002\u0007\u0011\f\u0005\u0003\f\u0001\u00162\u0006\"B.\u0001\t\u0003a\u0016aA4fiV\tQ\f\u0005\u0004$I\u0015*S%\u0007\u0005\u0006?\u0002!\t\u0001Y\u0001\u0004g\u0016$HCA/b\u0011\u0019\u0011g\f\"a\u0001G\u0006\t1\u000fE\u0002\f\u001f\u0016BQ!\u001a\u0001\u0005\u0002\u0019\fa!\u001e9eCR,GCA/h\u0011\u0015qD\r1\u0001i!\u0011Y\u0001)J\u0013\t\u000b)\u0004A\u0011A6\u0002\u00079LG.F\u0001m!\u0019\u0019C%J\u0013n39\u0011an]\u0007\u0002_*\u0011\u0001/]\u0001\nS6lW\u000f^1cY\u0016T!A\u001d\u0007\u0002\u0015\r|G\u000e\\3di&|g.\u0003\u0002u_\u0006\u0019a*\u001b7\t\u000bY\u0004A\u0011A<\u0002\t9|g.Z\u000b\u0002qB11\u0005J\u0013&sfq!a\u0003>\n\u0005md\u0011\u0001\u0002(p]\u0016DQ! \u0001\u0005\u0002y\fAaY8oIR\u0011Ql \u0005\u0007}q\u0004\r!!\u0001\u0011\u000b-\u0001U%a\u0001\u0011\u0007-\t)!C\u0002\u0002\b1\u0011qAQ8pY\u0016\fg\u000eC\u0004\u0002\f\u0001!\t!!\u0004\u0002\u000b\u0005dGn\u00144\u0016\r\u0005=\u0011qFA\u001a)\u0011\t\t\"!\u000e\u0011\u000b-\u0001U%a\u0005\u0011\u000f\r\u001aU%!\u0006\u00022A1\u0011qCA\u0014\u0003[qA!!\u0007\u0002$9!\u00111DA\u0011\u001b\t\tiBC\u0002\u0002 !\ta\u0001\u0010:p_Rt\u0014\"A\u0007\n\u0007\u0005\u0015B\"A\u0004qC\u000e\\\u0017mZ3\n\t\u0005%\u00121\u0006\u0002\u0005\u0019&\u001cHOC\u0002\u0002&1\u00012\u0001KA\u0018\t\u0019Q\u0013\u0011\u0002b\u00011A\u0019\u0001&a\r\u0005\r5\nIA1\u0001\u0019\u0011!\t9$!\u0003A\u0002\u0005e\u0012!\u0002:vY\u0016\u001c\bCBA\f\u0003w\ty$\u0003\u0003\u0002>\u0005-\"aA*fcB1aeHA\u0017\u0003cAq!a\u0011\u0001\t\u0003\t)%A\u0003b]f|e-\u0006\u0004\u0002H\u0005E\u0013Q\u000b\u000b\u0005\u0003\u0013\n9\u0006\u0005\u0005$I\u0015*\u00131JA*!\u0015q\u0017QJA(\u0013\r\tIc\u001c\t\u0004Q\u0005ECA\u0002\u0016\u0002B\t\u0007\u0001\u0004E\u0002)\u0003+\"a!LA!\u0005\u0004A\u0002\u0002CA\u001c\u0003\u0003\u0002\r!!\u0017\u0011\r\u0005]\u00111HA.!\u00191s$a\u0014\u0002T!9\u0011q\f\u0001\u0005\u0002\u0005\u0005\u0014a\u0003:fa\u0016\fG/\u00168uS2,b!a\u0019\u0002n\u0005MD\u0003BA3\u0003\u007f\"B!a\u001a\u0002zQ!\u0011\u0011NA;!!\u0019C%J\u0013\u0002l\u0005E\u0004c\u0001\u0015\u0002n\u00119\u0011qNA/\u0005\u0004A\"!\u0001+\u0011\u0007!\n\u0019\b\u0002\u0004.\u0003;\u0012\r\u0001\u0007\u0005\t\u0003o\ni\u00061\u0001\u0002l\u00059\u0011N\\5uS\u0006d\u0007\u0002CA>\u0003;\u0002\r!! \u0002\u0011\u0019Lg.[:iK\u0012\u0004ba\u0003!\u0002l\u0005\r\u0001\u0002CAA\u0003;\u0002\r!a!\u0002\tI,H.\u001a\t\u0007M}\t))!\u001d\u0011\r-\u0001\u00151NA6\u0001")
public interface StateRules
{
    Rules factory();
    
     <A, X> Rule<Object, Object, A, X> apply(final Function1<Object, Result<Object, A, X>> p0);
    
     <A> Rule<Object, Object, A, Nothing$> unit(final Function0<A> p0);
    
     <A> Rule<Object, Object, A, Nothing$> read(final Function1<Object, A> p0);
    
    Rule<Object, Object, Object, Nothing$> get();
    
    Rule<Object, Object, Object, Nothing$> set(final Function0<Object> p0);
    
    Rule<Object, Object, Object, Nothing$> update(final Function1<Object, Object> p0);
    
    Rule<Object, Object, Nil$, Nothing$> nil();
    
    Rule<Object, Object, None$, Nothing$> none();
    
    Rule<Object, Object, Object, Nothing$> cond(final Function1<Object, Object> p0);
    
     <A, X> Function1<Object, Result<Object, List<A>, X>> allOf(final Seq<Rule<Object, Object, A, X>> p0);
    
     <A, X> Rule<Object, Object, List<A>, X> anyOf(final Seq<Rule<Object, Object, A, X>> p0);
    
     <T, X> Rule<Object, Object, T, X> repeatUntil(final Rule<Object, Object, Function1<T, T>, X> p0, final Function1<T, Object> p1, final T p2);
}
