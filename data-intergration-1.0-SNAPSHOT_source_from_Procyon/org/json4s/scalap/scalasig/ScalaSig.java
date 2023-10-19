// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.scalap.scalasig;

import scala.Some;
import scala.None$;
import scala.runtime.AbstractFunction3;
import scala.Product$class;
import scala.runtime.Statics;
import scala.runtime.ScalaRunTime$;
import scala.collection.Iterator;
import scala.collection.immutable.IndexedSeq$;
import scala.Predef$;
import scala.runtime.RichInt$;
import scala.collection.TraversableOnce;
import scala.collection.mutable.StringBuilder;
import scala.MatchError;
import scala.Tuple2;
import scala.runtime.BoxesRunTime;
import org.json4s.scalap.Success;
import org.json4s.scalap.DefaultMemoisable$class;
import scala.Function0;
import org.json4s.scalap.Rule;
import scala.runtime.BoxedUnit;
import scala.Function1;
import scala.Tuple3;
import scala.Option;
import scala.collection.mutable.HashMap;
import scala.collection.immutable.List;
import org.json4s.scalap.$tilde;
import scala.collection.Seq;
import scala.reflect.ScalaSignature;
import scala.Serializable;
import scala.Product;
import org.json4s.scalap.DefaultMemoisable;

