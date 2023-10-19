// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.core.v3;

import org.postgresql.core.Utils;
import org.postgresql.core.Parser;
import org.postgresql.core.ParameterList;
import java.lang.ref.PhantomReference;
import org.postgresql.core.Field;

class SimpleQuery implements V3Query
{
    private final String[] fragments;
    private final ProtocolConnectionImpl protoConnection;
    private String statementName;
    private byte[] encodedStatementName;
    private Field[] fields;
    private boolean portalDescribed;
    private boolean statementDescribed;
    private PhantomReference cleanupRef;
    private int[] preparedTypes;
    private Integer cachedMaxResultRowSize;
    static final SimpleParameterList NO_PARAMETERS;
    
    SimpleQuery(final String[] fragments, final ProtocolConnectionImpl protoConnection) {
        this.fragments = this.unmarkDoubleQuestion(fragments, protoConnection);
        this.protoConnection = protoConnection;
    }
    
    @Override
    public ParameterList createParameterList() {
        if (this.fragments.length == 1) {
            return SimpleQuery.NO_PARAMETERS;
        }
        return new SimpleParameterList(this.fragments.length - 1, this.protoConnection);
    }
    
    @Override
    public String toString(final ParameterList parameters) {
        final StringBuilder sbuf = new StringBuilder(this.fragments[0]);
        for (int i = 1; i < this.fragments.length; ++i) {
            if (parameters == null) {
                sbuf.append('?');
            }
            else {
                sbuf.append(parameters.toString(i));
            }
            sbuf.append(this.fragments[i]);
        }
        return sbuf.toString();
    }
    
    @Override
    public String toString() {
        return this.toString(null);
    }
    
    @Override
    public void close() {
        this.unprepare();
    }
    
    @Override
    public SimpleQuery[] getSubqueries() {
        return null;
    }
    
    public int getMaxResultRowSize() {
        if (this.cachedMaxResultRowSize != null) {
            return this.cachedMaxResultRowSize;
        }
        if (!this.statementDescribed) {
            throw new IllegalStateException("Cannot estimate result row size on a statement that is not described");
        }
        int maxResultRowSize = 0;
        if (this.fields != null) {
            for (int i = 0; i < this.fields.length; ++i) {
                final Field f = this.fields[i];
                final int fieldLength = f.getLength();
                if (fieldLength < 1 || fieldLength >= 65535) {
                    maxResultRowSize = -1;
                    break;
                }
                maxResultRowSize += fieldLength;
            }
        }
        this.cachedMaxResultRowSize = maxResultRowSize;
        return maxResultRowSize;
    }
    
    String[] getFragments() {
        return this.fragments;
    }
    
    String[] unmarkDoubleQuestion(final String[] fragments, final ProtocolConnectionImpl protoConnection) {
        if (fragments != null && protoConnection != null) {
            final boolean standardConformingStrings = protoConnection.getStandardConformingStrings();
            for (int i = 0; i < fragments.length; ++i) {
                if (fragments[i] != null) {
                    fragments[i] = Parser.unmarkDoubleQuestion(fragments[i], standardConformingStrings);
                }
            }
        }
        return fragments;
    }
    
    void setStatementName(final String statementName) {
        this.statementName = statementName;
        this.encodedStatementName = Utils.encodeUTF8(statementName);
    }
    
    void setStatementTypes(final int[] paramTypes) {
        this.preparedTypes = paramTypes;
    }
    
    int[] getStatementTypes() {
        return this.preparedTypes;
    }
    
    String getStatementName() {
        return this.statementName;
    }
    
    boolean isPreparedFor(final int[] paramTypes) {
        if (this.statementName == null) {
            return false;
        }
        for (int i = 0; i < paramTypes.length; ++i) {
            if (paramTypes[i] != 0 && paramTypes[i] != this.preparedTypes[i]) {
                return false;
            }
        }
        return true;
    }
    
    boolean hasUnresolvedTypes() {
        if (this.preparedTypes == null) {
            return true;
        }
        for (int i = 0; i < this.preparedTypes.length; ++i) {
            if (this.preparedTypes[i] == 0) {
                return true;
            }
        }
        return false;
    }
    
    byte[] getEncodedStatementName() {
        return this.encodedStatementName;
    }
    
    void setFields(final Field[] fields) {
        this.fields = fields;
        this.cachedMaxResultRowSize = null;
    }
    
    Field[] getFields() {
        return this.fields;
    }
    
    boolean isPortalDescribed() {
        return this.portalDescribed;
    }
    
    void setPortalDescribed(final boolean portalDescribed) {
        this.portalDescribed = portalDescribed;
        this.cachedMaxResultRowSize = null;
    }
    
    @Override
    public boolean isStatementDescribed() {
        return this.statementDescribed;
    }
    
    void setStatementDescribed(final boolean statementDescribed) {
        this.statementDescribed = statementDescribed;
        this.cachedMaxResultRowSize = null;
    }
    
    @Override
    public boolean isEmpty() {
        return this.fragments.length == 1 && "".equals(this.fragments[0]);
    }
    
    void setCleanupRef(final PhantomReference cleanupRef) {
        if (this.cleanupRef != null) {
            this.cleanupRef.clear();
            this.cleanupRef.enqueue();
        }
        this.cleanupRef = cleanupRef;
    }
    
    void unprepare() {
        if (this.cleanupRef != null) {
            this.cleanupRef.clear();
            this.cleanupRef.enqueue();
            this.cleanupRef = null;
        }
        this.statementName = null;
        this.encodedStatementName = null;
        this.fields = null;
        this.portalDescribed = false;
        this.statementDescribed = false;
        this.cachedMaxResultRowSize = null;
    }
    
    static {
        NO_PARAMETERS = new SimpleParameterList(0, null);
    }
}
