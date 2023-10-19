// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.http.stat;

import java.util.LinkedHashMap;
import java.util.Map;
import com.alibaba.druid.support.monitor.annotation.AggregateType;
import com.alibaba.druid.support.monitor.annotation.MField;
import com.alibaba.druid.support.monitor.annotation.MTable;

@MTable(name = "druid_webapp")
public class WebAppStatValue
{
    @MField(groupBy = true, aggregate = AggregateType.None)
    String contextPath;
    @MField(aggregate = AggregateType.Last)
    int runningCount;
    @MField(aggregate = AggregateType.Max)
    int concurrentMax;
    @MField(aggregate = AggregateType.Sum)
    long requestCount;
    @MField(aggregate = AggregateType.Last)
    long sessionCount;
    @MField(aggregate = AggregateType.Sum)
    long jdbcFetchRowCount;
    @MField(aggregate = AggregateType.Sum)
    long jdbcUpdateCount;
    @MField(aggregate = AggregateType.Sum)
    long jdbcExecuteCount;
    @MField(aggregate = AggregateType.Sum)
    long jdbcExecuteTimeNano;
    @MField(aggregate = AggregateType.Sum)
    long jdbcCommitCount;
    @MField(aggregate = AggregateType.Sum)
    long jdbcRollbackCount;
    @MField(aggregate = AggregateType.Sum)
    long osMacOSXCount;
    @MField(aggregate = AggregateType.Sum)
    long osWindowsCount;
    @MField(aggregate = AggregateType.Sum)
    long osLinuxCount;
    @MField(aggregate = AggregateType.Sum)
    long osSymbianCount;
    @MField(aggregate = AggregateType.Sum)
    long osFreeBSDCount;
    @MField(aggregate = AggregateType.Sum)
    long osOpenBSDCount;
    @MField(aggregate = AggregateType.Sum)
    long osAndroidCount;
    @MField(aggregate = AggregateType.Sum)
    long osWindows98Count;
    @MField(aggregate = AggregateType.Sum)
    long osWindowsXPCount;
    @MField(aggregate = AggregateType.Sum)
    long osWindows2000Count;
    @MField(aggregate = AggregateType.Sum)
    long osWindowsVistaCount;
    @MField(aggregate = AggregateType.Sum)
    long osWindows7Count;
    @MField(aggregate = AggregateType.Sum)
    long osWindows8Count;
    @MField(aggregate = AggregateType.Sum)
    long osAndroid15Count;
    @MField(aggregate = AggregateType.Sum)
    long osAndroid16Count;
    @MField(aggregate = AggregateType.Sum)
    long osAndroid20Count;
    @MField(aggregate = AggregateType.Sum)
    long osAndroid21Count;
    @MField(aggregate = AggregateType.Sum)
    long osAndroid22Count;
    @MField(aggregate = AggregateType.Sum)
    long osAndroid23Count;
    @MField(aggregate = AggregateType.Sum)
    long osAndroid30Count;
    @MField(aggregate = AggregateType.Sum)
    long osAndroid31Count;
    @MField(aggregate = AggregateType.Sum)
    long osAndroid32Count;
    @MField(aggregate = AggregateType.Sum)
    long osAndroid40Count;
    @MField(aggregate = AggregateType.Sum)
    long osAndroid41Count;
    @MField(aggregate = AggregateType.Sum)
    long osAndroid42Count;
    @MField(aggregate = AggregateType.Sum)
    long osAndroid43Count;
    @MField(aggregate = AggregateType.Sum)
    long osLinuxUbuntuCount;
    @MField(aggregate = AggregateType.Sum)
    long browserIECount;
    @MField(aggregate = AggregateType.Sum)
    long browserFirefoxCount;
    @MField(aggregate = AggregateType.Sum)
    long browserChromeCount;
    @MField(aggregate = AggregateType.Sum)
    long browserSafariCount;
    @MField(aggregate = AggregateType.Sum)
    long browserOperaCount;
    @MField(aggregate = AggregateType.Sum)
    long browserIE5Count;
    @MField(aggregate = AggregateType.Sum)
    long browserIE6Count;
    @MField(aggregate = AggregateType.Sum)
    long browserIE7Count;
    @MField(aggregate = AggregateType.Sum)
    long browserIE8Count;
    @MField(aggregate = AggregateType.Sum)
    long browserIE9Count;
    @MField(aggregate = AggregateType.Sum)
    long browserIE10Count;
    @MField(aggregate = AggregateType.Sum)
    long browser360SECount;
    @MField(aggregate = AggregateType.Sum)
    long deviceAndroidCount;
    @MField(aggregate = AggregateType.Sum)
    long deviceIpadCount;
    @MField(aggregate = AggregateType.Sum)
    long deviceIphoneCount;
    @MField(aggregate = AggregateType.Sum)
    long deviceWindowsPhoneCount;
    @MField(aggregate = AggregateType.Sum)
    long botCount;
    @MField(aggregate = AggregateType.Sum)
    long botBaiduCount;
    @MField(aggregate = AggregateType.Sum)
    long botYoudaoCount;
    @MField(aggregate = AggregateType.Sum)
    long botGoogleCount;
    @MField(aggregate = AggregateType.Sum)
    long botMsnCount;
    @MField(aggregate = AggregateType.Sum)
    long botBingCount;
    @MField(aggregate = AggregateType.Sum)
    long botSosoCount;
    @MField(aggregate = AggregateType.Sum)
    long botSogouCount;
    @MField(aggregate = AggregateType.Sum)
    long botYahooCount;
    
