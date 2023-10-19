// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

import scala.Some;
import scala.None$;
import scala.Option;
import scala.runtime.AbstractFunction1;
import scala.runtime.ScalaRunTime$;
import scala.Product$class;
import scala.collection.Iterator;
import scala.Serializable;
import scala.Product;
import scala.runtime.BoxedUnit;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import scala.collection.Seq;
import scala.package$;
import scala.collection.immutable.Vector$;
import scala.Predef$;
import scala.runtime.RichInt$;
import scala.collection.immutable.List;
import scala.MatchError;
import scala.math.Numeric;
import scala.Function1;
import scala.collection.immutable.List$;
import scala.collection.TraversableOnce;
import scala.Tuple3;
import scala.runtime.BoxesRunTime;
import scala.collection.immutable.Nil$;
import scala.collection.immutable.Vector;
import java.io.Reader;
import scala.collection.mutable.StringBuilder;
import java.io.Writer;
import scala.reflect.ScalaSignature;

@ScalaSignature(bytes = "\u0006\u0001\r-w!B\u0001\u0003\u0011\u00039\u0011A\u0003)beN,'/\u0016;jY*\u00111\u0001B\u0001\u0007UN|g\u000eN:\u000b\u0003\u0015\t1a\u001c:h\u0007\u0001\u0001\"\u0001C\u0005\u000e\u0003\t1QA\u0003\u0002\t\u0002-\u0011!\u0002U1sg\u0016\u0014X\u000b^5m'\tIA\u0002\u0005\u0002\u000e!5\taBC\u0001\u0010\u0003\u0015\u00198-\u00197b\u0013\t\tbB\u0001\u0004B]f\u0014VM\u001a\u0005\u0006'%!\t\u0001F\u0001\u0007y%t\u0017\u000e\u001e \u0015\u0003\u001d1AAF\u0005\u0001/\tq\u0001+\u0019:tK\u0016C8-\u001a9uS>t7CA\u000b\u0019!\tI\u0012E\u0004\u0002\u001b?9\u00111DH\u0007\u00029)\u0011QDB\u0001\u0007yI|w\u000e\u001e \n\u0003=I!\u0001\t\b\u0002\u000fA\f7m[1hK&\u0011!e\t\u0002\n\u000bb\u001cW\r\u001d;j_:T!\u0001\t\b\t\u0011\u0015*\"\u0011!Q\u0001\n\u0019\nq!\\3tg\u0006<W\r\u0005\u0002(U9\u0011Q\u0002K\u0005\u0003S9\ta\u0001\u0015:fI\u00164\u0017BA\u0016-\u0005\u0019\u0019FO]5oO*\u0011\u0011F\u0004\u0005\t]U\u0011\t\u0011)A\u00051\u0005)1-Y;tK\")1#\u0006C\u0001aQ\u0019\u0011g\r\u001b\u0011\u0005I*R\"A\u0005\t\u000b\u0015z\u0003\u0019\u0001\u0014\t\u000b9z\u0003\u0019\u0001\r\t\rYJ\u0001\u0015!\u00038\u0003\r)uJ\u0012\t\u0003\u001baJ!!\u000f\b\u0003\t\rC\u0017M\u001d\u0005\u0007w%\u0001\u000b\u0011\u0002\u001f\u0002\u0019\u0005\u001b8-[5F]\u000e|G-\u001a:\u0011\u0005u\"U\"\u0001 \u000b\u0005}\u0002\u0015aB2iCJ\u001cX\r\u001e\u0006\u0003\u0003\n\u000b1A\\5p\u0015\u0005\u0019\u0015\u0001\u00026bm\u0006L!!\u0012 \u0003\u001d\rC\u0017M]:fi\u0016s7m\u001c3fe\u001a1q)\u0003Q\u0002*!\u0013ab\u0015;sS:<\u0017\t\u001d9f]\u0012,'/\u0006\u0002J\u001fN\u0011a\t\u0004\u0005\u0006'\u0019#\ta\u0013\u000b\u0002\u0019B\u0019!GR'\u0011\u00059{E\u0002\u0001\u0003\u0006!\u001a\u0013\r!\u0015\u0002\u0002)F\u0011!+\u0016\t\u0003\u001bMK!\u0001\u0016\b\u0003\u000f9{G\u000f[5oOB\u0011QBV\u0005\u0003/:\u00111!\u00118z\u0011\u0015IfI\"\u0001[\u0003\u0019\t\u0007\u000f]3oIR\u0011Qj\u0017\u0005\u00069b\u0003\rAJ\u0001\u0002g\")aL\u0012D\u0001?\u0006!1/\u001e2k+\u0005i\u0015f\u0001$bg\u001a1!-\u0003Q\u0001\n\r\u0014Qc\u0015;sS:<')^5mI\u0016\u0014\u0018\t\u001d9f]\u0012,'o\u0005\u0002bIB\u0019!GR3\u0011\u0005e1\u0017BA4$\u00055\u0019FO]5oO\n+\u0018\u000e\u001c3fe\"Aa,\u0019BC\u0002\u0013\u0005\u0011.F\u0001f\u0011!Y\u0017M!A!\u0002\u0013)\u0017!B:vE*\u0004\u0003\"B\nb\t\u0003iGC\u00018p!\t\u0011\u0014\rC\u0003_Y\u0002\u0007Q\rC\u0003ZC\u0012\u0005\u0011\u000f\u0006\u0002fe\")A\f\u001da\u0001M\u00191A/\u0003Q\u0001\nU\u0014Ac\u0015;sS:<wK]5uKJ\f\u0005\u000f]3oI\u0016\u00148CA:w!\r\u0011di\u001e\t\u0003qnl\u0011!\u001f\u0006\u0003u\n\u000b!![8\n\u0005qL(AB,sSR,'\u000f\u0003\u0005_g\n\u0015\r\u0011\"\u0001\u007f+\u00059\b\u0002C6t\u0005\u0003\u0005\u000b\u0011B<\t\rM\u0019H\u0011AA\u0002)\u0011\t)!a\u0002\u0011\u0005I\u001a\bB\u00020\u0002\u0002\u0001\u0007q\u000f\u0003\u0004Zg\u0012\u0005\u00111\u0002\u000b\u0004o\u00065\u0001B\u0002/\u0002\n\u0001\u0007a\u0005C\u0004\u0002\u0012%!\t!a\u0005\u0002\u000bE,x\u000e^3\u0015\t\u0005U\u0011\u0011\u0005\u000b\u0004M\u0005]\u0001BCA\r\u0003\u001f\u0001\n\u0011q\u0001\u0002\u001c\u00059am\u001c:nCR\u001c\bc\u0001\u0005\u0002\u001e%\u0019\u0011q\u0004\u0002\u0003\u000f\u0019{'/\\1ug\"1A,a\u0004A\u0002\u0019B\u0001\"!\u0005\n\t\u0003\u0011\u0011Q\u0005\u000b\u0007\u0003O\tY#!\f\u0015\u0007]\fI\u0003\u0003\u0005\u0002\u001a\u0005\r\u00029AA\u000e\u0011\u0019a\u00161\u0005a\u0001M!9\u0011qFA\u0012\u0001\u00049\u0018AB<sSR,'\u000f\u0003\u0005\u0002\u0012%\u0001K\u0011BA\u001a+\u0011\t)$a\u000f\u0015\r\u0005]\u0012qHA!)\u0011\tI$!\u0010\u0011\u00079\u000bY\u0004\u0002\u0004Q\u0003c\u0011\r!\u0015\u0005\t\u00033\t\t\u0004q\u0001\u0002\u001c!1A,!\rA\u0002\u0019B\u0001\"a\u0011\u00022\u0001\u0007\u0011QI\u0001\tCB\u0004XM\u001c3feB!!GRA\u001d\u0011\u001d\tI%\u0003C\u0001\u0003\u0017\nq!\u001e8rk>$X\rF\u0002'\u0003\u001bBq!a\u0014\u0002H\u0001\u0007a%\u0001\u0004tiJLgn\u001a\u0005\t\u0003\u0013JA\u0011\u0001\u0002\u0002TQ\u0019a%!\u0016\t\u0011\u0005]\u0013\u0011\u000ba\u0001\u00033\n1AY;g!\r\u0011\u00141\f\u0004\b\u0003;J\u0001AAA0\u0005\u0019\u0011UO\u001a4feN\u0019\u00111\f\u0007\t\u0017\u0005\r\u00141\fB\u0001B\u0003%\u0011QM\u0001\u0003S:\u00042\u0001_A4\u0013\r\tI'\u001f\u0002\u0007%\u0016\fG-\u001a:\t\u0017\u00055\u00141\fB\u0001B\u0003%\u0011qN\u0001\u0013G2|7/Z!vi>l\u0017\r^5dC2d\u0017\u0010E\u0002\u000e\u0003cJ1!a\u001d\u000f\u0005\u001d\u0011un\u001c7fC:DqaEA.\t\u0003\t9\b\u0006\u0004\u0002Z\u0005e\u00141\u0010\u0005\t\u0003G\n)\b1\u0001\u0002f!A\u0011QNA;\u0001\u0004\ty\u0007\u0003\u0006\u0002\u0000\u0005m\u0003\u0019!C\u0001\u0003\u0003\u000baa\u001c4gg\u0016$XCAAB!\ri\u0011QQ\u0005\u0004\u0003\u000fs!aA%oi\"Q\u00111RA.\u0001\u0004%\t!!$\u0002\u0015=4gm]3u?\u0012*\u0017\u000f\u0006\u0003\u0002\u0010\u0006U\u0005cA\u0007\u0002\u0012&\u0019\u00111\u0013\b\u0003\tUs\u0017\u000e\u001e\u0005\u000b\u0003/\u000bI)!AA\u0002\u0005\r\u0015a\u0001=%c!I\u00111TA.A\u0003&\u00111Q\u0001\b_\u001a47/\u001a;!\u0011)\ty*a\u0017A\u0002\u0013\u0005\u0011\u0011Q\u0001\bGV\u0014X*\u0019:l\u0011)\t\u0019+a\u0017A\u0002\u0013\u0005\u0011QU\u0001\fGV\u0014X*\u0019:l?\u0012*\u0017\u000f\u0006\u0003\u0002\u0010\u0006\u001d\u0006BCAL\u0003C\u000b\t\u00111\u0001\u0002\u0004\"I\u00111VA.A\u0003&\u00111Q\u0001\tGV\u0014X*\u0019:lA!Q\u0011qVA.\u0001\u0004%\t!!!\u0002\u001d\r,(/T1sWN+w-\\3oi\"Q\u00111WA.\u0001\u0004%\t!!.\u0002%\r,(/T1sWN+w-\\3oi~#S-\u001d\u000b\u0005\u0003\u001f\u000b9\f\u0003\u0006\u0002\u0018\u0006E\u0016\u0011!a\u0001\u0003\u0007C\u0011\"a/\u0002\\\u0001\u0006K!a!\u0002\u001f\r,(/T1sWN+w-\\3oi\u0002B!\"a0\u0002\\\u0001\u0007I\u0011AAa\u00031)wNZ%t\r\u0006LG.\u001e:f+\t\ty\u0007\u0003\u0006\u0002F\u0006m\u0003\u0019!C\u0001\u0003\u000f\f\u0001#Z8g\u0013N4\u0015-\u001b7ve\u0016|F%Z9\u0015\t\u0005=\u0015\u0011\u001a\u0005\u000b\u0003/\u000b\u0019-!AA\u0002\u0005=\u0004\"CAg\u00037\u0002\u000b\u0015BA8\u00035)wNZ%t\r\u0006LG.\u001e:fA!I\u0011\u0011[A.A\u0003&\u00111[\u0001\tg\u0016<W.\u001a8ugB)\u0011$!6\u0002Z&\u0019\u0011q[\u0012\u0003\rY+7\r^8s!\r\u0011\u00141\u001c\u0004\b\u0003;L\u0011\u0011EAp\u0005\u001d\u0019VmZ7f]R\u001cr!a7\r\u0003C\f9\u000fE\u0002\u000e\u0003GL1!!:\u000f\u0005\u001d\u0001&o\u001c3vGR\u00042!DAu\u0013\r\tYO\u0004\u0002\r'\u0016\u0014\u0018.\u00197ju\u0006\u0014G.\u001a\u0005\b'\u0005mG\u0011AAx)\t\tI\u000e\u0003\u0006\u0002t\u0006m'\u0019!D\u0001\u0003k\f1a]3h+\t\t9\u0010\u0005\u0003\u000e\u0003s<\u0014bAA~\u001d\t)\u0011I\u001d:bs&2\u00111\\A\u0000\u0005\u007f2aA!\u0001\n\u0001\n\r!!\u0005#jgB|7/\u00192mKN+w-\\3oiNA\u0011q`Am\u0003C\f9\u000fC\u0006\u0002t\u0006}(Q3A\u0005\u0002\u0005U\bb\u0003B\u0005\u0003\u007f\u0014\t\u0012)A\u0005\u0003o\fAa]3hA!91#a@\u0005\u0002\t5A\u0003\u0002B\b\u0005#\u00012AMA\u0000\u0011!\t\u0019Pa\u0003A\u0002\u0005]\bB\u0003B\u000b\u0003\u007f\f\t\u0011\"\u0001\u0003\u0018\u0005!1m\u001c9z)\u0011\u0011yA!\u0007\t\u0015\u0005M(1\u0003I\u0001\u0002\u0004\t9\u0010\u0003\u0006\u0003\u001e\u0005}\u0018\u0013!C\u0001\u0005?\tabY8qs\u0012\"WMZ1vYR$\u0013'\u0006\u0002\u0003\")\"\u0011q\u001fB\u0012W\t\u0011)\u0003\u0005\u0003\u0003(\tERB\u0001B\u0015\u0015\u0011\u0011YC!\f\u0002\u0013Ut7\r[3dW\u0016$'b\u0001B\u0018\u001d\u0005Q\u0011M\u001c8pi\u0006$\u0018n\u001c8\n\t\tM\"\u0011\u0006\u0002\u0012k:\u001c\u0007.Z2lK\u00124\u0016M]5b]\u000e,\u0007B\u0003B\u001c\u0003\u007f\f\t\u0011\"\u0011\u0003:\u0005i\u0001O]8ek\u000e$\bK]3gSb,\"Aa\u000f\u0011\t\tu\"1I\u0007\u0003\u0005\u007fQ1A!\u0011C\u0003\u0011a\u0017M\\4\n\u0007-\u0012y\u0004\u0003\u0006\u0003H\u0005}\u0018\u0011!C\u0001\u0003\u0003\u000bA\u0002\u001d:pIV\u001cG/\u0011:jifD!Ba\u0013\u0002\u0000\u0006\u0005I\u0011\u0001B'\u00039\u0001(o\u001c3vGR,E.Z7f]R$2!\u0016B(\u0011)\t9J!\u0013\u0002\u0002\u0003\u0007\u00111\u0011\u0005\u000b\u0005'\ny0!A\u0005B\tU\u0013a\u00049s_\u0012,8\r^%uKJ\fGo\u001c:\u0016\u0005\t]\u0003#\u0002B-\u0005?*VB\u0001B.\u0015\r\u0011iFD\u0001\u000bG>dG.Z2uS>t\u0017\u0002\u0002B1\u00057\u0012\u0001\"\u0013;fe\u0006$xN\u001d\u0005\u000b\u0005K\ny0!A\u0005\u0002\t\u001d\u0014\u0001C2b]\u0016\u000bX/\u00197\u0015\t\u0005=$\u0011\u000e\u0005\n\u0003/\u0013\u0019'!AA\u0002UC!B!\u001c\u0002\u0000\u0006\u0005I\u0011\tB8\u0003!A\u0017m\u001d5D_\u0012,GCAAB\u0011)\u0011\u0019(a@\u0002\u0002\u0013\u0005#QO\u0001\ti>\u001cFO]5oOR\u0011!1\b\u0005\u000b\u0005s\ny0!A\u0005B\tm\u0014AB3rk\u0006d7\u000f\u0006\u0003\u0002p\tu\u0004\"CAL\u0005o\n\t\u00111\u0001V\r\u0019\u0011\t)\u0003!\u0003\u0004\ny!+Z2zG2,GmU3h[\u0016tGo\u0005\u0005\u0003\u0000\u0005e\u0017\u0011]At\u0011-\t\u0019Pa \u0003\u0016\u0004%\t!!>\t\u0017\t%!q\u0010B\tB\u0003%\u0011q\u001f\u0005\b'\t}D\u0011\u0001BF)\u0011\u0011iIa$\u0011\u0007I\u0012y\b\u0003\u0005\u0002t\n%\u0005\u0019AA|\u0011)\u0011)Ba \u0002\u0002\u0013\u0005!1\u0013\u000b\u0005\u0005\u001b\u0013)\n\u0003\u0006\u0002t\nE\u0005\u0013!a\u0001\u0003oD!B!\b\u0003\u0000E\u0005I\u0011\u0001B\u0010\u0011)\u00119Da \u0002\u0002\u0013\u0005#\u0011\b\u0005\u000b\u0005\u000f\u0012y(!A\u0005\u0002\u0005\u0005\u0005B\u0003B&\u0005\u007f\n\t\u0011\"\u0001\u0003 R\u0019QK!)\t\u0015\u0005]%QTA\u0001\u0002\u0004\t\u0019\t\u0003\u0006\u0003T\t}\u0014\u0011!C!\u0005+B!B!\u001a\u0003\u0000\u0005\u0005I\u0011\u0001BT)\u0011\tyG!+\t\u0013\u0005]%QUA\u0001\u0002\u0004)\u0006B\u0003B7\u0005\u007f\n\t\u0011\"\u0011\u0003p!Q!1\u000fB@\u0003\u0003%\tE!\u001e\t\u0015\te$qPA\u0001\n\u0003\u0012\t\f\u0006\u0003\u0002p\tM\u0006\"CAL\u0005_\u000b\t\u00111\u0001V\u0011%\u00119,a\u0017!B\u0013\t90A\u0004tK\u001elWM\u001c;\t\u0013\tm\u00161\fQ!\n\u0005\r\u0015aA2ve\"I!qXA.A\u0003&\u00111Q\u0001\u000eGV\u00148+Z4nK:$\u0018\n\u001a=\t\u0011\t\r\u00171\fC\u0001\u0005\u000b\fA!\\1sWR\u0011\u0011q\u0012\u0005\t\u0005\u0013\fY\u0006\"\u0001\u0003F\u0006!!-Y2l\u0011!\u0011i-a\u0017\u0005\u0002\t=\u0017\u0001\u00028fqR,\u0012a\u000e\u0005\t\u0005'\fY\u0006\"\u0001\u0003:\u0005I1/\u001e2tiJLgn\u001a\u0005\t\u0005/\fY\u0006\"\u0001\u0003:\u0005!a.Z1s\u0011!\u0011Y.a\u0017\u0005\u0002\t\u0015\u0017a\u0002:fY\u0016\f7/\u001a\u0005\n\u0005?\fY\u0006\"\u0001\u0003\u0005\u000b\fa\"Y;u_6\fG/[2DY>\u001cX\rC\u0005\u0003d\u0006m\u0003\u0015\"\u0003\u0002\u0002\u0006!!/Z1e\u000f!\u00119/\u0003E\u0001\u0005\t%\u0018\u0001C*fO6,g\u000e^:\u0011\u0007I\u0012YO\u0002\u0005\u0003n&A\tA\u0001Bx\u0005!\u0019VmZ7f]R\u001c8c\u0001Bv\u0019!91Ca;\u0005\u0002\tMHC\u0001Bu\u0011-\u00119Pa;A\u0002\u0013\u0005!!!!\u0002\u0017M,w-\\3oiNK'0\u001a\u0005\f\u0005w\u0014Y\u000f1A\u0005\u0002\t\u0011i0A\btK\u001elWM\u001c;TSj,w\fJ3r)\u0011\tyIa@\t\u0015\u0005]%\u0011`A\u0001\u0002\u0004\t\u0019\tC\u0005\u0004\u0004\t-\b\u0015)\u0003\u0002\u0004\u0006a1/Z4nK:$8+\u001b>fA!I1q\u0001BvA\u0003%\u00111Q\u0001\u0011[\u0006Dh*^7PMN+w-\\3oiND\u0011ba\u0003\u0003l\u0002\u0006Ka!\u0004\u0002\u0019M,w-\\3oi\u000e{WO\u001c;\u0011\t\r=1QD\u0007\u0003\u0007#QAaa\u0005\u0004\u0016\u00051\u0011\r^8nS\u000eTAaa\u0006\u0004\u001a\u0005Q1m\u001c8dkJ\u0014XM\u001c;\u000b\u0007\rm!)\u0001\u0003vi&d\u0017\u0002BB\u0010\u0007#\u0011Q\"\u0011;p[&\u001c\u0017J\u001c;fO\u0016\u0014\b\"CAi\u0005W\u0004\u000b\u0011BB\u0012!\u0019\u0019)ca\n\u0002Z6\u00111QC\u0005\u0005\u0007S\u0019)B\u0001\nBeJ\f\u0017P\u00117pG.LgnZ)vKV,\u0007\"CB\u0017\u0005W$\tA\u0001Bc\u0003\u0015\u0019G.Z1s\u0011!\u0019\tDa;\u0005\u0002\u0005=\u0018!B1qa2L\b\"CB\u001b\u0005W\u0004K\u0011BB\u001c\u0003\u001d\t7-];je\u0016,\"!!7\t\u0011\tm'1\u001eC\u0001\u0007w!Ba!\u0010\u0004DA\u0019Qba\u0010\n\u0007\r\u0005cB\u0001\u0004B]f4\u0016\r\u001c\u0005\b9\u000ee\u0002\u0019AAm\u000f%\u00199%CA\u0001\u0012\u0003\u0019I%A\bSK\u000eL8\r\\3e'\u0016<W.\u001a8u!\r\u001141\n\u0004\n\u0005\u0003K\u0011\u0011!E\u0001\u0007\u001b\u001abaa\u0013\u0004P\u0005\u001d\b\u0003CB)\u0007/\n9P!$\u000e\u0005\rM#bAB+\u001d\u00059!/\u001e8uS6,\u0017\u0002BB-\u0007'\u0012\u0011#\u00112tiJ\f7\r\u001e$v]\u000e$\u0018n\u001c82\u0011\u001d\u001921\nC\u0001\u0007;\"\"a!\u0013\t\u0015\tM41JA\u0001\n\u000b\u0012)\b\u0003\u0006\u00042\r-\u0013\u0011!CA\u0007G\"BA!$\u0004f!A\u00111_B1\u0001\u0004\t9\u0010\u0003\u0006\u0004j\r-\u0013\u0011!CA\u0007W\nq!\u001e8baBd\u0017\u0010\u0006\u0003\u0004n\rM\u0004#B\u0007\u0004p\u0005]\u0018bAB9\u001d\t1q\n\u001d;j_:D!b!\u001e\u0004h\u0005\u0005\t\u0019\u0001BG\u0003\rAH\u0005\r\u0005\u000b\u0007s\u001aY%!A\u0005\n\rm\u0014a\u0003:fC\u0012\u0014Vm]8mm\u0016$\"a! \u0011\t\tu2qP\u0005\u0005\u0007\u0003\u0013yD\u0001\u0004PE*,7\r^\u0004\n\u0007\u000bK\u0011\u0011!E\u0001\u0007\u000f\u000b\u0011\u0003R5ta>\u001c\u0018M\u00197f'\u0016<W.\u001a8u!\r\u00114\u0011\u0012\u0004\n\u0005\u0003I\u0011\u0011!E\u0001\u0007\u0017\u001bba!#\u0004\u000e\u0006\u001d\b\u0003CB)\u0007/\n9Pa\u0004\t\u000fM\u0019I\t\"\u0001\u0004\u0012R\u00111q\u0011\u0005\u000b\u0005g\u001aI)!A\u0005F\tU\u0004BCB\u0019\u0007\u0013\u000b\t\u0011\"!\u0004\u0018R!!qBBM\u0011!\t\u0019p!&A\u0002\u0005]\bBCB5\u0007\u0013\u000b\t\u0011\"!\u0004\u001eR!1QNBP\u0011)\u0019)ha'\u0002\u0002\u0003\u0007!q\u0002\u0005\u000b\u0007s\u001aI)!A\u0005\n\rm\u0004\u0002CBS\u0013\u0001\u0006Iaa*\u0002\u0019\t\u0013xn[3o\t>,(\r\\3\u0011\t\r%6qV\u0007\u0003\u0007WS1a!,\u000f\u0003\u0011i\u0017\r\u001e5\n\t\rE61\u0016\u0002\u000b\u0005&<G)Z2j[\u0006d\u0007\u0002CB[\u0013\u0011\u0005!aa.\u0002\u0017A\f'o]3E_V\u0014G.\u001a\u000b\u0005\u0007s\u001by\fE\u0002\u000e\u0007wK1a!0\u000f\u0005\u0019!u.\u001e2mK\"1Ala-A\u0002\u0019B\u0011ba1\n#\u0003%\ta!2\u0002\u001fE,x\u000e^3%I\u00164\u0017-\u001e7uII\"Baa2\u0004J*\"\u00111\u0004B\u0012\u0011\u0019a6\u0011\u0019a\u0001M\u0001")
public final class ParserUtil
{
    public static Formats quote$default$2(final String s) {
        return ParserUtil$.MODULE$.quote$default$2(s);
    }
    
