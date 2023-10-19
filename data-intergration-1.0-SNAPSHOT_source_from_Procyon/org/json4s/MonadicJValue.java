// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

import scala.runtime.BoxedUnit;
import scala.Some;
import scala.None$;
import scala.collection.TraversableLike;
import scala.Function0;
import scala.MatchError;
import scala.util.matching.Regex;
import java.util.Locale;
import scala.collection.mutable.StringBuilder;
import scala.collection.immutable.StringOps;
import scala.Predef$;
import scala.runtime.BoxesRunTime;
import scala.Option;
import scala.PartialFunction;
import scala.Function2;
import scala.Tuple2;
import scala.Function1;
import scala.collection.immutable.List$;
import scala.collection.immutable.$colon$colon;
import scala.collection.immutable.Nil$;
import scala.collection.immutable.List;
import scala.reflect.ScalaSignature;

@ScalaSignature(bytes = "\u0006\u0001\t\u001dc\u0001B\u0001\u0003\u0001\u001d\u0011Q\"T8oC\u0012L7M\u0013,bYV,'BA\u0002\u0005\u0003\u0019Q7o\u001c85g*\tQ!A\u0002pe\u001e\u001c\u0001a\u0005\u0002\u0001\u0011A\u0011\u0011\u0002D\u0007\u0002\u0015)\t1\"A\u0003tG\u0006d\u0017-\u0003\u0002\u000e\u0015\t1\u0011I\\=SK\u001aD\u0001b\u0004\u0001\u0003\u0002\u0003\u0006I\u0001E\u0001\u0003UZ\u0004\"!E\u000b\u000f\u0005I\u0019R\"\u0001\u0002\n\u0005Q\u0011\u0011a\u00029bG.\fw-Z\u0005\u0003-]\u0011aA\u0013,bYV,'B\u0001\u000b\u0003\u0011\u0015I\u0002\u0001\"\u0001\u001b\u0003\u0019a\u0014N\\5u}Q\u00111\u0004\b\t\u0003%\u0001AQa\u0004\rA\u0002AAQA\b\u0001\u0005\u0002}\tq\u0001\n2tY\u0006\u001c\b\u000e\u0006\u0002\u0011A!)\u0011%\ba\u0001E\u0005Qa.Y7f)>4\u0015N\u001c3\u0011\u0005\r2cBA\u0005%\u0013\t)#\"\u0001\u0004Qe\u0016$WMZ\u0005\u0003O!\u0012aa\u0015;sS:<'BA\u0013\u000b\u0011\u0019Q\u0003\u0001)C\u0005W\u0005\u0001b-\u001b8e\t&\u0014Xm\u0019;Cs:\u000bW.\u001a\u000b\u0004Y]J\u0004cA\u00175!9\u0011af\r\b\u0003_Ij\u0011\u0001\r\u0006\u0003c\u0019\ta\u0001\u0010:p_Rt\u0014\"A\u0006\n\u0005QQ\u0011BA\u001b7\u0005\u0011a\u0015n\u001d;\u000b\u0005QQ\u0001\"\u0002\u001d*\u0001\u0004a\u0013A\u0001=t\u0011\u0015Q\u0014\u00061\u0001#\u0003\u0011q\u0017-\\3\t\rq\u0002\u0001\u0015\"\u0003>\u0003)1\u0017N\u001c3ESJ,7\r\u001e\u000b\u0004Yyz\u0004\"\u0002\u001d<\u0001\u0004a\u0003\"\u0002!<\u0001\u0004\t\u0015!\u00019\u0011\t%\u0011\u0005\u0003R\u0005\u0003\u0007*\u0011\u0011BR;oGRLwN\\\u0019\u0011\u0005%)\u0015B\u0001$\u000b\u0005\u001d\u0011un\u001c7fC:DQ\u0001\u0013\u0001\u0005\u0002%\u000ba\u0002\n2tY\u0006\u001c\b\u000e\n2tY\u0006\u001c\b\u000e\u0006\u0002\u0011\u0015\")\u0011e\u0012a\u0001E!)a\u0004\u0001C\u0001\u0019V\u0011QJ\u0015\u000b\u0003\u001dv\u00032!\f\u001bP!\t\u0001\u0006\f\u0005\u0002R%2\u0001A!B*L\u0005\u0004!&!A!\u0012\u0005U\u0003\u0002CA\u0005W\u0013\t9&BA\u0004O_RD\u0017N\\4\n\u0005eS&A\u0002,bYV,7/\u0003\u0002\u00177*\u0011ALA\u0001\b\u0015N|g.Q*U\u0011\u0015q6\n1\u0001`\u0003\u0015\u0019G.\u0019>{!\r\u0019\u0003\rU\u0005\u0003C\"\u0012Qa\u00117bgNDQ\u0001\u0013\u0001\u0005\u0002\r,\"\u0001\u001a5\u0015\u0005\u0015L\u0007cA\u00175MB\u0011q\r\u0017\t\u0003#\"$Qa\u00152C\u0002QCQA\u00182A\u0002)\u00042a\t1h\u0011\u0015a\u0007\u0001\"\u0003n\u00035!\u0018\u0010]3Qe\u0016$\u0017nY1uKV\u0011a.\u001e\u000b\u0003_J$\"\u0001\u00129\t\u000bE\\\u0007\u0019\u0001\t\u0002\t)\u001cxN\u001c\u0005\u0006=.\u0004\ra\u001d\t\u0004G\u0001$\bCA)v\t\u0015\u00196N1\u0001U\u0011\u00159\b\u0001\"\u0001y\u0003\u00111w\u000e\u001c3\u0016\u0005edHc\u0001>\u0002\u000eQ\u001910a\u0001\u0011\u0005EcH!B*w\u0005\u0004i\u0018CA+\u007f!\tIq0C\u0002\u0002\u0002)\u00111!\u00118z\u0011\u001d\t)A\u001ea\u0001\u0003\u000f\t\u0011A\u001a\t\u0007\u0013\u0005%1\u0010E>\n\u0007\u0005-!BA\u0005Gk:\u001cG/[8oe!1\u0011q\u0002<A\u0002m\f\u0011A\u001f\u0005\b\u0003'\u0001A\u0011AA\u000b\u0003%1w\u000e\u001c3GS\u0016dG-\u0006\u0003\u0002\u0018\u0005uA\u0003BA\r\u0003S!B!a\u0007\u0002 A\u0019\u0011+!\b\u0005\rM\u000b\tB1\u0001~\u0011!\t)!!\u0005A\u0002\u0005\u0005\u0002#C\u0005\u0002\n\u0005m\u00111EA\u000e!\r\t\u0012QE\u0005\u0004\u0003O9\"A\u0002&GS\u0016dG\r\u0003\u0005\u0002\u0010\u0005E\u0001\u0019AA\u000e\u0011\u001d\ti\u0003\u0001C\u0001\u0003_\t1!\\1q)\r\u0001\u0012\u0011\u0007\u0005\t\u0003\u000b\tY\u00031\u0001\u00024A!\u0011B\u0011\t\u0011\u0011\u001d\t9\u0004\u0001C\u0001\u0003s\t\u0001\"\\1q\r&,G\u000e\u001a\u000b\u0004!\u0005m\u0002\u0002CA\u0003\u0003k\u0001\r!!\u0010\u0011\r%\u0011\u00151EA\u0012\u0011\u001d\t\t\u0005\u0001C\u0001\u0003\u0007\na\u0002\u001e:b]N4wN]7GS\u0016dG\rF\u0002\u0011\u0003\u000bB\u0001\"!\u0002\u0002@\u0001\u0007\u0011q\t\t\b\u0013\u0005%\u00131EA\u0012\u0013\r\tYE\u0003\u0002\u0010!\u0006\u0014H/[1m\rVt7\r^5p]\"9\u0011q\n\u0001\u0005\u0002\u0005E\u0013!\u0003;sC:\u001chm\u001c:n)\r\u0001\u00121\u000b\u0005\t\u0003\u000b\ti\u00051\u0001\u0002VA)\u0011\"!\u0013\u0011!!9\u0011\u0011\f\u0001\u0005\u0002\u0005m\u0013a\u0002:fa2\f7-\u001a\u000b\u0006!\u0005u\u00131\r\u0005\t\u0003?\n9\u00061\u0001\u0002b\u0005\tA\u000eE\u0002.i\tBq!!\u001a\u0002X\u0001\u0007\u0001#A\u0006sKBd\u0017mY3nK:$\bbBA5\u0001\u0011\u0005\u00111N\u0001\nM&tGMR5fY\u0012$B!!\u001c\u0002tA)\u0011\"a\u001c\u0002$%\u0019\u0011\u0011\u000f\u0006\u0003\r=\u0003H/[8o\u0011\u001d\u0001\u0015q\ra\u0001\u0003k\u0002R!\u0003\"\u0002$\u0011Cq!!\u001f\u0001\t\u0003\tY(\u0001\u0003gS:$G\u0003BA?\u0003\u007f\u0002B!CA8!!1\u0001)a\u001eA\u0002\u0005Cq!a!\u0001\t\u0003\t))A\u0006gS2$XM\u001d$jK2$G\u0003BAD\u0003\u0013\u0003B!\f\u001b\u0002$!9\u0001)!!A\u0002\u0005U\u0004bBAG\u0001\u0011\u0005\u0011qR\u0001\u0007M&dG/\u001a:\u0015\u00071\n\t\n\u0003\u0004A\u0003\u0017\u0003\r!\u0011\u0005\b\u0003+\u0003A\u0011AAL\u0003)9\u0018\u000e\u001e5GS2$XM\u001d\u000b\u0005\u00033\u000bi\u000f\u0005\u0003\u0002\u001c\u0006uU\"\u0001\u0001\u0007\r\u0005}\u0005\u0001AAQ\u0005AQe+\u00197vK^KG\u000f\u001b$jYR,'oE\u0002\u0002\u001e\"A!\"!*\u0002\u001e\n\u0005\t\u0015!\u0003\u0011\u0003\u0011\u0019X\r\u001c4\t\u0013\u0001\u000biJ!A!\u0002\u0013\t\u0005bB\r\u0002\u001e\u0012\u0005\u00111\u0016\u000b\u0007\u00033\u000bi+a,\t\u000f\u0005\u0015\u0016\u0011\u0016a\u0001!!1\u0001)!+A\u0002\u0005C\u0001\"!\f\u0002\u001e\u0012\u0005\u00111W\u000b\u0005\u0003k\u000bY\f\u0006\u0003\u00028\u0006}\u0006\u0003B\u00175\u0003s\u00032!UA^\t\u001d\ti,!-C\u0002u\u0014\u0011\u0001\u0016\u0005\t\u0003\u000b\t\t\f1\u0001\u0002BB)\u0011B\u0011\t\u0002:\"A\u0011QYAO\t\u0003\t9-A\u0004gY\u0006$X*\u00199\u0016\t\u0005%\u0017q\u001a\u000b\u0005\u0003\u0017\f\t\u000e\u0005\u0003.i\u00055\u0007cA)\u0002P\u00129\u0011QXAb\u0005\u0004i\b\u0002CA\u0003\u0003\u0007\u0004\r!a5\u0011\u000b%\u0011\u0005#a3\t\u0011\u0005]\u0017Q\u0014C\u0001\u00033\fqAZ8sK\u0006\u001c\u0007\u000e\u0006\u0003\u0002\\\u0006\u0005\bcA\u0005\u0002^&\u0019\u0011q\u001c\u0006\u0003\tUs\u0017\u000e\u001e\u0005\t\u0003\u000b\t)\u000e1\u0001\u0002dB)\u0011B\u0011\t\u0002\\\"A\u0011QSAO\t\u0003\t9\u000f\u0006\u0003\u0002\u001a\u0006%\bbBAv\u0003K\u0004\r!Q\u0001\u0002c\"1\u0001)a%A\u0002\u0005Cq!!=\u0001\t\u0003\t\u00190A\u0006sK6|g/\u001a$jK2$Gc\u0001\t\u0002v\"9\u0001)a<A\u0002\u0005U\u0004bBA}\u0001\u0011\u0005\u00111`\u0001\u0007e\u0016lwN^3\u0015\u0007A\ti\u0010\u0003\u0004A\u0003o\u0004\r!\u0011\u0005\t\u0005\u0003\u0001\u0001\u0015\"\u0003\u0003\u0004\u0005A1-Y7fY&TX\rF\u0002#\u0005\u000bAqAa\u0002\u0002\u0000\u0002\u0007!%\u0001\u0003x_J$\u0007\u0002\u0003B\u0006\u0001\u0001&IA!\u0004\u0002\u0013A\f7oY1mSj,Gc\u0001\u0012\u0003\u0010!9!q\u0001B\u0005\u0001\u0004\u0011\u0003\u0002\u0003B\n\u0001\u0001&IA!\u0006\u00021UtG-\u001a:tG>\u0014XmQ1nK2\u001c\u0015m]3t\u001f:d\u0017\u0010F\u0002#\u0005/AqAa\u0002\u0003\u0012\u0001\u0007!\u0005\u0003\u0005\u0003\u001c\u0001\u0001K\u0011\u0002B\u000f\u0003))h\u000eZ3sg\u000e|'/\u001a\u000b\u0004E\t}\u0001b\u0002B\u0004\u00053\u0001\rA\t\u0005\b\u0005G\u0001A\u0011\u0001B\u0013\u00031\u0019\u0017-\\3mSj,7*Z=t+\u0005\u0001\u0002b\u0002B\u0015\u0001\u0011\u0005!QE\u0001\u000ea\u0006\u001c8-\u00197ju\u0016\\U-_:\t\u000f\t5\u0002\u0001\"\u0001\u0003&\u0005Y1O\\1lSj,7*Z=t\u0011\u001d\u0011\t\u0004\u0001C\u0001\u0005K\t1$\u001e8eKJ\u001c8m\u001c:f\u0007\u0006lW\r\\\"bg\u0016\\U-_:P]2L\bb\u0002B\u001b\u0001\u0011\u0005!QE\u0001\u000fk:$WM]:d_J,7*Z=t\u0011!\u0011I\u0004\u0001Q\u0005\n\tm\u0012A\u0004:foJLG/\u001a&t_:\f5\u000b\u0016\u000b\u0004!\tu\u0002\u0002\u0003B \u0005o\u0001\rA!\u0011\u0002!-,\u0017pQ1tKR\u0013\u0018M\\:g_Jl\u0007\u0003B\u0005CE\tBqA!\u0012\u0001\t\u0003\u0011)#A\u0004o_:+H\u000e\\:")
public class MonadicJValue
{
    private final JsonAST.JValue jv;
    
