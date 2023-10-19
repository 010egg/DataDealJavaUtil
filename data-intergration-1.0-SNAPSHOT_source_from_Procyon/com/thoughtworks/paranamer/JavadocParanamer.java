// 
// Decompiled by Procyon v0.5.36
// 

package com.thoughtworks.paranamer;

import java.io.FileInputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.net.URLConnection;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Member;
import java.lang.reflect.AccessibleObject;
import java.net.URL;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.File;

public class JavadocParanamer implements Paranamer
{
    private final JavadocProvider provider;
    public static final String __PARANAMER_DATA = "<init> java.io.File archiveOrDirectory \n<init> java.net.URL url \ngetCanonicalName java.lang.Class klass \ngetJavadocFilename java.lang.reflect.Member member \nlookupParameterNames java.lang.reflect.AccessibleObject accessible \nlookupParameterNames java.lang.reflect.AccessibleObject,boolean accessible,throwExceptionIfMissing \nstreamToString java.io.InputStream input \nurlToStream java.net.URL url \n";
    
    public JavadocParanamer(final File archiveOrDirectory) throws IOException {
        if (!archiveOrDirectory.exists()) {
            throw new FileNotFoundException(archiveOrDirectory.getAbsolutePath());
        }
        if (archiveOrDirectory.isDirectory()) {
            this.provider = new DirJavadocProvider(archiveOrDirectory);
        }
        else {
            if (!archiveOrDirectory.isFile()) {
                throw new IllegalArgumentException("neither file nor directory: " + archiveOrDirectory);
            }
            this.provider = new ZipJavadocProvider(archiveOrDirectory);
        }
    }
    
    public JavadocParanamer(final URL url) throws IOException {
        this.provider = new UrlJavadocProvider(url);
    }
    
    public String[] lookupParameterNames(final AccessibleObject accessible) {
        return this.lookupParameterNames(accessible, true);
    }
    
    public String[] lookupParameterNames(final AccessibleObject accessible, final boolean throwExceptionIfMissing) {
        if (!(accessible instanceof Member)) {
            throw new IllegalArgumentException(accessible.getClass().getCanonicalName());
        }
        try {
            final String javadocFilename = getJavadocFilename((Member)accessible);
            final InputStream stream = this.provider.getRawJavadoc(javadocFilename);
            final String raw = streamToString(stream);
            if (accessible instanceof Method) {
                return this.getMethodParameterNames((Method)accessible, raw);
            }
            if (accessible instanceof Constructor) {
                return this.getConstructorParameterNames((Constructor<?>)accessible, raw);
            }
            throw new IllegalArgumentException(accessible.getClass().getCanonicalName());
        }
        catch (IOException e) {
            if (throwExceptionIfMissing) {
                throw new ParameterNamesNotFoundException(accessible.toString(), e);
            }
            return Paranamer.EMPTY_NAMES;
        }
        catch (ParameterNamesNotFoundException e2) {
            if (throwExceptionIfMissing) {
                throw e2;
            }
            return Paranamer.EMPTY_NAMES;
        }
    }
    
    private String[] getConstructorParameterNames(final Constructor<?> cons, final String raw) {
        return this.getParameterNames(cons, cons.getDeclaringClass().getSimpleName(), cons.getParameterTypes(), raw);
    }
    
    private String[] getMethodParameterNames(final Method method, final String raw) {
        return this.getParameterNames(method, method.getName(), method.getParameterTypes(), raw);
    }
    
