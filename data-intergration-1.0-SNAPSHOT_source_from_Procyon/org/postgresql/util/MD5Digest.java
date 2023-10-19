// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.util;

import java.security.MessageDigest;

public class MD5Digest
{
    private MD5Digest() {
    }
    
    public static byte[] encode(final byte[] user, final byte[] password, final byte[] salt) {
        final byte[] hex_digest = new byte[35];
        try {
            final MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password);
            md.update(user);
            final byte[] temp_digest = md.digest();
            bytesToHex(temp_digest, hex_digest, 0);
            md.update(hex_digest, 0, 32);
            md.update(salt);
            final byte[] pass_digest = md.digest();
            bytesToHex(pass_digest, hex_digest, 3);
            hex_digest[0] = 109;
            hex_digest[1] = 100;
            hex_digest[2] = 53;
        }
        catch (Exception ex) {}
        return hex_digest;
    }
    
    private static void bytesToHex(final byte[] bytes, final byte[] hex, final int offset) {
        final char[] lookup = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        int pos = offset;
        for (int i = 0; i < 16; ++i) {
            final int c = bytes[i] & 0xFF;
            int j = c >> 4;
            hex[pos++] = (byte)lookup[j];
            j = (c & 0xF);
            hex[pos++] = (byte)lookup[j];
        }
    }
}
