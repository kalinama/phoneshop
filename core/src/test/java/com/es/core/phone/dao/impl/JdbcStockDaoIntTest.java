package com.es.core.phone.dao.impl;

import com.es.core.phone.dao.StockDao;
import com.es.core.phone.dao.exception.PrimaryKeyUniquenessException;
import com.es.core.phone.entity.Color;
import com.es.core.phone.entity.Phone;
import com.es.core.phone.entity.Stock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.test.util.AopTestUtils;
import org.springframework.transaction.annotation.Transactional;
import org.unitils.reflectionassert.ReflectionComparatorMode;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

@ContextConfiguration("classpath:context/applicationContext-core-test.xml")
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
public class JdbcStockDaoIntTest {
    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource(name = "jdbcStockDao")
    private StockDao stockDao;

    private List<Stock> testPhoneStocks;
    private Long phoneIdWithoutStock;

    private final static String tableName = "stocks";

    private final static String QUERY_GET_PHONE_BY_ID = "SELECT * FROM phones WHERE id = ?";
    private final static String QUERY_GET_PHONE_STOCKS = "SELECT stocks.stock, stocks.reserved FROM stocks WHERE phoneId = ?";
    private final static String QUERY_GET_PHONE_COLORS = "SELECT colors.id, colors.code FROM " +
            "(SELECT * FROM phone2color WHERE phoneId = ?) " +
            "AS phone2colorExcerpt INNER JOIN colors " +
            "ON phone2colorExcerpt.colorId = colors.id";
    private final static String QUERY_GET_STOCK_BY_ID = "SELECT * FROM " + tableName + " WHERE phoneId = ?";


    @Before
    public void setUp() {
        List<Long> ids = Arrays.asList(1000L, 1057L, 1092L, 1132L, 1192L, 1577L, 1777L, 1864L, 1914L, 8763L);
        testPhoneStocks = new ArrayList<>();
        for (long id : ids) {
            Phone testPhone = jdbcTemplate.queryForObject(QUERY_GET_PHONE_BY_ID, new Object[]{id},
                    new BeanPropertyRowMapper<>(Phone.class));
            List<Color> testPhoneColors = jdbcTemplate.query(QUERY_GET_PHONE_COLORS, new Object[]{id},
                    new BeanPropertyRowMapper<>(Color.class));
            testPhone.setColors(new HashSet<>(testPhoneColors));
            Stock stock = jdbcTemplate.queryForObject(QUERY_GET_PHONE_STOCKS, new Object[]{id},
                    new BeanPropertyRowMapper<>(Stock.class));
            stock.setPhone(testPhone);
            testPhoneStocks.add(stock);
        }
        phoneIdWithoutStock = 1002L;
    }

    @Test
    public void getByPhoneIdSuccess() {
        Long id = testPhoneStocks.get(0).getPhone().getId();
        Optional<Stock> actualStock = stockDao.get(id);
        Optional<Stock> expectedStock = Optional.of(testPhoneStocks.get(0));
        assertReflectionEquals(expectedStock, actualStock);
    }

    @Test(expected = PrimaryKeyUniquenessException.class)
    @DirtiesContext
    public void getStockByIdMoreThanOneTest() throws NoSuchFieldException, IllegalAccessException {
        List<Stock> stocks = Arrays.asList(new Stock(new Phone(), 1L, 2L),
                new Stock(new Phone(), 0L, 3L));
        JdbcTemplate jdbcTemplateMock = Mockito.mock(jdbcTemplate.getClass());
        Field field = JdbcStockDao.class.getDeclaredField("jdbcTemplate");
        field.setAccessible(true);
        field.set(AopTestUtils.getUltimateTargetObject(stockDao), jdbcTemplateMock);
        when(jdbcTemplateMock.query(anyString(), any(Object[].class), any(ResultSetExtractor.class)))
                .thenReturn(stocks);
        stockDao.get(1L);
    }

    @Test
    public void getByNotExistedPhoneId() {
        Long id = 1L;
        Optional<Stock> actualStock = stockDao.get(id);
        Optional<Stock> expectedStock = Optional.empty();
        assertReflectionEquals(expectedStock, actualStock);
    }

    @Test(expected = IllegalArgumentException.class)
    public void saveWithIdNull() {
        Phone phone = new Phone();
        phone.setId(null);
        stockDao.save(new Stock(phone, 0L, 1L));
    }

    @Test
    public void saveWithExistedIdCheckAddingEntityToDB() {
        Phone phone = new Phone();
        long id = testPhoneStocks.get(0).getPhone().getId();
        phone.setId(id);
        Stock stock = new Stock(phone, 2L,3L);

        checkAddingEntityToDB(stock, false);
    }

    @Test
    public void saveWithExistedIdCheckAddingConcreteEntityToDB() {
        Phone phone = new Phone();
        long id = testPhoneStocks.get(0).getPhone().getId();
        phone.setId(id);
        Stock stock = new Stock(phone, 2L,3L);

        checkAddingConcreteEntityToDB(stock);
    }

    @Test
    public void saveWithNotExistedIdCheckAddingEntityToDB() {
        Phone phone = new Phone();
        phone.setId(phoneIdWithoutStock);
        Stock stock = new Stock(phone, 2L,3L);

        checkAddingEntityToDB(stock, true);
    }

    @Test
    public void saveWithNotExistedIdCheckAddingConcreteEntityToDB() {
        Phone phone = new Phone();
        phone.setId(phoneIdWithoutStock);
        Stock stock = new Stock(phone, 2L,3L);

        checkAddingConcreteEntityToDB(stock);
    }

    private void checkAddingEntityToDB(Stock testNewStock, boolean isAdded) {
        int quantityOfStocksBeforeSave = JdbcTestUtils.countRowsInTable(jdbcTemplate, tableName);
        stockDao.save(testNewStock);
        int quantityOfStocksAfterSave = JdbcTestUtils.countRowsInTable(jdbcTemplate, tableName);

        if (isAdded) {
            assertSame(quantityOfStocksBeforeSave + 1, quantityOfStocksAfterSave);
        } else {
            assertSame(quantityOfStocksBeforeSave, quantityOfStocksAfterSave);
        }
    }

    private void checkAddingConcreteEntityToDB(Stock expectedStock) {
        stockDao.save(expectedStock);
        Stock actualStock = jdbcTemplate.queryForObject(QUERY_GET_STOCK_BY_ID,
                new Object[]{expectedStock.getPhone().getId()}, new BeanPropertyRowMapper<>(Stock.class));

        expectedStock.setPhone(null);
        assertReflectionEquals(expectedStock, actualStock, ReflectionComparatorMode.IGNORE_DEFAULTS);
    }

}
