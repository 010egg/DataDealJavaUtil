// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.http.stat;

import com.alibaba.druid.support.logging.LogFactory;
import com.alibaba.druid.util.JdbcSqlStatUtils;
import java.util.concurrent.locks.Lock;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import com.alibaba.druid.util.LRUCache;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicInteger;
import com.alibaba.druid.support.logging.Log;

public class WebAppStat
{
    private static final Log LOG;
    public static final int DEFAULT_MAX_STAT_URI_COUNT = 1000;
    public static final int DEFAULT_MAX_STAT_SESSION_COUNT = 1000;
    private static final ThreadLocal<WebAppStat> currentLocal;
    private volatile int maxStatUriCount;
    private volatile int maxStatSessionCount;
    private final AtomicInteger runningCount;
    private final AtomicInteger concurrentMax;
    private final AtomicLong requestCount;
    private final AtomicLong sessionCount;
    private final AtomicLong jdbcFetchRowCount;
    private final AtomicLong jdbcUpdateCount;
    private final AtomicLong jdbcExecuteCount;
    private final AtomicLong jdbcExecuteTimeNano;
    private final AtomicLong jdbcCommitCount;
    private final AtomicLong jdbcRollbackCount;
    private final ConcurrentMap<String, WebURIStat> uriStatMap;
    private final LRUCache<String, WebSessionStat> sessionStatMap;
    private final ReadWriteLock sessionStatLock;
    private final AtomicLong uriStatMapFullCount;
    private final AtomicLong uriSessionMapFullCount;
    private final AtomicLong osMacOSXCount;
    private final AtomicLong osWindowsCount;
    private final AtomicLong osLinuxCount;
    private final AtomicLong osSymbianCount;
    private final AtomicLong osFreeBSDCount;
    private final AtomicLong osOpenBSDCount;
    private final AtomicLong osAndroidCount;
    private final AtomicLong osWindows98Count;
    private final AtomicLong osWindowsXPCount;
    private final AtomicLong osWindows2000Count;
    private final AtomicLong osWindowsVistaCount;
    private final AtomicLong osWindows7Count;
    private final AtomicLong osWindows8Count;
    private final AtomicLong osAndroid15Count;
    private final AtomicLong osAndroid16Count;
    private final AtomicLong osAndroid20Count;
    private final AtomicLong osAndroid21Count;
    private final AtomicLong osAndroid22Count;
    private final AtomicLong osAndroid23Count;
    private final AtomicLong osAndroid30Count;
    private final AtomicLong osAndroid31Count;
    private final AtomicLong osAndroid32Count;
    private final AtomicLong osAndroid40Count;
    private final AtomicLong osAndroid41Count;
    private final AtomicLong osAndroid42Count;
    private final AtomicLong osAndroid43Count;
    private final AtomicLong osLinuxUbuntuCount;
    private final AtomicLong browserIECount;
    private final AtomicLong browserFirefoxCount;
    private final AtomicLong browserChromeCount;
    private final AtomicLong browserSafariCount;
    private final AtomicLong browserOperaCount;
    private final AtomicLong browserIE5Count;
    private final AtomicLong browserIE6Count;
    private final AtomicLong browserIE7Count;
    private final AtomicLong browserIE8Count;
    private final AtomicLong browserIE9Count;
    private final AtomicLong browserIE10Count;
    private final AtomicLong browser360SECount;
    private final AtomicLong deviceAndroidCount;
    private final AtomicLong deviceIpadCount;
    private final AtomicLong deviceIphoneCount;
    private final AtomicLong deviceWindowsPhoneCount;
    private final AtomicLong botCount;
    private final AtomicLong botBaiduCount;
    private final AtomicLong botYoudaoCount;
    private final AtomicLong botGoogleCount;
    private final AtomicLong botMsnCount;
    private final AtomicLong botBingCount;
    private final AtomicLong botSosoCount;
    private final AtomicLong botSogouCount;
    private final AtomicLong botYahooCount;
    private String contextPath;
    
    public static WebAppStat current() {
        return WebAppStat.currentLocal.get();
    }
    
    public void reset() {
        this.concurrentMax.set(0);
        this.requestCount.set(0L);
        this.requestCount.set(0L);
        this.sessionCount.set(0L);
        this.jdbcFetchRowCount.set(0L);
        this.jdbcUpdateCount.set(0L);
        this.jdbcExecuteCount.set(0L);
        this.jdbcExecuteTimeNano.set(0L);
        this.jdbcCommitCount.set(0L);
        this.jdbcRollbackCount.set(0L);
        this.sessionStatLock.readLock().lock();
        try {
            for (final Map.Entry<String, WebSessionStat> entry : this.sessionStatMap.entrySet()) {
                entry.getValue().reset();
            }
            this.sessionStatMap.clear();
        }
        finally {
            this.sessionStatLock.readLock().unlock();
        }
        this.uriStatMap.clear();
        this.uriStatMapFullCount.set(0L);
        this.uriSessionMapFullCount.set(0L);
        this.osMacOSXCount.set(0L);
        this.osWindowsCount.set(0L);
        this.osLinuxCount.set(0L);
        this.osSymbianCount.set(0L);
        this.osOpenBSDCount.set(0L);
        this.osFreeBSDCount.set(0L);
        this.osAndroidCount.set(0L);
        this.osWindows98Count.set(0L);
        this.osWindowsXPCount.set(0L);
        this.osWindows2000Count.set(0L);
        this.osWindowsVistaCount.set(0L);
        this.osWindows7Count.set(0L);
        this.osWindows8Count.set(0L);
        this.osLinuxUbuntuCount.set(0L);
        this.osAndroid15Count.set(0L);
        this.osAndroid16Count.set(0L);
        this.osAndroid20Count.set(0L);
        this.osAndroid21Count.set(0L);
        this.osAndroid22Count.set(0L);
        this.osAndroid23Count.set(0L);
        this.osAndroid30Count.set(0L);
        this.osAndroid31Count.set(0L);
        this.osAndroid32Count.set(0L);
        this.osAndroid40Count.set(0L);
        this.osAndroid41Count.set(0L);
        this.osAndroid42Count.set(0L);
        this.osAndroid43Count.set(0L);
        this.browserIE6Count.set(0L);
        this.browserIE7Count.set(0L);
        this.browserIE8Count.set(0L);
        this.browserIE9Count.set(0L);
        this.browserIE10Count.set(0L);
        this.browserIECount.set(0L);
        this.browserFirefoxCount.set(0L);
        this.browserChromeCount.set(0L);
        this.browserSafariCount.set(0L);
        this.browserOperaCount.set(0L);
        this.browser360SECount.set(0L);
        this.deviceAndroidCount.set(0L);
        this.deviceIpadCount.set(0L);
        this.deviceIphoneCount.set(0L);
        this.deviceWindowsPhoneCount.set(0L);
    }
    
