// 
// Decompiled by Procyon v0.5.36
// 

package io.pivotal.greenplum.spark.externaltable;

import scala.collection.Seq;
import scala.StringContext;
import scala.Predef$;
import org.apache.spark.scheduler.SparkListenerApplicationEnd;
import scala.Function0;
import io.pivotal.greenplum.spark.Logging$class;
import scala.runtime.TraitSetter;
import org.slf4j.Logger;
import scala.reflect.ScalaSignature;
import io.pivotal.greenplum.spark.Logging;
import org.apache.spark.scheduler.SparkListener;

@ScalaSignature(bytes = "\u0006\u0001Y2A!\u0001\u0002\u0001\u001b\ty2\u000b^8q\u000fB4G-[:u'\u0016\u0014h/[2f'B\f'o\u001b'jgR,g.\u001a:\u000b\u0005\r!\u0011!D3yi\u0016\u0014h.\u00197uC\ndWM\u0003\u0002\u0006\r\u0005)1\u000f]1sW*\u0011q\u0001C\u0001\nOJ,WM\u001c9mk6T!!\u0003\u0006\u0002\u000fALgo\u001c;bY*\t1\"\u0001\u0002j_\u000e\u00011c\u0001\u0001\u000f3A\u0011qbF\u0007\u0002!)\u0011\u0011CE\u0001\ng\u000eDW\rZ;mKJT!!B\n\u000b\u0005Q)\u0012AB1qC\u000eDWMC\u0001\u0017\u0003\ry'oZ\u0005\u00031A\u0011Qb\u00159be.d\u0015n\u001d;f]\u0016\u0014\bC\u0001\u000e\u001c\u001b\u0005!\u0011B\u0001\u000f\u0005\u0005\u001daunZ4j]\u001eD\u0001B\b\u0001\u0003\u0002\u0003\u0006IaH\u0001\u0004W\u0016L\bC\u0001\u0011\"\u001b\u0005\u0011\u0011B\u0001\u0012\u0003\u0005)\u0019VM\u001d<jG\u0016\\U-\u001f\u0005\u0006I\u0001!\t!J\u0001\u0007y%t\u0017\u000e\u001e \u0015\u0005\u0019:\u0003C\u0001\u0011\u0001\u0011\u0015q2\u00051\u0001 \u0011\u0015I\u0003\u0001\"\u0011+\u0003Ayg.\u00119qY&\u001c\u0017\r^5p]\u0016sG\r\u0006\u0002,cA\u0011AfL\u0007\u0002[)\ta&A\u0003tG\u0006d\u0017-\u0003\u00021[\t!QK\\5u\u0011\u0015\u0011\u0004\u00061\u00014\u00039\t\u0007\u000f\u001d7jG\u0006$\u0018n\u001c8F]\u0012\u0004\"a\u0004\u001b\n\u0005U\u0002\"aG*qCJ\\G*[:uK:,'/\u00119qY&\u001c\u0017\r^5p]\u0016sG\r")
public class StopGpfdistServiceSparkListener extends SparkListener implements Logging
{
    private final ServiceKey key;
    private transient Logger io$pivotal$greenplum$spark$Logging$$log_;
    
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
    
    public void onApplicationEnd(final SparkListenerApplicationEnd applicationEnd) {
        this.log().info(new StringContext((Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[] { "Stopping GpfdistService for ", "" })).s((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { this.key })));
        GpfdistServiceManager$.MODULE$.stopAndRemove(this.key);
    }
    
    public StopGpfdistServiceSparkListener(final ServiceKey key) {
        this.key = key;
        Logging$class.$init$(this);
    }
}
