package com.jarkos.stock.dto.kraken;

import com.jarkos.stock.abstractional.api.DashStockDataInterface;
import com.jarkos.stock.dto.kraken.general.KrakenStockData;

import java.math.BigDecimal;

/**
 * Created by jkostrzewa on 2017-09-23.
 */
public class KrakenDashStockData extends KrakenStockData implements DashStockDataInterface {

    public KrakenDashStockData(KrakenStockData k) {
        super(k.getError(), k.getResult());
    }

    @Override
    public BigDecimal getLastDashPrice() {
        return BigDecimal.valueOf(Float.valueOf(this.getResult().getDASHEUR().getLastTradePrice().get(0)));
    }

    @Override
    public BigDecimal getAskPrice() {
        return BigDecimal.valueOf(Float.valueOf(this.getResult().getDASHEUR().getAsk().get(0)));
    }

    @Override
    public Object getDashEurStockData() {
        return getResult().getDASHEUR();
    }
}
