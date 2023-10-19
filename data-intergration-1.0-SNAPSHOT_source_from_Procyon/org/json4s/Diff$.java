// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

import scala.Function1;
import scala.collection.immutable.$colon$colon;
import scala.collection.immutable.Nil$;
import scala.Some;
import scala.None$;
import scala.Tuple3;
import scala.Option;
import scala.math.BigDecimal;
import scala.math.BigInt;
import scala.MatchError;
import scala.collection.immutable.Set;
import scala.collection.immutable.List;
import scala.Tuple2;
import scala.Serializable;

public final class Diff$ implements Serializable
{
    public static final Diff$ MODULE$;
    
    static {
        new Diff$();
    }
    
    public Diff diff(final JsonAST.JValue val1, final JsonAST.JValue val2) {
        final Tuple2 tuple2 = new Tuple2((Object)val1, (Object)val2);
        Label_0080: {
            if (tuple2 != null) {
                final JsonAST.JValue x = (JsonAST.JValue)tuple2._1();
                final JsonAST.JValue y = (JsonAST.JValue)tuple2._2();
                final JsonAST.JValue value = x;
                final JsonAST.JValue obj = y;
                if (value == null) {
                    if (obj != null) {
                        break Label_0080;
                    }
                }
                else if (!value.equals(obj)) {
                    break Label_0080;
                }
                return new Diff(JsonAST.JNothing$.MODULE$, JsonAST.JNothing$.MODULE$, JsonAST.JNothing$.MODULE$);
            }
        }
        if (tuple2 != null) {
            final JsonAST.JValue value2 = (JsonAST.JValue)tuple2._1();
            final JsonAST.JValue value3 = (JsonAST.JValue)tuple2._2();
            if (value2 instanceof JsonAST.JObject) {
                final List xs = ((JsonAST.JObject)value2).obj();
                if (value3 instanceof JsonAST.JObject) {
                    final List ys = ((JsonAST.JObject)value3).obj();
                    return this.diffFields((List<Tuple2<String, JsonAST.JValue>>)xs, (List<Tuple2<String, JsonAST.JValue>>)ys);
                }
            }
        }
        if (tuple2 != null) {
            final JsonAST.JValue value4 = (JsonAST.JValue)tuple2._1();
            final JsonAST.JValue value5 = (JsonAST.JValue)tuple2._2();
            if (value4 instanceof JsonAST.JArray) {
                final List xs2 = ((JsonAST.JArray)value4).arr();
                if (value5 instanceof JsonAST.JArray) {
                    final List ys2 = ((JsonAST.JArray)value5).arr();
                    return this.diffVals((List<JsonAST.JValue>)xs2, (List<JsonAST.JValue>)ys2);
                }
            }
        }
        Label_0402: {
            if (tuple2 != null) {
                final JsonAST.JValue value6 = (JsonAST.JValue)tuple2._1();
                final JsonAST.JValue value7 = (JsonAST.JValue)tuple2._2();
                if (value6 instanceof JsonAST.JSet) {
                    final Set x2 = ((JsonAST.JSet)value6).set();
                    if (value7 instanceof JsonAST.JSet) {
                        final Set y2 = ((JsonAST.JSet)value7).set();
                        final JsonAST.JSet set = new JsonAST.JSet((Set<JsonAST.JValue>)x2);
                        final JsonAST.JSet obj2 = new JsonAST.JSet((Set<JsonAST.JValue>)y2);
                        if (set == null) {
                            if (obj2 == null) {
                                break Label_0402;
                            }
                        }
                        else if (set.equals(obj2)) {
                            break Label_0402;
                        }
                        return new Diff(JsonAST.JNothing$.MODULE$, new JsonAST.JSet((Set<JsonAST.JValue>)y2).difference(new JsonAST.JSet((Set<JsonAST.JValue>)x2)), new JsonAST.JSet((Set<JsonAST.JValue>)x2).difference(new JsonAST.JSet((Set<JsonAST.JValue>)y2)));
                    }
                }
            }
        }
        Label_0522: {
            if (tuple2 != null) {
                final JsonAST.JValue value8 = (JsonAST.JValue)tuple2._1();
                final JsonAST.JValue value9 = (JsonAST.JValue)tuple2._2();
                if (value8 instanceof JsonAST.JInt) {
                    final BigInt x3 = ((JsonAST.JInt)value8).num();
                    if (value9 instanceof JsonAST.JInt) {
                        final BigInt y3 = ((JsonAST.JInt)value9).num();
                        final BigInt bigInt = x3;
                        final BigInt obj3 = y3;
                        if (bigInt == null) {
                            if (obj3 == null) {
                                break Label_0522;
                            }
                        }
                        else if (bigInt.equals(obj3)) {
                            break Label_0522;
                        }
                        return new Diff(new JsonAST.JInt(y3), JsonAST.JNothing$.MODULE$, JsonAST.JNothing$.MODULE$);
                    }
                }
            }
        }
        if (tuple2 != null) {
            final JsonAST.JValue value10 = (JsonAST.JValue)tuple2._1();
            final JsonAST.JValue value11 = (JsonAST.JValue)tuple2._2();
            if (value10 instanceof JsonAST.JDouble) {
                final double x4 = ((JsonAST.JDouble)value10).num();
                if (value11 instanceof JsonAST.JDouble) {
                    final double y4 = ((JsonAST.JDouble)value11).num();
                    if (x4 != y4) {
                        return new Diff(new JsonAST.JDouble(y4), JsonAST.JNothing$.MODULE$, JsonAST.JNothing$.MODULE$);
                    }
                }
            }
        }
        Label_0743: {
            if (tuple2 != null) {
                final JsonAST.JValue value12 = (JsonAST.JValue)tuple2._1();
                final JsonAST.JValue value13 = (JsonAST.JValue)tuple2._2();
                if (value12 instanceof JsonAST.JDecimal) {
                    final BigDecimal x5 = ((JsonAST.JDecimal)value12).num();
                    if (value13 instanceof JsonAST.JDecimal) {
                        final BigDecimal y5 = ((JsonAST.JDecimal)value13).num();
                        final BigDecimal bigDecimal = x5;
                        final BigDecimal obj4 = y5;
                        if (bigDecimal == null) {
                            if (obj4 == null) {
                                break Label_0743;
                            }
                        }
                        else if (bigDecimal.equals(obj4)) {
                            break Label_0743;
                        }
                        return new Diff(new JsonAST.JDecimal(y5), JsonAST.JNothing$.MODULE$, JsonAST.JNothing$.MODULE$);
                    }
                }
            }
        }
        Label_0863: {
            if (tuple2 != null) {
                final JsonAST.JValue value14 = (JsonAST.JValue)tuple2._1();
                final JsonAST.JValue value15 = (JsonAST.JValue)tuple2._2();
                if (value14 instanceof JsonAST.JString) {
                    final String x6 = ((JsonAST.JString)value14).s();
                    if (value15 instanceof JsonAST.JString) {
                        final String y6 = ((JsonAST.JString)value15).s();
                        final String s = x6;
                        final String obj5 = y6;
                        if (s == null) {
                            if (obj5 == null) {
                                break Label_0863;
                            }
                        }
                        else if (s.equals(obj5)) {
                            break Label_0863;
                        }
                        return new Diff(new JsonAST.JString(y6), JsonAST.JNothing$.MODULE$, JsonAST.JNothing$.MODULE$);
                    }
                }
            }
        }
        if (tuple2 != null) {
            final JsonAST.JValue value16 = (JsonAST.JValue)tuple2._1();
            final JsonAST.JValue value17 = (JsonAST.JValue)tuple2._2();
            if (value16 instanceof JsonAST.JBool) {
                final boolean x7 = ((JsonAST.JBool)value16).value();
                if (value17 instanceof JsonAST.JBool) {
                    final boolean y7 = ((JsonAST.JBool)value17).value();
                    if (x7 != y7) {
                        return new Diff(new JsonAST.JBool(y7), JsonAST.JNothing$.MODULE$, JsonAST.JNothing$.MODULE$);
                    }
                }
            }
        }
        if (tuple2 != null) {
            final JsonAST.JValue obj6 = (JsonAST.JValue)tuple2._1();
            final JsonAST.JValue x8 = (JsonAST.JValue)tuple2._2();
            if (JsonAST.JNothing$.MODULE$.equals(obj6)) {
                return new Diff(JsonAST.JNothing$.MODULE$, x8, JsonAST.JNothing$.MODULE$);
            }
        }
        if (tuple2 != null) {
            final JsonAST.JValue x9 = (JsonAST.JValue)tuple2._1();
            if (JsonAST.JNothing$.MODULE$.equals(tuple2._2())) {
                return new Diff(JsonAST.JNothing$.MODULE$, JsonAST.JNothing$.MODULE$, x9);
            }
        }
        if (tuple2 == null) {
            throw new MatchError((Object)tuple2);
        }
        final JsonAST.JValue y8 = (JsonAST.JValue)tuple2._2();
        return new Diff(y8, JsonAST.JNothing$.MODULE$, JsonAST.JNothing$.MODULE$);
    }
    
