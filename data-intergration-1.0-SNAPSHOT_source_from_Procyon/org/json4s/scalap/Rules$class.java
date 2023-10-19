// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.scalap;

import scala.collection.Seq;
import scala.Function1;

public abstract class Rules$class
{
    public static Rule rule(final Rules $this, final Function1 f) {
        return $this.new DefaultRule(f);
    }
    
    public static InRule inRule(final Rules $this, final Rule rule) {
        return new InRule(rule);
    }
    
    public static SeqRule seqRule(final Rules $this, final Rule rule) {
        return new SeqRule(rule);
    }
    
    public static Rules.FromRule from(final Rules $this) {
        return (Rules.FromRule)new Rules$$anon.Rules$$anon$3($this);
    }
    
    public static StateRules state(final Rules $this) {
        return (StateRules)new Rules$$anon.Rules$$anon$4($this);
    }
    
    public static Rule success(final Rules $this, final Object out, final Object a) {
        return $this.rule((scala.Function1<Object, Result<Object, Object, Object>>)new Rules$$anonfun$success.Rules$$anonfun$success$1($this, out, a));
    }
    
    public static Rule failure(final Rules $this) {
        return $this.rule((scala.Function1<Object, Result<Object, Object, Object>>)new Rules$$anonfun$failure.Rules$$anonfun$failure$1($this));
    }
    
    public static Rule error(final Rules $this) {
        return $this.rule((scala.Function1<Object, Result<Object, Object, Object>>)new Rules$$anonfun$error.Rules$$anonfun$error$1($this));
    }
    
    public static Rule error(final Rules $this, final Object err) {
        return $this.rule((scala.Function1<Object, Result<Object, Object, Object>>)new Rules$$anonfun$error.Rules$$anonfun$error$2($this, err));
    }
    
    public static Rule oneOf(final Rules $this, final Seq rules) {
        return (Rule)new Rules$$anon.Rules$$anon$2($this, rules);
    }
    
    public static Rule ruleWithName(final Rules $this, final String _name, final Function1 f) {
        return (Rule)new Rules$$anon.Rules$$anon$1($this, _name, f);
    }
    
    public static Function1 expect(final Rules $this, final Rule rule) {
        return (Function1)new Rules$$anonfun$expect.Rules$$anonfun$expect$1($this, rule);
    }
    
    public static void $init$(final Rules $this) {
    }
}
