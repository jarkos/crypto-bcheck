package pl.jarkos.backend.stock.dto.bitstamp;

import pl.jarkos.backend.stock.abstractional.api.DashStockDataInterface;
import pl.jarkos.backend.stock.dto.bitstamp.general.BitstampStockData;

import java.math.BigDecimal;

import static pl.jarkos.backend.config.StockConfig.BITSTAMP_WITHDRAW_PROV;

public class BitstampDashStockData extends BitstampStockData implements DashStockDataInterface {

    @Override
    public BigDecimal getDashAfterWithdrawalProv(BigDecimal numberOfDashBoughtAfterTradeProv) {
        return numberOfDashBoughtAfterTradeProv.subtract(BITSTAMP_WITHDRAW_PROV);
    }

    @Override
    public Object getDashEurStockData() {
        return this;
    }
}
