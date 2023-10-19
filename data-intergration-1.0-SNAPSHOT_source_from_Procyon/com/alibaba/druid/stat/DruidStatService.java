// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.stat;

import com.alibaba.druid.support.logging.LogFactory;
import javax.management.MBeanServer;
import javax.management.JMException;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import com.alibaba.druid.support.json.JSONUtils;
import java.util.LinkedHashMap;
import java.text.DateFormat;
import com.alibaba.druid.sql.visitor.SchemaStatVisitor;
import java.util.Date;
import java.text.SimpleDateFormat;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.DbType;
import java.util.Comparator;
import java.util.Collections;
import com.alibaba.druid.util.MapComparator;
import com.alibaba.druid.support.http.stat.WebAppStatManager;
import com.alibaba.druid.support.spring.stat.SpringStatManager;
import java.util.List;
import java.util.Map;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.druid.support.logging.Log;

public final class DruidStatService implements DruidStatServiceMBean
{
    private static final Log LOG;
    public static final String MBEAN_NAME = "com.alibaba.druid:type=DruidStatService";
    private static final DruidStatService instance;
    private static DruidStatManagerFacade statManagerFacade;
    public static final int RESULT_CODE_SUCCESS = 1;
    public static final int RESULT_CODE_ERROR = -1;
    private static final int DEFAULT_PAGE = 1;
    private static final int DEFAULT_PER_PAGE_COUNT = Integer.MAX_VALUE;
    private static final String ORDER_TYPE_DESC = "desc";
    private static final String ORDER_TYPE_ASC = "asc";
    private static final String DEFAULT_ORDER_TYPE = "asc";
    private static final String DEFAULT_ORDERBY = "SQL";
    
    private DruidStatService() {
    }
    
    public static DruidStatService getInstance() {
        return DruidStatService.instance;
    }
    
    public boolean isResetEnable() {
        return DruidStatService.statManagerFacade.isResetEnable();
    }
    
    public void setResetEnable(final boolean value) {
        DruidStatService.statManagerFacade.setResetEnable(value);
    }
    
    @Override
    public String service(final String url) {
        final Map<String, String> parameters = getParameters(url);
        if (url.equals("/basic.json")) {
            return returnJSONResult(1, DruidStatService.statManagerFacade.returnJSONBasicStat());
        }
        if (url.equals("/reset-all.json")) {
            DruidStatService.statManagerFacade.resetAll();
            return returnJSONResult(1, null);
        }
        if (url.equals("/log-and-reset.json")) {
            DruidStatService.statManagerFacade.logAndResetDataSource();
            return returnJSONResult(1, null);
        }
        if (url.equals("/datasource.json")) {
            return returnJSONResult(1, DruidStatService.statManagerFacade.getDataSourceStatDataList());
        }
        if (url.equals("/activeConnectionStackTrace.json")) {
            return returnJSONResult(1, DruidStatService.statManagerFacade.getActiveConnStackTraceList());
        }
        if (url.startsWith("/datasource-")) {
            final Integer id = StringUtils.subStringToInteger(url, "datasource-", ".");
            final Object result = DruidStatService.statManagerFacade.getDataSourceStatData(id);
            return returnJSONResult((result == null) ? -1 : 1, result);
        }
        if (url.startsWith("/connectionInfo-") && url.endsWith(".json")) {
            final Integer id = StringUtils.subStringToInteger(url, "connectionInfo-", ".");
            final List<?> connectionInfoList = DruidStatService.statManagerFacade.getPoolingConnectionInfoByDataSourceId(id);
            return returnJSONResult((connectionInfoList == null) ? -1 : 1, connectionInfoList);
        }
        if (url.startsWith("/activeConnectionStackTrace-") && url.endsWith(".json")) {
            final Integer id = StringUtils.subStringToInteger(url, "activeConnectionStackTrace-", ".");
            return this.returnJSONActiveConnectionStackTrace(id);
        }
        if (url.startsWith("/sql.json")) {
            return returnJSONResult(1, this.getSqlStatDataList(parameters));
        }
        if (url.startsWith("/wall.json")) {
            return returnJSONResult(1, this.getWallStatMap(parameters));
        }
        if (url.startsWith("/wall-") && url.indexOf(".json") > 0) {
            final Integer dataSourceId = StringUtils.subStringToInteger(url, "wall-", ".json");
            final Object result = DruidStatService.statManagerFacade.getWallStatMap(dataSourceId);
            return returnJSONResult((result == null) ? -1 : 1, result);
        }
        if (url.startsWith("/sql-") && url.indexOf(".json") > 0) {
            final Integer id = StringUtils.subStringToInteger(url, "sql-", ".json");
            return this.getSqlStat(id);
        }
        if (url.startsWith("/weburi.json")) {
            return returnJSONResult(1, this.getWebURIStatDataList(parameters));
        }
        if (url.startsWith("/weburi-") && url.indexOf(".json") > 0) {
            final String uri = StringUtils.subString(url, "weburi-", ".json", true);
            return returnJSONResult(1, this.getWebURIStatData(uri));
        }
        if (url.startsWith("/webapp.json")) {
            return returnJSONResult(1, this.getWebAppStatDataList(parameters));
        }
        if (url.startsWith("/websession.json")) {
            return returnJSONResult(1, this.getWebSessionStatDataList(parameters));
        }
        if (url.startsWith("/websession-") && url.indexOf(".json") > 0) {
            final String id2 = StringUtils.subString(url, "websession-", ".json");
            return returnJSONResult(1, this.getWebSessionStatData(id2));
        }
        if (url.startsWith("/spring.json")) {
            return returnJSONResult(1, this.getSpringStatDataList(parameters));
        }
        if (url.startsWith("/spring-detail.json")) {
            final String clazz = parameters.get("class");
            final String method = parameters.get("method");
            return returnJSONResult(1, this.getSpringMethodStatData(clazz, method));
        }
        return returnJSONResult(-1, "Do not support this request, please contact with administrator.");
    }
    
