// 
// Decompiled by Procyon v0.5.36
// 

package resource;

import scala.Some;
import scala.None$;
import scala.Option;
import scala.util.Either;
import scala.Serializable;

public final class ExtractedEither$ implements Serializable
{
    public static final ExtractedEither$ MODULE$;
    
    static {
        new ExtractedEither$();
    }
    
    @Override
    public final String toString() {
        return "ExtractedEither";
    }
    
    public <A, B> ExtractedEither<A, B> apply(final Either<A, B> either) {
        return new ExtractedEither<A, B>(either);
    }
    
    public <A, B> Option<Either<A, B>> unapply(final ExtractedEither<A, B> x$0) {
        return (Option<Either<A, B>>)((x$0 == null) ? None$.MODULE$ : new Some((Object)x$0.either()));
    }
    
    private Object readResolve() {
        return ExtractedEither$.MODULE$;
    }
    
    private ExtractedEither$() {
        MODULE$ = this;
    }
}