@ScalaSignature(bytes = "\u0006\u0001\t]d\u0001B\u0001\u0003\u0001.\u0011\u0001bU2bY\u0006\u001c\u0016n\u001a\u0006\u0003\u0007\u0011\t\u0001b]2bY\u0006\u001c\u0018n\u001a\u0006\u0003\u000b\u0019\taa]2bY\u0006\u0004(BA\u0004\t\u0003\u0019Q7o\u001c85g*\t\u0011\"A\u0002pe\u001e\u001c\u0001aE\u0003\u0001\u0019I1\u0012\u0004\u0005\u0002\u000e!5\taBC\u0001\u0010\u0003\u0015\u00198-\u00197b\u0013\t\tbB\u0001\u0004B]f\u0014VM\u001a\t\u0003'Qi\u0011\u0001B\u0005\u0003+\u0011\u0011\u0011\u0003R3gCVdG/T3n_&\u001c\u0018M\u00197f!\tiq#\u0003\u0002\u0019\u001d\t9\u0001K]8ek\u000e$\bCA\u0007\u001b\u0013\tYbB\u0001\u0007TKJL\u0017\r\\5{C\ndW\r\u0003\u0005\u001e\u0001\tU\r\u0011\"\u0001\u001f\u00031i\u0017M[8s-\u0016\u00148/[8o+\u0005y\u0002CA\u0007!\u0013\t\tcBA\u0002J]RD\u0001b\t\u0001\u0003\u0012\u0003\u0006IaH\u0001\u000e[\u0006TwN\u001d,feNLwN\u001c\u0011\t\u0011\u0015\u0002!Q3A\u0005\u0002y\tA\"\\5o_J4VM]:j_:D\u0001b\n\u0001\u0003\u0012\u0003\u0006IaH\u0001\u000e[&twN\u001d,feNLwN\u001c\u0011\t\u0011%\u0002!Q3A\u0005\u0002)\nQ\u0001^1cY\u0016,\u0012a\u000b\t\u0004YQ:dBA\u00173\u001d\tq\u0013'D\u00010\u0015\t\u0001$\"\u0001\u0004=e>|GOP\u0005\u0002\u001f%\u00111GD\u0001\ba\u0006\u001c7.Y4f\u0013\t)dGA\u0002TKFT!a\r\b\u0011\tMAtDO\u0005\u0003s\u0011\u0011a\u0001\n;jY\u0012,\u0007CA\u001e=\u001b\u0005\u0011\u0011BA\u001f\u0003\u0005!\u0011\u0015\u0010^3D_\u0012,\u0007\u0002C \u0001\u0005#\u0005\u000b\u0011B\u0016\u0002\rQ\f'\r\\3!\u0011\u0015\t\u0005\u0001\"\u0001C\u0003\u0019a\u0014N\\5u}Q!1\tR#G!\tY\u0004\u0001C\u0003\u001e\u0001\u0002\u0007q\u0004C\u0003&\u0001\u0002\u0007q\u0004C\u0003*\u0001\u0002\u00071F\u0002\u0003I\u0001\u0001K%!B#oiJL8#B$\r%YI\u0002\u0002C&H\u0005+\u0007I\u0011\u0001\u0010\u0002\u000b%tG-\u001a=\t\u00115;%\u0011#Q\u0001\n}\ta!\u001b8eKb\u0004\u0003\u0002C(H\u0005+\u0007I\u0011\u0001\u0010\u0002\u0013\u0015tGO]=UsB,\u0007\u0002C)H\u0005#\u0005\u000b\u0011B\u0010\u0002\u0015\u0015tGO]=UsB,\u0007\u0005\u0003\u0005T\u000f\nU\r\u0011\"\u0001U\u0003!\u0011\u0017\u0010^3D_\u0012,W#\u0001\u001e\t\u0011Y;%\u0011#Q\u0001\ni\n\u0011BY=uK\u000e{G-\u001a\u0011\t\u000b\u0005;E\u0011\u0001-\u0015\te[F,\u0018\t\u00035\u001ek\u0011\u0001\u0001\u0005\u0006\u0017^\u0003\ra\b\u0005\u0006\u001f^\u0003\ra\b\u0005\u0006'^\u0003\rA\u000f\u0005\u0006?\u001e#\t\u0001Y\u0001\tg\u000e\fG.Y*jOV\t1\tC\u0003c\u000f\u0012\u00051-A\u0006tKR\u0014\u0015\u0010^3D_\u0012,GCA-e\u0011\u0015\u0019\u0016\r1\u0001;\u0011\u001d1w)!A\u0005\u0002\u001d\fAaY8qsR!\u0011\f[5k\u0011\u001dYU\r%AA\u0002}AqaT3\u0011\u0002\u0003\u0007q\u0004C\u0004TKB\u0005\t\u0019\u0001\u001e\t\u000f1<\u0015\u0013!C\u0001[\u0006q1m\u001c9zI\u0011,g-Y;mi\u0012\nT#\u00018+\u0005}y7&\u00019\u0011\u0005E4X\"\u0001:\u000b\u0005M$\u0018!C;oG\",7m[3e\u0015\t)h\"\u0001\u0006b]:|G/\u0019;j_:L!a\u001e:\u0003#Ut7\r[3dW\u0016$g+\u0019:jC:\u001cW\rC\u0004z\u000fF\u0005I\u0011A7\u0002\u001d\r|\u0007/\u001f\u0013eK\u001a\fW\u000f\u001c;%e!91pRI\u0001\n\u0003a\u0018AD2paf$C-\u001a4bk2$HeM\u000b\u0002{*\u0012!h\u001c\u0005\t\u007f\u001e\u000b\t\u0011\"\u0011\u0002\u0002\u0005i\u0001O]8ek\u000e$\bK]3gSb,\"!a\u0001\u0011\t\u0005\u0015\u0011qB\u0007\u0003\u0003\u000fQA!!\u0003\u0002\f\u0005!A.\u00198h\u0015\t\ti!\u0001\u0003kCZ\f\u0017\u0002BA\t\u0003\u000f\u0011aa\u0015;sS:<\u0007\u0002CA\u000b\u000f\u0006\u0005I\u0011\u0001\u0010\u0002\u0019A\u0014x\u000eZ;di\u0006\u0013\u0018\u000e^=\t\u0013\u0005eq)!A\u0005\u0002\u0005m\u0011A\u00049s_\u0012,8\r^#mK6,g\u000e\u001e\u000b\u0005\u0003;\t\u0019\u0003E\u0002\u000e\u0003?I1!!\t\u000f\u0005\r\te.\u001f\u0005\n\u0003K\t9\"!AA\u0002}\t1\u0001\u001f\u00132\u0011%\tIcRA\u0001\n\u0003\nY#A\bqe>$Wo\u0019;Ji\u0016\u0014\u0018\r^8s+\t\ti\u0003\u0005\u0004\u00020\u0005U\u0012QD\u0007\u0003\u0003cQ1!a\r\u000f\u0003)\u0019w\u000e\u001c7fGRLwN\\\u0005\u0005\u0003o\t\tD\u0001\u0005Ji\u0016\u0014\u0018\r^8s\u0011%\tYdRA\u0001\n\u0003\ti$\u0001\u0005dC:,\u0015/^1m)\u0011\ty$!\u0012\u0011\u00075\t\t%C\u0002\u0002D9\u0011qAQ8pY\u0016\fg\u000e\u0003\u0006\u0002&\u0005e\u0012\u0011!a\u0001\u0003;A\u0011\"!\u0013H\u0003\u0003%\t%a\u0013\u0002\u0011!\f7\u000f[\"pI\u0016$\u0012a\b\u0005\n\u0003\u001f:\u0015\u0011!C!\u0003#\n\u0001\u0002^8TiJLgn\u001a\u000b\u0003\u0003\u0007A\u0011\"!\u0016H\u0003\u0003%\t%a\u0016\u0002\r\u0015\fX/\u00197t)\u0011\ty$!\u0017\t\u0015\u0005\u0015\u00121KA\u0001\u0002\u0004\tibB\u0005\u0002^\u0001\t\t\u0011#\u0001\u0002`\u0005)QI\u001c;ssB\u0019!,!\u0019\u0007\u0011!\u0003\u0011\u0011!E\u0001\u0003G\u001aR!!\u0019\u0002fe\u0001\u0002\"a\u001a\u0002n}y\"(W\u0007\u0003\u0003SR1!a\u001b\u000f\u0003\u001d\u0011XO\u001c;j[\u0016LA!a\u001c\u0002j\t\t\u0012IY:ue\u0006\u001cGOR;oGRLwN\\\u001a\t\u000f\u0005\u000b\t\u0007\"\u0001\u0002tQ\u0011\u0011q\f\u0005\u000b\u0003\u001f\n\t'!A\u0005F\u0005E\u0003BCA=\u0003C\n\t\u0011\"!\u0002|\u0005)\u0011\r\u001d9msR9\u0011,! \u0002\u0000\u0005\u0005\u0005BB&\u0002x\u0001\u0007q\u0004\u0003\u0004P\u0003o\u0002\ra\b\u0005\u0007'\u0006]\u0004\u0019\u0001\u001e\t\u0015\u0005\u0015\u0015\u0011MA\u0001\n\u0003\u000b9)A\u0004v]\u0006\u0004\b\u000f\\=\u0015\t\u0005%\u0015Q\u0013\t\u0006\u001b\u0005-\u0015qR\u0005\u0004\u0003\u001bs!AB(qi&|g\u000e\u0005\u0004\u000e\u0003#{rDO\u0005\u0004\u0003's!A\u0002+va2,7\u0007C\u0005\u0002\u0018\u0006\r\u0015\u0011!a\u00013\u0006\u0019\u0001\u0010\n\u0019\t\u000f\u0005m\u0005\u0001\"\u0001\u0002\u001e\u0006A\u0001.Y:F]R\u0014\u0018\u0010\u0006\u0003\u0002@\u0005}\u0005BB&\u0002\u001a\u0002\u0007q\u0004C\u0004\u0002$\u0002!\t!!*\u0002\u0011\u001d,G/\u00128uef$2!WAT\u0011\u0019Y\u0015\u0011\u0015a\u0001?!9\u00111\u0016\u0001\u0005\u0002\u00055\u0016A\u00039beN,WI\u001c;ssR!\u0011QDAX\u0011\u0019Y\u0015\u0011\u0016a\u0001?!9\u00111\u0017\u0001\u0005\u0004\u0005U\u0016!C1qa2L(+\u001e7f+\u0011\t9,!0\u0015\t\u0005e\u0016\u0011\u001a\t\u0005\u0003w\u000bi\f\u0004\u0001\u0005\u0011\u0005}\u0016\u0011\u0017b\u0001\u0003\u0003\u0014\u0011!Q\t\u0005\u0003\u0007\fi\u0002E\u0002\u000e\u0003\u000bL1!a2\u000f\u0005\u001dqu\u000e\u001e5j]\u001eD\u0001\"a3\u00022\u0002\u0007\u0011QZ\u0001\u0007a\u0006\u00148/\u001a:\u0011\r\u0005=\u0017Q[A]\u001d\rY\u0014\u0011[\u0005\u0004\u0003'\u0014\u0011aD*dC2\f7+[4QCJ\u001cXM]:\n\t\u0005]\u0017\u0011\u001c\u0002\u0007!\u0006\u00148/\u001a:\u000b\u0007\u0005M'\u0001C\u0004\u0002P\u0001!\t%!\u0015\t\u0015\u0005}\u0007\u0001#b\u0001\n\u0003\t\t/A\u0004ts6\u0014w\u000e\\:\u0016\u0005\u0005\r\b\u0003\u0002\u00175\u0003K\u00042aOAt\u0013\r\tIO\u0001\u0002\u0007'fl'm\u001c7\t\u0015\u00055\b\u0001#A!B\u0013\t\u0019/\u0001\u0005ts6\u0014w\u000e\\:!\u0011)\t\t\u0010\u0001EC\u0002\u0013\u0005\u00111_\u0001\u0010i>\u0004H*\u001a<fY\u000ec\u0017m]:fgV\u0011\u0011Q\u001f\t\u0006Y\u0005]\u00181`\u0005\u0004\u0003s4$\u0001\u0002'jgR\u00042aOA\u007f\u0013\r\tyP\u0001\u0002\f\u00072\f7o]*z[\n|G\u000e\u0003\u0006\u0003\u0004\u0001A\t\u0011)Q\u0005\u0003k\f\u0001\u0003^8q\u0019\u00164X\r\\\"mCN\u001cXm\u001d\u0011\t\u0015\t\u001d\u0001\u0001#b\u0001\n\u0003\u0011I!A\bu_BdUM^3m\u001f\nTWm\u0019;t+\t\u0011Y\u0001E\u0003-\u0003o\u0014i\u0001E\u0002<\u0005\u001fI1A!\u0005\u0003\u00051y%M[3diNKXNY8m\u0011)\u0011)\u0002\u0001E\u0001B\u0003&!1B\u0001\u0011i>\u0004H*\u001a<fY>\u0013'.Z2ug\u0002B\u0001B\u001a\u0001\u0002\u0002\u0013\u0005!\u0011\u0004\u000b\b\u0007\nm!Q\u0004B\u0010\u0011!i\"q\u0003I\u0001\u0002\u0004y\u0002\u0002C\u0013\u0003\u0018A\u0005\t\u0019A\u0010\t\u0011%\u00129\u0002%AA\u0002-Bq\u0001\u001c\u0001\u0012\u0002\u0013\u0005Q\u000eC\u0004z\u0001E\u0005I\u0011A7\t\u0011m\u0004\u0011\u0013!C\u0001\u0005O)\"A!\u000b+\u0005-z\u0007\u0002C@\u0001\u0003\u0003%\t%!\u0001\t\u0011\u0005U\u0001!!A\u0005\u0002yA\u0011\"!\u0007\u0001\u0003\u0003%\tA!\r\u0015\t\u0005u!1\u0007\u0005\n\u0003K\u0011y#!AA\u0002}A\u0011\"!\u000b\u0001\u0003\u0003%\t%a\u000b\t\u0013\u0005m\u0002!!A\u0005\u0002\teB\u0003BA \u0005wA!\"!\n\u00038\u0005\u0005\t\u0019AA\u000f\u0011%\tI\u0005AA\u0001\n\u0003\nY\u0005C\u0005\u0002V\u0001\t\t\u0011\"\u0011\u0003BQ!\u0011q\bB\"\u0011)\t)Ca\u0010\u0002\u0002\u0003\u0007\u0011QD\u0004\n\u0005\u000f\u0012\u0011\u0011!E\u0001\u0005\u0013\n\u0001bU2bY\u0006\u001c\u0016n\u001a\t\u0004w\t-c\u0001C\u0001\u0003\u0003\u0003E\tA!\u0014\u0014\u000b\t-#qJ\r\u0011\u0011\u0005\u001d\u0014QN\u0010 W\rCq!\u0011B&\t\u0003\u0011\u0019\u0006\u0006\u0002\u0003J!Q\u0011q\nB&\u0003\u0003%)%!\u0015\t\u0015\u0005e$1JA\u0001\n\u0003\u0013I\u0006F\u0004D\u00057\u0012iFa\u0018\t\ru\u00119\u00061\u0001 \u0011\u0019)#q\u000ba\u0001?!1\u0011Fa\u0016A\u0002-B!\"!\"\u0003L\u0005\u0005I\u0011\u0011B2)\u0011\u0011)G!\u001b\u0011\u000b5\tYIa\u001a\u0011\r5\t\tjH\u0010,\u0011%\t9J!\u0019\u0002\u0002\u0003\u00071\t\u0003\u0006\u0003n\t-\u0013\u0011!C\u0005\u0005_\n1B]3bIJ+7o\u001c7wKR\u0011!\u0011\u000f\t\u0005\u0003\u000b\u0011\u0019(\u0003\u0003\u0003v\u0005\u001d!AB(cU\u0016\u001cG\u000f")
public class ScalaSig implements DefaultMemoisable, Product, Serializable
{
    private final int majorVersion;
    private final int minorVersion;
    private final Seq<$tilde<Object, ByteCode>> table;
    private Seq<Symbol> symbols;
    private List<ClassSymbol> topLevelClasses;
    private List<ObjectSymbol> topLevelObjects;
    private volatile Entry$ Entry$module;
    private final HashMap<Object, Object> map;
    private volatile byte bitmap$0;
    