    public static String unquote(final String string) {
        return ParserUtil$.MODULE$.unquote(string);
    }
    
    public static String quote(final String s, final Formats formats) {
        return ParserUtil$.MODULE$.quote(s, formats);
    }
    
    public static class ParseException extends Exception
    {
        public ParseException(final String message, final Exception cause) {
            super(message, cause);
        }
    }
    
    public abstract static class StringAppender<T>
    {
        public abstract T append(final String p0);
        
        public abstract T subj();
    }
    
    public static class StringWriterAppender extends StringAppender<Writer>
    {
        private final Writer subj;
        
        @Override
        public Writer subj() {
            return this.subj;
        }
        
        @Override
        public Writer append(final String s) {
            return this.subj().append((CharSequence)s);
        }
        
        public StringWriterAppender(final Writer subj) {
            this.subj = subj;
        }
    }
    
    public static class StringBuilderAppender extends StringAppender<StringBuilder>
    {
        private final StringBuilder subj;
        
        @Override
        public StringBuilder subj() {
            return this.subj;
        }
        
        @Override
        public StringBuilder append(final String s) {
            return this.subj().append(s);
        }
        
        public StringBuilderAppender(final StringBuilder subj) {
            this.subj = subj;
        }
    }
    
    public static class Buffer
    {
        private final Reader in;
        private final boolean closeAutomatically;
        private int offset;
        private int curMark;
        private int curMarkSegment;
        private boolean eofIsFailure;
        private Vector<Segment> segments;
        private char[] segment;
        private int cur;
        private int curSegmentIdx;
        
