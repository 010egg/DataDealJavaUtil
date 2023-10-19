// 
// Decompiled by Procyon v0.5.36
// 

package org.eclipse.jetty.http;

import org.eclipse.jetty.util.ArrayTernaryTrie;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.LazyList;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.Map;
import org.eclipse.jetty.util.StringUtil;
import org.eclipse.jetty.util.QuotedStringTokenizer;
import java.util.NoSuchElementException;
import java.util.List;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;
import java.util.Collection;
import org.eclipse.jetty.util.Trie;
import java.util.ArrayList;
import java.util.regex.Pattern;
import org.eclipse.jetty.util.log.Logger;

public class HttpFields implements Iterable<HttpField>
{
    private static final Logger LOG;
    private static final Pattern __splitter;
    public static final String __separators = ", \t";
    private final ArrayList<HttpField> _fields;
    private static final Float __one;
    private static final Float __zero;
    private static final Trie<Float> __qualities;
    
    public HttpFields() {
        this._fields = new ArrayList<HttpField>(20);
    }
    
    public Collection<String> getFieldNamesCollection() {
        final Set<String> list = new HashSet<String>(this._fields.size());
        for (final HttpField f : this._fields) {
            if (f != null) {
                list.add(f.getName());
            }
        }
        return list;
    }
    
    public Enumeration<String> getFieldNames() {
        return Collections.enumeration(this.getFieldNamesCollection());
    }
    
    public int size() {
        return this._fields.size();
    }
    
    public HttpField getField(final int i) {
        return this._fields.get(i);
    }
    
    @Override
    public Iterator<HttpField> iterator() {
        return this._fields.iterator();
    }
    
    public HttpField getField(final HttpHeader header) {
        for (int i = 0; i < this._fields.size(); ++i) {
            final HttpField f = this._fields.get(i);
            if (f.getHeader() == header) {
                return f;
            }
        }
        return null;
    }
    
    public HttpField getField(final String name) {
        for (int i = 0; i < this._fields.size(); ++i) {
            final HttpField f = this._fields.get(i);
            if (f.getName().equalsIgnoreCase(name)) {
                return f;
            }
        }
        return null;
    }
    
