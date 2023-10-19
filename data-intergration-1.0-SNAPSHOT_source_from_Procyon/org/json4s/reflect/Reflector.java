// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.reflect;

import scala.PartialFunction;
import scala.Option$;
import scala.math.Ordering;
import scala.math.Ordering$;
import scala.collection.GenTraversableOnce;
import scala.reflect.ClassTag$;
import scala.Array$;
import scala.MatchError;
import java.lang.reflect.Method;
import scala.Some;
import scala.collection.Iterable;
import scala.None$;
import scala.reflect.ManifestFactory$;
import java.lang.reflect.WildcardType;
import scala.collection.immutable.Map;
import scala.collection.immutable.List$;
import java.lang.reflect.TypeVariable;
import scala.collection.TraversableOnce;
import scala.collection.immutable.Nil$;
import scala.collection.Seq$;
import scala.Predef$;
import scala.collection.TraversableLike;
import java.lang.reflect.ParameterizedType;
import scala.runtime.BoxedUnit;
import java.lang.reflect.Modifier;
import java.lang.reflect.Field;
import scala.Function1;
import scala.util.control.Exception$;
import scala.collection.Iterator;
import scala.collection.mutable.ListBuffer;
import scala.reflect.Manifest;
import scala.Function0;
import scala.Option;
import scala.collection.Seq;
import org.json4s.Formats;
import java.lang.reflect.Type;
import scala.collection.immutable.Set;
import scala.Tuple2;
import scala.collection.immutable.List;
import scala.reflect.ScalaSignature;

