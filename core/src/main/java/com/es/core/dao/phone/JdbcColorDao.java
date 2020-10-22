package com.es.core.dao.phone;

import com.es.core.dao.phone.helper.JdbcHelper;
import com.es.core.model.phone.Color;

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
    private JdbcTemplate jdbcTemplate;
    private JdbcHelper jdbcHelper;

    private final static String QUERY_FOR_COLORS_UPDATE = "UPDATE colors SET code = :code WHERE id = :id";

    @Autowired
    public JdbcColorDao(JdbcHelper jdbcHelper) {
        this.jdbcHelper = jdbcHelper;
    }

    @Override
    public void save(Color color) {
        if (color.getId() == null) {
            Number newColorId = jdbcHelper.insertAndReturnGeneratedKey("colors",
                    new BeanPropertySqlParameterSource(color), "id");
            color.setId(newColorId.longValue());
        } else if (jdbcHelper.isEntityWithParamsExists("colors",
                Collections.singletonMap("id", color.getId().toString()))) {
            new NamedParameterJdbcTemplate(jdbcTemplate)
                    .update(QUERY_FOR_COLORS_UPDATE, new BeanPropertySqlParameterSource(color));
        } else {
            jdbcHelper.insert("colors", new BeanPropertySqlParameterSource(color));
        }
    }
}