        public int offset() {
            return this.offset;
        }
        
        public void offset_$eq(final int x$1) {
            this.offset = x$1;
        }
        
        public int curMark() {
            return this.curMark;
        }
        
        public void curMark_$eq(final int x$1) {
            this.curMark = x$1;
        }
        
        public int curMarkSegment() {
            return this.curMarkSegment;
        }
        
        public void curMarkSegment_$eq(final int x$1) {
            this.curMarkSegment = x$1;
        }
        
        public boolean eofIsFailure() {
            return this.eofIsFailure;
        }
        
        public void eofIsFailure_$eq(final boolean x$1) {
            this.eofIsFailure = x$1;
        }
        
        public void mark() {
            this.curMark_$eq(this.cur);
            this.curMarkSegment_$eq(this.curSegmentIdx);
        }
        
        public void back() {
            --this.cur;
        }
        
        public char next() {
            char org$json4s$ParserUtil$$EOF;
            if (this.cur == this.offset() && this.read() < 0) {
                if (this.eofIsFailure()) {
                    throw new ParseException("unexpected eof", null);
                }
                org$json4s$ParserUtil$$EOF = ParserUtil$.MODULE$.org$json4s$ParserUtil$$EOF;
            }
            else {
                final char c = this.segment[this.cur];
                ++this.cur;
                org$json4s$ParserUtil$$EOF = c;
            }
            return org$json4s$ParserUtil$$EOF;
        }
        
