// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.reflect;

import scala.collection.Seq;
import org.json4s.scalap.scalasig.ClassSymbol;
import scala.collection.immutable.List;
import org.json4s.scalap.scalasig.MethodSymbol;
import org.json4s.scalap.scalasig.ScalaSig;
import scala.collection.immutable.Vector;
import scala.Tuple2;
import scala.Option;
import scala.collection.Iterable;
import scala.reflect.ScalaSignature;

@ScalaSignature(bytes = "\u0006\u0001\r]t!B\u0001\u0003\u0011\u0003I\u0011AD*dC2\f7+[4SK\u0006$WM\u001d\u0006\u0003\u0007\u0011\tqA]3gY\u0016\u001cGO\u0003\u0002\u0006\r\u00051!n]8oiMT\u0011aB\u0001\u0004_J<7\u0001\u0001\t\u0003\u0015-i\u0011A\u0001\u0004\u0006\u0019\tA\t!\u0004\u0002\u000f'\u000e\fG.Y*jOJ+\u0017\rZ3s'\tYa\u0002\u0005\u0002\u0010%5\t\u0001CC\u0001\u0012\u0003\u0015\u00198-\u00197b\u0013\t\u0019\u0002C\u0001\u0004B]f\u0014VM\u001a\u0005\u0006+-!\tAF\u0001\u0007y%t\u0017\u000e\u001e \u0015\u0003%Aa\u0001G\u0006!\u0002\u0013I\u0012!\u00047pG\u0006d\u0007+\u0019;i\u001b\u0016lw\u000e\u0005\u0003\u001b;\u0001:cB\u0001\u0006\u001c\u0013\ta\"!A\u0004qC\u000e\\\u0017mZ3\n\u0005yy\"\u0001B'f[>T!\u0001\b\u0002\u0011\u0005\u0005\"cBA\b#\u0013\t\u0019\u0003#\u0001\u0004Qe\u0016$WMZ\u0005\u0003K\u0019\u0012aa\u0015;sS:<'BA\u0012\u0011!\ry\u0001FK\u0005\u0003SA\u0011aa\u00149uS>t\u0007GA\u00166!\ra\u0013gM\u0007\u0002[)\u0011afL\u0001\u0005Y\u0006twMC\u00011\u0003\u0011Q\u0017M^1\n\u0005Ij#!B\"mCN\u001c\bC\u0001\u001b6\u0019\u0001!\u0011BN\f\u0002\u0002\u0003\u0005)\u0011A\u001c\u0003\u0007}#\u0013'\u0005\u00029wA\u0011q\"O\u0005\u0003uA\u0011qAT8uQ&tw\r\u0005\u0002\u0010y%\u0011Q\b\u0005\u0002\u0004\u0003:L\bBB \fA\u0003%\u0001)\u0001\bsK6|G/\u001a)bi\"lU-\\8\u0011\tii\u0012I\u0015\t\u0005\u001f\t\u0003C)\u0003\u0002D!\t1A+\u001e9mKJ\u00022!\u0012'P\u001d\t15J\u0004\u0002H\u00156\t\u0001J\u0003\u0002J\u0011\u00051AH]8pizJ\u0011!E\u0005\u00039AI!!\u0014(\u0003\u0011%#XM]1cY\u0016T!\u0001\b\t\u0011\u00051\u0002\u0016BA).\u0005-\u0019E.Y:t\u0019>\fG-\u001a:\u0011\u0007=A3\u000b\r\u0002U-B\u0019A&M+\u0011\u0005Q2F!C,?\u0003\u0003\u0005\tQ!\u00018\u0005\ryFE\r\u0005\u00063.!\tAW\u0001\u0010e\u0016\fGmQ8ogR\u0014Xo\u0019;peR)1,Y2k_B\u0012Al\u0018\t\u0004Cus\u0016B\u0001\u001a'!\t!t\fB\u0005a1\u0006\u0005\t\u0011!B\u0001o\t\u0019q\f\n\u001b\t\u000b\tD\u0006\u0019\u0001\u0011\u0002\u000f\u0005\u0014xMT1nK\")A\r\u0017a\u0001K\u0006)1\r\\1{uB\u0012a\r\u001b\t\u0004Cu;\u0007C\u0001\u001bi\t%I7-!A\u0001\u0002\u000b\u0005qGA\u0002`IMBQa\u001b-A\u00021\fA\u0002^=qK\u0006\u0013x-\u00138eKb\u0004\"aD7\n\u00059\u0004\"aA%oi\")\u0001\u000f\u0017a\u0001c\u0006A\u0011M]4OC6,7\u000fE\u0002Fe\u0002J!a\u001d(\u0003\t1K7\u000f\u001e\u0005\u00063.!\t!\u001e\u000b\bmnd\u0018QAA\u0006a\t9\u0018\u0010E\u0002\";b\u0004\"\u0001N=\u0005\u0013i$\u0018\u0011!A\u0001\u0006\u00039$aA0%m!)!\r\u001ea\u0001A!)A\r\u001ea\u0001{B\u001aa0!\u0001\u0011\u0007\u0005jv\u0010E\u00025\u0003\u0003!!\"a\u0001}\u0003\u0003\u0005\tQ!\u00018\u0005\ryF%\u000e\u0005\b\u0003\u000f!\b\u0019AA\u0005\u00039!\u0018\u0010]3Be\u001eLe\u000eZ3yKN\u00042!\u0012:m\u0011\u0015\u0001H\u000f1\u0001r\u0011\u0019I6\u0002\"\u0001\u0002\u0010QQ\u0011\u0011CA\u000e\u0003;\t)#a\n1\t\u0005M\u0011q\u0003\t\u0005Cu\u000b)\u0002E\u00025\u0003/!1\"!\u0007\u0002\u000e\u0005\u0005\t\u0011!B\u0001o\t\u0019q\fJ\u001c\t\r\t\fi\u00011\u0001!\u0011\u001d!\u0017Q\u0002a\u0001\u0003?\u00012ACA\u0011\u0013\r\t\u0019C\u0001\u0002\n'\u000e\fG.\u0019+za\u0016Daa[A\u0007\u0001\u0004a\u0007B\u00029\u0002\u000e\u0001\u0007\u0011\u000f\u0003\u0004Z\u0017\u0011\u0005\u00111\u0006\u000b\u000b\u0003[\t9$!\u000f\u0002<\u0005u\u0002\u0007BA\u0018\u0003g\u0001B!I/\u00022A\u0019A'a\r\u0005\u0017\u0005U\u0012\u0011FA\u0001\u0002\u0003\u0015\ta\u000e\u0002\u0004?\u0012B\u0004B\u00022\u0002*\u0001\u0007\u0001\u0005C\u0004e\u0003S\u0001\r!a\b\t\u0011\u0005\u001d\u0011\u0011\u0006a\u0001\u0003\u0013Aa\u0001]A\u0015\u0001\u0004\t\bbBA!\u0017\u0011\u0005\u00111I\u0001\ne\u0016\fGMR5fY\u0012$\u0002\"!\u0012\u0002P\u0005M\u0013q\f\u0019\u0005\u0003\u000f\nY\u0005\u0005\u0003\";\u0006%\u0003c\u0001\u001b\u0002L\u0011Y\u0011QJA \u0003\u0003\u0005\tQ!\u00018\u0005\u0011yF%\r\u0019\t\u000f\u0005E\u0013q\ba\u0001A\u0005!a.Y7f\u0011\u001d!\u0017q\ba\u0001\u0003+\u0002D!a\u0016\u0002\\A!\u0011%XA-!\r!\u00141\f\u0003\f\u0003;\n\u0019&!A\u0001\u0002\u000b\u0005qGA\u0002`IeBaa[A \u0001\u0004a\u0007bBA2\u0017\u0011\u0005\u0011QM\u0001\nM&tGm\u00117bgN$B!a\u001a\u0002xA!\u0011\u0011NA:\u001b\t\tYG\u0003\u0003\u0002n\u0005=\u0014\u0001C:dC2\f7/[4\u000b\u0007\u0005ED!\u0001\u0004tG\u0006d\u0017\r]\u0005\u0005\u0003k\nYGA\u0006DY\u0006\u001c8oU=nE>d\u0007b\u00023\u0002b\u0001\u0007\u0011\u0011\u0010\u0019\u0005\u0003w\ny\b\u0005\u0003\";\u0006u\u0004c\u0001\u001b\u0002\u0000\u0011Y\u0011\u0011QA<\u0003\u0003\u0005\tQ!\u00018\u0005\u0011yF%\r\u001a\t\u000f\u0005\r4\u0002\"\u0001\u0002\u0006R1\u0011qQAE\u0003'\u0003Ba\u0004\u0015\u0002h!A\u00111RAB\u0001\u0004\ti)A\u0002tS\u001e\u0004B!!\u001b\u0002\u0010&!\u0011\u0011SA6\u0005!\u00196-\u00197b'&<\u0007b\u00023\u0002\u0004\u0002\u0007\u0011Q\u0013\u0019\u0005\u0003/\u000bY\n\u0005\u0003\";\u0006e\u0005c\u0001\u001b\u0002\u001c\u0012Y\u0011QTAJ\u0003\u0003\u0005\tQ!\u00018\u0005\u0011yF%M\u001a\t\u000f\u0005\u00056\u0002\"\u0001\u0002$\u0006\u0019b-\u001b8e\u0007>l\u0007/\u00198j_:|%M[3diR!\u0011qMAS\u0011\u001d!\u0017q\u0014a\u0001\u0003O\u0003D!!+\u0002.B!\u0011%XAV!\r!\u0014Q\u0016\u0003\f\u0003_\u000b)+!A\u0001\u0002\u000b\u0005qG\u0001\u0003`IE\"\u0004bBAQ\u0017\u0011\u0005\u00111\u0017\u000b\u0007\u0003\u000f\u000b),a.\t\u0011\u0005-\u0015\u0011\u0017a\u0001\u0003\u001bCq\u0001ZAY\u0001\u0004\tI\f\r\u0003\u0002<\u0006}\u0006\u0003B\u0011^\u0003{\u00032\u0001NA`\t-\t\t-a.\u0002\u0002\u0003\u0005)\u0011A\u001c\u0003\t}#\u0013'\u000e\u0005\b\u0003\u000b\\A\u0011AAd\u0003=1\u0017N\u001c3D_:\u001cHO];di>\u0014HCBAe\u0003#\f)\u000e\u0005\u0003\u0010Q\u0005-\u0007\u0003BA5\u0003\u001bLA!a4\u0002l\taQ*\u001a;i_\u0012\u001c\u00160\u001c2pY\"A\u00111[Ab\u0001\u0004\t9'A\u0001d\u0011\u0019\u0001\u00181\u0019a\u0001c\"9\u0011\u0011\\\u0006\u0005\u0002\u0005m\u0017!\u00034j]\u0012\f\u0005\u000f\u001d7z)\u0019\tI-!8\u0002`\"A\u00111[Al\u0001\u0004\t9\u0007\u0003\u0004q\u0003/\u0004\r!\u001d\u0005\b\u0003G\\A\u0011AAs\u0003)1\u0017N\u001c3GS\u0016dGm\u001d\u000b\u0005\u0003O\fi\u000fE\u0003F\u0003S\fY-C\u0002\u0002l:\u00131aU3r\u0011!\t\u0019.!9A\u0002\u0005\u001d\u0004bBAy\u0017\u0011%\u00111_\u0001\nM&tGMR5fY\u0012$b!!3\u0002v\n\u0005\u0001b\u00023\u0002p\u0002\u0007\u0011q\u001f\u0019\u0005\u0003s\fi\u0010\u0005\u0003\";\u0006m\bc\u0001\u001b\u0002~\u0012Y\u0011q`A{\u0003\u0003\u0005\tQ!\u00018\u0005\u0011yF%\r\u001c\t\u000f\u0005E\u0013q\u001ea\u0001A!9\u0011\u0011_\u0006\u0005\n\t\u0015ACBAe\u0005\u000f\u0011I\u0001\u0003\u0005\u0002T\n\r\u0001\u0019AA4\u0011\u001d\t\tFa\u0001A\u0002\u0001BqA!\u0004\f\t\u0003\u0011y!A\u0006gS:$\u0017I]4UsB,G\u0003\u0003B\t\u00057\u0011yBa\t1\t\tM!q\u0003\t\u0005Cu\u0013)\u0002E\u00025\u0005/!1B!\u0007\u0003\f\u0005\u0005\t\u0011!B\u0001o\t!q\fJ\u00198\u0011!\u0011iBa\u0003A\u0002\u0005-\u0017!A:\t\u000f\t\u0005\"1\u0002a\u0001Y\u00061\u0011M]4JIbDaa\u001bB\u0006\u0001\u0004a\u0007b\u0002B\u0007\u0017\u0011\u0005!q\u0005\u000b\t\u0005S\u0011\u0019D!\u000e\u00038A\"!1\u0006B\u0018!\u0011\tSL!\f\u0011\u0007Q\u0012y\u0003B\u0006\u00032\t\u0015\u0012\u0011!A\u0001\u0006\u00039$\u0001B0%caB\u0001B!\b\u0003&\u0001\u0007\u00111\u001a\u0005\b\u0005C\u0011)\u00031\u0001m\u0011!\t9A!\nA\u0002\u0005%\u0001b\u0002B\u001e\u0017\u0011%!QH\u0001\u0014M&tG-\u0011:h)f\u0004XMR8s\r&,G\u000e\u001a\u000b\u0007\u0005\u007f\u0011IEa\u00131\t\t\u0005#Q\t\t\u0005Cu\u0013\u0019\u0005E\u00025\u0005\u000b\"1Ba\u0012\u0003:\u0005\u0005\t\u0011!B\u0001o\t!q\fJ\u0019:\u0011!\u0011iB!\u000fA\u0002\u0005-\u0007b\u0002B'\u0005s\u0001\r\u0001\\\u0001\u000bif\u0004X-\u0011:h\u0013\u0012D\bb\u0002B)\u0017\u0011%!1K\u0001\bi>\u001cE.Y:t)\u0011\u0011)Fa#1\t\t]#1\f\t\u0005YE\u0012I\u0006E\u00025\u00057\"AB!\u0018\u0003P\u0005\u0005\t\u0011!B\u0001\u0005?\u00121aX\u00196#\r\u0011\tg\u000f\n\u0011\u0005G\u00129\u0007\u001cB7\u0005g\u0012IHa \u0003\u0006:1aA!\u001a\u0001\u0001\t\u0005$\u0001\u0004\u001fsK\u001aLg.Z7f]Rt\u0004cA\b\u0003j%\u0019!1\u000e\t\u0003\u000bMCwN\u001d;\u0011\u0007=\u0011y'C\u0002\u0003rA\u0011A\u0001T8oOB\u0019qB!\u001e\n\u0007\t]\u0004CA\u0004C_>dW-\u00198\u0011\u0007=\u0011Y(C\u0002\u0003~A\u0011QA\u00127pCR\u00042a\u0004BA\u0013\r\u0011\u0019\t\u0005\u0002\u0007\t>,(\r\\3\u0011\u0007=\u00119)C\u0002\u0003\nB\u0011AAQ=uK\"A!Q\u0004B(\u0001\u0004\u0011i\t\u0005\u0003\u0002j\t=\u0015\u0002\u0002BI\u0003W\u0012aaU=nE>d\u0007\u0002\u0003BK\u0017\u0001&IAa&\u0002\u0017%\u001c\bK]5nSRLg/\u001a\u000b\u0005\u0005g\u0012I\n\u0003\u0005\u0003\u001e\tM\u0005\u0019\u0001BG\u0011\u001d\u0011ij\u0003C\u0001\u0005?\u000bABZ5oIN\u001b\u0017\r\\1TS\u001e$BA!)\u0003$B!q\u0002KAG\u0011\u001d!'1\u0014a\u0001\u0005K\u0003DAa*\u0003,B!\u0011%\u0018BU!\r!$1\u0016\u0003\f\u0005[\u0013\u0019+!A\u0001\u0002\u000b\u0005qG\u0001\u0003`II\u0002\u0004\u0002\u0003BY\u0017\u0001&IAa-\u00025A\f'o]3DY\u0006\u001c8OR5mK\u001a\u0013x.\u001c\"zi\u0016\u001cu\u000eZ3\u0015\t\t\u0005&Q\u0017\u0005\bI\n=\u0006\u0019\u0001B\\a\u0011\u0011IL!0\u0011\t\u0005j&1\u0018\t\u0004i\tuFa\u0003B`\u0005k\u000b\t\u0011!A\u0003\u0002]\u0012Aa\u0018\u00133c!I!1Y\u0006C\u0002\u0013\u0005!QY\u0001\u0010\u001b>$W\u000f\\3GS\u0016dGMT1nKV\u0011!q\u0019\t\u0004Y\t%\u0017BA\u0013.\u0011!\u0011im\u0003Q\u0001\n\t\u001d\u0017\u0001E'pIVdWMR5fY\u0012t\u0015-\\3!\u0011%\u0011\tn\u0003b\u0001\n\u0003\u0011)-\u0001\bPkR,'OR5fY\u0012t\u0015-\\3\t\u0011\tU7\u0002)A\u0005\u0005\u000f\fqbT;uKJ4\u0015.\u001a7e\u001d\u0006lW\r\t\u0005\n\u00053\\!\u0019!C\u0001\u00057\fAb\u00117bgNdu.\u00193feN,\"A!8\u0011\u000b\t}'\u0011^(\u000e\u0005\t\u0005(\u0002\u0002Br\u0005K\f\u0011\"[7nkR\f'\r\\3\u000b\u0007\t\u001d\b#\u0001\u0006d_2dWm\u0019;j_:LAAa;\u0003b\n1a+Z2u_JD\u0001Ba<\fA\u0003%!Q\\\u0001\u000e\u00072\f7o\u001d'pC\u0012,'o\u001d\u0011\t\u000f\tM8\u0002\"\u0001\u0003v\u0006Q1m\\7qC:LwN\\:\u0015\u0011\t]81CB\f\u0007;\u0001Ba\u0004\u0015\u0003zB\"!1`B\u0001!\u0019y!I!@\u0004\fA!A&\rB\u0000!\r!4\u0011\u0001\u0003\f\u0007\u0007\u0019)!!A\u0001\u0002\u000b\u0005qG\u0001\u0003`II\u0012\u0004BCB\u0004\u0005c\f\t\u0011!\u0001\u0004\n\u0005AA%\u00198p]\u001a,hn\u0003\u0001\u0011\t=A3Q\u0002\t\u0004Y\r=\u0011bAB\t[\t1qJ\u00196fGRDqa!\u0006\u0003r\u0002\u0007\u0001%A\u0001u\u0011)\u0019IB!=\u0011\u0002\u0003\u000711D\u0001\nG>l\u0007/\u00198j_:\u00042a\u0004\u0015\u000f\u0011%\u0019yB!=\u0011\u0002\u0003\u0007A)\u0001\u0007dY\u0006\u001c8\u000fT8bI\u0016\u00148\u000fC\u0004\u0004$-!\ta!\n\u0002\u0019I,7o\u001c7wK\u000ec\u0017m]:\u0016\t\r\u001d2q\u0006\u000b\u0007\u0007S\u0019)da\u000e\u0011\t=A31\u0006\t\u0005Cu\u001bi\u0003E\u00025\u0007_!\u0001b!\r\u0004\"\t\u000711\u0007\u0002\u00021F\u0011\u0001H\u0004\u0005\b\u0003'\u001c\t\u00031\u0001!\u0011%\u0019yb!\t\u0011\u0002\u0003\u0007A\tC\u0004\u0004<-!Ia!\u0010\u0002%I,7o\u001c7wK\u000ec\u0017m]:DC\u000eDW\rZ\u000b\u0005\u0007\u007f\u00199\u0005\u0006\u0004\u0004B\r%31\n\t\u0005\u001f!\u001a\u0019\u0005\u0005\u0003\";\u000e\u0015\u0003c\u0001\u001b\u0004H\u0011A1\u0011GB\u001d\u0005\u0004\u0019\u0019\u0004C\u0004\u0002T\u000ee\u0002\u0019\u0001\u0011\t\u000f\r}1\u0011\ba\u0001\t\"I1qJ\u0006\u0012\u0002\u0013\u00051\u0011K\u0001\u0015G>l\u0007/\u00198j_:\u001cH\u0005Z3gCVdG\u000f\n\u001a\u0016\u0005\rM#\u0006BB\u000e\u0007+Z#aa\u0016\u0011\t\re31M\u0007\u0003\u00077RAa!\u0018\u0004`\u0005IQO\\2iK\u000e\\W\r\u001a\u0006\u0004\u0007C\u0002\u0012AC1o]>$\u0018\r^5p]&!1QMB.\u0005E)hn\u00195fG.,GMV1sS\u0006t7-\u001a\u0005\n\u0007SZ\u0011\u0013!C\u0001\u0007W\nAcY8na\u0006t\u0017n\u001c8tI\u0011,g-Y;mi\u0012\u001aTCAB7U\r!5Q\u000b\u0005\n\u0007cZ\u0011\u0013!C\u0001\u0007g\naC]3t_24Xm\u00117bgN$C-\u001a4bk2$HEM\u000b\u0005\u0007W\u001a)\b\u0002\u0005\u00042\r=$\u0019AB\u001a\u0001")
public final class ScalaSigReader
{
    public static <X> Iterable<ClassLoader> resolveClass$default$2() {
        return ScalaSigReader$.MODULE$.resolveClass$default$2();
    }
    