    public WebAppStat() {
        this(null);
    }
    
    public WebAppStat(final String contextPath) {
        this(contextPath, 1000);
    }
    
    public WebAppStat(final String contextPath, final int maxStatSessionCount) {
        this.maxStatUriCount = 1000;
        this.maxStatSessionCount = 1000;
        this.runningCount = new AtomicInteger();
        this.concurrentMax = new AtomicInteger();
        this.requestCount = new AtomicLong(0L);
        this.sessionCount = new AtomicLong(0L);
        this.jdbcFetchRowCount = new AtomicLong();
        this.jdbcUpdateCount = new AtomicLong();
        this.jdbcExecuteCount = new AtomicLong();
        this.jdbcExecuteTimeNano = new AtomicLong();
        this.jdbcCommitCount = new AtomicLong();
        this.jdbcRollbackCount = new AtomicLong();
        this.uriStatMap = new ConcurrentHashMap<String, WebURIStat>(16, 0.75f, 1);
        this.sessionStatLock = new ReentrantReadWriteLock();
        this.uriStatMapFullCount = new AtomicLong();
        this.uriSessionMapFullCount = new AtomicLong();
        this.osMacOSXCount = new AtomicLong(0L);
        this.osWindowsCount = new AtomicLong(0L);
        this.osLinuxCount = new AtomicLong(0L);
        this.osSymbianCount = new AtomicLong(0L);
        this.osFreeBSDCount = new AtomicLong(0L);
        this.osOpenBSDCount = new AtomicLong(0L);
        this.osAndroidCount = new AtomicLong(0L);
        this.osWindows98Count = new AtomicLong();
        this.osWindowsXPCount = new AtomicLong();
        this.osWindows2000Count = new AtomicLong();
        this.osWindowsVistaCount = new AtomicLong();
        this.osWindows7Count = new AtomicLong();
        this.osWindows8Count = new AtomicLong();
        this.osAndroid15Count = new AtomicLong(0L);
        this.osAndroid16Count = new AtomicLong(0L);
        this.osAndroid20Count = new AtomicLong(0L);
        this.osAndroid21Count = new AtomicLong(0L);
        this.osAndroid22Count = new AtomicLong(0L);
        this.osAndroid23Count = new AtomicLong(0L);
        this.osAndroid30Count = new AtomicLong(0L);
        this.osAndroid31Count = new AtomicLong(0L);
        this.osAndroid32Count = new AtomicLong(0L);
        this.osAndroid40Count = new AtomicLong(0L);
        this.osAndroid41Count = new AtomicLong(0L);
        this.osAndroid42Count = new AtomicLong(0L);
        this.osAndroid43Count = new AtomicLong(0L);
        this.osLinuxUbuntuCount = new AtomicLong(0L);
        this.browserIECount = new AtomicLong(0L);
        this.browserFirefoxCount = new AtomicLong(0L);
        this.browserChromeCount = new AtomicLong(0L);
        this.browserSafariCount = new AtomicLong(0L);
        this.browserOperaCount = new AtomicLong(0L);
        this.browserIE5Count = new AtomicLong(0L);
        this.browserIE6Count = new AtomicLong(0L);
        this.browserIE7Count = new AtomicLong(0L);
        this.browserIE8Count = new AtomicLong(0L);
        this.browserIE9Count = new AtomicLong(0L);
        this.browserIE10Count = new AtomicLong(0L);
        this.browser360SECount = new AtomicLong(0L);
        this.deviceAndroidCount = new AtomicLong(0L);
        this.deviceIpadCount = new AtomicLong(0L);
        this.deviceIphoneCount = new AtomicLong(0L);
        this.deviceWindowsPhoneCount = new AtomicLong(0L);
        this.botCount = new AtomicLong();
        this.botBaiduCount = new AtomicLong();
        this.botYoudaoCount = new AtomicLong();
        this.botGoogleCount = new AtomicLong();
        this.botMsnCount = new AtomicLong();
        this.botBingCount = new AtomicLong();
        this.botSosoCount = new AtomicLong();
        this.botSogouCount = new AtomicLong();
        this.botYahooCount = new AtomicLong();
        this.contextPath = contextPath;
        this.maxStatSessionCount = maxStatSessionCount;
        this.sessionStatMap = new LRUCache<String, WebSessionStat>(maxStatSessionCount);
    }
    
    public String getContextPath() {
        return this.contextPath;
    }
    
    public void beforeInvoke() {
        WebAppStat.currentLocal.set(this);
        final int running = this.runningCount.incrementAndGet();
        int max;
        do {
            max = this.concurrentMax.get();
        } while (running > max && !this.concurrentMax.compareAndSet(max, running));
        this.requestCount.incrementAndGet();
    }
    