@ScalaSignature(bytes = "\u0006\u0001\rEs!B\u0001\u0003\u0011\u0003I\u0011!\u0003*fM2,7\r^8s\u0015\t\u0019A!A\u0004sK\u001adWm\u0019;\u000b\u0005\u00151\u0011A\u00026t_:$4OC\u0001\b\u0003\ry'oZ\u0002\u0001!\tQ1\"D\u0001\u0003\r\u0015a!\u0001#\u0001\u000e\u0005%\u0011VM\u001a7fGR|'o\u0005\u0002\f\u001dA\u0011qBE\u0007\u0002!)\t\u0011#A\u0003tG\u0006d\u0017-\u0003\u0002\u0014!\t1\u0011I\\=SK\u001aDQ!F\u0006\u0005\u0002Y\ta\u0001P5oSRtD#A\u0005\t\raY\u0001\u0015!\u0003\u001a\u0003)\u0011\u0018m^\"mCN\u001cXm\u001d\t\u00055u\u0001\u0013F\u0004\u0002\u000b7%\u0011ADA\u0001\ba\u0006\u001c7.Y4f\u0013\tqrD\u0001\u0003NK6|'B\u0001\u000f\u0003!\t\ts%D\u0001#\u0015\t\u00191E\u0003\u0002%K\u0005!A.\u00198h\u0015\u00051\u0013\u0001\u00026bm\u0006L!\u0001\u000b\u0012\u0003\tQK\b/\u001a\u0019\u0003UA\u00022a\u000b\u0017/\u001b\u0005\u0019\u0013BA\u0017$\u0005\u0015\u0019E.Y:t!\ty\u0003\u0007\u0004\u0001\u0005\u0013E:\u0012\u0011!A\u0001\u0006\u0003\u0011$aA0%cE\u00111G\u000e\t\u0003\u001fQJ!!\u000e\t\u0003\u000f9{G\u000f[5oOB\u0011qbN\u0005\u0003qA\u00111!\u00118z\u0011\u0019Q4\u0002)A\u0005w\u0005qQO\\7b]\u001edW\r\u001a(b[\u0016\u001c\b\u0003\u0002\u000e\u001eyq\u0002\"!\u0010!\u000f\u0005=q\u0014BA \u0011\u0003\u0019\u0001&/\u001a3fM&\u0011\u0011I\u0011\u0002\u0007'R\u0014\u0018N\\4\u000b\u0005}\u0002\u0002B\u0002#\fA\u0003%Q)A\u0006eKN\u001c'/\u001b9u_J\u001c\b\u0003\u0002\u000e\u001e\r&\u0003\"AC$\n\u0005!\u0013!!C*dC2\fG+\u001f9f!\tQ!*\u0003\u0002L\u0005\t\u0001rJ\u00196fGR$Um]2sSB$xN\u001d\u0005\u0007\u001b.\u0001\u000b\u0011\u0002(\u0002\u0015A\u0014\u0018.\\5uSZ,7\u000fE\u0002P)\u0002j\u0011\u0001\u0015\u0006\u0003#J\u000b\u0011\"[7nkR\f'\r\\3\u000b\u0005M\u0003\u0012AC2pY2,7\r^5p]&\u0011Q\u000b\u0015\u0002\u0004'\u0016$\b\"B,\f\t\u0003A\u0016aC2mK\u0006\u00148)Y2iKN$\u0012!\u0017\t\u0003\u001fiK!a\u0017\t\u0003\tUs\u0017\u000e\u001e\u0005\u0006;.!\tAX\u0001\fSN\u0004&/[7ji&4X\rF\u0002`E\u0012\u0004\"a\u00041\n\u0005\u0005\u0004\"a\u0002\"p_2,\u0017M\u001c\u0005\u0006Gr\u0003\r\u0001I\u0001\u0002i\"9Q\r\u0018I\u0001\u0002\u00041\u0017!B3yiJ\f\u0007cA\u001fhA%\u0011QK\u0011\u0005\u0006S.!\tA[\u0001\fg\u000e\fG.\u0019+za\u0016|e-\u0006\u0002leR\u0011a\t\u001c\u0005\u0006[\"\u0004\u001dA\\\u0001\u0003[\u001a\u00042!P8r\u0013\t\u0001(I\u0001\u0005NC:Lg-Z:u!\ty#\u000fB\u0003tQ\n\u0007!GA\u0001U\u0011\u0015I7\u0002\"\u0001v)\t1e\u000fC\u0003xi\u0002\u0007\u00010A\u0003dY\u0006T(\u0010\r\u0002zyB\u0019QH_>\n\u00055\u0012\u0005CA\u0018}\t%ih/!A\u0001\u0002\u000b\u0005!GA\u0002`IIBQ![\u0006\u0005\u0002}$2ARA\u0001\u0011\u0015\u0019g\u00101\u0001!\u0011!\t)a\u0003Q\u0001\n\u0005\u001d\u0011aC:ue&tw\rV=qKN\u0004RAG\u000f=\u0003\u0013\u0001BaDA\u0006\r&\u0019\u0011Q\u0002\t\u0003\r=\u0003H/[8o\u0011\u0019I7\u0002\"\u0001\u0002\u0012Q!\u0011\u0011BA\n\u0011\u001d\t)\"a\u0004A\u0002q\nAA\\1nK\"9\u0011\u0011D\u0006\u0005\u0002\u0005m\u0011\u0001\u00033fg\u000e\u0014\u0018NY3\u0016\t\u0005u\u0011Q\u0005\u000b\u0006\u0013\u0006}\u0011q\u0005\u0005\b[\u0006]\u00019AA\u0011!\u0011it.a\t\u0011\u0007=\n)\u0003\u0002\u0004t\u0003/\u0011\rA\r\u0005\u000b\u0003S\t9\u0002%AA\u0004\u0005-\u0012a\u00024pe6\fGo\u001d\t\u0005\u0003[\ty#D\u0001\u0005\u0013\r\t\t\u0004\u0002\u0002\b\r>\u0014X.\u0019;t\u0011\u001d\tIb\u0003C\u0001\u0003k!2!SA\u001c\u0011!\tI$a\rA\u0002\u0005m\u0012AA:ua\u0011\ti$!\u0012\u0011\u000bi\ty$a\u0011\n\u0007\u0005\u0005sD\u0001\u000bSK\u001adWm\u0019;pe\u0012+7o\u0019:jE\u0006\u0014G.\u001a\t\u0004_\u0005\u0015CaCA$\u0003o\t\t\u0011!A\u0003\u0002I\u00121a\u0018\u00134\u0011\u001d\tYe\u0003C\u0001\u0003\u001b\n\u0001c\u0019:fCR,G)Z:de&\u0004Ho\u001c:\u0015\u000f%\u000by%a\u0015\u0002^!9\u0011\u0011KA%\u0001\u00041\u0015a\u0001;qK\"Q\u0011QKA%!\u0003\u0005\r!a\u0016\u0002\u001fA\f'/Y7OC6,'+Z1eKJ\u00042AGA-\u0013\r\tYf\b\u0002\u0014!\u0006\u0014\u0018-\\3uKJt\u0015-\\3SK\u0006$WM\u001d\u0005\u000b\u0003?\nI\u0005%AA\u0002\u0005\u0005\u0014!E2p[B\fg.[8o\u001b\u0006\u0004\b/\u001b8hgB1\u00111MA9\u0003orA!!\u001a\u0002p9!\u0011qMA7\u001b\t\tIGC\u0002\u0002l!\ta\u0001\u0010:p_Rt\u0014\"A\t\n\u0005q\u0001\u0012\u0002BA:\u0003k\u0012A\u0001T5ti*\u0011A\u0004\u0005\t\u0007\u001f\u0005e\u0014Q\u0010\b\n\u0007\u0005m\u0004C\u0001\u0004UkBdWM\r\u0019\u0005\u0003\u007f\n\u0019\t\u0005\u0003>u\u0006\u0005\u0005cA\u0018\u0002\u0004\u0012Y\u0011QQA/\u0003\u0003\u0005\tQ!\u00013\u0005\ryF\u0005\u000e\u0004\u0007\u0003\u0013[A!a#\u0003-\rc\u0017m]:EKN\u001c'/\u001b9u_J\u0014U/\u001b7eKJ\u001c2!a\"\u000f\u0011)\t\t&a\"\u0003\u0002\u0003\u0006IA\u0012\u0005\f\u0003+\n9I!A!\u0002\u0013\t9\u0006C\u0006\u0002`\u0005\u001d%\u0011!Q\u0001\n\u0005M\u0005CBA2\u0003c\n)\n\u0005\u0004\u0010\u0003s\n9J\u0004\u0019\u0005\u00033\u000bi\n\u0005\u0003>u\u0006m\u0005cA\u0018\u0002\u001e\u0012Y\u0011qTAI\u0003\u0003\u0005\tQ!\u00013\u0005\ryF%\u000e\u0005\b+\u0005\u001dE\u0011AAR)!\t)+!+\u0002,\u00065\u0006\u0003BAT\u0003\u000fk\u0011a\u0003\u0005\b\u0003#\n\t\u000b1\u0001G\u0011)\t)&!)\u0011\u0002\u0003\u0007\u0011q\u000b\u0005\u000b\u0003?\n\t\u000b%AA\u0002\u0005=\u0006CBA2\u0003c\n\t\f\u0005\u0004\u0010\u0003s\n\u0019L\u0004\u0019\u0005\u0003k\u000bI\f\u0005\u0003>u\u0006]\u0006cA\u0018\u0002:\u0012Y\u0011qTAW\u0003\u0003\u0005\tQ!\u00013\u0011)\ti,a\"A\u0002\u0013\u0005\u0011qX\u0001\nG>l\u0007/\u00198j_:,\"!!1\u0011\u000b=\tY!a1\u0011\u0007)\t)-C\u0002\u0002H\n\u00111cU5oO2,Go\u001c8EKN\u001c'/\u001b9u_JD!\"a3\u0002\b\u0002\u0007I\u0011AAg\u00035\u0019w.\u001c9b]&|gn\u0018\u0013fcR\u0019\u0011,a4\t\u0015\u0005E\u0017\u0011ZA\u0001\u0002\u0004\t\t-A\u0002yIEB\u0011\"!6\u0002\b\u0002\u0006K!!1\u0002\u0015\r|W\u000e]1oS>t\u0007\u0005\u0003\u0006\u0002Z\u0006\u001d\u0005\u0019!C\u0001\u00037\fa\u0002\u001e:jK\u0012\u001cu.\u001c9b]&|g.F\u0001`\u0011)\ty.a\"A\u0002\u0013\u0005\u0011\u0011]\u0001\u0013iJLW\rZ\"p[B\fg.[8o?\u0012*\u0017\u000fF\u0002Z\u0003GD\u0011\"!5\u0002^\u0006\u0005\t\u0019A0\t\u0011\u0005\u001d\u0018q\u0011Q!\n}\u000bq\u0002\u001e:jK\u0012\u001cu.\u001c9b]&|g\u000e\t\u0005\t\u0003W\f9\t\"\u0001\u0002n\u00061a-[3mIN$B!a<\u0002xB1\u00111MA9\u0003c\u00042ACAz\u0013\r\t)P\u0001\u0002\u0013!J|\u0007/\u001a:us\u0012+7o\u0019:jaR|'\u000fC\u0004x\u0003S\u0004\r!!?1\t\u0005m\u0018q \t\u0005{i\fi\u0010E\u00020\u0003\u007f$1B!\u0001\u0002x\u0006\u0005\t\u0011!B\u0001e\t\u0019q\f\n\u001c\t\u0011\t\u0015\u0011q\u0011C\u0001\u0005\u000f\t!\u0002\u001d:pa\u0016\u0014H/[3t+\t\u0011I\u0001\u0005\u0004\u0002d\t-\u0011\u0011_\u0005\u0005\u0005\u001b\t)HA\u0002TKFD\u0001B!\u0005\u0002\b\u0012\u0005!1C\u0001\u000eGR|'\u000fU1sC6$\u0016\u0010]3\u0015\u001b\u0019\u0013)Ba\u0006\u0003\"\t\u0015\"1\u0006B\u0017\u0011\u001d\t)Ba\u0004A\u0002qB\u0001B!\u0007\u0003\u0010\u0001\u0007!1D\u0001\u0006S:$W\r\u001f\t\u0004\u001f\tu\u0011b\u0001B\u0010!\t\u0019\u0011J\u001c;\t\u000f\t\r\"q\u0002a\u0001\r\u0006)qn\u001e8fe\"A!q\u0005B\b\u0001\u0004\u0011I#\u0001\ndi>\u0014\b+\u0019:b[\u0016$XM\u001d(b[\u0016\u001c\b#BA2\u0003cb\u0004BB2\u0003\u0010\u0001\u0007\u0001\u0005\u0003\u0006\u00030\t=\u0001\u0013!a\u0001\u0005c\t\u0011bY8oi\u0006Lg.\u001a:\u0011\u000b=\tYAa\r\u0011\r=\tIH\u0012B\u001b!\u0019\t\u0019'!\u001d\u0003\u001c!A!\u0011HAD\t\u0003\u0011Y$\u0001\rd_:\u001cHO];di>\u00148/\u00118e\u0007>l\u0007/\u00198j_:,\"A!\u0010\u0011\r\u0005\r$1\u0002B !\rQ!\u0011I\u0005\u0004\u0005\u0007\u0012!!F\"p]N$(/^2u_J$Um]2sSB$xN\u001d\u0005\t\u0005\u000f\n9\t\"\u0001\u0003J\u0005a2M]3bi\u0016\u001cuN\\:ueV\u001cGo\u001c:EKN\u001c'/\u001b9u_J\u001cH\u0003\u0002B\u001f\u0005\u0017B\u0001B!\u0014\u0003F\u0001\u0007!qJ\u0001\u0004G\u000e\u001c\bCBA2\u0005#\u0012)&\u0003\u0003\u0003T\u0005U$\u0001C%uKJ\f'\r\\3\u0011\u0007)\u00119&C\u0002\u0003Z\t\u0011!\"\u0012=fGV$\u0018M\u00197f\u0011!\u0011i&a\"\u0005\u0002\t}\u0013!\u00044j]\u0012\u001cu.\u001c9b]&|g\u000e\u0006\u0003\u0002B\n\u0005\u0004b\u0002B2\u00057\u0002\raX\u0001\u0016G\",7m[\"p[B\fg.[8o\u001b\u0006\u0004\b/\u001b8h\u0011!\u00119'a\"\u0005\u0002\t%\u0014A\u0002:fgVdG/\u0006\u0002\u0003lA\u0019!B!\u001c\n\u0007\t=$AA\bDY\u0006\u001c8\u000fR3tGJL\u0007\u000f^8s\u0011)\u0011\u0019(a\"\u0012\u0002\u0013\u0005!QO\u0001\u0018GR|'\u000fU1sC6$\u0016\u0010]3%I\u00164\u0017-\u001e7uIY*\"Aa\u001e+\t\tE\"\u0011P\u0016\u0003\u0005w\u0002BA! \u0003\b6\u0011!q\u0010\u0006\u0005\u0005\u0003\u0013\u0019)A\u0005v]\u000eDWmY6fI*\u0019!Q\u0011\t\u0002\u0015\u0005tgn\u001c;bi&|g.\u0003\u0003\u0003\n\n}$!E;oG\",7m[3e-\u0006\u0014\u0018.\u00198dK\u001eI!QR\u0006\u0002\u0002#%!qR\u0001\u0017\u00072\f7o\u001d#fg\u000e\u0014\u0018\u000e\u001d;pe\n+\u0018\u000e\u001c3feB!\u0011q\u0015BI\r%\tIiCA\u0001\u0012\u0013\u0011\u0019jE\u0002\u0003\u0012:Aq!\u0006BI\t\u0003\u00119\n\u0006\u0002\u0003\u0010\"Q!1\u0014BI#\u0003%\tA!(\u00027\u0011bWm]:j]&$He\u001a:fCR,'\u000f\n3fM\u0006,H\u000e\u001e\u00133+\t\u0011yJ\u000b\u0003\u0002X\te\u0004B\u0003BR\u0005#\u000b\n\u0011\"\u0001\u0003&\u0006YB\u0005\\3tg&t\u0017\u000e\u001e\u0013he\u0016\fG/\u001a:%I\u00164\u0017-\u001e7uIM*\"Aa*+\t\t%&\u0011\u0010\t\u0007\u0003G\n\tHa+\u0011\r=\tIH!,\u000fa\u0011\u0011yKa-\u0011\tuR(\u0011\u0017\t\u0004_\tMFaCAP\u0005C\u000b\t\u0011!A\u0003\u0002IBqAa.\f\t\u0003\u0011I,\u0001\u0007eK\u001a\fW\u000f\u001c;WC2,X\r\u0006\u0006\u0003<\n%'q\u001bBn\u0005?\u0004RaDA\u0006\u0005{\u0003Ra\u0004B`\u0005\u0007L1A!1\u0011\u0005%1UO\\2uS>t\u0007\u0007E\u0002,\u0005\u000bL1Aa2$\u0005\u0019y%M[3di\"A!1\u001aB[\u0001\u0004\u0011i-A\u0005d_6\u00048\t\\1tgB\"!q\u001aBj!\u0011i$P!5\u0011\u0007=\u0012\u0019\u000eB\u0006\u0003V\n%\u0017\u0011!A\u0001\u0006\u0003\u0011$aA0%o!9!\u0011\u001cB[\u0001\u0004q\u0011aB2p[B|%M\u001b\u0005\t\u0005;\u0014)\f1\u0001\u0003\u001c\u0005A\u0011M]4J]\u0012,\u0007\u0010C\u0004\u0003b\nU\u0006\u0019\u0001\u001f\u0002\u000fA\fG\u000f^3s]\"9!Q]\u0006\u0005\u0002\t\u001d\u0018A\u0003:bo\u000ec\u0017m]:PMR!!\u0011\u001eBza\u0011\u0011YOa<\u0011\tuR(Q\u001e\t\u0004_\t=Ha\u0003By\u0005G\f\t\u0011!A\u0003\u0002I\u00121a\u0018\u00139\u0011\u0019\u0019'1\u001da\u0001A!9!q_\u0006\u0005\u0002\te\u0018\u0001D;o[\u0006tw\r\\3OC6,Gc\u0001\u001f\u0003|\"9\u0011Q\u0003B{\u0001\u0004a\u0004b\u0002B\u0000\u0017\u0011\u00051\u0011A\u0001\u0014[.\u0004\u0016M]1nKR,'/\u001b>fIRK\b/\u001a\u000b\u0007\u0007\u0007\u0019iba\b\u0013\r\r\u0015!1YB\u0005\r\u001d\u00199A!@\u0001\u0007\u0007\u0011A\u0002\u0010:fM&tW-\\3oiz\u00022!IB\u0006\u0013\r\u0019iA\t\u0002\u0012!\u0006\u0014\u0018-\\3uKJL'0\u001a3UsB,\u0007\u0002CB\t\u0007\u000b!\taa\u0005\u0002\u0015\u001d,GOU1x)f\u0004X\r\u0006\u0002\u0004\u0016A\"1qCB\u000e!\u0011YCf!\u0007\u0011\u0007=\u001aY\u0002B\u0006\u0003r\u000e=\u0011\u0011!A\u0001\u0006\u0003\u0011\u0004b\u0002B\u0012\u0005{\u0004\r\u0001\t\u0005\t\u0007C\u0011i\u00101\u0001\u0004$\u0005AA/\u001f9f\u0003J<7\u000fE\u0003\u0002d\t-\u0001\u0005C\u0005\u0004(-\t\n\u0011\"\u0001\u0004*\u0005\u0011B-Z:de&\u0014W\r\n3fM\u0006,H\u000e\u001e\u00133+\u0011\u0019Yca\f\u0016\u0005\r5\"\u0006BA\u0016\u0005s\"aa]B\u0013\u0005\u0004\u0011\u0004\"CB\u001a\u0017E\u0005I\u0011AB\u001b\u0003UI7\u000f\u0015:j[&$\u0018N^3%I\u00164\u0017-\u001e7uII*\"aa\u000e+\u0007\u0019\u0014I\bC\u0005\u0004<-\t\n\u0011\"\u0001\u0003\u001e\u0006Q2M]3bi\u0016$Um]2sSB$xN\u001d\u0013eK\u001a\fW\u000f\u001c;%e!I1qH\u0006\u0012\u0002\u0013\u00051\u0011I\u0001\u001bGJ,\u0017\r^3EKN\u001c'/\u001b9u_J$C-\u001a4bk2$HeM\u000b\u0003\u0007\u0007RCa!\u0012\u0003zA1\u00111MA9\u0007\u000f\u0002baDA=\u0007\u0013r\u0001\u0007BB&\u0007\u001f\u0002B!\u0010>\u0004NA\u0019qfa\u0014\u0005\u0017\u0005\u00155QHA\u0001\u0002\u0003\u0015\tA\r")
public final class Reflector
{
    public static List<Tuple2<Class<?>, Object>> createDescriptor$default$3() {
        return Reflector$.MODULE$.createDescriptor$default$3();
    }
    
