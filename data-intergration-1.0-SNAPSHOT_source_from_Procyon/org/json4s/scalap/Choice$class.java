// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.scalap;

import scala.MatchError;
import scala.collection.immutable.$colon$colon;
import scala.collection.immutable.Nil$;
import scala.collection.immutable.List;
import scala.Function0;

public abstract class Choice$class
{
    public static Result apply(final Choice $this, final Object in) {
        return oneOf$1($this, $this.choices(), in);
    }
    
    public static Rule orElse(final Choice $this, final Function0 other) {
        return (Rule)new Choice$$anon.Choice$$anon$2($this, other);
    }
    
    private static final Result oneOf$1(Choice $this, List list, final Object in$1) {
        Failure$ module$;
        while (true) {
            final List obj = list;
            if (Nil$.MODULE$.equals(obj)) {
                module$ = Failure$.MODULE$;
                break;
            }
            if (!(obj instanceof $colon$colon)) {
                throw new MatchError((Object)obj);
            }
            final $colon$colon $colon$colon = ($colon$colon)obj;
            final Rule first = (Rule)$colon$colon.head();
            final List rest = $colon$colon.tl$1();
            final Result obj2 = (Result)first.apply(in$1);
            if (!Failure$.MODULE$.equals(obj2)) {
                module$ = (Failure$)obj2;
                break;
            }
            final Choice choice = $this;
            list = rest;
            $this = choice;
        }
        return module$;
    }
    
    public static void $init$(final Choice $this) {
    }
}
