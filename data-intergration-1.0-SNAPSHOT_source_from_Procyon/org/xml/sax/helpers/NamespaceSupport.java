// 
// Decompiled by Procyon v0.5.36
// 

package org.xml.sax.helpers;

import java.util.Hashtable;
import java.util.Vector;
import java.util.EmptyStackException;
import java.util.Enumeration;

public class NamespaceSupport
{
    public static final String XMLNS = "http://www.w3.org/XML/1998/namespace";
    private static final Enumeration EMPTY_ENUMERATION;
    private Context[] contexts;
    private Context currentContext;
    private int contextPos;
    
    public NamespaceSupport() {
        this.reset();
    }
    
    public void reset() {
        this.contexts = new Context[32];
        this.contextPos = 0;
        this.contexts[this.contextPos] = (this.currentContext = new Context());
        this.currentContext.declarePrefix("xml", "http://www.w3.org/XML/1998/namespace");
    }
    
    public void pushContext() {
        final int length = this.contexts.length;
        this.contexts[this.contextPos].declsOK = false;
        ++this.contextPos;
        if (this.contextPos >= length) {
            final Context[] contexts = new Context[length * 2];
            System.arraycopy(this.contexts, 0, contexts, 0, length);
            this.contexts = contexts;
        }
        this.currentContext = this.contexts[this.contextPos];
        if (this.currentContext == null) {
            this.contexts[this.contextPos] = (this.currentContext = new Context());
        }
        if (this.contextPos > 0) {
            this.currentContext.setParent(this.contexts[this.contextPos - 1]);
        }
    }
    
    public void popContext() {
        this.contexts[this.contextPos].clear();
        --this.contextPos;
        if (this.contextPos < 0) {
            throw new EmptyStackException();
        }
        this.currentContext = this.contexts[this.contextPos];
    }
    
    public boolean declarePrefix(final String s, final String s2) {
        if (s.equals("xml") || s.equals("xmlns")) {
            return false;
        }
        this.currentContext.declarePrefix(s, s2);
        return true;
    }
    
    public String[] processName(final String s, final String[] array, final boolean b) {
        final String[] processName = this.currentContext.processName(s, b);
        if (processName == null) {
            return null;
        }
        array[0] = processName[0];
        array[1] = processName[1];
        array[2] = processName[2];
        return array;
    }
    
    public String getURI(final String s) {
        return this.currentContext.getURI(s);
    }
    
    public Enumeration getPrefixes() {
        return this.currentContext.getPrefixes();
    }
    
    public String getPrefix(final String s) {
        return this.currentContext.getPrefix(s);
    }
    
    public Enumeration getPrefixes(final String s) {
        final Vector<String> vector = new Vector<String>();
        final Enumeration prefixes = this.getPrefixes();
        while (prefixes.hasMoreElements()) {
            final String obj = prefixes.nextElement();
            if (s.equals(this.getURI(obj))) {
                vector.addElement(obj);
            }
        }
        return vector.elements();
    }
    
    public Enumeration getDeclaredPrefixes() {
        return this.currentContext.getDeclaredPrefixes();
    }
    
    static {
        EMPTY_ENUMERATION = new Vector().elements();
    }
    
    final class Context
    {
        Hashtable prefixTable;
        Hashtable uriTable;
        Hashtable elementNameTable;
        Hashtable attributeNameTable;
        String defaultNS;
        boolean declsOK;
        private Vector declarations;
        private boolean declSeen;
        private Context parent;
        
        Context() {
            this.defaultNS = null;
            this.declsOK = true;
            this.declarations = null;
            this.declSeen = false;
            this.parent = null;
            this.copyTables();
        }
        
        void setParent(final Context parent) {
            this.parent = parent;
            this.declarations = null;
            this.prefixTable = parent.prefixTable;
            this.uriTable = parent.uriTable;
            this.elementNameTable = parent.elementNameTable;
            this.attributeNameTable = parent.attributeNameTable;
            this.defaultNS = parent.defaultNS;
            this.declSeen = false;
            this.declsOK = true;
        }
        
        void clear() {
            this.parent = null;
            this.prefixTable = null;
            this.uriTable = null;
            this.elementNameTable = null;
            this.attributeNameTable = null;
            this.defaultNS = null;
        }
        
        void declarePrefix(String intern, String intern2) {
            if (!this.declsOK) {
                throw new IllegalStateException("can't declare any more prefixes in this context");
            }
            if (!this.declSeen) {
                this.copyTables();
            }
            if (this.declarations == null) {
                this.declarations = new Vector();
            }
            intern = intern.intern();
            intern2 = intern2.intern();
            if ("".equals(intern)) {
                if ("".equals(intern2)) {
                    this.defaultNS = null;
                }
                else {
                    this.defaultNS = intern2;
                }
            }
            else {
                this.prefixTable.put(intern, intern2);
                this.uriTable.put(intern2, intern);
            }
            this.declarations.addElement(intern);
        }
        
        String[] processName(final String key, final boolean b) {
            this.declsOK = false;
            Hashtable hashtable;
            if (b) {
                hashtable = this.attributeNameTable;
            }
            else {
                hashtable = this.elementNameTable;
            }
            final String[] array = hashtable.get(key);
            if (array != null) {
                return array;
            }
            final String[] value = { null, null, key.intern() };
            final int index = key.indexOf(58);
            if (index == -1) {
                if (b || this.defaultNS == null) {
                    value[0] = "";
                }
                else {
                    value[0] = this.defaultNS;
                }
                value[1] = value[2];
            }
            else {
                final String substring = key.substring(0, index);
                final String substring2 = key.substring(index + 1);
                String defaultNS;
                if ("".equals(substring)) {
                    defaultNS = this.defaultNS;
                }
                else {
                    defaultNS = this.prefixTable.get(substring);
                }
                if (defaultNS == null) {
                    return null;
                }
                value[0] = defaultNS;
                value[1] = substring2.intern();
            }
            hashtable.put(value[2], value);
            return value;
        }
        
        String getURI(final String s) {
            if ("".equals(s)) {
                return this.defaultNS;
            }
            if (this.prefixTable == null) {
                return null;
            }
            return this.prefixTable.get(s);
        }
        
        String getPrefix(final String key) {
            if (this.uriTable == null) {
                return null;
            }
            return this.uriTable.get(key);
        }
        
        Enumeration getDeclaredPrefixes() {
            if (this.declarations == null) {
                return NamespaceSupport.EMPTY_ENUMERATION;
            }
            return this.declarations.elements();
        }
        
        Enumeration getPrefixes() {
            if (this.prefixTable == null) {
                return NamespaceSupport.EMPTY_ENUMERATION;
            }
            return this.prefixTable.keys();
        }
        
        private void copyTables() {
            if (this.prefixTable != null) {
                this.prefixTable = (Hashtable)this.prefixTable.clone();
            }
            else {
                this.prefixTable = new Hashtable();
            }
            if (this.uriTable != null) {
                this.uriTable = (Hashtable)this.uriTable.clone();
            }
            else {
                this.uriTable = new Hashtable();
            }
            this.elementNameTable = new Hashtable();
            this.attributeNameTable = new Hashtable();
            this.declSeen = true;
        }
    }
}
