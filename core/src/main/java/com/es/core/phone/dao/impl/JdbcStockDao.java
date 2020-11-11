package com.es.core.phone.dao.impl;

import com.es.core.phone.dao.PhoneDao;
import com.es.core.phone.dao.StockDao;
import com.es.core.phone.dao.exception.PrimaryKeyUniquenessException;
import com.es.core.phone.dao.helper.JdbcHelper;
import com.es.core.phone.entity.Phone;
import com.es.core.phone.entity.Stock;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class JdbcStockDao implements StockDao {

    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    private NamedParameterJdbcTemplate parameterJdbcTemplate;
    @Resource
    private JdbcHelper defaultJdbcHelper;
    @Resource
    private PhoneDao jdbcPhoneDao;

    private final static String QUERY_GET_STOCK = "SELECT * FROM stocks WHERE phoneId = ?";
    private final static String QUERY_FOR_STOCKS_UPDATE = "UPDATE stocks SET stock = :stock, " +
            "reserved = :reserved WHERE phoneId = :phoneId";


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
    }

    @Override
    public void save(Stock stock) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("phoneId", stock.getPhone().getId())
                .addValue("stock", stock.getStock())
                .addValue("reserved", stock.getReserved());

        if (stock.getPhone().getId() == null) {
            throw new IllegalArgumentException();
        } else if (defaultJdbcHelper.isEntityWithParamsExists("stocks",
                Collections.singletonMap("phoneId", stock.getPhone().getId().toString()))) {
           parameterJdbcTemplate.update(QUERY_FOR_STOCKS_UPDATE, parameterSource);
        } else {
            defaultJdbcHelper.insert("stocks",parameterSource);
        }
    }

}