    public JsonAST.JValue $bslash(final String nameToFind) {
        final JsonAST.JValue jv = this.jv;
        JsonAST.JArray apply;
        if (jv instanceof JsonAST.JArray) {
            final List xs = ((JsonAST.JArray)jv).arr();
            apply = package$.MODULE$.JArray().apply(this.org$json4s$MonadicJValue$$findDirectByName((List<JsonAST.JValue>)xs, nameToFind));
        }
        else {
            final List<JsonAST.JValue> org$json4s$MonadicJValue$$findDirectByName = this.org$json4s$MonadicJValue$$findDirectByName((List<JsonAST.JValue>)Nil$.MODULE$.$colon$colon((Object)this.jv), nameToFind);
            JsonAST.JValue value = null;
            Label_0150: {
                if (Nil$.MODULE$.equals(org$json4s$MonadicJValue$$findDirectByName)) {
                    value = package$.MODULE$.JNothing();
                }
                else {
                    if (org$json4s$MonadicJValue$$findDirectByName instanceof $colon$colon) {
                        final $colon$colon $colon$colon = ($colon$colon)org$json4s$MonadicJValue$$findDirectByName;
                        final JsonAST.JValue x = (JsonAST.JValue)$colon$colon.head();
                        if (Nil$.MODULE$.equals($colon$colon.tl$1())) {
                            value = x;
                            break Label_0150;
                        }
                    }
                    value = package$.MODULE$.JArray().apply(org$json4s$MonadicJValue$$findDirectByName);
                }
            }
            apply = (JsonAST.JArray)value;
        }
        return apply;
    }
    
