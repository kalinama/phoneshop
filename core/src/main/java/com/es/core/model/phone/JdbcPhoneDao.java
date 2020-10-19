package com.es.core.model.phone;

import org.simpleflatmapper.jdbc.spring.JdbcTemplateMapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class JdbcPhoneDao implements PhoneDao{
    @Resource
    private JdbcTemplate jdbcTemplate;
    private JdbcColorDao jdbcColorDao;
    private JdbcHelper jdbcHelper;

    private final static String QUERY_FOR_PHONES_SELECT_GET_METHOD =  "SELECT searchedPhone.id AS id, brand, model, price, " +
            "displaySizeInches, weightGr, lengthMm, widthMm, heightMm, announced, deviceType, " +
            "os, displayResolution, pixelDensity, displayTechnology,backCameraMegapixels, " +
            "frontCameraMegapixels, ramGb, internalStorageGb, batteryCapacityMah, " +
            "talkTimeHours, standByTimeHours, bluetooth, positioning, imageUrl, description, " +
            "colors.id AS colors_id, colors.code AS colors_code FROM " +
                "(SELECT * FROM phones WHERE phones.id = ?) AS searchedPhone " +
                "LEFT JOIN phone2color ON searchedPhone.id = phone2color.phoneId " +
                    "LEFT JOIN colors ON colors.id = phone2color.colorId ";

    private final static String QUERY_FOR_PHONES_SELECT_FIND_ALL_METHOD =  "SELECT phonesWithColor.id AS id, brand, " +
            "model, price, displaySizeInches, weightGr, lengthMm, widthMm, heightMm, " +
            "announced, deviceType, os, displayResolution, pixelDensity, displayTechnology, " +
            "backCameraMegapixels, frontCameraMegapixels, ramGb, internalStorageGb, batteryCapacityMah, " +
            "talkTimeHours, standByTimeHours, bluetooth, positioning, imageUrl, description, " +
            "colors.id AS colors_id, colors.code AS colors_code FROM " +
                "(SELECT * FROM phones " +
                    "WHERE phones.id NOT IN " +
                        "(SELECT phones.id FROM phones " +
                            "LEFT JOIN phone2color ON phones.id = phone2color.phoneId " +
                                "WHERE phone2color.phoneId IS NULL) " +
                    "OFFSET ? LIMIT ?) " +
                "AS phonesWithColor "  +
                "INNER JOIN phone2color ON phonesWithColor.id = phone2color.phoneId "+
                    "INNER JOIN colors ON colors.id = phone2color.colorId " +
                        "ORDER BY phonesWithColor.id ";

    private final static String QUERY_FOR_PHONES_UPDATE = "UPDATE phones SET brand=:brand, model=:model, price=:price, " +
            "displaySizeInches=:displaySizeInches, weightGr=:weightGr, lengthMm=:lengthMm, widthMm=:widthMm, " +
            "heightMm=:heightMm, announced=:announced, deviceType=:deviceType, os=:os, displayResolution=:displayResolution, " +
            "pixelDensity=:pixelDensity, displayTechnology=:displayTechnology, backCameraMegapixels=:backCameraMegapixels, " +
            "frontCameraMegapixels=:frontCameraMegapixels, ramGb=:ramGb, internalStorageGb=:internalStorageGb, " +
            "batteryCapacityMah=:batteryCapacityMah, talkTimeHours=:talkTimeHours, standByTimeHours=:standByTimeHours, " +
            "bluetooth=:bluetooth, positioning=:positioning, imageUrl=:imageUrl, description=:description WHERE id=:id";

    private final static String QUERY_FOR_PHONE2COLOR_DELETE = "DELETE FROM phone2color WHERE phoneId=:id";

    @Autowired
    public JdbcPhoneDao(JdbcColorDao jdbcColorDao, JdbcHelper jdbcHelper) {
        this.jdbcColorDao = jdbcColorDao;
        this.jdbcHelper = jdbcHelper;
    }

    public Optional<Phone> get(final Long key) {
        ResultSetExtractor<List<Phone>> resultSetExtractor = JdbcTemplateMapperFactory
                .newInstance().addKeys("id")
                .newResultSetExtractor(Phone.class);
        try {
          return Optional.of(jdbcTemplate.query(QUERY_FOR_PHONES_SELECT_GET_METHOD,
                  new Object[]{key}, resultSetExtractor).get(0));
        } catch(DataAccessException e) {
            return Optional.empty();
        }
    }

    public void save(final Phone phone) {
        Map<String, String> uniqueParametersConstraint = new HashMap<>();
        uniqueParametersConstraint.put("brand", phone.getBrand());
        uniqueParametersConstraint.put("model", phone.getModel());

        Optional<Long> idOfPhoneWithSuchUniqueParams = jdbcHelper
                .getIdOfExistedEntityByUniqueParams("phones", "id", uniqueParametersConstraint);
        if(idOfPhoneWithSuchUniqueParams.isPresent()
                && !idOfPhoneWithSuchUniqueParams.get().equals(phone.getId())) {
            throw new PhoneUniqueConstraintException();
        }

        if (phone.getId()==null) {
            Number newId = jdbcHelper.insertAndReturnGeneratedKey("phones",
                    new BeanPropertySqlParameterSource(phone), "id");
            phone.setId(newId.longValue());
        } else if (jdbcHelper.isEntityWithParamsExists("phones",
                Collections.singletonMap("id", phone.getId().toString()))) {
           rewritePhoneAndRemoveBindToColors(phone);
        } else {
                jdbcHelper.insert("phones", new BeanPropertySqlParameterSource(phone));
        }
        savePhoneColors(phone);
    }

    public List<Phone> findAll(int offset, int limit) {
        ResultSetExtractor<List<Phone>> resultSetExtractor = JdbcTemplateMapperFactory
                        .newInstance().addKeys("id")
                        .newResultSetExtractor(Phone.class);
        return  jdbcTemplate.query(QUERY_FOR_PHONES_SELECT_FIND_ALL_METHOD,
                new Object[]{offset, limit}, resultSetExtractor);
    }

    private void savePhoneColors(Phone phone){
        phone.getColors().forEach(color -> {
            jdbcColorDao.save(color);
            jdbcHelper.insert("phone2color", new MapSqlParameterSource()
                    .addValue("phoneId", phone.getId())
                    .addValue("colorId", color.getId()));
        });
    }
    private void rewritePhoneAndRemoveBindToColors(Phone phone){
        NamedParameterJdbcTemplate parameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        parameterJdbcTemplate.update(QUERY_FOR_PHONES_UPDATE,
                new BeanPropertySqlParameterSource(phone));
        parameterJdbcTemplate.update(QUERY_FOR_PHONE2COLOR_DELETE,
                new MapSqlParameterSource("id",phone.getId()));
    }

}
