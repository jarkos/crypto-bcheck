package pl.jarkos.backend.stock.abstractional.api;

import java.math.BigDecimal;

public interface EthStockDataInterface extends GeneralStockDataInterface {

    default String getType() {
        return "ETH";
    }

    BigDecimal getEthAfterWithdrawalProv(BigDecimal numberOfEthBoughtAfterTradeProv);

    Object getEthEurStockData();

}
