// 
// Decompiled by Procyon v0.5.36
// 

package resource;

import scala.reflect.OptManifest;
import scala.Function1;
import java.io.BufferedOutputStream;
import java.io.BufferedInputStream;
import java.nio.channels.FileChannel;
import java.io.BufferedWriter;
import scala.collection.Traversable;
import java.io.BufferedReader;
import java.net.URL;
import java.util.jar.JarFile;
import java.io.File;
import java.io.Reader;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import java.util.zip.GZIPOutputStream;
import java.util.jar.JarOutputStream;
import java.io.OutputStream;
import java.util.jar.JarInputStream;
import scala.Function0;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.nio.charset.Charset;
import scala.reflect.ScalaSignature;

@ScalaSignature(bytes = "\u0006\u0001\t]t!B\u0001\u0003\u0011\u0003)\u0011!B+tS:<'\"A\u0002\u0002\u0011I,7o\\;sG\u0016\u001c\u0001\u0001\u0005\u0002\u0007\u000f5\t!AB\u0003\t\u0005!\u0005\u0011BA\u0003Vg&twm\u0005\u0002\b\u0015A\u00111BD\u0007\u0002\u0019)\tQ\"A\u0003tG\u0006d\u0017-\u0003\u0002\u0010\u0019\t1\u0011I\\=SK\u001aDQ!E\u0004\u0005\u0002I\ta\u0001P5oSRtD#A\u0003\t\u000bQ9A\u0011A\u000b\u0002)\t,hMZ3sK\u0012|U\u000f\u001e9viN#(/Z1n)\t1\u0012\u0005E\u0002\u0007/eI!\u0001\u0007\u0002\u0003\u001f5\u000bg.Y4fIJ+7o\\;sG\u0016\u0004\"AG\u0010\u000e\u0003mQ!\u0001H\u000f\u0002\u0005%|'\"\u0001\u0010\u0002\t)\fg/Y\u0005\u0003Am\u0011ACQ;gM\u0016\u0014X\rZ(viB,Ho\u0015;sK\u0006l\u0007B\u0002\u0012\u0014\t\u0003\u00071%A\u0002pkR\u00042a\u0003\u0013'\u0013\t)CB\u0001\u0005=Eft\u0017-\\3?!\tQr%\u0003\u0002)7\taq*\u001e;qkR\u001cFO]3b[\")!f\u0002C\u0001W\u0005\u0019\"-\u001e4gKJ,G-\u00138qkR\u001cFO]3b[R\u0011A\u0006\r\t\u0004\r]i\u0003C\u0001\u000e/\u0013\ty3DA\nCk\u001a4WM]3e\u0013:\u0004X\u000f^*ue\u0016\fW\u000e\u0003\u00042S\u0011\u0005\rAM\u0001\u0003S:\u00042a\u0003\u00134!\tQB'\u0003\u000267\tY\u0011J\u001c9viN#(/Z1n\u0011\u00159t\u0001\"\u00019\u0003\u00111\u0017\u000e\\3\u0016\u0005ezDC\u0001\u001e\\)\tYd\u000bF\u0002=\u00116\u00032AB\f>!\tqt\b\u0004\u0001\u0005\u000b\u00013$\u0019A!\u0003\u0003Q\u000b\"AQ#\u0011\u0005-\u0019\u0015B\u0001#\r\u0005\u001dqu\u000e\u001e5j]\u001e\u0004\"a\u0003$\n\u0005\u001dc!aA!os\"9\u0011JNA\u0001\u0002\bQ\u0015AC3wS\u0012,gnY3%cA\u0019aaS\u001f\n\u00051\u0013!\u0001\u0003*fg>,(oY3\t\u000f93\u0014\u0011!a\u0002\u001f\u0006QQM^5eK:\u001cW\r\n\u001a\u0011\u0007A\u001bVH\u0004\u0002\f#&\u0011!\u000bD\u0001\u0007!J,G-\u001a4\n\u0005Q+&aC(qi6\u000bg.\u001b4fgRT!A\u0015\u0007\t\u000b]3\u0004\u0019\u0001-\u0002\rM|WO]2f!\tQ\u0012,\u0003\u0002[7\t!a)\u001b7f\u0011\u0015\td\u00071\u0001]!\u0011YQ\fW\u001f\n\u0005yc!!\u0003$v]\u000e$\u0018n\u001c82\u0011\u0015\u0001w\u0001\"\u0001b\u0003=1\u0017\u000e\\3J]B,Ho\u0015;sK\u0006lGC\u0001\u0017c\u0011\u00159v\f1\u0001Y\u0011\u0015!w\u0001\"\u0001f\u0003A1\u0017\u000e\\3PkR\u0004X\u000f^*ue\u0016\fW\u000e\u0006\u0002\u0017M\")qk\u0019a\u00011\")\u0001n\u0002C\u0001S\u0006qQO\u001d7J]B,Ho\u0015;sK\u0006lGC\u0001\u0017k\u0011\u0015Yw\r1\u0001m\u0003\r)(\u000f\u001c\t\u0003[Bl\u0011A\u001c\u0006\u0003_v\t1A\\3u\u0013\t\thNA\u0002V%2CQa]\u0004\u0005\u0002Q\f\u0001CZ5mK>+\b/\u001e;DQ\u0006tg.\u001a7\u0015\u0005Ut\bc\u0001\u0004\u0018mB\u0011q\u000f`\u0007\u0002q*\u0011\u0011P_\u0001\tG\"\fgN\\3mg*\u001110H\u0001\u0004]&|\u0017BA?y\u0005-1\u0015\u000e\\3DQ\u0006tg.\u001a7\t\u000b]\u0013\b\u0019\u0001-\t\u000f\u0005\u0005q\u0001\"\u0001\u0002\u0004\u0005\u0001b-\u001b7f\u0013:\u0004X\u000f^\"iC:tW\r\u001c\u000b\u0004k\u0006\u0015\u0001\"B,\u0000\u0001\u0004A\u0006\"CA\u0005\u000f\t\u0007I\u0011BA\u0006\u0003\u0011)HO\u001a\u001d\u0016\u0005\u00055\u0001\u0003BA\b\u0003+i!!!\u0005\u000b\u0007\u0005M!0A\u0004dQ\u0006\u00148/\u001a;\n\t\u0005]\u0011\u0011\u0003\u0002\b\u0007\"\f'o]3u\u0011!\tYb\u0002Q\u0001\n\u00055\u0011!B;uMb\u0002\u0003bBA\u0010\u000f\u0011\u0005\u0011\u0011E\u0001\u000bM&dWm\u0016:ji\u0016\u0014HCBA\u0012\u0003_\t\t\u0004\u0006\u0003\u0002&\u00055\u0002\u0003\u0002\u0004\u0018\u0003O\u00012AGA\u0015\u0013\r\tYc\u0007\u0002\u000f\u0005V4g-\u001a:fI^\u0013\u0018\u000e^3s\u0011\u00199\u0016Q\u0004a\u00011\"Q\u00111CA\u000f!\u0003\u0005\r!!\u0004\t\u0015\u0005M\u0012Q\u0004I\u0001\u0002\u0004\t)$\u0001\u0004baB,g\u000e\u001a\t\u0004\u0017\u0005]\u0012bAA\u001d\u0019\t9!i\\8mK\u0006t\u0007bBA\u001f\u000f\u0011\u0005\u0011qH\u0001\u000bM&dWMU3bI\u0016\u0014H\u0003BA!\u0003\u001b\"B!a\u0011\u0002LA!aaFA#!\rQ\u0012qI\u0005\u0004\u0003\u0013Z\"A\u0004\"vM\u001a,'/\u001a3SK\u0006$WM\u001d\u0005\u0007/\u0006m\u0002\u0019\u0001-\t\u0011\u0005M\u00111\ba\u0001\u0003\u001bAq!!\u0015\b\t\u0003\t\u0019&A\u0005gS2,G*\u001b8fgR!\u0011QKA<)\u0011\t9&!\u001e\u0011\r\u0005e\u0013\u0011NA8\u001d\u0011\tY&!\u001a\u000f\t\u0005u\u00131M\u0007\u0003\u0003?R1!!\u0019\u0005\u0003\u0019a$o\\8u}%\tQ\"C\u0002\u0002h1\tq\u0001]1dW\u0006<W-\u0003\u0003\u0002l\u00055$a\u0003+sCZ,'o]1cY\u0016T1!a\u001a\r!\r\u0001\u0016\u0011O\u0005\u0004\u0003g*&AB*ue&tw\r\u0003\u0004X\u0003\u001f\u0002\r\u0001\u0017\u0005\t\u0003'\ty\u00051\u0001\u0002\u000e!9\u00111P\u0004\u0005\u0002\u0005u\u0014!C;sYJ+\u0017\rZ3s)\u0011\ty(!\"\u0015\t\u0005\r\u0013\u0011\u0011\u0005\b\u0003\u0007\u000bI\b1\u0001m\u0003\u0005)\b\u0002CA\n\u0003s\u0002\r!!\u0004\t\u000f\u0005%u\u0001\"\u0001\u0002\f\u00069!.\u0019:GS2,G\u0003BAG\u0003G#B!a$\u0002\"B!aaFAI!\u0011\t\u0019*!(\u000e\u0005\u0005U%\u0002BAL\u00033\u000b1A[1s\u0015\r\tY*H\u0001\u0005kRLG.\u0003\u0003\u0002 \u0006U%a\u0002&be\u001aKG.\u001a\u0005\u0007/\u0006\u001d\u0005\u0019\u0001-\t\u0011\u0005\u0015\u0016q\u0011a\u0001\u0003k\taA^3sS\u001aL\bbBAU\u000f\u0011\u0005\u00111V\u0001\bu&\u0004h)\u001b7f)\u0011\ti+a/\u0011\t\u00199\u0012q\u0016\t\u0005\u0003c\u000b9,\u0004\u0002\u00024*!\u0011QWAM\u0003\rQ\u0018\u000e]\u0005\u0005\u0003s\u000b\u0019LA\u0004[SB4\u0015\u000e\\3\t\r]\u000b9\u000b1\u0001Y\u0011\u001d\tyl\u0002C\u0001\u0003\u0003\fAb\u001d;sK\u0006l'+Z1eKJ$b!a1\u0002L\u00065\u0007\u0003\u0002\u0004\u0018\u0003\u000b\u00042AGAd\u0013\r\tIm\u0007\u0002\u0007%\u0016\fG-\u001a:\t\rE\ni\f1\u00014\u0011!\t\u0019\"!0A\u0002\u00055\u0001bBAi\u000f\u0011\u0005\u00111[\u0001\u0010OjL\u0007/\u00138qkR\u001cFO]3b[R!\u0011Q[Ao!\u00111q#a6\u0011\t\u0005E\u0016\u0011\\\u0005\u0005\u00037\f\u0019LA\bH5&\u0003\u0016J\u001c9viN#(/Z1n\u0011\u001d\t\u0014q\u001aCA\u0002IBq!!9\b\t\u0003\t\u0019/\u0001\b{SBLe\u000e];u'R\u0014X-Y7\u0015\t\u0005\u0015\u0018Q\u001e\t\u0005\r]\t9\u000f\u0005\u0003\u00022\u0006%\u0018\u0002BAv\u0003g\u0013aBW5q\u0013:\u0004X\u000f^*ue\u0016\fW\u000eC\u00042\u0003?$\t\u0019\u0001\u001a\t\u000f\u0005Ex\u0001\"\u0001\u0002t\u0006y!0\u001b9PkR\u0004X\u000f^*ue\u0016\fW\u000e\u0006\u0003\u0002v\u0006u\b\u0003\u0002\u0004\u0018\u0003o\u0004B!!-\u0002z&!\u00111`AZ\u0005=Q\u0016\u000e](viB,Ho\u0015;sK\u0006l\u0007b\u0002\u0012\u0002p\u0012\u0005\ra\t\u0005\b\u0005\u00039A\u0011\u0001B\u0002\u0003A9'0\u001b9PkR\u0004X\u000f^*ue\u0016\fW\u000e\u0006\u0003\u0003\u0006\t5\u0001\u0003\u0002\u0004\u0018\u0005\u000f\u0001B!!-\u0003\n%!!1BAZ\u0005A9%,\u0013)PkR\u0004X\u000f^*ue\u0016\fW\u000eC\u0004#\u0003\u007f$\t\u0019A\u0012\t\u000f\tEq\u0001\"\u0001\u0003\u0014\u0005y!.\u0019:PkR\u0004X\u000f^*ue\u0016\fW\u000e\u0006\u0003\u0003\u0016\tu\u0001\u0003\u0002\u0004\u0018\u0005/\u0001B!a%\u0003\u001a%!!1DAK\u0005=Q\u0015M](viB,Ho\u0015;sK\u0006l\u0007b\u0002\u0012\u0003\u0010\u0011\u0005\ra\t\u0005\b\u0005C9A\u0011\u0001B\u0012\u00039Q\u0017M]%oaV$8\u000b\u001e:fC6$BA!\n\u0003.A!aa\u0006B\u0014!\u0011\t\u0019J!\u000b\n\t\t-\u0012Q\u0013\u0002\u000f\u0015\u0006\u0014\u0018J\u001c9viN#(/Z1n\u0011\u001d\t$q\u0004CA\u0002IBqA!\r\b\t\u0003\u0011\u0019$\u0001\u0005{SB,e\u000e\u001e:z)\u0011\u0011)Da\u0011\u0015\t\t]\"\u0011\b\t\u0004\r]\u0019\u0004\u0002\u0003B\u001e\u0005_\u0001\rA!\u0010\u0002\u000b\u0015tGO]=\u0011\t\u0005E&qH\u0005\u0005\u0005\u0003\n\u0019L\u0001\u0005[SB,e\u000e\u001e:z\u0011!\t)La\fA\u0002\u0005=\u0006b\u0002B$\u000f\u0011%!\u0011J\u0001 [\u0006\\WMQ;gM\u0016\u0014X\r\u001a*fC\u0012,'\u000fT5oKR\u0013\u0018M^3sg\u0016\u0014H\u0003\u0002B&\u0005#\u0002b!!\u0017\u0003N\u0005=\u0014\u0002\u0002B(\u0003[\u0012q\u0002\u0016:bm\u0016\u00148/\u00192mK>s7-\u001a\u0005\t\u0005'\u0012)\u00051\u0001\u0002F\u00051!/Z1eKJD\u0011Ba\u0016\b#\u0003%\tA!\u0017\u0002)\u0019LG.Z,sSR,'\u000f\n3fM\u0006,H\u000e\u001e\u00132+\t\u0011YF\u000b\u0003\u0002\u000e\tu3F\u0001B0!\u0011\u0011\tGa\u001b\u000e\u0005\t\r$\u0002\u0002B3\u0005O\n\u0011\"\u001e8dQ\u0016\u001c7.\u001a3\u000b\u0007\t%D\"\u0001\u0006b]:|G/\u0019;j_:LAA!\u001c\u0003d\t\tRO\\2iK\u000e\\W\r\u001a,be&\fgnY3\t\u0013\tEt!%A\u0005\u0002\tM\u0014\u0001\u00064jY\u0016<&/\u001b;fe\u0012\"WMZ1vYR$#'\u0006\u0002\u0003v)\"\u0011Q\u0007B/\u0001")
public final class Using
{
    public static boolean fileWriter$default$2() {
        return Using$.MODULE$.fileWriter$default$2();
    }
    
