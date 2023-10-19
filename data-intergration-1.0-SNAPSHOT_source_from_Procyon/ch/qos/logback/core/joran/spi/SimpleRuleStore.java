// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.joran.spi;

import java.util.Iterator;
import ch.qos.logback.core.util.OptionHelper;
import java.util.ArrayList;
import ch.qos.logback.core.Context;
import ch.qos.logback.core.joran.action.Action;
import java.util.List;
import java.util.HashMap;
import ch.qos.logback.core.spi.ContextAwareBase;

public class SimpleRuleStore extends ContextAwareBase implements RuleStore
{
    static String KLEENE_STAR;
    HashMap<ElementSelector, List<Action>> rules;
    
    public SimpleRuleStore(final Context context) {
        this.rules = new HashMap<ElementSelector, List<Action>>();
        this.setContext(context);
    }
    
    @Override
    public void addRule(final ElementSelector elementSelector, final Action action) {
        action.setContext(this.context);
        List<Action> a4p = this.rules.get(elementSelector);
        if (a4p == null) {
            a4p = new ArrayList<Action>();
            this.rules.put(elementSelector, a4p);
        }
        a4p.add(action);
    }
    
    @Override
    public void addRule(final ElementSelector elementSelector, final String actionClassName) {
        Action action = null;
        try {
            action = (Action)OptionHelper.instantiateByClassName(actionClassName, Action.class, this.context);
        }
        catch (Exception e) {
            this.addError("Could not instantiate class [" + actionClassName + "]", e);
        }
        if (action != null) {
            this.addRule(elementSelector, action);
        }
    }
    
    @Override
    public List<Action> matchActions(final ElementPath elementPath) {
        List<Action> actionList;
        if ((actionList = this.fullPathMatch(elementPath)) != null) {
            return actionList;
        }
        if ((actionList = this.suffixMatch(elementPath)) != null) {
            return actionList;
        }
        if ((actionList = this.prefixMatch(elementPath)) != null) {
            return actionList;
        }
        if ((actionList = this.middleMatch(elementPath)) != null) {
            return actionList;
        }
        return null;
    }
    
    List<Action> fullPathMatch(final ElementPath elementPath) {
        for (final ElementSelector selector : this.rules.keySet()) {
            if (selector.fullPathMatch(elementPath)) {
                return this.rules.get(selector);
            }
        }
        return null;
    }
    
    List<Action> suffixMatch(final ElementPath elementPath) {
        int max = 0;
        ElementSelector longestMatchingElementSelector = null;
        for (final ElementSelector selector : this.rules.keySet()) {
            if (this.isSuffixPattern(selector)) {
                final int r = selector.getTailMatchLength(elementPath);
                if (r <= max) {
                    continue;
                }
                max = r;
                longestMatchingElementSelector = selector;
            }
        }
        if (longestMatchingElementSelector != null) {
            return this.rules.get(longestMatchingElementSelector);
        }
        return null;
    }
    
    private boolean isSuffixPattern(final ElementSelector p) {
        return p.size() > 1 && p.get(0).equals(SimpleRuleStore.KLEENE_STAR);
    }
    
    List<Action> prefixMatch(final ElementPath elementPath) {
        int max = 0;
        ElementSelector longestMatchingElementSelector = null;
        for (final ElementSelector selector : this.rules.keySet()) {
            final String last = selector.peekLast();
            if (this.isKleeneStar(last)) {
                final int r = selector.getPrefixMatchLength(elementPath);
                if (r != selector.size() - 1 || r <= max) {
                    continue;
                }
                max = r;
                longestMatchingElementSelector = selector;
            }
        }
        if (longestMatchingElementSelector != null) {
            return this.rules.get(longestMatchingElementSelector);
        }
        return null;
    }
    
    private boolean isKleeneStar(final String last) {
        return SimpleRuleStore.KLEENE_STAR.equals(last);
    }
    
    List<Action> middleMatch(final ElementPath path) {
        int max = 0;
        ElementSelector longestMatchingElementSelector = null;
        for (final ElementSelector selector : this.rules.keySet()) {
            final String last = selector.peekLast();
            String first = null;
            if (selector.size() > 1) {
                first = selector.get(0);
            }
            if (this.isKleeneStar(last) && this.isKleeneStar(first)) {
                final List<String> copyOfPartList = selector.getCopyOfPartList();
                if (copyOfPartList.size() > 2) {
                    copyOfPartList.remove(0);
                    copyOfPartList.remove(copyOfPartList.size() - 1);
                }
                int r = 0;
                final ElementSelector clone = new ElementSelector(copyOfPartList);
                if (clone.isContainedIn(path)) {
                    r = clone.size();
                }
                if (r <= max) {
                    continue;
                }
                max = r;
                longestMatchingElementSelector = selector;
            }
        }
        if (longestMatchingElementSelector != null) {
            return this.rules.get(longestMatchingElementSelector);
        }
        return null;
    }
    
    @Override
    public String toString() {
        final String TAB = "  ";
        final StringBuilder retValue = new StringBuilder();
        retValue.append("SimpleRuleStore ( ").append("rules = ").append(this.rules).append("  ").append(" )");
        return retValue.toString();
    }
    
    static {
        SimpleRuleStore.KLEENE_STAR = "*";
    }
}
