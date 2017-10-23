package com.jarkos.stock.abstractional.api;

import java.math.BigDecimal;

public interface BtcStockDataInterface extends GeneralStockDataInterface {

    default String getType() {
        return "BTC";
    }

    BigDecimal getBtcAfterWithdrawalProv(BigDecimal btcToSubtractWithdrawProv);

    Object getBtcEurStockData();


}
