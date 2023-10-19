// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

import scala.math.BigDecimal;
import scala.math.BigInt;
import scala.reflect.ScalaSignature;

@ScalaSignature(bytes = "\u0006\u0001]4q!\u0001\u0002\u0011\u0002\u0007\u0005qAA\u0005J[Bd\u0017nY5ug*\u00111\u0001B\u0001\u0007UN|g\u000eN:\u000b\u0003\u0015\t1a\u001c:h\u0007\u0001\u0019\"\u0001\u0001\u0005\u0011\u0005%aQ\"\u0001\u0006\u000b\u0003-\tQa]2bY\u0006L!!\u0004\u0006\u0003\r\u0005s\u0017PU3g\u0011\u0015y\u0001\u0001\"\u0001\u0011\u0003\u0019!\u0013N\\5uIQ\t\u0011\u0003\u0005\u0002\n%%\u00111C\u0003\u0002\u0005+:LG\u000fC\u0003\u0016\u0001\u0011\ra#\u0001\u0007tQ>\u0014HO\r6wC2,X\r\u0006\u0002\u0018?A\u0011\u0001\u0004\b\b\u00033ii\u0011AA\u0005\u00037\t\tqAS:p]\u0006\u001bF+\u0003\u0002\u001e=\t1!JV1mk\u0016T!a\u0007\u0002\t\u000b\u0001\"\u0002\u0019A\u0011\u0002\u0003a\u0004\"!\u0003\u0012\n\u0005\rR!!B*i_J$\b\"B\u0013\u0001\t\u00071\u0013a\u00032zi\u0016\u0014$N^1mk\u0016$\"aF\u0014\t\u000b\u0001\"\u0003\u0019\u0001\u0015\u0011\u0005%I\u0013B\u0001\u0016\u000b\u0005\u0011\u0011\u0015\u0010^3\t\u000b1\u0002A1A\u0017\u0002\u0017\rD\u0017M\u001d\u001akm\u0006dW/\u001a\u000b\u0003/9BQ\u0001I\u0016A\u0002=\u0002\"!\u0003\u0019\n\u0005ER!\u0001B\"iCJDQa\r\u0001\u0005\u0004Q\n!\"\u001b8ue)4\u0018\r\\;f)\t9R\u0007C\u0003!e\u0001\u0007a\u0007\u0005\u0002\no%\u0011\u0001H\u0003\u0002\u0004\u0013:$\b\"\u0002\u001e\u0001\t\u0007Y\u0014a\u00037p]\u001e\u0014$N^1mk\u0016$\"a\u0006\u001f\t\u000b\u0001J\u0004\u0019A\u001f\u0011\u0005%q\u0014BA \u000b\u0005\u0011auN\\4\t\u000b\u0005\u0003A1\u0001\"\u0002\u001b\tLw-\u001b8ue)4\u0018\r\\;f)\t92\tC\u0003!\u0001\u0002\u0007A\t\u0005\u0002F\u001b:\u0011ai\u0013\b\u0003\u000f*k\u0011\u0001\u0013\u0006\u0003\u0013\u001a\ta\u0001\u0010:p_Rt\u0014\"A\u0006\n\u00051S\u0011a\u00029bG.\fw-Z\u0005\u0003\u001d>\u0013aAQ5h\u0013:$(B\u0001'\u000b\u0011\u0015\t\u0006Ab\u0001S\u00035!w.\u001e2mKJRg/\u00197vKR\u0011qc\u0015\u0005\u0006AA\u0003\r\u0001\u0016\t\u0003\u0013UK!A\u0016\u0006\u0003\r\u0011{WO\u00197f\u0011\u0015A\u0006Ab\u0001Z\u000311Gn\\1ue)4\u0018\r\\;f)\t9\"\fC\u0003!/\u0002\u00071\f\u0005\u0002\n9&\u0011QL\u0003\u0002\u0006\r2|\u0017\r\u001e\u0005\u0006?\u00021\u0019\u0001Y\u0001\u0012E&<G-Z2j[\u0006d'G\u001b<bYV,GCA\fb\u0011\u0015\u0001c\f1\u0001c!\t)5-\u0003\u0002e\u001f\nQ!)[4EK\u000eLW.\u00197\t\u000b\u0019\u0004A1A4\u0002\u001d\t|w\u000e\\3b]JRg/\u00197vKR\u0011q\u0003\u001b\u0005\u0006A\u0015\u0004\r!\u001b\t\u0003\u0013)L!a\u001b\u0006\u0003\u000f\t{w\u000e\\3b]\")Q\u000e\u0001C\u0002]\u0006i1\u000f\u001e:j]\u001e\u0014$N^1mk\u0016$\"aF8\t\u000b\u0001b\u0007\u0019\u00019\u0011\u0005E$hBA\u0005s\u0013\t\u0019(\"\u0001\u0004Qe\u0016$WMZ\u0005\u0003kZ\u0014aa\u0015;sS:<'BA:\u000b\u0001")
public interface Implicits
{
    JsonAST.JValue short2jvalue(final short p0);
    
    JsonAST.JValue byte2jvalue(final byte p0);
    
    JsonAST.JValue char2jvalue(final char p0);
    
    JsonAST.JValue int2jvalue(final int p0);
    
    JsonAST.JValue long2jvalue(final long p0);
    
    JsonAST.JValue bigint2jvalue(final BigInt p0);
    
    JsonAST.JValue double2jvalue(final double p0);
    
    JsonAST.JValue float2jvalue(final float p0);
    
    JsonAST.JValue bigdecimal2jvalue(final BigDecimal p0);
    
    JsonAST.JValue boolean2jvalue(final boolean p0);
    
    JsonAST.JValue string2jvalue(final String p0);
}
