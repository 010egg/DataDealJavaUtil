// 
// Decompiled by Procyon v0.5.36
// 

package io.pivotal.greenplum.spark.jdbc;

import scala.MatchError;
import scala.runtime.BoxedUnit;
import scala.None$;
import scala.Some;
import io.pivotal.greenplum.spark.conf.ConnectionPoolOptions;
import scala.Option;
import java.sql.Connection;
import io.pivotal.greenplum.spark.conf.GreenplumOptions;
import scala.Function0;
import io.pivotal.greenplum.spark.Logging$class;
import scala.runtime.TraitSetter;
import org.slf4j.Logger;
import javax.sql.DataSource;
import java.util.HashMap;
import scala.reflect.ScalaSignature;
import io.pivotal.greenplum.spark.Logging;

@ScalaSignature(bytes = "\u0006\u0001\u0005%r!B\u0001\u0003\u0011\u0003i\u0011!E\"p]:,7\r^5p]6\u000bg.Y4fe*\u00111\u0001B\u0001\u0005U\u0012\u00147M\u0003\u0002\u0006\r\u0005)1\u000f]1sW*\u0011q\u0001C\u0001\nOJ,WM\u001c9mk6T!!\u0003\u0006\u0002\u000fALgo\u001c;bY*\t1\"\u0001\u0002j_\u000e\u0001\u0001C\u0001\b\u0010\u001b\u0005\u0011a!\u0002\t\u0003\u0011\u0003\t\"!E\"p]:,7\r^5p]6\u000bg.Y4feN\u0011qB\u0005\t\u0003'Yi\u0011\u0001\u0006\u0006\u0002+\u0005)1oY1mC&\u0011q\u0003\u0006\u0002\u0007\u0003:L(+\u001a4\t\u000beyA\u0011\u0001\u000e\u0002\rqJg.\u001b;?)\u0005i\u0001\u0002\u0003\u000f\u0010\u0011\u000b\u0007I\u0011A\u000f\u0002#\r|gN\\3di&|g.T1oC\u001e,'/F\u0001\u001f!\tqqD\u0002\u0003\u0011\u0005\u0001\u00013cA\u0010\u0013CA\u0011!eI\u0007\u0002\t%\u0011A\u0005\u0002\u0002\b\u0019><w-\u001b8h\u0011!1sD!A!\u0002\u00139\u0013\u0001\u00039s_ZLG-\u001a:\u0011\u00059A\u0013BA\u0015\u0003\u0005I!\u0015\r^1T_V\u00148-\u001a)s_ZLG-\u001a:\t\u000beyB\u0011A\u0016\u0015\u0005ya\u0003\"\u0002\u0014+\u0001\u00049\u0003b\u0002\u0018 \u0005\u0004%IaL\u0001\u0006a>|Gn]\u000b\u0002aA!\u0011G\u000e\u001d<\u001b\u0005\u0011$BA\u001a5\u0003\u0011)H/\u001b7\u000b\u0003U\nAA[1wC&\u0011qG\r\u0002\b\u0011\u0006\u001c\b.T1q!\tq\u0011(\u0003\u0002;\u0005\ti1i\u001c8oK\u000e$\u0018n\u001c8LKf\u0004\"\u0001P!\u000e\u0003uR!AP \u0002\u0007M\fHNC\u0001A\u0003\u0015Q\u0017M^1y\u0013\t\u0011UH\u0001\u0006ECR\f7k\\;sG\u0016Da\u0001R\u0010!\u0002\u0013\u0001\u0014A\u00029p_2\u001c\b\u0005C\u0003G?\u0011\u0005q)A\u0007hKR\u001cuN\u001c8fGRLwN\u001c\u000b\u0004\u00116+\u0006CA%L\u001b\u0005Q%B\u0001 5\u0013\ta%J\u0001\u0006D_:tWm\u0019;j_:DQAT#A\u0002=\u000ba\u0001\u001a2PaR\u001c\bC\u0001)T\u001b\u0005\t&B\u0001*\u0005\u0003\u0011\u0019wN\u001c4\n\u0005Q\u000b&\u0001E$sK\u0016t\u0007\u000f\\;n\u001fB$\u0018n\u001c8t\u0011\u001d1V\t%AA\u0002]\u000b!\"Y;u_\u000e{W.\\5u!\t\u0019\u0002,\u0003\u0002Z)\t9!i\\8mK\u0006t\u0007BB. \t\u0003\u0011A,A\nhKR\u0004vn\u001c7fI\u000e{gN\\3di&|g\u000e\u0006\u0004I;\u001aDWn\u001c\u0005\u0006=j\u0003\raX\u0001\bU\u0012\u00147-\u0016:m!\t\u00017M\u0004\u0002\u0014C&\u0011!\rF\u0001\u0007!J,G-\u001a4\n\u0005\u0011,'AB*ue&twM\u0003\u0002c)!)qM\u0017a\u0001?\u0006AQo]3s\u001d\u0006lW\rC\u0003j5\u0002\u0007!.\u0001\u0005qCN\u001cxo\u001c:e!\r\u00192nX\u0005\u0003YR\u0011aa\u00149uS>t\u0007\"\u00028[\u0001\u0004y\u0016A\u00023sSZ,'\u000fC\u0004q5B\u0005\t\u0019A9\u0002\u000f=\u0004H/[8ogB\u0011\u0001K]\u0005\u0003gF\u0013QcQ8o]\u0016\u001cG/[8o!>|Gn\u00149uS>t7\u000f\u0003\u0004v?\u0011\u0005!A^\u0001\u0014O\u0016$\bk\\8mK\u0012$\u0015\r^1T_V\u00148-\u001a\u000b\u0007w]D\u0018P_>\t\u000by#\b\u0019A0\t\u000b\u001d$\b\u0019A0\t\u000b%$\b\u0019\u00016\t\u000b9$\b\u0019A0\t\u000bA$\b\u0019A9\t\u000fu|\u0012\u0013!C\u0001}\u00069r-\u001a;D_:tWm\u0019;j_:$C-\u001a4bk2$HEM\u000b\u0002\u007f*\u001aq+!\u0001,\u0005\u0005\r\u0001\u0003BA\u0003\u0003\u001fi!!a\u0002\u000b\t\u0005%\u00111B\u0001\nk:\u001c\u0007.Z2lK\u0012T1!!\u0004\u0015\u0003)\tgN\\8uCRLwN\\\u0005\u0005\u0003#\t9AA\tv]\u000eDWmY6fIZ\u000b'/[1oG\u0016D\u0011\"!\u0006 #\u0003%\t!a\u0006\u0002;\u001d,G\u000fU8pY\u0016$7i\u001c8oK\u000e$\u0018n\u001c8%I\u00164\u0017-\u001e7uIU*\"!!\u0007+\u0007E\f\t\u0001C\u0005\u0002\u001e=A\t\u0011)Q\u0005=\u0005\u00112m\u001c8oK\u000e$\u0018n\u001c8NC:\fw-\u001a:!\u0011\u00191u\u0002\"\u0001\u0002\"Q)\u0001*a\t\u0002&!1a*a\bA\u0002=C\u0001BVA\u0010!\u0003\u0005\ra\u0016\u0005\b{>\t\n\u0011\"\u0001\u007f\u0001")
public class ConnectionManager implements Logging
{
    private final DataSourceProvider provider;
    private final HashMap<ConnectionKey, DataSource> pools;
    private transient Logger io$pivotal$greenplum$spark$Logging$$log_;
    
