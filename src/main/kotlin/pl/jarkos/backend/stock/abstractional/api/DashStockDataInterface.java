package pl.jarkos.backend.stock.abstractional.api;

import java.math.BigDecimal;

public interface DashStockDataInterface extends GeneralStockDataInterface {

    default String getType() {
        return "DASH";
    }

    BigDecimal getDashAfterWithdrawalProv(BigDecimal numberOfDashBoughtAfterTradeProv);

    Object getDashEurStockData();

}
