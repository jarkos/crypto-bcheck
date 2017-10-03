package com.jarkos.stock.abstractional.api;

import java.math.BigDecimal;

/**
 * Created by jkostrzewa on 2017-09-22.
 */
public interface EthStockDataInterface {

    BigDecimal getLastEthPrice();

    BigDecimal getAskEthPrice();

    Object getEthEurStockData();

}
