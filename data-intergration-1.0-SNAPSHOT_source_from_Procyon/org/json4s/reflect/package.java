// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.reflect;

import scala.collection.immutable.List;
import scala.Predef$;
import scala.collection.Seq;
import scala.Some;
import scala.None$;
import scala.Tuple2;
import scala.runtime.AbstractFunction2;
import scala.Product$class;
import scala.runtime.ScalaRunTime$;
import scala.collection.Iterator;
import scala.runtime.BoxesRunTime;
import java.lang.reflect.ParameterizedType;
import scala.Option;
import scala.Serializable;
import scala.Product;
import scala.Function1;
import java.util.concurrent.ConcurrentHashMap;
import scala.runtime.Nothing$;
import org.json4s.Formats;
import scala.reflect.ScalaSignature;

@ScalaSignature(bytes = "\u0006\u0001\res!B\u0001\u0003\u0011\u0003I\u0011a\u00029bG.\fw-\u001a\u0006\u0003\u0007\u0011\tqA]3gY\u0016\u001cGO\u0003\u0002\u0006\r\u00051!n]8oiMT\u0011aB\u0001\u0004_J<7\u0001\u0001\t\u0003\u0015-i\u0011A\u0001\u0004\u0006\u0019\tA\t!\u0004\u0002\ba\u0006\u001c7.Y4f'\tYa\u0002\u0005\u0002\u0010%5\t\u0001CC\u0001\u0012\u0003\u0015\u00198-\u00197b\u0013\t\u0019\u0002C\u0001\u0004B]f\u0014VM\u001a\u0005\u0006+-!\tAF\u0001\u0007y%t\u0017\u000e\u001e \u0015\u0003%AQ\u0001G\u0006\u0005\u0002e\tab]1gKNKW\u000e\u001d7f\u001d\u0006lW\r\u0006\u0002\u001bEA\u00111\u0004I\u0007\u00029)\u0011QDH\u0001\u0005Y\u0006twMC\u0001 \u0003\u0011Q\u0017M^1\n\u0005\u0005b\"AB*ue&tw\rC\u0003$/\u0001\u0007A%A\u0003dY\u0006T(\u0010\r\u0002&]A\u0019a%\u000b\u0017\u000f\u0005=9\u0013B\u0001\u0015\u0011\u0003\u0019\u0001&/\u001a3fM&\u0011!f\u000b\u0002\u0006\u00072\f7o\u001d\u0006\u0003QA\u0001\"!\f\u0018\r\u0001\u0011IqFIA\u0001\u0002\u0003\u0015\t\u0001\r\u0002\u0004?\u0012\n\u0014CA\u00195!\ty!'\u0003\u00024!\t9aj\u001c;iS:<\u0007CA\b6\u0013\t1\u0004CA\u0002B]fDQ\u0001O\u0006\u0005\u0002e\n1b\u001d;sSB$u\u000e\u001c7beR\u0011!\b\u0010\t\u0003MmJ!!I\u0016\t\u000bu:\u0004\u0019\u0001\u001e\u0002\t9\fW.\u001a\u0004\u0006\u007f-\u0001!\u0001\u0011\u0002\u0005\u001b\u0016lw.F\u0002B\u000f*\u001b\"A\u0010\b\t\u000bUqD\u0011A\"\u0015\u0003\u0011\u0003B!\u0012 G\u00136\t1\u0002\u0005\u0002.\u000f\u0012)\u0001J\u0010b\u0001a\t\t\u0011\t\u0005\u0002.\u0015\u0012)1J\u0010b\u0001a\t\t!\u000b\u0003\u0004N}\u0001\u0006IAT\u0001\u0006G\u0006\u001c\u0007.\u001a\t\u0005\u001fR3\u0015*D\u0001Q\u0015\t\t&+\u0001\u0006d_:\u001cWO\u001d:f]RT!a\u0015\u0010\u0002\tU$\u0018\u000e\\\u0005\u0003+B\u0013\u0011cQ8oGV\u0014(/\u001a8u\u0011\u0006\u001c\b.T1q\u0011\u00159f\b\"\u0001Y\u0003\u0015\t\u0007\u000f\u001d7z)\rI\u0015l\u0017\u0005\u00065Z\u0003\rAR\u0001\u0002q\")AL\u0016a\u0001;\u0006\ta\r\u0005\u0003\u0010=\u001aK\u0015BA0\u0011\u0005%1UO\\2uS>t\u0017\u0007C\u0003b}\u0011\u0005!-A\u0004sKBd\u0017mY3\u0015\u0007%\u001bG\rC\u0003[A\u0002\u0007a\tC\u0003fA\u0002\u0007\u0011*A\u0001w\u0011\u00159g\b\"\u0001i\u0003\u0015\u0019G.Z1s)\u0005I\u0007CA\bk\u0013\tY\u0007C\u0001\u0003V]&$\b\u0002C7\f\u0005\u0004%\tA\u00018\u0002=\r{gn\u001d;sk\u000e$xN\u001d#fM\u0006,H\u000e\u001e,bYV,\u0007+\u0019;uKJtW#\u0001\u000e\t\rA\\\u0001\u0015!\u0003\u001b\u0003}\u0019uN\\:ueV\u001cGo\u001c:EK\u001a\fW\u000f\u001c;WC2,X\rU1ui\u0016\u0014h\u000e\t\u0005\te.\u0011\r\u0011\"\u0001\u0003]\u0006yQj\u001c3vY\u00164\u0015.\u001a7e\u001d\u0006lW\r\u0003\u0004u\u0017\u0001\u0006IAG\u0001\u0011\u001b>$W\u000f\\3GS\u0016dGMT1nK\u0002B\u0001B^\u0006C\u0002\u0013\u0005!a^\u0001\r\u00072\f7o\u001d'pC\u0012,'o]\u000b\u0002qB!\u0011P`A\u0001\u001b\u0005Q(BA>}\u0003%IW.\\;uC\ndWM\u0003\u0002~!\u0005Q1m\u001c7mK\u000e$\u0018n\u001c8\n\u0005}T(A\u0002,fGR|'\u000fE\u0002\u001c\u0003\u0007I1!!\u0002\u001d\u0005-\u0019E.Y:t\u0019>\fG-\u001a:\t\u000f\u0005%1\u0002)A\u0005q\u0006i1\t\\1tg2{\u0017\rZ3sg\u0002B\u0001\"!\u0004\fA\u0003%\u0011qB\u0001\na\u0006\u0014\u0018M\\1nKJ\u0004B!!\u0005\u0002\u001e5\u0011\u00111\u0003\u0006\u0005\u0003\u001b\t)B\u0003\u0003\u0002\u0018\u0005e\u0011\u0001\u0004;i_V<\u0007\u000e^<pe.\u001c(BAA\u000e\u0003\r\u0019w.\\\u0005\u0005\u0003?\t\u0019B\u0001\tDC\u000eD\u0017N\\4QCJ\fg.Y7fe\u001a1\u00111E\u0006A\u0003K\u0011\u0001\u0002V=qK&sgm\\\n\b\u0003Cq\u0011qEA\u0017!\ry\u0011\u0011F\u0005\u0004\u0003W\u0001\"a\u0002)s_\u0012,8\r\u001e\t\u0004\u001f\u0005=\u0012bAA\u0019!\ta1+\u001a:jC2L'0\u00192mK\"Q1%!\t\u0003\u0016\u0004%\t!!\u000e\u0016\u0005\u0005]\u0002\u0007BA\u001d\u0003{\u0001BAJ\u0015\u0002<A\u0019Q&!\u0010\u0005\u0017\u0005}\u0012\u0011IA\u0001\u0002\u0003\u0015\t\u0001\r\u0002\u0004?\u0012\u0012\u0004bCA\"\u0003C\u0011\t\u0012)A\u0005\u0003\u000b\naa\u00197buj\u0004\u0003\u0007BA$\u0003\u0017\u0002BAJ\u0015\u0002JA\u0019Q&a\u0013\u0005\u0017\u0005}\u0012\u0011IA\u0001\u0002\u0003\u0015\t\u0001\r\u0005\f\u0003\u001f\n\tC!f\u0001\n\u0003\t\t&A\tqCJ\fW.\u001a;fe&TX\r\u001a+za\u0016,\"!a\u0015\u0011\u000b=\t)&!\u0017\n\u0007\u0005]\u0003C\u0001\u0004PaRLwN\u001c\t\u0005\u00037\ny&\u0004\u0002\u0002^)\u00111\u0001H\u0005\u0005\u0003C\niFA\tQCJ\fW.\u001a;fe&TX\r\u001a+za\u0016D1\"!\u001a\u0002\"\tE\t\u0015!\u0003\u0002T\u0005\u0011\u0002/\u0019:b[\u0016$XM]5{K\u0012$\u0016\u0010]3!\u0011\u001d)\u0012\u0011\u0005C\u0001\u0003S\"b!a\u001b\u0002n\u0005]\u0004cA#\u0002\"!91%a\u001aA\u0002\u0005=\u0004\u0007BA9\u0003k\u0002BAJ\u0015\u0002tA\u0019Q&!\u001e\u0005\u0017\u0005}\u0012QNA\u0001\u0002\u0003\u0015\t\u0001\r\u0005\t\u0003\u001f\n9\u00071\u0001\u0002T!Q\u00111PA\u0011\u0003\u0003%\t!! \u0002\t\r|\u0007/\u001f\u000b\u0007\u0003W\ny(!!\t\u0013\r\nI\b%AA\u0002\u0005=\u0004BCA(\u0003s\u0002\n\u00111\u0001\u0002T!Q\u0011QQA\u0011#\u0003%\t!a\"\u0002\u001d\r|\u0007/\u001f\u0013eK\u001a\fW\u000f\u001c;%cU\u0011\u0011\u0011\u0012\u0019\u0005\u0003\u0017\u000b\t\nE\u0003\u001c\u0003\u001b\u000by)\u0003\u0002+9A\u0019Q&!%\u0005\u0017\u0005}\u00121QA\u0001\u0002\u0003\u0015\t\u0001\r\u0005\u000b\u0003+\u000b\t#%A\u0005\u0002\u0005]\u0015AD2paf$C-\u001a4bk2$HEM\u000b\u0003\u00033SC!a\u0015\u0002\u001c.\u0012\u0011Q\u0014\t\u0005\u0003?\u000bI+\u0004\u0002\u0002\"*!\u00111UAS\u0003%)hn\u00195fG.,GMC\u0002\u0002(B\t!\"\u00198o_R\fG/[8o\u0013\u0011\tY+!)\u0003#Ut7\r[3dW\u0016$g+\u0019:jC:\u001cW\rC\u0005\u00020\u0006\u0005\u0012\u0011!C!]\u0006i\u0001O]8ek\u000e$\bK]3gSbD!\"a-\u0002\"\u0005\u0005I\u0011AA[\u00031\u0001(o\u001c3vGR\f%/\u001b;z+\t\t9\fE\u0002\u0010\u0003sK1!a/\u0011\u0005\rIe\u000e\u001e\u0005\u000b\u0003\u007f\u000b\t#!A\u0005\u0002\u0005\u0005\u0017A\u00049s_\u0012,8\r^#mK6,g\u000e\u001e\u000b\u0004i\u0005\r\u0007BCAc\u0003{\u000b\t\u00111\u0001\u00028\u0006\u0019\u0001\u0010J\u0019\t\u0015\u0005%\u0017\u0011EA\u0001\n\u0003\nY-A\bqe>$Wo\u0019;Ji\u0016\u0014\u0018\r^8s+\t\ti\rE\u0003\u0002P\u0006EG'D\u0001}\u0013\r\t\u0019\u000e \u0002\t\u0013R,'/\u0019;pe\"Q\u0011q[A\u0011\u0003\u0003%\t!!7\u0002\u0011\r\fg.R9vC2$B!a7\u0002bB\u0019q\"!8\n\u0007\u0005}\u0007CA\u0004C_>dW-\u00198\t\u0013\u0005\u0015\u0017Q[A\u0001\u0002\u0004!\u0004BCAs\u0003C\t\t\u0011\"\u0011\u0002h\u0006A\u0001.Y:i\u0007>$W\r\u0006\u0002\u00028\"Q\u00111^A\u0011\u0003\u0003%\t%!<\u0002\u0011Q|7\u000b\u001e:j]\u001e$\u0012A\u0007\u0005\u000b\u0003c\f\t#!A\u0005B\u0005M\u0018AB3rk\u0006d7\u000f\u0006\u0003\u0002\\\u0006U\b\"CAc\u0003_\f\t\u00111\u00015\u000f%\tIpCA\u0001\u0012\u0003\tY0\u0001\u0005UsB,\u0017J\u001c4p!\r)\u0015Q \u0004\n\u0003GY\u0011\u0011!E\u0001\u0003\u007f\u001cb!!@\u0003\u0002\u00055\u0002C\u0003B\u0002\u0005\u0013\u0011i!a\u0015\u0002l5\u0011!Q\u0001\u0006\u0004\u0005\u000f\u0001\u0012a\u0002:v]RLW.Z\u0005\u0005\u0005\u0017\u0011)AA\tBEN$(/Y2u\rVt7\r^5p]J\u0002DAa\u0004\u0003\u0014A!a%\u000bB\t!\ri#1\u0003\u0003\f\u0003\u007f\ti0!A\u0001\u0002\u000b\u0005\u0001\u0007C\u0004\u0016\u0003{$\tAa\u0006\u0015\u0005\u0005m\bBCAv\u0003{\f\t\u0011\"\u0012\u0002n\"Iq+!@\u0002\u0002\u0013\u0005%Q\u0004\u000b\u0007\u0003W\u0012yB!\u000b\t\u000f\r\u0012Y\u00021\u0001\u0003\"A\"!1\u0005B\u0014!\u00111\u0013F!\n\u0011\u00075\u00129\u0003B\u0006\u0002@\t}\u0011\u0011!A\u0001\u0006\u0003\u0001\u0004\u0002CA(\u00057\u0001\r!a\u0015\t\u0015\t5\u0012Q`A\u0001\n\u0003\u0013y#A\u0004v]\u0006\u0004\b\u000f\\=\u0015\t\tE\"\u0011\t\u0019\u0005\u0005g\u0011y\u0004E\u0003\u0010\u0003+\u0012)\u0004E\u0004\u0010\u0005o\u0011Y$a\u0015\n\u0007\te\u0002C\u0001\u0004UkBdWM\r\t\u00067\u00055%Q\b\t\u0004[\t}BaCA \u0005W\t\t\u0011!A\u0003\u0002AB!Ba\u0011\u0003,\u0005\u0005\t\u0019AA6\u0003\rAH\u0005\r\u0005\u000b\u0005\u000f\ni0!A\u0005\n\t%\u0013a\u0003:fC\u0012\u0014Vm]8mm\u0016$\"Aa\u0013\u0011\u0007m\u0011i%C\u0002\u0003Pq\u0011aa\u00142kK\u000e$hA\u0003B*\u0017A\u0005\u0019\u0013\u0001\u0002\u0003V\tQ1k\\;sG\u0016$\u0016\u0010]3\u0014\u0007\tEc\u0002\u0003\u0005\u0003Z\tEc\u0011\u0001B.\u0003%\u00198-\u00197b)f\u0004X-\u0006\u0002\u0003^A\u0019!Ba\u0018\n\u0007\t\u0005$AA\u0005TG\u0006d\u0017\rV=qK\u001aI!QM\u0006\u0011\u0002G\u0005!q\r\u0002\u0014!\u0006\u0014\u0018-\\3uKJt\u0015-\\3SK\u0006$WM]\n\u0004\u0005Gr\u0001\u0002\u0003B6\u0005G2\tA!\u001c\u0002)1|wn[;q!\u0006\u0014\u0018-\\3uKJt\u0015-\\3t)\u0011\u0011yG!\"\u0011\u000b\tE$q\u0010\u001e\u000f\t\tM$Q\u0010\b\u0005\u0005k\u0012Y(\u0004\u0002\u0003x)\u0019!\u0011\u0010\u0005\u0002\rq\u0012xn\u001c;?\u0013\u0005\t\u0012BA\u0001\u0011\u0013\u0011\u0011\tIa!\u0003\u0007M+\u0017O\u0003\u0002\u0002!!A!q\u0011B5\u0001\u0004\u0011I)A\u0006d_:\u001cHO];di>\u0014\bc\u0001\u0006\u0003\f&\u0019!Q\u0012\u0002\u0003\u0015\u0015CXmY;uC\ndWMB\u0005\u0003\u0012.\u0001\n1%\u0001\u0003\u0014\n!\"+\u001a4mK\u000e$xN\u001d#fg\u000e\u0014\u0018NY1cY\u0016,BA!&\u00038N\u0019!q\u0012\b\t\u0011\te%q\u0012D\u0001\u00057\u000b\u0001cY8na\u0006t\u0017n\u001c8DY\u0006\u001c8/Z:\u0016\u0005\tu\u0005C\u0002B9\u0005?\u0013\u0019+\u0003\u0003\u0003\"\n\r%\u0001\u0002'jgR\u0004ba\u0004B\u001c\u0005Ks\u0001\u0007\u0002BT\u0005W\u0003BAJ\u0015\u0003*B\u0019QFa+\u0005\u0017\t5&qSA\u0001\u0002\u0003\u0015\t\u0001\r\u0002\u0004?\u0012\u001a\u0004\u0002CA\u0007\u0005\u001f3\tA!-\u0016\u0005\tM\u0006cA#\u0003d!A!\u0011\fBH\r\u0003\u0011Y\u0006B\u0004\u0003:\n=%\u0019\u0001\u0019\u0003\u0003QCqA!0\f\t\u0007\u0011y,\u0001\u000btG\u0006d\u0017\rV=qK\u0012+7o\u0019:jE\u0006\u0014G.\u001a\u000b\u0005\u0005\u0003\u0014\t\u000e\u0006\u0003\u0003D\n\u0015\u0007#B#\u0003\u0010\nu\u0003B\u0003Bd\u0005w\u0003\n\u0011q\u0001\u0003J\u00069am\u001c:nCR\u001c\b\u0003\u0002Bf\u0005\u001bl\u0011\u0001B\u0005\u0004\u0005\u001f$!a\u0002$pe6\fGo\u001d\u0005\t\u0005'\u0014Y\f1\u0001\u0003^\u0005\tA\u000fC\u0004\u0003X.!\u0019A!7\u0002!\rd\u0017m]:EKN\u001c'/\u001b2bE2,G\u0003\u0002Bn\u0005W$BA!8\u0003jB)QIa$\u0003`B\"!\u0011\u001dBs!\u00111\u0013Fa9\u0011\u00075\u0012)\u000fB\u0006\u0003h\nU\u0017\u0011!A\u0001\u0006\u0003\u0001$aA0%m!Q!q\u0019Bk!\u0003\u0005\u001dA!3\t\u0011\tM'Q\u001ba\u0001\u0005[\u0004DAa<\u0003tB!a%\u000bBy!\ri#1\u001f\u0003\f\u0005k\u0014Y/!A\u0001\u0002\u000b\u0005\u0001GA\u0002`IUBqA!?\f\t\u0007\u0011Y0A\ttiJLgn\u001a#fg\u000e\u0014\u0018NY1cY\u0016$BA!@\u0004\u0004Q!!q`B\u0001!\u0011)%q\u0012\u001e\t\u0015\t\u001d'q\u001fI\u0001\u0002\b\u0011I\rC\u0004\u0003T\n]\b\u0019\u0001\u001e\b\u000f\r\u001d1\u0002#\u0001\u0004\n\u0005y\u0001+\u0019:b]\u0006lWM\u001d*fC\u0012,'\u000fE\u0002F\u0007\u00171qa!\u0004\f\u0011\u0003\u0019yAA\bQCJ\fg.Y7feJ+\u0017\rZ3s'\u0015\u0019YA\u0004BZ\u0011\u001d)21\u0002C\u0001\u0007'!\"a!\u0003\t\u0011\t-41\u0002C\u0001\u0007/!BAa\u001c\u0004\u001a!A!qQB\u000b\u0001\u0004\u0011I\tC\u0004\u0004\u001e-!\taa\b\u0002\t\u0019\f\u0017\u000e\u001c\u000b\u0006c\r\u00052Q\u0005\u0005\b\u0007G\u0019Y\u00021\u0001;\u0003\ri7o\u001a\u0005\u000b\u0007O\u0019Y\u0002%AA\u0002\r%\u0012!B2bkN,\u0007\u0003\u0002B9\u0007WIAa!\f\u0003\u0004\nIQ\t_2faRLwN\u001c\u0005\n\u0007cY\u0011\u0013!C\u0001\u0007g\t1d\u001d;sS:<G)Z:de&\u0014\u0017M\u00197fI\u0011,g-Y;mi\u0012\u0012D\u0003BB\u001b\u0007oQCA!3\u0002\u001c\"9!1[B\u0018\u0001\u0004Q\u0004\"CB\u001e\u0017E\u0005I\u0011AB\u001f\u0003i\u0019G.Y:t\t\u0016\u001c8M]5cC\ndW\r\n3fM\u0006,H\u000e\u001e\u00133)\u0011\u0019)da\u0010\t\u0011\tM7\u0011\ba\u0001\u0007\u0003\u0002Daa\u0011\u0004HA!a%KB#!\ri3q\t\u0003\f\u0005k\u001cy$!A\u0001\u0002\u000b\u0005\u0001\u0007C\u0005\u0004L-\t\n\u0011\"\u0001\u0004N\u0005q2oY1mCRK\b/\u001a#fg\u000e\u0014\u0018NY1cY\u0016$C-\u001a4bk2$HE\r\u000b\u0005\u0007k\u0019y\u0005\u0003\u0005\u0003T\u000e%\u0003\u0019\u0001B/\u0011%\u0019\u0019fCI\u0001\n\u0003\u0019)&\u0001\bgC&dG\u0005Z3gCVdG\u000f\n\u001a\u0016\u0005\r]#\u0006BB\u0015\u00037\u0003")
public final class package
{
    public static Exception fail$default$2() {
        return package$.MODULE$.fail$default$2();
    }
    
