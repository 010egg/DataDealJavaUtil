// 
// Decompiled by Procyon v0.5.36
// 

package io.pivotal.greenplum.spark.jdbc;

import javax.sql.DataSource;
import io.pivotal.greenplum.spark.conf.ConnectionPoolOptions;
import scala.Option;
import org.slf4j.Logger;
import scala.Function0;
import scala.reflect.ScalaSignature;

@ScalaSignature(bytes = "\u0006\u0001!;Q!\u0001\u0002\t\u00025\ta\u0002S5lCJL\u0007K]8wS\u0012,'O\u0003\u0002\u0004\t\u0005!!\u000e\u001a2d\u0015\t)a!A\u0003ta\u0006\u00148N\u0003\u0002\b\u0011\u0005IqM]3f]BdW/\u001c\u0006\u0003\u0013)\tq\u0001]5w_R\fGNC\u0001\f\u0003\tIwn\u0001\u0001\u0011\u00059yQ\"\u0001\u0002\u0007\u000bA\u0011\u0001\u0012A\t\u0003\u001d!K7.\u0019:j!J|g/\u001b3feN!qB\u0005\r\u001c!\t\u0019b#D\u0001\u0015\u0015\u0005)\u0012!B:dC2\f\u0017BA\f\u0015\u0005\u0019\te.\u001f*fMB\u0011a\"G\u0005\u00035\t\u0011!\u0003R1uCN{WO]2f!J|g/\u001b3feB\u0011A$H\u0007\u0002\t%\u0011a\u0004\u0002\u0002\b\u0019><w-\u001b8h\u0011\u0015\u0001s\u0002\"\u0001\"\u0003\u0019a\u0014N\\5u}Q\tQ\u0002C\u0003$\u001f\u0011\u0005C%\u0001\tde\u0016\fG/\u001a#bi\u0006\u001cv.\u001e:dKR)Q%\f\u001a?\u0001B\u0011aeK\u0007\u0002O)\u0011\u0001&K\u0001\u0004gFd'\"\u0001\u0016\u0002\u000b)\fg/\u0019=\n\u00051:#A\u0003#bi\u0006\u001cv.\u001e:dK\")aF\ta\u0001_\u0005\u00191.Z=\u0011\u00059\u0001\u0014BA\u0019\u0003\u00055\u0019uN\u001c8fGRLwN\\&fs\")1G\ta\u0001i\u0005A\u0001/Y:to>\u0014H\rE\u0002\u0014k]J!A\u000e\u000b\u0003\r=\u0003H/[8o!\tA4H\u0004\u0002\u0014s%\u0011!\bF\u0001\u0007!J,G-\u001a4\n\u0005qj$AB*ue&twM\u0003\u0002;)!)qH\ta\u0001o\u00051AM]5wKJDQ!\u0011\u0012A\u0002\t\u000bqa\u001c9uS>t7\u000f\u0005\u0002D\r6\tAI\u0003\u0002F\t\u0005!1m\u001c8g\u0013\t9EIA\u000bD_:tWm\u0019;j_:\u0004vn\u001c7PaRLwN\\:")
public final class HikariProvider
{
    public static void logDebug(final Function0<String> msg) {
        HikariProvider$.MODULE$.logDebug(msg);
    }
    
    public static void logWarning(final Function0<String> msg) {
        HikariProvider$.MODULE$.logWarning(msg);
    }
    
    public static Logger log() {
        return HikariProvider$.MODULE$.log();
    }
    
    public static DataSource createDataSource(final ConnectionKey key, final Option<String> password, final String driver, final ConnectionPoolOptions options) {
        return HikariProvider$.MODULE$.createDataSource(key, password, driver, options);
    }
}