    public static Option<Tuple3<Object, Object, Seq<$tilde<Object, ByteCode>>>> unapply(final ScalaSig x$0) {
        return ScalaSig$.MODULE$.unapply(x$0);
    }
    
    public static ScalaSig apply(final int majorVersion, final int minorVersion, final Seq<$tilde<Object, ByteCode>> table) {
        return ScalaSig$.MODULE$.apply(majorVersion, minorVersion, table);
    }
    
    public static Function1<Tuple3<Object, Object, Seq<$tilde<Object, ByteCode>>>, ScalaSig> tupled() {
        return (Function1<Tuple3<Object, Object, Seq<$tilde<Object, ByteCode>>>, ScalaSig>)ScalaSig$.MODULE$.tupled();
    }
    
    public static Function1<Object, Function1<Object, Function1<Seq<$tilde<Object, ByteCode>>, ScalaSig>>> curried() {
        return (Function1<Object, Function1<Object, Function1<Seq<$tilde<Object, ByteCode>>, ScalaSig>>>)ScalaSig$.MODULE$.curried();
    }
    
    private Entry$ Entry$lzycompute() {
        synchronized (this) {
            if (this.Entry$module == null) {
                this.Entry$module = new Entry$();
            }
            final BoxedUnit unit = BoxedUnit.UNIT;
            return this.Entry$module;
        }
    }
    