    public static package.ParameterNameReader createDescriptor$default$2() {
        return Reflector$.MODULE$.createDescriptor$default$2();
    }
    
    public static Set<Type> isPrimitive$default$2() {
        return Reflector$.MODULE$.isPrimitive$default$2();
    }
    
    public static <T> Formats describe$default$2() {
        return Reflector$.MODULE$.describe$default$2();
    }
    
    public static Object mkParameterizedType(final Type type, final Seq<Type> seq) {
        return Reflector$.MODULE$.mkParameterizedType(type, (Seq)seq);
    }
    
    public static String unmangleName(final String name) {
        return Reflector$.MODULE$.unmangleName(name);
    }
    
    public static Class<?> rawClassOf(final Type t) {
        return Reflector$.MODULE$.rawClassOf(t);
    }
    
    public static Option<Function0<Object>> defaultValue(final Class<?> compClass, final Object compObj, final int argIndex, final String pattern) {
        return Reflector$.MODULE$.defaultValue(compClass, compObj, argIndex, pattern);
    }
    
    public static ObjectDescriptor createDescriptor(final ScalaType tpe, final package.ParameterNameReader paramNameReader, final List<Tuple2<Class<?>, Object>> companionMappings) {
        return Reflector$.MODULE$.createDescriptor(tpe, paramNameReader, companionMappings);
    }
    
