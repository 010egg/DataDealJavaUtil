// 
// Decompiled by Procyon v0.5.36
// 

package io.pivotal.greenplum.spark;

import java.sql.ResultSet;
import resource.ManagedResource;
import scala.Function1;
import scala.reflect.OptManifest;
import resource.Resource;
import java.sql.Connection;
import scala.reflect.ClassManifestFactory$;
import resource.Resource$;
import resource.package$;
import scala.collection.Seq;
import scala.StringContext;
import scala.Predef$;
import scala.util.Try;
import scala.Function0;
import scala.runtime.TraitSetter;
import org.slf4j.Logger;
import io.pivotal.greenplum.spark.conf.GreenplumOptions;
import io.pivotal.greenplum.spark.jdbc.ConnectionManager;
import scala.reflect.ScalaSignature;

@ScalaSignature(bytes = "\u0006\u0001\u0005\u001db\u0001B\u0001\u0003\u0001-\u00111bU9m\u000bb,7-\u001e;pe*\u00111\u0001B\u0001\u0006gB\f'o\u001b\u0006\u0003\u000b\u0019\t\u0011b\u001a:fK:\u0004H.^7\u000b\u0005\u001dA\u0011a\u00029jm>$\u0018\r\u001c\u0006\u0002\u0013\u0005\u0011\u0011n\\\u0002\u0001'\r\u0001AB\u0005\t\u0003\u001bAi\u0011A\u0004\u0006\u0002\u001f\u0005)1oY1mC&\u0011\u0011C\u0004\u0002\u0007\u0003:L(+\u001a4\u0011\u0005M!R\"\u0001\u0002\n\u0005U\u0011!a\u0002'pO\u001eLgn\u001a\u0005\t/\u0001\u0011\t\u0011)A\u00051\u0005\t2m\u001c8oK\u000e$\u0018n\u001c8NC:\fw-\u001a:\u0011\u0005eaR\"\u0001\u000e\u000b\u0005m\u0011\u0011\u0001\u00026eE\u000eL!!\b\u000e\u0003#\r{gN\\3di&|g.T1oC\u001e,'\u000f\u0003\u0005 \u0001\t\u0005\t\u0015!\u0003!\u0003A9'/Z3oa2,Xn\u00149uS>t7\u000f\u0005\u0002\"I5\t!E\u0003\u0002$\u0005\u0005!1m\u001c8g\u0013\t)#E\u0001\tHe\u0016,g\u000e\u001d7v[>\u0003H/[8og\")q\u0005\u0001C\u0001Q\u00051A(\u001b8jiz\"2!\u000b\u0016,!\t\u0019\u0002\u0001C\u0003\u0018M\u0001\u0007\u0001\u0004C\u0003 M\u0001\u0007\u0001\u0005C\u0003.\u0001\u0011\u0005a&A\u0004fq\u0016\u001cW\u000f^3\u0015\u0005=B\u0004c\u0001\u00194k5\t\u0011G\u0003\u00023\u001d\u0005!Q\u000f^5m\u0013\t!\u0014GA\u0002Uef\u0004\"!\u0004\u001c\n\u0005]r!a\u0002\"p_2,\u0017M\u001c\u0005\u0006s1\u0002\rAO\u0001\u0004gFd\u0007CA\u001e?\u001d\tiA(\u0003\u0002>\u001d\u00051\u0001K]3eK\u001aL!a\u0010!\u0003\rM#(/\u001b8h\u0015\tid\u0002C\u0003C\u0001\u0011\u00051)\u0001\u0007fq\u0016\u001cW\u000f^3Rk\u0016\u0014\u00180\u0006\u0002E\u0011R\u0019Q)\u0015*\u0011\u0007A\u001ad\t\u0005\u0002H\u00112\u0001A!B%B\u0005\u0004Q%aA(viF\u00111J\u0014\t\u0003\u001b1K!!\u0014\b\u0003\u000f9{G\u000f[5oOB\u0011QbT\u0005\u0003!:\u00111!\u00118z\u0011\u0015I\u0014\t1\u0001;\u0011\u0015\u0019\u0016\t1\u0001U\u0003-!(/\u00198tM>\u0014X.\u001a:\u0011\t5)vKR\u0005\u0003-:\u0011\u0011BR;oGRLwN\\\u0019\u0011\u0005acV\"A-\u000b\u0005eR&\"A.\u0002\t)\fg/Y\u0005\u0003;f\u0013\u0011BU3tk2$8+\u001a;\t\u000b}\u0003A\u0011\u00011\u0002\u001b\u0015DXmY;uKV\u0003H-\u0019;f)\t\tW\rE\u00021g\t\u0004\"!D2\n\u0005\u0011t!aA%oi\")\u0011H\u0018a\u0001u!)!\t\u0001C\u0001OV\u0011\u0001n\u001b\u000b\u0005S2l'\u000fE\u00021g)\u0004\"aR6\u0005\u000b%3'\u0019\u0001&\t\u000be2\u0007\u0019\u0001\u001e\t\u000b94\u0007\u0019A8\u0002\t\u0005\u0014xm\u001d\t\u0004\u001bAt\u0015BA9\u000f\u0005\u0015\t%O]1z\u0011\u0015\u0019f\r1\u0001t!\u0011iQk\u00166\t\u000bU\u0004A\u0011\u0002<\u0002\u001dQ\u0014\u0018P\u0012:p[6\u000bg.Y4fIV\u0011qO\u001f\u000b\u0003qr\u00042\u0001M\u001az!\t9%\u0010B\u0003|i\n\u0007!JA\u0001U\u0011\u0015iH\u000f1\u0001\u007f\u0003=i\u0017M\\1hK\u0012\u0014Vm]8ve\u000e,\u0007\u0003B@\u0002\u0006el!!!\u0001\u000b\u0005\u0005\r\u0011\u0001\u0003:fg>,(oY3\n\t\u0005\u001d\u0011\u0011\u0001\u0002\u0010\u001b\u0006t\u0017mZ3e%\u0016\u001cx.\u001e:dK\"9\u00111\u0002\u0001\u0005\n\u00055\u0011!\u00048p_B\u001cEn\\:fC\ndW-\u0006\u0002\u0002\u0010I1\u0011\u0011CA\u000b\u0003C1q!a\u0005\u0002\n\u0001\tyA\u0001\u0007=e\u00164\u0017N\\3nK:$h\b\u0005\u0003\u0002\u0018\u0005uQBAA\r\u0015\r\tYBW\u0001\u0005Y\u0006tw-\u0003\u0003\u0002 \u0005e!AB(cU\u0016\u001cG\u000f\u0005\u0003\u0002\u0018\u0005\r\u0012\u0002BA\u0013\u00033\u0011Q\"Q;u_\u000ecwn]3bE2,\u0007")
public class SqlExecutor implements Logging
{
    public final ConnectionManager io$pivotal$greenplum$spark$SqlExecutor$$connectionManager;
    public final GreenplumOptions io$pivotal$greenplum$spark$SqlExecutor$$greenplumOptions;
    private transient Logger io$pivotal$greenplum$spark$Logging$$log_;
    
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
    
