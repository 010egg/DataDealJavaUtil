// 
// Decompiled by Procyon v0.5.36
// 

package io.pivotal.greenplum.spark.externaltable;

import scala.collection.Iterator;
import scala.Function2;
import scala.Function0;
import io.pivotal.greenplum.spark.Logging$class;
import scala.runtime.TraitSetter;
import io.pivotal.greenplum.spark.SqlExecutor;
import io.pivotal.greenplum.spark.jdbc.ConnectionManager$;
import scala.runtime.BoxedUnit;
import org.slf4j.Logger;
import org.apache.spark.sql.Row;
import scala.Function1;
import io.pivotal.greenplum.spark.conf.GreenplumOptions;
import scala.reflect.ScalaSignature;
import io.pivotal.greenplum.spark.Logging;
import scala.Serializable;

@ScalaSignature(bytes = "\u0006\u0001)4A!\u0001\u0002\u0001\u001b\ty\u0001+\u0019:uSRLwN\\,sSR,'O\u0003\u0002\u0004\t\u0005iQ\r\u001f;fe:\fG\u000e^1cY\u0016T!!\u0002\u0004\u0002\u000bM\u0004\u0018M]6\u000b\u0005\u001dA\u0011!C4sK\u0016t\u0007\u000f\\;n\u0015\tI!\"A\u0004qSZ|G/\u00197\u000b\u0003-\t!![8\u0004\u0001M!\u0001A\u0004\u000b\u0018!\ty!#D\u0001\u0011\u0015\u0005\t\u0012!B:dC2\f\u0017BA\n\u0011\u0005\u0019\te.\u001f*fMB\u0011q\"F\u0005\u0003-A\u0011AbU3sS\u0006d\u0017N_1cY\u0016\u0004\"\u0001G\r\u000e\u0003\u0011I!A\u0007\u0003\u0003\u000f1{wmZ5oO\"AA\u0004\u0001B\u0001B\u0003%Q$A\u0007baBd\u0017nY1uS>t\u0017\n\u001a\t\u0003=\u0005r!aD\u0010\n\u0005\u0001\u0002\u0012A\u0002)sK\u0012,g-\u0003\u0002#G\t11\u000b\u001e:j]\u001eT!\u0001\t\t\t\u0011\u0015\u0002!\u0011!Q\u0001\n\u0019\n\u0001c\u001a:fK:\u0004H.^7PaRLwN\\:\u0011\u0005\u001dRS\"\u0001\u0015\u000b\u0005%\"\u0011\u0001B2p]\u001aL!a\u000b\u0015\u0003!\u001d\u0013X-\u001a8qYVlw\n\u001d;j_:\u001c\b\u0002C\u0017\u0001\u0005\u0003\u0005\u000b\u0011\u0002\u0018\u0002\u001dI|w\u000f\u0016:b]N4wN]7feB!qbL\u00192\u0013\t\u0001\u0004CA\u0005Gk:\u001cG/[8ocA\u0011!GO\u0007\u0002g)\u0011A'N\u0001\u0004gFd'BA\u00037\u0015\t9\u0004(\u0001\u0004ba\u0006\u001c\u0007.\u001a\u0006\u0002s\u0005\u0019qN]4\n\u0005m\u001a$a\u0001*po\")Q\b\u0001C\u0001}\u00051A(\u001b8jiz\"BaP!C\u0007B\u0011\u0001\tA\u0007\u0002\u0005!)A\u0004\u0010a\u0001;!)Q\u0005\u0010a\u0001M!)Q\u0006\u0010a\u0001]!AQ\t\u0001EC\u0002\u0013\u0005a)\u0001\bha\u001a$\u0017n\u001d;TKJ4\u0018nY3\u0016\u0003\u001d\u0003\"\u0001\u0011%\n\u0005%\u0013!AD$qM\u0012L7\u000f^*feZL7-\u001a\u0005\t\u0017\u0002A\t\u0011)Q\u0005\u000f\u0006yq\r\u001d4eSN$8+\u001a:wS\u000e,\u0007\u0005\u0003\u0005N\u0001!\u0015\r\u0011\"\u0001O\u0003I9'/Z3oa2,X\u000eR1uC6{g/\u001a:\u0016\u0003=\u0003\"\u0001\u0011)\n\u0005E\u0013!AE$sK\u0016t\u0007\u000f\\;n\t\u0006$\u0018-T8wKJD\u0001b\u0015\u0001\t\u0002\u0003\u0006KaT\u0001\u0014OJ,WM\u001c9mk6$\u0015\r^1N_Z,'\u000f\t\u0005\u0006+\u0002!\tAV\u0001\u000bO\u0016$8\t\\8tkJ,W#A,\u0011\u000b=A&,X5\n\u0005e\u0003\"!\u0003$v]\u000e$\u0018n\u001c83!\ty1,\u0003\u0002]!\t\u0019\u0011J\u001c;\u0011\u0007y3\u0017G\u0004\u0002`I:\u0011\u0001mY\u0007\u0002C*\u0011!\rD\u0001\u0007yI|w\u000e\u001e \n\u0003EI!!\u001a\t\u0002\u000fA\f7m[1hK&\u0011q\r\u001b\u0002\t\u0013R,'/\u0019;pe*\u0011Q\r\u0005\t\u0004=\u001aT\u0006")
public class PartitionWriter implements Serializable, Logging
{
    private final String applicationId;
    public final GreenplumOptions io$pivotal$greenplum$spark$externaltable$PartitionWriter$$greenplumOptions;
    public final Function1<Row, Row> io$pivotal$greenplum$spark$externaltable$PartitionWriter$$rowTransformer;
    private GpfdistService gpfdistService;
    private GreenplumDataMover greenplumDataMover;
    private transient Logger io$pivotal$greenplum$spark$Logging$$log_;
    private volatile byte bitmap$0;
    