    private Seq symbols$lzycompute() {
        synchronized (this) {
            if ((byte)(this.bitmap$0 & 0x1) == 0) {
                this.symbols = (Seq<Symbol>)this.applyRule((Rule<ScalaSig, ScalaSig, Seq, String>)ScalaSigParsers$.MODULE$.symbols());
                this.bitmap$0 |= 0x1;
            }
            final BoxedUnit unit = BoxedUnit.UNIT;
            return this.symbols;
        }
    }
    
    private List topLevelClasses$lzycompute() {
        synchronized (this) {
            if ((byte)(this.bitmap$0 & 0x2) == 0) {
                this.topLevelClasses = this.applyRule(ScalaSigParsers$.MODULE$.topLevelClasses());
                this.bitmap$0 |= 0x2;
            }
            final BoxedUnit unit = BoxedUnit.UNIT;
            return this.topLevelClasses;
        }
    }
    
    private List topLevelObjects$lzycompute() {
        synchronized (this) {
            if ((byte)(this.bitmap$0 & 0x4) == 0) {
                this.topLevelObjects = this.applyRule(ScalaSigParsers$.MODULE$.topLevelObjects());
                this.bitmap$0 |= 0x4;
            }
            final BoxedUnit unit = BoxedUnit.UNIT;
            return this.topLevelObjects;
        }
    }
    
