// 
// Decompiled by Procyon v0.5.36
// 

package io.pivotal.greenplum.spark.externaltable;

import java.util.concurrent.ConcurrentHashMap;
import scala.runtime.BoxesRunTime;
import io.pivotal.greenplum.spark.GreenplumCSVFormat$;
import scala.Function1;
import scala.collection.Seq$;
import scala.collection.TraversableOnce;
import org.apache.spark.sql.Row;
import java.io.InputStream;
import javax.servlet.ServletInputStream;
import scala.collection.immutable.StringOps$;
import scala.Option$;
import scala.Option;
import scala.Some;
import scala.None$;
import scala.MatchError;
import scala.runtime.BoxedUnit;
import scala.util.Success;
import scala.util.Failure;
import scala.collection.Seq;
import scala.StringContext;
import scala.Predef$;
import scala.util.Try$;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.eclipse.jetty.server.Request;
import scala.Function0;
import io.pivotal.greenplum.spark.Logging$class;
import scala.runtime.TraitSetter;
import org.slf4j.Logger;
import io.pivotal.greenplum.spark.util.TransactionData;
import scala.util.Try;
import java.util.Map;
import scala.reflect.ScalaSignature;
import io.pivotal.greenplum.spark.Logging;
import org.eclipse.jetty.server.handler.AbstractHandler;