    private Diff diffFields(final List<Tuple2<String, JsonAST.JValue>> vs1, final List<Tuple2<String, JsonAST.JValue>> vs2) {
        return this.diffRec$1(vs1, vs2);
    }
    
    private Diff diffVals(final List<JsonAST.JValue> vs1, final List<JsonAST.JValue> vs2) {
        return this.diffRec$2(vs1, vs2);
    }
    
    public Diff apply(final JsonAST.JValue changed, final JsonAST.JValue added, final JsonAST.JValue deleted) {
        return new Diff(changed, added, deleted);
    }
    
    public Option<Tuple3<JsonAST.JValue, JsonAST.JValue, JsonAST.JValue>> unapply(final Diff x$0) {
        return (Option<Tuple3<JsonAST.JValue, JsonAST.JValue, JsonAST.JValue>>)((x$0 == null) ? None$.MODULE$ : new Some((Object)new Tuple3((Object)x$0.changed(), (Object)x$0.added(), (Object)x$0.deleted())));
    }
    
    private Object readResolve() {
        return Diff$.MODULE$;
    }
    
    private final Diff diffRec$1(final List xleft, final List yleft) {
        Diff diff;
        if (Nil$.MODULE$.equals(xleft)) {
            diff = new Diff(JsonAST.JNothing$.MODULE$, yleft.isEmpty() ? JsonAST.JNothing$.MODULE$ : new JsonAST.JObject((List<Tuple2<String, JsonAST.JValue>>)yleft), JsonAST.JNothing$.MODULE$);
        }
        else {
            if (!(xleft instanceof $colon$colon)) {
                throw new MatchError((Object)xleft);
            }
            final $colon$colon $colon$colon = ($colon$colon)xleft;
            final Tuple2 x = (Tuple2)$colon$colon.head();
            final List xs = $colon$colon.tl$1();
            final Option find = yleft.find((Function1)new Diff$$anonfun.Diff$$anonfun$1(x));
            Diff diff2;
            if (find instanceof Some) {
                final Tuple2 y = (Tuple2)((Some)find).x();
                final Diff field = this.diff((JsonAST.JValue)x._2(), (JsonAST.JValue)y._2()).toField((String)y._1());
                if (field == null) {
                    throw new MatchError((Object)field);
                }
                final JsonAST.JValue c1 = field.changed();
                final JsonAST.JValue a1 = field.added();
                final JsonAST.JValue d1 = field.deleted();
                final Tuple3 tuple3 = new Tuple3((Object)c1, (Object)a1, (Object)d1);
                final JsonAST.JValue c2 = (JsonAST.JValue)tuple3._1();
                final JsonAST.JValue a2 = (JsonAST.JValue)tuple3._2();
                final JsonAST.JValue d2 = (JsonAST.JValue)tuple3._3();
                final Diff diffRec$1 = this.diffRec$1(xs, (List)yleft.filterNot((Function1)new Diff$$anonfun.Diff$$anonfun$2(y)));
                if (diffRec$1 == null) {
                    throw new MatchError((Object)diffRec$1);
                }
                final JsonAST.JValue c3 = diffRec$1.changed();
                final JsonAST.JValue a3 = diffRec$1.added();
                final JsonAST.JValue d3 = diffRec$1.deleted();
                final Tuple3 tuple4 = new Tuple3((Object)c3, (Object)a3, (Object)d3);
                final JsonAST.JValue c4 = (JsonAST.JValue)tuple4._1();
                final JsonAST.JValue a4 = (JsonAST.JValue)tuple4._2();
                final JsonAST.JValue d4 = (JsonAST.JValue)tuple4._3();
                diff2 = new Diff(JsonAST.JValue$.MODULE$.j2m(c2).merge(c4, (MergeDep<JsonAST.JValue, JsonAST.JValue, JsonAST.JValue>)JsonAST.JValue$.MODULE$.jjj()), JsonAST.JValue$.MODULE$.j2m(a2).merge(a4, (MergeDep<JsonAST.JValue, JsonAST.JValue, JsonAST.JValue>)JsonAST.JValue$.MODULE$.jjj()), JsonAST.JValue$.MODULE$.j2m(d2).merge(d4, (MergeDep<JsonAST.JValue, JsonAST.JValue, JsonAST.JValue>)JsonAST.JValue$.MODULE$.jjj()));
            }
            else {
                if (!None$.MODULE$.equals(find)) {
                    throw new MatchError((Object)find);
                }
                final Diff diffRec$2 = this.diffRec$1(xs, yleft);
                if (diffRec$2 == null) {
                    throw new MatchError((Object)diffRec$2);
                }
                final JsonAST.JValue c5 = diffRec$2.changed();
                final JsonAST.JValue a5 = diffRec$2.added();
                final JsonAST.JValue d5 = diffRec$2.deleted();
                final Tuple3 tuple5 = new Tuple3((Object)c5, (Object)a5, (Object)d5);
                final JsonAST.JValue c6 = (JsonAST.JValue)tuple5._1();
                final JsonAST.JValue a6 = (JsonAST.JValue)tuple5._2();
                final JsonAST.JValue d6 = (JsonAST.JValue)tuple5._3();
                diff2 = new Diff(c6, a6, JsonAST.JValue$.MODULE$.j2m(new JsonAST.JObject((List<Tuple2<String, JsonAST.JValue>>)Nil$.MODULE$.$colon$colon((Object)x))).merge(d6, (MergeDep<JsonAST.JObject, JsonAST.JValue, JsonAST.JValue>)JsonAST.JValue$.MODULE$.jjj()));
            }
            diff = diff2;
        }
        return diff;
    }
    