    public static Charset fileWriter$default$1() {
        return Using$.MODULE$.fileWriter$default$1();
    }
    
    public static ManagedResource<InputStream> zipEntry(final ZipFile zip, final ZipEntry entry) {
        return Using$.MODULE$.zipEntry(zip, entry);
    }
    
    public static ManagedResource<JarInputStream> jarInputStream(final Function0<InputStream> in) {
        return Using$.MODULE$.jarInputStream(in);
    }
    
    public static ManagedResource<JarOutputStream> jarOutputStream(final Function0<OutputStream> out) {
        return Using$.MODULE$.jarOutputStream(out);
    }
    
    public static ManagedResource<GZIPOutputStream> gzipOutputStream(final Function0<OutputStream> out) {
        return Using$.MODULE$.gzipOutputStream(out);
    }
    
    public static ManagedResource<ZipOutputStream> zipOutputStream(final Function0<OutputStream> out) {
        return Using$.MODULE$.zipOutputStream(out);
    }
    
    public static ManagedResource<ZipInputStream> zipInputStream(final Function0<InputStream> in) {
        return Using$.MODULE$.zipInputStream(in);
    }
    
    public static ManagedResource<GZIPInputStream> gzipInputStream(final Function0<InputStream> in) {
        return Using$.MODULE$.gzipInputStream(in);
    }
    