    public String getContextPath() {
        return this.contextPath;
    }
    
    public void setContextPath(final String contextPath) {
        this.contextPath = contextPath;
    }
    
    public int getRunningCount() {
        return this.runningCount;
    }
    
    public void setRunningCount(final int runningCount) {
        this.runningCount = runningCount;
    }
    
    public int getConcurrentMax() {
        return this.concurrentMax;
    }
    
    public void setConcurrentMax(final int concurrentMax) {
        this.concurrentMax = concurrentMax;
    }
    
    public long getRequestCount() {
        return this.requestCount;
    }
    
    public void setRequestCount(final long requestCount) {
        this.requestCount = requestCount;
    }
    
    public long getSessionCount() {
        return this.sessionCount;
    }
    
    public void setSessionCount(final long sessionCount) {
        this.sessionCount = sessionCount;
    }
    
    public long getJdbcFetchRowCount() {
        return this.jdbcFetchRowCount;
    }
    
    public void setJdbcFetchRowCount(final long jdbcFetchRowCount) {
        this.jdbcFetchRowCount = jdbcFetchRowCount;
    }
    
    public long getJdbcUpdateCount() {
        return this.jdbcUpdateCount;
    }
    
    public void setJdbcUpdateCount(final long jdbcUpdateCount) {
        this.jdbcUpdateCount = jdbcUpdateCount;
    }
    
    public long getJdbcExecuteCount() {
        return this.jdbcExecuteCount;
    }
    
    public void setJdbcExecuteCount(final long jdbcExecuteCount) {
        this.jdbcExecuteCount = jdbcExecuteCount;
    }
    
    public long getJdbcExecuteTimeNano() {
        return this.jdbcExecuteTimeNano;
    }
    
    public void setJdbcExecuteTimeNano(final long jdbcExecuteTimeNano) {
        this.jdbcExecuteTimeNano = jdbcExecuteTimeNano;
    }
    
    public long getJdbcCommitCount() {
        return this.jdbcCommitCount;
    }
    
    public void setJdbcCommitCount(final long jdbcCommitCount) {
        this.jdbcCommitCount = jdbcCommitCount;
    }
    
    public long getJdbcRollbackCount() {
        return this.jdbcRollbackCount;
    }
    
    public void setJdbcRollbackCount(final long jdbcRollbackCount) {
        this.jdbcRollbackCount = jdbcRollbackCount;
    }
    
    public long getOsMacOSXCount() {
        return this.osMacOSXCount;
    }
    
