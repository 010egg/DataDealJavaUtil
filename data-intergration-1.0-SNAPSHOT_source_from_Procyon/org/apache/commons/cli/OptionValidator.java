// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.cli;

final class OptionValidator
{
    static void validateOption(final String opt) throws IllegalArgumentException {
        if (opt == null) {
            return;
        }
        if (opt.length() == 1) {
            final char ch = opt.charAt(0);
            if (!isValidOpt(ch)) {
                throw new IllegalArgumentException("Illegal option name '" + ch + "'");
            }
        }
        else {
            for (final char ch2 : opt.toCharArray()) {
                if (!isValidChar(ch2)) {
                    throw new IllegalArgumentException("The option '" + opt + "' contains an illegal character : '" + ch2 + "'");
                }
            }
        }
    }
    
    private static boolean isValidOpt(final char c) {
        return isValidChar(c) || c == '?' || c == '@';
    }
    
    private static boolean isValidChar(final char c) {
        return Character.isJavaIdentifierPart(c);
    }
}