@ScalaSignature(bytes = "\u0006\u0001\u0005\u001dd\u0001B\u0001\u0003\u00015\u0011ab\u00129gI&\u001cH\u000fS1oI2,'O\u0003\u0002\u0004\t\u0005iQ\r\u001f;fe:\fG\u000e^1cY\u0016T!!\u0002\u0004\u0002\u000bM\u0004\u0018M]6\u000b\u0005\u001dA\u0011!C4sK\u0016t\u0007\u000f\\;n\u0015\tI!\"A\u0004qSZ|G/\u00197\u000b\u0003-\t!![8\u0004\u0001M\u0019\u0001A\u0004\u000f\u0011\u0005=QR\"\u0001\t\u000b\u0005E\u0011\u0012a\u00025b]\u0012dWM\u001d\u0006\u0003'Q\taa]3sm\u0016\u0014(BA\u000b\u0017\u0003\u0015QW\r\u001e;z\u0015\t9\u0002$A\u0004fG2L\u0007o]3\u000b\u0003e\t1a\u001c:h\u0013\tY\u0002CA\bBEN$(/Y2u\u0011\u0006tG\r\\3s!\tib$D\u0001\u0005\u0013\tyBAA\u0004M_\u001e<\u0017N\\4\t\u0011\u0005\u0002!\u0011!Q\u0001\n\t\n\u0011BY;gM\u0016\u0014X*\u00199\u0011\t\rB#\u0006N\u0007\u0002I)\u0011QEJ\u0001\u0005kRLGNC\u0001(\u0003\u0011Q\u0017M^1\n\u0005%\"#aA'baB\u00111&\r\b\u0003Y=j\u0011!\f\u0006\u0002]\u0005)1oY1mC&\u0011\u0001'L\u0001\u0007!J,G-\u001a4\n\u0005I\u001a$AB*ue&twM\u0003\u00021[A\u0019QgN\u001d\u000e\u0003YR!!J\u0017\n\u0005a2$a\u0001+ssB\u0011!\bP\u0007\u0002w)\u0011Q\u0005B\u0005\u0003{m\u0012q\u0002\u0016:b]N\f7\r^5p]\u0012\u000bG/\u0019\u0005\t\u007f\u0001\u0011\t\u0011)A\u0005\u0001\u0006i1/\u001a8e\u0005V4g-\u001a:NCB\u0004Ba\t\u0015+\u0003B\u0011!iQ\u0007\u0002\u0005%\u0011AI\u0001\u0002\u000e!\u0006\u0014H/\u001b;j_:$\u0015\r^1\t\u000b\u0019\u0003A\u0011A$\u0002\rqJg.\u001b;?)\rA\u0015J\u0013\t\u0003\u0005\u0002AQ!I#A\u0002\tBQaP#A\u0002\u0001CQA\u0012\u0001\u0005\u00021#\u0012\u0001\u0013\u0005\u0006\u001d\u0002!\teT\u0001\u0007Q\u0006tG\r\\3\u0015\u000bA\u001bVkW4\u0011\u00051\n\u0016B\u0001*.\u0005\u0011)f.\u001b;\t\u000bQk\u0005\u0019\u0001\u0016\u0002\tA\fG\u000f\u001b\u0005\u0006-6\u0003\raV\u0001\fE\u0006\u001cXMU3rk\u0016\u001cH\u000f\u0005\u0002Y36\t!#\u0003\u0002[%\t9!+Z9vKN$\b\"\u0002/N\u0001\u0004i\u0016a\u0002:fcV,7\u000f\u001e\t\u0003=\u0016l\u0011a\u0018\u0006\u0003A\u0006\fA\u0001\u001b;ua*\u0011!mY\u0001\bg\u0016\u0014h\u000f\\3u\u0015\u0005!\u0017!\u00026bm\u0006D\u0018B\u00014`\u0005IAE\u000f\u001e9TKJ4H.\u001a;SKF,Xm\u001d;\t\u000b!l\u0005\u0019A5\u0002\u0011I,7\u000f]8og\u0016\u0004\"A\u00186\n\u0005-|&a\u0005%uiB\u001cVM\u001d<mKR\u0014Vm\u001d9p]N,\u0007\"B7\u0001\t\u0013q\u0017!\u00035b]\u0012dWmR#U)\ry\u0007/\u001d\t\u0004k]\u0002\u0006\"\u0002+m\u0001\u0004Q\u0003\"\u00025m\u0001\u0004I\u0007\"B:\u0001\t\u0013!\u0018\u0001\u00069s_\u000e,7o\u001d)beRLG/[8o\t\u0006$\u0018\rF\u0002pkZDQ\u0001\u001b:A\u0002%DQ\u0001\u0016:A\u0002)BQ\u0001\u001f\u0001\u0005\u0002e\fAbZ3u!\u0006\u0014H/\u001b;j_:$2A_?\u007f!\ra30Q\u0005\u0003y6\u0012aa\u00149uS>t\u0007\"\u0002+x\u0001\u0004Q\u0003\"B@x\u0001\u0004\u0001\u0015aA7ba\"9\u00111\u0001\u0001\u0005\n\u0005\u0015\u0011!C:feZ,G)\u0019;b)\u0015y\u0017qAA\u0005\u0011\u0019A\u0017\u0011\u0001a\u0001S\"9\u00111BA\u0001\u0001\u0004\t\u0015!\u00049beRLG/[8o\t\u0006$\u0018\rC\u0004\u0002\u0010\u0001!I!!\u0005\u0002\u0015!\fg\u000e\u001a7f!>\u001bF\u000bF\u0004Q\u0003'\t)\"a\u0006\t\rQ\u000bi\u00011\u0001+\u0011\u0019a\u0016Q\u0002a\u0001;\"1\u0001.!\u0004A\u0002%Dq!a\u0007\u0001\t\u0013\ti\"\u0001\bs_^$vnQ*W'R\u0014\u0018N\\4\u0015\u0007)\ny\u0002\u0003\u0005\u0002\"\u0005e\u0001\u0019AA\u0012\u0003\r\u0011xn\u001e\t\u0005\u0003K\t\t$\u0004\u0002\u0002()!\u0011\u0011FA\u0016\u0003\r\u0019\u0018\u000f\u001c\u0006\u0004\u000b\u00055\"bAA\u00181\u00051\u0011\r]1dQ\u0016LA!a\r\u0002(\t\u0019!k\\<\t\u000f\u0005]\u0002\u0001\"\u0003\u0002:\u0005a\u0001O]8dKN\u001cXI\u001d:peRY\u0001+a\u000f\u0002>\u0005}\u0012\u0011IA&\u0011\u0019!\u0016Q\u0007a\u0001U!1A,!\u000eA\u0002uCa\u0001[A\u001b\u0001\u0004I\u0007\u0002CA\"\u0003k\u0001\r!!\u0012\u0002\t\r|G-\u001a\t\u0004Y\u0005\u001d\u0013bAA%[\t\u0019\u0011J\u001c;\t\u0011\u00055\u0013Q\u0007a\u0001\u0003\u001f\n\u0011!\u001a\t\u0005\u0003#\n\tG\u0004\u0003\u0002T\u0005uc\u0002BA+\u00037j!!a\u0016\u000b\u0007\u0005eC\"\u0001\u0004=e>|GOP\u0005\u0002]%\u0019\u0011qL\u0017\u0002\u000fA\f7m[1hK&!\u00111MA3\u0005%!\u0006N]8xC\ndWMC\u0002\u0002`5\u0002")
public class GpfdistHandler extends AbstractHandler implements Logging
{
    private final Map<String, Try<TransactionData>> bufferMap;
    private final Map<String, PartitionData> sendBufferMap;
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
    