    public void setOsMacOSXCount(final long osMacOSXCount) {
        this.osMacOSXCount = osMacOSXCount;
    }
    
    public long getOsWindowsCount() {
        return this.osWindowsCount;
    }
    
    public void setOsWindowsCount(final long osWindowsCount) {
        this.osWindowsCount = osWindowsCount;
    }
    
    public long getOsLinuxCount() {
        return this.osLinuxCount;
    }
    
    public void setOsLinuxCount(final long osLinuxCount) {
        this.osLinuxCount = osLinuxCount;
    }
    
    public long getOsSymbianCount() {
        return this.osSymbianCount;
    }
    
    public void setOsSymbianCount(final long osSymbianCount) {
        this.osSymbianCount = osSymbianCount;
    }
    
    public long getOsFreeBSDCount() {
        return this.osFreeBSDCount;
    }
    
    public void setOsFreeBSDCount(final long osFreeBSDCount) {
        this.osFreeBSDCount = osFreeBSDCount;
    }
    
    public long getOsOpenBSDCount() {
        return this.osOpenBSDCount;
    }
    
    public void setOsOpenBSDCount(final long osOpenBSDCount) {
        this.osOpenBSDCount = osOpenBSDCount;
    }
    
    public long getOsAndroidCount() {
        return this.osAndroidCount;
    }
    
    public void setOsAndroidCount(final long osAndroidCount) {
        this.osAndroidCount = osAndroidCount;
    }
    
    public long getOsWindows98Count() {
        return this.osWindows98Count;
    }
    
    public void setOsWindows98Count(final long osWindows98Count) {
        this.osWindows98Count = osWindows98Count;
    }
    
    public long getOsWindowsXPCount() {
        return this.osWindowsXPCount;
    }
    
    public void setOsWindowsXPCount(final long osWindowsXPCount) {
        this.osWindowsXPCount = osWindowsXPCount;
    }
    
    public long getOsWindows2000Count() {
        return this.osWindows2000Count;
    }
    
    public void setOsWindows2000Count(final long osWindows2000Count) {
        this.osWindows2000Count = osWindows2000Count;
    }
    
    public long getOsWindowsVistaCount() {
        return this.osWindowsVistaCount;
    }
    
    public void setOsWindowsVistaCount(final long osWindowsVistaCount) {
        this.osWindowsVistaCount = osWindowsVistaCount;
    }
    
    public long getOsWindows7Count() {
        return this.osWindows7Count;
    }
    
    public void setOsWindows7Count(final long osWindows7Count) {
        this.osWindows7Count = osWindows7Count;
    }
    
    public long getOsWindows8Count() {
        return this.osWindows8Count;
    }
    
    public void setOsWindows8Count(final long osWindows8Count) {
        this.osWindows8Count = osWindows8Count;
    }
    
    public long getOsAndroid15Count() {
        return this.osAndroid15Count;
    }
    
    public void setOsAndroid15Count(final long osAndroid15Count) {
        this.osAndroid15Count = osAndroid15Count;
    }
    
    public long getOsAndroid16Count() {
        return this.osAndroid16Count;
    }
    
    public void setOsAndroid16Count(final long osAndroid16Count) {
        this.osAndroid16Count = osAndroid16Count;
    }
    
    public long getOsAndroid20Count() {
        return this.osAndroid20Count;
    }
    
    public void setOsAndroid20Count(final long osAndroid20Count) {
        this.osAndroid20Count = osAndroid20Count;
    }
    
    public long getOsAndroid21Count() {
        return this.osAndroid21Count;
    }
    
    public void setOsAndroid21Count(final long osAndroid21Count) {
        this.osAndroid21Count = osAndroid21Count;
    }
    
    public long getOsAndroid22Count() {
        return this.osAndroid22Count;
    }
    
    public void setOsAndroid22Count(final long osAndroid22Count) {
        this.osAndroid22Count = osAndroid22Count;
    }
    
    public long getOsAndroid23Count() {
        return this.osAndroid23Count;
    }
    
