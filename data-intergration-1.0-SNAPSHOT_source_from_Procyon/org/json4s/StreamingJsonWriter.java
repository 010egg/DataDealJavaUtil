// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

import scala.collection.immutable.StringOps;
import scala.Predef$;
import scala.collection.immutable.Set;
import scala.collection.immutable.List;
import scala.MatchError;
import scala.Function1;
import scala.math.BigDecimal;
import scala.math.BigInt;
import scala.runtime.BoxesRunTime;
import scala.sys.package$;
import scala.reflect.ScalaSignature;
import java.io.Writer;

@ScalaSignature(bytes = "\u0006\u0001\u00055g!B\u0001\u0003\u0003S9!aE*ue\u0016\fW.\u001b8h\u0015N|gn\u0016:ji\u0016\u0014(BA\u0002\u0005\u0003\u0019Q7o\u001c85g*\tQ!A\u0002pe\u001e\u001c\u0001!\u0006\u0002\t+M\u0019\u0001!C\b\u0011\u0005)iQ\"A\u0006\u000b\u00031\tQa]2bY\u0006L!AD\u0006\u0003\r\u0005s\u0017PU3g!\r\u0001\u0012cE\u0007\u0002\u0005%\u0011!C\u0001\u0002\u000b\u0015N|gn\u0016:ji\u0016\u0014\bC\u0001\u000b\u0016\u0019\u0001!QA\u0006\u0001C\u0002]\u0011\u0011\u0001V\t\u00031m\u0001\"AC\r\n\u0005iY!a\u0002(pi\"Lgn\u001a\t\u00039\u0005j\u0011!\b\u0006\u0003=}\t!![8\u000b\u0003\u0001\nAA[1wC&\u0011!%\b\u0002\u0007/JLG/\u001a:\t\u000b\u0011\u0002A\u0011A\u0013\u0002\rqJg.\u001b;?)\u00051\u0003c\u0001\t\u0001'!1\u0001\u0006\u0001Q\u0007\u0012%\nQ\u0001\\3wK2,\u0012A\u000b\t\u0003\u0015-J!\u0001L\u0006\u0003\u0007%sG\u000f\u0003\u0004/\u0001\u00016\t\"K\u0001\u0007gB\f7-Z:\t\rA\u0002\u0001U\"\u00052\u0003\u0019\u0001(/\u001a;usV\t!\u0007\u0005\u0002\u000bg%\u0011Ag\u0003\u0002\b\u0005>|G.Z1o\u0011\u00191\u0004\u0001)D\to\u0005)an\u001c3fgV\t1\u0003C\u0004:\u0001\t\u0007k\u0011\u0003\u001e\u0002\u000f\u0019|'/\\1ugV\t1\b\u0005\u0002\u0011y%\u0011QH\u0001\u0002\b\r>\u0014X.\u0019;t\u0011\u0015y\u0004\u0001\"\u0001A\u0003)\u0019H/\u0019:u\u0003J\u0014\u0018-\u001f\u000b\u0002\u001f!)!\t\u0001C\u0001\u0001\u0006Y1\u000f^1si>\u0013'.Z2u\u0011\u0015!\u0005A\"\u0001F\u0003\u001d\tG\r\u001a(pI\u0016$\"a\u0004$\t\u000b\u001d\u001b\u0005\u0019\u0001%\u0002\t9|G-\u001a\t\u0003\u00132s!A\u0003&\n\u0005-[\u0011A\u0002)sK\u0012,g-\u0003\u0002N\u001d\n11\u000b\u001e:j]\u001eT!aS\u0006\t\u000bA\u0003a\u0011A)\u0002\u001f\u0005$G-\u00118e#V|G/\u001a(pI\u0016$\"a\u0004*\t\u000b\u001d{\u0005\u0019\u0001%\t\u000bQ\u0003A\u0011\u0001!\u0002\u0013\u0015tGm\u00142kK\u000e$\b\"\u0002,\u0001\t\u00039\u0016AC:uCJ$h)[3mIR\u0011q\u0002\u0017\u0005\u00063V\u0003\r\u0001S\u0001\u0005]\u0006lW\rC\u0003\\\u0001\u0011\u0005A,\u0001\u0004tiJLgn\u001a\u000b\u0003\u001fuCQA\u0018.A\u0002!\u000bQA^1mk\u0016DQ\u0001\u0019\u0001\u0005\u0002\u0005\fAAY=uKR\u0011qB\u0019\u0005\u0006=~\u0003\ra\u0019\t\u0003\u0015\u0011L!!Z\u0006\u0003\t\tKH/\u001a\u0005\u0006O\u0002!\t\u0001[\u0001\u0004S:$HCA\bj\u0011\u0015qf\r1\u0001+\u0011\u0015Y\u0007\u0001\"\u0001m\u0003\u0011awN\\4\u0015\u0005=i\u0007\"\u00020k\u0001\u0004q\u0007C\u0001\u0006p\u0013\t\u00018B\u0001\u0003M_:<\u0007\"\u0002:\u0001\t\u0003\u0019\u0018A\u00022jO&sG\u000f\u0006\u0002\u0010i\")a,\u001da\u0001kB\u0011aO \b\u0003ort!\u0001_>\u000e\u0003eT!A\u001f\u0004\u0002\rq\u0012xn\u001c;?\u0013\u0005a\u0011BA?\f\u0003\u001d\u0001\u0018mY6bO\u0016L1a`A\u0001\u0005\u0019\u0011\u0015nZ%oi*\u0011Qp\u0003\u0005\b\u0003\u000b\u0001A\u0011AA\u0004\u0003\u001d\u0011wn\u001c7fC:$2aDA\u0005\u0011\u0019q\u00161\u0001a\u0001e!9\u0011Q\u0002\u0001\u0005\u0002\u0005=\u0011!B:i_J$HcA\b\u0002\u0012!9a,a\u0003A\u0002\u0005M\u0001c\u0001\u0006\u0002\u0016%\u0019\u0011qC\u0006\u0003\u000bMCwN\u001d;\t\r\u0005m\u0001\u0001\"\u0001A\u0003!)g\u000eZ!se\u0006L\bbBA\u0010\u0001\u0011\u0005\u0011\u0011E\u0001\u0006M2|\u0017\r\u001e\u000b\u0004\u001f\u0005\r\u0002b\u00020\u0002\u001e\u0001\u0007\u0011Q\u0005\t\u0004\u0015\u0005\u001d\u0012bAA\u0015\u0017\t)a\t\\8bi\"9\u0011Q\u0006\u0001\u0005\u0002\u0005=\u0012A\u00023pk\ndW\rF\u0002\u0010\u0003cAqAXA\u0016\u0001\u0004\t\u0019\u0004E\u0002\u000b\u0003kI1!a\u000e\f\u0005\u0019!u.\u001e2mK\"9\u00111\b\u0001\u0005\u0002\u0005u\u0012A\u00032jO\u0012+7-[7bYR\u0019q\"a\u0010\t\u000fy\u000bI\u00041\u0001\u0002BA\u0019a/a\u0011\n\t\u0005\u0015\u0013\u0011\u0001\u0002\u000b\u0005&<G)Z2j[\u0006d\u0007bBA%\u0001\u0011\u0005\u00111J\u0001\re\u0016\u001cX\u000f\u001c;TiJLgnZ\u000b\u0002\u0011\"9\u0011q\n\u0001\u0005\u0002\u0005E\u0013!C1eI*3\u0016\r\\;f)\ry\u00111\u000b\u0005\t\u0003+\ni\u00051\u0001\u0002X\u0005\u0011!N\u001e\t\u0005\u00033\niFD\u0002\u0011\u00037J!! \u0002\n\t\u0005}\u0013\u0011\r\u0002\u0007\u0015Z\u000bG.^3\u000b\u0005u\u0014\u0001bBA3\u0001\u0011E\u0011qM\u0001\foJLG/\u001a)sKR$\u0018\u0010\u0006\u0003\u0002j\u0005=\u0004c\u0001\u0006\u0002l%\u0019\u0011QN\u0006\u0003\tUs\u0017\u000e\u001e\u0005\n\u0003c\n\u0019\u0007%AA\u0002)\nqa\\;uI\u0016tG\u000fC\u0005\u0002v\u0001\t\n\u0011\"\u0005\u0002x\u0005)rO]5uKB\u0013X\r\u001e;zI\u0011,g-Y;mi\u0012\nTCAA=U\rQ\u00131P\u0016\u0003\u0003{\u0002B!a \u0002\n6\u0011\u0011\u0011\u0011\u0006\u0005\u0003\u0007\u000b))A\u0005v]\u000eDWmY6fI*\u0019\u0011qQ\u0006\u0002\u0015\u0005tgn\u001c;bi&|g.\u0003\u0003\u0002\f\u0006\u0005%!E;oG\",7m[3e-\u0006\u0014\u0018.\u00198dK&J\u0001!a$\u0002\u0014\u0006]\u00151T\u0005\u0004\u0003#\u0013!\u0001G!se\u0006L8\u000b\u001e:fC6Lgn\u001a&t_:<&/\u001b;fe&\u0019\u0011Q\u0013\u0002\u00031\u0019KW\r\u001c3TiJ,\u0017-\\5oO*\u001bxN\\,sSR,'/C\u0002\u0002\u001a\n\u0011\u0011d\u00142kK\u000e$8\u000b\u001e:fC6Lgn\u001a&t_:<&/\u001b;fe&\u0019\u0011Q\u0014\u0002\u0003/I{w\u000e^*ue\u0016\fW.\u001b8h\u0015N|gn\u0016:ji\u0016\u0014x\u0001CAQ\u0005!\u0005!!a)\u0002'M#(/Z1nS:<'j]8o/JLG/\u001a:\u0011\u0007A\t)KB\u0004\u0002\u0005!\u0005!!a*\u0014\u0007\u0005\u0015\u0016\u0002C\u0004%\u0003K#\t!a+\u0015\u0005\u0005\r\u0006\"CAX\u0003K\u0003\u000b\u0011BAY\u00039\u0001xn]%oM&t\u0017\u000e^=WC2\u0004B!a-\u0002:6\u0011\u0011Q\u0017\u0006\u0004\u0003o{\u0012\u0001\u00027b]\u001eL1!TA[\u0011%\ti,!*!\u0002\u0013\t\t,\u0001\boK\u001eLeNZ5oSR,g+\u00197\t\u0013\u0005\u0005\u0017Q\u0015C\u0001\u0005\u0005\r\u0017A\u00045b]\u0012dW-\u00138gS:LG/\u001f\u000b\u0004\u0011\u0006\u0015\u0007b\u00020\u0002@\u0002\u0007\u0011Q\u0005\u0005\n\u0003\u0003\f)\u000b\"\u0001\u0003\u0003\u0013$2\u0001SAf\u0011\u001dq\u0016q\u0019a\u0001\u0003g\u0001")
public abstract class StreamingJsonWriter<T extends Writer> implements JsonWriter<T>
{
    public abstract int level();
    
