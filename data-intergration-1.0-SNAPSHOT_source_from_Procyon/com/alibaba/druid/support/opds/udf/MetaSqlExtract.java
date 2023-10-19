// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.opds.udf;

import org.apache.commons.lang.StringEscapeUtils;
import com.aliyun.odps.udf.UDF;

public class MetaSqlExtract extends UDF
{
    public String evaluate(final String xml) {
        if (xml == null || xml.length() == 0) {
            return null;
        }
        int p0 = xml.indexOf("<Query>");
        if (p0 == -1) {
            return null;
        }
        p0 += "<Query>".length();
        final int p2 = xml.indexOf("</Query>", p0);
        if (p2 == -1) {
            return null;
        }
        final String sql = xml.substring(p0, p2);
        final int p3 = xml.indexOf("<![CDATA[");
        if (p3 == -1) {
            return StringEscapeUtils.unescapeXml(sql);
        }
        if (sql.length() > "<![CDATA[".length() + 3) {
            return sql.substring("<![CDATA[".length(), sql.length() - 3);
        }
        return null;
    }
}
