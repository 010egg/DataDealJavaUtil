// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

import org.json4s.reflect.Executable;
import scala.Some;
import scala.MatchError;
import scala.collection.GenIterable;
import scala.collection.Seq$;
import scala.reflect.ManifestFactory$;
import org.json4s.reflect.ScalaType$;
import scala.None$;
import org.json4s.reflect.ConstructorParamDescriptor;
import scala.Function0;
import scala.runtime.BoxedUnit;
import org.json4s.reflect.ConstructorDescriptor;
import org.json4s.reflect.ClassDescriptor;
import scala.PartialFunction;
import java.util.ArrayList;
import scala.collection.immutable.Set;
import scala.Function2;
import scala.runtime.BoxesRunTime;
import java.lang.reflect.Array;
import scala.runtime.ScalaRunTime$;
import scala.Predef$;
import scala.Tuple2;
import scala.collection.immutable.List;
import scala.collection.Seq;
import scala.collection.immutable.Nil$;
import scala.Array$;
import scala.collection.mutable.StringBuilder;
import scala.reflect.ClassTag$;
import scala.collection.immutable.List$;
import scala.collection.TraversableOnce;
import scala.Function1;
import scala.Option;
import scala.reflect.Manifest;
import org.json4s.reflect.package;
import scala.collection.immutable.Map;
import org.json4s.reflect.ScalaType;
import scala.reflect.ScalaSignature;

