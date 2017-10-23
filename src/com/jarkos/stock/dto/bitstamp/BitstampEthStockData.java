package com.jarkos.stock.dto.bitstamp;

import com.jarkos.stock.abstractional.api.EthStockDataInterface;
import com.jarkos.stock.dto.bitstamp.general.BitstampStockData;

import java.math.BigDecimal;

import static com.jarkos.config.StockConfig.BITSTAMP_WITHDRAW_PROV;

public class BitstampEthStockData extends BitstampStockData implements EthStockDataInterface {

    public BitstampEthStockData(BitstampStockData b) {
        super(b.getHigh(), b.getLast(), b.getTimestamp(), b.getBid(), b.getVwap(), b.getVolume(), b.getLow(), b.getAsk(), b.getOpen());
    }

    @Override
    public BigDecimal getEthAfterWithdrawalProv(BigDecimal numberOfEthBoughtAfterTradeProv) {
        return numberOfEthBoughtAfterTradeProv.subtract(BITSTAMP_WITHDRAW_PROV);
    }


    @Override
    public Object getEthEurStockData() {
        return this;
    }
}
