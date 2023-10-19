// 
// Decompiled by Procyon v0.5.36
// 

package io.pivotal.greenplum.spark.externaltable;

import scala.MatchError;
import scala.util.Success;
import io.pivotal.greenplum.spark.GpfdistLocation;
import io.pivotal.greenplum.spark.ConnectorUtils$;
import org.apache.spark.SparkEnv$;
import io.pivotal.greenplum.spark.jdbc.ConnectionManager$;
import java.io.InputStream;
import scala.util.Try;
import org.postgresql.util.PSQLException;
import scala.util.Failure;
import io.pivotal.greenplum.spark.jdbc.Jdbc$;
import scala.collection.GenTraversable;
import scala.collection.GenIterable;
import scala.collection.GenSeq;
import scala.collection.GenSet;
import scala.collection.GenMap;
import scala.runtime.BoxesRunTime;
import scala.StringContext;
import org.apache.spark.sql.types.DataType;
import scala.reflect.ClassTag$;
import scala.Array$;
import org.apache.spark.sql.types.StructField;
import scala.Predef$;
import scala.collection.mutable.StringBuilder;
import scala.collection.immutable.Map;
import scala.Predef;
import scala.runtime.Nothing$;
import scala.collection.generic.CanBuildFrom;
import scala.collection.immutable.Vector;
import scala.collection.immutable.Set;
import scala.collection.immutable.IndexedSeq;
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
import scala.collection.GenTraversableOnce;
import scala.Function1;
import scala.collection.Iterator$class;
import scala.Function0;
import io.pivotal.greenplum.spark.Logging$class;
import scala.runtime.TraitSetter;
import org.apache.spark.sql.sources.Filter;
import org.slf4j.Logger;
import scala.runtime.BoxedUnit;
import org.apache.spark.sql.catalyst.expressions.SpecificInternalRow;
import scala.Function2;
import java.sql.Connection;
import scala.collection.Seq;
import io.pivotal.greenplum.spark.conf.GreenplumOptions;
import org.apache.spark.sql.types.StructType;
import io.pivotal.greenplum.spark.GreenplumPartition;
import scala.reflect.ScalaSignature;
import io.pivotal.greenplum.spark.Logging;
import org.apache.spark.sql.catalyst.InternalRow;
import scala.collection.Iterator;