    public static Iterable<ClassLoader> companions$default$3() {
        return ScalaSigReader$.MODULE$.companions$default$3();
    }
    
    public static Option<Object> companions$default$2() {
        return ScalaSigReader$.MODULE$.companions$default$2();
    }
    
    public static <X> Option<Class<X>> resolveClass(final String c, final Iterable<ClassLoader> classLoaders) {
        return ScalaSigReader$.MODULE$.resolveClass(c, classLoaders);
    }
    
    public static Option<Tuple2<Class<Object>, Option<Object>>> companions(final String t, final Option<Object> companion, final Iterable<ClassLoader> classLoaders) {
        return ScalaSigReader$.MODULE$.companions(t, companion, classLoaders);
    }
    
    public static Vector<ClassLoader> ClassLoaders() {
        return ScalaSigReader$.MODULE$.ClassLoaders();
    }
    
    public static String OuterFieldName() {
        return ScalaSigReader$.MODULE$.OuterFieldName();
    }
    
    public static String ModuleFieldName() {
        return ScalaSigReader$.MODULE$.ModuleFieldName();
    }
    
    public static Option<ScalaSig> findScalaSig(final Class<?> clazz) {
        return ScalaSigReader$.MODULE$.findScalaSig(clazz);
    }
    
    public static Class<?> findArgType(final MethodSymbol s, final int argIdx, final List<Object> typeArgIndexes) {
        return ScalaSigReader$.MODULE$.findArgType(s, argIdx, typeArgIndexes);
    }
    
