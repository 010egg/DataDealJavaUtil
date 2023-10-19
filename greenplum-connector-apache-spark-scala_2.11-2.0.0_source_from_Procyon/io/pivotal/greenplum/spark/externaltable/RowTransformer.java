// 
// Decompiled by Procyon v0.5.36
// 

package io.pivotal.greenplum.spark.externaltable;

import org.apache.spark.sql.Row;
import scala.Function1;
import scala.util.Try;
import scala.collection.Seq;
import org.slf4j.Logger;
import scala.Function0;
import scala.reflect.ScalaSignature;

@ScalaSignature(bytes = "\u0006\u0001];Q!\u0001\u0002\t\u00025\taBU8x)J\fgn\u001d4pe6,'O\u0003\u0002\u0004\t\u0005iQ\r\u001f;fe:\fG\u000e^1cY\u0016T!!\u0002\u0004\u0002\u000bM\u0004\u0018M]6\u000b\u0005\u001dA\u0011!C4sK\u0016t\u0007\u000f\\;n\u0015\tI!\"A\u0004qSZ|G/\u00197\u000b\u0003-\t!![8\u0004\u0001A\u0011abD\u0007\u0002\u0005\u0019)\u0001C\u0001E\u0001#\tq!k\\<Ue\u0006t7OZ8s[\u0016\u00148cA\b\u00131A\u00111CF\u0007\u0002))\tQ#A\u0003tG\u0006d\u0017-\u0003\u0002\u0018)\t1\u0011I\\=SK\u001a\u0004\"!\u0007\u000e\u000e\u0003\u0011I!a\u0007\u0003\u0003\u000f1{wmZ5oO\")Qd\u0004C\u0001=\u00051A(\u001b8jiz\"\u0012!\u0004\u0005\bA=\u0011\r\u0011\"\u0001\"\u0003AIG-\u001a8uSRLh)\u001e8di&|g.F\u0001#!\u0011\u00192%J\u0013\n\u0005\u0011\"\"!\u0003$v]\u000e$\u0018n\u001c82!\t1c&D\u0001(\u0015\tA\u0013&A\u0002tc2T!!\u0002\u0016\u000b\u0005-b\u0013AB1qC\u000eDWMC\u0001.\u0003\ry'oZ\u0005\u0003_\u001d\u00121AU8x\u0011\u0019\tt\u0002)A\u0005E\u0005\t\u0012\u000eZ3oi&$\u0018PR;oGRLwN\u001c\u0011\t\u000bMzA\u0011\u0001\u001b\u0002\u0017\u001d,GOR;oGRLwN\u001c\u000b\u0004km\u0002\u0006c\u0001\u001c:E5\tqG\u0003\u00029)\u0005!Q\u000f^5m\u0013\tQtGA\u0002UefDQ\u0001\u0010\u001aA\u0002u\n\u0011b\u001d9be.\u001cu\u000e\\:\u0011\u0007y2\u0015J\u0004\u0002@\t:\u0011\u0001iQ\u0007\u0002\u0003*\u0011!\tD\u0001\u0007yI|w\u000e\u001e \n\u0003UI!!\u0012\u000b\u0002\u000fA\f7m[1hK&\u0011q\t\u0013\u0002\u0004'\u0016\f(BA#\u0015!\tQUJ\u0004\u0002\u0014\u0017&\u0011A\nF\u0001\u0007!J,G-\u001a4\n\u00059{%AB*ue&twM\u0003\u0002M)!)\u0011K\ra\u0001{\u0005Aq\r\u001d3c\u0007>d7\u000fC\u0003T\u001f\u0011%A+\u0001\tg_Jl\u0017\r^\"pYVlg\u000eT5tiR\u0011\u0011*\u0016\u0005\u0006-J\u0003\r!P\u0001\bG>dW/\u001c8t\u0001")
public final class RowTransformer
{
    public static void logDebug(final Function0<String> msg) {
        RowTransformer$.MODULE$.logDebug(msg);
    }
    
    public static void logWarning(final Function0<String> msg) {
        RowTransformer$.MODULE$.logWarning(msg);
    }
    
    public static Logger log() {
        return RowTransformer$.MODULE$.log();
    }
    
    public static Try<Function1<Row, Row>> getFunction(final Seq<String> sparkCols, final Seq<String> gpdbCols) {
        return RowTransformer$.MODULE$.getFunction(sparkCols, gpdbCols);
    }
    
    public static Function1<Row, Row> identityFunction() {
        return RowTransformer$.MODULE$.identityFunction();
    }
}