        public String substring() {
            String s2;
            if (this.curSegmentIdx == this.curMarkSegment()) {
                s2 = new String(this.segment, this.curMark(), this.cur - this.curMark() - 1);
            }
            else {
                List parts = (List)Nil$.MODULE$;
                for (int i = this.curSegmentIdx; i >= this.curMarkSegment(); --i) {
                    final char[] s = ((Segment)this.segments.apply(i)).seg();
                    final int start = (i == this.curMarkSegment()) ? this.curMark() : 0;
                    final int end = (i == this.curSegmentIdx) ? this.cur : (s.length + 1);
                    parts = parts.$colon$colon((Object)new Tuple3((Object)BoxesRunTime.boxToInteger(start), (Object)BoxesRunTime.boxToInteger(end), (Object)s));
                }
                final int len = BoxesRunTime.unboxToInt(((TraversableOnce)parts.map((Function1)new ParserUtil$Buffer$$anonfun.ParserUtil$Buffer$$anonfun$1(this), List$.MODULE$.canBuildFrom())).sum((Numeric)Numeric.IntIsIntegral$.MODULE$));
                final char[] chars = new char[len];
                int i = 0;
                int pos = 0;
                while (i < parts.size()) {
                    final Tuple3 tuple3 = (Tuple3)parts.apply(i);
                    if (tuple3 == null) {
                        throw new MatchError((Object)tuple3);
                    }
                    final int start2 = BoxesRunTime.unboxToInt(tuple3._1());
                    final int end2 = BoxesRunTime.unboxToInt(tuple3._2());
                    final char[] b = (char[])tuple3._3();
                    final Tuple3 tuple4 = new Tuple3((Object)BoxesRunTime.boxToInteger(start2), (Object)BoxesRunTime.boxToInteger(end2), (Object)b);
                    final int start3 = BoxesRunTime.unboxToInt(tuple4._1());
                    final int end3 = BoxesRunTime.unboxToInt(tuple4._2());
                    final char[] b2 = (char[])tuple4._3();
                    final int partLen = end3 - start3 - 1;
                    System.arraycopy(b2, start3, chars, pos, partLen);
                    pos += partLen;
                    ++i;
                }
                s2 = new String(chars);
            }
            return s2;
        }
        
