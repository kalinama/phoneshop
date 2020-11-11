package com.es.core.phone.dao.impl;

import com.es.core.phone.dao.PhoneDao;
import com.es.core.phone.dao.exception.PrimaryKeyUniquenessException;
import com.es.core.phone.dao.impl.JdbcPhoneDao;
import com.es.core.phone.entity.Color;
import com.es.core.phone.entity.Phone;
import com.es.core.phone.entity.Stock;
import com.es.core.phone.enums.SortOrder;
import com.es.core.phone.enums.SortParameter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.test.util.AopTestUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ContextConfiguration("classpath:context/applicationContext-core-test.xml")
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
public class JdbcPhoneDaoIntTest {
    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    private PhoneDao jdbcPhoneDao;
    private List<Phone> testPhones;
    private List<Stock> testPhoneStocks;
    private Phone testNewPhone;

    private final static String tableName = "phones";

    private final static String QUERY_GET_PHONE_BY_ID = "SELECT * FROM " + tableName + " WHERE id = ?";
    private final static String QUERY_GET_PHONE_STOCKS = "SELECT stocks.stock, stocks.reserved FROM stocks WHERE phoneId = ?";
    private final static String QUERY_GET_PHONE_COLORS = "SELECT colors.id, colors.code FROM " +
                                                            "(SELECT * FROM phone2color WHERE phoneId = ?) " +
                                                        "AS phone2colorExcerpt INNER JOIN colors " +
                                                            "ON phone2colorExcerpt.colorId = colors.id";
    @Before
    public void setUp() {
        List<Long> ids = Arrays.asList(1000L, 1002L, 1057L, 1092L, 1132L, 1192L, 1577L, 1777L, 1864L, 1914L, 8763L);
        testPhones = new ArrayList<>();
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
            testPhones.add(testPhone);
        }
        Set<Color> newPhoneColors = new HashSet<>(Arrays.asList(new Color(1000L, "Black"), new Color(1001L, "White"), new Color("Pink")));
        testNewPhone = new Phone(null, "POJ", "POJ 1011 G9", null, new BigDecimal("10.1"), 482, new BigDecimal("276.0"), new BigDecimal("167.0"), new BigDecimal("12.6"), null, "Tablet", "Android (4.0)", newPhoneColors, "1280 x  800", 149, null, null, new BigDecimal("1.3"), null, new BigDecimal("8.0"), null, null, null, "2.1, EDR", "GPS", "manufacturer/ARCHOS/ARCHOS 101 G9.jpg", "The ARCHOS 101 G9 is a 10.1'' tablet, equipped with Google's open source OS. It offers a multi-core ARM CORTEX A9 processor at 1GHz, 8 or 16GB internal memory, microSD card slot, GPS, Wi-Fi, Bluetooth 2.1, and more.");
    }

    @Test
    public void getEntityByIdSuccessTest() {
        long id = 1000L;
        Optional<Phone> actualPhone = jdbcPhoneDao.get(id);
        Optional<Phone> expectedPhone = testPhones.stream()
                .filter(phone -> phone.getId().equals(id))
                .findFirst();
        assertEquals(expectedPhone, actualPhone);
    }

    @Test(expected = PrimaryKeyUniquenessException.class)
    @DirtiesContext
    public void getEntityByIdMoreThanOneTest() throws NoSuchFieldException, IllegalAccessException {
        List<Phone> phones = Arrays.asList(new Phone(), new Phone());
        JdbcTemplate jdbcTemplateMock = Mockito.mock(jdbcTemplate.getClass());
        Field field = JdbcPhoneDao.class.getDeclaredField("jdbcTemplate");
        field.setAccessible(true);
        field.set(AopTestUtils.getUltimateTargetObject(jdbcPhoneDao), jdbcTemplateMock);
        when(jdbcTemplateMock.query(anyString(), any(Object[].class), any(ResultSetExtractor.class)))
                .thenReturn(phones);
        jdbcPhoneDao.get(1L);
    }

    @Test
    public void getEntityByIdEmptyTest() {
        Optional<Phone> expectedPhone = Optional.empty();
        Optional<Phone> actualPhone = jdbcPhoneDao.get(999L);
        assertEquals(expectedPhone, actualPhone);
    }

    private Stream<Phone> getFilterPhones() {
        return testPhones.stream()
                .filter(phone -> !phone.getColors().isEmpty())
                .filter(phone -> testPhoneStocks.stream()
                        .anyMatch(stock -> phone.equals(stock.getPhone()) && stock.getStock()>0))
                .filter(phone -> !(phone.getPrice() == null) && (phone.getPrice().compareTo(new BigDecimal(0)) > 0));
    }

 /*@Test
    public void findAllPhonesWithColorsWithStockWithPriceWithoutQueryTest() {
        int offset = 1;
        int limit = 3;
        List<Phone> actualPhones = jdbcPhoneDao.findAll(offset, limit, null, SortParameter.brand, SortOrder.asc);
        List<Phone> expectedPhones = getFilterPhones()
                .skip(offset)
                .limit(limit)
                .collect(Collectors.toList());
        assertEquals(expectedPhones, actualPhones);
    }*/

   /* @Test
    public void findAllPhonesWithColorsWithStockWithPriceWithQueryTest() {
        int offset = 0;
        int limit = 10;
        String query = "a";
        List<Phone> actualPhones = jdbcPhoneDao.findAll(offset, limit, query, SortParameter.brand, SortOrder.asc);
        List<Phone> expectedPhones = getFilterPhones()
                .filter(phone -> phone.getBrand().toLowerCase().contains(query.toLowerCase())
                        || phone.getModel().toLowerCase().contains(query.toLowerCase()))
                .skip(offset)
                .limit(limit)
                .collect(Collectors.toList());
        assertEquals(expectedPhones, actualPhones);
    }*/

    /*@Test
    public void findAllPhonesSortedByPriceDescTest() {
        int offset = 1;
        int limit = 3;
        List<Phone> actualPhones = jdbcPhoneDao.findAll(offset, limit, null, SortParameter.price, SortOrder.desc);
        List<Phone> expectedPhones = getFilterPhones()
                .sorted(Comparator.comparing(Phone::getPrice).reversed())
                .skip(offset)
                .limit(limit)
                .collect(Collectors.toList());
        assertEquals(expectedPhones, actualPhones);
    }*/

    @Test(expected = IllegalArgumentException.class)
    public void findAllPhonesThrowException() {
        int offset = 1;
        int limit = 3;
        jdbcPhoneDao.findAll(offset, limit, null, null, SortOrder.desc);
    }

   /* @Test
    public void getQuantityWithQuery() {
        String query = "t";
        long actualPhoneQuantity = jdbcPhoneDao.getQuantity(query);
        long expectedPhoneQuantity = getFilterPhones()
                .filter(phone -> phone.getBrand().toLowerCase().contains(query.toLowerCase())
                        || phone.getModel().toLowerCase().contains(query.toLowerCase())).count();
        assertEquals(expectedPhoneQuantity, actualPhoneQuantity);
    }*/

    /*@Test
    public void getQuantityWithoutQuery() {
        long actualPhoneQuantity = jdbcPhoneDao.getQuantity(null);
        long expectedPhoneQuantity = getFilterPhones().count();
        assertEquals(expectedPhoneQuantity, actualPhoneQuantity);
    }*/

    @Test
    public void saveNewPhoneWithoutIdTestToCheckAddingEntityToDB() {
        checkAddingEntityToDB(true);
    }

    @Test
    public void saveNewPhoneWithoutIdTestToCheckAddingConcreteEntityToDB() {
        checkAddingConcreteEntityToDB();
    }

    @Test
    public void saveNewPhoneWithNotExistedIdTestToCheckAddingEntityToDB() {
        testNewPhone.setId(20000L);
        checkAddingEntityToDB(true);
    }

    @Test
    public void saveNewPhoneWithNotExistedIdTestToCheckAddingConcreteEntityToDB() {
        testNewPhone.setId(20000L);
        checkAddingConcreteEntityToDB();
    }

    @Test
    public void saveNewPhoneWithExistedIdTestToCheckAddingEntityToDB() {
        testNewPhone.setId(1000L);
        checkAddingEntityToDB(false);
    }

    @Test
    public void saveNewPhoneWithExistedIdTestToCheckAddingConcreteEntityToDB() {
        testNewPhone.setId(1000L);
        checkAddingConcreteEntityToDB();
    }

    @Test(expected = DataAccessException.class)
    public void savePhoneWithExistedModelBrandAndNewIdTestToCheckThrowingException() {
        testNewPhone.setId(20000L);
        testNewPhone.setModel(testPhones.get(0).getModel());
        testNewPhone.setBrand(testPhones.get(0).getBrand());
        jdbcPhoneDao.save(testNewPhone);
    }

    private void checkAddingEntityToDB(boolean isAdded) {
        int quantityOfPhonesBeforeSave = JdbcTestUtils.countRowsInTable(jdbcTemplate, tableName);
        jdbcPhoneDao.save(testNewPhone);
        int quantityOfPhonesAfterSave = JdbcTestUtils.countRowsInTable(jdbcTemplate, tableName);
        if (isAdded) {
            assertSame(quantityOfPhonesBeforeSave + 1, quantityOfPhonesAfterSave);
        } else {
            assertSame(quantityOfPhonesBeforeSave, quantityOfPhonesAfterSave);
        }
    }

    private void checkAddingConcreteEntityToDB() {
        jdbcPhoneDao.save(testNewPhone);
        Phone actualPhone = jdbcTemplate.queryForObject(QUERY_GET_PHONE_BY_ID, new Object[]{testNewPhone.getId()},
                new BeanPropertyRowMapper<>(Phone.class));
        List<Color> actualPhoneColors = jdbcTemplate.query(QUERY_GET_PHONE_COLORS, new Object[]{testNewPhone.getId()},
                new BeanPropertyRowMapper<>(Color.class));
        actualPhone.setColors(new HashSet<>(actualPhoneColors));
        assertEquals(testNewPhone, actualPhone);
    }
}
