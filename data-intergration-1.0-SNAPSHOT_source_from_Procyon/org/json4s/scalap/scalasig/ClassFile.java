// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.scalap.scalasig;

import scala.Product$class;
import scala.runtime.ScalaRunTime$;
import scala.collection.Iterator;
import scala.runtime.BoxesRunTime;
import scala.collection.Seq$;
import scala.Function1;
import scala.Tuple4;
import scala.Option;
import scala.collection.Seq;
import scala.reflect.ScalaSignature;
import scala.Serializable;
import scala.Product;

@ScalaSignature(bytes = "\u0006\u0001\u0005}h\u0001B\u0001\u0003\u0001.\u0011\u0011b\u00117bgN4\u0015\u000e\\3\u000b\u0005\r!\u0011\u0001C:dC2\f7/[4\u000b\u0005\u00151\u0011AB:dC2\f\u0007O\u0003\u0002\b\u0011\u00051!n]8oiMT\u0011!C\u0001\u0004_J<7\u0001A\n\u0005\u00011\u0011R\u0003\u0005\u0002\u000e!5\taBC\u0001\u0010\u0003\u0015\u00198-\u00197b\u0013\t\tbB\u0001\u0004B]f\u0014VM\u001a\t\u0003\u001bMI!\u0001\u0006\b\u0003\u000fA\u0013x\u000eZ;diB\u0011QBF\u0005\u0003/9\u0011AbU3sS\u0006d\u0017N_1cY\u0016D\u0001\"\u0007\u0001\u0003\u0016\u0004%\tAG\u0001\u0007Q\u0016\fG-\u001a:\u0016\u0003m\u0001\"\u0001H\u000f\u000e\u0003\tI!A\b\u0002\u0003\u001f\rc\u0017m]:GS2,\u0007*Z1eKJD\u0001\u0002\t\u0001\u0003\u0012\u0003\u0006IaG\u0001\bQ\u0016\fG-\u001a:!\u0011!\u0011\u0003A!f\u0001\n\u0003\u0019\u0013A\u00024jK2$7/F\u0001%!\r)S\u0006\r\b\u0003M-r!a\n\u0016\u000e\u0003!R!!\u000b\u0006\u0002\rq\u0012xn\u001c;?\u0013\u0005y\u0011B\u0001\u0017\u000f\u0003\u001d\u0001\u0018mY6bO\u0016L!AL\u0018\u0003\u0007M+\u0017O\u0003\u0002-\u001dA\u0011A$M\u0005\u0003e\t\u0011QAR5fY\u0012D\u0001\u0002\u000e\u0001\u0003\u0012\u0003\u0006I\u0001J\u0001\bM&,G\u000eZ:!\u0011!1\u0004A!f\u0001\n\u00039\u0014aB7fi\"|Gm]\u000b\u0002qA\u0019Q%L\u001d\u0011\u0005qQ\u0014BA\u001e\u0003\u0005\u0019iU\r\u001e5pI\"AQ\b\u0001B\tB\u0003%\u0001(\u0001\u0005nKRDw\u000eZ:!\u0011!y\u0004A!f\u0001\n\u0003\u0001\u0015AC1uiJL'-\u001e;fgV\t\u0011\tE\u0002&[\t\u0003\"\u0001H\"\n\u0005\u0011\u0013!!C!uiJL'-\u001e;f\u0011!1\u0005A!E!\u0002\u0013\t\u0015aC1uiJL'-\u001e;fg\u0002BQ\u0001\u0013\u0001\u0005\u0002%\u000ba\u0001P5oSRtD#\u0002&L\u00196s\u0005C\u0001\u000f\u0001\u0011\u0015Ir\t1\u0001\u001c\u0011\u0015\u0011s\t1\u0001%\u0011\u00151t\t1\u00019\u0011\u0015yt\t1\u0001B\u0011\u0015\u0001\u0006\u0001\"\u0001R\u00031i\u0017M[8s-\u0016\u00148/[8o+\u0005\u0011\u0006CA\u0007T\u0013\t!fBA\u0002J]RDQA\u0016\u0001\u0005\u0002E\u000bA\"\\5o_J4VM]:j_:DQ\u0001\u0017\u0001\u0005\u0002e\u000b\u0011b\u00197bgNt\u0015-\\3\u0016\u0003i\u0003\"!D.\n\u0005qs!aA!os\")a\f\u0001C\u00013\u0006Q1/\u001e9fe\u000ec\u0017m]:\t\u000b\u0001\u0004A\u0011A1\u0002\u0015%tG/\u001a:gC\u000e,7/F\u0001c!\r\u0019gMW\u0007\u0002I*\u0011QMD\u0001\u000bG>dG.Z2uS>t\u0017B\u0001\u0018e\u0011\u0015A\u0007\u0001\"\u0001j\u0003!\u0019wN\\:uC:$HC\u0001.k\u0011\u0015Yw\r1\u0001S\u0003\u0015Ig\u000eZ3y\u0011\u0015i\u0007\u0001\"\u0001o\u0003=\u0019wN\\:uC:$xK]1qa\u0016$GC\u0001.p\u0011\u0015YG\u000e1\u0001S\u0011\u0015\t\b\u0001\"\u0001s\u0003%\tG\u000f\u001e:jEV$X\r\u0006\u0002tmB\u0019Q\u0002\u001e\"\n\u0005Ut!AB(qi&|g\u000eC\u0003xa\u0002\u0007\u00010\u0001\u0003oC6,\u0007CA=}\u001d\ti!0\u0003\u0002|\u001d\u00051\u0001K]3eK\u001aL!! @\u0003\rM#(/\u001b8h\u0015\tYh\u0002C\u0005\u0002\u0002\u0001\u0011\r\u0011\"\u0001\u0002\u0004\u0005Y\"+\u0016(U\u00136+uLV%T\u0013\ncUiX!O\u001d>#\u0016\tV%P\u001dN+\"!!\u0002\u0011\t\u0005\u001d\u0011\u0011C\u0007\u0003\u0003\u0013QA!a\u0003\u0002\u000e\u0005!A.\u00198h\u0015\t\ty!\u0001\u0003kCZ\f\u0017bA?\u0002\n!A\u0011Q\u0003\u0001!\u0002\u0013\t)!\u0001\u000fS+:#\u0016*T#`-&\u001b\u0016J\u0011'F?\u0006sej\u0014+B)&{ej\u0015\u0011\t\u000f\u0005e\u0001\u0001\"\u0001\u0002\u001c\u0005Y\u0011M\u001c8pi\u0006$\u0018n\u001c8t+\t\ti\u0002\u0005\u0003\u000ei\u0006}\u0001\u0003B\u0013.\u0003C\u0001B!a\t\u0002*9\u0019A$!\n\n\u0007\u0005\u001d\"!A\bDY\u0006\u001c8OR5mKB\u000b'o]3s\u0013\u0011\tY#!\f\u0003\u0015\u0005sgn\u001c;bi&|gNC\u0002\u0002(\tAq!!\r\u0001\t\u0003\t\u0019$\u0001\u0006b]:|G/\u0019;j_:$B!!\u000e\u00028A!Q\u0002^A\u0011\u0011\u00199\u0018q\u0006a\u0001q\"I\u00111\b\u0001\u0002\u0002\u0013\u0005\u0011QH\u0001\u0005G>\u0004\u0018\u0010F\u0005K\u0003\u007f\t\t%a\u0011\u0002F!A\u0011$!\u000f\u0011\u0002\u0003\u00071\u0004\u0003\u0005#\u0003s\u0001\n\u00111\u0001%\u0011!1\u0014\u0011\bI\u0001\u0002\u0004A\u0004\u0002C \u0002:A\u0005\t\u0019A!\t\u0013\u0005%\u0003!%A\u0005\u0002\u0005-\u0013AD2paf$C-\u001a4bk2$H%M\u000b\u0003\u0003\u001bR3aGA(W\t\t\t\u0006\u0005\u0003\u0002T\u0005mSBAA+\u0015\u0011\t9&!\u0017\u0002\u0013Ut7\r[3dW\u0016$'bAA\u0019\u001d%!\u0011QLA+\u0005E)hn\u00195fG.,GMV1sS\u0006t7-\u001a\u0005\n\u0003C\u0002\u0011\u0013!C\u0001\u0003G\nabY8qs\u0012\"WMZ1vYR$#'\u0006\u0002\u0002f)\u001aA%a\u0014\t\u0013\u0005%\u0004!%A\u0005\u0002\u0005-\u0014AD2paf$C-\u001a4bk2$HeM\u000b\u0003\u0003[R3\u0001OA(\u0011%\t\t\bAI\u0001\n\u0003\t\u0019(\u0001\bd_BLH\u0005Z3gCVdG\u000f\n\u001b\u0016\u0005\u0005U$fA!\u0002P!I\u0011\u0011\u0010\u0001\u0002\u0002\u0013\u0005\u00131A\u0001\u000eaJ|G-^2u!J,g-\u001b=\t\u0011\u0005u\u0004!!A\u0005\u0002E\u000bA\u0002\u001d:pIV\u001cG/\u0011:jifD\u0011\"!!\u0001\u0003\u0003%\t!a!\u0002\u001dA\u0014x\u000eZ;di\u0016cW-\\3oiR\u0019!,!\"\t\u0013\u0005\u001d\u0015qPA\u0001\u0002\u0004\u0011\u0016a\u0001=%c!I\u00111\u0012\u0001\u0002\u0002\u0013\u0005\u0013QR\u0001\u0010aJ|G-^2u\u0013R,'/\u0019;peV\u0011\u0011q\u0012\t\u0005G\u0006E%,C\u0002\u0002\u0014\u0012\u0014\u0001\"\u0013;fe\u0006$xN\u001d\u0005\n\u0003/\u0003\u0011\u0011!C\u0001\u00033\u000b\u0001bY1o\u000bF,\u0018\r\u001c\u000b\u0005\u00037\u000b\t\u000bE\u0002\u000e\u0003;K1!a(\u000f\u0005\u001d\u0011un\u001c7fC:D\u0011\"a\"\u0002\u0016\u0006\u0005\t\u0019\u0001.\t\u0013\u0005\u0015\u0006!!A\u0005B\u0005\u001d\u0016\u0001\u00035bg\"\u001cu\u000eZ3\u0015\u0003IC\u0011\"a+\u0001\u0003\u0003%\t%!,\u0002\u0011Q|7\u000b\u001e:j]\u001e$\"!!\u0002\t\u0013\u0005E\u0006!!A\u0005B\u0005M\u0016AB3rk\u0006d7\u000f\u0006\u0003\u0002\u001c\u0006U\u0006\"CAD\u0003_\u000b\t\u00111\u0001[\u000f%\tILAA\u0001\u0012\u0003\tY,A\u0005DY\u0006\u001c8OR5mKB\u0019A$!0\u0007\u0011\u0005\u0011\u0011\u0011!E\u0001\u0003\u007f\u001bR!!0\u0002BV\u0001\u0012\"a1\u0002Jn!\u0003(\u0011&\u000e\u0005\u0005\u0015'bAAd\u001d\u00059!/\u001e8uS6,\u0017\u0002BAf\u0003\u000b\u0014\u0011#\u00112tiJ\f7\r\u001e$v]\u000e$\u0018n\u001c85\u0011\u001dA\u0015Q\u0018C\u0001\u0003\u001f$\"!a/\t\u0015\u0005-\u0016QXA\u0001\n\u000b\ni\u000b\u0003\u0006\u0002V\u0006u\u0016\u0011!CA\u0003/\fQ!\u00199qYf$\u0012BSAm\u00037\fi.a8\t\re\t\u0019\u000e1\u0001\u001c\u0011\u0019\u0011\u00131\u001ba\u0001I!1a'a5A\u0002aBaaPAj\u0001\u0004\t\u0005BCAr\u0003{\u000b\t\u0011\"!\u0002f\u00069QO\\1qa2LH\u0003BAt\u0003_\u0004B!\u0004;\u0002jB9Q\"a;\u001cIa\n\u0015bAAw\u001d\t1A+\u001e9mKRB\u0011\"!=\u0002b\u0006\u0005\t\u0019\u0001&\u0002\u0007a$\u0003\u0007\u0003\u0006\u0002v\u0006u\u0016\u0011!C\u0005\u0003o\f1B]3bIJ+7o\u001c7wKR\u0011\u0011\u0011 \t\u0005\u0003\u000f\tY0\u0003\u0003\u0002~\u0006%!AB(cU\u0016\u001cG\u000f")
public class ClassFile implements Product, Serializable
{
    private final ClassFileHeader header;
    private final Seq<Field> fields;
    private final Seq<Method> methods;
    private final Seq<Attribute> attributes;
    private final String RUNTIME_VISIBLE_ANNOTATIONS;
    
