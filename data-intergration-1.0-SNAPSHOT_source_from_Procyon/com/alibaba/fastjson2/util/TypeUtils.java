// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.util;

import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaMetafactory;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ConcurrentSkipListMap;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.BitSet;
import java.util.WeakHashMap;
import java.util.Hashtable;
import java.util.concurrent.TimeUnit;
import java.util.Currency;
import com.alibaba.fastjson2.JSONArray;
import java.util.TreeMap;
import java.util.IdentityHashMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.LongFunction;
import java.util.function.IntFunction;
import java.util.function.ObjIntConsumer;
import java.util.function.ToLongFunction;
import java.util.function.ToIntFunction;
import java.util.function.Supplier;
import java.util.NavigableSet;
import java.util.SortedSet;
import java.util.Arrays;
import java.io.IOException;
import java.util.Calendar;
import java.util.UUID;
import java.util.TreeSet;
import java.util.LinkedHashSet;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.LinkedHashMap;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.OptionalDouble;
import java.util.OptionalLong;
import java.util.OptionalInt;
import java.util.Optional;
import java.math.BigDecimal;
import java.lang.reflect.Proxy;
import com.alibaba.fastjson2.writer.ObjectWriter;
import java.util.function.Function;
import com.alibaba.fastjson2.reader.ObjectReader;
import com.alibaba.fastjson2.writer.ObjectWriterPrimitiveImpl;
import com.alibaba.fastjson2.reader.ObjectReaderImplEnum;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicInteger;
import com.alibaba.fastjson2.reader.ObjectReaderProvider;
import com.alibaba.fastjson2.JSONFactory;
import java.util.Iterator;
import java.lang.reflect.Array;
import com.alibaba.fastjson2.JSON;
import java.util.Collection;
import com.alibaba.fastjson2.reader.ObjectReaderImplInstant;
import com.alibaba.fastjson2.JSONReader;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.Instant;
import java.util.Date;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.WildcardType;
import java.lang.reflect.TypeVariable;
import java.util.List;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.lang.invoke.MethodHandles;
import com.alibaba.fastjson2.JSONException;
import java.lang.reflect.InvocationHandler;
import com.alibaba.fastjson2.JSONObject;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.lang.invoke.MethodHandle;
import java.math.BigInteger;
import java.lang.invoke.MethodType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Field;

public class TypeUtils
{
    public static final Class CLASS_JSON_OBJECT_1x;
    public static final Field FIELD_JSON_OBJECT_1x_map;
    public static final Class CLASS_JSON_ARRAY_1x;
    public static final Class CLASS_SINGLE_SET;
    public static final Class CLASS_SINGLE_LIST;
    public static final Class CLASS_UNMODIFIABLE_COLLECTION;
    public static final Class CLASS_UNMODIFIABLE_LIST;
    public static final Class CLASS_UNMODIFIABLE_SET;
    public static final Class CLASS_UNMODIFIABLE_SORTED_SET;
    public static final Class CLASS_UNMODIFIABLE_NAVIGABLE_SET;
    public static final ParameterizedType PARAM_TYPE_LIST_STR;
    public static final MethodType METHOD_TYPE_SUPPLIER;
    public static final MethodType METHOD_TYPE_FUNCTION;
    public static final MethodType METHOD_TYPE_TO_INT_FUNCTION;
    public static final MethodType METHOD_TYPE_TO_LONG_FUNCTION;
    public static final MethodType METHOD_TYPE_OBJECT_INT_CONSUMER;
    public static final MethodType METHOD_TYPE_INT_FUNCTION;
    public static final MethodType METHOD_TYPE_LONG_FUNCTION;
    public static final MethodType METHOD_TYPE_BI_FUNCTION;
    public static final MethodType METHOD_TYPE_BI_CONSUMER;
    public static final MethodType METHOD_TYPE_VOO;
    public static final MethodType METHOD_TYPE_OBJECT;
    public static final MethodType METHOD_TYPE_OBJECT_OBJECT;
    public static final MethodType METHOD_TYPE_INT_OBJECT;
    public static final MethodType METHOD_TYPE_LONG_OBJECT;
    public static final MethodType METHOD_TYPE_VOID_OBJECT_INT;
    public static final MethodType METHOD_TYPE_OBJECT_LONG;
    public static final MethodType METHOD_TYPE_VOID_LONG;
    public static final MethodType METHOD_TYPE_OBJECT_OBJECT_OBJECT;
    public static final MethodType METHOD_TYPE_VOID;
    public static final MethodType METHOD_TYPE_VOID_INT;
    public static final MethodType METHOD_TYPE_VOID_STRING;
    public static final MethodType METHOD_TYPE_OBJECT_INT;
    public static final BigInteger BIGINT_INT32_MIN;
    public static final BigInteger BIGINT_INT32_MAX;
    public static final BigInteger BIGINT_INT64_MIN;
    public static final BigInteger BIGINT_INT64_MAX;
    public static final double[] SMALL_10_POW;
    static final float[] SINGLE_SMALL_10_POW;
    static final double[] BIG_10_POW;
    static final double[] TINY_10_POW;
    static volatile boolean METHOD_NEW_PROXY_INSTANCE_ERROR;
    static volatile MethodHandle METHOD_NEW_PROXY_INSTANCE;
    static final Cache CACHE;
    static final AtomicReferenceFieldUpdater<Cache, char[]> CHARS_UPDATER;
    static final Map<Class, String> NAME_MAPPINGS;
    static final Map<String, Class> TYPE_MAPPINGS;
    private static final BigInteger[] BIG_TEN_POWERS_TABLE;
    
    public static <T> T newProxyInstance(final Class<T> objectClass, final JSONObject object) {
        MethodHandle newProxyInstance = TypeUtils.METHOD_NEW_PROXY_INSTANCE;
        try {
            if (newProxyInstance == null) {
                final Class<?> proxyClass = Class.forName("java.lang.reflect.Proxy");
                final MethodHandles.Lookup lookup = JDKUtils.trustedLookup(proxyClass);
                newProxyInstance = (TypeUtils.METHOD_NEW_PROXY_INSTANCE = lookup.findStatic(proxyClass, "newProxyInstance", MethodType.methodType(Object.class, ClassLoader.class, Class[].class, InvocationHandler.class)));
            }
        }
        catch (Throwable ignored) {
            TypeUtils.METHOD_NEW_PROXY_INSTANCE_ERROR = true;
        }
        try {
            return (T)newProxyInstance.invokeExact(objectClass.getClassLoader(), new Class[] { objectClass }, (InvocationHandler)object);
        }
        catch (Throwable e) {
            throw new JSONException("create proxy error : " + objectClass, e);
        }
    }
    
    static char[] toAsciiCharArray(final byte[] bytes) {
        final char[] charArray = new char[bytes.length];
        for (int i = 0; i < bytes.length; ++i) {
            charArray[i] = (char)bytes[i];
        }
        return charArray;
    }
    
    public static String toString(final char ch) {
        if (ch < X2.chars.length) {
            return X2.chars[ch];
        }
        return Character.toString(ch);
    }
    
    public static String toString(final byte ch) {
        if (ch >= 0 && ch < X2.chars.length) {
            return X2.chars[ch];
        }
        return new String(new byte[] { ch }, StandardCharsets.ISO_8859_1);
    }
    
    public static String toString(final char c0, final char c1) {
        if (c0 >= ' ' && c0 <= '~' && c1 >= ' ' && c1 <= '~') {
            final int value = (c0 - ' ') * 95 + (c1 - ' ');
            return X2.chars2[value];
        }
        return new String(new char[] { c0, c1 });
    }
    
    public static String toString(final byte c0, final byte c1) {
        if (c0 >= 32 && c0 <= 126 && c1 >= 32 && c1 <= 126) {
            final int value = (c0 - 32) * 95 + (c1 - 32);
            return X2.chars2[value];
        }
        return new String(new byte[] { c0, c1 }, StandardCharsets.ISO_8859_1);
    }
    
    public static Type intern(final Type type) {
        if (type instanceof ParameterizedType) {
            final ParameterizedType paramType = (ParameterizedType)type;
            final Type rawType = paramType.getRawType();
            final Type[] actualTypeArguments = paramType.getActualTypeArguments();
            if (rawType == List.class && actualTypeArguments.length == 1 && actualTypeArguments[0] == String.class) {
                return TypeUtils.PARAM_TYPE_LIST_STR;
            }
        }
        return type;
    }
    
    public static double parseDouble(final byte[] in, final int off, final int len) throws NumberFormatException {
        boolean isNegative = false;
        boolean signSeen = false;
        final int end = off + len;
        try {
            if (len == 0) {
                throw new NumberFormatException("empty String");
            }
            int i = off;
            switch (in[i]) {
                case 45: {
                    isNegative = true;
                }
                case 43: {
                    ++i;
                    signSeen = true;
                    break;
                }
            }
            final char[] digits = new char[len];
            int nDigits = 0;
            boolean decSeen = false;
            int decPt = 0;
            int nLeadZero = 0;
            int nTrailZero = 0;
            while (i < end) {
                final byte c = in[i];
                if (c == 48) {
                    ++nLeadZero;
                }
                else {
                    if (c != 46) {
                        break;
                    }
                    if (decSeen) {
                        throw new NumberFormatException("multiple points");
                    }
                    decPt = i - off;
                    if (signSeen) {
                        --decPt;
                    }
                    decSeen = true;
                }
                ++i;
            }
            while (i < end) {
                final byte c = in[i];
                if (c >= 49 && c <= 57) {
                    digits[nDigits++] = (char)c;
                    nTrailZero = 0;
                }
                else if (c == 48) {
                    digits[nDigits++] = (char)c;
                    ++nTrailZero;
                }
                else {
                    if (c != 46) {
                        break;
                    }
                    if (decSeen) {
                        throw new NumberFormatException("multiple points");
                    }
                    decPt = i - off;
                    if (signSeen) {
                        --decPt;
                    }
                    decSeen = true;
                }
                ++i;
            }
            nDigits -= nTrailZero;
            final boolean isZero = nDigits == 0;
            if (!isZero || nLeadZero != 0) {
                int decExp;
                if (decSeen) {
                    decExp = decPt - nLeadZero;
                }
                else {
                    decExp = nDigits + nTrailZero;
                }
                byte c;
                if (i < end && ((c = in[i]) == 101 || c == 69)) {
                    int expSign = 1;
                    int expVal = 0;
                    final int reallyBig = 214748364;
                    boolean expOverflow = false;
                    switch (in[++i]) {
                        case 45: {
                            expSign = -1;
                        }
                        case 43: {
                            ++i;
                            break;
                        }
                    }
                    final int expAt = i;
                    while (i < end) {
                        if (expVal >= reallyBig) {
                            expOverflow = true;
                        }
                        c = in[i++];
                        if (c < 48 || c > 57) {
                            --i;
                            break;
                        }
                        expVal = expVal * 10 + (c - 48);
                    }
                    final int BIG_DECIMAL_EXPONENT = 324;
                    final int expLimit = 324 + nDigits + nTrailZero;
                    if (expOverflow || expVal > expLimit) {
                        decExp = expSign * expLimit;
                    }
                    else {
                        decExp += expSign * expVal;
                    }
                    if (i == expAt) {
                        throw new NumberFormatException("For input string: \"" + new String(in, off, len) + "\"");
                    }
                }
                if (i >= end || i == end - 1) {
                    if (isZero) {
                        return 0.0;
                    }
                    return doubleValue(isNegative, decExp, digits, nDigits);
                }
            }
        }
        catch (StringIndexOutOfBoundsException ex) {}
        throw new NumberFormatException("For input string: \"" + new String(in, off, len) + "\"");
    }
    
    public static double parseDouble(final char[] in, final int off, final int len) throws NumberFormatException {
        boolean isNegative = false;
        boolean signSeen = false;
        final int end = off + len;
        try {
            if (len == 0) {
                throw new NumberFormatException("empty String");
            }
            int i = off;
            switch (in[i]) {
                case '-': {
                    isNegative = true;
                }
                case '+': {
                    ++i;
                    signSeen = true;
                    break;
                }
            }
            final char[] digits = new char[len];
            int nDigits = 0;
            boolean decSeen = false;
            int decPt = 0;
            int nLeadZero = 0;
            int nTrailZero = 0;
            while (i < end) {
                final char c = in[i];
                if (c == '0') {
                    ++nLeadZero;
                }
                else {
                    if (c != '.') {
                        break;
                    }
                    if (decSeen) {
                        throw new NumberFormatException("multiple points");
                    }
                    decPt = i - off;
                    if (signSeen) {
                        --decPt;
                    }
                    decSeen = true;
                }
                ++i;
            }
            while (i < end) {
                final char c = in[i];
                if (c >= '1' && c <= '9') {
                    digits[nDigits++] = c;
                    nTrailZero = 0;
                }
                else if (c == '0') {
                    digits[nDigits++] = c;
                    ++nTrailZero;
                }
                else {
                    if (c != '.') {
                        break;
                    }
                    if (decSeen) {
                        throw new NumberFormatException("multiple points");
                    }
                    decPt = i - off;
                    if (signSeen) {
                        --decPt;
                    }
                    decSeen = true;
                }
                ++i;
            }
            nDigits -= nTrailZero;
            final boolean isZero = nDigits == 0;
            if (!isZero || nLeadZero != 0) {
                int decExp;
                if (decSeen) {
                    decExp = decPt - nLeadZero;
                }
                else {
                    decExp = nDigits + nTrailZero;
                }
                char c;
                if (i < end && ((c = in[i]) == 'e' || c == 'E')) {
                    int expSign = 1;
                    int expVal = 0;
                    final int reallyBig = 214748364;
                    boolean expOverflow = false;
                    switch (in[++i]) {
                        case '-': {
                            expSign = -1;
                        }
                        case '+': {
                            ++i;
                            break;
                        }
                    }
                    final int expAt = i;
                    while (i < end) {
                        if (expVal >= reallyBig) {
                            expOverflow = true;
                        }
                        c = in[i++];
                        if (c < '0' || c > '9') {
                            --i;
                            break;
                        }
                        expVal = expVal * 10 + (c - '0');
                    }
                    final int BIG_DECIMAL_EXPONENT = 324;
                    final int expLimit = 324 + nDigits + nTrailZero;
                    if (expOverflow || expVal > expLimit) {
                        decExp = expSign * expLimit;
                    }
                    else {
                        decExp += expSign * expVal;
                    }
                    if (i == expAt) {
                        throw new NumberFormatException("For input string: \"" + new String(in, off, len) + "\"");
                    }
                }
                if (i >= end || i == end - 1) {
                    if (isZero) {
                        return 0.0;
                    }
                    return doubleValue(isNegative, decExp, digits, nDigits);
                }
            }
        }
        catch (StringIndexOutOfBoundsException ex) {}
        throw new NumberFormatException("For input string: \"" + new String(in, off, len) + "\"");
    }
    
    public static float parseFloat(final byte[] in, final int off, final int len) throws NumberFormatException {
        boolean isNegative = false;
        boolean signSeen = false;
        final int end = off + len;
        try {
            if (len == 0) {
                throw new NumberFormatException("empty String");
            }
            int i = off;
            switch (in[i]) {
                case 45: {
                    isNegative = true;
                }
                case 43: {
                    ++i;
                    signSeen = true;
                    break;
                }
            }
            final char[] digits = new char[len];
            int nDigits = 0;
            boolean decSeen = false;
            int decPt = 0;
            int nLeadZero = 0;
            int nTrailZero = 0;
            while (i < end) {
                final byte c = in[i];
                if (c == 48) {
                    ++nLeadZero;
                }
                else {
                    if (c != 46) {
                        break;
                    }
                    if (decSeen) {
                        throw new NumberFormatException("multiple points");
                    }
                    decPt = i - off;
                    if (signSeen) {
                        --decPt;
                    }
                    decSeen = true;
                }
                ++i;
            }
            while (i < end) {
                final byte c = in[i];
                if (c >= 49 && c <= 57) {
                    digits[nDigits++] = (char)c;
                    nTrailZero = 0;
                }
                else if (c == 48) {
                    digits[nDigits++] = (char)c;
                    ++nTrailZero;
                }
                else {
                    if (c != 46) {
                        break;
                    }
                    if (decSeen) {
                        throw new NumberFormatException("multiple points");
                    }
                    decPt = i - off;
                    if (signSeen) {
                        --decPt;
                    }
                    decSeen = true;
                }
                ++i;
            }
            nDigits -= nTrailZero;
            final boolean isZero = nDigits == 0;
            if (!isZero || nLeadZero != 0) {
                int decExp;
                if (decSeen) {
                    decExp = decPt - nLeadZero;
                }
                else {
                    decExp = nDigits + nTrailZero;
                }
                byte c;
                if (i < end && ((c = in[i]) == 101 || c == 69)) {
                    int expSign = 1;
                    int expVal = 0;
                    final int reallyBig = 214748364;
                    boolean expOverflow = false;
                    switch (in[++i]) {
                        case 45: {
                            expSign = -1;
                        }
                        case 43: {
                            ++i;
                            break;
                        }
                    }
                    final int expAt = i;
                    while (i < end) {
                        if (expVal >= reallyBig) {
                            expOverflow = true;
                        }
                        c = in[i++];
                        if (c < 48 || c > 57) {
                            --i;
                            break;
                        }
                        expVal = expVal * 10 + (c - 48);
                    }
                    final int BIG_DECIMAL_EXPONENT = 324;
                    final int expLimit = 324 + nDigits + nTrailZero;
                    if (expOverflow || expVal > expLimit) {
                        decExp = expSign * expLimit;
                    }
                    else {
                        decExp += expSign * expVal;
                    }
                    if (i == expAt) {
                        throw new NumberFormatException("For input string: \"" + new String(in, off, len) + "\"");
                    }
                }
                if (i >= end || i == end - 1) {
                    if (isZero) {
                        return 0.0f;
                    }
                    return floatValue(isNegative, decExp, digits, nDigits);
                }
            }
        }
        catch (StringIndexOutOfBoundsException ex) {}
        throw new NumberFormatException("For input string: \"" + new String(in, off, len) + "\"");
    }
    
