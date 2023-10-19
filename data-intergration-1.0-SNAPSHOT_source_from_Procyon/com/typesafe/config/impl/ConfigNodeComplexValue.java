// 
// Decompiled by Procyon v0.5.36
// 

package com.typesafe.config.impl;

import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;

abstract class ConfigNodeComplexValue extends AbstractConfigNodeValue
{
    protected final ArrayList<AbstractConfigNode> children;
    
    ConfigNodeComplexValue(final Collection<AbstractConfigNode> children) {
        this.children = new ArrayList<AbstractConfigNode>(children);
    }
    
    public final Collection<AbstractConfigNode> children() {
        return this.children;
    }
    
    protected Collection<Token> tokens() {
        final ArrayList<Token> tokens = new ArrayList<Token>();
        for (final AbstractConfigNode child : this.children) {
            tokens.addAll(child.tokens());
        }
        return tokens;
    }
    
    protected ConfigNodeComplexValue indentText(final AbstractConfigNode indentation) {
        final ArrayList<AbstractConfigNode> childrenCopy = new ArrayList<AbstractConfigNode>(this.children);
        for (int i = 0; i < childrenCopy.size(); ++i) {
            final AbstractConfigNode child = childrenCopy.get(i);
            if (child instanceof ConfigNodeSingleToken && Tokens.isNewline(((ConfigNodeSingleToken)child).token())) {
                childrenCopy.add(i + 1, indentation);
                ++i;
            }
            else if (child instanceof ConfigNodeField) {
                final AbstractConfigNode value = ((ConfigNodeField)child).value();
                if (value instanceof ConfigNodeComplexValue) {
                    childrenCopy.set(i, ((ConfigNodeField)child).replaceValue(((ConfigNodeComplexValue)value).indentText(indentation)));
                }
            }
            else if (child instanceof ConfigNodeComplexValue) {
                childrenCopy.set(i, ((ConfigNodeComplexValue)child).indentText(indentation));
            }
        }
        return this.newNode(childrenCopy);
    }
    
    abstract ConfigNodeComplexValue newNode(final Collection<AbstractConfigNode> p0);
}