    public WebURIStat getURIStat(final String uri) {
        return this.getURIStat(uri, false);
    }
    
    public WebURIStat getURIStat(final String uri, final boolean create) {
        WebURIStat uriStat = this.uriStatMap.get(uri);
        if (uriStat != null) {
            return uriStat;
        }
        if (!create) {
            return null;
        }
        if (this.uriStatMap.size() >= this.getMaxStatUriCount()) {
            final long fullCount = this.uriStatMapFullCount.getAndIncrement();
            if (fullCount == 0L) {
                WebAppStat.LOG.error("uriSessionMapFullCount is full");
            }
            return null;
        }
        this.uriStatMap.putIfAbsent(uri, new WebURIStat(uri));
        uriStat = this.uriStatMap.get(uri);
        return uriStat;
    }
    
    public WebSessionStat getSessionStat(final String sessionId) {
        return this.getSessionStat(sessionId, false);
    }
    
    public Map<String, Object> getSessionStatData(final String sessionId) {
        final WebSessionStat sessionStat = this.sessionStatMap.get(sessionId);
        if (sessionStat == null) {
            return null;
        }
        return sessionStat.getStatData();
    }
    
    public Map<String, Object> getURIStatData(final String uri) {
        final WebURIStat uriStat = this.getURIStat(uri);
        if (uriStat == null) {
            return null;
        }
        return uriStat.getStatData();
    }
    
    public WebSessionStat getSessionStat(final String sessionId, final boolean create) {
        this.sessionStatLock.readLock().lock();
        try {
            final WebSessionStat uriStat = this.sessionStatMap.get(sessionId);
            if (uriStat != null) {
                return uriStat;
            }
        }
        finally {
            this.sessionStatLock.readLock().unlock();
        }
        if (!create) {
            return null;
        }
        this.sessionStatLock.writeLock().lock();
        try {
            final WebSessionStat uriStat = this.sessionStatMap.get(sessionId);
            if (uriStat == null) {
                if (this.sessionStatMap.size() >= this.getMaxStatSessionCount()) {
                    final long fullCount = this.uriSessionMapFullCount.getAndIncrement();
                    if (fullCount == 0L) {
                        WebAppStat.LOG.warn("sessionStatMap is full");
                    }
                }
                final WebSessionStat newStat = new WebSessionStat(sessionId);
                this.sessionStatMap.put(sessionId, newStat);
                return newStat;
            }
            return uriStat;
        }
        finally {
            this.sessionStatLock.writeLock().unlock();
        }
    }
    
    public void afterInvoke(final Throwable error, final long nanoSpan) {
        this.runningCount.decrementAndGet();
        WebAppStat.currentLocal.set(null);
        final WebRequestStat requestStat = WebRequestStat.current();
        if (requestStat != null) {
            this.addJdbcExecuteCount(requestStat.getJdbcExecuteCount());
            this.addJdbcFetchRowCount(requestStat.getJdbcFetchRowCount());
            this.addJdbcUpdateCount(requestStat.getJdbcUpdateCount());
            this.addJdbcCommitCount(requestStat.getJdbcCommitCount());
            this.addJdbcRollbackCount(requestStat.getJdbcRollbackCount());
            this.addJdbcExecuteTimeNano(requestStat.getJdbcExecuteTimeNano());
        }
    }
    
    public void incrementSessionCount() {
        this.sessionCount.incrementAndGet();
    }
    
    public long getSessionCount() {
        return this.sessionCount.get();
    }
    
    public void addJdbcFetchRowCount(final long delta) {
        this.jdbcFetchRowCount.addAndGet(delta);
    }
    
    public long getJdbcFetchRowCount() {
        return this.jdbcFetchRowCount.get();
    }
    
    public void addJdbcUpdateCount(final long updateCount) {
        this.jdbcUpdateCount.addAndGet(updateCount);
    }
    
    public long getJdbcUpdateCount() {
        return this.jdbcUpdateCount.get();
    }
    
    public void incrementJdbcExecuteCount() {
        this.jdbcExecuteCount.incrementAndGet();
    }
    
    public void addJdbcExecuteCount(final long executeCount) {
        this.jdbcExecuteCount.addAndGet(executeCount);
    }
    
    public long getJdbcExecuteCount() {
        return this.jdbcExecuteCount.get();
    }
    
    public long getJdbcExecuteTimeNano() {
        return this.jdbcExecuteTimeNano.get();
    }
    
    public void addJdbcExecuteTimeNano(final long nano) {
        this.jdbcExecuteTimeNano.addAndGet(nano);
    }
    
    public void incrementJdbcCommitCount() {
        this.jdbcCommitCount.incrementAndGet();
    }
    
    public long getJdbcCommitCount() {
        return this.jdbcCommitCount.get();
    }
    
    public void addJdbcCommitCount(final long commitCount) {
        this.jdbcCommitCount.addAndGet(commitCount);
    }
    
    public void incrementJdbcRollbackCount() {
        this.jdbcRollbackCount.incrementAndGet();
    }
    
    public long getJdbcRollbackCount() {
        return this.jdbcRollbackCount.get();
    }
    
    public void addJdbcRollbackCount(final long rollbackCount) {
        this.jdbcRollbackCount.addAndGet(rollbackCount);
    }
    
    public int getMaxStatUriCount() {
        return this.maxStatUriCount;
    }
    
    public void setMaxStatUriCount(final int maxStatUriCount) {
        this.maxStatUriCount = maxStatUriCount;
    }
    
    public int getMaxStatSessionCount() {
        return this.maxStatSessionCount;
    }
    
