// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.reflect;

import scala.collection.immutable.Nil$;
import scala.collection.immutable.List$;
import scala.collection.immutable.List;
import org.json4s.JsonAST;
import scala.Symbol;
import java.sql.Timestamp;
import java.util.Date;
import scala.math.BigDecimal;
import java.math.BigInteger;
import scala.math.BigInt;
import scala.runtime.ScalaRunTime$;
import java.lang.reflect.Field;
import scala.reflect.ClassTag;
import scala.util.Either;
import scala.Option;
import java.util.Collection;
import scala.collection.Iterable;
import scala.collection.GenIterable;
import scala.Predef;
import scala.reflect.ClassTag$;
import scala.Array$;
import java.lang.reflect.TypeVariable;
import scala.collection.GenTraversableOnce;
import scala.Predef$;
import scala.runtime.BoxedUnit;
import scala.collection.immutable.Iterable$;
import scala.Function1;
import scala.collection.Seq$;
import scala.collection.TraversableOnce;
import scala.collection.mutable.StringBuilder;
import scala.collection.immutable.Map;
import scala.collection.Seq;
import scala.reflect.Manifest;
import scala.reflect.ScalaSignature;
import scala.Equals;

@ScalaSignature(bytes = "\u0006\u0001\r\u0015r!B\u0001\u0003\u0011\u0003I\u0011!C*dC2\fG+\u001f9f\u0015\t\u0019A!A\u0004sK\u001adWm\u0019;\u000b\u0005\u00151\u0011A\u00026t_:$4OC\u0001\b\u0003\ry'oZ\u0002\u0001!\tQ1\"D\u0001\u0003\r\u0015a!\u0001#\u0001\u000e\u0005%\u00196-\u00197b)f\u0004Xm\u0005\u0002\f\u001dA\u0011qBE\u0007\u0002!)\t\u0011#A\u0003tG\u0006d\u0017-\u0003\u0002\u0014!\t1\u0011I\\=SK\u001aDQ!F\u0006\u0005\u0002Y\ta\u0001P5oSRtD#A\u0005\t\u000faY!\u0019!C\u00053\u0005)A/\u001f9fgV\t!\u0004\u0005\u0003\u001c=\u0005\"dB\u0001\u0006\u001d\u0013\ti\"!A\u0004qC\u000e\\\u0017mZ3\n\u0005}\u0001#\u0001B'f[>T!!\b\u00021\u0005\tJ\u0003cA\u0012&O5\tAE\u0003\u0002\u0004!%\u0011a\u0005\n\u0002\t\u001b\u0006t\u0017NZ3tiB\u0011\u0001&\u000b\u0007\u0001\t%Q3&!A\u0001\u0002\u000b\u0005QFA\u0002`IEBa\u0001L\u0006!\u0002\u0013Q\u0012A\u0002;za\u0016\u001c\b%\u0005\u0002/cA\u0011qbL\u0005\u0003aA\u0011qAT8uQ&tw\r\u0005\u0002\u0010e%\u00111\u0007\u0005\u0002\u0004\u0003:L\bC\u0001\u00066\r\u0011a!\u0001\u0001\u001c\u0014\u0007Urq\u0007\u0005\u0002\u0010q%\u0011\u0011\b\u0005\u0002\u0007\u000bF,\u0018\r\\:\t\u0011m*$Q1A\u0005\nq\n\u0001\"\\1oS\u001a,7\u000f^\u000b\u0002{A\u0012a(\u0012\t\u0004\u007f\t#eBA\bA\u0013\t\t\u0005#\u0001\u0004Qe\u0016$WMZ\u0005\u0003M\rS!!\u0011\t\u0011\u0005!*E!\u0003$H\u0003\u0003\u0005\tQ!\u0001.\u0005\ryFE\u000e\u0005\t\u0011V\u0012\t\u0011)A\u0005\u0013\u0006IQ.\u00198jM\u0016\u001cH\u000f\t\u0019\u0003\u00152\u00032a\u0010\"L!\tAC\nB\u0005G\u000f\u0006\u0005\t\u0011!B\u0001[!)Q#\u000eC\u0001\u001dR\u0011Ag\u0014\u0005\u0006w5\u0003\r\u0001\u0015\u0019\u0003#N\u00032a\u0010\"S!\tA3\u000bB\u0005G\u001f\u0006\u0005\t\u0011!B\u0001[!9Q+\u000eb\u0001\n\u00031\u0016aB3sCN,(/Z\u000b\u0002/B\u0012\u0001\f\u0018\t\u0004\u007fe[\u0016B\u0001.D\u0005\u0015\u0019E.Y:t!\tAC\fB\u0005^=\u0006\u0005\t\u0011!B\u0001[\t\u0019q\fJ\u001c\t\r}+\u0004\u0015!\u0003a\u0003!)'/Y:ve\u0016\u0004\u0003GA1d!\ry\u0014L\u0019\t\u0003Q\r$\u0011\"\u00180\u0002\u0002\u0003\u0005)\u0011A\u0017\t\u000f\u0015,$\u0019!C\u0001M\u0006AA/\u001f9f\u0003J<7/F\u0001h!\rAw\u000e\u000e\b\u0003S:t!A[7\u000e\u0003-T!\u0001\u001c\u0005\u0002\rq\u0012xn\u001c;?\u0013\u0005\t\u0012BA\u000f\u0011\u0013\t\u0001\u0018OA\u0002TKFT!!\b\t\t\rM,\u0004\u0015!\u0003h\u0003%!\u0018\u0010]3Be\u001e\u001c\b\u0005\u0003\u0004vk\u0001\u0006KA^\u0001\n?RL\b/\u001a,beN\u0004BaP<zi%\u0011\u0001p\u0011\u0002\u0004\u001b\u0006\u0004\bCA {\u0013\tY8I\u0001\u0004TiJLgn\u001a\u0005\u0006{V\"\tA`\u0001\tif\u0004XMV1sgV\ta\u000fC\u0005\u0002\u0002U\u0012\r\u0011\"\u0001\u0002\u0004\u00059\u0011n]!se\u0006LXCAA\u0003!\ry\u0011qA\u0005\u0004\u0003\u0013\u0001\"a\u0002\"p_2,\u0017M\u001c\u0005\t\u0003\u001b)\u0004\u0015!\u0003\u0002\u0006\u0005A\u0011n]!se\u0006L\b\u0005C\u0004\u0002\u0012U\u0002\u000b\u0015B=\u0002\u0019}\u0013\u0018m\u001e$vY2t\u0015-\\3\t\u000f\u0005UQ\u0007\"\u0001\u0002\u0018\u0005Y!/Y<Gk2dg*Y7f+\u0005I\bbBA\u000ek\u0001\u0006K!_\u0001\u000f?J\fwoU5na2,g*Y7f\u0011\u001d\ty\"\u000eC\u0001\u0003/\tQB]1x'&l\u0007\u000f\\3OC6,\u0007BCA\u0012k!\u0015\r\u0011\"\u0001\u0002\u0018\u0005Q1/[7qY\u0016t\u0015-\\3\t\u0013\u0005\u001dR\u0007#A!B\u0013I\u0018aC:j[BdWMT1nK\u0002B!\"a\u000b6\u0011\u000b\u0007I\u0011AA\f\u0003!1W\u000f\u001c7OC6,\u0007\"CA\u0018k!\u0005\t\u0015)\u0003z\u0003%1W\u000f\u001c7OC6,\u0007\u0005\u0003\u0006\u00024UB)\u0019!C\u0001\u0003k\t\u0001\u0002^=qK&sgm\\\u000b\u0003\u0003o\u00012aGA\u001d\u0013\r\tY\u0004\t\u0002\t)f\u0004X-\u00138g_\"Q\u0011qH\u001b\t\u0002\u0003\u0006K!a\u000e\u0002\u0013QL\b/Z%oM>\u0004\u0003\"CA\"k\t\u0007I\u0011AA\u0002\u0003-I7\u000f\u0015:j[&$\u0018N^3\t\u0011\u0005\u001dS\u0007)A\u0005\u0003\u000b\tA\"[:Qe&l\u0017\u000e^5wK\u0002Bq!a\u00136\t\u0003\t\u0019!A\u0003jg6\u000b\u0007\u000fC\u0004\u0002PU\"\t!a\u0001\u0002\u0019%\u001cX*\u001e;bE2,W*\u00199\t\u000f\u0005MS\u0007\"\u0001\u0002\u0004\u0005a\u0011n]\"pY2,7\r^5p]\"9\u0011qK\u001b\u0005\u0002\u0005\r\u0011\u0001C5t\u001fB$\u0018n\u001c8\t\u000f\u0005mS\u0007\"\u0001\u0002\u0004\u0005A\u0011n]#ji\",'\u000fC\u0004\u0002`U\"\t!!\u0019\u0002!\u0011bWm]:%G>dwN\u001c\u0013mKN\u001cH\u0003BA\u0003\u0003GBq!!\u001a\u0002^\u0001\u0007A'\u0001\u0003uQ\u0006$\bbBA5k\u0011\u0005\u00111N\u0001\u0017I\u001d\u0014X-\u0019;fe\u0012\u001aw\u000e\\8oI\u001d\u0014X-\u0019;feR!\u0011QAA7\u0011\u001d\t)'a\u001aA\u0002QBq!!\u001d6\t\u0013\t\u0019(\u0001\btS:<G.\u001a;p]\u001aKW\r\u001c3\u0016\u0005\u0005U\u0004#B\b\u0002x\u0005m\u0014bAA=!\t1q\n\u001d;j_:\u0004B!! \u0002\n6\u0011\u0011q\u0010\u0006\u0004\u0007\u0005\u0005%\u0002BAB\u0003\u000b\u000bA\u0001\\1oO*\u0011\u0011qQ\u0001\u0005U\u00064\u0018-\u0003\u0003\u0002\f\u0006}$!\u0002$jK2$\u0007bBAHk\u0011\u0005\u00111A\u0001\fSN\u001c\u0016N\\4mKR|g\u000eC\u0004\u0002\u0014V\"\t!!&\u0002#MLgn\u001a7fi>t\u0017J\\:uC:\u001cW-\u0006\u0002\u0002\u0018B!q\"a\u001e\u000f\u0011\u001d\tY*\u000eC!\u0003;\u000b\u0001\u0002[1tQ\u000e{G-\u001a\u000b\u0003\u0003?\u00032aDAQ\u0013\r\t\u0019\u000b\u0005\u0002\u0004\u0013:$\bbBATk\u0011\u0005\u0013\u0011V\u0001\u0007KF,\u0018\r\\:\u0015\t\u0005\u0015\u00111\u0016\u0005\b\u0003[\u000b)\u000b1\u00012\u0003\ry'M\u001b\u0005\b\u0003c+D\u0011AAZ\u0003!\u0019\u0017M\\#rk\u0006dG\u0003BA\u0003\u0003kCq!!\u001a\u00020\u0002\u0007\u0011\u0007C\u0004\u0002:V\"\t!a/\u0002\t\r|\u0007/\u001f\u000b\bi\u0005u\u0016\u0011ZAf\u0011%)\u0016q\u0017I\u0001\u0002\u0004\ty\f\r\u0003\u0002B\u0006\u0015\u0007\u0003B Z\u0003\u0007\u00042\u0001KAc\t-\t9-!0\u0002\u0002\u0003\u0005)\u0011A\u0017\u0003\t}##\u0007\r\u0005\tK\u0006]\u0006\u0013!a\u0001O\"AQ0a.\u0011\u0002\u0003\u0007a\u000fC\u0004\u0002PV\"\t%!5\u0002\u0011Q|7\u000b\u001e:j]\u001e$\u0012!\u001f\u0005\n\u0003+,\u0014\u0013!C\u0001\u0003/\fabY8qs\u0012\"WMZ1vYR$\u0013'\u0006\u0002\u0002ZB\"\u00111\\Ap!\u0011y\u0014,!8\u0011\u0007!\ny\u000eB\u0006\u0002H\u0006M\u0017\u0011!A\u0001\u0006\u0003i\u0003\"CArkE\u0005I\u0011AAs\u00039\u0019w\u000e]=%I\u00164\u0017-\u001e7uII*\"!a:+\u0007\u001d\fIo\u000b\u0002\u0002lB!\u0011Q^A|\u001b\t\tyO\u0003\u0003\u0002r\u0006M\u0018!C;oG\",7m[3e\u0015\r\t)\u0010E\u0001\u000bC:tw\u000e^1uS>t\u0017\u0002BA}\u0003_\u0014\u0011#\u001e8dQ\u0016\u001c7.\u001a3WCJL\u0017M\\2f\u0011%\ti0NI\u0001\n\u0003\ty0\u0001\bd_BLH\u0005Z3gCVdG\u000fJ\u001a\u0016\u0005\t\u0005!f\u0001<\u0002j\"I!QA\u0006C\u0002\u0013%!qA\u0001\u0013g&tw\r\\3u_:4\u0015.\u001a7e\u001d\u0006lW-\u0006\u0002\u0003\nA!!1\u0002B\u0007\u001b\t\t\t)C\u0002|\u0003\u0003C\u0001B!\u0005\fA\u0003%!\u0011B\u0001\u0014g&tw\r\\3u_:4\u0015.\u001a7e\u001d\u0006lW\r\t\u0005\b\u0005+YA\u0011\u0001B\f\u0003\u0015\t\u0007\u000f\u001d7z+\u0011\u0011IBa\t\u0015\u0007Q\u0012Y\u0002\u0003\u0005\u0003\u001e\tM\u0001\u0019\u0001B\u0010\u0003\tig\r\u0005\u0003@\u0005\n\u0005\u0002c\u0001\u0015\u0003$\u00119!Q\u0005B\n\u0005\u0004i#!\u0001+\t\u000f\tU1\u0002\"\u0001\u0003*Q)AGa\u000b\u00038!9QKa\nA\u0002\t5\u0002\u0007\u0002B\u0018\u0005g\u0001BaP-\u00032A\u0019\u0001Fa\r\u0005\u0017\tU\"1FA\u0001\u0002\u0003\u0015\t!\f\u0002\u0004?\u0012\u0012\u0004\u0002C3\u0003(A\u0005\t\u0019A4\t\u000f\tU1\u0002\"\u0001\u0003<Q\u0019AG!\u0010\t\u0011\t}\"\u0011\ba\u0001\u0003o\ta\u0001^1sO\u0016$\b\"\u0003B\"\u0017\t\u0007I\u0011\u0002B#\u0003\u001dIe\u000e\u001e+za\u0016,\u0012\u0001\u000e\u0005\b\u0005\u0013Z\u0001\u0015!\u00035\u0003!Ie\u000e\u001e+za\u0016\u0004\u0003\"\u0003B'\u0017\t\u0007I\u0011\u0002B#\u0003)qU/\u001c2feRK\b/\u001a\u0005\b\u0005#Z\u0001\u0015!\u00035\u0003-qU/\u001c2feRK\b/\u001a\u0011\t\u0013\tU3B1A\u0005\n\t\u0015\u0013\u0001\u0003'p]\u001e$\u0016\u0010]3\t\u000f\te3\u0002)A\u0005i\u0005IAj\u001c8h)f\u0004X\r\t\u0005\n\u0005;Z!\u0019!C\u0005\u0005\u000b\n\u0001BQ=uKRK\b/\u001a\u0005\b\u0005CZ\u0001\u0015!\u00035\u0003%\u0011\u0015\u0010^3UsB,\u0007\u0005C\u0005\u0003f-\u0011\r\u0011\"\u0003\u0003F\u0005I1\u000b[8siRK\b/\u001a\u0005\b\u0005SZ\u0001\u0015!\u00035\u0003)\u0019\u0006n\u001c:u)f\u0004X\r\t\u0005\n\u0005[Z!\u0019!C\u0005\u0005\u000b\n1BQ8pY\u0016\fg\u000eV=qK\"9!\u0011O\u0006!\u0002\u0013!\u0014\u0001\u0004\"p_2,\u0017M\u001c+za\u0016\u0004\u0003\"\u0003B;\u0017\t\u0007I\u0011\u0002B#\u0003%1En\\1u)f\u0004X\rC\u0004\u0003z-\u0001\u000b\u0011\u0002\u001b\u0002\u0015\u0019cw.\u0019;UsB,\u0007\u0005C\u0005\u0003~-\u0011\r\u0011\"\u0003\u0003F\u0005QAi\\;cY\u0016$\u0016\u0010]3\t\u000f\t\u00055\u0002)A\u0005i\u0005YAi\\;cY\u0016$\u0016\u0010]3!\u0011%\u0011)i\u0003b\u0001\n\u0013\u0011)%\u0001\u0006TiJLgn\u001a+za\u0016DqA!#\fA\u0003%A'A\u0006TiJLgn\u001a+za\u0016\u0004\u0003\"\u0003BG\u0017\t\u0007I\u0011\u0002B#\u0003)\u0019\u00160\u001c2pYRK\b/\u001a\u0005\b\u0005#[\u0001\u0015!\u00035\u0003-\u0019\u00160\u001c2pYRK\b/\u001a\u0011\t\u0013\tU5B1A\u0005\n\t\u0015\u0013A\u0004\"jO\u0012+7-[7bYRK\b/\u001a\u0005\b\u00053[\u0001\u0015!\u00035\u0003=\u0011\u0015n\u001a#fG&l\u0017\r\u001c+za\u0016\u0004\u0003\"\u0003BO\u0017\t\u0007I\u0011\u0002B#\u0003)\u0011\u0015nZ%oiRK\b/\u001a\u0005\b\u0005C[\u0001\u0015!\u00035\u0003-\u0011\u0015nZ%oiRK\b/\u001a\u0011\t\u0013\t\u00156B1A\u0005\n\t\u0015\u0013A\u0003&WC2,X\rV=qK\"9!\u0011V\u0006!\u0002\u0013!\u0014a\u0003&WC2,X\rV=qK\u0002B\u0011B!,\f\u0005\u0004%IA!\u0012\u0002\u0017){%M[3diRK\b/\u001a\u0005\b\u0005c[\u0001\u0015!\u00035\u00031QuJ\u00196fGR$\u0016\u0010]3!\u0011%\u0011)l\u0003b\u0001\n\u0013\u0011)%\u0001\u0006K\u0003J\u0014\u0018-\u001f+za\u0016DqA!/\fA\u0003%A'A\u0006K\u0003J\u0014\u0018-\u001f+za\u0016\u0004\u0003\"\u0003B_\u0017\t\u0007I\u0011\u0002B#\u0003!!\u0015\r^3UsB,\u0007b\u0002Ba\u0017\u0001\u0006I\u0001N\u0001\n\t\u0006$X\rV=qK\u0002B\u0011B!2\f\u0005\u0004%IA!\u0012\u0002\u001bQKW.Z:uC6\u0004H+\u001f9f\u0011\u001d\u0011Im\u0003Q\u0001\nQ\na\u0002V5nKN$\u0018-\u001c9UsB,\u0007E\u0002\u0004\u0003N.!!q\u001a\u0002\u0013!JLW.\u001b;jm\u0016\u001c6-\u00197b)f\u0004XmE\u0002\u0003LRB1B!\b\u0003L\n\u0005\t\u0015!\u0003\u0003TB\"!Q\u001bBm!\u0011y$Ia6\u0011\u0007!\u0012I\u000eB\u0006\u0003\\\nE\u0017\u0011!A\u0001\u0006\u0003i#aA0%g!9QCa3\u0005\u0002\t}G\u0003\u0002Bq\u0005K\u0004BAa9\u0003L6\t1\u0002\u0003\u0005\u0003\u001e\tu\u0007\u0019\u0001Bta\u0011\u0011IO!<\u0011\t}\u0012%1\u001e\t\u0004Q\t5Ha\u0003Bn\u0005K\f\t\u0011!A\u0003\u00025B!\"a\u0011\u0003L\n\u0007I\u0011IA\u0002\u0011%\t9Ea3!\u0002\u0013\t)A\u0002\u0004\u0003v.!!q\u001f\u0002\u0010\u0007>\u0004\u0018.\u001a3TG\u0006d\u0017\rV=qKN\u0019!1\u001f\u001b\t\u0017\tu!1\u001fB\u0001B\u0003%!1 \u0019\u0005\u0005{\u001c\t\u0001\u0005\u0003@\u0005\n}\bc\u0001\u0015\u0004\u0002\u0011Y11\u0001B}\u0003\u0003\u0005\tQ!\u0001.\u0005\ryF\u0005\u000e\u0005\nk\nM(\u0011!Q!\nYD1\"a\u0011\u0003t\n\u0015\r\u0011\"\u0011\u0002\u0004!Y\u0011q\tBz\u0005\u0003\u0005\u000b\u0011BA\u0003\u0011\u001d)\"1\u001fC\u0001\u0007\u001b!\u0002ba\u0004\u0004\u0012\rm1Q\u0004\t\u0005\u0005G\u0014\u0019\u0010\u0003\u0005\u0003\u001e\r-\u0001\u0019AB\na\u0011\u0019)b!\u0007\u0011\t}\u00125q\u0003\t\u0004Q\reAaCB\u0002\u0007#\t\t\u0011!A\u0003\u00025Ba!^B\u0006\u0001\u00041\b\u0002CA\"\u0007\u0017\u0001\r!!\u0002\t\ru\u0014\u0019\u0010\"\u0011\u007f\u0011%\u0019\u0019cCI\u0001\n\u0003\t)/A\bbaBd\u0017\u0010\n3fM\u0006,H\u000e\u001e\u00133\u0001")
public class ScalaType implements Equals
{
    private final Manifest<?> org$json4s$reflect$ScalaType$$manifest;
    private final Class<?> erasure;
    private final Seq<ScalaType> typeArgs;
    private Map<String, ScalaType> _typeVars;
    private final boolean isArray;
    private String _rawFullName;
    private String _rawSimpleName;
    private String simpleName;
    private String fullName;
    private package.TypeInfo typeInfo;
    private final boolean isPrimitive;
    private volatile byte bitmap$0;
    
