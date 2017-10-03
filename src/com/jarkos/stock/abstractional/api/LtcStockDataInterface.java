package com.jarkos.stock.abstractional.api;

import java.math.BigDecimal;

/**
 * Created by jkostrzewa on 2017-09-10.
 */
public interface LtcStockDataInterface {

    BigDecimal getLastLtcPrice();

    BigDecimal getAskPrice();

    Object getLtcEurStockData();

}
