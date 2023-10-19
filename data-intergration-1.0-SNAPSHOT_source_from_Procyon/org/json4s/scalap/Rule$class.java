// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.scalap;

import scala.Function7;
import scala.Function6;
import scala.Function5;
import scala.Function4;
import scala.Function3;
import scala.Function2;
import scala.PartialFunction;
import scala.Function0;
import scala.Function1;

public abstract class Rule$class
{
    public static Rule as(final Rule $this, final String name) {
        return $this.factory().ruleWithName(name, (scala.Function1<Object, Result<Object, Object, Object>>)$this);
    }
    
    public static Rule flatMap(final Rule $this, final Function1 fa2ruleb) {
        return $this.mapResult((Function1)new Rule$$anonfun$flatMap.Rule$$anonfun$flatMap$1($this, fa2ruleb));
    }
    
    public static Rule map(final Rule $this, final Function1 fa2b) {
        return $this.flatMap((Function1)new Rule$$anonfun$map.Rule$$anonfun$map$1($this, fa2b));
    }
    
    public static Rule filter(final Rule $this, final Function1 f) {
        return $this.flatMap((Function1)new Rule$$anonfun$filter.Rule$$anonfun$filter$1($this, f));
    }
    
    public static Rule mapResult(final Rule $this, final Function1 f) {
        return $this.factory().rule((scala.Function1<Object, Result<Object, Object, Object>>)new Rule$$anonfun$mapResult.Rule$$anonfun$mapResult$1($this, f));
    }
    
    public static Rule orElse(final Rule $this, final Function0 other) {
        return (Rule)new Rule$$anon.Rule$$anon$1($this, other);
    }
    
    public static Rule orError(final Rule $this) {
        return $this.orElse((Function0)new Rule$$anonfun$orError.Rule$$anonfun$orError$1($this));
    }
    
    public static Rule $bar(final Rule $this, final Function0 other) {
        return $this.orElse(other);
    }
    
    public static Rule $up$up(final Rule $this, final Function1 fa2b) {
        return $this.map(fa2b);
    }
    
    public static Rule $up$up$qmark(final Rule $this, final PartialFunction pf) {
        return $this.filter((Function1)new Rule$$anonfun$$up$up$qmark.Rule$$anonfun$$up$up$qmark$1($this, pf)).$up$up((Function1)pf);
    }
    
    public static Rule $qmark$qmark(final Rule $this, final PartialFunction pf) {
        return $this.filter((Function1)new Rule$$anonfun$$qmark$qmark.Rule$$anonfun$$qmark$qmark$1($this, pf));
    }
    
    public static Rule $minus$up(final Rule $this, final Object b) {
        return $this.map((Function1)new Rule$$anonfun$$minus$up.Rule$$anonfun$$minus$up$1($this, b));
    }
    
    public static Rule $bang$up(final Rule $this, final Function1 fx2y) {
        return $this.mapResult((Function1)new Rule$$anonfun$$bang$up.Rule$$anonfun$$bang$up$1($this, fx2y));
    }
    
    public static Rule $greater$greater(final Rule $this, final Function1 fa2ruleb) {
        return $this.flatMap(fa2ruleb);
    }
    
    public static Rule $greater$minus$greater(final Rule $this, final Function1 fa2resultb) {
        return $this.flatMap((Function1)new Rule$$anonfun$$greater$minus$greater.Rule$$anonfun$$greater$minus$greater$1($this, fa2resultb));
    }
    
    public static Rule $greater$greater$qmark(final Rule $this, final PartialFunction pf) {
        return $this.filter((Function1)new Rule$$anonfun$$greater$greater$qmark.Rule$$anonfun$$greater$greater$qmark$1($this, pf)).flatMap((Function1)pf);
    }
    
    public static Rule $greater$greater$amp(final Rule $this, final Function1 fa2ruleb) {
        return $this.flatMap((Function1)new Rule$$anonfun$$greater$greater$amp.Rule$$anonfun$$greater$greater$amp$1($this, fa2ruleb));
    }
    
    public static Rule $tilde(final Rule $this, final Function0 next) {
        return $this.flatMap((Function1)new Rule$$anonfun$$tilde.Rule$$anonfun$$tilde$1($this, next));
    }
    
    public static Rule $tilde$minus(final Rule $this, final Function0 next) {
        return $this.flatMap((Function1)new Rule$$anonfun$$tilde$minus.Rule$$anonfun$$tilde$minus$1($this, next));
    }
    
    public static Rule $minus$tilde(final Rule $this, final Function0 next) {
        return $this.flatMap((Function1)new Rule$$anonfun$$minus$tilde.Rule$$anonfun$$minus$tilde$1($this, next));
    }
    