    public static Formats scalaTypeDescribable$default$2(final ScalaType t) {
        return package$.MODULE$.scalaTypeDescribable$default$2(t);
    }
    
    public static Formats classDescribable$default$2(final Class<?> t) {
        return package$.MODULE$.classDescribable$default$2(t);
    }
    
    public static Formats stringDescribable$default$2(final String t) {
        return package$.MODULE$.stringDescribable$default$2(t);
    }
    
    public static Nothing$ fail(final String msg, final Exception cause) {
        return package$.MODULE$.fail(msg, cause);
    }
    
    public static ReflectorDescribable<String> stringDescribable(final String t, final Formats formats) {
        return package$.MODULE$.stringDescribable(t, formats);
    }
    
    public static ReflectorDescribable<Class<?>> classDescribable(final Class<?> t, final Formats formats) {
        return package$.MODULE$.classDescribable(t, formats);
    }
    
    public static ReflectorDescribable<ScalaType> scalaTypeDescribable(final ScalaType t, final Formats formats) {
        return package$.MODULE$.scalaTypeDescribable(t, formats);
    }
    
    public static String stripDollar(final String name) {
        return package$.MODULE$.stripDollar(name);
    }
    
    public static String safeSimpleName(final Class<?> clazz) {
        return package$.MODULE$.safeSimpleName(clazz);
    }
    
