package com.es.core.dao.phone.helper;

import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.util.Map;
import java.util.Optional;

public interface JdbcHelper {

    void insert(String tableName, SqlParameterSource parameters);
    Number insertAndReturnGeneratedKey(String tableName, SqlParameterSource parameters, String generatedColumnName);
    boolean isEntityWithParamsExists(String tableName, Map<String,String> parameters);
}
