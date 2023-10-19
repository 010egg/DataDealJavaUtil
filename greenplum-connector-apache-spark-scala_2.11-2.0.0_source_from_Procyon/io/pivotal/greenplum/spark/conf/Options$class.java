// 
// Decompiled by Procyon v0.5.36
// 

package io.pivotal.greenplum.spark.conf;

import scala.Function1;
import scala.util.Try;
import scala.Function2;
import scala.MatchError;
import scala.collection.Seq;
import scala.StringContext;
import scala.Predef$;
import scala.None$;
import scala.Some;
import scala.Option;
import scala.Tuple2;

public abstract class Options$class
{
    public static String option(final Options $this, final String optionName, final WhatIfMissing whatIfMissing) {
        final Tuple2 tuple2 = new Tuple2((Object)$this.option(optionName), (Object)whatIfMissing);
        if (tuple2 != null) {
            final Option obj = (Option)tuple2._1();
            final WhatIfMissing obj2 = (WhatIfMissing)tuple2._2();
            if (((obj instanceof Some && ((Some)obj).x() == null) || None$.MODULE$.equals(obj)) && ErrorIfMissing$.MODULE$.equals(obj2)) {
                throw new IllegalArgumentException(new StringContext((Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[] { "requirement failed: Option '", "' is required." })).s((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { optionName })));
            }
        }
        if (tuple2 != null) {
            final Option obj3 = (Option)tuple2._1();
            final WhatIfMissing whatIfMissing2 = (WhatIfMissing)tuple2._2();
            if (((obj3 instanceof Some && ((Some)obj3).x() == null) || None$.MODULE$.equals(obj3)) && whatIfMissing2 instanceof Default) {
                final String value2;
                final String default1 = value2 = ((Default)whatIfMissing2).value();
                return value2;
            }
        }
        if (tuple2 != null) {
            final Option option = (Option)tuple2._1();
            if (option instanceof Some) {
                final String value2;
                final String value = value2 = (String)((Some)option).x();
                return value2;
            }
        }
        throw new MatchError((Object)tuple2);
    }
    
    public static Object option(final Options $this, final String optionName, final WhatIfMissing whatIfMissing, final Function2 convert) {
        final String stringValue = $this.option(optionName, whatIfMissing);
        return ((Try)convert.apply((Object)stringValue, (Object)optionName)).get();
    }
    
    public static Option option(final Options $this, final String optionName) {
        return $this.parameters().get((Object)optionName);
    }
    
    public static Option option(final Options $this, final String optionName, final Function2 convert) {
        return $this.option(optionName).map((Function1)new Options$$anonfun$option.Options$$anonfun$option$1($this, optionName, convert));
    }
    
    public static Function2 bool(final Options $this) {
        return (Function2)new Options$$anonfun$bool.Options$$anonfun$bool$1($this);
    }
    
    public static Function2 naturalLong(final Options $this) {
        return (Function2)new Options$$anonfun$naturalLong.Options$$anonfun$naturalLong$1($this);
    }
    
    public static Function2 int(final Options $this) {
        return (Function2)new Options$$anonfun$int.Options$$anonfun$int$1($this);
    }
    
    public static Function2 positiveInt(final Options $this) {
        return (Function2)new Options$$anonfun$positiveInt.Options$$anonfun$positiveInt$1($this);
    }
    
    public static Function2 nonNegativeInt(final Options $this) {
        return (Function2)new Options$$anonfun$nonNegativeInt.Options$$anonfun$nonNegativeInt$1($this);
    }
    
    public static void $init$(final Options $this) {
    }
}