    public static class Memo<A, R>
    {
        private final ConcurrentHashMap<A, R> cache;
        
        public R apply(final A x, final Function1<A, R> f) {
            Object o;
            if (this.cache.containsKey(x)) {
                o = this.cache.get(x);
            }
            else {
                final Object v = f.apply((Object)x);
                o = this.replace(x, v);
            }
            return (R)o;
        }
        
        public R replace(final A x, final R v) {
            this.cache.put(x, v);
            return v;
        }
        
        public void clear() {
            this.cache.clear();
        }
        
        public Memo() {
            this.cache = new ConcurrentHashMap<A, R>(1500, 1.0f, 1);
        }
    }
    
    public static class TypeInfo implements Product, Serializable
    {
        private final Class<?> clazz;
        private final Option<ParameterizedType> parameterizedType;
        
        public Class<?> clazz() {
            return this.clazz;
        }
        
        public Option<ParameterizedType> parameterizedType() {
            return this.parameterizedType;
        }
        
        public TypeInfo copy(final Class<?> clazz, final Option<ParameterizedType> parameterizedType) {
            return new TypeInfo(clazz, parameterizedType);
        }
        
        public Class<?> copy$default$1() {
            return this.clazz();
        }
        
        public Option<ParameterizedType> copy$default$2() {
            return this.parameterizedType();
        }
        
