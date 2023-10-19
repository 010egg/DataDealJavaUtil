// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.reflect;

import scala.StringContext;
import scala.Predef$;
import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import scala.MatchError;
import org.json4s.package;
import scala.reflect.ClassTag$;
import scala.Function1;
import scala.collection.Seq$;
import scala.collection.TraversableOnce;
import scala.collection.Seq;
import java.lang.reflect.Type;
import scala.None$;
import scala.Some;
import scala.Option;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import scala.reflect.ScalaSignature;

@ScalaSignature(bytes = "\u0006\u0001\u0005Ec\u0001B\u0001\u0003\u0001%\u0011!\"\u0012=fGV$\u0018M\u00197f\u0015\t\u0019A!A\u0004sK\u001adWm\u0019;\u000b\u0005\u00151\u0011A\u00026t_:$4OC\u0001\b\u0003\ry'oZ\u0002\u0001'\t\u0001!\u0002\u0005\u0002\f\u001d5\tABC\u0001\u000e\u0003\u0015\u00198-\u00197b\u0013\tyAB\u0001\u0004B]f\u0014VM\u001a\u0005\t#\u0001\u0011)\u0019!C\u0001%\u00051Q.\u001a;i_\u0012,\u0012a\u0005\t\u0003)ii\u0011!\u0006\u0006\u0003\u0007YQ!a\u0006\r\u0002\t1\fgn\u001a\u0006\u00023\u0005!!.\u0019<b\u0013\tYRC\u0001\u0004NKRDw\u000e\u001a\u0005\t;\u0001\u0011\t\u0011)A\u0005'\u00059Q.\u001a;i_\u0012\u0004\u0003\u0002C\u0010\u0001\u0005\u000b\u0007I\u0011\u0001\u0011\u0002\u0017\r|gn\u001d;sk\u000e$xN]\u000b\u0002CA\u0012!e\n\t\u0004)\r*\u0013B\u0001\u0013\u0016\u0005-\u0019uN\\:ueV\u001cGo\u001c:\u0011\u0005\u0019:C\u0002\u0001\u0003\nQ%\n\t\u0011!A\u0003\u0002=\u00121a\u0018\u00132\u0011!Q\u0003A!A!\u0002\u0013Y\u0013\u0001D2p]N$(/^2u_J\u0004\u0003G\u0001\u0017/!\r!2%\f\t\u0003M9\"\u0011\u0002K\u0015\u0002\u0002\u0003\u0005)\u0011A\u0018\u0012\u0005A\u001a\u0004CA\u00062\u0013\t\u0011DBA\u0004O_RD\u0017N\\4\u0011\u0005-!\u0014BA\u001b\r\u0005\r\te.\u001f\u0005\to\u0001\u0011\t\u0011)A\u0005q\u0005i\u0011n\u001d)sS6\f'/_\"u_J\u0004\"aC\u001d\n\u0005ib!a\u0002\"p_2,\u0017M\u001c\u0005\u0006y\u0001!I!P\u0001\u0007y%t\u0017\u000e\u001e \u0015\ty\u0002\u0015I\u0012\t\u0003\u007f\u0001i\u0011A\u0001\u0005\u0006#m\u0002\ra\u0005\u0005\u0006?m\u0002\rA\u0011\u0019\u0003\u0007\u0016\u00032\u0001F\u0012E!\t1S\tB\u0005)\u0003\u0006\u0005\t\u0011!B\u0001_!)qg\u000fa\u0001q!)A\b\u0001C\u0001\u0011R\u0011a(\u0013\u0005\u0006#\u001d\u0003\ra\u0005\u0005\u0006y\u0001!\ta\u0013\u000b\u0004}1\u0013\u0006\"B\u0010K\u0001\u0004i\u0005G\u0001(Q!\r!2e\u0014\t\u0003MA#\u0011\"\u0015'\u0002\u0002\u0003\u0005)\u0011A\u0018\u0003\u0007}##\u0007C\u00038\u0015\u0002\u0007\u0001\bC\u0003U\u0001\u0011\u0005Q+A\neK\u001a\fW\u000f\u001c;WC2,X\rU1ui\u0016\u0014h.F\u0001W!\rYq+W\u0005\u000312\u0011aa\u00149uS>t\u0007C\u0001.^\u001d\tY1,\u0003\u0002]\u0019\u00051\u0001K]3eK\u001aL!AX0\u0003\rM#(/\u001b8h\u0015\taF\u0002C\u0003b\u0001\u0011\u0005!-\u0001\u0007hKRlu\u000eZ5gS\u0016\u00148\u000fF\u0001d!\tYA-\u0003\u0002f\u0019\t\u0019\u0011J\u001c;\t\u000b\u001d\u0004A\u0011\u00015\u00021\u001d,GoR3oKJL7\rU1sC6,G/\u001a:UsB,7\u000fF\u0001j!\rY!\u000e\\\u0005\u0003W2\u0011Q!\u0011:sCf\u0004\"\u0001F7\n\u00059,\"\u0001\u0002+za\u0016DQ\u0001\u001d\u0001\u0005\u0002E\f\u0011cZ3u!\u0006\u0014\u0018-\\3uKJ$\u0016\u0010]3t)\u0005\u0011\bcA\u0006kgB\u0012A\u000f\u001f\t\u00045V<\u0018B\u0001<`\u0005\u0015\u0019E.Y:t!\t1\u0003\u0010B\u0005z_\u0006\u0005\t\u0011!B\u0001_\t\u0019q\fJ\u001a\t\u000bm\u0004A\u0011\u0001?\u0002#\u001d,G\u000fR3dY\u0006\u0014\u0018N\\4DY\u0006\u001c8\u000fF\u0001~a\rq\u0018\u0011\u0001\t\u00045V|\bc\u0001\u0014\u0002\u0002\u0011Q\u00111\u0001>\u0002\u0002\u0003\u0005)\u0011A\u0018\u0003\u0007}#C\u0007C\u0004\u0002\b\u0001!\t!!\u0003\u0002\r%tgo\\6f)\u0015\u0019\u00141BA\f\u0011!\ti!!\u0002A\u0002\u0005=\u0011!C2p[B\fg.[8o!\u0011Yq+!\u0005\u0011\u0007}\n\u0019\"C\u0002\u0002\u0016\t\u00111cU5oO2,Go\u001c8EKN\u001c'/\u001b9u_JD\u0001\"!\u0007\u0002\u0006\u0001\u0007\u00111D\u0001\u0005CJ<7\u000fE\u0003\u0002\u001e\u000552G\u0004\u0003\u0002 \u0005%b\u0002BA\u0011\u0003Oi!!a\t\u000b\u0007\u0005\u0015\u0002\"\u0001\u0004=e>|GOP\u0005\u0002\u001b%\u0019\u00111\u0006\u0007\u0002\u000fA\f7m[1hK&!\u0011qFA\u0019\u0005\r\u0019V-\u001d\u0006\u0004\u0003Wa\u0001bBA\u001b\u0001\u0011\u0005\u0011qG\u0001\u0016O\u0016$\u0018i]!dG\u0016\u001c8/\u001b2mK>\u0013'.Z2u+\t\tI\u0004E\u0002\u0015\u0003wI1!!\u0010\u0016\u0005A\t5mY3tg&\u0014G.Z(cU\u0016\u001cG\u000fC\u0004\u0002B\u0001!\t!a\u0011\u0002%\u001d,G/T1sW\u0016$\u0017i\u001d)sS6\f'/\u001f\u000b\u0002q!9\u0011q\t\u0001\u0005B\u0005%\u0013\u0001\u0003;p'R\u0014\u0018N\\4\u0015\u0005\u0005-\u0003\u0003BA'\u0003\u001fj\u0011AF\u0005\u0003=Z\u0001")
public class Executable
{
    private final Method method;
    private final Constructor<?> constructor;
    private final boolean isPrimaryCtor;
    
