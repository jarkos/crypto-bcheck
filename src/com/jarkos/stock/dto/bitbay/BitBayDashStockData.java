package com.jarkos.stock.dto.bitbay;

import com.jarkos.stock.abstractional.api.DashStockDataInterface;
import com.jarkos.stock.dto.bitbay.general.BitBayStockData;

import java.math.BigDecimal;

import static com.jarkos.config.StockConfig.BITBAY_DASH_WITHDRAW_PROV_AMOUNT;

public class BitBayDashStockData extends BitBayStockData implements DashStockDataInterface {

    public BitBayDashStockData(BitBayStockData b) {
        super(b.getMax(), b.getMin(), b.getLast(), b.getBid(), b.getAsk(), b.getVwap(), b.getAverage(), b.getVolume(), b.getBids(), b.getAsks(), b.getTransactions());
    }

    @Override
    public BigDecimal getDashAfterWithdrawalProv(BigDecimal numberOfDashBoughtAfterTradeProv) {
        return numberOfDashBoughtAfterTradeProv.subtract(BITBAY_DASH_WITHDRAW_PROV_AMOUNT);
    }

    @Override
    public Object getDashEurStockData() {
        return this;
    }
}
