
package com.jarkos.stock.dto.bitstamp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jarkos.stock.dto.AbstractStockData;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BitstampStockData extends AbstractStockData {

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

    @Override
    public float getLastBtcPrice() {
        return Float.valueOf(getLast());
    }

    @Override
    public float getLastLtcPrice() {
        return Float.valueOf(getLast());
    }

    @Override
    public Object getLtcEurStockData() {
        return this;
    }
}
