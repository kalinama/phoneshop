package com.es.core.order.dao.impl;

import com.es.core.order.dao.OrderItemDao;
import com.es.core.order.entity.OrderItem;
import com.es.core.phone.entity.Phone;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;
import org.unitils.reflectionassert.ReflectionComparatorMode;

import javax.annotation.Resource;

import static org.junit.Assert.assertSame;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

@ContextConfiguration("classpath:context/applicationContext-core-test.xml")
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
public class JdbcOrderItemDaoIntTest {
    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource(name = "jdbcOrderItemDao")
    private OrderItemDao orderItemDao;

    private Phone testPhone;

    private final static String tableName = "orderItems";
    private final static String QUERY_GET_ORDER_ITEM_BY_ID = "SELECT * FROM " + tableName + " WHERE id = ?";

    @Before
    public void setUp() {
        testPhone = new Phone();
        testPhone.setId(1000L);
    }

    @Test
    public void saveNewOrderItemWithoutIdTestToCheckAddingEntityToDB() {
        checkAddingEntityToDB(new OrderItem(null, testPhone, 2L), true);
    }

    @Test
    public void saveNewOrderItemWithoutIdTestToCheckAddingConcreteEntityToDB() {
        checkAddingConcreteEntityToDB(new OrderItem(null, testPhone, 2L));
    }

    @Test
    public void saveNewOrderItemWithNotExistedIdTestToCheckAddingEntityToDB() {
        checkAddingEntityToDB(new OrderItem(5L, testPhone, 2L), true);
    }

    @Test
    public void saveNewOrderItemWithNotExistedIdTestToCheckAddingConcreteEntityToDB() {
        checkAddingConcreteEntityToDB(new OrderItem(5L, testPhone, 2L));
    }

    @Test
    public void saveNewOrderItemWithExistedIdTestToCheckAddingEntityToDB() {
        checkAddingEntityToDB(new OrderItem(1L, testPhone, 2L), false);
    }

    @Test
    public void saveNewOrderItemWithExistedIdTestToCheckAddingConcreteEntityToDB() {
        checkAddingConcreteEntityToDB(new OrderItem(1L, testPhone, 2L));
    }

    private void checkAddingEntityToDB(OrderItem testNewOrderItem, boolean isAdded) {
        int quantityOfOrderItemsBeforeSave = JdbcTestUtils.countRowsInTable(jdbcTemplate, tableName);
        orderItemDao.save(testNewOrderItem);
        int quantityOfOrderItemsAfterSave = JdbcTestUtils.countRowsInTable(jdbcTemplate, tableName);

        if (isAdded) {
            assertSame(quantityOfOrderItemsBeforeSave + 1, quantityOfOrderItemsAfterSave);
        } else {
            assertSame(quantityOfOrderItemsBeforeSave, quantityOfOrderItemsAfterSave);
        }
    }

    private void checkAddingConcreteEntityToDB(OrderItem expectedOrderItem) {
        orderItemDao.save(expectedOrderItem);
        OrderItem actualColor = jdbcTemplate.queryForObject(QUERY_GET_ORDER_ITEM_BY_ID,
                new Object[]{expectedOrderItem.getId()}, new BeanPropertyRowMapper<>(OrderItem.class));

        expectedOrderItem.setPhone(null);
        expectedOrderItem.setOrder(null);
        assertReflectionEquals(expectedOrderItem, actualColor, ReflectionComparatorMode.IGNORE_DEFAULTS);
    }

}