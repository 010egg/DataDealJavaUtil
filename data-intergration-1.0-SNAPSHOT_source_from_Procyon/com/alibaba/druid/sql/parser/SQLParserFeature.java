// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.parser;

public enum SQLParserFeature
{
    KeepInsertValueClauseOriginalString, 
    KeepSelectListOriginalString, 
    UseInsertColumnsCache, 
    EnableSQLBinaryOpExprGroup, 
    OptimizedForParameterized, 
    OptimizedForForParameterizedSkipValue, 
    KeepComments, 
    SkipComments, 
    StrictForWall, 
    TDDLHint, 
    DRDSAsyncDDL, 
    DRDSBaseline, 
    InsertReader, 
    IgnoreNameQuotes, 
    KeepNameQuotes, 
    SelectItemGenerateAlias, 
    PipesAsConcat, 
    InsertValueCheckType, 
    InsertValueNative, 
    EnableCurrentTimeExpr, 
    EnableCurrentUserExpr, 
    KeepSourceLocation, 
    SupportUnicodeCodePoint, 
    PrintSQLWhileParsingFailed, 
    EnableMultiUnion, 
    Spark, 
    Presto;
    
    public final int mask;
    
    private SQLParserFeature() {
        this.mask = 1 << this.ordinal();
    }
    
    public static boolean isEnabled(final int features, final SQLParserFeature feature) {
        return (features & feature.mask) != 0x0;
    }
    
    public static int config(int features, final SQLParserFeature feature, final boolean state) {
        if (state) {
            features |= feature.mask;
        }
        else {
            features &= ~feature.mask;
        }
        return features;
    }
    
    public static int of(final SQLParserFeature... features) {
        if (features == null) {
            return 0;
        }
        int value = 0;
        for (final SQLParserFeature feature : features) {
            value |= feature.mask;
        }
        return value;
    }
}
