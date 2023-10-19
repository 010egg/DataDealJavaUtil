// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

import scala.Product;
import scala.runtime.ScalaRunTime$;
import scala.collection.Iterator;
import scala.runtime.BoxesRunTime;
import scala.Function1;
import scala.Option;
import java.io.File;
import scala.reflect.ScalaSignature;

@ScalaSignature(bytes = "\u0006\u0001\u0005Ma\u0001B\u0001\u0003\u0001\u001e\u0011\u0011BR5mK&s\u0007/\u001e;\u000b\u0005\r!\u0011A\u00026t_:$4OC\u0001\u0006\u0003\ry'oZ\u0002\u0001'\u0011\u0001\u0001\u0002\u0004\n\u0011\u0005%QQ\"\u0001\u0002\n\u0005-\u0011!!\u0003&t_:Le\u000e];u!\ti\u0001#D\u0001\u000f\u0015\u0005y\u0011!B:dC2\f\u0017BA\t\u000f\u0005\u001d\u0001&o\u001c3vGR\u0004\"!D\n\n\u0005Qq!\u0001D*fe&\fG.\u001b>bE2,\u0007\u0002\u0003\f\u0001\u0005+\u0007I\u0011A\f\u0002\t\u0019LG.Z\u000b\u00021A\u0011\u0011DH\u0007\u00025)\u00111\u0004H\u0001\u0003S>T\u0011!H\u0001\u0005U\u00064\u0018-\u0003\u0002 5\t!a)\u001b7f\u0011!\t\u0003A!E!\u0002\u0013A\u0012!\u00024jY\u0016\u0004\u0003\"B\u0012\u0001\t\u0003!\u0013A\u0002\u001fj]&$h\b\u0006\u0002&MA\u0011\u0011\u0002\u0001\u0005\u0006-\t\u0002\r\u0001\u0007\u0005\bQ\u0001\t\t\u0011\"\u0001*\u0003\u0011\u0019w\u000e]=\u0015\u0005\u0015R\u0003b\u0002\f(!\u0003\u0005\r\u0001\u0007\u0005\bY\u0001\t\n\u0011\"\u0001.\u00039\u0019w\u000e]=%I\u00164\u0017-\u001e7uIE*\u0012A\f\u0016\u00031=Z\u0013\u0001\r\t\u0003cYj\u0011A\r\u0006\u0003gQ\n\u0011\"\u001e8dQ\u0016\u001c7.\u001a3\u000b\u0005Ur\u0011AC1o]>$\u0018\r^5p]&\u0011qG\r\u0002\u0012k:\u001c\u0007.Z2lK\u00124\u0016M]5b]\u000e,\u0007bB\u001d\u0001\u0003\u0003%\tEO\u0001\u000eaJ|G-^2u!J,g-\u001b=\u0016\u0003m\u0002\"\u0001P \u000e\u0003uR!A\u0010\u000f\u0002\t1\fgnZ\u0005\u0003\u0001v\u0012aa\u0015;sS:<\u0007b\u0002\"\u0001\u0003\u0003%\taQ\u0001\raJ|G-^2u\u0003JLG/_\u000b\u0002\tB\u0011Q\"R\u0005\u0003\r:\u00111!\u00138u\u0011\u001dA\u0005!!A\u0005\u0002%\u000ba\u0002\u001d:pIV\u001cG/\u00127f[\u0016tG\u000f\u0006\u0002K\u001bB\u0011QbS\u0005\u0003\u0019:\u00111!\u00118z\u0011\u001dqu)!AA\u0002\u0011\u000b1\u0001\u001f\u00132\u0011\u001d\u0001\u0006!!A\u0005BE\u000bq\u0002\u001d:pIV\u001cG/\u0013;fe\u0006$xN]\u000b\u0002%B\u00191K\u0016&\u000e\u0003QS!!\u0016\b\u0002\u0015\r|G\u000e\\3di&|g.\u0003\u0002X)\nA\u0011\n^3sCR|'\u000fC\u0004Z\u0001\u0005\u0005I\u0011\u0001.\u0002\u0011\r\fg.R9vC2$\"a\u00170\u0011\u00055a\u0016BA/\u000f\u0005\u001d\u0011un\u001c7fC:DqA\u0014-\u0002\u0002\u0003\u0007!\nC\u0004a\u0001\u0005\u0005I\u0011I1\u0002\u0011!\f7\u000f[\"pI\u0016$\u0012\u0001\u0012\u0005\bG\u0002\t\t\u0011\"\u0011e\u0003!!xn\u0015;sS:<G#A\u001e\t\u000f\u0019\u0004\u0011\u0011!C!O\u00061Q-];bYN$\"a\u00175\t\u000f9+\u0017\u0011!a\u0001\u0015\u001e9!NAA\u0001\u0012\u0003Y\u0017!\u0003$jY\u0016Le\u000e];u!\tIANB\u0004\u0002\u0005\u0005\u0005\t\u0012A7\u0014\u00071t'\u0003\u0005\u0003peb)S\"\u00019\u000b\u0005Et\u0011a\u0002:v]RLW.Z\u0005\u0003gB\u0014\u0011#\u00112tiJ\f7\r\u001e$v]\u000e$\u0018n\u001c82\u0011\u0015\u0019C\u000e\"\u0001v)\u0005Y\u0007bB2m\u0003\u0003%)\u0005\u001a\u0005\bq2\f\t\u0011\"!z\u0003\u0015\t\u0007\u000f\u001d7z)\t)#\u0010C\u0003\u0017o\u0002\u0007\u0001\u0004C\u0004}Y\u0006\u0005I\u0011Q?\u0002\u000fUt\u0017\r\u001d9msR\u0019a0a\u0001\u0011\u00075y\b$C\u0002\u0002\u00029\u0011aa\u00149uS>t\u0007\u0002CA\u0003w\u0006\u0005\t\u0019A\u0013\u0002\u0007a$\u0003\u0007C\u0005\u0002\n1\f\t\u0011\"\u0003\u0002\f\u0005Y!/Z1e%\u0016\u001cx\u000e\u001c<f)\t\ti\u0001E\u0002=\u0003\u001fI1!!\u0005>\u0005\u0019y%M[3di\u0002")
public class FileInput extends JsonInput
{
    private final File file;
    
