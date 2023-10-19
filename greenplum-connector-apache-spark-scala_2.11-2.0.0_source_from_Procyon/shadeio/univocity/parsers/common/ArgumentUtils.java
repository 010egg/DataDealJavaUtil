// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import shadeio.univocity.parsers.common.input.BomInput;
import java.io.Reader;
import java.io.InputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.io.Writer;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Arrays;
import shadeio.univocity.parsers.common.fields.FieldSelector;

public class ArgumentUtils
{
    public static final String[] EMPTY_STRING_ARRAY;
    
    public static <T> void notEmpty(final String argDescription, final T... args) {
        if (args == null) {
            throw new IllegalArgumentException(argDescription + " must not be null");
        }
        if (args.length == 0) {
            throw new IllegalArgumentException(argDescription + " must not be empty");
        }
    }
    
    public static <T> void noNulls(final String argDescription, final T... args) {
        notEmpty(argDescription, (Object[])args);
        final Object[] arr$ = args;
        final int len$ = arr$.length;
        int i$ = 0;
        while (i$ < len$) {
            final T arg = (T)arr$[i$];
            if (arg == null) {
                if (args.length > 0) {
                    throw new IllegalArgumentException(argDescription + " must not contain nulls");
                }
                throw new IllegalArgumentException(argDescription + " must not be null");
            }
            else {
                ++i$;
            }
        }
    }
    
    public static int indexOf(final String[] array, final String element, final FieldSelector fieldSelector) {
        final int index = indexOf(array, element);
        if (fieldSelector == null || index == -1) {
            return index;
        }
        final int[] indexes = fieldSelector.getFieldIndexes(array);
        for (int i = 0; i < indexes.length; ++i) {
            if (indexes[i] == index) {
                return i;
            }
        }
        return -1;
    }
    
    public static int[] indexesOf(final Object[] array, final Object element) {
        int[] tmp = new int[0];
        int i = 0;
        int o = 0;
        while (i < array.length) {
            i = indexOf(array, element, i);
            if (i == -1) {
                break;
            }
            tmp = Arrays.copyOf(tmp, tmp.length + 1);
            tmp[o++] = i;
            ++i;
        }
        return tmp;
    }
    
    public static int indexOf(final Object[] array, final Object element) {
        return indexOf(array, element, 0);
    }
    
    private static int indexOf(final Object[] array, final Object element, final int from) {
        if (array == null) {
            throw new NullPointerException("Null array");
        }
        if (element == null) {
            for (int i = from; i < array.length; ++i) {
                if (array[i] == null) {
                    return i;
                }
            }
        }
        else if (element instanceof String && array instanceof String[]) {
            for (int i = from; i < array.length; ++i) {
                final String e = String.valueOf(array[i]);
                if (element.toString().equalsIgnoreCase(e)) {
                    return i;
                }
            }
        }
        else {
            for (int i = from; i < array.length; ++i) {
                if (element.equals(array[i])) {
                    return i;
                }
            }
        }
        return -1;
    }
    
    public static Object[] findMissingElements(final Object[] array, final Collection<?> elements) {
        return findMissingElements(array, elements.toArray());
    }
    
    public static Object[] findMissingElements(final Object[] array, final Object[] elements) {
        final List<Object> out = new ArrayList<Object>();
        for (final Object element : elements) {
            if (indexOf(array, element) == -1) {
                out.add(element);
            }
        }
        return out.toArray();
    }
    
    public static String[] normalize(final String[] strings) {
        final String[] out = new String[strings.length];
        for (int i = 0; i < strings.length; ++i) {
            out[i] = normalize(strings[i]);
        }
        return out;
    }
    
    public static String normalize(final String string) {
        if (string == null) {
            return null;
        }
        return string.trim().toLowerCase();
    }
    
    public static void normalize(final Collection<String> strings) {
        final LinkedHashSet<String> normalized = new LinkedHashSet<String>(strings.size());
        for (final String string : strings) {
            if (string == null) {
                normalized.add(null);
            }
            else {
                normalized.add(string.trim().toLowerCase());
            }
        }
        strings.clear();
        strings.addAll(normalized);
    }
    
    public static Writer newWriter(final OutputStream output) {
        return newWriter(output, (Charset)null);
    }
    