    public List<JsonAST.JValue> org$json4s$MonadicJValue$$findDirectByName(final List<JsonAST.JValue> xs, final String name) {
        return (List<JsonAST.JValue>)xs.flatMap((Function1)new MonadicJValue$$anonfun$org$json4s$MonadicJValue$$findDirectByName.MonadicJValue$$anonfun$org$json4s$MonadicJValue$$findDirectByName$1(this, name), List$.MODULE$.canBuildFrom());
    }
    
    public List<JsonAST.JValue> org$json4s$MonadicJValue$$findDirect(final List<JsonAST.JValue> xs, final Function1<JsonAST.JValue, Object> p) {
        return (List<JsonAST.JValue>)xs.flatMap((Function1)new MonadicJValue$$anonfun$org$json4s$MonadicJValue$$findDirect.MonadicJValue$$anonfun$org$json4s$MonadicJValue$$findDirect$1(this, (Function1)p), List$.MODULE$.canBuildFrom());
    }
    
    public JsonAST.JValue $bslash$bslash(final String nameToFind) {
        final List org$json4s$MonadicJValue$$find$1 = this.org$json4s$MonadicJValue$$find$1(this.jv, nameToFind);
        if (org$json4s$MonadicJValue$$find$1 instanceof $colon$colon) {
            final $colon$colon $colon$colon = ($colon$colon)org$json4s$MonadicJValue$$find$1;
            final Tuple2 tuple2 = (Tuple2)$colon$colon.head();
            final List tl$1 = $colon$colon.tl$1();
            if (tuple2 != null) {
                final JsonAST.JValue x = (JsonAST.JValue)tuple2._2();
                if (Nil$.MODULE$.equals(tl$1)) {
                    return x;
                }
            }
        }
        return package$.MODULE$.JObject().apply((List<Tuple2<String, JsonAST.JValue>>)org$json4s$MonadicJValue$$find$1);
    }
    
