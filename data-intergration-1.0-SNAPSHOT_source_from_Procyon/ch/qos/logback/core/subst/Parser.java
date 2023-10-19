// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.subst;

import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.spi.ScanException;
import java.util.List;

public class Parser
{
    final List<Token> tokenList;
    int pointer;
    
    public Parser(final List<Token> tokenList) {
        this.pointer = 0;
        this.tokenList = tokenList;
    }
    
    public Node parse() throws ScanException {
        if (this.tokenList == null || this.tokenList.isEmpty()) {
            return null;
        }
        return this.E();
    }
    
    private Node E() throws ScanException {
        final Node t = this.T();
        if (t == null) {
            return null;
        }
        final Node eOpt = this.Eopt();
        if (eOpt != null) {
            t.append(eOpt);
        }
        return t;
    }
    
    private Node Eopt() throws ScanException {
        final Token next = this.peekAtCurentToken();
        if (next == null) {
            return null;
        }
        return this.E();
    }
    
    private Node T() throws ScanException {
        final Token t = this.peekAtCurentToken();
        switch (t.type) {
            case LITERAL: {
                this.advanceTokenPointer();
                return this.makeNewLiteralNode(t.payload);
            }
            case CURLY_LEFT: {
                this.advanceTokenPointer();
                final Node innerNode = this.C();
                final Token right = this.peekAtCurentToken();
                this.expectCurlyRight(right);
                this.advanceTokenPointer();
                final Node curlyLeft = this.makeNewLiteralNode(CoreConstants.LEFT_ACCOLADE);
                curlyLeft.append(innerNode);
                curlyLeft.append(this.makeNewLiteralNode(CoreConstants.RIGHT_ACCOLADE));
                return curlyLeft;
            }
            case START: {
                this.advanceTokenPointer();
                final Node v = this.V();
                final Token w = this.peekAtCurentToken();
                this.expectCurlyRight(w);
                this.advanceTokenPointer();
                return v;
            }
            default: {
                return null;
            }
        }
    }
    
    private Node makeNewLiteralNode(final String s) {
        return new Node(Node.Type.LITERAL, s);
    }
    
    private Node V() throws ScanException {
        final Node e = this.E();
        final Node variable = new Node(Node.Type.VARIABLE, e);
        final Token t = this.peekAtCurentToken();
        if (this.isDefaultToken(t)) {
            this.advanceTokenPointer();
            final Node def = this.E();
            variable.defaultPart = def;
        }
        return variable;
    }
    
    private Node C() throws ScanException {
        final Node e0 = this.E();
        final Token t = this.peekAtCurentToken();
        if (this.isDefaultToken(t)) {
            this.advanceTokenPointer();
            final Node literal = this.makeNewLiteralNode(":-");
            e0.append(literal);
            final Node e2 = this.E();
            e0.append(e2);
        }
        return e0;
    }
    
    private boolean isDefaultToken(final Token t) {
        return t != null && t.type == Token.Type.DEFAULT;
    }
    
    void advanceTokenPointer() {
        ++this.pointer;
    }
    
    void expectNotNull(final Token t, final String expected) {
        if (t == null) {
            throw new IllegalArgumentException("All tokens consumed but was expecting \"" + expected + "\"");
        }
    }
    
    void expectCurlyRight(final Token t) throws ScanException {
        this.expectNotNull(t, "}");
        if (t.type != Token.Type.CURLY_RIGHT) {
            throw new ScanException("Expecting }");
        }
    }
    
    Token peekAtCurentToken() {
        if (this.pointer < this.tokenList.size()) {
            return this.tokenList.get(this.pointer);
        }
        return null;
    }
}
