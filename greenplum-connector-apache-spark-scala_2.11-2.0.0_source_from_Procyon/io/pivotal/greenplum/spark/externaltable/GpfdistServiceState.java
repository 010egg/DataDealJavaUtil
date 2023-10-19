// 
// Decompiled by Procyon v0.5.36
// 

package io.pivotal.greenplum.spark.externaltable;

import scala.Enumeration;
import scala.reflect.ScalaSignature;

@ScalaSignature(bytes = "\u0006\u0001i:Q!\u0001\u0002\t\u00025\t1c\u00129gI&\u001cHoU3sm&\u001cWm\u0015;bi\u0016T!a\u0001\u0003\u0002\u001b\u0015DH/\u001a:oC2$\u0018M\u00197f\u0015\t)a!A\u0003ta\u0006\u00148N\u0003\u0002\b\u0011\u0005IqM]3f]BdW/\u001c\u0006\u0003\u0013)\tq\u0001]5w_R\fGNC\u0001\f\u0003\tIwn\u0001\u0001\u0011\u00059yQ\"\u0001\u0002\u0007\u000bA\u0011\u0001\u0012A\t\u0003'\u001d\u0003h\rZ5tiN+'O^5dKN#\u0018\r^3\u0014\u0005=\u0011\u0002CA\n\u0017\u001b\u0005!\"\"A\u000b\u0002\u000bM\u001c\u0017\r\\1\n\u0005]!\"aC#ok6,'/\u0019;j_:DQ!G\b\u0005\u0002i\ta\u0001P5oSRtD#A\u0007\u0006\tqy\u0001!\b\u0002\u0006'R\fG/\u001a\t\u0003=}i\u0011aD\u0005\u0003AY\u0011QAV1mk\u0016DqAI\bC\u0002\u0013\u00051%A\u0004Ti>\u0004\b/\u001a3\u0016\u0003uAa!J\b!\u0002\u0013i\u0012\u0001C*u_B\u0004X\r\u001a\u0011\t\u000f\u001dz!\u0019!C\u0001G\u00051a)Y5mK\u0012Da!K\b!\u0002\u0013i\u0012a\u0002$bS2,G\r\t\u0005\bW=\u0011\r\u0011\"\u0001$\u0003!\u0019F/\u0019:uS:<\u0007BB\u0017\u0010A\u0003%Q$A\u0005Ti\u0006\u0014H/\u001b8hA!9qf\u0004b\u0001\n\u0003\u0019\u0013aB*uCJ$X\r\u001a\u0005\u0007c=\u0001\u000b\u0011B\u000f\u0002\u0011M#\u0018M\u001d;fI\u0002BqaM\bC\u0002\u0013\u00051%\u0001\u0005Ti>\u0004\b/\u001b8h\u0011\u0019)t\u0002)A\u0005;\u0005I1\u000b^8qa&tw\r\t\u0005\bo=\u0011\r\u0011\"\u0001$\u0003\u001d\u0011VO\u001c8j]\u001eDa!O\b!\u0002\u0013i\u0012\u0001\u0003*v]:Lgn\u001a\u0011")
public final class GpfdistServiceState
{
    public static Enumeration.Value Running() {
        return GpfdistServiceState$.MODULE$.Running();
    }
    
    public static Enumeration.Value Stopping() {
        return GpfdistServiceState$.MODULE$.Stopping();
    }
    
    public static Enumeration.Value Started() {
        return GpfdistServiceState$.MODULE$.Started();
    }
    
    public static Enumeration.Value Starting() {
        return GpfdistServiceState$.MODULE$.Starting();
    }
    
    public static Enumeration.Value Failed() {
        return GpfdistServiceState$.MODULE$.Failed();
    }
    
    public static Enumeration.Value Stopped() {
        return GpfdistServiceState$.MODULE$.Stopped();
    }
    
    public static Enumeration.Value withName(final String s) {
        return GpfdistServiceState$.MODULE$.withName(s);
    }
    
    public static Enumeration.Value apply(final int n) {
        return GpfdistServiceState$.MODULE$.apply(n);
    }
    
    public static int maxId() {
        return GpfdistServiceState$.MODULE$.maxId();
    }
    
    public static Enumeration.ValueSet values() {
        return GpfdistServiceState$.MODULE$.values();
    }
    
    public static String toString() {
        return GpfdistServiceState$.MODULE$.toString();
    }
}
