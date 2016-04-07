package com.dbalthassat.restapi.config;

import com.dbalthassat.restapi.property.DataSourceProperty;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.sql.DataSource;

@Configuration
@EntityScan(Application.BASE_PACKAGE + ".entity")
@EnableJpaRepositories(Application.BASE_PACKAGE + ".repository")
@EnableJpaAuditing
public class DataSourceConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceConfiguration.class);

    @Autowired
    private DataSourceProperty dataSourceProperty;

    @Bean(destroyMethod = "shutdown")
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.addDataSourceProperty("url", dataSourceProperty.getUrl());
        config.setDataSourceClassName(dataSourceProperty.getDataSourceClassName());
        config.setUsername(dataSourceProperty.getUsername());
        config.setPassword(dataSourceProperty.getPassword());
        LOGGER.debug("Configuration de la base de données : {}", dataSourceProperty.getUrl());
        return new HikariDataSource(config);
    }
}