    private String[] getParameterNames(final AccessibleObject a, final String name, final Class<?>[] types, final String raw) {
        if (types.length == 0) {
            return new String[0];
        }
        final StringBuilder regex = new StringBuilder();
        regex.append(String.format(">\\Q%s\\E</A></(?:B|strong)>\\(", name));
        for (final Class klass : types) {
            regex.append(String.format(",?\\s*(?:<A[^>]+>)?[\\w.]*\\Q%s\\E(?:</A>)?(?:&lt;[^&]+&gt;)?&nbsp;([^),\\s]+)", klass.getSimpleName()));
        }
        regex.append(String.format("\\)</CODE>", new Object[0]));
        final Pattern pattern = Pattern.compile(regex.toString(), 10);
        final Matcher matcher = pattern.matcher(raw);
        if (!matcher.find()) {
            throw new ParameterNamesNotFoundException(a + ", " + (Object)regex);
        }
        final String[] names = new String[types.length];
        for (int i = 0; i < names.length; ++i) {
            names[i] = matcher.group(1 + i).trim();
        }
        return names;
    }
    
    protected static String getJavadocFilename(final Member member) {
        return getCanonicalName(member.getDeclaringClass()).replace('.', '/') + ".html";
    }
    
    protected static String getCanonicalName(final Class<?> klass) {
        if (klass.isArray()) {
            return getCanonicalName(klass.getComponentType()) + "[]";
        }
        return klass.getName();
    }
    
    protected static String streamToString(final InputStream input) throws IOException {
        final InputStreamReader reader = new InputStreamReader(input, "UTF-8");
        final BufferedReader buffered = new BufferedReader(reader);
        try {
            final StringBuilder builder = new StringBuilder();
            String line;
            while ((line = buffered.readLine()) != null) {
                builder.append(line);
                builder.append("\n");
            }
            return builder.toString();
        }
        finally {
            buffered.close();
        }
    }
    
    protected static InputStream urlToStream(final URL url) throws IOException {
        final URLConnection conn = url.openConnection();
        conn.connect();
        return conn.getInputStream();
    }
    
    protected static class ZipJavadocProvider implements JavadocProvider
    {
        private final ZipFile zip;
        public static final String __PARANAMER_DATA = "<init> java.io.File file \ngetRawJavadoc java.lang.String fqn \n";
        
        public ZipJavadocProvider(final File file) throws IOException {
            this.zip = new ZipFile(file);
            this.find("package-list");
        }
        
        private ZipEntry find(final String postfix) throws FileNotFoundException {
            final Enumeration<? extends ZipEntry> entries = this.zip.entries();
            while (entries.hasMoreElements()) {
                final ZipEntry entry = (ZipEntry)entries.nextElement();
                final String name = entry.getName();
                if (name.endsWith(postfix)) {
                    return entry;
                }
            }
            throw new FileNotFoundException(postfix);
        }
        
        public InputStream getRawJavadoc(final String fqn) throws IOException {
            final ZipEntry entry = this.find(fqn);
            return this.zip.getInputStream(entry);
        }
    }
    
    protected static class UrlJavadocProvider implements JavadocProvider
    {
        private final URL base;
        public static final String __PARANAMER_DATA = "<init> java.net.URL base \ngetRawJavadoc java.lang.String fqn \n";
        
        public UrlJavadocProvider(final URL base) throws IOException {
            this.base = base;
            JavadocParanamer.streamToString(JavadocParanamer.urlToStream(new URL(base + "/package-list")));
        }
        
        public InputStream getRawJavadoc(final String fqn) throws IOException {
            return JavadocParanamer.urlToStream(new URL(this.base + "/" + fqn));
        }
    }
    
    protected static class DirJavadocProvider implements JavadocProvider
    {
        private final File dir;
        public static final String __PARANAMER_DATA = "<init> java.io.File dir \ngetRawJavadoc java.lang.String fqn \n";
        
        public DirJavadocProvider(final File dir) throws IOException {
            this.dir = dir;
            if (!new File(dir, "package-list").exists()) {
                throw new FileNotFoundException("package-list");
            }
        }
        
        public InputStream getRawJavadoc(final String fqn) throws IOException {
            final File file = new File(this.dir, fqn);
            return new FileInputStream(file);
        }
    }
    
    protected interface JavadocProvider
    {
        public static final String __PARANAMER_DATA = "getRawJavadoc java.lang.String canonicalClassName \n";
        
        InputStream getRawJavadoc(final String p0) throws IOException;
    }
}