    public static Rule $tilde$plus$plus(final Rule $this, final Function0 next) {
        return $this.flatMap((Function1)new Rule$$anonfun$$tilde$plus$plus.Rule$$anonfun$$tilde$plus$plus$1($this, next));
    }
    
    public static Rule $tilde$greater(final Rule $this, final Function0 next) {
        return $this.flatMap((Function1)new Rule$$anonfun$$tilde$greater.Rule$$anonfun$$tilde$greater$1($this, next));
    }
    
    public static Rule $less$tilde$colon(final Rule $this, final Function0 prev) {
        return ((Rule)prev.apply()).flatMap((Function1)new Rule$$anonfun$$less$tilde$colon.Rule$$anonfun$$less$tilde$colon$1($this));
    }
    
    public static Rule $tilde$bang(final Rule $this, final Function0 next) {
        return $this.flatMap((Function1)new Rule$$anonfun$$tilde$bang.Rule$$anonfun$$tilde$bang$1($this, next));
    }
    
    public static Rule $tilde$minus$bang(final Rule $this, final Function0 next) {
        return $this.flatMap((Function1)new Rule$$anonfun$$tilde$minus$bang.Rule$$anonfun$$tilde$minus$bang$1($this, next));
    }
    
    public static Rule $minus$tilde$bang(final Rule $this, final Function0 next) {
        return $this.flatMap((Function1)new Rule$$anonfun$$minus$tilde$bang.Rule$$anonfun$$minus$tilde$bang$1($this, next));
    }
    
    public static Rule $minus(final Rule $this, final Function0 exclude) {
        return $this.factory().inRule((Rule<Object, Object, Object, Object>)exclude.apply()).unary_$bang().$minus$tilde((scala.Function0<Rule<Object, Object, Object, Object>>)new Rule$$anonfun$$minus.Rule$$anonfun$$minus$1($this));
    }
    
    public static Rule $up$tilde$up(final Rule $this, final Function2 f, final Function1 A) {
        return $this.map((Function1)new Rule$$anonfun$$up$tilde$up.Rule$$anonfun$$up$tilde$up$1($this, f, A));
    }
    
    public static Rule $up$tilde$tilde$up(final Rule $this, final Function3 f, final Function1 A) {
        return $this.map((Function1)new Rule$$anonfun$$up$tilde$tilde$up.Rule$$anonfun$$up$tilde$tilde$up$1($this, f, A));
    }
    
    public static Rule $up$tilde$tilde$tilde$up(final Rule $this, final Function4 f, final Function1 A) {
        return $this.map((Function1)new Rule$$anonfun$$up$tilde$tilde$tilde$up.Rule$$anonfun$$up$tilde$tilde$tilde$up$1($this, f, A));
    }
    
    public static Rule $up$tilde$tilde$tilde$tilde$up(final Rule $this, final Function5 f, final Function1 A) {
        return $this.map((Function1)new Rule$$anonfun$$up$tilde$tilde$tilde$tilde$up.Rule$$anonfun$$up$tilde$tilde$tilde$tilde$up$1($this, f, A));
    }
    
    public static Rule $up$tilde$tilde$tilde$tilde$tilde$up(final Rule $this, final Function6 f, final Function1 A) {
        return $this.map((Function1)new Rule$$anonfun$$up$tilde$tilde$tilde$tilde$tilde$up.Rule$$anonfun$$up$tilde$tilde$tilde$tilde$tilde$up$1($this, f, A));
    }
    
    public static Rule $up$tilde$tilde$tilde$tilde$tilde$tilde$up(final Rule $this, final Function7 f, final Function1 A) {
        return $this.map((Function1)new Rule$$anonfun$$up$tilde$tilde$tilde$tilde$tilde$tilde$up.Rule$$anonfun$$up$tilde$tilde$tilde$tilde$tilde$tilde$up$1($this, f, A));
    }
    
    public static Rule $greater$tilde$greater(final Rule $this, final Function2 f, final Function1 A) {
        return $this.flatMap((Function1)new Rule$$anonfun$$greater$tilde$greater.Rule$$anonfun$$greater$tilde$greater$1($this, f, A));
    }
    
    public static Rule $up$minus$up(final Rule $this, final Function2 f) {
        return $this.map((Function1)new Rule$$anonfun$$up$minus$up.Rule$$anonfun$$up$minus$up$1($this, f));
    }
    
    public static Rule $up$tilde$greater$tilde$up(final Rule $this, final Function3 f, final Function1 A) {
        return $this.map((Function1)new Rule$$anonfun$$up$tilde$greater$tilde$up.Rule$$anonfun$$up$tilde$greater$tilde$up$1($this, f, A));
    }
    
    public static void $init$(final Rule $this) {
    }
}
