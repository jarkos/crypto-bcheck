package com.jarkos.stock.abstractional.api;

import java.math.BigDecimal;

public interface GeneralStockDataInterface {

    BigDecimal getLastPrice();

    BigDecimal getAskPrice();

    BigDecimal getBidPrice();

    BigDecimal getMakerTradeProvision();

    BigDecimal getTakerTradeProvision();

}
