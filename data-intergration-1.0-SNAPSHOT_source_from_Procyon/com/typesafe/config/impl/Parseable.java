// 
// Decompiled by Procyon v0.5.36
// 

package com.typesafe.config.impl;

import java.util.Enumeration;
import com.typesafe.config.ConfigMergeable;
import java.io.FileInputStream;
import java.net.URLConnection;
import java.io.StringReader;
import java.io.FileNotFoundException;
import java.util.Properties;
import java.net.URISyntaxException;
import java.net.MalformedURLException;
import java.net.URI;
import java.io.File;
import java.net.URL;
import java.io.FilterReader;
import java.io.UnsupportedEncodingException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;
import com.typesafe.config.parser.ConfigDocument;
import com.typesafe.config.ConfigObject;
import com.typesafe.config.ConfigException;
import com.typesafe.config.ConfigValue;
import java.io.IOException;
import java.io.Reader;
import com.typesafe.config.ConfigIncluder;
import com.typesafe.config.ConfigSyntax;
import java.util.LinkedList;
import com.typesafe.config.ConfigOrigin;
import com.typesafe.config.ConfigParseOptions;
import com.typesafe.config.ConfigIncludeContext;
import com.typesafe.config.ConfigParseable;

public abstract class Parseable implements ConfigParseable
{
    private ConfigIncludeContext includeContext;
    private ConfigParseOptions initialOptions;
    private ConfigOrigin initialOrigin;
    private static final ThreadLocal<LinkedList<Parseable>> parseStack;
    private static final int MAX_INCLUDE_DEPTH = 50;
    private static final String jsonContentType = "application/json";
    private static final String propertiesContentType = "text/x-java-properties";
    private static final String hoconContentType = "application/hocon";
    
    protected Parseable() {
    }
    
    private ConfigParseOptions fixupOptions(final ConfigParseOptions baseOptions) {
        ConfigSyntax syntax = baseOptions.getSyntax();
        if (syntax == null) {
            syntax = this.guessSyntax();
        }
        if (syntax == null) {
            syntax = ConfigSyntax.CONF;
        }
        ConfigParseOptions modified = baseOptions.setSyntax(syntax);
        modified = modified.appendIncluder(ConfigImpl.defaultIncluder());
        modified = modified.setIncluder(SimpleIncluder.makeFull(modified.getIncluder()));
        return modified;
    }
    
    protected void postConstruct(final ConfigParseOptions baseOptions) {
        this.initialOptions = this.fixupOptions(baseOptions);
        this.includeContext = new SimpleIncludeContext(this);
        if (this.initialOptions.getOriginDescription() != null) {
            this.initialOrigin = SimpleConfigOrigin.newSimple(this.initialOptions.getOriginDescription());
        }
        else {
            this.initialOrigin = this.createOrigin();
        }
    }
    
    protected abstract Reader reader() throws IOException;
    
    protected Reader reader(final ConfigParseOptions options) throws IOException {
        return this.reader();
    }
    
    protected static void trace(final String message) {
        if (ConfigImpl.traceLoadsEnabled()) {
            ConfigImpl.trace(message);
        }
    }
    
    ConfigSyntax guessSyntax() {
        return null;
    }
    
    ConfigSyntax contentType() {
        return null;
    }
    
    ConfigParseable relativeTo(final String filename) {
        String resource = filename;
        if (filename.startsWith("/")) {
            resource = filename.substring(1);
        }
        return newResources(resource, this.options().setOriginDescription(null));
    }
    
    ConfigIncludeContext includeContext() {
        return this.includeContext;
    }
    
    static AbstractConfigObject forceParsedToObject(final ConfigValue value) {
        if (value instanceof AbstractConfigObject) {
            return (AbstractConfigObject)value;
        }
        throw new ConfigException.WrongType(value.origin(), "", "object at file root", value.valueType().name());
    }
    
