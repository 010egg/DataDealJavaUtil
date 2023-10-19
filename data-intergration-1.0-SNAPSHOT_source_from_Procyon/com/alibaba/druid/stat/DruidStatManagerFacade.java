// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.stat;

import java.util.Enumeration;
import java.sql.Driver;
import java.sql.DriverManager;
import com.alibaba.druid.util.Utils;
import com.alibaba.druid.VERSION;
import com.alibaba.druid.util.JdbcSqlStatUtils;
import com.alibaba.druid.sql.visitor.SQLEvalVisitorUtils;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.HashMap;
import java.util.Collection;
import com.alibaba.druid.pool.DruidDataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.alibaba.druid.support.http.stat.WebAppStatManager;
import com.alibaba.druid.support.spring.stat.SpringStatManager;
import java.util.Iterator;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.druid.util.DruidDataSourceUtils;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

public final class DruidStatManagerFacade
{
    private static final DruidStatManagerFacade instance;
    private boolean resetEnable;
    private final AtomicLong resetCount;
    
    private DruidStatManagerFacade() {
        this.resetEnable = true;
        this.resetCount = new AtomicLong();
    }
    
    public static DruidStatManagerFacade getInstance() {
        return DruidStatManagerFacade.instance;
    }
    
    private Set<Object> getDruidDataSourceInstances() {
        return DruidDataSourceStatManager.getInstances().keySet();
    }
    
    public Object getDruidDataSourceByName(final String name) {
        for (final Object o : this.getDruidDataSourceInstances()) {
            final String itemName = DruidDataSourceUtils.getName(o);
            if (StringUtils.equals(name, itemName)) {
                return o;
            }
        }
        return null;
    }
    
    public void resetDataSourceStat() {
        DruidDataSourceStatManager.getInstance().reset();
    }
    
    public void resetSqlStat() {
        JdbcStatManager.getInstance().reset();
    }
    
    public void resetAll() {
        if (!this.isResetEnable()) {
            return;
        }
        SpringStatManager.getInstance().resetStat();
        WebAppStatManager.getInstance().resetStat();
        this.resetSqlStat();
        this.resetDataSourceStat();
        this.resetCount.incrementAndGet();
    }
    
    public void logAndResetDataSource() {
        if (!this.isResetEnable()) {
            return;
        }
        DruidDataSourceStatManager.getInstance().logAndResetDataSource();
    }
    
    public boolean isResetEnable() {
        return this.resetEnable;
    }
    
    public void setResetEnable(final boolean resetEnable) {
        this.resetEnable = resetEnable;
    }
    
    public Object getSqlStatById(final Integer id) {
        for (final Object ds : this.getDruidDataSourceInstances()) {
            final Object sqlStat = DruidDataSourceUtils.getSqlStat(ds, id);
            if (sqlStat != null) {
                return sqlStat;
            }
        }
        return null;
    }
    
    public Map<String, Object> getDataSourceStatData(final Integer id) {
        if (id == null) {
            return null;
        }
        final Object datasource = this.getDruidDataSourceById(id);
        return (datasource == null) ? null : this.dataSourceToMapData(datasource, false);
    }
    
    public Object getDruidDataSourceById(final Integer identity) {
        if (identity == null) {
            return null;
        }
        for (final Object datasource : this.getDruidDataSourceInstances()) {
            if (System.identityHashCode(datasource) == identity) {
                return datasource;
            }
        }
        return null;
    }
    
    public List<Map<String, Object>> getSqlStatDataList(final Integer dataSourceId) {
        final Set<Object> dataSources = this.getDruidDataSourceInstances();
        if (dataSourceId == null) {
            final JdbcDataSourceStat globalStat = JdbcDataSourceStat.getGlobal();
            final List<Map<String, Object>> sqlList = new ArrayList<Map<String, Object>>();
            DruidDataSource globalStatDataSource = null;
            for (final Object datasource : dataSources) {
                if (datasource instanceof DruidDataSource && ((DruidDataSource)datasource).getDataSourceStat() == globalStat) {
                    if (globalStatDataSource != null) {
                        continue;
                    }
                    globalStatDataSource = (DruidDataSource)datasource;
                }
                sqlList.addAll(this.getSqlStatDataList(datasource));
            }
            return sqlList;
        }
        for (final Object datasource2 : dataSources) {
            if (dataSourceId != null && dataSourceId != System.identityHashCode(datasource2)) {
                continue;
            }
            return this.getSqlStatDataList(datasource2);
        }
        return new ArrayList<Map<String, Object>>();
    }
    
