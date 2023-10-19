// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.scalap.scalasig;

import scala.Product$class;
import scala.runtime.ScalaRunTime$;
import scala.collection.Iterator;
import scala.runtime.BoxesRunTime;
import scala.Function1;
import scala.Tuple3;
import scala.Option;
import scala.reflect.ScalaSignature;
import scala.Serializable;
import scala.Product;

@ScalaSignature(bytes = "\u0006\u0001\u0005]d\u0001B\u0001\u0003\u0001.\u0011a\"\u0012=uKJt\u0017\r\\*z[\n|GN\u0003\u0002\u0004\t\u0005A1oY1mCNLwM\u0003\u0002\u0006\r\u000511oY1mCBT!a\u0002\u0005\u0002\r)\u001cxN\u001c\u001bt\u0015\u0005I\u0011aA8sO\u000e\u00011\u0003\u0002\u0001\r!Y\u0001\"!\u0004\b\u000e\u0003\tI!a\u0004\u0002\u0003\u001dM\u001b\u0017\r\\1TS\u001e\u001c\u00160\u001c2pYB\u0011\u0011\u0003F\u0007\u0002%)\t1#A\u0003tG\u0006d\u0017-\u0003\u0002\u0016%\t9\u0001K]8ek\u000e$\bCA\t\u0018\u0013\tA\"C\u0001\u0007TKJL\u0017\r\\5{C\ndW\r\u0003\u0005\u001b\u0001\tU\r\u0011\"\u0001\u001c\u0003\u0011q\u0017-\\3\u0016\u0003q\u0001\"!\b\u0011\u000f\u0005Eq\u0012BA\u0010\u0013\u0003\u0019\u0001&/\u001a3fM&\u0011\u0011E\t\u0002\u0007'R\u0014\u0018N\\4\u000b\u0005}\u0011\u0002\u0002\u0003\u0013\u0001\u0005#\u0005\u000b\u0011\u0002\u000f\u0002\u000b9\fW.\u001a\u0011\t\u0011\u0019\u0002!Q3A\u0005\u0002\u001d\na\u0001]1sK:$X#\u0001\u0015\u0011\u0007EI3&\u0003\u0002+%\t1q\n\u001d;j_:\u0004\"!\u0004\u0017\n\u00055\u0012!AB*z[\n|G\u000e\u0003\u00050\u0001\tE\t\u0015!\u0003)\u0003\u001d\u0001\u0018M]3oi\u0002B\u0001\"\r\u0001\u0003\u0016\u0004%\tAM\u0001\u0006K:$(/_\u000b\u0002gA\u0011Ag\u000e\t\u0003\u001bUJ!A\u000e\u0002\u0003\u0011M\u001b\u0017\r\\1TS\u001eL!\u0001O\u001b\u0003\u000b\u0015sGO]=\t\u0011i\u0002!\u0011#Q\u0001\nM\na!\u001a8uef\u0004\u0003\"\u0002\u001f\u0001\t\u0003i\u0014A\u0002\u001fj]&$h\b\u0006\u0003?\u007f\u0001\u000b\u0005CA\u0007\u0001\u0011\u0015Q2\b1\u0001\u001d\u0011\u001513\b1\u0001)\u0011\u0015\t4\b1\u00014\u0011\u0015\u0019\u0005\u0001\"\u0011E\u0003!!xn\u0015;sS:<G#\u0001\u000f\t\u000b\u0019\u0003A\u0011A$\u0002\u000f!\f7O\u00127bOR\u0011\u0001j\u0013\t\u0003#%K!A\u0013\n\u0003\u000f\t{w\u000e\\3b]\")A*\u0012a\u0001\u001b\u0006!a\r\\1h!\t\tb*\u0003\u0002P%\t!Aj\u001c8h\u0011\u001d\t\u0006!!A\u0005\u0002I\u000bAaY8qsR!ah\u0015+V\u0011\u001dQ\u0002\u000b%AA\u0002qAqA\n)\u0011\u0002\u0003\u0007\u0001\u0006C\u00042!B\u0005\t\u0019A\u001a\t\u000f]\u0003\u0011\u0013!C\u00011\u0006q1m\u001c9zI\u0011,g-Y;mi\u0012\nT#A-+\u0005qQ6&A.\u0011\u0005q\u000bW\"A/\u000b\u0005y{\u0016!C;oG\",7m[3e\u0015\t\u0001'#\u0001\u0006b]:|G/\u0019;j_:L!AY/\u0003#Ut7\r[3dW\u0016$g+\u0019:jC:\u001cW\rC\u0004e\u0001E\u0005I\u0011A3\u0002\u001d\r|\u0007/\u001f\u0013eK\u001a\fW\u000f\u001c;%eU\taM\u000b\u0002)5\"9\u0001\u000eAI\u0001\n\u0003I\u0017AD2paf$C-\u001a4bk2$HeM\u000b\u0002U*\u00121G\u0017\u0005\bY\u0002\t\t\u0011\"\u0011n\u00035\u0001(o\u001c3vGR\u0004&/\u001a4jqV\ta\u000e\u0005\u0002pi6\t\u0001O\u0003\u0002re\u0006!A.\u00198h\u0015\u0005\u0019\u0018\u0001\u00026bm\u0006L!!\t9\t\u000fY\u0004\u0011\u0011!C\u0001o\u0006a\u0001O]8ek\u000e$\u0018I]5usV\t\u0001\u0010\u0005\u0002\u0012s&\u0011!P\u0005\u0002\u0004\u0013:$\bb\u0002?\u0001\u0003\u0003%\t!`\u0001\u000faJ|G-^2u\u000b2,W.\u001a8u)\rq\u00181\u0001\t\u0003#}L1!!\u0001\u0013\u0005\r\te.\u001f\u0005\t\u0003\u000bY\u0018\u0011!a\u0001q\u0006\u0019\u0001\u0010J\u0019\t\u0013\u0005%\u0001!!A\u0005B\u0005-\u0011a\u00049s_\u0012,8\r^%uKJ\fGo\u001c:\u0016\u0005\u00055\u0001#BA\b\u0003+qXBAA\t\u0015\r\t\u0019BE\u0001\u000bG>dG.Z2uS>t\u0017\u0002BA\f\u0003#\u0011\u0001\"\u0013;fe\u0006$xN\u001d\u0005\n\u00037\u0001\u0011\u0011!C\u0001\u0003;\t\u0001bY1o\u000bF,\u0018\r\u001c\u000b\u0004\u0011\u0006}\u0001\"CA\u0003\u00033\t\t\u00111\u0001\u007f\u0011%\t\u0019\u0003AA\u0001\n\u0003\n)#\u0001\u0005iCND7i\u001c3f)\u0005A\b\"CA\u0015\u0001\u0005\u0005I\u0011IA\u0016\u0003\u0019)\u0017/^1mgR\u0019\u0001*!\f\t\u0013\u0005\u0015\u0011qEA\u0001\u0002\u0004qx!CA\u0019\u0005\u0005\u0005\t\u0012AA\u001a\u00039)\u0005\u0010^3s]\u0006d7+_7c_2\u00042!DA\u001b\r!\t!!!A\t\u0002\u0005]2#BA\u001b\u0003s1\u0002\u0003CA\u001e\u0003\u0003b\u0002f\r \u000e\u0005\u0005u\"bAA %\u00059!/\u001e8uS6,\u0017\u0002BA\"\u0003{\u0011\u0011#\u00112tiJ\f7\r\u001e$v]\u000e$\u0018n\u001c84\u0011\u001da\u0014Q\u0007C\u0001\u0003\u000f\"\"!a\r\t\u0013\r\u000b)$!A\u0005F\u0005-C#\u00018\t\u0015\u0005=\u0013QGA\u0001\n\u0003\u000b\t&A\u0003baBd\u0017\u0010F\u0004?\u0003'\n)&a\u0016\t\ri\ti\u00051\u0001\u001d\u0011\u00191\u0013Q\na\u0001Q!1\u0011'!\u0014A\u0002MB!\"a\u0017\u00026\u0005\u0005I\u0011QA/\u0003\u001d)h.\u00199qYf$B!a\u0018\u0002hA!\u0011#KA1!\u0019\t\u00121\r\u000f)g%\u0019\u0011Q\r\n\u0003\rQ+\b\u000f\\34\u0011%\tI'!\u0017\u0002\u0002\u0003\u0007a(A\u0002yIAB!\"!\u001c\u00026\u0005\u0005I\u0011BA8\u0003-\u0011X-\u00193SKN|GN^3\u0015\u0005\u0005E\u0004cA8\u0002t%\u0019\u0011Q\u000f9\u0003\r=\u0013'.Z2u\u0001")
public class ExternalSymbol extends ScalaSigSymbol implements Product, Serializable
{
    private final String name;
    private final Option<Symbol> parent;
    private final ScalaSig.Entry entry;
    
