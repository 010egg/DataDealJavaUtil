// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

import scala.collection.immutable.Nil$;
import scala.collection.mutable.ListBuffer$;
import scala.collection.Seq;
import scala.math.BigDecimal;
import scala.math.BigInt;
import scala.sys.package$;
import scala.Tuple2;
import scala.collection.mutable.ListBuffer;
import scala.reflect.ScalaSignature;

@ScalaSignature(bytes = "\u0006\u0001\u0005\u0015c\u0001B\u0001\u0003\r\u001d\u0011\u0011D\u0013#fG&l\u0017\r\u001c&PE*,7\r\u001e&t_:<&/\u001b;fe*\u00111\u0001B\u0001\u0007UN|g\u000eN:\u000b\u0003\u0015\t1a\u001c:h\u0007\u0001\u00192\u0001\u0001\u0005\u000f!\tIA\"D\u0001\u000b\u0015\u0005Y\u0011!B:dC2\f\u0017BA\u0007\u000b\u0005\u0019\te.\u001f*fMB\u0019q\u0002\u0005\n\u000e\u0003\tI!!\u0005\u0002\u0003\u0015)\u001bxN\\,sSR,'\u000f\u0005\u0002\u0014-9\u0011q\u0002F\u0005\u0003+\t\tq\u0001]1dW\u0006<W-\u0003\u0002\u00181\t1!JV1mk\u0016T!!\u0006\u0002\t\u0011i\u0001!\u0011!Q\u0001\n9\ta\u0001]1sK:$\b\"\u0002\u000f\u0001\t\u0003i\u0012A\u0002\u001fj]&$h\b\u0006\u0002\u001f?A\u0011q\u0002\u0001\u0005\u00065m\u0001\rA\u0004\u0005\u0007C\u0001\u0001\u000b\u0011\u0002\u0012\u0002\u000b9|G-Z:\u0011\u0007\rB#&D\u0001%\u0015\t)c%A\u0004nkR\f'\r\\3\u000b\u0005\u001dR\u0011AC2pY2,7\r^5p]&\u0011\u0011\u0006\n\u0002\u000b\u0019&\u001cHOQ;gM\u0016\u0014\bCA\n,\u0013\ta\u0003D\u0001\u0004K\r&,G\u000e\u001a\u0005\u0006]\u0001!\taL\u0001\bC\u0012$gj\u001c3f)\tq\u0002\u0007C\u00032[\u0001\u0007!&\u0001\u0003o_\u0012,\u0007\"B\u001a\u0001\t\u0003!\u0014AC:uCJ$\u0018I\u001d:bsR\ta\u0002C\u00037\u0001\u0011\u0005A'\u0001\u0005f]\u0012\f%O]1z\u0011\u0015A\u0004\u0001\"\u00015\u0003-\u0019H/\u0019:u\u001f\nTWm\u0019;\t\u000bi\u0002A\u0011\u0001\u001b\u0002\u0013\u0015tGm\u00142kK\u000e$\b\"\u0002\u001f\u0001\t\u0003i\u0014AB:ue&tw\r\u0006\u0002\u000f}!)qh\u000fa\u0001\u0001\u0006)a/\u00197vKB\u0011\u0011\t\u0012\b\u0003\u0013\tK!a\u0011\u0006\u0002\rA\u0013X\rZ3g\u0013\t)eI\u0001\u0004TiJLgn\u001a\u0006\u0003\u0007*AQ\u0001\u0013\u0001\u0005\u0002%\u000bAAY=uKR\u0011aB\u0013\u0005\u0006\u007f\u001d\u0003\ra\u0013\t\u0003\u00131K!!\u0014\u0006\u0003\t\tKH/\u001a\u0005\u0006\u001f\u0002!\t\u0001U\u0001\u0004S:$HC\u0001\bR\u0011\u0015yd\n1\u0001S!\tI1+\u0003\u0002U\u0015\t\u0019\u0011J\u001c;\t\u000bY\u0003A\u0011A,\u0002\t1|gn\u001a\u000b\u0003\u001daCQaP+A\u0002e\u0003\"!\u0003.\n\u0005mS!\u0001\u0002'p]\u001eDQ!\u0018\u0001\u0005\u0002y\u000baAY5h\u0013:$HC\u0001\b`\u0011\u0015yD\f1\u0001a!\t\t\u0007N\u0004\u0002cO:\u00111MZ\u0007\u0002I*\u0011QMB\u0001\u0007yI|w\u000e\u001e \n\u0003-I!!\u0006\u0006\n\u0005%T'A\u0002\"jO&sGO\u0003\u0002\u0016\u0015!)A\u000e\u0001C\u0001[\u00069!m\\8mK\u0006tGC\u0001\bo\u0011\u0015y4\u000e1\u0001p!\tI\u0001/\u0003\u0002r\u0015\t9!i\\8mK\u0006t\u0007\"B:\u0001\t\u0003!\u0018!B:i_J$HC\u0001\bv\u0011\u0015y$\u000f1\u0001w!\tIq/\u0003\u0002y\u0015\t)1\u000b[8si\")!\u0010\u0001C\u0001w\u0006)a\r\\8biR\u0011a\u0002 \u0005\u0006\u007fe\u0004\r! \t\u0003\u0013yL!a \u0006\u0003\u000b\u0019cw.\u0019;\t\u000f\u0005\r\u0001\u0001\"\u0001\u0002\u0006\u00051Am\\;cY\u0016$2ADA\u0004\u0011\u001dy\u0014\u0011\u0001a\u0001\u0003\u0013\u00012!CA\u0006\u0013\r\tiA\u0003\u0002\u0007\t>,(\r\\3\t\u000f\u0005E\u0001\u0001\"\u0001\u0002\u0014\u0005Q!-[4EK\u000eLW.\u00197\u0015\u00079\t)\u0002C\u0004@\u0003\u001f\u0001\r!a\u0006\u0011\u0007\u0005\fI\"C\u0002\u0002\u001c)\u0014!BQ5h\t\u0016\u001c\u0017.\\1m\u0011\u001d\ty\u0002\u0001C\u0001\u0003C\t!b\u001d;beR4\u0015.\u001a7e)\rq\u00111\u0005\u0005\b\u0003K\ti\u00021\u0001A\u0003\u0011q\u0017-\\3\t\u000f\u0005%\u0002\u0001\"\u0001\u0002,\u0005I\u0011\r\u001a3K-\u0006dW/\u001a\u000b\u0005\u0003[\tY\u0004\u0005\u0003\u0010!\u0005=\u0002cAA\u0019-9\u0019\u00111\u0007\u000b\u000f\t\u0005U\u0012\u0011\b\b\u0004G\u0006]\u0012\"A\u0003\n\u0005\r!\u0001\u0002CA\u001f\u0003O\u0001\r!a\f\u0002\u0005)4\bbBA!\u0001\u0011\u0005\u00111I\u0001\u0007e\u0016\u001cX\u000f\u001c;\u0016\u0003I\u0001")
public final class JDecimalJObjectJsonWriter implements JsonWriter<JsonAST.JValue>
{
    private final JsonWriter<JsonAST.JValue> parent;
    private final ListBuffer<Tuple2<String, JsonAST.JValue>> nodes;
    
