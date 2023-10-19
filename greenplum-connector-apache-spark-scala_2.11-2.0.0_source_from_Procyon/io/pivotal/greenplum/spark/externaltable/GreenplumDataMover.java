// 
// Decompiled by Procyon v0.5.36
// 

package io.pivotal.greenplum.spark.externaltable;

import scala.runtime.BoxedUnit;
import scala.MatchError;
import scala.util.Failure;
import scala.Function1;
import scala.util.Success;
import io.pivotal.greenplum.spark.GpfdistLocation;
import io.pivotal.greenplum.spark.ConnectorUtils$;
import scala.collection.Seq$;
import scala.collection.Seq;
import org.apache.spark.SparkEnv$;
import scala.util.Try;
import scala.Function0;
import io.pivotal.greenplum.spark.Logging$class;
import scala.runtime.TraitSetter;
import org.slf4j.Logger;
import io.pivotal.greenplum.spark.NetworkWrapper;
import io.pivotal.greenplum.spark.conf.GreenplumOptions;
import scala.reflect.ScalaSignature;
import io.pivotal.greenplum.spark.Logging;

@ScalaSignature(bytes = "\u0006\u0001\u001d4A!\u0001\u0002\u0001\u001b\t\u0011rI]3f]BdW/\u001c#bi\u0006luN^3s\u0015\t\u0019A!A\u0007fqR,'O\\1mi\u0006\u0014G.\u001a\u0006\u0003\u000b\u0019\tQa\u001d9be.T!a\u0002\u0005\u0002\u0013\u001d\u0014X-\u001a8qYVl'BA\u0005\u000b\u0003\u001d\u0001\u0018N^8uC2T\u0011aC\u0001\u0003S>\u001c\u0001aE\u0002\u0001\u001dQ\u0001\"a\u0004\n\u000e\u0003AQ\u0011!E\u0001\u0006g\u000e\fG.Y\u0005\u0003'A\u0011a!\u00118z%\u00164\u0007CA\u000b\u0017\u001b\u0005!\u0011BA\f\u0005\u0005\u001daunZ4j]\u001eD\u0001\"\u0007\u0001\u0003\u0002\u0003\u0006IAG\u0001\u000eCB\u0004H.[2bi&|g.\u00133\u0011\u0005mqbBA\b\u001d\u0013\ti\u0002#\u0001\u0004Qe\u0016$WMZ\u0005\u0003?\u0001\u0012aa\u0015;sS:<'BA\u000f\u0011\u0011!\u0011\u0003A!A!\u0002\u0013\u0019\u0013\u0001E4sK\u0016t\u0007\u000f\\;n\u001fB$\u0018n\u001c8t!\t!s%D\u0001&\u0015\t1C!\u0001\u0003d_:4\u0017B\u0001\u0015&\u0005A9%/Z3oa2,Xn\u00149uS>t7\u000f\u0003\u0005+\u0001\t\u0005\t\u0015!\u0003,\u00031!\u0018M\u00197f\u001b\u0006t\u0017mZ3s!\taS&D\u0001\u0003\u0013\tq#AA\u000bHe\u0016,g\u000e\u001d7v[R\u000b'\r\\3NC:\fw-\u001a:\t\u0011A\u0002!\u0011!Q\u0001\nE\nqa]3sm&\u001cW\r\u0005\u0002-e%\u00111G\u0001\u0002\u000f\u000fB4G-[:u'\u0016\u0014h/[2f\u0011!)\u0004A!A!\u0002\u00131\u0014a\u00028fi^|'o\u001b\t\u0003+]J!\u0001\u000f\u0003\u0003\u001d9+Go^8sW^\u0013\u0018\r\u001d9fe\")!\b\u0001C\u0001w\u00051A(\u001b8jiz\"b\u0001P\u001f?\u007f\u0001\u000b\u0005C\u0001\u0017\u0001\u0011\u0015I\u0012\b1\u0001\u001b\u0011\u0015\u0011\u0013\b1\u0001$\u0011\u0015Q\u0013\b1\u0001,\u0011\u0015\u0001\u0014\b1\u00012\u0011\u001d)\u0014\b%AA\u0002YBQa\u0011\u0001\u0005\u0002\u0011\u000b\u0001\"\\8wK\u0012\u000bG/\u0019\u000b\u0003\u000b:\u00032AR%L\u001b\u00059%B\u0001%\u0011\u0003\u0011)H/\u001b7\n\u0005);%a\u0001+ssB\u0011q\u0002T\u0005\u0003\u001bB\u00111!\u00138u\u0011\u0015y%\t1\u0001Q\u00035\u0001\u0018M\u001d;ji&|g\u000eR1uCB\u0011A&U\u0005\u0003%\n\u0011Q\u0002U1si&$\u0018n\u001c8ECR\fwa\u0002+\u0003\u0003\u0003E\t!V\u0001\u0013\u000fJ,WM\u001c9mk6$\u0015\r^1N_Z,'\u000f\u0005\u0002--\u001a9\u0011AAA\u0001\u0012\u000396C\u0001,\u000f\u0011\u0015Qd\u000b\"\u0001Z)\u0005)\u0006bB.W#\u0003%\t\u0001X\u0001\u001cI1,7o]5oSR$sM]3bi\u0016\u0014H\u0005Z3gCVdG\u000fJ\u001b\u0016\u0003uS#A\u000e0,\u0003}\u0003\"\u0001Y3\u000e\u0003\u0005T!AY2\u0002\u0013Ut7\r[3dW\u0016$'B\u00013\u0011\u0003)\tgN\\8uCRLwN\\\u0005\u0003M\u0006\u0014\u0011#\u001e8dQ\u0016\u001c7.\u001a3WCJL\u0017M\\2f\u0001")
public class GreenplumDataMover implements Logging
{
    private final String applicationId;
    private final GreenplumOptions greenplumOptions;
    public final GreenplumTableManager io$pivotal$greenplum$spark$externaltable$GreenplumDataMover$$tableManager;
    private final GpfdistService service;
    private final NetworkWrapper network;
    private transient Logger io$pivotal$greenplum$spark$Logging$$log_;
    
