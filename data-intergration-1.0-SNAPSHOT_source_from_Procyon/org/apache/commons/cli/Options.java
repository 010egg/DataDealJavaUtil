// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.cli;

import java.util.Collections;
import java.util.HashSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.io.Serializable;

public class Options implements Serializable
{
    private static final long serialVersionUID = 1L;
    private final Map<String, Option> shortOpts;
    private final Map<String, Option> longOpts;
    private final List<Object> requiredOpts;
    private final Map<String, OptionGroup> optionGroups;
    
    public Options() {
        this.shortOpts = new LinkedHashMap<String, Option>();
        this.longOpts = new LinkedHashMap<String, Option>();
        this.requiredOpts = new ArrayList<Object>();
        this.optionGroups = new LinkedHashMap<String, OptionGroup>();
    }
    
    public Options addOptionGroup(final OptionGroup group) {
        if (group.isRequired()) {
            this.requiredOpts.add(group);
        }
        for (final Option option : group.getOptions()) {
            option.setRequired(false);
            this.addOption(option);
            this.optionGroups.put(option.getKey(), group);
        }
        return this;
    }
    
    Collection<OptionGroup> getOptionGroups() {
        return new HashSet<OptionGroup>(this.optionGroups.values());
    }
    
    public Options addOption(final String opt, final String description) {
        this.addOption(opt, null, false, description);
        return this;
    }
    
    public Options addOption(final String opt, final boolean hasArg, final String description) {
        this.addOption(opt, null, hasArg, description);
        return this;
    }
    
    public Options addOption(final String opt, final String longOpt, final boolean hasArg, final String description) {
        this.addOption(new Option(opt, longOpt, hasArg, description));
        return this;
    }
    
    public Options addRequiredOption(final String opt, final String longOpt, final boolean hasArg, final String description) {
        final Option option = new Option(opt, longOpt, hasArg, description);
        option.setRequired(true);
        this.addOption(option);
        return this;
    }
    
    public Options addOption(final Option opt) {
        final String key = opt.getKey();
        if (opt.hasLongOpt()) {
            this.longOpts.put(opt.getLongOpt(), opt);
        }
        if (opt.isRequired()) {
            if (this.requiredOpts.contains(key)) {
                this.requiredOpts.remove(this.requiredOpts.indexOf(key));
            }
            this.requiredOpts.add(key);
        }
        this.shortOpts.put(key, opt);
        return this;
    }
    
    public Collection<Option> getOptions() {
        return Collections.unmodifiableCollection((Collection<? extends Option>)this.helpOptions());
    }
    
    List<Option> helpOptions() {
        return new ArrayList<Option>(this.shortOpts.values());
    }
    
    public List getRequiredOptions() {
        return Collections.unmodifiableList((List<?>)this.requiredOpts);
    }
    
    public Option getOption(String opt) {
        opt = Util.stripLeadingHyphens(opt);
        if (this.shortOpts.containsKey(opt)) {
            return this.shortOpts.get(opt);
        }
        return this.longOpts.get(opt);
    }
    
    public List<String> getMatchingOptions(String opt) {
        opt = Util.stripLeadingHyphens(opt);
        final List<String> matchingOpts = new ArrayList<String>();
        if (this.longOpts.keySet().contains(opt)) {
            return Collections.singletonList(opt);
        }
        for (final String longOpt : this.longOpts.keySet()) {
            if (longOpt.startsWith(opt)) {
                matchingOpts.add(longOpt);
            }
        }
        return matchingOpts;
    }
    
    public boolean hasOption(String opt) {
        opt = Util.stripLeadingHyphens(opt);
        return this.shortOpts.containsKey(opt) || this.longOpts.containsKey(opt);
    }
    
    public boolean hasLongOption(String opt) {
        opt = Util.stripLeadingHyphens(opt);
        return this.longOpts.containsKey(opt);
    }
    
    public boolean hasShortOption(String opt) {
        opt = Util.stripLeadingHyphens(opt);
        return this.shortOpts.containsKey(opt);
    }
    
    public OptionGroup getOptionGroup(final Option opt) {
        return this.optionGroups.get(opt.getKey());
    }
    
    @Override
    public String toString() {
        final StringBuilder buf = new StringBuilder();
        buf.append("[ Options: [ short ");
        buf.append(this.shortOpts.toString());
        buf.append(" ] [ long ");
        buf.append(this.longOpts);
        buf.append(" ]");
        return buf.toString();
    }
}
