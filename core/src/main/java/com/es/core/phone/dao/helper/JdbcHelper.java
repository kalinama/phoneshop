package com.es.core.phone.dao.helper;

import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.util.Map;

public interface JdbcHelper {

    void insert(String tableName, SqlParameterSource parameters);
    Number insertAndReturnGeneratedKey(String tableName, SqlParameterSource parameters, String generatedColumnName);
    boolean isEntityWithParamsExists(String tableName, Map<String,String> parameters);
}