    @Override
    public ConfigObject parse(final ConfigParseOptions baseOptions) {
        final LinkedList<Parseable> stack = Parseable.parseStack.get();
        if (stack.size() >= 50) {
            throw new ConfigException.Parse(this.initialOrigin, "include statements nested more than 50 times, you probably have a cycle in your includes. Trace: " + stack);
        }
        stack.addFirst(this);
        try {
            return forceParsedToObject(this.parseValue(baseOptions));
        }
        finally {
            stack.removeFirst();
            if (stack.isEmpty()) {
                Parseable.parseStack.remove();
            }
        }
    }
    
    final AbstractConfigValue parseValue(final ConfigParseOptions baseOptions) {
        final ConfigParseOptions options = this.fixupOptions(baseOptions);
        ConfigOrigin origin;
        if (options.getOriginDescription() != null) {
            origin = SimpleConfigOrigin.newSimple(options.getOriginDescription());
        }
        else {
            origin = this.initialOrigin;
        }
        return this.parseValue(origin, options);
    }
    
    private final AbstractConfigValue parseValue(final ConfigOrigin origin, final ConfigParseOptions finalOptions) {
        try {
            return this.rawParseValue(origin, finalOptions);
        }
        catch (IOException e) {
            if (finalOptions.getAllowMissing()) {
                return SimpleConfigObject.emptyMissing(origin);
            }
            trace("exception loading " + origin.description() + ": " + e.getClass().getName() + ": " + e.getMessage());
            throw new ConfigException.IO(origin, e.getClass().getName() + ": " + e.getMessage(), e);
        }
    }
    
    final ConfigDocument parseDocument(final ConfigParseOptions baseOptions) {
        final ConfigParseOptions options = this.fixupOptions(baseOptions);
        ConfigOrigin origin;
        if (options.getOriginDescription() != null) {
            origin = SimpleConfigOrigin.newSimple(options.getOriginDescription());
        }
        else {
            origin = this.initialOrigin;
        }
        return this.parseDocument(origin, options);
    }
    
    private final ConfigDocument parseDocument(final ConfigOrigin origin, final ConfigParseOptions finalOptions) {
        try {
            return this.rawParseDocument(origin, finalOptions);
        }
        catch (IOException e) {
            if (finalOptions.getAllowMissing()) {
                final ArrayList<AbstractConfigNode> children = new ArrayList<AbstractConfigNode>();
                children.add(new ConfigNodeObject(new ArrayList<AbstractConfigNode>()));
                return new SimpleConfigDocument(new ConfigNodeRoot(children, origin), finalOptions);
            }
            trace("exception loading " + origin.description() + ": " + e.getClass().getName() + ": " + e.getMessage());
            throw new ConfigException.IO(origin, e.getClass().getName() + ": " + e.getMessage(), e);
        }
    }
    
    protected AbstractConfigValue rawParseValue(final ConfigOrigin origin, final ConfigParseOptions finalOptions) throws IOException {
        final Reader reader = this.reader(finalOptions);
        final ConfigSyntax contentType = this.contentType();
        ConfigParseOptions optionsWithContentType;
        if (contentType != null) {
            if (ConfigImpl.traceLoadsEnabled() && finalOptions.getSyntax() != null) {
                trace("Overriding syntax " + finalOptions.getSyntax() + " with Content-Type which specified " + contentType);
            }
            optionsWithContentType = finalOptions.setSyntax(contentType);
        }
        else {
            optionsWithContentType = finalOptions;
        }
        try {
            return this.rawParseValue(reader, origin, optionsWithContentType);
        }
        finally {
            reader.close();
        }
    }
    
    private AbstractConfigValue rawParseValue(final Reader reader, final ConfigOrigin origin, final ConfigParseOptions finalOptions) throws IOException {
        if (finalOptions.getSyntax() == ConfigSyntax.PROPERTIES) {
            return PropertiesParser.parse(reader, origin);
        }
        final Iterator<Token> tokens = Tokenizer.tokenize(origin, reader, finalOptions.getSyntax());
        final ConfigNodeRoot document = ConfigDocumentParser.parse(tokens, origin, finalOptions);
        return ConfigParser.parse(document, origin, finalOptions, this.includeContext());
    }
    