    public static Class<?> findArgType(final MethodSymbol s, final int argIdx, final int typeArgIndex) {
        return ScalaSigReader$.MODULE$.findArgType(s, argIdx, typeArgIndex);
    }
    
    public static Seq<MethodSymbol> findFields(final ClassSymbol c) {
        return ScalaSigReader$.MODULE$.findFields(c);
    }
    
    public static Option<MethodSymbol> findApply(final ClassSymbol c, final List<String> argNames) {
        return ScalaSigReader$.MODULE$.findApply(c, argNames);
    }
    
    public static Option<MethodSymbol> findConstructor(final ClassSymbol c, final List<String> argNames) {
        return ScalaSigReader$.MODULE$.findConstructor(c, argNames);
    }
    
    public static Option<ClassSymbol> findCompanionObject(final ScalaSig sig, final Class<?> clazz) {
        return ScalaSigReader$.MODULE$.findCompanionObject(sig, clazz);
    }
    
    public static ClassSymbol findCompanionObject(final Class<?> clazz) {
        return ScalaSigReader$.MODULE$.findCompanionObject(clazz);
    }
    
    public static Option<ClassSymbol> findClass(final ScalaSig sig, final Class<?> clazz) {
        return ScalaSigReader$.MODULE$.findClass(sig, clazz);
    }
    
