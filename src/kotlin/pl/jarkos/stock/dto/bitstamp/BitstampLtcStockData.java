package pl.jarkos.stock.dto.bitstamp;

import pl.jarkos.stock.abstractional.api.LtcStockDataInterface;
import pl.jarkos.stock.dto.bitstamp.general.BitstampStockData;

import java.math.BigDecimal;

import static pl.jarkos.config.StockConfig.BITSTAMP_EUR_WITHDRAW_PROV_AMOUNT;

public class BitstampLtcStockData extends BitstampStockData implements LtcStockDataInterface {

    public BitstampLtcStockData(BitstampStockData b) {
        super(b.getHigh(), b.getLast(), b.getTimestamp(), b.getBid(), b.getVwap(), b.getVolume(), b.getLow(), b.getAsk(), b.getOpen());
    }

    @Override
    public BigDecimal getLtcAfterWithdrawalProv(BigDecimal ltcToSubtractWithdrawProv) {
        return ltcToSubtractWithdrawProv.subtract(BITSTAMP_EUR_WITHDRAW_PROV_AMOUNT);
    }

    @Override
    public Object getLtcEurStockData() {
        return this;
    }

}