    public static ManagedResource<Reader> streamReader(final InputStream in, final Charset charset) {
        return Using$.MODULE$.streamReader(in, charset);
    }
    
    public static ManagedResource<ZipFile> zipFile(final File source) {
        return Using$.MODULE$.zipFile(source);
    }
    
    public static ManagedResource<JarFile> jarFile(final boolean verify, final File source) {
        return Using$.MODULE$.jarFile(verify, source);
    }
    
    public static ManagedResource<BufferedReader> urlReader(final Charset charset, final URL u) {
        return Using$.MODULE$.urlReader(charset, u);
    }
    
    public static Traversable<String> fileLines(final Charset charset, final File source) {
        return Using$.MODULE$.fileLines(charset, source);
    }
    
    public static ManagedResource<BufferedReader> fileReader(final Charset charset, final File source) {
        return Using$.MODULE$.fileReader(charset, source);
    }
    
    public static ManagedResource<BufferedWriter> fileWriter(final Charset charset, final boolean append, final File source) {
        return Using$.MODULE$.fileWriter(charset, append, source);
    }
    
    public static ManagedResource<FileChannel> fileInputChannel(final File source) {
        return Using$.MODULE$.fileInputChannel(source);
    }
    
    public static ManagedResource<FileChannel> fileOuputChannel(final File source) {
        return Using$.MODULE$.fileOuputChannel(source);
    }
    
    public static ManagedResource<BufferedInputStream> urlInputStream(final URL url) {
        return Using$.MODULE$.urlInputStream(url);
    }
    
    public static ManagedResource<BufferedOutputStream> fileOutputStream(final File source) {
        return Using$.MODULE$.fileOutputStream(source);
    }
    
    public static ManagedResource<BufferedInputStream> fileInputStream(final File source) {
        return Using$.MODULE$.fileInputStream(source);
    }
    
    public static <T> ManagedResource<T> file(final Function1<File, T> in, final File source, final Resource<T> evidence$1, final OptManifest<T> evidence$2) {
        return Using$.MODULE$.file(in, source, evidence$1, evidence$2);
    }
    
    public static ManagedResource<BufferedInputStream> bufferedInputStream(final Function0<InputStream> in) {
        return Using$.MODULE$.bufferedInputStream(in);
    }
    
    public static ManagedResource<BufferedOutputStream> bufferedOutputStream(final Function0<OutputStream> out) {
        return Using$.MODULE$.bufferedOutputStream(out);
    }
}
