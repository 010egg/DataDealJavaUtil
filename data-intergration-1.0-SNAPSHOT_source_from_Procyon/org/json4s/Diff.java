// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

import scala.Product$class;
import scala.collection.Seq;
import scala.Tuple2;
import scala.Predef$;
import scala.runtime.ScalaRunTime$;
import scala.collection.Iterator;
import scala.runtime.BoxesRunTime;
import scala.Function1;
import scala.Tuple3;
import scala.Option;
import scala.reflect.ScalaSignature;
import scala.Serializable;
import scala.Product;

@ScalaSignature(bytes = "\u0006\u0001\u0005\rg\u0001B\u0001\u0003\u0001\u001e\u0011A\u0001R5gM*\u00111\u0001B\u0001\u0007UN|g\u000eN:\u000b\u0003\u0015\t1a\u001c:h\u0007\u0001\u0019B\u0001\u0001\u0005\u000f#A\u0011\u0011\u0002D\u0007\u0002\u0015)\t1\"A\u0003tG\u0006d\u0017-\u0003\u0002\u000e\u0015\t1\u0011I\\=SK\u001a\u0004\"!C\b\n\u0005AQ!a\u0002)s_\u0012,8\r\u001e\t\u0003\u0013II!a\u0005\u0006\u0003\u0019M+'/[1mSj\f'\r\\3\t\u0011U\u0001!Q3A\u0005\u0002Y\tqa\u00195b]\u001e,G-F\u0001\u0018!\tABD\u0004\u0002\u001a55\t!!\u0003\u0002\u001c\u0005\u00059!j]8o\u0003N#\u0016BA\u000f\u001f\u0005\u0019Qe+\u00197vK*\u00111D\u0001\u0005\tA\u0001\u0011\t\u0012)A\u0005/\u0005A1\r[1oO\u0016$\u0007\u0005\u0003\u0005#\u0001\tU\r\u0011\"\u0001\u0017\u0003\u0015\tG\rZ3e\u0011!!\u0003A!E!\u0002\u00139\u0012AB1eI\u0016$\u0007\u0005\u0003\u0005'\u0001\tU\r\u0011\"\u0001\u0017\u0003\u001d!W\r\\3uK\u0012D\u0001\u0002\u000b\u0001\u0003\u0012\u0003\u0006IaF\u0001\tI\u0016dW\r^3eA!)!\u0006\u0001C\u0001W\u00051A(\u001b8jiz\"B\u0001L\u0017/_A\u0011\u0011\u0004\u0001\u0005\u0006+%\u0002\ra\u0006\u0005\u0006E%\u0002\ra\u0006\u0005\u0006M%\u0002\ra\u0006\u0005\u0006c\u0001!\tAM\u0001\u0004[\u0006\u0004HC\u0001\u00174\u0011\u0015!\u0004\u00071\u00016\u0003\u00051\u0007\u0003B\u00057/]I!a\u000e\u0006\u0003\u0013\u0019+hn\u0019;j_:\f\u0004BB\u001d\u0001\t\u0003\u0011!(A\u0004u_\u001aKW\r\u001c3\u0015\u00051Z\u0004\"\u0002\u001f9\u0001\u0004i\u0014\u0001\u00028b[\u0016\u0004\"AP!\u000f\u0005%y\u0014B\u0001!\u000b\u0003\u0019\u0001&/\u001a3fM&\u0011!i\u0011\u0002\u0007'R\u0014\u0018N\\4\u000b\u0005\u0001S\u0001bB#\u0001\u0003\u0003%\tAR\u0001\u0005G>\u0004\u0018\u0010\u0006\u0003-\u000f\"K\u0005bB\u000bE!\u0003\u0005\ra\u0006\u0005\bE\u0011\u0003\n\u00111\u0001\u0018\u0011\u001d1C\t%AA\u0002]Aqa\u0013\u0001\u0012\u0002\u0013\u0005A*\u0001\bd_BLH\u0005Z3gCVdG\u000fJ\u0019\u0016\u00035S#a\u0006(,\u0003=\u0003\"\u0001U+\u000e\u0003ES!AU*\u0002\u0013Ut7\r[3dW\u0016$'B\u0001+\u000b\u0003)\tgN\\8uCRLwN\\\u0005\u0003-F\u0013\u0011#\u001e8dQ\u0016\u001c7.\u001a3WCJL\u0017M\\2f\u0011\u001dA\u0006!%A\u0005\u00021\u000babY8qs\u0012\"WMZ1vYR$#\u0007C\u0004[\u0001E\u0005I\u0011\u0001'\u0002\u001d\r|\u0007/\u001f\u0013eK\u001a\fW\u000f\u001c;%g!9A\fAA\u0001\n\u0003j\u0016!\u00049s_\u0012,8\r\u001e)sK\u001aL\u00070F\u0001_!\tyF-D\u0001a\u0015\t\t'-\u0001\u0003mC:<'\"A2\u0002\t)\fg/Y\u0005\u0003\u0005\u0002DqA\u001a\u0001\u0002\u0002\u0013\u0005q-\u0001\u0007qe>$Wo\u0019;Be&$\u00180F\u0001i!\tI\u0011.\u0003\u0002k\u0015\t\u0019\u0011J\u001c;\t\u000f1\u0004\u0011\u0011!C\u0001[\u0006q\u0001O]8ek\u000e$X\t\\3nK:$HC\u00018r!\tIq.\u0003\u0002q\u0015\t\u0019\u0011I\\=\t\u000fI\\\u0017\u0011!a\u0001Q\u0006\u0019\u0001\u0010J\u0019\t\u000fQ\u0004\u0011\u0011!C!k\u0006y\u0001O]8ek\u000e$\u0018\n^3sCR|'/F\u0001w!\r9(P\\\u0007\u0002q*\u0011\u0011PC\u0001\u000bG>dG.Z2uS>t\u0017BA>y\u0005!IE/\u001a:bi>\u0014\bbB?\u0001\u0003\u0003%\tA`\u0001\tG\u0006tW)];bYR\u0019q0!\u0002\u0011\u0007%\t\t!C\u0002\u0002\u0004)\u0011qAQ8pY\u0016\fg\u000eC\u0004sy\u0006\u0005\t\u0019\u00018\t\u0013\u0005%\u0001!!A\u0005B\u0005-\u0011\u0001\u00035bg\"\u001cu\u000eZ3\u0015\u0003!D\u0011\"a\u0004\u0001\u0003\u0003%\t%!\u0005\u0002\u0011Q|7\u000b\u001e:j]\u001e$\u0012A\u0018\u0005\n\u0003+\u0001\u0011\u0011!C!\u0003/\ta!Z9vC2\u001cHcA@\u0002\u001a!A!/a\u0005\u0002\u0002\u0003\u0007anB\u0004\u0002\u001e\tA\t!a\b\u0002\t\u0011KgM\u001a\t\u00043\u0005\u0005bAB\u0001\u0003\u0011\u0003\t\u0019c\u0005\u0003\u0002\"!\t\u0002b\u0002\u0016\u0002\"\u0011\u0005\u0011q\u0005\u000b\u0003\u0003?A\u0001\"a\u000b\u0002\"\u0011\u0005\u0011QF\u0001\u0005I&4g\rF\u0003-\u0003_\t\u0019\u0004C\u0004\u00022\u0005%\u0002\u0019A\f\u0002\tY\fG.\r\u0005\b\u0003k\tI\u00031\u0001\u0018\u0003\u00111\u0018\r\u001c\u001a\t\u0011\u0005e\u0012\u0011\u0005C\u0005\u0003w\t!\u0002Z5gM\u001aKW\r\u001c3t)\u0015a\u0013QHA0\u0011!\ty$a\u000eA\u0002\u0005\u0005\u0013a\u0001<tcA1\u00111IA*\u00033rA!!\u0012\u0002P9!\u0011qIA'\u001b\t\tIEC\u0002\u0002L\u0019\ta\u0001\u0010:p_Rt\u0014\"A\u0006\n\u0007\u0005E#\"A\u0004qC\u000e\\\u0017mZ3\n\t\u0005U\u0013q\u000b\u0002\u0005\u0019&\u001cHOC\u0002\u0002R)\u00012\u0001GA.\u0013\r\tiF\b\u0002\u0007\u0015\u001aKW\r\u001c3\t\u0011\u0005\u0005\u0014q\u0007a\u0001\u0003\u0003\n1A^:3\u0011!\t)'!\t\u0005\n\u0005\u001d\u0014\u0001\u00033jM\u001a4\u0016\r\\:\u0015\u000b1\nI'!\u001c\t\u0011\u0005}\u00121\ra\u0001\u0003W\u0002R!a\u0011\u0002T]A\u0001\"!\u0019\u0002d\u0001\u0007\u00111\u000e\u0004\u000e\u0003c\n\t\u0003%A\u0002\u0002\t\t\u0019(!#\u0003\u0011\u0011KgMZ1cY\u0016\u001c2!a\u001c\t\u0011!\t9(a\u001c\u0005\u0002\u0005e\u0014A\u0002\u0013j]&$H\u0005\u0006\u0002\u0002|A\u0019\u0011\"! \n\u0007\u0005}$B\u0001\u0003V]&$\b\u0002CA\u0016\u0003_\"\t!a!\u0015\u00071\n)\tC\u0004\u0002\b\u0006\u0005\u0005\u0019A\f\u0002\u000b=$\b.\u001a:\u0011\u0007\u0005-EDD\u0002\u0002\u000ejqA!a$\u0002\u0014:!\u0011qIAI\u0013\u0005)\u0011BA\u0002\u0005\u0011)\t9*!\t\u0002\u0002\u0013\u0005\u0015\u0011T\u0001\u0006CB\u0004H.\u001f\u000b\bY\u0005m\u0015QTAP\u0011\u0019)\u0012Q\u0013a\u0001/!1!%!&A\u0002]AaAJAK\u0001\u00049\u0002BCAR\u0003C\t\t\u0011\"!\u0002&\u00069QO\\1qa2LH\u0003BAT\u0003g\u0003R!CAU\u0003[K1!a+\u000b\u0005\u0019y\u0005\u000f^5p]B1\u0011\"a,\u0018/]I1!!-\u000b\u0005\u0019!V\u000f\u001d7fg!I\u0011QWAQ\u0003\u0003\u0005\r\u0001L\u0001\u0004q\u0012\u0002\u0004BCA]\u0003C\t\t\u0011\"\u0003\u0002<\u0006Y!/Z1e%\u0016\u001cx\u000e\u001c<f)\t\ti\fE\u0002`\u0003\u007fK1!!1a\u0005\u0019y%M[3di\u0002")
public class Diff implements Product, Serializable
{
    private final JsonAST.JValue changed;
    private final JsonAST.JValue added;
    private final JsonAST.JValue deleted;
    
