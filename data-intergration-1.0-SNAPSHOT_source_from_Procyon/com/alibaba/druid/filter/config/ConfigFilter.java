// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.filter.config;

import com.alibaba.druid.support.logging.LogFactory;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.Closeable;
import com.alibaba.druid.util.JdbcUtils;
import java.net.URL;
import com.alibaba.druid.util.StringUtils;
import java.security.PublicKey;
import java.sql.SQLException;
import java.util.Map;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import java.util.Properties;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.proxy.jdbc.DataSourceProxy;
import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.filter.FilterAdapter;

public class ConfigFilter extends FilterAdapter
{
    private static Log LOG;
    public static final String CONFIG_FILE = "config.file";
    public static final String CONFIG_DECRYPT = "config.decrypt";
    public static final String CONFIG_KEY = "config.decrypt.key";
    public static final String SYS_PROP_CONFIG_FILE = "druid.config.file";
    public static final String SYS_PROP_CONFIG_DECRYPT = "druid.config.decrypt";
    public static final String SYS_PROP_CONFIG_KEY = "druid.config.decrypt.key";
    
    @Override
    public void init(final DataSourceProxy dataSourceProxy) {
        if (!(dataSourceProxy instanceof DruidDataSource)) {
            ConfigFilter.LOG.error("ConfigLoader only support DruidDataSource");
        }
        final DruidDataSource dataSource = (DruidDataSource)dataSourceProxy;
        final Properties connectionProperties = dataSource.getConnectProperties();
        final Properties configFileProperties = this.loadPropertyFromConfigFile(connectionProperties);
        final boolean decrypt = this.isDecrypt(connectionProperties, configFileProperties);
        if (configFileProperties == null) {
            if (decrypt) {
                this.decrypt(dataSource, null);
            }
            return;
        }
        if (decrypt) {
            this.decrypt(dataSource, configFileProperties);
        }
        try {
            DruidDataSourceFactory.config(dataSource, configFileProperties);
        }
        catch (SQLException e) {
            throw new IllegalArgumentException("Config DataSource error.", e);
        }
    }
    
    public boolean isDecrypt(final Properties connectionProperties, final Properties configFileProperties) {
        String decrypterId = connectionProperties.getProperty("config.decrypt");
        if ((decrypterId == null || decrypterId.length() == 0) && configFileProperties != null) {
            decrypterId = configFileProperties.getProperty("config.decrypt");
        }
        if (decrypterId == null || decrypterId.length() == 0) {
            decrypterId = System.getProperty("druid.config.decrypt");
        }
        return Boolean.valueOf(decrypterId);
    }
    
    Properties loadPropertyFromConfigFile(final Properties connectionProperties) {
        String configFile = connectionProperties.getProperty("config.file");
        if (configFile == null) {
            configFile = System.getProperty("druid.config.file");
        }
        if (configFile == null || configFile.length() <= 0) {
            return null;
        }
        if (ConfigFilter.LOG.isInfoEnabled()) {
            ConfigFilter.LOG.info("DruidDataSource Config File load from : " + configFile);
        }
        final Properties info = this.loadConfig(configFile);
        if (info == null) {
            throw new IllegalArgumentException("Cannot load remote config file from the [config.file=" + configFile + "].");
        }
        return info;
    }
    
    public void decrypt(final DruidDataSource dataSource, final Properties info) {
        try {
            String encryptedPassword = null;
            if (info != null) {
                encryptedPassword = info.getProperty("password");
            }
            if (encryptedPassword == null || encryptedPassword.length() == 0) {
                encryptedPassword = dataSource.getConnectProperties().getProperty("password");
            }
            if (encryptedPassword == null || encryptedPassword.length() == 0) {
                encryptedPassword = dataSource.getPassword();
            }
            final PublicKey publicKey = this.getPublicKey(dataSource.getConnectProperties(), info);
            final String passwordPlainText = ConfigTools.decrypt(publicKey, encryptedPassword);
            if (info != null) {
                info.setProperty("password", passwordPlainText);
            }
            else {
                dataSource.setPassword(passwordPlainText);
            }
        }
        catch (Exception e) {
            throw new IllegalArgumentException("Failed to decrypt.", e);
        }
    }
    
    public PublicKey getPublicKey(final Properties connectionProperties, final Properties configFileProperties) {
        String key = null;
        if (configFileProperties != null) {
            key = configFileProperties.getProperty("config.decrypt.key");
        }
        if (StringUtils.isEmpty(key) && connectionProperties != null) {
            key = connectionProperties.getProperty("config.decrypt.key");
        }
        if (StringUtils.isEmpty(key)) {
            key = System.getProperty("druid.config.decrypt.key");
        }
        return ConfigTools.getPublicKey(key);
    }
    
    public Properties loadConfig(String filePath) {
        final Properties properties = new Properties();
        InputStream inStream = null;
        try {
            boolean xml = false;
            if (filePath.startsWith("file://")) {
                filePath = filePath.substring("file://".length());
                inStream = this.getFileAsStream(filePath);
                xml = filePath.endsWith(".xml");
            }
            else if (filePath.startsWith("http://") || filePath.startsWith("https://")) {
                final URL url = new URL(filePath);
                inStream = url.openStream();
                xml = url.getPath().endsWith(".xml");
            }
            else if (filePath.startsWith("classpath:")) {
                final String resourcePath = filePath.substring("classpath:".length());
                inStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourcePath);
                xml = resourcePath.endsWith(".xml");
            }
            else {
                inStream = this.getFileAsStream(filePath);
                xml = filePath.endsWith(".xml");
            }
            if (inStream == null) {
                ConfigFilter.LOG.error("load config file error, file : " + filePath);
                return null;
            }
            if (xml) {
                properties.loadFromXML(inStream);
            }
            else {
                properties.load(inStream);
            }
            return properties;
        }
        catch (Exception ex) {
            ConfigFilter.LOG.error("load config file error, file : " + filePath, ex);
            return null;
        }
        finally {
            JdbcUtils.close(inStream);
        }
    }
    
    private InputStream getFileAsStream(final String filePath) throws FileNotFoundException {
        InputStream inStream = null;
        final File file = new File(filePath);
        if (file.exists()) {
            inStream = new FileInputStream(file);
        }
        else {
            inStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
        }
        return inStream;
    }
    
    static {
        ConfigFilter.LOG = LogFactory.getLog(ConfigFilter.class);
    }
}