    protected ConfigDocument rawParseDocument(final ConfigOrigin origin, final ConfigParseOptions finalOptions) throws IOException {
        final Reader reader = this.reader(finalOptions);
        final ConfigSyntax contentType = this.contentType();
        ConfigParseOptions optionsWithContentType;
        if (contentType != null) {
            if (ConfigImpl.traceLoadsEnabled() && finalOptions.getSyntax() != null) {
                trace("Overriding syntax " + finalOptions.getSyntax() + " with Content-Type which specified " + contentType);
            }
            optionsWithContentType = finalOptions.setSyntax(contentType);
        }
        else {
            optionsWithContentType = finalOptions;
        }
        try {
            return this.rawParseDocument(reader, origin, optionsWithContentType);
        }
        finally {
            reader.close();
        }
    }
    
    private ConfigDocument rawParseDocument(final Reader reader, final ConfigOrigin origin, final ConfigParseOptions finalOptions) throws IOException {
        final Iterator<Token> tokens = Tokenizer.tokenize(origin, reader, finalOptions.getSyntax());
        return new SimpleConfigDocument(ConfigDocumentParser.parse(tokens, origin, finalOptions), finalOptions);
    }
    
    public ConfigObject parse() {
        return forceParsedToObject(this.parseValue(this.options()));
    }
    
    public ConfigDocument parseConfigDocument() {
        return this.parseDocument(this.options());
    }
    
    AbstractConfigValue parseValue() {
        return this.parseValue(this.options());
    }
    
    @Override
    public final ConfigOrigin origin() {
        return this.initialOrigin;
    }
    
    protected abstract ConfigOrigin createOrigin();
    
