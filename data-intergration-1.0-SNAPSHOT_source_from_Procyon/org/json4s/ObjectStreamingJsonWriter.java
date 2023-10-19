// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

import scala.math.BigDecimal;
import scala.math.BigInt;
import scala.sys.package$;
import scala.runtime.BoxedUnit;
import scala.reflect.ScalaSignature;
import java.io.Writer;

@ScalaSignature(bytes = "\u0006\u0001\u0005Md\u0001B\u0001\u0003\r\u001d\u0011\u0011d\u00142kK\u000e$8\u000b\u001e:fC6Lgn\u001a&t_:<&/\u001b;fe*\u00111\u0001B\u0001\u0007UN|g\u000eN:\u000b\u0003\u0015\t1a\u001c:h\u0007\u0001)\"\u0001C\b\u0014\u0005\u0001I\u0001c\u0001\u0006\f\u001b5\t!!\u0003\u0002\r\u0005\t\u00192\u000b\u001e:fC6Lgn\u001a&t_:<&/\u001b;feB\u0011ab\u0004\u0007\u0001\t\u0015\u0001\u0002A1\u0001\u0012\u0005\u0005!\u0016C\u0001\n\u0019!\t\u0019b#D\u0001\u0015\u0015\u0005)\u0012!B:dC2\f\u0017BA\f\u0015\u0005\u001dqu\u000e\u001e5j]\u001e\u0004\"!\u0007\u0010\u000e\u0003iQ!a\u0007\u000f\u0002\u0005%|'\"A\u000f\u0002\t)\fg/Y\u0005\u0003?i\u0011aa\u0016:ji\u0016\u0014\b\u0002C\u0011\u0001\u0005\u000b\u0007K\u0011\u0003\u0012\u0002\u000b9|G-Z:\u0016\u00035A\u0001\u0002\n\u0001\u0003\u0002\u0003\u0006I!D\u0001\u0007]>$Wm\u001d\u0011\t\u0011\u0019\u0002!Q1Q\u0005\u0012\u001d\nQ\u0001\\3wK2,\u0012\u0001\u000b\t\u0003'%J!A\u000b\u000b\u0003\u0007%sG\u000f\u0003\u0005-\u0001\t\u0005\t\u0015!\u0003)\u0003\u0019aWM^3mA!Aa\u0006\u0001B\u0001B\u0003%\u0011\"\u0001\u0004qCJ,g\u000e\u001e\u0005\ta\u0001\u0011)\u0019)C\tc\u00051\u0001O]3uif,\u0012A\r\t\u0003'MJ!\u0001\u000e\u000b\u0003\u000f\t{w\u000e\\3b]\"Aa\u0007\u0001B\u0001B\u0003%!'A\u0004qe\u0016$H/\u001f\u0011\t\u0011a\u0002!Q1Q\u0005\u0012\u001d\naa\u001d9bG\u0016\u001c\b\u0002\u0003\u001e\u0001\u0005\u0003\u0005\u000b\u0011\u0002\u0015\u0002\u000fM\u0004\u0018mY3tA!AA\b\u0001BCB\u0013EQ(A\u0004g_Jl\u0017\r^:\u0016\u0003y\u0002\"AC \n\u0005\u0001\u0013!a\u0002$pe6\fGo\u001d\u0005\t\u0005\u0002\u0011\t\u0011)A\u0005}\u0005Aam\u001c:nCR\u001c\b\u0005C\u0003E\u0001\u0011\u0005Q)\u0001\u0004=S:LGO\u0010\u000b\b\r\u001eC\u0015JS&M!\rQ\u0001!\u0004\u0005\u0006C\r\u0003\r!\u0004\u0005\u0006M\r\u0003\r\u0001\u000b\u0005\u0006]\r\u0003\r!\u0003\u0005\u0006a\r\u0003\rA\r\u0005\u0006q\r\u0003\r\u0001\u000b\u0005\u0006y\r\u0003\rA\u0010\u0005\u0007\u001d\u0002\u0001\u000b\u0015\u0002\u001a\u0002\u000f%\u001ch)\u001b:ti\")\u0001\u000b\u0001C\u0001E\u00051!/Z:vYRDQA\u0015\u0001\u0005\u0002M\u000bq!\u00193e\u001d>$W\r\u0006\u0002U/B\u0019!\"V\u0007\n\u0005Y\u0013!A\u0003&t_:<&/\u001b;fe\")\u0001,\u0015a\u00013\u0006!an\u001c3f!\tQVL\u0004\u0002\u00147&\u0011A\fF\u0001\u0007!J,G-\u001a4\n\u0005y{&AB*ue&twM\u0003\u0002])!)\u0011\r\u0001C!E\u0006IQM\u001c3PE*,7\r\u001e\u000b\u0002)\")A\r\u0001C\u0001K\u0006y\u0011\r\u001a3B]\u0012\fVo\u001c;f\u001d>$W\r\u0006\u0002UM\")\u0001l\u0019a\u00013\")\u0001\u000e\u0001C!E\u0006Q1\u000f^1si\u0006\u0013(/Y=\t\u000b)\u0004A\u0011\t2\u0002\u0011\u0015tG-\u0011:sCfDQ\u0001\u001c\u0001\u0005B\t\f1b\u001d;beR|%M[3di\")a\u000e\u0001C!_\u000611\u000f\u001e:j]\u001e$\"\u0001\u00169\t\u000bEl\u0007\u0019A-\u0002\u000bY\fG.^3\t\u000bM\u0004A\u0011\t;\u0002\t\tLH/\u001a\u000b\u0003)VDQ!\u001d:A\u0002Y\u0004\"aE<\n\u0005a$\"\u0001\u0002\"zi\u0016DQA\u001f\u0001\u0005Bm\f1!\u001b8u)\t!F\u0010C\u0003rs\u0002\u0007\u0001\u0006C\u0003\u007f\u0001\u0011\u0005s0\u0001\u0003m_:<Gc\u0001+\u0002\u0002!1\u0011/ a\u0001\u0003\u0007\u00012aEA\u0003\u0013\r\t9\u0001\u0006\u0002\u0005\u0019>tw\rC\u0004\u0002\f\u0001!\t%!\u0004\u0002\r\tLw-\u00138u)\r!\u0016q\u0002\u0005\bc\u0006%\u0001\u0019AA\t!\u0011\t\u0019\"a\t\u000f\t\u0005U\u0011q\u0004\b\u0005\u0003/\ti\"\u0004\u0002\u0002\u001a)\u0019\u00111\u0004\u0004\u0002\rq\u0012xn\u001c;?\u0013\u0005)\u0012bAA\u0011)\u00059\u0001/Y2lC\u001e,\u0017\u0002BA\u0013\u0003O\u0011aAQ5h\u0013:$(bAA\u0011)!9\u00111\u0006\u0001\u0005B\u00055\u0012a\u00022p_2,\u0017M\u001c\u000b\u0004)\u0006=\u0002BB9\u0002*\u0001\u0007!\u0007C\u0004\u00024\u0001!\t%!\u000e\u0002\u000bMDwN\u001d;\u0015\u0007Q\u000b9\u0004C\u0004r\u0003c\u0001\r!!\u000f\u0011\u0007M\tY$C\u0002\u0002>Q\u0011Qa\u00155peRDq!!\u0011\u0001\t\u0003\n\u0019%A\u0003gY>\fG\u000fF\u0002U\u0003\u000bBq!]A \u0001\u0004\t9\u0005E\u0002\u0014\u0003\u0013J1!a\u0013\u0015\u0005\u00151En\\1u\u0011\u001d\ty\u0005\u0001C!\u0003#\na\u0001Z8vE2,Gc\u0001+\u0002T!9\u0011/!\u0014A\u0002\u0005U\u0003cA\n\u0002X%\u0019\u0011\u0011\f\u000b\u0003\r\u0011{WO\u00197f\u0011\u001d\ti\u0006\u0001C!\u0003?\n!BY5h\t\u0016\u001c\u0017.\\1m)\r!\u0016\u0011\r\u0005\bc\u0006m\u0003\u0019AA2!\u0011\t\u0019\"!\u001a\n\t\u0005\u001d\u0014q\u0005\u0002\u000b\u0005&<G)Z2j[\u0006d\u0007bBA6\u0001\u0011\u0005\u0013QN\u0001\u000bgR\f'\u000f\u001e$jK2$Gc\u0001+\u0002p!9\u0011\u0011OA5\u0001\u0004I\u0016\u0001\u00028b[\u0016\u0004")
public final class ObjectStreamingJsonWriter<T extends Writer> extends StreamingJsonWriter<T>
{
    private final T nodes;
    private final int level;
    private final StreamingJsonWriter<T> parent;
    private final boolean pretty;
    private final int spaces;
    private final Formats formats;
    private boolean isFirst;
    