    public Try<Object> execute(final String sql) {
        this.log().debug(new StringContext((Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[] { "executing query: ", "" })).s((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { sql })));
        return this.tryFromManaged((ManagedResource<Object>)package$.MODULE$.managed((scala.Function0<Object>)new SqlExecutor$$anonfun$execute.SqlExecutor$$anonfun$execute$1(this), Resource$.MODULE$.connectionResource(), (scala.reflect.OptManifest<Object>)ClassManifestFactory$.MODULE$.classType((Class)Connection.class)).flatMap((scala.Function1<Object, ManagedResource<T>>)new SqlExecutor$$anonfun$execute.SqlExecutor$$anonfun$execute$2(this, sql)));
    }
    
    public <Out> Try<Out> executeQuery(final String sql, final Function1<ResultSet, Out> transformer) {
        this.log().debug(new StringContext((Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[] { "executing query: ", "" })).s((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { sql })));
        return this.tryFromManaged((ManagedResource<Out>)package$.MODULE$.managed((scala.Function0<Object>)new SqlExecutor$$anonfun$executeQuery.SqlExecutor$$anonfun$executeQuery$1(this), Resource$.MODULE$.connectionResource(), (scala.reflect.OptManifest<Object>)ClassManifestFactory$.MODULE$.classType((Class)Connection.class)).flatMap((scala.Function1<Object, ManagedResource<T>>)new SqlExecutor$$anonfun$executeQuery.SqlExecutor$$anonfun$executeQuery$2(this, sql, (Function1)transformer)));
    }
    
    public Try<Object> executeUpdate(final String sql) {
        this.log().debug(new StringContext((Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[] { "executing query: ", "" })).s((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { sql })));
        return this.tryFromManaged((ManagedResource<Object>)package$.MODULE$.managed((scala.Function0<Object>)new SqlExecutor$$anonfun$executeUpdate.SqlExecutor$$anonfun$executeUpdate$1(this), Resource$.MODULE$.connectionResource(), (scala.reflect.OptManifest<Object>)ClassManifestFactory$.MODULE$.classType((Class)Connection.class)).flatMap((scala.Function1<Object, ManagedResource<T>>)new SqlExecutor$$anonfun$executeUpdate.SqlExecutor$$anonfun$executeUpdate$2(this, sql)));
    }
    
    public <Out> Try<Out> executeQuery(final String sql, final Object[] args, final Function1<ResultSet, Out> transformer) {
        this.log().debug(new StringContext((Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[] { "executing query: ", "" })).s((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { sql })));
        return Predef$.MODULE$.genericArrayOps((Object)args).isEmpty() ? this.executeQuery(sql, transformer) : this.tryFromManaged((ManagedResource<Out>)package$.MODULE$.managed((scala.Function0<Object>)new SqlExecutor$$anonfun$executeQuery.SqlExecutor$$anonfun$executeQuery$3(this), Resource$.MODULE$.connectionResource(), (scala.reflect.OptManifest<Object>)ClassManifestFactory$.MODULE$.classType((Class)Connection.class)).flatMap((scala.Function1<Object, ManagedResource<T>>)new SqlExecutor$$anonfun$executeQuery.SqlExecutor$$anonfun$executeQuery$4(this, sql, args, (Function1)transformer)));
    }
    
    private <T> Try<T> tryFromManaged(final ManagedResource<T> managedResource) {
        return managedResource.map((scala.Function1<T, T>)new SqlExecutor$$anonfun$tryFromManaged.SqlExecutor$$anonfun$tryFromManaged$1(this)).tried();
    }
    
    public AutoCloseable io$pivotal$greenplum$spark$SqlExecutor$$noopCloseable() {
        return (AutoCloseable)new SqlExecutor$$anon.SqlExecutor$$anon$1(this);
    }
    
    public SqlExecutor(final ConnectionManager connectionManager, final GreenplumOptions greenplumOptions) {
        this.io$pivotal$greenplum$spark$SqlExecutor$$connectionManager = connectionManager;
        this.io$pivotal$greenplum$spark$SqlExecutor$$greenplumOptions = greenplumOptions;
        Logging$class.$init$(this);
    }
}
