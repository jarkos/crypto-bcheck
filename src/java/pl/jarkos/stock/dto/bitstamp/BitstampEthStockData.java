package pl.jarkos.stock.dto.bitstamp;

import pl.jarkos.stock.abstractional.api.EthStockDataInterface;
import pl.jarkos.stock.dto.bitstamp.general.BitstampStockData;

import java.math.BigDecimal;

import static pl.jarkos.config.StockConfig.BITSTAMP_WITHDRAW_PROV;

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
