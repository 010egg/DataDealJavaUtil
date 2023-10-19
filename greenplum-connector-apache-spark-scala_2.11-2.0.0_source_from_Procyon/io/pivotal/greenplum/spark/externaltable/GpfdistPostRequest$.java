// 
// Decompiled by Procyon v0.5.36
// 

package io.pivotal.greenplum.spark.externaltable;

import scala.collection.Seq;
import scala.collection.immutable.List$;
import scala.runtime.NonLocalReturnControl;
import scala.collection.immutable.StringOps;
import scala.Predef$;
import scala.Function1;
import javax.servlet.http.HttpServletRequest;
import scala.collection.immutable.List;

public final class GpfdistPostRequest$
{
    public static final GpfdistPostRequest$ MODULE$;
    private final String DISTRIBUTED_TRANSACTION_ID_HEADER;
    private final String SEGMENT_ID_HEADER;
    private final String SEGMENT_COUNT_HEADER;
    private final List<String> requiredHeaders;
    
    static {
        new GpfdistPostRequest$();
    }
    
    public GpfdistPostRequest.RequestValidation validate(final HttpServletRequest request) {
        final Object o = new Object();
        GpfdistPostRequest.RequestValidation requestValidation;
        try {
            if (request.getContentLength() < 0) {
                return new GpfdistPostRequest.InvalidRequest("Size of request is too large", 413);
            }
            this.requiredHeaders().foreach((Function1)new GpfdistPostRequest$$anonfun$validate.GpfdistPostRequest$$anonfun$validate$1(request, o));
            final int contentLength;
            final String header;
            final StringOps stringOps;
            requestValidation = new GpfdistPostRequest.ValidRequest(contentLength, header, stringOps.toInt());
            contentLength = request.getContentLength();
            header = request.getHeader(this.DISTRIBUTED_TRANSACTION_ID_HEADER());
            stringOps = new StringOps(Predef$.MODULE$.augmentString(request.getHeader(this.SEGMENT_ID_HEADER())));
        }
        catch (NonLocalReturnControl nonLocalReturnControl) {
            if (nonLocalReturnControl.key() != o) {
                throw nonLocalReturnControl;
            }
            requestValidation = (GpfdistPostRequest.RequestValidation)nonLocalReturnControl.value();
        }
        return requestValidation;
    }
    
    public String DISTRIBUTED_TRANSACTION_ID_HEADER() {
        return this.DISTRIBUTED_TRANSACTION_ID_HEADER;
    }
    
    public String SEGMENT_ID_HEADER() {
        return this.SEGMENT_ID_HEADER;
    }
    
    public String SEGMENT_COUNT_HEADER() {
        return this.SEGMENT_COUNT_HEADER;
    }
    
    private List<String> requiredHeaders() {
        return this.requiredHeaders;
    }
    
    private GpfdistPostRequest$() {
        MODULE$ = this;
        this.DISTRIBUTED_TRANSACTION_ID_HEADER = "X-GP-XID";
        this.SEGMENT_ID_HEADER = "X-GP-SEGMENT-ID";
        this.SEGMENT_COUNT_HEADER = "X-GP-SEGMENT-COUNT";
        this.requiredHeaders = (List<String>)List$.MODULE$.apply((Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[] { this.DISTRIBUTED_TRANSACTION_ID_HEADER(), this.SEGMENT_ID_HEADER() }));
    }
}
