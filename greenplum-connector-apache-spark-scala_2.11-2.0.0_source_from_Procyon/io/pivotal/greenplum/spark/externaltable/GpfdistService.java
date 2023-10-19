// 
// Decompiled by Procyon v0.5.36
// 

package io.pivotal.greenplum.spark.externaltable;

import io.pivotal.greenplum.spark.conf.ConnectorOptions$;
import scala.None$;
import scala.runtime.BoxedUnit;
import scala.Some;
import scala.Option$;
import scala.Option;
import scala.MatchError;
import scala.util.Failure;
import scala.util.Success;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import scala.collection.immutable.StringOps;
import scala.Enumeration;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.ServerConnector;
import scala.collection.immutable.Nil$;
import scala.collection.Iterator;
import scala.collection.Seq;
import scala.StringContext;
import scala.Predef$;
import scala.runtime.BoxesRunTime;
import scala.Function0;
import io.pivotal.greenplum.spark.Logging$class;
import scala.runtime.TraitSetter;
import org.slf4j.Logger;
import java.util.concurrent.atomic.AtomicBoolean;
import io.pivotal.greenplum.spark.util.TransactionData;
import scala.util.Try;
import java.util.Map;
import scala.collection.immutable.List;
import scala.reflect.ScalaSignature;
import io.pivotal.greenplum.spark.Logging;

