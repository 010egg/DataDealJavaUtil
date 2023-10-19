// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.visitor;

public enum VisitorFeature
{
    OutputUCase, 
    OutputPrettyFormat, 
    OutputParameterized, 
    OutputDesensitize, 
    OutputUseInsertValueClauseOriginalString, 
    OutputSkipSelectListCacheString, 
    OutputSkipInsertColumnsString, 
    OutputParameterizedQuesUnMergeInList, 
    OutputParameterizedQuesUnMergeOr, 
    OutputParameterizedQuesUnMergeAnd, 
    OutputParameterizedQuesUnMergeValuesList, 
    OutputParameterizedUnMergeShardingTable, 
    OutputParameterizedZeroReplaceNotUseOriginalSql, 
    OutputRegForPresto, 
    @Deprecated
    OutputKeepParenthesisWhenNotExpr, 
    OutputNameQuote, 
    OutputDistributedLiteralInCreateTableStmt;
    
    public final int mask;
    
    private VisitorFeature() {
        this.mask = 1 << this.ordinal();
    }
    
    public static boolean isEnabled(final int features, final VisitorFeature feature) {
        return (features & feature.mask) != 0x0;
    }
    
    public static int config(int features, final VisitorFeature feature, final boolean state) {
        if (state) {
            features |= feature.mask;
        }
        else {
            features &= ~feature.mask;
        }
        return features;
    }
    
    public static int of(final VisitorFeature... features) {
        if (features == null) {
            return 0;
        }
        int value = 0;
        for (final VisitorFeature feature : features) {
            value |= feature.mask;
        }
        return value;
    }
}
