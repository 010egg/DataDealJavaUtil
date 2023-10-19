// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

import scala.reflect.ScalaSignature;
import java.io.Writer;

@ScalaSignature(bytes = "\u0006\u0001E4A!\u0001\u0002\u0007\u000f\tA\u0012I\u001d:bsN#(/Z1nS:<'j]8o/JLG/\u001a:\u000b\u0005\r!\u0011A\u00026t_:$4OC\u0001\u0006\u0003\ry'oZ\u0002\u0001+\tAqb\u0005\u0002\u0001\u0013A\u0019!bC\u0007\u000e\u0003\tI!\u0001\u0004\u0002\u0003'M#(/Z1nS:<'j]8o/JLG/\u001a:\u0011\u00059yA\u0002\u0001\u0003\u0006!\u0001\u0011\r!\u0005\u0002\u0002)F\u0011!\u0003\u0007\t\u0003'Yi\u0011\u0001\u0006\u0006\u0002+\u0005)1oY1mC&\u0011q\u0003\u0006\u0002\b\u001d>$\b.\u001b8h!\tIb$D\u0001\u001b\u0015\tYB$\u0001\u0002j_*\tQ$\u0001\u0003kCZ\f\u0017BA\u0010\u001b\u0005\u00199&/\u001b;fe\"A\u0011\u0005\u0001BCB\u0013E!%A\u0003o_\u0012,7/F\u0001\u000e\u0011!!\u0003A!A!\u0002\u0013i\u0011A\u00028pI\u0016\u001c\b\u0005\u0003\u0005'\u0001\t\u0015\r\u0015\"\u0005(\u0003\u0015aWM^3m+\u0005A\u0003CA\n*\u0013\tQCCA\u0002J]RD\u0001\u0002\f\u0001\u0003\u0002\u0003\u0006I\u0001K\u0001\u0007Y\u00164X\r\u001c\u0011\t\u00119\u0002!\u0011!Q\u0001\n%\ta\u0001]1sK:$\b\u0002\u0003\u0019\u0001\u0005\u000b\u0007K\u0011C\u0019\u0002\rA\u0014X\r\u001e;z+\u0005\u0011\u0004CA\n4\u0013\t!DCA\u0004C_>dW-\u00198\t\u0011Y\u0002!\u0011!Q\u0001\nI\nq\u0001\u001d:fiRL\b\u0005\u0003\u00059\u0001\t\u0015\r\u0015\"\u0005(\u0003\u0019\u0019\b/Y2fg\"A!\b\u0001B\u0001B\u0003%\u0001&A\u0004ta\u0006\u001cWm\u001d\u0011\t\u0011q\u0002!Q1Q\u0005\u0012u\nqAZ8s[\u0006$8/F\u0001?!\tQq(\u0003\u0002A\u0005\t9ai\u001c:nCR\u001c\b\u0002\u0003\"\u0001\u0005\u0003\u0005\u000b\u0011\u0002 \u0002\u0011\u0019|'/\\1ug\u0002BQ\u0001\u0012\u0001\u0005\u0002\u0015\u000ba\u0001P5oSRtDc\u0002$H\u0011&S5\n\u0014\t\u0004\u0015\u0001i\u0001\"B\u0011D\u0001\u0004i\u0001\"\u0002\u0014D\u0001\u0004A\u0003\"\u0002\u0018D\u0001\u0004I\u0001\"\u0002\u0019D\u0001\u0004\u0011\u0004\"\u0002\u001dD\u0001\u0004A\u0003\"\u0002\u001fD\u0001\u0004q\u0004B\u0002(\u0001A\u0003&!'A\u0004jg\u001aK'o\u001d;\t\u000bA\u0003A\u0011\u0001\u0012\u0002\rI,7/\u001e7u\u0011\u0015\u0011\u0006\u0001\"\u0011T\u0003!)g\u000eZ!se\u0006LH#\u0001+\u0011\u0007))V\"\u0003\u0002W\u0005\tQ!j]8o/JLG/\u001a:\t\ra\u0003\u0001\u0015\"\u0003Z\u0003)9(/\u001b;f\u0007>lW.\u0019\u000b\u00025B\u00111cW\u0005\u00039R\u0011A!\u00168ji\")a\f\u0001C!'\u0006Q1\u000f^1si\u0006\u0013(/Y=\t\u000b\u0001\u0004A\u0011I*\u0002\u0017M$\u0018M\u001d;PE*,7\r\u001e\u0005\u0006E\u0002!\taY\u0001\bC\u0012$gj\u001c3f)\t!F\rC\u0003fC\u0002\u0007a-\u0001\u0003o_\u0012,\u0007CA4k\u001d\t\u0019\u0002.\u0003\u0002j)\u00051\u0001K]3eK\u001aL!a\u001b7\u0003\rM#(/\u001b8h\u0015\tIG\u0003C\u0003o\u0001\u0011\u0005q.A\bbI\u0012\fe\u000eZ)v_R,gj\u001c3f)\t!\u0006\u000fC\u0003f[\u0002\u0007a\r")
public final class ArrayStreamingJsonWriter<T extends Writer> extends StreamingJsonWriter<T>
{
    private final T nodes;
    private final int level;
    private final StreamingJsonWriter<T> parent;
    private final boolean pretty;
    private final int spaces;
    private final Formats formats;
    private boolean isFirst;
    
    @Override
    public T nodes() {
        return this.nodes;
    }
    
    @Override
    public int level() {
        return this.level;
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
    public T result() {
        return this.nodes();
    }
    
    @Override
    public JsonWriter<T> endArray() {
        this.writePretty(2);
        this.nodes().write(93);
        return this.parent;
    }
    
    private void writeComma() {
        if (this.isFirst) {
            this.isFirst = false;
        }
        else {
            this.nodes().write(44);
            this.writePretty(this.writePretty$default$1());
        }
    }
    
    @Override
    public JsonWriter<T> startArray() {
        this.writeComma();
        return super.startArray();
    }
    
    @Override
    public JsonWriter<T> startObject() {
        this.writeComma();
        return super.startObject();
    }
    
    @Override
    public JsonWriter<T> addNode(final String node) {
        this.writeComma();
        this.nodes().write(node);
        return this;
    }
    
    @Override
    public JsonWriter<T> addAndQuoteNode(final String node) {
        this.writeComma();
        this.nodes().append((CharSequence)"\"");
        ParserUtil$.MODULE$.quote(node, this.nodes(), this.formats());
        this.nodes().append((CharSequence)"\"");
        return this;
    }
    
    public ArrayStreamingJsonWriter(final T nodes, final int level, final StreamingJsonWriter<T> parent, final boolean pretty, final int spaces, final Formats formats) {
        this.nodes = nodes;
        this.level = level;
        this.parent = parent;
        this.pretty = pretty;
        this.spaces = spaces;
        this.formats = formats;
        nodes.write(91);
        this.writePretty(this.writePretty$default$1());
        this.isFirst = true;
    }
}