    public static Option<Tuple4<ClassFileHeader, Seq<Field>, Seq<Method>, Seq<Attribute>>> unapply(final ClassFile x$0) {
        return ClassFile$.MODULE$.unapply(x$0);
    }
    
    public static ClassFile apply(final ClassFileHeader header, final Seq<Field> fields, final Seq<Method> methods, final Seq<Attribute> attributes) {
        return ClassFile$.MODULE$.apply(header, fields, methods, attributes);
    }
    
    public static Function1<Tuple4<ClassFileHeader, Seq<Field>, Seq<Method>, Seq<Attribute>>, ClassFile> tupled() {
        return (Function1<Tuple4<ClassFileHeader, Seq<Field>, Seq<Method>, Seq<Attribute>>, ClassFile>)ClassFile$.MODULE$.tupled();
    }
    
    public static Function1<ClassFileHeader, Function1<Seq<Field>, Function1<Seq<Method>, Function1<Seq<Attribute>, ClassFile>>>> curried() {
        return (Function1<ClassFileHeader, Function1<Seq<Field>, Function1<Seq<Method>, Function1<Seq<Attribute>, ClassFile>>>>)ClassFile$.MODULE$.curried();
    }
    
    public ClassFileHeader header() {
        return this.header;
    }
    
    public Seq<Field> fields() {
        return this.fields;
    }
    