@ScalaSignature(bytes = "\u0006\u0001\r}r!B\u0001\u0003\u0011\u00039\u0011AC#yiJ\f7\r^5p]*\u00111\u0001B\u0001\u0007UN|g\u000eN:\u000b\u0003\u0015\t1a\u001c:h\u0007\u0001\u0001\"\u0001C\u0005\u000e\u0003\t1QA\u0003\u0002\t\u0002-\u0011!\"\u0012=ue\u0006\u001cG/[8o'\tIA\u0002\u0005\u0002\u000e!5\taBC\u0001\u0010\u0003\u0015\u00198-\u00197b\u0013\t\tbB\u0001\u0004B]f\u0014VM\u001a\u0005\u0006'%!\t\u0001F\u0001\u0007y%t\u0017\u000e\u001e \u0015\u0003\u001dAQAF\u0005\u0005\u0002]\tq!\u001a=ue\u0006\u001cG/\u0006\u0002\u00199Q\u0011\u0011D\r\u000b\u00045\u0015R\u0003CA\u000e\u001d\u0019\u0001!Q!H\u000bC\u0002y\u0011\u0011!Q\t\u0003?\t\u0002\"!\u0004\u0011\n\u0005\u0005r!a\u0002(pi\"Lgn\u001a\t\u0003\u001b\rJ!\u0001\n\b\u0003\u0007\u0005s\u0017\u0010C\u0003'+\u0001\u000fq%A\u0004g_Jl\u0017\r^:\u0011\u0005!A\u0013BA\u0015\u0003\u0005\u001d1uN]7biNDQaK\u000bA\u00041\n!!\u001c4\u0011\u00075\u0002$$D\u0001/\u0015\tyc\"A\u0004sK\u001adWm\u0019;\n\u0005Er#\u0001C'b]&4Wm\u001d;\t\u000bM*\u0002\u0019\u0001\u001b\u0002\t)\u001cxN\u001c\t\u0003kar!\u0001\u0003\u001c\n\u0005]\u0012\u0011a\u00029bG.\fw-Z\u0005\u0003si\u0012aA\u0013,bYV,'BA\u001c\u0003\u0011\u0015a\u0014\u0002\"\u0001>\u0003))\u0007\u0010\u001e:bGR|\u0005\u000f^\u000b\u0003}\u0011#\"a\u0010%\u0015\u0007\u0001+e\tE\u0002\u000e\u0003\u000eK!A\u0011\b\u0003\r=\u0003H/[8o!\tYB\tB\u0003\u001ew\t\u0007a\u0004C\u0003'w\u0001\u000fq\u0005C\u0003,w\u0001\u000fq\tE\u0002.a\rCQaM\u001eA\u0002QBQAF\u0005\u0005\u0002)#2aS'O)\t\u0011C\nC\u0003'\u0013\u0002\u000fq\u0005C\u00034\u0013\u0002\u0007A\u0007C\u0003P\u0013\u0002\u0007\u0001+\u0001\u0004uCJ<W\r\u001e\t\u0003kEK!A\u0015\u001e\u0003\u0011QK\b/Z%oM>DQ\u0001V\u0005\u0005\u0002U\u000bA\u0003Z3d_6\u0004xn]3XSRD')^5mI\u0016\u0014XC\u0001,Z)\r9FL\u0018\u000b\u00031n\u0003\"aG-\u0005\u000bi\u001b&\u0019\u0001\u0010\u0003\u0003QCQAJ*A\u0004\u001dBQ!X*A\u0002\t\n\u0011!\u0019\u0005\u0006?N\u0003\r\u0001Y\u0001\bEVLG\u000eZ3s!\rA\u0011\rW\u0005\u0003E\n\u0011!BS:p]^\u0013\u0018\u000e^3s\u0011\u0015!\u0017\u0002\"\u0001f\u0003Aaw.\u00193MCjLh+\u00197WC2,X\r\u0006\u0003#M\u001e\u0004\b\"B/d\u0001\u0004\u0011\u0003\"\u00025d\u0001\u0004I\u0017\u0001\u00028b[\u0016\u0004\"A[7\u000f\u00055Y\u0017B\u00017\u000f\u0003\u0019\u0001&/\u001a3fM&\u0011an\u001c\u0002\u0007'R\u0014\u0018N\\4\u000b\u00051t\u0001\"B9d\u0001\u0004\u0011\u0013\u0001\u00043fM\u0006,H\u000e\u001e,bYV,\u0007\u0002C:\n\u0011\u000b\u0007K\u0011\u0002;\u0002\u0019QL\b/Z:ICZ,g*\u0019(\u0016\u0003U\u00042A\u001b<y\u0013\t9xNA\u0002TKR\u0004$!_?\u0011\u0007)TH0\u0003\u0002|_\n)1\t\\1tgB\u00111$ \u0003\n}~\f\t\u0011!A\u0003\u0002y\u00111a\u0018\u00132\u0011)\t\t!\u0003E\u0001B\u0003&\u00111A\u0001\u000eif\u0004Xm\u001d%bm\u0016t\u0015M\u0014\u0011\u0011\t)4\u0018Q\u0001\u0019\u0005\u0003\u000f\tY\u0001\u0005\u0003ku\u0006%\u0001cA\u000e\u0002\f\u0011Iap`A\u0001\u0002\u0003\u0015\tA\b\u0005\b\u0003\u001fIA\u0011AA\t\u0003qIg\u000e^3s]\u0006dG)Z2p[B|7/Z,ji\"\u0014U/\u001b7eKJ,B!a\u0005\u0002(Q1\u0011QCA\u0010\u0003C!B!a\u0006\u0002\u001eA\u0019Q\"!\u0007\n\u0007\u0005maB\u0001\u0003V]&$\bB\u0002\u0014\u0002\u000e\u0001\u000fq\u0005\u0003\u0004^\u0003\u001b\u0001\rA\t\u0005\b?\u00065\u0001\u0019AA\u0012!\u0011A\u0011-!\n\u0011\u0007m\t9\u0003\u0002\u0004[\u0003\u001b\u0011\rA\b\u0005\b\u0003WIA\u0011AA\u0017\u0003%!WmY8na>\u001cX\r\u0006\u0003\u00020\u0005MBc\u0001\u001b\u00022!1a%!\u000bA\u0004\u001dBa!XA\u0015\u0001\u0004\u0011\u0003\u0002CA\u001c\u0013\u0001&I!!\u000f\u0002\u001d]\u0014\u0018\u000e^3Qe&l\u0017\u000e^5wKR1\u00111HA%\u0003\u0017\"B!!\u0010\u0002HA\"\u0011qHA\"!\u0011A\u0011-!\u0011\u0011\u0007m\t\u0019\u0005B\u0006\u0002F\u0005U\u0012\u0011!A\u0001\u0006\u0003q\"\u0001B0%eMBaAJA\u001b\u0001\b9\u0003BB/\u00026\u0001\u0007!\u0005C\u0004`\u0003k\u0001\r!!\u00141\t\u0005=\u00131\u000b\t\u0005\u0011\u0005\f\t\u0006E\u0002\u001c\u0003'\"1\"!\u0012\u0002L\u0005\u0005\t\u0011!B\u0001=!9\u0011qK\u0005\u0005\u0002\u0005e\u0013a\u00024mCR$XM\u001c\u000b\u0005\u00037\n)\u0007\u0006\u0003\u0002^\u0005\r\u0004#\u00026\u0002`%L\u0017bAA1_\n\u0019Q*\u00199\t\u0011\u0019\n)\u0006%AA\u0004\u001dBaaMA+\u0001\u0004!\u0004bBA5\u0013\u0011\u0005\u00111N\u0001\nk:4G.\u0019;uK:$r\u0001NA7\u0003c\nY\b\u0003\u0005\u0002p\u0005\u001d\u0004\u0019AA/\u0003\ri\u0017\r\u001d\u0005\u000b\u0003g\n9\u0007%AA\u0002\u0005U\u0014AF;tK\nKw\rR3dS6\fGNR8s\t>,(\r\\3\u0011\u00075\t9(C\u0002\u0002z9\u0011qAQ8pY\u0016\fg\u000e\u0003\u0006\u0002~\u0005\u001d\u0004\u0013!a\u0001\u0003k\n\u0001#^:f\u0005&<\u0017J\u001c;G_JduN\\4\t\rYIA\u0011AAA)\u0019\t\u0019)a\"\u0002\nR\u0019!%!\"\t\r\u0019\ny\bq\u0001(\u0011\u0019\u0019\u0014q\u0010a\u0001i!A\u00111RA@\u0001\u0004\ti)A\u0005tG\u0006d\u0017\rV=qKB!\u0011qRAJ\u001b\t\t\tJ\u0003\u00020\u0005%!\u0011QSAI\u0005%\u00196-\u00197b)f\u0004X\r\u0003\u0005\u0002\u001a&\u0001K\u0011BAN\u0003m)\u0007\u0010\u001e:bGR$U\r^3di&twMT8o)\u0016\u0014X.\u001b8bYR1\u0011QTAQ\u0003K#2AIAP\u0011\u00191\u0013q\u0013a\u0002O!9\u00111UAL\u0001\u0004!\u0014A\u00026wC2,X\r\u0003\u0005\u0002(\u0006]\u0005\u0019AAG\u0003\u001d!\u0018\u0010]3Be\u001e4a!a+\n\t\u00055&!E\"pY2,7\r^5p]\n+\u0018\u000e\u001c3feN\u0019\u0011\u0011\u0016\u0007\t\u0013M\nIK!A!\u0002\u0013!\u0004bCAZ\u0003S\u0013\t\u0011)A\u0005\u0003\u001b\u000b1\u0001\u001e9f\u0011%1\u0013\u0011\u0016B\u0001B\u0003-q\u0005C\u0004\u0014\u0003S#\t!!/\u0015\r\u0005m\u00161YAc)\u0011\ti,!1\u0011\t\u0005}\u0016\u0011V\u0007\u0002\u0013!1a%a.A\u0004\u001dBaaMA\\\u0001\u0004!\u0004\u0002CAZ\u0003o\u0003\r!!$\t\u0013\u0005\u001d\u0016\u0011\u0016Q\u0001\n\u00055\u0005\"CAf\u0003S\u0003K\u0011BAg\u00031i7nQ8mY\u0016\u001cG/[8o)\r\u0011\u0013q\u001a\u0005\t\u0003#\fI\r1\u0001\u0002T\u0006Y1m\u001c8tiJ,8\r^8s!\u0019i\u0011Q[AmE%\u0019\u0011q\u001b\b\u0003\u0013\u0019+hn\u0019;j_:\f\u0004\u0007BAn\u0003G\u0004R!DAo\u0003CL1!a8\u000f\u0005\u0015\t%O]1z!\rY\u00121\u001d\u0003\f\u0003K\fy-!A\u0001\u0002\u000b\u0005aD\u0001\u0003`II2\u0004\"CAu\u0003S\u0003K\u0011BAv\u00031i7\u000eV=qK\u0012\f%O]1z)\u0011\ti/!@\u0011\t\u0005=\u0018\u0011`\u0007\u0003\u0003cTA!a=\u0002v\u0006!A.\u00198h\u0015\t\t90\u0001\u0003kCZ\f\u0017\u0002BA~\u0003c\u0014aa\u00142kK\u000e$\bbB/\u0002h\u0002\u0007\u0011q \u0019\u0005\u0005\u0003\u0011)\u0001E\u0003\u000e\u0003;\u0014\u0019\u0001E\u0002\u001c\u0005\u000b!1Ba\u0002\u0002~\u0006\u0005\t\u0011!B\u0001=\t!q\f\n\u001a9\u0011!\u0011Y!!+\u0005\u0002\t5\u0011A\u0002:fgVdG/F\u0001#\r\u0019\u0011\t\"\u0003\u0003\u0003\u0014\t!2\t\\1tg&s7\u000f^1oG\u0016\u0014U/\u001b7eKJ\u001c2Aa\u0004\r\u0011%\u0019$q\u0002B\u0001B\u0003%A\u0007C\u0006\u0003\u001a\t=!\u0011!Q\u0001\n\tm\u0011!\u00023fg\u000e\u0014\b\u0003BAH\u0005;IAAa\b\u0002\u0012\ny1\t\\1tg\u0012+7o\u0019:jaR|'\u000fC\u0005'\u0005\u001f\u0011\t\u0011)A\u0006O!91Ca\u0004\u0005\u0002\t\u0015BC\u0002B\u0014\u0005[\u0011y\u0003\u0006\u0003\u0003*\t-\u0002\u0003BA`\u0005\u001fAaA\nB\u0012\u0001\b9\u0003BB\u001a\u0003$\u0001\u0007A\u0007\u0003\u0005\u0003\u001a\t\r\u0002\u0019\u0001B\u000e\u000f!\u0011\u0019Da\u0004\t\n\tU\u0012\u0001\u0003+za\u0016D\u0015N\u001c;\u0011\t\t]\"\u0011H\u0007\u0003\u0005\u001f1\u0001Ba\u000f\u0003\u0010!%!Q\b\u0002\t)f\u0004X\rS5oiN\u0019!\u0011\b\u0007\t\u000fM\u0011I\u0004\"\u0001\u0003BQ\u0011!Q\u0007\u0005\t\u0005\u000b\u0012I\u0004\"\u0001\u0003H\u00059QO\\1qa2LH\u0003\u0002B%\u0005[\u0002B!D!\u0003LA1QB!\u0014j\u0005#J1Aa\u0014\u000f\u0005\u0019!V\u000f\u001d7feA1!1\u000bB1\u0005OrAA!\u0016\u0003`9!!q\u000bB/\u001b\t\u0011IFC\u0002\u0003\\\u0019\ta\u0001\u0010:p_Rt\u0014\"A\b\n\u0005]r\u0011\u0002\u0002B2\u0005K\u0012A\u0001T5ti*\u0011qG\u0004\t\u0004k\t%\u0014b\u0001B6u\t1!JR5fY\u0012D\u0001Ba\u001c\u0003D\u0001\u0007!\u0011K\u0001\u0003MND\u0011Ba\u001d\u0003\u0010\u0001\u0006KA!\u001e\u0002\u0019}\u001bwN\\:ueV\u001cGo\u001c:\u0011\t\u0005=%qO\u0005\u0005\u0005s\n\tJA\u000bD_:\u001cHO];di>\u0014H)Z:de&\u0004Ho\u001c:\t\u0013\u0005E'q\u0002Q\u0005\n\tuTC\u0001B;\u0011%\u0011\tIa\u0004!\n\u0013\u0011\u0019)A\u0005tKR4\u0015.\u001a7egR\u0019AB!\"\t\ru\u0013y\b1\u0001\r\u0011%\u0011IIa\u0004!\n\u0013\u0011Y)\u0001\u0007ck&dGm\u0011;pe\u0006\u0013x\rF\u0003#\u0005\u001b\u0013y\t\u0003\u00044\u0005\u000f\u0003\r\u0001\u000e\u0005\t\u00053\u00119\t1\u0001\u0003\u0012B!\u0011q\u0012BJ\u0013\u0011\u0011)*!%\u00035\r{gn\u001d;sk\u000e$xN\u001d)be\u0006lG)Z:de&\u0004Ho\u001c:\t\u0013\te%q\u0002Q\u0005\n\t5\u0011aC5ogR\fg\u000e^5bi\u0016D\u0011B!(\u0003\u0010\u0001&IAa(\u0002\u001d5\\w+\u001b;i)f\u0004X\rS5oiR9!E!)\u0003&\n%\u0006b\u0002BR\u00057\u0003\r![\u0001\tif\u0004X\rS5oi\"A!q\u0015BN\u0001\u0004\u0011\t&\u0001\u0004gS\u0016dGm\u001d\u0005\t\u0005W\u0013Y\n1\u0001\u0002\u000e\u0006AA/\u001f9f\u0013:4w\u000e\u0003\u0005\u0003\f\t=A\u0011\u0001B\u0007\u0011!\u0011\t,\u0003Q\u0005\n\tM\u0016\u0001D2vgR|Wn\u0014:FYN,GC\u0002B[\u0005\u0003\u0014\u0019\r\u0006\u0003\u00038\nmFc\u0001\u0012\u0003:\"1aEa,A\u0004\u001dB\u0001B!0\u00030\u0002\u0007!qX\u0001\u0006i\",hn\u001b\t\u0006\u001b\u0005UGG\t\u0005\b\u001f\n=\u0006\u0019AAG\u0011\u0019\u0019$q\u0016a\u0001i!A!qY\u0005!\n\u0013\u0011I-A\u0004d_:4XM\u001d;\u0015\u000f\t\u0012YMa4\u0003R\"9!Q\u001aBc\u0001\u0004I\u0017aA6fs\"9qJ!2A\u0002\u00055\u0005B\u0002\u0014\u0003F\u0002\u0007q\u0005\u0003\u0005\u0003H&\u0001K\u0011\u0002Bk)%\u0011#q\u001bBm\u00057\u0014i\u000e\u0003\u00044\u0005'\u0004\r\u0001\u000e\u0005\b\u001f\nM\u0007\u0019AAG\u0011\u00191#1\u001ba\u0001O!A!q\u001cBj\u0001\u0004\u0011\t/A\u0004eK\u001a\fW\u000f\u001c;\u0011\t5\t%1\u001d\t\u0005\u001b\t\u0015(%C\u0002\u0003h:\u0011\u0011BR;oGRLwN\u001c\u0019\t\u0011\t-\u0018\u0002)C\u0005\u0005[\fqBZ8s[\u0006$H+[7fgR\fW\u000e\u001d\u000b\u0007\u0005_\u0014YPa@\u0011\t\tE(q_\u0007\u0003\u0005gTAA!>\u0002v\u0006\u00191/\u001d7\n\t\te(1\u001f\u0002\n)&lWm\u001d;b[BDqA!@\u0003j\u0002\u0007\u0011.A\u0001t\u0011\u00191#\u0011\u001ea\u0001O!A11A\u0005!\n\u0013\u0019)!\u0001\u0006g_Jl\u0017\r\u001e#bi\u0016$baa\u0002\u0004\u0014\rU\u0001\u0003BB\u0005\u0007\u001fi!aa\u0003\u000b\t\r5\u0011Q_\u0001\u0005kRLG.\u0003\u0003\u0004\u0012\r-!\u0001\u0002#bi\u0016DqA!@\u0004\u0002\u0001\u0007\u0011\u000e\u0003\u0004'\u0007\u0003\u0001\ra\n\u0005\n\u00073I\u0011\u0013!C\u0001\u00077\t\u0011C\u001a7biR,g\u000e\n3fM\u0006,H\u000e\u001e\u00133)\u0011\u0019ib!\r+\u0007\u001d\u001ayb\u000b\u0002\u0004\"A!11EB\u0017\u001b\t\u0019)C\u0003\u0003\u0004(\r%\u0012!C;oG\",7m[3e\u0015\r\u0019YCD\u0001\u000bC:tw\u000e^1uS>t\u0017\u0002BB\u0018\u0007K\u0011\u0011#\u001e8dQ\u0016\u001c7.\u001a3WCJL\u0017M\\2f\u0011\u0019\u00194q\u0003a\u0001i!I1QG\u0005\u0012\u0002\u0013\u00051qG\u0001\u0014k:4G.\u0019;uK:$C-\u001a4bk2$HEM\u000b\u0003\u0007sQC!!\u001e\u0004 !I1QH\u0005\u0012\u0002\u0013\u00051qG\u0001\u0014k:4G.\u0019;uK:$C-\u001a4bk2$He\r")
public final class Extraction
{
    public static boolean unflatten$default$3() {
        return Extraction$.MODULE$.unflatten$default$3();
    }
    
