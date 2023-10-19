// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

import org.json4s.prefs.EmptyValueStrategy$;
import scala.collection.Seq;
import java.lang.reflect.Type;
import scala.Predef$;
import scala.collection.immutable.Set;
import scala.collection.immutable.List;
import scala.collection.immutable.Nil$;
import org.json4s.reflect.package;
import scala.Function0;
import java.text.SimpleDateFormat;

public abstract class DefaultFormats$class
{
    public static SimpleDateFormat dateFormatter(final DefaultFormats $this) {
        final SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        f.setTimeZone(DefaultFormats$.MODULE$.UTC());
        return f;
    }
    
    public static Formats lossless(final DefaultFormats $this) {
        return (Formats)new DefaultFormats$$anon.DefaultFormats$$anon$1($this);
    }
    
    public static Formats withHints(final DefaultFormats $this, final TypeHints hints) {
        return (Formats)new DefaultFormats$$anon.DefaultFormats$$anon$2($this, hints);
    }
    
    public static void $init$(final DefaultFormats $this) {
        $this.org$json4s$DefaultFormats$_setter_$org$json4s$DefaultFormats$$df_$eq(new ThreadLocal((Function0<A>)new DefaultFormats$$anonfun.DefaultFormats$$anonfun$8($this)));
        $this.org$json4s$DefaultFormats$_setter_$typeHintFieldName_$eq("jsonClass");
        $this.org$json4s$DefaultFormats$_setter_$parameterNameReader_$eq(package.ParanamerReader$.MODULE$);
        $this.org$json4s$DefaultFormats$_setter_$typeHints_$eq(NoTypeHints$.MODULE$);
        $this.org$json4s$DefaultFormats$_setter_$customSerializers_$eq((List)Nil$.MODULE$);
        $this.org$json4s$DefaultFormats$_setter_$customKeySerializers_$eq((List)Nil$.MODULE$);
        $this.org$json4s$DefaultFormats$_setter_$fieldSerializers_$eq((List)Nil$.MODULE$);
        $this.org$json4s$DefaultFormats$_setter_$wantsBigInt_$eq(true);
        $this.org$json4s$DefaultFormats$_setter_$wantsBigDecimal_$eq(false);
        $this.org$json4s$DefaultFormats$_setter_$primitives_$eq((Set)Predef$.MODULE$.Set().apply((Seq)Predef$.MODULE$.wrapRefArray((Object[])new Type[] { JsonAST.JValue.class, JsonAST.JObject.class, JsonAST.JArray.class })));
        $this.org$json4s$DefaultFormats$_setter_$companions_$eq((List)Nil$.MODULE$);
        $this.org$json4s$DefaultFormats$_setter_$strictOptionParsing_$eq(false);
        $this.org$json4s$DefaultFormats$_setter_$emptyValueStrategy_$eq(EmptyValueStrategy$.MODULE$.default());
        $this.org$json4s$DefaultFormats$_setter_$allowNull_$eq(true);
        $this.org$json4s$DefaultFormats$_setter_$dateFormat_$eq((DateFormat)new DefaultFormats$$anon.DefaultFormats$$anon$4($this));
    }
}
