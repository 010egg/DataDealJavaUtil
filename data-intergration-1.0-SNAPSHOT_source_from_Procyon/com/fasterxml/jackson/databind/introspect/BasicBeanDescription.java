// 
// Decompiled by Procyon v0.5.36
// 

package com.fasterxml.jackson.databind.introspect;

import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import java.util.LinkedHashMap;
import java.util.Collection;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.MapperFeature;
import java.util.Map;
import java.lang.reflect.Type;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.util.Annotations;
import java.util.Set;
import com.fasterxml.jackson.databind.PropertyName;
import java.util.Iterator;
import java.util.Collections;
import com.fasterxml.jackson.databind.JavaType;
import java.util.List;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.BeanDescription;

public class BasicBeanDescription extends BeanDescription
{
    private static final Class<?>[] NO_VIEWS;
    protected final POJOPropertiesCollector _propCollector;
    protected final MapperConfig<?> _config;
    protected final AnnotationIntrospector _annotationIntrospector;
    protected final AnnotatedClass _classInfo;
    protected Class<?>[] _defaultViews;
    protected boolean _defaultViewsResolved;
    protected List<BeanPropertyDefinition> _properties;
    protected ObjectIdInfo _objectIdInfo;
    
    protected BasicBeanDescription(final POJOPropertiesCollector coll, final JavaType type, final AnnotatedClass classDef) {
        super(type);
        this._propCollector = coll;
        this._config = coll.getConfig();
        if (this._config == null) {
            this._annotationIntrospector = null;
        }
        else {
            this._annotationIntrospector = this._config.getAnnotationIntrospector();
        }
        this._classInfo = classDef;
    }
    
    protected BasicBeanDescription(final MapperConfig<?> config, final JavaType type, final AnnotatedClass classDef, final List<BeanPropertyDefinition> props) {
        super(type);
        this._propCollector = null;
        this._config = config;
        if (this._config == null) {
            this._annotationIntrospector = null;
        }
        else {
            this._annotationIntrospector = this._config.getAnnotationIntrospector();
        }
        this._classInfo = classDef;
        this._properties = props;
    }
    
    protected BasicBeanDescription(final POJOPropertiesCollector coll) {
        this(coll, coll.getType(), coll.getClassDef());
        this._objectIdInfo = coll.getObjectIdInfo();
    }
    
    public static BasicBeanDescription forDeserialization(final POJOPropertiesCollector coll) {
        return new BasicBeanDescription(coll);
    }
    
    public static BasicBeanDescription forSerialization(final POJOPropertiesCollector coll) {
        return new BasicBeanDescription(coll);
    }
    
    public static BasicBeanDescription forOtherUse(final MapperConfig<?> config, final JavaType type, final AnnotatedClass ac) {
        return new BasicBeanDescription(config, type, ac, Collections.emptyList());
    }
    
    protected List<BeanPropertyDefinition> _properties() {
        if (this._properties == null) {
            this._properties = this._propCollector.getProperties();
        }
        return this._properties;
    }
    
    public boolean removeProperty(final String propName) {
        final Iterator<BeanPropertyDefinition> it = this._properties().iterator();
        while (it.hasNext()) {
            final BeanPropertyDefinition prop = it.next();
            if (prop.getName().equals(propName)) {
                it.remove();
                return true;
            }
        }
        return false;
    }
    
    public boolean addProperty(final BeanPropertyDefinition def) {
        if (this.hasProperty(def.getFullName())) {
            return false;
        }
        this._properties().add(def);
        return true;
    }
    
    public boolean hasProperty(final PropertyName name) {
        return this.findProperty(name) != null;
    }
    
    public BeanPropertyDefinition findProperty(final PropertyName name) {
        for (final BeanPropertyDefinition prop : this._properties()) {
            if (prop.hasName(name)) {
                return prop;
            }
        }
        return null;
    }
    
    @Override
    public AnnotatedClass getClassInfo() {
        return this._classInfo;
    }
    
    @Override
    public ObjectIdInfo getObjectIdInfo() {
        return this._objectIdInfo;
    }
    
    @Override
    public List<BeanPropertyDefinition> findProperties() {
        return this._properties();
    }
    