    public <A extends JsonAST.JValue> List<Object> $bslash(final Class<A> clazz) {
        return (List<Object>)this.org$json4s$MonadicJValue$$findDirect(this.jv.children(), (Function1<JsonAST.JValue, Object>)new MonadicJValue$$anonfun$$bslash.MonadicJValue$$anonfun$$bslash$1(this, (Class)clazz)).map((Function1)new MonadicJValue$$anonfun$$bslash.MonadicJValue$$anonfun$$bslash$2(this), List$.MODULE$.canBuildFrom());
    }
    
    public <A extends JsonAST.JValue> List<Object> $bslash$bslash(final Class<A> clazz) {
        return (List<Object>)package$.MODULE$.jvalue2monadic(this.jv).filter((Function1<JsonAST.JValue, Object>)new MonadicJValue$$anonfun$$bslash$bslash.MonadicJValue$$anonfun$$bslash$bslash$1(this, (Class)clazz)).map((Function1)new MonadicJValue$$anonfun$$bslash$bslash.MonadicJValue$$anonfun$$bslash$bslash$2(this), List$.MODULE$.canBuildFrom());
    }
    
    public <A extends JsonAST.JValue> boolean org$json4s$MonadicJValue$$typePredicate(final Class<A> clazz, final JsonAST.JValue json) {
        final Class<? extends JsonAST.JValue> class1 = json.getClass();
        if (class1 == null) {
            if (clazz != null) {
                return false;
            }
        }
        else if (!class1.equals(clazz)) {
            return false;
        }
        return true;
        b = false;
        return b;
    }
    
