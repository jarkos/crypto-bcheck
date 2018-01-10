package pl.jarkos.backend.stock.abstractional.api;

import java.math.BigDecimal;

public interface BccStockDataInterface extends GeneralStockDataInterface {

    default String getType() {
        return "BCC";
    }

    BigDecimal getBccAfterWithdrawalProv(BigDecimal bccToSubtractWithdrawProv);

    Object getBccStockData();
}
