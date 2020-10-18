package com.es.core.model.phone;

import org.simpleflatmapper.jdbc.spring.JdbcTemplateMapperFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JdbcPhoneDao implements PhoneDao{
    @Resource
    private JdbcTemplate jdbcTemplate;

    public Optional<Phone> get(final Long key) {
        ResultSetExtractor<List<Phone>> resultSetExtractor = JdbcTemplateMapperFactory
                .newInstance().addKeys("id")
                .newResultSetExtractor(Phone.class);
        String query =
                "SELECT phones.id AS id, brand, model, price, displaySizeInches, weightGr, lengthMm, widthMm, heightMm, " +
                        "announced, deviceType, os, displayResolution, pixelDensity, displayTechnology, " +
                        "backCameraMegapixels, frontCameraMegapixels, ramGb, internalStorageGb, batteryCapacityMah, " +
                        "talkTimeHours, standByTimeHours, bluetooth, positioning, imageUrl, description, " +
                        "colors.id AS colors_id, colors.code AS colors_code FROM phones " +
                            "LEFT JOIN phone2color ON phones.id = phone2color.phoneId " +
                                "LEFT JOIN colors ON colors.id = phone2color.colorId " +
                                    "WHERE phones.id = ?" ;

        try {
          return Optional.of(jdbcTemplate.query(query, new Object[]{key}, resultSetExtractor).get(0));
        } catch(DataAccessException e) {
            return Optional.empty();
        }
    }

    public void save(final Phone phone) {
        //should we do checking on existed entity with the same unique combination? (as we do with colors)
        Number newId = insertWithGeneratedKey("phones",
                new BeanPropertySqlParameterSource(phone), "id");
        phone.setId(newId.longValue());

        phone.getColors().forEach(color -> {
            Optional<Long> idColor = getIdOfExistedEntity("colors", "id",
                    Collections.singletonMap("code", color.getCode()));

            if (!idColor.isPresent()) {
                Number colorIdGenerated = insertWithGeneratedKey("colors",
                        new BeanPropertySqlParameterSource(color), "id");
                color.setId(colorIdGenerated.longValue());
            } else
                color.setId(idColor.get());

            insert("phone2color", new MapSqlParameterSource()
                    .addValue("phoneId", phone.getId())
                    .addValue("colorId", color.getId()));
        });
    }

    public List<Phone> findAll(int offset, int limit) {
        ResultSetExtractor<List<Phone>> resultSetExtractor = JdbcTemplateMapperFactory
                        .newInstance().addKeys("id")
                        .newResultSetExtractor(Phone.class);
        String query =
                "SELECT phonesWithColor.id AS id, brand, model, price, displaySizeInches, weightGr, lengthMm, widthMm, heightMm, " +
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

        return  jdbcTemplate.query(query, new Object[]{offset, limit}, resultSetExtractor);
    }

    private Number insertWithGeneratedKey(String tableName, SqlParameterSource parameters, String generatedColumnName){
        return new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(tableName)
                .usingGeneratedKeyColumns(generatedColumnName)
                .executeAndReturnKey(parameters);
    }

    private void insert(String tableName, SqlParameterSource parameters){
        new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(tableName)
                .execute(parameters);
    }

    private Optional<Long> getIdOfExistedEntity(String tableName, String idColumnName,
                                                Map<String,String> uniqueParametersCombination){
        String query = "SELECT " + idColumnName + " FROM " + tableName +"  WHERE";
        query += uniqueParametersCombination.entrySet().stream()
                .map(entry -> " " + entry.getKey() + " = '" + entry.getValue() + "'")
                .collect(Collectors.joining("AND"));
        return jdbcTemplate.query(query, (ResultSet rs) -> {
            if (rs.next())
                return Optional.of(rs.getLong(idColumnName));
            else return Optional.empty();
        });
    }
}
