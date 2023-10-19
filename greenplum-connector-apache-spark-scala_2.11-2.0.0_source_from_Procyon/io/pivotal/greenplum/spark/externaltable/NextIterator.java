// 
// Decompiled by Procyon v0.5.36
// 

package io.pivotal.greenplum.spark.externaltable;

import scala.collection.GenTraversable;
import scala.collection.GenIterable;
import scala.collection.GenSeq;
import scala.collection.GenSet;
import scala.collection.GenMap;
import java.util.NoSuchElementException;
import scala.collection.mutable.StringBuilder;
import scala.collection.immutable.Map;
import scala.Predef;
import scala.runtime.Nothing$;
import scala.collection.generic.CanBuildFrom;
import scala.collection.immutable.Vector;
import scala.collection.immutable.Set;
import scala.collection.immutable.IndexedSeq;
import scala.collection.Seq;
import scala.collection.Iterable;
import scala.reflect.ClassTag;
import scala.collection.mutable.Buffer;
import scala.math.Ordering;
import scala.math.Numeric;
import scala.collection.TraversableOnce;
import scala.collection.TraversableOnce$class;
import scala.collection.immutable.List;
import scala.collection.immutable.Stream;
import scala.collection.Traversable;
import scala.collection.BufferedIterator;
import scala.Option;
import scala.Tuple2;
import scala.PartialFunction;
import scala.Function2;
import scala.collection.GenTraversableOnce;
import scala.Function0;
import scala.Function1;
import scala.collection.Iterator$class;
import scala.reflect.ScalaSignature;
import scala.collection.Iterator;

@ScalaSignature(bytes = "\u0006\u0001\u00194a!\u0001\u0002\u0002\u0002\u0011a!\u0001\u0004(fqRLE/\u001a:bi>\u0014(BA\u0002\u0005\u00035)\u0007\u0010^3s]\u0006dG/\u00192mK*\u0011QAB\u0001\u0006gB\f'o\u001b\u0006\u0003\u000f!\t\u0011b\u001a:fK:\u0004H.^7\u000b\u0005%Q\u0011a\u00029jm>$\u0018\r\u001c\u0006\u0002\u0017\u0005\u0011\u0011n\\\u000b\u0003\u001b\r\u001a2\u0001\u0001\b\u0015!\ty!#D\u0001\u0011\u0015\u0005\t\u0012!B:dC2\f\u0017BA\n\u0011\u0005\u0019\te.\u001f*fMB\u0019QCH\u0011\u000f\u0005YabBA\f\u001c\u001b\u0005A\"BA\r\u001b\u0003\u0019a$o\\8u}\r\u0001\u0011\"A\t\n\u0005u\u0001\u0012a\u00029bG.\fw-Z\u0005\u0003?\u0001\u0012\u0001\"\u0013;fe\u0006$xN\u001d\u0006\u0003;A\u0001\"AI\u0012\r\u0001\u0011)A\u0005\u0001b\u0001K\t\tQ+\u0005\u0002'SA\u0011qbJ\u0005\u0003QA\u0011qAT8uQ&tw\r\u0005\u0002\u0010U%\u00111\u0006\u0005\u0002\u0004\u0003:L\b\"B\u0017\u0001\t\u0003q\u0013A\u0002\u001fj]&$h\bF\u00010!\r\u0001\u0004!I\u0007\u0002\u0005!9!\u0007\u0001a\u0001\n\u0013\u0019\u0014aB4pi:+\u0007\u0010^\u000b\u0002iA\u0011q\"N\u0005\u0003mA\u0011qAQ8pY\u0016\fg\u000eC\u00049\u0001\u0001\u0007I\u0011B\u001d\u0002\u0017\u001d|GOT3yi~#S-\u001d\u000b\u0003uu\u0002\"aD\u001e\n\u0005q\u0002\"\u0001B+oSRDqAP\u001c\u0002\u0002\u0003\u0007A'A\u0002yIEBa\u0001\u0011\u0001!B\u0013!\u0014\u0001C4pi:+\u0007\u0010\u001e\u0011\t\u0013\t\u0003\u0001\u0019!a\u0001\n\u0013\u0019\u0015!\u00038fqR4\u0016\r\\;f+\u0005\t\u0003\"C#\u0001\u0001\u0004\u0005\r\u0011\"\u0003G\u00035qW\r\u001f;WC2,Xm\u0018\u0013fcR\u0011!h\u0012\u0005\b}\u0011\u000b\t\u00111\u0001\"\u0011\u0019I\u0005\u0001)Q\u0005C\u0005Qa.\u001a=u-\u0006dW/\u001a\u0011\t\u000f-\u0003\u0001\u0019!C\u0005g\u000511\r\\8tK\u0012Dq!\u0014\u0001A\u0002\u0013%a*\u0001\u0006dY>\u001cX\rZ0%KF$\"AO(\t\u000fyb\u0015\u0011!a\u0001i!1\u0011\u000b\u0001Q!\nQ\nqa\u00197pg\u0016$\u0007\u0005C\u0004T\u0001\u0001\u0007I\u0011C\u001a\u0002\u0011\u0019Lg.[:iK\u0012Dq!\u0016\u0001A\u0002\u0013Ea+\u0001\u0007gS:L7\u000f[3e?\u0012*\u0017\u000f\u0006\u0002;/\"9a\bVA\u0001\u0002\u0004!\u0004BB-\u0001A\u0003&A'A\u0005gS:L7\u000f[3eA!)1\f\u0001D\t9\u00069q-\u001a;OKb$H#A\u0011\t\u000by\u0003a\u0011C0\u0002\u000b\rdwn]3\u0015\u0003iBQ!\u0019\u0001\u0005\u0002}\u000bQb\u00197pg\u0016LeMT3fI\u0016$\u0007\"B2\u0001\t\u0003\u001a\u0014a\u00025bg:+\u0007\u0010\u001e\u0005\u0006K\u0002!\t\u0005X\u0001\u0005]\u0016DH\u000f")
public abstract class NextIterator<U> implements Iterator<U>
{
    private boolean gotNext;
    private U nextValue;
    private boolean closed;
    private boolean finished;
    
