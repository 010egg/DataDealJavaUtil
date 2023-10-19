// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.subst;

import java.util.Iterator;
import ch.qos.logback.core.util.OptionHelper;
import java.util.Stack;
import java.util.List;
import ch.qos.logback.core.spi.ScanException;
import ch.qos.logback.core.spi.PropertyContainer;

public class NodeToStringTransformer
{
    final Node node;
    final PropertyContainer propertyContainer0;
    final PropertyContainer propertyContainer1;
    
    public NodeToStringTransformer(final Node node, final PropertyContainer propertyContainer0, final PropertyContainer propertyContainer1) {
        this.node = node;
        this.propertyContainer0 = propertyContainer0;
        this.propertyContainer1 = propertyContainer1;
    }
    
    public NodeToStringTransformer(final Node node, final PropertyContainer propertyContainer0) {
        this(node, propertyContainer0, null);
    }
    
    public static String substituteVariable(final String input, final PropertyContainer pc0, final PropertyContainer pc1) throws ScanException {
        final Node node = tokenizeAndParseString(input);
        final NodeToStringTransformer nodeToStringTransformer = new NodeToStringTransformer(node, pc0, pc1);
        return nodeToStringTransformer.transform();
    }
    
    private static Node tokenizeAndParseString(final String value) throws ScanException {
        final Tokenizer tokenizer = new Tokenizer(value);
        final List<Token> tokens = tokenizer.tokenize();
        final Parser parser = new Parser(tokens);
        return parser.parse();
    }
    
    public String transform() throws ScanException {
        final StringBuilder stringBuilder = new StringBuilder();
        this.compileNode(this.node, stringBuilder, new Stack<Node>());
        return stringBuilder.toString();
    }
    
    private void compileNode(final Node inputNode, final StringBuilder stringBuilder, final Stack<Node> cycleCheckStack) throws ScanException {
        for (Node n = inputNode; n != null; n = n.next) {
            switch (n.type) {
                case LITERAL: {
                    this.handleLiteral(n, stringBuilder);
                    break;
                }
                case VARIABLE: {
                    this.handleVariable(n, stringBuilder, cycleCheckStack);
                    break;
                }
            }
        }
    }
    
    private void handleVariable(final Node n, final StringBuilder stringBuilder, final Stack<Node> cycleCheckStack) throws ScanException {
        if (this.haveVisitedNodeAlready(n, cycleCheckStack)) {
            cycleCheckStack.push(n);
            final String error = this.constructRecursionErrorMessage(cycleCheckStack);
            throw new IllegalArgumentException(error);
        }
        cycleCheckStack.push(n);
        final StringBuilder keyBuffer = new StringBuilder();
        final Node payload = (Node)n.payload;
        this.compileNode(payload, keyBuffer, cycleCheckStack);
        final String key = keyBuffer.toString();
        final String value = this.lookupKey(key);
        if (value != null) {
            final Node innerNode = tokenizeAndParseString(value);
            this.compileNode(innerNode, stringBuilder, cycleCheckStack);
            cycleCheckStack.pop();
            return;
        }
        if (n.defaultPart == null) {
            stringBuilder.append(key + "_IS_UNDEFINED");
            cycleCheckStack.pop();
            return;
        }
        final Node defaultPart = (Node)n.defaultPart;
        final StringBuilder defaultPartBuffer = new StringBuilder();
        this.compileNode(defaultPart, defaultPartBuffer, cycleCheckStack);
        cycleCheckStack.pop();
        final String defaultVal = defaultPartBuffer.toString();
        stringBuilder.append(defaultVal);
    }
    
    private String lookupKey(final String key) {
        String value = this.propertyContainer0.getProperty(key);
        if (value != null) {
            return value;
        }
        if (this.propertyContainer1 != null) {
            value = this.propertyContainer1.getProperty(key);
            if (value != null) {
                return value;
            }
        }
        value = OptionHelper.getSystemProperty(key, null);
        if (value != null) {
            return value;
        }
        value = OptionHelper.getEnv(key);
        if (value != null) {
            return value;
        }
        return null;
    }
    
    private void handleLiteral(final Node n, final StringBuilder stringBuilder) {
        stringBuilder.append((String)n.payload);
    }
    
    private String variableNodeValue(final Node variableNode) {
        final Node literalPayload = (Node)variableNode.payload;
        return (String)literalPayload.payload;
    }
    
    private String constructRecursionErrorMessage(final Stack<Node> recursionNodes) {
        final StringBuilder errorBuilder = new StringBuilder("Circular variable reference detected while parsing input [");
        for (final Node stackNode : recursionNodes) {
            errorBuilder.append("${").append(this.variableNodeValue(stackNode)).append("}");
            if (recursionNodes.lastElement() != stackNode) {
                errorBuilder.append(" --> ");
            }
        }
        errorBuilder.append("]");
        return errorBuilder.toString();
    }
    
    private boolean haveVisitedNodeAlready(final Node node, final Stack<Node> cycleDetectionStack) {
        for (final Node cycleNode : cycleDetectionStack) {
            if (this.equalNodes(node, cycleNode)) {
                return true;
            }
        }
        return false;
    }
    
    private boolean equalNodes(final Node node1, final Node node2) {
        return (node1.type == null || node1.type.equals(node2.type)) && (node1.payload == null || node1.payload.equals(node2.payload)) && (node1.defaultPart == null || node1.defaultPart.equals(node2.defaultPart));
    }
}
