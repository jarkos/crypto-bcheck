package com.jarkos.stock.abstractional.api;

import java.math.BigDecimal;

/**
 * Created by jkostrzewa on 2017-09-23.
 */
public interface DashStockDataInterface {

    BigDecimal getLastDashPrice();

    BigDecimal getAskPrice();

    Object getDashEurStockData();

}
