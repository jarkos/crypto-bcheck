package com.jarkos.stock.dto.bitstamp.general;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BitstampStockData {

    @SerializedName("high")
    @Expose
    private String high;
    @SerializedName("last")
    @Expose
    private String lastPrice;
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
        return Float.valueOf(lastPrice) > Float.valueOf(ask) ? ask : lastPrice;
    }

}
