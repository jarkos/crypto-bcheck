package pl.jarkos.backend.stock.dto.coinroom;

import pl.jarkos.backend.stock.abstractional.api.EthStockDataInterface;

import java.math.BigDecimal;

import static pl.jarkos.backend.config.StockConfig.COINROOM_ETH_WITHDRAW_PROV_AMOUNT;

public class CoinroomEthStockData extends CoinroomStockData implements EthStockDataInterface {

    public CoinroomEthStockData(CoinroomStockData c) {
        super(c.getLow(), c.getHigh(), c.getVwap(), c.getVolume(), c.getLast(), c.getAsk(), c.getBid());
    }

    @Override
    public BigDecimal getEthAfterWithdrawalProv(BigDecimal numberOfEthBoughtAfterTradeProv) {
        return numberOfEthBoughtAfterTradeProv.subtract(COINROOM_ETH_WITHDRAW_PROV_AMOUNT);
    }

    @Override
    public EthStockDataInterface getEthEurStockData() {
        return this;
    }
}