    public static ClassSymbol findClass(final Class<?> clazz) {
        return ScalaSigReader$.MODULE$.findClass(clazz);
    }
    
    public static Class<?> readField(final String name, final Class<?> clazz, final int typeArgIndex) {
        return ScalaSigReader$.MODULE$.readField(name, clazz, typeArgIndex);
    }
    
    public static Class<?> readConstructor(final String argName, final ScalaType clazz, final List<Object> typeArgIndexes, final List<String> argNames) {
        return ScalaSigReader$.MODULE$.readConstructor(argName, clazz, typeArgIndexes, argNames);
    }
    
    public static Class<?> readConstructor(final String argName, final ScalaType clazz, final int typeArgIndex, final List<String> argNames) {
        return ScalaSigReader$.MODULE$.readConstructor(argName, clazz, typeArgIndex, argNames);
    }
    
    public static Class<?> readConstructor(final String argName, final Class<?> clazz, final List<Object> typeArgIndexes, final List<String> argNames) {
        return ScalaSigReader$.MODULE$.readConstructor(argName, clazz, typeArgIndexes, argNames);
    }
    
    public static Class<?> readConstructor(final String argName, final Class<?> clazz, final int typeArgIndex, final List<String> argNames) {
        return ScalaSigReader$.MODULE$.readConstructor(argName, clazz, typeArgIndex, argNames);
    }
}
