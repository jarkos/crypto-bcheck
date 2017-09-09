package com.jarkos.stock.dto.kraken;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jarkos.stock.dto.AbstractStockData;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KrakenStockData extends AbstractStockData {

    @SerializedName("error")
    @Expose
    private List<Object> error = null;
    @SerializedName("result")
    @Expose
    private Result result;

    @Override
    public float getLastBtcPrice() {
        return Float.valueOf(this.getResult().getXXBTZEUR().getLastTradePrice().get(0));
    }

    @Override
    public float getLastLtcPrice() {
        return Float.valueOf(this.getResult().getXLTCZEUR().getLastTradePrice().get(0));
    }

    @Override
    public Object getLtcEurStockData() {
        return this.getResult().getXLTCZEUR();
    }
}
