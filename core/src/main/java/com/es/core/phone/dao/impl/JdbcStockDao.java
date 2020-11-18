package com.es.core.phone.dao.impl;

import com.es.core.phone.dao.PhoneDao;
import com.es.core.phone.dao.StockDao;
import com.es.core.phone.dao.exception.PrimaryKeyUniquenessException;
import com.es.core.phone.dao.helper.JdbcHelper;
import com.es.core.phone.entity.Phone;
import com.es.core.phone.entity.Stock;
import org.simpleflatmapper.jdbc.spring.JdbcTemplateMapperFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
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

    private final static String QUERY_FOR_STOCKS_UPDATE = "UPDATE stocks SET stock = :stock, " +
            "reserved = :reserved WHERE phoneId = :phoneId";

    private final static String QUERY_GET_STOCK_BY_ID = "SELECT stock, reserved, phones.id AS phone_id, phones.brand AS " +
            "phone_brand, phones.model AS phone_model, phones.price AS phone_price, phones.displaySizeInches AS phone_displaySizeInches, " +
            "phones.weightGr AS phone_weightGr, phones.lengthMm AS phone_lengthMm, phones.widthMm AS phone_widthMm, " +
            "phones.heightMm AS phone_heightMm, phones.announced AS phone_announced, phones.deviceType AS phone_deviceType, " +
            "phones.os AS phone_os, phones.displayResolution AS phone_displayResolution, phones.pixelDensity AS phone_pixelDensity, " +
            "phones.displayTechnology AS phone_displayTechnology, phones.backCameraMegapixels AS phone_backCameraMegapixels, " +
            "phones.frontCameraMegapixels AS phone_frontCameraMegapixels, phones.ramGb AS phone_ramGb, phones.internalStorageGb AS " +
            "phone_internalStorageGb, phones.batteryCapacityMah AS phone_batteryCapacityMah, phones.talkTimeHours AS phone_talkTimeHours, " +
            "phones.standByTimeHours AS phone_standByTimeHours, phones.bluetooth AS phone_bluetooth, phones.positioning AS " +
            "phone_positioning, phones.imageUrl AS phone_imageUrl, phones.description AS phone_description, colors.id AS " +
            "phone_colors_id, colors.code AS phone_colors_code FROM (SELECT * FROM stocks WHERE stocks.phoneId = ?) AS searchedStock " +
                "INNER JOIN phones ON searchedStock.phoneId = phones.id " +
                    "INNER JOIN phone2color ON phones.id = phone2color.phoneId " +
                        "INNER JOIN colors ON colors.id = phone2color.colorId";


    @Override
    public Optional<Stock> get(Long phoneId) {
        ResultSetExtractor<List<Stock>> resultSetExtractor = JdbcTemplateMapperFactory
                .newInstance().addKeys("phone_id")
                .newResultSetExtractor(Stock.class);
        List<Stock> result = jdbcTemplate.query(QUERY_GET_STOCK_BY_ID, new Object[]{phoneId}, resultSetExtractor);

       if (result.size() > 1) {
           throw new PrimaryKeyUniquenessException();
       }

       if (result.isEmpty()) {
            return Optional.empty();
        }

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
