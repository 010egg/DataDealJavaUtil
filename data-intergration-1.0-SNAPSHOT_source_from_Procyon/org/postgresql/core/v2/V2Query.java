// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.core.v2;

import java.util.List;
import org.postgresql.core.Parser;
import java.util.ArrayList;
import org.postgresql.core.ProtocolConnection;
import org.postgresql.core.ParameterList;
import org.postgresql.core.Query;

class V2Query implements Query
{
    private static final ParameterList NO_PARAMETERS;
    private final String[] fragments;
    private final boolean useEStringSyntax;
    
    V2Query(final String query, final boolean withParameters, final ProtocolConnection pconn) {
        this.useEStringSyntax = (pconn.getServerVersionNum() >= 80100);
        final boolean stdStrings = pconn.getStandardConformingStrings();
        if (!withParameters) {
            this.fragments = new String[] { query };
            return;
        }
        final List v = new ArrayList();
        int lastParmEnd = 0;
        final char[] aChars = query.toCharArray();
        for (int i = 0; i < aChars.length; ++i) {
            switch (aChars[i]) {
                case '\'': {
                    i = Parser.parseSingleQuotes(aChars, i, stdStrings);
                    break;
                }
                case '\"': {
                    i = Parser.parseDoubleQuotes(aChars, i);
                    break;
                }
                case '-': {
                    i = Parser.parseLineComment(aChars, i);
                    break;
                }
                case '/': {
                    i = Parser.parseBlockComment(aChars, i);
                    break;
                }
                case '$': {
                    i = Parser.parseDollarQuotes(aChars, i);
                    break;
                }
                case '?': {
                    if (i + 1 < aChars.length && aChars[i + 1] == '?') {
                        ++i;
                        break;
                    }
                    v.add(query.substring(lastParmEnd, i));
                    lastParmEnd = i + 1;
                    break;
                }
            }
        }
        v.add(query.substring(lastParmEnd, query.length()));
        this.fragments = new String[v.size()];
        for (int i = 0; i < this.fragments.length; ++i) {
            this.fragments[i] = Parser.unmarkDoubleQuestion(v.get(i), stdStrings);
        }
    }
    
    @Override
    public ParameterList createParameterList() {
        if (this.fragments.length == 1) {
            return V2Query.NO_PARAMETERS;
        }
        return new SimpleParameterList(this.fragments.length - 1, this.useEStringSyntax);
    }
    
    @Override
    public String toString(final ParameterList parameters) {
        final StringBuilder sbuf = new StringBuilder(this.fragments[0]);
        for (int i = 1; i < this.fragments.length; ++i) {
            if (parameters == null) {
                sbuf.append("?");
            }
            else {
                sbuf.append(parameters.toString(i));
            }
            sbuf.append(this.fragments[i]);
        }
        return sbuf.toString();
    }
    
    @Override
    public void close() {
    }
    
    String[] getFragments() {
        return this.fragments;
    }
    
    @Override
    public boolean isStatementDescribed() {
        return false;
    }
    
    @Override
    public boolean isEmpty() {
        return this.fragments.length == 1 && "".equals(this.fragments[0]);
    }
    
    static {
        NO_PARAMETERS = new SimpleParameterList(0, false);
    }
}