    @Override
    public HashMap<Object, Object> map() {
        return this.map;
    }
    
    @Override
    public void org$json4s$scalap$DefaultMemoisable$_setter_$map_$eq(final HashMap x$1) {
        this.map = (HashMap<Object, Object>)x$1;
    }
    
    @Override
    public <A> A memo(final Object key, final Function0<A> a) {
        return (A)DefaultMemoisable$class.memo(this, key, a);
    }
    
    @Override
    public <A> Object compute(final Object key, final Function0<A> a) {
        return DefaultMemoisable$class.compute(this, key, a);
    }
    
    @Override
    public <S, T> void onSuccess(final Object key, final Success<S, T> result) {
        DefaultMemoisable$class.onSuccess(this, key, result);
    }
    
    public int majorVersion() {
        return this.majorVersion;
    }
    
    public int minorVersion() {
        return this.minorVersion;
    }
    
    public Seq<$tilde<Object, ByteCode>> table() {
        return this.table;
    }
    
    public Entry$ Entry() {
        return (this.Entry$module == null) ? this.Entry$lzycompute() : this.Entry$module;
    }
    
    public boolean hasEntry(final int index) {
        return this.table().isDefinedAt(index);
    }
    
    public Entry getEntry(final int index) {
        final $tilde $tilde = ($tilde)this.table().apply(index);
        if ($tilde != null) {
            final int entryType = BoxesRunTime.unboxToInt($tilde._1());
            final ByteCode byteCode = (ByteCode)$tilde._2();
            final Tuple2 tuple2 = new Tuple2((Object)BoxesRunTime.boxToInteger(entryType), (Object)byteCode);
            final int entryType2 = tuple2._1$mcI$sp();
            final ByteCode byteCode2 = (ByteCode)tuple2._2();
            return new Entry(index, entryType2, byteCode2);
        }
        throw new MatchError((Object)$tilde);
    }
    
