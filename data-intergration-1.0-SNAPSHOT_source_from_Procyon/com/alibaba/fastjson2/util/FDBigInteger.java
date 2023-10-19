// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.util;

import java.util.Arrays;

public final class FDBigInteger
{
    private static final int[] SMALL_5_POW;
    private static final int MAX_FIVE_POW = 340;
    private static final FDBigInteger[] POW_5_CACHE;
    private int[] data;
    private int offset;
    private int nWords;
    private boolean isImmutable;
    
    private FDBigInteger(final int[] data, final int offset) {
        this.data = data;
        this.offset = offset;
        this.nWords = data.length;
        this.trimLeadingZeros();
    }
    
    public FDBigInteger(final long lValue, final char[] digits, final int kDigits, final int nDigits) {
        final int n = Math.max((nDigits + 8) / 9, 2);
        (this.data = new int[n])[0] = (int)lValue;
        this.data[1] = (int)(lValue >>> 32);
        this.offset = 0;
        this.nWords = 2;
        int i = kDigits;
        final int limit = nDigits - 5;
        while (i < limit) {
            int ilim;
            int v;
            for (ilim = i + 5, v = digits[i++] - '0'; i < ilim; v = 10 * v + digits[i++] - 48) {}
            this.multAddMe(100000, v);
        }
        int v;
        int factor;
        for (factor = 1, v = 0; i < nDigits; v = 10 * v + digits[i++] - 48, factor *= 10) {}
        if (factor != 1) {
            this.multAddMe(factor, v);
        }
        this.trimLeadingZeros();
    }
    
    public void makeImmutable() {
        this.isImmutable = true;
    }
    
    private void multAddMe(final int iv, final int addend) {
        final long v = (long)iv & 0xFFFFFFFFL;
        long p = v * ((long)this.data[0] & 0xFFFFFFFFL) + ((long)addend & 0xFFFFFFFFL);
        this.data[0] = (int)p;
        p >>>= 32;
        for (int i = 1; i < this.nWords; ++i) {
            p += v * ((long)this.data[i] & 0xFFFFFFFFL);
            this.data[i] = (int)p;
            p >>>= 32;
        }
        if (p != 0L) {
            this.data[this.nWords++] = (int)p;
        }
    }
    
    private void trimLeadingZeros() {
        int i = this.nWords;
        if (i > 0 && this.data[--i] == 0) {
            while (i > 0 && this.data[i - 1] == 0) {
                --i;
            }
            if ((this.nWords = i) == 0) {
                this.offset = 0;
            }
        }
    }
    
    private int size() {
        return this.nWords + this.offset;
    }
    
    public FDBigInteger multByPow52(final int p5, final int p2) {
        if (this.nWords == 0) {
            return this;
        }
        FDBigInteger res = this;
        if (p5 != 0) {
            final int extraSize = (p2 != 0) ? 1 : 0;
            if (p5 < FDBigInteger.SMALL_5_POW.length) {
                final int[] r = new int[this.nWords + 1 + extraSize];
                mult(this.data, this.nWords, FDBigInteger.SMALL_5_POW[p5], r);
                res = new FDBigInteger(r, this.offset);
            }
            else {
                final FDBigInteger pow5 = big5pow(p5);
                final int[] r = new int[this.nWords + pow5.size() + extraSize];
                mult(this.data, this.nWords, pow5.data, pow5.nWords, r);
                res = new FDBigInteger(r, this.offset + pow5.offset);
            }
        }
        return res.leftShift(p2);
    }
    
    private static FDBigInteger big5pow(final int p) {
        assert p >= 0 : p;
        if (p < 340) {
            return FDBigInteger.POW_5_CACHE[p];
        }
        return big5powRec(p);
    }
    
    private FDBigInteger mult(final int i) {
        if (this.nWords == 0) {
            return this;
        }
        final int[] r = new int[this.nWords + 1];
        mult(this.data, this.nWords, i, r);
        return new FDBigInteger(r, this.offset);
    }
    
    private FDBigInteger mult(final FDBigInteger other) {
        if (this.nWords == 0) {
            return this;
        }
        if (this.size() == 1) {
            return other.mult(this.data[0]);
        }
        if (other.nWords == 0) {
            return other;
        }
        if (other.size() == 1) {
            return this.mult(other.data[0]);
        }
        final int[] r = new int[this.nWords + other.nWords];
        mult(this.data, this.nWords, other.data, other.nWords, r);
        return new FDBigInteger(r, this.offset + other.offset);
    }
    
