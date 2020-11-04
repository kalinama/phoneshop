package com.es.core.phone.dao;

import com.es.core.phone.entity.Stock;

import java.util.Optional;

public interface StockDao {
    Optional<Stock> get(Long phoneId);

}
