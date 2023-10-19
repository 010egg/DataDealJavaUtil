// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

import scala.None$;
import scala.Option$;
import scala.Function0;
import scala.Option;
import scala.reflect.Manifest;
import scala.reflect.ScalaSignature;

@ScalaSignature(bytes = "\u0006\u0001M4A!\u0001\u0002\u0001\u000f\t1R\t\u001f;sC\u000e$\u0018M\u00197f\u0015N|g.Q:u\u001d>$WM\u0003\u0002\u0004\t\u00051!n]8oiMT\u0011!B\u0001\u0004_J<7\u0001A\n\u0003\u0001!\u0001\"!\u0003\u0007\u000e\u0003)Q\u0011aC\u0001\u0006g\u000e\fG.Y\u0005\u0003\u001b)\u0011a!\u00118z%\u00164\u0007\u0002C\b\u0001\u0005\u0003\u0005\u000b\u0011\u0002\t\u0002\u0005)4\bCA\t\u0016\u001d\t\u00112#D\u0001\u0003\u0013\t!\"!A\u0004qC\u000e\\\u0017mZ3\n\u0005Y9\"A\u0002&WC2,XM\u0003\u0002\u0015\u0005!)\u0011\u0004\u0001C\u00015\u00051A(\u001b8jiz\"\"a\u0007\u000f\u0011\u0005I\u0001\u0001\"B\b\u0019\u0001\u0004\u0001\u0002\"\u0002\u0010\u0001\t\u0003y\u0012aB3yiJ\f7\r^\u000b\u0003A\r\"2!\t\u00172!\t\u00113\u0005\u0004\u0001\u0005\u000b\u0011j\"\u0019A\u0013\u0003\u0003\u0005\u000b\"AJ\u0015\u0011\u0005%9\u0013B\u0001\u0015\u000b\u0005\u001dqu\u000e\u001e5j]\u001e\u0004\"!\u0003\u0016\n\u0005-R!aA!os\")Q&\ba\u0002]\u00059am\u001c:nCR\u001c\bC\u0001\n0\u0013\t\u0001$AA\u0004G_Jl\u0017\r^:\t\u000bIj\u00029A\u001a\u0002\u000554\u0007c\u0001\u001b8C5\tQG\u0003\u00027\u0015\u00059!/\u001a4mK\u000e$\u0018B\u0001\u001d6\u0005!i\u0015M\\5gKN$\b\"\u0002\u001e\u0001\t\u0003Y\u0014AC3yiJ\f7\r^(qiV\u0011A(\u0011\u000b\u0004{\t\u001b\u0005cA\u0005?\u0001&\u0011qH\u0003\u0002\u0007\u001fB$\u0018n\u001c8\u0011\u0005\t\nE!\u0002\u0013:\u0005\u0004)\u0003\"B\u0017:\u0001\bq\u0003\"\u0002\u001a:\u0001\b!\u0005c\u0001\u001b8\u0001\")a\t\u0001C\u0001\u000f\u0006iQ\r\u001f;sC\u000e$xJ]#mg\u0016,\"\u0001S&\u0015\u0005%{Ec\u0001&M\u001bB\u0011!e\u0013\u0003\u0006I\u0015\u0013\r!\n\u0005\u0006[\u0015\u0003\u001dA\f\u0005\u0006e\u0015\u0003\u001dA\u0014\t\u0004i]R\u0005B\u0002)F\t\u0003\u0007\u0011+A\u0004eK\u001a\fW\u000f\u001c;\u0011\u0007%\u0011&*\u0003\u0002T\u0015\tAAHY=oC6,g\bC\u0003V\u0001\u0011\u0005a+\u0001\u0002bgV\u0011q+\u0017\u000b\u00031j\u0003\"AI-\u0005\u000b\u0011\"&\u0019A\u0013\t\u000bm#\u00069\u0001/\u0002\rI,\u0017\rZ3s!\r\u0011R\fW\u0005\u0003=\n\u0011aAU3bI\u0016\u0014\b\"\u00021\u0001\t\u0003\t\u0017!B4fi\u0006\u001bXC\u00012f)\t\u0019g\rE\u0002\n}\u0011\u0004\"AI3\u0005\u000b\u0011z&\u0019A\u0013\t\u000bm{\u00069A4\u0011\u0007IiF\rC\u0003j\u0001\u0011\u0005!.A\u0006hKR\f5o\u0014:FYN,WCA6o)\ta\u0017\u000f\u0006\u0002n_B\u0011!E\u001c\u0003\u0006I!\u0014\r!\n\u0005\u00067\"\u0004\u001d\u0001\u001d\t\u0004%uk\u0007B\u0002)i\t\u0003\u0007!\u000fE\u0002\n%6\u0004")
public class ExtractableJsonAstNode
{
    private final JsonAST.JValue jv;
    
    public <A> A extract(final Formats formats, final Manifest<A> mf) {
        return Extraction$.MODULE$.extract(this.jv, formats, mf);
    }
    
    public <A> Option<A> extractOpt(final Formats formats, final Manifest<A> mf) {
        return Extraction$.MODULE$.extractOpt(this.jv, formats, mf);
    }
    
    public <A> A extractOrElse(final Function0<A> default, final Formats formats, final Manifest<A> mf) {
        return (A)Extraction$.MODULE$.extractOpt(this.jv, formats, (scala.reflect.Manifest<Object>)mf).getOrElse((Function0)default);
    }
    
    public <A> A as(final Reader<A> reader) {
        return reader.read(this.jv);
    }
    
    public <A> Option<A> getAs(final Reader<A> reader) {
        None$ module$;
        try {
            Option$.MODULE$.apply((Object)reader.read(this.jv));
        }
        finally {
            module$ = None$.MODULE$;
        }
        return (Option<A>)module$;
    }
    
    public <A> A getAsOrElse(final Function0<A> default, final Reader<A> reader) {
        return (A)this.getAs(reader).getOrElse((Function0)default);
    }
    
    public ExtractableJsonAstNode(final JsonAST.JValue jv) {
        this.jv = jv;
    }
}