    public static boolean unflatten$default$2() {
        return Extraction$.MODULE$.unflatten$default$2();
    }
    
    public static Formats flatten$default$2(final JsonAST.JValue json) {
        return Extraction$.MODULE$.flatten$default$2(json);
    }
    
    public static Object extract(final JsonAST.JValue json, final ScalaType scalaType, final Formats formats) {
        return Extraction$.MODULE$.extract(json, scalaType, formats);
    }
    
    public static JsonAST.JValue unflatten(final Map<String, String> map, final boolean useBigDecimalForDouble, final boolean useBigIntForLong) {
        return Extraction$.MODULE$.unflatten(map, useBigDecimalForDouble, useBigIntForLong);
    }
    
    public static Map<String, String> flatten(final JsonAST.JValue json, final Formats formats) {
        return Extraction$.MODULE$.flatten(json, formats);
    }
    
    public static JsonAST.JValue decompose(final Object a, final Formats formats) {
        return Extraction$.MODULE$.decompose(a, formats);
    }
    
    public static <T> void internalDecomposeWithBuilder(final Object a, final JsonWriter<T> builder, final Formats formats) {
        Extraction$.MODULE$.internalDecomposeWithBuilder(a, builder, formats);
    }
    
    public static Object loadLazyValValue(final Object a, final String name, final Object defaultValue) {
        return Extraction$.MODULE$.loadLazyValValue(a, name, defaultValue);
    }
    
