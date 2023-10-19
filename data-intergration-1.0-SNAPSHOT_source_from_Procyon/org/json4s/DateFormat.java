// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

import java.util.TimeZone;
import java.util.Date;
import scala.Option;
import scala.reflect.ScalaSignature;

@ScalaSignature(bytes = "\u0006\u0001A2q!\u0001\u0002\u0011\u0002G\u0005qA\u0001\u0006ECR,gi\u001c:nCRT!a\u0001\u0003\u0002\r)\u001cxN\u001c\u001bt\u0015\u0005)\u0011aA8sO\u000e\u00011C\u0001\u0001\t!\tIA\"D\u0001\u000b\u0015\u0005Y\u0011!B:dC2\f\u0017BA\u0007\u000b\u0005\u0019\te.\u001f*fM\")q\u0002\u0001D\u0001!\u0005)\u0001/\u0019:tKR\u0011\u0011\u0003\b\t\u0004\u0013I!\u0012BA\n\u000b\u0005\u0019y\u0005\u000f^5p]B\u0011QCG\u0007\u0002-)\u0011q\u0003G\u0001\u0005kRLGNC\u0001\u001a\u0003\u0011Q\u0017M^1\n\u0005m1\"\u0001\u0002#bi\u0016DQ!\b\bA\u0002y\t\u0011a\u001d\t\u0003?\tr!!\u0003\u0011\n\u0005\u0005R\u0011A\u0002)sK\u0012,g-\u0003\u0002$I\t11\u000b\u001e:j]\u001eT!!\t\u0006\t\u000b\u0019\u0002a\u0011A\u0014\u0002\r\u0019|'/\\1u)\tq\u0002\u0006C\u0003*K\u0001\u0007A#A\u0001e\u0011\u0015Y\u0003A\"\u0001-\u0003!!\u0018.\\3{_:,W#A\u0017\u0011\u0005Uq\u0013BA\u0018\u0017\u0005!!\u0016.\\3[_:,\u0007")
public interface DateFormat
{
    Option<Date> parse(final String p0);
    
    String format(final Date p0);
    
    TimeZone timezone();
}
