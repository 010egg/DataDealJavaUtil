// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

import scala.collection.GenSet;
import scala.collection.immutable.Set;
import scala.collection.immutable.Map;
import scala.math.BigInt;
import scala.math.BigDecimal;
import scala.runtime.Statics;
import scala.runtime.AbstractFunction1;
import scala.runtime.Null$;
import scala.runtime.ScalaRunTime$;
import scala.runtime.BoxesRunTime;
import scala.MatchError;
import scala.Predef$;
import scala.Tuple2;
import scala.Some;
import scala.None$;
import scala.Option;
import scala.collection.immutable.Nil$;
import scala.Function1;
import scala.collection.immutable.List$;
import scala.collection.immutable.List;
import scala.Product$class;
import scala.collection.Iterator;
import scala.Product;
import scala.runtime.BoxedUnit;
import scala.Serializable;
import scala.collection.Seq;
import scala.reflect.ScalaSignature;

@ScalaSignature(bytes = "\u0006\u0001\u0019\u0005q!B\u0001\u0003\u0011\u00039\u0011a\u0002&t_:\f5\u000b\u0016\u0006\u0003\u0007\u0011\taA[:p]R\u001a(\"A\u0003\u0002\u0007=\u0014xm\u0001\u0001\u0011\u0005!IQ\"\u0001\u0002\u0007\u000b)\u0011\u0001\u0012A\u0006\u0003\u000f)\u001bxN\\!T)N\u0011\u0011\u0002\u0004\t\u0003\u001bAi\u0011A\u0004\u0006\u0002\u001f\u0005)1oY1mC&\u0011\u0011C\u0004\u0002\u0007\u0003:L(+\u001a4\t\u000bMIA\u0011\u0001\u000b\u0002\rqJg.\u001b;?)\u00059\u0001\"\u0002\f\n\t\u00039\u0012AB2p]\u000e\fG\u000fF\u0002\u0019\t\u000b\u0002\"!\u0007\u000e\u000e\u0003%1QaG\u0005\u0002\"q\u0011aA\u0013,bYV,7#\u0002\u000e\r;\u0011:\u0003C\u0001\u0010\"\u001d\tAq$\u0003\u0002!\u0005\u0005!A)\u001b4g\u0013\t\u00113E\u0001\u0005ES\u001a4\u0017M\u00197f\u0015\t\u0001#\u0001\u0005\u0002\u000eK%\u0011aE\u0004\u0002\b!J|G-^2u!\ti\u0001&\u0003\u0002*\u001d\ta1+\u001a:jC2L'0\u00192mK\")1C\u0007C\u0001WQ\t\u0001\u0004B\u0003.5\t\u0005aF\u0001\u0004WC2,Xm]\t\u0003_I\u0002\"!\u0004\u0019\n\u0005Er!a\u0002(pi\"Lgn\u001a\t\u0003\u001bMJ!\u0001\u000e\b\u0003\u0007\u0005s\u0017\u0010C\u000375\u0019\u0005q'\u0001\u0004wC2,Xm]\u000b\u0002qA\u0011\u0011\bL\u0007\u00025!)1H\u0007C\u0001y\u0005A1\r[5mIJ,g.F\u0001>!\rqd\t\u0007\b\u0003\u007f\u0011s!\u0001Q\"\u000e\u0003\u0005S!A\u0011\u0004\u0002\rq\u0012xn\u001c;?\u0013\u0005y\u0011BA#\u000f\u0003\u001d\u0001\u0018mY6bO\u0016L!a\u0012%\u0003\t1K7\u000f\u001e\u0006\u0003\u000b:AQA\u0013\u000e\u0005\u0002-\u000bQ!\u00199qYf$\"\u0001\u0007'\t\u000b5K\u0005\u0019\u0001(\u0002\u0003%\u0004\"!D(\n\u0005As!aA%oi\")!K\u0007C\u0001'\u0006QA\u0005\u001d7vg\u0012\u0002H.^:\u0015\u0005a!\u0006\"B+R\u0001\u0004A\u0012!B8uQ\u0016\u0014\b\"B,\u001b\t\u0003A\u0016\u0001\u0003;p\u001fB$\u0018n\u001c8\u0016\u0003e\u00032!\u0004.\u0019\u0013\tYfB\u0001\u0004PaRLwN\u001c\u0005\u0006;j!\t\u0001W\u0001\u0007i>\u001cv.\\3*-iy\u00161MAT\u0003s\u0014yD!\"\u0003L\u000e\u00151QGBO\t\u00031A\u0001Y\u0005AC\n1!*\u0011:sCf\u001cBa\u0018\r%O!A1m\u0018BK\u0002\u0013\u0005A(A\u0002beJD\u0001\"Z0\u0003\u0012\u0003\u0006I!P\u0001\u0005CJ\u0014\b\u0005C\u0003\u0014?\u0012\u0005q\r\u0006\u0002iSB\u0011\u0011d\u0018\u0005\u0006G\u001a\u0004\r!P\u0003\u0005[}\u00031\u000eE\u0002?\rJBQAN0\u0005\u00025,\u0012A\u001c\t\u0003_*l\u0011a\u0018\u0005\u0006\u0015~#\t%\u001d\u000b\u00031IDQ!\u00149A\u00029Cq\u0001^0\u0002\u0002\u0013\u0005Q/\u0001\u0003d_BLHC\u00015w\u0011\u001d\u00197\u000f%AA\u0002uBq\u0001_0\u0012\u0002\u0013\u0005\u00110\u0001\bd_BLH\u0005Z3gCVdG\u000fJ\u0019\u0016\u0003iT#!P>,\u0003q\u00042!`A\u0003\u001b\u0005q(bA@\u0002\u0002\u0005IQO\\2iK\u000e\\W\r\u001a\u0006\u0004\u0003\u0007q\u0011AC1o]>$\u0018\r^5p]&\u0019\u0011q\u0001@\u0003#Ut7\r[3dW\u0016$g+\u0019:jC:\u001cW\rC\u0005\u0002\f}\u000b\t\u0011\"\u0011\u0002\u000e\u0005i\u0001O]8ek\u000e$\bK]3gSb,\"!a\u0004\u0011\t\u0005E\u00111D\u0007\u0003\u0003'QA!!\u0006\u0002\u0018\u0005!A.\u00198h\u0015\t\tI\"\u0001\u0003kCZ\f\u0017\u0002BA\u000f\u0003'\u0011aa\u0015;sS:<\u0007\"CA\u0011?\u0006\u0005I\u0011AA\u0012\u00031\u0001(o\u001c3vGR\f%/\u001b;z+\u0005q\u0005\"CA\u0014?\u0006\u0005I\u0011AA\u0015\u00039\u0001(o\u001c3vGR,E.Z7f]R$2AMA\u0016\u0011%\ti#!\n\u0002\u0002\u0003\u0007a*A\u0002yIEB\u0011\"!\r`\u0003\u0003%\t%a\r\u0002\u001fA\u0014x\u000eZ;di&#XM]1u_J,\"!!\u000e\u0011\u000b\u0005]\u0012Q\b\u001a\u000e\u0005\u0005e\"bAA\u001e\u001d\u0005Q1m\u001c7mK\u000e$\u0018n\u001c8\n\t\u0005}\u0012\u0011\b\u0002\t\u0013R,'/\u0019;pe\"I\u00111I0\u0002\u0002\u0013\u0005\u0011QI\u0001\tG\u0006tW)];bYR!\u0011qIA'!\ri\u0011\u0011J\u0005\u0004\u0003\u0017r!a\u0002\"p_2,\u0017M\u001c\u0005\n\u0003[\t\t%!AA\u0002IB\u0011\"!\u0015`\u0003\u0003%\t%a\u0015\u0002\u0011!\f7\u000f[\"pI\u0016$\u0012A\u0014\u0005\n\u0003/z\u0016\u0011!C!\u00033\n\u0001\u0002^8TiJLgn\u001a\u000b\u0003\u0003\u001fA\u0011\"!\u0018`\u0003\u0003%\t%a\u0018\u0002\r\u0015\fX/\u00197t)\u0011\t9%!\u0019\t\u0013\u00055\u00121LA\u0001\u0002\u0004\u0011dABA3\u0013\u0001\u000b9GA\u0003K\u0005>|GnE\u0003\u0002da!s\u0005C\u0006\u0002l\u0005\r$Q3A\u0005\u0002\u00055\u0014!\u0002<bYV,WCAA$\u0011-\t\t(a\u0019\u0003\u0012\u0003\u0006I!a\u0012\u0002\rY\fG.^3!\u0011\u001d\u0019\u00121\rC\u0001\u0003k\"B!a\u001e\u0002zA\u0019\u0011$a\u0019\t\u0011\u0005-\u00141\u000fa\u0001\u0003\u000f*a!LA2\u0001\u0005\u001d\u0003b\u0002\u001c\u0002d\u0011\u0005\u0011Q\u000e\u0005\ni\u0006\r\u0014\u0011!C\u0001\u0003\u0003#B!a\u001e\u0002\u0004\"Q\u00111NA@!\u0003\u0005\r!a\u0012\t\u0013a\f\u0019'%A\u0005\u0002\u0005\u001dUCAAEU\r\t9e\u001f\u0005\u000b\u0003\u0017\t\u0019'!A\u0005B\u00055\u0001BCA\u0011\u0003G\n\t\u0011\"\u0001\u0002$!Q\u0011qEA2\u0003\u0003%\t!!%\u0015\u0007I\n\u0019\nC\u0005\u0002.\u0005=\u0015\u0011!a\u0001\u001d\"Q\u0011\u0011GA2\u0003\u0003%\t%a\r\t\u0015\u0005\r\u00131MA\u0001\n\u0003\tI\n\u0006\u0003\u0002H\u0005m\u0005\"CA\u0017\u0003/\u000b\t\u00111\u00013\u0011)\t\t&a\u0019\u0002\u0002\u0013\u0005\u00131\u000b\u0005\u000b\u0003/\n\u0019'!A\u0005B\u0005e\u0003BCA/\u0003G\n\t\u0011\"\u0011\u0002$R!\u0011qIAS\u0011%\ti#!)\u0002\u0002\u0003\u0007!G\u0002\u0004\u0002*&\u0001\u00151\u0016\u0002\t\u0015\u0012+7-[7bYN9\u0011q\u0015\r\u0002.\u0012:\u0003cA\r\u00020\u001aI\u0011\u0011W\u0005\u0011\u0002G\u0005\u00111\u0017\u0002\b\u0015:+XNY3s'\r\ty\u000b\u0004\u0005\f\u0003o\u000b9K!f\u0001\n\u0003\tI,A\u0002ok6,\"!a/\u0011\u0007y\ni,C\u0002\u0002@\"\u0013!BQ5h\t\u0016\u001c\u0017.\\1m\u0011-\t\u0019-a*\u0003\u0012\u0003\u0006I!a/\u0002\t9,X\u000e\t\u0005\b'\u0005\u001dF\u0011AAd)\u0011\tI-a3\u0011\u0007e\t9\u000b\u0003\u0005\u00028\u0006\u0015\u0007\u0019AA^\u000b\u0019i\u0013q\u0015\u0001\u0002<\"9a'a*\u0005\u0002\u0005e\u0006\"\u0003;\u0002(\u0006\u0005I\u0011AAj)\u0011\tI-!6\t\u0015\u0005]\u0016\u0011\u001bI\u0001\u0002\u0004\tY\fC\u0005y\u0003O\u000b\n\u0011\"\u0001\u0002ZV\u0011\u00111\u001c\u0016\u0004\u0003w[\bBCA\u0006\u0003O\u000b\t\u0011\"\u0011\u0002\u000e!Q\u0011\u0011EAT\u0003\u0003%\t!a\t\t\u0015\u0005\u001d\u0012qUA\u0001\n\u0003\t\u0019\u000fF\u00023\u0003KD\u0011\"!\f\u0002b\u0006\u0005\t\u0019\u0001(\t\u0015\u0005E\u0012qUA\u0001\n\u0003\n\u0019\u0004\u0003\u0006\u0002D\u0005\u001d\u0016\u0011!C\u0001\u0003W$B!a\u0012\u0002n\"I\u0011QFAu\u0003\u0003\u0005\rA\r\u0005\u000b\u0003#\n9+!A\u0005B\u0005M\u0003BCA,\u0003O\u000b\t\u0011\"\u0011\u0002Z!Q\u0011QLAT\u0003\u0003%\t%!>\u0015\t\u0005\u001d\u0013q\u001f\u0005\n\u0003[\t\u00190!AA\u0002I2a!a?\n\u0001\u0006u(a\u0002&E_V\u0014G.Z\n\b\u0003sD\u0012Q\u0016\u0013(\u0011-\t9,!?\u0003\u0016\u0004%\tA!\u0001\u0016\u0005\t\r\u0001cA\u0007\u0003\u0006%\u0019!q\u0001\b\u0003\r\u0011{WO\u00197f\u0011-\t\u0019-!?\u0003\u0012\u0003\u0006IAa\u0001\t\u000fM\tI\u0010\"\u0001\u0003\u000eQ!!q\u0002B\t!\rI\u0012\u0011 \u0005\t\u0003o\u0013Y\u00011\u0001\u0003\u0004\u00151Q&!?\u0001\u0005\u0007AqANA}\t\u0003\u0011\t\u0001C\u0005u\u0003s\f\t\u0011\"\u0001\u0003\u001aQ!!q\u0002B\u000e\u0011)\t9La\u0006\u0011\u0002\u0003\u0007!1\u0001\u0005\nq\u0006e\u0018\u0013!C\u0001\u0005?)\"A!\t+\u0007\t\r1\u0010\u0003\u0006\u0002\f\u0005e\u0018\u0011!C!\u0003\u001bA!\"!\t\u0002z\u0006\u0005I\u0011AA\u0012\u0011)\t9#!?\u0002\u0002\u0013\u0005!\u0011\u0006\u000b\u0004e\t-\u0002\"CA\u0017\u0005O\t\t\u00111\u0001O\u0011)\t\t$!?\u0002\u0002\u0013\u0005\u00131\u0007\u0005\u000b\u0003\u0007\nI0!A\u0005\u0002\tEB\u0003BA$\u0005gA\u0011\"!\f\u00030\u0005\u0005\t\u0019\u0001\u001a\t\u0015\u0005E\u0013\u0011`A\u0001\n\u0003\n\u0019\u0006\u0003\u0006\u0002X\u0005e\u0018\u0011!C!\u00033B!\"!\u0018\u0002z\u0006\u0005I\u0011\tB\u001e)\u0011\t9E!\u0010\t\u0013\u00055\"\u0011HA\u0001\u0002\u0004\u0011dA\u0002B!\u0013\u0001\u0013\u0019E\u0001\u0003K\u0013:$8c\u0002B 1\u00055Fe\n\u0005\f\u0003o\u0013yD!f\u0001\n\u0003\u00119%\u0006\u0002\u0003JA\u0019aHa\u0013\n\u0007\t5\u0003J\u0001\u0004CS\u001eLe\u000e\u001e\u0005\f\u0003\u0007\u0014yD!E!\u0002\u0013\u0011I\u0005C\u0004\u0014\u0005\u007f!\tAa\u0015\u0015\t\tU#q\u000b\t\u00043\t}\u0002\u0002CA\\\u0005#\u0002\rA!\u0013\u0006\r5\u0012y\u0004\u0001B%\u0011\u001d1$q\bC\u0001\u0005\u000fB\u0011\u0002\u001eB \u0003\u0003%\tAa\u0018\u0015\t\tU#\u0011\r\u0005\u000b\u0003o\u0013i\u0006%AA\u0002\t%\u0003\"\u0003=\u0003@E\u0005I\u0011\u0001B3+\t\u00119GK\u0002\u0003JmD!\"a\u0003\u0003@\u0005\u0005I\u0011IA\u0007\u0011)\t\tCa\u0010\u0002\u0002\u0013\u0005\u00111\u0005\u0005\u000b\u0003O\u0011y$!A\u0005\u0002\t=Dc\u0001\u001a\u0003r!I\u0011Q\u0006B7\u0003\u0003\u0005\rA\u0014\u0005\u000b\u0003c\u0011y$!A\u0005B\u0005M\u0002BCA\"\u0005\u007f\t\t\u0011\"\u0001\u0003xQ!\u0011q\tB=\u0011%\tiC!\u001e\u0002\u0002\u0003\u0007!\u0007\u0003\u0006\u0002R\t}\u0012\u0011!C!\u0003'B!\"a\u0016\u0003@\u0005\u0005I\u0011IA-\u0011)\tiFa\u0010\u0002\u0002\u0013\u0005#\u0011\u0011\u000b\u0005\u0003\u000f\u0012\u0019\tC\u0005\u0002.\t}\u0014\u0011!a\u0001e\u00191!qQ\u0005A\u0005\u0013\u0013QA\u0013'p]\u001e\u001crA!\"\u0019\u0003[#s\u0005C\u0006\u00028\n\u0015%Q3A\u0005\u0002\t5UC\u0001BH!\ri!\u0011S\u0005\u0004\u0005's!\u0001\u0002'p]\u001eD1\"a1\u0003\u0006\nE\t\u0015!\u0003\u0003\u0010\"91C!\"\u0005\u0002\teE\u0003\u0002BN\u0005;\u00032!\u0007BC\u0011!\t9La&A\u0002\t=UAB\u0017\u0003\u0006\u0002\u0011y\tC\u00047\u0005\u000b#\tA!$\t\u0013Q\u0014))!A\u0005\u0002\t\u0015F\u0003\u0002BN\u0005OC!\"a.\u0003$B\u0005\t\u0019\u0001BH\u0011%A(QQI\u0001\n\u0003\u0011Y+\u0006\u0002\u0003.*\u001a!qR>\t\u0015\u0005-!QQA\u0001\n\u0003\ni\u0001\u0003\u0006\u0002\"\t\u0015\u0015\u0011!C\u0001\u0003GA!\"a\n\u0003\u0006\u0006\u0005I\u0011\u0001B[)\r\u0011$q\u0017\u0005\n\u0003[\u0011\u0019,!AA\u00029C!\"!\r\u0003\u0006\u0006\u0005I\u0011IA\u001a\u0011)\t\u0019E!\"\u0002\u0002\u0013\u0005!Q\u0018\u000b\u0005\u0003\u000f\u0012y\fC\u0005\u0002.\tm\u0016\u0011!a\u0001e!Q\u0011\u0011\u000bBC\u0003\u0003%\t%a\u0015\t\u0015\u0005]#QQA\u0001\n\u0003\nI\u0006\u0003\u0006\u0002^\t\u0015\u0015\u0011!C!\u0005\u000f$B!a\u0012\u0003J\"I\u0011Q\u0006Bc\u0003\u0003\u0005\rA\r\u0004\b\u0005\u001bL\u0001\u0012\u0011Bh\u0005!Qej\u001c;iS:<7#\u0002Bf1\u0011:\u0003bB\n\u0003L\u0012\u0005!1\u001b\u000b\u0003\u0005+\u00042!\u0007Bf\u000b\u0019i#1\u001a\u0001\u0003Z:\u0019QBa7\n\u0007\tug\"\u0001\u0003O_:,\u0007b\u0002\u001c\u0003L\u0012\u0005!\u0011]\u000b\u0003\u00053D!\"a\u0003\u0003L\u0006\u0005I\u0011IA\u0007\u0011)\t\tCa3\u0002\u0002\u0013\u0005\u00111\u0005\u0005\u000b\u0003O\u0011Y-!A\u0005\u0002\t%Hc\u0001\u001a\u0003l\"I\u0011Q\u0006Bt\u0003\u0003\u0005\rA\u0014\u0005\u000b\u0003c\u0011Y-!A\u0005B\u0005M\u0002BCA\"\u0005\u0017\f\t\u0011\"\u0001\u0003rR!\u0011q\tBz\u0011%\tiCa<\u0002\u0002\u0003\u0007!\u0007\u0003\u0006\u0002R\t-\u0017\u0011!C!\u0003'B!\"a\u0016\u0003L\u0006\u0005I\u0011IA-\u0011)\u0011YPa3\u0002\u0002\u0013%!Q`\u0001\fe\u0016\fGMU3t_24X\r\u0006\u0002\u0003\u0000B!\u0011\u0011CB\u0001\u0013\u0011\u0019\u0019!a\u0005\u0003\r=\u0013'.Z2u\r\u001d\u00199!\u0003EA\u0007\u0013\u0011QA\u0013(vY2\u001cRa!\u0002\u0019I\u001dBqaEB\u0003\t\u0003\u0019i\u0001\u0006\u0002\u0004\u0010A\u0019\u0011d!\u0002\u0006\r5\u001a)\u0001AB\n!\ri1QC\u0005\u0004\u0007/q!\u0001\u0002(vY2DqANB\u0003\t\u0003\u0019Y\"\u0006\u0002\u0004\u0014!Q\u00111BB\u0003\u0003\u0003%\t%!\u0004\t\u0015\u0005\u00052QAA\u0001\n\u0003\t\u0019\u0003\u0003\u0006\u0002(\r\u0015\u0011\u0011!C\u0001\u0007G!2AMB\u0013\u0011%\tic!\t\u0002\u0002\u0003\u0007a\n\u0003\u0006\u00022\r\u0015\u0011\u0011!C!\u0003gA!\"a\u0011\u0004\u0006\u0005\u0005I\u0011AB\u0016)\u0011\t9e!\f\t\u0013\u000552\u0011FA\u0001\u0002\u0004\u0011\u0004BCA)\u0007\u000b\t\t\u0011\"\u0011\u0002T!Q\u0011qKB\u0003\u0003\u0003%\t%!\u0017\t\u0015\tm8QAA\u0001\n\u0013\u0011iP\u0002\u0004\u00048%\u00015\u0011\b\u0002\b\u0015>\u0013'.Z2u'\u0015\u0019)\u0004\u0007\u0013(\u0011-\u0019id!\u000e\u0003\u0016\u0004%\taa\u0010\u0002\u0007=\u0014'.\u0006\u0002\u0004BA!aHRB\"!\rI2QI\u0003\u0007\u0007\u000fJ\u0001a!\u0013\u0003\r)3\u0015.\u001a7e!\u0019i11JB(1%\u00191Q\n\b\u0003\rQ+\b\u000f\\33!\u0011\u0019\tfa\u0016\u000f\u00075\u0019\u0019&C\u0002\u0004V9\ta\u0001\u0015:fI\u00164\u0017\u0002BA\u000f\u00073R1a!\u0016\u000f\u0011-\u0019if!\u000e\u0003\u0012\u0003\u0006Ia!\u0011\u0002\t=\u0014'\u000e\t\u0005\b'\rUB\u0011AB1)\u0011\u0019\u0019g!\u001a\u0011\u0007e\u0019)\u0004\u0003\u0005\u0004>\r}\u0003\u0019AB!\u000b\u0019i3Q\u0007\u0001\u0004jA91\u0011KB6\u0007\u001f\u0012\u0014\u0002BB7\u00073\u00121!T1q\u0011\u001d14Q\u0007C\u0001\u0007c*\"a!\u001b\t\u0011\u0005u3Q\u0007C!\u0007k\"B!a\u0012\u0004x!91\u0011PB:\u0001\u0004\u0011\u0014\u0001\u0002;iCRD\u0001\"!\u0015\u00046\u0011\u0005\u00131\u000b\u0005\ni\u000eU\u0012\u0011!C\u0001\u0007\u007f\"Baa\u0019\u0004\u0002\"Q1QHB?!\u0003\u0005\ra!\u0011\t\u0013a\u001c)$%A\u0005\u0002\r\u0015UCABDU\r\u0019\te\u001f\u0005\u000b\u0003\u0017\u0019)$!A\u0005B\u00055\u0001BCA\u0011\u0007k\t\t\u0011\"\u0001\u0002$!Q\u0011qEB\u001b\u0003\u0003%\taa$\u0015\u0007I\u001a\t\nC\u0005\u0002.\r5\u0015\u0011!a\u0001\u001d\"Q\u0011\u0011GB\u001b\u0003\u0003%\t%a\r\t\u0015\u0005\r3QGA\u0001\n\u0003\u00199\n\u0006\u0003\u0002H\re\u0005\"CA\u0017\u0007+\u000b\t\u00111\u00013\u0011)\t9f!\u000e\u0002\u0002\u0013\u0005\u0013\u0011\f\u0004\u0007\u0007?K\u0001i!)\u0003\t)\u001bV\r^\n\u0006\u0007;CBe\n\u0005\f\u0007K\u001biJ!f\u0001\n\u0003\u00199+A\u0002tKR,\"a!+\u0011\u000b\rE31\u0016\r\n\t\r56\u0011\f\u0002\u0004'\u0016$\bbCBY\u0007;\u0013\t\u0012)A\u0005\u0007S\u000bAa]3uA!91c!(\u0005\u0002\rUF\u0003BB\\\u0007s\u00032!GBO\u0011!\u0019)ka-A\u0002\r%VAB\u0017\u0004\u001e\u0002\u0019I\u000bC\u00047\u0007;#\taa*\t\u0011\u0005u3Q\u0014C!\u0007\u0003$B!a\u0012\u0004D\"91QYB`\u0001\u0004\u0011\u0014!A8\t\u0011\r%7Q\u0014C\u0001\u0007\u0017\f\u0011\"\u001b8uKJ\u001cXm\u0019;\u0015\t\r]6Q\u001a\u0005\t\u0007\u000b\u001c9\r1\u0001\u00048\"A1\u0011[BO\t\u0003\u0019\u0019.A\u0003v]&|g\u000e\u0006\u0003\u00048\u000eU\u0007\u0002CBc\u0007\u001f\u0004\raa.\t\u0011\re7Q\u0014C\u0001\u00077\f!\u0002Z5gM\u0016\u0014XM\\2f)\u0011\u00199l!8\t\u0011\r\u00157q\u001ba\u0001\u0007oC\u0011\u0002^BO\u0003\u0003%\ta!9\u0015\t\r]61\u001d\u0005\u000b\u0007K\u001by\u000e%AA\u0002\r%\u0006\"\u0003=\u0004\u001eF\u0005I\u0011ABt+\t\u0019IOK\u0002\u0004*nD!\"a\u0003\u0004\u001e\u0006\u0005I\u0011IA\u0007\u0011)\t\tc!(\u0002\u0002\u0013\u0005\u00111\u0005\u0005\u000b\u0003O\u0019i*!A\u0005\u0002\rEHc\u0001\u001a\u0004t\"I\u0011QFBx\u0003\u0003\u0005\rA\u0014\u0005\u000b\u0003c\u0019i*!A\u0005B\u0005M\u0002BCA\"\u0007;\u000b\t\u0011\"\u0001\u0004zR!\u0011qIB~\u0011%\tica>\u0002\u0002\u0003\u0007!\u0007\u0003\u0006\u0002R\ru\u0015\u0011!C!\u0003'B!\"a\u0016\u0004\u001e\u0006\u0005I\u0011IA-\r\u0019!\u0019!\u0003!\u0005\u0006\t9!j\u0015;sS:<7#\u0002C\u00011\u0011:\u0003b\u0003C\u0005\t\u0003\u0011)\u001a!C\u0001\t\u0017\t\u0011a]\u000b\u0003\u0007\u001fB1\u0002b\u0004\u0005\u0002\tE\t\u0015!\u0003\u0004P\u0005\u00111\u000f\t\u0005\b'\u0011\u0005A\u0011\u0001C\n)\u0011!)\u0002b\u0006\u0011\u0007e!\t\u0001\u0003\u0005\u0005\n\u0011E\u0001\u0019AB(\u000b\u0019iC\u0011\u0001\u0001\u0004P!9a\u0007\"\u0001\u0005\u0002\u0011-\u0001\"\u0003;\u0005\u0002\u0005\u0005I\u0011\u0001C\u0010)\u0011!)\u0002\"\t\t\u0015\u0011%AQ\u0004I\u0001\u0002\u0004\u0019y\u0005C\u0005y\t\u0003\t\n\u0011\"\u0001\u0005&U\u0011Aq\u0005\u0016\u0004\u0007\u001fZ\bBCA\u0006\t\u0003\t\t\u0011\"\u0011\u0002\u000e!Q\u0011\u0011\u0005C\u0001\u0003\u0003%\t!a\t\t\u0015\u0005\u001dB\u0011AA\u0001\n\u0003!y\u0003F\u00023\tcA\u0011\"!\f\u0005.\u0005\u0005\t\u0019\u0001(\t\u0015\u0005EB\u0011AA\u0001\n\u0003\n\u0019\u0004\u0003\u0006\u0002D\u0011\u0005\u0011\u0011!C\u0001\to!B!a\u0012\u0005:!I\u0011Q\u0006C\u001b\u0003\u0003\u0005\rA\r\u0005\u000b\u0003#\"\t!!A\u0005B\u0005M\u0003BCA,\t\u0003\t\t\u0011\"\u0011\u0002Z!Q\u0011Q\fC\u0001\u0003\u0003%\t\u0005\"\u0011\u0015\t\u0005\u001dC1\t\u0005\n\u0003[!y$!AA\u0002IBq\u0001b\u0012\u0016\u0001\u0004!I%\u0001\u0002ygB!Q\u0002b\u0013\u0019\u0013\r!iE\u0004\u0002\u000byI,\u0007/Z1uK\u0012tta\u0002C)\u0013!\u0005A1K\u0001\u0007\u0015Z\u000bG.^3\u0011\u0007e!)F\u0002\u0004\u001c\u0013!\u0005AqK\n\u0007\t+bA\u0011L\u0014\u0011\t\u0011mC\u0011\r\b\u0004\u0011\u0011u\u0013b\u0001C0\u0005\u0005)Q*\u001a:hK&!A1\rC3\u0005%iUM]4fC\ndWMC\u0002\u0005`\tAqa\u0005C+\t\u0003!I\u0007\u0006\u0002\u0005T!Q!1 C+\u0003\u0003%IA!@\b\u000f\u0011=\u0014\u0002#!\u0003V\u0006A!JT8uQ&twmB\u0004\u0005t%A\tia\u0004\u0002\u000b)sU\u000f\u001c7\b\u0013\u0011]\u0014\"!A\t\u0002\u0011e\u0014a\u0002&TiJLgn\u001a\t\u00043\u0011md!\u0003C\u0002\u0013\u0005\u0005\t\u0012\u0001C?'\u0015!Y\bb (!!!\t\tb\"\u0004P\u0011UQB\u0001CB\u0015\r!)ID\u0001\beVtG/[7f\u0013\u0011!I\tb!\u0003#\u0005\u00137\u000f\u001e:bGR4UO\\2uS>t\u0017\u0007C\u0004\u0014\tw\"\t\u0001\"$\u0015\u0005\u0011e\u0004BCA,\tw\n\t\u0011\"\u0012\u0002Z!I!\nb\u001f\u0002\u0002\u0013\u0005E1\u0013\u000b\u0005\t+!)\n\u0003\u0005\u0005\n\u0011E\u0005\u0019AB(\u0011)!I\nb\u001f\u0002\u0002\u0013\u0005E1T\u0001\bk:\f\u0007\u000f\u001d7z)\u0011!i\nb(\u0011\t5Q6q\n\u0005\u000b\tC#9*!AA\u0002\u0011U\u0011a\u0001=%a!Q!1 C>\u0003\u0003%IA!@\b\u0013\u0011\u001d\u0016\"!A\t\u0002\u0011%\u0016a\u0002&E_V\u0014G.\u001a\t\u00043\u0011-f!CA~\u0013\u0005\u0005\t\u0012\u0001CW'\u0015!Y\u000bb,(!!!\t\tb\"\u0003\u0004\t=\u0001bB\n\u0005,\u0012\u0005A1\u0017\u000b\u0003\tSC!\"a\u0016\u0005,\u0006\u0005IQIA-\u0011%QE1VA\u0001\n\u0003#I\f\u0006\u0003\u0003\u0010\u0011m\u0006\u0002CA\\\to\u0003\rAa\u0001\t\u0015\u0011eE1VA\u0001\n\u0003#y\f\u0006\u0003\u0005B\u0012\r\u0007\u0003B\u0007[\u0005\u0007A!\u0002\")\u0005>\u0006\u0005\t\u0019\u0001B\b\u0011)\u0011Y\u0010b+\u0002\u0002\u0013%!Q`\u0004\n\t\u0013L\u0011\u0011!E\u0001\t\u0017\f\u0001B\u0013#fG&l\u0017\r\u001c\t\u00043\u00115g!CAU\u0013\u0005\u0005\t\u0012\u0001Ch'\u0015!i\r\"5(!!!\t\tb\"\u0002<\u0006%\u0007bB\n\u0005N\u0012\u0005AQ\u001b\u000b\u0003\t\u0017D!\"a\u0016\u0005N\u0006\u0005IQIA-\u0011%QEQZA\u0001\n\u0003#Y\u000e\u0006\u0003\u0002J\u0012u\u0007\u0002CA\\\t3\u0004\r!a/\t\u0015\u0011eEQZA\u0001\n\u0003#\t\u000f\u0006\u0003\u0005d\u0012\u0015\b\u0003B\u0007[\u0003wC!\u0002\")\u0005`\u0006\u0005\t\u0019AAe\u0011)\u0011Y\u0010\"4\u0002\u0002\u0013%!Q`\u0004\n\tWL\u0011\u0011!E\u0001\t[\fQA\u0013'p]\u001e\u00042!\u0007Cx\r%\u00119)CA\u0001\u0012\u0003!\tpE\u0003\u0005p\u0012Mx\u0005\u0005\u0005\u0005\u0002\u0012\u001d%q\u0012BN\u0011\u001d\u0019Bq\u001eC\u0001\to$\"\u0001\"<\t\u0015\u0005]Cq^A\u0001\n\u000b\nI\u0006C\u0005K\t_\f\t\u0011\"!\u0005~R!!1\u0014C\u0000\u0011!\t9\fb?A\u0002\t=\u0005B\u0003CM\t_\f\t\u0011\"!\u0006\u0004Q!QQAC\u0004!\u0011i!La$\t\u0015\u0011\u0005V\u0011AA\u0001\u0002\u0004\u0011Y\n\u0003\u0006\u0003|\u0012=\u0018\u0011!C\u0005\u0005{<\u0011\"\"\u0004\n\u0003\u0003E\t!b\u0004\u0002\t)Ke\u000e\u001e\t\u00043\u0015Ea!\u0003B!\u0013\u0005\u0005\t\u0012AC\n'\u0015)\t\"\"\u0006(!!!\t\tb\"\u0003J\tU\u0003bB\n\u0006\u0012\u0011\u0005Q\u0011\u0004\u000b\u0003\u000b\u001fA!\"a\u0016\u0006\u0012\u0005\u0005IQIA-\u0011%QU\u0011CA\u0001\n\u0003+y\u0002\u0006\u0003\u0003V\u0015\u0005\u0002\u0002CA\\\u000b;\u0001\rA!\u0013\t\u0015\u0011eU\u0011CA\u0001\n\u0003+)\u0003\u0006\u0003\u0006(\u0015%\u0002\u0003B\u0007[\u0005\u0013B!\u0002\")\u0006$\u0005\u0005\t\u0019\u0001B+\u0011)\u0011Y0\"\u0005\u0002\u0002\u0013%!Q`\u0004\b\u000b_I\u0001\u0012AC\u0019\u0003\u0015Q%i\\8m!\rIR1\u0007\u0004\b\u0003KJ\u0001\u0012AC\u001b'\u0011)\u0019\u0004D\u0014\t\u000fM)\u0019\u0004\"\u0001\u0006:Q\u0011Q\u0011\u0007\u0005\u000b\u000b{)\u0019D1A\u0005\u0002\u0015}\u0012\u0001\u0002+sk\u0016,\"!a\u001e\t\u0013\u0015\rS1\u0007Q\u0001\n\u0005]\u0014!\u0002+sk\u0016\u0004\u0003BCC$\u000bg\u0011\r\u0011\"\u0001\u0006@\u0005)a)\u00197tK\"IQ1JC\u001aA\u0003%\u0011qO\u0001\u0007\r\u0006d7/\u001a\u0011\t\u0013)+\u0019$!A\u0005\u0002\u0016=C\u0003BA<\u000b#B\u0001\"a\u001b\u0006N\u0001\u0007\u0011q\t\u0005\u000b\t3+\u0019$!A\u0005\u0002\u0016UC\u0003BC,\u000b3\u0002B!\u0004.\u0002H!QA\u0011UC*\u0003\u0003\u0005\r!a\u001e\t\u0015\tmX1GA\u0001\n\u0013\u0011ipB\u0004\u0006`%A\t)\"\u0019\u0002\u000f){%M[3diB\u0019\u0011$b\u0019\u0007\u000f\r]\u0012\u0002#!\u0006fM)Q1\r\u0007%O!91#b\u0019\u0005\u0002\u0015%DCAC1\u0011\u001dQU1\rC\u0001\u000b[\"Baa\u0019\u0006p!AQ\u0011OC6\u0001\u0004)\u0019(\u0001\u0002ggB)Q\u0002b\u0013\u0004D!I!*b\u0019\u0002\u0002\u0013\u0005Uq\u000f\u000b\u0005\u0007G*I\b\u0003\u0005\u0004>\u0015U\u0004\u0019AB!\u0011)!I*b\u0019\u0002\u0002\u0013\u0005UQ\u0010\u000b\u0005\u000b\u007f*\t\t\u0005\u0003\u000e5\u000e\u0005\u0003B\u0003CQ\u000bw\n\t\u00111\u0001\u0004d!Q\u00111BC2\u0003\u0003%\t%!\u0004\t\u0015\u0005\u0005R1MA\u0001\n\u0003\t\u0019\u0003\u0003\u0006\u0002(\u0015\r\u0014\u0011!C\u0001\u000b\u0013#2AMCF\u0011%\ti#b\"\u0002\u0002\u0003\u0007a\n\u0003\u0006\u00022\u0015\r\u0014\u0011!C!\u0003gA!\"a\u0011\u0006d\u0005\u0005I\u0011ACI)\u0011\t9%b%\t\u0013\u00055RqRA\u0001\u0002\u0004\u0011\u0004BCA)\u000bG\n\t\u0011\"\u0011\u0002T!Q\u0011qKC2\u0003\u0003%\t%!\u0017\t\u0015\tmX1MA\u0001\n\u0013\u0011ipB\u0005\u0006\u001e&\t\t\u0011#\u0001\u0006 \u00061!*\u0011:sCf\u00042!GCQ\r!\u0001\u0017\"!A\t\u0002\u0015\r6#BCQ\u000bK;\u0003C\u0002CA\t\u000fk\u0004\u000eC\u0004\u0014\u000bC#\t!\"+\u0015\u0005\u0015}\u0005BCA,\u000bC\u000b\t\u0011\"\u0012\u0002Z!I!*\")\u0002\u0002\u0013\u0005Uq\u0016\u000b\u0004Q\u0016E\u0006BB2\u0006.\u0002\u0007Q\b\u0003\u0006\u0005\u001a\u0016\u0005\u0016\u0011!CA\u000bk#B!b.\u0006:B\u0019QBW\u001f\t\u0013\u0011\u0005V1WA\u0001\u0002\u0004A\u0007B\u0003B~\u000bC\u000b\t\u0011\"\u0003\u0003~\u001eIQqX\u0005\u0002\u0002#\u0005Q\u0011Y\u0001\u0005\u0015N+G\u000fE\u0002\u001a\u000b\u00074\u0011ba(\n\u0003\u0003E\t!\"2\u0014\u000b\u0015\rWqY\u0014\u0011\u0011\u0011\u0005EqQBU\u0007oCqaECb\t\u0003)Y\r\u0006\u0002\u0006B\"Q\u0011qKCb\u0003\u0003%)%!\u0017\t\u0013)+\u0019-!A\u0005\u0002\u0016EG\u0003BB\\\u000b'D\u0001b!*\u0006P\u0002\u00071\u0011\u0016\u0005\u000b\t3+\u0019-!A\u0005\u0002\u0016]G\u0003BCm\u000b7\u0004B!\u0004.\u0004*\"QA\u0011UCk\u0003\u0003\u0005\raa.\t\u0015\tmX1YA\u0001\n\u0013\u0011ipB\u0004\u0006b&A\t!b9\u0002\r)3\u0015.\u001a7e!\rIRQ\u001d\u0004\b\u0007\u000fJ\u0001\u0012ACt'\r))\u000f\u0004\u0005\b'\u0015\u0015H\u0011ACv)\t)\u0019\u000fC\u0004K\u000bK$\t!b<\u0015\r\r%S\u0011_C{\u0011!)\u00190\"<A\u0002\r=\u0013\u0001\u00028b[\u0016Dq!a\u001b\u0006n\u0002\u0007\u0001\u0004\u0003\u0005\u0005\u001a\u0016\u0015H\u0011AC})\u0011)Y0\"@\u0011\t5Q6\u0011\n\u0005\t\u000b\u007f,9\u00101\u0001\u0004D\u0005\ta\r")
public final class JsonAST
{
    public static JValue concat(final Seq<JValue> xs) {
        return JsonAST$.MODULE$.concat(xs);
    }
    