    public Iterator<U> seq() {
        return (Iterator<U>)Iterator$class.seq((Iterator)this);
    }
    
    public boolean isEmpty() {
        return Iterator$class.isEmpty((Iterator)this);
    }
    
    public boolean isTraversableAgain() {
        return Iterator$class.isTraversableAgain((Iterator)this);
    }
    
    public boolean hasDefiniteSize() {
        return Iterator$class.hasDefiniteSize((Iterator)this);
    }
    
    public Iterator<U> take(final int n) {
        return (Iterator<U>)Iterator$class.take((Iterator)this, n);
    }
    
    public Iterator<U> drop(final int n) {
        return (Iterator<U>)Iterator$class.drop((Iterator)this, n);
    }
    
    public Iterator<U> slice(final int from, final int until) {
        return (Iterator<U>)Iterator$class.slice((Iterator)this, from, until);
    }
    
    public <B> Iterator<B> map(final Function1<U, B> f) {
        return (Iterator<B>)Iterator$class.map((Iterator)this, (Function1)f);
    }
    
    public <B> Iterator<B> $plus$plus(final Function0<GenTraversableOnce<B>> that) {
        return (Iterator<B>)Iterator$class.$plus$plus((Iterator)this, (Function0)that);
    }
    
    public <B> Iterator<B> flatMap(final Function1<U, GenTraversableOnce<B>> f) {
        return (Iterator<B>)Iterator$class.flatMap((Iterator)this, (Function1)f);
    }
    
    public Iterator<U> filter(final Function1<U, Object> p) {
        return (Iterator<U>)Iterator$class.filter((Iterator)this, (Function1)p);
    }
    
    public <B> boolean corresponds(final GenTraversableOnce<B> that, final Function2<U, B, Object> p) {
        return Iterator$class.corresponds((Iterator)this, (GenTraversableOnce)that, (Function2)p);
    }
    
    public Iterator<U> withFilter(final Function1<U, Object> p) {
        return (Iterator<U>)Iterator$class.withFilter((Iterator)this, (Function1)p);
    }
    
    public Iterator<U> filterNot(final Function1<U, Object> p) {
        return (Iterator<U>)Iterator$class.filterNot((Iterator)this, (Function1)p);
    }
    
    public <B> Iterator<B> collect(final PartialFunction<U, B> pf) {
        return (Iterator<B>)Iterator$class.collect((Iterator)this, (PartialFunction)pf);
    }
    
    public <B> Iterator<B> scanLeft(final B z, final Function2<B, U, B> op) {
        return (Iterator<B>)Iterator$class.scanLeft((Iterator)this, (Object)z, (Function2)op);
    }
    
    public <B> Iterator<B> scanRight(final B z, final Function2<U, B, B> op) {
        return (Iterator<B>)Iterator$class.scanRight((Iterator)this, (Object)z, (Function2)op);
    }
    