    @Override
    public T nodes() {
        return this.nodes;
    }
    
    @Override
    public int level() {
        return this.level;
    }
    
    @Override
    public boolean pretty() {
        return this.pretty;
    }
    
    @Override
    public int spaces() {
        return this.spaces;
    }
    
    @Override
    public Formats formats() {
        return this.formats;
    }
    
    @Override
    public T result() {
        return this.nodes();
    }
    
    @Override
    public JsonWriter<T> addNode(final String node) {
        if (this.isFirst) {
            this.isFirst = false;
        }
        else {
            this.nodes().write(",");
        }
        this.nodes().write(node);
        return this;
    }
    
    @Override
    public JsonWriter<T> endObject() {
        this.writePretty(2);
        this.nodes().write(125);
        return this.parent;
    }
    
    @Override
    public JsonWriter<T> addAndQuoteNode(final String node) {
        if (this.isFirst) {
            this.isFirst = false;
            final BoxedUnit unit = BoxedUnit.UNIT;
        }
        else {
            this.nodes().append((CharSequence)",");
        }
        this.nodes().append((CharSequence)"\"");
        ParserUtil$.MODULE$.quote(node, this.nodes(), this.formats());
        this.nodes().append((CharSequence)"\"");
        return this;
    }
    
    @Override
    public JsonWriter<T> startArray() {
        throw package$.MODULE$.error("You have to start a field to be able to end it (startArray called before startField in a JObject builder)");
    }
    