    public Object parseEntry(final int index) {
        return this.applyRule((Rule<ScalaSig, ScalaSig, Object, String>)ScalaSigParsers$.MODULE$.parseEntry((Rule<Entry, Entry, A, String>)ScalaSigEntryParsers$.MODULE$.entry(), index));
    }
    
    public <A> A applyRule(final Rule<ScalaSig, ScalaSig, A, String> parser) {
        return (A)ScalaSigParsers$.MODULE$.expect((Rule<Object, Object, Object, Object>)parser).apply((Object)this);
    }
    
    @Override
    public String toString() {
        return new StringBuilder().append((Object)"ScalaSig version ").append((Object)BoxesRunTime.boxToInteger(this.majorVersion())).append((Object)".").append((Object)BoxesRunTime.boxToInteger(this.minorVersion())).append((Object)((TraversableOnce)RichInt$.MODULE$.until$extension0(Predef$.MODULE$.intWrapper(0), this.table().size()).map((Function1)new ScalaSig$$anonfun$toString.ScalaSig$$anonfun$toString$1(this), IndexedSeq$.MODULE$.canBuildFrom())).mkString("\n", "\n", "")).toString();
    }
    
    public Seq<Symbol> symbols() {
        return (Seq<Symbol>)(((byte)(this.bitmap$0 & 0x1) == 0) ? this.symbols$lzycompute() : this.symbols);
    }
    