    public Map<String, Object> getWallStatMap(final Integer dataSourceId) {
        final Set<Object> dataSources = this.getDruidDataSourceInstances();
        if (dataSourceId == null) {
            Map<String, Object> map = new HashMap<String, Object>();
            for (final Object datasource : dataSources) {
                final Map<String, Object> wallStat = DruidDataSourceUtils.getWallStatMap(datasource);
                map = (Map<String, Object>)mergeWallStat(map, wallStat);
            }
            return map;
        }
        for (final Object datasource2 : dataSources) {
            if (dataSourceId != null && dataSourceId != System.identityHashCode(datasource2)) {
                continue;
            }
            return DruidDataSourceUtils.getWallStatMap(datasource2);
        }
        return new HashMap<String, Object>();
    }
    
    @Deprecated
    public static Map mergWallStat(final Map mapA, final Map mapB) {
        return mergeWallStat(mapA, mapB);
    }
    
    public static Map mergeWallStat(final Map mapA, final Map mapB) {
        if (mapA == null || mapA.size() == 0) {
            return mapB;
        }
        if (mapB == null || mapB.size() == 0) {
            return mapA;
        }
        final Map<String, Object> newMap = new LinkedHashMap<String, Object>();
        for (final Object item : mapB.entrySet()) {
            final Map.Entry entry = (Map.Entry)item;
            final String key = entry.getKey();
            final Object valueB = entry.getValue();
            final Object valueA = mapA.get(key);
            if (valueA == null) {
                newMap.put(key, valueB);
            }
            else if (valueB == null) {
                newMap.put(key, valueA);
            }
            else if ("blackList".equals(key)) {
                final Map<String, Map<String, Object>> newSet = new HashMap<String, Map<String, Object>>();
                final Collection<Map<String, Object>> collectionA = (Collection<Map<String, Object>>)valueA;
                for (final Map<String, Object> blackItem : collectionA) {
                    if (newSet.size() >= 1000) {
                        break;
                    }
                    final String sql = blackItem.get("sql");
                    final Map<String, Object> oldItem = newSet.get(sql);
                    newSet.put(sql, mergeWallStat(oldItem, blackItem));
                }
                final Collection<Map<String, Object>> collectionB = (Collection<Map<String, Object>>)valueB;
                for (final Map<String, Object> blackItem2 : collectionB) {
                    if (newSet.size() >= 1000) {
                        break;
                    }
                    final String sql2 = blackItem2.get("sql");
                    final Map<String, Object> oldItem2 = newSet.get(sql2);
                    newSet.put(sql2, mergeWallStat(oldItem2, blackItem2));
                }
                newMap.put(key, newSet.values());
            }
            else if (valueA instanceof Map && valueB instanceof Map) {
                final Object newValue = mergeWallStat((Map)valueA, (Map)valueB);
                newMap.put(key, newValue);
            }
            else if (valueA instanceof Set && valueB instanceof Set) {
                final Set<Object> set = new HashSet<Object>();
                set.addAll((Collection<?>)valueA);
                set.addAll((Collection<?>)valueB);
                newMap.put(key, set);
            }
            else if (valueA instanceof List && valueB instanceof List) {
                final List<Map<String, Object>> mergedList = mergeNamedList((List)valueA, (List)valueB);
                newMap.put(key, mergedList);
            }
            else if (valueA instanceof long[] && valueB instanceof long[]) {
                final long[] arrayA = (long[])valueA;
                final long[] arrayB = (long[])valueB;
                final int len = (arrayA.length >= arrayB.length) ? arrayA.length : arrayB.length;
                final long[] sum = new long[len];
                for (int i = 0; i < sum.length; ++i) {
                    if (i < arrayA.length) {
                        final long[] array = sum;
                        final int n = i;
                        array[n] += arrayA.length;
                    }
                    if (i < arrayB.length) {
                        final long[] array2 = sum;
                        final int n2 = i;
                        array2[n2] += arrayB.length;
                    }
                }
                newMap.put(key, sum);
            }
            else if (valueA instanceof String && valueB instanceof String) {
                newMap.put(key, valueA);
            }
            else {
                final Object sum2 = SQLEvalVisitorUtils.add(valueA, valueB);
                newMap.put(key, sum2);
            }
        }
        return newMap;
    }
    
