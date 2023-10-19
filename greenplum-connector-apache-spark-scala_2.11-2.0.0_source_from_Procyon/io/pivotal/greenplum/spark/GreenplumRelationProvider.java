// 
// Decompiled by Procyon v0.5.36
// 

package io.pivotal.greenplum.spark;

import scala.collection.JavaConverters$;
import java.util.Properties;
import org.apache.spark.rdd.RDD;
import io.pivotal.greenplum.spark.externaltable.RowTransformer$;
import io.pivotal.greenplum.spark.jdbc.DataSourceProvider;
import io.pivotal.greenplum.spark.jdbc.ConnectionManager;
import io.pivotal.greenplum.spark.jdbc.HikariProvider$;
import io.pivotal.greenplum.spark.externaltable.PartitionWriter;
import scala.collection.Iterator;
import scala.Function2;
import io.pivotal.greenplum.spark.externaltable.GreenplumTableManager;
import io.pivotal.greenplum.spark.externaltable.DropExternalTablesSparkListener;
import org.apache.spark.SparkContext;
import org.apache.spark.scheduler.SparkListenerInterface;
import io.pivotal.greenplum.spark.externaltable.GreenplumTableManager$;
import io.pivotal.greenplum.spark.conf.GreenplumOptions;
import scala.runtime.VolatileByteRef;
import scala.runtime.ObjectRef;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.SaveMode;
import io.pivotal.greenplum.spark.jdbc.ColumnValueRange;
import scala.collection.Seq;
import scala.StringContext;
import scala.None$;
import scala.MatchError;
import scala.util.Right;
import io.pivotal.greenplum.spark.jdbc.Jdbc$;
import scala.util.Left;
import scala.runtime.BoxesRunTime;
import scala.Some;
import scala.Function1;
import scala.reflect.ClassTag$;
import scala.Predef$;
import io.pivotal.greenplum.spark.conf.GreenplumOptions$;
import scala.Option;
import org.apache.spark.sql.types.StructType;
import io.pivotal.greenplum.spark.externaltable.GreenplumQualifiedName;
import java.sql.Connection;
import org.apache.spark.sql.sources.BaseRelation;
import scala.collection.immutable.Map;
import org.apache.spark.sql.SQLContext;
import scala.Function0;
import org.apache.spark.sql.DataFrameReader;
import scala.runtime.BoxedUnit;
import scala.util.Either;
import org.apache.spark.sql.types.StructField;
import org.slf4j.Logger;
import scala.reflect.ScalaSignature;
import org.apache.spark.sql.sources.CreatableRelationProvider;
import org.apache.spark.sql.sources.DataSourceRegister;
import org.apache.spark.sql.sources.RelationProvider;