    public static float parseFloat(final char[] in, final int off, final int len) throws NumberFormatException {
        boolean isNegative = false;
        boolean signSeen = false;
        final int end = off + len;
        try {
            if (len == 0) {
                throw new NumberFormatException("empty String");
            }
            int i = off;
            switch (in[i]) {
                case '-': {
                    isNegative = true;
                }
                case '+': {
                    ++i;
                    signSeen = true;
                    break;
                }
            }
            final char[] digits = new char[len];
            int nDigits = 0;
            boolean decSeen = false;
            int decPt = 0;
            int nLeadZero = 0;
            int nTrailZero = 0;
            while (i < end) {
                final char c = in[i];
                if (c == '0') {
                    ++nLeadZero;
                }
                else {
                    if (c != '.') {
                        break;
                    }
                    if (decSeen) {
                        throw new NumberFormatException("multiple points");
                    }
                    decPt = i - off;
                    if (signSeen) {
                        --decPt;
                    }
                    decSeen = true;
                }
                ++i;
            }
            while (i < end) {
                final char c = in[i];
                if (c >= '1' && c <= '9') {
                    digits[nDigits++] = c;
                    nTrailZero = 0;
                }
                else if (c == '0') {
                    digits[nDigits++] = c;
                    ++nTrailZero;
                }
                else {
                    if (c != '.') {
                        break;
                    }
                    if (decSeen) {
                        throw new NumberFormatException("multiple points");
                    }
                    decPt = i - off;
                    if (signSeen) {
                        --decPt;
                    }
                    decSeen = true;
                }
                ++i;
            }
            nDigits -= nTrailZero;
            final boolean isZero = nDigits == 0;
            if (!isZero || nLeadZero != 0) {
                int decExp;
                if (decSeen) {
                    decExp = decPt - nLeadZero;
                }
                else {
                    decExp = nDigits + nTrailZero;
                }
                char c;
                if (i < end && ((c = in[i]) == 'e' || c == 'E')) {
                    int expSign = 1;
                    int expVal = 0;
                    final int reallyBig = 214748364;
                    boolean expOverflow = false;
                    switch (in[++i]) {
                        case '-': {
                            expSign = -1;
                        }
                        case '+': {
                            ++i;
                            break;
                        }
                    }
                    final int expAt = i;
                    while (i < end) {
                        if (expVal >= reallyBig) {
                            expOverflow = true;
                        }
                        c = in[i++];
                        if (c < '0' || c > '9') {
                            --i;
                            break;
                        }
                        expVal = expVal * 10 + (c - '0');
                    }
                    final int BIG_DECIMAL_EXPONENT = 324;
                    final int expLimit = 324 + nDigits + nTrailZero;
                    if (expOverflow || expVal > expLimit) {
                        decExp = expSign * expLimit;
                    }
                    else {
                        decExp += expSign * expVal;
                    }
                    if (i == expAt) {
                        throw new NumberFormatException("For input string: \"" + new String(in, off, len) + "\"");
                    }
                }
                if (i >= end || i == end - 1) {
                    if (isZero) {
                        return 0.0f;
                    }
                    return floatValue(isNegative, decExp, digits, nDigits);
                }
            }
        }
        catch (StringIndexOutOfBoundsException ex) {}
        throw new NumberFormatException("For input string: \"" + new String(in, off, len) + "\"");
    }
    
    public static double doubleValue(final boolean isNegative, final int decExp, final char[] digits, int nDigits) {
        final int MAX_DECIMAL_EXPONENT = 308;
        final int MIN_DECIMAL_EXPONENT = -324;
        final int MAX_NDIGITS = 1100;
        final int INT_DECIMAL_DIGITS = 9;
        final int MAX_DECIMAL_DIGITS = 15;
        final int DOUBLE_EXP_BIAS = 1023;
        final int EXP_SHIFT = 52;
        final long FRACT_HOB = 4503599627370496L;
        final int MAX_SMALL_TEN = TypeUtils.SMALL_10_POW.length - 1;
        final int SINGLE_MAX_SMALL_TEN = TypeUtils.SINGLE_SMALL_10_POW.length - 1;
        final int kDigits = Math.min(nDigits, 16);
        int iValue = digits[0] - '0';
        final int iDigits = Math.min(kDigits, 9);
        for (int i = 1; i < iDigits; ++i) {
            iValue = iValue * 10 + digits[i] - 48;
        }
        long lValue = iValue;
        for (int j = iDigits; j < kDigits; ++j) {
            lValue = lValue * 10L + (digits[j] - '0');
        }
        double dValue = (double)lValue;
        int exp = decExp - kDigits;
        if (nDigits <= 15) {
            if (exp == 0 || dValue == 0.0) {
                return isNegative ? (-dValue) : dValue;
            }
            if (exp >= 0) {
                if (exp <= MAX_SMALL_TEN) {
                    final double rValue = dValue * TypeUtils.SMALL_10_POW[exp];
                    return isNegative ? (-rValue) : rValue;
                }
                final int slop = 15 - kDigits;
                if (exp <= MAX_SMALL_TEN + slop) {
                    dValue *= TypeUtils.SMALL_10_POW[slop];
                    final double rValue2 = dValue * TypeUtils.SMALL_10_POW[exp - slop];
                    return isNegative ? (-rValue2) : rValue2;
                }
            }
            else if (exp >= -MAX_SMALL_TEN) {
                final double rValue = dValue / TypeUtils.SMALL_10_POW[-exp];
                return isNegative ? (-rValue) : rValue;
            }
        }
        if (exp > 0) {
            if (decExp > 309) {
                return isNegative ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
            }
            if ((exp & 0xF) != 0x0) {
                dValue *= TypeUtils.SMALL_10_POW[exp & 0xF];
            }
            if ((exp >>= 4) != 0) {
                int k = 0;
                while (exp > 1) {
                    if ((exp & 0x1) != 0x0) {
                        dValue *= TypeUtils.BIG_10_POW[k];
                    }
                    ++k;
                    exp >>= 1;
                }
                double t = dValue * TypeUtils.BIG_10_POW[k];
                if (Double.isInfinite(t)) {
                    t = dValue / 2.0;
                    t *= TypeUtils.BIG_10_POW[k];
                    if (Double.isInfinite(t)) {
                        return isNegative ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
                    }
                    t = Double.MAX_VALUE;
                }
                dValue = t;
            }
        }
        else if (exp < 0) {
            exp = -exp;
            if (decExp < -325) {
                return isNegative ? -0.0 : 0.0;
            }
            if ((exp & 0xF) != 0x0) {
                dValue /= TypeUtils.SMALL_10_POW[exp & 0xF];
            }
            if ((exp >>= 4) != 0) {
                int k = 0;
                while (exp > 1) {
                    if ((exp & 0x1) != 0x0) {
                        dValue *= TypeUtils.TINY_10_POW[k];
                    }
                    ++k;
                    exp >>= 1;
                }
                double t = dValue * TypeUtils.TINY_10_POW[k];
                if (t == 0.0) {
                    t = dValue * 2.0;
                    t *= TypeUtils.TINY_10_POW[k];
                    if (t == 0.0) {
                        return isNegative ? -0.0 : 0.0;
                    }
                    t = Double.MIN_VALUE;
                }
                dValue = t;
            }
        }
        if (nDigits > 1100) {
            nDigits = 1101;
            digits[1100] = '1';
        }
        FDBigInteger bigD0 = new FDBigInteger(lValue, digits, kDigits, nDigits);
        exp = decExp - nDigits;
        long ieeeBits = Double.doubleToRawLongBits(dValue);
        final int B5 = Math.max(0, -exp);
        final int D5 = Math.max(0, exp);
        bigD0 = bigD0.multByPow52(D5, 0);
        bigD0.makeImmutable();
        FDBigInteger bigD2 = null;
        int prevD2 = 0;
        do {
            int binexp = (int)(ieeeBits >>> 52);
            final long DOUBLE_SIGNIF_BIT_MASK = 4503599627370495L;
            long bigBbits = ieeeBits & 0xFFFFFFFFFFFFFL;
            if (binexp > 0) {
                bigBbits |= 0x10000000000000L;
            }
            else {
                assert bigBbits != 0L : bigBbits;
                final int leadingZeros = Long.numberOfLeadingZeros(bigBbits);
                final int shift = leadingZeros - 11;
                bigBbits <<= shift;
                binexp = 1 - shift;
            }
            binexp -= 1023;
            final int lowOrderZeros = Long.numberOfTrailingZeros(bigBbits);
            bigBbits >>>= lowOrderZeros;
            final int bigIntExp = binexp - 52 + lowOrderZeros;
            final int bigIntNBits = 53 - lowOrderZeros;
            int B6 = B5;
            int D6 = D5;
            if (bigIntExp >= 0) {
                B6 += bigIntExp;
            }
            else {
                D6 -= bigIntExp;
            }
            int Ulp2 = B6;
            int hulpbias;
            if (binexp <= -1023) {
                hulpbias = binexp + lowOrderZeros + 1023;
            }
            else {
                hulpbias = 1 + lowOrderZeros;
            }
            B6 += hulpbias;
            D6 += hulpbias;
            final int common2 = Math.min(B6, Math.min(D6, Ulp2));
            B6 -= common2;
            D6 -= common2;
            Ulp2 -= common2;
            final FDBigInteger bigB = FDBigInteger.valueOfMulPow52(bigBbits, B5, B6);
            if (bigD2 == null || prevD2 != D6) {
                bigD2 = bigD0.leftShift(D6);
                prevD2 = D6;
            }
            int cmpResult;
            boolean overvalue;
            FDBigInteger diff;
            if ((cmpResult = bigB.cmp(bigD2)) > 0) {
                overvalue = true;
                diff = bigB.leftInplaceSub(bigD2);
                if (bigIntNBits == 1 && bigIntExp > -1022 && --Ulp2 < 0) {
                    Ulp2 = 0;
                    diff = diff.leftShift(1);
                }
            }
            else {
                if (cmpResult >= 0) {
                    break;
                }
                overvalue = false;
                diff = bigD2.rightInplaceSub(bigB);
            }
            cmpResult = diff.cmpPow52(B5, Ulp2);
            if (cmpResult < 0) {
                break;
            }
            if (cmpResult == 0) {
                if ((ieeeBits & 0x1L) != 0x0L) {
                    ieeeBits += (overvalue ? -1L : 1L);
                    break;
                }
                break;
            }
            else {
                ieeeBits += (overvalue ? -1L : 1L);
                final long DOUBLE_EXP_BIT_MASK = 9218868437227405312L;
            }
        } while (ieeeBits != 0L && ieeeBits != 9218868437227405312L);
        if (isNegative) {
            final long DOUBLE_SIGN_BIT_MASK = Long.MIN_VALUE;
            ieeeBits |= Long.MIN_VALUE;
        }
        return Double.longBitsToDouble(ieeeBits);
    }
    
    public static float floatValue(final boolean isNegative, final int decExponent, final char[] digits, int nDigits) {
        final int SINGLE_MAX_NDIGITS = 200;
        final int SINGLE_MAX_DECIMAL_DIGITS = 7;
        final int MAX_DECIMAL_DIGITS = 15;
        final int FLOAT_EXP_BIAS = 127;
        final int SINGLE_EXP_SHIFT = 23;
        final int SINGLE_MAX_SMALL_TEN = TypeUtils.SINGLE_SMALL_10_POW.length - 1;
        final int kDigits = Math.min(nDigits, 8);
        int iValue = digits[0] - '0';
        for (int i = 1; i < kDigits; ++i) {
            iValue = iValue * 10 + digits[i] - 48;
        }
        float fValue = (float)iValue;
        int exp = decExponent - kDigits;
        if (nDigits <= 7) {
            if (exp == 0 || fValue == 0.0f) {
                return isNegative ? (-fValue) : fValue;
            }
            if (exp >= 0) {
                if (exp <= SINGLE_MAX_SMALL_TEN) {
                    fValue *= TypeUtils.SINGLE_SMALL_10_POW[exp];
                    return isNegative ? (-fValue) : fValue;
                }
                final int slop = 7 - kDigits;
                if (exp <= SINGLE_MAX_SMALL_TEN + slop) {
                    fValue *= TypeUtils.SINGLE_SMALL_10_POW[slop];
                    fValue *= TypeUtils.SINGLE_SMALL_10_POW[exp - slop];
                    return isNegative ? (-fValue) : fValue;
                }
            }
            else if (exp >= -SINGLE_MAX_SMALL_TEN) {
                fValue /= TypeUtils.SINGLE_SMALL_10_POW[-exp];
                return isNegative ? (-fValue) : fValue;
            }
        }
        else if (decExponent >= nDigits && nDigits + decExponent <= 15) {
            long lValue = iValue;
            for (int j = kDigits; j < nDigits; ++j) {
                lValue = lValue * 10L + (digits[j] - '0');
            }
            double dValue = (double)lValue;
            exp = decExponent - nDigits;
            dValue *= TypeUtils.SMALL_10_POW[exp];
            fValue = (float)dValue;
            return isNegative ? (-fValue) : fValue;
        }
        double dValue2 = fValue;
        if (exp > 0) {
            final int SINGLE_MAX_DECIMAL_EXPONENT = 38;
            if (decExponent > 39) {
                return isNegative ? Float.NEGATIVE_INFINITY : Float.POSITIVE_INFINITY;
            }
            if ((exp & 0xF) != 0x0) {
                dValue2 *= TypeUtils.SMALL_10_POW[exp & 0xF];
            }
            if ((exp >>= 4) != 0) {
                int k = 0;
                while (exp > 0) {
                    if ((exp & 0x1) != 0x0) {
                        dValue2 *= TypeUtils.BIG_10_POW[k];
                    }
                    ++k;
                    exp >>= 1;
                }
            }
        }
        else if (exp < 0) {
            exp = -exp;
            final int SINGLE_MIN_DECIMAL_EXPONENT = -45;
            if (decExponent < -46) {
                return isNegative ? -0.0f : 0.0f;
            }
            if ((exp & 0xF) != 0x0) {
                dValue2 /= TypeUtils.SMALL_10_POW[exp & 0xF];
            }
            if ((exp >>= 4) != 0) {
                int k = 0;
                while (exp > 0) {
                    if ((exp & 0x1) != 0x0) {
                        dValue2 *= TypeUtils.TINY_10_POW[k];
                    }
                    ++k;
                    exp >>= 1;
                }
            }
        }
        fValue = Math.max(Float.MIN_VALUE, Math.min(Float.MAX_VALUE, (float)dValue2));
        if (nDigits > 200) {
            nDigits = 201;
            digits[200] = '1';
        }
        FDBigInteger bigD0 = new FDBigInteger(iValue, digits, kDigits, nDigits);
        exp = decExponent - nDigits;
        int ieeeBits = Float.floatToRawIntBits(fValue);
        final int B5 = Math.max(0, -exp);
        final int D5 = Math.max(0, exp);
        bigD0 = bigD0.multByPow52(D5, 0);
        bigD0.makeImmutable();
        FDBigInteger bigD2 = null;
        int prevD2 = 0;
        while (true) {
            int binexp = ieeeBits >>> 23;
            final int FLOAT_SIGNIF_BIT_MASK = 8388607;
            int bigBbits = ieeeBits & 0x7FFFFF;
            if (binexp > 0) {
                final int SINGLE_FRACT_HOB = 8388608;
                bigBbits |= 0x800000;
            }
            else {
                assert bigBbits != 0 : bigBbits;
                final int leadingZeros = Integer.numberOfLeadingZeros(bigBbits);
                final int shift = leadingZeros - 8;
                bigBbits <<= shift;
                binexp = 1 - shift;
            }
            binexp -= 127;
            final int lowOrderZeros = Integer.numberOfTrailingZeros(bigBbits);
            bigBbits >>>= lowOrderZeros;
            final int bigIntExp = binexp - 23 + lowOrderZeros;
            final int bigIntNBits = 24 - lowOrderZeros;
            int B6 = B5;
            int D6 = D5;
            if (bigIntExp >= 0) {
                B6 += bigIntExp;
            }
            else {
                D6 -= bigIntExp;
            }
            int Ulp2 = B6;
            int hulpbias;
            if (binexp <= -127) {
                hulpbias = binexp + lowOrderZeros + 127;
            }
            else {
                hulpbias = 1 + lowOrderZeros;
            }
            B6 += hulpbias;
            D6 += hulpbias;
            final int common2 = Math.min(B6, Math.min(D6, Ulp2));
            B6 -= common2;
            D6 -= common2;
            Ulp2 -= common2;
            final FDBigInteger bigB = FDBigInteger.valueOfMulPow52(bigBbits, B5, B6);
            if (bigD2 == null || prevD2 != D6) {
                bigD2 = bigD0.leftShift(D6);
                prevD2 = D6;
            }
            int cmpResult;
            boolean overvalue;
            FDBigInteger diff;
            if ((cmpResult = bigB.cmp(bigD2)) > 0) {
                overvalue = true;
                diff = bigB.leftInplaceSub(bigD2);
                if (bigIntNBits == 1 && bigIntExp > -126 && --Ulp2 < 0) {
                    Ulp2 = 0;
                    diff = diff.leftShift(1);
                }
            }
            else {
                if (cmpResult >= 0) {
                    break;
                }
                overvalue = false;
                diff = bigD2.rightInplaceSub(bigB);
            }
            cmpResult = diff.cmpPow52(B5, Ulp2);
            if (cmpResult < 0) {
                break;
            }
            if (cmpResult == 0) {
                if ((ieeeBits & 0x1) != 0x0) {
                    ieeeBits += (overvalue ? -1 : 1);
                    break;
                }
                break;
            }
            else {
                ieeeBits += (overvalue ? -1 : 1);
                final int FLOAT_EXP_BIT_MASK = 2139095040;
                if (ieeeBits == 0) {
                    break;
                }
                if (ieeeBits == 2139095040) {
                    break;
                }
                continue;
            }
        }
        if (isNegative) {
            final int FLOAT_SIGN_BIT_MASK = Integer.MIN_VALUE;
            ieeeBits |= Integer.MIN_VALUE;
        }
        return Float.intBitsToFloat(ieeeBits);
    }
    
