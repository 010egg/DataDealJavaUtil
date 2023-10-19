// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.cli;

import java.util.Iterator;
import java.util.Collection;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

@Deprecated
public class PosixParser extends Parser
{
    private final List<String> tokens;
    private boolean eatTheRest;
    private Option currentOption;
    private Options options;
    
    public PosixParser() {
        this.tokens = new ArrayList<String>();
    }
    
    private void init() {
        this.eatTheRest = false;
        this.tokens.clear();
    }
    
    @Override
    protected String[] flatten(final Options options, final String[] arguments, final boolean stopAtNonOption) throws ParseException {
        this.init();
        this.options = options;
        final Iterator<String> iter = Arrays.asList(arguments).iterator();
        while (iter.hasNext()) {
            final String token = iter.next();
            if ("-".equals(token) || "--".equals(token)) {
                this.tokens.add(token);
            }
            else if (token.startsWith("--")) {
                final int pos = token.indexOf(61);
                final String opt = (pos == -1) ? token : token.substring(0, pos);
                final List<String> matchingOpts = options.getMatchingOptions(opt);
                if (matchingOpts.isEmpty()) {
                    this.processNonOptionToken(token, stopAtNonOption);
                }
                else {
                    if (matchingOpts.size() > 1) {
                        throw new AmbiguousOptionException(opt, matchingOpts);
                    }
                    this.currentOption = options.getOption(matchingOpts.get(0));
                    this.tokens.add("--" + this.currentOption.getLongOpt());
                    if (pos != -1) {
                        this.tokens.add(token.substring(pos + 1));
                    }
                }
            }
            else if (token.startsWith("-")) {
                if (token.length() == 2 || options.hasOption(token)) {
                    this.processOptionToken(token, stopAtNonOption);
                }
                else if (!options.getMatchingOptions(token).isEmpty()) {
                    final List<String> matchingOpts2 = options.getMatchingOptions(token);
                    if (matchingOpts2.size() > 1) {
                        throw new AmbiguousOptionException(token, matchingOpts2);
                    }
                    final Option opt2 = options.getOption(matchingOpts2.get(0));
                    this.processOptionToken("-" + opt2.getLongOpt(), stopAtNonOption);
                }
                else {
                    this.burstToken(token, stopAtNonOption);
                }
            }
            else {
                this.processNonOptionToken(token, stopAtNonOption);
            }
            this.gobble(iter);
        }
        return this.tokens.toArray(new String[this.tokens.size()]);
    }
    
    private void gobble(final Iterator<String> iter) {
        if (this.eatTheRest) {
            while (iter.hasNext()) {
                this.tokens.add(iter.next());
            }
        }
    }
    
    private void processNonOptionToken(final String value, final boolean stopAtNonOption) {
        if (stopAtNonOption && (this.currentOption == null || !this.currentOption.hasArg())) {
            this.eatTheRest = true;
            this.tokens.add("--");
        }
        this.tokens.add(value);
    }
    
    private void processOptionToken(final String token, final boolean stopAtNonOption) {
        if (stopAtNonOption && !this.options.hasOption(token)) {
            this.eatTheRest = true;
        }
        if (this.options.hasOption(token)) {
            this.currentOption = this.options.getOption(token);
        }
        this.tokens.add(token);
    }
    
    protected void burstToken(final String token, final boolean stopAtNonOption) {
        int i = 1;
        while (i < token.length()) {
            final String ch = String.valueOf(token.charAt(i));
            if (this.options.hasOption(ch)) {
                this.tokens.add("-" + ch);
                this.currentOption = this.options.getOption(ch);
                if (this.currentOption.hasArg() && token.length() != i + 1) {
                    this.tokens.add(token.substring(i + 1));
                    break;
                }
                ++i;
            }
            else {
                if (stopAtNonOption) {
                    this.processNonOptionToken(token.substring(i), true);
                    break;
                }
                this.tokens.add(token);
                break;
            }
        }
    }
}
