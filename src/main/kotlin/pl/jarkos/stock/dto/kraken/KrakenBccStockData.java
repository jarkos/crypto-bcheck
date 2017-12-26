package pl.jarkos.stock.dto.kraken;

import pl.jarkos.stock.abstractional.api.BccStockDataInterface;
import pl.jarkos.stock.dto.kraken.general.KrakenStockData;

import java.math.BigDecimal;

import static pl.jarkos.config.StockConfig.KRAKEN_BCC_WITHDRAW_PROV;

public class KrakenBccStockData extends KrakenStockData implements BccStockDataInterface {
    public KrakenBccStockData(KrakenStockData k) {
        super(k.getError(), k.getResult());
    }

    @Override
    public BigDecimal getLastPrice() {
        return BigDecimal.valueOf(Float.valueOf(this.getResult().getXBCHEUR().getLastTradePrice().get(0)));
    }

    @Override
    public BigDecimal getAskPrice() {
        return BigDecimal.valueOf(Float.valueOf(this.getResult().getXBCHEUR().getAsk().get(0)));
    }

    @Override
    public BigDecimal getBidPrice() {
        return BigDecimal.valueOf(Float.valueOf(this.getResult().getXBCHEUR().getBid().get(0)));
    }

    @Override
    public BigDecimal getBccAfterWithdrawalProv(BigDecimal bccToSubtractWithdrawProv) {
        return bccToSubtractWithdrawProv.subtract(KRAKEN_BCC_WITHDRAW_PROV);
    }


    @Override
    public Object getBccStockData() {
        return this.getResult().getXBCHEUR();
    }
}