    public static Seq<ScalaType> apply$default$2() {
        return ScalaType$.MODULE$.apply$default$2();
    }
    
    public static ScalaType apply(final package.TypeInfo target) {
        return ScalaType$.MODULE$.apply(target);
    }
    
    public static ScalaType apply(final Class<?> erasure, final Seq<ScalaType> typeArgs) {
        return ScalaType$.MODULE$.apply(erasure, typeArgs);
    }
    
    public static <T> ScalaType apply(final Manifest<T> mf) {
        return ScalaType$.MODULE$.apply((scala.reflect.Manifest<Object>)mf);
    }
    
    private String simpleName$lzycompute() {
        synchronized (this) {
            if ((byte)(this.bitmap$0 & 0x1) == 0) {
                this.simpleName = new StringBuilder().append((Object)this.rawSimpleName()).append((Object)(this.typeArgs().nonEmpty() ? ((TraversableOnce)this.typeArgs().map((Function1)new ScalaType$$anonfun$simpleName.ScalaType$$anonfun$simpleName$1(this), Seq$.MODULE$.canBuildFrom())).mkString("[", ", ", "]") : (this.typeVars().nonEmpty() ? ((TraversableOnce)this.typeVars().map((Function1)new ScalaType$$anonfun$simpleName.ScalaType$$anonfun$simpleName$2(this), Iterable$.MODULE$.canBuildFrom())).mkString("[", ", ", "]") : ""))).toString();
                this.bitmap$0 |= 0x1;
            }
            final BoxedUnit unit = BoxedUnit.UNIT;
            return this.simpleName;
        }
    }
    
