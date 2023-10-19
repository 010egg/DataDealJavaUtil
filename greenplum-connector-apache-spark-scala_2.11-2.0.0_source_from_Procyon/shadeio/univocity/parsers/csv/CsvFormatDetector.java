// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.csv;

import java.util.Set;
import java.util.Collection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import shadeio.univocity.parsers.common.input.InputAnalysisProcess;

abstract class CsvFormatDetector implements InputAnalysisProcess
{
    private final int MAX_ROW_SAMPLES;
    private final char comment;
    private final char suggestedDelimiter;
    private final char normalizedNewLine;
    private final int whitespaceRangeStart;
    private char[] allowedDelimiters;
    
    CsvFormatDetector(final int maxRowSamples, final CsvParserSettings settings, final int whitespaceRangeStart) {
        this.MAX_ROW_SAMPLES = maxRowSamples;
        this.whitespaceRangeStart = whitespaceRangeStart;
        this.allowedDelimiters = settings.getDelimitersForDetection();
        if (this.allowedDelimiters != null && this.allowedDelimiters.length > 0) {
            this.suggestedDelimiter = this.allowedDelimiters[0];
        }
        else {
            this.suggestedDelimiter = settings.getFormat().getDelimiter();
            this.allowedDelimiters = new char[0];
        }
        this.normalizedNewLine = settings.getFormat().getNormalizedNewline();
        this.comment = settings.getFormat().getComment();
    }
    
    private Map<Character, Integer> calculateTotals(final List<Map<Character, Integer>> symbolsPerRow) {
        final Map<Character, Integer> out = new HashMap<Character, Integer>();
        for (final Map<Character, Integer> rowStats : symbolsPerRow) {
            for (final Map.Entry<Character, Integer> symbolStats : rowStats.entrySet()) {
                final Character symbol = symbolStats.getKey();
                final Integer count = symbolStats.getValue();
                Integer total = out.get(symbol);
                if (total == null) {
                    total = 0;
                }
                out.put(symbol, total + count);
            }
        }
        return out;
    }
    
