package com.es.core.phone.service.impl;

import com.es.core.order.service.exception.OutOfStockException;
import com.es.core.phone.dao.StockDao;
import com.es.core.phone.entity.Stock;
import com.es.core.phone.service.StockService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class DefaultStockService implements StockService {

    @Resource
    private StockDao jdbcStockDao;

    @Override
    public void reservePhone(Long phoneId, Long quantity) throws OutOfStockException {
        Stock stock = jdbcStockDao.get(phoneId).orElseThrow(IllegalArgumentException::new);
        subtractStock(stock, quantity);
        stock.setReserved(stock.getReserved() + quantity);
        jdbcStockDao.save(stock);
    }

    @Override
    public void cancelPhoneReservation(Long phoneId, Long quantity) {
        Stock stock = jdbcStockDao.get(phoneId).orElseThrow(IllegalArgumentException::new);
        subtractReserved(stock, quantity);
        stock.setStock(stock.getStock() + quantity);
        jdbcStockDao.save(stock);
    }

    @Override
    public void deliverPhone(Long phoneId, Long quantity) {
        Stock stock = jdbcStockDao.get(phoneId).orElseThrow(IllegalArgumentException::new);
        subtractReserved(stock, quantity);
        jdbcStockDao.save(stock);
    }

    @Override
    public long getAvailableStock(Long phoneId) {
        Stock stock = jdbcStockDao.get(phoneId).orElseThrow(IllegalArgumentException::new);
        return stock.getStock();
    }

    private void subtractReserved(Stock stock, Long quantity) {
        long newReservedQuantity = stock.getReserved() - quantity;
        if (newReservedQuantity < 0) {
            throw new IllegalArgumentException();
        }
        stock.setReserved(newReservedQuantity);
    }

    private void subtractStock(Stock stock, Long quantity) throws OutOfStockException {
        long newStock = stock.getStock() - quantity;
        if (newStock < 0) {
            throw new OutOfStockException(stock.getStock());
        }
        stock.setStock(newStock);
    }


}
