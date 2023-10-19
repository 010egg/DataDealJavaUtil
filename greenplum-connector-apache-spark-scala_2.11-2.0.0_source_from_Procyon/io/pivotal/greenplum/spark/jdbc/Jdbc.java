// 
// Decompiled by Procyon v0.5.36
// 

package io.pivotal.greenplum.spark.jdbc;

import io.pivotal.greenplum.spark.GpfdistLocation;
import io.pivotal.greenplum.spark.externaltable.GreenplumQualifiedName;
import scala.collection.Seq;
import org.apache.spark.sql.types.StructType;
import java.sql.ResultSetMetaData;
import java.sql.Connection;
import org.slf4j.Logger;
import scala.Function0;
import scala.reflect.ScalaSignature;

@ScalaSignature(bytes = "\u0006\u0001\u0005Ex!B\u0001\u0003\u0011\u0003i\u0011\u0001\u0002&eE\u000eT!a\u0001\u0003\u0002\t)$'m\u0019\u0006\u0003\u000b\u0019\tQa\u001d9be.T!a\u0002\u0005\u0002\u0013\u001d\u0014X-\u001a8qYVl'BA\u0005\u000b\u0003\u001d\u0001\u0018N^8uC2T\u0011aC\u0001\u0003S>\u001c\u0001\u0001\u0005\u0002\u000f\u001f5\t!AB\u0003\u0011\u0005!\u0005\u0011C\u0001\u0003KI\n\u001c7cA\b\u00131A\u00111CF\u0007\u0002))\tQ#A\u0003tG\u0006d\u0017-\u0003\u0002\u0018)\t1\u0011I\\=SK\u001a\u0004\"!\u0007\u000e\u000e\u0003\u0011I!a\u0007\u0003\u0003\u000f1{wmZ5oO\")Qd\u0004C\u0001=\u00051A(\u001b8jiz\"\u0012!\u0004\u0005\u0006A=!\t!I\u0001\nG>\u0004\u0018\u0010V1cY\u0016$bAI\u00130oe\u0012\u0005CA\n$\u0013\t!CC\u0001\u0003V]&$\b\"\u0002\u0014 \u0001\u00049\u0013\u0001B2p]:\u0004\"\u0001K\u0017\u000e\u0003%R!AK\u0016\u0002\u0007M\fHNC\u0001-\u0003\u0011Q\u0017M^1\n\u00059J#AC\"p]:,7\r^5p]\")\u0001g\ba\u0001c\u0005A1O]2UC\ndW\r\u0005\u00023k5\t1G\u0003\u00025\t\u0005iQ\r\u001f;fe:\fG\u000e^1cY\u0016L!AN\u001a\u0003-\u001d\u0013X-\u001a8qYVl\u0017+^1mS\u001aLW\r\u001a(b[\u0016DQ\u0001O\u0010A\u0002E\n\u0001\u0002Z:u)\u0006\u0014G.\u001a\u0005\u0006u}\u0001\raO\u0001\naJ,G-[2bi\u0016\u0004\"\u0001P \u000f\u0005Mi\u0014B\u0001 \u0015\u0003\u0019\u0001&/\u001a3fM&\u0011\u0001)\u0011\u0002\u0007'R\u0014\u0018N\\4\u000b\u0005y\"\u0002\"B\" \u0001\u0004!\u0015aB2pYVlgn\u001d\t\u0004\u000b6[dB\u0001$L\u001d\t9%*D\u0001I\u0015\tIE\"\u0001\u0004=e>|GOP\u0005\u0002+%\u0011A\nF\u0001\ba\u0006\u001c7.Y4f\u0013\tquJA\u0002TKFT!\u0001\u0014\u000b\t\u000bE{A\u0011\u0001*\u0002'\u0015DH/\u001a:oC2$\u0016M\u00197f\u000bbL7\u000f^:\u0015\u0007M3v\u000b\u0005\u0002\u0014)&\u0011Q\u000b\u0006\u0002\b\u0005>|G.Z1o\u0011\u00151\u0003\u000b1\u0001(\u0011\u0015A\u0006\u000b1\u00012\u0003\u0015!\u0018M\u00197f\u0011\u0015Qv\u0002\"\u0001\\\u0003I9W\r^\"pYVlgn]'fi\u0006$\u0017\r^1\u0015\t\u0011cVL\u0018\u0005\u0006Me\u0003\ra\n\u0005\u00061f\u0003\r!\r\u0005\u0006?f\u0003\r\u0001R\u0001\fG>dW/\u001c8OC6,7\u000fC\u0003b\u001f\u0011\u0005!-\u0001\u0012de\u0016\fG/Z$qM\u0012L7\u000f^,sSR\f'\r\\3FqR,'O\\1m)\u0006\u0014G.\u001a\u000b\u0007E\r$Wm\u001a7\t\u000b\u0019\u0002\u0007\u0019A\u0014\t\u000bA\u0002\u0007\u0019A\u0019\t\u000b\u0019\u0004\u0007\u0019A\u0019\u0002\u0011\u0015DH\u000fV1cY\u0016DQ\u0001\u001b1A\u0002%\fqb\u001a9gI&\u001cH\u000fT8dCRLwN\u001c\t\u00033)L!a\u001b\u0003\u0003\u001f\u001d\u0003h\rZ5ti2{7-\u0019;j_:DQa\u00111A\u0002\u0011CQA\\\b\u0005\u0002=\f!\u0003\u001a:pa\u0016CH/\u001a:oC2$\u0016M\u00197fgR!!\u0005]9t\u0011\u00151S\u000e1\u0001(\u0011\u0015\u0011X\u000e1\u0001<\u0003\u0019\u00198\r[3nC\")A/\u001ca\u0001w\u00059R\r\u001f;fe:\fG\u000eV1cY\u0016t\u0015-\\3Qe\u00164\u0017\u000e\u001f\u0005\u0006m>!\ta^\u0001\u0013e\u0016$(/[3wKN+w-\\3oi&#7\u000f\u0006\u0002y}B\u00191#_>\n\u0005i$\"!B!se\u0006L\bCA\n}\u0013\tiHCA\u0002J]RDQAJ;A\u0002\u001dB\u0001\"!\u0001\u0010\t\u0003!\u00111A\u0001\u0010e\u0016$(/[3wKJ+7/\u001e7ugV!\u0011QAA\b)!\t9!!\r\u00024\u0005]B\u0003BA\u0005\u0003C\u0001B!R'\u0002\fA!\u0011QBA\b\u0019\u0001!q!!\u0005\u0000\u0005\u0004\t\u0019BA\u0001U#\u0011\t)\"a\u0007\u0011\u0007M\t9\"C\u0002\u0002\u001aQ\u0011qAT8uQ&tw\rE\u0002\u0014\u0003;I1!a\b\u0015\u0005\r\te.\u001f\u0005\n\u0003Gy\u0018\u0011!a\u0002\u0003K\t!\"\u001a<jI\u0016t7-\u001a\u00132!\u0019\t9#!\f\u0002\f5\u0011\u0011\u0011\u0006\u0006\u0004\u0003W!\u0012a\u0002:fM2,7\r^\u0005\u0005\u0003_\tIC\u0001\u0005DY\u0006\u001c8\u000fV1h\u0011\u00151s\u00101\u0001(\u0011\u0019\t)d a\u0001w\u0005A1/\u001d7Rk\u0016\u0014\u0018\u0010C\u0004\u0002:}\u0004\r!a\u000f\u0002\r\u001d,G\u000f^3s!\u001d\u0019\u0012QHA!\u0003\u0017I1!a\u0010\u0015\u0005%1UO\\2uS>t\u0017\u0007E\u0002)\u0003\u0007J1!!\u0012*\u0005%\u0011Vm];miN+G\u000fC\u0004\u0002J=!\t!a\u0013\u0002/\r|W\u000e];uK\u000e{G.^7o-\u0006dW/\u001a*b]\u001e,G\u0003CA'\u0003'\n)&a\u0016\u0011\u00079\ty%C\u0002\u0002R\t\u0011\u0001cQ8mk6tg+\u00197vKJ\u000bgnZ3\t\r\u0019\n9\u00051\u0001(\u0011\u0019A\u0016q\ta\u0001c!9\u0011\u0011LA$\u0001\u0004Y\u0014AC2pYVlgNT1nK\"9\u0011QL\b\u0005\n\u0005}\u0013!F9vKJL8i\u001c7v[:4\u0016\r\\;f%\u0006tw-\u001a\u000b\u0007\u0003C\n9'!\u001b\u0011\r\u0015\u000b\u0019gOA'\u0013\r\t)g\u0014\u0002\u0007\u000b&$\b.\u001a:\t\r\u0019\nY\u00061\u0001(\u0011\u001d\t)$a\u0017A\u0002mBq!!\u001c\u0010\t\u0003\ty'\u0001\u0007sKN|GN^3UC\ndW\r\u0006\u0005\u0002r\u0005%\u00151RAH!\u0011\t\u0019(!\"\u000e\u0005\u0005U$\u0002BA<\u0003s\nQ\u0001^=qKNT1AKA>\u0015\r)\u0011Q\u0010\u0006\u0005\u0003\u007f\n\t)\u0001\u0004ba\u0006\u001c\u0007.\u001a\u0006\u0003\u0003\u0007\u000b1a\u001c:h\u0013\u0011\t9)!\u001e\u0003\u0015M#(/^2u)f\u0004X\r\u0003\u0004'\u0003W\u0002\ra\n\u0005\b\u0003\u001b\u000bY\u00071\u0001<\u0003\r)(\u000f\u001c\u0005\u00071\u0006-\u0004\u0019A\u0019\t\u000f\u0005Mu\u0002\"\u0001\u0002\u0016\u0006qq-\u001a;D_2,XN\u001c(b[\u0016\u001cHc\u0001#\u0002\u0018\"A\u0011\u0011TAI\u0001\u0004\tY*\u0001\u0003nKR\f\u0007c\u0001\u0015\u0002\u001e&\u0019\u0011qT\u0015\u0003#I+7/\u001e7u'\u0016$X*\u001a;b\t\u0006$\u0018\rC\u0004\u0002$>!\t!!*\u0002\u0013\u001d,GoU2iK6\fG\u0003BA9\u0003OC\u0001\"!'\u0002\"\u0002\u0007\u00111\u0014\u0005\b\u0003W{A\u0011BAW\u0003=9W\r^\"bi\u0006d\u0017p\u001d;UsB,GCCAX\u0003k\u000bI,!0\u0002BB!\u00111OAY\u0013\u0011\t\u0019,!\u001e\u0003\u0011\u0011\u000bG/\u0019+za\u0016Dq!a.\u0002*\u0002\u000710A\u0004tc2$\u0016\u0010]3\t\u000f\u0005m\u0016\u0011\u0016a\u0001w\u0006I\u0001O]3dSNLwN\u001c\u0005\b\u0003\u007f\u000bI\u000b1\u0001|\u0003\u0015\u00198-\u00197f\u0011\u001d\t\u0019-!+A\u0002M\u000baa]5h]\u0016$\u0007bBAd\u001f\u0011%\u0011\u0011Z\u0001\fG>dG.Z2u\rJ|W.\u0006\u0003\u0002L\u0006eG\u0003BAg\u0003K$B!a4\u0002bR!\u0011\u0011[An!\u0015)\u00151[Al\u0013\r\t)n\u0014\u0002\u0007-\u0016\u001cGo\u001c:\u0011\t\u00055\u0011\u0011\u001c\u0003\t\u0003#\t)M1\u0001\u0002\u0014!Q\u0011Q\\Ac\u0003\u0003\u0005\u001d!a8\u0002\u0015\u00154\u0018\u000eZ3oG\u0016$#\u0007\u0005\u0004\u0002(\u00055\u0012q\u001b\u0005\t\u0003s\t)\r1\u0001\u0002dB91#!\u0010\u0002B\u0005]\u0007\u0002CAt\u0003\u000b\u0004\r!!\u0011\u0002\u0013I,7/\u001e7u'\u0016$\bbBAv\u001f\u0011\u0005\u0011Q^\u0001\u001cO\u0016$H)[:ue&\u0014W\u000f^3e)J\fgn]1di&|g.\u00133\u0015\u0007m\ny\u000f\u0003\u0004'\u0003S\u0004\ra\n")
public final class Jdbc
{
    public static void logDebug(final Function0<String> msg) {
        Jdbc$.MODULE$.logDebug(msg);
    }
    