        public String productPrefix() {
            return "TypeInfo";
        }
        
        public int productArity() {
            return 2;
        }
        
        public Object productElement(final int x$1) {
            Object o = null;
            switch (x$1) {
                default: {
                    throw new IndexOutOfBoundsException(BoxesRunTime.boxToInteger(x$1).toString());
                }
                case 1: {
                    o = this.parameterizedType();
                    break;
                }
                case 0: {
                    o = this.clazz();
                    break;
                }
            }
            return o;
        }
        
        public Iterator<Object> productIterator() {
            return (Iterator<Object>)ScalaRunTime$.MODULE$.typedProductIterator((Product)this);
        }
        
        public boolean canEqual(final Object x$1) {
            return x$1 instanceof TypeInfo;
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
                if (x$1 instanceof TypeInfo) {
                    final TypeInfo typeInfo = (TypeInfo)x$1;
                    final Class<?> clazz = this.clazz();
                    final Class<?> clazz2 = typeInfo.clazz();
                    boolean b = false;
                    Label_0109: {
                        Label_0108: {
                            if (clazz == null) {
                                if (clazz2 != null) {
                                    break Label_0108;
                                }
                            }
                            else if (!clazz.equals(clazz2)) {
                                break Label_0108;
                            }
                            final Option<ParameterizedType> parameterizedType = this.parameterizedType();
                            final Option<ParameterizedType> parameterizedType2 = typeInfo.parameterizedType();
                            if (parameterizedType == null) {
                                if (parameterizedType2 != null) {
                                    break Label_0108;
                                }
                            }
                            else if (!parameterizedType.equals(parameterizedType2)) {
                                break Label_0108;
                            }
                            if (typeInfo.canEqual(this)) {
                                b = true;
                                break Label_0109;
                            }
                        }
                        b = false;
                    }
                    if (b) {
                        return true;
                    }
                }
                return false;
            }
            return true;
        }
        