        public String near() {
            return new String(this.segment, RichInt$.MODULE$.max$extension(Predef$.MODULE$.intWrapper(this.cur - 20), 0), RichInt$.MODULE$.min$extension(Predef$.MODULE$.intWrapper(20), this.cur));
        }
        
        public void release() {
            this.segments.foreach((Function1)new ParserUtil$Buffer$$anonfun$release.ParserUtil$Buffer$$anonfun$release$1(this));
        }
        
        public void automaticClose() {
            if (this.closeAutomatically) {
                this.in.close();
            }
        }
        
        private int read() {
            if (this.offset() >= this.segment.length) {
                final Segment newSegment = Segments$.MODULE$.apply();
                this.offset_$eq(0);
                this.segment = newSegment.seg();
                this.segments = (Vector<Segment>)this.segments.$colon$plus((Object)newSegment, Vector$.MODULE$.canBuildFrom());
                this.curSegmentIdx = this.segments.length() - 1;
            }
            final int length = this.in.read(this.segment, this.offset(), this.segment.length - this.offset());
            this.cur = this.offset();
            this.offset_$eq(this.offset() + length);
            return length;
        }
        
        public Buffer(final Reader in, final boolean closeAutomatically) {
            this.in = in;
            this.closeAutomatically = closeAutomatically;
            this.offset = 0;
            this.curMark = -1;
            this.curMarkSegment = -1;
            this.eofIsFailure = false;
            this.segments = (Vector<Segment>)package$.MODULE$.Vector().apply((Seq)Predef$.MODULE$.wrapRefArray((Object[])new Segment[] { Segments$.MODULE$.apply() }));
            this.segment = ((Segment)this.segments.head()).seg();
            this.cur = 0;
            this.curSegmentIdx = 0;
        }
    }
    
