// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

import scala.reflect.ScalaSignature;

@ScalaSignature(bytes = "\u0006\u0001A2q!\u0001\u0002\u0011\u0002\u0007\u0005qA\u0001\fEs:\fW.[2K-\u0006dW/Z%na2L7-\u001b;t\u0015\t\u0019A!\u0001\u0004kg>tGg\u001d\u0006\u0002\u000b\u0005\u0019qN]4\u0004\u0001M\u0011\u0001\u0001\u0003\t\u0003\u00131i\u0011A\u0003\u0006\u0002\u0017\u0005)1oY1mC&\u0011QB\u0003\u0002\u0007\u0003:L(+\u001a4\t\u000b=\u0001A\u0011\u0001\t\u0002\r\u0011Jg.\u001b;%)\u0005\t\u0002CA\u0005\u0013\u0013\t\u0019\"B\u0001\u0003V]&$\b\"B\u000b\u0001\t\u00071\u0012A\u00033z]\u0006l\u0017n\u0019\u001aKmR\u0011qc\b\t\u00031qq!!\u0007\u000e\u000e\u0003\tI!a\u0007\u0002\u0002\u000fA\f7m[1hK&\u0011QD\b\u0002\u0007\u0015Z\u000bG.^3\u000b\u0005m\u0011\u0001\"\u0002\u0011\u0015\u0001\u0004\t\u0013!\u00023z]*3\bCA\r#\u0013\t\u0019#AA\u0007Es:\fW.[2K-\u0006dW/\u001a\u0005\u0006K\u0001!\u0019AJ\u0001\u0010Ift\u0017-\\5de5|g.\u00193jGR\u0011qE\u000b\t\u00033!J!!\u000b\u0002\u0003\u001b5{g.\u00193jG*3\u0016\r\\;f\u0011\u0015\u0001C\u00051\u0001\"\u0011\u0015a\u0003\u0001\"\u0001.\u0003\r!\u0017P\u001c\u000b\u0003C9BQaL\u0016A\u0002]\t!A\u001b<")
public interface DynamicJValueImplicits
{
    JsonAST.JValue dynamic2Jv(final DynamicJValue p0);
    
    MonadicJValue dynamic2monadic(final DynamicJValue p0);
    
    DynamicJValue dyn(final JsonAST.JValue p0);
}
