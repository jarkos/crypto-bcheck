package com.jarkos.stock.dto.bitstamp;

import com.jarkos.stock.abstractional.api.DashStockDataInterface;
import com.jarkos.stock.dto.bitstamp.general.BitstampStockData;

import java.math.BigDecimal;

import static com.jarkos.config.StockConfig.BITSTAMP_WITHDRAW_PROV;

public class BitstampDashStockData extends BitstampStockData implements DashStockDataInterface {

    @Override
    public BigDecimal getDashAfterWithdrawalProv(BigDecimal numberOfDashBoughtAfterTradeProv) {
        return numberOfDashBoughtAfterTradeProv.subtract(BITSTAMP_WITHDRAW_PROV);
    }

    @Override
    public Object getDashEurStockData() {
        return this;
    }
}
