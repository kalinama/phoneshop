package com.es.core.order.dao.impl;

import com.es.core.order.dao.OrderItemDao;
import com.es.core.order.entity.OrderItem;
import com.es.core.phone.dao.helper.JdbcHelper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collections;

@Component
public class JdbcOrderItemDao implements OrderItemDao {

    @Resource
    private NamedParameterJdbcTemplate parameterJdbcTemplate;
    @Resource
    private JdbcHelper defaultJdbcHelper;

    private final static String QUERY_FOR_ODER_ITEMS_UPDATE = "UPDATE orderItems SET phoneId = :phoneId, " +
            "quantity = :quantity WHERE id = :id";

    @Override
    public void save(final OrderItem orderItem) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("phoneId", orderItem.getPhone().getId())
                .addValue("quantity", orderItem.getQuantity())
                .addValue("id", orderItem.getId());

        if (orderItem.getId() == null) {
            Number newOrderItemId = defaultJdbcHelper.insertAndReturnGeneratedKey("orderItems",
                    parameterSource, "id");
            orderItem.setId(newOrderItemId.longValue());
        } else if (defaultJdbcHelper.isEntityWithParamsExists("orderItems",
                Collections.singletonMap("id", orderItem.getId().toString()))) {
            parameterJdbcTemplate.update(QUERY_FOR_ODER_ITEMS_UPDATE, parameterSource);
        } else {
            defaultJdbcHelper.insert("orderItems", parameterSource);
        }
    }
}
