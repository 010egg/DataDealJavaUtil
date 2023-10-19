// 
// Decompiled by Procyon v0.5.36
// 

package com.typesafe.config.impl;

import com.typesafe.config.ConfigOrigin;
import java.util.ArrayList;
import com.typesafe.config.ConfigSyntax;
import java.util.Iterator;
import java.util.Collection;

final class ConfigNodeObject extends ConfigNodeComplexValue
{
    ConfigNodeObject(final Collection<AbstractConfigNode> children) {
        super(children);
    }
    
    protected ConfigNodeObject newNode(final Collection<AbstractConfigNode> nodes) {
        return new ConfigNodeObject(nodes);
    }
    
    public boolean hasValue(final Path desiredPath) {
        for (final AbstractConfigNode node : this.children) {
            if (node instanceof ConfigNodeField) {
                final ConfigNodeField field = (ConfigNodeField)node;
                final Path key = field.path().value();
                if (key.equals(desiredPath) || key.startsWith(desiredPath)) {
                    return true;
                }
                if (!desiredPath.startsWith(key) || !(field.value() instanceof ConfigNodeObject)) {
                    continue;
                }
                final ConfigNodeObject obj = (ConfigNodeObject)field.value();
                final Path remainingPath = desiredPath.subPath(key.length());
                if (obj.hasValue(remainingPath)) {
                    return true;
                }
                continue;
            }
        }
        return false;
    }
    
    protected ConfigNodeObject changeValueOnPath(final Path desiredPath, final AbstractConfigNodeValue value, final ConfigSyntax flavor) {
        final ArrayList<AbstractConfigNode> childrenCopy = new ArrayList<AbstractConfigNode>(super.children);
        boolean seenNonMatching = false;
        AbstractConfigNodeValue valueCopy = value;
        for (int i = childrenCopy.size() - 1; i >= 0; --i) {
            if (childrenCopy.get(i) instanceof ConfigNodeSingleToken) {
                final Token t = childrenCopy.get(i).token();
                if (flavor == ConfigSyntax.JSON && !seenNonMatching && t == Tokens.COMMA) {
                    childrenCopy.remove(i);
                }
            }
            else if (childrenCopy.get(i) instanceof ConfigNodeField) {
                final ConfigNodeField node = childrenCopy.get(i);
                final Path key = node.path().value();
                if ((valueCopy == null && key.equals(desiredPath)) || (key.startsWith(desiredPath) && !key.equals(desiredPath))) {
                    childrenCopy.remove(i);
                    for (int j = i; j < childrenCopy.size() && childrenCopy.get(j) instanceof ConfigNodeSingleToken; --j, ++j) {
                        final Token t2 = childrenCopy.get(j).token();
                        if (!Tokens.isIgnoredWhitespace(t2) && t2 != Tokens.COMMA) {
                            break;
                        }
                        childrenCopy.remove(j);
                    }
                }
                else if (key.equals(desiredPath)) {
                    seenNonMatching = true;
                    final AbstractConfigNode before = (i - 1 > 0) ? childrenCopy.get(i - 1) : null;
                    AbstractConfigNodeValue indentedValue;
                    if (value instanceof ConfigNodeComplexValue && before instanceof ConfigNodeSingleToken && Tokens.isIgnoredWhitespace(((ConfigNodeSingleToken)before).token())) {
                        indentedValue = ((ConfigNodeComplexValue)value).indentText(before);
                    }
                    else {
                        indentedValue = value;
                    }
                    childrenCopy.set(i, node.replaceValue(indentedValue));
                    valueCopy = null;
                }
                else if (desiredPath.startsWith(key)) {
                    seenNonMatching = true;
                    if (node.value() instanceof ConfigNodeObject) {
                        final Path remainingPath = desiredPath.subPath(key.length());
                        childrenCopy.set(i, node.replaceValue(((ConfigNodeObject)node.value()).changeValueOnPath(remainingPath, valueCopy, flavor)));
                        if (valueCopy != null && !node.equals(super.children.get(i))) {
                            valueCopy = null;
                        }
                    }
                }
                else {
                    seenNonMatching = true;
                }
            }
        }
        return new ConfigNodeObject(childrenCopy);
    }
    
    public ConfigNodeObject setValueOnPath(final String desiredPath, final AbstractConfigNodeValue value) {
        return this.setValueOnPath(desiredPath, value, ConfigSyntax.CONF);
    }
    
