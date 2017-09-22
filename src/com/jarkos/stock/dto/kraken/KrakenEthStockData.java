package com.jarkos.stock.dto.kraken;

import com.jarkos.stock.abstractional.api.EthStockDataInterface;
import com.jarkos.stock.dto.kraken.general.KrakenStockData;

import java.math.BigDecimal;

/**
 * Created by jkostrzewa on 2017-09-22.
 */
public class KrakenEthStockData extends KrakenStockData implements EthStockDataInterface {

    public KrakenEthStockData(KrakenStockData k) {
        super(k.getError(), k.getResult());
    }

    @Override
    public BigDecimal getLastEthPrice() {
        return BigDecimal.valueOf(Float.valueOf(this.getResult().getXETHZEUR().getLastTradePrice().get(0)));
    }

    @Override
    public Object getEthEurStockData() {
        return this.getResult().getXETHZEUR();
    }
}