    public <A> A fold(final A z, final Function2<A, JsonAST.JValue, A> f) {
        return (A)this.rec$1(z, this.jv, f);
    }
    
    public <A> A foldField(final A z, final Function2<A, Tuple2<String, JsonAST.JValue>, A> f) {
        return (A)this.rec$2(z, this.jv, f);
    }
    
    public JsonAST.JValue map(final Function1<JsonAST.JValue, JsonAST.JValue> f) {
        return this.org$json4s$MonadicJValue$$rec$3(this.jv, f);
    }
    
    public JsonAST.JValue mapField(final Function1<Tuple2<String, JsonAST.JValue>, Tuple2<String, JsonAST.JValue>> f) {
        return this.org$json4s$MonadicJValue$$rec$4(this.jv, f);
    }
    
    public JsonAST.JValue transformField(final PartialFunction<Tuple2<String, JsonAST.JValue>, Tuple2<String, JsonAST.JValue>> f) {
        return this.mapField((Function1<Tuple2<String, JsonAST.JValue>, Tuple2<String, JsonAST.JValue>>)new MonadicJValue$$anonfun$transformField.MonadicJValue$$anonfun$transformField$1(this, (PartialFunction)f));
    }
    
    public JsonAST.JValue transform(final PartialFunction<JsonAST.JValue, JsonAST.JValue> f) {
        return this.map((Function1<JsonAST.JValue, JsonAST.JValue>)new MonadicJValue$$anonfun$transform.MonadicJValue$$anonfun$transform$1(this, (PartialFunction)f));
    }
    
    public JsonAST.JValue replace(final List<String> l, final JsonAST.JValue replacement) {
        return this.org$json4s$MonadicJValue$$rep$1(l, this.jv, replacement);
    }
    
    public Option<Tuple2<String, JsonAST.JValue>> findField(final Function1<Tuple2<String, JsonAST.JValue>, Object> p) {
        return (Option<Tuple2<String, JsonAST.JValue>>)this.org$json4s$MonadicJValue$$find$2(this.jv, p);
    }
    
    public Option<JsonAST.JValue> find(final Function1<JsonAST.JValue, Object> p) {
        return (Option<JsonAST.JValue>)this.org$json4s$MonadicJValue$$find$3(this.jv, p);
    }
    
    public List<Tuple2<String, JsonAST.JValue>> filterField(final Function1<Tuple2<String, JsonAST.JValue>, Object> p) {
        return (List<Tuple2<String, JsonAST.JValue>>)this.foldField((List)Nil$.MODULE$, (scala.Function2<List, Tuple2<String, JsonAST.JValue>, List>)new MonadicJValue$$anonfun$filterField.MonadicJValue$$anonfun$filterField$1(this, (Function1)p)).reverse();
    }
    
    public List<JsonAST.JValue> filter(final Function1<JsonAST.JValue, Object> p) {
        return (List<JsonAST.JValue>)this.fold((List)Nil$.MODULE$, (scala.Function2<List, JsonAST.JValue, List>)new MonadicJValue$$anonfun$filter.MonadicJValue$$anonfun$filter$1(this, (Function1)p)).reverse();
    }
    
    public JValueWithFilter withFilter(final Function1<JsonAST.JValue, Object> p) {
        return new JValueWithFilter(this.jv, p);
    }
    
    public JsonAST.JValue removeField(final Function1<Tuple2<String, JsonAST.JValue>, Object> p) {
        return package$.MODULE$.jvalue2monadic(this.jv).transform((PartialFunction<JsonAST.JValue, JsonAST.JValue>)new MonadicJValue$$anonfun$removeField.MonadicJValue$$anonfun$removeField$1(this, (Function1)p));
    }
    
    public JsonAST.JValue remove(final Function1<JsonAST.JValue, Object> p) {
        return BoxesRunTime.unboxToBoolean(p.apply((Object)this.jv)) ? package$.MODULE$.JNothing() : package$.MODULE$.jvalue2monadic(this.jv).transform((PartialFunction<JsonAST.JValue, JsonAST.JValue>)new MonadicJValue$$anonfun$remove.MonadicJValue$$anonfun$remove$1(this, (Function1)p));
    }
    
