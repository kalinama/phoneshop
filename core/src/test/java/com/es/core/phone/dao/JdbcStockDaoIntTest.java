package com.es.core.phone.dao;

import com.es.core.phone.entity.Color;
import com.es.core.phone.entity.Stock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@ContextConfiguration("classpath:context/applicationContext-core-test.xml")
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
public class JdbcStockDaoIntTest {
    @Resource
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private StockDao stockDao;

    @Test
    public void get() {
       Stock stock = stockDao.get(1002L).get();
       String f = stock.toString();
    }

}