    public static ObjectDescriptor describe(final package.ReflectorDescribable<?> st) {
        return Reflector$.MODULE$.describe(st);
    }
    
    public static <T> ObjectDescriptor describe(final Manifest<T> mf, final Formats formats) {
        return Reflector$.MODULE$.describe((scala.reflect.Manifest<Object>)mf, formats);
    }
    
    public static Option<ScalaType> scalaTypeOf(final String name) {
        return Reflector$.MODULE$.scalaTypeOf(name);
    }
    
    public static ScalaType scalaTypeOf(final Type t) {
        return Reflector$.MODULE$.scalaTypeOf(t);
    }
    
    public static ScalaType scalaTypeOf(final Class<?> clazz) {
        return Reflector$.MODULE$.scalaTypeOf(clazz);
    }
    
    public static <T> ScalaType scalaTypeOf(final Manifest<T> mf) {
        return Reflector$.MODULE$.scalaTypeOf((scala.reflect.Manifest<Object>)mf);
    }
    
    public static boolean isPrimitive(final Type t, final Set<Type> extra) {
        return Reflector$.MODULE$.isPrimitive(t, extra);
    }
    
    public static void clearCaches() {
        Reflector$.MODULE$.clearCaches();
    }
    
    public static class ClassDescriptorBuilder
    {
        public final ScalaType org$json4s$reflect$Reflector$ClassDescriptorBuilder$$tpe;
        public final package.ParameterNameReader org$json4s$reflect$Reflector$ClassDescriptorBuilder$$paramNameReader;
        public final List<Tuple2<Class<?>, Object>> org$json4s$reflect$Reflector$ClassDescriptorBuilder$$companionMappings;
        private Option<SingletonDescriptor> companion;
        private boolean triedCompanion;
        