    public static Writer newWriter(final OutputStream output, final String encoding) {
        return newWriter(output, Charset.forName(encoding));
    }
    
    public static Writer newWriter(final OutputStream output, final Charset encoding) {
        if (encoding != null) {
            return new OutputStreamWriter(output, encoding);
        }
        return new OutputStreamWriter(output);
    }
    
    public static Writer newWriter(final File file) {
        return newWriter(file, (Charset)null);
    }
    
    public static Writer newWriter(final File file, final String encoding) {
        return newWriter(file, Charset.forName(encoding));
    }
    
    public static Writer newWriter(final File file, final Charset encoding) {
        if (!file.exists()) {
            final File parent = file.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            try {
                file.createNewFile();
            }
            catch (IOException e) {
                throw new IllegalArgumentException("Unable to create file '" + file.getAbsolutePath() + "', please ensure your application has permission to create files in that path", e);
            }
        }
        FileOutputStream os;
        try {
            os = new FileOutputStream(file);
        }
        catch (FileNotFoundException e2) {
            throw new IllegalArgumentException(e2);
        }
        return newWriter(os, encoding);
    }
    
    public static Reader newReader(final InputStream input) {
        return newReader(input, (Charset)null);
    }
    
    public static Reader newReader(final InputStream input, final String encoding) {
        return newReader(input, (encoding == null) ? ((Charset)null) : Charset.forName(encoding));
    }
    
    public static Reader newReader(InputStream input, Charset encoding) {
        if (encoding == null) {
            final BomInput bomInput = new BomInput(input);
            if (bomInput.getEncoding() != null) {
                encoding = bomInput.getCharset();
            }
            if (bomInput.hasBytesStored()) {
                input = bomInput;
            }
        }
        if (encoding != null) {
            return new InputStreamReader(input, encoding);
        }
        return new InputStreamReader(input);
    }
    
    public static Reader newReader(final File file) {
        return newReader(file, (Charset)null);
    }
    
    public static Reader newReader(final File file, final String encoding) {
        return newReader(file, Charset.forName(encoding));
    }
    
    public static Reader newReader(final File file, final Charset encoding) {
        FileInputStream input;
        try {
            input = new FileInputStream(file);
        }
        catch (FileNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
        return newReader(input, encoding);
    }
    
    public static String[] toArray(final List<Enum> enums) {
        final String[] out = new String[enums.size()];
        for (int i = 0; i < out.length; ++i) {
            out[i] = enums.get(i).toString();
        }
        return out;
    }
    
    public static int[] toIntArray(final Collection<Integer> ints) {
        final int[] out = new int[ints.size()];
        int i = 0;
        for (final Integer boxed : ints) {
            out[i++] = boxed;
        }
        return out;
    }
    
    public static char[] toCharArray(final Collection<Character> characters) {
        final char[] out = new char[characters.size()];
        int i = 0;
        for (final Character boxed : characters) {
            out[i++] = boxed;
        }
        return out;
    }
    
    public static String restrictContent(final int length, final CharSequence content) {
        if (content == null) {
            return null;
        }
        if (length == 0) {
            return "<omitted>";
        }
        if (length == -1) {
            return content.toString();
        }
        final int errorMessageStart = content.length() - length;
        if (length > 0 && errorMessageStart > 0) {
            return "..." + content.subSequence(errorMessageStart, content.length()).toString();
        }
        return content.toString();
    }
    
    public static String restrictContent(final int length, final Object content) {
        if (content == null) {
            return null;
        }
        if (content instanceof Object[]) {
            return restrictContent(length, Arrays.toString((Object[])content));
        }
        return restrictContent(length, String.valueOf(content));
    }
    
    public static void throwUnchecked(final Throwable error) {
        throwsUnchecked(error);
    }
    
    private static <T extends Exception> void throwsUnchecked(final Throwable toThrow) throws T, Exception {
        throw (Exception)toThrow;
    }
    
    public static byte[] toByteArray(final int... ints) {
        final byte[] out = new byte[ints.length];
        for (int i = 0; i < ints.length; ++i) {
            out[i] = (byte)ints[i];
        }
        return out;
    }
    
    static {
        EMPTY_STRING_ARRAY = new String[0];
    }
}
