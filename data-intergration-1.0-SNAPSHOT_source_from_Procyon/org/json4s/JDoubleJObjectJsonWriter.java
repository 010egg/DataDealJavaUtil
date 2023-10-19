// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

import scala.collection.Seq;
import scala.collection.immutable.Nil$;
import scala.collection.mutable.ListBuffer$;
import scala.collection.immutable.List;
import scala.math.BigDecimal;
import scala.math.BigInt;
import scala.sys.package$;
import scala.Tuple2;
import scala.collection.mutable.ListBuffer;
import scala.reflect.ScalaSignature;

@ScalaSignature(bytes = "\u0006\u0001\u0005\u0015c\u0001B\u0001\u0003\r\u001d\u0011\u0001D\u0013#pk\ndWMS(cU\u0016\u001cGOS:p]^\u0013\u0018\u000e^3s\u0015\t\u0019A!\u0001\u0004kg>tGg\u001d\u0006\u0002\u000b\u0005\u0019qN]4\u0004\u0001M\u0019\u0001\u0001\u0003\b\u0011\u0005%aQ\"\u0001\u0006\u000b\u0003-\tQa]2bY\u0006L!!\u0004\u0006\u0003\r\u0005s\u0017PU3g!\ry\u0001CE\u0007\u0002\u0005%\u0011\u0011C\u0001\u0002\u000b\u0015N|gn\u0016:ji\u0016\u0014\bCA\n\u0017\u001d\tyA#\u0003\u0002\u0016\u0005\u00059\u0001/Y2lC\u001e,\u0017BA\f\u0019\u0005\u0019Qe+\u00197vK*\u0011QC\u0001\u0005\t5\u0001\u0011\t\u0011)A\u0005\u001d\u00051\u0001/\u0019:f]RDQ\u0001\b\u0001\u0005\u0002u\ta\u0001P5oSRtDC\u0001\u0010 !\ty\u0001\u0001C\u0003\u001b7\u0001\u0007a\u0002\u0003\u0004\"\u0001\u0001\u0006IAI\u0001\u0006]>$Wm\u001d\t\u0004G!RS\"\u0001\u0013\u000b\u0005\u00152\u0013aB7vi\u0006\u0014G.\u001a\u0006\u0003O)\t!bY8mY\u0016\u001cG/[8o\u0013\tICE\u0001\u0006MSN$()\u001e4gKJ\u0004\"aE\u0016\n\u00051B\"A\u0002&GS\u0016dG\rC\u0003/\u0001\u0011\u0005q&A\u0004bI\u0012tu\u000eZ3\u0015\u0005y\u0001\u0004\"B\u0019.\u0001\u0004Q\u0013\u0001\u00028pI\u0016DQa\r\u0001\u0005\u0002Q\n!b\u001d;beR\f%O]1z)\u0005q\u0001\"\u0002\u001c\u0001\t\u0003!\u0014\u0001C3oI\u0006\u0013(/Y=\t\u000ba\u0002A\u0011\u0001\u001b\u0002\u0017M$\u0018M\u001d;PE*,7\r\u001e\u0005\u0006u\u0001!\t\u0001N\u0001\nK:$wJ\u00196fGRDQ\u0001\u0010\u0001\u0005\u0002u\naa\u001d;sS:<GC\u0001\b?\u0011\u0015y4\b1\u0001A\u0003\u00151\u0018\r\\;f!\t\tEI\u0004\u0002\n\u0005&\u00111IC\u0001\u0007!J,G-\u001a4\n\u0005\u00153%AB*ue&twM\u0003\u0002D\u0015!)\u0001\n\u0001C\u0001\u0013\u0006!!-\u001f;f)\tq!\nC\u0003@\u000f\u0002\u00071\n\u0005\u0002\n\u0019&\u0011QJ\u0003\u0002\u0005\u0005f$X\rC\u0003P\u0001\u0011\u0005\u0001+A\u0002j]R$\"AD)\t\u000b}r\u0005\u0019\u0001*\u0011\u0005%\u0019\u0016B\u0001+\u000b\u0005\rIe\u000e\u001e\u0005\u0006-\u0002!\taV\u0001\u0005Y>tw\r\u0006\u0002\u000f1\")q(\u0016a\u00013B\u0011\u0011BW\u0005\u00037*\u0011A\u0001T8oO\")Q\f\u0001C\u0001=\u00061!-[4J]R$\"AD0\t\u000b}b\u0006\u0019\u00011\u0011\u0005\u0005DgB\u00012h\u001d\t\u0019g-D\u0001e\u0015\t)g!\u0001\u0004=e>|GOP\u0005\u0002\u0017%\u0011QCC\u0005\u0003S*\u0014aAQ5h\u0013:$(BA\u000b\u000b\u0011\u0015a\u0007\u0001\"\u0001n\u0003\u001d\u0011wn\u001c7fC:$\"A\u00048\t\u000b}Z\u0007\u0019A8\u0011\u0005%\u0001\u0018BA9\u000b\u0005\u001d\u0011un\u001c7fC:DQa\u001d\u0001\u0005\u0002Q\fQa\u001d5peR$\"AD;\t\u000b}\u0012\b\u0019\u0001<\u0011\u0005%9\u0018B\u0001=\u000b\u0005\u0015\u0019\u0006n\u001c:u\u0011\u0015Q\b\u0001\"\u0001|\u0003\u00151Gn\\1u)\tqA\u0010C\u0003@s\u0002\u0007Q\u0010\u0005\u0002\n}&\u0011qP\u0003\u0002\u0006\r2|\u0017\r\u001e\u0005\b\u0003\u0007\u0001A\u0011AA\u0003\u0003\u0019!w.\u001e2mKR\u0019a\"a\u0002\t\u000f}\n\t\u00011\u0001\u0002\nA\u0019\u0011\"a\u0003\n\u0007\u00055!B\u0001\u0004E_V\u0014G.\u001a\u0005\b\u0003#\u0001A\u0011AA\n\u0003)\u0011\u0017n\u001a#fG&l\u0017\r\u001c\u000b\u0004\u001d\u0005U\u0001bB \u0002\u0010\u0001\u0007\u0011q\u0003\t\u0004C\u0006e\u0011bAA\u000eU\nQ!)[4EK\u000eLW.\u00197\t\u000f\u0005}\u0001\u0001\"\u0001\u0002\"\u0005Q1\u000f^1si\u001aKW\r\u001c3\u0015\u00079\t\u0019\u0003C\u0004\u0002&\u0005u\u0001\u0019\u0001!\u0002\t9\fW.\u001a\u0005\b\u0003S\u0001A\u0011AA\u0016\u0003%\tG\r\u001a&WC2,X\r\u0006\u0003\u0002.\u0005m\u0002\u0003B\b\u0011\u0003_\u00012!!\r\u0017\u001d\r\t\u0019\u0004\u0006\b\u0005\u0003k\tIDD\u0002d\u0003oI\u0011!B\u0005\u0003\u0007\u0011A\u0001\"!\u0010\u0002(\u0001\u0007\u0011qF\u0001\u0003UZDq!!\u0011\u0001\t\u0003\t\u0019%\u0001\u0004sKN,H\u000e^\u000b\u0002%\u0001")
public final class JDoubleJObjectJsonWriter implements JsonWriter<JsonAST.JValue>
{
    private final JsonWriter<JsonAST.JValue> parent;
    private final ListBuffer<Tuple2<String, JsonAST.JValue>> nodes;
    
    public JDoubleJObjectJsonWriter addNode(final Tuple2<String, JsonAST.JValue> node) {
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
        if (parent instanceof JDoubleAstJsonWriter) {
            jsonWriter = ((JDoubleAstJsonWriter)parent).addNode(this.result());
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
        return new JDoubleJFieldJsonWriter(name, this);
    }
    
    @Override
    public JsonWriter<JsonAST.JValue> addJValue(final JsonAST.JValue jv) {
        throw package$.MODULE$.error("You have to start a field to be able to end it (addJValue called before startField in a JObject builder)");
    }
    
    @Override
    public JsonAST.JValue result() {
        return org.json4s.package$.MODULE$.JObject().apply((List<Tuple2<String, JsonAST.JValue>>)this.nodes.toList());
    }
    
    public JDoubleJObjectJsonWriter(final JsonWriter<JsonAST.JValue> parent) {
        this.parent = parent;
        this.nodes = (ListBuffer<Tuple2<String, JsonAST.JValue>>)ListBuffer$.MODULE$.apply((Seq)Nil$.MODULE$);
    }
}
