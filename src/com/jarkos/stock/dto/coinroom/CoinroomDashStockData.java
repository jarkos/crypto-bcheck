package com.jarkos.stock.dto.coinroom;

import com.jarkos.stock.abstractional.api.DashStockDataInterface;

import java.math.BigDecimal;

import static com.jarkos.config.StockConfig.COINROOM_DASH_WITHDRAW_PROV_AMOUNT;

public class CoinroomDashStockData extends CoinroomStockData implements DashStockDataInterface {

    public CoinroomDashStockData(CoinroomStockData c) {
        super(c.getLow(), c.getHigh(), c.getVwap(), c.getVolume(), c.getLast(), c.getAsk(), c.getBid());
    }

    @Override
    public BigDecimal getDashAfterWithdrawalProv(BigDecimal numberOfDashBoughtAfterTradeProv) {
        return numberOfDashBoughtAfterTradeProv.subtract(COINROOM_DASH_WITHDRAW_PROV_AMOUNT);
    }

    @Override
    public Object getDashEurStockData() {
        return this;
    }

}
