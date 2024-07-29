package com.nure.ua.fomin.users.config;

import com.nure.ua.fomin.users.config.properties.DataSourcesProperties;
import com.nure.ua.fomin.users.repository.UserRepository;
import com.nure.ua.fomin.users.repository.impl.UserRepositoryImpl;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
public class DbConfiguration {

    private final DataSourcesProperties dataSourcesProperties;

    @Bean
    public DataSource multiRoutingDataSource() {
        Map<Object, Object> dataSources = dataSourcesProperties.getDataSources()
                .stream()
                .collect(Collectors.toMap(DataSourcesProperties.DataSourceConfig::getName,
                        dataSourceConfig -> DataSourceBuilder.create()
                                .type(HikariDataSource.class)
                                .url(dataSourceConfig.getUrl())
                                .username(dataSourceConfig.getUser())
                                .password(dataSourceConfig.getPassword())
                                .build()));
        MultiRoutingDataSource multiRoutingDataSource = new MultiRoutingDataSource();
        multiRoutingDataSource.setTargetDataSources(dataSources);
        return multiRoutingDataSource;
    }

    @Bean
    public List<UserRepository> userRepositories(@Qualifier("multiRoutingDataSource") DataSource dataSource,
                                                 DataSourcesProperties dataSourcesProperties) {
        return dataSourcesProperties.getDataSources()
                .stream()
                .map(dataSourceConfig -> new UserRepositoryImpl(dataSource, dataSourceConfig))
                .collect(Collectors.toList());
    }

}
