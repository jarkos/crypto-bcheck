package com.jarkos.stock.dto.kraken;

import com.jarkos.stock.abstractional.api.EthStockDataInterface;
import com.jarkos.stock.dto.kraken.general.KrakenStockData;

import java.math.BigDecimal;

import static com.jarkos.config.StockConfig.KRAKEN_ETH_WITHDRAW_PROV_AMOUNT;

public class KrakenEthStockData extends KrakenStockData implements EthStockDataInterface {

    public KrakenEthStockData(KrakenStockData k) {
        super(k.getError(), k.getResult());
    }

    @Override
    public BigDecimal getLastPrice() {
        return BigDecimal.valueOf(Float.valueOf(this.getResult().getXETHZEUR().getLastTradePrice().get(0)));
    }

    @Override
    public BigDecimal getAskPrice() {
        return BigDecimal.valueOf(Float.valueOf(this.getResult().getXETHZEUR().getAsk().get(0)));
    }

    @Override
    public BigDecimal getBidPrice() {
        return BigDecimal.valueOf(Float.valueOf(this.getResult().getXETHZEUR().getBid().get(0)));
    }

    @Override
    public Object getEthEurStockData() {
        return this.getResult().getXETHZEUR();
    }

    @Override
    public BigDecimal getEthAfterWithdrawalProv(BigDecimal numberOfEthBoughtAfterTradeProv) {
        return numberOfEthBoughtAfterTradeProv.subtract(KRAKEN_ETH_WITHDRAW_PROV_AMOUNT);
    }

}