        public Option<SingletonDescriptor> companion() {
            return this.companion;
        }
        
        public void companion_$eq(final Option<SingletonDescriptor> x$1) {
            this.companion = x$1;
        }
        
        public boolean triedCompanion() {
            return this.triedCompanion;
        }
        
        public void triedCompanion_$eq(final boolean x$1) {
            this.triedCompanion = x$1;
        }
        
        public List<PropertyDescriptor> fields(final Class<?> clazz) {
            final ListBuffer lb = new ListBuffer();
            final Iterator ls = (Iterator)Exception$.MODULE$.allCatch().withApply((Function1)new Reflector$ClassDescriptorBuilder$$anonfun.Reflector$ClassDescriptorBuilder$$anonfun$1(this)).apply((Function0)new Reflector$ClassDescriptorBuilder$$anonfun.Reflector$ClassDescriptorBuilder$$anonfun$2(this, (Class)clazz));
            while (ls.hasNext()) {
                final Field f = (Field)ls.next();
                final int mod = f.getModifiers();
                if (Modifier.isStatic(mod) || Modifier.isTransient(mod) || Modifier.isVolatile(mod) || f.isSynthetic()) {
                    final BoxedUnit boxedUnit = BoxedUnit.UNIT;
                }
                else {
                    final ScalaType$ module$ = ScalaType$.MODULE$;
                    final Class<?> type = f.getType();
                    final Type genericType = f.getGenericType();
                    Object module$2;
                    if (genericType instanceof ParameterizedType) {
                        module$2 = ((TraversableLike)Predef$.MODULE$.refArrayOps((Object[])((ParameterizedType)genericType).getActualTypeArguments()).toSeq().zipWithIndex(Seq$.MODULE$.canBuildFrom())).map((Function1)new Reflector$ClassDescriptorBuilder$$anonfun.Reflector$ClassDescriptorBuilder$$anonfun$3(this, (Class)clazz, f), Seq$.MODULE$.canBuildFrom());
                    }
                    else {
                        module$2 = Nil$.MODULE$;
                    }
                    final ScalaType st = module$.apply(type, (Seq<ScalaType>)module$2);
                    final String name = f.getName();
                    final String outerFieldName = ScalaSigReader$.MODULE$.OuterFieldName();
                    Label_0253: {
                        if (name == null) {
                            if (outerFieldName != null) {
                                break Label_0253;
                            }
                        }
                        else if (!name.equals(outerFieldName)) {
                            break Label_0253;
                        }
                        final BoxedUnit boxedUnit = BoxedUnit.UNIT;
                        continue;
                    }
                    final String decoded = Reflector$.MODULE$.unmangleName(f.getName());
                    f.setAccessible(true);
                    lb.$plus$eq((Object)new PropertyDescriptor(decoded, f.getName(), st, f));
                }
            }
            if (clazz.getSuperclass() == null) {
                final BoxedUnit unit = BoxedUnit.UNIT;
            }
            else {
                lb.$plus$plus$eq((TraversableOnce)this.fields(clazz.getSuperclass()));
            }
            return (List<PropertyDescriptor>)lb.toList();
        }
        