@ScalaSignature(bytes = "\u0006\u0001\u0005]c\u0001B\u0001\u0003\u00015\u0011ab\u00129gI&\u001cHoU3sm&\u001cWM\u0003\u0002\u0004\t\u0005iQ\r\u001f;fe:\fG\u000e^1cY\u0016T!!\u0002\u0004\u0002\u000bM\u0004\u0018M]6\u000b\u0005\u001dA\u0011!C4sK\u0016t\u0007\u000f\\;n\u0015\tI!\"A\u0004qSZ|G/\u00197\u000b\u0003-\t!![8\u0004\u0001M\u0019\u0001A\u0004\u000b\u0011\u0005=\u0011R\"\u0001\t\u000b\u0003E\tQa]2bY\u0006L!a\u0005\t\u0003\r\u0005s\u0017PU3g!\t)b#D\u0001\u0005\u0013\t9BAA\u0004M_\u001e<\u0017N\\4\t\u0011e\u0001!\u0011!Q\u0001\ni\t1c]3sm\u0016\u00148i\u001c8oK\u000e$xN\u001d%pgR\u0004\"a\u0007\u0010\u000f\u0005=a\u0012BA\u000f\u0011\u0003\u0019\u0001&/\u001a3fM&\u0011q\u0004\t\u0002\u0007'R\u0014\u0018N\\4\u000b\u0005u\u0001\u0002\u0002\u0003\u0012\u0001\u0005\u0003\u0005\u000b\u0011B\u0012\u0002\u001d\u00054\u0018-\u001b7bE2,\u0007k\u001c:ugB\u0019A\u0005L\u0018\u000f\u0005\u0015RcB\u0001\u0014*\u001b\u00059#B\u0001\u0015\r\u0003\u0019a$o\\8u}%\t\u0011#\u0003\u0002,!\u00059\u0001/Y2lC\u001e,\u0017BA\u0017/\u0005\u0011a\u0015n\u001d;\u000b\u0005-\u0002\u0002CA\b1\u0013\t\t\u0004CA\u0002J]RD\u0001b\r\u0001\u0003\u0002\u0003\u0006I\u0001N\u0001\nEV4g-\u001a:NCB\u0004B!\u000e\u001e\u001by5\taG\u0003\u00028q\u0005!Q\u000f^5m\u0015\u0005I\u0014\u0001\u00026bm\u0006L!a\u000f\u001c\u0003\u00075\u000b\u0007\u000fE\u0002>\u007f\u0005k\u0011A\u0010\u0006\u0003oAI!\u0001\u0011 \u0003\u0007Q\u0013\u0018\u0010\u0005\u0002C\t6\t1I\u0003\u00028\t%\u0011Qi\u0011\u0002\u0010)J\fgn]1di&|g\u000eR1uC\"Aq\t\u0001B\u0001B\u0003%\u0001*A\u0007tK:$')\u001e4gKJl\u0015\r\u001d\t\u0005kiR\u0012\n\u0005\u0002K\u00176\t!!\u0003\u0002M\u0005\ti\u0001+\u0019:uSRLwN\u001c#bi\u0006D\u0001B\u0014\u0001\u0003\u0006\u0004%IaT\u0001\u0007g\u0016\u0014h/\u001a:\u0016\u0003A\u0003\"AS)\n\u0005I\u0013!!D*feZ,'o\u0016:baB,'\u000f\u0003\u0005U\u0001\t\u0005\t\u0015!\u0003Q\u0003\u001d\u0019XM\u001d<fe\u0002B\u0001B\u0016\u0001\u0003\u0002\u0003\u0006IaV\u0001\u0010i&lWm\\;u\u0013:l\u0015\u000e\u001c7jgB\u0011q\u0002W\u0005\u00033B\u0011A\u0001T8oO\")1\f\u0001C\u00019\u00061A(\u001b8jiz\"r!\u00180`A\u0006\u00147\r\u0005\u0002K\u0001!)\u0011D\u0017a\u00015!)!E\u0017a\u0001G!)1G\u0017a\u0001i!)qI\u0017a\u0001\u0011\")aJ\u0017a\u0001!\")aK\u0017a\u0001/\")1\f\u0001C\u0001KR1QLZ4iS*DQA\t3A\u0002\rBQa\r3A\u0002QBQa\u00123A\u0002!CQA\u00143A\u0002ACQA\u00163A\u0002]Cq\u0001\u001c\u0001C\u0002\u0013%Q.A\u0004ti\u0006\u0014H/\u001a3\u0016\u00039\u0004\"a\u001c;\u000e\u0003AT!!\u001d:\u0002\r\u0005$x.\\5d\u0015\t\u0019h'\u0001\u0006d_:\u001cWO\u001d:f]RL!!\u001e9\u0003\u001b\u0005#x.\\5d\u0005>|G.Z1o\u0011\u00199\b\u0001)A\u0005]\u0006A1\u000f^1si\u0016$\u0007\u0005C\u0003z\u0001\u0011\u0005!0A\u0003ti\u0006\u0014H\u000fF\u0001|!\tyA0\u0003\u0002~!\t!QK\\5u\u0011\u0015y\b\u0001\"\u0001{\u0003\u0011\u0019Ho\u001c9\t\u000f\u0005\r\u0001\u0001\"\u0001\u0002\u0006\u00059q-\u001a;Q_J$H#A\u0018\t\u000f\u0005%\u0001\u0001\"\u0001\u0002\f\u0005)1\u000f^1uKV\u0011\u0011Q\u0002\t\u0005\u0003\u001f\t)BD\u0002K\u0003#I1!a\u0005\u0003\u0003M9\u0005O\u001a3jgR\u001cVM\u001d<jG\u0016\u001cF/\u0019;f\u0013\u0011\t9\"!\u0007\u0003\u000bY\u000bG.^3\n\u0007\u0005m\u0001CA\u0006F]VlWM]1uS>t\u0007bBA\u0010\u0001\u0011\u0005\u0011\u0011E\u0001\u0013O\u0016$(+Z2fSZ,G\rR1uC\u001a{'\u000f\u0006\u0003\u0002$\u0005=\u0002\u0003B\u001f@\u0003K\u0001B!a\n\u0002,5\u0011\u0011\u0011\u0006\u0006\u0003\u0017aJA!!\f\u0002*\tY\u0011J\u001c9viN#(/Z1n\u0011\u001d\t\t$!\bA\u0002i\tQ\u0002\u001e:b]N\f7\r^5p]&#\u0007bBA\u001b\u0001\u0011\u0005\u0011qG\u0001\u000bO\u0016$H+[7f_V$H#A,\t\u000f\u0005m\u0002\u0001\"\u0001\u0002>\u0005\u00192/\u001a;QCJ$\u0018\u000e^5p]\u0012\u000bG/\u0019$peR1\u0011qHA!\u0003\u000b\u00022!P ^\u0011\u001d\t\u0019%!\u000fA\u0002i\t1a[3z\u0011\u001d\t9%!\u000fA\u0002%\u000bQ\u0002]1si&$\u0018n\u001c8ECR\f\u0007bBA&\u0001\u0011\u0005\u0011QJ\u0001\u0017e\u0016lwN^3QCJ$\u0018\u000e^5p]\u0012\u000bG/\u0019$peR!\u0011qJA+!\u0011y\u0011\u0011K%\n\u0007\u0005M\u0003C\u0001\u0004PaRLwN\u001c\u0005\b\u0003\u0007\nI\u00051\u0001\u001b\u0001")
public class GpfdistService implements Logging
{
    private final String serverConnectorHost;
    private final List<Object> availablePorts;
    private final Map<String, Try<TransactionData>> bufferMap;
    private final Map<String, PartitionData> sendBufferMap;
    private final ServerWrapper server;
    private final long timeoutInMillis;
    private final AtomicBoolean started;
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
    
    private ServerWrapper server() {
        return this.server;
    }
    
    private AtomicBoolean started() {
        return this.started;
    }
    