    private static void mult(final int[] src, final int srcLen, final int value, final int[] dst) {
        final long val = (long)value & 0xFFFFFFFFL;
        long carry = 0L;
        for (int i = 0; i < srcLen; ++i) {
            final long product = ((long)src[i] & 0xFFFFFFFFL) * val + carry;
            dst[i] = (int)product;
            carry = product >>> 32;
        }
        dst[srcLen] = (int)carry;
    }
    
    private static void mult(final int[] s1, final int s1Len, final int[] s2, final int s2Len, final int[] dst) {
        for (int i = 0; i < s1Len; ++i) {
            final long v = (long)s1[i] & 0xFFFFFFFFL;
            long p = 0L;
            for (int j = 0; j < s2Len; ++j) {
                p += ((long)dst[i + j] & 0xFFFFFFFFL) + v * ((long)s2[j] & 0xFFFFFFFFL);
                dst[i + j] = (int)p;
                p >>>= 32;
            }
            dst[i + s2Len] = (int)p;
        }
    }
    
    private static void mult(final int[] src, final int srcLen, final int v0, final int v1, final int[] dst) {
        long v2 = (long)v0 & 0xFFFFFFFFL;
        long carry = 0L;
        for (int j = 0; j < srcLen; ++j) {
            final long product = v2 * ((long)src[j] & 0xFFFFFFFFL) + carry;
            dst[j] = (int)product;
            carry = product >>> 32;
        }
        dst[srcLen] = (int)carry;
        v2 = ((long)v1 & 0xFFFFFFFFL);
        carry = 0L;
        for (int j = 0; j < srcLen; ++j) {
            final long product = ((long)dst[j + 1] & 0xFFFFFFFFL) + v2 * ((long)src[j] & 0xFFFFFFFFL) + carry;
            dst[j + 1] = (int)product;
            carry = product >>> 32;
        }
        dst[srcLen + 1] = (int)carry;
    }
    
    private static void leftShift(final int[] src, int idx, final int[] result, final int bitcount, final int anticount, int prev) {
        while (idx > 0) {
            int v = prev << bitcount;
            prev = src[idx - 1];
            v |= prev >>> anticount;
            result[idx] = v;
            --idx;
        }
        int v = prev << bitcount;
        result[0] = v;
    }
    
    public FDBigInteger leftShift(final int shift) {
        if (shift == 0 || this.nWords == 0) {
            return this;
        }
        final int wordcount = shift >> 5;
        final int bitcount = shift & 0x1F;
        if (!this.isImmutable) {
            if (bitcount != 0) {
                final int anticount = 32 - bitcount;
                if (this.data[0] << bitcount == 0) {
                    int idx = 0;
                    int prev = this.data[idx];
                    while (idx < this.nWords - 1) {
                        int v = prev >>> anticount;
                        prev = this.data[idx + 1];
                        v |= prev << bitcount;
                        this.data[idx] = v;
                        ++idx;
                    }
                    int v = prev >>> anticount;
                    if ((this.data[idx] = v) == 0) {
                        --this.nWords;
                    }
                    ++this.offset;
                }
                else {
                    final int idx = this.nWords - 1;
                    final int prev = this.data[idx];
                    final int hi = prev >>> anticount;
                    int[] result = this.data;
                    final int[] src = this.data;
                    if (hi != 0) {
                        if (this.nWords == this.data.length) {
                            result = (this.data = new int[this.nWords + 1]);
                        }
                        result[this.nWords++] = hi;
                    }
                    leftShift(src, idx, result, bitcount, anticount, prev);
                }
            }
            this.offset += wordcount;
            return this;
        }
        if (bitcount == 0) {
            return new FDBigInteger(Arrays.copyOf(this.data, this.nWords), this.offset + wordcount);
        }
        final int anticount = 32 - bitcount;
        int idx = this.nWords - 1;
        int prev = this.data[idx];
        final int hi = prev >>> anticount;
        int[] result;
        if (hi != 0) {
            result = new int[this.nWords + 1];
            result[this.nWords] = hi;
        }
        else {
            result = new int[this.nWords];
        }
        leftShift(this.data, idx, result, bitcount, anticount, prev);
        return new FDBigInteger(result, this.offset + wordcount);
    }
    
    private static FDBigInteger big5powRec(final int p) {
        if (p < 340) {
            return FDBigInteger.POW_5_CACHE[p];
        }
        final int q = p >> 1;
        final int r = p - q;
        final FDBigInteger bigq = big5powRec(q);
        if (r < FDBigInteger.SMALL_5_POW.length) {
            return bigq.mult(FDBigInteger.SMALL_5_POW[r]);
        }
        return bigq.mult(big5powRec(r));
    }
    