    public String org$json4s$MonadicJValue$$camelize(final String word) {
        String string;
        if (new StringOps(Predef$.MODULE$.augmentString(word)).nonEmpty()) {
            final String w = this.org$json4s$MonadicJValue$$pascalize(word);
            string = new StringBuilder().append((Object)w.substring(0, 1).toLowerCase(Locale.ENGLISH)).append((Object)w.substring(1)).toString();
        }
        else {
            string = word;
        }
        return string;
    }
    
    public String org$json4s$MonadicJValue$$pascalize(final String word) {
        final List lst = Predef$.MODULE$.refArrayOps((Object[])Predef$.MODULE$.refArrayOps((Object[])word.split("_")).filterNot((Function1)new MonadicJValue$$anonfun.MonadicJValue$$anonfun$1(this))).toList();
        return ((List)((List)lst.tail()).map((Function1)new MonadicJValue$$anonfun$org$json4s$MonadicJValue$$pascalize.MonadicJValue$$anonfun$org$json4s$MonadicJValue$$pascalize$1(this), List$.MODULE$.canBuildFrom())).$colon$colon((Object)lst.headOption().map((Function1)new MonadicJValue$$anonfun.MonadicJValue$$anonfun$2(this)).get()).mkString("");
    }
    
    public String org$json4s$MonadicJValue$$underscoreCamelCasesOnly(final String word) {
        final Regex firstPattern = new StringOps(Predef$.MODULE$.augmentString("([A-Z]+)([A-Z][a-z])")).r();
        final Regex secondPattern = new StringOps(Predef$.MODULE$.augmentString("([a-z\\d])([A-Z])")).r();
        final String replacementPattern = "$1_$2";
        return secondPattern.replaceAllIn((CharSequence)firstPattern.replaceAllIn((CharSequence)word, replacementPattern), replacementPattern).toLowerCase();
    }
    
    public String org$json4s$MonadicJValue$$underscore(final String word) {
        final Regex spacesPattern = new StringOps(Predef$.MODULE$.augmentString("[-\\s]")).r();
        return spacesPattern.replaceAllIn((CharSequence)this.org$json4s$MonadicJValue$$underscoreCamelCasesOnly(word), "_");
    }
    
    public JsonAST.JValue camelizeKeys() {
        return this.rewriteJsonAST((Function1<String, String>)new MonadicJValue$$anonfun$camelizeKeys.MonadicJValue$$anonfun$camelizeKeys$1(this));
    }
    
    public JsonAST.JValue pascalizeKeys() {
        return this.rewriteJsonAST((Function1<String, String>)new MonadicJValue$$anonfun$pascalizeKeys.MonadicJValue$$anonfun$pascalizeKeys$1(this));
    }
    
    public JsonAST.JValue snakizeKeys() {
        return this.rewriteJsonAST((Function1<String, String>)new MonadicJValue$$anonfun$snakizeKeys.MonadicJValue$$anonfun$snakizeKeys$1(this));
    }
    
    public JsonAST.JValue underscoreCamelCaseKeysOnly() {
        return this.rewriteJsonAST((Function1<String, String>)new MonadicJValue$$anonfun$underscoreCamelCaseKeysOnly.MonadicJValue$$anonfun$underscoreCamelCaseKeysOnly$1(this));
    }
    
    public JsonAST.JValue underscoreKeys() {
        return this.snakizeKeys();
    }
    
    private JsonAST.JValue rewriteJsonAST(final Function1<String, String> keyCaseTransform) {
        return this.transformField((PartialFunction<Tuple2<String, JsonAST.JValue>, Tuple2<String, JsonAST.JValue>>)new MonadicJValue$$anonfun$rewriteJsonAST.MonadicJValue$$anonfun$rewriteJsonAST$1(this, (Function1)keyCaseTransform));
    }
    
    public JsonAST.JValue noNulls() {
        return this.remove((Function1<JsonAST.JValue, Object>)new MonadicJValue$$anonfun$noNulls.MonadicJValue$$anonfun$noNulls$1(this));
    }
    
    public final List org$json4s$MonadicJValue$$find$1(final JsonAST.JValue json, final String nameToFind$1) {
        Object module$;
        if (json instanceof JsonAST.JObject) {
            final List l = ((JsonAST.JObject)json).obj();
            module$ = l.foldLeft((Object)Nil$.MODULE$, (Function2)new MonadicJValue$$anonfun$org$json4s$MonadicJValue$$find$1.MonadicJValue$$anonfun$org$json4s$MonadicJValue$$find$1$1(this, nameToFind$1));
        }
        else if (json instanceof JsonAST.JArray) {
            final List i = ((JsonAST.JArray)json).arr();
            module$ = i.foldLeft((Object)Nil$.MODULE$, (Function2)new MonadicJValue$$anonfun$org$json4s$MonadicJValue$$find$1.MonadicJValue$$anonfun$org$json4s$MonadicJValue$$find$1$2(this, nameToFind$1));
        }
        else {
            module$ = Nil$.MODULE$;
        }
        return (List)module$;
    }
    
