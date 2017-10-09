package com.jarkos.stock.dto.huobi.general;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Trade {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("price")
    @Expose
    private Float price;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("amount")
    @Expose
    private Float amount;
    @SerializedName("direction")
    @Expose
    private String direction;
    @SerializedName("tradeId")
    @Expose
    private Long tradeId;
    @SerializedName("ts")
    @Expose
    private Long ts;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("en_type")
    @Expose
    private String enType;

}