    public static FDBigInteger valueOfMulPow52(final long value, final int p5, final int p2) {
        assert p5 >= 0 : p5;
        assert p2 >= 0 : p2;
        int v0 = (int)value;
        int v2 = (int)(value >>> 32);
        final int wordcount = p2 >> 5;
        final int bitcount = p2 & 0x1F;
        if (p5 != 0) {
            if (p5 >= FDBigInteger.SMALL_5_POW.length) {
                final FDBigInteger pow5 = big5pow(p5);
                int[] r;
                if (v2 == 0) {
                    r = new int[pow5.nWords + 1 + ((p2 != 0) ? 1 : 0)];
                    mult(pow5.data, pow5.nWords, v0, r);
                }
                else {
                    r = new int[pow5.nWords + 2 + ((p2 != 0) ? 1 : 0)];
                    mult(pow5.data, pow5.nWords, v0, v2, r);
                }
                return new FDBigInteger(r, pow5.offset).leftShift(p2);
            }
            final long pow6 = (long)FDBigInteger.SMALL_5_POW[p5] & 0xFFFFFFFFL;
            long carry = ((long)v0 & 0xFFFFFFFFL) * pow6;
            v0 = (int)carry;
            carry >>>= 32;
            carry += ((long)v2 & 0xFFFFFFFFL) * pow6;
            v2 = (int)carry;
            final int v3 = (int)(carry >>> 32);
            if (bitcount == 0) {
                return new FDBigInteger(new int[] { v0, v2, v3 }, wordcount);
            }
            return new FDBigInteger(new int[] { v0 << bitcount, v2 << bitcount | v0 >>> 32 - bitcount, v3 << bitcount | v2 >>> 32 - bitcount, v3 >>> 32 - bitcount }, wordcount);
        }
        else {
            if (p2 == 0) {
                return new FDBigInteger(new int[] { v0, v2 }, 0);
            }
            if (bitcount == 0) {
                return new FDBigInteger(new int[] { v0, v2 }, wordcount);
            }
            return new FDBigInteger(new int[] { v0 << bitcount, v2 << bitcount | v0 >>> 32 - bitcount, v2 >>> 32 - bitcount }, wordcount);
        }
    }
    
    public int cmp(final FDBigInteger other) {
        final int aSize = this.nWords + this.offset;
        final int bSize = other.nWords + other.offset;
        if (aSize > bSize) {
            return 1;
        }
        if (aSize < bSize) {
            return -1;
        }
        int aLen = this.nWords;
        int bLen = other.nWords;
        while (aLen > 0 && bLen > 0) {
            final int a = this.data[--aLen];
            final int b = other.data[--bLen];
            if (a != b) {
                return (((long)a & 0xFFFFFFFFL) < ((long)b & 0xFFFFFFFFL)) ? -1 : 1;
            }
        }
        if (aLen > 0) {
            return checkZeroTail(this.data, aLen);
        }
        if (bLen > 0) {
            return -checkZeroTail(other.data, bLen);
        }
        return 0;
    }
    
    private static int checkZeroTail(final int[] a, int from) {
        while (from > 0) {
            if (a[--from] != 0) {
                return 1;
            }
        }
        return 0;
    }
    
    public FDBigInteger leftInplaceSub(final FDBigInteger subtrahend) {
        assert this.size() >= subtrahend.size() : "result should be positive";
        FDBigInteger minuend;
        if (this.isImmutable) {
            minuend = new FDBigInteger(this.data.clone(), this.offset);
        }
        else {
            minuend = this;
        }
        int offsetDiff = subtrahend.offset - minuend.offset;
        final int[] sData = subtrahend.data;
        int[] mData = minuend.data;
        final int subLen = subtrahend.nWords;
        int minLen = minuend.nWords;
        if (offsetDiff < 0) {
            final int rLen = minLen - offsetDiff;
            if (rLen < mData.length) {
                System.arraycopy(mData, 0, mData, -offsetDiff, minLen);
                Arrays.fill(mData, 0, -offsetDiff, 0);
            }
            else {
                final int[] r = new int[rLen];
                System.arraycopy(mData, 0, r, -offsetDiff, minLen);
                mData = (minuend.data = r);
            }
            minuend.offset = subtrahend.offset;
            minLen = (minuend.nWords = rLen);
            offsetDiff = 0;
        }
        long borrow = 0L;
        int mIndex = offsetDiff;
        for (int sIndex = 0; sIndex < subLen && mIndex < minLen; ++sIndex, ++mIndex) {
            final long diff = ((long)mData[mIndex] & 0xFFFFFFFFL) - ((long)sData[sIndex] & 0xFFFFFFFFL) + borrow;
            mData[mIndex] = (int)diff;
            borrow = diff >> 32;
        }
        while (borrow != 0L && mIndex < minLen) {
            final long diff2 = ((long)mData[mIndex] & 0xFFFFFFFFL) + borrow;
            mData[mIndex] = (int)diff2;
            borrow = diff2 >> 32;
            ++mIndex;
        }
        assert borrow == 0L : borrow;
        minuend.trimLeadingZeros();
        return minuend;
    }
    