    @Override
    public void handle(final String path, final Request baseRequest, final HttpServletRequest request, final HttpServletResponse response) {
        final String method = request.getMethod();
        Object o;
        if ("GET".equals(method)) {
            o = this.handleGET(path, response);
        }
        else if ("POST".equals(method)) {
            o = Try$.MODULE$.apply((Function0)new GpfdistHandler$$anonfun.GpfdistHandler$$anonfun$1(this, path, request, response));
        }
        else {
            o = new Failure((Throwable)new WebException(405, new StringContext((Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[] { "Method ", " is not supported" })).s((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { request.getMethod() })), WebException$.MODULE$.apply$default$3()));
        }
        final Object o2;
        final Try result = (Try)(o2 = o);
        if (o2 instanceof Success) {
            response.setStatus(200);
            if (this.log().isDebugEnabled()) {
                this.log().debug(new StringContext((Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[] { "Successfully handled ", " request for ", "" })).s((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { request.getMethod(), path })));
                final BoxedUnit boxedUnit = BoxedUnit.UNIT;
            }
            else {
                final BoxedUnit boxedUnit = BoxedUnit.UNIT;
            }
        }
        else {
            if (!(o2 instanceof Failure)) {
                throw new MatchError(o2);
            }
            final Throwable exception;
            final Throwable e = exception = ((Failure)o2).exception();
            int code2;
            if (exception instanceof WebException) {
                final int c = code2 = ((WebException)exception).code();
            }
            else {
                code2 = 500;
            }
            final int code = code2;
            if (this.log().isDebugEnabled()) {
                e.printStackTrace();
            }
            this.processError(path, request, response, code, e);
            final BoxedUnit unit = BoxedUnit.UNIT;
        }
        baseRequest.setHandled(true);
    }
    
    private Try<BoxedUnit> handleGET(final String path, final HttpServletResponse response) {
        if (this.log().isDebugEnabled()) {
            this.log().debug(new StringContext((Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[] { "Processing GET for ", "" })).s((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { path })));
        }
        return this.processPartitionData(response, path);
    }
    
    private Try<BoxedUnit> processPartitionData(final HttpServletResponse response, final String path) {
        final Option<PartitionData> partition = this.getPartition(path, this.sendBufferMap);
        Object o;
        if (None$.MODULE$.equals(partition)) {
            o = new Failure((Throwable)new WebException(400, new StringContext((Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[] { "no data available for ", "" })).s((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { path })), WebException$.MODULE$.apply$default$3()));
        }
        else {
            if (!(partition instanceof Some)) {
                throw new MatchError((Object)partition);
            }
            final PartitionData partitionData = (PartitionData)((Some)partition).x();
            o = (partitionData.handled().compareAndSet(false, true) ? this.serveData(response, partitionData) : new Success((Object)BoxedUnit.UNIT));
        }
        return (Try<BoxedUnit>)o;
    }
    
    public Option<PartitionData> getPartition(final String path, final Map<String, PartitionData> map) {
        Object o;
        if (path == null || "".equals(path) || "/".equals(path)) {
            o = None$.MODULE$;
        }
        else {
            o = Option$.MODULE$.apply((Object)map.get(StringOps$.MODULE$.slice$extension(Predef$.MODULE$.augmentString(path), 1, path.length())));
        }
        return (Option<PartitionData>)o;
    }
    
    private Try<BoxedUnit> serveData(final HttpServletResponse response, final PartitionData partitionData) {
        return (Try<BoxedUnit>)Try$.MODULE$.apply((Function0)new GpfdistHandler$$anonfun$serveData.GpfdistHandler$$anonfun$serveData$1(this, response, partitionData));
    }
    
