package com.jarkos.stock.dto.bitstamp;

import com.jarkos.stock.abstractional.api.LtcStockDataInterface;
import com.jarkos.stock.dto.bitstamp.general.BitstampStockData;

import java.math.BigDecimal;

import static com.jarkos.config.StockConfig.BITSTAMP_EUR_WITHDRAW_PROV_AMOUNT;

public class BitstampLtcStockData extends BitstampStockData implements LtcStockDataInterface {

    public BitstampLtcStockData(BitstampStockData b) {
        super(b.getHigh(), b.getLast(), b.getTimestamp(), b.getBid(), b.getVwap(), b.getVolume(), b.getLow(), b.getAsk(), b.getOpen());
    }

    @Override
    public BigDecimal getLtcAfterWithdrawalProv(BigDecimal ltcToSubtractWithdrawProv) {
        return ltcToSubtractWithdrawProv.subtract(BITSTAMP_EUR_WITHDRAW_PROV_AMOUNT);
    }

    @Override
    public Object getLtcEurStockData() {
        return this;
    }

}
