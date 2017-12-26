package pl.jarkos.backend.stock.dto.kraken.general;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.jarkos.backend.coinmarket.enums.StockNameEnum;
import pl.jarkos.backend.stock.abstractional.api.GeneralStockDataInterface;

import java.math.BigDecimal;
import java.util.List;

import static pl.jarkos.backend.config.StockConfig.KRAKEN_MAKER_TRADE_PROV_PERCENTAGE;
import static pl.jarkos.backend.config.StockConfig.KRAKEN_TAKER_TRADE_PROV_PERCENTAGE;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class KrakenStockData implements GeneralStockDataInterface {

    @SerializedName("error")
    @Expose
    private List<Object> error = null;
    @SerializedName("result")
    @Expose
    private Result result;

    @Override
    public BigDecimal getLastPrice() {
        return null;
    }

    @Override
    public BigDecimal getAskPrice() {
        return null;
    }

    @Override
    public BigDecimal getBidPrice() {
        return null;
    }

    public BigDecimal getMakerProvision() {
        return KRAKEN_MAKER_TRADE_PROV_PERCENTAGE;
    }

    public BigDecimal getTakerProvision() {
        return KRAKEN_TAKER_TRADE_PROV_PERCENTAGE;
    }

    @Override
    public String getStockName() {
        return StockNameEnum.Kraken.name();
    }

}