    public static <T> T decomposeWithBuilder(final Object a, final JsonWriter<T> builder, final Formats formats) {
        return Extraction$.MODULE$.decomposeWithBuilder(a, builder, formats);
    }
    
    public static Object extract(final JsonAST.JValue json, final package.TypeInfo target, final Formats formats) {
        return Extraction$.MODULE$.extract(json, target, formats);
    }
    
    public static <A> Option<A> extractOpt(final JsonAST.JValue json, final Formats formats, final Manifest<A> mf) {
        return Extraction$.MODULE$.extractOpt(json, formats, mf);
    }
    
    public static <A> A extract(final JsonAST.JValue json, final Formats formats, final Manifest<A> mf) {
        return Extraction$.MODULE$.extract(json, formats, mf);
    }
    
    public static class CollectionBuilder
    {
        private final JsonAST.JValue json;
        public final ScalaType org$json4s$Extraction$CollectionBuilder$$tpe;
        public final Formats org$json4s$Extraction$CollectionBuilder$$formats;
        public final ScalaType org$json4s$Extraction$CollectionBuilder$$typeArg;
        
        private Object mkCollection(final Function1<Object, Object> constructor) {
            final JsonAST.JValue json = this.json;
            Object o;
            if (json instanceof JsonAST.JArray) {
                final List arr = ((JsonAST.JArray)json).arr();
                o = ((TraversableOnce)arr.map((Function1)new Extraction$CollectionBuilder$$anonfun.Extraction$CollectionBuilder$$anonfun$9(this), List$.MODULE$.canBuildFrom())).toArray(ClassTag$.MODULE$.Any());
            }
            else {
                final JsonAST.JNothing$ jNothing = package$.MODULE$.JNothing();
                final JsonAST.JArray obj = (JsonAST.JArray)json;
                boolean b = false;
                Label_0138: {
                    Label_0099: {
                        if (jNothing == null) {
                            if (obj != null) {
                                break Label_0099;
                            }
                        }
                        else if (!jNothing.equals(obj)) {
                            break Label_0099;
                        }
                        b = true;
                        break Label_0138;
                    }
                    final JsonAST.JNull$ jNull = package$.MODULE$.JNull();
                    final JsonAST.JArray obj2 = (JsonAST.JArray)json;
                    Label_0135: {
                        if (jNull == null) {
                            if (obj2 != null) {
                                break Label_0135;
                            }
                        }
                        else if (!jNull.equals(obj2)) {
                            break Label_0135;
                        }
                        b = true;
                        break Label_0138;
                    }
                    b = false;
                }
                if (!b || this.org$json4s$Extraction$CollectionBuilder$$formats.strictArrayExtraction()) {
                    throw org.json4s.reflect.package$.MODULE$.fail(new StringBuilder().append((Object)"Expected collection but got ").append((Object)json).append((Object)" for root ").append((Object)this.json).append((Object)" and mapping ").append((Object)this.org$json4s$Extraction$CollectionBuilder$$tpe).toString(), org.json4s.reflect.package$.MODULE$.fail$default$2());
                }
                o = Array$.MODULE$.apply((Seq)Nil$.MODULE$, ClassTag$.MODULE$.AnyRef());
            }
            final Object array = o;
            return constructor.apply(array);
        }
        
