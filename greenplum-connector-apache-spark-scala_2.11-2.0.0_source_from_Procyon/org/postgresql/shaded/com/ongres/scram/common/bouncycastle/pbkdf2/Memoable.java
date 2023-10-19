// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.shaded.com.ongres.scram.common.bouncycastle.pbkdf2;

public interface Memoable
{
    Memoable copy();
    
    void reset(final Memoable p0);
}
