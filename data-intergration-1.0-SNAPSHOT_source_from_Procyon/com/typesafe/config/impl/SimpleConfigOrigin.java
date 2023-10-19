// 
// Decompiled by Procyon v0.5.36
// 

package com.typesafe.config.impl;

import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;
import java.util.Iterator;
import java.util.Collections;
import java.util.Collection;
import java.util.ArrayList;
import java.net.URL;
import java.net.MalformedURLException;
import java.io.File;
import com.typesafe.config.ConfigException;
import java.util.List;
import com.typesafe.config.ConfigOrigin;

final class SimpleConfigOrigin implements ConfigOrigin
{
    private final String description;
    private final int lineNumber;
    private final int endLineNumber;
    private final OriginType originType;
    private final String urlOrNull;
    private final String resourceOrNull;
    private final List<String> commentsOrNull;
    static final String MERGE_OF_PREFIX = "merge of ";
    
    protected SimpleConfigOrigin(final String description, final int lineNumber, final int endLineNumber, final OriginType originType, final String urlOrNull, final String resourceOrNull, final List<String> commentsOrNull) {
        if (description == null) {
            throw new ConfigException.BugOrBroken("description may not be null");
        }
        this.description = description;
        this.lineNumber = lineNumber;
        this.endLineNumber = endLineNumber;
        this.originType = originType;
        this.urlOrNull = urlOrNull;
        this.resourceOrNull = resourceOrNull;
        this.commentsOrNull = commentsOrNull;
    }
    
    static SimpleConfigOrigin newSimple(final String description) {
        return new SimpleConfigOrigin(description, -1, -1, OriginType.GENERIC, null, null, null);
    }
    
    static SimpleConfigOrigin newFile(final String filename) {
        String url;
        try {
            url = new File(filename).toURI().toURL().toExternalForm();
        }
        catch (MalformedURLException e) {
            url = null;
        }
        return new SimpleConfigOrigin(filename, -1, -1, OriginType.FILE, url, null, null);
    }
    
    static SimpleConfigOrigin newURL(final URL url) {
        final String u = url.toExternalForm();
        return new SimpleConfigOrigin(u, -1, -1, OriginType.URL, u, null, null);
    }
    
    static SimpleConfigOrigin newResource(final String resource, final URL url) {
        String desc;
        if (url != null) {
            desc = resource + " @ " + url.toExternalForm();
        }
        else {
            desc = resource;
        }
        return new SimpleConfigOrigin(desc, -1, -1, OriginType.RESOURCE, (url != null) ? url.toExternalForm() : null, resource, null);
    }
    
    static SimpleConfigOrigin newResource(final String resource) {
        return newResource(resource, null);
    }
    
    @Override
    public SimpleConfigOrigin withLineNumber(final int lineNumber) {
        if (lineNumber == this.lineNumber && lineNumber == this.endLineNumber) {
            return this;
        }
        return new SimpleConfigOrigin(this.description, lineNumber, lineNumber, this.originType, this.urlOrNull, this.resourceOrNull, this.commentsOrNull);
    }
    
    SimpleConfigOrigin addURL(final URL url) {
        return new SimpleConfigOrigin(this.description, this.lineNumber, this.endLineNumber, this.originType, (url != null) ? url.toExternalForm() : null, this.resourceOrNull, this.commentsOrNull);
    }
    
    @Override
    public SimpleConfigOrigin withComments(final List<String> comments) {
        if (ConfigImplUtil.equalsHandlingNull(comments, this.commentsOrNull)) {
            return this;
        }
        return new SimpleConfigOrigin(this.description, this.lineNumber, this.endLineNumber, this.originType, this.urlOrNull, this.resourceOrNull, comments);
    }
    
    SimpleConfigOrigin prependComments(final List<String> comments) {
        if (ConfigImplUtil.equalsHandlingNull(comments, this.commentsOrNull) || comments == null) {
            return this;
        }
        if (this.commentsOrNull == null) {
            return this.withComments(comments);
        }
        final List<String> merged = new ArrayList<String>(comments.size() + this.commentsOrNull.size());
        merged.addAll(comments);
        merged.addAll(this.commentsOrNull);
        return this.withComments(merged);
    }
    
