package pl.jarkos.backend.stock.dto.coinroom;

import pl.jarkos.backend.stock.abstractional.api.BccStockDataInterface;

import java.math.BigDecimal;

import static pl.jarkos.backend.config.StockConfig.COINROOM_BCC_WITHDRAW_PROV_AMOUNT;

public class CoinroomBccStockData extends CoinroomStockData implements BccStockDataInterface {

    public CoinroomBccStockData(CoinroomStockData c) {
        super(c.getLow(), c.getHigh(), c.getVwap(), c.getVolume(), c.getLast(), c.getAsk(), c.getBid());
    }

    @Override
    public BigDecimal getBccAfterWithdrawalProv(BigDecimal bccToSubtractWithdrawProv) {
        return bccToSubtractWithdrawProv.subtract(COINROOM_BCC_WITHDRAW_PROV_AMOUNT);
    }


    @Override
    public Object getBccStockData() {
        return this;
    }
}
