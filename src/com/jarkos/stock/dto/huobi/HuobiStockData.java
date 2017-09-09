
package com.jarkos.stock.dto.huobi;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HuobiStockData {

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

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public Float getPLast() {
        return pLast;
    }

    public void setPLast(Float pLast) {
        this.pLast = pLast;
    }

    public Float getPLow() {
        return pLow;
    }

    public void setPLow(Float pLow) {
        this.pLow = pLow;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public List<TopBuy> getTopBuy() {
        return topBuy;
    }

    public void setTopBuy(List<TopBuy> topBuy) {
        this.topBuy = topBuy;
    }

    public List<Sell> getSells() {
        return sells;
    }

    public void setSells(List<Sell> sells) {
        this.sells = sells;
    }

    public Integer getAmp() {
        return amp;
    }

    public void setAmp(Integer amp) {
        this.amp = amp;
    }

    public Float getPOpen() {
        return pOpen;
    }

    public void setPOpen(Float pOpen) {
        this.pOpen = pOpen;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Float getTotal() {
        return total;
    }

    public void setTotal(Float total) {
        this.total = total;
    }

    public List<Buy> getBuys() {
        return buys;
    }

    public void setBuys(List<Buy> buys) {
        this.buys = buys;
    }

    public List<TopSell> getTopSell() {
        return topSell;
    }

    public void setTopSell(List<TopSell> topSell) {
        this.topSell = topSell;
    }

    public Float getPHigh() {
        return pHigh;
    }

    public void setPHigh(Float pHigh) {
        this.pHigh = pHigh;
    }

    public Float getPNew() {
        return pNew;
    }

    public void setPNew(Float pNew) {
        this.pNew = pNew;
    }

    public List<Trade> getTrades() {
        return trades;
    }

    public void setTrades(List<Trade> trades) {
        this.trades = trades;
    }

}