        public Object org$json4s$Extraction$CollectionBuilder$$mkTypedArray(final Object a) {
            return ((Tuple2)Predef$.MODULE$.genericArrayOps(a).foldLeft((Object)new Tuple2(Array.newInstance(this.org$json4s$Extraction$CollectionBuilder$$typeArg.erasure(), ScalaRunTime$.MODULE$.array_length(a)), (Object)BoxesRunTime.boxToInteger(0)), (Function2)new Extraction$CollectionBuilder$$anonfun$org$json4s$Extraction$CollectionBuilder$$mkTypedArray.Extraction$CollectionBuilder$$anonfun$org$json4s$Extraction$CollectionBuilder$$mkTypedArray$1(this)))._1();
        }
        
        public Object result() {
            final PartialFunction custom = Formats$.MODULE$.customDeserializer((Tuple2<package.TypeInfo, JsonAST.JValue>)new Tuple2((Object)this.org$json4s$Extraction$CollectionBuilder$$tpe.typeInfo(), (Object)this.json), this.org$json4s$Extraction$CollectionBuilder$$formats);
            Object o;
            if (custom.isDefinedAt((Object)new Tuple2((Object)this.org$json4s$Extraction$CollectionBuilder$$tpe.typeInfo(), (Object)this.json))) {
                o = custom.apply((Object)new Tuple2((Object)this.org$json4s$Extraction$CollectionBuilder$$tpe.typeInfo(), (Object)this.json));
            }
            else {
                final Class<?> erasure = this.org$json4s$Extraction$CollectionBuilder$$tpe.erasure();
                final Class<List> obj = List.class;
                Label_0127: {
                    if (erasure == null) {
                        if (obj != null) {
                            break Label_0127;
                        }
                    }
                    else if (!erasure.equals(obj)) {
                        break Label_0127;
                    }
                    o = this.mkCollection((Function1<Object, Object>)new Extraction$CollectionBuilder$$anonfun$result.Extraction$CollectionBuilder$$anonfun$result$1(this));
                    return o;
                }
                final Class<?> erasure2 = this.org$json4s$Extraction$CollectionBuilder$$tpe.erasure();
                final Class<Set> obj2 = Set.class;
                Label_0171: {
                    if (erasure2 == null) {
                        if (obj2 != null) {
                            break Label_0171;
                        }
                    }
                    else if (!erasure2.equals(obj2)) {
                        break Label_0171;
                    }
                    o = this.mkCollection((Function1<Object, Object>)new Extraction$CollectionBuilder$$anonfun$result.Extraction$CollectionBuilder$$anonfun$result$2(this));
                    return o;
                }
                final Class<?> erasure3 = this.org$json4s$Extraction$CollectionBuilder$$tpe.erasure();
                final Class<scala.collection.mutable.Set> obj3 = scala.collection.mutable.Set.class;
                Label_0218: {
                    if (erasure3 == null) {
                        if (obj3 != null) {
                            break Label_0218;
                        }
                    }
                    else if (!erasure3.equals(obj3)) {
                        break Label_0218;
                    }
                    o = this.mkCollection((Function1<Object, Object>)new Extraction$CollectionBuilder$$anonfun$result.Extraction$CollectionBuilder$$anonfun$result$3(this));
                    return o;
                }
                final Class<?> erasure4 = this.org$json4s$Extraction$CollectionBuilder$$tpe.erasure();
                final Class<scala.collection.mutable.Seq> obj4 = scala.collection.mutable.Seq.class;
                Label_0265: {
                    if (erasure4 == null) {
                        if (obj4 != null) {
                            break Label_0265;
                        }
                    }
                    else if (!erasure4.equals(obj4)) {
                        break Label_0265;
                    }
                    o = this.mkCollection((Function1<Object, Object>)new Extraction$CollectionBuilder$$anonfun$result.Extraction$CollectionBuilder$$anonfun$result$4(this));
                    return o;
                }
                final Class<?> erasure5 = this.org$json4s$Extraction$CollectionBuilder$$tpe.erasure();
                final Class<ArrayList> obj5 = ArrayList.class;
                Label_0312: {
                    if (erasure5 == null) {
                        if (obj5 != null) {
                            break Label_0312;
                        }
                    }
                    else if (!erasure5.equals(obj5)) {
                        break Label_0312;
                    }
                    o = this.mkCollection((Function1<Object, Object>)new Extraction$CollectionBuilder$$anonfun$result.Extraction$CollectionBuilder$$anonfun$result$5(this));
                    return o;
                }
                o = (this.org$json4s$Extraction$CollectionBuilder$$tpe.erasure().isArray() ? this.mkCollection((Function1<Object, Object>)new Extraction$CollectionBuilder$$anonfun$result.Extraction$CollectionBuilder$$anonfun$result$6(this)) : this.mkCollection((Function1<Object, Object>)new Extraction$CollectionBuilder$$anonfun$result.Extraction$CollectionBuilder$$anonfun$result$7(this)));
            }
            return o;
        }
        
