package com.aditor.dashboard.spring;

import com.aditor.dashboard.spring.validation.ColumnMappingConfig;
import com.aditor.dashboard.spring.validation.ColumnMappingConfigsHolder;
import com.aditor.dashboard.spring.validation.ColumnMappingConfigsLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

/**
 * Created by art on 12/1/15.
 */
@Configuration
@ComponentScan("com.aditor.dashboard")
public class SpringConfig {

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://10.240.0.15:3306/appsflyer?useServerPrepStmts=true&zeroDateTimeBehavior=convertToNull");
        //dataSource.setUrl("jdbc:mysql://localhost:3306/appsflyer?useServerPrepStmts=true&zeroDateTimeBehavior=convertToNull");
        dataSource.setUsername("root");
        //dataSource.setPassword("123456");
        dataSource.setPassword("Q!w2E#r4T%");
        return dataSource;
    }

    @Bean
    public ColumnMappingConfigsHolder columnMappingConfigsHolder()
    {
        ColumnMappingConfigsLoader loader = new ColumnMappingConfigsLoader();
        ColumnMappingConfigsHolder holder = new ColumnMappingConfigsHolder();

        Map<String, List<ColumnMappingConfig>> columnConfigs = loader.getColumnConfigs();
        columnConfigs.keySet().forEach(configName -> {
            holder.addConfigList(columnConfigs.get(configName), configName);
        });


        return holder;
    }
}
