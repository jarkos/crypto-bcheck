package pl.jarkos.backend.stock.dto.bitstamp;

import pl.jarkos.backend.stock.abstractional.api.BccStockDataInterface;
import pl.jarkos.backend.stock.dto.bitstamp.general.BitstampStockData;
import pl.jarkos.backend.stock.exception.NotSupportedOperationException;

import java.math.BigDecimal;

public class BitstampBccStockData extends BitstampStockData implements BccStockDataInterface {

    public BitstampBccStockData(BitstampStockData b) {
        super(b.getHigh(), b.getLast(), b.getTimestamp(), b.getBid(), b.getVwap(), b.getVolume(), b.getLow(), b.getAsk(), b.getOpen());
    }

    @Override
    public BigDecimal getBccAfterWithdrawalProv(BigDecimal bccToSubtractWithdrawProv) {
        try {
            throw new NotSupportedOperationException("Exception for fetching bcc withdrawal fee from " + this.getClass());
        } catch (NotSupportedOperationException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Object getBccStockData() {
        return this;
    }
}
