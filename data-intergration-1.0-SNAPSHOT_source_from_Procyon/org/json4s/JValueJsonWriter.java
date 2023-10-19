// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

import scala.math.BigInt;
import scala.math.BigInt$;
import scala.sys.package$;
import scala.reflect.ScalaSignature;

@ScalaSignature(bytes = "\u0006\u0001Q4Q!\u0001\u0002\u0002*\u001d\u0011\u0001C\u0013,bYV,'j]8o/JLG/\u001a:\u000b\u0005\r!\u0011A\u00026t_:$4OC\u0001\u0006\u0003\ry'oZ\u0002\u0001'\r\u0001\u0001B\u0004\t\u0003\u00131i\u0011A\u0003\u0006\u0002\u0017\u0005)1oY1mC&\u0011QB\u0003\u0002\u0007\u0003:L(+\u001a4\u0011\u0007=\u0001\"#D\u0001\u0003\u0013\t\t\"A\u0001\u0006Kg>twK]5uKJ\u0004\"a\u0005\f\u000f\u0005=!\u0012BA\u000b\u0003\u0003\u001d\u0001\u0018mY6bO\u0016L!a\u0006\r\u0003\r)3\u0016\r\\;f\u0015\t)\"\u0001C\u0003\u001b\u0001\u0011\u00051$\u0001\u0004=S:LGO\u0010\u000b\u00029A\u0011q\u0002\u0001\u0005\u0006=\u00011\taH\u0001\bC\u0012$gj\u001c3f)\tq\u0001\u0005C\u0003\";\u0001\u0007!#\u0001\u0003o_\u0012,\u0007\"B\u0012\u0001\t\u0003!\u0013!C3oI>\u0013'.Z2u)\u0005q\u0001\"\u0002\u0014\u0001\t\u00039\u0013AC:uCJ$h)[3mIR\u0011a\u0002\u000b\u0005\u0006S\u0015\u0002\rAK\u0001\u0005]\u0006lW\r\u0005\u0002,]9\u0011\u0011\u0002L\u0005\u0003[)\ta\u0001\u0015:fI\u00164\u0017BA\u00181\u0005\u0019\u0019FO]5oO*\u0011QF\u0003\u0005\u0006e\u0001!\taM\u0001\u0007gR\u0014\u0018N\\4\u0015\u00059!\u0004\"B\u001b2\u0001\u0004Q\u0013!\u0002<bYV,\u0007\"B\u001c\u0001\t\u0003A\u0014\u0001\u00022zi\u0016$\"AD\u001d\t\u000bU2\u0004\u0019\u0001\u001e\u0011\u0005%Y\u0014B\u0001\u001f\u000b\u0005\u0011\u0011\u0015\u0010^3\t\u000by\u0002A\u0011A \u0002\u0007%tG\u000f\u0006\u0002\u000f\u0001\")Q'\u0010a\u0001\u0003B\u0011\u0011BQ\u0005\u0003\u0007*\u00111!\u00138u\u0011\u0015)\u0005\u0001\"\u0001G\u0003\u0011awN\\4\u0015\u000599\u0005\"B\u001bE\u0001\u0004A\u0005CA\u0005J\u0013\tQ%B\u0001\u0003M_:<\u0007\"\u0002'\u0001\t\u0003i\u0015A\u00022jO&sG\u000f\u0006\u0002\u000f\u001d\")Qg\u0013a\u0001\u001fB\u0011\u0001k\u0016\b\u0003#Zs!AU+\u000e\u0003MS!\u0001\u0016\u0004\u0002\rq\u0012xn\u001c;?\u0013\u0005Y\u0011BA\u000b\u000b\u0013\tA\u0016L\u0001\u0004CS\u001eLe\u000e\u001e\u0006\u0003+)AQa\u0017\u0001\u0005\u0002q\u000bqAY8pY\u0016\fg\u000e\u0006\u0002\u000f;\")QG\u0017a\u0001=B\u0011\u0011bX\u0005\u0003A*\u0011qAQ8pY\u0016\fg\u000eC\u0003c\u0001\u0011\u00051-A\u0003tQ>\u0014H\u000f\u0006\u0002\u000fI\")Q'\u0019a\u0001KB\u0011\u0011BZ\u0005\u0003O*\u0011Qa\u00155peRDQ!\u001b\u0001\u0005\u0002\u0011\n\u0001\"\u001a8e\u0003J\u0014\u0018-\u001f\u0005\u0006W\u0002!\t\u0001\\\u0001\nC\u0012$'JV1mk\u0016$\"AD7\t\u000b9T\u0007\u0019\u0001\n\u0002\u0005)4\u0018f\u0001\u0001qe&\u0011\u0011O\u0001\u0002\u0016\u0015\u0012+7-[7bY\u0006\u001bHOS:p]^\u0013\u0018\u000e^3s\u0013\t\u0019(A\u0001\u000bK\t>,(\r\\3BgRT5o\u001c8Xe&$XM\u001d")
public abstract class JValueJsonWriter implements JsonWriter<JsonAST.JValue>
{
    public abstract JsonWriter<JsonAST.JValue> addNode(final JsonAST.JValue p0);
    
    @Override
    public JsonWriter<JsonAST.JValue> endObject() {
        throw package$.MODULE$.error("You have to start an object to be able to end it (endObject called before startObject)");
    }
    
    @Override
    public JsonWriter<JsonAST.JValue> startField(final String name) {
        throw package$.MODULE$.error("You have to start an object before starting a field.");
    }
    
    @Override
    public JsonWriter<JsonAST.JValue> string(final String value) {
        return this.addNode(org.json4s.package$.MODULE$.JString().apply(value));
    }
    
    @Override
    public JsonWriter<JsonAST.JValue> byte(final byte value) {
        return this.addNode(org.json4s.package$.MODULE$.JInt().apply(BigInt$.MODULE$.int2bigInt((int)value)));
    }
    
    @Override
    public JsonWriter<JsonAST.JValue> int(final int value) {
        return this.addNode(org.json4s.package$.MODULE$.JInt().apply(BigInt$.MODULE$.int2bigInt(value)));
    }
    
    @Override
    public JsonWriter<JsonAST.JValue> long(final long value) {
        return this.addNode(org.json4s.package$.MODULE$.JInt().apply(BigInt$.MODULE$.long2bigInt(value)));
    }
    
    @Override
    public JsonWriter<JsonAST.JValue> bigInt(final BigInt value) {
        return this.addNode(org.json4s.package$.MODULE$.JInt().apply(value));
    }
    
    @Override
    public JsonWriter<JsonAST.JValue> boolean(final boolean value) {
        return this.addNode(org.json4s.package$.MODULE$.JBool().apply(value));
    }
    
    @Override
    public JsonWriter<JsonAST.JValue> short(final short value) {
        return this.addNode(org.json4s.package$.MODULE$.JInt().apply(BigInt$.MODULE$.int2bigInt((int)value)));
    }
    
    @Override
    public JsonWriter<JsonAST.JValue> endArray() {
        throw package$.MODULE$.error("You have to start an object to be able to end it (endArray called before startArray)");
    }
    
    @Override
    public JsonWriter<JsonAST.JValue> addJValue(final JsonAST.JValue jv) {
        return this.addNode(jv);
    }
}
