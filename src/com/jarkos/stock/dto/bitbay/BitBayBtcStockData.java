package com.jarkos.stock.dto.bitbay;

import com.jarkos.stock.abstractional.api.BtcStockDataInterface;
import com.jarkos.stock.dto.bitbay.general.BitBayStockData;

import java.math.BigDecimal;

import static com.jarkos.config.StockConfig.BITBAY_BTC_WITHDRAW_PROV_AMOUNT;

public class BitBayBtcStockData extends BitBayStockData implements BtcStockDataInterface {

    public BitBayBtcStockData(BitBayStockData b) {
        super(b.getMax(), b.getMin(), b.getLast(), b.getBid(), b.getAsk(), b.getVwap(), b.getAverage(), b.getVolume(), b.getBids(), b.getAsks(), b.getTransactions());
    }

    @Override
    public BigDecimal getBtcAfterWithdrawalProv(BigDecimal btcToSubtractWithdrawProv) {
        return btcToSubtractWithdrawProv.subtract(BITBAY_BTC_WITHDRAW_PROV_AMOUNT);
    }

    @Override
    public Object getBtcEurStockData() {
        return this;
    }

}