    private final Diff diffRec$2(final List xleft, final List yleft) {
        final Tuple2 tuple2 = new Tuple2((Object)xleft, (Object)yleft);
        if (tuple2 != null) {
            final List xs = (List)tuple2._1();
            if (Nil$.MODULE$.equals(tuple2._2())) {
                return new Diff(JsonAST.JNothing$.MODULE$, JsonAST.JNothing$.MODULE$, xs.isEmpty() ? JsonAST.JNothing$.MODULE$ : new JsonAST.JArray((List<JsonAST.JValue>)xs));
            }
        }
        if (tuple2 != null) {
            final List obj = (List)tuple2._1();
            final List ys = (List)tuple2._2();
            if (Nil$.MODULE$.equals(obj)) {
                return new Diff(JsonAST.JNothing$.MODULE$, ys.isEmpty() ? JsonAST.JNothing$.MODULE$ : new JsonAST.JArray((List<JsonAST.JValue>)ys), JsonAST.JNothing$.MODULE$);
            }
        }
        if (tuple2 != null) {
            final List list = (List)tuple2._1();
            final List list2 = (List)tuple2._2();
            if (list instanceof $colon$colon) {
                final $colon$colon $colon$colon = ($colon$colon)list;
                final JsonAST.JValue x = (JsonAST.JValue)$colon$colon.head();
                final List xs2 = $colon$colon.tl$1();
                if (list2 instanceof $colon$colon) {
                    final $colon$colon $colon$colon2 = ($colon$colon)list2;
                    final JsonAST.JValue y = (JsonAST.JValue)$colon$colon2.head();
                    final List ys2 = $colon$colon2.tl$1();
                    final Diff diff2 = this.diff(x, y);
                    if (diff2 == null) {
                        throw new MatchError((Object)diff2);
                    }
                    final JsonAST.JValue c1 = diff2.changed();
                    final JsonAST.JValue a1 = diff2.added();
                    final JsonAST.JValue d1 = diff2.deleted();
                    final Tuple3 tuple3 = new Tuple3((Object)c1, (Object)a1, (Object)d1);
                    final JsonAST.JValue c2 = (JsonAST.JValue)tuple3._1();
                    final JsonAST.JValue a2 = (JsonAST.JValue)tuple3._2();
                    final JsonAST.JValue d2 = (JsonAST.JValue)tuple3._3();
                    final Diff diffRec$2 = this.diffRec$2(xs2, ys2);
                    if (diffRec$2 != null) {
                        final JsonAST.JValue c3 = diffRec$2.changed();
                        final JsonAST.JValue a3 = diffRec$2.added();
                        final JsonAST.JValue d3 = diffRec$2.deleted();
                        final Tuple3 tuple4 = new Tuple3((Object)c3, (Object)a3, (Object)d3);
                        final JsonAST.JValue c4 = (JsonAST.JValue)tuple4._1();
                        final JsonAST.JValue a4 = (JsonAST.JValue)tuple4._2();
                        final JsonAST.JValue d4 = (JsonAST.JValue)tuple4._3();
                        return new Diff(c2.$plus$plus(c4), a2.$plus$plus(a4), d2.$plus$plus(d4));
                    }
                    throw new MatchError((Object)diffRec$2);
                }
            }
        }
        throw new MatchError((Object)tuple2);
    }
    
    private Diff$() {
        MODULE$ = this;
    }
}
