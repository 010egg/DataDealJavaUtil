// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.scalap.scalasig;

import scala.reflect.ClassTag$;
import scala.collection.Seq$;
import scala.collection.TraversableOnce;
import scala.MatchError;
import scala.collection.Seq;
import scala.Some;
import scala.Function1;
import scala.Function0;
import org.json4s.scalap.Main$;
import scala.Option;

public final class ScalaSigParser$
{
    public static final ScalaSigParser$ MODULE$;
    
    static {
        new ScalaSigParser$();
    }
    
    public Option<ScalaSig> scalaSigFromAnnotation(final ClassFile classFile) {
        return (Option<ScalaSig>)classFile.annotation(Main$.MODULE$.SCALA_SIG_ANNOTATION()).orElse((Function0)new ScalaSigParser$$anonfun$scalaSigFromAnnotation.ScalaSigParser$$anonfun$scalaSigFromAnnotation$1(classFile)).map((Function1)new ScalaSigParser$$anonfun$scalaSigFromAnnotation.ScalaSigParser$$anonfun$scalaSigFromAnnotation$2(classFile));
    }
    
    public Option<ScalaSig> scalaSigFromAttribute(final ClassFile classFile) {
        return (Option<ScalaSig>)classFile.attribute(Main$.MODULE$.SCALA_SIG()).map((Function1)new ScalaSigParser$$anonfun$scalaSigFromAttribute.ScalaSigParser$$anonfun$scalaSigFromAttribute$1()).map((Function1)new ScalaSigParser$$anonfun$scalaSigFromAttribute.ScalaSigParser$$anonfun$scalaSigFromAttribute$2());
    }
    
    public Option<ScalaSig> parse(final ClassFile classFile) {
        final Option<ScalaSig> scalaSigFromAttribute;
        final Option scalaSig = scalaSigFromAttribute = this.scalaSigFromAttribute(classFile);
        if (scalaSigFromAttribute instanceof Some) {
            final ScalaSig scalaSig2 = (ScalaSig)((Some)scalaSigFromAttribute).x();
            if (scalaSig2 != null) {
                final Seq entries = scalaSig2.table();
                if (entries.length() == 0) {
                    return this.scalaSigFromAnnotation(classFile);
                }
            }
        }
        return scalaSigFromAttribute;
    }
    
    public Option<ScalaSig> parse(final Class<?> clazz) {
        final ByteCode byteCode = ByteCode$.MODULE$.forClass(clazz);
        final ClassFile classFile = ClassFileParser$.MODULE$.parse(byteCode);
        return this.parse(classFile);
    }
    
    public final byte[] org$json4s$scalap$scalasig$ScalaSigParser$$getBytes$1(final ClassFileParser.AnnotationElement bytesElem, final ClassFile classFile$1) {
        final ClassFileParser.ElementValue elementValue = bytesElem.elementValue();
        byte[] array;
        if (elementValue instanceof ClassFileParser.ConstValueIndex) {
            final int index = ((ClassFileParser.ConstValueIndex)elementValue).index();
            array = this.org$json4s$scalap$scalasig$ScalaSigParser$$bytesForIndex$1(index, classFile$1);
        }
        else {
            if (!(elementValue instanceof ClassFileParser.ArrayValue)) {
                throw new MatchError((Object)elementValue);
            }
            final Seq signatureParts = ((ClassFileParser.ArrayValue)elementValue).values();
            array = this.mergedLongSignatureBytes$1(signatureParts, classFile$1);
        }
        return array;
    }
    
    private final byte[] mergedLongSignatureBytes$1(final Seq signatureParts, final ClassFile classFile$1) {
        return (byte[])((TraversableOnce)signatureParts.flatMap((Function1)new ScalaSigParser$$anonfun$mergedLongSignatureBytes$1.ScalaSigParser$$anonfun$mergedLongSignatureBytes$1$1(classFile$1), Seq$.MODULE$.canBuildFrom())).toArray(ClassTag$.MODULE$.Byte());
    }
    
    public final byte[] org$json4s$scalap$scalasig$ScalaSigParser$$bytesForIndex$1(final int index, final ClassFile classFile$1) {
        return ((StringBytesPair)classFile$1.constantWrapped(index)).bytes();
    }
    
    private ScalaSigParser$() {
        MODULE$ = this;
    }
}
