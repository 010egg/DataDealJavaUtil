// 
// Decompiled by Procyon v0.5.36
// 

package io.pivotal.greenplum.spark.conf;

import scala.collection.immutable.Nil$;
import scala.collection.Seq;
import scala.StringContext;
import scala.collection.mutable.StringBuilder;
import java.time.Duration;
import scala.Predef$;
import scala.runtime.BoxesRunTime;
import io.pivotal.greenplum.spark.ConnectorUtils$;
import org.apache.commons.lang.StringUtils;
import scala.Option;
import scala.util.Try;
import scala.Function2;
import scala.Function0;
import io.pivotal.greenplum.spark.Logging$class;
import scala.runtime.TraitSetter;
import org.slf4j.Logger;
import scala.collection.immutable.List;
import scala.collection.immutable.Map;
import scala.reflect.ScalaSignature;
import io.pivotal.greenplum.spark.Logging;
import scala.Serializable;

@ScalaSignature(bytes = "\u0006\u0001\u0005mc\u0001B\u0001\u0003\u00015\u0011\u0001cQ8o]\u0016\u001cGo\u001c:PaRLwN\\:\u000b\u0005\r!\u0011\u0001B2p]\u001aT!!\u0002\u0004\u0002\u000bM\u0004\u0018M]6\u000b\u0005\u001dA\u0011!C4sK\u0016t\u0007\u000f\\;n\u0015\tI!\"A\u0004qSZ|G/\u00197\u000b\u0003-\t!![8\u0004\u0001M)\u0001A\u0004\u000b\u00187A\u0011qBE\u0007\u0002!)\t\u0011#A\u0003tG\u0006d\u0017-\u0003\u0002\u0014!\t1\u0011I\\=SK\u001a\u0004\"aD\u000b\n\u0005Y\u0001\"\u0001D*fe&\fG.\u001b>bE2,\u0007C\u0001\r\u001a\u001b\u0005\u0011\u0011B\u0001\u000e\u0003\u0005\u001dy\u0005\u000f^5p]N\u0004\"\u0001H\u000f\u000e\u0003\u0011I!A\b\u0003\u0003\u000f1{wmZ5oO\"A\u0001\u0005\u0001BC\u0002\u0013\u0005\u0011%\u0001\u0006qCJ\fW.\u001a;feN,\u0012A\t\t\u0005G\u0019J\u0013F\u0004\u0002\u0010I%\u0011Q\u0005E\u0001\u0007!J,G-\u001a4\n\u0005\u001dB#aA'ba*\u0011Q\u0005\u0005\t\u0003G)J!a\u000b\u0015\u0003\rM#(/\u001b8h\u0011!i\u0003A!A!\u0002\u0013\u0011\u0013a\u00039be\u0006lW\r^3sg\u0002B\u0001b\f\u0001\u0003\u0006\u0004%\t!I\u0001\u0004K:4\b\u0002C\u0019\u0001\u0005\u0003\u0005\u000b\u0011\u0002\u0012\u0002\t\u0015tg\u000f\t\u0005\u0006g\u0001!\t\u0001N\u0001\u0007y%t\u0017\u000e\u001e \u0015\u0007U2t\u0007\u0005\u0002\u0019\u0001!)\u0001E\ra\u0001E!9qF\rI\u0001\u0002\u0004\u0011\u0003\"B\u001a\u0001\t\u0003ID#A\u001b\t\u000fm\u0002!\u0019!C\u0001y\u0005!\u0001o\u001c:u+\u0005i\u0004c\u0001 G\u0013:\u0011q\b\u0012\b\u0003\u0001\u000ek\u0011!\u0011\u0006\u0003\u00052\ta\u0001\u0010:p_Rt\u0014\"A\t\n\u0005\u0015\u0003\u0012a\u00029bG.\fw-Z\u0005\u0003\u000f\"\u0013A\u0001T5ti*\u0011Q\t\u0005\t\u0003\u001f)K!a\u0013\t\u0003\u0007%sG\u000f\u0003\u0004N\u0001\u0001\u0006I!P\u0001\u0006a>\u0014H\u000f\t\u0005\b\u001f\u0002\u0011\r\u0011\"\u0001Q\u0003-)8/\u001a%pgRt\u0017-\\3\u0016\u0003E\u0003\"a\u0004*\n\u0005M\u0003\"a\u0002\"p_2,\u0017M\u001c\u0005\u0007+\u0002\u0001\u000b\u0011B)\u0002\u0019U\u001cX\rS8ti:\fW.\u001a\u0011\t\u000f]\u0003!\u0019!C\u00011\u00069\u0011\r\u001a3sKN\u001cX#A\u0015\t\ri\u0003\u0001\u0015!\u0003*\u0003!\tG\r\u001a:fgN\u0004\u0003b\u0002/\u0001\u0005\u0004%\t\u0001W\u0001\u0011]\u0016$xo\u001c:l\u0013:$XM\u001d4bG\u0016DaA\u0018\u0001!\u0002\u0013I\u0013!\u00058fi^|'o[%oi\u0016\u0014h-Y2fA!9\u0001\r\u0001b\u0001\n\u0003\t\u0017AE'B1~#\u0016*T#P+R{V*\u0013'M\u0013N+\u0012A\u0019\t\u0003\u001f\rL!\u0001\u001a\t\u0003\t1{gn\u001a\u0005\u0007M\u0002\u0001\u000b\u0011\u00022\u0002'5\u000b\u0005l\u0018+J\u001b\u0016{U\u000bV0N\u00132c\u0015j\u0015\u0011\t\u000f!\u0004!\u0019!C\u0001C\u0006yA/[7f_V$\u0018J\\'jY2L7\u000f\u0003\u0004k\u0001\u0001\u0006IAY\u0001\u0011i&lWm\\;u\u0013:l\u0015\u000e\u001c7jg\u0002:Q\u0001\u001c\u0002\t\u00025\f\u0001cQ8o]\u0016\u001cGo\u001c:PaRLwN\\:\u0011\u0005aqg!B\u0001\u0003\u0011\u0003y7c\u00018\u000f)!)1G\u001cC\u0001cR\tQ\u000eC\u0004t]\n\u0007I\u0011\u0001;\u0002#\u001d\u0003FIQ0O\u000bR;vJU&`!>\u0013F+F\u0001v!\t180D\u0001x\u0015\tA\u00180\u0001\u0003mC:<'\"\u0001>\u0002\t)\fg/Y\u0005\u0003W]Da! 8!\u0002\u0013)\u0018AE$Q\t\n{f*\u0012+X\u001fJ[u\fU(S)\u0002Bqa 8C\u0002\u0013\u0005A/\u0001\u000bH!\u0012\u0013uLT#U/>\u00136jX!E\tJ+5k\u0015\u0005\b\u0003\u0007q\u0007\u0015!\u0003v\u0003U9\u0005\u000b\u0012\"`\u001d\u0016#vk\u0014*L?\u0006#EIU#T'\u0002B\u0001\"a\u0002o\u0005\u0004%\t\u0001^\u0001\u0017\u000fB#%i\u0018(F)^{%kS0J\u001dR+%KR!D\u000b\"9\u00111\u00028!\u0002\u0013)\u0018aF$Q\t\n{f*\u0012+X\u001fJ[u,\u0013(U\u000bJ3\u0015iQ#!\u0011!\tyA\u001cb\u0001\n\u0003!\u0018!F$Q\t\n{f*\u0012+X\u001fJ[u\fS(T):\u000bU*\u0012\u0005\b\u0003'q\u0007\u0015!\u0003v\u0003Y9\u0005\u000b\u0012\"`\u001d\u0016#vk\u0014*L?\"{5\u000b\u0016(B\u001b\u0016\u0003\u0003\u0002CA\f]\n\u0007I\u0011\u0001;\u0002)\u001d\u0003FIQ0O\u000bR;vJU&`)&kUiT+U\u0011\u001d\tYB\u001cQ\u0001\nU\fQc\u0012)E\u0005~sU\tV,P%.{F+S'F\u001fV#\u0006\u0005\u0003\u0005\u0002 9\u0014\r\u0011\"\u0001u\u0003M9\u0005\u000b\u0012\"`\u000b:3vLV!S?B\u0013VIR%Y\u0011\u001d\t\u0019C\u001cQ\u0001\nU\fAc\u0012)E\u0005~+eJV0W\u0003J{\u0006KU#G\u0013b\u0003\u0003\u0002CA\u0014]\n\u0007I\u0011\u0001;\u0002=\u001d\u0003FIQ0E\u000b\u001a\u000bU\u000b\u0014+`\u001d\u0016#vk\u0014*L?&sE+\u0012*G\u0003\u000e+\u0005bBA\u0016]\u0002\u0006I!^\u0001 \u000fB#%i\u0018#F\r\u0006+F\nV0O\u000bR;vJU&`\u0013:#VI\u0015$B\u0007\u0016\u0003\u0003\u0002CA\u0018]\n\u0007I\u0011\u0001;\u00021\u001d\u0003FIQ0E\u000b\u001a\u000bU\u000b\u0014+`'\u0016\u0013f+\u0012*`\u0011>\u001bF\u000bC\u0004\u000249\u0004\u000b\u0011B;\u00023\u001d\u0003FIQ0E\u000b\u001a\u000bU\u000b\u0014+`'\u0016\u0013f+\u0012*`\u0011>\u001bF\u000b\t\u0005\n\u0003oq\u0017\u0013!C\u0001\u0003s\t1\u0004\n7fgNLg.\u001b;%OJ,\u0017\r^3sI\u0011,g-Y;mi\u0012\u0012TCAA\u001eU\r\u0011\u0013QH\u0016\u0003\u0003\u007f\u0001B!!\u0011\u0002L5\u0011\u00111\t\u0006\u0005\u0003\u000b\n9%A\u0005v]\u000eDWmY6fI*\u0019\u0011\u0011\n\t\u0002\u0015\u0005tgn\u001c;bi&|g.\u0003\u0003\u0002N\u0005\r#!E;oG\",7m[3e-\u0006\u0014\u0018.\u00198dK\"I\u0011\u0011\u000b8\u0002\u0002\u0013%\u00111K\u0001\fe\u0016\fGMU3t_24X\r\u0006\u0002\u0002VA\u0019a/a\u0016\n\u0007\u0005esO\u0001\u0004PE*,7\r\u001e")
public class ConnectorOptions implements Serializable, Options, Logging
{
    private final Map<String, String> parameters;
    private final Map<String, String> env;
    private final List<Object> port;
    private final boolean useHostname;
    private final String address;
    private final String networkInterface;
    private final long MAX_TIMEOUT_MILLIS;
    private final long timeoutInMillis;
    private transient Logger io$pivotal$greenplum$spark$Logging$$log_;
    
