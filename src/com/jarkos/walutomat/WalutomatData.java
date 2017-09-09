
package com.jarkos.walutomat;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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

    public String getBuy() {
        return buy;
    }

    public void setBuy(String buy) {
        this.buy = buy;
    }

    public String getSell() {
        return sell;
    }

    public void setSell(String sell) {
        this.sell = sell;
    }

    public String getBuyOld() {
        return buyOld;
    }

    public void setBuyOld(String buyOld) {
        this.buyOld = buyOld;
    }

    public String getSellOld() {
        return sellOld;
    }

    public void setSellOld(String sellOld) {
        this.sellOld = sellOld;
    }

    public Integer getCountSell() {
        return countSell;
    }

    public void setCountSell(Integer countSell) {
        this.countSell = countSell;
    }

    public Integer getCountBuy() {
        return countBuy;
    }

    public void setCountBuy(Integer countBuy) {
        this.countBuy = countBuy;
    }

    public String getAvg() {
        return avg;
    }

    public void setAvg(String avg) {
        this.avg = avg;
    }

    public String getAvgOld() {
        return avgOld;
    }

    public void setAvgOld(String avgOld) {
        this.avgOld = avgOld;
    }

}
