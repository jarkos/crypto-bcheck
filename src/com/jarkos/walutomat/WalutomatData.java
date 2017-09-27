package com.jarkos.walutomat;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class WalutomatData {

    @SerializedName("buy")
    @Expose
    private String buy;
    @SerializedName("sell")
    @Expose
    private String sell;
    @SerializedName("buy_old")
    @Expose
    private String buyOld;
    @SerializedName("sell_old")
    @Expose
    private String sellOld;
    @SerializedName("count_sell")
    @Expose
    private Integer countSell;
    @SerializedName("count_buy")
    @Expose
    private Integer countBuy;
    @SerializedName("avg")
    @Expose
    private String avg;
    @SerializedName("avg_old")
    @Expose
    private String avgOld;

    public BigDecimal getBuyExchangeRate() {
        return BigDecimal.valueOf(Float.valueOf(getBuy()));
    }

    public BigDecimal getSellExchangeRate() {
        return BigDecimal.valueOf(Float.valueOf(getSell()));
    }
}
