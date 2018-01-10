package pl.jarkos.backend.stock.dto.bitstamp;

import pl.jarkos.backend.stock.abstractional.api.EthStockDataInterface;
import pl.jarkos.backend.stock.dto.bitstamp.general.BitstampStockData;

import java.math.BigDecimal;

import static pl.jarkos.backend.config.StockConfig.BITSTAMP_WITHDRAW_PROV;

public class BitstampEthStockData extends BitstampStockData implements EthStockDataInterface {

    public BitstampEthStockData(BitstampStockData b) {
        super(b.getHigh(), b.getLast(), b.getTimestamp(), b.getBid(), b.getVwap(), b.getVolume(), b.getLow(), b.getAsk(), b.getOpen());
    }

    @Override
    public BigDecimal getEthAfterWithdrawalProv(BigDecimal numberOfEthBoughtAfterTradeProv) {
        return numberOfEthBoughtAfterTradeProv.subtract(BITSTAMP_WITHDRAW_PROV);
    }


    @Override
    public Object getEthEurStockData() {
        return this;
    }
}
