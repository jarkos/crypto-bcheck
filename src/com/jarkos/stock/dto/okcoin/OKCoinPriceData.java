
package com.jarkos.stock.dto.okcoin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OKCoinPriceData {

    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("ticker")
    @Expose
    private Ticker ticker;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Ticker getTicker() {
        return ticker;
    }

    public void setTicker(Ticker ticker) {
        this.ticker = ticker;
    }

}
