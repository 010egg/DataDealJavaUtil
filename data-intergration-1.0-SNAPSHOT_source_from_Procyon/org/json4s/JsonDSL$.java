// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

import scala.math.BigInt;
import scala.collection.immutable.List;
import scala.Tuple2;
import scala.Symbol;
import scala.Option;
import scala.collection.immutable.Map;
import scala.Function1;
import scala.collection.Iterable;
import scala.math.BigDecimal;

public final class JsonDSL$ implements JsonDSL, DoubleMode
{
    public static final JsonDSL$ MODULE$;
    
    static {
        new JsonDSL$();
    }
    
    @Override
    public JsonAST.JValue double2jvalue(final double x) {
        return DoubleMode$class.double2jvalue(this, x);
    }
    
    @Override
    public JsonAST.JValue float2jvalue(final float x) {
        return DoubleMode$class.float2jvalue(this, x);
    }
    
    @Override
    public JsonAST.JValue bigdecimal2jvalue(final BigDecimal x) {
        return DoubleMode$class.bigdecimal2jvalue(this, x);
    }
    
    @Override
    public <A> JsonAST.JArray seq2jvalue(final Iterable<A> s, final Function1<A, JsonAST.JValue> ev) {
        return JsonDSL$class.seq2jvalue(this, s, ev);
    }
    
    @Override
    public <A> JsonAST.JObject map2jvalue(final Map<String, A> m, final Function1<A, JsonAST.JValue> ev) {
        return JsonDSL$class.map2jvalue(this, m, ev);
    }
    
    @Override
    public <A> JsonAST.JValue option2jvalue(final Option<A> opt, final Function1<A, JsonAST.JValue> ev) {
        return JsonDSL$class.option2jvalue(this, opt, ev);
    }
    
    @Override
    public JsonAST.JString symbol2jvalue(final Symbol x) {
        return JsonDSL$class.symbol2jvalue(this, x);
    }
    
    @Override
    public <A> JsonAST.JObject pair2jvalue(final Tuple2<String, A> t, final Function1<A, JsonAST.JValue> ev) {
        return JsonDSL$class.pair2jvalue(this, t, ev);
    }
    
    @Override
    public JsonAST.JObject list2jvalue(final List<Tuple2<String, JsonAST.JValue>> l) {
        return JsonDSL$class.list2jvalue(this, l);
    }
    
    @Override
    public JsonListAssoc jobject2assoc(final JsonAST.JObject o) {
        return JsonDSL$class.jobject2assoc(this, o);
    }
    
    @Override
    public <A> JsonAssoc<A> pair2Assoc(final Tuple2<String, A> t, final Function1<A, JsonAST.JValue> ev) {
        return (JsonAssoc<A>)JsonDSL$class.pair2Assoc(this, t, ev);
    }
    
    @Override
    public JsonAST.JValue short2jvalue(final short x) {
        return Implicits$class.short2jvalue(this, x);
    }
    
    @Override
    public JsonAST.JValue byte2jvalue(final byte x) {
        return Implicits$class.byte2jvalue(this, x);
    }
    
    @Override
    public JsonAST.JValue char2jvalue(final char x) {
        return Implicits$class.char2jvalue(this, x);
    }
    
    @Override
    public JsonAST.JValue int2jvalue(final int x) {
        return Implicits$class.int2jvalue(this, x);
    }
    
    @Override
    public JsonAST.JValue long2jvalue(final long x) {
        return Implicits$class.long2jvalue(this, x);
    }
    
    @Override
    public JsonAST.JValue bigint2jvalue(final BigInt x) {
        return Implicits$class.bigint2jvalue(this, x);
    }
    
    @Override
    public JsonAST.JValue boolean2jvalue(final boolean x) {
        return Implicits$class.boolean2jvalue(this, x);
    }
    
    @Override
    public JsonAST.JValue string2jvalue(final String x) {
        return Implicits$class.string2jvalue(this, x);
    }
    
    private JsonDSL$() {
        Implicits$class.$init$(MODULE$ = this);
        JsonDSL$class.$init$(this);
        DoubleMode$class.$init$(this);
    }
}