    public void setOsAndroid23Count(final long osAndroid23Count) {
        this.osAndroid23Count = osAndroid23Count;
    }
    
    public long getOsAndroid30Count() {
        return this.osAndroid30Count;
    }
    
    public void setOsAndroid30Count(final long osAndroid30Count) {
        this.osAndroid30Count = osAndroid30Count;
    }
    
    public long getOsAndroid31Count() {
        return this.osAndroid31Count;
    }
    
    public void setOsAndroid31Count(final long osAndroid31Count) {
        this.osAndroid31Count = osAndroid31Count;
    }
    
    public long getOsAndroid32Count() {
        return this.osAndroid32Count;
    }
    
    public void setOsAndroid32Count(final long osAndroid32Count) {
        this.osAndroid32Count = osAndroid32Count;
    }
    
    public long getOsAndroid40Count() {
        return this.osAndroid40Count;
    }
    
    public void setOsAndroid40Count(final long osAndroid40Count) {
        this.osAndroid40Count = osAndroid40Count;
    }
    
    public long getOsAndroid41Count() {
        return this.osAndroid41Count;
    }
    
    public void setOsAndroid41Count(final long osAndroid41Count) {
        this.osAndroid41Count = osAndroid41Count;
    }
    
    public long getOsAndroid42Count() {
        return this.osAndroid42Count;
    }
    
    public void setOsAndroid42Count(final long osAndroid42Count) {
        this.osAndroid42Count = osAndroid42Count;
    }
    
    public long getOsAndroid43Count() {
        return this.osAndroid43Count;
    }
    
    public void setOsAndroid43Count(final long osAndroid43Count) {
        this.osAndroid43Count = osAndroid43Count;
    }
    
    public long getOsLinuxUbuntuCount() {
        return this.osLinuxUbuntuCount;
    }
    
    public void setOsLinuxUbuntuCount(final long osLinuxUbuntuCount) {
        this.osLinuxUbuntuCount = osLinuxUbuntuCount;
    }
    
    public long getBrowserIECount() {
        return this.browserIECount;
    }
    
    public void setBrowserIECount(final long browserIECount) {
        this.browserIECount = browserIECount;
    }
    
    public long getBrowserFirefoxCount() {
        return this.browserFirefoxCount;
    }
    
    public void setBrowserFirefoxCount(final long browserFirefoxCount) {
        this.browserFirefoxCount = browserFirefoxCount;
    }
    
    public long getBrowserChromeCount() {
        return this.browserChromeCount;
    }
    
    public void setBrowserChromeCount(final long browserChromeCount) {
        this.browserChromeCount = browserChromeCount;
    }
    
    public long getBrowserSafariCount() {
        return this.browserSafariCount;
    }
    
    public void setBrowserSafariCount(final long browserSafariCount) {
        this.browserSafariCount = browserSafariCount;
    }
    
    public long getBrowserOperaCount() {
        return this.browserOperaCount;
    }
    
    public void setBrowserOperaCount(final long browserOperaCount) {
        this.browserOperaCount = browserOperaCount;
    }
    
    public long getBrowserIE5Count() {
        return this.browserIE5Count;
    }
    
    public void setBrowserIE5Count(final long browserIE5Count) {
        this.browserIE5Count = browserIE5Count;
    }
    
    public long getBrowserIE6Count() {
        return this.browserIE6Count;
    }
    
    public void setBrowserIE6Count(final long browserIE6Count) {
        this.browserIE6Count = browserIE6Count;
    }
    
    public long getBrowserIE7Count() {
        return this.browserIE7Count;
    }
    
    public void setBrowserIE7Count(final long browserIE7Count) {
        this.browserIE7Count = browserIE7Count;
    }
    
    public long getBrowserIE8Count() {
        return this.browserIE8Count;
    }
    
    public void setBrowserIE8Count(final long browserIE8Count) {
        this.browserIE8Count = browserIE8Count;
    }
    
    public long getBrowserIE9Count() {
        return this.browserIE9Count;
    }
    
