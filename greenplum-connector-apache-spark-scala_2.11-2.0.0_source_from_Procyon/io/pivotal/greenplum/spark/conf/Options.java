// 
// Decompiled by Procyon v0.5.36
// 

package io.pivotal.greenplum.spark.conf;

import scala.Option;
import scala.util.Try;
import scala.Function2;
import scala.collection.immutable.Map;
import scala.reflect.ScalaSignature;

@ScalaSignature(bytes = "\u0006\u0001e4q!\u0001\u0002\u0011\u0002\u0007\u0005QBA\u0004PaRLwN\\:\u000b\u0005\r!\u0011\u0001B2p]\u001aT!!\u0002\u0004\u0002\u000bM\u0004\u0018M]6\u000b\u0005\u001dA\u0011!C4sK\u0016t\u0007\u000f\\;n\u0015\tI!\"A\u0004qSZ|G/\u00197\u000b\u0003-\t!![8\u0004\u0001M\u0011\u0001A\u0004\t\u0003\u001fIi\u0011\u0001\u0005\u0006\u0002#\u0005)1oY1mC&\u00111\u0003\u0005\u0002\u0007\u0003:L(+\u001a4\t\u000bU\u0001A\u0011\u0001\f\u0002\r\u0011Jg.\u001b;%)\u00059\u0002CA\b\u0019\u0013\tI\u0002C\u0001\u0003V]&$\b\"B\u000e\u0001\r#a\u0012A\u00039be\u0006lW\r^3sgV\tQ\u0004\u0005\u0003\u001fC\u0011\"cBA\b \u0013\t\u0001\u0003#\u0001\u0004Qe\u0016$WMZ\u0005\u0003E\r\u00121!T1q\u0015\t\u0001\u0003\u0003\u0005\u0002\u001fK%\u0011ae\t\u0002\u0007'R\u0014\u0018N\\4\t\u000b!\u0002A\u0011A\u0015\u0002\r=\u0004H/[8o)\r!#\u0006\f\u0005\u0006W\u001d\u0002\r\u0001J\u0001\u000b_B$\u0018n\u001c8OC6,\u0007\"B\u0017(\u0001\u0004q\u0013!D<iCRLe-T5tg&tw\r\u0005\u00020a5\t!!\u0003\u00022\u0005\tiq\u000b[1u\u0013\u001al\u0015n]:j]\u001eDQ\u0001\u000b\u0001\u0005\u0002M*\"\u0001N\u001c\u0015\tU\u0002\u0015I\u0011\t\u0003m]b\u0001\u0001B\u00039e\t\u0007\u0011HA\u0001U#\tQT\b\u0005\u0002\u0010w%\u0011A\b\u0005\u0002\b\u001d>$\b.\u001b8h!\tya(\u0003\u0002@!\t\u0019\u0011I\\=\t\u000b-\u0012\u0004\u0019\u0001\u0013\t\u000b5\u0012\u0004\u0019\u0001\u0018\t\u000b\r\u0013\u0004\u0019\u0001#\u0002\u000f\r|gN^3siB)q\"\u0012\u0013%\u000f&\u0011a\t\u0005\u0002\n\rVt7\r^5p]J\u00022\u0001S&6\u001b\u0005I%B\u0001&\u0011\u0003\u0011)H/\u001b7\n\u00051K%a\u0001+ss\")\u0001\u0006\u0001C\u0001\u001dR\u0011qJ\u0015\t\u0004\u001fA#\u0013BA)\u0011\u0005\u0019y\u0005\u000f^5p]\")1&\u0014a\u0001I!)\u0001\u0006\u0001C\u0001)V\u0011Q\u000b\u0017\u000b\u0004-fS\u0006cA\bQ/B\u0011a\u0007\u0017\u0003\u0006qM\u0013\r!\u000f\u0005\u0006WM\u0003\r\u0001\n\u0005\u0006\u0007N\u0003\ra\u0017\t\u0006\u001f\u0015#C\u0005\u0018\t\u0004\u0011.;\u0006\"\u00020\u0001\t\u0003y\u0016\u0001\u00022p_2,\u0012\u0001\u0019\t\u0006\u001f\u0015#C%\u0019\t\u0004\u0011.\u0013\u0007CA\bd\u0013\t!\u0007CA\u0004C_>dW-\u00198\t\u000b\u0019\u0004A\u0011A4\u0002\u00179\fG/\u001e:bY2{gnZ\u000b\u0002QB)q\"\u0012\u0013%SB\u0019\u0001j\u00136\u0011\u0005=Y\u0017B\u00017\u0011\u0005\u0011auN\\4\t\u000b9\u0004A\u0011A8\u0002\u0007%tG/F\u0001q!\u0015yQ\t\n\u0013r!\rA5J\u001d\t\u0003\u001fML!\u0001\u001e\t\u0003\u0007%sG\u000fC\u0003w\u0001\u0011\u0005q.A\u0006q_NLG/\u001b<f\u0013:$\b\"\u0002=\u0001\t\u0003y\u0017A\u00048p]:+w-\u0019;jm\u0016Le\u000e\u001e")
public interface Options
{
    Map<String, String> parameters();
    
    String option(final String p0, final WhatIfMissing p1);
    
     <T> T option(final String p0, final WhatIfMissing p1, final Function2<String, String, Try<T>> p2);
    
    Option<String> option(final String p0);
    
     <T> Option<T> option(final String p0, final Function2<String, String, Try<T>> p1);
    
    Function2<String, String, Try<Object>> bool();
    
    Function2<String, String, Try<Object>> naturalLong();
    
    Function2<String, String, Try<Object>> int();
    
    Function2<String, String, Try<Object>> positiveInt();
    
    Function2<String, String, Try<Object>> nonNegativeInt();
}
