// 
// Decompiled by Procyon v0.5.36
// 

package io.pivotal.greenplum.spark.conf;

import org.apache.spark.sql.catalyst.util.CaseInsensitiveMap$;
import scala.runtime.BoxesRunTime;
import scala.util.Try;
import scala.Function2;
import scala.Function0;
import io.pivotal.greenplum.spark.Logging$class;
import scala.runtime.TraitSetter;
import scala.collection.immutable.Map;
import scala.collection.immutable.List;
import org.slf4j.Logger;
import scala.Option;
import org.apache.spark.sql.catalyst.util.CaseInsensitiveMap;
import scala.reflect.ScalaSignature;
import io.pivotal.greenplum.spark.Logging;
import scala.Serializable;

@ScalaSignature(bytes = "\u0006\u0001\u0005-f\u0001B\u0001\u0003\u00015\u0011\u0001c\u0012:fK:\u0004H.^7PaRLwN\\:\u000b\u0005\r!\u0011\u0001B2p]\u001aT!!\u0002\u0004\u0002\u000bM\u0004\u0018M]6\u000b\u0005\u001dA\u0011!C4sK\u0016t\u0007\u000f\\;n\u0015\tI!\"A\u0004qSZ|G/\u00197\u000b\u0003-\t!![8\u0004\u0001M)\u0001A\u0004\u000b\u00187A\u0011qBE\u0007\u0002!)\t\u0011#A\u0003tG\u0006d\u0017-\u0003\u0002\u0014!\t1\u0011I\\=SK\u001a\u0004\"aD\u000b\n\u0005Y\u0001\"\u0001D*fe&\fG.\u001b>bE2,\u0007C\u0001\r\u001a\u001b\u0005\u0011\u0011B\u0001\u000e\u0003\u0005\u001dy\u0005\u000f^5p]N\u0004\"\u0001H\u000f\u000e\u0003\u0011I!A\b\u0003\u0003\u000f1{wmZ5oO\"A\u0001\u0005\u0001BC\u0002\u0013\u0005\u0011%\u0001\u0006qCJ\fW.\u001a;feN,\u0012A\t\t\u0004G=\nT\"\u0001\u0013\u000b\u0005\u00152\u0013\u0001B;uS2T!a\n\u0015\u0002\u0011\r\fG/\u00197zgRT!!\u000b\u0016\u0002\u0007M\fHN\u0003\u0002\u0006W)\u0011A&L\u0001\u0007CB\f7\r[3\u000b\u00039\n1a\u001c:h\u0013\t\u0001DE\u0001\nDCN,\u0017J\\:f]NLG/\u001b<f\u001b\u0006\u0004\bC\u0001\u001a6\u001d\ty1'\u0003\u00025!\u00051\u0001K]3eK\u001aL!AN\u001c\u0003\rM#(/\u001b8h\u0015\t!\u0004\u0003\u0003\u0005:\u0001\t\u0005\t\u0015!\u0003#\u0003-\u0001\u0018M]1nKR,'o\u001d\u0011\t\u000bm\u0002A\u0011\u0001\u001f\u0002\rqJg.\u001b;?)\tid\b\u0005\u0002\u0019\u0001!)\u0001E\u000fa\u0001E!)1\b\u0001C\u0001\u0001R\u0011Q(\u0011\u0005\u0006\u0005~\u0002\raQ\u0001\u0007a\u0006\u0014\u0018-\\:\u0011\tI\"\u0015'M\u0005\u0003\u000b^\u00121!T1q\u0011\u001d9\u0005A1A\u0005\u0002!\u000b1!\u001e:m+\u0005\t\u0004B\u0002&\u0001A\u0003%\u0011'\u0001\u0003ve2\u0004\u0003b\u0002'\u0001\u0005\u0004%\t\u0001S\u0001\u0005kN,'\u000f\u0003\u0004O\u0001\u0001\u0006I!M\u0001\u0006kN,'\u000f\t\u0005\b!\u0002\u0011\r\u0011\"\u0001R\u0003!\u0001\u0018m]:x_J$W#\u0001*\u0011\u0007=\u0019\u0016'\u0003\u0002U!\t1q\n\u001d;j_:DaA\u0016\u0001!\u0002\u0013\u0011\u0016!\u00039bgN<xN\u001d3!\u0011\u001dA\u0006A1A\u0005\u0002!\u000b\u0001\u0002\u001a2TG\",W.\u0019\u0005\u00075\u0002\u0001\u000b\u0011B\u0019\u0002\u0013\u0011\u00147k\u00195f[\u0006\u0004\u0003b\u0002/\u0001\u0005\u0004%\t\u0001S\u0001\bI\n$\u0016M\u00197f\u0011\u0019q\u0006\u0001)A\u0005c\u0005AAM\u0019+bE2,\u0007\u0005C\u0004a\u0001\t\u0007I\u0011\u0001%\u0002\u001fA\f'\u000f^5uS>t7i\u001c7v[:DaA\u0019\u0001!\u0002\u0013\t\u0014\u0001\u00059beRLG/[8o\u0007>dW/\u001c8!\u0011\u001d!\u0007A1A\u0005\u0002\u0015\f!\u0002]1si&$\u0018n\u001c8t+\u00051\u0007cA\bTOB\u0011q\u0002[\u0005\u0003SB\u00111!\u00138u\u0011\u0019Y\u0007\u0001)A\u0005M\u0006Y\u0001/\u0019:uSRLwN\\:!\u0011\u001di\u0007A1A\u0005\u0002!\u000ba\u0001\u001a:jm\u0016\u0014\bBB8\u0001A\u0003%\u0011'A\u0004ee&4XM\u001d\u0011\t\u000fE\u0004!\u0019!C\u0001e\u0006\u00012m\u001c8oK\u000e$xN](qi&|gn]\u000b\u0002gB\u0011\u0001\u0004^\u0005\u0003k\n\u0011\u0001cQ8o]\u0016\u001cGo\u001c:PaRLwN\\:\t\r]\u0004\u0001\u0015!\u0003t\u0003E\u0019wN\u001c8fGR|'o\u00149uS>t7\u000f\t\u0005\bs\u0002\u0011\r\u0011\"\u0001{\u0003U\u0019wN\u001c8fGRLwN\u001c)p_2|\u0005\u000f^5p]N,\u0012a\u001f\t\u00031qL!! \u0002\u0003+\r{gN\\3di&|g\u000eU8pY>\u0003H/[8og\"1q\u0010\u0001Q\u0001\nm\facY8o]\u0016\u001cG/[8o!>|Gn\u00149uS>t7\u000f\t\u0005\n\u0003\u0007\u0001!\u0019!C\u0001\u0003\u000b\tQ\u0002\u001e:v]\u000e\fG/\u001a+bE2,WCAA\u0004!\ry\u0011\u0011B\u0005\u0004\u0003\u0017\u0001\"a\u0002\"p_2,\u0017M\u001c\u0005\t\u0003\u001f\u0001\u0001\u0015!\u0003\u0002\b\u0005qAO];oG\u0006$X\rV1cY\u0016\u0004\u0003\u0002CA\n\u0001\t\u0007I\u0011A)\u0002\u001b\u0011L7\u000f\u001e:jEV$X\r\u001a\"z\u0011\u001d\t9\u0002\u0001Q\u0001\nI\u000ba\u0002Z5tiJL'-\u001e;fI\nK\b\u0005C\u0005\u0002\u001c\u0001\u0011\r\u0011\"\u0001\u0002\u0006\u0005!\u0012\u000e^3sCR|'o\u00149uS6L'0\u0019;j_:D\u0001\"a\b\u0001A\u0003%\u0011qA\u0001\u0016SR,'/\u0019;pe>\u0003H/[7ju\u0006$\u0018n\u001c8!\u000f\u001d\t\u0019C\u0001E\u0001\u0003K\t\u0001c\u0012:fK:\u0004H.^7PaRLwN\\:\u0011\u0007a\t9C\u0002\u0004\u0002\u0005!\u0005\u0011\u0011F\n\u0004\u0003O\u0019\bbB\u001e\u0002(\u0011\u0005\u0011Q\u0006\u000b\u0003\u0003KA!\"!\r\u0002(\t\u0007I\u0011AA\u001a\u0003!9\u0005\u000b\u0012\"`+JcUCAA\u001b!\u0011\t9$!\u0011\u000e\u0005\u0005e\"\u0002BA\u001e\u0003{\tA\u0001\\1oO*\u0011\u0011qH\u0001\u0005U\u00064\u0018-C\u00027\u0003sA\u0011\"!\u0012\u0002(\u0001\u0006I!!\u000e\u0002\u0013\u001d\u0003FIQ0V%2\u0003\u0003BCA%\u0003O\u0011\r\u0011\"\u0001\u00024\u0005Iq\t\u0015#C?V\u001bVI\u0015\u0005\n\u0003\u001b\n9\u0003)A\u0005\u0003k\t!b\u0012)E\u0005~+6+\u0012*!\u0011)\t\t&a\nC\u0002\u0013\u0005\u00111G\u0001\u000e\u000fB#%i\u0018)B'N;vJ\u0015#\t\u0013\u0005U\u0013q\u0005Q\u0001\n\u0005U\u0012AD$Q\t\n{\u0006+Q*T/>\u0013F\t\t\u0005\u000b\u00033\n9C1A\u0005\u0002\u0005M\u0012\u0001E$Q\t\n{6k\u0011%F\u001b\u0006{f*Q'F\u0011%\ti&a\n!\u0002\u0013\t)$A\tH!\u0012\u0013ulU\"I\u000b6\u000buLT!N\u000b\u0002B!\"!\u0019\u0002(\t\u0007I\u0011AA\u001a\u0003=9\u0005\u000b\u0012\"`)\u0006\u0013E*R0O\u00036+\u0005\"CA3\u0003O\u0001\u000b\u0011BA\u001b\u0003A9\u0005\u000b\u0012\"`)\u0006\u0013E*R0O\u00036+\u0005\u0005\u0003\u0006\u0002j\u0005\u001d\"\u0019!C\u0001\u0003g\tQc\u0012)E\u0005~\u0003\u0016I\u0015+J)&{ejX\"P\u0019Vke\nC\u0005\u0002n\u0005\u001d\u0002\u0015!\u0003\u00026\u00051r\t\u0015#C?B\u000b%\u000bV%U\u0013>sulQ(M+6s\u0005\u0005\u0003\u0006\u0002r\u0005\u001d\"\u0019!C\u0001\u0003g\tqb\u0012)E\u0005~\u0003\u0016I\u0015+J)&{ej\u0015\u0005\n\u0003k\n9\u0003)A\u0005\u0003k\t\u0001c\u0012)E\u0005~\u0003\u0016I\u0015+J)&{ej\u0015\u0011\t\u0015\u0005e\u0014q\u0005b\u0001\n\u0003\t\u0019$A\u0006H!\u0012\u0013u\f\u0012*J-\u0016\u0013\u0006\"CA?\u0003O\u0001\u000b\u0011BA\u001b\u000319\u0005\u000b\u0012\"`\tJKe+\u0012*!\u0011)\t\t)a\nC\u0002\u0013\u0005\u00111G\u0001\u0014\u000fB#%i\u0018#J'R\u0013\u0016JQ+U\u000b\u0012{&)\u0017\u0005\n\u0003\u000b\u000b9\u0003)A\u0005\u0003k\tAc\u0012)E\u0005~#\u0015j\u0015+S\u0013\n+F+\u0012#`\u0005f\u0003\u0003BCAE\u0003O\u0011\r\u0011\"\u0001\u00024\u0005\u0019r\t\u0015#C?R\u0013VKT\"B)\u0016{F+\u0011\"M\u000b\"I\u0011QRA\u0014A\u0003%\u0011QG\u0001\u0015\u000fB#%i\u0018+S+:\u001b\u0015\tV#`)\u0006\u0013E*\u0012\u0011\t\u0015\u0005E\u0015q\u0005b\u0001\n\u0003\t\u0019$\u0001\u000eH!\u0012\u0013u,\u0013+F%\u0006#vJU0P!RKU*\u0013.B)&{e\nC\u0005\u0002\u0016\u0006\u001d\u0002\u0015!\u0003\u00026\u0005Yr\t\u0015#C?&#VIU!U\u001fJ{v\n\u0015+J\u001b&S\u0016\tV%P\u001d\u0002B!\"!'\u0002(\t\u0007I\u0011AA\u001a\u0003u!UIR!V\u0019R{\u0006+\u0011*U\u0013RKuJT0D\u001f2+VJT0O\u00036+\u0005\"CAO\u0003O\u0001\u000b\u0011BA\u001b\u0003y!UIR!V\u0019R{\u0006+\u0011*U\u0013RKuJT0D\u001f2+VJT0O\u00036+\u0005\u0005\u0003\u0006\u0002\"\u0006\u001d\u0012\u0011!C\u0005\u0003G\u000b1B]3bIJ+7o\u001c7wKR\u0011\u0011Q\u0015\t\u0005\u0003o\t9+\u0003\u0003\u0002*\u0006e\"AB(cU\u0016\u001cG\u000f")
public class GreenplumOptions implements Serializable, Options, Logging
{
    private final CaseInsensitiveMap<String> parameters;
    private final String url;
    private final String user;
    private final Option<String> password;
    private final String dbSchema;
    private final String dbTable;
    private final String partitionColumn;
    private final Option<Object> partitions;
    private final String driver;
    private final ConnectorOptions connectorOptions;
    private final ConnectionPoolOptions connectionPoolOptions;
    private final boolean truncateTable;
    private final Option<String> distributedBy;
    private final boolean iteratorOptimization;
    private transient Logger io$pivotal$greenplum$spark$Logging$$log_;
    