    public static class Segments$
    {
        public static final Segments$ MODULE$;
        private int segmentSize;
        private final int maxNumOfSegments;
        private AtomicInteger segmentCount;
        private final ArrayBlockingQueue<Segment> segments;
        
        static {
            new Segments$();
        }
        
        public int segmentSize() {
            return this.segmentSize;
        }
        
        public void segmentSize_$eq(final int x$1) {
            this.segmentSize = x$1;
        }
        
        public void clear() {
            this.segments.clear();
        }
        
        public Segment apply() {
            final Segment s = this.acquire();
            return (s == null) ? new DisposableSegment(new char[this.segmentSize()]) : s;
        }
        
        private Segment acquire() {
            final int curCount = this.segmentCount.get();
            final boolean createNew = this.segments.size() == 0 && curCount < this.maxNumOfSegments && this.segmentCount.compareAndSet(curCount, curCount + 1);
            return createNew ? new RecycledSegment(new char[this.segmentSize()]) : this.segments.poll();
        }
        
        public Object release(final Segment s) {
            Object o;
            if (s instanceof RecycledSegment) {
                o = BoxesRunTime.boxToBoolean(this.segments.offer(s));
            }
            else {
                o = BoxedUnit.UNIT;
            }
            return o;
        }
        
        public Segments$() {
            MODULE$ = this;
            this.segmentSize = 1000;
            this.maxNumOfSegments = 10000;
            this.segmentCount = new AtomicInteger(0);
            this.segments = new ArrayBlockingQueue<Segment>(this.maxNumOfSegments);
        }
    }
    
