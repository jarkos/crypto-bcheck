package com.jarkos.stock.dto.coinroom;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jarkos.stock.abstractional.api.GeneralStockDataInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

import static com.jarkos.config.StockConfig.COINROOM_TRADE_PROVISION_PERCENTAGE_MAKER;
import static com.jarkos.config.StockConfig.COINROOM_TRADE_PROVISION_PERCENTAGE_TAKER;

@Getter
@Setter
@AllArgsConstructor
public class CoinroomStockData implements GeneralStockDataInterface {

    @SerializedName("low")
    @Expose
    private Float low;
    @SerializedName("high")
    @Expose
    private Float high;
    @SerializedName("vwap")
    @Expose
    private Float vwap;
    @SerializedName("volume")
    @Expose
    private Float volume;
    @SerializedName("last")
    @Expose
    private Float last;
    @SerializedName("ask")
    @Expose
    private Float ask;
    @SerializedName("bid")
    @Expose
    private Float bid;

    public Float getLast() {
        return last > ask ? ask : last;
    }

    @Override
    public BigDecimal getLastPrice() {
        return BigDecimal.valueOf(getLast());
    }

    @Override
    public BigDecimal getAskPrice() {
        return BigDecimal.valueOf(getAsk());
    }

    @Override
    public BigDecimal getBidPrice() {
        return BigDecimal.valueOf(getBid());
    }

    @Override
    public BigDecimal getMakerProvision() {
        return COINROOM_TRADE_PROVISION_PERCENTAGE_MAKER;
    }

    @Override
    public BigDecimal getTakerProvision() {
        return COINROOM_TRADE_PROVISION_PERCENTAGE_TAKER;
    }
}
