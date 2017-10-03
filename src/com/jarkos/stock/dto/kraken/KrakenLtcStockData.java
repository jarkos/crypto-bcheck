package com.jarkos.stock.dto.kraken;

import com.jarkos.stock.abstractional.api.LtcStockDataInterface;
import com.jarkos.stock.dto.kraken.general.KrakenStockData;

import java.math.BigDecimal;

/**
 * Created by jkostrzewa on 2017-09-10.
 */
public class KrakenLtcStockData extends KrakenStockData implements LtcStockDataInterface {

    public KrakenLtcStockData(KrakenStockData k) {
        super(k.getError(), k.getResult());
    }

    @Override
    public BigDecimal getLastLtcPrice() {
        return BigDecimal.valueOf(Float.valueOf(this.getResult().getXLTCZEUR().getLastTradePrice().get(0)));
    }

    @Override
    public BigDecimal getAskPrice() {
        return BigDecimal.valueOf(Float.valueOf(this.getResult().getXLTCZEUR().getAsk().get(0)));
    }

    @Override
    public Object getLtcEurStockData() {
        return this.getResult().getXLTCZEUR();
    }

}
