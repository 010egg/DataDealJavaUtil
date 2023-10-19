// 
// Decompiled by Procyon v0.5.36
// 

package resource;

import scala.runtime.BoxesRunTime;
import scala.collection.GenTraversable$class;
import scala.collection.GenIterable;
import scala.collection.GenSeq;
import scala.collection.GenSet;
import scala.collection.GenMap;
import scala.StringContext;
import scala.collection.mutable.StringBuilder;
import scala.collection.immutable.Vector;
import scala.collection.immutable.Set;
import scala.collection.immutable.IndexedSeq;
import scala.collection.Seq;
import scala.collection.Iterable;
import scala.reflect.ClassTag;
import scala.collection.mutable.Buffer;
import scala.math.Ordering;
import scala.math.Numeric;
import scala.collection.TraversableOnce$class;
import scala.collection.immutable.List;
import scala.collection.Parallelizable;
import scala.collection.Parallelizable$class;
import scala.collection.Parallel;
import scala.collection.generic.FilterMonadic;
import scala.collection.TraversableView;
import scala.runtime.Nothing$;
import scala.collection.immutable.Stream;
import scala.collection.Iterator;
import scala.Function2;
import scala.Option;
import scala.collection.immutable.Map;
import scala.PartialFunction;
import scala.collection.generic.CanBuildFrom;
import scala.collection.GenTraversableOnce;
import scala.collection.parallel.ParIterable;
import scala.collection.parallel.Combiner;
import scala.collection.TraversableLike;
import scala.collection.TraversableLike$class;
import scala.collection.GenTraversable;
import scala.Tuple3;
import scala.Tuple2;
import scala.collection.generic.GenericTraversableTemplate;
import scala.collection.generic.GenericTraversableTemplate$class;
import scala.collection.mutable.Builder;
import scala.collection.Traversable$class;
import scala.collection.generic.GenericCompanion;
import java.util.zip.ZipEntry;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipOutputStream;
import java.util.zip.ZipInputStream;
import java.util.zip.GZIPInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.zip.ZipFile;
import java.util.jar.JarFile;
import scala.collection.TraversableOnce;
import scala.Predef;
import scala.Predef$;
import scala.collection.Traversable;
import java.io.BufferedWriter;
import java.nio.channels.FileChannel;
import java.net.URL;
import java.io.File;
import java.io.BufferedInputStream;
import java.io.InputStream;
import scala.Function1;
import scala.reflect.OptManifest;
import scala.reflect.ClassManifestFactory$;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import scala.Function0;
import scala.runtime.BoxedUnit;
import scala.runtime.VolatileObjectRef;
import java.io.BufferedReader;
import java.nio.charset.Charset;

public final class Using$
{
    public static final Using$ MODULE$;
    private final Charset utf8;
    
    static {
        new Using$();
    }
    
    private Using$traverser$2.traverser$2$ traverser$1$lzycompute(final BufferedReader x$1, final VolatileObjectRef x$2) {
        synchronized (this) {
            if (x$2.elem == null) {
                x$2.elem = new Using$traverser$2.traverser$2$(x$1);
            }
            final BoxedUnit unit = BoxedUnit.UNIT;
            return (Using$traverser$2.traverser$2$)x$2.elem;
        }
    }
    
    public ManagedResource<BufferedOutputStream> bufferedOutputStream(final Function0<OutputStream> out) {
        return (ManagedResource<BufferedOutputStream>)package$.MODULE$.managed((scala.Function0<Object>)out, Resource$.MODULE$.closeableResource(), (scala.reflect.OptManifest<Object>)ClassManifestFactory$.MODULE$.classType((Class)OutputStream.class)).map((scala.Function1<Object, Object>)new Using$$anonfun$bufferedOutputStream.Using$$anonfun$bufferedOutputStream$1());
    }
    
    public ManagedResource<BufferedInputStream> bufferedInputStream(final Function0<InputStream> in) {
        return (ManagedResource<BufferedInputStream>)package$.MODULE$.managed((scala.Function0<Object>)in, Resource$.MODULE$.closeableResource(), (scala.reflect.OptManifest<Object>)ClassManifestFactory$.MODULE$.classType((Class)InputStream.class)).map((scala.Function1<Object, Object>)new Using$$anonfun$bufferedInputStream.Using$$anonfun$bufferedInputStream$1());
    }
    
