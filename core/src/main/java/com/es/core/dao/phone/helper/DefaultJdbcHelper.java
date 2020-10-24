package com.es.core.dao.phone.helper;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class DefaultJdbcHelper implements JdbcHelper {

    @Resource
    private JdbcTemplate jdbcTemplate;

    public boolean isEntityWithParamsExists(String tableName,
                                             Map<String,String> parameters){
        String query = "SELECT count(*) FROM " + tableName + " WHERE";
        query += parameters.entrySet().stream()
                .map(entry -> " " + entry.getKey() + " = '" + entry.getValue() + "'")
                .collect(Collectors.joining(" AND"));
        return 0 < jdbcTemplate.queryForObject(query, Integer.class);
    }

    public Number insertAndReturnGeneratedKey(String tableName, SqlParameterSource parameters, String generatedColumnName){
        return new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(tableName)
                .usingGeneratedKeyColumns(generatedColumnName)
                .executeAndReturnKey(parameters);
    }

    public void insert(String tableName, SqlParameterSource parameters){
        new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(tableName)
                .execute(parameters);
    }
}