    public void setMaxStatSessionCount(final int maxStatSessionCount) {
        this.maxStatSessionCount = maxStatSessionCount;
    }
    
    public int getRunningCount() {
        return this.runningCount.get();
    }
    
    public long getConcurrentMax() {
        return this.concurrentMax.get();
    }
    
    public long getRequestCount() {
        return this.requestCount.get();
    }
    
    public Map<String, Object> getStatData() {
        return this.getStatValue(false).getStatData();
    }
    
    public List<WebURIStatValue> getURIStatValueList(final boolean reset) {
        final List<WebURIStatValue> list = new ArrayList<WebURIStatValue>(this.uriStatMap.size());
        for (final WebURIStat uriStat : this.uriStatMap.values()) {
            final WebURIStatValue statValue = uriStat.getValue(reset);
            if (statValue.getRunningCount() == 0 && statValue.getRequestCount() == 0L) {
                continue;
            }
            list.add(statValue);
        }
        return list;
    }
    
    public List<Map<String, Object>> getURIStatDataList() {
        final List<Map<String, Object>> uriStatDataList = new ArrayList<Map<String, Object>>(this.uriStatMap.size());
        for (final WebURIStat uriStat : this.uriStatMap.values()) {
            final Map<String, Object> uriStatData = uriStat.getStatData();
            final int runningCount = uriStatData.get("RunningCount").intValue();
            final long requestCount = uriStatData.get("RequestCount");
            if (runningCount == 0 && requestCount == 0L) {
                continue;
            }
            uriStatDataList.add(uriStatData);
        }
        return uriStatDataList;
    }
    
    public List<Map<String, Object>> getSessionStatDataList() {
        final Lock lock = this.sessionStatLock.readLock();
        lock.lock();
        try {
            final List<Map<String, Object>> sessionStatDataList = new ArrayList<Map<String, Object>>(this.sessionStatMap.size());
            for (final WebSessionStat sessionStat : this.sessionStatMap.values()) {
                final Map<String, Object> sessionStatData = sessionStat.getStatData();
                final int runningCount = sessionStatData.get("RunningCount").intValue();
                final long requestCount = sessionStatData.get("RequestCount");
                if (runningCount == 0 && requestCount == 0L) {
                    continue;
                }
                sessionStatDataList.add(sessionStatData);
            }
            return sessionStatDataList;
        }
        finally {
            lock.unlock();
        }
    }
    
