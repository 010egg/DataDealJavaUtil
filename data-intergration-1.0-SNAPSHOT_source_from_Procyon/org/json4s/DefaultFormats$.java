// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

import scala.Function0;
import scala.PartialFunction;
import scala.Option;
import scala.collection.Iterable;
import scala.collection.Seq;
import org.json4s.prefs.EmptyValueStrategy;
import java.lang.reflect.Type;
import scala.collection.immutable.Set;
import scala.Tuple2;
import scala.collection.immutable.List;
import org.json4s.reflect.package;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public final class DefaultFormats$ implements DefaultFormats
{
    public static final DefaultFormats$ MODULE$;
    private final TimeZone UTC;
    private final ThreadLocal<SimpleDateFormat> losslessDate;
    private final String typeHintFieldName;
    private final package.ParameterNameReader parameterNameReader;
    private final TypeHints typeHints;
    private final List<Serializer<?>> customSerializers;
    private final List<KeySerializer<?>> customKeySerializers;
    private final List<Tuple2<Class<?>, FieldSerializer<?>>> fieldSerializers;
    private final boolean wantsBigInt;
    private final boolean wantsBigDecimal;
    private final Set<Type> primitives;
    private final List<Tuple2<Class<?>, Object>> companions;
    private final boolean strictOptionParsing;
    private final EmptyValueStrategy emptyValueStrategy;
    private final boolean allowNull;
    private final DateFormat dateFormat;
    private final ThreadLocal org$json4s$DefaultFormats$$df;
    
    static {
        new DefaultFormats$();
    }
    
    @Override
    public String typeHintFieldName() {
        return this.typeHintFieldName;
    }
    
    @Override
    public package.ParameterNameReader parameterNameReader() {
        return this.parameterNameReader;
    }
    
    @Override
    public TypeHints typeHints() {
        return this.typeHints;
    }
    
    @Override
    public List<Serializer<?>> customSerializers() {
        return this.customSerializers;
    }
    
    @Override
    public List<KeySerializer<?>> customKeySerializers() {
        return this.customKeySerializers;
    }
    
    @Override
    public List<Tuple2<Class<?>, FieldSerializer<?>>> fieldSerializers() {
        return this.fieldSerializers;
    }
    
    @Override
    public boolean wantsBigInt() {
        return this.wantsBigInt;
    }
    
    @Override
    public boolean wantsBigDecimal() {
        return this.wantsBigDecimal;
    }
    
    @Override
    public Set<Type> primitives() {
        return this.primitives;
    }
    
    @Override
    public List<Tuple2<Class<?>, Object>> companions() {
        return this.companions;
    }
    
    @Override
    public boolean strictOptionParsing() {
        return this.strictOptionParsing;
    }
    
    @Override
    public EmptyValueStrategy emptyValueStrategy() {
        return this.emptyValueStrategy;
    }
    
    @Override
    public boolean allowNull() {
        return this.allowNull;
    }
    
    @Override
    public DateFormat dateFormat() {
        return this.dateFormat;
    }
    
    @Override
    public ThreadLocal org$json4s$DefaultFormats$$df() {
        return this.org$json4s$DefaultFormats$$df;
    }
    
    @Override
    public void org$json4s$DefaultFormats$_setter_$org$json4s$DefaultFormats$$df_$eq(final ThreadLocal x$1) {
        this.org$json4s$DefaultFormats$$df = x$1;
    }
    
    @Override
    public void org$json4s$DefaultFormats$_setter_$typeHintFieldName_$eq(final String x$1) {
        this.typeHintFieldName = x$1;
    }
    
    @Override
    public void org$json4s$DefaultFormats$_setter_$parameterNameReader_$eq(final package.ParameterNameReader x$1) {
        this.parameterNameReader = x$1;
    }
    
    @Override
    public void org$json4s$DefaultFormats$_setter_$typeHints_$eq(final TypeHints x$1) {
        this.typeHints = x$1;
    }
    
    @Override
    public void org$json4s$DefaultFormats$_setter_$customSerializers_$eq(final List x$1) {
        this.customSerializers = (List<Serializer<?>>)x$1;
    }
    
    @Override
    public void org$json4s$DefaultFormats$_setter_$customKeySerializers_$eq(final List x$1) {
        this.customKeySerializers = (List<KeySerializer<?>>)x$1;
    }
    
    @Override
    public void org$json4s$DefaultFormats$_setter_$fieldSerializers_$eq(final List x$1) {
        this.fieldSerializers = (List<Tuple2<Class<?>, FieldSerializer<?>>>)x$1;
    }
    
    @Override
    public void org$json4s$DefaultFormats$_setter_$wantsBigInt_$eq(final boolean x$1) {
        this.wantsBigInt = x$1;
    }
    
    @Override
    public void org$json4s$DefaultFormats$_setter_$wantsBigDecimal_$eq(final boolean x$1) {
        this.wantsBigDecimal = x$1;
    }
    
    @Override
    public void org$json4s$DefaultFormats$_setter_$primitives_$eq(final Set x$1) {
        this.primitives = (Set<Type>)x$1;
    }
    
    @Override
    public void org$json4s$DefaultFormats$_setter_$companions_$eq(final List x$1) {
        this.companions = (List<Tuple2<Class<?>, Object>>)x$1;
    }
    
    @Override
    public void org$json4s$DefaultFormats$_setter_$strictOptionParsing_$eq(final boolean x$1) {
        this.strictOptionParsing = x$1;
    }
    
    @Override
    public void org$json4s$DefaultFormats$_setter_$emptyValueStrategy_$eq(final EmptyValueStrategy x$1) {
        this.emptyValueStrategy = x$1;
    }
    
    @Override
    public void org$json4s$DefaultFormats$_setter_$allowNull_$eq(final boolean x$1) {
        this.allowNull = x$1;
    }
    
    @Override
    public void org$json4s$DefaultFormats$_setter_$dateFormat_$eq(final DateFormat x$1) {
        this.dateFormat = x$1;
    }
    
    @Override
    public SimpleDateFormat dateFormatter() {
        return DefaultFormats$class.dateFormatter(this);
    }
    
    @Override
    public Formats lossless() {
        return DefaultFormats$class.lossless(this);
    }
    
    @Override
    public Formats withHints(final TypeHints hints) {
        return DefaultFormats$class.withHints(this, hints);
    }
    
    @Override
    public boolean strictArrayExtraction() {
        return Formats$class.strictArrayExtraction(this);
    }
    
    @Override
    public boolean alwaysEscapeUnicode() {
        return Formats$class.alwaysEscapeUnicode(this);
    }
    
    @Override
    public Formats withBigInt() {
        return Formats$class.withBigInt(this);
    }
    
    @Override
    public Formats withLong() {
        return Formats$class.withLong(this);
    }
    
    @Override
    public Formats withBigDecimal() {
        return Formats$class.withBigDecimal(this);
    }
    
    @Override
    public Formats withDouble() {
        return Formats$class.withDouble(this);
    }
    
    @Override
    public Formats withCompanions(final Seq<Tuple2<Class<?>, Object>> comps) {
        return Formats$class.withCompanions(this, comps);
    }
    
    @Override
    public Formats preservingEmptyValues() {
        return Formats$class.preservingEmptyValues(this);
    }
    
    @Override
    public Formats skippingEmptyValues() {
        return Formats$class.skippingEmptyValues(this);
    }
    
    @Override
    public Formats withEmptyValueStrategy(final EmptyValueStrategy strategy) {
        return Formats$class.withEmptyValueStrategy(this, strategy);
    }
    
    @Override
    public Formats withTypeHintFieldName(final String name) {
        return Formats$class.withTypeHintFieldName(this, name);
    }
    
    @Override
    public Formats withEscapeUnicode() {
        return Formats$class.withEscapeUnicode(this);
    }
    
    @Override
    public Formats withStrictOptionParsing() {
        return Formats$class.withStrictOptionParsing(this);
    }
    
    @Override
    public Formats withStrictArrayExtraction() {
        return Formats$class.withStrictArrayExtraction(this);
    }
    
    @Override
    public Formats strict() {
        return Formats$class.strict(this);
    }
    
    @Override
    public Formats nonStrict() {
        return Formats$class.nonStrict(this);
    }
    
    @Override
    public Formats disallowNull() {
        return Formats$class.disallowNull(this);
    }
    
    @Override
    public Formats $plus(final TypeHints extraHints) {
        return Formats$class.$plus(this, extraHints);
    }
    
    @Override
    public Formats $plus(final Serializer<?> newSerializer) {
        return Formats$class.$plus(this, newSerializer);
    }
    
    @Override
    public Formats $plus(final KeySerializer<?> newSerializer) {
        return Formats$class.$plus(this, newSerializer);
    }
    
    @Override
    public Formats $plus$plus(final Iterable<Serializer<?>> newSerializers) {
        return Formats$class.$plus$plus(this, newSerializers);
    }
    
    @Override
    public Formats $minus(final Serializer<?> serializer) {
        return Formats$class.$minus(this, serializer);
    }
    
    @Override
    public Formats addKeySerializers(final Iterable<KeySerializer<?>> newKeySerializers) {
        return Formats$class.addKeySerializers(this, newKeySerializers);
    }
    
    @Override
    public <A> Formats $plus(final FieldSerializer<A> newSerializer) {
        return Formats$class.$plus(this, newSerializer);
    }
    
    @Override
    public Option<FieldSerializer<?>> fieldSerializer(final Class<?> clazz) {
        return (Option<FieldSerializer<?>>)Formats$class.fieldSerializer(this, clazz);
    }
    
    @Override
    @Deprecated
    public PartialFunction<Object, JsonAST.JValue> customSerializer(final Formats format) {
        return (PartialFunction<Object, JsonAST.JValue>)Formats$class.customSerializer(this, format);
    }
    
    @Override
    @Deprecated
    public PartialFunction<Tuple2<package.TypeInfo, JsonAST.JValue>, Object> customDeserializer(final Formats format) {
        return (PartialFunction<Tuple2<package.TypeInfo, JsonAST.JValue>, Object>)Formats$class.customDeserializer(this, format);
    }
    
    @Override
    @Deprecated
    public PartialFunction<Object, String> customKeySerializer(final Formats format) {
        return (PartialFunction<Object, String>)Formats$class.customKeySerializer(this, format);
    }
    
    @Override
    @Deprecated
    public PartialFunction<Tuple2<package.TypeInfo, String>, Object> customKeyDeserializer(final Formats format) {
        return (PartialFunction<Tuple2<package.TypeInfo, String>, Object>)Formats$class.customKeyDeserializer(this, format);
    }
    
    public TimeZone UTC() {
        return this.UTC;
    }
    
    public ThreadLocal<SimpleDateFormat> losslessDate() {
        return this.losslessDate;
    }
    
    private Object readResolve() {
        return DefaultFormats$.MODULE$;
    }
    
    public final SimpleDateFormat org$json4s$DefaultFormats$$createSdf$1() {
        final SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        f.setTimeZone(this.UTC());
        return f;
    }
    
    private DefaultFormats$() {
        Formats$class.$init$(MODULE$ = this);
        DefaultFormats$class.$init$(this);
        this.UTC = TimeZone.getTimeZone("UTC");
        this.losslessDate = new ThreadLocal<SimpleDateFormat>((scala.Function0<SimpleDateFormat>)new DefaultFormats$$anonfun.DefaultFormats$$anonfun$7());
    }
}
