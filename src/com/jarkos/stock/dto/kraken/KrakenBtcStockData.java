package com.jarkos.stock.dto.kraken;

import com.jarkos.stock.abstractional.api.BtcStockDataInterface;
import com.jarkos.stock.dto.kraken.general.KrakenStockData;

import java.math.BigDecimal;

/**
 * Created by jkostrzewa on 2017-09-10.
 */
public class KrakenBtcStockData extends KrakenStockData implements BtcStockDataInterface {

    public KrakenBtcStockData(KrakenStockData k) {
        super(k.getError(), k.getResult());
    }

    @Override
    public BigDecimal getLastBtcPrice() {
        return BigDecimal.valueOf(Float.valueOf(this.getResult().getXXBTZEUR().getLastTradePrice().get(0)));
    }

    @Override
    public Object getBtcEurStockData() {
        return this.getResult().getXXBTZEUR();
    }

}