    public static Option<File> unapply(final FileInput x$0) {
        return FileInput$.MODULE$.unapply(x$0);
    }
    
    public static FileInput apply(final File file) {
        return FileInput$.MODULE$.apply(file);
    }
    
    public static <A> Function1<File, A> andThen(final Function1<FileInput, A> function1) {
        return (Function1<File, A>)FileInput$.MODULE$.andThen((Function1)function1);
    }
    
    public static <A> Function1<A, FileInput> compose(final Function1<A, File> function1) {
        return (Function1<A, FileInput>)FileInput$.MODULE$.compose((Function1)function1);
    }
    
    public File file() {
        return this.file;
    }
    
    public FileInput copy(final File file) {
        return new FileInput(file);
    }
    
    public File copy$default$1() {
        return this.file();
    }
    
    @Override
    public String productPrefix() {
        return "FileInput";
    }
    
    public int productArity() {
        return 1;
    }
    
    public Object productElement(final int x$1) {
        switch (x$1) {
            default: {
                throw new IndexOutOfBoundsException(BoxesRunTime.boxToInteger(x$1).toString());
            }
            case 0: {
                return this.file();
            }
        }
    }
    
    @Override
    public Iterator<Object> productIterator() {
        return (Iterator<Object>)ScalaRunTime$.MODULE$.typedProductIterator((Product)this);
    }
    
    public boolean canEqual(final Object x$1) {
        return x$1 instanceof FileInput;
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
            if (x$1 instanceof FileInput) {
                final FileInput fileInput = (FileInput)x$1;
                final File file = this.file();
                final File file2 = fileInput.file();
                boolean b = false;
                Label_0077: {
                    Label_0076: {
                        if (file == null) {
                            if (file2 != null) {
                                break Label_0076;
                            }
                        }
                        else if (!file.equals(file2)) {
                            break Label_0076;
                        }
                        if (fileInput.canEqual(this)) {
                            b = true;
                            break Label_0077;
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
    
    public FileInput(final File file) {
        this.file = file;
    }
}