    public abstract static class Segment implements Product, Serializable
    {
        public Iterator<Object> productIterator() {
            return (Iterator<Object>)Product$class.productIterator((Product)this);
        }
        
        public String productPrefix() {
            return Product$class.productPrefix((Product)this);
        }
        
        public abstract char[] seg();
        
        public Segment() {
            Product$class.$init$((Product)this);
        }
    }
    
    public static class RecycledSegment extends Segment
    {
        private final char[] seg;
        
        @Override
        public char[] seg() {
            return this.seg;
        }
        
        public RecycledSegment copy(final char[] seg) {
            return new RecycledSegment(seg);
        }
        
        public char[] copy$default$1() {
            return this.seg();
        }
        
        @Override
        public String productPrefix() {
            return "RecycledSegment";
        }
        
        public int productArity() {
            return 1;
        }
        
        public Object productElement(final int x$1) {
            switch (x$1) {
                default: {
                    throw new IndexOutOfBoundsException(BoxesRunTime.boxToInteger(x$1).toString());
                }
                case 0: {
                    return this.seg();
                }
            }
        }
        
        @Override
        public Iterator<Object> productIterator() {
            return (Iterator<Object>)ScalaRunTime$.MODULE$.typedProductIterator((Product)this);
        }
        
        public boolean canEqual(final Object x$1) {
            return x$1 instanceof RecycledSegment;
        }
        