    public abstract int spaces();
    
    public abstract boolean pretty();
    
    public abstract T nodes();
    
    public abstract Formats formats();
    
    @Override
    public JsonWriter<T> startArray() {
        return new ArrayStreamingJsonWriter<T>(this.nodes(), this.level() + 1, this, this.pretty(), this.spaces(), this.formats());
    }
    
    @Override
    public JsonWriter<T> startObject() {
        return new ObjectStreamingJsonWriter<T>(this.nodes(), this.level() + 1, this, this.pretty(), this.spaces(), this.formats());
    }
    
    public abstract JsonWriter<T> addNode(final String p0);
    
    public abstract JsonWriter<T> addAndQuoteNode(final String p0);
    
    @Override
    public JsonWriter<T> endObject() {
        throw package$.MODULE$.error("You have to start an object to be able to end it (endObject called before startObject)");
    }
    
    @Override
    public JsonWriter<T> startField(final String name) {
        throw package$.MODULE$.error("You have to start an object before starting a field.");
    }
    
    @Override
    public JsonWriter<T> string(final String value) {
        return this.addAndQuoteNode(value);
    }
    
    @Override
    public JsonWriter<T> byte(final byte value) {
        return this.addNode(BoxesRunTime.boxToByte(value).toString());
    }
    