    public static Map<String, String> $lessinit$greater$default$2() {
        return ConnectorOptions$.MODULE$.$lessinit$greater$default$2();
    }
    
    public static String GPDB_DEFAULT_SERVER_HOST() {
        return ConnectorOptions$.MODULE$.GPDB_DEFAULT_SERVER_HOST();
    }
    
    public static String GPDB_DEFAULT_NETWORK_INTERFACE() {
        return ConnectorOptions$.MODULE$.GPDB_DEFAULT_NETWORK_INTERFACE();
    }
    
    public static String GPDB_ENV_VAR_PREFIX() {
        return ConnectorOptions$.MODULE$.GPDB_ENV_VAR_PREFIX();
    }
    
    public static String GPDB_NETWORK_TIMEOUT() {
        return ConnectorOptions$.MODULE$.GPDB_NETWORK_TIMEOUT();
    }
    
    public static String GPDB_NETWORK_HOSTNAME() {
        return ConnectorOptions$.MODULE$.GPDB_NETWORK_HOSTNAME();
    }
    
    public static String GPDB_NETWORK_INTERFACE() {
        return ConnectorOptions$.MODULE$.GPDB_NETWORK_INTERFACE();
    }
    
    public static String GPDB_NETWORK_ADDRESS() {
        return ConnectorOptions$.MODULE$.GPDB_NETWORK_ADDRESS();
    }
    
