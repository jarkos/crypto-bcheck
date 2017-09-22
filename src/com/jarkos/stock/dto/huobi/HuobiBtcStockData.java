package com.jarkos.stock.dto.huobi;

import com.jarkos.stock.abstractional.api.BtcStockDataInterface;
import com.jarkos.stock.dto.huobi.general.HuobiStockData;

import java.math.BigDecimal;

/**
 * Created by jkostrzewa on 2017-09-10.
 */

public class HuobiBtcStockData extends HuobiStockData implements BtcStockDataInterface {

    public HuobiBtcStockData(HuobiStockData h) {
        super(h.getAmount(), h.getPLast(), h.getPLow(), h.getSymbol(), h.getTopBuy(), h.getSells(), h.getAmp(), h.getPOpen(), h.getLevel(), h.getTotal(), h.getBuys(),
              h.getTopSell(), h.getPHigh(), h.getPNew(), h.getTrades());
    }

    @Override
    public BigDecimal getLastBtcPrice() {
        return BigDecimal.valueOf(getPLast());
    }

    @Override
    public Object getBtcEurStockData() {
        return this;
    }

}