    private GpfdistService gpfdistService$lzycompute() {
        synchronized (this) {
            if ((byte)(this.bitmap$0 & 0x1) == 0) {
                this.gpfdistService = GpfdistServiceManager$.MODULE$.getService(this.io$pivotal$greenplum$spark$externaltable$PartitionWriter$$greenplumOptions.connectorOptions());
                this.bitmap$0 |= 0x1;
            }
            final BoxedUnit unit = BoxedUnit.UNIT;
            return this.gpfdistService;
        }
    }
    
    private GreenplumDataMover greenplumDataMover$lzycompute() {
        synchronized (this) {
            if ((byte)(this.bitmap$0 & 0x2) == 0) {
                final SqlExecutor sqlExecutor = new SqlExecutor(ConnectionManager$.MODULE$.connectionManager(), this.io$pivotal$greenplum$spark$externaltable$PartitionWriter$$greenplumOptions);
                final GreenplumTableManager tableManager = new GreenplumTableManager(sqlExecutor);
                this.greenplumDataMover = new GreenplumDataMover(this.applicationId, this.io$pivotal$greenplum$spark$externaltable$PartitionWriter$$greenplumOptions, tableManager, this.gpfdistService(), GreenplumDataMover$.MODULE$.$lessinit$greater$default$5());
                this.bitmap$0 |= 0x2;
            }
            final BoxedUnit unit = BoxedUnit.UNIT;
            // monitorexit(this)
            this.applicationId = null;
            return this.greenplumDataMover;
        }
    }
    
    public Logger io$pivotal$greenplum$spark$Logging$$log_() {
        return this.io$pivotal$greenplum$spark$Logging$$log_;
    }
    
    @TraitSetter
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
    
    public GpfdistService gpfdistService() {
        return ((byte)(this.bitmap$0 & 0x1) == 0) ? this.gpfdistService$lzycompute() : this.gpfdistService;
    }
    
    public GreenplumDataMover greenplumDataMover() {
        return ((byte)(this.bitmap$0 & 0x2) == 0) ? this.greenplumDataMover$lzycompute() : this.greenplumDataMover;
    }
    
    public Function2<Object, Iterator<Row>, Iterator<Object>> getClosure() {
        return (Function2<Object, Iterator<Row>, Iterator<Object>>)new PartitionWriter$$anonfun$getClosure.PartitionWriter$$anonfun$getClosure$1(this);
    }
    
    public PartitionWriter(final String applicationId, final GreenplumOptions greenplumOptions, final Function1<Row, Row> rowTransformer) {
        this.applicationId = applicationId;
        this.io$pivotal$greenplum$spark$externaltable$PartitionWriter$$greenplumOptions = greenplumOptions;
        this.io$pivotal$greenplum$spark$externaltable$PartitionWriter$$rowTransformer = rowTransformer;
        Logging$class.$init$(this);
    }
}