    @Override
    public JsonWriter<T> int(final int value) {
        return this.addNode(BoxesRunTime.boxToInteger(value).toString());
    }
    
    @Override
    public JsonWriter<T> long(final long value) {
        return this.addNode(BoxesRunTime.boxToLong(value).toString());
    }
    
    @Override
    public JsonWriter<T> bigInt(final BigInt value) {
        return this.addNode(value.toString());
    }
    
    @Override
    public JsonWriter<T> boolean(final boolean value) {
        return this.addNode(value ? "true" : "false");
    }
    
    @Override
    public JsonWriter<T> short(final short value) {
        return this.addNode(BoxesRunTime.boxToShort(value).toString());
    }
    
    @Override
    public JsonWriter<T> endArray() {
        throw package$.MODULE$.error("You have to start an object to be able to end it (endArray called before startArray)");
    }
    
    @Override
    public JsonWriter<T> float(final float value) {
        return this.addNode(StreamingJsonWriter$.MODULE$.handleInfinity(value));
    }
    
    @Override
    public JsonWriter<T> double(final double value) {
        return this.addNode(StreamingJsonWriter$.MODULE$.handleInfinity(value));
    }
    
    @Override
    public JsonWriter<T> bigDecimal(final BigDecimal value) {
        return this.addNode(value.toString());
    }
    
