package pl.jarkos.stock.abstractional.api;

import java.math.BigDecimal;

public interface LtcStockDataInterface extends GeneralStockDataInterface {

    default String getType() {
        return "LTC";
    }

    BigDecimal getLtcAfterWithdrawalProv(BigDecimal ltcToSubtractWithdrawProv);

    Object getLtcEurStockData();

}
