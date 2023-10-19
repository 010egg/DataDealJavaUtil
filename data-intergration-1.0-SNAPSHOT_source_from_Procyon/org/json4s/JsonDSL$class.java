// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

import scala.collection.Seq;
import scala.Predef$;
import scala.Symbol;
import scala.MatchError;
import scala.None$;
import scala.Some;
import scala.Option;
import scala.Tuple2;
import scala.collection.immutable.Map;
import scala.collection.immutable.List$;
import scala.collection.immutable.List;
import scala.Function1;
import scala.collection.Iterable;

public abstract class JsonDSL$class
{
    public static JsonAST.JArray seq2jvalue(final JsonDSL $this, final Iterable s, final Function1 ev) {
        return new JsonAST.JArray((List<JsonAST.JValue>)s.toList().map(ev, List$.MODULE$.canBuildFrom()));
    }
    
    public static JsonAST.JObject map2jvalue(final JsonDSL $this, final Map m, final Function1 ev) {
        return new JsonAST.JObject((List<Tuple2<String, JsonAST.JValue>>)m.toList().map((Function1)new JsonDSL$$anonfun$map2jvalue.JsonDSL$$anonfun$map2jvalue$1($this, ev), List$.MODULE$.canBuildFrom()));
    }
    
    public static JsonAST.JValue option2jvalue(final JsonDSL $this, final Option opt, final Function1 ev) {
        JsonAST.JValue module$;
        if (opt instanceof Some) {
            final Object x = ((Some)opt).x();
            module$ = (JsonAST.JValue)ev.apply(x);
        }
        else {
            if (!None$.MODULE$.equals(opt)) {
                throw new MatchError((Object)opt);
            }
            module$ = JsonAST.JNothing$.MODULE$;
        }
        return module$;
    }
    
    public static JsonAST.JString symbol2jvalue(final JsonDSL $this, final Symbol x) {
        return new JsonAST.JString(x.name());
    }
    
    public static JsonAST.JObject pair2jvalue(final JsonDSL $this, final Tuple2 t, final Function1 ev) {
        return new JsonAST.JObject((List<Tuple2<String, JsonAST.JValue>>)List$.MODULE$.apply((Seq)Predef$.MODULE$.wrapRefArray((Object[])new Tuple2[] { JsonAST.JField$.MODULE$.apply((String)t._1(), (JsonAST.JValue)ev.apply(t._2())) })));
    }
    
    public static JsonAST.JObject list2jvalue(final JsonDSL $this, final List l) {
        return new JsonAST.JObject((List<Tuple2<String, JsonAST.JValue>>)l);
    }
    
    public static JsonDSL.JsonListAssoc jobject2assoc(final JsonDSL $this, final JsonAST.JObject o) {
        return $this.new JsonListAssoc(o.obj());
    }
    
    public static JsonDSL.JsonAssoc pair2Assoc(final JsonDSL $this, final Tuple2 t, final Function1 ev) {
        return $this.new JsonAssoc((Tuple2<String, A>)t, (Function1<A, JsonAST.JValue>)ev);
    }
    
    public static void $init$(final JsonDSL $this) {
    }
}
