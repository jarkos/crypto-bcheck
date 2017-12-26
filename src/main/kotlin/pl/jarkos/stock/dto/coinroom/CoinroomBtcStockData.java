package pl.jarkos.stock.dto.coinroom;

import pl.jarkos.stock.abstractional.api.BtcStockDataInterface;

import java.math.BigDecimal;

import static pl.jarkos.config.StockConfig.COINROOM_BTC_WITHDRAW_PROV_AMOUNT;

public class CoinroomBtcStockData extends CoinroomStockData implements BtcStockDataInterface {

    public CoinroomBtcStockData(CoinroomStockData c) {
        super(c.getLow(), c.getHigh(), c.getVwap(), c.getVolume(), c.getLast(), c.getAsk(), c.getBid());
    }

    @Override
    public BigDecimal getBtcAfterWithdrawalProv(BigDecimal btcToSubtractWithdrawProv) {
        return btcToSubtractWithdrawProv.subtract(COINROOM_BTC_WITHDRAW_PROV_AMOUNT);
    }

    @Override
    public BtcStockDataInterface getBtcEurStockData() {
        return this;
    }
}