    public static String DEFAULT_PARTITION_COLUMN_NAME() {
        return GreenplumOptions$.MODULE$.DEFAULT_PARTITION_COLUMN_NAME();
    }
    
    public static String GPDB_ITERATOR_OPTIMIZATION() {
        return GreenplumOptions$.MODULE$.GPDB_ITERATOR_OPTIMIZATION();
    }
    
    public static String GPDB_TRUNCATE_TABLE() {
        return GreenplumOptions$.MODULE$.GPDB_TRUNCATE_TABLE();
    }
    
    public static String GPDB_DISTRIBUTED_BY() {
        return GreenplumOptions$.MODULE$.GPDB_DISTRIBUTED_BY();
    }
    
    public static String GPDB_DRIVER() {
        return GreenplumOptions$.MODULE$.GPDB_DRIVER();
    }
    
    public static String GPDB_PARTITIONS() {
        return GreenplumOptions$.MODULE$.GPDB_PARTITIONS();
    }
    
    public static String GPDB_PARTITION_COLUMN() {
        return GreenplumOptions$.MODULE$.GPDB_PARTITION_COLUMN();
    }
    
    public static String GPDB_TABLE_NAME() {
        return GreenplumOptions$.MODULE$.GPDB_TABLE_NAME();
    }
    
