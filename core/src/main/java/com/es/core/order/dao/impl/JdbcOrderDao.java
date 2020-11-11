package com.es.core.order.dao.impl;

import com.es.core.order.dao.OrderDao;
import com.es.core.order.dao.OrderItemDao;
import com.es.core.order.entity.Order;
import com.es.core.phone.dao.exception.PrimaryKeyUniquenessException;
import com.es.core.phone.dao.helper.JdbcHelper;
import org.simpleflatmapper.jdbc.spring.JdbcTemplateMapperFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Types;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class JdbcOrderDao implements OrderDao {

    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    private NamedParameterJdbcTemplate parameterJdbcTemplate;
    @Resource
    private JdbcHelper defaultJdbcHelper;
    @Resource
    private OrderItemDao jdbcOrderItemDao;

    private final static String QUERY_FOR_ORDERS_UPDATE = "UPDATE orders SET secureId=:secureId, subtotal=:subtotal, " +
            "deliveryPrice=:deliveryPrice, totalPrice=:totalPrice, firstName=:firstName, lastName=:lastName, " +
            "deliveryAddress=:deliveryAddress, contactPhoneNo=:contactPhoneNo, additionalInformation=:additionalInformation, " +
            "date=:date, status=:status WHERE id=:id";

    private final static String QUERY_FOR_ORDER2ORDER_ITEM_DELETE = "DELETE FROM order2orderItem WHERE orderId=:id";

    private final static String QUERY_FOR_ORDERS_GET_ORDER_BY_SECURE_ID = "SELECT searchedOrder.id AS id, secureId, subtotal, " +
            "deliveryPrice, totalPrice, firstName, lastName, deliveryAddress, contactPhoneNo, additionalInformation, orderingDate, " +
            "status, orderItems.id AS orderItems_id, orderItems.quantity AS orderItems_quantity, phones.id AS orderItems_phone_id, " +
            "phones.brand AS orderItems_phone_brand, phones.model AS orderItems_phone_model, phones.price AS orderItems_phone_price, " +
            "phones.displaySizeInches AS orderItems_phone_displaySizeInches, phones.weightGr AS orderItems_phone_weightGr, " +
            "phones.lengthMm AS orderItems_phone_lengthMm , phones.widthMm AS orderItems_phone_widthMm, phones.heightMm AS orderItems_phone_heightMm, " +
            "phones.announced AS orderItems_phone_announced, phones.deviceType AS orderItems_phone_deviceType, phones.os AS orderItems_phone_os, " +
            "phones.displayResolution AS orderItems_phone_displayResolution, phones.pixelDensity AS orderItems_phone_pixelDensity, " +
            "phones.displayTechnology AS orderItems_phone_displayTechnology, phones.backCameraMegapixels AS orderItems_phone_backCameraMegapixels, " +
            "phones.frontCameraMegapixels AS orderItems_phone_frontCameraMegapixels, phones.ramGb AS orderItems_phone_ramGb, " +
            "phones.internalStorageGb AS orderItems_phone_internalStorageGb, phones.batteryCapacityMah AS orderItems_phone_batteryCapacityMah, " +
            "phones.talkTimeHours AS orderItems_phone_talkTimeHours, phones.standByTimeHours AS orderItems_phone_standByTimeHours, " +
            "phones.bluetooth AS orderItems_phone_bluetooth, phones.positioning AS orderItems_phone_positioning, phones.imageUrl AS " +
            "orderItems_phone_imageUrl, phones.description AS orderItems_phone_description, colors.id AS orderItems_phone_colors_id, " +
            "colors.code AS orderItems_phone_colors_code FROM (SELECT * FROM orders WHERE orders.secureId = ?) AS searchedOrder " +
                "INNER JOIN order2orderItem ON searchedOrder.id = order2orderItem.orderId " +
                    "INNER JOIN orderItems ON order2orderItem.orderItemId = orderItems.id " +
                        "INNER JOIN phones ON orderItems.phoneId = phones.id " +
                            "INNER JOIN phone2color ON phones.id = phone2color.phoneId " +
                                "INNER JOIN colors ON colors.id = phone2color.colorId";


    @Override
    public Optional<Order> get(String secureId) {
        ResultSetExtractor<List<Order>> resultSetExtractor = JdbcTemplateMapperFactory
                .newInstance().addKeys("id", "orderItems_phone_id")
                .newResultSetExtractor(Order.class);
        List<Order> orders = jdbcTemplate.query(QUERY_FOR_ORDERS_GET_ORDER_BY_SECURE_ID,
                new Object[]{secureId}, resultSetExtractor);

        if (orders.size() > 1) {
            throw new PrimaryKeyUniquenessException();
        }
        if (orders.isEmpty()) {
            return Optional.empty();
        }
        Order order = orders.get(0);
        order.getOrderItems().forEach(orderItem -> orderItem.setOrder(order));
        return Optional.of(order);
    }

    @Override
    @Transactional(rollbackFor = DataAccessException.class)
    public void save(final Order order) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(order);
        parameterSource.registerSqlType("status", Types.VARCHAR);

        if (order.getId() == null) {
            Number newId = defaultJdbcHelper.insertAndReturnGeneratedKey("orders",
                    parameterSource, "id");
            order.setId(newId.longValue());
        } else if (defaultJdbcHelper.isEntityWithParamsExists("orders",
                Collections.singletonMap("id", order.getId().toString()))) {
            rewriteOrderAndRemoveBindToItems(order);
        } else {
            defaultJdbcHelper.insert("orders", parameterSource);
        }
        saveOrderItems(order);
    }

    private void saveOrderItems(Order order) {
        order.getOrderItems().forEach(orderItem -> {
            jdbcOrderItemDao.save(orderItem);
            defaultJdbcHelper.insert("order2orderItem", new MapSqlParameterSource()
                    .addValue("orderId", order.getId())
                    .addValue("orderItemId", orderItem.getId()));
        });
    }

    private void rewriteOrderAndRemoveBindToItems(Order order) {
        parameterJdbcTemplate.update(QUERY_FOR_ORDERS_UPDATE, new BeanPropertySqlParameterSource(order));
        parameterJdbcTemplate.update(QUERY_FOR_ORDER2ORDER_ITEM_DELETE,
                new MapSqlParameterSource("id", order.getId()));
    }

}