    @Deprecated
    @Override
    public AnnotatedMethod findJsonValueMethod() {
        return (this._propCollector == null) ? null : this._propCollector.getJsonValueMethod();
    }
    
    @Override
    public AnnotatedMember findJsonValueAccessor() {
        return (this._propCollector == null) ? null : this._propCollector.getJsonValueAccessor();
    }
    
    @Override
    public Set<String> getIgnoredPropertyNames() {
        final Set<String> ign = (this._propCollector == null) ? null : this._propCollector.getIgnoredPropertyNames();
        if (ign == null) {
            return Collections.emptySet();
        }
        return ign;
    }
    
    @Override
    public boolean hasKnownClassAnnotations() {
        return this._classInfo.hasAnnotations();
    }
    
    @Override
    public Annotations getClassAnnotations() {
        return this._classInfo.getAnnotations();
    }
    
    @Deprecated
    @Override
    public TypeBindings bindingsForBeanType() {
        return this._type.getBindings();
    }
    
    @Deprecated
    @Override
    public JavaType resolveType(final Type jdkType) {
        if (jdkType == null) {
            return null;
        }
        return this._config.getTypeFactory().constructType(jdkType, this._type.getBindings());
    }
    
    @Override
    public AnnotatedConstructor findDefaultConstructor() {
        return this._classInfo.getDefaultConstructor();
    }
    
    @Override
    public AnnotatedMember findAnySetterAccessor() throws IllegalArgumentException {
        if (this._propCollector != null) {
            final AnnotatedMethod anyMethod = this._propCollector.getAnySetterMethod();
            if (anyMethod != null) {
                final Class<?> type = anyMethod.getRawParameterType(0);
                if (type != String.class && type != Object.class) {
                    throw new IllegalArgumentException(String.format("Invalid 'any-setter' annotation on method '%s()': first argument not of type String or Object, but %s", anyMethod.getName(), type.getName()));
                }
                return anyMethod;
            }
            else {
                final AnnotatedMember anyField = this._propCollector.getAnySetterField();
                if (anyField != null) {
                    final Class<?> type2 = anyField.getRawType();
                    if (!Map.class.isAssignableFrom(type2)) {
                        throw new IllegalArgumentException(String.format("Invalid 'any-setter' annotation on field '%s': type is not instance of java.util.Map", anyField.getName()));
                    }
                    return anyField;
                }
            }
        }
        return null;
    }
    
    @Override
    public Map<Object, AnnotatedMember> findInjectables() {
        if (this._propCollector != null) {
            return this._propCollector.getInjectables();
        }
        return Collections.emptyMap();
    }
    
    @Override
    public List<AnnotatedConstructor> getConstructors() {
        return this._classInfo.getConstructors();
    }
    
