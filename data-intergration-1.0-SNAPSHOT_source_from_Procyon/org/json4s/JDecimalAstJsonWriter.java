// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

import scala.math.BigDecimal;
import scala.reflect.ScalaSignature;

@ScalaSignature(bytes = "\u0006\u0001\u001d3Q!\u0001\u0002\u0002*\u001d\u0011QC\u0013#fG&l\u0017\r\\!ti*\u001bxN\\,sSR,'O\u0003\u0002\u0004\t\u00051!n]8oiMT\u0011!B\u0001\u0004_J<7\u0001A\n\u0003\u0001!\u0001\"!\u0003\u0006\u000e\u0003\tI!a\u0003\u0002\u0003!)3\u0016\r\\;f\u0015N|gn\u0016:ji\u0016\u0014\b\"B\u0007\u0001\t\u0003q\u0011A\u0002\u001fj]&$h\bF\u0001\u0010!\tI\u0001\u0001C\u0003\u0012\u0001\u0011\u0005!#\u0001\u0006ti\u0006\u0014H/\u0011:sCf$\u0012a\u0005\t\u0004\u0013Q1\u0012BA\u000b\u0003\u0005)Q5o\u001c8Xe&$XM\u001d\t\u0003/iq!!\u0003\r\n\u0005e\u0011\u0011a\u00029bG.\fw-Z\u0005\u00037q\u0011aA\u0013,bYV,'BA\r\u0003\u0011\u0015q\u0002\u0001\"\u0001\u0013\u0003-\u0019H/\u0019:u\u001f\nTWm\u0019;\t\u000b\u0001\u0002A\u0011A\u0011\u0002\u000b\u0019dw.\u0019;\u0015\u0005M\u0011\u0003\"B\u0012 \u0001\u0004!\u0013!\u0002<bYV,\u0007CA\u0013)\u001b\u00051#\"A\u0014\u0002\u000bM\u001c\u0017\r\\1\n\u0005%2#!\u0002$m_\u0006$\b\"B\u0016\u0001\t\u0003a\u0013A\u00023pk\ndW\r\u0006\u0002\u0014[!)1E\u000ba\u0001]A\u0011QeL\u0005\u0003a\u0019\u0012a\u0001R8vE2,\u0007\"\u0002\u001a\u0001\t\u0003\u0019\u0014A\u00032jO\u0012+7-[7bYR\u00111\u0003\u000e\u0005\u0006GE\u0002\r!\u000e\t\u0003mur!a\u000e\u001f\u000f\u0005aZT\"A\u001d\u000b\u0005i2\u0011A\u0002\u001fs_>$h(C\u0001(\u0013\tIb%\u0003\u0002?\u007f\tQ!)[4EK\u000eLW.\u00197\u000b\u0005e1\u0013\u0006\u0002\u0001B\u0007\u0016K!A\u0011\u0002\u00033)#UmY5nC2\f5\u000f\u001e*p_RT5o\u001c8Xe&$XM]\u0005\u0003\t\n\u0011\u0001D\u0013#fG&l\u0017\r\u001c&BeJ\f\u0017PS:p]^\u0013\u0018\u000e^3s\u0013\t1%A\u0001\rK\t\u0016\u001c\u0017.\\1m\u0015\u001aKW\r\u001c3Kg>twK]5uKJ\u0004")
public abstract class JDecimalAstJsonWriter extends JValueJsonWriter
{
    @Override
    public JsonWriter<JsonAST.JValue> startArray() {
        return new JDecimalJArrayJsonWriter(this);
    }
    
    @Override
    public JsonWriter<JsonAST.JValue> startObject() {
        return new JDecimalJObjectJsonWriter(this);
    }
    
    @Override
    public JsonWriter<JsonAST.JValue> float(final float value) {
        return this.double(value);
    }
    
    @Override
    public JsonWriter<JsonAST.JValue> double(final double value) {
        return this.addNode(package$.MODULE$.JDecimal().apply(scala.package$.MODULE$.BigDecimal().apply(value)));
    }
    
    @Override
    public JsonWriter<JsonAST.JValue> bigDecimal(final BigDecimal value) {
        return this.addNode(package$.MODULE$.JDecimal().apply(value));
    }
}