    public Iterator<U> takeWhile(final Function1<U, Object> p) {
        return (Iterator<U>)Iterator$class.takeWhile((Iterator)this, (Function1)p);
    }
    
    public Tuple2<Iterator<U>, Iterator<U>> partition(final Function1<U, Object> p) {
        return (Tuple2<Iterator<U>, Iterator<U>>)Iterator$class.partition((Iterator)this, (Function1)p);
    }
    
    public Tuple2<Iterator<U>, Iterator<U>> span(final Function1<U, Object> p) {
        return (Tuple2<Iterator<U>, Iterator<U>>)Iterator$class.span((Iterator)this, (Function1)p);
    }
    
    public Iterator<U> dropWhile(final Function1<U, Object> p) {
        return (Iterator<U>)Iterator$class.dropWhile((Iterator)this, (Function1)p);
    }
    
    public <B> Iterator<Tuple2<U, B>> zip(final Iterator<B> that) {
        return (Iterator<Tuple2<U, B>>)Iterator$class.zip((Iterator)this, (Iterator)that);
    }
    
    public <A1> Iterator<A1> padTo(final int len, final A1 elem) {
        return (Iterator<A1>)Iterator$class.padTo((Iterator)this, len, (Object)elem);
    }
    
    public Iterator<Tuple2<U, Object>> zipWithIndex() {
        return (Iterator<Tuple2<U, Object>>)Iterator$class.zipWithIndex((Iterator)this);
    }
    
    public <B, A1, B1> Iterator<Tuple2<A1, B1>> zipAll(final Iterator<B> that, final A1 thisElem, final B1 thatElem) {
        return (Iterator<Tuple2<A1, B1>>)Iterator$class.zipAll((Iterator)this, (Iterator)that, (Object)thisElem, (Object)thatElem);
    }
    
    public <U> void foreach(final Function1<U, U> f) {
        Iterator$class.foreach((Iterator)this, (Function1)f);
    }
    
    public boolean forall(final Function1<U, Object> p) {
        return Iterator$class.forall((Iterator)this, (Function1)p);
    }
    
    public boolean exists(final Function1<U, Object> p) {
        return Iterator$class.exists((Iterator)this, (Function1)p);
    }
    
    public boolean contains(final Object elem) {
        return Iterator$class.contains((Iterator)this, elem);
    }
    
    public Option<U> find(final Function1<U, Object> p) {
        return (Option<U>)Iterator$class.find((Iterator)this, (Function1)p);
    }
    
    public int indexWhere(final Function1<U, Object> p) {
        return Iterator$class.indexWhere((Iterator)this, (Function1)p);
    }
    
    public <B> int indexOf(final B elem) {
        return Iterator$class.indexOf((Iterator)this, (Object)elem);
    }
    
    public BufferedIterator<U> buffered() {
        return (BufferedIterator<U>)Iterator$class.buffered((Iterator)this);
    }
    
    public <B> Iterator.GroupedIterator<B> grouped(final int size) {
        return (Iterator.GroupedIterator<B>)Iterator$class.grouped((Iterator)this, size);
    }
    
    public <B> Iterator.GroupedIterator<B> sliding(final int size, final int step) {
        return (Iterator.GroupedIterator<B>)Iterator$class.sliding((Iterator)this, size, step);
    }
    
    public int length() {
        return Iterator$class.length((Iterator)this);
    }
    
    public Tuple2<Iterator<U>, Iterator<U>> duplicate() {
        return (Tuple2<Iterator<U>, Iterator<U>>)Iterator$class.duplicate((Iterator)this);
    }
    
    public <B> Iterator<B> patch(final int from, final Iterator<B> patchElems, final int replaced) {
        return (Iterator<B>)Iterator$class.patch((Iterator)this, from, (Iterator)patchElems, replaced);
    }
    
    public <B> void copyToArray(final Object xs, final int start, final int len) {
        Iterator$class.copyToArray((Iterator)this, xs, start, len);
    }
    
    public boolean sameElements(final Iterator<?> that) {
        return Iterator$class.sameElements((Iterator)this, (Iterator)that);
    }
    
    public Traversable<U> toTraversable() {
        return (Traversable<U>)Iterator$class.toTraversable((Iterator)this);
    }
    
    public Iterator<U> toIterator() {
        return (Iterator<U>)Iterator$class.toIterator((Iterator)this);
    }
    
    public Stream<U> toStream() {
        return (Stream<U>)Iterator$class.toStream((Iterator)this);
    }
    