    private static List<Map<String, Object>> mergeNamedList(final List listA, final List listB) {
        final Map<String, Map<String, Object>> mapped = new HashMap<String, Map<String, Object>>();
        for (final Object item : listA) {
            final Map<String, Object> map = (Map<String, Object>)item;
            final String name = map.get("name");
            mapped.put(name, map);
        }
        final List<Map<String, Object>> mergedList = new ArrayList<Map<String, Object>>();
        for (final Object item2 : listB) {
            final Map<String, Object> mapB = (Map<String, Object>)item2;
            final String name2 = mapB.get("name");
            final Map<String, Object> mapA = mapped.get(name2);
            final Map<String, Object> mergedMap = (Map<String, Object>)mergeWallStat(mapA, mapB);
            mergedList.add(mergedMap);
        }
        return mergedList;
    }
    
    public List<Map<String, Object>> getSqlStatDataList(final Object datasource) {
        final List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        final Map<?, ?> sqlStatMap = (Map<?, ?>)DruidDataSourceUtils.getSqlStatMap(datasource);
        for (final Object sqlStat : sqlStatMap.values()) {
            final Map<String, Object> data = JdbcSqlStatUtils.getData(sqlStat);
            final long executeCount = data.get("ExecuteCount");
            final long runningCount = data.get("RunningCount");
            if (executeCount == 0L && runningCount == 0L) {
                continue;
            }
            result.add(data);
        }
        return result;
    }
    
    public Map<String, Object> getSqlStatData(final Integer id) {
        if (id == null) {
            return null;
        }
        final Object sqlStat = this.getSqlStatById(id);
        if (sqlStat == null) {
            return null;
        }
        return JdbcSqlStatUtils.getData(sqlStat);
    }
    
    public List<Map<String, Object>> getDataSourceStatDataList() {
        return this.getDataSourceStatDataList(false);
    }
    
    public List<Map<String, Object>> getDataSourceStatDataList(final boolean includeSqlList) {
        final List<Map<String, Object>> datasourceList = new ArrayList<Map<String, Object>>();
        for (final Object dataSource : this.getDruidDataSourceInstances()) {
            datasourceList.add(this.dataSourceToMapData(dataSource, includeSqlList));
        }
        return datasourceList;
    }
    
    public List<List<String>> getActiveConnStackTraceList() {
        final List<List<String>> traceList = new ArrayList<List<String>>();
        for (final Object dataSource : this.getDruidDataSourceInstances()) {
            final List<String> stacks = ((DruidDataSource)dataSource).getActiveConnectionStackTrace();
            if (stacks.size() > 0) {
                traceList.add(stacks);
            }
        }
        return traceList;
    }
    
    public Map<String, Object> returnJSONBasicStat() {
        final Map<String, Object> dataMap = new LinkedHashMap<String, Object>();
        dataMap.put("Version", VERSION.getVersionNumber());
        dataMap.put("Drivers", this.getDriversData());
        dataMap.put("ResetEnable", this.isResetEnable());
        dataMap.put("ResetCount", this.getResetCount());
        dataMap.put("JavaVMName", System.getProperty("java.vm.name"));
        dataMap.put("JavaVersion", System.getProperty("java.version"));
        dataMap.put("JavaClassPath", System.getProperty("java.class.path"));
        dataMap.put("StartTime", Utils.getStartTime());
        return dataMap;
    }
    
    public long getResetCount() {
        return this.resetCount.get();
    }
    
    private List<String> getDriversData() {
        final List<String> drivers = new ArrayList<String>();
        final Enumeration<Driver> e = DriverManager.getDrivers();
        while (e.hasMoreElements()) {
            final Driver driver = e.nextElement();
            drivers.add(driver.getClass().getName());
        }
        return drivers;
    }
    
    public List<Map<String, Object>> getPoolingConnectionInfoByDataSourceId(final Integer id) {
        final Object datasource = this.getDruidDataSourceById(id);
        if (datasource == null) {
            return null;
        }
        return DruidDataSourceUtils.getPoolingConnectionInfo(datasource);
    }
    
    public List<String> getActiveConnectionStackTraceByDataSourceId(final Integer id) {
        final Object datasource = this.getDruidDataSourceById(id);
        if (datasource == null || !DruidDataSourceUtils.isRemoveAbandoned(datasource)) {
            return null;
        }
        return DruidDataSourceUtils.getActiveConnectionStackTrace(datasource);
    }
    
    private Map<String, Object> dataSourceToMapData(final Object dataSource, final boolean includeSql) {
        final Map<String, Object> map = DruidDataSourceUtils.getStatData(dataSource);
        if (includeSql) {
            final List<Map<String, Object>> sqlList = this.getSqlStatDataList(dataSource);
            map.put("SQL", sqlList);
        }
        return map;
    }
    
    static {
        instance = new DruidStatManagerFacade();
    }
}