    public static String GPDB_SCHEMA_NAME() {
        return GreenplumOptions$.MODULE$.GPDB_SCHEMA_NAME();
    }
    
    public static String GPDB_PASSWORD() {
        return GreenplumOptions$.MODULE$.GPDB_PASSWORD();
    }
    
    public static String GPDB_USER() {
        return GreenplumOptions$.MODULE$.GPDB_USER();
    }
    
    public static String GPDB_URL() {
        return GreenplumOptions$.MODULE$.GPDB_URL();
    }
    
    public static long timeoutInMillis() {
        return GreenplumOptions$.MODULE$.timeoutInMillis();
    }
    
    public static long MAX_TIMEOUT_MILLIS() {
        return GreenplumOptions$.MODULE$.MAX_TIMEOUT_MILLIS();
    }
    
    public static String networkInterface() {
        return GreenplumOptions$.MODULE$.networkInterface();
    }
    
    public static String address() {
        return GreenplumOptions$.MODULE$.address();
    }
    
    public static boolean useHostname() {
        return GreenplumOptions$.MODULE$.useHostname();
    }
    
    public static List<Object> port() {
        return GreenplumOptions$.MODULE$.port();
    }
    
    public static Map<String, String> env() {
        return GreenplumOptions$.MODULE$.env();
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
    
    public CaseInsensitiveMap<String> parameters() {
        return this.parameters;
    }
    
    public String url() {
        return this.url;
    }
    
    public String user() {
        return this.user;
    }
    
    public Option<String> password() {
        return this.password;
    }
    
    public String dbSchema() {
        return this.dbSchema;
    }
    
    public String dbTable() {
        return this.dbTable;
    }
    
    public String partitionColumn() {
        return this.partitionColumn;
    }
    
    public Option<Object> partitions() {
        return this.partitions;
    }
    
    public String driver() {
        return this.driver;
    }
    
    public ConnectorOptions connectorOptions() {
        return this.connectorOptions;
    }
    
    public ConnectionPoolOptions connectionPoolOptions() {
        return this.connectionPoolOptions;
    }
    
    public boolean truncateTable() {
        return this.truncateTable;
    }
    
    public Option<String> distributedBy() {
        return this.distributedBy;
    }
    
    public boolean iteratorOptimization() {
        return this.iteratorOptimization;
    }
    
    public GreenplumOptions(final CaseInsensitiveMap<String> parameters) {
        this.parameters = parameters;
        Options$class.$init$(this);
        Logging$class.$init$(this);
        this.url = this.option(GreenplumOptions$.MODULE$.GPDB_URL(), ErrorIfMissing$.MODULE$);
        this.user = this.option(GreenplumOptions$.MODULE$.GPDB_USER(), ErrorIfMissing$.MODULE$);
        this.password = this.option(GreenplumOptions$.MODULE$.GPDB_PASSWORD());
        this.dbSchema = this.option(GreenplumOptions$.MODULE$.GPDB_SCHEMA_NAME(), new Default("public"));
        this.dbTable = this.option(GreenplumOptions$.MODULE$.GPDB_TABLE_NAME(), ErrorIfMissing$.MODULE$);
        this.partitionColumn = this.option(GreenplumOptions$.MODULE$.GPDB_PARTITION_COLUMN(), new Default("gp_segment_id"));
        this.partitions = this.option(GreenplumOptions$.MODULE$.GPDB_PARTITIONS(), this.positiveInt());
        this.driver = this.option(GreenplumOptions$.MODULE$.GPDB_DRIVER(), new Default("org.postgresql.Driver"));
        this.connectorOptions = new ConnectorOptions((Map<String, String>)parameters, ConnectorOptions$.MODULE$.$lessinit$greater$default$2());
        this.connectionPoolOptions = new ConnectionPoolOptions((Map<String, String>)parameters);
        this.truncateTable = BoxesRunTime.unboxToBoolean(this.option(GreenplumOptions$.MODULE$.GPDB_TRUNCATE_TABLE(), new Default("false"), this.bool()));
        this.distributedBy = this.option(GreenplumOptions$.MODULE$.GPDB_DISTRIBUTED_BY());
        this.iteratorOptimization = BoxesRunTime.unboxToBoolean(this.option(GreenplumOptions$.MODULE$.GPDB_ITERATOR_OPTIMIZATION(), new Default("true"), this.bool()));
    }
    
    public GreenplumOptions(final Map<String, String> params) {
        this((CaseInsensitiveMap<String>)CaseInsensitiveMap$.MODULE$.apply((Map)params));
    }
}
