// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.xa;

import org.postgresql.util.Base64;
import java.util.Arrays;
import javax.transaction.xa.Xid;

class RecoveredXid implements Xid
{
    int formatId;
    byte[] globalTransactionId;
    byte[] branchQualifier;
    
    @Override
    public int getFormatId() {
        return this.formatId;
    }
    
    @Override
    public byte[] getGlobalTransactionId() {
        return this.globalTransactionId;
    }
    
    @Override
    public byte[] getBranchQualifier() {
        return this.branchQualifier;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + Arrays.hashCode(this.branchQualifier);
        result = 31 * result + this.formatId;
        result = 31 * result + Arrays.hashCode(this.globalTransactionId);
        return result;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Xid)) {
            return false;
        }
        final Xid other = (Xid)o;
        return other.getFormatId() == this.formatId && Arrays.equals(this.globalTransactionId, other.getGlobalTransactionId()) && Arrays.equals(this.branchQualifier, other.getBranchQualifier());
    }
    
    @Override
    public String toString() {
        return xidToString(this);
    }
    
    static String xidToString(final Xid xid) {
        return xid.getFormatId() + "_" + Base64.encodeBytes(xid.getGlobalTransactionId(), 8) + "_" + Base64.encodeBytes(xid.getBranchQualifier(), 8);
    }
    
    static Xid stringToXid(final String s) {
        final RecoveredXid xid = new RecoveredXid();
        final int a = s.indexOf("_");
        final int b = s.lastIndexOf("_");
        if (a == b) {
            return null;
        }
        try {
            xid.formatId = Integer.parseInt(s.substring(0, a));
            xid.globalTransactionId = Base64.decode(s.substring(a + 1, b));
            xid.branchQualifier = Base64.decode(s.substring(b + 1));
            if (xid.globalTransactionId == null || xid.branchQualifier == null) {
                return null;
            }
        }
        catch (Exception ex) {
            return null;
        }
        return xid;
    }
}