    public void computeUserAgent(final String userAgent) {
        if (userAgent == null || userAgent.length() == 0) {
            return;
        }
        final int MOZILLA_COMPATIBLE_OFFSET = 25;
        final boolean is360SE = userAgent.endsWith("360SE)");
        if (is360SE) {
            this.browser360SECount.incrementAndGet();
        }
        boolean isIE = userAgent.startsWith("MSIE", 25);
        int iePrefixIndex = 30;
        boolean isGoogleToolbar = false;
        if (!isIE) {
            isGoogleToolbar = userAgent.startsWith("GoogleToolbar", 25);
            if (isGoogleToolbar) {
                final int tmp = userAgent.indexOf("IE ");
                if (tmp != -1) {
                    isIE = true;
                    iePrefixIndex = tmp + 3;
                }
            }
        }
        if (isIE) {
            this.browserIECount.incrementAndGet();
            char v1 = ' ';
            char v2 = ' ';
            if (userAgent.length() > iePrefixIndex + 1) {
                v1 = userAgent.charAt(iePrefixIndex);
                v2 = userAgent.charAt(iePrefixIndex + 1);
            }
            else if (userAgent.length() > iePrefixIndex) {
                v1 = userAgent.charAt(iePrefixIndex);
            }
            switch (v1) {
                case '5': {
                    this.browserIE5Count.incrementAndGet();
                    break;
                }
                case '6': {
                    this.browserIE6Count.incrementAndGet();
                    break;
                }
                case '7': {
                    this.browserIE7Count.incrementAndGet();
                    break;
                }
                case '8': {
                    this.browserIE8Count.incrementAndGet();
                    break;
                }
                case '9': {
                    this.browserIE9Count.incrementAndGet();
                    break;
                }
                case '1': {
                    if (v2 == '0') {
                        this.browserIE10Count.incrementAndGet();
                        break;
                    }
                    break;
                }
            }
            this.osWindowsCount.incrementAndGet();
            this.computeUserAgentIEWindowsVersion(userAgent);
            if (userAgent.contains("Windows Phone")) {
                this.deviceWindowsPhoneCount.incrementAndGet();
            }
            return;
        }
        boolean isWindows = false;
        boolean isMac = false;
        boolean isIpad = false;
        boolean isIPhone = false;
        boolean isLinux = false;
        boolean isX11 = false;
        boolean isBSD = false;
        if (userAgent.startsWith("Windows", 13)) {
            isWindows = true;
        }
        else if (userAgent.startsWith("Macintosh", 13)) {
            isMac = true;
        }
        else if (userAgent.startsWith("iPad", 13)) {
            isIpad = true;
            isMac = true;
        }
        else if (userAgent.startsWith("iPhone", 13)) {
            isIPhone = true;
            isMac = true;
        }
        else if (userAgent.startsWith("Linux", 13)) {
            isLinux = true;
        }
        else if (userAgent.startsWith("X11", 13)) {
            isX11 = true;
        }
        boolean isAndroid = false;
        if (isWindows) {
            isWindows = true;
            this.osWindowsCount.incrementAndGet();
            if (userAgent.contains("Windows Phone")) {
                this.deviceWindowsPhoneCount.incrementAndGet();
            }
        }
        else if (isMac) {
            isMac = true;
            this.osMacOSXCount.incrementAndGet();
            if (isIpad && userAgent.contains("iPad")) {
                this.deviceIpadCount.incrementAndGet();
            }
            else if (isIPhone || userAgent.contains("iPhone")) {
                this.deviceIphoneCount.incrementAndGet();
            }
        }
        else if (isLinux) {
            this.osLinuxCount.incrementAndGet();
            isAndroid = this.computeUserAgentAndroid(userAgent);
        }
        else if (userAgent.contains("Symbian")) {
            this.osSymbianCount.incrementAndGet();
        }
        else if (userAgent.contains("Ubuntu")) {
            this.osLinuxCount.incrementAndGet();
            this.osLinuxUbuntuCount.incrementAndGet();
            isLinux = true;
        }
        if (isX11) {
            if (userAgent.contains("OpenBSD")) {
                this.osOpenBSDCount.incrementAndGet();
                isBSD = true;
            }
            else if (userAgent.contains("FreeBSD")) {
                this.osFreeBSDCount.incrementAndGet();
                isBSD = true;
            }
            else if (!isLinux && userAgent.contains("Linux")) {
                this.osLinuxCount.incrementAndGet();
                isLinux = true;
            }
        }
        final boolean isOpera = userAgent.startsWith("Opera");
        if (isOpera) {
            if (userAgent.contains("Windows")) {
                this.osWindowsCount.incrementAndGet();
            }
            else if (userAgent.contains("Linux")) {
                this.osWindowsCount.incrementAndGet();
            }
            else if (userAgent.contains("Macintosh")) {
                this.osMacOSXCount.incrementAndGet();
            }
            this.browserOperaCount.incrementAndGet();
            return;
        }
        if (isWindows) {
            this.computeUserAgentFirefoxWindowsVersion(userAgent);
        }
        if (isWindows || isMac || isLinux || isBSD) {
            if (userAgent.contains("Chrome")) {
                this.browserChromeCount.incrementAndGet();
                return;
            }
            if (!isAndroid && userAgent.contains("Safari")) {
                this.browserSafariCount.incrementAndGet();
                return;
            }
            if (userAgent.contains("Firefox")) {
                this.browserFirefoxCount.incrementAndGet();
                return;
            }
        }
        if (userAgent.startsWith("User-Agent: ")) {
            final String rest = userAgent.substring("User-Agent: ".length());
            this.computeUserAgent(rest);
        }
        final boolean isJava = userAgent.startsWith("Java");
        if (isJava) {
            this.botCount.incrementAndGet();
        }
        if (userAgent.startsWith("msnbot")) {
            this.botCount.incrementAndGet();
            this.botMsnCount.incrementAndGet();
        }
        else if (userAgent.startsWith("Sosospider+")) {
            this.botCount.incrementAndGet();
            this.botSosoCount.incrementAndGet();
        }
        else if (userAgent.startsWith("Sogou")) {
            this.botCount.incrementAndGet();
            this.botSogouCount.incrementAndGet();
        }
        else if (userAgent.startsWith("HuaweiSymantecSpider")) {
            this.botCount.incrementAndGet();
        }
        else if (userAgent.startsWith("Yeti/")) {
            this.botCount.incrementAndGet();
        }
        else if (userAgent.startsWith("mahonie")) {
            this.botCount.incrementAndGet();
        }
        else if (userAgent.startsWith("findlinks")) {
            this.botCount.incrementAndGet();
        }
        else if (userAgent.startsWith("Updownerbot")) {
            this.botCount.incrementAndGet();
        }
        else if (userAgent.startsWith("DoCoMo/")) {
            this.botCount.incrementAndGet();
        }
        else if (userAgent.startsWith("Crawl")) {
            this.botCount.incrementAndGet();
        }
        else if (userAgent.startsWith("SkimBot")) {
            this.botCount.incrementAndGet();
        }
        else if (userAgent.startsWith("YoudaoBot", 25)) {
            this.botCount.incrementAndGet();
            this.botYoudaoCount.incrementAndGet();
        }
        else if (userAgent.startsWith("bingbot", 25)) {
            this.botCount.incrementAndGet();
            this.botBingCount.incrementAndGet();
        }
        else if (userAgent.startsWith("Googlebot", 25)) {
            this.botCount.incrementAndGet();
            this.botGoogleCount.incrementAndGet();
        }
        else if (userAgent.startsWith("Baiduspider", 25)) {
            this.botCount.incrementAndGet();
            this.botBaiduCount.incrementAndGet();
        }
        else if (userAgent.startsWith("MJ12bot", 25)) {
            this.botCount.incrementAndGet();
            this.botBaiduCount.incrementAndGet();
        }
        else if (userAgent.startsWith("Mail.RU/", 25)) {
            this.botCount.incrementAndGet();
        }
        else if (userAgent.startsWith("Yahoo!", 25)) {
            this.botCount.incrementAndGet();
            this.botYahooCount.incrementAndGet();
        }
        else if (userAgent.startsWith("KaloogaBot", 25)) {
            this.botCount.incrementAndGet();
        }
        else if (userAgent.startsWith("YandexBot", 25)) {
            this.botCount.incrementAndGet();
        }
        else if (userAgent.startsWith("Ezooms/", 25)) {
            this.botCount.incrementAndGet();
        }
        else if (userAgent.startsWith("Exabot/", 25)) {
            this.botCount.incrementAndGet();
        }
        else if (userAgent.startsWith("AhrefsBot/", 25)) {
            this.botCount.incrementAndGet();
        }
        else if (userAgent.startsWith("YodaoBot/", 25)) {
            this.botCount.incrementAndGet();
        }
        else if (userAgent.startsWith("BeetleBot", 25)) {
            this.botCount.incrementAndGet();
        }
        else if (userAgent.startsWith("archive.org_bot", 25)) {
            this.botCount.incrementAndGet();
        }
        else if (userAgent.startsWith("aiHitBot", 25)) {
            this.botCount.incrementAndGet();
        }
        else if (userAgent.startsWith("EventGuruBot", 25)) {
            this.botCount.incrementAndGet();
        }
        else if (userAgent.equals("Mozilla/5.0 ()")) {
            this.botCount.incrementAndGet();
        }
        else if (userAgent.equals("\"Mozilla/5.0")) {
            this.botCount.incrementAndGet();
        }
        else if (userAgent.equals("Mozilla")) {
            this.botCount.incrementAndGet();
        }
        else if (userAgent.equals("-")) {
            this.botCount.incrementAndGet();
        }
        else if (userAgent.contains("Spider") || userAgent.contains("spider")) {
            this.botCount.incrementAndGet();
        }
        else if (userAgent.contains("crawl") || userAgent.contains("Crawl")) {
            this.botCount.incrementAndGet();
        }
        else if (userAgent.contains("Bot") || userAgent.contains("bot")) {
            this.botCount.incrementAndGet();
        }
    }
    
