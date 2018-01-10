package pl.jarkos.backend.stock.dto.bitstamp.general;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.jarkos.backend.coinmarket.enums.StockNameEnum;
import pl.jarkos.backend.stock.abstractional.api.GeneralStockDataInterface;

import java.math.BigDecimal;

import static pl.jarkos.backend.config.StockConfig.BITSTAMP_TRADE_PROVISION_PERCENTAGE;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BitstampStockData implements GeneralStockDataInterface {

    @SerializedName("high")
    @Expose
    private String high;
    @SerializedName("last")
    @Expose
    private String last;
    @SerializedName("timestamp")
    @Expose
    private String timestamp;
    @SerializedName("bid")
    @Expose
    private String bid;
    @SerializedName("vwap")
    @Expose
    private String vwap;
    @SerializedName("volume")
    @Expose
    private String volume;
    @SerializedName("low")
    @Expose
    private String low;
    @SerializedName("ask")
    @Expose
    private String ask;
    @SerializedName("open")
    @Expose
    private String open;

    public String getLast() {
        return Float.valueOf(last) > Float.valueOf(ask) ? ask : last;
    }

    @Override
    public BigDecimal getLastPrice() {
        return BigDecimal.valueOf(Float.valueOf(getLast()));
    }

    @Override
    public BigDecimal getAskPrice() {
        return BigDecimal.valueOf(Float.valueOf(getAsk()));
    }

    @Override
    public BigDecimal getBidPrice() {
        return BigDecimal.valueOf(Float.valueOf(getBid()));
    }

    @Override
    public BigDecimal getMakerProvision() {
        return BITSTAMP_TRADE_PROVISION_PERCENTAGE;
    }

    @Override
    public BigDecimal getTakerProvision() {
        return BITSTAMP_TRADE_PROVISION_PERCENTAGE;
    }

    @Override
    public String getStockName() {
        return StockNameEnum.Bitstamp.name();
    }

}