    public void setBrowserIE9Count(final long browserIE9Count) {
        this.browserIE9Count = browserIE9Count;
    }
    
    public long getBrowserIE10Count() {
        return this.browserIE10Count;
    }
    
    public void setBrowserIE10Count(final long browserIE10Count) {
        this.browserIE10Count = browserIE10Count;
    }
    
    public long getBrowser360SECount() {
        return this.browser360SECount;
    }
    
    public void setBrowser360SECount(final long browser360seCount) {
        this.browser360SECount = browser360seCount;
    }
    
    public long getDeviceAndroidCount() {
        return this.deviceAndroidCount;
    }
    
    public void setDeviceAndroidCount(final long deviceAndroidCount) {
        this.deviceAndroidCount = deviceAndroidCount;
    }
    
    public long getDeviceIpadCount() {
        return this.deviceIpadCount;
    }
    
    public void setDeviceIpadCount(final long deviceIpadCount) {
        this.deviceIpadCount = deviceIpadCount;
    }
    
    public long getDeviceIphoneCount() {
        return this.deviceIphoneCount;
    }
    
    public void setDeviceIphoneCount(final long deviceIphoneCount) {
        this.deviceIphoneCount = deviceIphoneCount;
    }
    
    public long getDeviceWindowsPhoneCount() {
        return this.deviceWindowsPhoneCount;
    }
    
    public void setDeviceWindowsPhoneCount(final long deviceWindowsPhoneCount) {
        this.deviceWindowsPhoneCount = deviceWindowsPhoneCount;
    }
    
    public long getBotCount() {
        return this.botCount;
    }
    
    public void setBotCount(final long botCount) {
        this.botCount = botCount;
    }
    
    public long getBotBaiduCount() {
        return this.botBaiduCount;
    }
    
    public void setBotBaiduCount(final long botBaiduCount) {
        this.botBaiduCount = botBaiduCount;
    }
    
    public long getBotYoudaoCount() {
        return this.botYoudaoCount;
    }
    
    public void setBotYoudaoCount(final long botYoudaoCount) {
        this.botYoudaoCount = botYoudaoCount;
    }
    
    public long getBotGoogleCount() {
        return this.botGoogleCount;
    }
    
    public void setBotGoogleCount(final long botGoogleCount) {
        this.botGoogleCount = botGoogleCount;
    }
    
    public long getBotMsnCount() {
        return this.botMsnCount;
    }
    
    public void setBotMsnCount(final long botMsnCount) {
        this.botMsnCount = botMsnCount;
    }
    
    public long getBotBingCount() {
        return this.botBingCount;
    }
    
    public void setBotBingCount(final long botBingCount) {
        this.botBingCount = botBingCount;
    }
    
    public long getBotSosoCount() {
        return this.botSosoCount;
    }
    
    public void setBotSosoCount(final long botSosoCount) {
        this.botSosoCount = botSosoCount;
    }
    
    public long getBotSogouCount() {
        return this.botSogouCount;
    }
    
    public void setBotSogouCount(final long botSogouCount) {
        this.botSogouCount = botSogouCount;
    }
    
    public long getBotYahooCount() {
        return this.botYahooCount;
    }
    
    public void setBotYahooCount(final long botYahooCount) {
        this.botYahooCount = botYahooCount;
    }
    
    public long getJdbcExecuteTimeMillis() {
        return this.getJdbcExecuteTimeNano() / 1000000L;
    }
    
