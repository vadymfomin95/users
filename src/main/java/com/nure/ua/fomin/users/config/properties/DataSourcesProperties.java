package com.nure.ua.fomin.users.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "ua.nure.fomin")
public class DataSourcesProperties {

    private List<DataSourceConfig> dataSources = new ArrayList<>();

    @Data
    public static class DataSourceConfig {
        private String name;
        private String strategy;
        private String url;
        private String table;
        private String user;
        private String password;
        private Mapping mapping = new Mapping();
    }

    @Data
    public static class Mapping {

        private String id;

        private String username;

        private String name;

        private String surname;
    }
}