    public Seq<Method> methods() {
        return this.methods;
    }
    
    public Seq<Attribute> attributes() {
        return this.attributes;
    }
    
    public int majorVersion() {
        return this.header().major();
    }
    
    public int minorVersion() {
        return this.header().minor();
    }
    
    public Object className() {
        return this.constant(this.header().classIndex());
    }
    
    public Object superClass() {
        return this.constant(this.header().superClassIndex());
    }
    
    public Seq<Object> interfaces() {
        return (Seq<Object>)this.header().interfaces().map((Function1)new ClassFile$$anonfun$interfaces.ClassFile$$anonfun$interfaces$1(this), Seq$.MODULE$.canBuildFrom());
    }
    
    public Object constant(final int index) {
        final Object apply = this.header().constants().apply(index);
        Object string;
        if (apply instanceof StringBytesPair) {
            final String str = (String)(string = ((StringBytesPair)apply).string());
        }
        else {
            string = apply;
        }
        return string;
    }
    
    public Object constantWrapped(final int index) {
        return this.header().constants().apply(index);
    }
    
    public Option<Attribute> attribute(final String name) {
        return (Option<Attribute>)this.attributes().find((Function1)new ClassFile$$anonfun$attribute.ClassFile$$anonfun$attribute$1(this, name));
    }
    
