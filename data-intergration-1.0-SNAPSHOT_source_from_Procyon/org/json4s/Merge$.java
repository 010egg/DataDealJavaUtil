// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

import scala.Option;
import scala.MatchError;
import scala.None$;
import scala.collection.Seq;
import scala.Predef$;
import scala.Some;
import scala.Function1;
import scala.collection.immutable.$colon$colon;
import scala.collection.GenTraversableOnce;
import scala.collection.immutable.List$;
import scala.collection.immutable.Nil$;
import scala.Tuple2;
import scala.collection.immutable.List;

public final class Merge$
{
    public static final Merge$ MODULE$;
    
    static {
        new Merge$();
    }
    
    public <A extends JsonAST.JValue, B extends JsonAST.JValue, R extends JsonAST.JValue> R merge(final A val1, final B val2, final MergeDep<A, B, R> instance) {
        return instance.apply(val1, val2);
    }
    
    public List<Tuple2<String, JsonAST.JValue>> mergeFields(final List<Tuple2<String, JsonAST.JValue>> vs1, final List<Tuple2<String, JsonAST.JValue>> vs2) {
        return (List<Tuple2<String, JsonAST.JValue>>)this.mergeRec$1((List)Nil$.MODULE$, vs1, vs2);
    }
    
    public List<JsonAST.JValue> mergeVals(final List<JsonAST.JValue> vs1, final List<JsonAST.JValue> vs2) {
        return (List<JsonAST.JValue>)this.mergeRec$2((List)Nil$.MODULE$, vs1, vs2);
    }
    
    private final List mergeRec$1(List acc, List xleft, List yleft) {
        List obj;
        while (true) {
            obj = xleft;
            if (Nil$.MODULE$.equals(obj)) {
                return (List)acc.$plus$plus((GenTraversableOnce)yleft, List$.MODULE$.canBuildFrom());
            }
            if (!(obj instanceof $colon$colon)) {
                break;
            }
            final $colon$colon $colon$colon = ($colon$colon)obj;
            final Tuple2 tuple2 = (Tuple2)$colon$colon.head();
            final List xs = $colon$colon.tl$1();
            if (tuple2 == null) {
                break;
            }
            final String xn = (String)tuple2._1();
            final JsonAST.JValue xv = (JsonAST.JValue)tuple2._2();
            final Option find = yleft.find((Function1)new Merge$$anonfun.Merge$$anonfun$1(xn));
            if (find instanceof Some) {
                final Tuple2 y = (Tuple2)((Some)find).x();
                if (y != null) {
                    final JsonAST.JValue yv = (JsonAST.JValue)y._2();
                    final List list = (List)acc.$plus$plus((GenTraversableOnce)List$.MODULE$.apply((Seq)Predef$.MODULE$.wrapRefArray((Object[])new Tuple2[] { JsonAST.JField$.MODULE$.apply(xn, this.merge(xv, yv, (MergeDep<JsonAST.JValue, JsonAST.JValue, JsonAST.JValue>)JsonAST.JValue$.MODULE$.jjj())) })), List$.MODULE$.canBuildFrom());
                    final List list2 = xs;
                    yleft = (List)yleft.filterNot((Function1)new Merge$$anonfun$mergeRec$1.Merge$$anonfun$mergeRec$1$1(y));
                    xleft = list2;
                    acc = list;
                    continue;
                }
            }
            if (!None$.MODULE$.equals(find)) {
                throw new MatchError((Object)find);
            }
            final List list3 = (List)acc.$plus$plus((GenTraversableOnce)List$.MODULE$.apply((Seq)Predef$.MODULE$.wrapRefArray((Object[])new Tuple2[] { JsonAST.JField$.MODULE$.apply(xn, xv) })), List$.MODULE$.canBuildFrom());
            final List list4 = xs;
            yleft = yleft;
            xleft = list4;
            acc = list3;
        }
        throw new MatchError((Object)obj);
    }
    
    private final List mergeRec$2(List acc, List xleft, List yleft) {
        while (true) {
            final List obj = xleft;
            if (Nil$.MODULE$.equals(obj)) {
                return (List)acc.$plus$plus((GenTraversableOnce)yleft, List$.MODULE$.canBuildFrom());
            }
            if (!(obj instanceof $colon$colon)) {
                throw new MatchError((Object)obj);
            }
            final $colon$colon $colon$colon = ($colon$colon)obj;
            final JsonAST.JValue x = (JsonAST.JValue)$colon$colon.head();
            final List xs = $colon$colon.tl$1();
            final Option find = yleft.find((Function1)new Merge$$anonfun.Merge$$anonfun$2(x));
            if (find instanceof Some) {
                final JsonAST.JValue y = (JsonAST.JValue)((Some)find).x();
                final List list = (List)acc.$plus$plus((GenTraversableOnce)List$.MODULE$.apply((Seq)Predef$.MODULE$.wrapRefArray((Object[])new JsonAST.JValue[] { this.merge(x, y, (MergeDep<JsonAST.JValue, JsonAST.JValue, JsonAST.JValue>)JsonAST.JValue$.MODULE$.jjj()) })), List$.MODULE$.canBuildFrom());
                final List list2 = xs;
                yleft = (List)yleft.filterNot((Function1)new Merge$$anonfun$mergeRec$2.Merge$$anonfun$mergeRec$2$1(y));
                xleft = list2;
                acc = list;
            }
            else {
                if (!None$.MODULE$.equals(find)) {
                    throw new MatchError((Object)find);
                }
                final List list3 = (List)acc.$plus$plus((GenTraversableOnce)List$.MODULE$.apply((Seq)Predef$.MODULE$.wrapRefArray((Object[])new JsonAST.JValue[] { x })), List$.MODULE$.canBuildFrom());
                final List list4 = xs;
                yleft = yleft;
                xleft = list4;
                acc = list3;
            }
        }
    }
    
    private Merge$() {
        MODULE$ = this;
    }
}
