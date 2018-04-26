package com.easy.ms.dynamicdb;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

public class DynamicDataSourceProperties implements EnvironmentAware {
    private Environment environment;
    private Map<String, Object> mainDataSource;
    private Map<String, Object> pool;
    private Map<String, Map<String, Object>> customDataSource;
    private String[] customDataSourceNames;

    @PostConstruct
    public void init() {
        RelaxedPropertyResolver relaxedPropertyResolver = new RelaxedPropertyResolver(environment, "spring.");
        mainDataSource = relaxedPropertyResolver.getSubProperties("datasource.");
        pool = relaxedPropertyResolver.getSubProperties("pool.");

        relaxedPropertyResolver = new RelaxedPropertyResolver(environment, "custom.datasource.");

        customDataSourceNames = relaxedPropertyResolver.getProperty("names").split(",");
        customDataSource = new HashMap<>();
        for (int i = 0; i < customDataSourceNames.length; ++i) {
            customDataSource.put(customDataSourceNames[i],
                    relaxedPropertyResolver.getSubProperties(customDataSourceNames[i] + "."));
        }
    }

    public Map<String, Object> getMainDataSource() {
        return mainDataSource;
    }

    public void setMainDataSource(Map<String, Object> mainDataSource) {
        this.mainDataSource = mainDataSource;
    }

    public Map<String, Object> getPool() {
        return pool;
    }

    public void setPool(Map<String, Object> pool) {
        this.pool = pool;
    }

    public Map<String, Map<String, Object>> getCustomDataSource() {
        return customDataSource;
    }

    public void setCustomDataSource(Map<String, Map<String, Object>> customDataSource) {
        this.customDataSource = customDataSource;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public String[] getCustomDataSourceNames() {
        return customDataSourceNames;
    }

    public void setCustomDataSourceNames(String[] customDataSourceNames) {
        this.customDataSourceNames = customDataSourceNames;
    }

}