@ScalaSignature(bytes = "\u0006\u0001\tEh\u0001B\u0001\u0003\u0001-\u0011\u0011d\u0012:fK:\u0004H.^7SK2\fG/[8o!J|g/\u001b3fe*\u00111\u0001B\u0001\u0006gB\f'o\u001b\u0006\u0003\u000b\u0019\t\u0011b\u001a:fK:\u0004H.^7\u000b\u0005\u001dA\u0011a\u00029jm>$\u0018\r\u001c\u0006\u0002\u0013\u0005\u0011\u0011n\\\u0002\u0001'\u0019\u0001ABE\u0010#KA\u0011Q\u0002E\u0007\u0002\u001d)\tq\"A\u0003tG\u0006d\u0017-\u0003\u0002\u0012\u001d\t1\u0011I\\=SK\u001a\u0004\"aE\u000f\u000e\u0003QQ!!\u0006\f\u0002\u000fM|WO]2fg*\u0011q\u0003G\u0001\u0004gFd'BA\u0002\u001a\u0015\tQ2$\u0001\u0004ba\u0006\u001c\u0007.\u001a\u0006\u00029\u0005\u0019qN]4\n\u0005y!\"\u0001\u0005*fY\u0006$\u0018n\u001c8Qe>4\u0018\u000eZ3s!\t\u0019\u0002%\u0003\u0002\")\t\u0011B)\u0019;b'>,(oY3SK\u001eL7\u000f^3s!\t\u00192%\u0003\u0002%)\tI2I]3bi\u0006\u0014G.\u001a*fY\u0006$\u0018n\u001c8Qe>4\u0018\u000eZ3s!\t1s%D\u0001\u0003\u0013\tA#AA\u0004M_\u001e<\u0017N\\4\t\u000b)\u0002A\u0011A\u0016\u0002\rqJg.\u001b;?)\u0005a\u0003C\u0001\u0014\u0001\u0011\u0015q\u0003\u0001\"\u00110\u0003%\u0019\bn\u001c:u\u001d\u0006lW\rF\u00011!\t\tDG\u0004\u0002\u000ee%\u00111GD\u0001\u0007!J,G-\u001a4\n\u0005U2$AB*ue&twM\u0003\u00024\u001d!)\u0001\b\u0001C\u0001s\u0005q1M]3bi\u0016\u0014V\r\\1uS>tGc\u0001\u001e>\u0007B\u00111cO\u0005\u0003yQ\u0011ABQ1tKJ+G.\u0019;j_:DQAP\u001cA\u0002}\n!b]9m\u0007>tG/\u001a=u!\t\u0001\u0015)D\u0001\u0017\u0013\t\u0011eC\u0001\u0006T#2\u001buN\u001c;fqRDQ\u0001R\u001cA\u0002\u0015\u000b!\u0002]1sC6,G/\u001a:t!\u0011\td\t\r\u0019\n\u0005\u001d3$aA'ba\"1\u0011\n\u0001C\u0001\u0005)\u000b\u0011cY8naV$X\rU1si&$\u0018n\u001c8t)\u001dY\u0015K\u00172kYR\u00042!\u0004'O\u0013\tieBA\u0003BeJ\f\u0017\u0010\u0005\u0002'\u001f&\u0011\u0001K\u0001\u0002\u0013\u000fJ,WM\u001c9mk6\u0004\u0016M\u001d;ji&|g\u000eC\u0003S\u0011\u0002\u00071+\u0001\u0003d_:t\u0007C\u0001+Y\u001b\u0005)&BA\fW\u0015\u00059\u0016\u0001\u00026bm\u0006L!!W+\u0003\u0015\r{gN\\3di&|g\u000eC\u0003\\\u0011\u0002\u0007A,A\u0003uC\ndW\r\u0005\u0002^A6\taL\u0003\u0002`\u0005\u0005iQ\r\u001f;fe:\fG\u000e^1cY\u0016L!!\u00190\u0003-\u001d\u0013X-\u001a8qYVl\u0017+^1mS\u001aLW\r\u001a(b[\u0016DQa\u0019%A\u0002\u0011\faa]2iK6\f\u0007CA3i\u001b\u00051'BA4\u0017\u0003\u0015!\u0018\u0010]3t\u0013\tIgM\u0001\u0006TiJ,8\r\u001e+za\u0016DQa\u001b%A\u0002A\n1\u0003]1si&$\u0018n\u001c8D_2,XN\u001c(b[\u0016DQ!\u001c%A\u00029\fq\u0002]1si&$\u0018n\u001c8t\u0007>,h\u000e\u001e\t\u0004\u001b=\f\u0018B\u00019\u000f\u0005\u0019y\u0005\u000f^5p]B\u0011QB]\u0005\u0003g:\u00111!\u00138u\u0011\u0015)\b\n1\u0001w\u000319\u0007oU3h[\u0016tG/\u00133t!\riA*\u001d\u0005\u0006q\u0001!\t\u0005\u001f\u000b\u0007ueTx0!\u0001\t\u000by:\b\u0019A \t\u000bm<\b\u0019\u0001?\u0002\t5|G-\u001a\t\u0003\u0001vL!A \f\u0003\u0011M\u000bg/Z'pI\u0016DQ\u0001R<A\u0002\u0015Cq!a\u0001x\u0001\u0004\t)!\u0001\u0003eCR\f\u0007\u0003BA\u0004\u0003GqA!!\u0003\u0002 9!\u00111BA\u000f\u001d\u0011\ti!a\u0007\u000f\t\u0005=\u0011\u0011\u0004\b\u0005\u0003#\t9\"\u0004\u0002\u0002\u0014)\u0019\u0011Q\u0003\u0006\u0002\rq\u0012xn\u001c;?\u0013\u0005a\u0012B\u0001\u000e\u001c\u0013\t\u0019\u0011$\u0003\u0002\u00181%\u0019\u0011\u0011\u0005\f\u0002\u000fA\f7m[1hK&!\u0011QEA\u0014\u0005%!\u0015\r^1Ge\u0006lWMC\u0002\u0002\"YAq!a\u000b\u0001\t#\ti#A\u0013de\u0016\fG/\u001a#s_B,\u0005\u0010^3s]\u0006dG+\u00192mKN\u001c\u0006/\u0019:l\u0019&\u001cH/\u001a8feR1\u0011qFA\u001b\u0003s\u00012!XA\u0019\u0013\r\t\u0019D\u0018\u0002 \tJ|\u0007/\u0012=uKJt\u0017\r\u001c+bE2,7o\u00159be.d\u0015n\u001d;f]\u0016\u0014\bbBA\u001c\u0003S\u0001\r\u0001M\u0001\fi\u0006\u0014G.\u001a)sK\u001aL\u0007\u0010\u0003\u0005\u0002<\u0005%\u0002\u0019AA\u001f\u0003A9'/Z3oa2,Xn\u00149uS>t7\u000f\u0005\u0003\u0002@\u0005\u0015SBAA!\u0015\r\t\u0019EA\u0001\u0005G>tg-\u0003\u0003\u0002H\u0005\u0005#\u0001E$sK\u0016t\u0007\u000f\\;n\u001fB$\u0018n\u001c8t\u0011\u001d\tY\u0005\u0001C\t\u0003\u001b\nqbZ3u)\u0006\u0014G.Z'b]\u0006<WM\u001d\u000b\u0005\u0003\u001f\n)\u0006E\u0002^\u0003#J1!a\u0015_\u0005U9%/Z3oa2,X\u000eV1cY\u0016l\u0015M\\1hKJD\u0001\"a\u0016\u0002J\u0001\u0007\u0011\u0011L\u0001\tKb,7-\u001e;peB\u0019a%a\u0017\n\u0007\u0005u#AA\u0006Tc2,\u00050Z2vi>\u0014\bbBA1\u0001\u0011E\u00111M\u0001\u0013O\u0016$\b+\u0019:uSRLwN\\,sSR,'\u000f\u0006\u0005\u0002f\u0005\r\u0015QQAI!!i\u0011qM9\u0002l\u0005\u0005\u0015bAA5\u001d\tIa)\u001e8di&|gN\r\t\u0007\u0003[\n)(a\u001f\u000f\t\u0005=\u00141\u000f\b\u0005\u0003#\t\t(C\u0001\u0010\u0013\r\t\tCD\u0005\u0005\u0003o\nIH\u0001\u0005Ji\u0016\u0014\u0018\r^8s\u0015\r\t\tC\u0004\t\u0004\u0001\u0006u\u0014bAA@-\t\u0019!k\\<\u0011\u000b\u00055\u0014QO9\t\u0011\u0005m\u0012q\fa\u0001\u0003{A\u0001\"a\"\u0002`\u0001\u0007\u0011\u0011R\u0001\rgB\f'o[\"p]R,\u0007\u0010\u001e\t\u0005\u0003\u0017\u000bi)D\u0001\u0019\u0013\r\ty\t\u0007\u0002\r'B\f'o[\"p]R,\u0007\u0010\u001e\u0005\t\u0003'\u000by\u00061\u0001\u0002\u0016\u0006q!o\\<Ue\u0006t7OZ8s[\u0016\u0014\bcB\u0007\u0002\u0018\u0006m\u00141P\u0005\u0004\u00033s!!\u0003$v]\u000e$\u0018n\u001c82\u0011\u001d\ti\n\u0001C\t\u0003?\u000bqbZ3u'B\f'o[\"p]R,\u0007\u0010\u001e\u000b\u0005\u0003\u0013\u000b\t\u000b\u0003\u0004?\u00037\u0003\ra\u0010\u0005\b\u0003K\u0003A\u0011CAT\u000399W\r^*rY\u0016CXmY;u_J$B!!\u0017\u0002*\"A\u00111HAR\u0001\u0004\ti\u0004C\u0004\u0002.\u0002!I!a,\u0002\u001bM\fg/\u001a#bi\u00064%/Y7f)-\t\u0018\u0011WA[\u0003s\u000bY,!0\t\u0011\u0005M\u00161\u0016a\u0001\u0003\u001f\nA\u0002^1cY\u0016l\u0015M\\1hKJDq!a.\u0002,\u0002\u0007A,A\u0005eKN$H+\u00192mK\"A\u00111AAV\u0001\u0004\t)\u0001\u0003\u0005\u0002<\u0005-\u0006\u0019AA\u001f\u0011!\t9)a+A\u0002\u0005%uaBAa\u0005!\u0005\u00111Y\u0001\u001a\u000fJ,WM\u001c9mk6\u0014V\r\\1uS>t\u0007K]8wS\u0012,'\u000fE\u0002'\u0003\u000b4a!\u0001\u0002\t\u0002\u0005\u001d7cAAc\u0019!9!&!2\u0005\u0002\u0005-GCAAb\u0011!\ty-!2\u0005\u0002\u0005E\u0017!B1qa2LX\u0003BAj\u0003?$B!!6\u0002\\B\u0019\u0001)a6\n\u0007\u0005egCA\bECR\fgI]1nKJ+\u0017\rZ3s\u0011!\ti.!4A\u0004\u0005U\u0017!\u0001;\u0005\u0011\u0005\u0005\u0018Q\u001ab\u0001\u0003G\u0014\u0011!Q\t\u0005\u0003K\fY\u000fE\u0002\u000e\u0003OL1!!;\u000f\u0005\u001dqu\u000e\u001e5j]\u001e\u00042!DAw\u0013\r\tyO\u0004\u0002\u0004\u0003:LhaBAz\u0003\u000b\u001c\u0011Q\u001f\u0002\u0019\u000fJ,WM\u001c9mk6$\u0015\r^1Ge\u0006lWMU3bI\u0016\u00148\u0003BAy\u0003o\u00042!DA}\u0013\r\tYP\u0004\u0002\u0007\u0003:Lh+\u00197\t\u0017\u0005}\u0018\u0011\u001fBC\u0002\u0013\u0005!\u0011A\u0001\u0007e\u0016\fG-\u001a:\u0016\u0005\u0005U\u0007b\u0003B\u0003\u0003c\u0014\t\u0011)A\u0005\u0003+\fqA]3bI\u0016\u0014\b\u0005C\u0004+\u0003c$\tA!\u0003\u0015\t\t-!q\u0002\t\u0005\u0005\u001b\t\t0\u0004\u0002\u0002F\"A\u0011q B\u0004\u0001\u0004\t)\u000eC\u0004\u0006\u0003c$\tAa\u0005\u0015\u0011\u0005\u0015!Q\u0003B\r\u0005;AqAa\u0006\u0003\u0012\u0001\u0007\u0001'A\u0004kI\n\u001cWK\u001d7\t\u000f\tm!\u0011\u0003a\u0001a\u0005IA/\u00192mK:\u000bW.\u001a\u0005\t\u0005?\u0011\t\u00021\u0001\u0003\"\u0005Q\u0001O]8qKJ$\u0018.Z:\u0011\t\t\r\"\u0011F\u0007\u0003\u0005KQ1Aa\nW\u0003\u0011)H/\u001b7\n\t\t-\"Q\u0005\u0002\u000b!J|\u0007/\u001a:uS\u0016\u001c\bbB\u0003\u0002r\u0012\u0005!q\u0006\u000b\u0011\u0003\u000b\u0011\tDa\r\u00036\t]\"1\bB \u0005\u0007BqAa\u0006\u0003.\u0001\u0007\u0001\u0007C\u0004\u0003\u001c\t5\u0002\u0019\u0001\u0019\t\r\r\u0014i\u00031\u00011\u0011\u001d\u0011ID!\fA\u0002A\n\u0001\"^:fe:\fW.\u001a\u0005\b\u0005{\u0011i\u00031\u00011\u0003!\u0001\u0018m]:x_J$\u0007b\u0002B!\u0005[\u0001\r\u0001M\u0001\u0010a\u0006\u0014H/\u001b;j_:\u001cu\u000e\\;n]\"Q!q\u0004B\u0017!\u0003\u0005\rA!\t\t\u0015\t\u001d\u0013\u0011_I\u0001\n\u0003\u0011I%A\nhe\u0016,g\u000e\u001d7v[\u0012\"WMZ1vYR$s'\u0006\u0002\u0003L)\"!\u0011\u0005B'W\t\u0011y\u0005\u0005\u0003\u0003R\tmSB\u0001B*\u0015\u0011\u0011)Fa\u0016\u0002\u0013Ut7\r[3dW\u0016$'b\u0001B-\u001d\u0005Q\u0011M\u001c8pi\u0006$\u0018n\u001c8\n\t\tu#1\u000b\u0002\u0012k:\u001c\u0007.Z2lK\u00124\u0016M]5b]\u000e,\u0007B\u0003B1\u0003c\f\t\u0011\"\u0011\u0003d\u0005A\u0001.Y:i\u0007>$W\rF\u0001r\u0011)\u00119'!=\u0002\u0002\u0013\u0005#\u0011N\u0001\u0007KF,\u0018\r\\:\u0015\t\t-$\u0011\u000f\t\u0004\u001b\t5\u0014b\u0001B8\u001d\t9!i\\8mK\u0006t\u0007B\u0003B:\u0005K\n\t\u00111\u0001\u0002l\u0006\u0019\u0001\u0010J\u0019\t\u0015\t]\u0014QYA\u0001\n\u0007\u0011I(\u0001\rHe\u0016,g\u000e\u001d7v[\u0012\u000bG/\u0019$sC6,'+Z1eKJ$BAa\u0003\u0003|!A\u0011q B;\u0001\u0004\t)\u000e\u0003\u0005\u0003\u0000\u0005\u0015G\u0011\u0001BA\u0003a\u0019\u0007.Z2l!\u0006\u0014H/\u001b;j_:\u001cu\u000e\\;n]RK\b/\u001a\u000b\u0005\u0005\u0007\u0013)\n\u0005\u0005\u0002n\t\u0015%\u0011\u0012BH\u0013\u0011\u00119)!\u001f\u0003\r\u0015KG\u000f[3s!\ri!1R\u0005\u0004\u0005\u001bs!\u0001B+oSR\u0004B!!\u001c\u0003\u0012&!!1SA=\u0005aIE\u000e\\3hC2\f%oZ;nK:$X\t_2faRLwN\u001c\u0005\t\u0005/\u0013i\b1\u0001\u0003\u001a\u000611m\u001c7v[:\u00042!\u001aBN\u0013\r\u0011iJ\u001a\u0002\f'R\u0014Xo\u0019;GS\u0016dGm\u0002\u0006\u0003x\u0005\u0015\u0017\u0011!E\u0001\u0005C\u0003BA!\u0004\u0003$\u001aQ\u00111_Ac\u0003\u0003E\tA!*\u0014\u0007\t\rF\u0002C\u0004+\u0005G#\tA!+\u0015\u0005\t\u0005\u0006\u0002\u0003BW\u0005G#)Aa,\u0002)\u001d\u0014X-\u001a8qYVlG%\u001a=uK:\u001c\u0018n\u001c81)\u0011\u0011\tL!/\u0015\u0011\u0005\u0015!1\u0017B[\u0005oCqAa\u0006\u0003,\u0002\u0007\u0001\u0007C\u0004\u0003\u001c\t-\u0006\u0019\u0001\u0019\t\u0011\t}!1\u0016a\u0001\u0005CA\u0001Ba/\u0003,\u0002\u0007!1B\u0001\u0006IQD\u0017n\u001d\u0005\t\u0005\u007f\u0013\u0019\u000b\"\u0002\u0003B\u0006!rM]3f]BdW/\u001c\u0013fqR,gn]5p]F\"BAa1\u0003TR\u0001\u0012Q\u0001Bc\u0005\u000f\u0014IMa3\u0003N\n='\u0011\u001b\u0005\b\u0005/\u0011i\f1\u00011\u0011\u001d\u0011YB!0A\u0002ABaa\u0019B_\u0001\u0004\u0001\u0004b\u0002B\u001d\u0005{\u0003\r\u0001\r\u0005\b\u0005{\u0011i\f1\u00011\u0011\u001d\u0011\tE!0A\u0002AB!Ba\b\u0003>B\u0005\t\u0019\u0001B\u0011\u0011!\u0011YL!0A\u0002\t-\u0001B\u0003Bl\u0005G\u000b\n\u0011\"\u0002\u0003Z\u0006irM]3f]BdW/\u001c\u0013eK\u001a\fW\u000f\u001c;%o\u0011*\u0007\u0010^3og&|g\u000e\u0006\u0003\u0003L\tm\u0007\u0002\u0003B^\u0005+\u0004\rAa\u0003\t\u0015\t}'1UA\u0001\n\u000b\u0011\t/\u0001\niCND7i\u001c3fI\u0015DH/\u001a8tS>tG\u0003\u0002B2\u0005GD\u0001Ba/\u0003^\u0002\u0007!1\u0002\u0005\u000b\u0005O\u0014\u0019+!A\u0005\u0006\t%\u0018\u0001E3rk\u0006d7\u000fJ3yi\u0016t7/[8o)\u0011\u0011YOa<\u0015\t\t-$Q\u001e\u0005\u000b\u0005g\u0012)/!AA\u0002\u0005-\b\u0002\u0003B^\u0005K\u0004\rAa\u0003")
public class GreenplumRelationProvider implements RelationProvider, DataSourceRegister, CreatableRelationProvider, Logging
{
    private transient Logger io$pivotal$greenplum$spark$Logging$$log_;
    
