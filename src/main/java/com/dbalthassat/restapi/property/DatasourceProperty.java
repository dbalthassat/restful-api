package com.dbalthassat.restapi.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@SuppressWarnings("unused")
@Component
@ConfigurationProperties("datasource")
public class DataSourceProperty {
    @NotNull
    private String url;

    @NotNull
    private String dataSourceClassName;

    @NotNull
    private String username;

    @NotNull
    private String password;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDataSourceClassName() {
        return dataSourceClassName;
    }

    public void setDataSourceClassName(String dataSourceClassName) {
        this.dataSourceClassName = dataSourceClassName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
