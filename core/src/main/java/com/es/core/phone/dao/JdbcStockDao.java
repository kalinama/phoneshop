package com.es.core.phone.dao;

import com.es.core.phone.dao.exception.PrimaryKeyUniquenessException;
import com.es.core.phone.entity.Phone;
import com.es.core.phone.entity.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Component
public class JdbcStockDao implements StockDao{

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Resource
    private PhoneDao jdbcPhoneDao;

    private final static String QUERY_GET_STOCK = "SELECT * FROM stocks WHERE phoneId = ?";

    @Override
    public Optional<Stock> get(Long phoneId) {
       List<Stock> result = jdbcTemplate.query(QUERY_GET_STOCK, new Object[]{phoneId}, new BeanPropertyRowMapper<>(Stock.class));
       Optional<Phone> phone = jdbcPhoneDao.get(phoneId);

       if (result.size() > 1) {
           throw new PrimaryKeyUniquenessException();
       }

       if (result.isEmpty() || !phone.isPresent()) {
            return Optional.empty();
        }

       result.get(0).setPhone(phone.get());
       return Optional.of(result.get(0));
      // stock.setPhone(phoneDao.get(phoneId).get()); //custom exception
    }
}