    private final Object rec$1(final Object acc, final JsonAST.JValue v, final Function2 f$1) {
        final Object newAcc = f$1.apply(acc, (Object)v);
        Object o;
        if (v instanceof JsonAST.JObject) {
            final List l = ((JsonAST.JObject)v).obj();
            o = l.foldLeft(newAcc, (Function2)new MonadicJValue$$anonfun$rec$1.MonadicJValue$$anonfun$rec$1$1(this, f$1));
        }
        else if (v instanceof JsonAST.JArray) {
            final List i = ((JsonAST.JArray)v).arr();
            o = i.foldLeft(newAcc, (Function2)new MonadicJValue$$anonfun$rec$1.MonadicJValue$$anonfun$rec$1$2(this, f$1));
        }
        else {
            o = newAcc;
        }
        return o;
    }
    
    private final Object rec$2(final Object acc, final JsonAST.JValue v, final Function2 f$2) {
        Object o;
        if (v instanceof JsonAST.JObject) {
            final List l = ((JsonAST.JObject)v).obj();
            o = l.foldLeft(acc, (Function2)new MonadicJValue$$anonfun$rec$2.MonadicJValue$$anonfun$rec$2$1(this, f$2));
        }
        else if (v instanceof JsonAST.JArray) {
            final List i = ((JsonAST.JArray)v).arr();
            o = i.foldLeft(acc, (Function2)new MonadicJValue$$anonfun$rec$2.MonadicJValue$$anonfun$rec$2$2(this, f$2));
        }
        else {
            o = acc;
        }
        return o;
    }
    
    public final JsonAST.JValue org$json4s$MonadicJValue$$rec$3(final JsonAST.JValue v, final Function1 f$3) {
        JsonAST.JValue value;
        if (v instanceof JsonAST.JObject) {
            final List l = ((JsonAST.JObject)v).obj();
            value = (JsonAST.JValue)f$3.apply((Object)package$.MODULE$.JObject().apply((List<Tuple2<String, JsonAST.JValue>>)l.map((Function1)new MonadicJValue$$anonfun$org$json4s$MonadicJValue$$rec$3.MonadicJValue$$anonfun$org$json4s$MonadicJValue$$rec$3$1(this, f$3), List$.MODULE$.canBuildFrom())));
        }
        else if (v instanceof JsonAST.JArray) {
            final List i = ((JsonAST.JArray)v).arr();
            value = (JsonAST.JValue)f$3.apply((Object)package$.MODULE$.JArray().apply((List<JsonAST.JValue>)i.map((Function1)new MonadicJValue$$anonfun$org$json4s$MonadicJValue$$rec$3.MonadicJValue$$anonfun$org$json4s$MonadicJValue$$rec$3$2(this, f$3), List$.MODULE$.canBuildFrom())));
        }
        else {
            value = (JsonAST.JValue)f$3.apply((Object)v);
        }
        return value;
    }
    
    public final JsonAST.JValue org$json4s$MonadicJValue$$rec$4(final JsonAST.JValue v, final Function1 f$4) {
        JsonAST.JValue value;
        if (v instanceof JsonAST.JObject) {
            final List l = ((JsonAST.JObject)v).obj();
            value = package$.MODULE$.JObject().apply((List<Tuple2<String, JsonAST.JValue>>)l.map((Function1)new MonadicJValue$$anonfun$org$json4s$MonadicJValue$$rec$4.MonadicJValue$$anonfun$org$json4s$MonadicJValue$$rec$4$1(this, f$4), List$.MODULE$.canBuildFrom()));
        }
        else if (v instanceof JsonAST.JArray) {
            final List i = ((JsonAST.JArray)v).arr();
            value = package$.MODULE$.JArray().apply((List<JsonAST.JValue>)i.map((Function1)new MonadicJValue$$anonfun$org$json4s$MonadicJValue$$rec$4.MonadicJValue$$anonfun$org$json4s$MonadicJValue$$rec$4$2(this, f$4), List$.MODULE$.canBuildFrom()));
        }
        else {
            value = v;
        }
        return value;
    }
    
    public final JsonAST.JValue org$json4s$MonadicJValue$$rep$1(final List l, final JsonAST.JValue in, final JsonAST.JValue replacement$1) {
        JsonAST.JValue value;
        if (l instanceof $colon$colon) {
            final $colon$colon $colon$colon = ($colon$colon)l;
            final String x = (String)$colon$colon.head();
            final List xs = $colon$colon.tl$1();
            JsonAST.JValue apply;
            if (in instanceof JsonAST.JObject) {
                final List fields = ((JsonAST.JObject)in).obj();
                apply = package$.MODULE$.JObject().apply((List<Tuple2<String, JsonAST.JValue>>)fields.map((Function1)new MonadicJValue$$anonfun$org$json4s$MonadicJValue$$rep$1.MonadicJValue$$anonfun$org$json4s$MonadicJValue$$rep$1$1(this, replacement$1, x, xs), List$.MODULE$.canBuildFrom()));
            }
            else {
                apply = in;
            }
            value = apply;
        }
        else {
            if (!Nil$.MODULE$.equals(l)) {
                throw new MatchError((Object)l);
            }
            value = in;
        }
        return value;
    }
    