    public String resultString() {
        return this.result().toString();
    }
    
    @Override
    public JsonWriter<T> addJValue(final JsonAST.JValue jv) {
        final JsonAST.JNull$ jNull = org.json4s.package$.MODULE$.JNull();
        Label_0040: {
            if (jNull == null) {
                if (jv != null) {
                    break Label_0040;
                }
            }
            else if (!jNull.equals(jv)) {
                break Label_0040;
            }
            return this.addNode("null");
        }
        JsonWriter<T> jsonWriter;
        if (jv instanceof JsonAST.JString) {
            final String str = ((JsonAST.JString)jv).s();
            jsonWriter = this.string(str);
        }
        else if (jv instanceof JsonAST.JInt) {
            final BigInt i = ((JsonAST.JInt)jv).num();
            jsonWriter = this.bigInt(i);
        }
        else if (jv instanceof JsonAST.JLong) {
            final long j = ((JsonAST.JLong)jv).num();
            jsonWriter = this.long(j);
        }
        else if (jv instanceof JsonAST.JDouble) {
            final double d = ((JsonAST.JDouble)jv).num();
            jsonWriter = this.double(d);
        }
        else if (jv instanceof JsonAST.JDecimal) {
            final BigDecimal d2 = ((JsonAST.JDecimal)jv).num();
            jsonWriter = this.bigDecimal(d2);
        }
        else if (jv instanceof JsonAST.JBool) {
            final boolean b = ((JsonAST.JBool)jv).value();
            jsonWriter = this.boolean(b);
        }
        else if (jv instanceof JsonAST.JArray) {
            final List arr = ((JsonAST.JArray)jv).arr();
            final JsonWriter ab = this.startArray();
            arr.foreach((Function1)new StreamingJsonWriter$$anonfun$addJValue.StreamingJsonWriter$$anonfun$addJValue$1(this, ab));
            jsonWriter = ab.endArray();
        }
        else if (jv instanceof JsonAST.JSet) {
            final Set s = ((JsonAST.JSet)jv).set();
            final JsonWriter ab2 = this.startArray();
            s.foreach((Function1)new StreamingJsonWriter$$anonfun$addJValue.StreamingJsonWriter$$anonfun$addJValue$2(this, ab2));
            jsonWriter = ab2.endArray();
        }
        else {
            if (!(jv instanceof JsonAST.JObject)) {
                final JsonAST.JNothing$ jNothing = org.json4s.package$.MODULE$.JNothing();
                if (jNothing == null) {
                    if (jv != null) {
                        throw new MatchError((Object)jv);
                    }
                }
                else if (!jNothing.equals(jv)) {
                    throw new MatchError((Object)jv);
                }
                jsonWriter = this;
                return jsonWriter;
            }
            final List flds = ((JsonAST.JObject)jv).obj();
            final JsonWriter obj = this.startObject();
            flds.foreach((Function1)new StreamingJsonWriter$$anonfun$addJValue.StreamingJsonWriter$$anonfun$addJValue$3(this, obj));
            jsonWriter = obj.endObject();
        }
        return jsonWriter;
    }
    
    public void writePretty(final int outdent) {
        if (this.pretty()) {
            this.nodes().write(10);
            this.nodes().write(new StringOps(Predef$.MODULE$.augmentString(" ")).$times(this.level() * this.spaces() - outdent));
        }
    }
    
    public int writePretty$default$1() {
        return 0;
    }
}