        public Seq<PropertyDescriptor> properties() {
            return (Seq<PropertyDescriptor>)this.fields(this.org$json4s$reflect$Reflector$ClassDescriptorBuilder$$tpe.erasure());
        }
        
        public ScalaType ctorParamType(final String name, final int index, final ScalaType owner, final List<String> ctorParameterNames, final Type t, final Option<Tuple2<ScalaType, List<Object>>> container) {
            final Option idxes = container.map((Function1)new Reflector$ClassDescriptorBuilder$$anonfun.Reflector$ClassDescriptorBuilder$$anonfun$4(this));
            ScalaType copy;
            if (t instanceof TypeVariable) {
                final TypeVariable typeVariable = (TypeVariable)t;
                final ScalaType a = (ScalaType)owner.typeVars().getOrElse((Object)typeVariable.getName(), (Function0)new Reflector$ClassDescriptorBuilder$$anonfun.Reflector$ClassDescriptorBuilder$$anonfun$5(this, typeVariable));
                final Class<?> erasure = a.erasure();
                final Class<Object> obj = Object.class;
                ScalaType scalaType = null;
                Label_0121: {
                    Label_0119: {
                        if (erasure == null) {
                            if (obj != null) {
                                break Label_0119;
                            }
                        }
                        else if (!erasure.equals(obj)) {
                            break Label_0119;
                        }
                        final Class r = ScalaSigReader$.MODULE$.readConstructor(name, owner, index, ctorParameterNames);
                        scalaType = Reflector$.MODULE$.scalaTypeOf(r);
                        break Label_0121;
                    }
                    scalaType = a;
                }
                copy = scalaType;
            }
            else if (t instanceof ParameterizedType) {
                final ParameterizedType t2 = (ParameterizedType)t;
                final ScalaType st = Reflector$.MODULE$.scalaTypeOf(t2);
                final List x$17;
                final List actualArgs = x$17 = (List)((List)Predef$.MODULE$.refArrayOps((Object[])t2.getActualTypeArguments()).toList().zipWithIndex(List$.MODULE$.canBuildFrom())).map((Function1)new Reflector$ClassDescriptorBuilder$$anonfun.Reflector$ClassDescriptorBuilder$$anonfun$6(this, name, index, owner, (List)ctorParameterNames, (Option)container, st), List$.MODULE$.canBuildFrom());
                final Class x$18 = st.copy$default$1();
                final Map x$19 = st.copy$default$3();
                copy = st.copy(x$18, (Seq<ScalaType>)x$17, (Map<String, ScalaType>)x$19);
            }
            else if (t instanceof WildcardType) {
                final Type[] upper = ((WildcardType)t).getUpperBounds();
                copy = ((upper != null && upper.length > 0) ? Reflector$.MODULE$.scalaTypeOf(upper[0]) : Reflector$.MODULE$.scalaTypeOf((scala.reflect.Manifest<Object>)ManifestFactory$.MODULE$.Object()));
            }
            else {
                final ScalaType st2 = Reflector$.MODULE$.scalaTypeOf(t);
                final Class<?> erasure2 = st2.erasure();
                final Class<Object> obj2 = Object.class;
                ScalaType scalaType2 = null;
                Label_0394: {
                    Label_0392: {
                        if (erasure2 == null) {
                            if (obj2 != null) {
                                break Label_0392;
                            }
                        }
                        else if (!erasure2.equals(obj2)) {
                            break Label_0392;
                        }
                        scalaType2 = Reflector$.MODULE$.scalaTypeOf(ScalaSigReader$.MODULE$.readConstructor(name, owner, (List<Object>)idxes.getOrElse((Function0)new Reflector$ClassDescriptorBuilder$$anonfun$ctorParamType.Reflector$ClassDescriptorBuilder$$anonfun$ctorParamType$1(this, index)), ctorParameterNames));
                        break Label_0394;
                    }
                    scalaType2 = st2;
                }
                copy = scalaType2;
            }
            return copy;
        }
        