    public static class JValue$ implements Merge.Mergeable, Serializable
    {
        public static final JValue$ MODULE$;
        private volatile MergeDeps.ooo$ ooo$module;
        private volatile MergeDeps.aaa$ aaa$module;
        
        static {
            new JValue$();
        }
        
        @Override
        public <A extends JValue> MergeSyntax<A> j2m(final A json) {
            return (MergeSyntax<A>)Merge$Mergeable$class.j2m(this, json);
        }
        
        private MergeDeps.ooo$ ooo$lzycompute() {
            synchronized (this) {
                if (this.ooo$module == null) {
                    this.ooo$module = new MergeDeps.ooo$(this);
                }
                final BoxedUnit unit = BoxedUnit.UNIT;
                return this.ooo$module;
            }
        }
        
        public MergeDeps.ooo$ ooo() {
            return (this.ooo$module == null) ? this.ooo$lzycompute() : this.ooo$module;
        }
        
        private MergeDeps.aaa$ aaa$lzycompute() {
            synchronized (this) {
                if (this.aaa$module == null) {
                    this.aaa$module = new MergeDeps.aaa$(this);
                }
                final BoxedUnit unit = BoxedUnit.UNIT;
                return this.aaa$module;
            }
        }
        
        public MergeDeps.aaa$ aaa() {
            return (this.aaa$module == null) ? this.aaa$lzycompute() : this.aaa$module;
        }
        