    public synchronized void start() {
        final Iterator portsIterator = this.availablePorts.iterator();
        if (!this.started().get()) {
            if (portsIterator.hasNext() && !this.started().get()) {
                final int currentPort = BoxesRunTime.unboxToInt(portsIterator.next());
            }
            if (!this.started().get()) {
                throw new RuntimeException(new StringContext((Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[] { "Unable to start GpfdistService on any of ports=", "" })).s((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { this.availablePorts.mkString(", ") })));
            }
        }
    }
    
    public synchronized void stop() {
        if (this.started().compareAndSet(true, false)) {
            this.server().stop();
        }
    }
    
    public int getPort() {
        final Enumeration.Value state = this.state();
        final Enumeration.Value started = GpfdistServiceState$.MODULE$.Started();
        if (state == null) {
            if (started != null) {
                throw new RuntimeException(new StringContext((Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[] { "There is no server started" })).s((Seq)Nil$.MODULE$));
            }
        }
        else if (!state.equals(started)) {
            throw new RuntimeException(new StringContext((Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[] { "There is no server started" })).s((Seq)Nil$.MODULE$));
        }
        return ((ServerConnector)Predef$.MODULE$.refArrayOps((Object[])this.server().getConnectors()).head()).getLocalPort();
    }
    
    public Enumeration.Value state() {
        return GpfdistServiceState$.MODULE$.withName(new StringOps(Predef$.MODULE$.augmentString(this.server().getState().toLowerCase())).capitalize());
    }
    
    public Try<InputStream> getReceivedDataFor(final String transactionId) {
        final Try<TransactionData> try1 = this.bufferMap.remove(transactionId);
        Object o;
        if (try1 == null) {
            o = new Success((Object)new ByteArrayInputStream(new byte[0]));
        }
        else if (try1 instanceof Success) {
            final TransactionData transactionData = (TransactionData)((Success)try1).value();
            o = new Success((Object)transactionData.getInputStream());
        }
        else {
            if (!(try1 instanceof Failure)) {
                throw new MatchError((Object)try1);
            }
            final Throwable exception = ((Failure)try1).exception();
            o = new Failure(exception);
        }
        return (Try<InputStream>)o;
    }
    
    public long getTimeout() {
        return this.timeoutInMillis;
    }
    
    public Try<GpfdistService> setPartitionDataFor(final String key, final PartitionData partitionData) {
        Object o;
        if (this.sendBufferMap.containsKey(key)) {
            final String msg = new StringContext((Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[] { "Send buffer already exists for the given path = ", "" })).s((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { key }));
            o = new Failure((Throwable)new IllegalStateException(msg));
        }
        else {
            this.sendBufferMap.put(key, partitionData);
            o = new Success((Object)this);
        }
        return (Try<GpfdistService>)o;
    }
    
    public Option<PartitionData> removePartitionDataFor(final String key) {
        final Option apply;
        final Option partitionInfo = apply = Option$.MODULE$.apply((Object)this.sendBufferMap.remove(key));
        if (apply instanceof Some) {
            final PartitionData p = (PartitionData)((Some)apply).x();
            if (p.handled().get()) {
                final BoxedUnit boxedUnit = BoxedUnit.UNIT;
            }
            else {
                this.log().warn(new StringOps(Predef$.MODULE$.augmentString(new StringContext((Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[] { "Data has not been marked as processed for path ", "\n               | and partition ", "" })).s((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { key, BoxesRunTime.boxToInteger(p.partitionIndex()) })))).stripMargin());
                final BoxedUnit boxedUnit = BoxedUnit.UNIT;
            }
        }
        else {
            if (!None$.MODULE$.equals(apply)) {
                throw new MatchError((Object)apply);
            }
            this.log().warn(new StringContext((Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[] { "No partition exists for path ", "" })).s((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { key })));
            final BoxedUnit unit = BoxedUnit.UNIT;
        }
        return (Option<PartitionData>)partitionInfo;
    }
    
    public GpfdistService(final String serverConnectorHost, final List<Object> availablePorts, final Map<String, Try<TransactionData>> bufferMap, final Map<String, PartitionData> sendBufferMap, final ServerWrapper server, final long timeoutInMillis) {
        this.serverConnectorHost = serverConnectorHost;
        this.availablePorts = availablePorts;
        this.bufferMap = bufferMap;
        this.sendBufferMap = sendBufferMap;
        this.server = server;
        this.timeoutInMillis = timeoutInMillis;
        Logging$class.$init$(this);
        if (server == null) {
            throw new NullPointerException("server is null");
        }
        this.started = new AtomicBoolean(false);
    }
    
    public GpfdistService(final List<Object> availablePorts, final Map<String, Try<TransactionData>> bufferMap, final Map<String, PartitionData> sendBufferMap, final ServerWrapper server, final long timeoutInMillis) {
        this(ConnectorOptions$.MODULE$.GPDB_DEFAULT_SERVER_HOST(), availablePorts, bufferMap, sendBufferMap, server, timeoutInMillis);
    }
}