@ScalaSignature(bytes = "\u0006\u0001\u0005Ud\u0001B\u0001\u0003\u00015\u0011Ac\u0012:fK:\u0004H.^7S_^LE/\u001a:bi>\u0014(BA\u0002\u0005\u00035)\u0007\u0010^3s]\u0006dG/\u00192mK*\u0011QAB\u0001\u0006gB\f'o\u001b\u0006\u0003\u000f!\t\u0011b\u001a:fK:\u0004H.^7\u000b\u0005%Q\u0011a\u00029jm>$\u0018\r\u001c\u0006\u0002\u0017\u0005\u0011\u0011n\\\u0002\u0001'\u0011\u0001a\u0002F\u0017\u0011\u0005=\u0011R\"\u0001\t\u000b\u0003E\tQa]2bY\u0006L!a\u0005\t\u0003\r\u0005s\u0017PU3g!\r)R\u0004\t\b\u0003-mq!a\u0006\u000e\u000e\u0003aQ!!\u0007\u0007\u0002\rq\u0012xn\u001c;?\u0013\u0005\t\u0012B\u0001\u000f\u0011\u0003\u001d\u0001\u0018mY6bO\u0016L!AH\u0010\u0003\u0011%#XM]1u_JT!\u0001\b\t\u0011\u0005\u0005ZS\"\u0001\u0012\u000b\u0005\r\"\u0013\u0001C2bi\u0006d\u0017p\u001d;\u000b\u0005\u00152\u0013aA:rY*\u0011Qa\n\u0006\u0003Q%\na!\u00199bG\",'\"\u0001\u0016\u0002\u0007=\u0014x-\u0003\u0002-E\tY\u0011J\u001c;fe:\fGNU8x!\tqs&D\u0001\u0005\u0013\t\u0001DAA\u0004M_\u001e<\u0017N\\4\t\u0011I\u0002!\u0011!Q\u0001\nM\nQ\"\u00199qY&\u001c\u0017\r^5p]&#\u0007C\u0001\u001b8\u001d\tyQ'\u0003\u00027!\u00051\u0001K]3eK\u001aL!\u0001O\u001d\u0003\rM#(/\u001b8h\u0015\t1\u0004\u0003\u0003\u0005<\u0001\t\u0005\t\u0015!\u0003=\u0003%\u0001\u0018M\u001d;ji&|g\u000e\u0005\u0002/{%\u0011a\b\u0002\u0002\u0013\u000fJ,WM\u001c9mk6\u0004\u0016M\u001d;ji&|g\u000e\u0003\u0005A\u0001\t\u0005\t\u0015!\u0003B\u0003\u0019\u00198\r[3nCB\u0011!)R\u0007\u0002\u0007*\u0011A\tJ\u0001\u0006if\u0004Xm]\u0005\u0003\r\u000e\u0013!b\u0015;sk\u000e$H+\u001f9f\u0011!A\u0005A!A!\u0002\u0013I\u0015\u0001E4sK\u0016t\u0007\u000f\\;n\u001fB$\u0018n\u001c8t!\tQU*D\u0001L\u0015\taE!\u0001\u0003d_:4\u0017B\u0001(L\u0005A9%/Z3oa2,Xn\u00149uS>t7\u000f\u0003\u0005Q\u0001\t\u0005\t\u0015!\u0003R\u0003)\tG\u000e\\\"pYVlgn\u001d\t\u0004+I\u001b\u0014BA* \u0005\r\u0019V-\u001d\u0005\t+\u0002\u0011\t\u0011)A\u0005-\u00069a-\u001b7uKJ\u001c\bcA\bX3&\u0011\u0001\f\u0005\u0002\u0006\u0003J\u0014\u0018-\u001f\t\u00035vk\u0011a\u0017\u0006\u00039\u0012\nqa]8ve\u000e,7/\u0003\u0002_7\n1a)\u001b7uKJDQ\u0001\u0019\u0001\u0005\u0002\u0005\fa\u0001P5oSRtDc\u00022eK\u001a<\u0007.\u001b\t\u0003G\u0002i\u0011A\u0001\u0005\u0006e}\u0003\ra\r\u0005\u0006w}\u0003\r\u0001\u0010\u0005\u0006\u0001~\u0003\r!\u0011\u0005\u0006\u0011~\u0003\r!\u0013\u0005\u0006!~\u0003\r!\u0015\u0005\u0006+~\u0003\rA\u0016\u0005\bW\u0002\u0011\r\u0011\"\u0003m\u0003\u0011\u0019wN\u001c8\u0016\u00035\u0004\"A\u001c:\u000e\u0003=T!!\n9\u000b\u0003E\fAA[1wC&\u00111o\u001c\u0002\u000b\u0007>tg.Z2uS>t\u0007BB;\u0001A\u0003%Q.A\u0003d_:t\u0007\u0005C\u0004x\u0001\t\u0007I\u0011\u0002=\u0002#\u0019LG\u000e^3s/\",'/Z\"mCV\u001cX-F\u00014\u0011\u0019Q\b\u0001)A\u0005g\u0005\u0011b-\u001b7uKJ<\u0006.\u001a:f\u00072\fWo]3!\u0011\u001da\bA1A\u0005\na\fab\u001e5fe\u0016\u0004&/\u001a3jG\u0006$X\r\u0003\u0004\u007f\u0001\u0001\u0006IaM\u0001\u0010o\",'/\u001a)sK\u0012L7-\u0019;fA!I\u0011\u0011\u0001\u0001C\u0002\u0013\u0005\u00111A\u0001\rI\u0006$\u0018-\u0013;fe\u0006$xN]\u000b\u0003\u0003\u000b\u00012aYA\u0004\u0013\r\tIA\u0001\u0002\r\t\u0006$\u0018-\u0013;fe\u0006$xN\u001d\u0005\t\u0003\u001b\u0001\u0001\u0015!\u0003\u0002\u0006\u0005iA-\u0019;b\u0013R,'/\u0019;pe\u0002B\u0011\"!\u0005\u0001\u0001\u0004%\t!a\u0005\u0002\u001dA\u0014xnY3tg\u0016$7i\\;oiV\u0011\u0011Q\u0003\t\u0004\u001f\u0005]\u0011bAA\r!\t\u0019\u0011J\u001c;\t\u0013\u0005u\u0001\u00011A\u0005\u0002\u0005}\u0011A\u00059s_\u000e,7o]3e\u0007>,h\u000e^0%KF$B!!\t\u0002(A\u0019q\"a\t\n\u0007\u0005\u0015\u0002C\u0001\u0003V]&$\bBCA\u0015\u00037\t\t\u00111\u0001\u0002\u0016\u0005\u0019\u0001\u0010J\u0019\t\u0011\u00055\u0002\u0001)Q\u0005\u0003+\tq\u0002\u001d:pG\u0016\u001c8/\u001a3D_VtG\u000f\t\u0005\n\u0003c\u0001!\u0019!C\u0001\u0003g\t!bY8om\u0016\u0014H/\u001a:t+\t\t)\u0004\u0005\u0003\u0010/\u0006]\u0002\u0003C\b\u0002:M\ni$!\t\n\u0007\u0005m\u0002CA\u0005Gk:\u001cG/[8oeA!\u0011qHA#\u001b\t\t\tEC\u0002\u0002D\t\n1\"\u001a=qe\u0016\u001c8/[8og&!\u0011qIA!\u0005M\u0019\u0006/Z2jM&\u001c\u0017J\u001c;fe:\fGNU8x\u0011!\tY\u0005\u0001Q\u0001\n\u0005U\u0012aC2p]Z,'\u000f^3sg\u0002Bq!a\u0014\u0001\t\u0003\n\t&A\u0004iCNtU\r\u001f;\u0016\u0005\u0005M\u0003cA\b\u0002V%\u0019\u0011q\u000b\t\u0003\u000f\t{w\u000e\\3b]\"9\u00111\f\u0001\u0005B\u0005u\u0013\u0001\u00028fqR$\u0012\u0001I\u0004\b\u0003C\u0012\u0001\u0012AA2\u0003Q9%/Z3oa2,XNU8x\u0013R,'/\u0019;peB\u00191-!\u001a\u0007\r\u0005\u0011\u0001\u0012AA4'\r\t)G\u0004\u0005\bA\u0006\u0015D\u0011AA6)\t\t\u0019\u0007\u0003\u0005\u0002p\u0005\u0015D\u0011AA9\u0003Q1\u0017\u000e\u001c;fe^CWM]3Qe\u0016$\u0017nY1uKR\u00191'a\u001d\t\rU\u000bi\u00071\u0001W\u0001")
public class GreenplumRowIterator implements Iterator<InternalRow>, Logging
{
    private final GreenplumPartition partition;
    public final StructType io$pivotal$greenplum$spark$externaltable$GreenplumRowIterator$$schema;
    public final GreenplumOptions io$pivotal$greenplum$spark$externaltable$GreenplumRowIterator$$greenplumOptions;
    private final Seq<String> allColumns;
    private final Connection conn;
    private final String io$pivotal$greenplum$spark$externaltable$GreenplumRowIterator$$filterWhereClause;
    private final String wherePredicate;
    private final DataIterator dataIterator;
    private int processedCount;
    private final Function2<String, SpecificInternalRow, BoxedUnit>[] converters;
    private transient Logger io$pivotal$greenplum$spark$Logging$$log_;
    