        public CollectionBuilder(final JsonAST.JValue json, final ScalaType tpe, final Formats formats) {
            this.json = json;
            this.org$json4s$Extraction$CollectionBuilder$$tpe = tpe;
            this.org$json4s$Extraction$CollectionBuilder$$formats = formats;
            this.org$json4s$Extraction$CollectionBuilder$$typeArg = (ScalaType)tpe.typeArgs().head();
        }
    }
    
    public static class ClassInstanceBuilder
    {
        public final JsonAST.JValue org$json4s$Extraction$ClassInstanceBuilder$$json;
        public final ClassDescriptor org$json4s$Extraction$ClassInstanceBuilder$$descr;
        public final Formats org$json4s$Extraction$ClassInstanceBuilder$$formats;
        private ConstructorDescriptor _constructor;
        private volatile TypeHint$ TypeHint$module;
        
        private TypeHint$ TypeHint$lzycompute() {
            synchronized (this) {
                if (this.TypeHint$module == null) {
                    this.TypeHint$module = new TypeHint$();
                }
                final BoxedUnit unit = BoxedUnit.UNIT;
                return this.TypeHint$module;
            }
        }
        
        private TypeHint$ TypeHint() {
            return (this.TypeHint$module == null) ? this.TypeHint$lzycompute() : this.TypeHint$module;
        }
        
        public ConstructorDescriptor org$json4s$Extraction$ClassInstanceBuilder$$constructor() {
            if (this._constructor == null) {
                ConstructorDescriptor constructor;
                if (this.org$json4s$Extraction$ClassInstanceBuilder$$descr.constructors().size() == 1) {
                    constructor = (ConstructorDescriptor)this.org$json4s$Extraction$ClassInstanceBuilder$$descr.constructors().head();
                }
                else {
                    final JsonAST.JValue org$json4s$Extraction$ClassInstanceBuilder$$json = this.org$json4s$Extraction$ClassInstanceBuilder$$json;
                    Object module$;
                    if (org$json4s$Extraction$ClassInstanceBuilder$$json instanceof JsonAST.JObject) {
                        final List fs = ((JsonAST.JObject)org$json4s$Extraction$ClassInstanceBuilder$$json).obj();
                        module$ = fs.map((Function1)new Extraction$ClassInstanceBuilder$$anonfun.Extraction$ClassInstanceBuilder$$anonfun$11(this), List$.MODULE$.canBuildFrom());
                    }
                    else {
                        module$ = Nil$.MODULE$;
                    }
                    final List argNames = (List)module$;
                    final Option r = this.org$json4s$Extraction$ClassInstanceBuilder$$descr.bestMatching((List<String>)argNames);
                    constructor = (ConstructorDescriptor)r.getOrElse((Function0)new Extraction$ClassInstanceBuilder$$anonfun$org$json4s$Extraction$ClassInstanceBuilder$$constructor.Extraction$ClassInstanceBuilder$$anonfun$org$json4s$Extraction$ClassInstanceBuilder$$constructor$1(this));
                }
                this._constructor = constructor;
            }
            return this._constructor;
        }
        
        private Object setFields(final Object a) {
            final JsonAST.JValue org$json4s$Extraction$ClassInstanceBuilder$$json = this.org$json4s$Extraction$ClassInstanceBuilder$$json;
            Object o;
            if (org$json4s$Extraction$ClassInstanceBuilder$$json instanceof JsonAST.JObject) {
                final List fields = ((JsonAST.JObject)org$json4s$Extraction$ClassInstanceBuilder$$json).obj();
                this.org$json4s$Extraction$ClassInstanceBuilder$$formats.fieldSerializer(a.getClass()).foreach((Function1)new Extraction$ClassInstanceBuilder$$anonfun$setFields.Extraction$ClassInstanceBuilder$$anonfun$setFields$1(this, a, fields));
                o = a;
            }
            else {
                o = a;
            }
            return o;
        }
        
        public Object org$json4s$Extraction$ClassInstanceBuilder$$buildCtorArg(final JsonAST.JValue json, final ConstructorParamDescriptor descr) {
            final Option default1 = descr.defaultValue();
            Label_0053: {
                if (descr.isOptional()) {
                    final JsonAST.JNothing$ jNothing = package$.MODULE$.JNothing();
                    if (json == null) {
                        if (jNothing != null) {
                            break Label_0053;
                        }
                    }
                    else if (!json.equals(jNothing)) {
                        break Label_0053;
                    }
                    return this.defv$1(None$.MODULE$, default1);
                }
                try {
                    final JsonAST.JNothing$ jNothing2 = package$.MODULE$.JNothing();
                    Object o2 = null;
                    Label_0120: {
                        Label_0105: {
                            if (json == null) {
                                if (jNothing2 != null) {
                                    break Label_0105;
                                }
                            }
                            else if (!json.equals(jNothing2)) {
                                break Label_0105;
                            }
                            if (default1.isDefined()) {
                                o2 = ((Function0)default1.get()).apply();
                                break Label_0120;
                            }
                        }
                        o2 = Extraction$.MODULE$.extract(json, descr.argType(), this.org$json4s$Extraction$ClassInstanceBuilder$$formats);
                    }
                    final Object x = o2;
                    if (descr.isOptional()) {
                        return (x == null) ? this.defv$1(None$.MODULE$, default1) : x;
                    }
                    if (x != null) {
                        return x;
                    }
                    if (default1.isEmpty() && descr.argType().$less$colon$less(ScalaType$.MODULE$.apply((scala.reflect.Manifest<Object>)Predef$.MODULE$.manifest(ManifestFactory$.MODULE$.AnyVal())))) {
                        throw new org.json4s.package.MappingException("Null invalid value for a sub-type of AnyVal");
                    }
                    return this.defv$1(x, default1);
                }
                finally {
                    final Throwable t;
                    final org.json4s.package.MappingException ex = (org.json4s.package.MappingException)t;
                    if (ex instanceof org.json4s.package.MappingException) {
                        final org.json4s.package.MappingException cause = ex;
                        final String msg = cause.msg();
                        if (descr.isOptional() && !this.org$json4s$Extraction$ClassInstanceBuilder$$formats.strictOptionParsing()) {
                            return this.defv$1(None$.MODULE$, default1);
                        }
                        throw org.json4s.reflect.package$.MODULE$.fail(new StringBuilder().append((Object)"No usable value for ").append((Object)descr.name()).append((Object)"\n").append((Object)msg).toString(), cause);
                    }
                }
            }
        }
        
