package com.es.core.order;

import java.util.function.BiConsumer;

public interface StockOperation extends BiConsumer<Long, Long> {
    @Override
    void accept(Long phoneId, Long quantity);
}
