package com.es.core.phone.service.impl;

import com.es.core.phone.dao.PhoneDao;
import com.es.core.phone.entity.Phone;
import com.es.core.phone.enums.SortOrder;
import com.es.core.phone.enums.SortParameter;
import com.es.core.phone.service.PhoneService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Service
public class DefaultPhoneService implements PhoneService {
    @Resource
    private PhoneDao jdbcPhoneDao;

    @Override
    public Optional<Phone> get(Long key) {
        return jdbcPhoneDao.get(key);
    }

    @Override
    public Optional<Phone> getByModel(String model) {
        return jdbcPhoneDao.getByModel(model);
    }

    @Override
    public List<Phone> findAll(int offset, int limit, String query, SortParameter sortParameter, SortOrder sortOrder) {
        return jdbcPhoneDao.findAll(offset, limit, query, sortParameter, sortOrder);
    }

    @Override
    public int getQuantity(String query) {
        return jdbcPhoneDao.getQuantity(query);
    }
}