    public final Option org$json4s$MonadicJValue$$find$2(final JsonAST.JValue json, final Function1 p$3) {
        Object o;
        if (json instanceof JsonAST.JObject) {
            final List fs = ((JsonAST.JObject)json).obj();
            o = fs.find(p$3).orElse((Function0)new MonadicJValue$$anonfun$org$json4s$MonadicJValue$$find$2.MonadicJValue$$anonfun$org$json4s$MonadicJValue$$find$2$1(this, p$3, fs));
        }
        else if (json instanceof JsonAST.JArray) {
            final List l = ((JsonAST.JArray)json).arr();
            o = ((TraversableLike)l.flatMap((Function1)new MonadicJValue$$anonfun$org$json4s$MonadicJValue$$find$2.MonadicJValue$$anonfun$org$json4s$MonadicJValue$$find$2$2(this, p$3), List$.MODULE$.canBuildFrom())).headOption();
        }
        else {
            o = None$.MODULE$;
        }
        return (Option)o;
    }
    
    public final Option org$json4s$MonadicJValue$$find$3(final JsonAST.JValue json, final Function1 p$4) {
        if (BoxesRunTime.unboxToBoolean(p$4.apply((Object)json))) {
            return (Option)new Some((Object)json);
        }
        Object o;
        if (json instanceof JsonAST.JObject) {
            final List fs = ((JsonAST.JObject)json).obj();
            o = ((TraversableLike)fs.flatMap((Function1)new MonadicJValue$$anonfun$org$json4s$MonadicJValue$$find$3.MonadicJValue$$anonfun$org$json4s$MonadicJValue$$find$3$1(this, p$4), List$.MODULE$.canBuildFrom())).headOption();
        }
        else if (json instanceof JsonAST.JArray) {
            final List l = ((JsonAST.JArray)json).arr();
            o = ((TraversableLike)l.flatMap((Function1)new MonadicJValue$$anonfun$org$json4s$MonadicJValue$$find$3.MonadicJValue$$anonfun$org$json4s$MonadicJValue$$find$3$2(this, p$4), List$.MODULE$.canBuildFrom())).headOption();
        }
        else {
            o = None$.MODULE$;
        }
        return (Option)o;
    }
    
    public MonadicJValue(final JsonAST.JValue jv) {
        this.jv = jv;
    }
    
    public class JValueWithFilter
    {
        private final JsonAST.JValue self;
        public final Function1<JsonAST.JValue, Object> org$json4s$MonadicJValue$JValueWithFilter$$p;
        
        public <T> List<T> map(final Function1<JsonAST.JValue, T> f) {
            return (List<T>)package$.MODULE$.jvalue2monadic(this.self).filter(this.org$json4s$MonadicJValue$JValueWithFilter$$p).map((Function1)f, List$.MODULE$.canBuildFrom());
        }
        
        public <T> List<T> flatMap(final Function1<JsonAST.JValue, List<T>> f) {
            return (List<T>)package$.MODULE$.jvalue2monadic(this.self).filter(this.org$json4s$MonadicJValue$JValueWithFilter$$p).flatMap((Function1)f, List$.MODULE$.canBuildFrom());
        }
        
        public void foreach(final Function1<JsonAST.JValue, BoxedUnit> f) {
            package$.MODULE$.jvalue2monadic(this.self).filter(this.org$json4s$MonadicJValue$JValueWithFilter$$p).foreach((Function1)f);
        }
        
        public JValueWithFilter withFilter(final Function1<JsonAST.JValue, Object> q) {
            return this.org$json4s$MonadicJValue$JValueWithFilter$$$outer().new JValueWithFilter(this.self, (Function1<JsonAST.JValue, Object>)new MonadicJValue$JValueWithFilter$$anonfun$withFilter.MonadicJValue$JValueWithFilter$$anonfun$withFilter$1(this, (Function1)q));
        }
        
        public /* synthetic */ MonadicJValue org$json4s$MonadicJValue$JValueWithFilter$$$outer() {
            return MonadicJValue.this;
        }
        
        public JValueWithFilter(final JsonAST.JValue self, final Function1<JsonAST.JValue, Object> p) {
            this.self = self;
            this.org$json4s$MonadicJValue$JValueWithFilter$$p = p;
            if (MonadicJValue.this == null) {
                throw null;
            }
        }
    }
}