    private List<Map<String, Object>> getSpringStatDataList(final Map<String, String> parameters) {
        final List<Map<String, Object>> array = SpringStatManager.getInstance().getMethodStatData();
        return this.comparatorOrderBy(array, parameters);
    }
    
    private List<Map<String, Object>> getWebURIStatDataList(final Map<String, String> parameters) {
        final List<Map<String, Object>> array = WebAppStatManager.getInstance().getURIStatData();
        return this.comparatorOrderBy(array, parameters);
    }
    
    private Map<String, Object> getWebURIStatData(final String uri) {
        return WebAppStatManager.getInstance().getURIStatData(uri);
    }
    
    private Map<String, Object> getWebSessionStatData(final String sessionId) {
        return WebAppStatManager.getInstance().getSessionStat(sessionId);
    }
    
    private Map<String, Object> getSpringMethodStatData(final String clazz, final String method) {
        return SpringStatManager.getInstance().getMethodStatData(clazz, method);
    }
    
    private List<Map<String, Object>> getWebSessionStatDataList(final Map<String, String> parameters) {
        final List<Map<String, Object>> array = WebAppStatManager.getInstance().getSessionStatData();
        return this.comparatorOrderBy(array, parameters);
    }
    
    private List<Map<String, Object>> getWebAppStatDataList(final Map<String, String> parameters) {
        final List<Map<String, Object>> array = WebAppStatManager.getInstance().getWebAppStatData();
        return this.comparatorOrderBy(array, parameters);
    }
    
    private List<Map<String, Object>> comparatorOrderBy(final List<Map<String, Object>> array, final Map<String, String> parameters) {
        if (array == null || array.isEmpty()) {
            return null;
        }
        String orderType = null;
        Integer page = 1;
        Integer perPageCount = Integer.MAX_VALUE;
        String orderBy;
        if (parameters == null) {
            orderBy = "SQL";
            orderType = "asc";
            page = 1;
            perPageCount = Integer.MAX_VALUE;
        }
        else {
            orderBy = parameters.get("orderBy");
            orderType = parameters.get("orderType");
            final String pageParam = parameters.get("page");
            if (pageParam != null && pageParam.length() != 0) {
                page = Integer.parseInt(pageParam);
            }
            final String pageCountParam = parameters.get("perPageCount");
            if (pageCountParam != null && pageCountParam.length() > 0) {
                perPageCount = Integer.parseInt(pageCountParam);
            }
        }
        orderBy = ((orderBy == null) ? "SQL" : orderBy);
        orderType = ((orderType == null) ? "asc" : orderType);
        if (!"desc".equals(orderType)) {
            orderType = "asc";
        }
        if (orderBy.trim().length() != 0) {
            Collections.sort(array, (Comparator<? super Map<String, Object>>)new MapComparator(orderBy, "desc".equals(orderType)));
        }
        final int fromIndex = (page - 1) * perPageCount;
        int toIndex = page * perPageCount;
        if (toIndex > array.size()) {
            toIndex = array.size();
        }
        return array.subList(fromIndex, toIndex);
    }
    
    private List<Map<String, Object>> getSqlStatDataList(final Map<String, String> parameters) {
        Integer dataSourceId = null;
        final String dataSourceIdParam = parameters.get("dataSourceId");
        if (dataSourceIdParam != null && dataSourceIdParam.length() > 0) {
            dataSourceId = Integer.parseInt(dataSourceIdParam);
        }
        final List<Map<String, Object>> array = DruidStatService.statManagerFacade.getSqlStatDataList(dataSourceId);
        final List<Map<String, Object>> sortedArray = this.comparatorOrderBy(array, parameters);
        return sortedArray;
    }
    
