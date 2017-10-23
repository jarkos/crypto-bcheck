package com.jarkos.stock.dto.bitbay;

import com.jarkos.stock.abstractional.api.EthStockDataInterface;
import com.jarkos.stock.dto.bitbay.general.BitBayStockData;

import java.math.BigDecimal;

import static com.jarkos.config.StockConfig.BITBAY_ETH_WITHDRAW_PROV_AMOUNT;

public class BitBayEthStockData extends BitBayStockData implements EthStockDataInterface {

    public BitBayEthStockData(BitBayStockData b) {
        super(b.getMax(), b.getMin(), b.getLast(), b.getBid(), b.getAsk(), b.getVwap(), b.getAverage(), b.getVolume(), b.getBids(), b.getAsks(), b.getTransactions());
    }

    @Override
    public BigDecimal getEthAfterWithdrawalProv(BigDecimal numberOfEthBoughtAfterTradeProv) {
        return numberOfEthBoughtAfterTradeProv.subtract(BITBAY_ETH_WITHDRAW_PROV_AMOUNT);
    }

    @Override
    public Object getEthEurStockData() {
        return this;
    }
}