        public TypeInfo(final Class<?> clazz, final Option<ParameterizedType> parameterizedType) {
            this.clazz = clazz;
            this.parameterizedType = parameterizedType;
            Product$class.$init$((Product)this);
        }
    }
    
    public static class TypeInfo$ extends AbstractFunction2<Class<?>, Option<ParameterizedType>, TypeInfo> implements Serializable
    {
        public static final TypeInfo$ MODULE$;
        
        static {
            new TypeInfo$();
        }
        
        public final String toString() {
            return "TypeInfo";
        }
        
        public TypeInfo apply(final Class<?> clazz, final Option<ParameterizedType> parameterizedType) {
            return new TypeInfo(clazz, parameterizedType);
        }
        
        public Option<Tuple2<Class<Object>, Option<ParameterizedType>>> unapply(final TypeInfo x$0) {
            return (Option<Tuple2<Class<Object>, Option<ParameterizedType>>>)((x$0 == null) ? None$.MODULE$ : new Some((Object)new Tuple2((Object)x$0.clazz(), (Object)x$0.parameterizedType())));
        }
        
        private Object readResolve() {
            return TypeInfo$.MODULE$;
        }
        
        public TypeInfo$() {
            MODULE$ = this;
        }
    }
    
    public static class ParanamerReader$ implements ParameterNameReader
    {
        public static final ParanamerReader$ MODULE$;
        
        static {
            new ParanamerReader$();
        }
        
        @Override
        public Seq<String> lookupParameterNames(final Executable constructor) {
            return (Seq<String>)Predef$.MODULE$.refArrayOps((Object[])package$.MODULE$.org$json4s$reflect$package$$paranamer.lookupParameterNames(constructor.getAsAccessibleObject())).toSeq();
        }
        
        public ParanamerReader$() {
            MODULE$ = this;
        }
    }
    
    public interface SourceType
    {
        ScalaType scalaType();
    }
    
    public interface ParameterNameReader
    {
        Seq<String> lookupParameterNames(final Executable p0);
    }
    
    public interface ReflectorDescribable<T>
    {
        List<Tuple2<Class<?>, Object>> companionClasses();
        
        ParameterNameReader paranamer();
        
        ScalaType scalaType();
    }
}