    public static Option<Tuple3<JsonAST.JValue, JsonAST.JValue, JsonAST.JValue>> unapply(final Diff x$0) {
        return Diff$.MODULE$.unapply(x$0);
    }
    
    public static Diff apply(final JsonAST.JValue changed, final JsonAST.JValue added, final JsonAST.JValue deleted) {
        return Diff$.MODULE$.apply(changed, added, deleted);
    }
    
    public static Diff diff(final JsonAST.JValue val1, final JsonAST.JValue val2) {
        return Diff$.MODULE$.diff(val1, val2);
    }
    
    public JsonAST.JValue changed() {
        return this.changed;
    }
    
    public JsonAST.JValue added() {
        return this.added;
    }
    
    public JsonAST.JValue deleted() {
        return this.deleted;
    }
    
    public Diff map(final Function1<JsonAST.JValue, JsonAST.JValue> f) {
        return new Diff(this.applyTo$1(this.changed(), f), this.applyTo$1(this.added(), f), this.applyTo$1(this.deleted(), f));
    }
    
    public Diff toField(final String name) {
        return new Diff(this.applyTo$2(this.changed(), name), this.applyTo$2(this.added(), name), this.applyTo$2(this.deleted(), name));
    }
    
    public Diff copy(final JsonAST.JValue changed, final JsonAST.JValue added, final JsonAST.JValue deleted) {
        return new Diff(changed, added, deleted);
    }
    