    public static String filterWherePredicate(final Filter[] filters) {
        return GreenplumRowIterator$.MODULE$.filterWherePredicate(filters);
    }
    
    public Logger io$pivotal$greenplum$spark$Logging$$log_() {
        return this.io$pivotal$greenplum$spark$Logging$$log_;
    }
    
    @TraitSetter
    public void io$pivotal$greenplum$spark$Logging$$log__$eq(final Logger x$1) {
        this.io$pivotal$greenplum$spark$Logging$$log_ = x$1;
    }
    
    public Logger log() {
        return Logging$class.log(this);
    }
    
    public void logWarning(final Function0<String> msg) {
        Logging$class.logWarning(this, msg);
    }
    
    public void logDebug(final Function0<String> msg) {
        Logging$class.logDebug(this, msg);
    }
    
    public Iterator<InternalRow> seq() {
        return (Iterator<InternalRow>)Iterator$class.seq((Iterator)this);
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
    
    public Iterator<InternalRow> take(final int n) {
        return (Iterator<InternalRow>)Iterator$class.take((Iterator)this, n);
    }
    
    public Iterator<InternalRow> drop(final int n) {
        return (Iterator<InternalRow>)Iterator$class.drop((Iterator)this, n);
    }
    
    public Iterator<InternalRow> slice(final int from, final int until) {
        return (Iterator<InternalRow>)Iterator$class.slice((Iterator)this, from, until);
    }
    
    public <B> Iterator<B> map(final Function1<InternalRow, B> f) {
        return (Iterator<B>)Iterator$class.map((Iterator)this, (Function1)f);
    }
    
    public <B> Iterator<B> $plus$plus(final Function0<GenTraversableOnce<B>> that) {
        return (Iterator<B>)Iterator$class.$plus$plus((Iterator)this, (Function0)that);
    }
    
    public <B> Iterator<B> flatMap(final Function1<InternalRow, GenTraversableOnce<B>> f) {
        return (Iterator<B>)Iterator$class.flatMap((Iterator)this, (Function1)f);
    }
    
    public Iterator<InternalRow> filter(final Function1<InternalRow, Object> p) {
        return (Iterator<InternalRow>)Iterator$class.filter((Iterator)this, (Function1)p);
    }
    
    public <B> boolean corresponds(final GenTraversableOnce<B> that, final Function2<InternalRow, B, Object> p) {
        return Iterator$class.corresponds((Iterator)this, (GenTraversableOnce)that, (Function2)p);
    }
    
    public Iterator<InternalRow> withFilter(final Function1<InternalRow, Object> p) {
        return (Iterator<InternalRow>)Iterator$class.withFilter((Iterator)this, (Function1)p);
    }
    
    public Iterator<InternalRow> filterNot(final Function1<InternalRow, Object> p) {
        return (Iterator<InternalRow>)Iterator$class.filterNot((Iterator)this, (Function1)p);
    }
    
    public <B> Iterator<B> collect(final PartialFunction<InternalRow, B> pf) {
        return (Iterator<B>)Iterator$class.collect((Iterator)this, (PartialFunction)pf);
    }
    
    public <B> Iterator<B> scanLeft(final B z, final Function2<B, InternalRow, B> op) {
        return (Iterator<B>)Iterator$class.scanLeft((Iterator)this, (Object)z, (Function2)op);
    }
    
    public <B> Iterator<B> scanRight(final B z, final Function2<InternalRow, B, B> op) {
        return (Iterator<B>)Iterator$class.scanRight((Iterator)this, (Object)z, (Function2)op);
    }
    
    public Iterator<InternalRow> takeWhile(final Function1<InternalRow, Object> p) {
        return (Iterator<InternalRow>)Iterator$class.takeWhile((Iterator)this, (Function1)p);
    }
    
    public Tuple2<Iterator<InternalRow>, Iterator<InternalRow>> partition(final Function1<InternalRow, Object> p) {
        return (Tuple2<Iterator<InternalRow>, Iterator<InternalRow>>)Iterator$class.partition((Iterator)this, (Function1)p);
    }
    
    public Tuple2<Iterator<InternalRow>, Iterator<InternalRow>> span(final Function1<InternalRow, Object> p) {
        return (Tuple2<Iterator<InternalRow>, Iterator<InternalRow>>)Iterator$class.span((Iterator)this, (Function1)p);
    }
    
    public Iterator<InternalRow> dropWhile(final Function1<InternalRow, Object> p) {
        return (Iterator<InternalRow>)Iterator$class.dropWhile((Iterator)this, (Function1)p);
    }
    
    public <B> Iterator<Tuple2<InternalRow, B>> zip(final Iterator<B> that) {
        return (Iterator<Tuple2<InternalRow, B>>)Iterator$class.zip((Iterator)this, (Iterator)that);
    }
    
    public <A1> Iterator<A1> padTo(final int len, final A1 elem) {
        return (Iterator<A1>)Iterator$class.padTo((Iterator)this, len, (Object)elem);
    }
    
    public Iterator<Tuple2<InternalRow, Object>> zipWithIndex() {
        return (Iterator<Tuple2<InternalRow, Object>>)Iterator$class.zipWithIndex((Iterator)this);
    }
    
    public <B, A1, B1> Iterator<Tuple2<A1, B1>> zipAll(final Iterator<B> that, final A1 thisElem, final B1 thatElem) {
        return (Iterator<Tuple2<A1, B1>>)Iterator$class.zipAll((Iterator)this, (Iterator)that, (Object)thisElem, (Object)thatElem);
    }
    
    public <U> void foreach(final Function1<InternalRow, U> f) {
        Iterator$class.foreach((Iterator)this, (Function1)f);
    }
    
    public boolean forall(final Function1<InternalRow, Object> p) {
        return Iterator$class.forall((Iterator)this, (Function1)p);
    }
    
    public boolean exists(final Function1<InternalRow, Object> p) {
        return Iterator$class.exists((Iterator)this, (Function1)p);
    }
    
    public boolean contains(final Object elem) {
        return Iterator$class.contains((Iterator)this, elem);
    }
    
    public Option<InternalRow> find(final Function1<InternalRow, Object> p) {
        return (Option<InternalRow>)Iterator$class.find((Iterator)this, (Function1)p);
    }
    
    public int indexWhere(final Function1<InternalRow, Object> p) {
        return Iterator$class.indexWhere((Iterator)this, (Function1)p);
    }
    
    public <B> int indexOf(final B elem) {
        return Iterator$class.indexOf((Iterator)this, (Object)elem);
    }
    
    public BufferedIterator<InternalRow> buffered() {
        return (BufferedIterator<InternalRow>)Iterator$class.buffered((Iterator)this);
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
    
    public Tuple2<Iterator<InternalRow>, Iterator<InternalRow>> duplicate() {
        return (Tuple2<Iterator<InternalRow>, Iterator<InternalRow>>)Iterator$class.duplicate((Iterator)this);
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
    
    public Traversable<InternalRow> toTraversable() {
        return (Traversable<InternalRow>)Iterator$class.toTraversable((Iterator)this);
    }
    
    public Iterator<InternalRow> toIterator() {
        return (Iterator<InternalRow>)Iterator$class.toIterator((Iterator)this);
    }
    
    public Stream<InternalRow> toStream() {
        return (Stream<InternalRow>)Iterator$class.toStream((Iterator)this);
    }
    
    @Override
    public String toString() {
        return Iterator$class.toString((Iterator)this);
    }
    
    public <B> int sliding$default$2() {
        return Iterator$class.sliding$default$2((Iterator)this);
    }
    
    public List<InternalRow> reversed() {
        return (List<InternalRow>)TraversableOnce$class.reversed((TraversableOnce)this);
    }
    
    public int size() {
        return TraversableOnce$class.size((TraversableOnce)this);
    }
    
    public boolean nonEmpty() {
        return TraversableOnce$class.nonEmpty((TraversableOnce)this);
    }
    
    public int count(final Function1<InternalRow, Object> p) {
        return TraversableOnce$class.count((TraversableOnce)this, (Function1)p);
    }
    
    public <B> Option<B> collectFirst(final PartialFunction<InternalRow, B> pf) {
        return (Option<B>)TraversableOnce$class.collectFirst((TraversableOnce)this, (PartialFunction)pf);
    }
    
    public <B> B $div$colon(final B z, final Function2<B, InternalRow, B> op) {
        return (B)TraversableOnce$class.$div$colon((TraversableOnce)this, (Object)z, (Function2)op);
    }
    
    public <B> B $colon$bslash(final B z, final Function2<InternalRow, B, B> op) {
        return (B)TraversableOnce$class.$colon$bslash((TraversableOnce)this, (Object)z, (Function2)op);
    }
    
    public <B> B foldLeft(final B z, final Function2<B, InternalRow, B> op) {
        return (B)TraversableOnce$class.foldLeft((TraversableOnce)this, (Object)z, (Function2)op);
    }
    
    public <B> B foldRight(final B z, final Function2<InternalRow, B, B> op) {
        return (B)TraversableOnce$class.foldRight((TraversableOnce)this, (Object)z, (Function2)op);
    }
    
    public <B> B reduceLeft(final Function2<B, InternalRow, B> op) {
        return (B)TraversableOnce$class.reduceLeft((TraversableOnce)this, (Function2)op);
    }
    
    public <B> B reduceRight(final Function2<InternalRow, B, B> op) {
        return (B)TraversableOnce$class.reduceRight((TraversableOnce)this, (Function2)op);
    }
    
    public <B> Option<B> reduceLeftOption(final Function2<B, InternalRow, B> op) {
        return (Option<B>)TraversableOnce$class.reduceLeftOption((TraversableOnce)this, (Function2)op);
    }
    
    public <B> Option<B> reduceRightOption(final Function2<InternalRow, B, B> op) {
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
    
    public <B> B aggregate(final Function0<B> z, final Function2<B, InternalRow, B> seqop, final Function2<B, B, B> combop) {
        return (B)TraversableOnce$class.aggregate((TraversableOnce)this, (Function0)z, (Function2)seqop, (Function2)combop);
    }
    
    public <B> B sum(final Numeric<B> num) {
        return (B)TraversableOnce$class.sum((TraversableOnce)this, (Numeric)num);
    }
    
    public <B> B product(final Numeric<B> num) {
        return (B)TraversableOnce$class.product((TraversableOnce)this, (Numeric)num);
    }
    
    public Object min(final Ordering cmp) {
        return TraversableOnce$class.min((TraversableOnce)this, cmp);
    }
    
    public Object max(final Ordering cmp) {
        return TraversableOnce$class.max((TraversableOnce)this, cmp);
    }
    
    public Object maxBy(final Function1 f, final Ordering cmp) {
        return TraversableOnce$class.maxBy((TraversableOnce)this, f, cmp);
    }
    
    public Object minBy(final Function1 f, final Ordering cmp) {
        return TraversableOnce$class.minBy((TraversableOnce)this, f, cmp);
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
    
    public List<InternalRow> toList() {
        return (List<InternalRow>)TraversableOnce$class.toList((TraversableOnce)this);
    }
    
    public Iterable<InternalRow> toIterable() {
        return (Iterable<InternalRow>)TraversableOnce$class.toIterable((TraversableOnce)this);
    }
    
    public Seq<InternalRow> toSeq() {
        return (Seq<InternalRow>)TraversableOnce$class.toSeq((TraversableOnce)this);
    }
    
    public IndexedSeq<InternalRow> toIndexedSeq() {
        return (IndexedSeq<InternalRow>)TraversableOnce$class.toIndexedSeq((TraversableOnce)this);
    }
    
    public <B> Buffer<B> toBuffer() {
        return (Buffer<B>)TraversableOnce$class.toBuffer((TraversableOnce)this);
    }
    
    public <B> Set<B> toSet() {
        return (Set<B>)TraversableOnce$class.toSet((TraversableOnce)this);
    }
    
    public Vector<InternalRow> toVector() {
        return (Vector<InternalRow>)TraversableOnce$class.toVector((TraversableOnce)this);
    }
    
    public <Col> Col to(final CanBuildFrom<Nothing$, InternalRow, Col> cbf) {
        return (Col)TraversableOnce$class.to((TraversableOnce)this, (CanBuildFrom)cbf);
    }
    
    public <T, U> Map<T, U> toMap(final Predef.$less$colon$less<InternalRow, Tuple2<T, U>> ev) {
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
    
    private Connection conn() {
        return this.conn;
    }
    
    public String io$pivotal$greenplum$spark$externaltable$GreenplumRowIterator$$filterWhereClause() {
        return this.io$pivotal$greenplum$spark$externaltable$GreenplumRowIterator$$filterWhereClause;
    }
    
    private String wherePredicate() {
        return this.wherePredicate;
    }
    
    public DataIterator dataIterator() {
        return this.dataIterator;
    }
    
    public int processedCount() {
        return this.processedCount;
    }
    
    public void processedCount_$eq(final int x$1) {
        this.processedCount = x$1;
    }
    
    public Function2<String, SpecificInternalRow, BoxedUnit>[] converters() {
        return this.converters;
    }
    
    public boolean hasNext() {
        return this.dataIterator().hasNext();
    }
    
    public InternalRow next() {
        final SpecificInternalRow row = new SpecificInternalRow((Seq)Predef$.MODULE$.wrapRefArray((Object[])Predef$.MODULE$.refArrayOps((Object[])this.io$pivotal$greenplum$spark$externaltable$GreenplumRowIterator$$schema.fields()).map((Function1)new GreenplumRowIterator$$anonfun.GreenplumRowIterator$$anonfun$2(this), Array$.MODULE$.canBuildFrom(ClassTag$.MODULE$.apply((Class)DataType.class)))));
        try {
            final String[] record = this.dataIterator().next();
            Predef$.MODULE$.refArrayOps((Object[])Predef$.MODULE$.refArrayOps((Object[])record).zipWithIndex(Array$.MODULE$.canBuildFrom(ClassTag$.MODULE$.apply((Class)Tuple2.class)))).foreach((Function1)new GreenplumRowIterator$$anonfun$next.GreenplumRowIterator$$anonfun$next$1(this, row));
            this.processedCount_$eq(this.processedCount() + 1);
            if (this.processedCount() % 100000 == 0 && this.log().isDebugEnabled()) {
                this.log().debug(new StringContext((Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[] { "partition.index = ", " processedCount = ", "" })).s((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { BoxesRunTime.boxToInteger(this.partition.index()), BoxesRunTime.boxToInteger(this.processedCount()) })));
            }
            return (InternalRow)row;
        }
        catch (Exception ex) {
            this.dataIterator().closeIfNeeded();
            throw ex;
        }
    }
    
    private final void liftedTree1$1(final GreenplumQualifiedName internalTable$1, final GreenplumQualifiedName externalTable$1, final GpfdistService service$1, final String txId$1) {
        try {
            Jdbc$.MODULE$.copyTable(this.conn(), internalTable$1, externalTable$1, this.wherePredicate(), this.allColumns);
            this.conn().commit();
        }
        catch (PSQLException ex) {
            final Try<InputStream> receivedData = service$1.getReceivedDataFor(txId$1);
            if (receivedData instanceof Failure) {
                final Throwable error = ((Failure)receivedData).exception();
                throw error;
            }
            throw ex;
        }
    }
    
    public GreenplumRowIterator(final String applicationId, final GreenplumPartition partition, final StructType schema, final GreenplumOptions greenplumOptions, final Seq<String> allColumns, final Filter[] filters) {
        this.partition = partition;
        this.io$pivotal$greenplum$spark$externaltable$GreenplumRowIterator$$schema = schema;
        this.io$pivotal$greenplum$spark$externaltable$GreenplumRowIterator$$greenplumOptions = greenplumOptions;
        this.allColumns = allColumns;
        TraversableOnce$class.$init$((TraversableOnce)this);
        Iterator$class.$init$((Iterator)this);
        Logging$class.$init$(this);
        this.conn = ConnectionManager$.MODULE$.getConnection(greenplumOptions, false);
        this.io$pivotal$greenplum$spark$externaltable$GreenplumRowIterator$$filterWhereClause = GreenplumRowIterator$.MODULE$.filterWherePredicate(filters);
        String wherePredicate;
        if (this.io$pivotal$greenplum$spark$externaltable$GreenplumRowIterator$$filterWhereClause().length() > 0) {
            this.logDebug((Function0<String>)new GreenplumRowIterator$$anonfun.GreenplumRowIterator$$anonfun$1(this));
            wherePredicate = new StringContext((Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[] { "(", ") AND (", ")" })).s((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { this.io$pivotal$greenplum$spark$externaltable$GreenplumRowIterator$$filterWhereClause(), partition.whereClause() }));
        }
        else {
            wherePredicate = partition.whereClause();
        }
        this.wherePredicate = wherePredicate;
        try {
            final long threadId = Thread.currentThread().getId();
            final String executorId = SparkEnv$.MODULE$.get().executorId();
            final String externalTableName = GreenplumTableManager$.MODULE$.generateExternalTableName(applicationId, greenplumOptions.dbTable(), executorId, threadId, allColumns);
            final GreenplumQualifiedName internalTable = new GreenplumQualifiedName(greenplumOptions.dbSchema(), greenplumOptions.dbTable());
            final GreenplumQualifiedName externalTable = new GreenplumQualifiedName(greenplumOptions.dbSchema(), externalTableName);
            final GpfdistService service = GpfdistServiceManager$.MODULE$.getService(greenplumOptions.connectorOptions());
            if (!Jdbc$.MODULE$.externalTableExists(this.conn(), externalTable)) {
                if (this.log().isDebugEnabled()) {
                    this.log().debug(new StringBuilder().append((Object)new StringContext((Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[] { "External table ", " not found, " })).s((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { externalTable }))).append((Object)new StringContext((Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[] { "creating table w/ port=", ", columns=", "" })).s((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { BoxesRunTime.boxToInteger(service.getPort()), allColumns.mkString(",") }))).toString());
                }
                final String serverAddress = ConnectorUtils$.MODULE$.getServerAddress(greenplumOptions.connectorOptions(), ConnectorUtils$.MODULE$.getServerAddress$default$2());
                final int port = service.getPort();
                Jdbc$.MODULE$.createGpfdistWritableExternalTable(this.conn(), internalTable, externalTable, new GpfdistLocation(serverAddress, port), allColumns);
            }
            final String txId = Jdbc$.MODULE$.getDistributedTransactionId(this.conn());
            this.log().debug(new StringBuilder().append((Object)new StringContext((Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[] { "Distributed transaction id is ", "" })).s((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { txId }))).append((Object)new StringContext((Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[] { " for a partition ", "" })).s((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { partition.whereClause() }))).toString());
            this.liftedTree1$1(internalTable, externalTable, service, txId);
            final Try<InputStream> receivedData = service.getReceivedDataFor(txId);
            if (receivedData instanceof Success) {
                final InputStream data = (InputStream)((Success)receivedData).value();
                if (this.log().isDebugEnabled()) {
                    this.log().debug(new StringContext((Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[] { "Reading table data for ", "" })).s((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { txId })));
                }
                final DataIterator dataIterator = new DataIterator(data);
                this.conn().rollback();
                this.conn().close();
                this.dataIterator = dataIterator;
                this.processedCount = 0;
                this.converters = DataTypeConverterFactory$.MODULE$.create(schema);
                return;
            }
            if (receivedData instanceof Failure) {
                final Throwable error = ((Failure)receivedData).exception();
                this.log().error("Table copy succeeded but there was an error in the HTTP handler; this should not happen");
                this.log().error(error.getMessage());
                throw error;
            }
            throw new MatchError((Object)receivedData);
        }
        finally {
            this.conn().rollback();
            this.conn().close();
        }
    }
}