    public static Option<Tuple3<String, Option<Symbol>, ScalaSig.Entry>> unapply(final ExternalSymbol x$0) {
        return ExternalSymbol$.MODULE$.unapply(x$0);
    }
    
    public static ExternalSymbol apply(final String name, final Option<Symbol> parent, final ScalaSig.Entry entry) {
        return ExternalSymbol$.MODULE$.apply(name, parent, entry);
    }
    
    public static Function1<Tuple3<String, Option<Symbol>, ScalaSig.Entry>, ExternalSymbol> tupled() {
        return (Function1<Tuple3<String, Option<Symbol>, ScalaSig.Entry>, ExternalSymbol>)ExternalSymbol$.MODULE$.tupled();
    }
    
    public static Function1<String, Function1<Option<Symbol>, Function1<ScalaSig.Entry, ExternalSymbol>>> curried() {
        return (Function1<String, Function1<Option<Symbol>, Function1<ScalaSig.Entry, ExternalSymbol>>>)ExternalSymbol$.MODULE$.curried();
    }
    
    public String name() {
        return this.name;
    }
    
    public Option<Symbol> parent() {
        return this.parent;
    }
    
    @Override
    public ScalaSig.Entry entry() {
        return this.entry;
    }
    
    public String toString() {
        return this.path();
    }
    
