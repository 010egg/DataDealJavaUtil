// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

import scala.reflect.ScalaSignature;
import scala.Dynamic;

@ScalaSignature(bytes = "\u0006\u0001)3A!\u0001\u0002\u0001\u000f\tiA)\u001f8b[&\u001c'JV1mk\u0016T!a\u0001\u0003\u0002\r)\u001cxN\u001c\u001bt\u0015\u0005)\u0011aA8sO\u000e\u00011c\u0001\u0001\t\u001dA\u0011\u0011\u0002D\u0007\u0002\u0015)\t1\"A\u0003tG\u0006d\u0017-\u0003\u0002\u000e\u0015\t1\u0011I\\=SK\u001a\u0004\"!C\b\n\u0005AQ!a\u0002#z]\u0006l\u0017n\u0019\u0005\t%\u0001\u0011)\u0019!C\u0001'\u0005\u0019!/Y<\u0016\u0003Q\u0001\"!F\r\u000f\u0005Y9R\"\u0001\u0002\n\u0005a\u0011\u0011a\u00029bG.\fw-Z\u0005\u00035m\u0011aA\u0013,bYV,'B\u0001\r\u0003\u0011!i\u0002A!A!\u0002\u0013!\u0012\u0001\u0002:bo\u0002BQa\b\u0001\u0005\u0002\u0001\na\u0001P5oSRtDCA\u0011#!\t1\u0002\u0001C\u0003\u0013=\u0001\u0007A\u0003C\u0003%\u0001\u0011\u0005Q%A\u0007tK2,7\r\u001e#z]\u0006l\u0017n\u0019\u000b\u0003C\u0019BQaJ\u0012A\u0002!\nAA\\1nKB\u0011\u0011\u0006\f\b\u0003\u0013)J!a\u000b\u0006\u0002\rA\u0013X\rZ3g\u0013\ticF\u0001\u0004TiJLgn\u001a\u0006\u0003W)AQ\u0001\r\u0001\u0005BE\n\u0001\u0002[1tQ\u000e{G-\u001a\u000b\u0002eA\u0011\u0011bM\u0005\u0003i)\u00111!\u00138u\u0011\u00151\u0004\u0001\"\u00118\u0003\u0019)\u0017/^1mgR\u0011\u0001h\u000f\t\u0003\u0013eJ!A\u000f\u0006\u0003\u000f\t{w\u000e\\3b]\")A(\u000ea\u0001{\u0005\u0011\u0001/\r\t\u0003\u0013yJ!a\u0010\u0006\u0003\u0007\u0005s\u0017pB\u0003B\u0005!\u0005!)A\u0007Es:\fW.[2K-\u0006dW/\u001a\t\u0003-\r3Q!\u0001\u0002\t\u0002\u0011\u001b2a\u0011\u0005F!\t1b)\u0003\u0002H\u0005\t1B)\u001f8b[&\u001c'JV1mk\u0016LU\u000e\u001d7jG&$8\u000fC\u0003 \u0007\u0012\u0005\u0011\nF\u0001C\u0001")
public class DynamicJValue implements Dynamic
{
    private final JsonAST.JValue raw;
    
    public static DynamicJValue dyn(final JsonAST.JValue jv) {
        return DynamicJValue$.MODULE$.dyn(jv);
    }
    
    public static MonadicJValue dynamic2monadic(final DynamicJValue dynJv) {
        return DynamicJValue$.MODULE$.dynamic2monadic(dynJv);
    }
    
    public static JsonAST.JValue dynamic2Jv(final DynamicJValue dynJv) {
        return DynamicJValue$.MODULE$.dynamic2Jv(dynJv);
    }
    
    public JsonAST.JValue raw() {
        return this.raw;
    }
    
    public DynamicJValue selectDynamic(final String name) {
        return new DynamicJValue(package$.MODULE$.jvalue2monadic(this.raw()).$bslash(name));
    }
    
    @Override
    public int hashCode() {
        return this.raw().hashCode();
    }
    
    @Override
    public boolean equals(final Object p1) {
        boolean b2;
        if (p1 instanceof DynamicJValue) {
            final DynamicJValue dynamicJValue = (DynamicJValue)p1;
            final JsonAST.JValue raw = this.raw();
            final JsonAST.JValue raw2 = dynamicJValue.raw();
            boolean b = false;
            Label_0050: {
                Label_0049: {
                    if (raw == null) {
                        if (raw2 != null) {
                            break Label_0049;
                        }
                    }
                    else if (!raw.equals(raw2)) {
                        break Label_0049;
                    }
                    b = true;
                    break Label_0050;
                }
                b = false;
            }
            b2 = b;
        }
        else if (p1 instanceof JsonAST.JValue) {
            final JsonAST.JValue value = (JsonAST.JValue)p1;
            final JsonAST.JValue raw3 = this.raw();
            final JsonAST.JValue obj = value;
            boolean b3 = false;
            Label_0102: {
                Label_0101: {
                    if (raw3 == null) {
                        if (obj != null) {
                            break Label_0101;
                        }
                    }
                    else if (!raw3.equals(obj)) {
                        break Label_0101;
                    }
                    b3 = true;
                    break Label_0102;
                }
                b3 = false;
            }
            b2 = b3;
        }
        else {
            b2 = false;
        }
        return b2;
    }
    
    public DynamicJValue(final JsonAST.JValue raw) {
        this.raw = raw;
    }
}