    public List<ClassSymbol> topLevelClasses() {
        return (List<ClassSymbol>)(((byte)(this.bitmap$0 & 0x2) == 0) ? this.topLevelClasses$lzycompute() : this.topLevelClasses);
    }
    
    public List<ObjectSymbol> topLevelObjects() {
        return (List<ObjectSymbol>)(((byte)(this.bitmap$0 & 0x4) == 0) ? this.topLevelObjects$lzycompute() : this.topLevelObjects);
    }
    
    public ScalaSig copy(final int majorVersion, final int minorVersion, final Seq<$tilde<Object, ByteCode>> table) {
        return new ScalaSig(majorVersion, minorVersion, table);
    }
    
    public int copy$default$1() {
        return this.majorVersion();
    }
    
    public int copy$default$2() {
        return this.minorVersion();
    }
    
    public Seq<$tilde<Object, ByteCode>> copy$default$3() {
        return this.table();
    }
    
    public String productPrefix() {
        return "ScalaSig";
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
                o = this.table();
                break;
            }
            case 1: {
                o = BoxesRunTime.boxToInteger(this.minorVersion());
                break;
            }
            case 0: {
                o = BoxesRunTime.boxToInteger(this.majorVersion());
                break;
            }
        }
        return o;
    }
    
    public Iterator<Object> productIterator() {
        return (Iterator<Object>)ScalaRunTime$.MODULE$.typedProductIterator((Product)this);
    }
    
    public boolean canEqual(final Object x$1) {
        return x$1 instanceof ScalaSig;
    }
    
    @Override
    public int hashCode() {
        return Statics.finalizeHash(Statics.mix(Statics.mix(Statics.mix(-889275714, this.majorVersion()), this.minorVersion()), Statics.anyHash((Object)this.table())), 3);
    }
    
    @Override
    public boolean equals(final Object x$1) {
        if (this != x$1) {
            if (x$1 instanceof ScalaSig) {
                final ScalaSig scalaSig = (ScalaSig)x$1;
                boolean b = false;
                Label_0101: {
                    Label_0100: {
                        if (this.majorVersion() == scalaSig.majorVersion() && this.minorVersion() == scalaSig.minorVersion()) {
                            final Seq<$tilde<Object, ByteCode>> table = this.table();
                            final Seq<$tilde<Object, ByteCode>> table2 = scalaSig.table();
                            if (table == null) {
                                if (table2 != null) {
                                    break Label_0100;
                                }
                            }
                            else if (!table.equals(table2)) {
                                break Label_0100;
                            }
                            if (scalaSig.canEqual(this)) {
                                b = true;
                                break Label_0101;
                            }
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
    
    public ScalaSig(final int majorVersion, final int minorVersion, final Seq<$tilde<Object, ByteCode>> table) {
        this.majorVersion = majorVersion;
        this.minorVersion = minorVersion;
        this.table = table;
        DefaultMemoisable$class.$init$(this);
        Product$class.$init$((Product)this);
    }
    
    public class Entry$ extends AbstractFunction3<Object, Object, ByteCode, Entry> implements Serializable
    {
        public final String toString() {
            return "Entry";
        }
        
        public Entry apply(final int index, final int entryType, final ByteCode byteCode) {
            return new Entry(index, entryType, byteCode);
        }
        
        public Option<Tuple3<Object, Object, ByteCode>> unapply(final Entry x$0) {
            return (Option<Tuple3<Object, Object, ByteCode>>)((x$0 == null) ? None$.MODULE$ : new Some((Object)new Tuple3((Object)BoxesRunTime.boxToInteger(x$0.index()), (Object)BoxesRunTime.boxToInteger(x$0.entryType()), (Object)x$0.byteCode())));
        }
        
        public Entry$() {
            if (ScalaSig.this == null) {
                throw null;
            }
        }
    }
    
    public class Entry implements DefaultMemoisable, Product, Serializable
    {
        private final int index;
        private final int entryType;
        private final ByteCode byteCode;
        private final HashMap<Object, Object> map;
        
        @Override
        public HashMap<Object, Object> map() {
            return this.map;
        }
        
        @Override
        public void org$json4s$scalap$DefaultMemoisable$_setter_$map_$eq(final HashMap x$1) {
            this.map = (HashMap<Object, Object>)x$1;
        }
        
        @Override
        public <A> A memo(final Object key, final Function0<A> a) {
            return (A)DefaultMemoisable$class.memo(this, key, a);
        }
        
        @Override
        public <A> Object compute(final Object key, final Function0<A> a) {
            return DefaultMemoisable$class.compute(this, key, a);
        }
        
        @Override
        public <S, T> void onSuccess(final Object key, final Success<S, T> result) {
            DefaultMemoisable$class.onSuccess(this, key, result);
        }
        
        public int index() {
            return this.index;
        }
        
        public int entryType() {
            return this.entryType;
        }
        
        public ByteCode byteCode() {
            return this.byteCode;
        }
        
        public ScalaSig scalaSig() {
            return this.org$json4s$scalap$scalasig$ScalaSig$Entry$$$outer();
        }
        
        public Entry setByteCode(final ByteCode byteCode) {
            return this.org$json4s$scalap$scalasig$ScalaSig$Entry$$$outer().new Entry(this.index(), this.entryType(), byteCode);
        }
        
        public Entry copy(final int index, final int entryType, final ByteCode byteCode) {
            return this.org$json4s$scalap$scalasig$ScalaSig$Entry$$$outer().new Entry(index, entryType, byteCode);
        }
        
        public int copy$default$1() {
            return this.index();
        }
        
        public int copy$default$2() {
            return this.entryType();
        }
        
        public ByteCode copy$default$3() {
            return this.byteCode();
        }
        
        public String productPrefix() {
            return "Entry";
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
                    o = this.byteCode();
                    break;
                }
                case 1: {
                    o = BoxesRunTime.boxToInteger(this.entryType());
                    break;
                }
                case 0: {
                    o = BoxesRunTime.boxToInteger(this.index());
                    break;
                }
            }
            return o;
        }
        
        public Iterator<Object> productIterator() {
            return (Iterator<Object>)ScalaRunTime$.MODULE$.typedProductIterator((Product)this);
        }
        
        public boolean canEqual(final Object x$1) {
            return x$1 instanceof Entry;
        }
        
        @Override
        public int hashCode() {
            return Statics.finalizeHash(Statics.mix(Statics.mix(Statics.mix(-889275714, this.index()), this.entryType()), Statics.anyHash((Object)this.byteCode())), 3);
        }
        
        @Override
        public String toString() {
            return ScalaRunTime$.MODULE$._toString((Product)this);
        }
        
        @Override
        public boolean equals(final Object x$1) {
            if (this != x$1) {
                if (x$1 instanceof Entry && ((Entry)x$1).org$json4s$scalap$scalasig$ScalaSig$Entry$$$outer() == this.org$json4s$scalap$scalasig$ScalaSig$Entry$$$outer()) {
                    final Entry entry = (Entry)x$1;
                    boolean b = false;
                    Label_0115: {
                        Label_0114: {
                            if (this.index() == entry.index() && this.entryType() == entry.entryType()) {
                                final ByteCode byteCode = this.byteCode();
                                final ByteCode byteCode2 = entry.byteCode();
                                if (byteCode == null) {
                                    if (byteCode2 != null) {
                                        break Label_0114;
                                    }
                                }
                                else if (!byteCode.equals(byteCode2)) {
                                    break Label_0114;
                                }
                                if (entry.canEqual(this)) {
                                    b = true;
                                    break Label_0115;
                                }
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
        
        public /* synthetic */ ScalaSig org$json4s$scalap$scalasig$ScalaSig$Entry$$$outer() {
            return ScalaSig.this;
        }
        
        public Entry(final int index, final int entryType, final ByteCode byteCode) {
            this.index = index;
            this.entryType = entryType;
            this.byteCode = byteCode;
            if (ScalaSig.this == null) {
                throw null;
            }
            DefaultMemoisable$class.$init$(this);
            Product$class.$init$((Product)this);
        }
    }
}
