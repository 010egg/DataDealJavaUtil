// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.scalap.scalasig;

import scala.Option;
import scala.reflect.ScalaSignature;

@ScalaSignature(bytes = "\u0006\u0001\u0019;Q!\u0001\u0002\t\u0002-\tabU2bY\u0006\u001c\u0016n\u001a)beN,'O\u0003\u0002\u0004\t\u0005A1oY1mCNLwM\u0003\u0002\u0006\r\u000511oY1mCBT!a\u0002\u0005\u0002\r)\u001cxN\u001c\u001bt\u0015\u0005I\u0011aA8sO\u000e\u0001\u0001C\u0001\u0007\u000e\u001b\u0005\u0011a!\u0002\b\u0003\u0011\u0003y!AD*dC2\f7+[4QCJ\u001cXM]\n\u0003\u001bA\u0001\"!\u0005\u000b\u000e\u0003IQ\u0011aE\u0001\u0006g\u000e\fG.Y\u0005\u0003+I\u0011a!\u00118z%\u00164\u0007\"B\f\u000e\t\u0003A\u0012A\u0002\u001fj]&$h\bF\u0001\f\u0011\u0015QR\u0002\"\u0001\u001c\u0003Y\u00198-\u00197b'&<gI]8n\u0003:tw\u000e^1uS>tGC\u0001\u000f#!\r\tRdH\u0005\u0003=I\u0011aa\u00149uS>t\u0007C\u0001\u0007!\u0013\t\t#A\u0001\u0005TG\u0006d\u0017mU5h\u0011\u0015\u0019\u0013\u00041\u0001%\u0003%\u0019G.Y:t\r&dW\r\u0005\u0002\rK%\u0011aE\u0001\u0002\n\u00072\f7o\u001d$jY\u0016DQ\u0001K\u0007\u0005\u0002%\nQc]2bY\u0006\u001c\u0016n\u001a$s_6\fE\u000f\u001e:jEV$X\r\u0006\u0002\u001dU!)1e\na\u0001I!)A&\u0004C\u0001[\u0005)\u0001/\u0019:tKR\u0011AD\f\u0005\u0006G-\u0002\r\u0001\n\u0005\u0006Y5!\t\u0001\r\u000b\u00039EBQAM\u0018A\u0002M\nQa\u00197buj\u0004$\u0001N\u001f\u0011\u0007UB4H\u0004\u0002\u0012m%\u0011qGE\u0001\u0007!J,G-\u001a4\n\u0005eR$!B\"mCN\u001c(BA\u001c\u0013!\taT\b\u0004\u0001\u0005\u0013y\n\u0014\u0011!A\u0001\u0006\u0003y$aA0%cE\u0011\u0001i\u0011\t\u0003#\u0005K!A\u0011\n\u0003\u000f9{G\u000f[5oOB\u0011\u0011\u0003R\u0005\u0003\u000bJ\u00111!\u00118z\u0001")
public final class ScalaSigParser
{
    public static Option<ScalaSig> parse(final Class<?> clazz) {
        return ScalaSigParser$.MODULE$.parse(clazz);
    }
    
    public static Option<ScalaSig> parse(final ClassFile classFile) {
        return ScalaSigParser$.MODULE$.parse(classFile);
    }
    
    public static Option<ScalaSig> scalaSigFromAttribute(final ClassFile classFile) {
        return ScalaSigParser$.MODULE$.scalaSigFromAttribute(classFile);
    }
    
    public static Option<ScalaSig> scalaSigFromAnnotation(final ClassFile classFile) {
        return ScalaSigParser$.MODULE$.scalaSigFromAnnotation(classFile);
    }
}
