// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.scalap;

import scala.runtime.BoxesRunTime;
import scala.runtime.Nothing$;
import scala.MatchError;
import scala.collection.immutable.$colon$colon;
import scala.collection.immutable.Nil$;
import scala.collection.immutable.List;
import scala.collection.Seq$;
import scala.collection.Seq;
import scala.Function0;
import scala.Function1;

public abstract class StateRules$class
{
    public static Rule apply(final StateRules $this, final Function1 f) {
        return $this.factory().rule((scala.Function1<Object, Result<Object, Object, Object>>)f);
    }
    
    public static Rule unit(final StateRules $this, final Function0 a) {
        return $this.apply((scala.Function1<Object, Result<Object, Object, Object>>)new StateRules$$anonfun$unit.StateRules$$anonfun$unit$1($this, a));
    }
    
    public static Rule read(final StateRules $this, final Function1 f) {
        return $this.apply((scala.Function1<Object, Result<Object, Object, Object>>)new StateRules$$anonfun$read.StateRules$$anonfun$read$1($this, f));
    }
    
    public static Rule get(final StateRules $this) {
        return $this.apply((scala.Function1<Object, Result<Object, Object, Object>>)new StateRules$$anonfun$get.StateRules$$anonfun$get$1($this));
    }
    
    public static Rule set(final StateRules $this, final Function0 s) {
        return $this.apply((scala.Function1<Object, Result<Object, Object, Object>>)new StateRules$$anonfun$set.StateRules$$anonfun$set$1($this, s));
    }
    
    public static Rule update(final StateRules $this, final Function1 f) {
        return $this.apply((scala.Function1<Object, Result<Object, Object, Object>>)new StateRules$$anonfun$update.StateRules$$anonfun$update$1($this, f));
    }
    
    public static Rule nil(final StateRules $this) {
        return $this.unit((scala.Function0<Object>)new StateRules$$anonfun$nil.StateRules$$anonfun$nil$1($this));
    }
    
    public static Rule none(final StateRules $this) {
        return $this.unit((scala.Function0<Object>)new StateRules$$anonfun$none.StateRules$$anonfun$none$1($this));
    }
    
    public static Rule cond(final StateRules $this, final Function1 f) {
        return $this.get().filter((scala.Function1<Object, Object>)f);
    }
    
    public static Function1 allOf(final StateRules $this, final Seq rules) {
        return (Function1)new StateRules$$anonfun$allOf.StateRules$$anonfun$allOf$1($this, rules);
    }
    
    public static Rule anyOf(final StateRules $this, final Seq rules) {
        return $this.factory().rule((scala.Function1<Object, Result<Object, scala.collection.immutable.List<Object>, Object>>)$this.allOf((scala.collection.Seq<Rule<Object, Object, Object, X>>)rules.map((Function1)new StateRules$$anonfun$anyOf.StateRules$$anonfun$anyOf$1($this), Seq$.MODULE$.canBuildFrom()))).$up$up((scala.Function1<scala.collection.immutable.List<Object>, Object>)new StateRules$$anonfun$anyOf.StateRules$$anonfun$anyOf$2($this));
    }
    
    public static Rule repeatUntil(final StateRules $this, final Rule rule, final Function1 finished, final Object initial) {
        return $this.apply((scala.Function1<Object, Result<Object, Object, Object>>)new StateRules$$anonfun$repeatUntil.StateRules$$anonfun$repeatUntil$1($this, rule, finished, initial));
    }
    
    public static final Result rep$1(StateRules $this, Object in, List rules, List results) {
        Result<Nothing$, Nothing$, X> module$;
        while (true) {
            final List obj = rules;
            if (Nil$.MODULE$.equals(obj)) {
                return (Result<Nothing$, Nothing$, X>)new Success(in, results.reverse());
            }
            if (!(obj instanceof $colon$colon)) {
                throw new MatchError((Object)obj);
            }
            final $colon$colon $colon$colon = ($colon$colon)obj;
            final Rule rule = (Rule)$colon$colon.head();
            final List tl = $colon$colon.tl$1();
            final Result obj2 = (Result)rule.apply(in);
            if (Failure$.MODULE$.equals(obj2)) {
                module$ = Failure$.MODULE$;
                break;
            }
            if (obj2 instanceof Error) {
                final Object x = ((Error<Object>)obj2).error();
                module$ = new Error<Object>(x);
                break;
            }
            if (!(obj2 instanceof Success)) {
                throw new MatchError((Object)obj2);
            }
            final Success<Object, A> success = (Success<Object, A>)obj2;
            final Object out = success.out();
            final Object v = success.value();
            final StateRules stateRules = $this;
            final Object o = out;
            final List list = tl;
            results = results.$colon$colon(v);
            rules = list;
            in = o;
            $this = stateRules;
        }
        return module$;
    }
    
    public static final Result rep$2(StateRules $this, Object in, Object t, final Rule rule$2, final Function1 finished$1) {
        while (!BoxesRunTime.unboxToBoolean(finished$1.apply(t))) {
            final Result obj = (Result)rule$2.apply(in);
            if (!(obj instanceof Success)) {
                Result<Nothing$, Nothing$, X> module$;
                if (Failure$.MODULE$.equals(obj)) {
                    module$ = Failure$.MODULE$;
                }
                else {
                    if (!(obj instanceof Error)) {
                        throw new MatchError((Object)obj);
                    }
                    final Object x = ((Error<Object>)obj).error();
                    module$ = new Error<Object>(x);
                }
                return module$;
            }
            final Success<Object, A> success = (Success<Object, A>)obj;
            final Object out = success.out();
            final Function1 f = (Function1)success.value();
            final StateRules stateRules = $this;
            final Object o = out;
            t = f.apply(t);
            in = o;
            $this = stateRules;
        }
        return (Result<Nothing$, Nothing$, X>)new Success(in, t);
    }
    
    public static void $init$(final StateRules $this) {
    }
}