    private void computeUserAgentFirefoxWindowsVersion(final String userAgent) {
        if (userAgent.startsWith("Windows NT 5.1", 13)) {
            this.osWindowsXPCount.incrementAndGet();
        }
        else if (userAgent.startsWith("Windows NT 5.1", 25)) {
            this.osWindowsXPCount.incrementAndGet();
        }
        else if (userAgent.startsWith("Windows NT 6.0", 13)) {
            this.osWindowsVistaCount.incrementAndGet();
        }
        else if (userAgent.startsWith("Windows NT 6.1", 13)) {
            this.osWindows7Count.incrementAndGet();
        }
        else if (userAgent.startsWith("Windows NT 6.2", 13)) {
            this.osWindows8Count.incrementAndGet();
        }
        else if (userAgent.startsWith("Windows NT 5.0", 13)) {
            this.osWindows2000Count.incrementAndGet();
        }
        else if (userAgent.startsWith("Windows NT 5.0", 25)) {
            this.osWindows2000Count.incrementAndGet();
        }
    }
    
    private void computeUserAgentIEWindowsVersion(final String userAgent) {
        if (userAgent.startsWith("Windows NT 5.1", 35)) {
            this.osWindowsXPCount.incrementAndGet();
        }
        else if (userAgent.startsWith("Windows NT 5.0", 35)) {
            this.osWindows2000Count.incrementAndGet();
        }
        else if (userAgent.startsWith("Windows NT 5.0", 36)) {
            this.osWindows2000Count.incrementAndGet();
        }
        else if (userAgent.startsWith("Windows NT 6.0", 35)) {
            this.osWindowsVistaCount.incrementAndGet();
        }
        else if (userAgent.startsWith("Windows NT 6.1", 35)) {
            this.osWindows7Count.incrementAndGet();
        }
        else if (userAgent.startsWith("Windows NT 6.2", 36)) {
            this.osWindows8Count.incrementAndGet();
        }
        else if (userAgent.startsWith("Windows 98", 36)) {
            this.osWindows98Count.incrementAndGet();
        }
        else if (userAgent.startsWith("Windows 98", 35)) {
            this.osWindows98Count.incrementAndGet();
        }
        else if (userAgent.startsWith("Windows XP", 35)) {
            this.osWindowsXPCount.incrementAndGet();
        }
        else if (userAgent.startsWith("Windows XP", 34)) {
            this.osWindowsXPCount.incrementAndGet();
        }
    }
    
    private boolean computeUserAgentAndroid(final String userAgent) {
        boolean isAndroid = userAgent.startsWith("Android", 23);
        int toffset = 31;
        if (!isAndroid) {
            isAndroid = userAgent.startsWith("Android", 20);
            toffset = 28;
        }
        if (isAndroid) {
            this.osAndroidCount.incrementAndGet();
            this.deviceAndroidCount.incrementAndGet();
            if (userAgent.startsWith("1.5", toffset)) {
                this.osAndroid15Count.incrementAndGet();
            }
            else if (userAgent.startsWith("1.6", toffset)) {
                this.osAndroid16Count.incrementAndGet();
            }
            else if (userAgent.startsWith("2.0", toffset)) {
                this.osAndroid20Count.incrementAndGet();
            }
            else if (userAgent.startsWith("2.1", toffset)) {
                this.osAndroid21Count.incrementAndGet();
            }
            else if (userAgent.startsWith("2.2", toffset)) {
                this.osAndroid22Count.incrementAndGet();
            }
            else if (userAgent.startsWith("2.3.3", toffset)) {
                this.osAndroid23Count.incrementAndGet();
            }
            else if (userAgent.startsWith("2.3.4", toffset)) {
                this.osAndroid23Count.incrementAndGet();
            }
            else if (userAgent.startsWith("3.0", toffset)) {
                this.osAndroid30Count.incrementAndGet();
            }
            else if (userAgent.startsWith("3.1", toffset)) {
                this.osAndroid31Count.incrementAndGet();
            }
            else if (userAgent.startsWith("3.2", toffset)) {
                this.osAndroid32Count.incrementAndGet();
            }
            else if (userAgent.startsWith("4.0", toffset)) {
                this.osAndroid40Count.incrementAndGet();
            }
            else if (userAgent.startsWith("4.1", toffset)) {
                this.osAndroid41Count.incrementAndGet();
            }
            else if (userAgent.startsWith("4.2", toffset)) {
                this.osAndroid42Count.incrementAndGet();
            }
            else if (userAgent.startsWith("4.3", toffset)) {
                this.osAndroid43Count.incrementAndGet();
            }
            return true;
        }
        return false;
    }
    
