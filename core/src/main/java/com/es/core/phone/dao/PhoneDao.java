package com.es.core.phone.dao;

import com.es.core.phone.enums.SortOrder;
import com.es.core.phone.enums.SortParameter;
import com.es.core.phone.entity.Phone;

import java.util.List;
import java.util.Optional;

public interface PhoneDao {
    Optional<Phone> get(Long key);
    void save(Phone phone);
    List<Phone> findAll(int offset, int limit, String query, SortParameter sortParameter, SortOrder sortOrder);
    int getQuantity(String query);
}