    public static String GPDB_NETWORK_PORT() {
        return ConnectorOptions$.MODULE$.GPDB_NETWORK_PORT();
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
    
    public String option(final String optionName, final WhatIfMissing whatIfMissing) {
        return Options$class.option(this, optionName, whatIfMissing);
    }
    
    public <T> T option(final String optionName, final WhatIfMissing whatIfMissing, final Function2<String, String, Try<T>> convert) {
        return (T)Options$class.option(this, optionName, whatIfMissing, convert);
    }
    
    public Option<String> option(final String optionName) {
        return (Option<String>)Options$class.option(this, optionName);
    }
    
    public <T> Option<T> option(final String optionName, final Function2<String, String, Try<T>> convert) {
        return (Option<T>)Options$class.option(this, optionName, convert);
    }
    
    public Function2<String, String, Try<Object>> bool() {
        return (Function2<String, String, Try<Object>>)Options$class.bool(this);
    }
    
    public Function2<String, String, Try<Object>> naturalLong() {
        return (Function2<String, String, Try<Object>>)Options$class.naturalLong(this);
    }
    
    public Function2<String, String, Try<Object>> int() {
        return (Function2<String, String, Try<Object>>)Options$class.int(this);
    }
    
    public Function2<String, String, Try<Object>> positiveInt() {
        return (Function2<String, String, Try<Object>>)Options$class.positiveInt(this);
    }
    
    public Function2<String, String, Try<Object>> nonNegativeInt() {
        return (Function2<String, String, Try<Object>>)Options$class.nonNegativeInt(this);
    }
    
    public Map<String, String> parameters() {
        return this.parameters;
    }
    
    public Map<String, String> env() {
        return this.env;
    }
    
    public List<Object> port() {
        return this.port;
    }
    
    public boolean useHostname() {
        return this.useHostname;
    }
    
    public String address() {
        return this.address;
    }
    
    public String networkInterface() {
        return this.networkInterface;
    }
    
    public long MAX_TIMEOUT_MILLIS() {
        return this.MAX_TIMEOUT_MILLIS;
    }
    
    public long timeoutInMillis() {
        return this.timeoutInMillis;
    }
    
    public ConnectorOptions(final Map<String, String> parameters, final Map<String, String> env) {
        this.parameters = parameters;
        this.env = env;
        Options$class.$init$(this);
        Logging$class.$init$(this);
        final String value = this.option(ConnectorOptions$.MODULE$.GPDB_NETWORK_PORT(), new Default("0"));
        final String portString = (String)(StringUtils.startsWith(value, ConnectorOptions$.MODULE$.GPDB_ENV_VAR_PREFIX()) ? env.getOrElse((Object)value.substring(ConnectorOptions$.MODULE$.GPDB_ENV_VAR_PREFIX().length()), (Function0)new ConnectorOptions$$anonfun.ConnectorOptions$$anonfun$2(this)) : value);
        this.port = ConnectorUtils$.MODULE$.parsePortString(portString);
        this.useHostname = BoxesRunTime.unboxToBoolean(this.option(ConnectorOptions$.MODULE$.GPDB_NETWORK_HOSTNAME(), new Default("false"), this.bool()));
        final String value2 = (String)this.option(ConnectorOptions$.MODULE$.GPDB_NETWORK_ADDRESS()).orNull(Predef$.MODULE$.$conforms());
        String address;
        if (StringUtils.isNotBlank(value2)) {
            final String environmentVariableKey = value2.startsWith(ConnectorOptions$.MODULE$.GPDB_ENV_VAR_PREFIX()) ? value2.substring(ConnectorOptions$.MODULE$.GPDB_ENV_VAR_PREFIX().length()) : value2;
            address = (String)env.get((Object)environmentVariableKey).orNull(Predef$.MODULE$.$conforms());
        }
        else {
            address = null;
        }
        this.address = address;
        final String value3 = (String)this.option(ConnectorOptions$.MODULE$.GPDB_NETWORK_INTERFACE()).orNull(Predef$.MODULE$.$conforms());
        this.networkInterface = (String)(StringUtils.startsWith(value3, ConnectorOptions$.MODULE$.GPDB_ENV_VAR_PREFIX()) ? env.get((Object)value3.substring(ConnectorOptions$.MODULE$.GPDB_ENV_VAR_PREFIX().length())).orNull(Predef$.MODULE$.$conforms()) : value3);
        this.MAX_TIMEOUT_MILLIS = Duration.ofHours(2L).toMillis();
        this.timeoutInMillis = BoxesRunTime.unboxToLong(this.option(ConnectorOptions$.MODULE$.GPDB_NETWORK_TIMEOUT(), this.naturalLong()).getOrElse((Function0)new ConnectorOptions$$anonfun.ConnectorOptions$$anonfun$1(this)));
        if (this.timeoutInMillis() > this.MAX_TIMEOUT_MILLIS()) {
            throw new IllegalArgumentException(new StringBuilder().append((Object)new StringContext((Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[] { "requirement failed: Option " })).s((Seq)Nil$.MODULE$)).append((Object)new StringContext((Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[] { "'", "' has a value of '", "' that is " })).s((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { ConnectorOptions$.MODULE$.GPDB_NETWORK_TIMEOUT(), BoxesRunTime.boxToLong(this.timeoutInMillis()) }))).append((Object)new StringContext((Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[] { "greater than the maximum allowed value of ", " milliseconds." })).s((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { BoxesRunTime.boxToLong(this.MAX_TIMEOUT_MILLIS()) }))).toString());
        }
    }
    
    public ConnectorOptions() {
        this((Map<String, String>)Predef$.MODULE$.Map().empty(), ConnectorOptions$.MODULE$.$lessinit$greater$default$2());
    }
}