    public long getOSMacOSXCount() {
        return this.osMacOSXCount.get();
    }
    
    public long getOSWindowsCount() {
        return this.osWindowsCount.get();
    }
    
    public long getOSLinuxCount() {
        return this.osLinuxCount.get();
    }
    
    public long getOSSymbianCount() {
        return this.osSymbianCount.get();
    }
    
    public long getOSFreeBSDCount() {
        return this.osFreeBSDCount.get();
    }
    
    public long getOSOpenBSDCount() {
        return this.osOpenBSDCount.get();
    }
    
    public long getOSAndroidCount() {
        return this.osAndroidCount.get();
    }
    
    public long getOSWindows98Count() {
        return this.osWindows98Count.get();
    }
    
    public long getOSWindowsXPCount() {
        return this.osWindowsXPCount.get();
    }
    
    public long getOSWindows2000Count() {
        return this.osWindows2000Count.get();
    }
    
    public long getOSWindowsVistaCount() {
        return this.osWindowsVistaCount.get();
    }
    
    public long getOSWindows7Count() {
        return this.osWindows7Count.get();
    }
    
    public long getOSWindows8Count() {
        return this.osWindows8Count.get();
    }
    
    public long getOSAndroid15Count() {
        return this.osAndroid15Count.get();
    }
    
    public long getOSAndroid16Count() {
        return this.osAndroid16Count.get();
    }
    
    public long getOSAndroid20Count() {
        return this.osAndroid20Count.get();
    }
    
    public long getOSAndroid21Count() {
        return this.osAndroid21Count.get();
    }
    
    public long getOSAndroid22Count() {
        return this.osAndroid22Count.get();
    }
    
    public long getOSAndroid23Count() {
        return this.osAndroid23Count.get();
    }
    
    public long getOSAndroid30Count() {
        return this.osAndroid30Count.get();
    }
    
    public long getOSAndroid31Count() {
        return this.osAndroid31Count.get();
    }
    
    public long getOSAndroid32Count() {
        return this.osAndroid32Count.get();
    }
    
    public long getOSAndroid40Count() {
        return this.osAndroid40Count.get();
    }
    
    public long getOSAndroid41Count() {
        return this.osAndroid41Count.get();
    }
    
    public long getOSAndroid42Count() {
        return this.osAndroid42Count.get();
    }
    
    public long getOSAndroid43Count() {
        return this.osAndroid43Count.get();
    }
    
    public long getOSLinuxUbuntuCount() {
        return this.osLinuxUbuntuCount.get();
    }
    
    public long getBrowserIECount() {
        return this.browserIECount.get();
    }
    
    public long getBrowserFirefoxCount() {
        return this.browserFirefoxCount.get();
    }
    
    public long getBrowserChromeCount() {
        return this.browserChromeCount.get();
    }
    
    public long getBrowserSafariCount() {
        return this.browserSafariCount.get();
    }
    
    public long getBrowserOperaCount() {
        return this.browserOperaCount.get();
    }
    
    public long getBrowserIE5Count() {
        return this.browserIE5Count.get();
    }
    
    public long getBrowserIE6Count() {
        return this.browserIE6Count.get();
    }
    
    public long getBrowserIE7Count() {
        return this.browserIE7Count.get();
    }
    
    public long getBrowserIE8Count() {
        return this.browserIE8Count.get();
    }
    
    public long getBrowserIE9Count() {
        return this.browserIE9Count.get();
    }
    
    public long getBrowserIE10Count() {
        return this.browserIE10Count.get();
    }
    
    public long getBrowser360SECount() {
        return this.browser360SECount.get();
    }
    
    public long getDeviceAndroidCount() {
        return this.deviceAndroidCount.get();
    }
    
    public long getDeviceIpadCount() {
        return this.deviceIpadCount.get();
    }
    
    public long getDeviceIphoneCount() {
        return this.deviceIphoneCount.get();
    }
    
    public long getDeviceWindowsPhoneCount() {
        return this.deviceWindowsPhoneCount.get();
    }
    
    public long getBotCount() {
        return this.botCount.get();
    }
    
    public long getBotBaiduCount() {
        return this.botBaiduCount.get();
    }
    
    public long getBotYoudaoCount() {
        return this.botYoudaoCount.get();
    }
    
    public long getBotGoogleCount() {
        return this.botGoogleCount.get();
    }
    
    public long getBotMsnCount() {
        return this.botMsnCount.get();
    }
    
    public long getBotBingCount() {
        return this.botBingCount.get();
    }
    
    public long getBotSosoCount() {
        return this.botSosoCount.get();
    }
    
    public long getBotSogouCount() {
        return this.botSogouCount.get();
    }
    
    public long getBotYahooCount() {
        return this.botYahooCount.get();
    }
    
