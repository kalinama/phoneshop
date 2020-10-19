package com.es.core.model.phone;

import org.springframework.core.env.MapPropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class JdbcHelper {

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

    public Optional<Long> getIdOfExistedEntityByUniqueParams(String tableName, String idColumnName,
                                                              Map<String,String> uniqueParametersCombination){
        String query = "SELECT " + idColumnName + "  FROM " + tableName + " WHERE";
        query += uniqueParametersCombination.entrySet().stream()
                .map(entry -> " " + entry.getKey() + " = '" + entry.getValue() + "'")
                .collect(Collectors.joining("AND"));
        return jdbcTemplate.query(query, (ResultSet rs) -> {
            if (rs.next()) {
                return Optional.of(rs.getLong(idColumnName));
            } else {
                return Optional.empty();
            }
        });
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
