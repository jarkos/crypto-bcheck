package com.jarkos.stock.dto.bitbay;

import com.jarkos.stock.abstractional.api.BccStockDataInterface;
import com.jarkos.stock.dto.bitbay.general.BitBayStockData;

import java.math.BigDecimal;

import static com.jarkos.config.StockConfig.BITBAY_BCC_WITHDRAW_PROV_AMOUNT;

public class BitBayBccStockData extends BitBayStockData implements BccStockDataInterface {

    public BitBayBccStockData(BitBayStockData b) {
        super(b.getMax(), b.getMin(), b.getLast(), b.getBid(), b.getAsk(), b.getVwap(), b.getAverage(), b.getVolume(), b.getBids(), b.getAsks(), b.getTransactions());
    }

    @Override
    public BigDecimal getBccAfterWithdrawalProv(BigDecimal bccToSubtractWithdrawProv) {
        return bccToSubtractWithdrawProv.subtract(BITBAY_BCC_WITHDRAW_PROV_AMOUNT);
    }

    @Override
    public Object getBccStockData() {
        return this;
    }
}