    public boolean contains(final HttpHeader header, final String value) {
        for (int i = 0; i < this._fields.size(); ++i) {
            final HttpField f = this._fields.get(i);
            if (f.getHeader() == header && this.contains(f, value)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean contains(final String name, final String value) {
        for (int i = 0; i < this._fields.size(); ++i) {
            final HttpField f = this._fields.get(i);
            if (f.getName().equalsIgnoreCase(name) && this.contains(f, value)) {
                return true;
            }
        }
        return false;
    }
    
    private boolean contains(final HttpField field, final String value) {
        final String v = field.getValue();
        if (v == null) {
            return false;
        }
        if (value.equalsIgnoreCase(v)) {
            return true;
        }
        final String[] split = HttpFields.__splitter.split(v);
        for (int i = 0; split != null && i < split.length; ++i) {
            if (value.equals(split[i])) {
                return true;
            }
        }
        return false;
    }
    
    public boolean contains(final HttpHeader header) {
        for (int i = 0; i < this._fields.size(); ++i) {
            final HttpField f = this._fields.get(i);
            if (f.getHeader() == header) {
                return true;
            }
        }
        return false;
    }
    
    public boolean containsKey(final String name) {
        for (int i = 0; i < this._fields.size(); ++i) {
            final HttpField f = this._fields.get(i);
            if (f.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }
    
    public String getStringField(final HttpHeader header) {
        return this.getStringField(header.asString());
    }
    
    public String get(final HttpHeader header) {
        return this.getStringField(header.asString());
    }
    
    public String get(final String header) {
        return this.getStringField(header);
    }
    
    public String getStringField(final String name) {
        final HttpField field = this.getField(name);
        return (field == null) ? null : field.getValue();
    }
    
    public List<String> getValuesList(final String name) {
        final List<String> list = new ArrayList<String>();
        for (final HttpField f : this._fields) {
            if (f.getName().equalsIgnoreCase(name)) {
                list.add(f.getValue());
            }
        }
        return list;
    }
    
    public Enumeration<String> getValues(final String name) {
        for (int i = 0; i < this._fields.size(); ++i) {
            final HttpField f = this._fields.get(i);
            if (f.getName().equalsIgnoreCase(name) && f.getValue() != null) {
                final int first = i;
                return new Enumeration<String>() {
                    HttpField field = f;
                    int i = first + 1;
                    
                    @Override
                    public boolean hasMoreElements() {
                        if (this.field == null) {
                            while (this.i < HttpFields.this._fields.size()) {
                                this.field = HttpFields.this._fields.get(this.i++);
                                if (this.field.getName().equalsIgnoreCase(name) && this.field.getValue() != null) {
                                    return true;
                                }
                            }
                            this.field = null;
                            return false;
                        }
                        return true;
                    }
                    
                    @Override
                    public String nextElement() throws NoSuchElementException {
                        if (this.hasMoreElements()) {
                            final String value = this.field.getValue();
                            this.field = null;
                            return value;
                        }
                        throw new NoSuchElementException();
                    }
                };
            }
        }
        final List<String> empty = Collections.emptyList();
        return Collections.enumeration(empty);
    }
    
    public Enumeration<String> getValues(final String name, final String separators) {
        final Enumeration<String> e = this.getValues(name);
        if (e == null) {
            return null;
        }
        return new Enumeration<String>() {
            QuotedStringTokenizer tok = null;
            
            @Override
            public boolean hasMoreElements() {
                if (this.tok != null && this.tok.hasMoreElements()) {
                    return true;
                }
                while (e.hasMoreElements()) {
                    final String value = e.nextElement();
                    if (value != null) {
                        this.tok = new QuotedStringTokenizer(value, separators, false, false);
                        if (this.tok.hasMoreElements()) {
                            return true;
                        }
                        continue;
                    }
                }
                this.tok = null;
                return false;
            }
            
            @Override
            public String nextElement() throws NoSuchElementException {
                if (!this.hasMoreElements()) {
                    throw new NoSuchElementException();
                }
                String next = (String)this.tok.nextElement();
                if (next != null) {
                    next = next.trim();
                }
                return next;
            }
        };
    }
    
    public void put(final HttpField field) {
        boolean put = false;
        int i = this._fields.size();
        while (i-- > 0) {
            final HttpField f = this._fields.get(i);
            if (f.isSame(field)) {
                if (put) {
                    this._fields.remove(i);
                }
                else {
                    this._fields.set(i, field);
                    put = true;
                }
            }
        }
        if (!put) {
            this._fields.add(field);
        }
    }
    
    public void put(final String name, final String value) {
        if (value == null) {
            this.remove(name);
        }
        else {
            this.put(new HttpField(name, value));
        }
    }
    
    public void put(final HttpHeader header, final HttpHeaderValue value) {
        this.put(header, value.toString());
    }
    
    public void put(final HttpHeader header, final String value) {
        if (value == null) {
            this.remove(header);
        }
        else {
            this.put(new HttpField(header, value));
        }
    }
    
    public void put(final String name, final List<String> list) {
        this.remove(name);
        for (final String v : list) {
            if (v != null) {
                this.add(name, v);
            }
        }
    }
    
    public void add(final String name, final String value) throws IllegalArgumentException {
        if (value == null) {
            return;
        }
        final HttpField field = new HttpField(name, value);
        this._fields.add(field);
    }
    
    public void add(final HttpHeader header, final HttpHeaderValue value) throws IllegalArgumentException {
        this.add(header, value.toString());
    }
    
    public void add(final HttpHeader header, final String value) throws IllegalArgumentException {
        if (value == null) {
            throw new IllegalArgumentException("null value");
        }
        final HttpField field = new HttpField(header, value);
        this._fields.add(field);
    }
    
    public HttpField remove(final HttpHeader name) {
        int i = this._fields.size();
        while (i-- > 0) {
            final HttpField f = this._fields.get(i);
            if (f.getHeader() == name) {
                return this._fields.remove(i);
            }
        }
        return null;
    }
    
    public HttpField remove(final String name) {
        int i = this._fields.size();
        while (i-- > 0) {
            final HttpField f = this._fields.get(i);
            if (f.getName().equalsIgnoreCase(name)) {
                return this._fields.remove(i);
            }
        }
        return null;
    }
    
    public long getLongField(final String name) throws NumberFormatException {
        final HttpField field = this.getField(name);
        return (field == null) ? -1L : StringUtil.toLong(field.getValue());
    }
    
    public long getDateField(final String name) {
        final HttpField field = this.getField(name);
        if (field == null) {
            return -1L;
        }
        final String val = valueParameters(field.getValue(), null);
        if (val == null) {
            return -1L;
        }
        final long date = DateParser.parseDate(val);
        if (date == -1L) {
            throw new IllegalArgumentException("Cannot convert date: " + val);
        }
        return date;
    }
    
    public void putLongField(final HttpHeader name, final long value) {
        final String v = Long.toString(value);
        this.put(name, v);
    }
    
    public void putLongField(final String name, final long value) {
        final String v = Long.toString(value);
        this.put(name, v);
    }
    
    public void putDateField(final HttpHeader name, final long date) {
        final String d = DateGenerator.formatDate(date);
        this.put(name, d);
    }
    
    public void putDateField(final String name, final long date) {
        final String d = DateGenerator.formatDate(date);
        this.put(name, d);
    }
    
    public void addDateField(final String name, final long date) {
        final String d = DateGenerator.formatDate(date);
        this.add(name, d);
    }
    
    @Override
    public String toString() {
        try {
            final StringBuilder buffer = new StringBuilder();
            for (final HttpField field : this._fields) {
                if (field != null) {
                    String tmp = field.getName();
                    if (tmp != null) {
                        buffer.append(tmp);
                    }
                    buffer.append(": ");
                    tmp = field.getValue();
                    if (tmp != null) {
                        buffer.append(tmp);
                    }
                    buffer.append("\r\n");
                }
            }
            buffer.append("\r\n");
            return buffer.toString();
        }
        catch (Exception e) {
            HttpFields.LOG.warn(e);
            return e.toString();
        }
    }
    
    public void clear() {
        this._fields.clear();
    }
    
    public void add(final HttpField field) {
        this._fields.add(field);
    }
    
    public void add(final HttpFields fields) {
        if (fields == null) {
            return;
        }
        final Enumeration<String> e = fields.getFieldNames();
        while (e.hasMoreElements()) {
            final String name = e.nextElement();
            final Enumeration<String> values = fields.getValues(name);
            while (values.hasMoreElements()) {
                this.add(name, values.nextElement());
            }
        }
    }
    
    public static String valueParameters(final String value, final Map<String, String> parameters) {
        if (value == null) {
            return null;
        }
        final int i = value.indexOf(59);
        if (i < 0) {
            return value;
        }
        if (parameters == null) {
            return value.substring(0, i).trim();
        }
        final StringTokenizer tok1 = new QuotedStringTokenizer(value.substring(i), ";", false, true);
        while (tok1.hasMoreTokens()) {
            final String token = tok1.nextToken();
            final StringTokenizer tok2 = new QuotedStringTokenizer(token, "= ");
            if (tok2.hasMoreTokens()) {
                final String paramName = tok2.nextToken();
                String paramVal = null;
                if (tok2.hasMoreTokens()) {
                    paramVal = tok2.nextToken();
                }
                parameters.put(paramName, paramVal);
            }
        }
        return value.substring(0, i).trim();
    }
    
    public static Float getQuality(final String value) {
        if (value == null) {
            return HttpFields.__zero;
        }
        int qe = value.indexOf(";");
        if (qe++ < 0 || qe == value.length()) {
            return HttpFields.__one;
        }
        if (value.charAt(qe++) == 'q') {
            ++qe;
            final Float q = HttpFields.__qualities.get(value, qe, value.length() - qe);
            if (q != null) {
                return q;
            }
        }
        final Map<String, String> params = new HashMap<String, String>(4);
        valueParameters(value, params);
        String qs = params.get("q");
        if (qs == null) {
            qs = "*";
        }
        Float q2 = HttpFields.__qualities.get(qs);
        if (q2 == null) {
            try {
                q2 = new Float(qs);
            }
            catch (Exception e) {
                q2 = HttpFields.__one;
            }
        }
        return q2;
    }
    
    public static List<String> qualityList(final Enumeration<String> e) {
        if (e == null || !e.hasMoreElements()) {
            return Collections.emptyList();
        }
        Object list = null;
        Object qual = null;
        while (e.hasMoreElements()) {
            final String v = e.nextElement();
            final Float q = getQuality(v);
            if (q >= 0.001) {
                list = LazyList.add(list, v);
                qual = LazyList.add(qual, q);
            }
        }
        final List<String> vl = LazyList.getList(list, false);
        if (vl.size() < 2) {
            return vl;
        }
        final List<Float> ql = LazyList.getList(qual, false);
        Float last = HttpFields.__zero;
        int i = vl.size();
        while (i-- > 0) {
            final Float q2 = ql.get(i);
            if (last.compareTo(q2) > 0) {
                final String tmp = vl.get(i);
                vl.set(i, vl.get(i + 1));
                vl.set(i + 1, tmp);
                ql.set(i, ql.get(i + 1));
                ql.set(i + 1, q2);
                last = HttpFields.__zero;
                i = vl.size();
            }
            else {
                last = q2;
            }
        }
        ql.clear();
        return vl;
    }
    
    static {
        LOG = Log.getLogger(HttpFields.class);
        __splitter = Pattern.compile("\\s*,\\s*");
        __one = new Float("1.0");
        __zero = new Float("0.0");
        (__qualities = new ArrayTernaryTrie<Float>()).put("*", HttpFields.__one);
        HttpFields.__qualities.put("1.0", HttpFields.__one);
        HttpFields.__qualities.put("1", HttpFields.__one);
        HttpFields.__qualities.put("0.9", new Float("0.9"));
        HttpFields.__qualities.put("0.8", new Float("0.8"));
        HttpFields.__qualities.put("0.7", new Float("0.7"));
        HttpFields.__qualities.put("0.66", new Float("0.66"));
        HttpFields.__qualities.put("0.6", new Float("0.6"));
        HttpFields.__qualities.put("0.5", new Float("0.5"));
        HttpFields.__qualities.put("0.4", new Float("0.4"));
        HttpFields.__qualities.put("0.33", new Float("0.33"));
        HttpFields.__qualities.put("0.3", new Float("0.3"));
        HttpFields.__qualities.put("0.2", new Float("0.2"));
        HttpFields.__qualities.put("0.1", new Float("0.1"));
        HttpFields.__qualities.put("0", HttpFields.__zero);
        HttpFields.__qualities.put("0.0", HttpFields.__zero);
    }
}
