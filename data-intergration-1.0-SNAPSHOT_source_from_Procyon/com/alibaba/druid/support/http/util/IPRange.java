// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.http.util;

public class IPRange
{
    private IPAddress ipAddress;
    private IPAddress ipSubnetMask;
    private int extendedNetworkPrefix;
    
    public IPRange(final String range) {
        this.ipAddress = null;
        this.ipSubnetMask = null;
        this.extendedNetworkPrefix = 0;
        this.parseRange(range);
    }
    
    public final IPAddress getIPAddress() {
        return this.ipAddress;
    }
    
    public final IPAddress getIPSubnetMask() {
        return this.ipSubnetMask;
    }
    
    public final int getExtendedNetworkPrefix() {
        return this.extendedNetworkPrefix;
    }
    
    @Override
    public String toString() {
        return this.ipAddress.toString() + "/" + this.extendedNetworkPrefix;
    }
    
    final void parseRange(final String range) {
        if (range == null) {
            throw new IllegalArgumentException("Invalid IP range");
        }
        final int index = range.indexOf(47);
        String subnetStr = null;
        if (index == -1) {
            this.ipAddress = new IPAddress(range);
        }
        else {
            this.ipAddress = new IPAddress(range.substring(0, index));
            subnetStr = range.substring(index + 1);
        }
        try {
            if (subnetStr != null) {
                this.extendedNetworkPrefix = Integer.parseInt(subnetStr);
                if (this.extendedNetworkPrefix < 0 || this.extendedNetworkPrefix > 32) {
                    throw new IllegalArgumentException("Invalid IP range [" + range + "]");
                }
                this.ipSubnetMask = this.computeMaskFromNetworkPrefix(this.extendedNetworkPrefix);
            }
        }
        catch (NumberFormatException ex) {
            this.ipSubnetMask = new IPAddress(subnetStr);
            this.extendedNetworkPrefix = this.computeNetworkPrefixFromMask(this.ipSubnetMask);
            if (this.extendedNetworkPrefix == -1) {
                throw new IllegalArgumentException("Invalid IP range [" + range + "]", ex);
            }
        }
    }
    
    private int computeNetworkPrefixFromMask(final IPAddress mask) {
        int result = 0;
        int tmp;
        for (tmp = mask.getIPAddress(); (tmp & 0x1) == 0x1; tmp >>>= 1) {
            ++result;
        }
        if (tmp != 0) {
            return -1;
        }
        return result;
    }
    
    public static String toDecimalString(final String inBinaryIpAddress) {
        final StringBuilder decimalIp = new StringBuilder();
        final String[] binary = new String[4];
        for (int i = 0, c = 0; i < 32; i += 8, ++c) {
            binary[c] = inBinaryIpAddress.substring(i, i + 8);
            final int octet = Integer.parseInt(binary[c], 2);
            decimalIp.append(octet);
            if (c < 3) {
                decimalIp.append('.');
            }
        }
        return decimalIp.toString();
    }
    
    private IPAddress computeMaskFromNetworkPrefix(final int prefix) {
        final StringBuilder str = new StringBuilder();
        for (int i = 0; i < 32; ++i) {
            if (i < prefix) {
                str.append("1");
            }
            else {
                str.append("0");
            }
        }
        final String decimalString = toDecimalString(str.toString());
        return new IPAddress(decimalString);
    }
    
    public boolean isIPAddressInRange(final IPAddress address) {
        if (this.ipSubnetMask == null) {
            return this.ipAddress.equals(address);
        }
        final int result1 = address.getIPAddress() & this.ipSubnetMask.getIPAddress();
        final int result2 = this.ipAddress.getIPAddress() & this.ipSubnetMask.getIPAddress();
        return result1 == result2;
    }
}