    public <T> ManagedResource<T> file(final Function1<File, T> in, final File source, final Resource<T> evidence$1, final OptManifest<T> evidence$2) {
        return package$.MODULE$.managed((scala.Function0<T>)new Using$$anonfun$file.Using$$anonfun$file$1((Function1)in, source), evidence$1, evidence$2);
    }
    
    public ManagedResource<BufferedInputStream> fileInputStream(final File source) {
        return this.file((scala.Function1<File, BufferedInputStream>)new Using$$anonfun$fileInputStream.Using$$anonfun$fileInputStream$1(), source, Resource$.MODULE$.closeableResource(), (scala.reflect.OptManifest<BufferedInputStream>)ClassManifestFactory$.MODULE$.classType((Class)BufferedInputStream.class));
    }
    
    public ManagedResource<BufferedOutputStream> fileOutputStream(final File source) {
        return this.file((scala.Function1<File, BufferedOutputStream>)new Using$$anonfun$fileOutputStream.Using$$anonfun$fileOutputStream$1(), source, Resource$.MODULE$.closeableResource(), (scala.reflect.OptManifest<BufferedOutputStream>)ClassManifestFactory$.MODULE$.classType((Class)BufferedOutputStream.class));
    }
    
    public ManagedResource<BufferedInputStream> urlInputStream(final URL url) {
        return this.bufferedInputStream((Function0<InputStream>)new Using$$anonfun$urlInputStream.Using$$anonfun$urlInputStream$1(url));
    }
    
    public ManagedResource<FileChannel> fileOuputChannel(final File source) {
        return this.file((scala.Function1<File, FileChannel>)new Using$$anonfun$fileOuputChannel.Using$$anonfun$fileOuputChannel$1(), source, Resource$.MODULE$.closeableResource(), (scala.reflect.OptManifest<FileChannel>)ClassManifestFactory$.MODULE$.classType((Class)FileChannel.class));
    }
    
    public ManagedResource<FileChannel> fileInputChannel(final File source) {
        return this.file((scala.Function1<File, FileChannel>)new Using$$anonfun$fileInputChannel.Using$$anonfun$fileInputChannel$1(), source, Resource$.MODULE$.closeableResource(), (scala.reflect.OptManifest<FileChannel>)ClassManifestFactory$.MODULE$.classType((Class)FileChannel.class));
    }
    
    private Charset utf8() {
        return this.utf8;
    }
    
    public ManagedResource<BufferedWriter> fileWriter(final Charset charset, final boolean append, final File source) {
        return this.file((scala.Function1<File, BufferedWriter>)new Using$$anonfun$fileWriter.Using$$anonfun$fileWriter$1(charset, append), source, Resource$.MODULE$.closeableResource(), (scala.reflect.OptManifest<BufferedWriter>)ClassManifestFactory$.MODULE$.classType((Class)BufferedWriter.class));
    }
    
    public Charset fileWriter$default$1() {
        return this.utf8();
    }
    
    public boolean fileWriter$default$2() {
        return false;
    }
    
    public ManagedResource<BufferedReader> fileReader(final Charset charset, final File source) {
        return this.file((scala.Function1<File, BufferedReader>)new Using$$anonfun$fileReader.Using$$anonfun$fileReader$1(charset), source, Resource$.MODULE$.closeableResource(), (scala.reflect.OptManifest<BufferedReader>)ClassManifestFactory$.MODULE$.classType((Class)BufferedReader.class));
    }
    
    public Traversable<String> fileLines(final Charset charset, final File source) {
        return this.fileReader(charset, source).map((scala.Function1<BufferedReader, Object>)new Using$$anonfun$fileLines.Using$$anonfun$fileLines$1()).toTraversable((Predef.$less$colon$less<Object, scala.collection.TraversableOnce<String>>)Predef$.MODULE$.$conforms());
    }
    
    public ManagedResource<BufferedReader> urlReader(final Charset charset, final URL u) {
        return package$.MODULE$.managed((scala.Function0<BufferedReader>)new Using$$anonfun$urlReader.Using$$anonfun$urlReader$1(charset, u), Resource$.MODULE$.closeableResource(), (scala.reflect.OptManifest<BufferedReader>)ClassManifestFactory$.MODULE$.classType((Class)BufferedReader.class));
    }
    