    public Map<String, Object> getStatData() {
        final Map<String, Object> data = new LinkedHashMap<String, Object>();
        data.put("ContextPath", this.getContextPath());
        data.put("RunningCount", this.getRunningCount());
        data.put("ConcurrentMax", this.getConcurrentMax());
        data.put("RequestCount", this.getRequestCount());
        data.put("SessionCount", this.getSessionCount());
        data.put("JdbcCommitCount", this.getJdbcCommitCount());
        data.put("JdbcRollbackCount", this.getJdbcRollbackCount());
        data.put("JdbcExecuteCount", this.getJdbcExecuteCount());
        data.put("JdbcExecuteTimeMillis", this.getJdbcExecuteTimeMillis());
        data.put("JdbcFetchRowCount", this.getJdbcFetchRowCount());
        data.put("JdbcUpdateCount", this.getJdbcUpdateCount());
        data.put("OSMacOSXCount", this.getOsMacOSXCount());
        data.put("OSWindowsCount", this.getOsWindowsCount());
        data.put("OSLinuxCount", this.getOsLinuxCount());
        data.put("OSSymbianCount", this.getOsSymbianCount());
        data.put("OSFreeBSDCount", this.getOsFreeBSDCount());
        data.put("OSOpenBSDCount", this.getOsOpenBSDCount());
        data.put("OSAndroidCount", this.getOsAndroidCount());
        data.put("OSWindows98Count", this.getOsWindows98Count());
        data.put("OSWindowsXPCount", this.getOsWindowsXPCount());
        data.put("OSWindows2000Count", this.getOsWindows2000Count());
        data.put("OSWindowsVistaCount", this.getOsWindowsVistaCount());
        data.put("OSWindows7Count", this.getOsWindows7Count());
        data.put("OSWindows8Count", this.getOsWindows8Count());
        data.put("OSAndroid15Count", this.getOsAndroid15Count());
        data.put("OSAndroid16Count", this.getOsAndroid16Count());
        data.put("OSAndroid20Count", this.getOsAndroid20Count());
        data.put("OSAndroid21Count", this.getOsAndroid21Count());
        data.put("OSAndroid22Count", this.getOsAndroid22Count());
        data.put("OSAndroid23Count", this.getOsAndroid23Count());
        data.put("OSAndroid30Count", this.getOsAndroid30Count());
        data.put("OSAndroid31Count", this.getOsAndroid31Count());
        data.put("OSAndroid32Count", this.getOsAndroid32Count());
        data.put("OSAndroid40Count", this.getOsAndroid40Count());
        data.put("OSAndroid41Count", this.getOsAndroid41Count());
        data.put("OSAndroid42Count", this.getOsAndroid42Count());
        data.put("OSAndroid43Count", this.getOsAndroid43Count());
        data.put("OSLinuxUbuntuCount", this.getOsLinuxUbuntuCount());
        data.put("BrowserIECount", this.getBrowserIECount());
        data.put("BrowserFirefoxCount", this.getBrowserFirefoxCount());
        data.put("BrowserChromeCount", this.getBrowserChromeCount());
        data.put("BrowserSafariCount", this.getBrowserSafariCount());
        data.put("BrowserOperaCount", this.getBrowserOperaCount());
        data.put("BrowserIE5Count", this.getBrowserIE5Count());
        data.put("BrowserIE6Count", this.getBrowserIE6Count());
        data.put("BrowserIE7Count", this.getBrowserIE7Count());
        data.put("BrowserIE8Count", this.getBrowserIE8Count());
        data.put("BrowserIE9Count", this.getBrowserIE9Count());
        data.put("BrowserIE10Count", this.getBrowserIE10Count());
        data.put("Browser360SECount", this.getBrowser360SECount());
        data.put("DeviceAndroidCount", this.getDeviceAndroidCount());
        data.put("DeviceIpadCount", this.getDeviceIpadCount());
        data.put("DeviceIphoneCount", this.getDeviceIphoneCount());
        data.put("DeviceWindowsPhoneCount", this.getDeviceWindowsPhoneCount());
        data.put("BotCount", this.getBotCount());
        data.put("BotBaiduCount", this.getBotBaiduCount());
        data.put("BotYoudaoCount", this.getBotYoudaoCount());
        data.put("BotGoogleCount", this.getBotGoogleCount());
        data.put("BotMsnCount", this.getBotMsnCount());
        data.put("BotBingCount", this.getBotBingCount());
        data.put("BotSosoCount", this.getBotSosoCount());
        data.put("BotSogouCount", this.getBotSogouCount());
        data.put("BotYahooCount", this.getBotYahooCount());
        return data;
    }
}