    public ConfigNodeObject setValueOnPath(final String desiredPath, final AbstractConfigNodeValue value, final ConfigSyntax flavor) {
        final ConfigNodePath path = PathParser.parsePathNode(desiredPath, flavor);
        return this.setValueOnPath(path, value, flavor);
    }
    
    private ConfigNodeObject setValueOnPath(final ConfigNodePath desiredPath, final AbstractConfigNodeValue value, final ConfigSyntax flavor) {
        final ConfigNodeObject node = this.changeValueOnPath(desiredPath.value(), value, flavor);
        if (!node.hasValue(desiredPath.value())) {
            return node.addValueOnPath(desiredPath, value, flavor);
        }
        return node;
    }
    
    private Collection<AbstractConfigNode> indentation() {
        boolean seenNewLine = false;
        final ArrayList<AbstractConfigNode> indentation = new ArrayList<AbstractConfigNode>();
        if (this.children.isEmpty()) {
            return indentation;
        }
        for (int i = 0; i < this.children.size(); ++i) {
            if (!seenNewLine) {
                if (this.children.get(i) instanceof ConfigNodeSingleToken && Tokens.isNewline(this.children.get(i).token())) {
                    seenNewLine = true;
                    indentation.add(new ConfigNodeSingleToken(Tokens.newLine(null)));
                }
            }
            else if (this.children.get(i) instanceof ConfigNodeSingleToken && Tokens.isIgnoredWhitespace(this.children.get(i).token()) && i + 1 < this.children.size() && (this.children.get(i + 1) instanceof ConfigNodeField || this.children.get(i + 1) instanceof ConfigNodeInclude)) {
                indentation.add(this.children.get(i));
                return indentation;
            }
        }
        if (indentation.isEmpty()) {
            indentation.add(new ConfigNodeSingleToken(Tokens.newIgnoredWhitespace(null, " ")));
        }
        else {
            final AbstractConfigNode last = this.children.get(this.children.size() - 1);
            if (last instanceof ConfigNodeSingleToken && ((ConfigNodeSingleToken)last).token() == Tokens.CLOSE_CURLY) {
                final AbstractConfigNode beforeLast = this.children.get(this.children.size() - 2);
                String indent = "";
                if (beforeLast instanceof ConfigNodeSingleToken && Tokens.isIgnoredWhitespace(((ConfigNodeSingleToken)beforeLast).token())) {
                    indent = ((ConfigNodeSingleToken)beforeLast).token().tokenText();
                }
                indent += "  ";
                indentation.add(new ConfigNodeSingleToken(Tokens.newIgnoredWhitespace(null, indent)));
                return indentation;
            }
        }
        return indentation;
    }
    