    @Override
    public void execute(final char[] characters, final int length) {
        final Set<Character> allSymbols = new HashSet<Character>();
        Map<Character, Integer> symbols = new HashMap<Character, Integer>();
        final Map<Character, Integer> escape = new HashMap<Character, Integer>();
        final List<Map<Character, Integer>> symbolsPerRow = new ArrayList<Map<Character, Integer>>();
        int doubleQuoteCount = 0;
        int singleQuoteCount = 0;
        char inQuote = '\0';
        boolean afterNewLine = true;
        int i;
        for (i = 0; i < length; ++i) {
            char ch = characters[i];
            if (afterNewLine && ch == this.comment) {
                while (++i < length) {
                    ch = characters[i];
                    if (ch == '\r' || ch == '\n') {
                        break;
                    }
                    if (ch == this.normalizedNewLine) {
                        break;
                    }
                }
            }
            else if (ch == '\"' || ch == '\'') {
                if (inQuote == ch) {
                    if (ch == '\"') {
                        ++doubleQuoteCount;
                    }
                    else {
                        ++singleQuoteCount;
                    }
                    if (i + 1 < length) {
                        final char next = characters[i + 1];
                        if (Character.isLetterOrDigit(next) || (next <= ' ' && this.whitespaceRangeStart < next && next != '\n' && next != '\r')) {
                            final char prev = characters[i - 1];
                            if (!Character.isLetterOrDigit(prev)) {
                                increment(escape, prev);
                            }
                        }
                    }
                    inQuote = '\0';
                }
                else if (inQuote == '\0') {
                    char prev2;
                    int j;
                    for (prev2 = '\0', j = i; prev2 <= ' ' && --j >= 0; prev2 = characters[j]) {}
                    if (j < 0 || !Character.isLetterOrDigit(prev2)) {
                        inQuote = ch;
                    }
                }
            }
            else if (inQuote == '\0') {
                afterNewLine = false;
                if (isSymbol(ch)) {
                    allSymbols.add(ch);
                    increment(symbols, ch);
                }
                else if ((ch == '\r' || ch == '\n' || ch == this.normalizedNewLine) && symbols.size() > 0) {
                    afterNewLine = true;
                    symbolsPerRow.add(symbols);
                    if (symbolsPerRow.size() == this.MAX_ROW_SAMPLES) {
                        break;
                    }
                    symbols = new HashMap<Character, Integer>();
                }
            }
        }
        if (symbols.size() > 0 && length < characters.length) {
            symbolsPerRow.add(symbols);
        }
        if (length >= characters.length && i >= length && symbolsPerRow.size() > 1) {
            symbolsPerRow.remove(symbolsPerRow.size() - 1);
        }
        final Map<Character, Integer> totals = this.calculateTotals(symbolsPerRow);
        final Map<Character, Integer> sums = new HashMap<Character, Integer>();
        final Set<Character> toRemove = new HashSet<Character>();
        for (final Map<Character, Integer> previous : symbolsPerRow) {
            for (final Map<Character, Integer> current : symbolsPerRow) {
                for (final Character symbol : allSymbols) {
                    final Integer previousCount = previous.get(symbol);
                    final Integer currentCount = current.get(symbol);
                    if (previousCount == null && currentCount == null) {
                        toRemove.add(symbol);
                    }
                    if (previousCount != null) {
                        if (currentCount == null) {
                            continue;
                        }
                        increment(sums, symbol, Math.abs(previousCount - currentCount));
                    }
                }
            }
        }
        sums.keySet().removeAll(toRemove);
        if (this.allowedDelimiters.length > 0) {
            final Set<Character> toRetain = new HashSet<Character>();
            for (final char c : this.allowedDelimiters) {
                toRetain.add(c);
            }
            sums.keySet().retainAll(toRetain);
        }
        final char delimiterMax = max(sums, totals, this.suggestedDelimiter);
        final char delimiterMin = min(sums, totals, this.suggestedDelimiter);
        char delimiter = '\0';
        Label_0985: {
            if (delimiterMax != delimiterMin) {
                if (sums.get(delimiterMin) == 0 && sums.get(delimiterMax) != 0) {
                    delimiter = delimiterMin;
                }
                else {
                    for (final char c2 : this.allowedDelimiters) {
                        if (c2 == delimiterMin) {
                            delimiter = delimiterMin;
                            break Label_0985;
                        }
                        if (c2 == delimiterMax) {
                            delimiter = delimiterMax;
                            break Label_0985;
                        }
                    }
                    if (totals.get(delimiterMin) > totals.get(delimiterMax)) {
                        delimiter = delimiterMin;
                    }
                    else {
                        delimiter = delimiterMax;
                    }
                }
            }
            else {
                delimiter = delimiterMax;
            }
        }
        final char quote = (doubleQuoteCount >= singleQuoteCount) ? '\"' : '\'';
        escape.remove(delimiter);
        final char quoteEscape = max(escape, totals, quote);
        this.apply(delimiter, quote, quoteEscape);
    }
    
    private static void increment(final Map<Character, Integer> map, final char symbol) {
        increment(map, symbol, 1);
    }
    
    private static void increment(final Map<Character, Integer> map, final char symbol, final int incrementSize) {
        Integer count = map.get(symbol);
        if (count == null) {
            count = 0;
        }
        map.put(symbol, count + incrementSize);
    }
    
    private static char min(final Map<Character, Integer> map, final Map<Character, Integer> totals, final char defaultChar) {
        return getChar(map, totals, defaultChar, true);
    }
    
    private static char max(final Map<Character, Integer> map, final Map<Character, Integer> totals, final char defaultChar) {
        return getChar(map, totals, defaultChar, false);
    }
    
    private static char getChar(final Map<Character, Integer> map, final Map<Character, Integer> totals, char defaultChar, final boolean min) {
        int val = min ? Integer.MAX_VALUE : Integer.MIN_VALUE;
        for (final Map.Entry<Character, Integer> e : map.entrySet()) {
            final int sum = e.getValue();
            if ((min && sum <= val) || (!min && sum >= val)) {
                final char newChar = e.getKey();
                if (val == sum) {
                    final Integer currentTotal = totals.get(defaultChar);
                    final Integer newTotal = totals.get(newChar);
                    if (currentTotal != null && newTotal != null) {
                        if ((min || newTotal >= currentTotal) && (min || newTotal <= currentTotal)) {
                            continue;
                        }
                        defaultChar = newChar;
                    }
                    else {
                        if (!isSymbol(newChar)) {
                            continue;
                        }
                        defaultChar = newChar;
                    }
                }
                else {
                    val = sum;
                    defaultChar = newChar;
                }
            }
        }
        return defaultChar;
    }
    
    private static boolean isSymbol(final char ch) {
        return !Character.isLetterOrDigit(ch) && (ch == '\t' || ch > ' ');
    }
    
    abstract void apply(final char p0, final char p1, final char p2);
}