    public WebAppStatValue getStatValue(final boolean reset) {
        final WebAppStatValue val = new WebAppStatValue();
        val.setContextPath(this.contextPath);
        val.setRunningCount(this.getRunningCount());
        val.concurrentMax = JdbcSqlStatUtils.get(this.concurrentMax, reset);
        val.requestCount = JdbcSqlStatUtils.get(this.requestCount, reset);
        val.sessionCount = JdbcSqlStatUtils.get(this.sessionCount, reset);
        val.jdbcFetchRowCount = JdbcSqlStatUtils.get(this.jdbcFetchRowCount, reset);
        val.jdbcUpdateCount = JdbcSqlStatUtils.get(this.jdbcUpdateCount, reset);
        val.jdbcExecuteCount = JdbcSqlStatUtils.get(this.jdbcExecuteCount, reset);
        val.jdbcExecuteTimeNano = JdbcSqlStatUtils.get(this.jdbcExecuteTimeNano, reset);
        val.jdbcCommitCount = JdbcSqlStatUtils.get(this.jdbcCommitCount, reset);
        val.jdbcRollbackCount = JdbcSqlStatUtils.get(this.jdbcRollbackCount, reset);
        val.osMacOSXCount = JdbcSqlStatUtils.get(this.osMacOSXCount, reset);
        val.osWindowsCount = JdbcSqlStatUtils.get(this.osWindowsCount, reset);
        val.osLinuxCount = JdbcSqlStatUtils.get(this.osLinuxCount, reset);
        val.osSymbianCount = JdbcSqlStatUtils.get(this.osSymbianCount, reset);
        val.osFreeBSDCount = JdbcSqlStatUtils.get(this.osFreeBSDCount, reset);
        val.osOpenBSDCount = JdbcSqlStatUtils.get(this.osOpenBSDCount, reset);
        val.osAndroidCount = JdbcSqlStatUtils.get(this.osAndroidCount, reset);
        val.osWindows98Count = JdbcSqlStatUtils.get(this.osWindows98Count, reset);
        val.osWindowsXPCount = JdbcSqlStatUtils.get(this.osWindowsXPCount, reset);
        val.osWindows2000Count = JdbcSqlStatUtils.get(this.osWindows2000Count, reset);
        val.osWindowsVistaCount = JdbcSqlStatUtils.get(this.osWindowsVistaCount, reset);
        val.osWindows7Count = JdbcSqlStatUtils.get(this.osWindows7Count, reset);
        val.osWindows8Count = JdbcSqlStatUtils.get(this.osWindows8Count, reset);
        val.osAndroid15Count = JdbcSqlStatUtils.get(this.osAndroid15Count, reset);
        val.osAndroid16Count = JdbcSqlStatUtils.get(this.osAndroid16Count, reset);
        val.osAndroid20Count = JdbcSqlStatUtils.get(this.osAndroid20Count, reset);
        val.osAndroid21Count = JdbcSqlStatUtils.get(this.osAndroid21Count, reset);
        val.osAndroid22Count = JdbcSqlStatUtils.get(this.osAndroid22Count, reset);
        val.osAndroid23Count = JdbcSqlStatUtils.get(this.osAndroid23Count, reset);
        val.osAndroid30Count = JdbcSqlStatUtils.get(this.osAndroid30Count, reset);
        val.osAndroid31Count = JdbcSqlStatUtils.get(this.osAndroid31Count, reset);
        val.osAndroid32Count = JdbcSqlStatUtils.get(this.osAndroid32Count, reset);
        val.osAndroid40Count = JdbcSqlStatUtils.get(this.osAndroid40Count, reset);
        val.osAndroid41Count = JdbcSqlStatUtils.get(this.osAndroid41Count, reset);
        val.osAndroid42Count = JdbcSqlStatUtils.get(this.osAndroid42Count, reset);
        val.osAndroid43Count = JdbcSqlStatUtils.get(this.osAndroid43Count, reset);
        val.osLinuxUbuntuCount = JdbcSqlStatUtils.get(this.osLinuxUbuntuCount, reset);
        val.browserIECount = JdbcSqlStatUtils.get(this.browserIECount, reset);
        val.browserFirefoxCount = JdbcSqlStatUtils.get(this.browserFirefoxCount, reset);
        val.browserChromeCount = JdbcSqlStatUtils.get(this.browserChromeCount, reset);
        val.browserSafariCount = JdbcSqlStatUtils.get(this.browserSafariCount, reset);
        val.browserOperaCount = JdbcSqlStatUtils.get(this.browserOperaCount, reset);
        val.browserIE5Count = JdbcSqlStatUtils.get(this.browserIE5Count, reset);
        val.browserIE6Count = JdbcSqlStatUtils.get(this.browserIE6Count, reset);
        val.browserIE7Count = JdbcSqlStatUtils.get(this.browserIE7Count, reset);
        val.browserIE8Count = JdbcSqlStatUtils.get(this.browserIE8Count, reset);
        val.browserIE9Count = JdbcSqlStatUtils.get(this.browserIE9Count, reset);
        val.browserIE10Count = JdbcSqlStatUtils.get(this.browserIE10Count, reset);
        val.browser360SECount = JdbcSqlStatUtils.get(this.browser360SECount, reset);
        val.deviceAndroidCount = JdbcSqlStatUtils.get(this.deviceAndroidCount, reset);
        val.deviceIpadCount = JdbcSqlStatUtils.get(this.deviceIpadCount, reset);
        val.deviceIphoneCount = JdbcSqlStatUtils.get(this.deviceIphoneCount, reset);
        val.deviceWindowsPhoneCount = JdbcSqlStatUtils.get(this.deviceWindowsPhoneCount, reset);
        val.botCount = JdbcSqlStatUtils.get(this.botCount, reset);
        val.botBaiduCount = JdbcSqlStatUtils.get(this.botBaiduCount, reset);
        val.botYoudaoCount = JdbcSqlStatUtils.get(this.botYoudaoCount, reset);
        val.botGoogleCount = JdbcSqlStatUtils.get(this.botGoogleCount, reset);
        val.botMsnCount = JdbcSqlStatUtils.get(this.botMsnCount, reset);
        val.botBingCount = JdbcSqlStatUtils.get(this.botBingCount, reset);
        val.botSosoCount = JdbcSqlStatUtils.get(this.botSosoCount, reset);
        val.botSogouCount = JdbcSqlStatUtils.get(this.botSogouCount, reset);
        val.botYahooCount = JdbcSqlStatUtils.get(this.botYahooCount, reset);
        return val;
    }
    
    static {
        LOG = LogFactory.getLog(WebAppStat.class);
        currentLocal = new ThreadLocal<WebAppStat>();
    }
}
