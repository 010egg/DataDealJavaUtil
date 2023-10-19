// 
// Decompiled by Procyon v0.5.36
// 

package com.typesafe.config.impl;

import com.typesafe.config.ConfigException;

final class ResolveSource
{
    final AbstractConfigObject root;
    final Node<Container> pathFromRoot;
    
    ResolveSource(final AbstractConfigObject root, final Node<Container> pathFromRoot) {
        this.root = root;
        this.pathFromRoot = pathFromRoot;
    }
    
    ResolveSource(final AbstractConfigObject root) {
        this.root = root;
        this.pathFromRoot = null;
    }
    
    private AbstractConfigObject rootMustBeObj(final Container value) {
        if (value instanceof AbstractConfigObject) {
            return (AbstractConfigObject)value;
        }
        return SimpleConfigObject.empty();
    }
    
    private static ResultWithPath findInObject(final AbstractConfigObject obj, final ResolveContext context, final Path path) throws AbstractConfigValue.NotPossibleToResolve {
        if (ConfigImpl.traceSubstitutionsEnabled()) {
            ConfigImpl.trace("*** finding '" + path + "' in " + obj);
        }
        final Path restriction = context.restrictToChild();
        final ResolveResult<? extends AbstractConfigValue> partiallyResolved = context.restrict(path).resolve(obj, new ResolveSource(obj));
        final ResolveContext newContext = partiallyResolved.context.restrict(restriction);
        if (partiallyResolved.value instanceof AbstractConfigObject) {
            final ValueWithPath pair = findInObject((AbstractConfigObject)partiallyResolved.value, path);
            return new ResultWithPath(ResolveResult.make(newContext, pair.value), pair.pathFromRoot);
        }
        throw new ConfigException.BugOrBroken("resolved object to non-object " + obj + " to " + partiallyResolved);
    }
    
    private static ValueWithPath findInObject(final AbstractConfigObject obj, final Path path) {
        try {
            return findInObject(obj, path, null);
        }
        catch (ConfigException.NotResolved e) {
            throw ConfigImpl.improveNotResolved(path, e);
        }
    }
    
    private static ValueWithPath findInObject(final AbstractConfigObject obj, final Path path, final Node<Container> parents) {
        final String key = path.first();
        final Path next = path.remainder();
        if (ConfigImpl.traceSubstitutionsEnabled()) {
            ConfigImpl.trace("*** looking up '" + key + "' in " + obj);
        }
        final AbstractConfigValue v = obj.attemptPeekWithPartialResolve(key);
        final Node<Container> newParents = (Node<Container>)((parents == null) ? new Node<AbstractConfigObject>(obj) : parents.prepend(obj));
        if (next == null) {
            return new ValueWithPath(v, newParents);
        }
        if (v instanceof AbstractConfigObject) {
            return findInObject((AbstractConfigObject)v, next, newParents);
        }
        return new ValueWithPath(null, newParents);
    }
    
    ResultWithPath lookupSubst(final ResolveContext context, final SubstitutionExpression subst, final int prefixLength) throws AbstractConfigValue.NotPossibleToResolve {
        if (ConfigImpl.traceSubstitutionsEnabled()) {
            ConfigImpl.trace(context.depth(), "searching for " + subst);
        }
        if (ConfigImpl.traceSubstitutionsEnabled()) {
            ConfigImpl.trace(context.depth(), subst + " - looking up relative to file it occurred in");
        }
        ResultWithPath result = findInObject(this.root, context, subst.path());
        if (result.result.value == null) {
            final Path unprefixed = subst.path().subPath(prefixLength);
            if (prefixLength > 0) {
                if (ConfigImpl.traceSubstitutionsEnabled()) {
                    ConfigImpl.trace(result.result.context.depth(), unprefixed + " - looking up relative to parent file");
                }
                result = findInObject(this.root, result.result.context, unprefixed);
            }
            if (result.result.value == null && result.result.context.options().getUseSystemEnvironment()) {
                if (ConfigImpl.traceSubstitutionsEnabled()) {
                    ConfigImpl.trace(result.result.context.depth(), unprefixed + " - looking up in system environment");
                }
                result = findInObject(ConfigImpl.envVariablesAsConfigObject(), context, unprefixed);
            }
        }
        if (ConfigImpl.traceSubstitutionsEnabled()) {
            ConfigImpl.trace(result.result.context.depth(), "resolved to " + result);
        }
        return result;
    }
    
    ResolveSource pushParent(final Container parent) {
        if (parent == null) {
            throw new ConfigException.BugOrBroken("can't push null parent");
        }
        if (ConfigImpl.traceSubstitutionsEnabled()) {
            ConfigImpl.trace("pushing parent " + parent + " ==root " + (parent == this.root) + " onto " + this);
        }
        if (this.pathFromRoot != null) {
            final Container parentParent = this.pathFromRoot.head();
            if (ConfigImpl.traceSubstitutionsEnabled() && parentParent != null && !parentParent.hasDescendant((AbstractConfigValue)parent)) {
                ConfigImpl.trace("***** BUG ***** trying to push non-child of " + parentParent + ", non-child was " + parent);
            }
            return new ResolveSource(this.root, this.pathFromRoot.prepend(parent));
        }
        if (parent == this.root) {
            return new ResolveSource(this.root, new Node<Container>(parent));
        }
        if (ConfigImpl.traceSubstitutionsEnabled() && this.root.hasDescendant((AbstractConfigValue)parent)) {
            ConfigImpl.trace("***** BUG ***** tried to push parent " + parent + " without having a path to it in " + this);
        }
        return this;
    }
    
    ResolveSource resetParents() {
        if (this.pathFromRoot == null) {
            return this;
        }
        return new ResolveSource(this.root);
    }
    
