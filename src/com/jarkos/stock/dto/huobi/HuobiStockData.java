package com.jarkos.stock.dto.huobi;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jarkos.stock.dto.AbstractStockData;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HuobiStockData extends AbstractStockData {

    @SerializedName("amount")
    @Expose
    private Float amount;
    @SerializedName("p_last")
    @Expose
    private Float pLast;
    @SerializedName("p_low")
    @Expose
    private Float pLow;
    @SerializedName("symbol")
    @Expose
    private String symbol;
    @SerializedName("top_buy")
    @Expose
    private List<TopBuy> topBuy = null;
    @SerializedName("sells")
    @Expose
    private List<Sell> sells = null;
    @SerializedName("amp")
    @Expose
    private Integer amp;
    @SerializedName("p_open")
    @Expose
    private Float pOpen;
    @SerializedName("level")
    @Expose
    private Integer level;
    @SerializedName("total")
    @Expose
    private Float total;
    @SerializedName("buys")
    @Expose
    private List<Buy> buys = null;
    @SerializedName("top_sell")
    @Expose
    private List<TopSell> topSell = null;
    @SerializedName("p_high")
    @Expose
    private Float pHigh;
    @SerializedName("p_new")
    @Expose
    private Float pNew;
    @SerializedName("trades")
    @Expose
    private List<Trade> trades = null;

    @Override
    public float getLastBtcPrice() {
        return getPLast();
    }

    @Override
    public float getLastLtcPrice() {
        return getPLast();
    }

    @Override
    public Object getLtcEurStockData() {
        return this;
    }
}