    public ManagedResource<JarFile> jarFile(final boolean verify, final File source) {
        return this.file((scala.Function1<File, JarFile>)new Using$$anonfun$jarFile.Using$$anonfun$jarFile$1(verify), source, Resource$.MODULE$.jarFileResource(), (scala.reflect.OptManifest<JarFile>)ClassManifestFactory$.MODULE$.classType((Class)JarFile.class));
    }
    
    public ManagedResource<ZipFile> zipFile(final File source) {
        return this.file((scala.Function1<File, ZipFile>)new Using$$anonfun$zipFile.Using$$anonfun$zipFile$1(), source, Resource$.MODULE$.closeableResource(), (scala.reflect.OptManifest<ZipFile>)ClassManifestFactory$.MODULE$.classType((Class)ZipFile.class));
    }
    
    public ManagedResource<Reader> streamReader(final InputStream in, final Charset charset) {
        return package$.MODULE$.managed((scala.Function0<Reader>)new Using$$anonfun$streamReader.Using$$anonfun$streamReader$1(in, charset), Resource$.MODULE$.closeableResource(), (scala.reflect.OptManifest<Reader>)ClassManifestFactory$.MODULE$.classType((Class)InputStreamReader.class));
    }
    
    public ManagedResource<GZIPInputStream> gzipInputStream(final Function0<InputStream> in) {
        return package$.MODULE$.managed((scala.Function0<GZIPInputStream>)new Using$$anonfun$gzipInputStream.Using$$anonfun$gzipInputStream$1((Function0)in), Resource$.MODULE$.closeableResource(), (scala.reflect.OptManifest<GZIPInputStream>)ClassManifestFactory$.MODULE$.classType((Class)GZIPInputStream.class));
    }
    
    public ManagedResource<ZipInputStream> zipInputStream(final Function0<InputStream> in) {
        return package$.MODULE$.managed((scala.Function0<ZipInputStream>)new Using$$anonfun$zipInputStream.Using$$anonfun$zipInputStream$1((Function0)in), Resource$.MODULE$.closeableResource(), (scala.reflect.OptManifest<ZipInputStream>)ClassManifestFactory$.MODULE$.classType((Class)ZipInputStream.class));
    }
    
    public ManagedResource<ZipOutputStream> zipOutputStream(final Function0<OutputStream> out) {
        return package$.MODULE$.managed((scala.Function0<ZipOutputStream>)new Using$$anonfun$zipOutputStream.Using$$anonfun$zipOutputStream$1((Function0)out), Resource$.MODULE$.closeableResource(), (scala.reflect.OptManifest<ZipOutputStream>)ClassManifestFactory$.MODULE$.classType((Class)ZipOutputStream.class));
    }
    
    public ManagedResource<GZIPOutputStream> gzipOutputStream(final Function0<OutputStream> out) {
        return package$.MODULE$.managed((scala.Function0<GZIPOutputStream>)new Using$$anonfun$gzipOutputStream.Using$$anonfun$gzipOutputStream$1((Function0)out), Resource$.MODULE$.gzipOuputStraemResource(), (scala.reflect.OptManifest<GZIPOutputStream>)ClassManifestFactory$.MODULE$.classType((Class)GZIPOutputStream.class));
    }
    
    public ManagedResource<JarOutputStream> jarOutputStream(final Function0<OutputStream> out) {
        return package$.MODULE$.managed((scala.Function0<JarOutputStream>)new Using$$anonfun$jarOutputStream.Using$$anonfun$jarOutputStream$1((Function0)out), Resource$.MODULE$.closeableResource(), (scala.reflect.OptManifest<JarOutputStream>)ClassManifestFactory$.MODULE$.classType((Class)JarOutputStream.class));
    }
    
    public ManagedResource<JarInputStream> jarInputStream(final Function0<InputStream> in) {
        return package$.MODULE$.managed((scala.Function0<JarInputStream>)new Using$$anonfun$jarInputStream.Using$$anonfun$jarInputStream$1((Function0)in), Resource$.MODULE$.closeableResource(), (scala.reflect.OptManifest<JarInputStream>)ClassManifestFactory$.MODULE$.classType((Class)JarInputStream.class));
    }
    