    private String fullName$lzycompute() {
        synchronized (this) {
            if ((byte)(this.bitmap$0 & 0x2) == 0) {
                this.fullName = new StringBuilder().append((Object)this.rawFullName()).append((Object)(this.typeArgs().nonEmpty() ? ((TraversableOnce)this.typeArgs().map((Function1)new ScalaType$$anonfun$fullName.ScalaType$$anonfun$fullName$1(this), Seq$.MODULE$.canBuildFrom())).mkString("[", ", ", "]") : "")).toString();
                this.bitmap$0 |= 0x2;
            }
            final BoxedUnit unit = BoxedUnit.UNIT;
            return this.fullName;
        }
    }
    
    private package.TypeInfo typeInfo$lzycompute() {
        synchronized (this) {
            if ((byte)(this.bitmap$0 & 0x4) == 0) {
                this.typeInfo = (package.TypeInfo)new ScalaType$$anon.ScalaType$$anon$1(this);
                this.bitmap$0 |= 0x4;
            }
            final BoxedUnit unit = BoxedUnit.UNIT;
            return this.typeInfo;
        }
    }
    
    public Manifest<?> org$json4s$reflect$ScalaType$$manifest() {
        return this.org$json4s$reflect$ScalaType$$manifest;
    }
    
    public Class<?> erasure() {
        return this.erasure;
    }
    