        public <A extends JValue, B extends JValue> Object jjj() {
            return LowPriorityMergeDep$class.jjj(this);
        }
        
        private Object readResolve() {
            return JValue$.MODULE$;
        }
        
        public JValue$() {
            LowPriorityMergeDep$class.$init$(MODULE$ = this);
            MergeDeps$class.$init$(this);
            Merge$Mergeable$class.$init$(this);
        }
    }
    
    public abstract static class JValue implements Diff.Diffable, Product, Serializable
    {
        public Iterator<Object> productIterator() {
            return (Iterator<Object>)Product$class.productIterator((Product)this);
        }
        
        public String productPrefix() {
            return Product$class.productPrefix((Product)this);
        }
        
        @Override
        public Diff diff(final JValue other) {
            return Diff$Diffable$class.diff(this, other);
        }
        
        public abstract Object values();
        
        public List<JValue> children() {
            Object o;
            if (this instanceof JObject) {
                final List l = ((JObject)this).obj();
                o = l.map((Function1)new JsonAST$JValue$$anonfun$children.JsonAST$JValue$$anonfun$children$1(this), List$.MODULE$.canBuildFrom());
            }
            else if (this instanceof JArray) {
                final List i = (List)(o = ((JArray)this).arr());
            }
            else {
                o = Nil$.MODULE$;
            }
            return (List<JValue>)o;
        }
        
