// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.util;

import java.util.HashMap;
import java.util.Map;
import java.io.Serializable;

public class ServerErrorMessage implements Serializable
{
    private static final Character SEVERITY;
    private static final Character MESSAGE;
    private static final Character DETAIL;
    private static final Character HINT;
    private static final Character POSITION;
    private static final Character WHERE;
    private static final Character FILE;
    private static final Character LINE;
    private static final Character ROUTINE;
    private static final Character SQLSTATE;
    private static final Character INTERNAL_POSITION;
    private static final Character INTERNAL_QUERY;
    private static final Character SCHEMA;
    private static final Character TABLE;
    private static final Character COLUMN;
    private static final Character DATATYPE;
    private static final Character CONSTRAINT;
    private final Map m_mesgParts;
    private final int verbosity;
    
    public ServerErrorMessage(final String p_serverError, final int verbosity) {
        this.m_mesgParts = new HashMap();
        this.verbosity = verbosity;
        final char[] l_chars = p_serverError.toCharArray();
        for (int l_pos = 0, l_length = l_chars.length; l_pos < l_length; ++l_pos) {
            final char l_mesgType = l_chars[l_pos];
            if (l_mesgType != '\0') {
                int l_startString;
                for (l_startString = ++l_pos; l_pos < l_length && l_chars[l_pos] != '\0'; ++l_pos) {}
                final String l_mesgPart = new String(l_chars, l_startString, l_pos - l_startString);
                this.m_mesgParts.put(new Character(l_mesgType), l_mesgPart);
            }
        }
    }
    
    public String getSQLState() {
        return this.m_mesgParts.get(ServerErrorMessage.SQLSTATE);
    }
    
    public String getMessage() {
        return this.m_mesgParts.get(ServerErrorMessage.MESSAGE);
    }
    
    public String getSeverity() {
        return this.m_mesgParts.get(ServerErrorMessage.SEVERITY);
    }
    
    public String getDetail() {
        return this.m_mesgParts.get(ServerErrorMessage.DETAIL);
    }
    
    public String getHint() {
        return this.m_mesgParts.get(ServerErrorMessage.HINT);
    }
    
    public int getPosition() {
        return this.getIntegerPart(ServerErrorMessage.POSITION);
    }
    
    public String getWhere() {
        return this.m_mesgParts.get(ServerErrorMessage.WHERE);
    }
    
    public String getSchema() {
        return this.m_mesgParts.get(ServerErrorMessage.SCHEMA);
    }
    
    public String getTable() {
        return this.m_mesgParts.get(ServerErrorMessage.TABLE);
    }
    
    public String getColumn() {
        return this.m_mesgParts.get(ServerErrorMessage.COLUMN);
    }
    
    public String getDatatype() {
        return this.m_mesgParts.get(ServerErrorMessage.DATATYPE);
    }
    
    public String getConstraint() {
        return this.m_mesgParts.get(ServerErrorMessage.CONSTRAINT);
    }
    
    public String getFile() {
        return this.m_mesgParts.get(ServerErrorMessage.FILE);
    }
    
    public int getLine() {
        return this.getIntegerPart(ServerErrorMessage.LINE);
    }
    
    public String getRoutine() {
        return this.m_mesgParts.get(ServerErrorMessage.ROUTINE);
    }
    
    public String getInternalQuery() {
        return this.m_mesgParts.get(ServerErrorMessage.INTERNAL_QUERY);
    }
    
    public int getInternalPosition() {
        return this.getIntegerPart(ServerErrorMessage.INTERNAL_POSITION);
    }
    
    private int getIntegerPart(final Character c) {
        final String s = this.m_mesgParts.get(c);
        if (s == null) {
            return 0;
        }
        return Integer.parseInt(s);
    }
    
    @Override
    public String toString() {
        final StringBuilder l_totalMessage = new StringBuilder();
        String l_message = this.m_mesgParts.get(ServerErrorMessage.SEVERITY);
        if (l_message != null) {
            l_totalMessage.append(l_message).append(": ");
        }
        l_message = this.m_mesgParts.get(ServerErrorMessage.MESSAGE);
        if (l_message != null) {
            l_totalMessage.append(l_message);
        }
        l_message = this.m_mesgParts.get(ServerErrorMessage.DETAIL);
        if (l_message != null) {
            l_totalMessage.append("\n  ").append(GT.tr("Detail: {0}", l_message));
        }
        l_message = this.m_mesgParts.get(ServerErrorMessage.HINT);
        if (l_message != null) {
            l_totalMessage.append("\n  ").append(GT.tr("Hint: {0}", l_message));
        }
        l_message = this.m_mesgParts.get(ServerErrorMessage.POSITION);
        if (l_message != null) {
            l_totalMessage.append("\n  ").append(GT.tr("Position: {0}", l_message));
        }
        l_message = this.m_mesgParts.get(ServerErrorMessage.WHERE);
        if (l_message != null) {
            l_totalMessage.append("\n  ").append(GT.tr("Where: {0}", l_message));
        }
        if (this.verbosity > 2) {
            final String l_internalQuery = this.m_mesgParts.get(ServerErrorMessage.INTERNAL_QUERY);
            if (l_internalQuery != null) {
                l_totalMessage.append("\n  ").append(GT.tr("Internal Query: {0}", l_internalQuery));
            }
            final String l_internalPosition = this.m_mesgParts.get(ServerErrorMessage.INTERNAL_POSITION);
            if (l_internalPosition != null) {
                l_totalMessage.append("\n  ").append(GT.tr("Internal Position: {0}", l_internalPosition));
            }
            final String l_file = this.m_mesgParts.get(ServerErrorMessage.FILE);
            final String l_line = this.m_mesgParts.get(ServerErrorMessage.LINE);
            final String l_routine = this.m_mesgParts.get(ServerErrorMessage.ROUTINE);
            if (l_file != null || l_line != null || l_routine != null) {
                l_totalMessage.append("\n  ").append(GT.tr("Location: File: {0}, Routine: {1}, Line: {2}", new Object[] { l_file, l_routine, l_line }));
            }
            l_message = this.m_mesgParts.get(ServerErrorMessage.SQLSTATE);
            if (l_message != null) {
                l_totalMessage.append("\n  ").append(GT.tr("Server SQLState: {0}", l_message));
            }
        }
        return l_totalMessage.toString();
    }
    
    static {
        SEVERITY = new Character('S');
        MESSAGE = new Character('M');
        DETAIL = new Character('D');
        HINT = new Character('H');
        POSITION = new Character('P');
        WHERE = new Character('W');
        FILE = new Character('F');
        LINE = new Character('L');
        ROUTINE = new Character('R');
        SQLSTATE = new Character('C');
        INTERNAL_POSITION = new Character('p');
        INTERNAL_QUERY = new Character('q');
        SCHEMA = new Character('s');
        TABLE = new Character('t');
        COLUMN = new Character('c');
        DATATYPE = new Character('d');
        CONSTRAINT = new Character('n');
    }
}
