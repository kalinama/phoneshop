package com.es.core.phone.dao;

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
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.AopTestUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ContextConfiguration("classpath:context/applicationContext-core-test.xml")
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
public class JdbcStockDaoIntTest {
    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource(name = "jdbcStockDao")
    private StockDao stockDao;

    private List<Stock> testPhoneStocks;

    private final static String QUERY_GET_PHONE_BY_ID = "SELECT * FROM phones WHERE id = ?";
    private final static String QUERY_GET_PHONE_STOCKS = "SELECT stocks.stock, stocks.reserved FROM stocks WHERE phoneId = ?";
    private final static String QUERY_GET_PHONE_COLORS = "SELECT colors.id, colors.code FROM " +
            "(SELECT * FROM phone2color WHERE phoneId = ?) " +
            "AS phone2colorExcerpt INNER JOIN colors " +
            "ON phone2colorExcerpt.colorId = colors.id";

    @Before
    public void setUp() {
        List<Long> ids = Arrays.asList(1000L, 1002L, 1057L, 1092L, 1132L, 1192L, 1577L, 1777L, 1864L, 1914L, 8763L);
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
    }

    @Test
    public void getByPhoneIdSuccess() {
        Long id = testPhoneStocks.get(0).getPhone().getId();
        Optional<Stock> actualStock = stockDao.get(id);
        Optional<Stock> expectedStock = Optional.of(testPhoneStocks.get(0));
       assertEquals(expectedStock, actualStock);
    }

    @Test(expected = PrimaryKeyUniquenessException.class)
    @DirtiesContext
    public void getStockByIdMoreThanOneTest() throws NoSuchFieldException, IllegalAccessException {
        List<Stock> stocks = Arrays.asList(new Stock(), new Stock());
        JdbcTemplate jdbcTemplateMock = Mockito.mock(jdbcTemplate.getClass());
        Field field = JdbcStockDao.class.getDeclaredField("jdbcTemplate");
        field.setAccessible(true);
        field.set(AopTestUtils.getUltimateTargetObject(stockDao), jdbcTemplateMock);
        when(jdbcTemplateMock.query(anyString(), any(Object[].class), any(RowMapper.class)))
                .thenReturn(stocks);
        stockDao.get(1L);
    }

    @Test
    public void getByNotExistedPhoneId() {
        Long id = 1L;
        Optional<Stock> actualStock = stockDao.get(id);
        Optional<Stock> expectedStock = Optional.empty();
        assertEquals(expectedStock, actualStock);
    }

}