    SimpleConfigOrigin appendComments(final List<String> comments) {
        if (ConfigImplUtil.equalsHandlingNull(comments, this.commentsOrNull) || comments == null) {
            return this;
        }
        if (this.commentsOrNull == null) {
            return this.withComments(comments);
        }
        final List<String> merged = new ArrayList<String>(comments.size() + this.commentsOrNull.size());
        merged.addAll(this.commentsOrNull);
        merged.addAll(comments);
        return this.withComments(merged);
    }
    
    @Override
    public String description() {
        if (this.lineNumber < 0) {
            return this.description;
        }
        if (this.endLineNumber == this.lineNumber) {
            return this.description + ": " + this.lineNumber;
        }
        return this.description + ": " + this.lineNumber + "-" + this.endLineNumber;
    }
    
    @Override
    public boolean equals(final Object other) {
        if (other instanceof SimpleConfigOrigin) {
            final SimpleConfigOrigin otherOrigin = (SimpleConfigOrigin)other;
            return this.description.equals(otherOrigin.description) && this.lineNumber == otherOrigin.lineNumber && this.endLineNumber == otherOrigin.endLineNumber && this.originType == otherOrigin.originType && ConfigImplUtil.equalsHandlingNull(this.urlOrNull, otherOrigin.urlOrNull) && ConfigImplUtil.equalsHandlingNull(this.resourceOrNull, otherOrigin.resourceOrNull);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int h = 41 * (41 + this.description.hashCode());
        h = 41 * (h + this.lineNumber);
        h = 41 * (h + this.endLineNumber);
        h = 41 * (h + this.originType.hashCode());
        if (this.urlOrNull != null) {
            h = 41 * (h + this.urlOrNull.hashCode());
        }
        if (this.resourceOrNull != null) {
            h = 41 * (h + this.resourceOrNull.hashCode());
        }
        return h;
    }
    
    @Override
    public String toString() {
        return "ConfigOrigin(" + this.description + ")";
    }
    
    @Override
    public String filename() {
        if (this.originType == OriginType.FILE) {
            return this.description;
        }
        if (this.urlOrNull == null) {
            return null;
        }
        URL url;
        try {
            url = new URL(this.urlOrNull);
        }
        catch (MalformedURLException e) {
            return null;
        }
        if (url.getProtocol().equals("file")) {
            return url.getFile();
        }
        return null;
    }
    
    @Override
    public URL url() {
        if (this.urlOrNull == null) {
            return null;
        }
        try {
            return new URL(this.urlOrNull);
        }
        catch (MalformedURLException e) {
            return null;
        }
    }
    
    @Override
    public String resource() {
        return this.resourceOrNull;
    }
    
    @Override
    public int lineNumber() {
        return this.lineNumber;
    }
    
    @Override
    public List<String> comments() {
        if (this.commentsOrNull != null) {
            return Collections.unmodifiableList((List<? extends String>)this.commentsOrNull);
        }
        return Collections.emptyList();
    }
    
    private static SimpleConfigOrigin mergeTwo(final SimpleConfigOrigin a, final SimpleConfigOrigin b) {
        OriginType mergedType;
        if (a.originType == b.originType) {
            mergedType = a.originType;
        }
        else {
            mergedType = OriginType.GENERIC;
        }
        String aDesc = a.description;
        String bDesc = b.description;
        if (aDesc.startsWith("merge of ")) {
            aDesc = aDesc.substring("merge of ".length());
        }
        if (bDesc.startsWith("merge of ")) {
            bDesc = bDesc.substring("merge of ".length());
        }
        String mergedDesc;
        int mergedStartLine;
        int mergedEndLine;
        if (aDesc.equals(bDesc)) {
            mergedDesc = aDesc;
            if (a.lineNumber < 0) {
                mergedStartLine = b.lineNumber;
            }
            else if (b.lineNumber < 0) {
                mergedStartLine = a.lineNumber;
            }
            else {
                mergedStartLine = Math.min(a.lineNumber, b.lineNumber);
            }
            mergedEndLine = Math.max(a.endLineNumber, b.endLineNumber);
        }
        else {
            String aFull = a.description();
            String bFull = b.description();
            if (aFull.startsWith("merge of ")) {
                aFull = aFull.substring("merge of ".length());
            }
            if (bFull.startsWith("merge of ")) {
                bFull = bFull.substring("merge of ".length());
            }
            mergedDesc = "merge of " + aFull + "," + bFull;
            mergedStartLine = -1;
            mergedEndLine = -1;
        }
        String mergedURL;
        if (ConfigImplUtil.equalsHandlingNull(a.urlOrNull, b.urlOrNull)) {
            mergedURL = a.urlOrNull;
        }
        else {
            mergedURL = null;
        }
        String mergedResource;
        if (ConfigImplUtil.equalsHandlingNull(a.resourceOrNull, b.resourceOrNull)) {
            mergedResource = a.resourceOrNull;
        }
        else {
            mergedResource = null;
        }
        List<String> mergedComments;
        if (ConfigImplUtil.equalsHandlingNull(a.commentsOrNull, b.commentsOrNull)) {
            mergedComments = a.commentsOrNull;
        }
        else {
            mergedComments = new ArrayList<String>();
            if (a.commentsOrNull != null) {
                mergedComments.addAll(a.commentsOrNull);
            }
            if (b.commentsOrNull != null) {
                mergedComments.addAll(b.commentsOrNull);
            }
        }
        return new SimpleConfigOrigin(mergedDesc, mergedStartLine, mergedEndLine, mergedType, mergedURL, mergedResource, mergedComments);
    }
    
    private static int similarity(final SimpleConfigOrigin a, final SimpleConfigOrigin b) {
        int count = 0;
        if (a.originType == b.originType) {
            ++count;
        }
        if (a.description.equals(b.description)) {
            ++count;
            if (a.lineNumber == b.lineNumber) {
                ++count;
            }
            if (a.endLineNumber == b.endLineNumber) {
                ++count;
            }
            if (ConfigImplUtil.equalsHandlingNull(a.urlOrNull, b.urlOrNull)) {
                ++count;
            }
            if (ConfigImplUtil.equalsHandlingNull(a.resourceOrNull, b.resourceOrNull)) {
                ++count;
            }
        }
        return count;
    }
    
    private static SimpleConfigOrigin mergeThree(final SimpleConfigOrigin a, final SimpleConfigOrigin b, final SimpleConfigOrigin c) {
        if (similarity(a, b) >= similarity(b, c)) {
            return mergeTwo(mergeTwo(a, b), c);
        }
        return mergeTwo(a, mergeTwo(b, c));
    }
    
    static ConfigOrigin mergeOrigins(final ConfigOrigin a, final ConfigOrigin b) {
        return mergeTwo((SimpleConfigOrigin)a, (SimpleConfigOrigin)b);
    }
    
    static ConfigOrigin mergeOrigins(final List<? extends AbstractConfigValue> stack) {
        final List<ConfigOrigin> origins = new ArrayList<ConfigOrigin>(stack.size());
        for (final AbstractConfigValue v : stack) {
            origins.add(v.origin());
        }
        return mergeOrigins(origins);
    }
    
    static ConfigOrigin mergeOrigins(final Collection<? extends ConfigOrigin> stack) {
        if (stack.isEmpty()) {
            throw new ConfigException.BugOrBroken("can't merge empty list of origins");
        }
        if (stack.size() == 1) {
            return (ConfigOrigin)stack.iterator().next();
        }
        if (stack.size() == 2) {
            final Iterator<? extends ConfigOrigin> i = stack.iterator();
            return mergeTwo((SimpleConfigOrigin)i.next(), (SimpleConfigOrigin)i.next());
        }
        final List<SimpleConfigOrigin> remaining = new ArrayList<SimpleConfigOrigin>();
        for (final ConfigOrigin o : stack) {
            remaining.add((SimpleConfigOrigin)o);
        }
        while (remaining.size() > 2) {
            final SimpleConfigOrigin c = remaining.get(remaining.size() - 1);
            remaining.remove(remaining.size() - 1);
            final SimpleConfigOrigin b = remaining.get(remaining.size() - 1);
            remaining.remove(remaining.size() - 1);
            final SimpleConfigOrigin a = remaining.get(remaining.size() - 1);
            remaining.remove(remaining.size() - 1);
            final SimpleConfigOrigin merged = mergeThree(a, b, c);
            remaining.add(merged);
        }
        return mergeOrigins(remaining);
    }
    
    Map<SerializedConfigValue.SerializedField, Object> toFields() {
        final Map<SerializedConfigValue.SerializedField, Object> m = new EnumMap<SerializedConfigValue.SerializedField, Object>(SerializedConfigValue.SerializedField.class);
        m.put(SerializedConfigValue.SerializedField.ORIGIN_DESCRIPTION, this.description);
        if (this.lineNumber >= 0) {
            m.put(SerializedConfigValue.SerializedField.ORIGIN_LINE_NUMBER, this.lineNumber);
        }
        if (this.endLineNumber >= 0) {
            m.put(SerializedConfigValue.SerializedField.ORIGIN_END_LINE_NUMBER, this.endLineNumber);
        }
        m.put(SerializedConfigValue.SerializedField.ORIGIN_TYPE, this.originType.ordinal());
        if (this.urlOrNull != null) {
            m.put(SerializedConfigValue.SerializedField.ORIGIN_URL, this.urlOrNull);
        }
        if (this.resourceOrNull != null) {
            m.put(SerializedConfigValue.SerializedField.ORIGIN_RESOURCE, this.resourceOrNull);
        }
        if (this.commentsOrNull != null) {
            m.put(SerializedConfigValue.SerializedField.ORIGIN_COMMENTS, this.commentsOrNull);
        }
        return m;
    }
    
    Map<SerializedConfigValue.SerializedField, Object> toFieldsDelta(final SimpleConfigOrigin baseOrigin) {
        Map<SerializedConfigValue.SerializedField, Object> baseFields;
        if (baseOrigin != null) {
            baseFields = baseOrigin.toFields();
        }
        else {
            baseFields = Collections.emptyMap();
        }
        return fieldsDelta(baseFields, this.toFields());
    }
    
    static Map<SerializedConfigValue.SerializedField, Object> fieldsDelta(final Map<SerializedConfigValue.SerializedField, Object> base, final Map<SerializedConfigValue.SerializedField, Object> child) {
        final Map<SerializedConfigValue.SerializedField, Object> m = new EnumMap<SerializedConfigValue.SerializedField, Object>(child);
        for (final Map.Entry<SerializedConfigValue.SerializedField, Object> baseEntry : base.entrySet()) {
            final SerializedConfigValue.SerializedField f = baseEntry.getKey();
            if (m.containsKey(f) && ConfigImplUtil.equalsHandlingNull(baseEntry.getValue(), m.get(f))) {
                m.remove(f);
            }
            else {
                if (m.containsKey(f)) {
                    continue;
                }
                switch (f) {
                    case ORIGIN_DESCRIPTION: {
                        throw new ConfigException.BugOrBroken("origin missing description field? " + child);
                    }
                    case ORIGIN_LINE_NUMBER: {
                        m.put(SerializedConfigValue.SerializedField.ORIGIN_LINE_NUMBER, -1);
                        continue;
                    }
                    case ORIGIN_END_LINE_NUMBER: {
                        m.put(SerializedConfigValue.SerializedField.ORIGIN_END_LINE_NUMBER, -1);
                        continue;
                    }
                    case ORIGIN_TYPE: {
                        throw new ConfigException.BugOrBroken("should always be an ORIGIN_TYPE field");
                    }
                    case ORIGIN_URL: {
                        m.put(SerializedConfigValue.SerializedField.ORIGIN_NULL_URL, "");
                        continue;
                    }
                    case ORIGIN_RESOURCE: {
                        m.put(SerializedConfigValue.SerializedField.ORIGIN_NULL_RESOURCE, "");
                        continue;
                    }
                    case ORIGIN_COMMENTS: {
                        m.put(SerializedConfigValue.SerializedField.ORIGIN_NULL_COMMENTS, "");
                        continue;
                    }
                    case ORIGIN_NULL_URL:
                    case ORIGIN_NULL_RESOURCE:
                    case ORIGIN_NULL_COMMENTS: {
                        throw new ConfigException.BugOrBroken("computing delta, base object should not contain " + f + " " + base);
                    }
                    case END_MARKER:
                    case ROOT_VALUE:
                    case ROOT_WAS_CONFIG:
                    case UNKNOWN:
                    case VALUE_DATA:
                    case VALUE_ORIGIN: {
                        throw new ConfigException.BugOrBroken("should not appear here: " + f);
                    }
                }
            }
        }
        return m;
    }
    
    static SimpleConfigOrigin fromFields(final Map<SerializedConfigValue.SerializedField, Object> m) throws IOException {
        if (m.isEmpty()) {
            return null;
        }
        final String description = m.get(SerializedConfigValue.SerializedField.ORIGIN_DESCRIPTION);
        final Integer lineNumber = m.get(SerializedConfigValue.SerializedField.ORIGIN_LINE_NUMBER);
        final Integer endLineNumber = m.get(SerializedConfigValue.SerializedField.ORIGIN_END_LINE_NUMBER);
        final Number originTypeOrdinal = m.get(SerializedConfigValue.SerializedField.ORIGIN_TYPE);
        if (originTypeOrdinal == null) {
            throw new IOException("Missing ORIGIN_TYPE field");
        }
        final OriginType originType = OriginType.values()[originTypeOrdinal.byteValue()];
        final String urlOrNull = m.get(SerializedConfigValue.SerializedField.ORIGIN_URL);
        String resourceOrNull = m.get(SerializedConfigValue.SerializedField.ORIGIN_RESOURCE);
        final List<String> commentsOrNull = m.get(SerializedConfigValue.SerializedField.ORIGIN_COMMENTS);
        if (originType == OriginType.RESOURCE && resourceOrNull == null) {
            resourceOrNull = description;
        }
        return new SimpleConfigOrigin(description, (lineNumber != null) ? lineNumber : -1, (endLineNumber != null) ? endLineNumber : -1, originType, urlOrNull, resourceOrNull, commentsOrNull);
    }
    
    static Map<SerializedConfigValue.SerializedField, Object> applyFieldsDelta(final Map<SerializedConfigValue.SerializedField, Object> base, final Map<SerializedConfigValue.SerializedField, Object> delta) throws IOException {
        final Map<SerializedConfigValue.SerializedField, Object> m = new EnumMap<SerializedConfigValue.SerializedField, Object>(delta);
        for (final Map.Entry<SerializedConfigValue.SerializedField, Object> baseEntry : base.entrySet()) {
            final SerializedConfigValue.SerializedField f = baseEntry.getKey();
            if (delta.containsKey(f)) {
                continue;
            }
            switch (f) {
                case ORIGIN_DESCRIPTION: {
                    m.put(f, base.get(f));
                    continue;
                }
                case ORIGIN_URL: {
                    if (delta.containsKey(SerializedConfigValue.SerializedField.ORIGIN_NULL_URL)) {
                        m.remove(SerializedConfigValue.SerializedField.ORIGIN_NULL_URL);
                        continue;
                    }
                    m.put(f, base.get(f));
                    continue;
                }
                case ORIGIN_RESOURCE: {
                    if (delta.containsKey(SerializedConfigValue.SerializedField.ORIGIN_NULL_RESOURCE)) {
                        m.remove(SerializedConfigValue.SerializedField.ORIGIN_NULL_RESOURCE);
                        continue;
                    }
                    m.put(f, base.get(f));
                    continue;
                }
                case ORIGIN_COMMENTS: {
                    if (delta.containsKey(SerializedConfigValue.SerializedField.ORIGIN_NULL_COMMENTS)) {
                        m.remove(SerializedConfigValue.SerializedField.ORIGIN_NULL_COMMENTS);
                        continue;
                    }
                    m.put(f, base.get(f));
                    continue;
                }
                case ORIGIN_NULL_URL:
                case ORIGIN_NULL_RESOURCE:
                case ORIGIN_NULL_COMMENTS: {
                    throw new ConfigException.BugOrBroken("applying fields, base object should not contain " + f + " " + base);
                }
                case ORIGIN_LINE_NUMBER:
                case ORIGIN_END_LINE_NUMBER:
                case ORIGIN_TYPE: {
                    m.put(f, base.get(f));
                    continue;
                }
                case END_MARKER:
                case ROOT_VALUE:
                case ROOT_WAS_CONFIG:
                case UNKNOWN:
                case VALUE_DATA:
                case VALUE_ORIGIN: {
                    throw new ConfigException.BugOrBroken("should not appear here: " + f);
                }
            }
        }
        return m;
    }
    
    static SimpleConfigOrigin fromBase(final SimpleConfigOrigin baseOrigin, final Map<SerializedConfigValue.SerializedField, Object> delta) throws IOException {
        Map<SerializedConfigValue.SerializedField, Object> baseFields;
        if (baseOrigin != null) {
            baseFields = baseOrigin.toFields();
        }
        else {
            baseFields = Collections.emptyMap();
        }
        final Map<SerializedConfigValue.SerializedField, Object> fields = applyFieldsDelta(baseFields, delta);
        return fromFields(fields);
    }
}
