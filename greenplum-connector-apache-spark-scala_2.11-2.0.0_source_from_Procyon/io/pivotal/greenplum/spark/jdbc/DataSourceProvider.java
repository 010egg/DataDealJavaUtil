// 
// Decompiled by Procyon v0.5.36
// 

package io.pivotal.greenplum.spark.jdbc;

import javax.sql.DataSource;
import io.pivotal.greenplum.spark.conf.ConnectionPoolOptions;
import scala.Option;
import scala.reflect.ScalaSignature;

@ScalaSignature(bytes = "\u0006\u0001m2q!\u0001\u0002\u0011\u0002G\u0005QB\u0001\nECR\f7k\\;sG\u0016\u0004&o\u001c<jI\u0016\u0014(BA\u0002\u0005\u0003\u0011QGMY2\u000b\u0005\u00151\u0011!B:qCJ\\'BA\u0004\t\u0003%9'/Z3oa2,XN\u0003\u0002\n\u0015\u00059\u0001/\u001b<pi\u0006d'\"A\u0006\u0002\u0005%|7\u0001A\n\u0003\u00019\u0001\"a\u0004\n\u000e\u0003AQ\u0011!E\u0001\u0006g\u000e\fG.Y\u0005\u0003'A\u0011a!\u00118z%\u00164\u0007\"B\u000b\u0001\r\u00031\u0012\u0001E2sK\u0006$X\rR1uCN{WO]2f)\u00159r$J\u00194!\tAR$D\u0001\u001a\u0015\tQ2$A\u0002tc2T\u0011\u0001H\u0001\u0006U\u00064\u0018\r_\u0005\u0003=e\u0011!\u0002R1uCN{WO]2f\u0011\u0015\u0001C\u00031\u0001\"\u0003\rYW-\u001f\t\u0003E\rj\u0011AA\u0005\u0003I\t\u0011QbQ8o]\u0016\u001cG/[8o\u0017\u0016L\b\"\u0002\u0014\u0015\u0001\u00049\u0013\u0001\u00039bgN<xN\u001d3\u0011\u0007=A#&\u0003\u0002*!\t1q\n\u001d;j_:\u0004\"a\u000b\u0018\u000f\u0005=a\u0013BA\u0017\u0011\u0003\u0019\u0001&/\u001a3fM&\u0011q\u0006\r\u0002\u0007'R\u0014\u0018N\\4\u000b\u00055\u0002\u0002\"\u0002\u001a\u0015\u0001\u0004Q\u0013A\u00023sSZ,'\u000fC\u00035)\u0001\u0007Q'A\u0004paRLwN\\:\u0011\u0005YJT\"A\u001c\u000b\u0005a\"\u0011\u0001B2p]\u001aL!AO\u001c\u0003+\r{gN\\3di&|g\u000eU8pY>\u0003H/[8og\u0002")
public interface DataSourceProvider
{
    DataSource createDataSource(final ConnectionKey p0, final Option<String> p1, final String p2, final ConnectionPoolOptions p3);
}
