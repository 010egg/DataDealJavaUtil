// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.util;

import java.util.Arrays;

public final class FnvHash
{
    public static final long BASIC = -3750763034362895579L;
    public static final long PRIME = 1099511628211L;
    
    public static long fnv1a_64(final String input) {
        if (input == null) {
            return 0L;
        }
        long hash = -3750763034362895579L;
        for (int i = 0; i < input.length(); ++i) {
            final char c = input.charAt(i);
            hash ^= c;
            hash *= 1099511628211L;
        }
        return hash;
    }
    
    public static long fnv1a_64(final StringBuilder input) {
        if (input == null) {
            return 0L;
        }
        long hash = -3750763034362895579L;
        for (int i = 0; i < input.length(); ++i) {
            final char c = input.charAt(i);
            hash ^= c;
            hash *= 1099511628211L;
        }
        return hash;
    }
    
    public static long fnv1a_64(final String input, final int offset, int end) {
        if (input == null) {
            return 0L;
        }
        if (input.length() < end) {
            end = input.length();
        }
        long hash = -3750763034362895579L;
        for (int i = offset; i < end; ++i) {
            final char c = input.charAt(i);
            hash ^= c;
            hash *= 1099511628211L;
        }
        return hash;
    }
    
    public static long fnv1a_64(final byte[] input, final int offset, int end) {
        if (input == null) {
            return 0L;
        }
        if (input.length < end) {
            end = input.length;
        }
        long hash = -3750763034362895579L;
        for (int i = offset; i < end; ++i) {
            final byte c = input[i];
            hash ^= c;
            hash *= 1099511628211L;
        }
        return hash;
    }
    
    public static long fnv1a_64(final char[] chars) {
        if (chars == null) {
            return 0L;
        }
        long hash = -3750763034362895579L;
        for (int i = 0; i < chars.length; ++i) {
            final char c = chars[i];
            hash ^= c;
            hash *= 1099511628211L;
        }
        return hash;
    }
    
    public static long hashCode64(final String name) {
        if (name == null) {
            return 0L;
        }
        boolean quote = false;
        final int len = name.length();
        if (len > 2) {
            final char c0 = name.charAt(0);
            final char c2 = name.charAt(len - 1);
            if ((c0 == '`' && c2 == '`') || (c0 == '\"' && c2 == '\"') || (c0 == '\'' && c2 == '\'') || (c0 == '[' && c2 == ']')) {
                quote = true;
            }
        }
        if (quote) {
            return hashCode64(name, 1, len - 1);
        }
        return hashCode64(name, 0, len);
    }
    
    public static long fnv1a_64_lower(final String key) {
        long hashCode = -3750763034362895579L;
        for (int i = 0; i < key.length(); ++i) {
            char ch = key.charAt(i);
            if (ch >= 'A' && ch <= 'Z') {
                ch += ' ';
            }
            hashCode ^= ch;
            hashCode *= 1099511628211L;
        }
        return hashCode;
    }
    
    public static long fnv1a_64_lower(final StringBuilder key) {
        long hashCode = -3750763034362895579L;
        for (int i = 0; i < key.length(); ++i) {
            char ch = key.charAt(i);
            if (ch >= 'A' && ch <= 'Z') {
                ch += ' ';
            }
            hashCode ^= ch;
            hashCode *= 1099511628211L;
        }
        return hashCode;
    }
    
    public static long fnv1a_64_lower(final long basic, final StringBuilder key) {
        long hashCode = basic;
        for (int i = 0; i < key.length(); ++i) {
            char ch = key.charAt(i);
            if (ch >= 'A' && ch <= 'Z') {
                ch += ' ';
            }
            hashCode ^= ch;
            hashCode *= 1099511628211L;
        }
        return hashCode;
    }
    
    public static long hashCode64(final String key, final int offset, final int end) {
        long hashCode = -3750763034362895579L;
        for (int i = offset; i < end; ++i) {
            char ch = key.charAt(i);
            if (ch >= 'A' && ch <= 'Z') {
                ch += ' ';
            }
            hashCode ^= ch;
            hashCode *= 1099511628211L;
        }
        return hashCode;
    }
    
    public static long hashCode64(final long basic, final String name) {
        if (name == null) {
            return basic;
        }
        boolean quote = false;
        final int len = name.length();
        if (len > 2) {
            final char c0 = name.charAt(0);
            final char c2 = name.charAt(len - 1);
            if ((c0 == '`' && c2 == '`') || (c0 == '\"' && c2 == '\"') || (c0 == '\'' && c2 == '\'') || (c0 == '[' && c2 == ']')) {
                quote = true;
            }
        }
        if (quote) {
            final int offset = 1;
            int end = len - 1;
            for (int i = end - 1; i >= 0; --i) {
                final char ch = name.charAt(i);
                if (ch != ' ') {
                    break;
                }
                --end;
            }
            return hashCode64(basic, name, offset, end);
        }
        return hashCode64(basic, name, 0, len);
    }
    
    public static long hashCode64(final long basic, final String key, final int offset, final int end) {
        long hashCode = basic;
        for (int i = offset; i < end; ++i) {
            char ch = key.charAt(i);
            if (ch >= 'A' && ch <= 'Z') {
                ch += ' ';
            }
            hashCode ^= ch;
            hashCode *= 1099511628211L;
        }
        return hashCode;
    }
    
    public static long fnv_32_lower(final String key) {
        long hashCode = -2128831035L;
        for (int i = 0; i < key.length(); ++i) {
            char ch = key.charAt(i);
            if (ch != '_') {
                if (ch != '-') {
                    if (ch >= 'A' && ch <= 'Z') {
                        ch += ' ';
                    }
                    hashCode ^= ch;
                    hashCode *= 16777619L;
                }
            }
        }
        return hashCode;
    }
    
    public static long[] fnv1a_64_lower(final String[] strings, final boolean sort) {
        final long[] hashCodes = new long[strings.length];
        for (int i = 0; i < strings.length; ++i) {
            hashCodes[i] = fnv1a_64_lower(strings[i]);
        }
        if (sort) {
            Arrays.sort(hashCodes);
        }
        return hashCodes;
    }
    
    public static long hashCode64(final String owner, final String name) {
        long hashCode = -3750763034362895579L;
        if (owner != null) {
            final String item = owner;
            boolean quote = false;
            final int len = item.length();
            if (len > 2) {
                final char c0 = item.charAt(0);
                final char c2 = item.charAt(len - 1);
                if ((c0 == '`' && c2 == '`') || (c0 == '\"' && c2 == '\"') || (c0 == '\'' && c2 == '\'') || (c0 == '[' && c2 == ']')) {
                    quote = true;
                }
            }
            final int start = quote ? 1 : 0;
            for (int end = quote ? (len - 1) : len, j = start; j < end; ++j) {
                char ch = item.charAt(j);
                if (ch >= 'A' && ch <= 'Z') {
                    ch += ' ';
                }
                hashCode ^= ch;
                hashCode *= 1099511628211L;
            }
            hashCode ^= 0x2EL;
            hashCode *= 1099511628211L;
        }
        if (name != null) {
            final String item = name;
            boolean quote = false;
            final int len = item.length();
            if (len > 2) {
                final char c0 = item.charAt(0);
                final char c2 = item.charAt(len - 1);
                if ((c0 == '`' && c2 == '`') || (c0 == '\"' && c2 == '\"') || (c0 == '\'' && c2 == '\'') || (c0 == '[' && c2 == ']')) {
                    quote = true;
                }
            }
            final int start = quote ? 1 : 0;
            for (int end = quote ? (len - 1) : len, j = start; j < end; ++j) {
                char ch = item.charAt(j);
                if (ch >= 'A' && ch <= 'Z') {
                    ch += ' ';
                }
                hashCode ^= ch;
                hashCode *= 1099511628211L;
            }
        }
        return hashCode;
    }
    
