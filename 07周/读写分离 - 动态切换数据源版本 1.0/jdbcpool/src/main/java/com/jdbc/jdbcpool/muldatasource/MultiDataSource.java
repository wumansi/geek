package com.jdbc.jdbcpool.muldatasource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Component
public class MultiDataSource extends AbstractRoutingDataSource {

    private static final ThreadLocal<String> dataSourceKey = new InheritableThreadLocal<>();

    public static void setDataSourceKey(String dataSource){
        dataSourceKey.set(dataSource);
    }

    public static void toDefault() {
        dataSourceKey.remove();
    }
    public MultiDataSource(@Qualifier("db1DataSource") DataSource db1DataSource, @Qualifier("db2DataSource") DataSource db2DataSource){
        this.setDefaultTargetDataSource(db1DataSource);
        Map<Object,Object> dsMap = new HashMap<>();
        dsMap.put("dataSource", db1DataSource);
        dsMap.put("dataSource2", db2DataSource);
        this.setTargetDataSources(dsMap);
    }
    @Override
    protected Object determineCurrentLookupKey() {
        return dataSourceKey.get();
    }
}