    public Map<String, Object> getWallStatMap(final Map<String, String> parameters) {
        Integer dataSourceId = null;
        final String dataSourceIdParam = parameters.get("dataSourceId");
        if (dataSourceIdParam != null && dataSourceIdParam.length() > 0) {
            dataSourceId = Integer.parseInt(dataSourceIdParam);
        }
        Map<String, Object> result = DruidStatService.statManagerFacade.getWallStatMap(dataSourceId);
        if (result != null) {
            final List<Map<String, Object>> tables = result.get("tables");
            if (tables != null) {
                final List<Map<String, Object>> sortedArray = this.comparatorOrderBy(tables, parameters);
                result.put("tables", sortedArray);
            }
            final List<Map<String, Object>> functions = result.get("functions");
            if (functions != null) {
                final List<Map<String, Object>> sortedArray2 = this.comparatorOrderBy(functions, parameters);
                result.put("functions", sortedArray2);
            }
        }
        else {
            result = Collections.emptyMap();
        }
        return result;
    }
    
    private String getSqlStat(final Integer id) {
        final Map<String, Object> map = DruidStatService.statManagerFacade.getSqlStatData(id);
        if (map == null) {
            return returnJSONResult(-1, null);
        }
        final DbType dbType = DbType.of(map.get("DbType"));
        final String sql = map.get("SQL");
        map.put("formattedSql", SQLUtils.format(sql, dbType));
        final List<SQLStatement> statementList = SQLUtils.parseStatements(sql, dbType);
        if (!statementList.isEmpty()) {
            final SQLStatement sqlStmt = statementList.get(0);
            final SchemaStatVisitor visitor = SQLUtils.createSchemaStatVisitor(dbType);
            sqlStmt.accept(visitor);
            map.put("parsedTable", visitor.getTables().toString());
            map.put("parsedFields", visitor.getColumns().toString());
            map.put("parsedConditions", visitor.getConditions().toString());
            map.put("parsedRelationships", visitor.getRelationships().toString());
            map.put("parsedOrderbycolumns", visitor.getOrderByColumns().toString());
        }
        final DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SSS");
        final Date maxTimespanOccurTime = map.get("MaxTimespanOccurTime");
        if (maxTimespanOccurTime != null) {
            map.put("MaxTimespanOccurTime", format.format(maxTimespanOccurTime));
        }
        return returnJSONResult((map == null) ? -1 : 1, map);
    }
    
    private String returnJSONActiveConnectionStackTrace(final Integer id) {
        final List<String> result = DruidStatService.statManagerFacade.getActiveConnectionStackTraceByDataSourceId(id);
        if (result == null) {
            return returnJSONResult(-1, "require set removeAbandoned=true");
        }
        return returnJSONResult(1, result);
    }
    
    public static String returnJSONResult(final int resultCode, final Object content) {
        final Map<String, Object> dataMap = new LinkedHashMap<String, Object>();
        dataMap.put("ResultCode", resultCode);
        dataMap.put("Content", content);
        return JSONUtils.toJSONString(dataMap);
    }
    
    public static void registerMBean() {
        final MBeanServer mbeanServer = ManagementFactory.getPlatformMBeanServer();
        try {
            final ObjectName objectName = new ObjectName("com.alibaba.druid:type=DruidStatService");
            if (!mbeanServer.isRegistered(objectName)) {
                mbeanServer.registerMBean(DruidStatService.instance, objectName);
            }
        }
        catch (JMException ex) {
            DruidStatService.LOG.error("register mbean error", ex);
        }
    }
    
    public static void unregisterMBean() {
        final MBeanServer mbeanServer = ManagementFactory.getPlatformMBeanServer();
        try {
            mbeanServer.unregisterMBean(new ObjectName("com.alibaba.druid:type=DruidStatService"));
        }
        catch (JMException ex) {
            DruidStatService.LOG.error("unregister mbean error", ex);
        }
    }
    
    public static Map<String, String> getParameters(String url) {
        if (url == null || (url = url.trim()).length() == 0) {
            return Collections.emptyMap();
        }
        final String parametersStr = StringUtils.subString(url, "?", null);
        if (parametersStr == null || parametersStr.length() == 0) {
            return Collections.emptyMap();
        }
        final String[] parametersArray = parametersStr.split("&");
        final Map<String, String> parameters = new LinkedHashMap<String, String>();
        for (final String parameterStr : parametersArray) {
            final int index = parameterStr.indexOf("=");
            if (index > 0) {
                final String name = parameterStr.substring(0, index);
                final String value = parameterStr.substring(index + 1);
                parameters.put(name, value);
            }
        }
        return parameters;
    }
    
    static {
        LOG = LogFactory.getLog(DruidStatService.class);
        instance = new DruidStatService();
        DruidStatService.statManagerFacade = DruidStatManagerFacade.getInstance();
    }
}