        public Option<Tuple2<ScalaType, List<Object>>> ctorParamType$default$6() {
            return (Option<Tuple2<ScalaType, List<Object>>>)None$.MODULE$;
        }
        
        public Seq<ConstructorDescriptor> constructorsAndCompanion() {
            final Class er = this.org$json4s$reflect$Reflector$ClassDescriptorBuilder$$tpe.erasure();
            final Iterable ccs = (Iterable)Exception$.MODULE$.allCatch().withApply((Function1)new Reflector$ClassDescriptorBuilder$$anonfun.Reflector$ClassDescriptorBuilder$$anonfun$9(this)).apply((Function0)new Reflector$ClassDescriptorBuilder$$anonfun.Reflector$ClassDescriptorBuilder$$anonfun$10(this, er));
            final Seq constructorDescriptors = this.createConstructorDescriptors((Iterable<Executable>)ccs);
            this.companion_$eq(this.findCompanion(false));
            final Option<SingletonDescriptor> companion = this.companion();
            Method[] array;
            if (companion instanceof Some) {
                final SingletonDescriptor singletonDescriptor = (SingletonDescriptor)((Some)companion).x();
                array = (Method[])Predef$.MODULE$.refArrayOps((Object[])singletonDescriptor.instance().getClass().getMethods()).filter((Function1)new Reflector$ClassDescriptorBuilder$$anonfun.Reflector$ClassDescriptorBuilder$$anonfun$12(this, er));
            }
            else {
                if (!None$.MODULE$.equals(companion)) {
                    throw new MatchError((Object)companion);
                }
                array = (Method[])Array$.MODULE$.apply((Seq)Nil$.MODULE$, ClassTag$.MODULE$.apply((Class)Method.class));
            }
            final Method[] applyMethods = array;
            final Executable[] applyExecutables = (Executable[])Predef$.MODULE$.refArrayOps((Object[])applyMethods).map((Function1)new Reflector$ClassDescriptorBuilder$$anonfun.Reflector$ClassDescriptorBuilder$$anonfun$13(this), Array$.MODULE$.canBuildFrom(ClassTag$.MODULE$.apply((Class)Executable.class)));
            final Seq all = (Seq)constructorDescriptors.$plus$plus((GenTraversableOnce)this.createConstructorDescriptors((Iterable<Executable>)Predef$.MODULE$.wrapRefArray((Object[])applyExecutables)), Seq$.MODULE$.canBuildFrom());
            return (Seq<ConstructorDescriptor>)all.toList().sortBy((Function1)new Reflector$ClassDescriptorBuilder$$anonfun$constructorsAndCompanion.Reflector$ClassDescriptorBuilder$$anonfun$constructorsAndCompanion$1(this), Ordering$.MODULE$.Tuple4((Ordering)Ordering.Boolean$.MODULE$, (Ordering)Ordering.Boolean$.MODULE$, (Ordering)Ordering.Int$.MODULE$, (Ordering)Ordering.String$.MODULE$));
        }
        
