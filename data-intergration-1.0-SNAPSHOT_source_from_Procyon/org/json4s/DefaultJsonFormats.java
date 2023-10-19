// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

import scala.reflect.ScalaSignature;

@ScalaSignature(bytes = "\u0006\u0001q:Q!\u0001\u0002\t\u0002\u001d\t!\u0003R3gCVdGOS:p]\u001a{'/\\1ug*\u00111\u0001B\u0001\u0007UN|g\u000eN:\u000b\u0003\u0015\t1a\u001c:h\u0007\u0001\u0001\"\u0001C\u0005\u000e\u0003\t1QA\u0003\u0002\t\u0002-\u0011!\u0003R3gCVdGOS:p]\u001a{'/\\1ugN\u0019\u0011\u0002\u0004\n\u0011\u00055\u0001R\"\u0001\b\u000b\u0003=\tQa]2bY\u0006L!!\u0005\b\u0003\r\u0005s\u0017PU3g!\tA1#\u0003\u0002\u0015\u0005\t\tBi\\;cY\u0016T5o\u001c8G_Jl\u0017\r^:\t\u000bYIA\u0011A\f\u0002\rqJg.\u001b;?)\u00059aa\u0002\u0006\u0003!\u0003\r\t!G\n\u000311AQa\u0007\r\u0005\u0002q\ta\u0001J5oSR$C#A\u000f\u0011\u00055q\u0012BA\u0010\u000f\u0005\u0011)f.\u001b;\t\u000b\u0005BB1\u0001\u0012\u0002\u001b\u001d+g.\u001a:jG\u001a{'/\\1u+\t\u0019\u0013\u0006F\u0002%e]\u00022\u0001C\u0013(\u0013\t1#A\u0001\u0006Kg>tgi\u001c:nCR\u0004\"\u0001K\u0015\r\u0001\u0011)!\u0006\tb\u0001W\t\tA+\u0005\u0002-_A\u0011Q\"L\u0005\u0003]9\u0011qAT8uQ&tw\r\u0005\u0002\u000ea%\u0011\u0011G\u0004\u0002\u0004\u0003:L\b\"B\u001a!\u0001\b!\u0014A\u0002:fC\u0012,'\u000fE\u0002\tk\u001dJ!A\u000e\u0002\u0003\rI+\u0017\rZ3s\u0011\u0015A\u0004\u0005q\u0001:\u0003\u00199(/\u001b;feB\u0019\u0001BO\u0014\n\u0005m\u0012!AB,sSR,'\u000f")
public interface DefaultJsonFormats
{
     <T> JsonFormat<T> GenericFormat(final Reader<T> p0, final Writer<T> p1);
}
