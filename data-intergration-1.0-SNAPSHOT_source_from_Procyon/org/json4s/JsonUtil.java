// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

import scala.Function1;
import scala.Option;
import java.io.Writer;
import scala.reflect.Manifest;
import scala.reflect.ScalaSignature;

@ScalaSignature(bytes = "\u0006\u0001\u0005]b!B\u0001\u0003\u0003\u00039!\u0001\u0003&t_:,F/\u001b7\u000b\u0005\r!\u0011A\u00026t_:$4OC\u0001\u0006\u0003\ry'oZ\u0002\u0001'\t\u0001\u0001\u0002\u0005\u0002\n\u00195\t!BC\u0001\f\u0003\u0015\u00198-\u00197b\u0013\ti!B\u0001\u0004B]f\u0014VM\u001a\u0005\t\u001f\u0001\u0011\t\u0011)A\u0005!\u0005!a-\u001c;t!\t\t\"#D\u0001\u0003\u0013\t\u0019\"AA\u0004G_Jl\u0017\r^:\t\u000bU\u0001A\u0011\u0001\f\u0002\rqJg.\u001b;?)\t9\u0002\u0004\u0005\u0002\u0012\u0001!)q\u0002\u0006a\u0001!!9!\u0004\u0001b!\n'Y\u0012a\u00024pe6\fGo]\u000b\u0002!!1Q\u0004\u0001Q\u0001\nA\t\u0001BZ8s[\u0006$8\u000f\t\u0005\u0006?\u00011\t\u0001I\u0001\u0006oJLG/Z\u000b\u0003CE\"\"AI\u001c\u0015\u0005\rR\u0003C\u0001\u0013(\u001d\tIQ%\u0003\u0002'\u0015\u00051\u0001K]3eK\u001aL!\u0001K\u0015\u0003\rM#(/\u001b8h\u0015\t1#\u0002C\u0004,=\u0005\u0005\t9\u0001\u0017\u0002\u0015\u00154\u0018\u000eZ3oG\u0016$\u0013\u0007E\u0002%[=J!AL\u0015\u0003\u00115\u000bg.\u001b4fgR\u0004\"\u0001M\u0019\r\u0001\u0011)!G\bb\u0001g\t\t\u0011)\u0005\u00025\u0011A\u0011\u0011\"N\u0005\u0003m)\u0011qAT8uQ&tw\rC\u00039=\u0001\u0007q&A\u0001b\u0011\u0015y\u0002A\"\u0001;+\rYTJ\u0010\u000b\u0004y9{ECA\u001fJ!\t\u0001d\bB\u0003@s\t\u0007\u0001IA\u0001X#\t!\u0014\t\u0005\u0002C\u000f6\t1I\u0003\u0002E\u000b\u0006\u0011\u0011n\u001c\u0006\u0002\r\u0006!!.\u0019<b\u0013\tA5I\u0001\u0004Xe&$XM\u001d\u0005\b\u0015f\n\t\u0011q\u0001L\u0003))g/\u001b3f]\u000e,GE\r\t\u0004I5b\u0005C\u0001\u0019N\t\u0015\u0011\u0014H1\u00014\u0011\u0015A\u0014\b1\u0001M\u0011\u0015\u0001\u0016\b1\u0001>\u0003\ryW\u000f\u001e\u0005\u0006%\u00021\taU\u0001\foJLG/\u001a)sKR$\u00180\u0006\u0002U/R\u00111%\u0016\u0005\u0006qE\u0003\rA\u0016\t\u0003a]#QAM)C\u0002MBQA\u0015\u0001\u0007\u0002e+2AW0])\rYV\f\u0019\t\u0003aq#Qa\u0010-C\u0002\u0001CQ\u0001\u000f-A\u0002y\u0003\"\u0001M0\u0005\u000bIB&\u0019A\u001a\t\u000bAC\u0006\u0019A.\t\u000b\t\u0004A\u0011A2\u0002\tI,\u0017\rZ\u000b\u0003I\u001e$\"!Z8\u0015\u0005\u0019d\u0007C\u0001\u0019h\t\u0015\u0011\u0014M1\u0001i#\t!\u0014\u000e\u0005\u0002\nU&\u00111N\u0003\u0002\u0004\u0003:L\bbB7b\u0003\u0003\u0005\u001dA\\\u0001\u000bKZLG-\u001a8dK\u0012\u001a\u0004c\u0001\u0013.M\")\u0001/\u0019a\u0001c\u0006!!n]8o!\t\t\"/\u0003\u0002t\u0005\tI!j]8o\u0013:\u0004X\u000f\u001e\u0005\u0006k\u0002!\tA^\u0001\be\u0016\fGm\u00149u+\t9X\u0010F\u0002y\u0003\u0007!\"!\u001f@\u0011\u0007%QH0\u0003\u0002|\u0015\t1q\n\u001d;j_:\u0004\"\u0001M?\u0005\u000bI\"(\u0019\u00015\t\u0011}$\u0018\u0011!a\u0002\u0003\u0003\t!\"\u001a<jI\u0016t7-\u001a\u00135!\r!S\u0006 \u0005\u0006aR\u0004\r!\u001d\u0005\b\u0003\u000f\u0001a\u0011AA\u0005\u0003\u0015\u0001\u0018M]:f)\u0011\tY!!\u0007\u0011\t\u00055\u00111\u0003\b\u0004#\u0005=\u0011bAA\t\u0005\u00059\u0001/Y2lC\u001e,\u0017\u0002BA\u000b\u0003/\u0011aA\u0013,bYV,'bAA\t\u0005!1\u0001/!\u0002A\u0002EDq!!\b\u0001\r\u0003\ty\"\u0001\u0005qCJ\u001cXm\u00149u)\u0011\t\t#a\t\u0011\t%Q\u00181\u0002\u0005\u0007a\u0006m\u0001\u0019A9\t\u000f\u0005\u001d\u0002\u0001\"\u0001\u0002*\u0005IA-Z2p[B|7/\u001a\u000b\u0005\u0003\u0017\tY\u0003C\u0004\u0002.\u0005\u0015\u0002\u0019A5\u0002\u0007\u0005t\u0017\u0010C\u0004\u00022\u00011\t!a\r\u0002\u0017]LG\u000f\u001b$pe6\fGo\u001d\u000b\u0004/\u0005U\u0002BB\b\u00020\u0001\u0007\u0001\u0003")
public abstract class JsonUtil
{
    private final Formats formats;
    