    public Method method() {
        return this.method;
    }
    
    public Constructor<?> constructor() {
        return this.constructor;
    }
    
    public Option<String> defaultValuePattern() {
        return (Option<String>)(this.isPrimaryCtor ? new Some((Object)package$.MODULE$.ConstructorDefaultValuePattern()) : None$.MODULE$);
    }
    
    public int getModifiers() {
        return (this.method() == null) ? this.constructor().getModifiers() : this.method().getModifiers();
    }
    
    public Type[] getGenericParameterTypes() {
        return (this.method() == null) ? this.constructor().getGenericParameterTypes() : this.method().getGenericParameterTypes();
    }
    
    public Class<?>[] getParameterTypes() {
        return (this.method() == null) ? this.constructor().getParameterTypes() : this.method().getParameterTypes();
    }
    
    public Class<?> getDeclaringClass() {
        return (this.method() == null) ? this.constructor().getDeclaringClass() : this.method().getDeclaringClass();
    }
    
    public Object invoke(final Option<SingletonDescriptor> companion, final Seq<Object> args) {
        Object o;
        if (this.method() == null) {
            o = this.constructor().newInstance((Object[])((TraversableOnce)args.map((Function1)new Executable$$anonfun$invoke.Executable$$anonfun$invoke$2(this), Seq$.MODULE$.canBuildFrom())).toArray(ClassTag$.MODULE$.AnyRef()));
        }
        else if (companion instanceof Some) {
            final SingletonDescriptor cmp = (SingletonDescriptor)((Some)companion).x();
            o = this.method().invoke(cmp.instance(), (Object[])((TraversableOnce)args.map((Function1)new Executable$$anonfun$invoke.Executable$$anonfun$invoke$1(this), Seq$.MODULE$.canBuildFrom())).toArray(ClassTag$.MODULE$.AnyRef()));
        }
        else {
            if (None$.MODULE$.equals(companion)) {
                throw new package.MappingException("Trying to call apply method, but the companion object was not found.");
            }
            throw new MatchError((Object)companion);
        }
        return o;
    }
    
    public AccessibleObject getAsAccessibleObject() {
        return (this.method() == null) ? this.constructor() : this.method();
    }
    
    public boolean getMarkedAsPrimary() {
        final boolean markedByAnnotation = this.method() == null && this.constructor().isAnnotationPresent(PrimaryConstructor.class);
        return markedByAnnotation || this.isPrimaryCtor;
    }
    
    @Override
    public String toString() {
        return (this.method() == null) ? new StringContext((Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[] { "Executable(Constructor(", "))" })).s((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { this.constructor() })) : new StringContext((Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[] { "Executable(Method(", "))" })).s((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { this.method() }));
    }
    
    private Executable(final Method method, final Constructor<?> constructor, final boolean isPrimaryCtor) {
        this.method = method;
        this.constructor = constructor;
        this.isPrimaryCtor = isPrimaryCtor;
    }
    
    public Executable(final Method method) {
        this(method, null, false);
    }
    
    public Executable(final Constructor<?> constructor, final boolean isPrimaryCtor) {
        this(null, constructor, isPrimaryCtor);
    }
}
