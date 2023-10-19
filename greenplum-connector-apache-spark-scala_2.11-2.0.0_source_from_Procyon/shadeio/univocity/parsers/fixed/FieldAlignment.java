// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.fixed;

public enum FieldAlignment
{
    LEFT, 
    CENTER, 
    RIGHT;
    
    public int calculatePadding(final int totalLength, final int lengthToWrite) {
        if (this == FieldAlignment.LEFT || totalLength <= lengthToWrite) {
            return 0;
        }
        if (this == FieldAlignment.RIGHT) {
            return totalLength - lengthToWrite;
        }
        int padding = totalLength / 2 - lengthToWrite / 2;
        if (lengthToWrite + padding > totalLength) {
            --padding;
        }
        return padding;
    }
}
