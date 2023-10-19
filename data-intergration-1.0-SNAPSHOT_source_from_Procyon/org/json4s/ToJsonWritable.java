// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

import scala.reflect.ScalaSignature;

@ScalaSignature(bytes = "\u0006\u0001M2A!\u0001\u0002\u0001\u000f\tqAk\u001c&t_:<&/\u001b;bE2,'BA\u0002\u0005\u0003\u0019Q7o\u001c85g*\tQ!A\u0002pe\u001e\u001c\u0001!\u0006\u0002\t'M\u0011\u0001!\u0003\t\u0003\u00155i\u0011a\u0003\u0006\u0002\u0019\u0005)1oY1mC&\u0011ab\u0003\u0002\u0007\u0003:L(+\u001a4\t\u0011A\u0001!\u0011!Q\u0001\nE\t\u0011!\u0019\t\u0003%Ma\u0001\u0001B\u0003\u0015\u0001\t\u0007QCA\u0001U#\t1\u0012\u0004\u0005\u0002\u000b/%\u0011\u0001d\u0003\u0002\b\u001d>$\b.\u001b8h!\tQ!$\u0003\u0002\u001c\u0017\t\u0019\u0011I\\=\t\u0011u\u0001!\u0011!Q\u0001\fy\taa\u001e:ji\u0016\u0014\bcA\u0010!#5\t!!\u0003\u0002\"\u0005\t1qK]5uKJDQa\t\u0001\u0005\u0002\u0011\na\u0001P5oSRtDCA\u0013))\t1s\u0005E\u0002 \u0001EAQ!\b\u0012A\u0004yAQ\u0001\u0005\u0012A\u0002EAQA\u000b\u0001\u0005\u0002-\n\u0001\"Y:K-\u0006dW/Z\u000b\u0002YA\u0011Q\u0006\r\b\u0003?9J!a\f\u0002\u0002\u000fA\f7m[1hK&\u0011\u0011G\r\u0002\u0007\u0015Z\u000bG.^3\u000b\u0005=\u0012\u0001")
public class ToJsonWritable<T>
{
    private final T a;
    private final Writer<T> writer;
    
    public JsonAST.JValue asJValue() {
        return this.writer.write(this.a);
    }
    
    public ToJsonWritable(final T a, final Writer<T> writer) {
        this.a = a;
        this.writer = writer;
    }
}