    public Seq<ScalaType> typeArgs() {
        return this.typeArgs;
    }
    
    public Map<String, ScalaType> typeVars() {
        if (this._typeVars == null) {
            this._typeVars = (Map<String, ScalaType>)Predef$.MODULE$.Map().empty().$plus$plus((GenTraversableOnce)Predef$.MODULE$.refArrayOps((Object[])Predef$.MODULE$.refArrayOps((Object[])this.erasure().getTypeParameters()).map((Function1)new ScalaType$$anonfun$typeVars.ScalaType$$anonfun$typeVars$2(this), Array$.MODULE$.canBuildFrom(ClassTag$.MODULE$.apply((Class)String.class)))).zip((GenIterable)this.typeArgs(), Array$.MODULE$.fallbackCanBuildFrom(Predef.DummyImplicit$.MODULE$.dummyImplicit())));
        }
        return this._typeVars;
    }
    
    public boolean isArray() {
        return this.isArray;
    }
    
    public String rawFullName() {
        if (this._rawFullName == null) {
            this._rawFullName = this.erasure().getName();
        }
        return this._rawFullName;
    }
    
    public String rawSimpleName() {
        if (this._rawSimpleName == null) {
            this._rawSimpleName = package$.MODULE$.safeSimpleName(this.erasure());
        }
        return this._rawSimpleName;
    }
    
