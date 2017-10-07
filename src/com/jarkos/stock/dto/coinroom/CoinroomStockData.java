package com.jarkos.stock.dto.coinroom;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CoinroomStockData {

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

}