    public Formats formats() {
        return this.formats;
    }
    
    public abstract <A> String write(final A p0, final Manifest<A> p1);
    
    public abstract <A, W extends Writer> W write(final A p0, final W p1, final Manifest<A> p2);
    
    public abstract <A> String writePretty(final A p0);
    
    public abstract <A, W extends Writer> W writePretty(final A p0, final W p1);
    
    public <A> A read(final JsonInput json, final Manifest<A> evidence$3) {
        return package$.MODULE$.jvalue2extractable(this.parse(json)).extract(this.formats(), evidence$3);
    }
    
    public <A> Option<A> readOpt(final JsonInput json, final Manifest<A> evidence$4) {
        return (Option<A>)this.parseOpt(json).flatMap((Function1)new JsonUtil$$anonfun$readOpt.JsonUtil$$anonfun$readOpt$1(this, (Manifest)evidence$4));
    }
    
    public abstract JsonAST.JValue parse(final JsonInput p0);
    
    public abstract Option<JsonAST.JValue> parseOpt(final JsonInput p0);
    
    public JsonAST.JValue decompose(final Object any) {
        return Extraction$.MODULE$.decompose(any, this.formats());
    }
    
    public abstract JsonUtil withFormats(final Formats p0);
    
    public JsonUtil(final Formats fmts) {
        this.formats = fmts;
    }
}
