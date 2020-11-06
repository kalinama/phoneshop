package com.es.core.phone.dao.impl;

import com.es.core.phone.dao.ColorDao;
import com.es.core.phone.dao.PhoneDao;
import com.es.core.phone.enums.SortOrder;
import com.es.core.phone.enums.SortParameter;
import com.es.core.phone.dao.exception.PrimaryKeyUniquenessException;
import com.es.core.phone.dao.helper.JdbcHelper;
import com.es.core.phone.entity.Phone;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class JdbcPhoneDao implements PhoneDao {
    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    private ColorDao jdbcColorDao;
    @Resource
    private  JdbcHelper defaultJdbcHelper;

    private final static String QUERY_FOR_PHONES_SELECT_GET_METHOD = "SELECT searchedPhone.id AS id, brand, model, price, " +
            "displaySizeInches, weightGr, lengthMm, widthMm, heightMm, announced, deviceType, " +
            "os, displayResolution, pixelDensity, displayTechnology,backCameraMegapixels, " +
            "frontCameraMegapixels, ramGb, internalStorageGb, batteryCapacityMah, " +
            "talkTimeHours, standByTimeHours, bluetooth, positioning, imageUrl, description, " +
            "colors.id AS colors_id, colors.code AS colors_code FROM " +
            "(SELECT * FROM phones WHERE phones.id = ?) AS searchedPhone " +
            "LEFT JOIN phone2color ON searchedPhone.id = phone2color.phoneId " +
            "LEFT JOIN colors ON colors.id = phone2color.colorId ";

    private final static String SUBQUERY_FOR_PHONES_NOT_DISPLAYED =
            " SELECT DISTINCT phones.id FROM phones " +
                    "LEFT JOIN phone2color ON phones.id = phone2color.phoneId " +
                    "LEFT JOIN stocks ON phones.id = stocks.phoneId " +
                    "WHERE phone2color.phoneId IS NULL " +
                    "OR stocks.phoneId IS NULL OR stocks.stock < 1 OR phones.price IS NULL";

    private final static String SUBQUERY_FOR_MATCHING_PHONES = " AND (lower(phones.model) LIKE '%%%s%%' OR lower(phones.brand) LIKE '%%%s%%')";

    private final static String QUERY_FOR_PHONES_SELECT_FIND_ALL_METHOD = "SELECT phonesWithColor.id AS id, brand, " +
            "model, price, displaySizeInches, weightGr, lengthMm, widthMm, heightMm, " +
            "announced, deviceType, os, displayResolution, pixelDensity, displayTechnology, " +
            "backCameraMegapixels, frontCameraMegapixels, ramGb, internalStorageGb, batteryCapacityMah, " +
            "talkTimeHours, standByTimeHours, bluetooth, positioning, imageUrl, description, " +
            "colors.id AS colors_id, colors.code AS colors_code FROM " +
                "(SELECT * FROM phones " +
                    "WHERE phones.id NOT IN (" + SUBQUERY_FOR_PHONES_NOT_DISPLAYED + ") " +
                    "%s ORDER BY %s %s OFFSET %d LIMIT %d) " +
            "AS phonesWithColor "  +
                "INNER JOIN phone2color ON phonesWithColor.id = phone2color.phoneId "+
                    "INNER JOIN colors ON colors.id = phone2color.colorId";

    private final static String QUERY_FOR_PHONES_UPDATE = "UPDATE phones SET brand=:brand, model=:model, price=:price, " +
            "displaySizeInches=:displaySizeInches, weightGr=:weightGr, lengthMm=:lengthMm, widthMm=:widthMm, " +
            "heightMm=:heightMm, announced=:announced, deviceType=:deviceType, os=:os, displayResolution=:displayResolution, " +
            "pixelDensity=:pixelDensity, displayTechnology=:displayTechnology, backCameraMegapixels=:backCameraMegapixels, " +
            "frontCameraMegapixels=:frontCameraMegapixels, ramGb=:ramGb, internalStorageGb=:internalStorageGb, " +
            "batteryCapacityMah=:batteryCapacityMah, talkTimeHours=:talkTimeHours, standByTimeHours=:standByTimeHours, " +
            "bluetooth=:bluetooth, positioning=:positioning, imageUrl=:imageUrl, description=:description WHERE id=:id";

    private final static String QUERY_FOR_PHONE2COLOR_DELETE = "DELETE FROM phone2color WHERE phoneId=:id";

    private final static String QUERY_FOR_PHONES_QUANTITY = "SELECT count(*) FROM phones " +
            "WHERE phones.id NOT IN (" + SUBQUERY_FOR_PHONES_NOT_DISPLAYED + ")";

    @Override
    public Optional<Phone> get(final Long key) {
        ResultSetExtractor<List<Phone>> resultSetExtractor = JdbcTemplateMapperFactory
                .newInstance().addKeys("id")
                .newResultSetExtractor(Phone.class);
        List<Phone> phones = jdbcTemplate.query(QUERY_FOR_PHONES_SELECT_GET_METHOD,
                new Object[]{key}, resultSetExtractor);

        if (phones.size() > 1) {
            throw new PrimaryKeyUniquenessException();
        }
        if (phones.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(phones.get(0));
    }

    @Override
    @Transactional(rollbackFor = DataAccessException.class)
    public void save(final Phone phone) {
        if (phone.getId() == null) {
            Number newId = defaultJdbcHelper.insertAndReturnGeneratedKey("phones",
                    new BeanPropertySqlParameterSource(phone), "id");
            phone.setId(newId.longValue());
        } else if (defaultJdbcHelper.isEntityWithParamsExists("phones",
                Collections.singletonMap("id", phone.getId().toString()))) {
            rewritePhoneAndRemoveBindToColors(phone);
        } else {
            defaultJdbcHelper.insert("phones", new BeanPropertySqlParameterSource(phone));
        }
        savePhoneColors(phone);
    }

    @Override
    public List<Phone> findAll(int offset, int limit, String query, SortParameter sortParameter, SortOrder sortOrder) {
        ResultSetExtractor<List<Phone>> resultSetExtractor = JdbcTemplateMapperFactory
                .newInstance().addKeys("id")
                .newResultSetExtractor(Phone.class);

        if (sortParameter == null || sortOrder == null) {
            throw new IllegalArgumentException();
        }
        String subQuery = (query == null) ? ""
                : String.format(SUBQUERY_FOR_MATCHING_PHONES, query.toLowerCase(), query.toLowerCase());
        String queryToDB = String.format(QUERY_FOR_PHONES_SELECT_FIND_ALL_METHOD, subQuery,
                "phones." + sortParameter.toString(), sortOrder.toString(), offset, limit);
        return jdbcTemplate.query(queryToDB, resultSetExtractor);
    }

    @Override
    public int getQuantity(String query) {
        String subQuery = (query == null) ? ""
                : String.format(SUBQUERY_FOR_MATCHING_PHONES, query.toLowerCase(), query.toLowerCase());
        return jdbcTemplate.queryForObject(QUERY_FOR_PHONES_QUANTITY + subQuery, Integer.class);
    }

    private void savePhoneColors(Phone phone) {
        phone.getColors().forEach(color -> {
            jdbcColorDao.save(color);
            defaultJdbcHelper.insert("phone2color", new MapSqlParameterSource()
                    .addValue("phoneId", phone.getId())
                    .addValue("colorId", color.getId()));
        });
    }

    private void rewritePhoneAndRemoveBindToColors(Phone phone) {
        NamedParameterJdbcTemplate parameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        parameterJdbcTemplate.update(QUERY_FOR_PHONES_UPDATE,
                new BeanPropertySqlParameterSource(phone));
        parameterJdbcTemplate.update(QUERY_FOR_PHONE2COLOR_DELETE,
                new MapSqlParameterSource("id", phone.getId()));
    }
}