        public Seq<ConstructorDescriptor> createConstructorDescriptors(final Iterable<Executable> ccs) {
            return (Seq<ConstructorDescriptor>)((TraversableLike)Option$.MODULE$.apply((Object)ccs).map((Function1)new Reflector$ClassDescriptorBuilder$$anonfun$createConstructorDescriptors.Reflector$ClassDescriptorBuilder$$anonfun$createConstructorDescriptors$1(this)).getOrElse((Function0)new Reflector$ClassDescriptorBuilder$$anonfun$createConstructorDescriptors.Reflector$ClassDescriptorBuilder$$anonfun$createConstructorDescriptors$2(this))).map((Function1)new Reflector$ClassDescriptorBuilder$$anonfun$createConstructorDescriptors.Reflector$ClassDescriptorBuilder$$anonfun$createConstructorDescriptors$3(this), Seq$.MODULE$.canBuildFrom());
        }
        
        public Option<SingletonDescriptor> findCompanion(final boolean checkCompanionMapping) {
            Option option;
            if (checkCompanionMapping) {
                final Option mapping = this.org$json4s$reflect$Reflector$ClassDescriptorBuilder$$companionMappings.find((Function1)new Reflector$ClassDescriptorBuilder$$anonfun.Reflector$ClassDescriptorBuilder$$anonfun$22(this)).map((Function1)new Reflector$ClassDescriptorBuilder$$anonfun.Reflector$ClassDescriptorBuilder$$anonfun$23(this));
                option = mapping.map((Function1)new Reflector$ClassDescriptorBuilder$$anonfun$findCompanion.Reflector$ClassDescriptorBuilder$$anonfun$findCompanion$2(this));
            }
            else if (this.companion().isEmpty() && !this.triedCompanion()) {
                this.triedCompanion_$eq(true);
                option = ScalaSigReader$.MODULE$.companions(this.org$json4s$reflect$Reflector$ClassDescriptorBuilder$$tpe.rawFullName(), ScalaSigReader$.MODULE$.companions$default$2(), ScalaSigReader$.MODULE$.companions$default$3()).collect((PartialFunction)new Reflector$ClassDescriptorBuilder$$anonfun$findCompanion.Reflector$ClassDescriptorBuilder$$anonfun$findCompanion$1(this));
            }
            else {
                option = this.companion();
            }
            return (Option<SingletonDescriptor>)option;
        }
        
        public ClassDescriptor result() {
            final Seq constructors = this.constructorsAndCompanion();
            return new ClassDescriptor(this.org$json4s$reflect$Reflector$ClassDescriptorBuilder$$tpe.simpleName(), this.org$json4s$reflect$Reflector$ClassDescriptorBuilder$$tpe.fullName(), this.org$json4s$reflect$Reflector$ClassDescriptorBuilder$$tpe, this.companion(), (Seq<ConstructorDescriptor>)constructors, this.properties());
        }
        
        public ClassDescriptorBuilder(final ScalaType tpe, final package.ParameterNameReader paramNameReader, final List<Tuple2<Class<?>, Object>> companionMappings) {
            this.org$json4s$reflect$Reflector$ClassDescriptorBuilder$$tpe = tpe;
            this.org$json4s$reflect$Reflector$ClassDescriptorBuilder$$paramNameReader = paramNameReader;
            this.org$json4s$reflect$Reflector$ClassDescriptorBuilder$$companionMappings = companionMappings;
            this.companion = (Option<SingletonDescriptor>)None$.MODULE$;
            this.triedCompanion = false;
        }
    }
    
    public static class ClassDescriptorBuilder$
    {
        public static final ClassDescriptorBuilder$ MODULE$;
        
        static {
            new ClassDescriptorBuilder$();
        }
        
        public package.ParameterNameReader $lessinit$greater$default$2() {
            return package.ParanamerReader$.MODULE$;
        }
        
        public List<Tuple2<Class<?>, Object>> $lessinit$greater$default$3() {
            return (List<Tuple2<Class<?>, Object>>)Nil$.MODULE$;
        }
        
        public ClassDescriptorBuilder$() {
            MODULE$ = this;
        }
    }
}