    protected ConfigNodeObject addValueOnPath(final ConfigNodePath desiredPath, final AbstractConfigNodeValue value, final ConfigSyntax flavor) {
        final Path path = desiredPath.value();
        final ArrayList<AbstractConfigNode> childrenCopy = new ArrayList<AbstractConfigNode>(super.children);
        final ArrayList<AbstractConfigNode> indentation = new ArrayList<AbstractConfigNode>(this.indentation());
        AbstractConfigNodeValue indentedValue;
        if (value instanceof ConfigNodeComplexValue && !indentation.isEmpty()) {
            indentedValue = ((ConfigNodeComplexValue)value).indentText(indentation.get(indentation.size() - 1));
        }
        else {
            indentedValue = value;
        }
        final boolean sameLine = indentation.size() <= 0 || !(indentation.get(0) instanceof ConfigNodeSingleToken) || !Tokens.isNewline(indentation.get(0).token());
        if (path.length() > 1) {
            for (int i = super.children.size() - 1; i >= 0; --i) {
                if (super.children.get(i) instanceof ConfigNodeField) {
                    final ConfigNodeField node = super.children.get(i);
                    final Path key = node.path().value();
                    if (path.startsWith(key) && node.value() instanceof ConfigNodeObject) {
                        final ConfigNodePath remainingPath = desiredPath.subPath(key.length());
                        final ConfigNodeObject newValue = (ConfigNodeObject)node.value();
                        childrenCopy.set(i, node.replaceValue(newValue.addValueOnPath(remainingPath, value, flavor)));
                        return new ConfigNodeObject(childrenCopy);
                    }
                }
            }
        }
        final boolean startsWithBrace = !super.children.isEmpty() && super.children.get(0) instanceof ConfigNodeSingleToken && super.children.get(0).token() == Tokens.OPEN_CURLY;
        final ArrayList<AbstractConfigNode> newNodes = new ArrayList<AbstractConfigNode>();
        newNodes.addAll(indentation);
        newNodes.add(desiredPath.first());
        newNodes.add(new ConfigNodeSingleToken(Tokens.newIgnoredWhitespace(null, " ")));
        newNodes.add(new ConfigNodeSingleToken(Tokens.COLON));
        newNodes.add(new ConfigNodeSingleToken(Tokens.newIgnoredWhitespace(null, " ")));
        if (path.length() == 1) {
            newNodes.add(indentedValue);
        }
        else {
            final ArrayList<AbstractConfigNode> newObjectNodes = new ArrayList<AbstractConfigNode>();
            newObjectNodes.add(new ConfigNodeSingleToken(Tokens.OPEN_CURLY));
            if (indentation.isEmpty()) {
                newObjectNodes.add(new ConfigNodeSingleToken(Tokens.newLine(null)));
            }
            newObjectNodes.addAll(indentation);
            newObjectNodes.add(new ConfigNodeSingleToken(Tokens.CLOSE_CURLY));
            final ConfigNodeObject newObject = new ConfigNodeObject(newObjectNodes);
            newNodes.add(newObject.addValueOnPath(desiredPath.subPath(1), indentedValue, flavor));
        }
        if (flavor == ConfigSyntax.JSON || startsWithBrace || sameLine) {
            int j = childrenCopy.size() - 1;
            while (j >= 0) {
                if ((flavor == ConfigSyntax.JSON || sameLine) && childrenCopy.get(j) instanceof ConfigNodeField) {
                    if (j + 1 >= childrenCopy.size() || !(childrenCopy.get(j + 1) instanceof ConfigNodeSingleToken) || childrenCopy.get(j + 1).token() != Tokens.COMMA) {
                        childrenCopy.add(j + 1, new ConfigNodeSingleToken(Tokens.COMMA));
                        break;
                    }
                    break;
                }
                else {
                    if (startsWithBrace && childrenCopy.get(j) instanceof ConfigNodeSingleToken && childrenCopy.get(j).token == Tokens.CLOSE_CURLY) {
                        final AbstractConfigNode previous = childrenCopy.get(j - 1);
                        if (previous instanceof ConfigNodeSingleToken && Tokens.isNewline(((ConfigNodeSingleToken)previous).token())) {
                            childrenCopy.add(j - 1, new ConfigNodeField(newNodes));
                            --j;
                        }
                        else if (previous instanceof ConfigNodeSingleToken && Tokens.isIgnoredWhitespace(((ConfigNodeSingleToken)previous).token())) {
                            final AbstractConfigNode beforePrevious = childrenCopy.get(j - 2);
                            if (sameLine) {
                                childrenCopy.add(j - 1, new ConfigNodeField(newNodes));
                                --j;
                            }
                            else if (beforePrevious instanceof ConfigNodeSingleToken && Tokens.isNewline(((ConfigNodeSingleToken)beforePrevious).token())) {
                                childrenCopy.add(j - 2, new ConfigNodeField(newNodes));
                                j -= 2;
                            }
                            else {
                                childrenCopy.add(j, new ConfigNodeField(newNodes));
                            }
                        }
                        else {
                            childrenCopy.add(j, new ConfigNodeField(newNodes));
                        }
                    }
                    --j;
                }
            }
        }
        if (!startsWithBrace) {
            if (!childrenCopy.isEmpty() && childrenCopy.get(childrenCopy.size() - 1) instanceof ConfigNodeSingleToken && Tokens.isNewline(childrenCopy.get(childrenCopy.size() - 1).token())) {
                childrenCopy.add(childrenCopy.size() - 1, new ConfigNodeField(newNodes));
            }
            else {
                childrenCopy.add(new ConfigNodeField(newNodes));
            }
        }
        return new ConfigNodeObject(childrenCopy);
    }
    
    public ConfigNodeObject removeValueOnPath(final String desiredPath, final ConfigSyntax flavor) {
        final Path path = PathParser.parsePathNode(desiredPath, flavor).value();
        return this.changeValueOnPath(path, null, flavor);
    }
}