    @Override
    public ConfigParseOptions options() {
        return this.initialOptions;
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
    
    private static ConfigSyntax syntaxFromExtension(final String name) {
        if (name.endsWith(".json")) {
            return ConfigSyntax.JSON;
        }
        if (name.endsWith(".conf")) {
            return ConfigSyntax.CONF;
        }
        if (name.endsWith(".properties")) {
            return ConfigSyntax.PROPERTIES;
        }
        return null;
    }
    
    private static Reader readerFromStream(final InputStream input) {
        return readerFromStream(input, "UTF-8");
    }
    
    private static Reader readerFromStream(final InputStream input, final String encoding) {
        try {
            final Reader reader = new InputStreamReader(input, encoding);
            return new BufferedReader(reader);
        }
        catch (UnsupportedEncodingException e) {
            throw new ConfigException.BugOrBroken("Java runtime does not support UTF-8", e);
        }
    }
    
    private static Reader doNotClose(final Reader input) {
        return new FilterReader(input) {
            @Override
            public void close() {
            }
        };
    }
    
    static URL relativeTo(final URL url, final String filename) {
        if (new File(filename).isAbsolute()) {
            return null;
        }
        try {
            final URI siblingURI = url.toURI();
            final URI relative = new URI(filename);
            final URL resolved = siblingURI.resolve(relative).toURL();
            return resolved;
        }
        catch (MalformedURLException e) {
            return null;
        }
        catch (URISyntaxException e2) {
            return null;
        }
        catch (IllegalArgumentException e3) {
            return null;
        }
    }
    
    static File relativeTo(final File file, final String filename) {
        final File child = new File(filename);
        if (child.isAbsolute()) {
            return null;
        }
        final File parent = file.getParentFile();
        if (parent == null) {
            return null;
        }
        return new File(parent, filename);
    }
    
    public static Parseable newNotFound(final String whatNotFound, final String message, final ConfigParseOptions options) {
        return new ParseableNotFound(whatNotFound, message, options);
    }
    
    public static Parseable newReader(final Reader reader, final ConfigParseOptions options) {
        return new ParseableReader(doNotClose(reader), options);
    }
    
    public static Parseable newString(final String input, final ConfigParseOptions options) {
        return new ParseableString(input, options);
    }
    
    public static Parseable newURL(final URL input, final ConfigParseOptions options) {
        if (input.getProtocol().equals("file")) {
            return newFile(ConfigImplUtil.urlToFile(input), options);
        }
        return new ParseableURL(input, options);
    }
    
    public static Parseable newFile(final File input, final ConfigParseOptions options) {
        return new ParseableFile(input, options);
    }
    
    private static Parseable newResourceURL(final URL input, final ConfigParseOptions options, final String resource, final Relativizer relativizer) {
        return new ParseableResourceURL(input, options, resource, relativizer);
    }
    
    public static Parseable newResources(final Class<?> klass, final String resource, final ConfigParseOptions options) {
        return newResources(convertResourceName(klass, resource), options.setClassLoader(klass.getClassLoader()));
    }
    
    private static String convertResourceName(final Class<?> klass, final String resource) {
        if (resource.startsWith("/")) {
            return resource.substring(1);
        }
        final String className = klass.getName();
        final int i = className.lastIndexOf(46);
        if (i < 0) {
            return resource;
        }
        final String packageName = className.substring(0, i);
        final String packagePath = packageName.replace('.', '/');
        return packagePath + "/" + resource;
    }
    
    public static Parseable newResources(final String resource, final ConfigParseOptions options) {
        if (options.getClassLoader() == null) {
            throw new ConfigException.BugOrBroken("null class loader; pass in a class loader or use Thread.currentThread().setContextClassLoader()");
        }
        return new ParseableResources(resource, options);
    }
    
    public static Parseable newProperties(final Properties properties, final ConfigParseOptions options) {
        return new ParseableProperties(properties, options);
    }
    
    static {
        parseStack = new ThreadLocal<LinkedList<Parseable>>() {
            @Override
            protected LinkedList<Parseable> initialValue() {
                return new LinkedList<Parseable>();
            }
        };
    }
    
    private static final class ParseableNotFound extends Parseable
    {
        private final String what;
        private final String message;
        
        ParseableNotFound(final String what, final String message, final ConfigParseOptions options) {
            this.what = what;
            this.message = message;
            this.postConstruct(options);
        }
        
        @Override
        protected Reader reader() throws IOException {
            throw new FileNotFoundException(this.message);
        }
        
        @Override
        protected ConfigOrigin createOrigin() {
            return SimpleConfigOrigin.newSimple(this.what);
        }
    }
    
    private static final class ParseableReader extends Parseable
    {
        private final Reader reader;
        
        ParseableReader(final Reader reader, final ConfigParseOptions options) {
            this.reader = reader;
            this.postConstruct(options);
        }
        
        @Override
        protected Reader reader() {
            if (ConfigImpl.traceLoadsEnabled()) {
                Parseable.trace("Loading config from reader " + this.reader);
            }
            return this.reader;
        }
        
        @Override
        protected ConfigOrigin createOrigin() {
            return SimpleConfigOrigin.newSimple("Reader");
        }
    }
    
    private static final class ParseableString extends Parseable
    {
        private final String input;
        
        ParseableString(final String input, final ConfigParseOptions options) {
            this.input = input;
            this.postConstruct(options);
        }
        
        @Override
        protected Reader reader() {
            if (ConfigImpl.traceLoadsEnabled()) {
                Parseable.trace("Loading config from a String " + this.input);
            }
            return new StringReader(this.input);
        }
        
        @Override
        protected ConfigOrigin createOrigin() {
            return SimpleConfigOrigin.newSimple("String");
        }
        
        @Override
        public String toString() {
            return this.getClass().getSimpleName() + "(" + this.input + ")";
        }
    }
    
    private static class ParseableURL extends Parseable
    {
        protected final URL input;
        private String contentType;
        
        protected ParseableURL(final URL input) {
            this.contentType = null;
            this.input = input;
        }
        
        ParseableURL(final URL input, final ConfigParseOptions options) {
            this(input);
            this.postConstruct(options);
        }
        
        @Override
        protected Reader reader() throws IOException {
            throw new ConfigException.BugOrBroken("reader() without options should not be called on ParseableURL");
        }
        
        private static String acceptContentType(final ConfigParseOptions options) {
            if (options.getSyntax() == null) {
                return null;
            }
            switch (options.getSyntax()) {
                case JSON: {
                    return "application/json";
                }
                case CONF: {
                    return "application/hocon";
                }
                case PROPERTIES: {
                    return "text/x-java-properties";
                }
                default: {
                    return null;
                }
            }
        }
        
        @Override
        protected Reader reader(final ConfigParseOptions options) throws IOException {
            try {
                if (ConfigImpl.traceLoadsEnabled()) {
                    Parseable.trace("Loading config from a URL: " + this.input.toExternalForm());
                }
                final URLConnection connection = this.input.openConnection();
                final String acceptContent = acceptContentType(options);
                if (acceptContent != null) {
                    connection.setRequestProperty("Accept", acceptContent);
                }
                connection.connect();
                this.contentType = connection.getContentType();
                if (this.contentType != null) {
                    if (ConfigImpl.traceLoadsEnabled()) {
                        Parseable.trace("URL sets Content-Type: '" + this.contentType + "'");
                    }
                    this.contentType = this.contentType.trim();
                    final int semi = this.contentType.indexOf(59);
                    if (semi >= 0) {
                        this.contentType = this.contentType.substring(0, semi);
                    }
                }
                final InputStream stream = connection.getInputStream();
                return readerFromStream(stream);
            }
            catch (FileNotFoundException fnf) {
                throw fnf;
            }
            catch (IOException e) {
                throw new ConfigException.BugOrBroken("Cannot load config from URL: " + this.input.toExternalForm(), e);
            }
        }
        
        @Override
        ConfigSyntax guessSyntax() {
            return syntaxFromExtension(this.input.getPath());
        }
        
        @Override
        ConfigSyntax contentType() {
            if (this.contentType == null) {
                return null;
            }
            if (this.contentType.equals("application/json")) {
                return ConfigSyntax.JSON;
            }
            if (this.contentType.equals("text/x-java-properties")) {
                return ConfigSyntax.PROPERTIES;
            }
            if (this.contentType.equals("application/hocon")) {
                return ConfigSyntax.CONF;
            }
            if (ConfigImpl.traceLoadsEnabled()) {
                Parseable.trace("'" + this.contentType + "' isn't a known content type");
            }
            return null;
        }
        
        @Override
        ConfigParseable relativeTo(final String filename) {
            final URL url = Parseable.relativeTo(this.input, filename);
            if (url == null) {
                return null;
            }
            return Parseable.newURL(url, this.options().setOriginDescription(null));
        }
        
        @Override
        protected ConfigOrigin createOrigin() {
            return SimpleConfigOrigin.newURL(this.input);
        }
        
        @Override
        public String toString() {
            return this.getClass().getSimpleName() + "(" + this.input.toExternalForm() + ")";
        }
    }
    
    private static final class ParseableFile extends Parseable
    {
        private final File input;
        
        ParseableFile(final File input, final ConfigParseOptions options) {
            this.input = input;
            this.postConstruct(options);
        }
        
        @Override
        protected Reader reader() throws IOException {
            if (ConfigImpl.traceLoadsEnabled()) {
                Parseable.trace("Loading config from a file: " + this.input);
            }
            final InputStream stream = new FileInputStream(this.input);
            return readerFromStream(stream);
        }
        
        @Override
        ConfigSyntax guessSyntax() {
            return syntaxFromExtension(this.input.getName());
        }
        
        @Override
        ConfigParseable relativeTo(final String filename) {
            File sibling;
            if (new File(filename).isAbsolute()) {
                sibling = new File(filename);
            }
            else {
                sibling = Parseable.relativeTo(this.input, filename);
            }
            if (sibling == null) {
                return null;
            }
            if (sibling.exists()) {
                Parseable.trace(sibling + " exists, so loading it as a file");
                return Parseable.newFile(sibling, this.options().setOriginDescription(null));
            }
            Parseable.trace(sibling + " does not exist, so trying it as a classpath resource");
            return super.relativeTo(filename);
        }
        
        @Override
        protected ConfigOrigin createOrigin() {
            return SimpleConfigOrigin.newFile(this.input.getPath());
        }
        
        @Override
        public String toString() {
            return this.getClass().getSimpleName() + "(" + this.input.getPath() + ")";
        }
    }
    
    private static final class ParseableResourceURL extends ParseableURL
    {
        private final Relativizer relativizer;
        private final String resource;
        
        ParseableResourceURL(final URL input, final ConfigParseOptions options, final String resource, final Relativizer relativizer) {
            super(input);
            this.relativizer = relativizer;
            this.resource = resource;
            this.postConstruct(options);
        }
        
        @Override
        protected ConfigOrigin createOrigin() {
            return SimpleConfigOrigin.newResource(this.resource, this.input);
        }
        
        @Override
        ConfigParseable relativeTo(final String filename) {
            return this.relativizer.relativeTo(filename);
        }
    }
    
    private static final class ParseableResources extends Parseable implements Relativizer
    {
        private final String resource;
        
        ParseableResources(final String resource, final ConfigParseOptions options) {
            this.resource = resource;
            this.postConstruct(options);
        }
        
        @Override
        protected Reader reader() throws IOException {
            throw new ConfigException.BugOrBroken("reader() should not be called on resources");
        }
        
        @Override
        protected AbstractConfigObject rawParseValue(final ConfigOrigin origin, final ConfigParseOptions finalOptions) throws IOException {
            final ClassLoader loader = finalOptions.getClassLoader();
            if (loader == null) {
                throw new ConfigException.BugOrBroken("null class loader; pass in a class loader or use Thread.currentThread().setContextClassLoader()");
            }
            final Enumeration<URL> e = loader.getResources(this.resource);
            if (!e.hasMoreElements()) {
                if (ConfigImpl.traceLoadsEnabled()) {
                    Parseable.trace("Loading config from class loader " + loader + " but there were no resources called " + this.resource);
                }
                throw new IOException("resource not found on classpath: " + this.resource);
            }
            AbstractConfigObject merged = SimpleConfigObject.empty(origin);
            while (e.hasMoreElements()) {
                final URL url = e.nextElement();
                if (ConfigImpl.traceLoadsEnabled()) {
                    Parseable.trace("Loading config from resource '" + this.resource + "' URL " + url.toExternalForm() + " from class loader " + loader);
                }
                final Parseable element = newResourceURL(url, finalOptions, this.resource, this);
                final AbstractConfigValue v = element.parseValue();
                merged = merged.withFallback(v);
            }
            return merged;
        }
        
        @Override
        ConfigSyntax guessSyntax() {
            return syntaxFromExtension(this.resource);
        }
        
        static String parent(final String resource) {
            final int i = resource.lastIndexOf(47);
            if (i < 0) {
                return null;
            }
            return resource.substring(0, i);
        }
        
        @Override
        public ConfigParseable relativeTo(final String sibling) {
            if (sibling.startsWith("/")) {
                return Parseable.newResources(sibling.substring(1), this.options().setOriginDescription(null));
            }
            final String parent = parent(this.resource);
            if (parent == null) {
                return Parseable.newResources(sibling, this.options().setOriginDescription(null));
            }
            return Parseable.newResources(parent + "/" + sibling, this.options().setOriginDescription(null));
        }
        
        @Override
        protected ConfigOrigin createOrigin() {
            return SimpleConfigOrigin.newResource(this.resource);
        }
        
        @Override
        public String toString() {
            return this.getClass().getSimpleName() + "(" + this.resource + ")";
        }
    }
    
    private static final class ParseableProperties extends Parseable
    {
        private final Properties props;
        
        ParseableProperties(final Properties props, final ConfigParseOptions options) {
            this.props = props;
            this.postConstruct(options);
        }
        
        @Override
        protected Reader reader() throws IOException {
            throw new ConfigException.BugOrBroken("reader() should not be called on props");
        }
        
        @Override
        protected AbstractConfigObject rawParseValue(final ConfigOrigin origin, final ConfigParseOptions finalOptions) {
            if (ConfigImpl.traceLoadsEnabled()) {
                Parseable.trace("Loading config from properties " + this.props);
            }
            return PropertiesParser.fromProperties(origin, this.props);
        }
        
        @Override
        ConfigSyntax guessSyntax() {
            return ConfigSyntax.PROPERTIES;
        }
        
        @Override
        protected ConfigOrigin createOrigin() {
            return SimpleConfigOrigin.newSimple("properties");
        }
        
        @Override
        public String toString() {
            return this.getClass().getSimpleName() + "(" + this.props.size() + " props)";
        }
    }
    
    protected interface Relativizer
    {
        ConfigParseable relativeTo(final String p0);
    }
}
