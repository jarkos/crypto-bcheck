package pl.jarkos.stock.dto.kraken;

import pl.jarkos.stock.abstractional.api.LtcStockDataInterface;
import pl.jarkos.stock.dto.kraken.general.KrakenStockData;

import java.math.BigDecimal;

import static pl.jarkos.config.StockConfig.KRAKEN_LTC_WITHDRAW_PROV;

public class KrakenLtcStockData extends KrakenStockData implements LtcStockDataInterface {

    public KrakenLtcStockData(KrakenStockData k) {
        super(k.getError(), k.getResult());
    }

    @Override
    public BigDecimal getLastPrice() {
        return BigDecimal.valueOf(Float.valueOf(this.getResult().getXLTCZEUR().getLastTradePrice().get(0)));
    }

    @Override
    public BigDecimal getAskPrice() {
        return BigDecimal.valueOf(Float.valueOf(this.getResult().getXLTCZEUR().getAsk().get(0)));
    }

    @Override
    public BigDecimal getBidPrice() {
        return BigDecimal.valueOf(Float.valueOf(this.getResult().getXLTCZEUR().getBid().get(0)));
    }

    @Override
    public BigDecimal getLtcAfterWithdrawalProv(BigDecimal ltcToSubtractWithdrawProv) {
        return ltcToSubtractWithdrawProv.subtract(KRAKEN_LTC_WITHDRAW_PROV);
    }

    @Override
    public Object getLtcEurStockData() {
        return this.getResult().getXLTCZEUR();
    }

}
