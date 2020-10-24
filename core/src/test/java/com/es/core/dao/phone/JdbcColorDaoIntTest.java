package com.es.core.dao.phone;

import com.es.core.model.phone.Color;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

@ContextConfiguration("classpath:context/applicationContext-core-test.xml")
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
public class JdbcColorDaoIntTest {
    @Resource
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ColorDao colorDao;

    private final static String tableName = "colors";
    private final static String QUERY_GET_COLOR_BY_ID = "SELECT * FROM " + tableName + " WHERE id = ?";

    @Test
    public void saveNewColorWithoutIdTestToCheckAddingEntityToDB() {
        checkAddingEntityToDB(new Color("Pink"), true);
    }

    @Test
    public void saveNewColorWithoutIdTestToCheckAddingConcreteEntityToDB() {
        Color expectedColor = new Color("Pink");
        checkAddingConcreteEntityToDB(expectedColor);
    }

    @Test
    public void saveNewColorWithNotExistedIdTestToCheckAddingEntityToDB() {
        checkAddingEntityToDB(new Color(2000L, "Pink"), true);
    }

    @Test
    public void saveNewColorWithNotExistedIdTestToCheckAddingConcreteEntityToDB() {
        Color expectedColor = new Color(2000L, "Pink");
        checkAddingConcreteEntityToDB(expectedColor);
    }

    @Test
    public void saveNewColorWithExistedIdTestToCheckAddingEntityToDB() {
        checkAddingEntityToDB(new Color(1000L, "Pink"), false);
    }

    @Test
    public void saveNewColorWithExistedIdTestToCheckAddingConcreteEntityToDB() {
        Color expectedColor = new Color(1000L, "Pink");
        checkAddingConcreteEntityToDB(expectedColor);
    }

    @Test(expected = DataAccessException.class)
    public void saveColorWithExistedCodeAndNewIdTestToCheckThrowingException() {
        colorDao.save(new Color(2000L, "Black"));
    }

    private void checkAddingEntityToDB(Color testNewColor, boolean isAdded) {
        int quantityOfColorsBeforeSave = JdbcTestUtils.countRowsInTable(jdbcTemplate, tableName);
        colorDao.save(testNewColor);
        int quantityOfColorsAfterSave = JdbcTestUtils.countRowsInTable(jdbcTemplate, tableName);
        if (isAdded) {
            assertSame(quantityOfColorsBeforeSave + 1, quantityOfColorsAfterSave);
        } else {
            assertSame(quantityOfColorsBeforeSave, quantityOfColorsAfterSave);
        }
    }

    private void checkAddingConcreteEntityToDB(Color expectedColor) {
        colorDao.save(expectedColor);
        Color actualColor = jdbcTemplate.queryForObject(QUERY_GET_COLOR_BY_ID, new Object[]{expectedColor.getId()},
                new BeanPropertyRowMapper<>(Color.class));
        assertEquals(expectedColor, actualColor);
    }
}
