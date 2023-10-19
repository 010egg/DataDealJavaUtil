// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

import java.io.StringWriter;
import scala.reflect.ScalaSignature;
import java.io.Writer;

@ScalaSignature(bytes = "\u0006\u0001\u0005ea\u0001B\u0001\u0003\r\u001d\u0011qCU8piN#(/Z1nS:<'j]8o/JLG/\u001a:\u000b\u0005\r!\u0011A\u00026t_:$4OC\u0001\u0006\u0003\ry'oZ\u0002\u0001+\tAqb\u0005\u0002\u0001\u0013A\u0019!bC\u0007\u000e\u0003\tI!\u0001\u0004\u0002\u0003'M#(/Z1nS:<'j]8o/JLG/\u001a:\u0011\u00059yA\u0002\u0001\u0003\u0006!\u0001\u0011\r!\u0005\u0002\u0002)F\u0011!\u0003\u0007\t\u0003'Yi\u0011\u0001\u0006\u0006\u0002+\u0005)1oY1mC&\u0011q\u0003\u0006\u0002\b\u001d>$\b.\u001b8h!\tIb$D\u0001\u001b\u0015\tYB$\u0001\u0002j_*\tQ$\u0001\u0003kCZ\f\u0017BA\u0010\u001b\u0005\u00199&/\u001b;fe\"A\u0011\u0005\u0001BCB\u0013E!%A\u0003o_\u0012,7/F\u0001\u000e\u0011!!\u0003A!A!\u0002\u0013i\u0011A\u00028pI\u0016\u001c\b\u0005\u0003\u0005'\u0001\t\u0015\r\u0015\"\u0005(\u0003\u0019\u0001(/\u001a;usV\t\u0001\u0006\u0005\u0002\u0014S%\u0011!\u0006\u0006\u0002\b\u0005>|G.Z1o\u0011!a\u0003A!A!\u0002\u0013A\u0013a\u00029sKR$\u0018\u0010\t\u0005\t]\u0001\u0011)\u0019)C\t_\u000511\u000f]1dKN,\u0012\u0001\r\t\u0003'EJ!A\r\u000b\u0003\u0007%sG\u000f\u0003\u00055\u0001\t\u0005\t\u0015!\u00031\u0003\u001d\u0019\b/Y2fg\u0002B\u0001B\u000e\u0001\u0003\u0006\u0004&\tbN\u0001\bM>\u0014X.\u0019;t+\u0005A\u0004C\u0001\u0006:\u0013\tQ$AA\u0004G_Jl\u0017\r^:\t\u0011q\u0002!\u0011!Q\u0001\na\n\u0001BZ8s[\u0006$8\u000f\t\u0005\u0006}\u0001!\taP\u0001\u0007y%t\u0017\u000e\u001e \u0015\u000b\u0001\u000b%i\u0011#\u0011\u0007)\u0001Q\u0002C\u0004\"{A\u0005\t\u0019A\u0007\t\u000f\u0019j\u0004\u0013!a\u0001Q!9a&\u0010I\u0001\u0002\u0004\u0001\u0004b\u0002\u001c>!\u0003\u0005\r\u0001\u000f\u0005\b\r\u0002\u0011\r\u0015\"\u00050\u0003\u0015aWM^3m\u0011\u0019A\u0005\u0001)A\u0005a\u00051A.\u001a<fY\u0002BQA\u0013\u0001\u0005\u0002-\u000bq!\u00193e\u001d>$W\r\u0006\u0002M\u001fB\u0019!\"T\u0007\n\u00059\u0013!A\u0003&t_:<&/\u001b;fe\")\u0001+\u0013a\u0001#\u0006!an\u001c3f!\t\u0011VK\u0004\u0002\u0014'&\u0011A\u000bF\u0001\u0007!J,G-\u001a4\n\u0005Y;&AB*ue&twM\u0003\u0002U)!)\u0011\f\u0001C\u00015\u0006y\u0011\r\u001a3B]\u0012\fVo\u001c;f\u001d>$W\r\u0006\u0002M7\")\u0001\u000b\u0017a\u0001#\")Q\f\u0001C\u0001E\u00051!/Z:vYR<qa\u0018\u0002\u0002\u0002#%\u0001-A\fS_>$8\u000b\u001e:fC6Lgn\u001a&t_:<&/\u001b;feB\u0011!\"\u0019\u0004\b\u0003\t\t\t\u0011#\u0003c'\t\t7\r\u0005\u0002\u0014I&\u0011Q\r\u0006\u0002\u0007\u0003:L(+\u001a4\t\u000by\nG\u0011A4\u0015\u0003\u0001Dq![1\u0012\u0002\u0013\u0005!.A\u000e%Y\u0016\u001c8/\u001b8ji\u0012:'/Z1uKJ$C-\u001a4bk2$H%M\u000b\u0003Wf,\u0012\u0001\u001c\u0016\u0003[B\u0004\"!\u00078\n\u0005=T\"\u0001D*ue&twm\u0016:ji\u0016\u00148&A9\u0011\u0005I<X\"A:\u000b\u0005Q,\u0018!C;oG\",7m[3e\u0015\t1H#\u0001\u0006b]:|G/\u0019;j_:L!\u0001_:\u0003#Ut7\r[3dW\u0016$g+\u0019:jC:\u001cW\rB\u0003\u0011Q\n\u0007\u0011\u0003C\u0004|CF\u0005I\u0011\u0001?\u00027\u0011bWm]:j]&$He\u001a:fCR,'\u000f\n3fM\u0006,H\u000e\u001e\u00133+\tix0F\u0001\u007fU\tA\u0003\u000fB\u0003\u0011u\n\u0007\u0011\u0003C\u0005\u0002\u0004\u0005\f\n\u0011\"\u0001\u0002\u0006\u0005YB\u0005\\3tg&t\u0017\u000e\u001e\u0013he\u0016\fG/\u001a:%I\u00164\u0017-\u001e7uIM*B!a\u0002\u0002\fU\u0011\u0011\u0011\u0002\u0016\u0003aA$a\u0001EA\u0001\u0005\u0004\t\u0002\"CA\bCF\u0005I\u0011AA\t\u0003m!C.Z:tS:LG\u000fJ4sK\u0006$XM\u001d\u0013eK\u001a\fW\u000f\u001c;%iU!\u00111CA\f+\t\t)B\u000b\u00029a\u00121\u0001#!\u0004C\u0002E\u0001")
public final class RootStreamingJsonWriter<T extends Writer> extends StreamingJsonWriter<T>
{
    private final T nodes;
    private final boolean pretty;
    private final int spaces;
    private final Formats formats;
    private final int level;
    
