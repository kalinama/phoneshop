package com.es.core.phone.service;

import com.es.core.phone.entity.Phone;
import com.es.core.phone.enums.SortOrder;
import com.es.core.phone.enums.SortParameter;

import java.util.List;
import java.util.Optional;

public interface PhoneService {
    Optional<Phone> get(Long key);
    Optional<Phone> getByModel(String model);
    List<Phone> findAll(int offset, int limit, String query, SortParameter sortParameter, SortOrder sortOrder);
    int getQuantity(String query);
}