    public static Class<?> getMapping(final Type type) {
        if (type == null) {
            return null;
        }
        if (type.getClass() == Class.class) {
            return (Class<?>)type;
        }
        if (type instanceof ParameterizedType) {
            return getMapping(((ParameterizedType)type).getRawType());
        }
        if (type instanceof TypeVariable) {
            final Type boundType = ((TypeVariable)type).getBounds()[0];
            if (boundType instanceof Class) {
                return (Class<?>)boundType;
            }
            return getMapping(boundType);
        }
        else {
            if (type instanceof WildcardType) {
                final Type[] upperBounds = ((WildcardType)type).getUpperBounds();
                if (upperBounds.length == 1) {
                    return getMapping(upperBounds[0]);
                }
            }
            if (type instanceof GenericArrayType) {
                final Type genericComponentType = ((GenericArrayType)type).getGenericComponentType();
                final Class<?> componentClass = getClass(genericComponentType);
                return getArrayClass(componentClass);
            }
            return Object.class;
        }
    }
    
    public static Date toDate(final Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Date) {
            return (Date)obj;
        }
        if (obj instanceof Instant) {
            final Instant instant = (Instant)obj;
            return new Date(instant.toEpochMilli());
        }
        if (obj instanceof ZonedDateTime) {
            final ZonedDateTime zdt = (ZonedDateTime)obj;
            return new Date(zdt.toInstant().toEpochMilli());
        }
        if (obj instanceof LocalDate) {
            final LocalDate localDate = (LocalDate)obj;
            final ZonedDateTime zdt2 = localDate.atStartOfDay(ZoneId.systemDefault());
            return new Date(zdt2.toInstant().toEpochMilli());
        }
        if (obj instanceof LocalDateTime) {
            final LocalDateTime ldt = (LocalDateTime)obj;
            final ZonedDateTime zdt2 = ldt.atZone(ZoneId.systemDefault());
            return new Date(zdt2.toInstant().toEpochMilli());
        }
        if (obj instanceof String) {
            return DateUtils.parseDate((String)obj);
        }
        if (obj instanceof Long || obj instanceof Integer) {
            return new Date(((Number)obj).longValue());
        }
        throw new JSONException("can not cast to Date from " + obj.getClass());
    }
    
    public static Instant toInstant(final Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Instant) {
            return (Instant)obj;
        }
        if (obj instanceof Date) {
            return ((Date)obj).toInstant();
        }
        if (obj instanceof ZonedDateTime) {
            final ZonedDateTime zdt = (ZonedDateTime)obj;
            return zdt.toInstant();
        }
        if (obj instanceof String) {
            final String str = (String)obj;
            if (str.isEmpty() || "null".equals(str)) {
                return null;
            }
            JSONReader jsonReader;
            if (str.charAt(0) != '\"') {
                jsonReader = JSONReader.of('\"' + str + '\"');
            }
            else {
                jsonReader = JSONReader.of(str);
            }
            return jsonReader.read(Instant.class);
        }
        else {
            if (obj instanceof Map) {
                return (Instant)ObjectReaderImplInstant.INSTANCE.createInstance((Map)obj, 0L);
            }
            throw new JSONException("can not cast to Date from " + obj.getClass());
        }
    }
    
    public static Object[] cast(final Object obj, final Type[] types) {
        if (obj == null) {
            return null;
        }
        final Object[] array = new Object[types.length];
        if (obj instanceof Collection) {
            int i = 0;
            for (final Object item : (Collection)obj) {
                final int index = i++;
                array[index] = cast(item, types[index]);
            }
        }
        else {
            final Class<?> objectClass = obj.getClass();
            if (!objectClass.isArray()) {
                throw new JSONException("can not cast to types " + JSON.toJSONString(types) + " from " + objectClass);
            }
            for (int length = Array.getLength(obj), j = 0; j < array.length && j < length; ++j) {
                final Object item2 = Array.get(obj, j);
                array[j] = cast(item2, types[j]);
            }
        }
        return array;
    }
    
    public static String[] toStringArray(final Object object) {
        if (object == null || object instanceof String[]) {
            return (String[])object;
        }
        if (object instanceof Collection) {
            final Collection collection = (Collection)object;
            final String[] array = new String[collection.size()];
            int i = 0;
            for (final Object item : (Collection)object) {
                final int index = i++;
                array[index] = (String)((item == null || item instanceof String) ? item : item.toString());
            }
            return array;
        }
        final Class<?> objectClass = object.getClass();
        if (objectClass.isArray()) {
            final int length = Array.getLength(object);
            final String[] array2 = new String[length];
            for (int j = 0; j < array2.length; ++j) {
                final Object item = Array.get(object, j);
                array2[j] = (String)((item == null || item instanceof String) ? item : item.toString());
            }
            return array2;
        }
        return cast(object, String[].class);
    }
    
    public static <T> T cast(final Object obj, final Type type) {
        final ObjectReaderProvider provider = JSONFactory.getDefaultObjectReaderProvider();
        if (type instanceof Class) {
            return cast(obj, (Class<T>)type, provider);
        }
        if (obj instanceof Collection) {
            return provider.getObjectReader(type).createInstance((Collection)obj);
        }
        if (obj instanceof Map) {
            return provider.getObjectReader(type).createInstance((Map)obj, 0L);
        }
        return JSON.parseObject(JSON.toJSONString(obj), type);
    }
    
    public static <T> T cast(final Object obj, final Class<T> targetClass) {
        return cast(obj, targetClass, JSONFactory.getDefaultObjectReaderProvider());
    }
    
    public static <T> T cast(final Object obj, final Class<T> targetClass, final ObjectReaderProvider provider) {
        if (obj == null) {
            return null;
        }
        if (targetClass.isInstance(obj)) {
            return (T)obj;
        }
        if (targetClass == Date.class) {
            return (T)toDate(obj);
        }
        if (targetClass == Instant.class) {
            return (T)toInstant(obj);
        }
        if (targetClass == String.class) {
            if (obj instanceof Character) {
                return (T)obj.toString();
            }
            return (T)JSON.toJSONString(obj);
        }
        else {
            if (targetClass == AtomicInteger.class) {
                return (T)new AtomicInteger(toIntValue(obj));
            }
            if (targetClass == AtomicLong.class) {
                return (T)new AtomicLong(toLongValue(obj));
            }
            if (targetClass == AtomicBoolean.class) {
                return (T)new AtomicBoolean((boolean)obj);
            }
            if (obj instanceof Map) {
                final ObjectReader objectReader = provider.getObjectReader(targetClass);
                return objectReader.createInstance((Map)obj, 0L);
            }
            final Function typeConvert = provider.getTypeConvert(obj.getClass(), targetClass);
            if (typeConvert != null) {
                return typeConvert.apply(obj);
            }
            if (obj instanceof String) {
                final String json = (String)obj;
                if (json.isEmpty() || "null".equals(json)) {
                    return null;
                }
                final char first = json.trim().charAt(0);
                JSONReader jsonReader;
                if (first == '\"' || first == '{' || first == '[') {
                    jsonReader = JSONReader.of(json);
                }
                else {
                    jsonReader = JSONReader.of(JSON.toJSONString(json));
                }
                final ObjectReader objectReader2 = JSONFactory.getDefaultObjectReaderProvider().getObjectReader(targetClass);
                return objectReader2.readObject(jsonReader, null, null, 0L);
            }
            else {
                if (targetClass.isEnum() && obj instanceof Integer) {
                    final int intValue = (int)obj;
                    final ObjectReader objectReader3 = JSONFactory.getDefaultObjectReaderProvider().getObjectReader(targetClass);
                    if (objectReader3 instanceof ObjectReaderImplEnum) {
                        return (T)((ObjectReaderImplEnum)objectReader3).of(intValue);
                    }
                }
                if (obj instanceof Collection) {
                    return provider.getObjectReader(targetClass).createInstance((Collection)obj);
                }
                final String className = targetClass.getName();
                if (obj instanceof Integer || obj instanceof Long) {
                    final long millis = ((Number)obj).longValue();
                    final String s = className;
                    switch (s) {
                        case "java.sql.Date": {
                            return (T)JdbcSupport.createDate(millis);
                        }
                        case "java.sql.Timestamp": {
                            return (T)JdbcSupport.createTimestamp(millis);
                        }
                        case "java.sql.Time": {
                            return (T)JdbcSupport.createTime(millis);
                        }
                    }
                }
                final ObjectWriter objectWriter = JSONFactory.getDefaultObjectWriterProvider().getObjectWriter(obj.getClass());
                if (objectWriter instanceof ObjectWriterPrimitiveImpl) {
                    final Function function = ((ObjectWriterPrimitiveImpl)objectWriter).getFunction();
                    if (function != null) {
                        final Object apply = function.apply(obj);
                        if (targetClass.isInstance(apply)) {
                            return (T)apply;
                        }
                    }
                }
                throw new JSONException("can not cast to " + className + ", from " + obj.getClass());
            }
        }
    }
    
    public static String getTypeName(Class type) {
        final String mapTypeName = TypeUtils.NAME_MAPPINGS.get(type);
        if (mapTypeName != null) {
            return mapTypeName;
        }
        if (Proxy.isProxyClass(type)) {
            final Class[] interfaces = type.getInterfaces();
            if (interfaces.length > 0) {
                type = interfaces[0];
            }
        }
        final String typeName2;
        final String typeName = typeName2 = type.getTypeName();
        switch (typeName2) {
            case "com.alibaba.fastjson.JSONObject": {
                TypeUtils.NAME_MAPPINGS.putIfAbsent(type, "JO1");
                return TypeUtils.NAME_MAPPINGS.get(type);
            }
            case "com.alibaba.fastjson.JSONArray": {
                TypeUtils.NAME_MAPPINGS.putIfAbsent(type, "JA1");
                return TypeUtils.NAME_MAPPINGS.get(type);
            }
            default: {
                final int index = typeName.indexOf(36);
                if (index != -1 && isInteger(typeName.substring(index + 1))) {
                    final Class superclass = type.getSuperclass();
                    if (Map.class.isAssignableFrom(superclass)) {
                        return getTypeName(superclass);
                    }
                }
                return typeName;
            }
        }
    }
    
    public static Class getMapping(final String typeName) {
        return TypeUtils.TYPE_MAPPINGS.get(typeName);
    }
    
    public static BigDecimal toBigDecimal(final Object value) {
        if (value == null || value instanceof BigDecimal) {
            return (BigDecimal)value;
        }
        if (value instanceof Byte || value instanceof Short || value instanceof Integer || value instanceof Long) {
            return BigDecimal.valueOf(((Number)value).longValue());
        }
        if (!(value instanceof String)) {
            return cast(value, BigDecimal.class);
        }
        final String str = (String)value;
        if (str.isEmpty() || "null".equals(str)) {
            return null;
        }
        return new BigDecimal(str);
    }
    
    public static BigDecimal toBigDecimal(final long i) {
        return BigDecimal.valueOf(i);
    }
    
    public static BigDecimal toBigDecimal(final float f) {
        final byte[] bytes = new byte[15];
        final int size = DoubleToDecimal.toString(f, bytes, 0, true);
        return parseBigDecimal(bytes, 0, size);
    }
    
    public static BigDecimal toBigDecimal(final double d) {
        final byte[] bytes = new byte[24];
        final int size = DoubleToDecimal.toString(d, bytes, 0, true);
        return parseBigDecimal(bytes, 0, size);
    }
    
    public static BigDecimal toBigDecimal(final String str) {
        if (str == null || str.isEmpty() || "null".equals(str)) {
            return null;
        }
        if (JDKUtils.STRING_CODER != null) {
            final int code = JDKUtils.STRING_CODER.applyAsInt(str);
            if (code == JDKUtils.LATIN1 && JDKUtils.STRING_VALUE != null) {
                final byte[] bytes = JDKUtils.STRING_VALUE.apply(str);
                return parseBigDecimal(bytes, 0, bytes.length);
            }
        }
        final char[] chars = JDKUtils.getCharArray(str);
        return parseBigDecimal(chars, 0, chars.length);
    }
    
    public static BigDecimal toBigDecimal(final char[] chars) {
        if (chars == null) {
            return null;
        }
        return parseBigDecimal(chars, 0, chars.length);
    }
    
    public static BigDecimal toBigDecimal(final byte[] strBytes) {
        if (strBytes == null) {
            return null;
        }
        return parseBigDecimal(strBytes, 0, strBytes.length);
    }
    
    public static boolean isInt32(final BigInteger value) {
        return value.compareTo(TypeUtils.BIGINT_INT32_MIN) >= 0 && value.compareTo(TypeUtils.BIGINT_INT32_MAX) <= 0;
    }
    
    public static boolean isInt64(final BigInteger value) {
        return value.compareTo(TypeUtils.BIGINT_INT64_MIN) >= 0 && value.compareTo(TypeUtils.BIGINT_INT64_MAX) <= 0;
    }
    
    public static boolean isInteger(final BigDecimal decimal) {
        final int scale = decimal.scale();
        if (scale == 0) {
            return true;
        }
        final int precision = decimal.precision();
        if (precision < 20 && JDKUtils.FIELD_DECIMAL_INT_COMPACT_OFFSET != -1L) {
            final long intCompact = JDKUtils.UNSAFE.getLong(decimal, JDKUtils.FIELD_DECIMAL_INT_COMPACT_OFFSET);
            switch (scale) {
                case 1: {
                    return intCompact % 10L == 0L;
                }
                case 2: {
                    return intCompact % 100L == 0L;
                }
                case 3: {
                    return intCompact % 1000L == 0L;
                }
                case 4: {
                    return intCompact % 10000L == 0L;
                }
                case 5: {
                    return intCompact % 100000L == 0L;
                }
                case 6: {
                    return intCompact % 1000000L == 0L;
                }
                case 7: {
                    return intCompact % 10000000L == 0L;
                }
                case 8: {
                    return intCompact % 100000000L == 0L;
                }
                case 9: {
                    return intCompact % 1000000000L == 0L;
                }
            }
        }
        return decimal.stripTrailingZeros().scale() == 0;
    }
    
    public static BigInteger toBigInteger(final Object value) {
        if (value == null || value instanceof BigInteger) {
            return (BigInteger)value;
        }
        if (value instanceof Byte || value instanceof Short || value instanceof Integer || value instanceof Long) {
            return BigInteger.valueOf(((Number)value).longValue());
        }
        if (!(value instanceof String)) {
            throw new JSONException("can not cast to bigint");
        }
        final String str = (String)value;
        if (str.isEmpty() || "null".equals(str)) {
            return null;
        }
        return new BigInteger(str);
    }
    
    public static Long toLong(final Object value) {
        if (value == null || value instanceof Long) {
            return (Long)value;
        }
        if (value instanceof Number) {
            return ((Number)value).longValue();
        }
        if (!(value instanceof String)) {
            throw new JSONException("can not cast to long, class " + value.getClass());
        }
        final String str = (String)value;
        if (str.isEmpty() || "null".equals(str)) {
            return null;
        }
        return Long.parseLong(str);
    }
    
    public static long toLongValue(final Object value) {
        if (value == null) {
            return 0L;
        }
        if (value instanceof Long) {
            return (long)value;
        }
        if (value instanceof Number) {
            return ((Number)value).longValue();
        }
        if (!(value instanceof String)) {
            throw new JSONException("can not cast to long from " + value.getClass());
        }
        final String str = (String)value;
        if (str.isEmpty() || "null".equals(str)) {
            return 0L;
        }
        return Long.parseLong(str);
    }
    
    public static Boolean parseBoolean(final byte[] bytes, final int off, final int len) {
        switch (len) {
            case 0: {
                return null;
            }
            case 1: {
                final byte b0 = bytes[off];
                if (b0 == 49 || b0 == 89) {
                    return Boolean.TRUE;
                }
                if (b0 == 48 || b0 == 78) {
                    return Boolean.FALSE;
                }
                break;
            }
            case 4: {
                if (bytes[off] == 116 && bytes[off + 1] == 114 && bytes[off + 2] == 117 && bytes[off + 3] == 101) {
                    return Boolean.TRUE;
                }
                break;
            }
            case 5: {
                if (bytes[off] == 102 && bytes[off + 1] == 97 && bytes[off + 2] == 108 && bytes[off + 3] == 115 && bytes[off + 4] == 101) {
                    return Boolean.FALSE;
                }
                break;
            }
        }
        final String str = new String(bytes, off, len);
        return Boolean.parseBoolean(str);
    }
    
    public static Boolean parseBoolean(final char[] bytes, final int off, final int len) {
        switch (len) {
            case 0: {
                return null;
            }
            case 1: {
                final char b0 = bytes[off];
                if (b0 == '1' || b0 == 'Y') {
                    return Boolean.TRUE;
                }
                if (b0 == '0' || b0 == 'N') {
                    return Boolean.FALSE;
                }
                break;
            }
            case 4: {
                if (bytes[off] == 't' && bytes[off + 1] == 'r' && bytes[off + 2] == 'u' && bytes[off + 3] == 'e') {
                    return Boolean.TRUE;
                }
                break;
            }
            case 5: {
                if (bytes[off] == 'f' && bytes[off + 1] == 'a' && bytes[off + 2] == 'l' && bytes[off + 3] == 's' && bytes[off + 4] == 'e') {
                    return Boolean.FALSE;
                }
                break;
            }
        }
        final String str = new String(bytes, off, len);
        return Boolean.parseBoolean(str);
    }
    
    public static int parseInt(final byte[] bytes, final int off, final int len) {
        switch (len) {
            case 1: {
                final byte b0 = bytes[off];
                if (b0 >= 48 && b0 <= 57) {
                    return b0 - 48;
                }
                break;
            }
            case 2: {
                final byte b0 = bytes[off];
                final byte b2 = bytes[off + 1];
                if (b0 >= 48 && b0 <= 57 && b2 >= 48 && b2 <= 57) {
                    return (b0 - 48) * 10 + (b2 - 48);
                }
                break;
            }
            case 3: {
                final byte b0 = bytes[off];
                final byte b2 = bytes[off + 1];
                final byte b3 = bytes[off + 2];
                if (b0 >= 48 && b0 <= 57 && b2 >= 48 && b2 <= 57 && b3 >= 48 && b3 <= 57) {
                    return (b0 - 48) * 100 + (b2 - 48) * 10 + (b3 - 48);
                }
                break;
            }
            case 4: {
                final byte b0 = bytes[off];
                final byte b2 = bytes[off + 1];
                final byte b3 = bytes[off + 2];
                final byte b4 = bytes[off + 3];
                if (b0 >= 48 && b0 <= 57 && b2 >= 48 && b2 <= 57 && b3 >= 48 && b3 <= 57 && b4 >= 48 && b4 <= 57) {
                    return (b0 - 48) * 1000 + (b2 - 48) * 100 + (b3 - 48) * 10 + (b4 - 48);
                }
                break;
            }
            case 5: {
                final byte b0 = bytes[off];
                final byte b2 = bytes[off + 1];
                final byte b3 = bytes[off + 2];
                final byte b4 = bytes[off + 3];
                final byte b5 = bytes[off + 4];
                if (b0 >= 48 && b0 <= 57 && b2 >= 48 && b2 <= 57 && b3 >= 48 && b3 <= 57 && b4 >= 48 && b4 <= 57 && b5 >= 48 && b5 <= 57) {
                    return (b0 - 48) * 10000 + (b2 - 48) * 1000 + (b3 - 48) * 100 + (b4 - 48) * 10 + (b5 - 48);
                }
                break;
            }
            case 6: {
                final byte b0 = bytes[off];
                final byte b2 = bytes[off + 1];
                final byte b3 = bytes[off + 2];
                final byte b4 = bytes[off + 3];
                final byte b5 = bytes[off + 4];
                final byte b6 = bytes[off + 5];
                if (b0 >= 48 && b0 <= 57 && b2 >= 48 && b2 <= 57 && b3 >= 48 && b3 <= 57 && b4 >= 48 && b4 <= 57 && b5 >= 48 && b5 <= 57 && b6 >= 48 && b6 <= 57) {
                    return (b0 - 48) * 100000 + (b2 - 48) * 10000 + (b3 - 48) * 1000 + (b4 - 48) * 100 + (b5 - 48) * 10 + (b6 - 48);
                }
                break;
            }
            case 7: {
                final byte b0 = bytes[off];
                final byte b2 = bytes[off + 1];
                final byte b3 = bytes[off + 2];
                final byte b4 = bytes[off + 3];
                final byte b5 = bytes[off + 4];
                final byte b6 = bytes[off + 5];
                final byte b7 = bytes[off + 6];
                if (b0 >= 48 && b0 <= 57 && b2 >= 48 && b2 <= 57 && b3 >= 48 && b3 <= 57 && b4 >= 48 && b4 <= 57 && b5 >= 48 && b5 <= 57 && b6 >= 48 && b6 <= 57 && b7 >= 48 && b7 <= 57) {
                    return (b0 - 48) * 1000000 + (b2 - 48) * 100000 + (b3 - 48) * 10000 + (b4 - 48) * 1000 + (b5 - 48) * 100 + (b6 - 48) * 10 + (b7 - 48);
                }
                break;
            }
            case 8: {
                final byte b0 = bytes[off];
                final byte b2 = bytes[off + 1];
                final byte b3 = bytes[off + 2];
                final byte b4 = bytes[off + 3];
                final byte b5 = bytes[off + 4];
                final byte b6 = bytes[off + 5];
                final byte b7 = bytes[off + 6];
                final byte b8 = bytes[off + 7];
                if (b0 >= 48 && b0 <= 57 && b2 >= 48 && b2 <= 57 && b3 >= 48 && b3 <= 57 && b4 >= 48 && b4 <= 57 && b5 >= 48 && b5 <= 57 && b6 >= 48 && b6 <= 57 && b7 >= 48 && b7 <= 57 && b8 >= 48 && b8 <= 57) {
                    return (b0 - 48) * 10000000 + (b2 - 48) * 1000000 + (b3 - 48) * 100000 + (b4 - 48) * 10000 + (b5 - 48) * 1000 + (b6 - 48) * 100 + (b7 - 48) * 10 + (b8 - 48);
                }
                break;
            }
        }
        final String str = new String(bytes, off, len);
        return Integer.parseInt(str);
    }
    
    public static int parseInt(final char[] bytes, final int off, final int len) {
        switch (len) {
            case 1: {
                final char b0 = bytes[off];
                if (b0 >= '0' && b0 <= '9') {
                    return b0 - '0';
                }
                break;
            }
            case 2: {
                final char b0 = bytes[off];
                final char b2 = bytes[off + 1];
                if (b0 >= '0' && b0 <= '9' && b2 >= '0' && b2 <= '9') {
                    return (b0 - '0') * 10 + (b2 - '0');
                }
                break;
            }
            case 3: {
                final char b0 = bytes[off];
                final char b2 = bytes[off + 1];
                final char b3 = bytes[off + 2];
                if (b0 >= '0' && b0 <= '9' && b2 >= '0' && b2 <= '9' && b3 >= '0' && b3 <= '9') {
                    return (b0 - '0') * 100 + (b2 - '0') * 10 + (b3 - '0');
                }
                break;
            }
            case 4: {
                final char b0 = bytes[off];
                final char b2 = bytes[off + 1];
                final char b3 = bytes[off + 2];
                final char b4 = bytes[off + 3];
                if (b0 >= '0' && b0 <= '9' && b2 >= '0' && b2 <= '9' && b3 >= '0' && b3 <= '9' && b4 >= '0' && b4 <= '9') {
                    return (b0 - '0') * 1000 + (b2 - '0') * 100 + (b3 - '0') * 10 + (b4 - '0');
                }
                break;
            }
            case 5: {
                final char b0 = bytes[off];
                final char b2 = bytes[off + 1];
                final char b3 = bytes[off + 2];
                final char b4 = bytes[off + 3];
                final char b5 = bytes[off + 4];
                if (b0 >= '0' && b0 <= '9' && b2 >= '0' && b2 <= '9' && b3 >= '0' && b3 <= '9' && b4 >= '0' && b4 <= '9' && b5 >= '0' && b5 <= '9') {
                    return (b0 - '0') * 10000 + (b2 - '0') * 1000 + (b3 - '0') * 100 + (b4 - '0') * 10 + (b5 - '0');
                }
                break;
            }
            case 6: {
                final char b0 = bytes[off];
                final char b2 = bytes[off + 1];
                final char b3 = bytes[off + 2];
                final char b4 = bytes[off + 3];
                final char b5 = bytes[off + 4];
                final char b6 = bytes[off + 5];
                if (b0 >= '0' && b0 <= '9' && b2 >= '0' && b2 <= '9' && b3 >= '0' && b3 <= '9' && b4 >= '0' && b4 <= '9' && b5 >= '0' && b5 <= '9' && b6 >= '0' && b6 <= '9') {
                    return (b0 - '0') * 100000 + (b2 - '0') * 10000 + (b3 - '0') * 1000 + (b4 - '0') * 100 + (b5 - '0') * 10 + (b6 - '0');
                }
                break;
            }
            case 7: {
                final char b0 = bytes[off];
                final char b2 = bytes[off + 1];
                final char b3 = bytes[off + 2];
                final char b4 = bytes[off + 3];
                final char b5 = bytes[off + 4];
                final char b6 = bytes[off + 5];
                final char b7 = bytes[off + 6];
                if (b0 >= '0' && b0 <= '9' && b2 >= '0' && b2 <= '9' && b3 >= '0' && b3 <= '9' && b4 >= '0' && b4 <= '9' && b5 >= '0' && b5 <= '9' && b6 >= '0' && b6 <= '9' && b7 >= '0' && b7 <= '9') {
                    return (b0 - '0') * 1000000 + (b2 - '0') * 100000 + (b3 - '0') * 10000 + (b4 - '0') * 1000 + (b5 - '0') * 100 + (b6 - '0') * 10 + (b7 - '0');
                }
                break;
            }
            case 8: {
                final char b0 = bytes[off];
                final char b2 = bytes[off + 1];
                final char b3 = bytes[off + 2];
                final char b4 = bytes[off + 3];
                final char b5 = bytes[off + 4];
                final char b6 = bytes[off + 5];
                final char b7 = bytes[off + 6];
                final char b8 = bytes[off + 7];
                if (b0 >= '0' && b0 <= '9' && b2 >= '0' && b2 <= '9' && b3 >= '0' && b3 <= '9' && b4 >= '0' && b4 <= '9' && b5 >= '0' && b5 <= '9' && b6 >= '0' && b6 <= '9' && b7 >= '0' && b7 <= '9' && b8 >= '0' && b8 <= '9') {
                    return (b0 - '0') * 10000000 + (b2 - '0') * 1000000 + (b3 - '0') * 100000 + (b4 - '0') * 10000 + (b5 - '0') * 1000 + (b6 - '0') * 100 + (b7 - '0') * 10 + (b8 - '0');
                }
                break;
            }
        }
        final String str = new String(bytes, off, len);
        return Integer.parseInt(str);
    }
    
    public static long parseLong(final byte[] bytes, final int off, final int len) {
        switch (len) {
            case 1: {
                final byte b0 = bytes[off];
                if (b0 >= 48 && b0 <= 57) {
                    return b0 - 48;
                }
                break;
            }
            case 2: {
                final byte b0 = bytes[off];
                final byte b2 = bytes[off + 1];
                if (b0 >= 48 && b0 <= 57 && b2 >= 48 && b2 <= 57) {
                    return (b0 - 48) * 10L + (b2 - 48);
                }
                break;
            }
            case 3: {
                final byte b0 = bytes[off];
                final byte b2 = bytes[off + 1];
                final byte b3 = bytes[off + 2];
                if (b0 >= 48 && b0 <= 57 && b2 >= 48 && b2 <= 57 && b3 >= 48 && b3 <= 57) {
                    return (b0 - 48) * 100L + (b2 - 48) * 10 + (b3 - 48);
                }
                break;
            }
            case 4: {
                final byte b0 = bytes[off];
                final byte b2 = bytes[off + 1];
                final byte b3 = bytes[off + 2];
                final byte b4 = bytes[off + 3];
                if (b0 >= 48 && b0 <= 57 && b2 >= 48 && b2 <= 57 && b3 >= 48 && b3 <= 57 && b4 >= 48 && b4 <= 57) {
                    return (b0 - 48) * 1000L + (b2 - 48) * 100 + (b3 - 48) * 10 + (b4 - 48);
                }
                break;
            }
            case 5: {
                final byte b0 = bytes[off];
                final byte b2 = bytes[off + 1];
                final byte b3 = bytes[off + 2];
                final byte b4 = bytes[off + 3];
                final byte b5 = bytes[off + 4];
                if (b0 >= 48 && b0 <= 57 && b2 >= 48 && b2 <= 57 && b3 >= 48 && b3 <= 57 && b4 >= 48 && b4 <= 57 && b5 >= 48 && b5 <= 57) {
                    return (b0 - 48) * 10000L + (b2 - 48) * 1000 + (b3 - 48) * 100 + (b4 - 48) * 10 + (b5 - 48);
                }
                break;
            }
            case 6: {
                final byte b0 = bytes[off];
                final byte b2 = bytes[off + 1];
                final byte b3 = bytes[off + 2];
                final byte b4 = bytes[off + 3];
                final byte b5 = bytes[off + 4];
                final byte b6 = bytes[off + 5];
                if (b0 >= 48 && b0 <= 57 && b2 >= 48 && b2 <= 57 && b3 >= 48 && b3 <= 57 && b4 >= 48 && b4 <= 57 && b5 >= 48 && b5 <= 57 && b6 >= 48 && b6 <= 57) {
                    return (b0 - 48) * 100000L + (b2 - 48) * 10000 + (b3 - 48) * 1000 + (b4 - 48) * 100 + (b5 - 48) * 10 + (b6 - 48);
                }
                break;
            }
            case 7: {
                final byte b0 = bytes[off];
                final byte b2 = bytes[off + 1];
                final byte b3 = bytes[off + 2];
                final byte b4 = bytes[off + 3];
                final byte b5 = bytes[off + 4];
                final byte b6 = bytes[off + 5];
                final byte b7 = bytes[off + 6];
                if (b0 >= 48 && b0 <= 57 && b2 >= 48 && b2 <= 57 && b3 >= 48 && b3 <= 57 && b4 >= 48 && b4 <= 57 && b5 >= 48 && b5 <= 57 && b6 >= 48 && b6 <= 57 && b7 >= 48 && b7 <= 57) {
                    return (b0 - 48) * 1000000L + (b2 - 48) * 100000 + (b3 - 48) * 10000 + (b4 - 48) * 1000 + (b5 - 48) * 100 + (b6 - 48) * 10 + (b7 - 48);
                }
                break;
            }
            case 8: {
                final byte b0 = bytes[off];
                final byte b2 = bytes[off + 1];
                final byte b3 = bytes[off + 2];
                final byte b4 = bytes[off + 3];
                final byte b5 = bytes[off + 4];
                final byte b6 = bytes[off + 5];
                final byte b7 = bytes[off + 6];
                final byte b8 = bytes[off + 7];
                if (b0 >= 48 && b0 <= 57 && b2 >= 48 && b2 <= 57 && b3 >= 48 && b3 <= 57 && b4 >= 48 && b4 <= 57 && b5 >= 48 && b5 <= 57 && b6 >= 48 && b6 <= 57 && b7 >= 48 && b7 <= 57 && b8 >= 48 && b8 <= 57) {
                    return (b0 - 48) * 10000000L + (b2 - 48) * 1000000 + (b3 - 48) * 100000 + (b4 - 48) * 10000 + (b5 - 48) * 1000 + (b6 - 48) * 100 + (b7 - 48) * 10 + (b8 - 48);
                }
                break;
            }
        }
        final String str = new String(bytes, off, len);
        return Long.parseLong(str);
    }
    
    public static long parseLong(final char[] bytes, final int off, final int len) {
        switch (len) {
            case 1: {
                final char b0 = bytes[off];
                if (b0 >= '0' && b0 <= '9') {
                    return b0 - '0';
                }
                break;
            }
            case 2: {
                final char b0 = bytes[off];
                final char b2 = bytes[off + 1];
                if (b0 >= '0' && b0 <= '9' && b2 >= '0' && b2 <= '9') {
                    return (b0 - '0') * 10L + (b2 - '0');
                }
                break;
            }
            case 3: {
                final char b0 = bytes[off];
                final char b2 = bytes[off + 1];
                final char b3 = bytes[off + 2];
                if (b0 >= '0' && b0 <= '9' && b2 >= '0' && b2 <= '9' && b3 >= '0' && b3 <= '9') {
                    return (b0 - '0') * 100L + (b2 - '0') * 10 + (b3 - '0');
                }
                break;
            }
            case 4: {
                final char b0 = bytes[off];
                final char b2 = bytes[off + 1];
                final char b3 = bytes[off + 2];
                final char b4 = bytes[off + 3];
                if (b0 >= '0' && b0 <= '9' && b2 >= '0' && b2 <= '9' && b3 >= '0' && b3 <= '9' && b4 >= '0' && b4 <= '9') {
                    return (b0 - '0') * 1000L + (b2 - '0') * 100 + (b3 - '0') * 10 + (b4 - '0');
                }
                break;
            }
            case 5: {
                final char b0 = bytes[off];
                final char b2 = bytes[off + 1];
                final char b3 = bytes[off + 2];
                final char b4 = bytes[off + 3];
                final char b5 = bytes[off + 4];
                if (b0 >= '0' && b0 <= '9' && b2 >= '0' && b2 <= '9' && b3 >= '0' && b3 <= '9' && b4 >= '0' && b4 <= '9' && b5 >= '0' && b5 <= '9') {
                    return (b0 - '0') * 10000L + (b2 - '0') * 1000 + (b3 - '0') * 100 + (b4 - '0') * 10 + (b5 - '0');
                }
                break;
            }
            case 6: {
                final char b0 = bytes[off];
                final char b2 = bytes[off + 1];
                final char b3 = bytes[off + 2];
                final char b4 = bytes[off + 3];
                final char b5 = bytes[off + 4];
                final char b6 = bytes[off + 5];
                if (b0 >= '0' && b0 <= '9' && b2 >= '0' && b2 <= '9' && b3 >= '0' && b3 <= '9' && b4 >= '0' && b4 <= '9' && b5 >= '0' && b5 <= '9' && b6 >= '0' && b6 <= '9') {
                    return (b0 - '0') * 100000L + (b2 - '0') * 10000 + (b3 - '0') * 1000 + (b4 - '0') * 100 + (b5 - '0') * 10 + (b6 - '0');
                }
                break;
            }
            case 7: {
                final char b0 = bytes[off];
                final char b2 = bytes[off + 1];
                final char b3 = bytes[off + 2];
                final char b4 = bytes[off + 3];
                final char b5 = bytes[off + 4];
                final char b6 = bytes[off + 5];
                final char b7 = bytes[off + 6];
                if (b0 >= '0' && b0 <= '9' && b2 >= '0' && b2 <= '9' && b3 >= '0' && b3 <= '9' && b4 >= '0' && b4 <= '9' && b5 >= '0' && b5 <= '9' && b6 >= '0' && b6 <= '9' && b7 >= '0' && b7 <= '9') {
                    return (b0 - '0') * 1000000L + (b2 - '0') * 100000 + (b3 - '0') * 10000 + (b4 - '0') * 1000 + (b5 - '0') * 100 + (b6 - '0') * 10 + (b7 - '0');
                }
                break;
            }
            case 8: {
                final char b0 = bytes[off];
                final char b2 = bytes[off + 1];
                final char b3 = bytes[off + 2];
                final char b4 = bytes[off + 3];
                final char b5 = bytes[off + 4];
                final char b6 = bytes[off + 5];
                final char b7 = bytes[off + 6];
                final char b8 = bytes[off + 7];
                if (b0 >= '0' && b0 <= '9' && b2 >= '0' && b2 <= '9' && b3 >= '0' && b3 <= '9' && b4 >= '0' && b4 <= '9' && b5 >= '0' && b5 <= '9' && b6 >= '0' && b6 <= '9' && b7 >= '0' && b7 <= '9' && b8 >= '0' && b8 <= '9') {
                    return (b0 - '0') * 10000000L + (b2 - '0') * 1000000 + (b3 - '0') * 100000 + (b4 - '0') * 10000 + (b5 - '0') * 1000 + (b6 - '0') * 100 + (b7 - '0') * 10 + (b8 - '0');
                }
                break;
            }
        }
        final String str = new String(bytes, off, len);
        return Long.parseLong(str);
    }
    
    public static BigDecimal parseBigDecimal(final char[] bytes, final int off, final int len) {
        if (bytes == null || len == 0) {
            return null;
        }
        boolean negative = false;
        int j = off;
        if (bytes[off] == '-') {
            negative = true;
            ++j;
        }
        if (len <= 20 || (negative && len == 21)) {
            final int end = off + len;
            int dot = 0;
            int dotIndex = -1;
            long unscaleValue = 0L;
            while (j < end) {
                final char b = bytes[j];
                if (b == '.') {
                    if (++dot > 1) {
                        break;
                    }
                    dotIndex = j;
                }
                else {
                    if (b < '0' || b > '9') {
                        unscaleValue = -1L;
                        break;
                    }
                    final long r = unscaleValue * 10L;
                    if ((unscaleValue | 0xAL) >>> 31 != 0L && r / 10L != unscaleValue) {
                        unscaleValue = -1L;
                        break;
                    }
                    unscaleValue = r + (b - '0');
                }
                ++j;
            }
            int scale = 0;
            if (unscaleValue >= 0L && dot <= 1) {
                if (negative) {
                    unscaleValue = -unscaleValue;
                }
                if (dotIndex != -1) {
                    scale = len - (dotIndex - off) - 1;
                }
                return BigDecimal.valueOf(unscaleValue, scale);
            }
        }
        return new BigDecimal(bytes, off, len);
    }
    
    public static BigDecimal parseBigDecimal(final byte[] bytes, final int off, final int len) {
        if (bytes == null || len == 0) {
            return null;
        }
        boolean negative = false;
        int j = off;
        if (bytes[off] == 45) {
            negative = true;
            ++j;
        }
        if (len <= 20 || (negative && len == 21)) {
            final int end = off + len;
            int dot = 0;
            int dotIndex = -1;
            long unscaleValue = 0L;
            while (j < end) {
                final byte b = bytes[j];
                if (b == 46) {
                    if (++dot > 1) {
                        break;
                    }
                    dotIndex = j;
                }
                else {
                    if (b < 48 || b > 57) {
                        unscaleValue = -1L;
                        break;
                    }
                    final long r = unscaleValue * 10L;
                    if ((unscaleValue | 0xAL) >>> 31 != 0L && r / 10L != unscaleValue) {
                        unscaleValue = -1L;
                        break;
                    }
                    unscaleValue = r + (b - 48);
                }
                ++j;
            }
            int scale = 0;
            if (unscaleValue >= 0L && dot <= 1) {
                if (negative) {
                    unscaleValue = -unscaleValue;
                }
                if (dotIndex != -1) {
                    scale = len - (dotIndex - off) - 1;
                }
                return BigDecimal.valueOf(unscaleValue, scale);
            }
        }
        char[] chars;
        if (off == 0 && len == bytes.length) {
            chars = X1.TO_CHARS.apply(bytes);
        }
        else {
            chars = new char[len];
            for (int i = 0; i < len; ++i) {
                chars[i] = (char)bytes[off + i];
            }
        }
        return new BigDecimal(chars, 0, chars.length);
    }
    
    public static Integer toInteger(final Object value) {
        if (value == null || value instanceof Integer) {
            return (Integer)value;
        }
        if (value instanceof Number) {
            return ((Number)value).intValue();
        }
        if (value instanceof String) {
            final String str = (String)value;
            if (str.isEmpty() || "null".equals(str)) {
                return null;
            }
            return Integer.parseInt(str);
        }
        else {
            if (value instanceof Map && ((Map)value).isEmpty()) {
                return null;
            }
            if (value instanceof Boolean) {
                return ((boolean)value) ? 1 : 0;
            }
            throw new JSONException("can not cast to integer");
        }
    }
    
    public static Byte toByte(final Object value) {
        if (value == null || value instanceof Byte) {
            return (Byte)value;
        }
        if (value instanceof Number) {
            return ((Number)value).byteValue();
        }
        if (!(value instanceof String)) {
            throw new JSONException("can not cast to byte");
        }
        final String str = (String)value;
        if (str.isEmpty() || "null".equals(str)) {
            return null;
        }
        return Byte.parseByte(str);
    }
    
    public static byte toByteValue(final Object value) {
        if (value == null) {
            return 0;
        }
        if (value instanceof Byte) {
            return (byte)value;
        }
        if (value instanceof Number) {
            return ((Number)value).byteValue();
        }
        if (!(value instanceof String)) {
            throw new JSONException("can not cast to byte");
        }
        final String str = (String)value;
        if (str.isEmpty() || "null".equals(str)) {
            return 0;
        }
        return Byte.parseByte(str);
    }
    
    public static Short toShort(final Object value) {
        if (value == null || value instanceof Short) {
            return (Short)value;
        }
        if (value instanceof Number) {
            return ((Number)value).shortValue();
        }
        if (!(value instanceof String)) {
            throw new JSONException("can not cast to byte");
        }
        final String str = (String)value;
        if (str.isEmpty() || "null".equals(str)) {
            return null;
        }
        return Short.parseShort(str);
    }
    
    public static short toShortValue(final Object value) {
        if (value == null) {
            return 0;
        }
        if (value instanceof Short) {
            return (short)value;
        }
        if (value instanceof Number) {
            return (byte)((Number)value).shortValue();
        }
        if (!(value instanceof String)) {
            throw new JSONException("can not cast to byte");
        }
        final String str = (String)value;
        if (str.isEmpty() || "null".equals(str)) {
            return 0;
        }
        return Short.parseShort(str);
    }
    
    public static int toIntValue(final Object value) {
        if (value == null) {
            return 0;
        }
        if (value instanceof Integer) {
            return (int)value;
        }
        if (value instanceof Number) {
            return ((Number)value).intValue();
        }
        if (!(value instanceof String)) {
            throw new JSONException("can not cast to decimal");
        }
        final String str = (String)value;
        if (str.isEmpty() || "null".equals(str)) {
            return 0;
        }
        if (str.indexOf(46) != -1) {
            return new BigDecimal(str).intValueExact();
        }
        return Integer.parseInt(str);
    }
    
    public static boolean toBooleanValue(final Object value) {
        if (value == null) {
            return false;
        }
        if (value instanceof Boolean) {
            return (boolean)value;
        }
        if (value instanceof String) {
            final String str = (String)value;
            return !str.isEmpty() && !"null".equals(str) && Boolean.parseBoolean(str);
        }
        if (value instanceof Number) {
            final int intValue = ((Number)value).intValue();
            if (intValue == 1) {
                return true;
            }
            if (intValue == 0) {
                return false;
            }
        }
        throw new JSONException("can not cast to boolean");
    }
    
    public static Boolean toBoolean(final Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Boolean) {
            return (Boolean)value;
        }
        if (!(value instanceof String)) {
            if (value instanceof Number) {
                final int intValue = ((Number)value).intValue();
                if (intValue == 1) {
                    return true;
                }
                if (intValue == 0) {
                    return false;
                }
            }
            throw new JSONException("can not cast to boolean");
        }
        final String str = (String)value;
        if (str.isEmpty() || "null".equals(str)) {
            return null;
        }
        return Boolean.parseBoolean(str);
    }
    
    public static float toFloatValue(final Object value) {
        if (value == null) {
            return 0.0f;
        }
        if (value instanceof Float) {
            return (float)value;
        }
        if (value instanceof Number) {
            return ((Number)value).floatValue();
        }
        if (!(value instanceof String)) {
            throw new JSONException("can not cast to decimal");
        }
        final String str = (String)value;
        if (str.isEmpty() || "null".equals(str)) {
            return 0.0f;
        }
        return Float.parseFloat(str);
    }
    
    public static Float toFloat(final Object value) {
        if (value == null || value instanceof Float) {
            return (Float)value;
        }
        if (value instanceof Number) {
            return ((Number)value).floatValue();
        }
        if (!(value instanceof String)) {
            throw new JSONException("can not cast to decimal");
        }
        final String str = (String)value;
        if (str.isEmpty() || "null".equals(str)) {
            return null;
        }
        return Float.parseFloat(str);
    }
    
    public static double toDoubleValue(final Object value) {
        if (value == null) {
            return 0.0;
        }
        if (value instanceof Double) {
            return (double)value;
        }
        if (value instanceof Number) {
            return ((Number)value).doubleValue();
        }
        if (!(value instanceof String)) {
            throw new JSONException("can not cast to decimal");
        }
        final String str = (String)value;
        if (str.isEmpty() || "null".equals(str)) {
            return 0.0;
        }
        return Double.parseDouble(str);
    }
    
    public static Double toDouble(final Object value) {
        if (value == null || value instanceof Double) {
            return (Double)value;
        }
        if (value instanceof Number) {
            return ((Number)value).doubleValue();
        }
        if (!(value instanceof String)) {
            throw new JSONException("can not cast to decimal");
        }
        final String str = (String)value;
        if (str.isEmpty() || "null".equals(str)) {
            return null;
        }
        return Double.parseDouble(str);
    }
    
    public static int compare(Object a, Object b) {
        if (a.getClass() == b.getClass()) {
            return ((Comparable)a).compareTo(b);
        }
        final Class typeA = a.getClass();
        final Class typeB = b.getClass();
        if (typeA == BigDecimal.class) {
            if (typeB == Integer.class) {
                b = new BigDecimal((int)b);
            }
            else if (typeB == Long.class) {
                b = new BigDecimal((long)b);
            }
            else if (typeB == Float.class) {
                b = BigDecimal.valueOf((float)b);
            }
            else if (typeB == Double.class) {
                b = BigDecimal.valueOf((double)b);
            }
            else if (typeB == BigInteger.class) {
                b = new BigDecimal((BigInteger)b);
            }
        }
        else if (typeA == BigInteger.class) {
            if (typeB == Integer.class) {
                b = BigInteger.valueOf((int)b);
            }
            else if (typeB == Long.class) {
                b = BigInteger.valueOf((long)b);
            }
            else if (typeB == Float.class) {
                b = BigDecimal.valueOf((float)b);
                a = new BigDecimal((BigInteger)a);
            }
            else if (typeB == Double.class) {
                b = BigDecimal.valueOf((double)b);
                a = new BigDecimal((BigInteger)a);
            }
            else if (typeB == BigDecimal.class) {
                a = new BigDecimal((BigInteger)a);
            }
        }
        else if (typeA == Long.class) {
            if (typeB == Integer.class) {
                b = new Long((int)b);
            }
            else if (typeB == BigDecimal.class) {
                a = new BigDecimal((long)a);
            }
            else if (typeB == Float.class) {
                a = new Float((float)(long)a);
            }
            else if (typeB == Double.class) {
                a = new Double((double)(long)a);
            }
            else if (typeB == BigInteger.class) {
                a = BigInteger.valueOf((long)a);
            }
            else if (typeB == String.class) {
                a = BigDecimal.valueOf((long)a);
                b = new BigDecimal((String)b);
            }
        }
        else if (typeA == Integer.class) {
            if (typeB == Long.class) {
                a = new Long((int)a);
            }
            else if (typeB == BigDecimal.class) {
                a = new BigDecimal((int)a);
            }
            else if (typeB == BigInteger.class) {
                a = BigInteger.valueOf((int)a);
            }
            else if (typeB == Float.class) {
                a = new Float((float)(int)a);
            }
            else if (typeB == Double.class) {
                a = new Double((int)a);
            }
            else if (typeB == String.class) {
                a = BigDecimal.valueOf((int)a);
                b = new BigDecimal((String)b);
            }
        }
        else if (typeA == Double.class) {
            if (typeB == Integer.class) {
                b = new Double((int)b);
            }
            else if (typeB == Long.class) {
                b = new Double((double)(long)b);
            }
            else if (typeB == Float.class) {
                b = new Double((float)b);
            }
            else if (typeB == BigDecimal.class) {
                a = BigDecimal.valueOf((double)a);
            }
            else if (typeB == String.class) {
                a = BigDecimal.valueOf((double)a);
                b = new BigDecimal((String)b);
            }
            else if (typeB == BigInteger.class) {
                a = BigDecimal.valueOf((double)a);
                b = new BigDecimal((BigInteger)b);
            }
        }
        else if (typeA == Float.class) {
            if (typeB == Integer.class) {
                b = new Float((float)(int)b);
            }
            else if (typeB == Long.class) {
                b = new Float((float)(long)b);
            }
            else if (typeB == Double.class) {
                a = new Double((float)a);
            }
            else if (typeB == BigDecimal.class) {
                a = BigDecimal.valueOf((float)a);
            }
            else if (typeB == String.class) {
                a = BigDecimal.valueOf((float)a);
                b = new BigDecimal((String)b);
            }
            else if (typeB == BigInteger.class) {
                a = BigDecimal.valueOf((float)a);
                b = new BigDecimal((BigInteger)b);
            }
        }
        else if (typeA == String.class) {
            final String strA = (String)a;
            if (typeB == Integer.class) {
                NumberFormatException error = null;
                try {
                    a = Integer.parseInt(strA);
                }
                catch (NumberFormatException ex) {
                    error = ex;
                }
                if (error != null) {
                    try {
                        a = Long.parseLong(strA);
                        b = b;
                        error = null;
                    }
                    catch (NumberFormatException ex) {
                        error = ex;
                    }
                }
                if (error != null) {
                    a = new BigDecimal(strA);
                    b = BigDecimal.valueOf((int)b);
                }
            }
            else if (typeB == Long.class) {
                a = new BigDecimal(strA);
                b = BigDecimal.valueOf((long)b);
            }
            else if (typeB == Float.class) {
                a = Float.parseFloat(strA);
            }
            else if (typeB == Double.class) {
                a = Double.parseDouble(strA);
            }
            else if (typeB == BigInteger.class) {
                a = new BigInteger(strA);
            }
            else if (typeB == BigDecimal.class) {
                a = new BigDecimal(strA);
            }
        }
        return ((Comparable)a).compareTo(b);
    }
    
    public static Object getDefaultValue(final Type paramType) {
        if (paramType == Integer.TYPE) {
            return 0;
        }
        if (paramType == Long.TYPE) {
            return 0L;
        }
        if (paramType == Float.TYPE) {
            return 0.0f;
        }
        if (paramType == Double.TYPE) {
            return 0.0;
        }
        if (paramType == Boolean.TYPE) {
            return Boolean.FALSE;
        }
        if (paramType == Short.TYPE) {
            return 0;
        }
        if (paramType == Byte.TYPE) {
            return 0;
        }
        if (paramType == Character.TYPE) {
            return '\0';
        }
        if (paramType == Optional.class) {
            return Optional.empty();
        }
        if (paramType == OptionalInt.class) {
            return OptionalInt.empty();
        }
        if (paramType == OptionalLong.class) {
            return OptionalLong.empty();
        }
        if (paramType == OptionalDouble.class) {
            return OptionalDouble.empty();
        }
        return null;
    }
    
    public static Class loadClass(String className) {
        if (className.length() >= 192) {
            return null;
        }
        final String s = className;
        switch (s) {
            case "O":
            case "Object":
            case "java.lang.Object": {
                return Object.class;
            }
            case "java.util.Collections$EmptyMap": {
                return Collections.EMPTY_MAP.getClass();
            }
            case "java.util.Collections$EmptyList": {
                return Collections.EMPTY_LIST.getClass();
            }
            case "java.util.Collections$EmptySet": {
                return Collections.EMPTY_SET.getClass();
            }
            case "java.util.Optional": {
                return Optional.class;
            }
            case "java.util.OptionalInt": {
                return OptionalInt.class;
            }
            case "java.util.OptionalLong": {
                return OptionalLong.class;
            }
            case "List":
            case "java.util.List": {
                return List.class;
            }
            case "A":
            case "ArrayList":
            case "java.util.ArrayList": {
                return ArrayList.class;
            }
            case "LA":
            case "LinkedList":
            case "java.util.LinkedList": {
                return LinkedList.class;
            }
            case "Map":
            case "java.util.Map": {
                return Map.class;
            }
            case "M":
            case "HashMap":
            case "java.util.HashMap": {
                return HashMap.class;
            }
            case "LM":
            case "LinkedHashMap":
            case "java.util.LinkedHashMap": {
                return LinkedHashMap.class;
            }
            case "ConcurrentHashMap": {
                return ConcurrentHashMap.class;
            }
            case "ConcurrentLinkedQueue": {
                return ConcurrentLinkedQueue.class;
            }
            case "ConcurrentLinkedDeque": {
                return ConcurrentLinkedDeque.class;
            }
            case "JSONObject": {
                return JSONObject.class;
            }
            case "JO1": {
                className = "com.alibaba.fastjson.JSONObject";
                break;
            }
            case "Set":
            case "java.util.Set": {
                return Set.class;
            }
            case "HashSet":
            case "java.util.HashSet": {
                return HashSet.class;
            }
            case "LinkedHashSet":
            case "java.util.LinkedHashSet": {
                return LinkedHashSet.class;
            }
            case "TreeSet":
            case "java.util.TreeSet": {
                return TreeSet.class;
            }
            case "java.lang.Class": {
                return Class.class;
            }
            case "java.lang.Integer": {
                return Integer.class;
            }
            case "java.lang.Long": {
                return Long.class;
            }
            case "String":
            case "java.lang.String": {
                return String.class;
            }
            case "[String": {
                return String[].class;
            }
            case "I":
            case "int": {
                return Integer.TYPE;
            }
            case "S":
            case "short": {
                return Short.TYPE;
            }
            case "J":
            case "long": {
                return Long.TYPE;
            }
            case "Z":
            case "boolean": {
                return Boolean.TYPE;
            }
            case "B":
            case "byte": {
                return Byte.TYPE;
            }
            case "F":
            case "float": {
                return Float.TYPE;
            }
            case "D":
            case "double": {
                return Double.TYPE;
            }
            case "C":
            case "char": {
                return Character.TYPE;
            }
            case "[B":
            case "byte[]": {
                return byte[].class;
            }
            case "[S":
            case "short[]": {
                return short[].class;
            }
            case "[I":
            case "int[]": {
                return int[].class;
            }
            case "[J":
            case "long[]": {
                return long[].class;
            }
            case "[F":
            case "float[]": {
                return float[].class;
            }
            case "[D":
            case "double[]": {
                return double[].class;
            }
            case "[C":
            case "char[]": {
                return char[].class;
            }
            case "[Z":
            case "boolean[]": {
                return boolean[].class;
            }
            case "[O": {
                return Object[].class;
            }
            case "UUID": {
                return UUID.class;
            }
            case "Date": {
                return Date.class;
            }
            case "Calendar": {
                return Calendar.class;
            }
            case "java.io.IOException": {
                return IOException.class;
            }
            case "java.util.Collections$UnmodifiableRandomAccessList": {
                return TypeUtils.CLASS_UNMODIFIABLE_LIST;
            }
            case "java.util.Arrays$ArrayList": {
                return Arrays.asList(1).getClass();
            }
            case "java.util.Collections$SingletonList": {
                return TypeUtils.CLASS_SINGLE_LIST;
            }
            case "java.util.Collections$SingletonSet": {
                return TypeUtils.CLASS_SINGLE_SET;
            }
        }
        final Class mapping = TypeUtils.TYPE_MAPPINGS.get(className);
        if (mapping != null) {
            return mapping;
        }
        if (className.startsWith("java.util.ImmutableCollections$")) {
            try {
                return Class.forName(className);
            }
            catch (ClassNotFoundException e) {
                return TypeUtils.CLASS_UNMODIFIABLE_LIST;
            }
        }
        if (className.charAt(0) == 'L' && className.charAt(className.length() - 1) == ';') {
            className = className.substring(1, className.length() - 1);
        }
        if (className.charAt(0) == '[' || className.endsWith("[]")) {
            final String itemClassName = (className.charAt(0) == '[') ? className.substring(1) : className.substring(0, className.length() - 2);
            final Class itemClass = loadClass(itemClassName);
            if (itemClass == null) {
                throw new JSONException("load class error " + className);
            }
            return Array.newInstance(itemClass, 0).getClass();
        }
        else {
            final ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            if (contextClassLoader != null) {
                try {
                    return contextClassLoader.loadClass(className);
                }
                catch (ClassNotFoundException ex) {}
            }
            try {
                return JSON.class.getClassLoader().loadClass(className);
            }
            catch (ClassNotFoundException ex2) {
                try {
                    return Class.forName(className);
                }
                catch (ClassNotFoundException ex3) {
                    return null;
                }
            }
        }
    }
    
    public static Class<?> getArrayClass(final Class componentClass) {
        if (componentClass == Integer.TYPE) {
            return int[].class;
        }
        if (componentClass == Byte.TYPE) {
            return byte[].class;
        }
        if (componentClass == Short.TYPE) {
            return short[].class;
        }
        if (componentClass == Long.TYPE) {
            return long[].class;
        }
        if (componentClass == String.class) {
            return String[].class;
        }
        if (componentClass == Object.class) {
            return Object[].class;
        }
        return Array.newInstance(componentClass, 1).getClass();
    }
    
    public static Class nonePrimitive(final Class type) {
        if (type.isPrimitive()) {
            final String name2;
            final String name = name2 = type.getName();
            switch (name2) {
                case "byte": {
                    return Byte.class;
                }
                case "short": {
                    return Short.class;
                }
                case "int": {
                    return Integer.class;
                }
                case "long": {
                    return Long.class;
                }
                case "float": {
                    return Float.class;
                }
                case "double": {
                    return Double.class;
                }
                case "char": {
                    return Character.class;
                }
                case "boolean": {
                    return Boolean.class;
                }
            }
        }
        return type;
    }
    
    public static Class<?> getClass(final Type type) {
        if (type == null) {
            return null;
        }
        if (type.getClass() == Class.class) {
            return (Class<?>)type;
        }
        if (type instanceof ParameterizedType) {
            return getClass(((ParameterizedType)type).getRawType());
        }
        if (type instanceof TypeVariable) {
            final Type boundType = ((TypeVariable)type).getBounds()[0];
            if (boundType instanceof Class) {
                return (Class<?>)boundType;
            }
            return getClass(boundType);
        }
        else {
            if (type instanceof WildcardType) {
                final Type[] upperBounds = ((WildcardType)type).getUpperBounds();
                if (upperBounds.length == 1) {
                    return getClass(upperBounds[0]);
                }
            }
            if (type instanceof GenericArrayType) {
                final GenericArrayType genericArrayType = (GenericArrayType)type;
                final Type componentType = genericArrayType.getGenericComponentType();
                final Class<?> componentClass = getClass(componentType);
                return getArrayClass(componentClass);
            }
            return Object.class;
        }
    }
    
    public static boolean isProxy(final Class<?> clazz) {
        final Class<?>[] interfaces = clazz.getInterfaces();
        final int length = interfaces.length;
        int i = 0;
        while (i < length) {
            final Class<?> item = interfaces[i];
            final String name;
            final String interfaceName = name = item.getName();
            switch (name) {
                case "org.springframework.cglib.proxy.Factory":
                case "javassist.util.proxy.ProxyObject":
                case "org.apache.ibatis.javassist.util.proxy.ProxyObject":
                case "org.hibernate.proxy.HibernateProxy":
                case "org.springframework.context.annotation.ConfigurationClassEnhancer$EnhancedConfiguration":
                case "org.mockito.cglib.proxy.Factory":
                case "net.sf.cglib.proxy.Factory": {
                    return true;
                }
                default: {
                    ++i;
                    continue;
                }
            }
        }
        return false;
    }
    
    public static Map getInnerMap(Map object) {
        if (TypeUtils.CLASS_JSON_OBJECT_1x == null || !TypeUtils.CLASS_JSON_OBJECT_1x.isInstance(object) || TypeUtils.FIELD_JSON_OBJECT_1x_map == null) {
            return object;
        }
        try {
            object = (Map)TypeUtils.FIELD_JSON_OBJECT_1x_map.get(object);
        }
        catch (IllegalAccessException ex) {}
        return object;
    }
    
    public static boolean isFunction(final Class type) {
        if (type.isInterface()) {
            final String typeName = type.getName();
            return typeName.startsWith("java.util.function.") || type.isAnnotationPresent(FunctionalInterface.class);
        }
        return false;
    }
    
    public static boolean isInteger(final String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        char ch = str.charAt(0);
        final boolean sign = ch == '-' || ch == '+';
        if (sign) {
            if (str.length() == 1) {
                return false;
            }
        }
        else if (ch < '0' || ch > '9') {
            return false;
        }
        for (int i = 1; i < str.length(); ++i) {
            ch = str.charAt(i);
            if (ch < '0' || ch > '9') {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isInteger(final byte[] str, final int off, final int len) {
        if (str == null || len == 0) {
            return false;
        }
        char ch = (char)str[off];
        final boolean sign = ch == '-' || ch == '+';
        if (sign) {
            if (len == 1) {
                return false;
            }
        }
        else if (ch < '0' || ch > '9') {
            return false;
        }
        for (int end = off + len, i = off + 1; i < end; ++i) {
            ch = (char)str[i];
            if (ch < '0' || ch > '9') {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isInteger(final char[] str, final int off, final int len) {
        if (str == null || len == 0) {
            return false;
        }
        char ch = str[off];
        final boolean sign = ch == '-' || ch == '+';
        if (sign) {
            if (len == 1) {
                return false;
            }
        }
        else if (ch < '0' || ch > '9') {
            return false;
        }
        for (int end = off + len, i = off + 1; i < end; ++i) {
            ch = str[i];
            if (ch < '0' || ch > '9') {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isNumber(final String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        char ch = str.charAt(0);
        final boolean sign = ch == '-' || ch == '+';
        int offset;
        if (sign) {
            if (str.length() == 1) {
                return false;
            }
            ch = str.charAt(1);
            offset = 1;
        }
        else if (ch == '.') {
            if (str.length() == 1) {
                return false;
            }
            offset = 1;
        }
        else {
            offset = 0;
        }
        final int end = str.length();
        final boolean dot = ch == '.';
        final boolean space = false;
        boolean num = false;
        Label_0167: {
            if (!dot && ch >= '0' && ch <= '9') {
                num = true;
                while (offset < end) {
                    ch = str.charAt(offset++);
                    if (space || ch < '0' || ch > '9') {
                        break Label_0167;
                    }
                }
                return true;
            }
        }
        boolean small = false;
        Label_0248: {
            if (ch == '.') {
                small = true;
                if (offset >= end) {
                    return true;
                }
                ch = str.charAt(offset++);
                if (ch >= '0' && ch <= '9') {
                    while (offset < end) {
                        ch = str.charAt(offset++);
                        if (space || ch < '0' || ch > '9') {
                            break Label_0248;
                        }
                    }
                    return true;
                }
            }
        }
        if (!num && !small) {
            return false;
        }
        if (ch == 'e' || ch == 'E') {
            if (offset == end) {
                return true;
            }
            ch = str.charAt(offset++);
            boolean eSign = false;
            if (ch == '+' || ch == '-') {
                eSign = true;
                if (offset >= end) {
                    return false;
                }
                ch = str.charAt(offset++);
            }
            if (ch >= '0' && ch <= '9') {
                while (offset < end) {
                    ch = str.charAt(offset++);
                    if (ch < '0') {
                        return false;
                    }
                    if (ch > '9') {
                        return false;
                    }
                }
                return true;
            }
            if (eSign) {
                return false;
            }
        }
        return false;
    }
    
    public static boolean isNumber(final byte[] str, final int off, final int len) {
        if (str == null || len == 0) {
            return false;
        }
        char ch = (char)str[off];
        final boolean sign = ch == '-' || ch == '+';
        int offset;
        if (sign) {
            if (len == 1) {
                return false;
            }
            ch = (char)str[off + 1];
            offset = off + 1;
        }
        else if (ch == '.') {
            if (len == 1) {
                return false;
            }
            offset = off + 1;
        }
        else {
            offset = off;
        }
        final int end = off + len;
        final boolean dot = ch == '.';
        final boolean space = false;
        boolean num = false;
        Label_0167: {
            if (!dot && ch >= '0' && ch <= '9') {
                num = true;
                while (offset < end) {
                    ch = (char)str[offset++];
                    if (space || ch < '0' || ch > '9') {
                        break Label_0167;
                    }
                }
                return true;
            }
        }
        boolean small = false;
        Label_0250: {
            if (ch == '.') {
                small = true;
                if (offset >= end) {
                    return true;
                }
                ch = (char)str[offset++];
                if (ch >= '0' && ch <= '9') {
                    while (offset < end) {
                        ch = (char)str[offset++];
                        if (space || ch < '0' || ch > '9') {
                            break Label_0250;
                        }
                    }
                    return true;
                }
            }
        }
        if (!num && !small) {
            return false;
        }
        if (ch == 'e' || ch == 'E') {
            if (offset == end) {
                return true;
            }
            ch = (char)str[offset++];
            boolean eSign = false;
            if (ch == '+' || ch == '-') {
                eSign = true;
                if (offset >= end) {
                    return false;
                }
                ch = (char)str[offset++];
            }
            if (ch >= '0' && ch <= '9') {
                while (offset < end) {
                    ch = (char)str[offset++];
                    if (ch < '0') {
                        return false;
                    }
                    if (ch > '9') {
                        return false;
                    }
                }
                return true;
            }
            if (eSign) {
                return false;
            }
        }
        return false;
    }
    
    public static boolean isNumber(final char[] str, final int off, final int len) {
        if (str == null || len == 0) {
            return false;
        }
        char ch = str[off];
        final boolean sign = ch == '-' || ch == '+';
        int offset;
        if (sign) {
            if (len == 1) {
                return false;
            }
            ch = str[off + 1];
            offset = off + 1;
        }
        else if (ch == '.') {
            if (len == 1) {
                return false;
            }
            offset = off + 1;
        }
        else {
            offset = off;
        }
        final int end = off + len;
        final boolean dot = ch == '.';
        final boolean space = false;
        boolean num = false;
        Label_0164: {
            if (!dot && ch >= '0' && ch <= '9') {
                num = true;
                while (offset < end) {
                    ch = str[offset++];
                    if (space || ch < '0' || ch > '9') {
                        break Label_0164;
                    }
                }
                return true;
            }
        }
        boolean small = false;
        Label_0245: {
            if (ch == '.') {
                small = true;
                if (offset >= end) {
                    return true;
                }
                ch = str[offset++];
                if (ch >= '0' && ch <= '9') {
                    while (offset < end) {
                        ch = str[offset++];
                        if (space || ch < '0' || ch > '9') {
                            break Label_0245;
                        }
                    }
                    return true;
                }
            }
        }
        if (!num && !small) {
            return false;
        }
        if (ch == 'e' || ch == 'E') {
            if (offset == end) {
                return true;
            }
            ch = str[offset++];
            boolean eSign = false;
            if (ch == '+' || ch == '-') {
                eSign = true;
                if (offset >= end) {
                    return false;
                }
                ch = str[offset++];
            }
            if (ch >= '0' && ch <= '9') {
                while (offset < end) {
                    ch = str[offset++];
                    if (ch < '0') {
                        return false;
                    }
                    if (ch > '9') {
                        return false;
                    }
                }
                return true;
            }
            if (eSign) {
                return false;
            }
        }
        return false;
    }
    
    public static boolean isUUID(final String str) {
        if (str == null) {
            return false;
        }
        if (str.length() == 32) {
            for (int i = 0; i < 32; ++i) {
                final char ch = str.charAt(i);
                final boolean valid = (ch >= '0' && ch <= '9') || (ch >= 'A' && ch <= 'F') || (ch >= 'a' && ch <= 'f');
                if (!valid) {
                    return false;
                }
            }
            return true;
        }
        if (str.length() == 36) {
            for (int i = 0; i < 36; ++i) {
                final char ch = str.charAt(i);
                if (i == 8 || i == 13 || i == 18 || i == 23) {
                    if (ch != '-') {
                        return false;
                    }
                }
                else {
                    final boolean valid = (ch >= '0' && ch <= '9') || (ch >= 'A' && ch <= 'F') || (ch >= 'a' && ch <= 'f');
                    if (!valid) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }
    
    public static boolean validateIPv4(final String str) {
        return validateIPv4(str, 0);
    }
    
    static boolean validateIPv4(final String str, final int off) {
        if (str == null) {
            return false;
        }
        final int strlen = str.length();
        final int len = strlen - off;
        if (len < 7 || len > 25) {
            return false;
        }
        int start = off;
        int dotCount = 0;
        for (int i = off; i < strlen; ++i) {
            final char ch = str.charAt(i);
            if (ch == '.' || i == strlen - 1) {
                final int end = (ch == '.') ? i : (i + 1);
                final int n = end - start;
                switch (n) {
                    case 1: {
                        final char c0 = str.charAt(end - 1);
                        if (c0 < '0' || c0 > '9') {
                            return false;
                        }
                        break;
                    }
                    case 2: {
                        final char c0 = str.charAt(end - 2);
                        final char c2 = str.charAt(end - 1);
                        if (c0 < '0' || c0 > '9') {
                            return false;
                        }
                        if (c2 < '0' || c2 > '9') {
                            return false;
                        }
                        break;
                    }
                    case 3: {
                        final char c0 = str.charAt(end - 3);
                        final char c2 = str.charAt(end - 2);
                        final char c3 = str.charAt(end - 1);
                        if (c0 < '0' || c0 > '2') {
                            return false;
                        }
                        if (c2 < '0' || c2 > '9') {
                            return false;
                        }
                        if (c3 < '0' || c3 > '9') {
                            return false;
                        }
                        final int value = (c0 - '0') * 100 + (c2 - '0') * 10 + (c3 - '0');
                        if (value > 255) {
                            return false;
                        }
                        break;
                    }
                    default: {
                        return false;
                    }
                }
                if (ch == '.') {
                    ++dotCount;
                    start = i + 1;
                }
            }
        }
        return dotCount == 3;
    }
    
    public static boolean validateIPv6(final String str) {
        if (str == null) {
            return false;
        }
        final int len = str.length();
        if (len < 2 || len > 39) {
            return false;
        }
        int start = 0;
        int colonCount = 0;
        int i = 0;
        while (i < len) {
            final char ch = str.charAt(i);
            if (ch == '.') {
                final boolean ipV4 = validateIPv4(str, start);
                if (!ipV4) {
                    return false;
                }
                break;
            }
            else {
                if (ch == ':' || i == len - 1) {
                    final int end = (ch == ':') ? i : (i + 1);
                    final int n = end - start;
                    switch (n) {
                        case 0: {
                            break;
                        }
                        case 1: {
                            final char c0 = str.charAt(end - 1);
                            if ((c0 < '0' || c0 > '9') && (c0 < 'A' || c0 > 'F') && (c0 < 'a' || c0 > 'f')) {
                                return false;
                            }
                            break;
                        }
                        case 2: {
                            final char c0 = str.charAt(end - 2);
                            final char c2 = str.charAt(end - 1);
                            if ((c0 < '0' || c0 > '9') && (c0 < 'A' || c0 > 'F') && (c0 < 'a' || c0 > 'f')) {
                                return false;
                            }
                            if ((c2 < '0' || c2 > '9') && (c2 < 'A' || c2 > 'F') && (c2 < 'a' || c2 > 'f')) {
                                return false;
                            }
                            break;
                        }
                        case 3: {
                            final char c0 = str.charAt(end - 3);
                            final char c2 = str.charAt(end - 2);
                            final char c3 = str.charAt(end - 1);
                            if ((c0 < '0' || c0 > '9') && (c0 < 'A' || c0 > 'F') && (c0 < 'a' || c0 > 'f')) {
                                return false;
                            }
                            if ((c2 < '0' || c2 > '9') && (c2 < 'A' || c2 > 'F') && (c2 < 'a' || c2 > 'f')) {
                                return false;
                            }
                            if ((c3 < '0' || c3 > '9') && (c3 < 'A' || c3 > 'F') && (c3 < 'a' || c3 > 'f')) {
                                return false;
                            }
                            break;
                        }
                        case 4: {
                            final char c0 = str.charAt(end - 4);
                            final char c2 = str.charAt(end - 3);
                            final char c3 = str.charAt(end - 2);
                            final char c4 = str.charAt(end - 1);
                            if ((c0 < '0' || c0 > '9') && (c0 < 'A' || c0 > 'F') && (c0 < 'a' || c0 > 'f')) {
                                return false;
                            }
                            if ((c2 < '0' || c2 > '9') && (c2 < 'A' || c2 > 'F') && (c2 < 'a' || c2 > 'f')) {
                                return false;
                            }
                            if ((c3 < '0' || c3 > '9') && (c3 < 'A' || c3 > 'F') && (c3 < 'a' || c3 > 'f')) {
                                return false;
                            }
                            if ((c4 < '0' || c4 > '9') && (c4 < 'A' || c4 > 'F') && (c4 < 'a' || c4 > 'f')) {
                                return false;
                            }
                            break;
                        }
                        default: {
                            return false;
                        }
                    }
                    if (ch == ':') {
                        ++colonCount;
                        start = i + 1;
                    }
                }
                ++i;
            }
        }
        return colonCount > 0 && colonCount < 8;
    }
    
    public static double doubleValue(final int signNum, final long intCompact, final int scale) {
        final int P_D = 53;
        final int Q_MIN_D = -1074;
        final int Q_MAX_D = 971;
        final double L = 3.321928094887362;
        final int bitLength = 64 - Long.numberOfLeadingZeros(intCompact);
        final long qb = bitLength - (long)Math.ceil(scale * 3.321928094887362);
        if (qb < -1076L) {
            return signNum * 0.0;
        }
        if (qb > 1025L) {
            return signNum * Double.POSITIVE_INFINITY;
        }
        if (scale < 0) {
            final BigInteger pow10 = TypeUtils.BIG_TEN_POWERS_TABLE[-scale];
            final BigInteger w = BigInteger.valueOf(intCompact);
            return signNum * w.multiply(pow10).doubleValue();
        }
        if (scale == 0) {
            return signNum * (double)intCompact;
        }
        final BigInteger w2 = BigInteger.valueOf(intCompact);
        final int ql = (int)qb - 56;
        final BigInteger pow11 = TypeUtils.BIG_TEN_POWERS_TABLE[scale];
        BigInteger m;
        BigInteger n;
        if (ql <= 0) {
            m = w2.shiftLeft(-ql);
            n = pow11;
        }
        else {
            m = w2;
            n = pow11.shiftLeft(ql);
        }
        final BigInteger[] qr = m.divideAndRemainder(n);
        final long i = qr[0].longValue();
        final int sb = qr[1].signum();
        final int dq = 9 - Long.numberOfLeadingZeros(i);
        final int eq = -1076 - ql;
        if (dq >= eq) {
            return signNum * Math.scalb((double)(i | (long)sb), ql);
        }
        final long mask = (1L << eq) - 1L;
        final long j = i >> eq | (long)Long.signum(i & mask) | (long)sb;
        return signNum * Math.scalb((double)j, -1076);
    }
    
    public static float floatValue(final int signNum, final long intCompact, final int scale) {
        final int P_F = 24;
        final int Q_MIN_F = -149;
        final int Q_MAX_F = 104;
        final double L = 3.321928094887362;
        final int bitLength = 64 - Long.numberOfLeadingZeros(intCompact);
        final long qb = bitLength - (long)Math.ceil(scale * 3.321928094887362);
        if (qb < -151L) {
            return signNum * 0.0f;
        }
        if (qb > 129L) {
            return signNum * Float.POSITIVE_INFINITY;
        }
        if (scale < 0) {
            final BigInteger w = BigInteger.valueOf(intCompact);
            return signNum * w.multiply(TypeUtils.BIG_TEN_POWERS_TABLE[-scale]).floatValue();
        }
        final BigInteger w = BigInteger.valueOf(intCompact);
        final int ql = (int)qb - 27;
        final BigInteger pow10 = TypeUtils.BIG_TEN_POWERS_TABLE[scale];
        BigInteger m;
        BigInteger n;
        if (ql <= 0) {
            m = w.shiftLeft(-ql);
            n = pow10;
        }
        else {
            m = w;
            n = pow10.shiftLeft(ql);
        }
        final BigInteger[] qr = m.divideAndRemainder(n);
        final int i = qr[0].intValue();
        final int sb = qr[1].signum();
        final int dq = 6 - Integer.numberOfLeadingZeros(i);
        final int eq = -151 - ql;
        if (dq >= eq) {
            return signNum * Math.scalb((float)(i | sb), ql);
        }
        final int mask = (1 << eq) - 1;
        final int j = i >> eq | Integer.signum(i & mask) | sb;
        return signNum * Math.scalb((float)j, -151);
    }
    
    static {
        CLASS_SINGLE_SET = Collections.singleton(1).getClass();
        CLASS_SINGLE_LIST = Collections.singletonList(1).getClass();
        CLASS_UNMODIFIABLE_COLLECTION = Collections.unmodifiableCollection((Collection<?>)new ArrayList<Object>()).getClass();
        CLASS_UNMODIFIABLE_LIST = Collections.unmodifiableList((List<?>)new ArrayList<Object>()).getClass();
        CLASS_UNMODIFIABLE_SET = Collections.unmodifiableSet((Set<?>)new HashSet<Object>()).getClass();
        CLASS_UNMODIFIABLE_SORTED_SET = Collections.unmodifiableSortedSet(new TreeSet<Object>()).getClass();
        CLASS_UNMODIFIABLE_NAVIGABLE_SET = Collections.unmodifiableNavigableSet(new TreeSet<Object>()).getClass();
        PARAM_TYPE_LIST_STR = new ParameterizedTypeImpl(List.class, new Type[] { String.class });
        METHOD_TYPE_SUPPLIER = MethodType.methodType(Supplier.class);
        METHOD_TYPE_FUNCTION = MethodType.methodType(Function.class);
        METHOD_TYPE_TO_INT_FUNCTION = MethodType.methodType(ToIntFunction.class);
        METHOD_TYPE_TO_LONG_FUNCTION = MethodType.methodType(ToLongFunction.class);
        METHOD_TYPE_OBJECT_INT_CONSUMER = MethodType.methodType(ObjIntConsumer.class);
        METHOD_TYPE_INT_FUNCTION = MethodType.methodType(IntFunction.class);
        METHOD_TYPE_LONG_FUNCTION = MethodType.methodType(LongFunction.class);
        METHOD_TYPE_BI_FUNCTION = MethodType.methodType(BiFunction.class);
        METHOD_TYPE_BI_CONSUMER = MethodType.methodType(BiConsumer.class);
        METHOD_TYPE_VOO = MethodType.methodType(Void.TYPE, Object.class, Object.class);
        METHOD_TYPE_OBJECT = MethodType.methodType(Object.class);
        METHOD_TYPE_OBJECT_OBJECT = MethodType.methodType(Object.class, Object.class);
        METHOD_TYPE_INT_OBJECT = MethodType.methodType(Integer.TYPE, Object.class);
        METHOD_TYPE_LONG_OBJECT = MethodType.methodType(Long.TYPE, Object.class);
        METHOD_TYPE_VOID_OBJECT_INT = MethodType.methodType(Void.TYPE, Object.class, Integer.TYPE);
        METHOD_TYPE_OBJECT_LONG = MethodType.methodType(Object.class, Long.TYPE);
        METHOD_TYPE_VOID_LONG = MethodType.methodType(Void.TYPE, Long.TYPE);
        METHOD_TYPE_OBJECT_OBJECT_OBJECT = MethodType.methodType(Object.class, Object.class, Object.class);
        METHOD_TYPE_VOID = MethodType.methodType(Void.TYPE);
        METHOD_TYPE_VOID_INT = MethodType.methodType(Void.TYPE, Integer.TYPE);
        METHOD_TYPE_VOID_STRING = MethodType.methodType(Void.TYPE, String.class);
        METHOD_TYPE_OBJECT_INT = MethodType.methodType(Object.class, Integer.TYPE);
        BIGINT_INT32_MIN = BigInteger.valueOf(-2147483648L);
        BIGINT_INT32_MAX = BigInteger.valueOf(2147483647L);
        BIGINT_INT64_MIN = BigInteger.valueOf(Long.MIN_VALUE);
        BIGINT_INT64_MAX = BigInteger.valueOf(Long.MAX_VALUE);
        SMALL_10_POW = new double[] { 1.0, 10.0, 100.0, 1000.0, 10000.0, 100000.0, 1000000.0, 1.0E7, 1.0E8, 1.0E9, 1.0E10, 1.0E11, 1.0E12, 1.0E13, 1.0E14, 1.0E15, 1.0E16, 1.0E17, 1.0E18, 1.0E19, 1.0E20, 1.0E21, 1.0E22 };
        SINGLE_SMALL_10_POW = new float[] { 1.0f, 10.0f, 100.0f, 1000.0f, 10000.0f, 100000.0f, 1000000.0f, 1.0E7f, 1.0E8f, 1.0E9f, 1.0E10f };
        BIG_10_POW = new double[] { 1.0E16, 1.0E32, 1.0E64, 1.0E128, 1.0E256 };
        TINY_10_POW = new double[] { 1.0E-16, 1.0E-32, 1.0E-64, 1.0E-128, 1.0E-256 };
        CACHE = new Cache();
        CHARS_UPDATER = AtomicReferenceFieldUpdater.newUpdater(Cache.class, char[].class, "chars");
        NAME_MAPPINGS = new IdentityHashMap<Class, String>();
        TYPE_MAPPINGS = new ConcurrentHashMap<String, Class>();
        CLASS_JSON_OBJECT_1x = loadClass("com.alibaba.fastjson.JSONObject");
        Field field = null;
        if (TypeUtils.CLASS_JSON_OBJECT_1x != null) {
            try {
                field = TypeUtils.CLASS_JSON_OBJECT_1x.getDeclaredField("map");
                field.setAccessible(true);
            }
            catch (Throwable t) {}
        }
        FIELD_JSON_OBJECT_1x_map = field;
        CLASS_JSON_ARRAY_1x = loadClass("com.alibaba.fastjson.JSONArray");
        TypeUtils.NAME_MAPPINGS.put(Byte.TYPE, "B");
        TypeUtils.NAME_MAPPINGS.put(Short.TYPE, "S");
        TypeUtils.NAME_MAPPINGS.put(Integer.TYPE, "I");
        TypeUtils.NAME_MAPPINGS.put(Long.TYPE, "J");
        TypeUtils.NAME_MAPPINGS.put(Float.TYPE, "F");
        TypeUtils.NAME_MAPPINGS.put(Double.TYPE, "D");
        TypeUtils.NAME_MAPPINGS.put(Character.TYPE, "C");
        TypeUtils.NAME_MAPPINGS.put(Boolean.TYPE, "Z");
        TypeUtils.NAME_MAPPINGS.put(Object[].class, "[O");
        TypeUtils.NAME_MAPPINGS.put(Object[][].class, "[[O");
        TypeUtils.NAME_MAPPINGS.put(byte[].class, "[B");
        TypeUtils.NAME_MAPPINGS.put(byte[][].class, "[[B");
        TypeUtils.NAME_MAPPINGS.put(short[].class, "[S");
        TypeUtils.NAME_MAPPINGS.put(short[][].class, "[[S");
        TypeUtils.NAME_MAPPINGS.put(int[].class, "[I");
        TypeUtils.NAME_MAPPINGS.put(int[][].class, "[[I");
        TypeUtils.NAME_MAPPINGS.put(long[].class, "[J");
        TypeUtils.NAME_MAPPINGS.put(long[][].class, "[[J");
        TypeUtils.NAME_MAPPINGS.put(float[].class, "[F");
        TypeUtils.NAME_MAPPINGS.put(float[][].class, "[[F");
        TypeUtils.NAME_MAPPINGS.put(double[].class, "[D");
        TypeUtils.NAME_MAPPINGS.put(double[][].class, "[[D");
        TypeUtils.NAME_MAPPINGS.put(char[].class, "[C");
        TypeUtils.NAME_MAPPINGS.put(char[][].class, "[[C");
        TypeUtils.NAME_MAPPINGS.put(boolean[].class, "[Z");
        TypeUtils.NAME_MAPPINGS.put(boolean[][].class, "[[Z");
        TypeUtils.NAME_MAPPINGS.put(Byte[].class, "[Byte");
        TypeUtils.NAME_MAPPINGS.put(Byte[][].class, "[[Byte");
        TypeUtils.NAME_MAPPINGS.put(Short[].class, "[Short");
        TypeUtils.NAME_MAPPINGS.put(Short[][].class, "[[Short");
        TypeUtils.NAME_MAPPINGS.put(Integer[].class, "[Integer");
        TypeUtils.NAME_MAPPINGS.put(Integer[][].class, "[[Integer");
        TypeUtils.NAME_MAPPINGS.put(Long[].class, "[Long");
        TypeUtils.NAME_MAPPINGS.put(Long[][].class, "[[Long");
        TypeUtils.NAME_MAPPINGS.put(Float[].class, "[Float");
        TypeUtils.NAME_MAPPINGS.put(Float[][].class, "[[Float");
        TypeUtils.NAME_MAPPINGS.put(Double[].class, "[Double");
        TypeUtils.NAME_MAPPINGS.put(Double[][].class, "[[Double");
        TypeUtils.NAME_MAPPINGS.put(Character[].class, "[Character");
        TypeUtils.NAME_MAPPINGS.put(Character[][].class, "[[Character");
        TypeUtils.NAME_MAPPINGS.put(Boolean[].class, "[Boolean");
        TypeUtils.NAME_MAPPINGS.put(Boolean[][].class, "[[Boolean");
        TypeUtils.NAME_MAPPINGS.put(String[].class, "[String");
        TypeUtils.NAME_MAPPINGS.put(String[][].class, "[[String");
        TypeUtils.NAME_MAPPINGS.put(BigDecimal[].class, "[BigDecimal");
        TypeUtils.NAME_MAPPINGS.put(BigDecimal[][].class, "[[BigDecimal");
        TypeUtils.NAME_MAPPINGS.put(BigInteger[].class, "[BigInteger");
        TypeUtils.NAME_MAPPINGS.put(BigInteger[][].class, "[[BigInteger");
        TypeUtils.NAME_MAPPINGS.put(UUID[].class, "[UUID");
        TypeUtils.NAME_MAPPINGS.put(UUID[][].class, "[[UUID");
        TypeUtils.NAME_MAPPINGS.put(Object.class, "Object");
        TypeUtils.NAME_MAPPINGS.put(Object[].class, "[O");
        TypeUtils.NAME_MAPPINGS.put(HashMap.class, "M");
        TypeUtils.TYPE_MAPPINGS.put("HashMap", HashMap.class);
        TypeUtils.TYPE_MAPPINGS.put("java.util.HashMap", HashMap.class);
        TypeUtils.NAME_MAPPINGS.put(LinkedHashMap.class, "LM");
        TypeUtils.TYPE_MAPPINGS.put("LinkedHashMap", LinkedHashMap.class);
        TypeUtils.TYPE_MAPPINGS.put("java.util.LinkedHashMap", LinkedHashMap.class);
        TypeUtils.NAME_MAPPINGS.put(TreeMap.class, "TM");
        TypeUtils.TYPE_MAPPINGS.put("TreeMap", TreeMap.class);
        TypeUtils.NAME_MAPPINGS.put(ArrayList.class, "A");
        TypeUtils.TYPE_MAPPINGS.put("ArrayList", ArrayList.class);
        TypeUtils.TYPE_MAPPINGS.put("java.util.ArrayList", ArrayList.class);
        TypeUtils.NAME_MAPPINGS.put(LinkedList.class, "LA");
        TypeUtils.TYPE_MAPPINGS.put("LA", LinkedList.class);
        TypeUtils.TYPE_MAPPINGS.put("LinkedList", LinkedList.class);
        TypeUtils.TYPE_MAPPINGS.put("java.util.LinkedList", LinkedList.class);
        TypeUtils.TYPE_MAPPINGS.put("java.util.concurrent.ConcurrentLinkedQueue", ConcurrentLinkedQueue.class);
        TypeUtils.TYPE_MAPPINGS.put("java.util.concurrent.ConcurrentLinkedDeque", ConcurrentLinkedDeque.class);
        TypeUtils.NAME_MAPPINGS.put(HashSet.class, "HashSet");
        TypeUtils.NAME_MAPPINGS.put(TreeSet.class, "TreeSet");
        TypeUtils.NAME_MAPPINGS.put(LinkedHashSet.class, "LinkedHashSet");
        TypeUtils.NAME_MAPPINGS.put(ConcurrentHashMap.class, "ConcurrentHashMap");
        TypeUtils.NAME_MAPPINGS.put(ConcurrentLinkedQueue.class, "ConcurrentLinkedQueue");
        TypeUtils.NAME_MAPPINGS.put(ConcurrentLinkedDeque.class, "ConcurrentLinkedDeque");
        TypeUtils.NAME_MAPPINGS.put(JSONObject.class, "JSONObject");
        TypeUtils.NAME_MAPPINGS.put(JSONArray.class, "JSONArray");
        TypeUtils.NAME_MAPPINGS.put(Currency.class, "Currency");
        TypeUtils.NAME_MAPPINGS.put(TimeUnit.class, "TimeUnit");
        final Class[] array;
        final Class<?>[] classes = (Class<?>[])(array = new Class[] { Object.class, Cloneable.class, AutoCloseable.class, Exception.class, RuntimeException.class, IllegalAccessError.class, IllegalAccessException.class, IllegalArgumentException.class, IllegalMonitorStateException.class, IllegalStateException.class, IllegalThreadStateException.class, IndexOutOfBoundsException.class, InstantiationError.class, InstantiationException.class, InternalError.class, InterruptedException.class, LinkageError.class, NegativeArraySizeException.class, NoClassDefFoundError.class, NoSuchFieldError.class, NoSuchFieldException.class, NoSuchMethodError.class, NoSuchMethodException.class, NullPointerException.class, NumberFormatException.class, OutOfMemoryError.class, SecurityException.class, StackOverflowError.class, StringIndexOutOfBoundsException.class, TypeNotPresentException.class, VerifyError.class, StackTraceElement.class, Hashtable.class, TreeMap.class, IdentityHashMap.class, WeakHashMap.class, HashSet.class, LinkedHashSet.class, TreeSet.class, LinkedList.class, TimeUnit.class, ConcurrentHashMap.class, AtomicInteger.class, AtomicLong.class, Collections.EMPTY_MAP.getClass(), Boolean.class, Character.class, Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class, Number.class, String.class, BigDecimal.class, BigInteger.class, BitSet.class, Calendar.class, Date.class, Locale.class, UUID.class, Currency.class, SimpleDateFormat.class, JSONObject.class, JSONArray.class, ConcurrentSkipListMap.class, ConcurrentSkipListSet.class });
        for (final Class clazz : array) {
            TypeUtils.TYPE_MAPPINGS.put(clazz.getSimpleName(), clazz);
            TypeUtils.TYPE_MAPPINGS.put(clazz.getName(), clazz);
            TypeUtils.NAME_MAPPINGS.put(clazz, clazz.getSimpleName());
        }
        TypeUtils.TYPE_MAPPINGS.put("JO10", JSONObject1O.class);
        TypeUtils.TYPE_MAPPINGS.put("[O", Object[].class);
        TypeUtils.TYPE_MAPPINGS.put("[Ljava.lang.Object;", Object[].class);
        TypeUtils.TYPE_MAPPINGS.put("[java.lang.Object", Object[].class);
        TypeUtils.TYPE_MAPPINGS.put("[Object", Object[].class);
        TypeUtils.TYPE_MAPPINGS.put("StackTraceElement", StackTraceElement.class);
        TypeUtils.TYPE_MAPPINGS.put("[StackTraceElement", StackTraceElement[].class);
        final String[] array2;
        final String[] items = array2 = new String[] { "java.util.Collections$UnmodifiableMap", "java.util.Collections$UnmodifiableCollection" };
        for (final String className : array2) {
            final Class<?> clazz2 = (Class<?>)loadClass(className);
            TypeUtils.TYPE_MAPPINGS.put(clazz2.getName(), clazz2);
        }
        if (TypeUtils.CLASS_JSON_OBJECT_1x != null) {
            TypeUtils.TYPE_MAPPINGS.putIfAbsent("JO1", TypeUtils.CLASS_JSON_OBJECT_1x);
            TypeUtils.TYPE_MAPPINGS.putIfAbsent(TypeUtils.CLASS_JSON_OBJECT_1x.getName(), TypeUtils.CLASS_JSON_OBJECT_1x);
        }
        if (TypeUtils.CLASS_JSON_ARRAY_1x != null) {
            TypeUtils.TYPE_MAPPINGS.putIfAbsent("JA1", TypeUtils.CLASS_JSON_ARRAY_1x);
            TypeUtils.TYPE_MAPPINGS.putIfAbsent(TypeUtils.CLASS_JSON_ARRAY_1x.getName(), TypeUtils.CLASS_JSON_ARRAY_1x);
        }
        TypeUtils.NAME_MAPPINGS.put(new HashMap().keySet().getClass(), "Set");
        TypeUtils.NAME_MAPPINGS.put(new LinkedHashMap().keySet().getClass(), "Set");
        TypeUtils.NAME_MAPPINGS.put(new TreeMap().keySet().getClass(), "Set");
        TypeUtils.NAME_MAPPINGS.put(new ConcurrentHashMap().keySet().getClass(), "Set");
        TypeUtils.NAME_MAPPINGS.put(new ConcurrentSkipListMap().keySet().getClass(), "Set");
        TypeUtils.TYPE_MAPPINGS.put("Set", HashSet.class);
        TypeUtils.NAME_MAPPINGS.put(new HashMap().values().getClass(), "List");
        TypeUtils.NAME_MAPPINGS.put(new LinkedHashMap().values().getClass(), "List");
        TypeUtils.NAME_MAPPINGS.put(new TreeMap().values().getClass(), "List");
        TypeUtils.NAME_MAPPINGS.put(new ConcurrentHashMap().values().getClass(), "List");
        TypeUtils.NAME_MAPPINGS.put(new ConcurrentSkipListMap().values().getClass(), "List");
        TypeUtils.TYPE_MAPPINGS.put("List", ArrayList.class);
        TypeUtils.TYPE_MAPPINGS.put("java.util.ImmutableCollections$Map1", HashMap.class);
        TypeUtils.TYPE_MAPPINGS.put("java.util.ImmutableCollections$MapN", LinkedHashMap.class);
        TypeUtils.TYPE_MAPPINGS.put("java.util.ImmutableCollections$Set12", LinkedHashSet.class);
        TypeUtils.TYPE_MAPPINGS.put("java.util.ImmutableCollections$SetN", LinkedHashSet.class);
        TypeUtils.TYPE_MAPPINGS.put("java.util.ImmutableCollections$List12", ArrayList.class);
        TypeUtils.TYPE_MAPPINGS.put("java.util.ImmutableCollections$ListN", ArrayList.class);
        TypeUtils.TYPE_MAPPINGS.put("java.util.ImmutableCollections$SubList", ArrayList.class);
        for (final Map.Entry<Class, String> entry : TypeUtils.NAME_MAPPINGS.entrySet()) {
            TypeUtils.TYPE_MAPPINGS.putIfAbsent(entry.getValue(), entry.getKey());
        }
        final BigInteger[] bigInts = new BigInteger[128];
        bigInts[0] = BigInteger.ONE;
        bigInts[1] = BigInteger.TEN;
        long longValue = 10L;
        for (int i = 2; i < 19; ++i) {
            longValue *= 10L;
            bigInts[i] = BigInteger.valueOf(longValue);
        }
        BigInteger bigInt = bigInts[18];
        for (int j = 19; j < 128; ++j) {
            bigInt = bigInt.multiply(BigInteger.TEN);
            bigInts[j] = bigInt;
        }
        BIG_TEN_POWERS_TABLE = bigInts;
    }
    
    static class X1
    {
        static final Function<byte[], char[]> TO_CHARS;
        
        static {
            Function<byte[], char[]> toChars = null;
            if (JDKUtils.JVM_VERSION > 9) {
                try {
                    final Class<?> latin1Class = Class.forName("java.lang.StringLatin1");
                    final MethodHandles.Lookup lookup = JDKUtils.trustedLookup(latin1Class);
                    final MethodHandle handle = lookup.findStatic(latin1Class, "toChars", MethodType.methodType(char[].class, byte[].class));
                    final CallSite callSite = LambdaMetafactory.metafactory(lookup, "apply", MethodType.methodType(Function.class), MethodType.methodType(Object.class, Object.class), handle, MethodType.methodType(char[].class, byte[].class));
                    toChars = (Function<byte[], char[]>)callSite.getTarget().invokeExact();
                }
                catch (Throwable t) {}
            }
            if (toChars == null) {
                toChars = TypeUtils::toAsciiCharArray;
            }
            TO_CHARS = toChars;
        }
    }
    
    static class X2
    {
        static final String[] chars;
        static final String[] chars2;
        static final char START = ' ';
        static final char END = '~';
        static final int SIZE2 = 95;
        
        static {
            final String[] array0 = new String[128];
            for (char i = '\0'; i < array0.length; ++i) {
                array0[i] = Character.toString(i);
            }
            chars = array0;
            final String[] array2 = new String[9025];
            final char[] c2 = new char[2];
            for (char j = ' '; j <= '~'; ++j) {
                for (char k = ' '; k <= '~'; ++k) {
                    final int value = (j - ' ') * 95 + (k - ' ');
                    c2[0] = j;
                    c2[1] = k;
                    array2[value] = new String(c2);
                }
            }
            chars2 = array2;
        }
    }
    
    static class Cache
    {
        volatile char[] chars;
    }
}