        public JValue apply(final int i) {
            return JNothing$.MODULE$;
        }
        
        public JValue $plus$plus(final JValue other) {
            return this.append$1(this, other);
        }
        
        public Option<JValue> toOption() {
            Object module$;
            if (JNothing$.MODULE$.equals(this) || JNull$.MODULE$.equals(this)) {
                module$ = None$.MODULE$;
            }
            else {
                module$ = new Some((Object)this);
            }
            return (Option<JValue>)module$;
        }
        
        public Option<JValue> toSome() {
            Object module$;
            if (JNothing$.MODULE$.equals(this)) {
                module$ = None$.MODULE$;
            }
            else {
                module$ = new Some((Object)this);
            }
            return (Option<JValue>)module$;
        }
        
        private final JValue append$1(final JValue value1, final JValue value2) {
            final Tuple2 tuple2 = new Tuple2((Object)value1, (Object)value2);
            if (tuple2 != null) {
                final JValue obj = (JValue)tuple2._1();
                final JValue x = (JValue)tuple2._2();
                if (JNothing$.MODULE$.equals(obj)) {
                    return x;
                }
            }
            if (tuple2 != null) {
                final JValue x2 = (JValue)tuple2._1();
                if (JNothing$.MODULE$.equals(tuple2._2())) {
                    return x2;
                }
            }
            if (tuple2 != null) {
                final JValue value4 = (JValue)tuple2._1();
                final JValue value5 = (JValue)tuple2._2();
                if (value4 instanceof JArray) {
                    final List xs = ((JArray)value4).arr();
                    if (value5 instanceof JArray) {
                        final List ys = ((JArray)value5).arr();
                        return new JArray((List<JValue>)ys.$colon$colon$colon(xs));
                    }
                }
            }
            if (tuple2 != null) {
                final JValue value6 = (JValue)tuple2._1();
                final JValue v = (JValue)tuple2._2();
                if (value6 instanceof JArray) {
                    final List xs2 = ((JArray)value6).arr();
                    if (v != null) {
                        return new JArray((List<JValue>)List$.MODULE$.apply((Seq)Predef$.MODULE$.wrapRefArray((Object[])new JValue[] { v })).$colon$colon$colon(xs2));
                    }
                }
            }
            if (tuple2 != null) {
                final JValue v2 = (JValue)tuple2._1();
                final JValue value7 = (JValue)tuple2._2();
                if (v2 != null) {
                    final JValue value8 = v2;
                    if (value7 instanceof JArray) {
                        final List xs3 = ((JArray)value7).arr();
                        return new JArray((List<JValue>)xs3.$colon$colon((Object)value8));
                    }
                }
            }
            if (tuple2 == null) {
                throw new MatchError((Object)tuple2);
            }
            final JValue x3 = (JValue)tuple2._1();
            final JValue y = (JValue)tuple2._2();
            return new JArray((List<JValue>)Nil$.MODULE$.$colon$colon((Object)y).$colon$colon((Object)x3));
        }
        