    @Override
    public String toString() {
        return Iterator$class.toString((Iterator)this);
    }
    
    public <B> int sliding$default$2() {
        return Iterator$class.sliding$default$2((Iterator)this);
    }
    
    public List<U> reversed() {
        return (List<U>)TraversableOnce$class.reversed((TraversableOnce)this);
    }
    
    public int size() {
        return TraversableOnce$class.size((TraversableOnce)this);
    }
    
    public boolean nonEmpty() {
        return TraversableOnce$class.nonEmpty((TraversableOnce)this);
    }
    
    public int count(final Function1<U, Object> p) {
        return TraversableOnce$class.count((TraversableOnce)this, (Function1)p);
    }
    
    public <B> Option<B> collectFirst(final PartialFunction<U, B> pf) {
        return (Option<B>)TraversableOnce$class.collectFirst((TraversableOnce)this, (PartialFunction)pf);
    }
    
    public <B> B $div$colon(final B z, final Function2<B, U, B> op) {
        return (B)TraversableOnce$class.$div$colon((TraversableOnce)this, (Object)z, (Function2)op);
    }
    
    public <B> B $colon$bslash(final B z, final Function2<U, B, B> op) {
        return (B)TraversableOnce$class.$colon$bslash((TraversableOnce)this, (Object)z, (Function2)op);
    }
    
    public <B> B foldLeft(final B z, final Function2<B, U, B> op) {
        return (B)TraversableOnce$class.foldLeft((TraversableOnce)this, (Object)z, (Function2)op);
    }
    
    public <B> B foldRight(final B z, final Function2<U, B, B> op) {
        return (B)TraversableOnce$class.foldRight((TraversableOnce)this, (Object)z, (Function2)op);
    }
    
    public <B> B reduceLeft(final Function2<B, U, B> op) {
        return (B)TraversableOnce$class.reduceLeft((TraversableOnce)this, (Function2)op);
    }
    
    public <B> B reduceRight(final Function2<U, B, B> op) {
        return (B)TraversableOnce$class.reduceRight((TraversableOnce)this, (Function2)op);
    }
    
    public <B> Option<B> reduceLeftOption(final Function2<B, U, B> op) {
        return (Option<B>)TraversableOnce$class.reduceLeftOption((TraversableOnce)this, (Function2)op);
    }
    
    public <B> Option<B> reduceRightOption(final Function2<U, B, B> op) {
        return (Option<B>)TraversableOnce$class.reduceRightOption((TraversableOnce)this, (Function2)op);
    }
    
    public <A1> A1 reduce(final Function2<A1, A1, A1> op) {
        return (A1)TraversableOnce$class.reduce((TraversableOnce)this, (Function2)op);
    }
    
    public <A1> Option<A1> reduceOption(final Function2<A1, A1, A1> op) {
        return (Option<A1>)TraversableOnce$class.reduceOption((TraversableOnce)this, (Function2)op);
    }
    
    public <A1> A1 fold(final A1 z, final Function2<A1, A1, A1> op) {
        return (A1)TraversableOnce$class.fold((TraversableOnce)this, (Object)z, (Function2)op);
    }
    
    public <B> B aggregate(final Function0<B> z, final Function2<B, U, B> seqop, final Function2<B, B, B> combop) {
        return (B)TraversableOnce$class.aggregate((TraversableOnce)this, (Function0)z, (Function2)seqop, (Function2)combop);
    }
    
    public <B> B sum(final Numeric<B> num) {
        return (B)TraversableOnce$class.sum((TraversableOnce)this, (Numeric)num);
    }
    
    public <B> B product(final Numeric<B> num) {
        return (B)TraversableOnce$class.product((TraversableOnce)this, (Numeric)num);
    }
    
    public <B> U min(final Ordering<B> cmp) {
        return (U)TraversableOnce$class.min((TraversableOnce)this, (Ordering)cmp);
    }
    
    public <B> U max(final Ordering<B> cmp) {
        return (U)TraversableOnce$class.max((TraversableOnce)this, (Ordering)cmp);
    }
    
    public <B> U maxBy(final Function1<U, B> f, final Ordering<B> cmp) {
        return (U)TraversableOnce$class.maxBy((TraversableOnce)this, (Function1)f, (Ordering)cmp);
    }
    
    public <B> U minBy(final Function1<U, B> f, final Ordering<B> cmp) {
        return (U)TraversableOnce$class.minBy((TraversableOnce)this, (Function1)f, (Ordering)cmp);
    }
    
