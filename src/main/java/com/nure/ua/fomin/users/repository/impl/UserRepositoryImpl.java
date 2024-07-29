package com.nure.ua.fomin.users.repository.impl;

import com.nure.ua.fomin.users.config.properties.DataSourcesProperties;
import com.nure.ua.fomin.users.entity.Filter;
import com.nure.ua.fomin.users.entity.User;
import com.nure.ua.fomin.users.repository.UserRepository;
import com.nure.ua.fomin.users.utils.DataSourceContextHolder;
import com.nure.ua.fomin.users.utils.ReflectionUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UserRepositoryImpl implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final RowMapper<User> rowMapper;

    private final DataSourcesProperties.DataSourceConfig dataSourceConfig;

    public UserRepositoryImpl(DataSource dataSource, DataSourcesProperties.DataSourceConfig dataSourceConfig) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.dataSourceConfig = dataSourceConfig;
        this.rowMapper = (rs, rowNum) -> {
            User user = new User();
            user.setId(rs.getString(dataSourceConfig.getMapping().getId()));
            user.setName(rs.getString(dataSourceConfig.getMapping().getName()));
            user.setSurname(rs.getString(dataSourceConfig.getMapping().getSurname()));
            user.setUsername(rs.getString(dataSourceConfig.getMapping().getUsername()));
            return user;
        };
    }

    @Override
    public List<User> getAll() {
        DataSourceContextHolder.setCurrentDataSource(dataSourceConfig.getName());
        List<User> userEntities = jdbcTemplate.query(String.format("select * from %s", dataSourceConfig.getTable()), rowMapper);
        DataSourceContextHolder.clear();
        return userEntities;
    }

    @Override
    public List<User> search(List<Filter> searchCriteria) {
        DataSourceContextHolder.setCurrentDataSource(dataSourceConfig.getName());

        Map<String, String> sqlParameters = searchCriteria
                .stream()
                .filter(filter -> ReflectionUtils.getValue(dataSourceConfig.getMapping(), filter.getName()) != null)
                .collect(Collectors.toMap(filter -> (String) ReflectionUtils.getValue(dataSourceConfig.getMapping(), filter.getName()),
                        Filter::getValue));

        if (sqlParameters.isEmpty()) {
            return getAll();
        }

        String filteringPart = sqlParameters.keySet()
                .stream()
                .map(key -> key + "= :" + key)
                .collect(Collectors.joining(" and "));
        String query = String.format("select * from %s where %s", dataSourceConfig.getTable(), filteringPart);

        SqlParameterSource namedParameters = new MapSqlParameterSource().addValues(sqlParameters);
        List<User> userEntities = namedParameterJdbcTemplate.query(query, namedParameters, rowMapper);

        DataSourceContextHolder.clear();
        return userEntities;
    }
}
