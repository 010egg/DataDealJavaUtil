// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.util;

import com.alibaba.druid.support.logging.LogFactory;
import java.net.URLConnection;
import java.io.IOException;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import com.alibaba.druid.support.logging.Log;

public class HttpClientUtils
{
    private static final Log LOG;
    
    public static boolean post(final String serverUrl, final String data, final long timeout) {
        StringBuilder responseBuilder = null;
        BufferedReader reader = null;
        OutputStreamWriter wr = null;
        try {
            final URL url = new URL(serverUrl);
            final URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            conn.setConnectTimeout(5000);
            wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();
            if (HttpClientUtils.LOG.isDebugEnabled()) {
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                responseBuilder = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    responseBuilder.append(line).append("\n");
                }
                HttpClientUtils.LOG.debug(responseBuilder.toString());
            }
        }
        catch (IOException e) {
            HttpClientUtils.LOG.error("", e);
            if (wr != null) {
                try {
                    wr.close();
                }
                catch (IOException e) {
                    HttpClientUtils.LOG.error("close error", e);
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (IOException e) {
                    HttpClientUtils.LOG.error("close error", e);
                }
            }
        }
        finally {
            if (wr != null) {
                try {
                    wr.close();
                }
                catch (IOException e2) {
                    HttpClientUtils.LOG.error("close error", e2);
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (IOException e2) {
                    HttpClientUtils.LOG.error("close error", e2);
                }
            }
        }
        return false;
    }
    
    public static void main(final String[] args) {
        post("http://www.alibaba.com/trade/search", "fsb=y&IndexArea=product_en&CatId=&SearchText=test", 6000L);
    }
    
    static {
        LOG = LogFactory.getLog(HttpClientUtils.class);
    }
}