    public static void logWarning(final Function0<String> msg) {
        Jdbc$.MODULE$.logWarning(msg);
    }
    
    public static Logger log() {
        return Jdbc$.MODULE$.log();
    }
    
    public static String getDistributedTransactionId(final Connection conn) {
        return Jdbc$.MODULE$.getDistributedTransactionId(conn);
    }
    
    public static StructType getSchema(final ResultSetMetaData meta) {
        return Jdbc$.MODULE$.getSchema(meta);
    }
    
    public static Seq<String> getColumnNames(final ResultSetMetaData meta) {
        return Jdbc$.MODULE$.getColumnNames(meta);
    }
    
    public static StructType resolveTable(final Connection conn, final String url, final GreenplumQualifiedName table) {
        return Jdbc$.MODULE$.resolveTable(conn, url, table);
    }
    
    public static ColumnValueRange computeColumnValueRange(final Connection conn, final GreenplumQualifiedName table, final String columnName) {
        return Jdbc$.MODULE$.computeColumnValueRange(conn, table, columnName);
    }
    
    public static int[] retrieveSegmentIds(final Connection conn) {
        return Jdbc$.MODULE$.retrieveSegmentIds(conn);
    }
    
    public static void dropExternalTables(final Connection conn, final String schema, final String externalTableNamePrefix) {
        Jdbc$.MODULE$.dropExternalTables(conn, schema, externalTableNamePrefix);
    }
    
    public static void createGpfdistWritableExternalTable(final Connection conn, final GreenplumQualifiedName srcTable, final GreenplumQualifiedName extTable, final GpfdistLocation gpfdistLocation, final Seq<String> columns) {
        Jdbc$.MODULE$.createGpfdistWritableExternalTable(conn, srcTable, extTable, gpfdistLocation, columns);
    }
    
    public static Seq<String> getColumnsMetadata(final Connection conn, final GreenplumQualifiedName table, final Seq<String> columnNames) {
        return Jdbc$.MODULE$.getColumnsMetadata(conn, table, columnNames);
    }
    
    public static boolean externalTableExists(final Connection conn, final GreenplumQualifiedName table) {
        return Jdbc$.MODULE$.externalTableExists(conn, table);
    }
    
    public static void copyTable(final Connection conn, final GreenplumQualifiedName srcTable, final GreenplumQualifiedName dstTable, final String predicate, final Seq<String> columns) {
        Jdbc$.MODULE$.copyTable(conn, srcTable, dstTable, predicate, columns);
    }
}
