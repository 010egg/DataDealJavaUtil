// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

import scala.math.BigDecimal;
import scala.reflect.ScalaSignature;

@ScalaSignature(bytes = "\u0006\u0001\u001d3Q!\u0001\u0002\u0002*\u001d\u0011AC\u0013#pk\ndW-Q:u\u0015N|gn\u0016:ji\u0016\u0014(BA\u0002\u0005\u0003\u0019Q7o\u001c85g*\tQ!A\u0002pe\u001e\u001c\u0001a\u0005\u0002\u0001\u0011A\u0011\u0011BC\u0007\u0002\u0005%\u00111B\u0001\u0002\u0011\u0015Z\u000bG.^3Kg>twK]5uKJDQ!\u0004\u0001\u0005\u00029\ta\u0001P5oSRtD#A\b\u0011\u0005%\u0001\u0001\"B\t\u0001\t\u0003\u0011\u0012AC:uCJ$\u0018I\u001d:bsR\t1\u0003E\u0002\n)YI!!\u0006\u0002\u0003\u0015)\u001bxN\\,sSR,'\u000f\u0005\u0002\u001859\u0011\u0011\u0002G\u0005\u00033\t\tq\u0001]1dW\u0006<W-\u0003\u0002\u001c9\t1!JV1mk\u0016T!!\u0007\u0002\t\u000by\u0001A\u0011\u0001\n\u0002\u0017M$\u0018M\u001d;PE*,7\r\u001e\u0005\u0006A\u0001!\t!I\u0001\u0006M2|\u0017\r\u001e\u000b\u0003'\tBQaI\u0010A\u0002\u0011\nQA^1mk\u0016\u0004\"!\n\u0015\u000e\u0003\u0019R\u0011aJ\u0001\u0006g\u000e\fG.Y\u0005\u0003S\u0019\u0012QA\u00127pCRDQa\u000b\u0001\u0005\u00021\na\u0001Z8vE2,GCA\n.\u0011\u0015\u0019#\u00061\u0001/!\t)s&\u0003\u00021M\t1Ai\\;cY\u0016DQA\r\u0001\u0005\u0002M\n!BY5h\t\u0016\u001c\u0017.\\1m)\t\u0019B\u0007C\u0003$c\u0001\u0007Q\u0007\u0005\u00027{9\u0011q\u0007\u0010\b\u0003qmj\u0011!\u000f\u0006\u0003u\u0019\ta\u0001\u0010:p_Rt\u0014\"A\u0014\n\u0005e1\u0013B\u0001 @\u0005)\u0011\u0015n\u001a#fG&l\u0017\r\u001c\u0006\u00033\u0019JC\u0001A!D\u000b&\u0011!I\u0001\u0002\u0019\u0015\u0012{WO\u00197f\u0003N$(k\\8u\u0015N|gn\u0016:ji\u0016\u0014\u0018B\u0001#\u0003\u0005]QEi\\;cY\u0016T\u0015I\u001d:bs*\u001bxN\\,sSR,'/\u0003\u0002G\u0005\t9\"\nR8vE2,'JR5fY\u0012T5o\u001c8Xe&$XM\u001d")
public abstract class JDoubleAstJsonWriter extends JValueJsonWriter
{
    @Override
    public JsonWriter<JsonAST.JValue> startArray() {
        return new JDoubleJArrayJsonWriter(this);
    }
    
    @Override
    public JsonWriter<JsonAST.JValue> startObject() {
        return new JDoubleJObjectJsonWriter(this);
    }
    
    @Override
    public JsonWriter<JsonAST.JValue> float(final float value) {
        return this.addNode(package$.MODULE$.JDouble().apply(value));
    }
    
    @Override
    public JsonWriter<JsonAST.JValue> double(final double value) {
        return this.addNode(package$.MODULE$.JDouble().apply(value));
    }
    
    @Override
    public JsonWriter<JsonAST.JValue> bigDecimal(final BigDecimal value) {
        return this.addNode(package$.MODULE$.JDouble().apply(value.doubleValue()));
    }
}
