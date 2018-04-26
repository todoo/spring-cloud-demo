package com.easy.ms.dynamicdb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement(order = 1)
@ConditionalOnClass(DynamicDatasourceAutoConfigure.class)
@Import({ DynamicDataSourceProperties.class })
public class DynamicDatasourceAutoConfigure {

    @Autowired
    private DynamicDataSourceProperties dynamicDataSourceProperties;

    @Primary
    @Bean
    public DataSource dataSource() {

        DynamicDataSource dynamicDataSource = this.buildDynamicDataSource();
        return dynamicDataSource;
    }

    private void initDynamicDataSourceContextHolder() {
        List<String> dataSourceIds = new ArrayList<>();
        dataSourceIds.addAll(Arrays.asList(dynamicDataSourceProperties.getCustomDataSourceNames()));
        dataSourceIds.add("default");
        DynamicDataSourceContextHolder.setDataSourceIds(dataSourceIds);
        DynamicDataSourceContextHolder.setCurrentDataSourceId("default");
    }

    private DynamicDataSource buildDynamicDataSource() {
        this.initDynamicDataSourceContextHolder();
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        Map<String, Object> mainDbMap = dynamicDataSourceProperties.getMainDataSource();
        Map<String, Map<String, Object>> customDbs = dynamicDataSourceProperties.getCustomDataSource();
        Map<String, Object> poolMap = dynamicDataSourceProperties.getPool();

        Map<Object, Object> targetDataSources = new HashMap<Object, Object>();
        targetDataSources.put("default", this.buildDataSource("default", mainDbMap, poolMap));
        for (String dbId : customDbs.keySet()) {
            targetDataSources.put(dbId, this.buildDataSource(dbId, customDbs.get(dbId), poolMap));
        }
        dynamicDataSource.setTargetDataSources(targetDataSources);
        dynamicDataSource.setDefaultTargetDataSource(targetDataSources.get("default"));

        return dynamicDataSource;
    }

    private DataSource buildDataSource(String dbName, Map<String, Object> dbMap, Map<String, Object> poolMap) {
        AtomikosDataSourceBean atomikosDataSourceBean = new AtomikosDataSourceBean();

        Properties xaProperties = new Properties();
        xaProperties.put("driverClassName", String.valueOf(dbMap.get("driverClassName")));
        xaProperties.put("url", String.valueOf(dbMap.get("url")));
        xaProperties.put("password", String.valueOf(dbMap.get("password")));
        xaProperties.put("username", String.valueOf(dbMap.get("username")));
        xaProperties.put("initialSize", (Integer) poolMap.get("initial-size"));
        xaProperties.put("maxActive", (Integer) poolMap.get("maximum-pool-size"));
        xaProperties.put("minIdle", (Integer) poolMap.get("min-idle"));
        xaProperties.put("maxWait", (Integer) poolMap.get("max-wait"));
        xaProperties.put("validationQuery", (String) poolMap.get("validation-query"));
        xaProperties.put("testOnBorrow", (Boolean) poolMap.get("test-on-borrow"));
        xaProperties.put("testWhileIdle", (Boolean) poolMap.get("test-while-idle"));
        // xaProperties.put("pinGlobalTxToPhysicalConnection", true);

        atomikosDataSourceBean.setXaDataSourceClassName("com.alibaba.druid.pool.xa.DruidXADataSource");
        atomikosDataSourceBean.setPoolSize((Integer) poolMap.get("initial-size"));
        atomikosDataSourceBean.setMinPoolSize((Integer) poolMap.get("initial-size"));
        atomikosDataSourceBean.setMaxPoolSize((Integer) poolMap.get("maximum-pool-size"));
        atomikosDataSourceBean.setUniqueResourceName(dbName);
        atomikosDataSourceBean.setXaProperties(xaProperties);

        return atomikosDataSourceBean;

    }

    @Bean
    public DataSource datasource1() {
        AtomikosDataSourceBean ds = new AtomikosDataSourceBean();

        Properties xaProperties = new Properties();
        xaProperties.put("driverClassName", "com.mysql.jdbc.Driver");
        xaProperties.put("url", "jdbc:mysql://localhost:3306/test");
        xaProperties.put("password", "123456");
        xaProperties.put("username", "root");
        ds.setXaDataSourceClassName("com.alibaba.druid.pool.xa.DruidXADataSource");
        ds.setUniqueResourceName("datasource1");
        ds.setPoolSize(5);
        ds.setXaProperties(xaProperties);
        return ds;
    }

    @Bean
    public DataSource datasource2() {
        AtomikosDataSourceBean ds = new AtomikosDataSourceBean();

        Properties xaProperties = new Properties();
        xaProperties.put("driverClassName", "com.mysql.jdbc.Driver");
        xaProperties.put("url", "jdbc:mysql://localhost:3306/test1");
        xaProperties.put("password", "123456");
        xaProperties.put("username", "root");
        ds.setXaDataSourceClassName("com.alibaba.druid.pool.xa.DruidXADataSource");
        ds.setUniqueResourceName("datasource2");
        ds.setPoolSize(5);
        ds.setXaProperties(xaProperties);
        return ds;
    }

    @Bean("sysJdbcTemplate")
    public JdbcTemplate sysJdbcTemplate(@Qualifier("datasource1") DataSource ds) {
        return new JdbcTemplate(ds);
    }

    @Bean("busJdbcTemplate")
    public JdbcTemplate busJdbcTemplate(@Qualifier("datasource2") DataSource ds) {
        return new JdbcTemplate(ds);
    }

    @Bean("jdbcTemplate")
    public JdbcTemplate jdbcTemplate(@Qualifier("dataSource") DataSource ds) {
        return new JdbcTemplate(ds);
    }
}