        private Object instantiate() {
            final Executable jconstructor = this.org$json4s$Extraction$ClassInstanceBuilder$$constructor().constructor();
            final JsonAST.JValue org$json4s$Extraction$ClassInstanceBuilder$$json = this.org$json4s$Extraction$ClassInstanceBuilder$$json;
            Label_0339: {
                JsonAST.JValue value;
                if (org$json4s$Extraction$ClassInstanceBuilder$$json instanceof JsonAST.JObject) {
                    final List fields = ((JsonAST.JObject)org$json4s$Extraction$ClassInstanceBuilder$$json).obj();
                    value = (JsonAST.JValue)this.org$json4s$Extraction$ClassInstanceBuilder$$formats.fieldSerializer(this.org$json4s$Extraction$ClassInstanceBuilder$$descr.erasure().erasure()).map((Function1)new Extraction$ClassInstanceBuilder$$anonfun.Extraction$ClassInstanceBuilder$$anonfun$15(this, fields)).getOrElse((Function0)new Extraction$ClassInstanceBuilder$$anonfun.Extraction$ClassInstanceBuilder$$anonfun$16(this));
                }
                else {
                    if (org$json4s$Extraction$ClassInstanceBuilder$$json == null) {
                        break Label_0339;
                    }
                    value = org$json4s$Extraction$ClassInstanceBuilder$$json;
                }
                final JsonAST.JValue deserializedJson = value;
                final Seq paramsWithOriginalTypes = (Seq)this.org$json4s$Extraction$ClassInstanceBuilder$$constructor().params().zip((GenIterable)Predef$.MODULE$.wrapRefArray((Object[])jconstructor.getParameterTypes()), Seq$.MODULE$.canBuildFrom());
                final Seq args = (Seq)paramsWithOriginalTypes.collect((PartialFunction)new Extraction$ClassInstanceBuilder$$anonfun.Extraction$ClassInstanceBuilder$$anonfun$3(this, deserializedJson), Seq$.MODULE$.canBuildFrom());
                try {
                    final Class<?> declaringClass = jconstructor.getDeclaringClass();
                    final Class<Object> obj = Object.class;
                    Label_0317: {
                        if (declaringClass == null) {
                            if (obj != null) {
                                break Label_0317;
                            }
                        }
                        else if (!declaringClass.equals(obj)) {
                            break Label_0317;
                        }
                        final JsonAST.JValue value2 = deserializedJson;
                        if (value2 instanceof JsonAST.JObject) {
                            final Option<Tuple2<String, List<Tuple2<String, JsonAST.JValue>>>> unapply = this.TypeHint().unapply(((JsonAST.JObject)value2).obj());
                            if (!unapply.isEmpty()) {
                                final String t = (String)((Tuple2)unapply.get())._1();
                                final List fs = (List)((Tuple2)unapply.get())._2();
                                return this.mkWithTypeHint(t, (List<Tuple2<String, JsonAST.JValue>>)fs, this.org$json4s$Extraction$ClassInstanceBuilder$$descr.erasure());
                            }
                        }
                        if (value2 == null) {
                            throw new MatchError((Object)value2);
                        }
                        final Object o = value2.values();
                        return o;
                    }
                    final Object instance = jconstructor.invoke(this.org$json4s$Extraction$ClassInstanceBuilder$$descr.companion(), (Seq<Object>)args);
                    return this.setFields(instance);
                    throw new MatchError((Object)org$json4s$Extraction$ClassInstanceBuilder$$json);
                }
                finally {
                    final Throwable t3;
                    final Throwable t2 = t3;
                    if (t2 instanceof IllegalArgumentException || t2 instanceof InstantiationException) {
                        final Some[] constructorParamTypes = (Some[])Predef$.MODULE$.refArrayOps((Object[])jconstructor.getParameterTypes()).map((Function1)new Extraction$ClassInstanceBuilder$$anonfun.Extraction$ClassInstanceBuilder$$anonfun$17(this), Array$.MODULE$.canBuildFrom(ClassTag$.MODULE$.apply((Class)Some.class)));
                        final Seq argTypes = (Seq)args.map((Function1)new Extraction$ClassInstanceBuilder$$anonfun.Extraction$ClassInstanceBuilder$$anonfun$18(this), Seq$.MODULE$.canBuildFrom());
                        final String[] argsTypeComparisonResult = (String[])Predef$.MODULE$.refArrayOps((Object[])Predef$.MODULE$.refArrayOps((Object[])constructorParamTypes).zipAll((GenIterable)argTypes, (Object)None$.MODULE$, (Object)None$.MODULE$, Array$.MODULE$.canBuildFrom(ClassTag$.MODULE$.apply((Class)Tuple2.class)))).map((Function1)new Extraction$ClassInstanceBuilder$$anonfun.Extraction$ClassInstanceBuilder$$anonfun$19(this), Array$.MODULE$.canBuildFrom(ClassTag$.MODULE$.apply((Class)String.class)));
                        throw org.json4s.reflect.package$.MODULE$.fail(new StringBuilder().append((Object)"Parsed JSON values do not match with class constructor\nargs=").append((Object)args.mkString(",")).append((Object)"\narg types=").append((Object)((TraversableOnce)args.map((Function1)new Extraction$ClassInstanceBuilder$$anonfun$instantiate.Extraction$ClassInstanceBuilder$$anonfun$instantiate$1(this), Seq$.MODULE$.canBuildFrom())).mkString(",")).append((Object)"\nexecutable=").append((Object)jconstructor).append((Object)"\ncause=").append((Object)t2.getMessage()).append((Object)"\ntypes comparison result=").append((Object)Predef$.MODULE$.refArrayOps((Object[])argsTypeComparisonResult).mkString(",")).toString(), org.json4s.reflect.package$.MODULE$.fail$default$2());
                    }
                }
            }
        }
        
