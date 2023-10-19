// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.util;

import com.alibaba.druid.DbType;

public interface JdbcConstants
{
    public static final DbType JTDS = DbType.jtds;
    public static final String MOCK = "mock";
    public static final DbType HSQL = DbType.hsql;
    public static final DbType DB2 = DbType.db2;
    public static final String DB2_DRIVER = "com.ibm.db2.jcc.DB2Driver";
    public static final String DB2_DRIVER2 = "COM.ibm.db2.jdbc.app.DB2Driver";
    public static final String DB2_DRIVER3 = "COM.ibm.db2.jdbc.net.DB2Driver";
    public static final DbType POSTGRESQL = DbType.postgresql;
    public static final String POSTGRESQL_DRIVER = "org.postgresql.Driver";
    public static final DbType SYBASE = DbType.sybase;
    public static final DbType SQL_SERVER = DbType.sqlserver;
    public static final String SQL_SERVER_DRIVER = "com.microsoft.jdbc.sqlserver.SQLServerDriver";
    public static final String SQL_SERVER_DRIVER_SQLJDBC4 = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    public static final String SQL_SERVER_DRIVER_JTDS = "net.sourceforge.jtds.jdbc.Driver";
    public static final DbType ORACLE = DbType.oracle;
    public static final String ORACLE_DRIVER = "oracle.jdbc.OracleDriver";
    public static final String ORACLE_DRIVER2 = "oracle.jdbc.driver.OracleDriver";
    public static final DbType ALI_ORACLE = DbType.ali_oracle;
    public static final String ALI_ORACLE_DRIVER = "com.alibaba.jdbc.AlibabaDriver";
    public static final DbType MYSQL = DbType.mysql;
    public static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
    public static final String MYSQL_DRIVER_6 = "com.mysql.cj.jdbc.Driver";
    public static final String MYSQL_DRIVER_REPLICATE = "com.mysql.jdbc.";
    public static final String MARIADB = "mariadb";
    public static final String MARIADB_DRIVER = "org.mariadb.jdbc.Driver";
    public static final DbType DERBY = DbType.derby;
    public static final String HBASE = "hbase";
    public static final DbType HIVE = DbType.hive;
    public static final String HIVE_DRIVER = "org.apache.hive.jdbc.HiveDriver";
    public static final DbType H2 = DbType.h2;
    public static final String H2_DRIVER = "org.h2.Driver";
    public static final DbType DM = DbType.dm;
    public static final String DM_DRIVER = "dm.jdbc.driver.DmDriver";
    public static final DbType KINGBASE = DbType.kingbase;
    public static final String KINGBASE_DRIVER = "com.kingbase.Driver";
    public static final String KINGBASE8_DRIVER = "com.kingbase8.Driver";
    public static final DbType GBASE = DbType.gbase;
    public static final String GBASE_DRIVER = "com.gbase.jdbc.Driver";
    public static final DbType XUGU = DbType.xugu;
    public static final String XUGU_DRIVER = "com.xugu.cloudjdbc.Driver";
    public static final DbType OCEANBASE = DbType.oceanbase;
    public static final DbType OCEANBASE_ORACLE = DbType.oceanbase_oracle;
    public static final String OCEANBASE_DRIVER = "com.alipay.oceanbase.jdbc.Driver";
    public static final DbType INFORMIX = DbType.informix;
    public static final DbType ODPS = DbType.odps;
    public static final String ODPS_DRIVER = "com.aliyun.odps.jdbc.OdpsDriver";
    public static final String TERADATA = "teradata";
    public static final String TERADATA_DRIVER = "com.teradata.jdbc.TeraDriver";
    public static final String LOG4JDBC = "log4jdbc";
    public static final String LOG4JDBC_DRIVER = "net.sf.log4jdbc.DriverSpy";
    public static final String PHOENIX = "phoenix";
    public static final String PHOENIX_DRIVER = "org.apache.phoenix.jdbc.PhoenixDriver";
    public static final DbType ENTERPRISEDB = DbType.edb;
    public static final String ENTERPRISEDB_DRIVER = "com.edb.Driver";
    public static final String KYLIN = "kylin";
    public static final String KYLIN_DRIVER = "org.apache.kylin.jdbc.Driver";
    public static final String SQLITE = "sqlite";
    public static final String SQLITE_DRIVER = "org.sqlite.JDBC";
    public static final String ALIYUN_ADS = "aliyun_ads";
    public static final DbType ALIYUN_DRDS = DbType.drds;
    public static final String PRESTO = "presto";
    public static final String PRESTO_DRIVER = "com.facebook.presto.jdbc.PrestoDriver";
    public static final String TRINO = "trino";
    public static final String TRINO_DRIVER = "io.trino.jdbc.TrinoDriver";
    public static final String ELASTIC_SEARCH = "elastic_search";
    public static final String ELASTIC_SEARCH_DRIVER = "com.alibaba.xdriver.elastic.jdbc.ElasticDriver";
    public static final DbType CLICKHOUSE = DbType.clickhouse;
    public static final String CLICKHOUSE_DRIVER = "ru.yandex.clickhouse.ClickHouseDriver";
    public static final String KDB = "kdb";
    public static final String KDB_DRIVER = "com.inspur.jdbc.KdDriver";
    public static final DbType POLARDB = DbType.polardb;
    public static final String POLARDB_DRIVER = "com.aliyun.polardb.Driver";
    public static final DbType GREENPLUM = DbType.greenplum;
    public static final String GREENPLUM_DRIVER = "com.pivotal.jdbc.GreenplumDriver";
}
