package com.jarkos.stock.dto.kraken;

import com.jarkos.stock.abstractional.api.DashStockDataInterface;
import com.jarkos.stock.dto.kraken.general.KrakenStockData;

import java.math.BigDecimal;

import static com.jarkos.config.StockConfig.KRAKEN_DASH_WITHDRAW_PROV;

public class KrakenDashStockData extends KrakenStockData implements DashStockDataInterface {

    public KrakenDashStockData(KrakenStockData k) {
        super(k.getError(), k.getResult());
    }

    @Override
    public BigDecimal getLastPrice() {
        return BigDecimal.valueOf(Float.valueOf(this.getResult().getDASHEUR().getLastTradePrice().get(0)));
    }

    @Override
    public BigDecimal getAskPrice() {
        return BigDecimal.valueOf(Float.valueOf(this.getResult().getDASHEUR().getAsk().get(0)));
    }

    @Override
    public BigDecimal getBidPrice() {
        return BigDecimal.valueOf(Float.valueOf(this.getResult().getDASHEUR().getBid().get(0)));
    }


    @Override
    public BigDecimal getDashAfterWithdrawalProv(BigDecimal numberOfDashBoughtAfterTradeProv) {
        return numberOfDashBoughtAfterTradeProv.subtract(KRAKEN_DASH_WITHDRAW_PROV);
    }

    @Override
    public Object getDashEurStockData() {
        return getResult().getDASHEUR();
    }
}