        private Object mkWithTypeHint(final String typeHint, final List<Tuple2<String, JsonAST.JValue>> fields, final ScalaType typeInfo) {
            final JsonAST.JObject obj = package$.MODULE$.JObject().apply((List<Tuple2<String, JsonAST.JValue>>)fields.filterNot((Function1)new Extraction$ClassInstanceBuilder$$anonfun.Extraction$ClassInstanceBuilder$$anonfun$20(this)));
            final PartialFunction deserializer = this.org$json4s$Extraction$ClassInstanceBuilder$$formats.typeHints().deserialize();
            Object o;
            if (deserializer.isDefinedAt((Object)new Tuple2((Object)typeHint, (Object)obj))) {
                o = deserializer.apply((Object)new Tuple2((Object)typeHint, (Object)obj));
            }
            else {
                final Class concreteClass = (Class)this.org$json4s$Extraction$ClassInstanceBuilder$$formats.typeHints().classFor(typeHint).getOrElse((Function0)new Extraction$ClassInstanceBuilder$$anonfun.Extraction$ClassInstanceBuilder$$anonfun$21(this, typeHint));
                o = Extraction$.MODULE$.extract(obj, typeInfo.copy(concreteClass, typeInfo.copy$default$2(), typeInfo.copy$default$3()), this.org$json4s$Extraction$ClassInstanceBuilder$$formats);
            }
            return o;
        }
        
        public Object result() {
            boolean b = false;
            final JsonAST.JValue org$json4s$Extraction$ClassInstanceBuilder$$json = this.org$json4s$Extraction$ClassInstanceBuilder$$json;
            final JsonAST.JNull$ jNull = package$.MODULE$.JNull();
            final JsonAST.JValue obj = org$json4s$Extraction$ClassInstanceBuilder$$json;
            Label_0061: {
                if (jNull == null) {
                    if (obj != null) {
                        break Label_0061;
                    }
                }
                else if (!jNull.equals(obj)) {
                    break Label_0061;
                }
                b = true;
                if (this.org$json4s$Extraction$ClassInstanceBuilder$$formats.allowNull()) {
                    return null;
                }
            }
            if (b && !this.org$json4s$Extraction$ClassInstanceBuilder$$formats.allowNull()) {
                throw org.json4s.reflect.package$.MODULE$.fail(new StringBuilder().append((Object)"Did not find value which can be converted into ").append((Object)this.org$json4s$Extraction$ClassInstanceBuilder$$descr.fullName()).toString(), org.json4s.reflect.package$.MODULE$.fail$default$2());
            }
            if (org$json4s$Extraction$ClassInstanceBuilder$$json instanceof JsonAST.JObject) {
                final Option<Tuple2<String, List<Tuple2<String, JsonAST.JValue>>>> unapply = this.TypeHint().unapply(((JsonAST.JObject)org$json4s$Extraction$ClassInstanceBuilder$$json).obj());
                if (!unapply.isEmpty()) {
                    final String t = (String)((Tuple2)unapply.get())._1();
                    final List fs = (List)((Tuple2)unapply.get())._2();
                    return this.mkWithTypeHint(t, (List<Tuple2<String, JsonAST.JValue>>)fs, this.org$json4s$Extraction$ClassInstanceBuilder$$descr.erasure());
                }
            }
            return this.instantiate();
        }
        
        private final Object defv$1(final Object v, final Option default$1) {
            return default$1.isDefined() ? ((Function0)default$1.get()).apply() : v;
        }
        
        public ClassInstanceBuilder(final JsonAST.JValue json, final ClassDescriptor descr, final Formats formats) {
            this.org$json4s$Extraction$ClassInstanceBuilder$$json = json;
            this.org$json4s$Extraction$ClassInstanceBuilder$$descr = descr;
            this.org$json4s$Extraction$ClassInstanceBuilder$$formats = formats;
            this._constructor = null;
        }
        
        public class TypeHint$
        {
            public Option<Tuple2<String, List<Tuple2<String, JsonAST.JValue>>>> unapply(final List<Tuple2<String, JsonAST.JValue>> fs) {
                Object module$2;
                if (ClassInstanceBuilder.this.org$json4s$Extraction$ClassInstanceBuilder$$formats.typeHints().shouldExtractHints(ClassInstanceBuilder.this.org$json4s$Extraction$ClassInstanceBuilder$$descr.erasure().erasure())) {
                    final Tuple2 partition = fs.partition((Function1)new Extraction$ClassInstanceBuilder$TypeHint$$anonfun.Extraction$ClassInstanceBuilder$TypeHint$$anonfun$10(this));
                    Object module$;
                    if (partition != null && Nil$.MODULE$.equals(partition._1())) {
                        module$ = None$.MODULE$;
                    }
                    else {
                        if (partition == null) {
                            throw new MatchError((Object)partition);
                        }
                        final List t = (List)partition._1();
                        final List f = (List)partition._2();
                        module$ = new Some((Object)new Tuple2((Object)((JsonAST.JValue)((Tuple2)t.head())._2()).values().toString(), (Object)f));
                    }
                    module$2 = module$;
                }
                else {
                    module$2 = None$.MODULE$;
                }
                return (Option<Tuple2<String, List<Tuple2<String, JsonAST.JValue>>>>)module$2;
            }
            
            public TypeHint$() {
                if (ClassInstanceBuilder.this == null) {
                    throw null;
                }
            }
        }
    }
}