        @Override
        public int hashCode() {
            return ScalaRunTime$.MODULE$._hashCode((Product)this);
        }
        
        @Override
        public String toString() {
            return ScalaRunTime$.MODULE$._toString((Product)this);
        }
        
        @Override
        public boolean equals(final Object x$1) {
            if (this != x$1) {
                if (x$1 instanceof RecycledSegment) {
                    final RecycledSegment recycledSegment = (RecycledSegment)x$1;
                    if (this.seg() == recycledSegment.seg() && recycledSegment.canEqual(this)) {
                        return true;
                    }
                }
                return false;
            }
            return true;
        }
        
        public RecycledSegment(final char[] seg) {
            this.seg = seg;
        }
    }
    
    public static class RecycledSegment$ extends AbstractFunction1<char[], RecycledSegment> implements Serializable
    {
        public static final RecycledSegment$ MODULE$;
        
        static {
            new RecycledSegment$();
        }
        
        public final String toString() {
            return "RecycledSegment";
        }
        
        public RecycledSegment apply(final char[] seg) {
            return new RecycledSegment(seg);
        }
        
        public Option<char[]> unapply(final RecycledSegment x$0) {
            return (Option<char[]>)((x$0 == null) ? None$.MODULE$ : new Some((Object)x$0.seg()));
        }
        
        private Object readResolve() {
            return RecycledSegment$.MODULE$;
        }
        
        public RecycledSegment$() {
            MODULE$ = this;
        }
    }
    
    public static class DisposableSegment extends Segment
    {
        private final char[] seg;
        
        @Override
        public char[] seg() {
            return this.seg;
        }
        
        public DisposableSegment copy(final char[] seg) {
            return new DisposableSegment(seg);
        }
        
        public char[] copy$default$1() {
            return this.seg();
        }
        
        @Override
        public String productPrefix() {
            return "DisposableSegment";
        }
        
        public int productArity() {
            return 1;
        }
        
        public Object productElement(final int x$1) {
            switch (x$1) {
                default: {
                    throw new IndexOutOfBoundsException(BoxesRunTime.boxToInteger(x$1).toString());
                }
                case 0: {
                    return this.seg();
                }
            }
        }
        
        @Override
        public Iterator<Object> productIterator() {
            return (Iterator<Object>)ScalaRunTime$.MODULE$.typedProductIterator((Product)this);
        }
        
        public boolean canEqual(final Object x$1) {
            return x$1 instanceof DisposableSegment;
        }
        
        @Override
        public int hashCode() {
            return ScalaRunTime$.MODULE$._hashCode((Product)this);
        }
        
        @Override
        public String toString() {
            return ScalaRunTime$.MODULE$._toString((Product)this);
        }
        
        @Override
        public boolean equals(final Object x$1) {
            if (this != x$1) {
                if (x$1 instanceof DisposableSegment) {
                    final DisposableSegment disposableSegment = (DisposableSegment)x$1;
                    if (this.seg() == disposableSegment.seg() && disposableSegment.canEqual(this)) {
                        return true;
                    }
                }
                return false;
            }
            return true;
        }
        
        public DisposableSegment(final char[] seg) {
            this.seg = seg;
        }
    }
    
    public static class DisposableSegment$ extends AbstractFunction1<char[], DisposableSegment> implements Serializable
    {
        public static final DisposableSegment$ MODULE$;
        
        static {
            new DisposableSegment$();
        }
        
        public final String toString() {
            return "DisposableSegment";
        }
        
        public DisposableSegment apply(final char[] seg) {
            return new DisposableSegment(seg);
        }
        
        public Option<char[]> unapply(final DisposableSegment x$0) {
            return (Option<char[]>)((x$0 == null) ? None$.MODULE$ : new Some((Object)x$0.seg()));
        }
        
        private Object readResolve() {
            return DisposableSegment$.MODULE$;
        }
        
        public DisposableSegment$() {
            MODULE$ = this;
        }
    }
}
