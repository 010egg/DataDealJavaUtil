// 
// Decompiled by Procyon v0.5.36
// 

package io.pivotal.greenplum.spark.conf;

import scala.runtime.BoxesRunTime;
import scala.collection.Seq;
import scala.StringContext;
import scala.Predef$;
import scala.Option;
import scala.util.Try;
import scala.Function2;
import scala.collection.immutable.Map;
import scala.reflect.ScalaSignature;
import scala.Serializable;

@ScalaSignature(bytes = "\u0006\u000153A!\u0001\u0002\u0001\u001b\t)2i\u001c8oK\u000e$\u0018n\u001c8Q_>dw\n\u001d;j_:\u001c(BA\u0002\u0005\u0003\u0011\u0019wN\u001c4\u000b\u0005\u00151\u0011!B:qCJ\\'BA\u0004\t\u0003%9'/Z3oa2,XN\u0003\u0002\n\u0015\u00059\u0001/\u001b<pi\u0006d'\"A\u0006\u0002\u0005%|7\u0001A\n\u0005\u00019!r\u0003\u0005\u0002\u0010%5\t\u0001CC\u0001\u0012\u0003\u0015\u00198-\u00197b\u0013\t\u0019\u0002C\u0001\u0004B]f\u0014VM\u001a\t\u0003\u001fUI!A\u0006\t\u0003\u0019M+'/[1mSj\f'\r\\3\u0011\u0005aIR\"\u0001\u0002\n\u0005i\u0011!aB(qi&|gn\u001d\u0005\t9\u0001\u0011)\u0019!C\u0001;\u0005Q\u0001/\u0019:b[\u0016$XM]:\u0016\u0003y\u0001Ba\b\u0012&K9\u0011q\u0002I\u0005\u0003CA\ta\u0001\u0015:fI\u00164\u0017BA\u0012%\u0005\ri\u0015\r\u001d\u0006\u0003CA\u0001\"a\b\u0014\n\u0005\u001d\"#AB*ue&tw\r\u0003\u0005*\u0001\t\u0005\t\u0015!\u0003\u001f\u0003-\u0001\u0018M]1nKR,'o\u001d\u0011\t\u000b-\u0002A\u0011\u0001\u0017\u0002\rqJg.\u001b;?)\tic\u0006\u0005\u0002\u0019\u0001!)AD\u000ba\u0001=!)1\u0006\u0001C\u0001aQ\tQ\u0006C\u00043\u0001\t\u0007I\u0011B\u001a\u0002\rA\u0014XMZ5y+\u0005!\u0004CA\u001b;\u001b\u00051$BA\u001c9\u0003\u0011a\u0017M\\4\u000b\u0003e\nAA[1wC&\u0011qE\u000e\u0005\u0007y\u0001\u0001\u000b\u0011\u0002\u001b\u0002\u000fA\u0014XMZ5yA!9a\b\u0001b\u0001\n\u0003y\u0014aC7j]&lW/\\%eY\u0016,\u0012\u0001\u0011\t\u0003\u001f\u0005K!A\u0011\t\u0003\u0007%sG\u000f\u0003\u0004E\u0001\u0001\u0006I\u0001Q\u0001\r[&t\u0017.\\;n\u0013\u0012dW\r\t\u0005\b\r\u0002\u0011\r\u0011\"\u0001@\u0003=i\u0017\r_5nk6\u0004vn\u001c7TSj,\u0007B\u0002%\u0001A\u0003%\u0001)\u0001\tnCbLW.^7Q_>d7+\u001b>fA!9!\n\u0001b\u0001\n\u0003y\u0014!D5eY\u0016$\u0016.\\3pkRl5\u000f\u0003\u0004M\u0001\u0001\u0006I\u0001Q\u0001\u000fS\u0012dW\rV5nK>,H/T:!\u0001")
public class ConnectionPoolOptions implements Serializable, Options
{
    private final Map<String, String> parameters;
    private final String prefix;
    private final int minimumIdle;
    private final int maximumPoolSize;
    private final int idleTimeoutMs;
    
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
    
    private String prefix() {
        return this.prefix;
    }
    
    public int minimumIdle() {
        return this.minimumIdle;
    }
    
    public int maximumPoolSize() {
        return this.maximumPoolSize;
    }
    
    public int idleTimeoutMs() {
        return this.idleTimeoutMs;
    }
    
    public ConnectionPoolOptions(final Map<String, String> parameters) {
        this.parameters = parameters;
        Options$class.$init$(this);
        this.prefix = "pool";
        this.minimumIdle = BoxesRunTime.unboxToInt(this.option(new StringContext((Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[] { "", ".minIdle" })).s((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { this.prefix() })), new Default("0"), this.nonNegativeInt()));
        this.maximumPoolSize = BoxesRunTime.unboxToInt(this.option(new StringContext((Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[] { "", ".maxSize" })).s((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { this.prefix() })), new Default("64"), this.positiveInt()));
        this.idleTimeoutMs = BoxesRunTime.unboxToInt(this.option(new StringContext((Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[] { "", ".timeoutMs" })).s((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { this.prefix() })), new Default("10000"), this.positiveInt()));
    }
    
    public ConnectionPoolOptions() {
        this((Map<String, String>)Predef$.MODULE$.Map().empty());
    }
}
