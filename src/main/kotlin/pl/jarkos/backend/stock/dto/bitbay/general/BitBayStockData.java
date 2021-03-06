package pl.jarkos.backend.stock.dto.bitbay.general;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.jarkos.backend.coinmarket.enums.StockNameEnum;
import pl.jarkos.backend.stock.abstractional.api.GeneralStockDataInterface;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import static pl.jarkos.backend.config.StockConfig.BITBAY_TRADE_PROVISION_PERCENTAGE_MAKER;
import static pl.jarkos.backend.config.StockConfig.BITBAY_TRADE_PROVISION_PERCENTAGE_TAKER;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BitBayStockData implements Serializable, GeneralStockDataInterface {

    @SerializedName("max")
    @Expose
    private Float max;
    @SerializedName("min")
    @Expose
    private Float min;
    @SerializedName("last")
    @Expose
    private Float last;
    @SerializedName("bid")
    @Expose
    private Float bid;
    @SerializedName("ask")
    @Expose
    private Float ask;
    @SerializedName("vwap")
    @Expose
    private Float vwap;
    @SerializedName("average")
    @Expose
    private Float average;
    @SerializedName("volume")
    @Expose
    private Float volume;
    @SerializedName("bids")
    @Expose
    private List<List<Float>> bids = null;
    @SerializedName("asks")
    @Expose
    private List<List<Float>> asks = null;
    @SerializedName("transactions")
    @Expose
    private List<Transaction> transactions = null;
    private final static long serialVersionUID = 4485884411538419890L;

    public Float getLast() {
        return last > ask ? ask : last;
    }

    @Override
    public BigDecimal getLastPrice() {
        return BigDecimal.valueOf(this.getLast());
    }

    @Override
    public BigDecimal getAskPrice() {
        return BigDecimal.valueOf(this.getAsk());
    }

    @Override
    public BigDecimal getBidPrice() {
        return BigDecimal.valueOf(this.getBid());
    }

    public BigDecimal getMakerProvision() {
        return BITBAY_TRADE_PROVISION_PERCENTAGE_MAKER;
    }

    public BigDecimal getTakerProvision() {
        return BITBAY_TRADE_PROVISION_PERCENTAGE_TAKER;
    }

    @Override
    public String getStockName() {
        return StockNameEnum.BitBay.name();
    }

}