    public String RUNTIME_VISIBLE_ANNOTATIONS() {
        return this.RUNTIME_VISIBLE_ANNOTATIONS;
    }
    
    public Option<Seq<ClassFileParser.Annotation>> annotations() {
        return (Option<Seq<ClassFileParser.Annotation>>)this.attributes().find((Function1)new ClassFile$$anonfun$annotations.ClassFile$$anonfun$annotations$1(this)).map((Function1)new ClassFile$$anonfun$annotations.ClassFile$$anonfun$annotations$2(this));
    }
    
    public Option<ClassFileParser.Annotation> annotation(final String name) {
        return (Option<ClassFileParser.Annotation>)this.annotations().flatMap((Function1)new ClassFile$$anonfun$annotation.ClassFile$$anonfun$annotation$1(this, name));
    }
    
    public ClassFile copy(final ClassFileHeader header, final Seq<Field> fields, final Seq<Method> methods, final Seq<Attribute> attributes) {
        return new ClassFile(header, fields, methods, attributes);
    }
    
    public ClassFileHeader copy$default$1() {
        return this.header();
    }
    
    public Seq<Field> copy$default$2() {
        return this.fields();
    }
    
    public Seq<Method> copy$default$3() {
        return this.methods();
    }
    
    public Seq<Attribute> copy$default$4() {
        return this.attributes();
    }
    