    @Override
    public JsonWriter<T> endArray() {
        throw package$.MODULE$.error("You have to start an array to be able to end it (endArray called before startArray)");
    }
    
    @Override
    public JsonWriter<T> startObject() {
        throw package$.MODULE$.error("You have to start a field to be able to end it (startObject called before startField in a JObject builder)");
    }
    
    @Override
    public JsonWriter<T> string(final String value) {
        throw package$.MODULE$.error("You have to start a field to be able to end it (string called before startField in a JObject builder)");
    }
    
    @Override
    public JsonWriter<T> byte(final byte value) {
        throw package$.MODULE$.error("You have to start a field to be able to end it (byte called before startField in a JObject builder)");
    }
    
    @Override
    public JsonWriter<T> int(final int value) {
        throw package$.MODULE$.error("You have to start a field to be able to end it (int called before startField in a JObject builder)");
    }
    
    @Override
    public JsonWriter<T> long(final long value) {
        throw package$.MODULE$.error("You have to start a field to be able to end it (long called before startField in a JObject builder)");
    }
    
    @Override
    public JsonWriter<T> bigInt(final BigInt value) {
        throw package$.MODULE$.error("You have to start a field to be able to end it (bigInt called before startField in a JObject builder)");
    }
    
    @Override
    public JsonWriter<T> boolean(final boolean value) {
        throw package$.MODULE$.error("You have to start a field to be able to end it (boolean called before startField in a JObject builder)");
    }
    
    @Override
    public JsonWriter<T> short(final short value) {
        throw package$.MODULE$.error("You have to start a field to be able to end it (short called before startField in a JObject builder)");
    }
    
    @Override
    public JsonWriter<T> float(final float value) {
        throw package$.MODULE$.error("You have to start a field to be able to end it (float called before startField in a JObject builder)");
    }
    
    @Override
    public JsonWriter<T> double(final double value) {
        throw package$.MODULE$.error("You have to start a field to be able to end it (double called before startField in a JObject builder)");
    }
    
    @Override
    public JsonWriter<T> bigDecimal(final BigDecimal value) {
        throw package$.MODULE$.error("You have to start a field to be able to end it (bigDecimal called before startField in a JObject builder)");
    }
    
    @Override
    public JsonWriter<T> startField(final String name) {
        final FieldStreamingJsonWriter r = new FieldStreamingJsonWriter(name, this.isFirst, this.nodes(), this.level(), (ObjectStreamingJsonWriter<T>)this, this.pretty(), this.spaces(), this.formats());
        if (this.isFirst) {
            this.isFirst = false;
        }
        return (JsonWriter<T>)r;
    }
    
    public ObjectStreamingJsonWriter(final T nodes, final int level, final StreamingJsonWriter<T> parent, final boolean pretty, final int spaces, final Formats formats) {
        this.nodes = nodes;
        this.level = level;
        this.parent = parent;
        this.pretty = pretty;
        this.spaces = spaces;
        this.formats = formats;
        nodes.write(123);
        this.writePretty(this.writePretty$default$1());
        this.isFirst = true;
    }
}