    public static <T extends Writer> Formats $lessinit$greater$default$4() {
        return RootStreamingJsonWriter$.MODULE$.$lessinit$greater$default$4();
    }
    
    public static <T extends Writer> int $lessinit$greater$default$3() {
        return RootStreamingJsonWriter$.MODULE$.$lessinit$greater$default$3();
    }
    
    public static <T extends Writer> boolean $lessinit$greater$default$2() {
        return RootStreamingJsonWriter$.MODULE$.$lessinit$greater$default$2();
    }
    
    public static <T extends Writer> StringWriter $lessinit$greater$default$1() {
        return RootStreamingJsonWriter$.MODULE$.$lessinit$greater$default$1();
    }
    
    @Override
    public T nodes() {
        return this.nodes;
    }
    
    @Override
    public boolean pretty() {
        return this.pretty;
    }
    
    @Override
    public int spaces() {
        return this.spaces;
    }
    
    @Override
    public Formats formats() {
        return this.formats;
    }
    
    @Override
    public int level() {
        return this.level;
    }
    
    @Override
    public JsonWriter<T> addNode(final String node) {
        this.nodes().write(node);
        return this;
    }
    
    @Override
    public JsonWriter<T> addAndQuoteNode(final String node) {
        this.nodes().append((CharSequence)"\"");
        ParserUtil$.MODULE$.quote(node, this.nodes(), this.formats());
        this.nodes().append((CharSequence)"\"");
        return this;
    }
    
    @Override
    public T result() {
        return this.nodes();
    }
    
    public RootStreamingJsonWriter(final T nodes, final boolean pretty, final int spaces, final Formats formats) {
        this.nodes = nodes;
        this.pretty = pretty;
        this.spaces = spaces;
        this.formats = formats;
        this.level = 0;
    }
}