    public JsonAST.JValue copy$default$1() {
        return this.changed();
    }
    
    public JsonAST.JValue copy$default$2() {
        return this.added();
    }
    
    public JsonAST.JValue copy$default$3() {
        return this.deleted();
    }
    
    public String productPrefix() {
        return "Diff";
    }
    
    public int productArity() {
        return 3;
    }
    
    public Object productElement(final int x$1) {
        JsonAST.JValue value = null;
        switch (x$1) {
            default: {
                throw new IndexOutOfBoundsException(BoxesRunTime.boxToInteger(x$1).toString());
            }
            case 2: {
                value = this.deleted();
                break;
            }
            case 1: {
                value = this.added();
                break;
            }
            case 0: {
                value = this.changed();
                break;
            }
        }
        return value;
    }
    
    public Iterator<Object> productIterator() {
        return (Iterator<Object>)ScalaRunTime$.MODULE$.typedProductIterator((Product)this);
    }
    
    public boolean canEqual(final Object x$1) {
        return x$1 instanceof Diff;
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
            if (x$1 instanceof Diff) {
                final Diff diff = (Diff)x$1;
                final JsonAST.JValue changed = this.changed();
                final JsonAST.JValue changed2 = diff.changed();
                boolean b = false;
                Label_0141: {
                    Label_0140: {
                        if (changed == null) {
                            if (changed2 != null) {
                                break Label_0140;
                            }
                        }
                        else if (!changed.equals(changed2)) {
                            break Label_0140;
                        }
                        final JsonAST.JValue added = this.added();
                        final JsonAST.JValue added2 = diff.added();
                        if (added == null) {
                            if (added2 != null) {
                                break Label_0140;
                            }
                        }
                        else if (!added.equals(added2)) {
                            break Label_0140;
                        }
                        final JsonAST.JValue deleted = this.deleted();
                        final JsonAST.JValue deleted2 = diff.deleted();
                        if (deleted == null) {
                            if (deleted2 != null) {
                                break Label_0140;
                            }
                        }
                        else if (!deleted.equals(deleted2)) {
                            break Label_0140;
                        }
                        if (diff.canEqual(this)) {
                            b = true;
                            break Label_0141;
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
    
    private final JsonAST.JValue applyTo$1(final JsonAST.JValue x, final Function1 f$1) {
        JsonAST.JValue module$;
        if (JsonAST.JNothing$.MODULE$.equals(x)) {
            module$ = JsonAST.JNothing$.MODULE$;
        }
        else {
            module$ = (JsonAST.JValue)f$1.apply((Object)x);
        }
        return module$;
    }
    
    private final JsonAST.JValue applyTo$2(final JsonAST.JValue x, final String name$1) {
        JsonAST.JValue value;
        if (JsonAST.JNothing$.MODULE$.equals(x)) {
            value = JsonAST.JNothing$.MODULE$;
        }
        else {
            value = JsonAST.JObject$.MODULE$.apply((Seq<Tuple2<String, JsonAST.JValue>>)Predef$.MODULE$.wrapRefArray((Object[])new Tuple2[] { new Tuple2((Object)name$1, (Object)x) }));
        }
        return value;
    }
    
    public Diff(final JsonAST.JValue changed, final JsonAST.JValue added, final JsonAST.JValue deleted) {
        this.changed = changed;
        this.added = added;
        this.deleted = deleted;
        Product$class.$init$((Product)this);
    }
    
    public interface Diffable
    {
        Diff diff(final JsonAST.JValue p0);
    }
}