    private static Node<Container> replace(final Node<Container> list, final Container old, final AbstractConfigValue replacement) {
        final Container child = list.head();
        if (child != old) {
            throw new ConfigException.BugOrBroken("Can only replace() the top node we're resolving; had " + child + " on top and tried to replace " + old + " overall list was " + list);
        }
        final Container parent = (list.tail() == null) ? null : list.tail().head();
        if (replacement == null || !(replacement instanceof Container)) {
            if (parent == null) {
                return null;
            }
            final AbstractConfigValue newParent = parent.replaceChild((AbstractConfigValue)old, null);
            return replace(list.tail(), parent, newParent);
        }
        else {
            if (parent == null) {
                return new Node<Container>((Container)replacement);
            }
            final AbstractConfigValue newParent = parent.replaceChild((AbstractConfigValue)old, replacement);
            final Node<Container> newTail = replace(list.tail(), parent, newParent);
            if (newTail != null) {
                return newTail.prepend((Container)replacement);
            }
            return new Node<Container>((Container)replacement);
        }
    }
    
    ResolveSource replaceCurrentParent(final Container old, final Container replacement) {
        if (ConfigImpl.traceSubstitutionsEnabled()) {
            ConfigImpl.trace("replaceCurrentParent old " + old + "@" + System.identityHashCode(old) + " replacement " + replacement + "@" + System.identityHashCode(old) + " in " + this);
        }
        if (old == replacement) {
            return this;
        }
        if (this.pathFromRoot != null) {
            final Node<Container> newPath = replace(this.pathFromRoot, old, (AbstractConfigValue)replacement);
            if (ConfigImpl.traceSubstitutionsEnabled()) {
                ConfigImpl.trace("replaced " + old + " with " + replacement + " in " + this);
                ConfigImpl.trace("path was: " + this.pathFromRoot + " is now " + newPath);
            }
            if (newPath != null) {
                return new ResolveSource(newPath.last(), newPath);
            }
            return new ResolveSource(SimpleConfigObject.empty());
        }
        else {
            if (old == this.root) {
                return new ResolveSource(this.rootMustBeObj(replacement));
            }
            throw new ConfigException.BugOrBroken("attempt to replace root " + this.root + " with " + replacement);
        }
    }
    
    ResolveSource replaceWithinCurrentParent(final AbstractConfigValue old, final AbstractConfigValue replacement) {
        if (ConfigImpl.traceSubstitutionsEnabled()) {
            ConfigImpl.trace("replaceWithinCurrentParent old " + old + "@" + System.identityHashCode(old) + " replacement " + replacement + "@" + System.identityHashCode(old) + " in " + this);
        }
        if (old == replacement) {
            return this;
        }
        if (this.pathFromRoot != null) {
            final Container parent = this.pathFromRoot.head();
            final AbstractConfigValue newParent = parent.replaceChild(old, replacement);
            return this.replaceCurrentParent(parent, (newParent instanceof Container) ? newParent : null);
        }
        if (old == this.root && replacement instanceof Container) {
            return new ResolveSource(this.rootMustBeObj((Container)replacement));
        }
        throw new ConfigException.BugOrBroken("replace in parent not possible " + old + " with " + replacement + " in " + this);
    }
    
    @Override
    public String toString() {
        return "ResolveSource(root=" + this.root + ", pathFromRoot=" + this.pathFromRoot + ")";
    }
    
    static final class Node<T>
    {
        final T value;
        final Node<T> next;
        
        Node(final T value, final Node<T> next) {
            this.value = value;
            this.next = next;
        }
        
        Node(final T value) {
            this(value, null);
        }
        
        Node<T> prepend(final T value) {
            return new Node<T>(value, this);
        }
        
        T head() {
            return this.value;
        }
        
        Node<T> tail() {
            return this.next;
        }
        
        T last() {
            Node<T> i;
            for (i = this; i.next != null; i = i.next) {}
            return i.value;
        }
        
        Node<T> reverse() {
            if (this.next == null) {
                return this;
            }
            Node<T> reversed = new Node<T>(this.value);
            for (Node<T> i = this.next; i != null; i = i.next) {
                reversed = reversed.prepend(i.value);
            }
            return reversed;
        }
        
        @Override
        public String toString() {
            final StringBuffer sb = new StringBuffer();
            sb.append("[");
            for (Node<T> toAppendValue = this.reverse(); toAppendValue != null; toAppendValue = toAppendValue.next) {
                sb.append(toAppendValue.value.toString());
                if (toAppendValue.next != null) {
                    sb.append(" <= ");
                }
            }
            sb.append("]");
            return sb.toString();
        }
    }
    
    static final class ValueWithPath
    {
        final AbstractConfigValue value;
        final Node<Container> pathFromRoot;
        
        ValueWithPath(final AbstractConfigValue value, final Node<Container> pathFromRoot) {
            this.value = value;
            this.pathFromRoot = pathFromRoot;
        }
        
        @Override
        public String toString() {
            return "ValueWithPath(value=" + this.value + ", pathFromRoot=" + this.pathFromRoot + ")";
        }
    }
    
    static final class ResultWithPath
    {
        final ResolveResult<? extends AbstractConfigValue> result;
        final Node<Container> pathFromRoot;
        
        ResultWithPath(final ResolveResult<? extends AbstractConfigValue> result, final Node<Container> pathFromRoot) {
            this.result = result;
            this.pathFromRoot = pathFromRoot;
        }
        
        @Override
        public String toString() {
            return "ResultWithPath(result=" + this.result + ", pathFromRoot=" + this.pathFromRoot + ")";
        }
    }
}
