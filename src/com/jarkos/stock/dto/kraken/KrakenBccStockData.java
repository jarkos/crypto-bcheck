package com.jarkos.stock.dto.kraken;

import com.jarkos.stock.abstractional.api.BccStockDataInterface;
import com.jarkos.stock.dto.kraken.general.KrakenStockData;

import java.math.BigDecimal;

/**
 * Created by jkostrzewa on 2017-09-11.
 */
public class KrakenBccStockData extends KrakenStockData implements BccStockDataInterface {
    public KrakenBccStockData(KrakenStockData k) {
        super(k.getError(), k.getResult());
    }

    @Override
    public BigDecimal getLastBccPrice() {
        return BigDecimal.valueOf(Float.valueOf(this.getResult().getXBCHEUR().getLastTradePrice().get(0)));
    }

    @Override
    public BigDecimal getAskBccPrice() {
        return BigDecimal.valueOf(Float.valueOf(this.getResult().getXBCHEUR().getAsk().get(0)));
    }
}