    public ManagedResource<InputStream> zipEntry(final ZipFile zip, final ZipEntry entry) {
        return package$.MODULE$.managed((scala.Function0<InputStream>)new Using$$anonfun$zipEntry.Using$$anonfun$zipEntry$1(zip, entry), Resource$.MODULE$.closeableResource(), (scala.reflect.OptManifest<InputStream>)ClassManifestFactory$.MODULE$.classType((Class)InputStream.class));
    }
    
    public TraversableOnce<String> resource$Using$$makeBufferedReaderLineTraverser(final BufferedReader reader) {
        public class traverser$2$ implements Traversable<String>
        {
            private final BufferedReader reader$1;
            
            public GenericCompanion<Traversable> companion() {
                return (GenericCompanion<Traversable>)Traversable$class.companion((Traversable)this);
            }
            
            public Traversable<String> seq() {
                return (Traversable<String>)Traversable$class.seq((Traversable)this);
            }
            
            public Builder<String, Traversable<String>> newBuilder() {
                return (Builder<String, Traversable<String>>)GenericTraversableTemplate$class.newBuilder((GenericTraversableTemplate)this);
            }
            
            public <B> Builder<B, Traversable<B>> genericBuilder() {
                return (Builder<B, Traversable<B>>)GenericTraversableTemplate$class.genericBuilder((GenericTraversableTemplate)this);
            }
            
            public <A1, A2> Tuple2<Traversable<A1>, Traversable<A2>> unzip(final Function1<String, Tuple2<A1, A2>> asPair) {
                return (Tuple2<Traversable<A1>, Traversable<A2>>)GenericTraversableTemplate$class.unzip((GenericTraversableTemplate)this, (Function1)asPair);
            }
            
            public <A1, A2, A3> Tuple3<Traversable<A1>, Traversable<A2>, Traversable<A3>> unzip3(final Function1<String, Tuple3<A1, A2, A3>> asTriple) {
                return (Tuple3<Traversable<A1>, Traversable<A2>, Traversable<A3>>)GenericTraversableTemplate$class.unzip3((GenericTraversableTemplate)this, (Function1)asTriple);
            }
            
            public GenTraversable flatten(final Function1 asTraversable) {
                return GenericTraversableTemplate$class.flatten((GenericTraversableTemplate)this, asTraversable);
            }
            
            public GenTraversable transpose(final Function1 asTraversable) {
                return GenericTraversableTemplate$class.transpose((GenericTraversableTemplate)this, asTraversable);
            }
            
            public Object repr() {
                return TraversableLike$class.repr((TraversableLike)this);
            }
            
            public final boolean isTraversableAgain() {
                return TraversableLike$class.isTraversableAgain((TraversableLike)this);
            }
            
            public Traversable<String> thisCollection() {
                return (Traversable<String>)TraversableLike$class.thisCollection((TraversableLike)this);
            }
            
            public Traversable toCollection(final Object repr) {
                return TraversableLike$class.toCollection((TraversableLike)this, repr);
            }
            
            public Combiner<String, ParIterable<String>> parCombiner() {
                return (Combiner<String, ParIterable<String>>)TraversableLike$class.parCombiner((TraversableLike)this);
            }
            
            public boolean isEmpty() {
                return TraversableLike$class.isEmpty((TraversableLike)this);
            }
            
            public boolean hasDefiniteSize() {
                return TraversableLike$class.hasDefiniteSize((TraversableLike)this);
            }
            
            public <B, That> That $plus$plus(final GenTraversableOnce<B> that, final CanBuildFrom<Traversable<String>, B, That> bf) {
                return (That)TraversableLike$class.$plus$plus((TraversableLike)this, (GenTraversableOnce)that, (CanBuildFrom)bf);
            }
            
            public <B, That> That $plus$plus$colon(final TraversableOnce<B> that, final CanBuildFrom<Traversable<String>, B, That> bf) {
                return (That)TraversableLike$class.$plus$plus$colon((TraversableLike)this, (TraversableOnce)that, (CanBuildFrom)bf);
            }
            
            public <B, That> That $plus$plus$colon(final Traversable<B> that, final CanBuildFrom<Traversable<String>, B, That> bf) {
                return (That)TraversableLike$class.$plus$plus$colon((TraversableLike)this, (Traversable)that, (CanBuildFrom)bf);
            }
            
            public <B, That> That map(final Function1<String, B> f, final CanBuildFrom<Traversable<String>, B, That> bf) {
                return (That)TraversableLike$class.map((TraversableLike)this, (Function1)f, (CanBuildFrom)bf);
            }
            
            public <B, That> That flatMap(final Function1<String, GenTraversableOnce<B>> f, final CanBuildFrom<Traversable<String>, B, That> bf) {
                return (That)TraversableLike$class.flatMap((TraversableLike)this, (Function1)f, (CanBuildFrom)bf);
            }
            
            public Object filter(final Function1 p) {
                return TraversableLike$class.filter((TraversableLike)this, p);
            }
            
            public Object filterNot(final Function1 p) {
                return TraversableLike$class.filterNot((TraversableLike)this, p);
            }
            
            public <B, That> That collect(final PartialFunction<String, B> pf, final CanBuildFrom<Traversable<String>, B, That> bf) {
                return (That)TraversableLike$class.collect((TraversableLike)this, (PartialFunction)pf, (CanBuildFrom)bf);
            }
            
            public Tuple2<Traversable<String>, Traversable<String>> partition(final Function1<String, Object> p) {
                return (Tuple2<Traversable<String>, Traversable<String>>)TraversableLike$class.partition((TraversableLike)this, (Function1)p);
            }
            
            public <K> Map<K, Traversable<String>> groupBy(final Function1<String, K> f) {
                return (Map<K, Traversable<String>>)TraversableLike$class.groupBy((TraversableLike)this, (Function1)f);
            }
            
            public boolean forall(final Function1<String, Object> p) {
                return TraversableLike$class.forall((TraversableLike)this, (Function1)p);
            }
            
            public boolean exists(final Function1<String, Object> p) {
                return TraversableLike$class.exists((TraversableLike)this, (Function1)p);
            }
            
            public Option<String> find(final Function1<String, Object> p) {
                return (Option<String>)TraversableLike$class.find((TraversableLike)this, (Function1)p);
            }
            
            public <B, That> That scan(final B z, final Function2<B, B, B> op, final CanBuildFrom<Traversable<String>, B, That> cbf) {
                return (That)TraversableLike$class.scan((TraversableLike)this, (Object)z, (Function2)op, (CanBuildFrom)cbf);
            }
            
            public <B, That> That scanLeft(final B z, final Function2<B, String, B> op, final CanBuildFrom<Traversable<String>, B, That> bf) {
                return (That)TraversableLike$class.scanLeft((TraversableLike)this, (Object)z, (Function2)op, (CanBuildFrom)bf);
            }
            
            public <B, That> That scanRight(final B z, final Function2<String, B, B> op, final CanBuildFrom<Traversable<String>, B, That> bf) {
                return (That)TraversableLike$class.scanRight((TraversableLike)this, (Object)z, (Function2)op, (CanBuildFrom)bf);
            }
            
            public Object head() {
                return TraversableLike$class.head((TraversableLike)this);
            }
            
            public Option<String> headOption() {
                return (Option<String>)TraversableLike$class.headOption((TraversableLike)this);
            }
            
            public Object tail() {
                return TraversableLike$class.tail((TraversableLike)this);
            }
            
            public Object last() {
                return TraversableLike$class.last((TraversableLike)this);
            }
            
            public Option<String> lastOption() {
                return (Option<String>)TraversableLike$class.lastOption((TraversableLike)this);
            }
            
            public Object init() {
                return TraversableLike$class.init((TraversableLike)this);
            }
            
            public Object take(final int n) {
                return TraversableLike$class.take((TraversableLike)this, n);
            }
            
            public Object drop(final int n) {
                return TraversableLike$class.drop((TraversableLike)this, n);
            }
            
            public Object slice(final int from, final int until) {
                return TraversableLike$class.slice((TraversableLike)this, from, until);
            }
            
            public Object sliceWithKnownDelta(final int from, final int until, final int delta) {
                return TraversableLike$class.sliceWithKnownDelta((TraversableLike)this, from, until, delta);
            }
            
            public Object sliceWithKnownBound(final int from, final int until) {
                return TraversableLike$class.sliceWithKnownBound((TraversableLike)this, from, until);
            }
            
            public Object takeWhile(final Function1 p) {
                return TraversableLike$class.takeWhile((TraversableLike)this, p);
            }
            
            public Object dropWhile(final Function1 p) {
                return TraversableLike$class.dropWhile((TraversableLike)this, p);
            }
            
            public Tuple2<Traversable<String>, Traversable<String>> span(final Function1<String, Object> p) {
                return (Tuple2<Traversable<String>, Traversable<String>>)TraversableLike$class.span((TraversableLike)this, (Function1)p);
            }
            
            public Tuple2<Traversable<String>, Traversable<String>> splitAt(final int n) {
                return (Tuple2<Traversable<String>, Traversable<String>>)TraversableLike$class.splitAt((TraversableLike)this, n);
            }
            
            public Iterator<Traversable<String>> tails() {
                return (Iterator<Traversable<String>>)TraversableLike$class.tails((TraversableLike)this);
            }
            
            public Iterator<Traversable<String>> inits() {
                return (Iterator<Traversable<String>>)TraversableLike$class.inits((TraversableLike)this);
            }
            
            public <B> void copyToArray(final Object xs, final int start, final int len) {
                TraversableLike$class.copyToArray((TraversableLike)this, xs, start, len);
            }
            
            public Traversable<String> toTraversable() {
                return (Traversable<String>)TraversableLike$class.toTraversable((TraversableLike)this);
            }
            
            public Iterator<String> toIterator() {
                return (Iterator<String>)TraversableLike$class.toIterator((TraversableLike)this);
            }
            
            public Stream<String> toStream() {
                return (Stream<String>)TraversableLike$class.toStream((TraversableLike)this);
            }
            
            public <Col> Col to(final CanBuildFrom<Nothing$, String, Col> cbf) {
                return (Col)TraversableLike$class.to((TraversableLike)this, (CanBuildFrom)cbf);
            }
            
            public String stringPrefix() {
                return TraversableLike$class.stringPrefix((TraversableLike)this);
            }
            
            public Object view() {
                return TraversableLike$class.view((TraversableLike)this);
            }
            
            public TraversableView<String, Traversable<String>> view(final int from, final int until) {
                return (TraversableView<String, Traversable<String>>)TraversableLike$class.view((TraversableLike)this, from, until);
            }
            
            public FilterMonadic<String, Traversable<String>> withFilter(final Function1<String, Object> p) {
                return (FilterMonadic<String, Traversable<String>>)TraversableLike$class.withFilter((TraversableLike)this, (Function1)p);
            }
            
            public Parallel par() {
                return Parallelizable$class.par((Parallelizable)this);
            }
            
            public List<String> reversed() {
                return (List<String>)TraversableOnce$class.reversed((TraversableOnce)this);
            }
            
            public int size() {
                return TraversableOnce$class.size((TraversableOnce)this);
            }
            
            public boolean nonEmpty() {
                return TraversableOnce$class.nonEmpty((TraversableOnce)this);
            }
            
            public int count(final Function1<String, Object> p) {
                return TraversableOnce$class.count((TraversableOnce)this, (Function1)p);
            }
            
            public <B> Option<B> collectFirst(final PartialFunction<String, B> pf) {
                return (Option<B>)TraversableOnce$class.collectFirst((TraversableOnce)this, (PartialFunction)pf);
            }
            
            public <B> B $div$colon(final B z, final Function2<B, String, B> op) {
                return (B)TraversableOnce$class.$div$colon((TraversableOnce)this, (Object)z, (Function2)op);
            }
            
            public <B> B $colon$bslash(final B z, final Function2<String, B, B> op) {
                return (B)TraversableOnce$class.$colon$bslash((TraversableOnce)this, (Object)z, (Function2)op);
            }
            
            public <B> B foldLeft(final B z, final Function2<B, String, B> op) {
                return (B)TraversableOnce$class.foldLeft((TraversableOnce)this, (Object)z, (Function2)op);
            }
            
            public <B> B foldRight(final B z, final Function2<String, B, B> op) {
                return (B)TraversableOnce$class.foldRight((TraversableOnce)this, (Object)z, (Function2)op);
            }
            
            public <B> B reduceLeft(final Function2<B, String, B> op) {
                return (B)TraversableOnce$class.reduceLeft((TraversableOnce)this, (Function2)op);
            }
            
            public <B> B reduceRight(final Function2<String, B, B> op) {
                return (B)TraversableOnce$class.reduceRight((TraversableOnce)this, (Function2)op);
            }
            
            public <B> Option<B> reduceLeftOption(final Function2<B, String, B> op) {
                return (Option<B>)TraversableOnce$class.reduceLeftOption((TraversableOnce)this, (Function2)op);
            }
            
            public <B> Option<B> reduceRightOption(final Function2<String, B, B> op) {
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
            
            public <B> B aggregate(final Function0<B> z, final Function2<B, String, B> seqop, final Function2<B, B, B> combop) {
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
            
            public List<String> toList() {
                return (List<String>)TraversableOnce$class.toList((TraversableOnce)this);
            }
            
            public Iterable<String> toIterable() {
                return (Iterable<String>)TraversableOnce$class.toIterable((TraversableOnce)this);
            }
            
            public Seq<String> toSeq() {
                return (Seq<String>)TraversableOnce$class.toSeq((TraversableOnce)this);
            }
            
            public IndexedSeq<String> toIndexedSeq() {
                return (IndexedSeq<String>)TraversableOnce$class.toIndexedSeq((TraversableOnce)this);
            }
            
            public <B> Buffer<B> toBuffer() {
                return (Buffer<B>)TraversableOnce$class.toBuffer((TraversableOnce)this);
            }
            
            public <B> Set<B> toSet() {
                return (Set<B>)TraversableOnce$class.toSet((TraversableOnce)this);
            }
            
            public Vector<String> toVector() {
                return (Vector<String>)TraversableOnce$class.toVector((TraversableOnce)this);
            }
            
            public <T, U> Map<T, U> toMap(final Predef.$less$colon$less<String, Tuple2<T, U>> ev) {
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
            
            public <U> void foreach(final Function1<String, U> f) {
                this.read$1(f);
            }
            
            @Override
            public String toString() {
                return new StringContext((Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[] { "BufferedReaderLineIterator(", ")" })).s((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { this.reader$1 }));
            }
            
            private final void read$1(final Function1 f$1) {
                while (true) {
                    final String line = this.reader$1.readLine();
                    if (line == null) {
                        break;
                    }
                    f$1.apply((Object)line);
                }
                final BoxedUnit unit = BoxedUnit.UNIT;
                final BoxedUnit unit2 = BoxedUnit.UNIT;
            }
            
            public traverser$2$(final BufferedReader reader$1) {
                this.reader$1 = reader$1;
                TraversableOnce$class.$init$((TraversableOnce)this);
                Parallelizable$class.$init$((Parallelizable)this);
                TraversableLike$class.$init$((TraversableLike)this);
                GenericTraversableTemplate$class.$init$((GenericTraversableTemplate)this);
                GenTraversable$class.$init$((GenTraversable)this);
                Traversable$class.$init$((Traversable)this);
            }
        }
        final VolatileObjectRef traverser$module = VolatileObjectRef.zero();
        return (TraversableOnce<String>)this.traverser$1(reader, traverser$module);
    }
    
    public final Object resource$Using$$open$1(final Function1 in$1, final File source$1) {
        final File parent = source$1.getParentFile();
        if (parent == null) {
            final BoxedUnit unit = BoxedUnit.UNIT;
        }
        else {
            BoxesRunTime.boxToBoolean(parent.mkdirs());
        }
        return in$1.apply((Object)source$1);
    }
    
    private final Using$traverser$2.traverser$2$ traverser$1(final BufferedReader reader$1, final VolatileObjectRef traverser$module$1) {
        return (Using$traverser$2.traverser$2$)((traverser$module$1.elem == null) ? this.traverser$1$lzycompute(reader$1, traverser$module$1) : traverser$module$1.elem);
    }
    
    private Using$() {
        MODULE$ = this;
        this.utf8 = Charset.forName("UTF-8");
    }
}
