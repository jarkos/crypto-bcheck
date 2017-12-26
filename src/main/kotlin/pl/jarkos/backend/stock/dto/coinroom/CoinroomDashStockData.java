package pl.jarkos.backend.stock.dto.coinroom;

import pl.jarkos.backend.stock.abstractional.api.DashStockDataInterface;

import java.math.BigDecimal;

import static pl.jarkos.backend.config.StockConfig.COINROOM_DASH_WITHDRAW_PROV_AMOUNT;

public class CoinroomDashStockData extends CoinroomStockData implements DashStockDataInterface {

    public CoinroomDashStockData(CoinroomStockData c) {
        super(c.getLow(), c.getHigh(), c.getVwap(), c.getVolume(), c.getLast(), c.getAsk(), c.getBid());
    }

    @Override
    public BigDecimal getDashAfterWithdrawalProv(BigDecimal numberOfDashBoughtAfterTradeProv) {
        return numberOfDashBoughtAfterTradeProv.subtract(COINROOM_DASH_WITHDRAW_PROV_AMOUNT);
    }

    @Override
    public Object getDashEurStockData() {
        return this;
    }

}
