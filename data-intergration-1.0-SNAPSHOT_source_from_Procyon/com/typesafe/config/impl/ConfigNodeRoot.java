// 
// Decompiled by Procyon v0.5.36
// 

package com.typesafe.config.impl;

import java.util.ArrayList;
import com.typesafe.config.ConfigSyntax;
import java.util.Iterator;
import com.typesafe.config.ConfigException;
import java.util.Collection;
import com.typesafe.config.ConfigOrigin;

final class ConfigNodeRoot extends ConfigNodeComplexValue
{
    private final ConfigOrigin origin;
    
    ConfigNodeRoot(final Collection<AbstractConfigNode> children, final ConfigOrigin origin) {
        super(children);
        this.origin = origin;
    }
    
    protected ConfigNodeRoot newNode(final Collection<AbstractConfigNode> nodes) {
        throw new ConfigException.BugOrBroken("Tried to indent the root object");
    }
    
    protected ConfigNodeComplexValue value() {
        for (final AbstractConfigNode node : this.children) {
            if (node instanceof ConfigNodeComplexValue) {
                return (ConfigNodeComplexValue)node;
            }
        }
        throw new ConfigException.BugOrBroken("ConfigNodeRoot did not contain a value");
    }
    
    protected ConfigNodeRoot setValue(final String desiredPath, final AbstractConfigNodeValue value, final ConfigSyntax flavor) {
        final ArrayList<AbstractConfigNode> childrenCopy = new ArrayList<AbstractConfigNode>(this.children);
        for (int i = 0; i < childrenCopy.size(); ++i) {
            final AbstractConfigNode node = childrenCopy.get(i);
            if (node instanceof ConfigNodeComplexValue) {
                if (node instanceof ConfigNodeArray) {
                    throw new ConfigException.WrongType(this.origin, "The ConfigDocument had an array at the root level, and values cannot be modified inside an array.");
                }
                if (node instanceof ConfigNodeObject) {
                    if (value == null) {
                        childrenCopy.set(i, ((ConfigNodeObject)node).removeValueOnPath(desiredPath, flavor));
                    }
                    else {
                        childrenCopy.set(i, ((ConfigNodeObject)node).setValueOnPath(desiredPath, value, flavor));
                    }
                    return new ConfigNodeRoot(childrenCopy, this.origin);
                }
            }
        }
        throw new ConfigException.BugOrBroken("ConfigNodeRoot did not contain a value");
    }
    
    protected boolean hasValue(final String desiredPath) {
        final Path path = PathParser.parsePath(desiredPath);
        final ArrayList<AbstractConfigNode> childrenCopy = new ArrayList<AbstractConfigNode>(this.children);
        for (int i = 0; i < childrenCopy.size(); ++i) {
            final AbstractConfigNode node = childrenCopy.get(i);
            if (node instanceof ConfigNodeComplexValue) {
                if (node instanceof ConfigNodeArray) {
                    throw new ConfigException.WrongType(this.origin, "The ConfigDocument had an array at the root level, and values cannot be modified inside an array.");
                }
                if (node instanceof ConfigNodeObject) {
                    return ((ConfigNodeObject)node).hasValue(path);
                }
            }
        }
        throw new ConfigException.BugOrBroken("ConfigNodeRoot did not contain a value");
    }
}
