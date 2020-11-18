package com.es.core.phone.dao.impl;

import com.es.core.phone.dao.ColorDao;
import com.es.core.phone.dao.helper.JdbcHelper;
import com.es.core.phone.entity.Color;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collections;

@Component
public class JdbcColorDao implements ColorDao {
    @Resource
    private NamedParameterJdbcTemplate parameterJdbcTemplate;
    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    private JdbcHelper defaultJdbcHelper;

    private final static String QUERY_FOR_COLORS_UPDATE = "UPDATE colors SET code = :code WHERE id = :id";

    @Override
    public void save(final Color color) {
        if (color.getId() == null) {
            Number newColorId = defaultJdbcHelper.insertAndReturnGeneratedKey("colors",
                    new BeanPropertySqlParameterSource(color), "id");
            color.setId(newColorId.longValue());
        } else if (defaultJdbcHelper.isEntityWithParamsExists("colors",
                Collections.singletonMap("id", color.getId().toString()))) {
            parameterJdbcTemplate.update(QUERY_FOR_COLORS_UPDATE, new BeanPropertySqlParameterSource(color));
        } else {
            defaultJdbcHelper.insert("colors", new BeanPropertySqlParameterSource(color));
        }
    }
}
