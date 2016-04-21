package com.dbalthassat.restapi.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@SuppressWarnings("unused")
@Component
@ConfigurationProperties("env")
public class EnvProperty {
    @NotNull
    private String profile;

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}
