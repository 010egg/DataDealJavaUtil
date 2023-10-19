// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.csv;

public enum UnescapedQuoteHandling
{
    STOP_AT_CLOSING_QUOTE, 
    STOP_AT_DELIMITER, 
    SKIP_VALUE, 
    RAISE_ERROR;
}