    public <B> void copyToBuffer(final Buffer<B> dest) {
        TraversableOnce$class.copyToBuffer((TraversableOnce)this, (Buffer)dest);
    }
    
    public <B> void copyToArray(final Object xs, final int start) {
        TraversableOnce$class.copyToArray((TraversableOnce)this, xs, start);
    }
    
    public <B> void copyToArray(final Object xs) {
        TraversableOnce$class.copyToArray((TraversableOnce)this, xs);
    }
    
    public <B> Object toArray(final ClassTag<B> evidence$1) {
        return TraversableOnce$class.toArray((TraversableOnce)this, (ClassTag)evidence$1);
    }
    
    public List<U> toList() {
        return (List<U>)TraversableOnce$class.toList((TraversableOnce)this);
    }
    
    public Iterable<U> toIterable() {
        return (Iterable<U>)TraversableOnce$class.toIterable((TraversableOnce)this);
    }
    
    public Seq<U> toSeq() {
        return (Seq<U>)TraversableOnce$class.toSeq((TraversableOnce)this);
    }
    
    public IndexedSeq<U> toIndexedSeq() {
        return (IndexedSeq<U>)TraversableOnce$class.toIndexedSeq((TraversableOnce)this);
    }
    
    public <B> Buffer<B> toBuffer() {
        return (Buffer<B>)TraversableOnce$class.toBuffer((TraversableOnce)this);
    }
    
    public <B> Set<B> toSet() {
        return (Set<B>)TraversableOnce$class.toSet((TraversableOnce)this);
    }
    
    public Vector<U> toVector() {
        return (Vector<U>)TraversableOnce$class.toVector((TraversableOnce)this);
    }
    
    public <Col> Col to(final CanBuildFrom<Nothing$, U, Col> cbf) {
        return (Col)TraversableOnce$class.to((TraversableOnce)this, (CanBuildFrom)cbf);
    }
    
    public <T, U> Map<T, U> toMap(final Predef.$less$colon$less<U, Tuple2<T, U>> ev) {
        return (Map<T, U>)TraversableOnce$class.toMap((TraversableOnce)this, (Predef.$less$colon$less)ev);
    }
    
    public String mkString(final String start, final String sep, final String end) {
        return TraversableOnce$class.mkString((TraversableOnce)this, start, sep, end);
    }
    
    public String mkString(final String sep) {
        return TraversableOnce$class.mkString((TraversableOnce)this, sep);
    }
    
    public String mkString() {
        return TraversableOnce$class.mkString((TraversableOnce)this);
    }
    
    public StringBuilder addString(final StringBuilder b, final String start, final String sep, final String end) {
        return TraversableOnce$class.addString((TraversableOnce)this, b, start, sep, end);
    }
    
    public StringBuilder addString(final StringBuilder b, final String sep) {
        return TraversableOnce$class.addString((TraversableOnce)this, b, sep);
    }
    
    public StringBuilder addString(final StringBuilder b) {
        return TraversableOnce$class.addString((TraversableOnce)this, b);
    }
    
    private boolean gotNext() {
        return this.gotNext;
    }
    
    private void gotNext_$eq(final boolean x$1) {
        this.gotNext = x$1;
    }
    
    private U nextValue() {
        return this.nextValue;
    }
    
    private void nextValue_$eq(final U x$1) {
        this.nextValue = x$1;
    }
    
    private boolean closed() {
        return this.closed;
    }
    
    private void closed_$eq(final boolean x$1) {
        this.closed = x$1;
    }
    
    public boolean finished() {
        return this.finished;
    }
    
    public void finished_$eq(final boolean x$1) {
        this.finished = x$1;
    }
    
    public abstract U getNext();
    
    public abstract void close();
    
    public void closeIfNeeded() {
        if (!this.closed()) {
            this.closed_$eq(true);
            this.close();
        }
    }
    
    public boolean hasNext() {
        if (!this.finished() && !this.gotNext()) {
            this.nextValue_$eq(this.getNext());
            if (this.finished()) {
                this.closeIfNeeded();
            }
            this.gotNext_$eq(true);
        }
        return !this.finished();
    }
    
    public U next() {
        if (this.hasNext()) {
            this.gotNext_$eq(false);
            return this.nextValue();
        }
        throw new NoSuchElementException("End of stream");
    }
    
    public NextIterator() {
        TraversableOnce$class.$init$((TraversableOnce)this);
        Iterator$class.$init$((Iterator)this);
        this.gotNext = false;
        this.closed = false;
        this.finished = false;
    }
}