    public static NetworkWrapper $lessinit$greater$default$5() {
        return GreenplumDataMover$.MODULE$.$lessinit$greater$default$5();
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
    
    public Try<Object> moveData(final PartitionData partitionData) {
        final String executorId = SparkEnv$.MODULE$.get().executorId();
        final long threadId = Thread.currentThread().getId();
        final GreenplumQualifiedName targetTable = new GreenplumQualifiedName(this.greenplumOptions.dbSchema(), this.greenplumOptions.dbTable());
        final String externalTableName = GreenplumTableManager$.MODULE$.generateExternalTableName(this.applicationId, targetTable.name(), executorId, threadId, (Seq<String>)Seq$.MODULE$.empty());
        final GreenplumQualifiedName sourceTable = new GreenplumQualifiedName(this.greenplumOptions.dbSchema(), externalTableName);
        final String serverAddress = ConnectorUtils$.MODULE$.getServerAddress(this.greenplumOptions.connectorOptions(), this.network);
        final int port = this.service.getPort();
        final Try<BoxedUnit> readableExternalTableIfNotExists;
        final Try result = readableExternalTableIfNotExists = this.io$pivotal$greenplum$spark$externaltable$GreenplumDataMover$$tableManager.createReadableExternalTableIfNotExists(targetTable, sourceTable, new GpfdistLocation(serverAddress, port));
        Object o;
        if (readableExternalTableIfNotExists instanceof Success) {
            final Try rowsCopied = this.service.setPartitionDataFor(sourceTable.name(), partitionData).flatMap((Function1)new GreenplumDataMover$$anonfun.GreenplumDataMover$$anonfun$1(this, targetTable, sourceTable));
            this.service.removePartitionDataFor(sourceTable.name());
            o = rowsCopied;
        }
        else {
            if (!(readableExternalTableIfNotExists instanceof Failure)) {
                throw new MatchError((Object)readableExternalTableIfNotExists);
            }
            final Throwable exception = ((Failure)readableExternalTableIfNotExists).exception();
            o = new Failure(exception);
        }
        return (Try<Object>)o;
    }
    
    public GreenplumDataMover(final String applicationId, final GreenplumOptions greenplumOptions, final GreenplumTableManager tableManager, final GpfdistService service, final NetworkWrapper network) {
        this.applicationId = applicationId;
        this.greenplumOptions = greenplumOptions;
        this.io$pivotal$greenplum$spark$externaltable$GreenplumDataMover$$tableManager = tableManager;
        this.service = service;
        this.network = network;
        Logging$class.$init$(this);
    }
}