    public JDecimalJObjectJsonWriter addNode(final Tuple2<String, JsonAST.JValue> node) {
        this.nodes.$plus$eq((Object)node);
        return this;
    }
    
    @Override
    public JsonWriter<JsonAST.JValue> startArray() {
        throw package$.MODULE$.error("You have to start a field to be able to end it (startArray called before startField in a JObject builder)");
    }
    
    @Override
    public JsonWriter<JsonAST.JValue> endArray() {
        throw package$.MODULE$.error("You have to start an array to be able to end it (endArray called before startArray)");
    }
    
    @Override
    public JsonWriter<JsonAST.JValue> startObject() {
        throw package$.MODULE$.error("You have to start a field to be able to end it (startObject called before startField in a JObject builder)");
    }
    
    @Override
    public JsonWriter<JsonAST.JValue> endObject() {
        final JsonWriter<JsonAST.JValue> parent = this.parent;
        JsonWriter<JsonAST.JValue> jsonWriter;
        if (parent instanceof JDecimalAstJsonWriter) {
            jsonWriter = ((JDecimalAstJsonWriter)parent).addNode(this.result());
        }
        else {
            jsonWriter = this.parent;
        }
        return jsonWriter;
    }
    
    @Override
    public JsonWriter<JsonAST.JValue> string(final String value) {
        throw package$.MODULE$.error("You have to start a field to be able to end it (string called before startField in a JObject builder)");
    }
    
    @Override
    public JsonWriter<JsonAST.JValue> byte(final byte value) {
        throw package$.MODULE$.error("You have to start a field to be able to end it (byte called before startField in a JObject builder)");
    }
    
    @Override
    public JsonWriter<JsonAST.JValue> int(final int value) {
        throw package$.MODULE$.error("You have to start a field to be able to end it (int called before startField in a JObject builder)");
    }
    
    @Override
    public JsonWriter<JsonAST.JValue> long(final long value) {
        throw package$.MODULE$.error("You have to start a field to be able to end it (long called before startField in a JObject builder)");
    }
    
    @Override
    public JsonWriter<JsonAST.JValue> bigInt(final BigInt value) {
        throw package$.MODULE$.error("You have to start a field to be able to end it (bigInt called before startField in a JObject builder)");
    }
    
    @Override
    public JsonWriter<JsonAST.JValue> boolean(final boolean value) {
        throw package$.MODULE$.error("You have to start a field to be able to end it (boolean called before startField in a JObject builder)");
    }
    
    @Override
    public JsonWriter<JsonAST.JValue> short(final short value) {
        throw package$.MODULE$.error("You have to start a field to be able to end it (short called before startField in a JObject builder)");
    }
    
    @Override
    public JsonWriter<JsonAST.JValue> float(final float value) {
        throw package$.MODULE$.error("You have to start a field to be able to end it (float called before startField in a JObject builder)");
    }
    
    @Override
    public JsonWriter<JsonAST.JValue> double(final double value) {
        throw package$.MODULE$.error("You have to start a field to be able to end it (double called before startField in a JObject builder)");
    }
    
    @Override
    public JsonWriter<JsonAST.JValue> bigDecimal(final BigDecimal value) {
        throw package$.MODULE$.error("You have to start a field to be able to end it (bigDecimal called before startField in a JObject builder)");
    }
    
    @Override
    public JsonWriter<JsonAST.JValue> startField(final String name) {
        return new JDecimalJFieldJsonWriter(name, this);
    }
    
    @Override
    public JsonWriter<JsonAST.JValue> addJValue(final JsonAST.JValue jv) {
        throw package$.MODULE$.error("You have to start a field to be able to end it (addJValue called before startField in a JObject builder)");
    }
    
    @Override
    public JsonAST.JValue result() {
        return org.json4s.package$.MODULE$.JObject().apply((Seq<Tuple2<String, JsonAST.JValue>>)this.nodes.toList());
    }
    
    public JDecimalJObjectJsonWriter(final JsonWriter<JsonAST.JValue> parent) {
        this.parent = parent;
        this.nodes = (ListBuffer<Tuple2<String, JsonAST.JValue>>)ListBuffer$.MODULE$.apply((Seq)Nil$.MODULE$);
    }
}