    public static ConnectionManager connectionManager() {
        return ConnectionManager$.MODULE$.connectionManager();
    }
    
    @Override
    public Logger io$pivotal$greenplum$spark$Logging$$log_() {
        return this.io$pivotal$greenplum$spark$Logging$$log_;
    }
    
    @TraitSetter
    @Override
    public void io$pivotal$greenplum$spark$Logging$$log__$eq(final Logger x$1) {
        this.io$pivotal$greenplum$spark$Logging$$log_ = x$1;
    }
    
    @Override
    public Logger log() {
        return Logging$class.log(this);
    }
    
    @Override
    public void logWarning(final Function0<String> msg) {
        Logging$class.logWarning(this, msg);
    }
    
    @Override
    public void logDebug(final Function0<String> msg) {
        Logging$class.logDebug(this, msg);
    }
    
    private HashMap<ConnectionKey, DataSource> pools() {
        return this.pools;
    }
    
    public Connection getConnection(final GreenplumOptions dbOpts, final boolean autoCommit) {
        final Connection connection = this.getPooledConnection(dbOpts.url(), dbOpts.user(), dbOpts.password(), dbOpts.driver(), dbOpts.connectionPoolOptions());
        connection.setAutoCommit(autoCommit);
        return connection;
    }
    
    public boolean getConnection$default$2() {
        return true;
    }
    
    public Connection getPooledConnection(final String jdbcUrl, final String userName, final Option<String> password, final String driver, final ConnectionPoolOptions options) {
        return this.getPooledDataSource(jdbcUrl, userName, password, driver, options).getConnection();
    }
    
    public ConnectionPoolOptions getPooledConnection$default$5() {
        return new ConnectionPoolOptions();
    }
    
    public DataSource getPooledDataSource(final String jdbcUrl, final String userName, final Option<String> password, final String driver, final ConnectionPoolOptions options) {
        Label_0162: {
            ConnectionKey connectionKey2;
            if (password instanceof Some) {
                final String pass = (String)((Some)password).x();
                connectionKey2 = ConnectionKey$.MODULE$.valueOf(jdbcUrl, userName, pass);
            }
            else {
                if (!None$.MODULE$.equals(password)) {
                    break Label_0162;
                }
                connectionKey2 = ConnectionKey$.MODULE$.valueOf(jdbcUrl, userName);
            }
            final ConnectionKey connectionKey = connectionKey2;
            Label_0148: {
                if (this.pools().containsKey(connectionKey)) {
                    final BoxedUnit unit = BoxedUnit.UNIT;
                    break Label_0148;
                }
                synchronized (this.pools()) {
                    if (this.pools().containsKey(connectionKey)) {
                        final BoxedUnit unit2 = BoxedUnit.UNIT;
                    }
                    else {
                        final DataSource dataSource = this.provider.createDataSource(connectionKey, password, driver, options);
                        this.pools().put(connectionKey, dataSource);
                    }
                    // monitorexit(this.pools())
                    return this.pools().get(connectionKey);
                    throw new MatchError((Object)password);
                }
            }
        }
    }
    
    public ConnectionManager(final DataSourceProvider provider) {
        this.provider = provider;
        Logging$class.$init$(this);
        this.pools = new HashMap<ConnectionKey, DataSource>();
    }
}