    public void io$pivotal$greenplum$spark$externaltable$GpfdistHandler$$handlePOST(final String path, final HttpServletRequest request, final HttpServletResponse response) {
        if (this.log().isDebugEnabled()) {
            this.log().debug(new StringContext((Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[] { "Processing POST for ", "" })).s((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { path })));
        }
        boolean b = false;
        GpfdistPostRequest.ValidRequest validRequest = null;
        final GpfdistPostRequest.RequestValidation validate = GpfdistPostRequest$.MODULE$.validate(request);
        if (validate instanceof GpfdistPostRequest.InvalidRequest) {
            final GpfdistPostRequest.InvalidRequest invalidRequest = (GpfdistPostRequest.InvalidRequest)validate;
            final String errorMessage = invalidRequest.message();
            final int statusCode = invalidRequest.statusCode();
            throw new WebException(statusCode, errorMessage, WebException$.MODULE$.apply$default$3());
        }
        if (validate instanceof GpfdistPostRequest.ValidRequest) {
            b = true;
            validRequest = (GpfdistPostRequest.ValidRequest)validate;
            if (0 == validRequest.contentLength()) {
                final BoxedUnit unit = BoxedUnit.UNIT;
                return;
            }
        }
        if (!b) {
            throw new MatchError((Object)validate);
        }
        final int contentLength = validRequest.contentLength();
        final String transactionId = validRequest.transactionId();
        final int segmentId = validRequest.segmentId();
        final Option apply = Option$.MODULE$.apply((Object)request.getInputStream());
        if (None$.MODULE$.equals(apply)) {
            this.log().warn("Received POST request with no body");
            throw new WebException(400, WebException$.MODULE$.apply$default$2(), WebException$.MODULE$.apply$default$3());
        }
        if (!(apply instanceof Some)) {
            throw new MatchError((Object)apply);
        }
        final ServletInputStream requestInputStream = (ServletInputStream)((Some)apply).x();
        this.bufferMap.putIfAbsent(transactionId, (Try<TransactionData>)new Success((Object)new TransactionData()));
        final Try<TransactionData> try1 = this.bufferMap.get(transactionId);
        if (try1 instanceof Failure) {
            throw new WebException(500, WebException$.MODULE$.apply$default$2(), WebException$.MODULE$.apply$default$3());
        }
        if (!(try1 instanceof Success)) {
            throw new MatchError((Object)try1);
        }
        final TransactionData txData = (TransactionData)((Success)try1).value();
        final Try<BoxedUnit> write = txData.write(segmentId, requestInputStream, contentLength);
        if (write instanceof Failure) {
            final Throwable exception = ((Failure)write).exception();
            this.bufferMap.put(transactionId, (Try<TransactionData>)new Failure(exception));
            throw new WebException(400, exception.getMessage(), WebException$.MODULE$.apply$default$3());
        }
        final BoxedUnit unit2 = BoxedUnit.UNIT;
        final BoxedUnit unit3 = BoxedUnit.UNIT;
        final BoxedUnit unit4 = BoxedUnit.UNIT;
        final BoxedUnit unit5 = BoxedUnit.UNIT;
    }
    
    public String io$pivotal$greenplum$spark$externaltable$GpfdistHandler$$rowToCSVString(final Row row) {
        return ((TraversableOnce)row.toSeq().map((Function1)new GpfdistHandler$$anonfun$io$pivotal$greenplum$spark$externaltable$GpfdistHandler$$rowToCSVString.GpfdistHandler$$anonfun$io$pivotal$greenplum$spark$externaltable$GpfdistHandler$$rowToCSVString$1(this), Seq$.MODULE$.canBuildFrom())).mkString(BoxesRunTime.boxToCharacter(GreenplumCSVFormat$.MODULE$.CHAR_DELIMITER()).toString());
    }
    
    private void processError(final String path, final HttpServletRequest request, final HttpServletResponse response, final int code, final Throwable e) {
        this.log().error(new StringContext((Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[] { "Failed to handle ", " request for ", " : ", "" })).s((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { request.getMethod(), path, e })));
        response.setStatus(code);
        if (e != null && e.getMessage() != null) {
            response.getWriter().write(e.getMessage());
        }
    }
    
    public GpfdistHandler(final Map<String, Try<TransactionData>> bufferMap, final Map<String, PartitionData> sendBufferMap) {
        this.bufferMap = bufferMap;
        this.sendBufferMap = sendBufferMap;
        Logging$class.$init$(this);
    }
    
    public GpfdistHandler() {
        this(new ConcurrentHashMap<String, Try<TransactionData>>(), new ConcurrentHashMap<String, PartitionData>());
    }
}