    public static Either<BoxedUnit, IllegalArgumentException> checkPartitionColumnType(final StructField column) {
        return GreenplumRelationProvider$.MODULE$.checkPartitionColumnType(column);
    }
    
    public static DataFrameReader GreenplumDataFrameReader(final DataFrameReader reader) {
        return GreenplumRelationProvider$.MODULE$.GreenplumDataFrameReader(reader);
    }
    
    public static <A> DataFrameReader apply(final DataFrameReader t) {
        return GreenplumRelationProvider$.MODULE$.apply(t);
    }
    
    public Logger io$pivotal$greenplum$spark$Logging$$log_() {
        return this.io$pivotal$greenplum$spark$Logging$$log_;
    }
    
    public void io$pivotal$greenplum$spark$Logging$$log__$eq(final Logger x$1) {
        this.io$pivotal$greenplum$spark$Logging$$log_ = x$1;
    }
    
    public Logger log() {
        return Logging$class.log(this);
    }
    
    public void logWarning(final Function0<String> msg) {
        Logging$class.logWarning(this, msg);
    }
    
    public void logDebug(final Function0<String> msg) {
        Logging$class.logDebug(this, msg);
    }
    
    public String shortName() {
        return "greenplum";
    }
    
    public BaseRelation createRelation(final SQLContext sqlContext, final Map<String, String> parameters) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: dup            
        //     4: aload_2         /* parameters */
        //     5: invokespecial   io/pivotal/greenplum/spark/conf/GreenplumOptions.<init>:(Lscala/collection/immutable/Map;)V
        //     8: astore_3        /* greenplumOptions */
        //     9: aload_0         /* this */
        //    10: aload_1         /* sqlContext */
        //    11: invokevirtual   io/pivotal/greenplum/spark/GreenplumRelationProvider.getSparkContext:(Lorg/apache/spark/sql/SQLContext;)Lorg/apache/spark/SparkContext;
        //    14: astore          sparkContext
        //    16: getstatic       io/pivotal/greenplum/spark/jdbc/ConnectionManager$.MODULE$:Lio/pivotal/greenplum/spark/jdbc/ConnectionManager$;
        //    19: aload_3         /* greenplumOptions */
        //    20: getstatic       io/pivotal/greenplum/spark/jdbc/ConnectionManager$.MODULE$:Lio/pivotal/greenplum/spark/jdbc/ConnectionManager$;
        //    23: invokevirtual   io/pivotal/greenplum/spark/jdbc/ConnectionManager$.getConnection$default$2:()Z
        //    26: invokevirtual   io/pivotal/greenplum/spark/jdbc/ConnectionManager$.getConnection:(Lio/pivotal/greenplum/spark/conf/GreenplumOptions;Z)Ljava/sql/Connection;
        //    29: astore          conn
        //    31: new             Lio/pivotal/greenplum/spark/externaltable/GreenplumQualifiedName;
        //    34: dup            
        //    35: aload_3         /* greenplumOptions */
        //    36: invokevirtual   io/pivotal/greenplum/spark/conf/GreenplumOptions.dbSchema:()Ljava/lang/String;
        //    39: aload_3         /* greenplumOptions */
        //    40: invokevirtual   io/pivotal/greenplum/spark/conf/GreenplumOptions.dbTable:()Ljava/lang/String;
        //    43: invokespecial   io/pivotal/greenplum/spark/externaltable/GreenplumQualifiedName.<init>:(Ljava/lang/String;Ljava/lang/String;)V
        //    46: astore          table
        //    48: aload_3         /* greenplumOptions */
        //    49: invokevirtual   io/pivotal/greenplum/spark/conf/GreenplumOptions.url:()Ljava/lang/String;
        //    52: astore          url
        //    54: getstatic       io/pivotal/greenplum/spark/jdbc/Jdbc$.MODULE$:Lio/pivotal/greenplum/spark/jdbc/Jdbc$;
        //    57: aload           conn
        //    59: aload           url
        //    61: aload           table
        //    63: invokevirtual   io/pivotal/greenplum/spark/jdbc/Jdbc$.resolveTable:(Ljava/sql/Connection;Ljava/lang/String;Lio/pivotal/greenplum/spark/externaltable/GreenplumQualifiedName;)Lorg/apache/spark/sql/types/StructType;
        //    66: astore          schema
        //    68: aload_3         /* greenplumOptions */
        //    69: invokevirtual   io/pivotal/greenplum/spark/conf/GreenplumOptions.partitionColumn:()Ljava/lang/String;
        //    72: astore          partitionColumnName
        //    74: aload_3         /* greenplumOptions */
        //    75: invokevirtual   io/pivotal/greenplum/spark/conf/GreenplumOptions.partitions:()Lscala/Option;
        //    78: astore          partitionsCount
        //    80: getstatic       io/pivotal/greenplum/spark/jdbc/Jdbc$.MODULE$:Lio/pivotal/greenplum/spark/jdbc/Jdbc$;
        //    83: aload           conn
        //    85: invokevirtual   io/pivotal/greenplum/spark/jdbc/Jdbc$.retrieveSegmentIds:(Ljava/sql/Connection;)[I
        //    88: astore          gpSegmentIds
        //    90: aload_0         /* this */
        //    91: aload           conn
        //    93: aload           table
        //    95: aload           schema
        //    97: aload           partitionColumnName
        //    99: aload           partitionsCount
        //   101: aload           gpSegmentIds
        //   103: invokevirtual   io/pivotal/greenplum/spark/GreenplumRelationProvider.computePartitions:(Ljava/sql/Connection;Lio/pivotal/greenplum/spark/externaltable/GreenplumQualifiedName;Lorg/apache/spark/sql/types/StructType;Ljava/lang/String;Lscala/Option;[I)[Lio/pivotal/greenplum/spark/GreenplumPartition;
        //   106: astore          partitions
        //   108: aload_0         /* this */
        //   109: invokevirtual   io/pivotal/greenplum/spark/GreenplumRelationProvider.log:()Lorg/slf4j/Logger;
        //   112: new             Lscala/StringContext;
        //   115: dup            
        //   116: getstatic       scala/Predef$.MODULE$:Lscala/Predef$;
        //   119: iconst_2       
        //   120: anewarray       Ljava/lang/String;
        //   123: dup            
        //   124: iconst_0       
        //   125: ldc             "NumPartitions = "
        //   127: aastore        
        //   128: dup            
        //   129: iconst_1       
        //   130: ldc             ""
        //   132: aastore        
        //   133: checkcast       [Ljava/lang/Object;
        //   136: invokevirtual   scala/Predef$.wrapRefArray:([Ljava/lang/Object;)Lscala/collection/mutable/WrappedArray;
        //   139: invokespecial   scala/StringContext.<init>:(Lscala/collection/Seq;)V
        //   142: getstatic       scala/Predef$.MODULE$:Lscala/Predef$;
        //   145: iconst_1       
        //   146: anewarray       Ljava/lang/Object;
        //   149: dup            
        //   150: iconst_0       
        //   151: aload           partitions
        //   153: arraylength    
        //   154: invokestatic    scala/runtime/BoxesRunTime.boxToInteger:(I)Ljava/lang/Integer;
        //   157: aastore        
        //   158: invokevirtual   scala/Predef$.genericWrapArray:(Ljava/lang/Object;)Lscala/collection/mutable/WrappedArray;
        //   161: invokevirtual   scala/StringContext.s:(Lscala/collection/Seq;)Ljava/lang/String;
        //   164: invokeinterface org/slf4j/Logger.debug:(Ljava/lang/String;)V
        //   169: getstatic       io/pivotal/greenplum/spark/externaltable/GreenplumTableManager$.MODULE$:Lio/pivotal/greenplum/spark/externaltable/GreenplumTableManager$;
        //   172: aload           sparkContext
        //   174: invokevirtual   org/apache/spark/SparkContext.applicationId:()Ljava/lang/String;
        //   177: aload_3         /* greenplumOptions */
        //   178: invokevirtual   io/pivotal/greenplum/spark/conf/GreenplumOptions.dbTable:()Ljava/lang/String;
        //   181: invokevirtual   io/pivotal/greenplum/spark/externaltable/GreenplumTableManager$.generateExternalTableNamePrefix:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
        //   184: astore          tablePrefix
        //   186: aload_0         /* this */
        //   187: aload           tablePrefix
        //   189: aload_3         /* greenplumOptions */
        //   190: invokevirtual   io/pivotal/greenplum/spark/GreenplumRelationProvider.createDropExternalTablesSparkListener:(Ljava/lang/String;Lio/pivotal/greenplum/spark/conf/GreenplumOptions;)Lio/pivotal/greenplum/spark/externaltable/DropExternalTablesSparkListener;
        //   193: astore          dropTablesListener
        //   195: aload           sparkContext
        //   197: aload           dropTablesListener
        //   199: invokevirtual   org/apache/spark/SparkContext.addSparkListener:(Lorg/apache/spark/scheduler/SparkListenerInterface;)V
        //   202: new             Lio/pivotal/greenplum/spark/GreenplumRelation;
        //   205: dup            
        //   206: aload           schema
        //   208: aload           partitions
        //   210: aload_3         /* greenplumOptions */
        //   211: aload_1         /* sqlContext */
        //   212: invokespecial   io/pivotal/greenplum/spark/GreenplumRelation.<init>:(Lorg/apache/spark/sql/types/StructType;[Lio/pivotal/greenplum/spark/GreenplumPartition;Lio/pivotal/greenplum/spark/conf/GreenplumOptions;Lorg/apache/spark/sql/SQLContext;)V
        //   215: aload           conn
        //   217: invokeinterface java/sql/Connection.close:()V
        //   222: areturn        
        //   223: astore          6
        //   225: aload           5
        //   227: invokeinterface java/sql/Connection.close:()V
        //   232: aload           6
        //   234: athrow         
        //    Signature:
        //  (Lorg/apache/spark/sql/SQLContext;Lscala/collection/immutable/Map<Ljava/lang/String;Ljava/lang/String;>;)Lorg/apache/spark/sql/sources/BaseRelation;
        //    StackMapTable: 00 01 FF 00 DF 00 06 07 00 02 07 00 DC 07 00 DE 07 00 52 07 00 C0 07 00 D6 00 01 07 00 E0
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type
        //  -----  -----  -----  -----  ----
        //  31     215    223    235    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        //     at com.strobel.decompiler.ast.AstBuilder.convertLocalVariables(AstBuilder.java:2895)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2445)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:211)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public GreenplumPartition[] computePartitions(final Connection conn, final GreenplumQualifiedName table, final StructType schema, final String partitionColumnName, final Option<Object> partitionsCount, final int[] gpSegmentIds) {
        final String default_PARTITION_COLUMN_NAME = GreenplumOptions$.MODULE$.DEFAULT_PARTITION_COLUMN_NAME();
        Label_0089: {
            if (partitionColumnName == null) {
                if (default_PARTITION_COLUMN_NAME != null) {
                    break Label_0089;
                }
            }
            else if (!partitionColumnName.equals(default_PARTITION_COLUMN_NAME)) {
                break Label_0089;
            }
            Predef$.MODULE$.assert(partitionsCount.isEmpty(), (Function0)new GreenplumRelationProvider$$anonfun$computePartitions.GreenplumRelationProvider$$anonfun$computePartitions$1(this, partitionColumnName));
            return (GreenplumPartition[])Predef$.MODULE$.refArrayOps((Object[])GreenplumRelation$.MODULE$.segmentPartitions(gpSegmentIds)).toArray(ClassTag$.MODULE$.apply((Class)GreenplumPartition.class));
        }
        final Option find = Predef$.MODULE$.refArrayOps((Object[])schema.fields()).find((Function1)new GreenplumRelationProvider$$anonfun.GreenplumRelationProvider$$anonfun$2(this, partitionColumnName));
        GreenplumPartition[] columnPartition;
        if (find instanceof Some) {
            final StructField partitionColumn;
            final StructField structField = partitionColumn = (StructField)((Some)find).x();
            final int numPartitions = BoxesRunTime.unboxToInt(partitionsCount.getOrElse((Function0)new GreenplumRelationProvider$$anonfun.GreenplumRelationProvider$$anonfun$1(this, gpSegmentIds)));
            final Either<BoxedUnit, IllegalArgumentException> checkPartitionColumnType = GreenplumRelationProvider$.MODULE$.checkPartitionColumnType(partitionColumn);
            if (checkPartitionColumnType instanceof Left) {
                final ColumnValueRange range = Jdbc$.MODULE$.computeColumnValueRange(conn, table, partitionColumnName);
                columnPartition = GreenplumRelation$.MODULE$.columnPartition(numPartitions, partitionColumn, range);
            }
            else {
                if (checkPartitionColumnType instanceof Right) {
                    final IllegalArgumentException e = (IllegalArgumentException)((Right)checkPartitionColumnType).b();
                    throw e;
                }
                throw new MatchError((Object)checkPartitionColumnType);
            }
        }
        else {
            if (None$.MODULE$.equals(find)) {
                throw new IllegalArgumentException(new StringContext((Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[] { "'", "' does not exist in ", " table" })).s((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { partitionColumnName, table })));
            }
            throw new MatchError((Object)find);
        }
        return columnPartition;
    }
    
    public BaseRelation createRelation(final SQLContext sqlContext, final SaveMode mode, final Map<String, String> parameters, final Dataset<Row> data) {
        final ObjectRef relation$lzy = ObjectRef.zero();
        final VolatileByteRef bitmap$0 = VolatileByteRef.create((byte)0);
        final GreenplumOptions greenplumOptions = new GreenplumOptions(parameters);
        final SparkContext sparkContext = this.getSparkContext(sqlContext);
        final GreenplumQualifiedName destTable = new GreenplumQualifiedName(greenplumOptions.dbSchema(), greenplumOptions.dbTable());
        final String tablePrefix = GreenplumTableManager$.MODULE$.generateExternalTableNamePrefix(sparkContext.applicationId(), greenplumOptions.dbTable());
        final DropExternalTablesSparkListener dropTablesListener = this.createDropExternalTablesSparkListener(tablePrefix, greenplumOptions);
        sparkContext.addSparkListener((SparkListenerInterface)dropTablesListener);
        final SqlExecutor executor = this.getSqlExecutor(greenplumOptions);
        final GreenplumTableManager tableManager = this.getTableManager(executor);
        final boolean tableCreated = BoxesRunTime.unboxToBoolean(tableManager.prepareTableForWrite(destTable, data.schema(), greenplumOptions, mode).get());
        Label_0218: {
            if (!tableCreated) {
                final SaveMode ignore = SaveMode.Ignore;
                if (mode == null) {
                    if (ignore != null) {
                        break Label_0218;
                    }
                }
                else if (!mode.equals(ignore)) {
                    break Label_0218;
                }
                this.log().debug(new StringContext((Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[] { "Table ", " already exists with SaveMode.Ignore. Data is ignored." })).s((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { destTable })));
                final BoxedUnit unit = BoxedUnit.UNIT;
                return (BaseRelation)new GreenplumRelationProvider$$anon.GreenplumRelationProvider$$anon$1(this, sqlContext, (Map)parameters, relation$lzy, bitmap$0);
            }
        }
        BoxesRunTime.boxToInteger(this.saveDataFrame(tableManager, destTable, data, greenplumOptions, sparkContext));
        return (BaseRelation)new GreenplumRelationProvider$$anon.GreenplumRelationProvider$$anon$1(this, sqlContext, (Map)parameters, relation$lzy, bitmap$0);
    }
    
    public DropExternalTablesSparkListener createDropExternalTablesSparkListener(final String tablePrefix, final GreenplumOptions greenplumOptions) {
        return new DropExternalTablesSparkListener(tablePrefix, greenplumOptions);
    }
    
    public GreenplumTableManager getTableManager(final SqlExecutor executor) {
        return new GreenplumTableManager(executor);
    }
    
    public Function2<Object, Iterator<Row>, Iterator<Object>> getPartitionWriter(final GreenplumOptions greenplumOptions, final SparkContext sparkContext, final Function1<Row, Row> rowTransformer) {
        return new PartitionWriter(sparkContext.applicationId(), greenplumOptions, rowTransformer).getClosure();
    }
    
    public SparkContext getSparkContext(final SQLContext sqlContext) {
        return sqlContext.sparkContext();
    }
    
    public SqlExecutor getSqlExecutor(final GreenplumOptions greenplumOptions) {
        return new SqlExecutor(new ConnectionManager(HikariProvider$.MODULE$), greenplumOptions);
    }
    
    private int saveDataFrame(final GreenplumTableManager tableManager, final GreenplumQualifiedName destTable, final Dataset<Row> data, final GreenplumOptions greenplumOptions, final SparkContext sparkContext) {
        final Seq gpdbColumns = (Seq)tableManager.getColumnNames(destTable).get();
        final Function1 rowTransformer = (Function1)RowTransformer$.MODULE$.getFunction((Seq<String>)Predef$.MODULE$.wrapRefArray((Object[])data.columns()), (Seq<String>)gpdbColumns).get();
        final Function2 partitionWriter = this.getPartitionWriter(greenplumOptions, sparkContext, (Function1<Row, Row>)rowTransformer);
        final RDD rowCounts = data.rdd().mapPartitionsWithIndex(partitionWriter, data.rdd().mapPartitionsWithIndex$default$2(), ClassTag$.MODULE$.Int());
        return BoxesRunTime.unboxToInt(rowCounts.fold((Object)BoxesRunTime.boxToInteger(0), (Function2)new GreenplumRelationProvider$$anonfun$saveDataFrame.GreenplumRelationProvider$$anonfun$saveDataFrame$1(this)));
    }
    
    private final BaseRelation relation$lzycompute$1(final SQLContext sqlContext$1, final Map parameters$1, final ObjectRef relation$lzy$1, final VolatileByteRef bitmap$0$1) {
        synchronized (this) {
            if ((byte)(bitmap$0$1.elem & 0x1) == 0) {
                relation$lzy$1.elem = this.createRelation(sqlContext$1, (Map<String, String>)parameters$1);
                bitmap$0$1.elem |= 0x1;
            }
            final BoxedUnit unit = BoxedUnit.UNIT;
            return (BaseRelation)relation$lzy$1.elem;
        }
    }
    
    public final BaseRelation io$pivotal$greenplum$spark$GreenplumRelationProvider$$relation$1(final SQLContext sqlContext$1, final Map parameters$1, final ObjectRef relation$lzy$1, final VolatileByteRef bitmap$0$1) {
        return (BaseRelation)(((byte)(bitmap$0$1.elem & 0x1) == 0) ? this.relation$lzycompute$1(sqlContext$1, parameters$1, relation$lzy$1, bitmap$0$1) : relation$lzy$1.elem);
    }
    
    public GreenplumRelationProvider() {
        Logging$class.$init$(this);
    }
    
    public static final class GreenplumDataFrameReader
    {
        private final DataFrameReader reader;
        
        public DataFrameReader reader() {
            return this.reader;
        }
        
        public Dataset<Row> greenplum(final String jdbcUrl, final String tableName, final Properties properties) {
            return GreenplumDataFrameReader$.MODULE$.greenplum$extension0(this.reader(), jdbcUrl, tableName, properties);
        }
        
        public Dataset<Row> greenplum(final String jdbcUrl, final String tableName, final String schema, final String username, final String password, final String partitionColumn, final Properties properties) {
            return GreenplumDataFrameReader$.MODULE$.greenplum$extension1(this.reader(), jdbcUrl, tableName, schema, username, password, partitionColumn, properties);
        }
        
        public Properties greenplum$default$7() {
            return GreenplumDataFrameReader$.MODULE$.greenplum$default$7$extension(this.reader());
        }
        
        @Override
        public int hashCode() {
            return GreenplumDataFrameReader$.MODULE$.hashCode$extension(this.reader());
        }
        
        @Override
        public boolean equals(final Object x$1) {
            return GreenplumDataFrameReader$.MODULE$.equals$extension(this.reader(), x$1);
        }
        
        public GreenplumDataFrameReader(final DataFrameReader reader) {
            this.reader = reader;
        }
    }
    
    public static class GreenplumDataFrameReader$
    {
        public static final GreenplumDataFrameReader$ MODULE$;
        
        static {
            new GreenplumDataFrameReader$();
        }
        
        public final Dataset<Row> greenplum$extension0(final DataFrameReader $this, final String jdbcUrl, final String tableName, final Properties properties) {
            return (Dataset<Row>)$this.format("greenplum").options((scala.collection.Map)JavaConverters$.MODULE$.propertiesAsScalaMapConverter(properties).asScala()).option(GreenplumOptions$.MODULE$.GPDB_URL(), jdbcUrl).option(GreenplumOptions$.MODULE$.GPDB_TABLE_NAME(), tableName).load();
        }
        
        public final Dataset<Row> greenplum$extension1(final DataFrameReader $this, final String jdbcUrl, final String tableName, final String schema, final String username, final String password, final String partitionColumn, final Properties properties) {
            return (Dataset<Row>)$this.format("greenplum").options((scala.collection.Map)JavaConverters$.MODULE$.propertiesAsScalaMapConverter(properties).asScala()).option(GreenplumOptions$.MODULE$.GPDB_URL(), jdbcUrl).option(GreenplumOptions$.MODULE$.GPDB_SCHEMA_NAME(), schema).option(GreenplumOptions$.MODULE$.GPDB_TABLE_NAME(), tableName).option(GreenplumOptions$.MODULE$.GPDB_USER(), username).option(GreenplumOptions$.MODULE$.GPDB_PASSWORD(), password).option(GreenplumOptions$.MODULE$.GPDB_PARTITION_COLUMN(), partitionColumn).load();
        }
        
        public final Properties greenplum$default$7$extension(final DataFrameReader $this) {
            return new Properties();
        }
        
        public final int hashCode$extension(final DataFrameReader $this) {
            return $this.hashCode();
        }
        
        public final boolean equals$extension(final DataFrameReader $this, final Object x$1) {
            if (x$1 instanceof GreenplumDataFrameReader) {
                final DataFrameReader obj = (x$1 == null) ? null : ((GreenplumDataFrameReader)x$1).reader();
                boolean b = false;
                Label_0071: {
                    Label_0070: {
                        if ($this == null) {
                            if (obj != null) {
                                break Label_0070;
                            }
                        }
                        else if (!$this.equals(obj)) {
                            break Label_0070;
                        }
                        b = true;
                        break Label_0071;
                    }
                    b = false;
                }
                if (b) {
                    return true;
                }
            }
            return false;
        }
        
        public GreenplumDataFrameReader$() {
            MODULE$ = this;
        }
    }
}
