package com.jarkos.stock.dto.kraken.general;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jarkos.stock.abstractional.api.GeneralStockDataInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class KrakenStockData implements GeneralStockDataInterface {

    @SerializedName("error")
    @Expose
    private List<Object> error = null;
    @SerializedName("result")
    @Expose
    private Result result;

    @Override
    public BigDecimal getLastPrice() {
        return null;
    }

    @Override
    public BigDecimal getAskPrice() {
        return null;
    }

    @Override
    public BigDecimal getBidPrice() {
        return null;
    }

    public BigDecimal getMakerTradeProvision() {
        return null;
    }

    public BigDecimal getTakerTradeProvision() {
        return null;
    }

}