    public String simpleName() {
        return ((byte)(this.bitmap$0 & 0x1) == 0) ? this.simpleName$lzycompute() : this.simpleName;
    }
    
    public String fullName() {
        return ((byte)(this.bitmap$0 & 0x2) == 0) ? this.fullName$lzycompute() : this.fullName;
    }
    
    public package.TypeInfo typeInfo() {
        return ((byte)(this.bitmap$0 & 0x4) == 0) ? this.typeInfo$lzycompute() : this.typeInfo;
    }
    
    public boolean isPrimitive() {
        return this.isPrimitive;
    }
    
    public boolean isMap() {
        return Map.class.isAssignableFrom(this.erasure()) || scala.collection.Map.class.isAssignableFrom(this.erasure());
    }
    
    public boolean isMutableMap() {
        return scala.collection.mutable.Map.class.isAssignableFrom(this.erasure());
    }
    
    public boolean isCollection() {
        return this.erasure().isArray() || Iterable.class.isAssignableFrom(this.erasure()) || Collection.class.isAssignableFrom(this.erasure());
    }
    
    public boolean isOption() {
        return Option.class.isAssignableFrom(this.erasure());
    }
    
    public boolean isEither() {
        return Either.class.isAssignableFrom(this.erasure());
    }
    
    public boolean $less$colon$less(final ScalaType that) {
        return this.org$json4s$reflect$ScalaType$$manifest().$less$colon$less((ClassTag)that.org$json4s$reflect$ScalaType$$manifest());
    }
    