    public boolean hasFlag(final long flag) {
        return false;
    }
    
    public ExternalSymbol copy(final String name, final Option<Symbol> parent, final ScalaSig.Entry entry) {
        return new ExternalSymbol(name, parent, entry);
    }
    
    public String copy$default$1() {
        return this.name();
    }
    
    public Option<Symbol> copy$default$2() {
        return this.parent();
    }
    
    public ScalaSig.Entry copy$default$3() {
        return this.entry();
    }
    
    public String productPrefix() {
        return "ExternalSymbol";
    }
    
    public int productArity() {
        return 3;
    }
    
    public Object productElement(final int x$1) {
        Object o = null;
        switch (x$1) {
            default: {
                throw new IndexOutOfBoundsException(BoxesRunTime.boxToInteger(x$1).toString());
            }
            case 2: {
                o = this.entry();
                break;
            }
            case 1: {
                o = this.parent();
                break;
            }
            case 0: {
                o = this.name();
                break;
            }
        }
        return o;
    }
    
    public Iterator<Object> productIterator() {
        return (Iterator<Object>)ScalaRunTime$.MODULE$.typedProductIterator((Product)this);
    }
    
    public boolean canEqual(final Object x$1) {
        return x$1 instanceof ExternalSymbol;
    }
    
    public int hashCode() {
        return ScalaRunTime$.MODULE$._hashCode((Product)this);
    }
    
    public boolean equals(final Object x$1) {
        if (this != x$1) {
            if (x$1 instanceof ExternalSymbol) {
                final ExternalSymbol externalSymbol = (ExternalSymbol)x$1;
                final String name = this.name();
                final String name2 = externalSymbol.name();
                boolean b = false;
                Label_0141: {
                    Label_0140: {
                        if (name == null) {
                            if (name2 != null) {
                                break Label_0140;
                            }
                        }
                        else if (!name.equals(name2)) {
                            break Label_0140;
                        }
                        final Option<Symbol> parent = this.parent();
                        final Option<Symbol> parent2 = externalSymbol.parent();
                        if (parent == null) {
                            if (parent2 != null) {
                                break Label_0140;
                            }
                        }
                        else if (!parent.equals(parent2)) {
                            break Label_0140;
                        }
                        final ScalaSig.Entry entry = this.entry();
                        final ScalaSig.Entry entry2 = externalSymbol.entry();
                        if (entry == null) {
                            if (entry2 != null) {
                                break Label_0140;
                            }
                        }
                        else if (!entry.equals(entry2)) {
                            break Label_0140;
                        }
                        if (externalSymbol.canEqual(this)) {
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
    
    public ExternalSymbol(final String name, final Option<Symbol> parent, final ScalaSig.Entry entry) {
        this.name = name;
        this.parent = parent;
        this.entry = entry;
        Product$class.$init$((Product)this);
    }
}
