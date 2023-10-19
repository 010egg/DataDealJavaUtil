// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.http.util;

import java.util.NoSuchElementException;

public class IPAddress implements Cloneable
{
    protected int ipAddress;
    
    public IPAddress(final String ipAddressStr) {
        this.ipAddress = 0;
        this.ipAddress = this.parseIPAddress(ipAddressStr);
    }
    
    public IPAddress(final int address) {
        this.ipAddress = 0;
        this.ipAddress = address;
    }
    
    public final int getIPAddress() {
        return this.ipAddress;
    }
    
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        int temp = this.ipAddress & 0xFF;
        result.append(temp);
        result.append(".");
        temp = (this.ipAddress >> 8 & 0xFF);
        result.append(temp);
        result.append(".");
        temp = (this.ipAddress >> 16 & 0xFF);
        result.append(temp);
        result.append(".");
        temp = (this.ipAddress >> 24 & 0xFF);
        result.append(temp);
        return result.toString();
    }
    
    public final boolean isClassA() {
        return (this.ipAddress & 0x1) == 0x0;
    }
    
    public final boolean isClassB() {
        return (this.ipAddress & 0x3) == 0x1;
    }
    
    public final boolean isClassC() {
        return (this.ipAddress & 0x7) == 0x3;
    }
    
    final int parseIPAddress(final String ipAddressStr) {
        int result = 0;
        if (ipAddressStr == null) {
            throw new IllegalArgumentException();
        }
        try {
            String tmp = ipAddressStr;
            int offset = 0;
            for (int i = 0; i < 3; ++i) {
                final int index = tmp.indexOf(46);
                if (index == -1) {
                    throw new IllegalArgumentException("Invalid IP Address [" + ipAddressStr + "]");
                }
                final String numberStr = tmp.substring(0, index);
                final int number = Integer.parseInt(numberStr);
                if (number < 0 || number > 255) {
                    throw new IllegalArgumentException("Invalid IP Address [" + ipAddressStr + "]");
                }
                result += number << offset;
                offset += 8;
                tmp = tmp.substring(index + 1);
            }
            if (tmp.length() <= 0) {
                throw new IllegalArgumentException("Invalid IP Address [" + ipAddressStr + "]");
            }
            final int number2 = Integer.parseInt(tmp);
            if (number2 < 0 || number2 > 255) {
                throw new IllegalArgumentException("Invalid IP Address [" + ipAddressStr + "]");
            }
            result += number2 << offset;
            this.ipAddress = result;
        }
        catch (NoSuchElementException ex) {
            throw new IllegalArgumentException("Invalid IP Address [" + ipAddressStr + "]", ex);
        }
        catch (NumberFormatException ex2) {
            throw new IllegalArgumentException("Invalid IP Address [" + ipAddressStr + "]", ex2);
        }
        return result;
    }
    
    @Override
    public int hashCode() {
        return this.ipAddress;
    }
    
    @Override
    public boolean equals(final Object another) {
        return another instanceof IPAddress && this.ipAddress == ((IPAddress)another).ipAddress;
    }
}