        public JValue() {
            Diff$Diffable$class.$init$(this);
            Product$class.$init$((Product)this);
        }
    }
    
    public static class JNothing$ extends JValue
    {
        public static final JNothing$ MODULE$;
        
        static {
            new JNothing$();
        }
        
        @Override
        public None$ values() {
            return None$.MODULE$;
        }
        
        @Override
        public String productPrefix() {
            return "JNothing";
        }
        
        public int productArity() {
            return 0;
        }
        
        public Object productElement(final int x$1) {
            throw new IndexOutOfBoundsException(BoxesRunTime.boxToInteger(x$1).toString());
        }
        
        @Override
        public Iterator<Object> productIterator() {
            return (Iterator<Object>)ScalaRunTime$.MODULE$.typedProductIterator((Product)this);
        }
        
        public boolean canEqual(final Object x$1) {
            return x$1 instanceof JNothing$;
        }
        
        @Override
        public int hashCode() {
            return -382044125;
        }
        
        @Override
        public String toString() {
            return "JNothing";
        }
        
        private Object readResolve() {
            return JNothing$.MODULE$;
        }
        
        public JNothing$() {
            MODULE$ = this;
        }
    }
    
    public static class JNull$ extends JValue
    {
        public static final JNull$ MODULE$;
        
        static {
            new JNull$();
        }
        
