package pl.jarkos.backend.stock.dto.kraken;

import pl.jarkos.backend.stock.abstractional.api.BtcStockDataInterface;
import pl.jarkos.backend.stock.dto.kraken.general.KrakenStockData;

import java.math.BigDecimal;

import static pl.jarkos.backend.config.StockConfig.KRAKEN_BTC_WITHDRAW_PROV;

public class KrakenBtcStockData extends KrakenStockData implements BtcStockDataInterface {

    public KrakenBtcStockData(KrakenStockData k) {
        super(k.getError(), k.getResult());
    }

    @Override
    public BigDecimal getLastPrice() {
        return BigDecimal.valueOf(Float.valueOf(this.getResult().getXXBTZEUR().getLastTradePrice().get(0)));
    }

    @Override
    public BigDecimal getAskPrice() {
        return BigDecimal.valueOf(Float.valueOf(this.getResult().getXXBTZEUR().getAsk().get(0)));
    }

    @Override
    public BigDecimal getBidPrice() {
        return BigDecimal.valueOf(Float.valueOf(this.getResult().getXXBTZEUR().getBid().get(0)));
    }

    @Override
    public BigDecimal getBtcAfterWithdrawalProv(BigDecimal btcToSubtractWithdrawProv) {
        return btcToSubtractWithdrawProv.subtract(KRAKEN_BTC_WITHDRAW_PROV);
    }

    @Override
    public Object getBtcEurStockData() {
        return this.getResult().getXXBTZEUR();
    }

}
