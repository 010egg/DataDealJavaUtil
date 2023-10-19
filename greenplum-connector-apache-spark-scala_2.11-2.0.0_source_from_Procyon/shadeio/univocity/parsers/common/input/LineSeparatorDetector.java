// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common.input;

abstract class LineSeparatorDetector implements InputAnalysisProcess
{
    @Override
    public void execute(final char[] characters, final int length) {
        char separator1 = '\0';
        char separator2 = '\0';
        for (final char ch : characters) {
            if (ch == '\n' || ch == '\r') {
                if (separator1 != '\0') {
                    separator2 = ch;
                    break;
                }
                separator1 = ch;
            }
            else if (separator1 != '\0') {
                break;
            }
        }
        char lineSeparator1 = separator1;
        char lineSeparator2 = separator2;
        if (separator1 != '\0') {
            if (separator1 == '\n') {
                lineSeparator1 = '\n';
                lineSeparator2 = '\0';
            }
            else {
                lineSeparator1 = '\r';
                if (separator2 == '\n') {
                    lineSeparator2 = '\n';
                }
                else {
                    lineSeparator2 = '\0';
                }
            }
        }
        this.apply(lineSeparator1, lineSeparator2);
    }
    
    protected abstract void apply(final char p0, final char p1);
}
