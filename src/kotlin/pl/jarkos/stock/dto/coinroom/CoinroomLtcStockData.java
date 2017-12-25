package pl.jarkos.stock.dto.coinroom;

import pl.jarkos.stock.abstractional.api.LtcStockDataInterface;

import java.math.BigDecimal;

import static pl.jarkos.config.StockConfig.COINROOM_LTC_WITHDRAW_PROV_AMOUNT;

public class CoinroomLtcStockData extends CoinroomStockData implements LtcStockDataInterface {

    public CoinroomLtcStockData(CoinroomStockData c) {
        super(c.getLow(), c.getHigh(), c.getVwap(), c.getVolume(), c.getLast(), c.getAsk(), c.getBid());
    }

    @Override
    public BigDecimal getLtcAfterWithdrawalProv(BigDecimal ltcToSubtractWithdrawProv) {
        return ltcToSubtractWithdrawProv.subtract(COINROOM_LTC_WITHDRAW_PROV_AMOUNT);
    }

    @Override
    public Object getLtcEurStockData() {
        return this;
    }
}
