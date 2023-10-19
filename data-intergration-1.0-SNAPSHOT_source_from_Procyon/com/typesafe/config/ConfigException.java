// 
// Decompiled by Procyon v0.5.36
// 

package com.typesafe.config;

import java.util.Iterator;
import java.lang.reflect.Field;
import java.io.ObjectInputStream;
import java.io.IOException;
import com.typesafe.config.impl.ConfigImplUtil;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public abstract class ConfigException extends RuntimeException implements Serializable
{
    private static final long serialVersionUID = 1L;
    private final transient ConfigOrigin origin;
    
    protected ConfigException(final ConfigOrigin origin, final String message, final Throwable cause) {
        super(origin.description() + ": " + message, cause);
        this.origin = origin;
    }
    
    protected ConfigException(final ConfigOrigin origin, final String message) {
        this(origin.description() + ": " + message, null);
    }
    
    protected ConfigException(final String message, final Throwable cause) {
        super(message, cause);
        this.origin = null;
    }
    
    protected ConfigException(final String message) {
        this(message, null);
    }
    
    public ConfigOrigin origin() {
        return this.origin;
    }
    
    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        ConfigImplUtil.writeOrigin(out, this.origin);
    }
    
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        final ConfigOrigin origin = ConfigImplUtil.readOrigin(in);
        Field f;
        try {
            f = ConfigException.class.getDeclaredField("origin");
        }
        catch (NoSuchFieldException e) {
            throw new IOException("ConfigException has no origin field?", e);
        }
        catch (SecurityException e2) {
            throw new IOException("unable to fill out origin field in ConfigException", e2);
        }
        f.setAccessible(true);
        try {
            f.set(this, origin);
        }
        catch (IllegalArgumentException e3) {
            throw new IOException("unable to set origin field", e3);
        }
        catch (IllegalAccessException e4) {
            throw new IOException("unable to set origin field", e4);
        }
    }
    
    public static class WrongType extends ConfigException
    {
        private static final long serialVersionUID = 1L;
        
        public WrongType(final ConfigOrigin origin, final String path, final String expected, final String actual, final Throwable cause) {
            super(origin, path + " has type " + actual + " rather than " + expected, cause);
        }
        
        public WrongType(final ConfigOrigin origin, final String path, final String expected, final String actual) {
            this(origin, path, expected, actual, null);
        }
        
        public WrongType(final ConfigOrigin origin, final String message, final Throwable cause) {
            super(origin, message, cause);
        }
        
        public WrongType(final ConfigOrigin origin, final String message) {
            super(origin, message, null);
        }
    }
    
    public static class Missing extends ConfigException
    {
        private static final long serialVersionUID = 1L;
        
        public Missing(final String path, final Throwable cause) {
            super("No configuration setting found for key '" + path + "'", cause);
        }
        
        public Missing(final String path) {
            this(path, null);
        }
        
        protected Missing(final ConfigOrigin origin, final String message, final Throwable cause) {
            super(origin, message, cause);
        }
        
        protected Missing(final ConfigOrigin origin, final String message) {
            this(origin, message, null);
        }
    }
    
    public static class Null extends Missing
    {
        private static final long serialVersionUID = 1L;
        
        private static String makeMessage(final String path, final String expected) {
            if (expected != null) {
                return "Configuration key '" + path + "' is set to null but expected " + expected;
            }
            return "Configuration key '" + path + "' is null";
        }
        
        public Null(final ConfigOrigin origin, final String path, final String expected, final Throwable cause) {
            super(origin, makeMessage(path, expected), cause);
        }
        
        public Null(final ConfigOrigin origin, final String path, final String expected) {
            this(origin, path, expected, null);
        }
    }
    
    public static class BadValue extends ConfigException
    {
        private static final long serialVersionUID = 1L;
        
        public BadValue(final ConfigOrigin origin, final String path, final String message, final Throwable cause) {
            super(origin, "Invalid value at '" + path + "': " + message, cause);
        }
        
        public BadValue(final ConfigOrigin origin, final String path, final String message) {
            this(origin, path, message, null);
        }
        
        public BadValue(final String path, final String message, final Throwable cause) {
            super("Invalid value at '" + path + "': " + message, cause);
        }
        
        public BadValue(final String path, final String message) {
            this(path, message, null);
        }
    }
    
    public static class BadPath extends ConfigException
    {
        private static final long serialVersionUID = 1L;
        
        public BadPath(final ConfigOrigin origin, final String path, final String message, final Throwable cause) {
            super(origin, (path != null) ? ("Invalid path '" + path + "': " + message) : message, cause);
        }
        
        public BadPath(final ConfigOrigin origin, final String path, final String message) {
            this(origin, path, message, null);
        }
        
        public BadPath(final String path, final String message, final Throwable cause) {
            super((path != null) ? ("Invalid path '" + path + "': " + message) : message, cause);
        }
        
        public BadPath(final String path, final String message) {
            this(path, message, null);
        }
        
        public BadPath(final ConfigOrigin origin, final String message) {
            this(origin, null, message);
        }
    }
    
    public static class BugOrBroken extends ConfigException
    {
        private static final long serialVersionUID = 1L;
        
        public BugOrBroken(final String message, final Throwable cause) {
            super(message, cause);
        }
        
        public BugOrBroken(final String message) {
            this(message, null);
        }
    }
    
    public static class IO extends ConfigException
    {
        private static final long serialVersionUID = 1L;
        
        public IO(final ConfigOrigin origin, final String message, final Throwable cause) {
            super(origin, message, cause);
        }
        
        public IO(final ConfigOrigin origin, final String message) {
            this(origin, message, null);
        }
    }
    
    public static class Parse extends ConfigException
    {
        private static final long serialVersionUID = 1L;
        
        public Parse(final ConfigOrigin origin, final String message, final Throwable cause) {
            super(origin, message, cause);
        }
        
        public Parse(final ConfigOrigin origin, final String message) {
            this(origin, message, null);
        }
    }
    
    public static class UnresolvedSubstitution extends Parse
    {
        private static final long serialVersionUID = 1L;
        
        public UnresolvedSubstitution(final ConfigOrigin origin, final String detail, final Throwable cause) {
            super(origin, "Could not resolve substitution to a value: " + detail, cause);
        }
        
        public UnresolvedSubstitution(final ConfigOrigin origin, final String detail) {
            this(origin, detail, null);
        }
    }
    
    public static class NotResolved extends BugOrBroken
    {
        private static final long serialVersionUID = 1L;
        
        public NotResolved(final String message, final Throwable cause) {
            super(message, cause);
        }
        
        public NotResolved(final String message) {
            this(message, null);
        }
    }
    
    public static class ValidationProblem
    {
        private final String path;
        private final ConfigOrigin origin;
        private final String problem;
        
        public ValidationProblem(final String path, final ConfigOrigin origin, final String problem) {
            this.path = path;
            this.origin = origin;
            this.problem = problem;
        }
        
        public String path() {
            return this.path;
        }
        
        public ConfigOrigin origin() {
            return this.origin;
        }
        
        public String problem() {
            return this.problem;
        }
        
        @Override
        public String toString() {
            return "ValidationProblem(" + this.path + "," + this.origin + "," + this.problem + ")";
        }
    }
    
    public static class ValidationFailed extends ConfigException
    {
        private static final long serialVersionUID = 1L;
        private final Iterable<ValidationProblem> problems;
        
        public ValidationFailed(final Iterable<ValidationProblem> problems) {
            super(makeMessage(problems), null);
            this.problems = problems;
        }
        
        public Iterable<ValidationProblem> problems() {
            return this.problems;
        }
        
        private static String makeMessage(final Iterable<ValidationProblem> problems) {
            final StringBuilder sb = new StringBuilder();
            for (final ValidationProblem p : problems) {
                sb.append(p.origin().description());
                sb.append(": ");
                sb.append(p.path());
                sb.append(": ");
                sb.append(p.problem());
                sb.append(", ");
            }
            if (sb.length() == 0) {
                throw new BugOrBroken("ValidationFailed must have a non-empty list of problems");
            }
            sb.setLength(sb.length() - 2);
            return sb.toString();
        }
    }
    
    public static class BadBean extends BugOrBroken
    {
        private static final long serialVersionUID = 1L;
        
        public BadBean(final String message, final Throwable cause) {
            super(message, cause);
        }
        
        public BadBean(final String message) {
            this(message, null);
        }
    }
    
    public static class Generic extends ConfigException
    {
        private static final long serialVersionUID = 1L;
        
        public Generic(final String message, final Throwable cause) {
            super(message, cause);
        }
        
        public Generic(final String message) {
            this(message, null);
        }
    }
}