    @Override
    public Object instantiateBean(final boolean fixAccess) {
        final AnnotatedConstructor ac = this._classInfo.getDefaultConstructor();
        if (ac == null) {
            return null;
        }
        if (fixAccess) {
            ac.fixAccess(this._config.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS));
        }
        try {
            return ac.getAnnotated().newInstance(new Object[0]);
        }
        catch (Exception e) {
            Throwable t;
            for (t = e; t.getCause() != null; t = t.getCause()) {}
            ClassUtil.throwIfError(t);
            ClassUtil.throwIfRTE(t);
            throw new IllegalArgumentException("Failed to instantiate bean of type " + this._classInfo.getAnnotated().getName() + ": (" + t.getClass().getName() + ") " + t.getMessage(), t);
        }
    }
    
    @Override
    public AnnotatedMethod findMethod(final String name, final Class<?>[] paramTypes) {
        return this._classInfo.findMethod(name, paramTypes);
    }
    
    @Override
    public JsonFormat.Value findExpectedFormat(JsonFormat.Value defValue) {
        if (this._annotationIntrospector != null) {
            final JsonFormat.Value v = this._annotationIntrospector.findFormat(this._classInfo);
            if (v != null) {
                if (defValue == null) {
                    defValue = v;
                }
                else {
                    defValue = defValue.withOverrides(v);
                }
            }
        }
        final JsonFormat.Value v = this._config.getDefaultPropertyFormat(this._classInfo.getRawType());
        if (v != null) {
            if (defValue == null) {
                defValue = v;
            }
            else {
                defValue = defValue.withOverrides(v);
            }
        }
        return defValue;
    }
    
    @Override
    public Class<?>[] findDefaultViews() {
        if (!this._defaultViewsResolved) {
            this._defaultViewsResolved = true;
            Class<?>[] def = (Class<?>[])((this._annotationIntrospector == null) ? null : this._annotationIntrospector.findViews(this._classInfo));
            if (def == null && !this._config.isEnabled(MapperFeature.DEFAULT_VIEW_INCLUSION)) {
                def = BasicBeanDescription.NO_VIEWS;
            }
            this._defaultViews = def;
        }
        return this._defaultViews;
    }
    
    @Override
    public Converter<Object, Object> findSerializationConverter() {
        if (this._annotationIntrospector == null) {
            return null;
        }
        return this._createConverter(this._annotationIntrospector.findSerializationConverter(this._classInfo));
    }
    
    @Override
    public JsonInclude.Value findPropertyInclusion(final JsonInclude.Value defValue) {
        if (this._annotationIntrospector != null) {
            final JsonInclude.Value incl = this._annotationIntrospector.findPropertyInclusion(this._classInfo);
            if (incl != null) {
                return (defValue == null) ? incl : defValue.withOverrides(incl);
            }
        }
        return defValue;
    }
    
    @Override
    public AnnotatedMember findAnyGetter() throws IllegalArgumentException {
        final AnnotatedMember anyGetter = (this._propCollector == null) ? null : this._propCollector.getAnyGetter();
        if (anyGetter != null) {
            final Class<?> type = anyGetter.getRawType();
            if (!Map.class.isAssignableFrom(type)) {
                throw new IllegalArgumentException("Invalid 'any-getter' annotation on method " + anyGetter.getName() + "(): return type is not instance of java.util.Map");
            }
        }
        return anyGetter;
    }
    
    @Override
    public List<BeanPropertyDefinition> findBackReferences() {
        List<BeanPropertyDefinition> result = null;
        HashSet<String> names = null;
        for (final BeanPropertyDefinition property : this._properties()) {
            final AnnotationIntrospector.ReferenceProperty refDef = property.findReferenceType();
            if (refDef != null) {
                if (!refDef.isBackReference()) {
                    continue;
                }
                final String refName = refDef.getName();
                if (result == null) {
                    result = new ArrayList<BeanPropertyDefinition>();
                    names = new HashSet<String>();
                    names.add(refName);
                }
                else if (!names.add(refName)) {
                    throw new IllegalArgumentException("Multiple back-reference properties with name '" + refName + "'");
                }
                result.add(property);
            }
        }
        return result;
    }
    
    @Deprecated
    @Override
    public Map<String, AnnotatedMember> findBackReferenceProperties() {
        final List<BeanPropertyDefinition> props = this.findBackReferences();
        if (props == null) {
            return null;
        }
        final Map<String, AnnotatedMember> result = new HashMap<String, AnnotatedMember>();
        for (final BeanPropertyDefinition prop : props) {
            result.put(prop.getName(), prop.getMutator());
        }
        return result;
    }
    
    @Override
    public List<AnnotatedMethod> getFactoryMethods() {
        final List<AnnotatedMethod> candidates = this._classInfo.getFactoryMethods();
        if (candidates.isEmpty()) {
            return candidates;
        }
        final ArrayList<AnnotatedMethod> result = new ArrayList<AnnotatedMethod>();
        for (final AnnotatedMethod am : candidates) {
            if (this.isFactoryMethod(am)) {
                result.add(am);
            }
        }
        return result;
    }
    
    @Override
    public Constructor<?> findSingleArgConstructor(final Class<?>... argTypes) {
        for (final AnnotatedConstructor ac : this._classInfo.getConstructors()) {
            if (ac.getParameterCount() == 1) {
                final Class<?> actArg = ac.getRawParameterType(0);
                for (final Class<?> expArg : argTypes) {
                    if (expArg == actArg) {
                        return ac.getAnnotated();
                    }
                }
            }
        }
        return null;
    }
    
    @Override
    public Method findFactoryMethod(final Class<?>... expArgTypes) {
        for (final AnnotatedMethod am : this._classInfo.getFactoryMethods()) {
            if (this.isFactoryMethod(am) && am.getParameterCount() == 1) {
                final Class<?> actualArgType = am.getRawParameterType(0);
                for (final Class<?> expArgType : expArgTypes) {
                    if (actualArgType.isAssignableFrom(expArgType)) {
                        return am.getAnnotated();
                    }
                }
            }
        }
        return null;
    }
    
    protected boolean isFactoryMethod(final AnnotatedMethod am) {
        final Class<?> rt = am.getRawReturnType();
        if (!this.getBeanClass().isAssignableFrom(rt)) {
            return false;
        }
        final JsonCreator.Mode mode = this._annotationIntrospector.findCreatorAnnotation(this._config, am);
        if (mode != null && mode != JsonCreator.Mode.DISABLED) {
            return true;
        }
        final String name = am.getName();
        if ("valueOf".equals(name) && am.getParameterCount() == 1) {
            return true;
        }
        if ("fromString".equals(name) && am.getParameterCount() == 1) {
            final Class<?> cls = am.getRawParameterType(0);
            if (cls == String.class || CharSequence.class.isAssignableFrom(cls)) {
                return true;
            }
        }
        return false;
    }
    
    @Deprecated
    protected PropertyName _findCreatorPropertyName(final AnnotatedParameter param) {
        PropertyName name = this._annotationIntrospector.findNameForDeserialization(param);
        if (name == null || name.isEmpty()) {
            final String str = this._annotationIntrospector.findImplicitPropertyName(param);
            if (str != null && !str.isEmpty()) {
                name = PropertyName.construct(str);
            }
        }
        return name;
    }
    
    @Override
    public Class<?> findPOJOBuilder() {
        return (this._annotationIntrospector == null) ? null : this._annotationIntrospector.findPOJOBuilder(this._classInfo);
    }
    
    @Override
    public JsonPOJOBuilder.Value findPOJOBuilderConfig() {
        return (this._annotationIntrospector == null) ? null : this._annotationIntrospector.findPOJOBuilderConfig(this._classInfo);
    }
    
    @Override
    public Converter<Object, Object> findDeserializationConverter() {
        if (this._annotationIntrospector == null) {
            return null;
        }
        return this._createConverter(this._annotationIntrospector.findDeserializationConverter(this._classInfo));
    }
    
    @Override
    public String findClassDescription() {
        return (this._annotationIntrospector == null) ? null : this._annotationIntrospector.findClassDescription(this._classInfo);
    }
    
    @Deprecated
    public LinkedHashMap<String, AnnotatedField> _findPropertyFields(final Collection<String> ignoredProperties, final boolean forSerialization) {
        final LinkedHashMap<String, AnnotatedField> results = new LinkedHashMap<String, AnnotatedField>();
        for (final BeanPropertyDefinition property : this._properties()) {
            final AnnotatedField f = property.getField();
            if (f != null) {
                final String name = property.getName();
                if (ignoredProperties != null && ignoredProperties.contains(name)) {
                    continue;
                }
                results.put(name, f);
            }
        }
        return results;
    }
    
    protected Converter<Object, Object> _createConverter(final Object converterDef) {
        if (converterDef == null) {
            return null;
        }
        if (converterDef instanceof Converter) {
            return (Converter<Object, Object>)converterDef;
        }
        if (!(converterDef instanceof Class)) {
            throw new IllegalStateException("AnnotationIntrospector returned Converter definition of type " + converterDef.getClass().getName() + "; expected type Converter or Class<Converter> instead");
        }
        final Class<?> converterClass = (Class<?>)converterDef;
        if (converterClass == Converter.None.class || ClassUtil.isBogusClass(converterClass)) {
            return null;
        }
        if (!Converter.class.isAssignableFrom(converterClass)) {
            throw new IllegalStateException("AnnotationIntrospector returned Class " + converterClass.getName() + "; expected Class<Converter>");
        }
        final HandlerInstantiator hi = this._config.getHandlerInstantiator();
        Converter<?, ?> conv = (hi == null) ? null : hi.converterInstance(this._config, this._classInfo, converterClass);
        if (conv == null) {
            conv = ClassUtil.createInstance(converterClass, this._config.canOverrideAccessModifiers());
        }
        return (Converter<Object, Object>)conv;
    }
    
    static {
        NO_VIEWS = new Class[0];
    }
}