    public String productPrefix() {
        return "ClassFile";
    }
    
    public int productArity() {
        return 4;
    }
    
    public Object productElement(final int x$1) {
        Object o = null;
        switch (x$1) {
            default: {
                throw new IndexOutOfBoundsException(BoxesRunTime.boxToInteger(x$1).toString());
            }
            case 3: {
                o = this.attributes();
                break;
            }
            case 2: {
                o = this.methods();
                break;
            }
            case 1: {
                o = this.fields();
                break;
            }
            case 0: {
                o = this.header();
                break;
            }
        }
        return o;
    }
    
    public Iterator<Object> productIterator() {
        return (Iterator<Object>)ScalaRunTime$.MODULE$.typedProductIterator((Product)this);
    }
    
    public boolean canEqual(final Object x$1) {
        return x$1 instanceof ClassFile;
    }
    
    @Override
    public int hashCode() {
        return ScalaRunTime$.MODULE$._hashCode((Product)this);
    }
    
    @Override
    public String toString() {
        return ScalaRunTime$.MODULE$._toString((Product)this);
    }
    
    @Override
    public boolean equals(final Object x$1) {
        if (this != x$1) {
            if (x$1 instanceof ClassFile) {
                final ClassFile classFile = (ClassFile)x$1;
                final ClassFileHeader header = this.header();
                final ClassFileHeader header2 = classFile.header();
                boolean b = false;
                Label_0173: {
                    Label_0172: {
                        if (header == null) {
                            if (header2 != null) {
                                break Label_0172;
                            }
                        }
                        else if (!header.equals(header2)) {
                            break Label_0172;
                        }
                        final Seq<Field> fields = this.fields();
                        final Seq<Field> fields2 = classFile.fields();
                        if (fields == null) {
                            if (fields2 != null) {
                                break Label_0172;
                            }
                        }
                        else if (!fields.equals(fields2)) {
                            break Label_0172;
                        }
                        final Seq<Method> methods = this.methods();
                        final Seq<Method> methods2 = classFile.methods();
                        if (methods == null) {
                            if (methods2 != null) {
                                break Label_0172;
                            }
                        }
                        else if (!methods.equals(methods2)) {
                            break Label_0172;
                        }
                        final Seq<Attribute> attributes = this.attributes();
                        final Seq<Attribute> attributes2 = classFile.attributes();
                        if (attributes == null) {
                            if (attributes2 != null) {
                                break Label_0172;
                            }
                        }
                        else if (!attributes.equals(attributes2)) {
                            break Label_0172;
                        }
                        if (classFile.canEqual(this)) {
                            b = true;
                            break Label_0173;
                        }
                    }
                    b = false;
                }
                if (b) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }
    
    public ClassFile(final ClassFileHeader header, final Seq<Field> fields, final Seq<Method> methods, final Seq<Attribute> attributes) {
        this.header = header;
        this.fields = fields;
        this.methods = methods;
        this.attributes = attributes;
        Product$class.$init$((Product)this);
        this.RUNTIME_VISIBLE_ANNOTATIONS = "RuntimeVisibleAnnotations";
    }
}