    public FDBigInteger rightInplaceSub(FDBigInteger subtrahend) {
        assert this.size() >= subtrahend.size() : "result should be positive";
        final FDBigInteger minuend = this;
        if (subtrahend.isImmutable) {
            subtrahend = new FDBigInteger(subtrahend.data.clone(), subtrahend.offset);
        }
        int offsetDiff = minuend.offset - subtrahend.offset;
        int[] sData = subtrahend.data;
        final int[] mData = minuend.data;
        int subLen = subtrahend.nWords;
        final int minLen = minuend.nWords;
        if (offsetDiff < 0) {
            if (minLen < sData.length) {
                System.arraycopy(sData, 0, sData, -offsetDiff, subLen);
                Arrays.fill(sData, 0, -offsetDiff, 0);
            }
            else {
                final int[] r = new int[minLen];
                System.arraycopy(sData, 0, r, -offsetDiff, subLen);
                sData = (subtrahend.data = r);
            }
            subtrahend.offset = minuend.offset;
            subLen -= offsetDiff;
            offsetDiff = 0;
        }
        else {
            final int rLen = minLen + offsetDiff;
            if (rLen >= sData.length) {
                sData = (subtrahend.data = Arrays.copyOf(sData, rLen));
            }
        }
        int sIndex = 0;
        long borrow = 0L;
        while (sIndex < offsetDiff) {
            final long diff = -((long)sData[sIndex] & 0xFFFFFFFFL) + borrow;
            sData[sIndex] = (int)diff;
            borrow = diff >> 32;
            ++sIndex;
        }
        for (int mIndex = 0; mIndex < minLen; ++mIndex) {
            final long diff2 = ((long)mData[mIndex] & 0xFFFFFFFFL) - ((long)sData[sIndex] & 0xFFFFFFFFL) + borrow;
            sData[sIndex] = (int)diff2;
            borrow = diff2 >> 32;
            ++sIndex;
        }
        assert borrow == 0L : borrow;
        subtrahend.nWords = sIndex;
        subtrahend.trimLeadingZeros();
        return subtrahend;
    }
    
    public int cmpPow52(final int p5, final int p2) {
        if (p5 != 0) {
            return this.cmp(big5pow(p5).leftShift(p2));
        }
        final int wordcount = p2 >> 5;
        final int bitcount = p2 & 0x1F;
        final int size = this.nWords + this.offset;
        if (size > wordcount + 1) {
            return 1;
        }
        if (size < wordcount + 1) {
            return -1;
        }
        final int a = this.data[this.nWords - 1];
        final int b = 1 << bitcount;
        if (a != b) {
            return (((long)a & 0xFFFFFFFFL) < ((long)b & 0xFFFFFFFFL)) ? -1 : 1;
        }
        return checkZeroTail(this.data, this.nWords - 1);
    }
    
    static {
        SMALL_5_POW = new int[] { 1, 5, 25, 125, 625, 3125, 15625, 78125, 390625, 1953125, 9765625, 48828125, 244140625, 1220703125 };
        POW_5_CACHE = new FDBigInteger[340];
        int i;
        for (i = 0; i < FDBigInteger.SMALL_5_POW.length; ++i) {
            final FDBigInteger pow5 = new FDBigInteger(new int[] { FDBigInteger.SMALL_5_POW[i] }, 0);
            pow5.makeImmutable();
            FDBigInteger.POW_5_CACHE[i] = pow5;
        }
        FDBigInteger prev = FDBigInteger.POW_5_CACHE[i - 1];
        while (i < 340) {
            prev = (FDBigInteger.POW_5_CACHE[i] = prev.mult(5));
            prev.makeImmutable();
            ++i;
        }
    }
}