    public interface Constants
    {
        public static final long HIGH_PRIORITY = FnvHash.fnv1a_64_lower("HIGH_PRIORITY");
        public static final long DISTINCTROW = FnvHash.fnv1a_64_lower("DISTINCTROW");
        public static final long STRAIGHT = FnvHash.fnv1a_64_lower("STRAIGHT");
        public static final long STRAIGHT_JOIN = FnvHash.fnv1a_64_lower("STRAIGHT_JOIN");
        public static final long SQL_SMALL_RESULT = FnvHash.fnv1a_64_lower("SQL_SMALL_RESULT");
        public static final long SQL_BIG_RESULT = FnvHash.fnv1a_64_lower("SQL_BIG_RESULT");
        public static final long SQL_BUFFER_RESULT = FnvHash.fnv1a_64_lower("SQL_BUFFER_RESULT");
        public static final long CACHE = FnvHash.fnv1a_64_lower("CACHE");
        public static final long SQL_CACHE = FnvHash.fnv1a_64_lower("SQL_CACHE");
        public static final long SQL_NO_CACHE = FnvHash.fnv1a_64_lower("SQL_NO_CACHE");
        public static final long SQL_CALC_FOUND_ROWS = FnvHash.fnv1a_64_lower("SQL_CALC_FOUND_ROWS");
        public static final long TOP = FnvHash.fnv1a_64_lower("TOP");
        public static final long OUTFILE = FnvHash.fnv1a_64_lower("OUTFILE");
        public static final long SETS = FnvHash.fnv1a_64_lower("SETS");
        public static final long REGEXP = FnvHash.fnv1a_64_lower("REGEXP");
        public static final long RLIKE = FnvHash.fnv1a_64_lower("RLIKE");
        public static final long USING = FnvHash.fnv1a_64_lower("USING");
        public static final long MATCHED = FnvHash.fnv1a_64_lower("MATCHED");
        public static final long IGNORE = FnvHash.fnv1a_64_lower("IGNORE");
        public static final long FORCE = FnvHash.fnv1a_64_lower("FORCE");
        public static final long CROSS = FnvHash.fnv1a_64_lower("CROSS");
        public static final long NATURAL = FnvHash.fnv1a_64_lower("NATURAL");
        public static final long APPLY = FnvHash.fnv1a_64_lower("APPLY");
        public static final long CONNECT = FnvHash.fnv1a_64_lower("CONNECT");
        public static final long START = FnvHash.fnv1a_64_lower("START");
        public static final long BTREE = FnvHash.fnv1a_64_lower("BTREE");
        public static final long HASH = FnvHash.fnv1a_64_lower("HASH");
        public static final long LIST = FnvHash.fnv1a_64_lower("LIST");
        public static final long NO_WAIT = FnvHash.fnv1a_64_lower("NO_WAIT");
        public static final long WAIT = FnvHash.fnv1a_64_lower("WAIT");
        public static final long NOWAIT = FnvHash.fnv1a_64_lower("NOWAIT");
        public static final long ERRORS = FnvHash.fnv1a_64_lower("ERRORS");
        public static final long VALUE = FnvHash.fnv1a_64_lower("VALUE");
        public static final long OBJECT = FnvHash.fnv1a_64_lower("OBJECT");
        public static final long NEXT = FnvHash.fnv1a_64_lower("NEXT");
        public static final long NEXTVAL = FnvHash.fnv1a_64_lower("NEXTVAL");
        public static final long CURRVAL = FnvHash.fnv1a_64_lower("CURRVAL");
        public static final long PREVVAL = FnvHash.fnv1a_64_lower("PREVVAL");
        public static final long PREVIOUS = FnvHash.fnv1a_64_lower("PREVIOUS");
        public static final long LOW_PRIORITY = FnvHash.fnv1a_64_lower("LOW_PRIORITY");
        public static final long COMMIT_ON_SUCCESS = FnvHash.fnv1a_64_lower("COMMIT_ON_SUCCESS");
        public static final long ROLLBACK_ON_FAIL = FnvHash.fnv1a_64_lower("ROLLBACK_ON_FAIL");
        public static final long QUEUE_ON_PK = FnvHash.fnv1a_64_lower("QUEUE_ON_PK");
        public static final long TARGET_AFFECT_ROW = FnvHash.fnv1a_64_lower("TARGET_AFFECT_ROW");
        public static final long COLLATE = FnvHash.fnv1a_64_lower("COLLATE");
        public static final long BOOLEAN = FnvHash.fnv1a_64_lower("BOOLEAN");
        public static final long SMALLINT = FnvHash.fnv1a_64_lower("SMALLINT");
        public static final long SHORT = FnvHash.fnv1a_64_lower("SHORT");
        public static final long TINY = FnvHash.fnv1a_64_lower("TINY");
        public static final long TINYINT = FnvHash.fnv1a_64_lower("TINYINT");
        public static final long CHARSET = FnvHash.fnv1a_64_lower("CHARSET");
        public static final long SEMI = FnvHash.fnv1a_64_lower("SEMI");
        public static final long ANTI = FnvHash.fnv1a_64_lower("ANTI");
        public static final long PRIOR = FnvHash.fnv1a_64_lower("PRIOR");
        public static final long NOCYCLE = FnvHash.fnv1a_64_lower("NOCYCLE");
        public static final long CYCLE = FnvHash.fnv1a_64_lower("CYCLE");
        public static final long CONNECT_BY_ROOT = FnvHash.fnv1a_64_lower("CONNECT_BY_ROOT");
        public static final long DATE = FnvHash.fnv1a_64_lower("DATE");
        public static final long GSON = FnvHash.fnv1a_64_lower("GSON");
        public static final long NEW = FnvHash.fnv1a_64_lower("NEW");
        public static final long NEWDATE = FnvHash.fnv1a_64_lower("NEWDATE");
        public static final long DATETIME = FnvHash.fnv1a_64_lower("DATETIME");
        public static final long TIME = FnvHash.fnv1a_64_lower("TIME");
        public static final long ZONE = FnvHash.fnv1a_64_lower("ZONE");
        public static final long JSON = FnvHash.fnv1a_64_lower("JSON");
        public static final long TIMESTAMP = FnvHash.fnv1a_64_lower("TIMESTAMP");
        public static final long TIMESTAMPTZ = FnvHash.fnv1a_64_lower("TIMESTAMPTZ");
        public static final long CLOB = FnvHash.fnv1a_64_lower("CLOB");
        public static final long NCLOB = FnvHash.fnv1a_64_lower("NCLOB");
        public static final long TINYBLOB = FnvHash.fnv1a_64_lower("TINYBLOB");
        public static final long BLOB = FnvHash.fnv1a_64_lower("BLOB");
        public static final long XMLTYPE = FnvHash.fnv1a_64_lower("XMLTYPE");
        public static final long BFILE = FnvHash.fnv1a_64_lower("BFILE");
        public static final long UROWID = FnvHash.fnv1a_64_lower("UROWID");
        public static final long ROWID = FnvHash.fnv1a_64_lower("ROWID");
        public static final long REF = FnvHash.fnv1a_64_lower("REF");
        public static final long INTEGER = FnvHash.fnv1a_64_lower("INTEGER");
        public static final long INT = FnvHash.fnv1a_64_lower("INT");
        public static final long INT24 = FnvHash.fnv1a_64_lower("INT24");
        public static final long BINARY_FLOAT = FnvHash.fnv1a_64_lower("BINARY_FLOAT");
        public static final long BINARY_DOUBLE = FnvHash.fnv1a_64_lower("BINARY_DOUBLE");
        public static final long FLOAT = FnvHash.fnv1a_64_lower("FLOAT");
        public static final long REAL = FnvHash.fnv1a_64_lower("REAL");
        public static final long NUMBER = FnvHash.fnv1a_64_lower("NUMBER");
        public static final long NUMERIC = FnvHash.fnv1a_64_lower("NUMERIC");
        public static final long DEC = FnvHash.fnv1a_64_lower("DEC");
        public static final long DECIMAL = FnvHash.fnv1a_64_lower("DECIMAL");
        public static final long CURRENT = FnvHash.fnv1a_64_lower("CURRENT");
        public static final long COUNT = FnvHash.fnv1a_64_lower("COUNT");
        public static final long ROW_NUMBER = FnvHash.fnv1a_64_lower("ROW_NUMBER");
        public static final long FIRST_VALUE = FnvHash.fnv1a_64_lower("FIRST_VALUE");
        public static final long LAST_VALUE = FnvHash.fnv1a_64_lower("LAST_VALUE");
        public static final long WM_CONCAT = FnvHash.fnv1a_64_lower("WM_CONCAT");
        public static final long AVG = FnvHash.fnv1a_64_lower("AVG");
        public static final long MAX = FnvHash.fnv1a_64_lower("MAX");
        public static final long MIN = FnvHash.fnv1a_64_lower("MIN");
        public static final long STDDEV = FnvHash.fnv1a_64_lower("STDDEV");
        public static final long RANK = FnvHash.fnv1a_64_lower("RANK");
        public static final long SUM = FnvHash.fnv1a_64_lower("SUM");
        public static final long ARBITRARY = FnvHash.fnv1a_64_lower("ARBITRARY");
        public static final long GROUP_CONCAT = FnvHash.fnv1a_64_lower("GROUP_CONCAT");
        public static final long CONVERT_TZ = FnvHash.fnv1a_64_lower("CONVERT_TZ");
        public static final long DEDUPLICATION = FnvHash.fnv1a_64_lower("DEDUPLICATION");
        public static final long CONVERT = FnvHash.fnv1a_64_lower("CONVERT");
        public static final long CHAR = FnvHash.fnv1a_64_lower("CHAR");
        public static final long ENUM = FnvHash.fnv1a_64_lower("ENUM");
        public static final long STRING = FnvHash.fnv1a_64_lower("STRING");
        public static final long VARCHAR = FnvHash.fnv1a_64_lower("VARCHAR");
        public static final long VARCHAR2 = FnvHash.fnv1a_64_lower("VARCHAR2");
        public static final long NCHAR = FnvHash.fnv1a_64_lower("NCHAR");
        public static final long NVARCHAR = FnvHash.fnv1a_64_lower("NVARCHAR");
        public static final long NVARCHAR2 = FnvHash.fnv1a_64_lower("NVARCHAR2");
        public static final long NCHAR_VARYING = FnvHash.fnv1a_64_lower("nchar varying");
        public static final long VARBINARY = FnvHash.fnv1a_64_lower("VARBINARY");
        public static final long TINYTEXT = FnvHash.fnv1a_64_lower("TINYTEXT");
        public static final long TEXT = FnvHash.fnv1a_64_lower("TEXT");
        public static final long MEDIUMTEXT = FnvHash.fnv1a_64_lower("MEDIUMTEXT");
        public static final long LONGTEXT = FnvHash.fnv1a_64_lower("LONGTEXT");
        public static final long TRIM = FnvHash.fnv1a_64_lower("TRIM");
        public static final long LEADING = FnvHash.fnv1a_64_lower("LEADING");
        public static final long BOTH = FnvHash.fnv1a_64_lower("BOTH");
        public static final long TRAILING = FnvHash.fnv1a_64_lower("TRAILING");
        public static final long MOD = FnvHash.fnv1a_64_lower("MOD");
        public static final long MATCH = FnvHash.fnv1a_64_lower("MATCH");
        public static final long AGAINST = FnvHash.fnv1a_64_lower("AGAINST");
        public static final long EXTRACT = FnvHash.fnv1a_64_lower("EXTRACT");
        public static final long POLYGON = FnvHash.fnv1a_64_lower("POLYGON");
        public static final long CIRCLE = FnvHash.fnv1a_64_lower("CIRCLE");
        public static final long LSEG = FnvHash.fnv1a_64_lower("LSEG");
        public static final long POINT = FnvHash.fnv1a_64_lower("POINT");
        public static final long BOX = FnvHash.fnv1a_64_lower("BOX");
        public static final long MACADDR = FnvHash.fnv1a_64_lower("MACADDR");
        public static final long INET = FnvHash.fnv1a_64_lower("INET");
        public static final long CIDR = FnvHash.fnv1a_64_lower("CIDR");
        public static final long POSITION = FnvHash.fnv1a_64_lower("POSITION");
        public static final long DUAL = FnvHash.fnv1a_64_lower("DUAL");
        public static final long LEVEL = FnvHash.fnv1a_64_lower("LEVEL");
        public static final long CONNECT_BY_ISCYCLE = FnvHash.fnv1a_64_lower("CONNECT_BY_ISCYCLE");
        public static final long CURRENT_TIMESTAMP = FnvHash.fnv1a_64_lower("CURRENT_TIMESTAMP");
        public static final long LOCALTIMESTAMP = FnvHash.fnv1a_64_lower("LOCALTIMESTAMP");
        public static final long LOCALTIME = FnvHash.fnv1a_64_lower("LOCALTIME");
        public static final long SESSIONTIMEZONE = FnvHash.fnv1a_64_lower("SESSIONTIMEZONE");
        public static final long DBTIMEZONE = FnvHash.fnv1a_64_lower("DBTIMEZONE");
        public static final long CURRENT_DATE = FnvHash.fnv1a_64_lower("CURRENT_DATE");
        public static final long CURRENT_TIME = FnvHash.fnv1a_64_lower("CURRENT_TIME");
        public static final long CURTIME = FnvHash.fnv1a_64_lower("CURTIME");
        public static final long CURRENT_USER = FnvHash.fnv1a_64_lower("CURRENT_USER");
        public static final long FALSE = FnvHash.fnv1a_64_lower("FALSE");
        public static final long TRUE = FnvHash.fnv1a_64_lower("TRUE");
        public static final long LESS = FnvHash.fnv1a_64_lower("LESS");
        public static final long MAXVALUE = FnvHash.fnv1a_64_lower("MAXVALUE");
        public static final long OFFSET = FnvHash.fnv1a_64_lower("OFFSET");
        public static final long LIMIT = FnvHash.fnv1a_64_lower("LIMIT");
        public static final long RAW = FnvHash.fnv1a_64_lower("RAW");
        public static final long LONG_RAW = FnvHash.fnv1a_64_lower("LONG RAW");
        public static final long LONG = FnvHash.fnv1a_64_lower("LONG");
        public static final long BYTE = FnvHash.fnv1a_64_lower("BYTE");
        public static final long ROWNUM = FnvHash.fnv1a_64_lower("ROWNUM");
        public static final long SYSDATE = FnvHash.fnv1a_64_lower("SYSDATE");
        public static final long NOW = FnvHash.fnv1a_64_lower("NOW");
        public static final long ADDTIME = FnvHash.fnv1a_64_lower("ADDTIME");
        public static final long SUBTIME = FnvHash.fnv1a_64_lower("SUBTIME");
        public static final long TIMEDIFF = FnvHash.fnv1a_64_lower("TIMEDIFF");
        public static final long SQLCODE = FnvHash.fnv1a_64_lower("SQLCODE");
        public static final long PRECISION = FnvHash.fnv1a_64_lower("PRECISION");
        public static final long DOUBLE = FnvHash.fnv1a_64_lower("DOUBLE");
        public static final long DOUBLE_PRECISION = FnvHash.fnv1a_64_lower("DOUBLE PRECISION");
        public static final long WITHOUT = FnvHash.fnv1a_64_lower("WITHOUT");
        public static final long BITAND = FnvHash.fnv1a_64_lower("BITAND");
        public static final long DEFINER = FnvHash.fnv1a_64_lower("DEFINER");
        public static final long EVENT = FnvHash.fnv1a_64_lower("EVENT");
        public static final long RESOURCE = FnvHash.fnv1a_64_lower("RESOURCE");
        public static final long RESOURCES = FnvHash.fnv1a_64_lower("RESOURCES");
        public static final long FILE = FnvHash.fnv1a_64_lower("FILE");
        public static final long JAR = FnvHash.fnv1a_64_lower("JAR");
        public static final long PY = FnvHash.fnv1a_64_lower("PY");
        public static final long ARCHIVE = FnvHash.fnv1a_64_lower("archive");
        public static final long DETERMINISTIC = FnvHash.fnv1a_64_lower("DETERMINISTIC");
        public static final long CONTAINS = FnvHash.fnv1a_64_lower("CONTAINS");
        public static final long SQL = FnvHash.fnv1a_64_lower("SQL");
        public static final long CALL = FnvHash.fnv1a_64_lower("CALL");
        public static final long CHARACTER = FnvHash.fnv1a_64_lower("CHARACTER");
        public static final long UNNEST = FnvHash.fnv1a_64_lower("UNNEST");
        public static final long VALIDATE = FnvHash.fnv1a_64_lower("VALIDATE");
        public static final long NOVALIDATE = FnvHash.fnv1a_64_lower("NOVALIDATE");
        public static final long SIMILAR = FnvHash.fnv1a_64_lower("SIMILAR");
        public static final long CASCADE = FnvHash.fnv1a_64_lower("CASCADE");
        public static final long RELY = FnvHash.fnv1a_64_lower("RELY");
        public static final long NORELY = FnvHash.fnv1a_64_lower("NORELY");
        public static final long ROW = FnvHash.fnv1a_64_lower("ROW");
        public static final long ROWS = FnvHash.fnv1a_64_lower("ROWS");
        public static final long RANGE = FnvHash.fnv1a_64_lower("RANGE");
        public static final long PRECEDING = FnvHash.fnv1a_64_lower("PRECEDING");
        public static final long FOLLOWING = FnvHash.fnv1a_64_lower("FOLLOWING");
        public static final long UNBOUNDED = FnvHash.fnv1a_64_lower("UNBOUNDED");
        public static final long SIBLINGS = FnvHash.fnv1a_64_lower("SIBLINGS");
        public static final long RESPECT = FnvHash.fnv1a_64_lower("RESPECT");
        public static final long NULLS = FnvHash.fnv1a_64_lower("NULLS");
        public static final long FIRST = FnvHash.fnv1a_64_lower("FIRST");
        public static final long LAST = FnvHash.fnv1a_64_lower("LAST");
        public static final long AUTO_INCREMENT = FnvHash.fnv1a_64_lower("AUTO_INCREMENT");
        public static final long STORAGE = FnvHash.fnv1a_64_lower("STORAGE");
        public static final long STORED = FnvHash.fnv1a_64_lower("STORED");
        public static final long VIRTUAL = FnvHash.fnv1a_64_lower("VIRTUAL");
        public static final long SIGNED = FnvHash.fnv1a_64_lower("SIGNED");
        public static final long UNSIGNED = FnvHash.fnv1a_64_lower("UNSIGNED");
        public static final long ZEROFILL = FnvHash.fnv1a_64_lower("ZEROFILL");
        public static final long GLOBAL = FnvHash.fnv1a_64_lower("GLOBAL");
        public static final long LOCAL = FnvHash.fnv1a_64_lower("LOCAL");
        public static final long TEMPORARY = FnvHash.fnv1a_64_lower("TEMPORARY");
        public static final long NONCLUSTERED = FnvHash.fnv1a_64_lower("NONCLUSTERED");
        public static final long SESSION = FnvHash.fnv1a_64_lower("SESSION");
        public static final long NAMES = FnvHash.fnv1a_64_lower("NAMES");
        public static final long PARTIAL = FnvHash.fnv1a_64_lower("PARTIAL");
        public static final long SIMPLE = FnvHash.fnv1a_64_lower("SIMPLE");
        public static final long RESTRICT = FnvHash.fnv1a_64_lower("RESTRICT");
        public static final long ON = FnvHash.fnv1a_64_lower("ON");
        public static final long ACTION = FnvHash.fnv1a_64_lower("ACTION");
        public static final long SEPARATOR = FnvHash.fnv1a_64_lower("SEPARATOR");
        public static final long DATA = FnvHash.fnv1a_64_lower("DATA");
        public static final long MIGRATE = FnvHash.fnv1a_64_lower("MIGRATE");
        public static final long MAX_ROWS = FnvHash.fnv1a_64_lower("MAX_ROWS");
        public static final long MIN_ROWS = FnvHash.fnv1a_64_lower("MIN_ROWS");
        public static final long PACK_KEYS = FnvHash.fnv1a_64_lower("PACK_KEYS");
        public static final long ENGINE = FnvHash.fnv1a_64_lower("ENGINE");
        public static final long SKIP = FnvHash.fnv1a_64_lower("SKIP");
        public static final long RECURSIVE = FnvHash.fnv1a_64_lower("RECURSIVE");
        public static final long ROLLUP = FnvHash.fnv1a_64_lower("ROLLUP");
        public static final long CUBE = FnvHash.fnv1a_64_lower("CUBE");
        public static final long YEAR = FnvHash.fnv1a_64_lower("YEAR");
        public static final long QUARTER = FnvHash.fnv1a_64_lower("QUARTER");
        public static final long MONTH = FnvHash.fnv1a_64_lower("MONTH");
        public static final long WEEK = FnvHash.fnv1a_64_lower("WEEK");
        public static final long WEEKDAY = FnvHash.fnv1a_64_lower("WEEKDAY");
        public static final long WEEKOFYEAR = FnvHash.fnv1a_64_lower("WEEKOFYEAR");
        public static final long YEARWEEK = FnvHash.fnv1a_64_lower("YEARWEEK");
        public static final long YEAR_OF_WEEK = FnvHash.fnv1a_64_lower("YEAR_OF_WEEK");
        public static final long YOW = FnvHash.fnv1a_64_lower("YOW");
        public static final long YEARMONTH = FnvHash.fnv1a_64_lower("YEARMONTH");
        public static final long TO_TIMESTAMP = FnvHash.fnv1a_64_lower("TO_TIMESTAMP");
        public static final long DAY = FnvHash.fnv1a_64_lower("DAY");
        public static final long DAYOFMONTH = FnvHash.fnv1a_64_lower("DAYOFMONTH");
        public static final long DAYOFWEEK = FnvHash.fnv1a_64_lower("DAYOFWEEK");
        public static final long DATE_TRUNC = FnvHash.fnv1a_64_lower("DATE_TRUNC");
        public static final long DAYOFYEAR = FnvHash.fnv1a_64_lower("DAYOFYEAR");
        public static final long MONTH_BETWEEN = FnvHash.fnv1a_64_lower("MONTH_BETWEEN");
        public static final long TIMESTAMPADD = FnvHash.fnv1a_64_lower("TIMESTAMPADD");
        public static final long HOUR = FnvHash.fnv1a_64_lower("HOUR");
        public static final long MINUTE = FnvHash.fnv1a_64_lower("MINUTE");
        public static final long SECOND = FnvHash.fnv1a_64_lower("SECOND");
        public static final long MICROSECOND = FnvHash.fnv1a_64_lower("MICROSECOND");
        public static final long CURDATE = FnvHash.fnv1a_64_lower("CURDATE");
        public static final long CUR_DATE = FnvHash.fnv1a_64_lower("CUR_DATE");
        public static final long DATE_DIFF = FnvHash.fnv1a_64_lower("DATE_DIFF");
        public static final long SECONDS = FnvHash.fnv1a_64_lower("SECONDS");
        public static final long MINUTES = FnvHash.fnv1a_64_lower("MINUTES");
        public static final long HOURS = FnvHash.fnv1a_64_lower("HOURS");
        public static final long DAYS = FnvHash.fnv1a_64_lower("DAYS");
        public static final long MONTHS = FnvHash.fnv1a_64_lower("MONTHS");
        public static final long YEARS = FnvHash.fnv1a_64_lower("YEARS");
        public static final long BEFORE = FnvHash.fnv1a_64_lower("BEFORE");
        public static final long AFTER = FnvHash.fnv1a_64_lower("AFTER");
        public static final long INSTEAD = FnvHash.fnv1a_64_lower("INSTEAD");
        public static final long DEFERRABLE = FnvHash.fnv1a_64_lower("DEFERRABLE");
        public static final long AS = FnvHash.fnv1a_64_lower("AS");
        public static final long DELAYED = FnvHash.fnv1a_64_lower("DELAYED");
        public static final long GO = FnvHash.fnv1a_64_lower("GO");
        public static final long WAITFOR = FnvHash.fnv1a_64_lower("WAITFOR");
        public static final long EXEC = FnvHash.fnv1a_64_lower("EXEC");
        public static final long EXECUTE = FnvHash.fnv1a_64_lower("EXECUTE");
        public static final long SOURCE = FnvHash.fnv1a_64_lower("SOURCE");
        public static final long STAR = FnvHash.fnv1a_64_lower("*");
        public static final long TO_CHAR = FnvHash.fnv1a_64_lower("TO_CHAR");
        public static final long UNIX_TIMESTAMP = FnvHash.fnv1a_64_lower("UNIX_TIMESTAMP");
        public static final long FROM_UNIXTIME = FnvHash.fnv1a_64_lower("FROM_UNIXTIME");
        public static final long TO_UNIXTIME = FnvHash.fnv1a_64_lower("TO_UNIXTIME");
        public static final long SYS_GUID = FnvHash.fnv1a_64_lower("SYS_GUID");
        public static final long LAST_DAY = FnvHash.fnv1a_64_lower("LAST_DAY");
        public static final long MAKEDATE = FnvHash.fnv1a_64_lower("MAKEDATE");
        public static final long ASCII = FnvHash.fnv1a_64_lower("ASCII");
        public static final long DAYNAME = FnvHash.fnv1a_64_lower("DAYNAME");
        public static final long STATISTICS = FnvHash.fnv1a_64_lower("STATISTICS");
        public static final long TRANSACTION = FnvHash.fnv1a_64_lower("TRANSACTION");
        public static final long OFF = FnvHash.fnv1a_64_lower("OFF");
        public static final long IDENTITY_INSERT = FnvHash.fnv1a_64_lower("IDENTITY_INSERT");
        public static final long PASSWORD = FnvHash.fnv1a_64_lower("PASSWORD");
        public static final long SOCKET = FnvHash.fnv1a_64_lower("SOCKET");
        public static final long OWNER = FnvHash.fnv1a_64_lower("OWNER");
        public static final long PORT = FnvHash.fnv1a_64_lower("PORT");
        public static final long PUBLIC = FnvHash.fnv1a_64_lower("PUBLIC");
        public static final long SYNONYM = FnvHash.fnv1a_64_lower("SYNONYM");
        public static final long MATERIALIZED = FnvHash.fnv1a_64_lower("MATERIALIZED");
        public static final long BITMAP = FnvHash.fnv1a_64_lower("BITMAP");
        public static final long LABEL = FnvHash.fnv1a_64_lower("LABEL");
        public static final long PACKAGE = FnvHash.fnv1a_64_lower("PACKAGE");
        public static final long PACKAGES = FnvHash.fnv1a_64_lower("PACKAGES");
        public static final long TRUNC = FnvHash.fnv1a_64_lower("TRUNC");
        public static final long SYSTIMESTAMP = FnvHash.fnv1a_64_lower("SYSTIMESTAMP");
        public static final long TYPE = FnvHash.fnv1a_64_lower("TYPE");
        public static final long RECORD = FnvHash.fnv1a_64_lower("RECORD");
        public static final long MAP = FnvHash.fnv1a_64_lower("MAP");
        public static final long MAPJOIN = FnvHash.fnv1a_64_lower("MAPJOIN");
        public static final long MAPPED = FnvHash.fnv1a_64_lower("MAPPED");
        public static final long MAPPING = FnvHash.fnv1a_64_lower("MAPPING");
        public static final long COLPROPERTIES = FnvHash.fnv1a_64_lower("COLPROPERTIES");
        public static final long ONLY = FnvHash.fnv1a_64_lower("ONLY");
        public static final long MEMBER = FnvHash.fnv1a_64_lower("MEMBER");
        public static final long STATIC = FnvHash.fnv1a_64_lower("STATIC");
        public static final long FINAL = FnvHash.fnv1a_64_lower("FINAL");
        public static final long INSTANTIABLE = FnvHash.fnv1a_64_lower("INSTANTIABLE");
        public static final long UNSUPPORTED = FnvHash.fnv1a_64_lower("UNSUPPORTED");
        public static final long VARRAY = FnvHash.fnv1a_64_lower("VARRAY");
        public static final long WRAPPED = FnvHash.fnv1a_64_lower("WRAPPED");
        public static final long AUTHID = FnvHash.fnv1a_64_lower("AUTHID");
        public static final long UNDER = FnvHash.fnv1a_64_lower("UNDER");
        public static final long USERENV = FnvHash.fnv1a_64_lower("USERENV");
        public static final long NUMTODSINTERVAL = FnvHash.fnv1a_64_lower("NUMTODSINTERVAL");
        public static final long LATERAL = FnvHash.fnv1a_64_lower("LATERAL");
        public static final long NONE = FnvHash.fnv1a_64_lower("NONE");
        public static final long PARTITIONING = FnvHash.fnv1a_64_lower("PARTITIONING");
        public static final long VALIDPROC = FnvHash.fnv1a_64_lower("VALIDPROC");
        public static final long COMPRESS = FnvHash.fnv1a_64_lower("COMPRESS");
        public static final long YES = FnvHash.fnv1a_64_lower("YES");
        public static final long WMSYS = FnvHash.fnv1a_64_lower("WMSYS");
        public static final long DEPTH = FnvHash.fnv1a_64_lower("DEPTH");
        public static final long BREADTH = FnvHash.fnv1a_64_lower("BREADTH");
        public static final long SCHEDULE = FnvHash.fnv1a_64_lower("SCHEDULE");
        public static final long COMPLETION = FnvHash.fnv1a_64_lower("COMPLETION");
        public static final long RENAME = FnvHash.fnv1a_64_lower("RENAME");
        public static final long AT = FnvHash.fnv1a_64_lower("AT");
        public static final long LANGUAGE = FnvHash.fnv1a_64_lower("LANGUAGE");
        public static final long LOGFILE = FnvHash.fnv1a_64_lower("LOGFILE");
        public static final long LOG = FnvHash.fnv1a_64_lower("LOG");
        public static final long INITIAL_SIZE = FnvHash.fnv1a_64_lower("INITIAL_SIZE");
        public static final long MAX_SIZE = FnvHash.fnv1a_64_lower("MAX_SIZE");
        public static final long NODEGROUP = FnvHash.fnv1a_64_lower("NODEGROUP");
        public static final long EXTENT_SIZE = FnvHash.fnv1a_64_lower("EXTENT_SIZE");
        public static final long AUTOEXTEND_SIZE = FnvHash.fnv1a_64_lower("AUTOEXTEND_SIZE");
        public static final long FILE_BLOCK_SIZE = FnvHash.fnv1a_64_lower("FILE_BLOCK_SIZE");
        public static final long BLOCK_SIZE = FnvHash.fnv1a_64_lower("BLOCK_SIZE");
        public static final long REPLICA_NUM = FnvHash.fnv1a_64_lower("REPLICA_NUM");
        public static final long TABLET_SIZE = FnvHash.fnv1a_64_lower("TABLET_SIZE");
        public static final long PCTFREE = FnvHash.fnv1a_64_lower("PCTFREE");
        public static final long USE_BLOOM_FILTER = FnvHash.fnv1a_64_lower("USE_BLOOM_FILTER");
        public static final long SERVER = FnvHash.fnv1a_64_lower("SERVER");
        public static final long HOST = FnvHash.fnv1a_64_lower("HOST");
        public static final long ADD = FnvHash.fnv1a_64_lower("ADD");
        public static final long REMOVE = FnvHash.fnv1a_64_lower("REMOVE");
        public static final long MOVE = FnvHash.fnv1a_64_lower("MOVE");
        public static final long ALGORITHM = FnvHash.fnv1a_64_lower("ALGORITHM");
        public static final long LINEAR = FnvHash.fnv1a_64_lower("LINEAR");
        public static final long EVERY = FnvHash.fnv1a_64_lower("EVERY");
        public static final long STARTS = FnvHash.fnv1a_64_lower("STARTS");
        public static final long ENDS = FnvHash.fnv1a_64_lower("ENDS");
        public static final long BINARY = FnvHash.fnv1a_64_lower("BINARY");
        public static final long GEOMETRY = FnvHash.fnv1a_64_lower("GEOMETRY");
        public static final long ISOPEN = FnvHash.fnv1a_64_lower("ISOPEN");
        public static final long CONFLICT = FnvHash.fnv1a_64_lower("CONFLICT");
        public static final long NOTHING = FnvHash.fnv1a_64_lower("NOTHING");
        public static final long COMMIT = FnvHash.fnv1a_64_lower("COMMIT");
        public static final long DESCRIBE = FnvHash.fnv1a_64_lower("DESCRIBE");
        public static final long SQLXML = FnvHash.fnv1a_64_lower("SQLXML");
        public static final long BIT = FnvHash.fnv1a_64_lower("BIT");
        public static final long LONGBLOB = FnvHash.fnv1a_64_lower("LONGBLOB");
        public static final long RS = FnvHash.fnv1a_64_lower("RS");
        public static final long RR = FnvHash.fnv1a_64_lower("RR");
        public static final long CS = FnvHash.fnv1a_64_lower("CS");
        public static final long UR = FnvHash.fnv1a_64_lower("UR");
        public static final long INT4 = FnvHash.fnv1a_64_lower("INT4");
        public static final long VARBIT = FnvHash.fnv1a_64_lower("VARBIT");
        public static final long DECODE = FnvHash.fnv1a_64_lower("DECODE");
        public static final long IF = FnvHash.fnv1a_64_lower("IF");
        public static final long FUNCTION = FnvHash.fnv1a_64_lower("FUNCTION");
        public static final long EXTERNAL = FnvHash.fnv1a_64_lower("EXTERNAL");
        public static final long SORTED = FnvHash.fnv1a_64_lower("SORTED");
        public static final long CLUSTERED = FnvHash.fnv1a_64_lower("CLUSTERED");
        public static final long LIFECYCLE = FnvHash.fnv1a_64_lower("LIFECYCLE");
        public static final long LOCATION = FnvHash.fnv1a_64_lower("LOCATION");
        public static final long PARTITIONS = FnvHash.fnv1a_64_lower("PARTITIONS");
        public static final long FORMAT = FnvHash.fnv1a_64_lower("FORMAT");
        public static final long ENCODE = FnvHash.fnv1a_64_lower("ENCODE");
        public static final long SELECT = FnvHash.fnv1a_64_lower("SELECT");
        public static final long DELETE = FnvHash.fnv1a_64_lower("DELETE");
        public static final long UPDATE = FnvHash.fnv1a_64_lower("UPDATE");
        public static final long INSERT = FnvHash.fnv1a_64_lower("INSERT");
        public static final long REPLACE = FnvHash.fnv1a_64_lower("REPLACE");
        public static final long TRUNCATE = FnvHash.fnv1a_64_lower("TRUNCATE");
        public static final long CREATE = FnvHash.fnv1a_64_lower("CREATE");
        public static final long MERGE = FnvHash.fnv1a_64_lower("MERGE");
        public static final long SHOW = FnvHash.fnv1a_64_lower("SHOW");
        public static final long ALTER = FnvHash.fnv1a_64_lower("ALTER");
        public static final long DESC = FnvHash.fnv1a_64_lower("DESC");
        public static final long SET = FnvHash.fnv1a_64_lower("SET");
        public static final long KILL = FnvHash.fnv1a_64_lower("KILL");
        public static final long MSCK = FnvHash.fnv1a_64_lower("MSCK");
        public static final long USE = FnvHash.fnv1a_64_lower("USE");
        public static final long ROLLBACK = FnvHash.fnv1a_64_lower("ROLLBACK");
        public static final long GRANT = FnvHash.fnv1a_64_lower("GRANT");
        public static final long REVOKE = FnvHash.fnv1a_64_lower("REVOKE");
        public static final long DROP = FnvHash.fnv1a_64_lower("DROP");
        public static final long USER = FnvHash.fnv1a_64_lower("USER");
        public static final long USAGE = FnvHash.fnv1a_64_lower("USAGE");
        public static final long PCTUSED = FnvHash.fnv1a_64_lower("PCTUSED");
        public static final long OPAQUE = FnvHash.fnv1a_64_lower("OPAQUE");
        public static final long INHERITS = FnvHash.fnv1a_64_lower("INHERITS");
        public static final long DELIMITED = FnvHash.fnv1a_64_lower("DELIMITED");
        public static final long ARRAY = FnvHash.fnv1a_64_lower("ARRAY");
        public static final long SCALAR = FnvHash.fnv1a_64_lower("SCALAR");
        public static final long STRUCT = FnvHash.fnv1a_64_lower("STRUCT");
        public static final long TABLE = FnvHash.fnv1a_64_lower("TABLE");
        public static final long UNIONTYPE = FnvHash.fnv1a_64_lower("UNIONTYPE");
        public static final long TDDL = FnvHash.fnv1a_64_lower("TDDL");
        public static final long CONCURRENTLY = FnvHash.fnv1a_64_lower("CONCURRENTLY");
        public static final long TABLES = FnvHash.fnv1a_64_lower("TABLES");
        public static final long ROLES = FnvHash.fnv1a_64_lower("ROLES");
        public static final long NOCACHE = FnvHash.fnv1a_64_lower("NOCACHE");
        public static final long NOPARALLEL = FnvHash.fnv1a_64_lower("NOPARALLEL");
        public static final long EXIST = FnvHash.fnv1a_64_lower("EXIST");
        public static final long EXISTS = FnvHash.fnv1a_64_lower("EXISTS");
        public static final long SOUNDS = FnvHash.fnv1a_64_lower("SOUNDS");
        public static final long TBLPROPERTIES = FnvHash.fnv1a_64_lower("TBLPROPERTIES");
        public static final long TABLEGROUP = FnvHash.fnv1a_64_lower("TABLEGROUP");
        public static final long TABLEGROUPS = FnvHash.fnv1a_64_lower("TABLEGROUPS");
        public static final long DIMENSION = FnvHash.fnv1a_64_lower("DIMENSION");
        public static final long OPTIONS = FnvHash.fnv1a_64_lower("OPTIONS");
        public static final long OPTIMIZER = FnvHash.fnv1a_64_lower("OPTIMIZER");
        public static final long FULLTEXT = FnvHash.fnv1a_64_lower("FULLTEXT");
        public static final long SPATIAL = FnvHash.fnv1a_64_lower("SPATIAL");
        public static final long SUBPARTITION_AVAILABLE_PARTITION_NUM = FnvHash.fnv1a_64_lower("SUBPARTITION_AVAILABLE_PARTITION_NUM");
        public static final long EXTRA = FnvHash.fnv1a_64_lower("EXTRA");
        public static final long DATABASES = FnvHash.fnv1a_64_lower("DATABASES");
        public static final long COLUMNS = FnvHash.fnv1a_64_lower("COLUMNS");
        public static final long PROCESS = FnvHash.fnv1a_64_lower("PROCESS");
        public static final long PROCESSLIST = FnvHash.fnv1a_64_lower("PROCESSLIST");
        public static final long MPP = FnvHash.fnv1a_64_lower("MPP");
        public static final long SERDE = FnvHash.fnv1a_64_lower("SERDE");
        public static final long SORT = FnvHash.fnv1a_64_lower("SORT");
        public static final long ZORDER = FnvHash.fnv1a_64_lower("ZORDER");
        public static final long FIELDS = FnvHash.fnv1a_64_lower("FIELDS");
        public static final long COLLECTION = FnvHash.fnv1a_64_lower("COLLECTION");
        public static final long SKEWED = FnvHash.fnv1a_64_lower("SKEWED");
        public static final long SYMBOL = FnvHash.fnv1a_64_lower("SYMBOL");
        public static final long LOAD = FnvHash.fnv1a_64_lower("LOAD");
        public static final long VIEWS = FnvHash.fnv1a_64_lower("VIEWS");
        public static final long SUBSTR = FnvHash.fnv1a_64_lower("SUBSTR");
        public static final long TO_BASE64 = FnvHash.fnv1a_64_lower("TO_BASE64");
        public static final long REGEXP_SUBSTR = FnvHash.fnv1a_64_lower("REGEXP_SUBSTR");
        public static final long REGEXP_COUNT = FnvHash.fnv1a_64_lower("REGEXP_COUNT");
        public static final long REGEXP_EXTRACT = FnvHash.fnv1a_64_lower("REGEXP_EXTRACT");
        public static final long REGEXP_EXTRACT_ALL = FnvHash.fnv1a_64_lower("REGEXP_EXTRACT_ALL");
        public static final long REGEXP_LIKE = FnvHash.fnv1a_64_lower("REGEXP_LIKE");
        public static final long REGEXP_REPLACE = FnvHash.fnv1a_64_lower("REGEXP_REPLACE");
        public static final long REGEXP_SPLIT = FnvHash.fnv1a_64_lower("REGEXP_SPLIT");
        public static final long CONCAT = FnvHash.fnv1a_64_lower("CONCAT");
        public static final long LCASE = FnvHash.fnv1a_64_lower("LCASE");
        public static final long UCASE = FnvHash.fnv1a_64_lower("UCASE");
        public static final long LOWER = FnvHash.fnv1a_64_lower("LOWER");
        public static final long UPPER = FnvHash.fnv1a_64_lower("UPPER");
        public static final long LENGTH = FnvHash.fnv1a_64_lower("LENGTH");
        public static final long LOCATE = FnvHash.fnv1a_64_lower("LOCATE");
        public static final long UDF_SYS_ROWCOUNT = FnvHash.fnv1a_64_lower("UDF_SYS_ROWCOUNT");
        public static final long CHAR_LENGTH = FnvHash.fnv1a_64_lower("CHAR_LENGTH");
        public static final long CHARACTER_LENGTH = FnvHash.fnv1a_64_lower("CHARACTER_LENGTH");
        public static final long SUBSTRING = FnvHash.fnv1a_64_lower("SUBSTRING");
        public static final long SUBSTRING_INDEX = FnvHash.fnv1a_64_lower("SUBSTRING_INDEX");
        public static final long LEFT = FnvHash.fnv1a_64_lower("LEFT");
        public static final long RIGHT = FnvHash.fnv1a_64_lower("RIGHT");
        public static final long RTRIM = FnvHash.fnv1a_64_lower("RTRIM");
        public static final long LEN = FnvHash.fnv1a_64_lower("LEN");
        public static final long GREAST = FnvHash.fnv1a_64_lower("GREAST");
        public static final long LEAST = FnvHash.fnv1a_64_lower("LEAST");
        public static final long IFNULL = FnvHash.fnv1a_64_lower("IFNULL");
        public static final long NULLIF = FnvHash.fnv1a_64_lower("NULLIF");
        public static final long GREATEST = FnvHash.fnv1a_64_lower("GREATEST");
        public static final long COALESCE = FnvHash.fnv1a_64_lower("COALESCE");
        public static final long ISNULL = FnvHash.fnv1a_64_lower("ISNULL");
        public static final long NVL = FnvHash.fnv1a_64_lower("NVL");
        public static final long NVL2 = FnvHash.fnv1a_64_lower("NVL2");
        public static final long TO_DATE = FnvHash.fnv1a_64_lower("TO_DATE");
        public static final long DATEADD = FnvHash.fnv1a_64_lower("DATEADD");
        public static final long DATE_ADD = FnvHash.fnv1a_64_lower("DATE_ADD");
        public static final long ADDDATE = FnvHash.fnv1a_64_lower("ADDDATE");
        public static final long DATE_SUB = FnvHash.fnv1a_64_lower("DATE_SUB");
        public static final long SUBDATE = FnvHash.fnv1a_64_lower("SUBDATE");
        public static final long DATE_PARSE = FnvHash.fnv1a_64_lower("DATE_PARSE");
        public static final long STR_TO_DATE = FnvHash.fnv1a_64_lower("STR_TO_DATE");
        public static final long CLOTHES_FEATURE_EXTRACT_V1 = FnvHash.fnv1a_64_lower("CLOTHES_FEATURE_EXTRACT_V1");
        public static final long CLOTHES_ATTRIBUTE_EXTRACT_V1 = FnvHash.fnv1a_64_lower("CLOTHES_ATTRIBUTE_EXTRACT_V1");
        public static final long GENERIC_FEATURE_EXTRACT_V1 = FnvHash.fnv1a_64_lower("GENERIC_FEATURE_EXTRACT_V1");
        public static final long FACE_FEATURE_EXTRACT_V1 = FnvHash.fnv1a_64_lower("FACE_FEATURE_EXTRACT_V1");
        public static final long TEXT_FEATURE_EXTRACT_V1 = FnvHash.fnv1a_64_lower("TEXT_FEATURE_EXTRACT_V1");
        public static final long JSON_TABLE = FnvHash.fnv1a_64_lower("JSON_TABLE");
        public static final long JSON_EXTRACT = FnvHash.fnv1a_64_lower("JSON_EXTRACT");
        public static final long JSON_EXTRACT_SCALAR = FnvHash.fnv1a_64_lower("json_extract_scalar");
        public static final long JSON_ARRAY_GET = FnvHash.fnv1a_64_lower("JSON_ARRAY_GET");
        public static final long ADD_MONTHS = FnvHash.fnv1a_64_lower("ADD_MONTHS");
        public static final long ABS = FnvHash.fnv1a_64_lower("ABS");
        public static final long ACOS = FnvHash.fnv1a_64_lower("ACOS");
        public static final long ASIN = FnvHash.fnv1a_64_lower("ASIN");
        public static final long ATAN = FnvHash.fnv1a_64_lower("ATAN");
        public static final long ATAN2 = FnvHash.fnv1a_64_lower("ATAN2");
        public static final long COS = FnvHash.fnv1a_64_lower("COS");
        public static final long FLOOR = FnvHash.fnv1a_64_lower("FLOOR");
        public static final long CEIL = FnvHash.fnv1a_64_lower("CEIL");
        public static final long SQRT = FnvHash.fnv1a_64_lower("SQRT");
        public static final long LEAD = FnvHash.fnv1a_64_lower("LEAD");
        public static final long LAG = FnvHash.fnv1a_64_lower("LAG");
        public static final long CEILING = FnvHash.fnv1a_64_lower("CEILING");
        public static final long POWER = FnvHash.fnv1a_64_lower("POWER");
        public static final long EXP = FnvHash.fnv1a_64_lower("EXP");
        public static final long LN = FnvHash.fnv1a_64_lower("LN");
        public static final long LOG10 = FnvHash.fnv1a_64_lower("LOG10");
        public static final long INTERVAL = FnvHash.fnv1a_64_lower("INTERVAL");
        public static final long FROM_DAYS = FnvHash.fnv1a_64_lower("FROM_DAYS");
        public static final long TO_DAYS = FnvHash.fnv1a_64_lower("TO_DAYS");
        public static final long BIGINT = FnvHash.fnv1a_64_lower("BIGINT");
        public static final long LONGLONG = FnvHash.fnv1a_64_lower("LONGLONG");
        public static final long DISCARD = FnvHash.fnv1a_64_lower("DISCARD");
        public static final long EXCHANGE = FnvHash.fnv1a_64_lower("EXCHANGE");
        public static final long ROLE = FnvHash.fnv1a_64_lower("ROLE");
        public static final long OVERWRITE = FnvHash.fnv1a_64_lower("OVERWRITE");
        public static final long NO = FnvHash.fnv1a_64_lower("NO");
        public static final long CATALOG = FnvHash.fnv1a_64_lower("CATALOG");
        public static final long CATALOGS = FnvHash.fnv1a_64_lower("CATALOGS");
        public static final long FUNCTIONS = FnvHash.fnv1a_64_lower("FUNCTIONS");
        public static final long SCHEMAS = FnvHash.fnv1a_64_lower("SCHEMAS");
        public static final long CHANGE = FnvHash.fnv1a_64_lower("CHANGE");
        public static final long MODIFY = FnvHash.fnv1a_64_lower("MODIFY");
        public static final long BEGIN = FnvHash.fnv1a_64_lower("BEGIN");
        public static final long PATH = FnvHash.fnv1a_64_lower("PATH");
        public static final long ENCRYPTION = FnvHash.fnv1a_64_lower("ENCRYPTION");
        public static final long COMPRESSION = FnvHash.fnv1a_64_lower("COMPRESSION");
        public static final long KEY_BLOCK_SIZE = FnvHash.fnv1a_64_lower("KEY_BLOCK_SIZE");
        public static final long CHECKSUM = FnvHash.fnv1a_64_lower("CHECKSUM");
        public static final long CONNECTION = FnvHash.fnv1a_64_lower("CONNECTION");
        public static final long DATASOURCES = FnvHash.fnv1a_64_lower("DATASOURCES");
        public static final long NODE = FnvHash.fnv1a_64_lower("NODE");
        public static final long HELP = FnvHash.fnv1a_64_lower("HELP");
        public static final long BROADCASTS = FnvHash.fnv1a_64_lower("BROADCASTS");
        public static final long MASTER = FnvHash.fnv1a_64_lower("MASTER");
        public static final long SLAVE = FnvHash.fnv1a_64_lower("SLAVE");
        public static final long SQL_DELAY_CUTOFF = FnvHash.fnv1a_64_lower("SQL_DELAY_CUTOFF");
        public static final long SOCKET_TIMEOUT = FnvHash.fnv1a_64_lower("SOCKET_TIMEOUT");
        public static final long FORBID_EXECUTE_DML_ALL = FnvHash.fnv1a_64_lower("FORBID_EXECUTE_DML_ALL");
        public static final long SCAN = FnvHash.fnv1a_64_lower("SCAN");
        public static final long NOLOGFILE = FnvHash.fnv1a_64_lower("NOLOGFILE");
        public static final long NOBADFILE = FnvHash.fnv1a_64_lower("NOBADFILE");
        public static final long TERMINATED = FnvHash.fnv1a_64_lower("TERMINATED");
        public static final long LTRIM = FnvHash.fnv1a_64_lower("LTRIM");
        public static final long MISSING = FnvHash.fnv1a_64_lower("MISSING");
        public static final long SUBPARTITION = FnvHash.fnv1a_64_lower("SUBPARTITION");
        public static final long SUBPARTITIONS = FnvHash.fnv1a_64_lower("SUBPARTITIONS");
        public static final long GENERATED = FnvHash.fnv1a_64_lower("GENERATED");
        public static final long ALWAYS = FnvHash.fnv1a_64_lower("ALWAYS");
        public static final long VISIBLE = FnvHash.fnv1a_64_lower("VISIBLE");
        public static final long INCLUDING = FnvHash.fnv1a_64_lower("INCLUDING");
        public static final long EXCLUDE = FnvHash.fnv1a_64_lower("EXCLUDE");
        public static final long EXCLUDING = FnvHash.fnv1a_64_lower("EXCLUDING");
        public static final long ROUTINE = FnvHash.fnv1a_64_lower("ROUTINE");
        public static final long IDENTIFIED = FnvHash.fnv1a_64_lower("IDENTIFIED");
        public static final long DELIMITER = FnvHash.fnv1a_64_lower("DELIMITER");
        public static final long UNKNOWN = FnvHash.fnv1a_64_lower("UNKNOWN");
        public static final long WEIGHT_STRING = FnvHash.fnv1a_64_lower("WEIGHT_STRING");
        public static final long REVERSE = FnvHash.fnv1a_64_lower("REVERSE");
        public static final long DATE_FORMAT = FnvHash.fnv1a_64_lower("DATE_FORMAT");
        public static final long DAY_OF_WEEK = FnvHash.fnv1a_64_lower("DAY_OF_WEEK");
        public static final long DATEDIFF = FnvHash.fnv1a_64_lower("DATEDIFF");
        public static final long GET_FORMAT = FnvHash.fnv1a_64_lower("GET_FORMAT");
        public static final long TIMESTAMPDIFF = FnvHash.fnv1a_64_lower("TIMESTAMPDIFF");
        public static final long MONTHNAME = FnvHash.fnv1a_64_lower("MONTHNAME");
        public static final long PERIOD_ADD = FnvHash.fnv1a_64_lower("PERIOD_ADD");
        public static final long PERIOD_DIFF = FnvHash.fnv1a_64_lower("PERIOD_DIFF");
        public static final long ROUND = FnvHash.fnv1a_64_lower("ROUND");
        public static final long DBPARTITION = FnvHash.fnv1a_64_lower("DBPARTITION");
        public static final long TBPARTITION = FnvHash.fnv1a_64_lower("TBPARTITION");
        public static final long EXTPARTITION = FnvHash.fnv1a_64_lower("EXTPARTITION");
        public static final long STARTWITH = FnvHash.fnv1a_64_lower("STARTWITH");
        public static final long TBPARTITIONS = FnvHash.fnv1a_64_lower("TBPARTITIONS");
        public static final long DBPARTITIONS = FnvHash.fnv1a_64_lower("DBPARTITIONS");
        public static final long PARTITIONED = FnvHash.fnv1a_64_lower("PARTITIONED");
        public static final long PARALLEL = FnvHash.fnv1a_64_lower("PARALLEL");
        public static final long ALLOW = FnvHash.fnv1a_64_lower("ALLOW");
        public static final long DISALLOW = FnvHash.fnv1a_64_lower("DISALLOW");
        public static final long PIVOT = FnvHash.fnv1a_64_lower("PIVOT");
        public static final long MODEL = FnvHash.fnv1a_64_lower("MODEL");
        public static final long KEEP = FnvHash.fnv1a_64_lower("KEEP");
        public static final long REFERENCE = FnvHash.fnv1a_64_lower("REFERENCE");
        public static final long RETURN = FnvHash.fnv1a_64_lower("RETURN");
        public static final long RETURNS = FnvHash.fnv1a_64_lower("RETURNS");
        public static final long ROWTYPE = FnvHash.fnv1a_64_lower("ROWTYPE");
        public static final long WINDOW = FnvHash.fnv1a_64_lower("WINDOW");
        public static final long MULTIVALUE = FnvHash.fnv1a_64_lower("MULTIVALUE");
        public static final long OPTIONALLY = FnvHash.fnv1a_64_lower("OPTIONALLY");
        public static final long ENCLOSED = FnvHash.fnv1a_64_lower("ENCLOSED");
        public static final long ESCAPED = FnvHash.fnv1a_64_lower("ESCAPED");
        public static final long ESCAPE = FnvHash.fnv1a_64_lower("ESCAPE");
        public static final long LINES = FnvHash.fnv1a_64_lower("LINES");
        public static final long STARTING = FnvHash.fnv1a_64_lower("STARTING");
        public static final long DISTRIBUTE = FnvHash.fnv1a_64_lower("DISTRIBUTE");
        public static final long DISTRIBUTED = FnvHash.fnv1a_64_lower("DISTRIBUTED");
        public static final long CLUSTER = FnvHash.fnv1a_64_lower("CLUSTER");
        public static final long RUNNING = FnvHash.fnv1a_64_lower("RUNNING");
        public static final long CLUSTERING = FnvHash.fnv1a_64_lower("CLUSTERING");
        public static final long PCTVERSION = FnvHash.fnv1a_64_lower("PCTVERSION");
        public static final long IDENTITY = FnvHash.fnv1a_64_lower("IDENTITY");
        public static final long INCREMENT = FnvHash.fnv1a_64_lower("INCREMENT");
        public static final long MINVALUE = FnvHash.fnv1a_64_lower("MINVALUE");
        public static final long ANN = FnvHash.fnv1a_64_lower("ANN");
        public static final long ANN_DISTANCE = FnvHash.fnv1a_64_lower("ANN_DISTANCE");
        public static final long SUPPLEMENTAL = FnvHash.fnv1a_64_lower("SUPPLEMENTAL");
        public static final long SUBSTITUTABLE = FnvHash.fnv1a_64_lower("SUBSTITUTABLE");
        public static final long BASICFILE = FnvHash.fnv1a_64_lower("BASICFILE");
        public static final long IN_MEMORY_METADATA = FnvHash.fnv1a_64_lower("IN_MEMORY_METADATA");
        public static final long CURSOR_SPECIFIC_SEGMENT = FnvHash.fnv1a_64_lower("CURSOR_SPECIFIC_SEGMENT");
        public static final long DEFER = FnvHash.fnv1a_64_lower("DEFER");
        public static final long UNDO_LOG_LIMIT = FnvHash.fnv1a_64_lower("UNDO_LOG_LIMIT");
        public static final long DBPROPERTIES = FnvHash.fnv1a_64_lower("DBPROPERTIES");
        public static final long ANNINDEX = FnvHash.fnv1a_64_lower("ANNINDEX");
        public static final long RTTYPE = FnvHash.fnv1a_64_lower("RTTYPE");
        public static final long DISTANCE = FnvHash.fnv1a_64_lower("DISTANCE");
        public static final long IDXPROPERTIES = FnvHash.fnv1a_64_lower("IDXPROPERTIES");
        public static final long RECOVER = FnvHash.fnv1a_64_lower("RECOVER");
        public static final long BACKUP = FnvHash.fnv1a_64_lower("BACKUP");
        public static final long RESTORE = FnvHash.fnv1a_64_lower("RESTORE");
        public static final long EXSTORE = FnvHash.fnv1a_64_lower("EXSTORE");
        public static final long UNDO = FnvHash.fnv1a_64_lower("UNDO");
        public static final long NOSCAN = FnvHash.fnv1a_64_lower("NOSCAN");
        public static final long EXTENDED = FnvHash.fnv1a_64_lower("EXTENDED");
        public static final long FORMATTED = FnvHash.fnv1a_64_lower("FORMATTED");
        public static final long DEPENDENCY = FnvHash.fnv1a_64_lower("DEPENDENCY");
        public static final long AUTHORIZATION = FnvHash.fnv1a_64_lower("AUTHORIZATION");
        public static final long ANALYZE = FnvHash.fnv1a_64_lower("ANALYZE");
        public static final long EXPORT = FnvHash.fnv1a_64_lower("EXPORT");
        public static final long IMPORT = FnvHash.fnv1a_64_lower("IMPORT");
        public static final long TABLESAMPLE = FnvHash.fnv1a_64_lower("TABLESAMPLE");
        public static final long BUCKET = FnvHash.fnv1a_64_lower("BUCKET");
        public static final long BUCKETS = FnvHash.fnv1a_64_lower("BUCKETS");
        public static final long UNARCHIVE = FnvHash.fnv1a_64_lower("UNARCHIVE");
        public static final long SEQUENCES = FnvHash.fnv1a_64_lower("SEQUENCES");
        public static final long OUTLINE = FnvHash.fnv1a_64_lower("OUTLINE");
        public static final long ORD = FnvHash.fnv1a_64_lower("ORD");
        public static final long SPACE = FnvHash.fnv1a_64_lower("SPACE");
        public static final long REPEAT = FnvHash.fnv1a_64_lower("REPEAT");
        public static final long SLOW = FnvHash.fnv1a_64_lower("SLOW");
        public static final long PLAN = FnvHash.fnv1a_64_lower("PLAN");
        public static final long PLANCACHE = FnvHash.fnv1a_64_lower("PLANCACHE");
        public static final long RECYCLEBIN = FnvHash.fnv1a_64_lower("RECYCLEBIN");
        public static final long PURGE = FnvHash.fnv1a_64_lower("PURGE");
        public static final long FLASHBACK = FnvHash.fnv1a_64_lower("FLASHBACK");
        public static final long INPUTFORMAT = FnvHash.fnv1a_64_lower("INPUTFORMAT");
        public static final long OUTPUTFORMAT = FnvHash.fnv1a_64_lower("OUTPUTFORMAT");
        public static final long DUMP = FnvHash.fnv1a_64_lower("DUMP");
        public static final long BROADCAST = FnvHash.fnv1a_64_lower("BROADCAST");
        public static final long GROUP = FnvHash.fnv1a_64_lower("GROUP");
        public static final long GROUPING = FnvHash.fnv1a_64_lower("GROUPING");
        public static final long WITH = FnvHash.fnv1a_64_lower("WITH");
        public static final long FROM = FnvHash.fnv1a_64_lower("FROM");
        public static final long WHO = FnvHash.fnv1a_64_lower("WHO");
        public static final long WHOAMI = FnvHash.fnv1a_64_lower("WHOAMI");
        public static final long GRANTS = FnvHash.fnv1a_64_lower("GRANTS");
        public static final long STATISTIC = FnvHash.fnv1a_64_lower("STATISTIC");
        public static final long STATISTIC_LIST = FnvHash.fnv1a_64_lower("STATISTIC_LIST");
        public static final long STATUS = FnvHash.fnv1a_64_lower("STATUS");
        public static final long FULL = FnvHash.fnv1a_64_lower("FULL");
        public static final long STATS = FnvHash.fnv1a_64_lower("STATS");
        public static final long OUTLINES = FnvHash.fnv1a_64_lower("OUTLINES");
        public static final long VERSION = FnvHash.fnv1a_64_lower("VERSION");
        public static final long CONFIG = FnvHash.fnv1a_64_lower("CONFIG");
        public static final long USERS = FnvHash.fnv1a_64_lower("USERS");
        public static final long PHYSICAL_PROCESSLIST = FnvHash.fnv1a_64_lower("PHYSICAL_PROCESSLIST");
        public static final long PHYSICAL = FnvHash.fnv1a_64_lower("PHYSICAL");
        public static final long DISTANCEMEASURE = FnvHash.fnv1a_64_lower("DISTANCEMEASURE");
        public static final long UNIT = FnvHash.fnv1a_64_lower("UNIT");
        public static final long DB = FnvHash.fnv1a_64_lower("DB");
        public static final long STEP = FnvHash.fnv1a_64_lower("STEP");
        public static final long HEX = FnvHash.fnv1a_64_lower("HEX");
        public static final long UNHEX = FnvHash.fnv1a_64_lower("UNHEX");
        public static final long POLICY = FnvHash.fnv1a_64_lower("POLICY");
        public static final long QUERY_TASK = FnvHash.fnv1a_64_lower("QUERY_TASK");
        public static final long UUID = FnvHash.fnv1a_64_lower("UUID");
        public static final long PCTTHRESHOLD = FnvHash.fnv1a_64_lower("PCTTHRESHOLD");
        public static final long UNUSABLE = FnvHash.fnv1a_64_lower("UNUSABLE");
        public static final long FILTER = FnvHash.fnv1a_64_lower("FILTER");
        public static final long BIT_COUNT = FnvHash.fnv1a_64_lower("BIT_COUNT");
        public static final long STDDEV_SAMP = FnvHash.fnv1a_64_lower("STDDEV_SAMP");
        public static final long PERCENT_RANK = FnvHash.fnv1a_64_lower("PERCENT_RANK");
        public static final long DENSE_RANK = FnvHash.fnv1a_64_lower("DENSE_RANK");
        public static final long CUME_DIST = FnvHash.fnv1a_64_lower("CUME_DIST");
        public static final long CARDINALITY = FnvHash.fnv1a_64_lower("CARDINALITY");
        public static final long TRY_CAST = FnvHash.fnv1a_64_lower("TRY_CAST");
        public static final long COVERING = FnvHash.fnv1a_64_lower("COVERING");
        public static final long CHARFILTER = FnvHash.fnv1a_64_lower("CHARFILTER");
        public static final long CHARFILTERS = FnvHash.fnv1a_64_lower("CHARFILTERS");
        public static final long TOKENIZER = FnvHash.fnv1a_64_lower("TOKENIZER");
        public static final long TOKENIZERS = FnvHash.fnv1a_64_lower("TOKENIZERS");
        public static final long TOKENFILTER = FnvHash.fnv1a_64_lower("TOKENFILTER");
        public static final long TOKENFILTERS = FnvHash.fnv1a_64_lower("TOKENFILTERS");
        public static final long ANALYZER = FnvHash.fnv1a_64_lower("ANALYZER");
        public static final long ANALYZERS = FnvHash.fnv1a_64_lower("ANALYZERS");
        public static final long DICTIONARY = FnvHash.fnv1a_64_lower("DICTIONARY");
        public static final long DICTIONARIES = FnvHash.fnv1a_64_lower("DICTIONARIES");
        public static final long QUERY = FnvHash.fnv1a_64_lower("QUERY");
        public static final long META = FnvHash.fnv1a_64_lower("META");
        public static final long TRY = FnvHash.fnv1a_64_lower("TRY");
        public static final long D = FnvHash.fnv1a_64_lower("D");
        public static final long T = FnvHash.fnv1a_64_lower("T");
        public static final long TS = FnvHash.fnv1a_64_lower("TS");
        public static final long FN = FnvHash.fnv1a_64_lower("FN");
        public static final long COPY = FnvHash.fnv1a_64_lower("COPY");
        public static final long CREDENTIALS = FnvHash.fnv1a_64_lower("CREDENTIALS");
        public static final long ACCESS_KEY_ID = FnvHash.fnv1a_64_lower("ACCESS_KEY_ID");
        public static final long ACCESS_KEY_SECRET = FnvHash.fnv1a_64_lower("ACCESS_KEY_SECRET");
        public static final long BERNOULLI = FnvHash.fnv1a_64_lower("BERNOULLI");
        public static final long SYSTEM = FnvHash.fnv1a_64_lower("SYSTEM");
        public static final long SYNC = FnvHash.fnv1a_64_lower("SYNC");
        public static final long INIT = FnvHash.fnv1a_64_lower("INIT");
        public static final long BD = FnvHash.fnv1a_64_lower("BD");
        public static final long FORMAT_DATETIME = FnvHash.fnv1a_64_lower("FORMAT_DATETIME");
        public static final long WITHIN = FnvHash.fnv1a_64_lower("WITHIN");
        public static final long RULE = FnvHash.fnv1a_64_lower("RULE");
        public static final long EXPLAIN = FnvHash.fnv1a_64_lower("EXPLAIN");
        public static final long ISOLATION = FnvHash.fnv1a_64_lower("ISOLATION");
        public static final long READ = FnvHash.fnv1a_64_lower("READ");
        public static final long UNCOMMITTED = FnvHash.fnv1a_64_lower("UNCOMMITTED");
        public static final long COMMITTED = FnvHash.fnv1a_64_lower("COMMITTED");
        public static final long REPEATABLE = FnvHash.fnv1a_64_lower("REPEATABLE");
        public static final long SERIALIZABLE = FnvHash.fnv1a_64_lower("SERIALIZABLE");
        public static final long _LATIN1 = FnvHash.fnv1a_64_lower("_LATIN1");
        public static final long _GBK = FnvHash.fnv1a_64_lower("_GBK");
        public static final long _BIG5 = FnvHash.fnv1a_64_lower("_BIG5");
        public static final long _UTF8 = FnvHash.fnv1a_64_lower("_UTF8");
        public static final long _UTF8MB4 = FnvHash.fnv1a_64_lower("_UTF8MB4");
        public static final long _UTF16 = FnvHash.fnv1a_64_lower("_UTF16");
        public static final long _UTF16LE = FnvHash.fnv1a_64_lower("_UTF16LE");
        public static final long _UTF32 = FnvHash.fnv1a_64_lower("_UTF32");
        public static final long _UCS2 = FnvHash.fnv1a_64_lower("_UCS2");
        public static final long _UJIS = FnvHash.fnv1a_64_lower("_UJIS");
        public static final long X = FnvHash.fnv1a_64_lower("X");
        public static final long TRANSFORM = FnvHash.fnv1a_64_lower("TRANSFORM");
        public static final long NESTED = FnvHash.fnv1a_64_lower("NESTED");
        public static final long RESTART = FnvHash.fnv1a_64_lower("RESTART");
        public static final long ASOF = FnvHash.fnv1a_64_lower("ASOF");
        public static final long JSON_SET = FnvHash.fnv1a_64_lower("JSON_SET");
        public static final long JSONB_SET = FnvHash.fnv1a_64_lower("JSONB_SET");
        public static final long TUNNEL = FnvHash.fnv1a_64_lower("TUNNEL");
        public static final long DOWNLOAD = FnvHash.fnv1a_64_lower("DOWNLOAD");
        public static final long UPLOAD = FnvHash.fnv1a_64_lower("UPLOAD");
        public static final long CLONE = FnvHash.fnv1a_64_lower("CLONE");
        public static final long INSTALL = FnvHash.fnv1a_64_lower("INSTALL");
        public static final long UNLOAD = FnvHash.fnv1a_64_lower("UNLOAD");
    }
}