    public boolean $greater$colon$greater(final ScalaType that) {
        return this.org$json4s$reflect$ScalaType$$manifest().$greater$colon$greater((ClassTag)that.org$json4s$reflect$ScalaType$$manifest());
    }
    
    private Option<Field> singletonField() {
        return (Option<Field>)Predef$.MODULE$.refArrayOps((Object[])this.erasure().getFields()).find((Function1)new ScalaType$$anonfun$singletonField.ScalaType$$anonfun$singletonField$1(this));
    }
    
    public boolean isSingleton() {
        return this.singletonField().isDefined();
    }
    
    public Option<Object> singletonInstance() {
        return (Option<Object>)this.singletonField().map((Function1)new ScalaType$$anonfun$singletonInstance.ScalaType$$anonfun$singletonInstance$1(this));
    }
    
    @Override
    public int hashCode() {
        return ScalaRunTime$.MODULE$.hash((Object)this.org$json4s$reflect$ScalaType$$manifest());
    }
    
    @Override
    public boolean equals(final Object obj) {
        boolean b2;
        if (obj instanceof ScalaType) {
            final ScalaType scalaType = (ScalaType)obj;
            final Manifest<?> org$json4s$reflect$ScalaType$$manifest = this.org$json4s$reflect$ScalaType$$manifest();
            final Manifest<?> org$json4s$reflect$ScalaType$$manifest2 = scalaType.org$json4s$reflect$ScalaType$$manifest();
            boolean b = false;
            Label_0050: {
                Label_0049: {
                    if (org$json4s$reflect$ScalaType$$manifest == null) {
                        if (org$json4s$reflect$ScalaType$$manifest2 != null) {
                            break Label_0049;
                        }
                    }
                    else if (!org$json4s$reflect$ScalaType$$manifest.equals(org$json4s$reflect$ScalaType$$manifest2)) {
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
    
    public boolean canEqual(final Object that) {
        return that instanceof ScalaType && this.org$json4s$reflect$ScalaType$$manifest().canEqual((Object)((ScalaType)that).org$json4s$reflect$ScalaType$$manifest());
    }
    
    public ScalaType copy(final Class<?> erasure, final Seq<ScalaType> typeArgs, final Map<String, ScalaType> typeVars) {
        final Class<Integer> type = Integer.TYPE;
        if (erasure == null) {
            if (type == null) {
                return ScalaType$.MODULE$.org$json4s$reflect$ScalaType$$IntType();
            }
        }
        else if (erasure.equals(type)) {
            return ScalaType$.MODULE$.org$json4s$reflect$ScalaType$$IntType();
        }
        final Class<Integer> obj = Integer.class;
        if (erasure == null) {
            if (obj == null) {
                return ScalaType$.MODULE$.org$json4s$reflect$ScalaType$$IntType();
            }
        }
        else if (erasure.equals(obj)) {
            return ScalaType$.MODULE$.org$json4s$reflect$ScalaType$$IntType();
        }
        final Class<Long> type2 = Long.TYPE;
        if (erasure == null) {
            if (type2 == null) {
                return ScalaType$.MODULE$.org$json4s$reflect$ScalaType$$LongType();
            }
        }
        else if (erasure.equals(type2)) {
            return ScalaType$.MODULE$.org$json4s$reflect$ScalaType$$LongType();
        }
        final Class<Long> obj2 = Long.class;
        if (erasure == null) {
            if (obj2 == null) {
                return ScalaType$.MODULE$.org$json4s$reflect$ScalaType$$LongType();
            }
        }
        else if (erasure.equals(obj2)) {
            return ScalaType$.MODULE$.org$json4s$reflect$ScalaType$$LongType();
        }
        final Class<Byte> type3 = Byte.TYPE;
        if (erasure == null) {
            if (type3 == null) {
                return ScalaType$.MODULE$.org$json4s$reflect$ScalaType$$ByteType();
            }
        }
        else if (erasure.equals(type3)) {
            return ScalaType$.MODULE$.org$json4s$reflect$ScalaType$$ByteType();
        }
        final Class<Byte> obj3 = Byte.class;
        if (erasure == null) {
            if (obj3 == null) {
                return ScalaType$.MODULE$.org$json4s$reflect$ScalaType$$ByteType();
            }
        }
        else if (erasure.equals(obj3)) {
            return ScalaType$.MODULE$.org$json4s$reflect$ScalaType$$ByteType();
        }
        final Class<Short> type4 = Short.TYPE;
        if (erasure == null) {
            if (type4 == null) {
                return ScalaType$.MODULE$.org$json4s$reflect$ScalaType$$ShortType();
            }
        }
        else if (erasure.equals(type4)) {
            return ScalaType$.MODULE$.org$json4s$reflect$ScalaType$$ShortType();
        }
        final Class<Short> obj4 = Short.class;
        if (erasure == null) {
            if (obj4 == null) {
                return ScalaType$.MODULE$.org$json4s$reflect$ScalaType$$ShortType();
            }
        }
        else if (erasure.equals(obj4)) {
            return ScalaType$.MODULE$.org$json4s$reflect$ScalaType$$ShortType();
        }
        final Class<Float> type5 = Float.TYPE;
        if (erasure == null) {
            if (type5 == null) {
                return ScalaType$.MODULE$.org$json4s$reflect$ScalaType$$FloatType();
            }
        }
        else if (erasure.equals(type5)) {
            return ScalaType$.MODULE$.org$json4s$reflect$ScalaType$$FloatType();
        }
        final Class<Float> obj5 = Float.class;
        if (erasure == null) {
            if (obj5 == null) {
                return ScalaType$.MODULE$.org$json4s$reflect$ScalaType$$FloatType();
            }
        }
        else if (erasure.equals(obj5)) {
            return ScalaType$.MODULE$.org$json4s$reflect$ScalaType$$FloatType();
        }
        final Class<Double> type6 = Double.TYPE;
        if (erasure == null) {
            if (type6 == null) {
                return ScalaType$.MODULE$.org$json4s$reflect$ScalaType$$DoubleType();
            }
        }
        else if (erasure.equals(type6)) {
            return ScalaType$.MODULE$.org$json4s$reflect$ScalaType$$DoubleType();
        }
        final Class<Double> obj6 = Double.class;
        if (erasure == null) {
            if (obj6 == null) {
                return ScalaType$.MODULE$.org$json4s$reflect$ScalaType$$DoubleType();
            }
        }
        else if (erasure.equals(obj6)) {
            return ScalaType$.MODULE$.org$json4s$reflect$ScalaType$$DoubleType();
        }
        final Class<BigInt> obj7 = BigInt.class;
        if (erasure == null) {
            if (obj7 == null) {
                return ScalaType$.MODULE$.org$json4s$reflect$ScalaType$$BigIntType();
            }
        }
        else if (erasure.equals(obj7)) {
            return ScalaType$.MODULE$.org$json4s$reflect$ScalaType$$BigIntType();
        }
        final Class<BigInteger> obj8 = BigInteger.class;
        if (erasure == null) {
            if (obj8 == null) {
                return ScalaType$.MODULE$.org$json4s$reflect$ScalaType$$BigIntType();
            }
        }
        else if (erasure.equals(obj8)) {
            return ScalaType$.MODULE$.org$json4s$reflect$ScalaType$$BigIntType();
        }
        final Class<BigDecimal> obj9 = BigDecimal.class;
        if (erasure == null) {
            if (obj9 == null) {
                return ScalaType$.MODULE$.org$json4s$reflect$ScalaType$$BigDecimalType();
            }
        }
        else if (erasure.equals(obj9)) {
            return ScalaType$.MODULE$.org$json4s$reflect$ScalaType$$BigDecimalType();
        }
        final Class<java.math.BigDecimal> obj10 = java.math.BigDecimal.class;
        if (erasure == null) {
            if (obj10 == null) {
                return ScalaType$.MODULE$.org$json4s$reflect$ScalaType$$BigDecimalType();
            }
        }
        else if (erasure.equals(obj10)) {
            return ScalaType$.MODULE$.org$json4s$reflect$ScalaType$$BigDecimalType();
        }
        final Class<Boolean> type7 = Boolean.TYPE;
        if (erasure == null) {
            if (type7 == null) {
                return ScalaType$.MODULE$.org$json4s$reflect$ScalaType$$BooleanType();
            }
        }
        else if (erasure.equals(type7)) {
            return ScalaType$.MODULE$.org$json4s$reflect$ScalaType$$BooleanType();
        }
        final Class<Boolean> obj11 = Boolean.class;
        if (erasure == null) {
            if (obj11 == null) {
                return ScalaType$.MODULE$.org$json4s$reflect$ScalaType$$BooleanType();
            }
        }
        else if (erasure.equals(obj11)) {
            return ScalaType$.MODULE$.org$json4s$reflect$ScalaType$$BooleanType();
        }
        final Class<String> obj12 = String.class;
        if (erasure == null) {
            if (obj12 == null) {
                return ScalaType$.MODULE$.org$json4s$reflect$ScalaType$$StringType();
            }
        }
        else if (erasure.equals(obj12)) {
            return ScalaType$.MODULE$.org$json4s$reflect$ScalaType$$StringType();
        }
        final Class<String> obj13 = String.class;
        if (erasure == null) {
            if (obj13 == null) {
                return ScalaType$.MODULE$.org$json4s$reflect$ScalaType$$StringType();
            }
        }
        else if (erasure.equals(obj13)) {
            return ScalaType$.MODULE$.org$json4s$reflect$ScalaType$$StringType();
        }
        final Class<Date> obj14 = Date.class;
        Label_0664: {
            if (erasure == null) {
                if (obj14 != null) {
                    break Label_0664;
                }
            }
            else if (!erasure.equals(obj14)) {
                break Label_0664;
            }
            return ScalaType$.MODULE$.org$json4s$reflect$ScalaType$$DateType();
        }
        final Class<Timestamp> obj15 = Timestamp.class;
        Label_0700: {
            if (erasure == null) {
                if (obj15 != null) {
                    break Label_0700;
                }
            }
            else if (!erasure.equals(obj15)) {
                break Label_0700;
            }
            return ScalaType$.MODULE$.org$json4s$reflect$ScalaType$$TimestampType();
        }
        final Class<Symbol> obj16 = Symbol.class;
        Label_0736: {
            if (erasure == null) {
                if (obj16 != null) {
                    break Label_0736;
                }
            }
            else if (!erasure.equals(obj16)) {
                break Label_0736;
            }
            return ScalaType$.MODULE$.org$json4s$reflect$ScalaType$$SymbolType();
        }
        final Class<Number> obj17 = Number.class;
        Label_0772: {
            if (erasure == null) {
                if (obj17 != null) {
                    break Label_0772;
                }
            }
            else if (!erasure.equals(obj17)) {
                break Label_0772;
            }
            return ScalaType$.MODULE$.org$json4s$reflect$ScalaType$$NumberType();
        }
        final Class<JsonAST.JObject> obj18 = JsonAST.JObject.class;
        Label_0808: {
            if (erasure == null) {
                if (obj18 != null) {
                    break Label_0808;
                }
            }
            else if (!erasure.equals(obj18)) {
                break Label_0808;
            }
            return ScalaType$.MODULE$.org$json4s$reflect$ScalaType$$JObjectType();
        }
        final Class<JsonAST.JArray> obj19 = JsonAST.JArray.class;
        Label_0844: {
            if (erasure == null) {
                if (obj19 != null) {
                    break Label_0844;
                }
            }
            else if (!erasure.equals(obj19)) {
                break Label_0844;
            }
            return ScalaType$.MODULE$.org$json4s$reflect$ScalaType$$JArrayType();
        }
        final Class<JsonAST.JValue> obj20 = JsonAST.JValue.class;
        Label_0880: {
            if (erasure == null) {
                if (obj20 != null) {
                    break Label_0880;
                }
            }
            else if (!erasure.equals(obj20)) {
                break Label_0880;
            }
            return ScalaType$.MODULE$.org$json4s$reflect$ScalaType$$JValueType();
        }
        final Manifest mf = ManifestFactory$.MODULE$.manifestOf(erasure, (Seq<Manifest<?>>)typeArgs.map((Function1)new ScalaType$$anonfun.ScalaType$$anonfun$5(this), Seq$.MODULE$.canBuildFrom()));
        final CopiedScalaType st = new CopiedScalaType((Manifest<?>)mf, typeVars, this.isPrimitive());
        return typeArgs.isEmpty() ? ScalaType$.MODULE$.org$json4s$reflect$ScalaType$$types().replace((Manifest<?>)mf, st) : st;
        scalaType = ScalaType$.MODULE$.org$json4s$reflect$ScalaType$$StringType();
        return scalaType;
        scalaType = ScalaType$.MODULE$.org$json4s$reflect$ScalaType$$BooleanType();
        return scalaType;
        scalaType = ScalaType$.MODULE$.org$json4s$reflect$ScalaType$$BigDecimalType();
        return scalaType;
        scalaType = ScalaType$.MODULE$.org$json4s$reflect$ScalaType$$BigIntType();
        return scalaType;
        scalaType = ScalaType$.MODULE$.org$json4s$reflect$ScalaType$$DoubleType();
        return scalaType;
        scalaType = ScalaType$.MODULE$.org$json4s$reflect$ScalaType$$FloatType();
        return scalaType;
        scalaType = ScalaType$.MODULE$.org$json4s$reflect$ScalaType$$ShortType();
        return scalaType;
        scalaType = ScalaType$.MODULE$.org$json4s$reflect$ScalaType$$ByteType();
        return scalaType;
        scalaType = ScalaType$.MODULE$.org$json4s$reflect$ScalaType$$LongType();
        return scalaType;
        scalaType = ScalaType$.MODULE$.org$json4s$reflect$ScalaType$$IntType();
        return scalaType;
    }
    
    public Class<?> copy$default$1() {
        return this.erasure();
    }
    
    public Seq<ScalaType> copy$default$2() {
        return this.typeArgs();
    }
    
    public Map<String, ScalaType> copy$default$3() {
        return this._typeVars;
    }
    
    @Override
    public String toString() {
        return this.simpleName();
    }
    
    public ScalaType(final Manifest<?> manifest) {
        this.org$json4s$reflect$ScalaType$$manifest = manifest;
        this.erasure = (Class<?>)manifest.runtimeClass();
        this.typeArgs = (Seq<ScalaType>)((List)manifest.typeArguments().map((Function1)new ScalaType$$anonfun.ScalaType$$anonfun$4(this), List$.MODULE$.canBuildFrom())).$plus$plus((GenTraversableOnce)(this.erasure().isArray() ? List$.MODULE$.apply((Seq)Predef$.MODULE$.wrapRefArray((Object[])new ScalaType[] { Reflector$.MODULE$.scalaTypeOf(this.erasure().getComponentType()) })) : Nil$.MODULE$), List$.MODULE$.canBuildFrom());
        this._typeVars = null;
        this.isArray = this.erasure().isArray();
        this._rawFullName = null;
        this._rawSimpleName = null;
        this.isPrimitive = false;
    }
    
    public static class PrimitiveScalaType extends ScalaType
    {
        private final boolean isPrimitive;
        
        @Override
        public boolean isPrimitive() {
            return this.isPrimitive;
        }
        
        public PrimitiveScalaType(final Manifest<?> mf) {
            super(mf);
            this.isPrimitive = true;
        }
    }
    
    public static class CopiedScalaType extends ScalaType
    {
        private Map<String, ScalaType> _typeVars;
        private final boolean isPrimitive;
        
        @Override
        public boolean isPrimitive() {
            return this.isPrimitive;
        }
        
        @Override
        public Map<String, ScalaType> typeVars() {
            if (this._typeVars == null) {
                this._typeVars = (Map<String, ScalaType>)Predef$.MODULE$.Map().empty().$plus$plus((GenTraversableOnce)Predef$.MODULE$.refArrayOps((Object[])Predef$.MODULE$.refArrayOps((Object[])this.erasure().getTypeParameters()).map((Function1)new ScalaType$CopiedScalaType$$anonfun$typeVars.ScalaType$CopiedScalaType$$anonfun$typeVars$1(this), Array$.MODULE$.canBuildFrom(ClassTag$.MODULE$.apply((Class)String.class)))).zip((GenIterable)this.typeArgs(), Array$.MODULE$.fallbackCanBuildFrom(Predef.DummyImplicit$.MODULE$.dummyImplicit())));
            }
            return this._typeVars;
        }
        
        public CopiedScalaType(final Manifest<?> mf, final Map<String, ScalaType> _typeVars, final boolean isPrimitive) {
            this._typeVars = _typeVars;
            this.isPrimitive = isPrimitive;
            super(mf);
        }
    }
}
