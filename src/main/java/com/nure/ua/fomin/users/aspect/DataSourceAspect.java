package com.nure.ua.fomin.users.aspect;

import com.nure.ua.fomin.users.config.properties.DataSourcesProperties;
import com.nure.ua.fomin.users.entity.User;
import com.nure.ua.fomin.users.utils.DataSourceContextHolder;
import com.nure.ua.fomin.users.utils.ReflectionUtils;
import lombok.SneakyThrows;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.List;

@Aspect
@Component
public class DataSourceAspect {

    private static final String DATA_SOURCE_CONFIG_FIELD_NAME = "dataSourceConfig";

    @SneakyThrows
    @Around("@annotation(MultipleDataSource)")
    public List<User> enableDataSource(ProceedingJoinPoint joinPoint) {
        DataSourcesProperties.DataSourceConfig dataSourceConfig = (DataSourcesProperties.DataSourceConfig) ReflectionUtils
                .getValue(joinPoint.getTarget(), DATA_SOURCE_CONFIG_FIELD_NAME);

        DataSourceContextHolder.setCurrentDataSource(dataSourceConfig.getName());

        List<User> users = (List<User>) joinPoint.proceed();

        DataSourceContextHolder.clear();
        return users;
    }
}
