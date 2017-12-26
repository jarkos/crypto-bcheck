package pl.jarkos.backend.stock.abstractional.api;

import java.math.BigDecimal;

public interface GeneralStockDataInterface {

    BigDecimal getLastPrice();

    BigDecimal getAskPrice();

    BigDecimal getBidPrice();

    BigDecimal getMakerProvision();

    BigDecimal getTakerProvision();

    String getStockName();

}
