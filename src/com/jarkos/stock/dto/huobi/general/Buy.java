package com.jarkos.stock.dto.huobi.general;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Buy {

    @SerializedName("price")
    @Expose
    private Float price;
    @SerializedName("amount")
    @Expose
    private Float amount;
    @SerializedName("level")
    @Expose
    private Integer level;

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

}
