package com.jarkos.stock.dto.bitstamp;

import com.jarkos.stock.abstractional.api.BtcStockDataInterface;
import com.jarkos.stock.dto.bitstamp.general.BitstampStockData;

import java.math.BigDecimal;

import static com.jarkos.config.StockConfig.BITSTAMP_WITHDRAW_PROV;

public class BitstampBtcStockData extends BitstampStockData implements BtcStockDataInterface {

    public BitstampBtcStockData(BitstampStockData b) {
        super(b.getHigh(), b.getLast(), b.getTimestamp(), b.getBid(), b.getVwap(), b.getVolume(), b.getLow(), b.getAsk(), b.getOpen());
    }

    @Override
    public BigDecimal getBtcAfterWithdrawalProv(BigDecimal btcToSubtractWithdrawProv) {
        // NON FEE
        return btcToSubtractWithdrawProv.subtract(BITSTAMP_WITHDRAW_PROV);
    }

    @Override
    public BtcStockDataInterface getBtcEurStockData() {
        return this;
    }
}