        @Override
        public Null$ values() {
            return null;
        }
        
        @Override
        public String productPrefix() {
            return "JNull";
        }
        
        public int productArity() {
            return 0;
        }
        
        public Object productElement(final int x$1) {
            throw new IndexOutOfBoundsException(BoxesRunTime.boxToInteger(x$1).toString());
        }
        
        @Override
        public Iterator<Object> productIterator() {
            return (Iterator<Object>)ScalaRunTime$.MODULE$.typedProductIterator((Product)this);
        }
        
        public boolean canEqual(final Object x$1) {
            return x$1 instanceof JNull$;
        }
        
        @Override
        public int hashCode() {
            return 70780145;
        }
        
        @Override
        public String toString() {
            return "JNull";
        }
        
        private Object readResolve() {
            return JNull$.MODULE$;
        }
        
        public JNull$() {
            MODULE$ = this;
        }
    }
    
    public static class JString extends JValue
    {
        private final String s;
        
        public String s() {
            return this.s;
        }
        
        @Override
        public String values() {
            return this.s();
        }
        
        public JString copy(final String s) {
            return new JString(s);
        }
        
        public String copy$default$1() {
            return this.s();
        }
        
        @Override
        public String productPrefix() {
            return "JString";
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
                    return this.s();
                }
            }
        }
        
        @Override
        public Iterator<Object> productIterator() {
            return (Iterator<Object>)ScalaRunTime$.MODULE$.typedProductIterator((Product)this);
        }
        
        public boolean canEqual(final Object x$1) {
            return x$1 instanceof JString;
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
                if (x$1 instanceof JString) {
                    final JString string = (JString)x$1;
                    final String s = this.s();
                    final String s2 = string.s();
                    boolean b = false;
                    Label_0077: {
                        Label_0076: {
                            if (s == null) {
                                if (s2 != null) {
                                    break Label_0076;
                                }
                            }
                            else if (!s.equals(s2)) {
                                break Label_0076;
                            }
                            if (string.canEqual(this)) {
                                b = true;
                                break Label_0077;
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
        
        public JString(final String s) {
            this.s = s;
        }
    }
    
    public static class JString$ extends AbstractFunction1<String, JString> implements Serializable
    {
        public static final JString$ MODULE$;
        
        static {
            new JString$();
        }
        
        public final String toString() {
            return "JString";
        }
        
        public JString apply(final String s) {
            return new JString(s);
        }
        
        public Option<String> unapply(final JString x$0) {
            return (Option<String>)((x$0 == null) ? None$.MODULE$ : new Some((Object)x$0.s()));
        }
        
        private Object readResolve() {
            return JString$.MODULE$;
        }
        
        public JString$() {
            MODULE$ = this;
        }
    }
    
    public static class JDouble extends JValue implements JNumber
    {
        private final double num;
        
        public double num() {
            return this.num;
        }
        
        @Override
        public double values() {
            return this.num();
        }
        
        public JDouble copy(final double num) {
            return new JDouble(num);
        }
        
        public double copy$default$1() {
            return this.num();
        }
        
        @Override
        public String productPrefix() {
            return "JDouble";
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
                    return BoxesRunTime.boxToDouble(this.num());
                }
            }
        }
        
        @Override
        public Iterator<Object> productIterator() {
            return (Iterator<Object>)ScalaRunTime$.MODULE$.typedProductIterator((Product)this);
        }
        
        public boolean canEqual(final Object x$1) {
            return x$1 instanceof JDouble;
        }
        
        @Override
        public int hashCode() {
            return Statics.finalizeHash(Statics.mix(-889275714, Statics.doubleHash(this.num())), 1);
        }
        
        @Override
        public String toString() {
            return ScalaRunTime$.MODULE$._toString((Product)this);
        }
        
        @Override
        public boolean equals(final Object x$1) {
            if (this != x$1) {
                if (x$1 instanceof JDouble) {
                    final JDouble double1 = (JDouble)x$1;
                    if (this.num() == double1.num() && double1.canEqual(this)) {
                        return true;
                    }
                }
                return false;
            }
            return true;
        }
        
        public JDouble(final double num) {
            this.num = num;
        }
    }
    
    public static class JDouble$ extends AbstractFunction1<Object, JDouble> implements Serializable
    {
        public static final JDouble$ MODULE$;
        
        static {
            new JDouble$();
        }
        
        public final String toString() {
            return "JDouble";
        }
        
        public JDouble apply(final double num) {
            return new JDouble(num);
        }
        
        public Option<Object> unapply(final JDouble x$0) {
            return (Option<Object>)((x$0 == null) ? None$.MODULE$ : new Some((Object)BoxesRunTime.boxToDouble(x$0.num())));
        }
        
        private Object readResolve() {
            return JDouble$.MODULE$;
        }
        
        public JDouble$() {
            MODULE$ = this;
        }
    }
    
    public static class JDecimal extends JValue implements JNumber
    {
        private final BigDecimal num;
        
        public BigDecimal num() {
            return this.num;
        }
        
        @Override
        public BigDecimal values() {
            return this.num();
        }
        
        public JDecimal copy(final BigDecimal num) {
            return new JDecimal(num);
        }
        
        public BigDecimal copy$default$1() {
            return this.num();
        }
        
        @Override
        public String productPrefix() {
            return "JDecimal";
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
                    return this.num();
                }
            }
        }
        
        @Override
        public Iterator<Object> productIterator() {
            return (Iterator<Object>)ScalaRunTime$.MODULE$.typedProductIterator((Product)this);
        }
        
        public boolean canEqual(final Object x$1) {
            return x$1 instanceof JDecimal;
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
                if (x$1 instanceof JDecimal) {
                    final JDecimal decimal = (JDecimal)x$1;
                    final BigDecimal num = this.num();
                    final BigDecimal num2 = decimal.num();
                    boolean b = false;
                    Label_0077: {
                        Label_0076: {
                            if (num == null) {
                                if (num2 != null) {
                                    break Label_0076;
                                }
                            }
                            else if (!num.equals(num2)) {
                                break Label_0076;
                            }
                            if (decimal.canEqual(this)) {
                                b = true;
                                break Label_0077;
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
        
        public JDecimal(final BigDecimal num) {
            this.num = num;
        }
    }
    
    public static class JDecimal$ extends AbstractFunction1<BigDecimal, JDecimal> implements Serializable
    {
        public static final JDecimal$ MODULE$;
        
        static {
            new JDecimal$();
        }
        
        public final String toString() {
            return "JDecimal";
        }
        
        public JDecimal apply(final BigDecimal num) {
            return new JDecimal(num);
        }
        
        public Option<BigDecimal> unapply(final JDecimal x$0) {
            return (Option<BigDecimal>)((x$0 == null) ? None$.MODULE$ : new Some((Object)x$0.num()));
        }
        
        private Object readResolve() {
            return JDecimal$.MODULE$;
        }
        
        public JDecimal$() {
            MODULE$ = this;
        }
    }
    
    public static class JLong extends JValue implements JNumber
    {
        private final long num;
        
        public long num() {
            return this.num;
        }
        
        @Override
        public long values() {
            return this.num();
        }
        
        public JLong copy(final long num) {
            return new JLong(num);
        }
        
        public long copy$default$1() {
            return this.num();
        }
        
        @Override
        public String productPrefix() {
            return "JLong";
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
                    return BoxesRunTime.boxToLong(this.num());
                }
            }
        }
        
        @Override
        public Iterator<Object> productIterator() {
            return (Iterator<Object>)ScalaRunTime$.MODULE$.typedProductIterator((Product)this);
        }
        
        public boolean canEqual(final Object x$1) {
            return x$1 instanceof JLong;
        }
        
        @Override
        public int hashCode() {
            return Statics.finalizeHash(Statics.mix(-889275714, Statics.longHash(this.num())), 1);
        }
        
        @Override
        public String toString() {
            return ScalaRunTime$.MODULE$._toString((Product)this);
        }
        
        @Override
        public boolean equals(final Object x$1) {
            if (this != x$1) {
                if (x$1 instanceof JLong) {
                    final JLong long1 = (JLong)x$1;
                    if (this.num() == long1.num() && long1.canEqual(this)) {
                        return true;
                    }
                }
                return false;
            }
            return true;
        }
        
        public JLong(final long num) {
            this.num = num;
        }
    }
    
    public static class JLong$ extends AbstractFunction1<Object, JLong> implements Serializable
    {
        public static final JLong$ MODULE$;
        
        static {
            new JLong$();
        }
        
        public final String toString() {
            return "JLong";
        }
        
        public JLong apply(final long num) {
            return new JLong(num);
        }
        
        public Option<Object> unapply(final JLong x$0) {
            return (Option<Object>)((x$0 == null) ? None$.MODULE$ : new Some((Object)BoxesRunTime.boxToLong(x$0.num())));
        }
        
        private Object readResolve() {
            return JLong$.MODULE$;
        }
        
        public JLong$() {
            MODULE$ = this;
        }
    }
    
    public static class JInt extends JValue implements JNumber
    {
        private final BigInt num;
        
        public BigInt num() {
            return this.num;
        }
        
        @Override
        public BigInt values() {
            return this.num();
        }
        
        public JInt copy(final BigInt num) {
            return new JInt(num);
        }
        
        public BigInt copy$default$1() {
            return this.num();
        }
        
        @Override
        public String productPrefix() {
            return "JInt";
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
                    return this.num();
                }
            }
        }
        
        @Override
        public Iterator<Object> productIterator() {
            return (Iterator<Object>)ScalaRunTime$.MODULE$.typedProductIterator((Product)this);
        }
        
        public boolean canEqual(final Object x$1) {
            return x$1 instanceof JInt;
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
                if (x$1 instanceof JInt) {
                    final JInt int1 = (JInt)x$1;
                    final BigInt num = this.num();
                    final BigInt num2 = int1.num();
                    boolean b = false;
                    Label_0077: {
                        Label_0076: {
                            if (num == null) {
                                if (num2 != null) {
                                    break Label_0076;
                                }
                            }
                            else if (!num.equals(num2)) {
                                break Label_0076;
                            }
                            if (int1.canEqual(this)) {
                                b = true;
                                break Label_0077;
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
        
        public JInt(final BigInt num) {
            this.num = num;
        }
    }
    
    public static class JInt$ extends AbstractFunction1<BigInt, JInt> implements Serializable
    {
        public static final JInt$ MODULE$;
        
        static {
            new JInt$();
        }
        
        public final String toString() {
            return "JInt";
        }
        
        public JInt apply(final BigInt num) {
            return new JInt(num);
        }
        
        public Option<BigInt> unapply(final JInt x$0) {
            return (Option<BigInt>)((x$0 == null) ? None$.MODULE$ : new Some((Object)x$0.num()));
        }
        
        private Object readResolve() {
            return JInt$.MODULE$;
        }
        
        public JInt$() {
            MODULE$ = this;
        }
    }
    
    public static class JBool extends JValue
    {
        private final boolean value;
        
        public boolean value() {
            return this.value;
        }
        
        @Override
        public boolean values() {
            return this.value();
        }
        
        public JBool copy(final boolean value) {
            return new JBool(value);
        }
        
        public boolean copy$default$1() {
            return this.value();
        }
        
        @Override
        public String productPrefix() {
            return "JBool";
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
                    return BoxesRunTime.boxToBoolean(this.value());
                }
            }
        }
        
        @Override
        public Iterator<Object> productIterator() {
            return (Iterator<Object>)ScalaRunTime$.MODULE$.typedProductIterator((Product)this);
        }
        
        public boolean canEqual(final Object x$1) {
            return x$1 instanceof JBool;
        }
        
        @Override
        public int hashCode() {
            return Statics.finalizeHash(Statics.mix(-889275714, this.value() ? 1231 : 1237), 1);
        }
        
        @Override
        public String toString() {
            return ScalaRunTime$.MODULE$._toString((Product)this);
        }
        
        @Override
        public boolean equals(final Object x$1) {
            if (this != x$1) {
                if (x$1 instanceof JBool) {
                    final JBool bool = (JBool)x$1;
                    if (this.value() == bool.value() && bool.canEqual(this)) {
                        return true;
                    }
                }
                return false;
            }
            return true;
        }
        
        public JBool(final boolean value) {
            this.value = value;
        }
    }
    
    public static class JBool$ implements Serializable
    {
        public static final JBool$ MODULE$;
        private final JBool True;
        private final JBool False;
        
        static {
            new JBool$();
        }
        
        public JBool True() {
            return this.True;
        }
        
        public JBool False() {
            return this.False;
        }
        
        public JBool apply(final boolean value) {
            return new JBool(value);
        }
        
        public Option<Object> unapply(final JBool x$0) {
            return (Option<Object>)((x$0 == null) ? None$.MODULE$ : new Some((Object)BoxesRunTime.boxToBoolean(x$0.value())));
        }
        
        private Object readResolve() {
            return JBool$.MODULE$;
        }
        
        public JBool$() {
            MODULE$ = this;
            this.True = new JBool(true);
            this.False = new JBool(false);
        }
    }
    
    public static class JObject$ implements Product, Serializable
    {
        public static final JObject$ MODULE$;
        
        static {
            new JObject$();
        }
        
        public JObject apply(final Seq<Tuple2<String, JValue>> fs) {
            return new JObject((List<Tuple2<String, JValue>>)fs.toList());
        }
        
        public JObject apply(final List<Tuple2<String, JValue>> obj) {
            return new JObject(obj);
        }
        
        public Option<List<Tuple2<String, JValue>>> unapply(final JObject x$0) {
            return (Option<List<Tuple2<String, JValue>>>)((x$0 == null) ? None$.MODULE$ : new Some((Object)x$0.obj()));
        }
        
        public String productPrefix() {
            return "JObject";
        }
        
        public int productArity() {
            return 0;
        }
        
        public Object productElement(final int x$1) {
            throw new IndexOutOfBoundsException(BoxesRunTime.boxToInteger(x$1).toString());
        }
        
        public Iterator<Object> productIterator() {
            return (Iterator<Object>)ScalaRunTime$.MODULE$.typedProductIterator((Product)this);
        }
        
        public boolean canEqual(final Object x$1) {
            return x$1 instanceof JObject$;
        }
        
        @Override
        public int hashCode() {
            return -688738263;
        }
        
        @Override
        public String toString() {
            return "JObject";
        }
        
        private Object readResolve() {
            return JObject$.MODULE$;
        }
        
        public JObject$() {
            Product$class.$init$((Product)(MODULE$ = this));
        }
    }
    
    public static class JObject extends JValue
    {
        private final List<Tuple2<String, JValue>> obj;
        
        public List<Tuple2<String, JValue>> obj() {
            return this.obj;
        }
        
        @Override
        public Map<String, Object> values() {
            return (Map<String, Object>)this.obj().iterator().map((Function1)new JsonAST$JObject$$anonfun$values.JsonAST$JObject$$anonfun$values$1(this)).toMap(Predef$.MODULE$.$conforms());
        }
        
        @Override
        public boolean equals(final Object that) {
            boolean b2;
            if (that instanceof JObject) {
                final JObject object = (JObject)that;
                final Set set = this.obj().toSet();
                final Set set2 = object.obj().toSet();
                boolean b = false;
                Label_0056: {
                    Label_0055: {
                        if (set == null) {
                            if (set2 != null) {
                                break Label_0055;
                            }
                        }
                        else if (!set.equals(set2)) {
                            break Label_0055;
                        }
                        b = true;
                        break Label_0056;
                    }
                    b = false;
                }
                b2 = b;
            }
            else {
                b2 = false;
            }
            return b2;
        }
        
        @Override
        public int hashCode() {
            return this.obj().toSet().hashCode();
        }
        
        public JObject copy(final List<Tuple2<String, JValue>> obj) {
            return new JObject(obj);
        }
        
        public List<Tuple2<String, JValue>> copy$default$1() {
            return this.obj();
        }
        
        @Override
        public String productPrefix() {
            return "JObject";
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
                    return this.obj();
                }
            }
        }
        
        @Override
        public Iterator<Object> productIterator() {
            return (Iterator<Object>)ScalaRunTime$.MODULE$.typedProductIterator((Product)this);
        }
        
        public boolean canEqual(final Object x$1) {
            return x$1 instanceof JObject;
        }
        
        @Override
        public String toString() {
            return ScalaRunTime$.MODULE$._toString((Product)this);
        }
        
        public JObject(final List<Tuple2<String, JValue>> obj) {
            this.obj = obj;
        }
    }
    
    public static class JArray extends JValue
    {
        private final List<JValue> arr;
        
        public List<JValue> arr() {
            return this.arr;
        }
        
        @Override
        public List<Object> values() {
            return (List<Object>)this.arr().map((Function1)new JsonAST$JArray$$anonfun$values.JsonAST$JArray$$anonfun$values$2(this), List$.MODULE$.canBuildFrom());
        }
        
        @Override
        public JValue apply(final int i) {
            return (JValue)this.arr().apply(i);
        }
        
        public JArray copy(final List<JValue> arr) {
            return new JArray(arr);
        }
        
        public List<JValue> copy$default$1() {
            return this.arr();
        }
        
        @Override
        public String productPrefix() {
            return "JArray";
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
                    return this.arr();
                }
            }
        }
        
        @Override
        public Iterator<Object> productIterator() {
            return (Iterator<Object>)ScalaRunTime$.MODULE$.typedProductIterator((Product)this);
        }
        
        public boolean canEqual(final Object x$1) {
            return x$1 instanceof JArray;
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
                if (x$1 instanceof JArray) {
                    final JArray array = (JArray)x$1;
                    final List<JValue> arr = this.arr();
                    final List<JValue> arr2 = array.arr();
                    boolean b = false;
                    Label_0077: {
                        Label_0076: {
                            if (arr == null) {
                                if (arr2 != null) {
                                    break Label_0076;
                                }
                            }
                            else if (!arr.equals(arr2)) {
                                break Label_0076;
                            }
                            if (array.canEqual(this)) {
                                b = true;
                                break Label_0077;
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
        
        public JArray(final List<JValue> arr) {
            this.arr = arr;
        }
    }
    
    public static class JArray$ extends AbstractFunction1<List<JValue>, JArray> implements Serializable
    {
        public static final JArray$ MODULE$;
        
        static {
            new JArray$();
        }
        
        public final String toString() {
            return "JArray";
        }
        
        public JArray apply(final List<JValue> arr) {
            return new JArray(arr);
        }
        
        public Option<List<JValue>> unapply(final JArray x$0) {
            return (Option<List<JValue>>)((x$0 == null) ? None$.MODULE$ : new Some((Object)x$0.arr()));
        }
        
        private Object readResolve() {
            return JArray$.MODULE$;
        }
        
        public JArray$() {
            MODULE$ = this;
        }
    }
    
    public static class JSet extends JValue
    {
        private final Set<JValue> set;
        
        public Set<JValue> set() {
            return this.set;
        }
        
        @Override
        public Set<JValue> values() {
            return this.set();
        }
        
        @Override
        public boolean equals(final Object o) {
            boolean b2;
            if (o instanceof JSet) {
                final Set<JValue> values = ((JSet)o).values();
                final Set<JValue> values2 = this.values();
                boolean b = false;
                Label_0050: {
                    Label_0049: {
                        if (values == null) {
                            if (values2 != null) {
                                break Label_0049;
                            }
                        }
                        else if (!values.equals(values2)) {
                            break Label_0049;
                        }
                        b = true;
                        break Label_0050;
                    }
                    b = false;
                }
                b2 = b;
            }
            else {
                b2 = false;
            }
            return b2;
        }
        
        public JSet intersect(final JSet o) {
            return new JSet((Set<JValue>)o.values().intersect((GenSet)this.values()));
        }
        
        public JSet union(final JSet o) {
            return new JSet((Set<JValue>)o.values().union((GenSet)this.values()));
        }
        
        public JSet difference(final JSet o) {
            return new JSet((Set<JValue>)this.values().diff((GenSet)o.values()));
        }
        
        public JSet copy(final Set<JValue> set) {
            return new JSet(set);
        }
        
        public Set<JValue> copy$default$1() {
            return this.set();
        }
        
        @Override
        public String productPrefix() {
            return "JSet";
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
                    return this.set();
                }
            }
        }
        
        @Override
        public Iterator<Object> productIterator() {
            return (Iterator<Object>)ScalaRunTime$.MODULE$.typedProductIterator((Product)this);
        }
        
        public boolean canEqual(final Object x$1) {
            return x$1 instanceof JSet;
        }
        
        @Override
        public int hashCode() {
            return ScalaRunTime$.MODULE$._hashCode((Product)this);
        }
        
        @Override
        public String toString() {
            return ScalaRunTime$.MODULE$._toString((Product)this);
        }
        
        public JSet(final Set<JValue> set) {
            this.set = set;
        }
    }
    
    public static class JSet$ extends AbstractFunction1<Set<JValue>, JSet> implements Serializable
    {
        public static final JSet$ MODULE$;
        
        static {
            new JSet$();
        }
        
        public final String toString() {
            return "JSet";
        }
        
        public JSet apply(final Set<JValue> set) {
            return new JSet(set);
        }
        
        public Option<Set<JValue>> unapply(final JSet x$0) {
            return (Option<Set<JValue>>)((x$0 == null) ? None$.MODULE$ : new Some((Object)x$0.set()));
        }
        
        private Object readResolve() {
            return JSet$.MODULE$;
        }
        
        public JSet$() {
            MODULE$ = this;
        }
    }
    
    public static class JField$
    {
        public static final JField$ MODULE$;
        
        static {
            new JField$();
        }
        
        public Tuple2<String, JValue> apply(final String name, final JValue value) {
            return (Tuple2<String, JValue>)new Tuple2((Object)name, (Object)value);
        }
        
        public Option<Tuple2<String, JValue>> unapply(final Tuple2<String, JValue> f) {
            return (Option<Tuple2<String, JValue>>)new Some((Object)f);
        }
        
        public JField$() {
            MODULE$ = this;
        }
    }
    
    public interface JNumber
    {
    }
}
